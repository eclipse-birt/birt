/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.odf.writer;

import java.util.Collection;

import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.odf.OdfUtil;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.w3c.dom.css.CSSValue;

@SuppressWarnings("nls")

/**
 * Style writer for style tag and attributes.
 */
public class StyleEntryWriter {
	protected final String RIGHT = "right";
	protected final String LEFT = "left";
	protected final String TOP = "top";
	protected final String BOTTOM = "bottom";

	protected XMLWriter writer;

	private int reportDpi;

	private static final int[] PADDING_PROPS = { StyleConstant.PADDING_BOTTOM, StyleConstant.PADDING_TOP,
			StyleConstant.PADDING_LEFT, StyleConstant.PADDING_RIGHT };

	private static final int[] BORDER_STYLE_PROPS = { StyleConstant.BORDER_BOTTOM_STYLE_PROP,
			StyleConstant.BORDER_TOP_STYLE_PROP, StyleConstant.BORDER_LEFT_STYLE_PROP,
			StyleConstant.BORDER_RIGHT_STYLE_PROP };

	private static final int[] BORDER_COLOR_PROPS = { StyleConstant.BORDER_BOTTOM_COLOR_PROP,
			StyleConstant.BORDER_TOP_COLOR_PROP, StyleConstant.BORDER_LEFT_COLOR_PROP,
			StyleConstant.BORDER_RIGHT_COLOR_PROP };

	private static final int[] BORDER_WIDTH_PROPS = { StyleConstant.BORDER_BOTTOM_WIDTH_PROP,
			StyleConstant.BORDER_TOP_WIDTH_PROP, StyleConstant.BORDER_LEFT_WIDTH_PROP,
			StyleConstant.BORDER_RIGHT_WIDTH_PROP };

	private static final int[] MARGIN_PROPS = { StyleConstant.MARGIN_BOTTOM, StyleConstant.MARGIN_TOP,
			StyleConstant.MARGIN_LEFT, StyleConstant.MARGIN_RIGHT };

	private static final String[] SIDES = { "bottom", "top", "left", "right" };

	public StyleEntryWriter(XMLWriter writer, int reportDpi) {
		this.writer = writer;
		this.reportDpi = reportDpi;
	}

	public void writeDefaultStyle(StyleEntry aStyle) {
		writer.openTag("style:default-style"); //$NON-NLS-1$
		writeStyleEntry(aStyle);
		writer.closeTag("style:default-style"); //$NON-NLS-1$
	}

	public void writeStyle(StyleEntry aStyle) {
		if (aStyle.getType() != StyleConstant.TYPE_PAGE_LAYOUT) {
			writer.openTag("style:style"); //$NON-NLS-1$
			writeStyleEntry(aStyle);
			writer.closeTag("style:style"); //$NON-NLS-1$
		} else {
			writer.openTag("style:page-layout"); //$NON-NLS-1$
			writer.attribute("style:name", aStyle.getName()); //$NON-NLS-1$

			writePageLayout(aStyle);
			writer.closeTag("style:page-layout"); //$NON-NLS-1$
		}
	}

	private void writeStyleEntry(StyleEntry aStyle) {
		writer.attribute("style:name", aStyle.getName()); //$NON-NLS-1$
		switch (aStyle.getType()) {
		case StyleEntry.TYPE_PARAGRAPH:
			writeParagraphStyle(aStyle, false);
			writeTextProperties(aStyle);
			break;
		case StyleEntry.TYPE_TEXT:
			writeTextStyle(aStyle);
			break;
		case StyleEntry.TYPE_TABLE:
			writeTableStyle(aStyle);
			break;
		case StyleEntry.TYPE_TABLE_COLUMN:
			writeTableColumnStyle(aStyle);
			break;
		case StyleEntry.TYPE_TABLE_ROW:
			writeTableRowStyle(aStyle);
			break;
		case StyleEntry.TYPE_TABLE_CELL:
			writeTableCellStyle(aStyle);
			// if text properties exist
			if (aStyle.getProperty(StyleConstant.H_ALIGN_PROP) != null) {
				writeParagraphProperties(aStyle, true);
			}

			if (aStyle.getProperty(StyleConstant.FONT_FAMILY_PROP) != null) {
				// also write them out
				writeTextProperties(aStyle);
			}
			break;
		case StyleEntry.TYPE_DRAW:
			writeGraphicStyle(aStyle);
			break;
		}
	}

	private void writeTableStyle(StyleEntry aStyle) {
		writer.attribute("style:family", "table"); //$NON-NLS-1$ //$NON-NLS-2$
		String masterPage = aStyle.getStringProperty(StyleConstant.MASTER_PAGE);
		if (masterPage != null) {
			writer.attribute("style:master-page-name", masterPage);
		}

		writer.openTag("style:table-properties");

		writer.attribute("table:border-model", "collapsing");
		// align table to left by default, which allows having the table width
		// smaller than the page width

		String direction = aStyle.getStringProperty(StyleConstant.DIRECTION_PROP);
		String tableAlign = "rtl".equals(direction) ? "right" : "left";
		writer.attribute("table:align", tableAlign);

		Double tableWidth = aStyle.getDoubleProperty(StyleConstant.WIDTH);
		if (tableWidth != null) {
			writeTableWidth(tableWidth);
		}

		writeBorders(aStyle);
		writeBackgroundColor(aStyle.getStringProperty(StyleConstant.BACKGROUND_COLOR_PROP));

		writeAlign(aStyle);
		writePageBreaks(aStyle);

		writer.closeTag("style:table-properties");
	}

	private void writePageBreaks(StyleEntry aStyle) {
		String breakBefore = aStyle.getStringProperty(StyleConstant.PAGE_BREAK_BEFORE);
		if (breakBefore != null && !"auto".equals(breakBefore)) {
			writer.attribute("fo:break-before", breakBefore);
		}
	}

	private void writeTableColumnStyle(StyleEntry aStyle) {
		writer.attribute("style:family", "table-column"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.openTag("style:table-column-properties");

		Double width = aStyle.getDoubleProperty(StyleConstant.WIDTH);
		if (width != null && width >= 0.0) {
			writer.attribute("style:column-width", getDimension(width));
		}
		// writer.attribute("style:rel-column-width", aStyle.getWidth( ));

		writer.closeTag("style:table-column-properties");
	}

	private void writeTableRowStyle(StyleEntry style) {
		writer.attribute("style:family", "table-row"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.openTag("style:table-row-properties");

		Double height = style.getDoubleProperty(StyleConstant.HEIGHT);
		if (height != null && height > 0.0) {
			writer.attribute("style:row-height", getDimension(height));
		}

		Double minHeight = style.getDoubleProperty(StyleConstant.MIN_HEIGHT);
		if (minHeight != null && minHeight > 0.0) {
			writer.attribute("style:min-row-height", getDimension(minHeight));
		} else {
			writer.attribute("style:use-optimal-row-height", "true");
		}

		writer.closeTag("style:table-row-properties");
	}

	private void writeTableCellStyle(StyleEntry aStyle) {
		writer.attribute("style:family", "table-cell"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.openTag("style:table-cell-properties");

		Double cellWidth = aStyle.getDoubleProperty(StyleConstant.WIDTH);
		if (cellWidth != null) {
			writeCellWidth(cellWidth.intValue());
		}

		writeCellProperties(aStyle);
		writeAlign(aStyle);

		writeBackgroundImage(aStyle);
		writer.closeTag("style:table-cell-properties");
	}

	private void writeCellWidth(double width) {
		if (width >= 0) {
			writer.attribute("style:width", getDimension(width));
		}
	}

	private void writeBorders(StyleEntry style) {
		writeBorders(style, false);
	}

	private void writeGraphicStyle(StyleEntry style) {
		writer.attribute("style:family", "graphic");
		writer.attribute("style:parent-style-name", "Graphics");
		writer.openTag("style:graphic-properties");

		writeBorders(style);
		writeBackgroundColor(style.getStringProperty(StyleConstant.BACKGROUND_COLOR_PROP));
		writeFrameStyle(style);

		writer.openTag("style:background-image");
		writer.closeTag("style:background-image");

		writer.closeTag("style:graphic-properties");
	}

	private void writeFrameStyle(StyleEntry style) {
		String fill = style.getStringProperty(StyleConstant.GRAPHIC_FILL);
		if (fill != null) {
			writer.attribute("draw:fill", fill);
		}

		String fillColor = style.getStringProperty(StyleConstant.GRAPHIC_FILL_COLOR);
		if (fillColor != null) {
			writer.attribute("draw:fill-color", fillColor);
		}

		String stroke = style.getStringProperty(StyleConstant.GRAPHIC_STROKE);
		if (stroke != null) {
			writer.attribute("draw:stroke", stroke);
		}

		Double strokeWidth = style.getDoubleProperty(StyleConstant.GRAPHIC_STROKE_WIDTH);
		if (stroke != null) {
			writer.attribute("svg:stroke-width", getDimension(strokeWidth));
		}

		String color = style.getStringProperty(StyleConstant.COLOR_PROP);
		if (color != null) {
			writer.attribute("svg:stroke-color", color);
		}

	}

	public void writeParagraphStyle(StyleEntry aStyle, boolean noCellProps) {
		writer.attribute("style:family", "paragraph"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.attribute("style:parent-style-name", "Standard");
		String masterPage = aStyle.getStringProperty(StyleConstant.MASTER_PAGE);
		if (masterPage != null) {
			writer.attribute("style:master-page-name", masterPage);
		}

		writeParagraphProperties(aStyle, noCellProps);

	}

	/**
	 * @param aStyle
	 * @param noCellProps
	 */
	private void writeParagraphProperties(StyleEntry aStyle, boolean noCellProps) {
		// paragraph styles
		writer.openTag("style:paragraph-properties");
		// from writeTextInParagraph()
		CSSValue lineHeight = (CSSValue) aStyle.getProperty(StyleConstant.LINE_HEIGHT);
		if (lineHeight != null && !"normal".equalsIgnoreCase(lineHeight.getCssText())) //$NON-NLS-1$
		{
			writeSpacing(lineHeight);
		}

		if (!noCellProps) {
			writeBackgroundColor(aStyle.getStringProperty(StyleConstant.BACKGROUND_COLOR_PROP));
			writeBorders(aStyle, true);
			writePaddings(aStyle);
		}

		writeAlign(aStyle);

		writeIndent(aStyle);

		writePageBreaks(aStyle);

		writeBackgroundImage(aStyle);

		writer.closeTag("style:paragraph-properties");
	}

	public void writeTextStyle(StyleEntry style) {
		writer.attribute("style:family", "text"); //$NON-NLS-1$ //$NON-NLS-2$
		writeTextProperties(style);
	}

	/**
	 * @param style
	 */
	private void writeTextProperties(StyleEntry style) {
		// text properties
		writer.openTag("style:text-properties");

		Boolean isHidden = style.getBoolProperty(StyleConstant.HIDDEN);
		if (isHidden != null && isHidden.booleanValue()) {
			writer.attribute("text:display", "none");
		}

		// TODO: declare global fonts ? use font-name to map
		// to them instead of defining the font attributes directly here

		writeFont(style.getStringProperty(StyleEntry.FONT_FAMILY_PROP));
		writeFontStyle(style);
		writeFontWeight(style);
		writeFontSize(style);

		writeTextColor(style);
		writeLetterSpacing(style);
		writeTextUnderline(style);
		writeTextLineThrough(style);

		writeTextTransform(style);

		writer.closeTag("style:text-properties");
	}

	private void writeTextTransform(StyleEntry style) {
		String textTransform = style.getStringProperty(StyleConstant.TEXT_TRANSFORM);
		if (textTransform != null && !"none".equals(textTransform)) {
			writer.attribute("fo:text-transform", textTransform);
		}
	}

	private void writeSpacing(CSSValue spacing) {
		if (spacing != null) {
			writer.attribute("fo:line-height", getDimension(spacing));
		}
	}

	protected void writeBackgroundColor(String color) {
		String cssColor = OdfUtil.parseColor(color);
		if (cssColor == null) {
			return;
		}

		writer.attribute("fo:background-color", cssColor);
	}

	private void writeMargins(StyleEntry style) {
		for (int i = 0; i < MARGIN_PROPS.length; i++) {
			String margin = getDimension(style.getProperty(MARGIN_PROPS[i]));
			if (margin != null) {
				writer.attribute("fo:margin-" + SIDES[i], margin);
			}
		}
	}

	private void writeCellBorders(StyleEntry style) {
		writeBorders(style, false);
	}

	/**
	 * 
	 * @param style
	 * @param margins margins array, bottom, top, left, right
	 */
	protected void writeBorders(StyleEntry style, boolean includeMargins) {
		for (int i = 0; i < 4; i++) {
			String borderStyle = style.getStringProperty(BORDER_STYLE_PROPS[i]);
			if (hasBorder(borderStyle)) {
				writeSingleBorder(SIDES[i], borderStyle, style.getStringProperty(BORDER_COLOR_PROPS[i]),
						getDimension(style.getProperty(BORDER_WIDTH_PROPS[i])),
						includeMargins ? getDimension(style.getProperty(MARGIN_PROPS[i])) : null);
			}
		}
	}

	protected void writePaddings(StyleEntry style) {
		for (int i = 0; i < PADDING_PROPS.length; i++) {
			CSSValue paddingValue = (CSSValue) style.getProperty(PADDING_PROPS[i]);
			if (paddingValue != null) {
				writeSinglePadding(SIDES[i], getDimension(paddingValue));
			}
		}
	}

	private void writeSingleBorder(String type, String borderStyle, String color, String width, String margin) {
		writer.attribute("fo:border-" + type, width + " " + borderStyle + " " + OdfUtil.parseColor(color));
		if (margin != null) {
			writer.attribute("fo:margin-" + type, margin);
		}
	}

	/**
	 * @param width
	 * @return
	 */
	private String getDimension(CSSValue width) {
		return getDimension(OdfUtil.getDimensionValue(width, reportDpi));
	}

	private void writeSinglePadding(String type, String value) {
		writer.attribute("fo:padding-" + type, value);
	}

	protected void writeAlign(StyleEntry style) {
		String align = style.getStringProperty(StyleConstant.H_ALIGN_PROP);
		if (align != null) {
			writer.attribute("fo:text-align", align); //$NON-NLS-1$
		}

		String verticalAlign = style.getStringProperty(StyleConstant.V_ALIGN_PROP);
		if (verticalAlign != null) {
			writer.attribute("style:vertical-align", verticalAlign);
		}
	}

	private void writeTextColor(StyleEntry style) {
		String val = OdfUtil.parseColor(style.getStringProperty(StyleConstant.COLOR_PROP));
		if (val != null) {
			writer.attribute("fo:color", val);
		}
	}

	private void writeTextUnderline(StyleEntry style) {
		Boolean underline = style.getBoolProperty(StyleConstant.TEXT_UNDERLINE_PROP);
		if (underline != null && underline.booleanValue()) {
			writer.attribute("style:text-underline-type", "single");
		}

		Boolean overline = style.getBoolProperty(StyleConstant.TEXT_OVERLINE_PROP);
		if (overline != null && overline.booleanValue()) {
			writer.attribute("style:text-overline-type", "single");
		}
	}

	private void writeTextLineThrough(StyleEntry style) {
		Boolean lineThrough = style.getBoolProperty(StyleConstant.TEXT_LINE_THROUGH_PROP);
		if (lineThrough != null && lineThrough.booleanValue()) {
			writer.attribute("style:text-line-through-type", "single");
		}
	}

	private void writeLetterSpacing(StyleEntry style) {
		CSSValue spacing = (CSSValue) style.getProperty(StyleConstant.LETTER_SPACING);
		if (spacing != null) {
			double letterSpacing = OdfUtil.getDimensionValue((CSSValue) style.getProperty(StyleConstant.LETTER_SPACING),
					reportDpi);
			writer.attribute("fo:letter-spacing", getDimension(letterSpacing));
		}
	}

	/**
	 * If the cell properties is not set, then check the row properties and write
	 * those properties.
	 * 
	 * @param style this cell style
	 */
	private void writeCellProperties(StyleEntry style) {
		// A cell background color may inherit from row background,
		// so we should get the row background color here,
		// if the cell background is transparent
		if (style == null) {
			return;
		}

		writer.attribute("style:text-align-source", "fix");

		writeBackgroundColor(style.getStringProperty(StyleConstant.BACKGROUND_COLOR_PROP));
		writeCellBorders(style);
		writePaddings(style); // default seems to be 1pt, even if padding property is null
		writer.attribute("fo:wrap-option",
				CSSConstants.CSS_NOWRAP_VALUE.equalsIgnoreCase(style.getStringProperty(StyleConstant.WHITE_SPACE))
						? "no-wrap"
						: "wrap");
	}

	private void writeBackgroundImage(StyleEntry style) {
		String url = style.getStringProperty(StyleConstant.BACKGROUND_IMAGE_URL);
		if (url == null || url.length() == 0) {
			return;
		}

		// open office always keeps this tag, even if empty
		writer.openTag("style:background-image");
		if (url != null) {
			String repeat = style.getStringProperty(StyleConstant.BACKGROUND_IMAGE_REPEAT);
			// TODO: embed the image into the document, inside the package
			writer.attribute("xlink:href", url);

			String width = style.getStringProperty(StyleConstant.BACKGROUND_IMAGE_WIDTH);
			String height = style.getStringProperty(StyleConstant.BACKGROUND_IMAGE_HEIGHT);

			if ("cover".equals(width) || "cover".equals(height)) {
				repeat = "stretch";
			}

			if ("no-repeat".equals(repeat)) {
				String leftPos = style.getStringProperty(StyleConstant.BACKGROUND_IMAGE_LEFT);
				String topPos = style.getStringProperty(StyleConstant.BACKGROUND_IMAGE_TOP);

				if ("100%".equals(leftPos)) {
					leftPos = "right";
				} else if ("50%".equals(leftPos)) {
					leftPos = "center";
				} else {
					leftPos = "left";
				}

				if ("100%".equals(topPos)) {
					topPos = "bottom";
				} else if ("50%".equals(topPos)) {
					topPos = "center";
				} else {
					topPos = "top";
				}

				writer.attribute("style:position", leftPos + " " + topPos);
			}

			writer.attribute("style:repeat", repeat);
		}
		writer.closeTag("style:background-image");
	}

	protected void writeFontSize(StyleEntry styleEntry) {
		double size = 0;
		Double sizeDouble = (Double) styleEntry.getDoubleProperty(StyleEntry.FONT_SIZE_PROP);
		if (sizeDouble != null) {
			size = sizeDouble.doubleValue();
		} else {
			CSSValue fontSize = (CSSValue) styleEntry.getProperty(StyleEntry.FONT_SIZE_PROP);
			if (fontSize != null) {
				size = OdfUtil.parseFontSize(PropertyUtil.getDimensionValue(fontSize));
			}
		}

		if (size > 0) {
			writer.attribute("fo:font-size", size + "pt");
			writer.attribute("style:font-size-asian", size + "pt");
			writer.attribute("style:font-size-complex", size + "pt");
		}
	}

	protected void writeFont(String fontFamily) {
		if (fontFamily != null && !StyleConstant.NULL.equals(fontFamily)) {
			writer.attribute("fo:font-family", fontFamily);
			writer.attribute("style:font-family-asian", fontFamily);
			writer.attribute("style:font-family-complex", fontFamily);
		}
	}

	protected void writeFontStyle(StyleEntry styleEntry) {
		String val = styleEntry.getStringProperty(StyleEntry.FONT_STYLE_PROP);
		if (!"normal".equalsIgnoreCase(val)) {
			writer.attribute("fo:font-style", val);
			writer.attribute("style:font-style-asian", val);
			writer.attribute("style:font-style-complex", val);
		}
	}

	protected void writeFontWeight(StyleEntry styleEntry) {
		String weight = styleEntry.getStringProperty(StyleEntry.FONT_WEIGHT_PROP);
		if (weight != null && !"normal".equals(weight)) {
			writer.attribute("fo:font-weight", weight);
			writer.attribute("style:font-weight-asian", weight);
			writer.attribute("style:font-weight-complex", weight);
		}
	}

	private boolean hasBorder(String borderStyle) {
		return !(borderStyle == null || "none".equalsIgnoreCase(borderStyle));
	}

	private void writeTableWidth(double tableWidth) {
		if (tableWidth >= 0.0) {
			writer.attribute("style:width", getDimension(tableWidth));
		}
	}

	private void writeIndent(StyleEntry style) {
		CSSValue value = (CSSValue) style.getProperty(StyleConstant.TEXT_INDENT);
		if (value != null) {
			writer.attribute("fo:text-indent", getDimension(value));
		}
	}

	private String getDimension(Double value) {
		if (value != null) {
			// round to 1/1000
			value = Math.round(value * 1000.0) / 1000.0;
			// return ( value / 1000.0 ) + "in";
			return value + "in";
		}
		return "";
	}

	private String getDimension(Object value) {
		if (value == null) {
			return "0in";
		}

		if (value instanceof CSSValue) {
			return getDimension((CSSValue) value);
		}

		if (value instanceof DimensionType) {
			// double value
			return getDimension(OdfUtil.convertTo((DimensionType) value, reportDpi));
		}

		return value.toString();
	}

	private void writePageLayout(StyleEntry style) {
		writer.openTag("style:page-layout-properties");
		writePageLayoutProperties(style);
		writer.closeTag("style:page-layout-properties");

		writer.openTag("style:header-style");
		writer.openTag("style:header-footer-properties");
		writer.attribute("fo:min-height", getDimension(style.getProperty(StyleConstant.HEADER_HEIGHT)));
		writer.closeTag("style:header-footer-properties");
		writer.closeTag("style:header-style");

		writer.openTag("style:footer-style");
		writer.openTag("style:header-footer-properties");
		writer.attribute("fo:min-height", getDimension(style.getIntegerProperty(StyleConstant.FOOTER_HEIGHT)));
		writer.closeTag("style:header-footer-properties");
		writer.closeTag("style:footer-style");
	}

	private void writePageLayoutProperties(StyleEntry style) {
		// write borders and margin
		writeMargins(style);
		writeBorders(style);
		writeBackgroundColor(style.getStringProperty(StyleConstant.BACKGROUND_COLOR_PROP));

		writer.attribute("style:print-orientation", style.getStringProperty(StyleConstant.PAGE_ORIENTATION));
		writer.attribute("fo:page-width", getDimension(style.getProperty(StyleConstant.WIDTH)));
		writer.attribute("fo:page-height", getDimension(style.getProperty(StyleConstant.HEIGHT)));

		writer.attribute("style:num-format", "1");

		String direction = style.getStringProperty(StyleConstant.DIRECTION_PROP);
		if ("rtl".equals(direction)) {
			writer.attribute("style:writing-mode", "rl");
		} else {
			writer.attribute("style:writing-mode", "lr");
		}

		writeBackgroundImage(style);
	}

	public void writeStyles(Collection<StyleEntry> styles) {
		if (styles == null) {
			return;
		}
		for (StyleEntry style : styles) {
			writeStyle(style);
		}
	}

}
