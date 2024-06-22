/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013, 2024 James Talbut and others
 *  jim-emitters@spudsoft.co.uk
 *
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PageMargin;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.AreaStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.w3c.dom.css.CSSValue;

import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

/**
 * StyleManagerHUtils is an extension of the StyleManagerUtils to provide
 * HSSFWorkbook specific functionality.
 *
 * @author Jim Talbut
 *
 */
public class StyleManagerHUtils extends StyleManagerUtils {
	private short paletteIndex = 64;
	private static short minPaletteIndex = 40;

	private static Factory factory = new StyleManagerUtils.Factory() {
		@Override
		public StyleManagerUtils create(Logger log) {
			return new StyleManagerHUtils(log);
		}
	};

	/**
	 * Get factory object
	 *
	 * @return Return a factory object
	 */
	public static Factory getFactory() {
		return factory;
	}

	/**
	 * @param log Logger used by StyleManagerHUtils to record anything of interest.
	 */
	public StyleManagerHUtils(Logger log) {
		super(log);
	}

	@Override
	public RichTextString createRichTextString(String value) {
		return new HSSFRichTextString(value);
	}

	/**
	 * Converts a BIRT border style into a POI border style (short constant defined
	 * in CellStyle).
	 *
	 * @param birtBorder The BIRT border style.
	 * @param width      The width of the border as understood by BIRT.
	 * @return One of the CellStyle BORDER constants.
	 */
	private BorderStyle poiBorderStyleFromBirt(String birtBorder, String width) {
		if ("none".equals(birtBorder)) {
			return BorderStyle.NONE; // CellStyle.BORDER_NONE;
		}
		DimensionType dim = DimensionType.parserUnit(width);
		double pxWidth = 3.0;
		if ((dim != null) && ("px".equals(dim.getUnits()))) {
			pxWidth = dim.getMeasure();
		}
		if ("solid".equals(birtBorder)) {
			if (pxWidth < 2.9) {
				return BorderStyle.THIN; // CellStyle.BORDER_THIN;
			} else if (pxWidth < 3.1) {
				return BorderStyle.MEDIUM; // CellStyle.BORDER_MEDIUM;
			} else {
				return BorderStyle.THICK; // CellStyle.BORDER_THICK;
			}
		} else if ("dashed".equals(birtBorder)) {
			if (pxWidth < 2.9) {
				return BorderStyle.DASHED; // CellStyle.BORDER_DASHED;
			}
			return BorderStyle.MEDIUM_DASHED; // CellStyle.BORDER_MEDIUM_DASHED;
		} else if ("dotted".equals(birtBorder)) {
			return BorderStyle.DOTTED; // CellStyle.BORDER_DOTTED;
		} else if ("double".equals(birtBorder)) {
			return BorderStyle.DOUBLE; // CellStyle.BORDER_DOUBLE;
		} else if ("none".equals(birtBorder)) {
			return BorderStyle.NONE; // CellStyle.BORDER_NONE;
		}

		log.debug("Border style \"", birtBorder, "\" is not recognised");
		return BorderStyle.NONE; // CellStyle.BORDER_NONE;
	}

	/**
	 * Get an HSSFPalette index for a workbook that closely approximates the passed
	 * in colour.
	 *
	 * @param workbook The workbook for which the colour is being sought.
	 * @param colour   The colour, in the form "rgb(<i>r</i>, <i>g</i>, <i>b</i>)".
	 * @return The index into the HSSFPallete for the workbook for a colour that
	 *         approximates the passed in colour.
	 */
	private short getHColour(HSSFWorkbook workbook, String colour) {
		int[] rgbInt = ColorUtil.getRGBs(colour);
		if (rgbInt == null) {
			return 0;
		}

		byte[] rgbByte = { (byte) rgbInt[0], (byte) rgbInt[1], (byte) rgbInt[2] };
		HSSFPalette palette = workbook.getCustomPalette();

		HSSFColor result = palette.findColor(rgbByte[0], rgbByte[1], rgbByte[2]);
		if (result == null) {
			if (paletteIndex > minPaletteIndex) {
				--paletteIndex;
				palette.setColorAtIndex(paletteIndex, rgbByte[0], rgbByte[1], rgbByte[2]);
				return paletteIndex;
			}
			result = palette.findSimilarColor(rgbByte[0], rgbByte[1], rgbByte[2]);
		}
		return result.getIndex();
	}

	@Override
	public void applyBorderStyle(Workbook workbook, CellStyle style, BirtStyle birtStyle) {
		// TODO: implements the border apply at once to the cell object based on
		// birtStyle
	}

	@Override
	public void applyBorderStyle(Workbook workbook, CellStyle style, BorderSide side, CSSValue colour,
			CSSValue borderStyle, CSSValue width) {
		if ((colour != null) || (borderStyle != null) || (width != null)) {
			String colourString = colour == null ? "rgb(0,0,0)" : colour.getCssText();
			String borderStyleString = borderStyle == null ? "solid" : borderStyle.getCssText();
			String widthString = width == null ? "medium" : width.getCssText();

			if (style instanceof HSSFCellStyle) {
				HSSFCellStyle hStyle = (HSSFCellStyle) style;

				BorderStyle hBorderStyle = poiBorderStyleFromBirt(borderStyleString, widthString);
				short colourIndex = getHColour((HSSFWorkbook) workbook, colourString);
				if (colourIndex > 0) {
					if (!BorderStyle.NONE /* CellStyle.BORDER_NONE */.equals(hBorderStyle)) {
						switch (side) {
						case TOP:
							hStyle.setBorderTop(hBorderStyle);
							hStyle.setTopBorderColor(colourIndex);
							// log.debug( "Top border: " + xStyle.getBorderTop() + " / " +
							// xStyle.getTopBorderXSSFColor().getARGBHex() );
							break;
						case LEFT:
							hStyle.setBorderLeft(hBorderStyle);
							hStyle.setLeftBorderColor(colourIndex);
							// log.debug( "Left border: " + xStyle.getBorderLeft() + " / " +
							// xStyle.getLeftBorderXSSFColor().getARGBHex() );
							break;
						case RIGHT:
							hStyle.setBorderRight(hBorderStyle);
							hStyle.setRightBorderColor(colourIndex);
							// log.debug( "Right border: " + xStyle.getBorderRight() + " / " +
							// xStyle.getRightBorderXSSFColor().getARGBHex() );
							break;
						case BOTTOM:
							hStyle.setBorderBottom(hBorderStyle);
							hStyle.setBottomBorderColor(colourIndex);
							// log.debug( "Bottom border: " + xStyle.getBorderBottom() + " / " +
							// xStyle.getBottomBorderXSSFColor().getARGBHex() );
							break;
						case DIAGONAL:
							throw new UnsupportedOperationException("Border Style " + side + " is unsupported");
						case HORIZONTAL:
							throw new UnsupportedOperationException("Border Style " + side + " is unsupported");
						case VERTICAL:
							throw new UnsupportedOperationException("Border Style " + side + " is unsupported");
						}
					}
				}
			}
		}
	}

	@Override
	public void addColourToFont(Workbook workbook, Font font, String colour) {
		// if (IStyle.TRANSPARENT_VALUE.equals(colour)) {
		if ((colour == null) || CSSValueConstants.TRANSPARENT_VALUE.getCssText().equals(colour)) {
			return;
		}
		if (font instanceof HSSFFont) {
			HSSFFont hFont = (HSSFFont) font;
			short colourIndex = getHColour((HSSFWorkbook) workbook, colour);
			if (colourIndex > 0) {
				hFont.setColor(colourIndex);
			}
		}
	}

	@Override
	public void addBackgroundColourToStyle(Workbook workbook, CellStyle style, String colour) {
		if ((colour == null) || CSSValueConstants.TRANSPARENT_VALUE.equals(colour)) {
			return;
		}
		if (style instanceof HSSFCellStyle) {
			HSSFCellStyle cellStyle = (HSSFCellStyle) style;
			short colourIndex = getHColour((HSSFWorkbook) workbook, colour);
			if (colourIndex > 0) {
				cellStyle.setFillForegroundColor(colourIndex);
				// cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			}
		}
	}

	@Override
	public Font correctFontColorIfBackground(FontManager fm, Workbook wb, BirtStyle birtStyle, Font font) {
		HSSFPalette palette = ((HSSFWorkbook) wb).getCustomPalette();

		CSSValue bgColour = birtStyle.getProperty(StyleConstants.STYLE_BACKGROUND_COLOR);
		int bgRgb[] = parseColour(bgColour == null ? null : bgColour.getCssText(), "white");

		// short fgRgb[] = HSSFColor.BLACK.triplet;
		short fgRgb[] = HSSFColor.HSSFColorPredefined.BLACK.getTriplet();
		if ((font != null) && (font.getColor() != Short.MAX_VALUE)) {
			fgRgb = palette.getColor(font.getColor()).getTriplet();
		}
		if ((fgRgb[0] == 255) && (fgRgb[1] == 255) && (fgRgb[2] == 255)) {
			fgRgb[0] = fgRgb[1] = fgRgb[2] = 0;
		} else if ((fgRgb[0] == 0) && (fgRgb[1] == 0) && (fgRgb[2] == 0)) {
			fgRgb[0] = fgRgb[1] = fgRgb[2] = 255;
		}

		if ((bgRgb[0] == fgRgb[0]) && (bgRgb[1] == fgRgb[1]) && (bgRgb[2] == fgRgb[2])) {

			IStyle addedStyle = new AreaStyle(fm.getCssEngine());
			addedStyle.setColor(contrastColour(bgRgb));

			return fm.getFontWithExtraStyle(font, addedStyle);
		}
		return font;
	}

	@Override
	public int anchorDxFromMM(double widthMM, double colWidthMM) {
		return (int) (1023.0 * widthMM / colWidthMM);
	}

	@Override
	public int anchorDyFromPoints(float height, float rowHeight) {
		return (int) (255.0 * height / rowHeight);
	}

	@Override
	public void prepareMarginDimensions(Sheet sheet, IPageContent page) {
		double headerHeight = 0.5;
		double footerHeight = 0.5;
		if ((page.getHeaderHeight() != null) && isAbsolute(page.getHeaderHeight())) {
			headerHeight = page.getHeaderHeight().convertTo(DimensionType.UNITS_IN);
			sheet.getPrintSetup().setHeaderMargin(headerHeight);
		}
		if ((page.getFooterHeight() != null) && isAbsolute(page.getFooterHeight())) {
			footerHeight = page.getFooterHeight().convertTo(DimensionType.UNITS_IN);
			sheet.getPrintSetup().setFooterMargin(footerHeight);
		}
		if ((page.getMarginBottom() != null) && isAbsolute(page.getMarginBottom())) {
			sheet.setMargin(PageMargin.getByShortValue(Sheet.BottomMargin),
					footerHeight + page.getMarginBottom().convertTo(DimensionType.UNITS_IN));
		}
		if ((page.getMarginLeft() != null) && isAbsolute(page.getMarginLeft())) {
			sheet.setMargin(PageMargin.getByShortValue(Sheet.LeftMargin),
					page.getMarginLeft().convertTo(DimensionType.UNITS_IN));
		}
		if ((page.getMarginRight() != null) && isAbsolute(page.getMarginRight())) {
			sheet.setMargin(PageMargin.getByShortValue(Sheet.RightMargin),
					page.getMarginRight().convertTo(DimensionType.UNITS_IN));
		}
		if ((page.getMarginTop() != null) && isAbsolute(page.getMarginTop())) {
			sheet.setMargin(PageMargin.getByShortValue(Sheet.TopMargin),
					headerHeight + page.getMarginTop().convertTo(DimensionType.UNITS_IN));
		}
	}

}
