/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
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

package uk.co.spudsoft.birt.emitters.excel.handlers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.layout.emitter.Image;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;
import org.w3c.dom.css.CSSValue;

import uk.co.spudsoft.birt.emitters.excel.Area;
import uk.co.spudsoft.birt.emitters.excel.AreaBorders;
import uk.co.spudsoft.birt.emitters.excel.BirtStyle;
import uk.co.spudsoft.birt.emitters.excel.CellImage;
import uk.co.spudsoft.birt.emitters.excel.ClientAnchorConversions;
import uk.co.spudsoft.birt.emitters.excel.Coordinate;
import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.RichTextRun;
import uk.co.spudsoft.birt.emitters.excel.StyleManager;
import uk.co.spudsoft.birt.emitters.excel.StyleManagerUtils;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class CellContentHandler extends AbstractHandler {

	/**
	 * Number of milliseconds in a day, to determine whether a given date is
	 * date/time/datetime
	 */
	private static final long oneDay = 24 * 60 * 60 * 1000;

	/**
	 * The last value added to a cell
	 */
	protected Object lastValue;
	/**
	 * The BIRT element that provided the lastValue
	 */
	protected IContent lastElement;
	/**
	 * List of font changes for a single cell.
	 */
	protected List<RichTextRun> richTextRuns = new ArrayList<>();
	/**
	 * When having to join multiple text blocks together, track whether they are
	 * block or inline display
	 */
	protected boolean lastCellContentsWasBlock;
	/**
	 * When having to join multiple text blocks together, track whether they need
	 * more of a gap between them (basically used for flattened table cells)
	 */
	protected boolean lastCellContentsRequiresSpace;
	/**
	 * The span of the current cell
	 */
	protected int colSpan;
	/**
	 * Visitor to enable processing of child elements created for foreign (HTML)
	 * elements.
	 */
	protected ContentEmitterVisitor contentVisitor;
	/**
	 * Override the cell alignment to this instead, unless zero
	 */
	protected CSSValue preferredAlignment;
	/**
	 * URL that this cell should hyperlink to
	 */
	protected String hyperlinkUrl;
	/**
	 * Bookmark that this cell should hyperlink to
	 */
	protected String hyperlinkBookmark;

	private static String DATA_PROTOCOL = "data:";

	private static String DATA_PROTOCOL_BASE = ";base64,";

	public CellContentHandler(IContentEmitter emitter, Logger log, IHandler parent, ICellContent cell) {
		super(log, parent, cell);
		contentVisitor = new ContentEmitterVisitor(emitter);
		colSpan = 1;
	}

	@Override
	public void startCell(HandlerState state, ICellContent cell) throws BirtException {
		if (cell.getBookmark() != null) {
			System.err.println("Bookmark: " + cell.getBookmark());
		}
	}

	/**
	 * Finish processing for the current (real) cell.
	 *
	 * @param element The element that signifies the end of the cell (this may not
	 *                be an ICellContent object if the cell is created for a label
	 *                or text outside of a table).
	 */
	protected void endCellContent(HandlerState state, ICellContent birtCell, IContent element, Cell cell, Area area) {
		StyleManager sm = state.getSm();
		StyleManagerUtils smu = state.getSmu();

		BirtStyle birtCellStyle = null;
		if (birtCell != null) {
			birtCellStyle = new BirtStyle(birtCell);
			if (element != null) {
				// log.debug( "Overlaying style from ", element );
				birtCellStyle.overlay(element);
			}
		} else if (element != null) {
			birtCellStyle = new BirtStyle(element);
		} else {
			birtCellStyle = new BirtStyle(state.getSm().getCssEngine());
		}
		if (preferredAlignment != null) {
			birtCellStyle.setProperty(StyleConstants.STYLE_TEXT_ALIGN, preferredAlignment);
		}
		if (CSSConstants.CSS_TRANSPARENT_VALUE.equals(birtCellStyle.getString(StyleConstants.STYLE_BACKGROUND_COLOR))) {
			if (parent != null) {
				birtCellStyle.setProperty(StyleConstants.STYLE_BACKGROUND_COLOR, parent.getBackgroundColour());
			}
		}
		if (hyperlinkUrl != null) {
			Hyperlink hyperlink = cell.getSheet().getWorkbook().getCreationHelper()
					.createHyperlink(HyperlinkType.URL /* Hyperlink.LINK_URL */);
			hyperlink.setAddress(hyperlinkUrl);
			cell.setHyperlink(hyperlink);
		}
		if (hyperlinkBookmark != null) {
			Hyperlink hyperlink = cell.getSheet().getWorkbook().getCreationHelper()
					.createHyperlink(HyperlinkType.DOCUMENT /* Hyperlink.LINK_DOCUMENT */);
			hyperlink.setAddress(prepareName(hyperlinkBookmark));
			cell.setHyperlink(hyperlink);
		}

		if (lastValue != null) {
			if (lastValue instanceof String) {
				String lastString = (String) lastValue;

				smu.correctFontColorIfBackground(birtCellStyle);
				for (RichTextRun run : richTextRuns) {
					run.font = smu.correctFontColorIfBackground(sm.getFontManager(), state.getWb(), birtCellStyle,
							run.font);
				}

				if (!richTextRuns.isEmpty()) {
					RichTextString rich = smu.createRichTextString(lastString);
					int runStart = richTextRuns.get(0).startIndex;
					Font lastFont = richTextRuns.get(0).font;
					for (int i = 0; i < richTextRuns.size(); ++i) {
						RichTextRun run = richTextRuns.get(i);
						log.debug("Run: ", run.startIndex, " font :", run.font);
						if (!lastFont.equals(run.font)) {
							log.debug("Applying ", runStart, " - ", run.startIndex);
							rich.applyFont(runStart, run.startIndex, lastFont);
							runStart = run.startIndex;
							lastFont = richTextRuns.get(i).font;
						}
					}

					log.debug("Finalising with ", runStart, " - ", lastString.length());
					rich.applyFont(runStart, lastString.length(), lastFont);

					setCellContents(cell, rich);
				} else {
					setCellContents(cell, lastString);
				}

				if (lastString.contains("\n")) {
					if (!CSSConstants.CSS_NOWRAP_VALUE.equals(lastElement.getStyle().getWhiteSpace())) {
						birtCellStyle.setProperty(StyleConstants.STYLE_WHITE_SPACE,
								new StringValue(StringValue.CSS_STRING, CSSConstants.CSS_PRE_VALUE));
					}
				}
				if (!richTextRuns.isEmpty()) {
					birtCellStyle.setProperty(StyleConstants.STYLE_VERTICAL_ALIGN,
							new StringValue(StringValue.CSS_STRING, CSSConstants.CSS_TOP_VALUE));
				}
				if (preferredAlignment != null) {
					birtCellStyle.setProperty(StyleConstants.STYLE_TEXT_ALIGN, preferredAlignment);
				}

			} else {
				setCellContents(cell, lastValue);
			}
		}

		int colIndex = cell.getColumnIndex();
		state.getSmu().applyAreaBordersToCell(state.areaBorders, cell, birtCellStyle, state.rowNum, colIndex);

		if ((birtCell != null) && ((birtCell.getColSpan() > 1) || (birtCell.getRowSpan() > 1))) {
			AreaBorders mergedRegionBorders = AreaBorders.createForMergedCells(state.rowNum + birtCell.getRowSpan() - 1,
					colIndex, colIndex + birtCell.getColSpan() - 1, state.rowNum, birtCellStyle);
			if (mergedRegionBorders != null) {
				state.insertBorderOverload(mergedRegionBorders);
			}
		}

		String customNumberFormat = EmitterServices.stringOption(state.getRenderOptions(), element,
				ExcelEmitter.CUSTOM_NUMBER_FORMAT, null);
		if (customNumberFormat != null) {
			StyleManagerUtils.setNumberFormat(birtCellStyle, ExcelEmitter.CUSTOM_NUMBER_FORMAT + customNumberFormat,
					null);
		}

		setCellStyle(sm, cell, birtCellStyle, lastValue);

		// Excel auto calculates the row height (if it isn't specified) as long as the
		// cell isn't merged - if it is merged I have to do it
		if (((colSpan > 1) || (state.rowHasSpans(state.rowNum)))
				&& ((lastValue instanceof String) || (lastValue instanceof RichTextString))) {
			int spannedRowAlgorithm = EmitterServices.integerOption(state.getRenderOptions(), element,
					ExcelEmitter.SPANNED_ROW_HEIGHT, ExcelEmitter.SPANNED_ROW_HEIGHT_SPREAD);
			Font defaultFont = state.getWb().getFontAt(cell.getCellStyle().getFontIndexAsInt() /* .getFontIndex() */);
			double cellWidth = spanWidthMillimetres(state.currentSheet, cell.getColumnIndex(),
					cell.getColumnIndex() + colSpan - 1);
			float cellDesiredHeight = smu.calculateTextHeightPoints(cell.getStringCellValue(), defaultFont, cellWidth,
					richTextRuns);
			if (cellDesiredHeight > state.requiredRowHeightInPoints) {
				int rowSpan = birtCell.getRowSpan();
				if (rowSpan < 2) {
					state.requiredRowHeightInPoints = cellDesiredHeight;
				} else {
					switch (spannedRowAlgorithm) {
					case ExcelEmitter.SPANNED_ROW_HEIGHT_FIRST:
						state.requiredRowHeightInPoints = cellDesiredHeight;
						break;
					case ExcelEmitter.SPANNED_ROW_HEIGHT_IGNORED:
						break;
					default:
						if (area != null) {
							area.setHeight(cellDesiredHeight);
						}
					}
				}
			}
		}

		// Adjust the required row height for any relevant areas based on what's left
		float rowSpanHeightRequirement = state.calculateRowSpanHeightRequirement(state.rowNum);
		if (rowSpanHeightRequirement > state.requiredRowHeightInPoints) {
			state.requiredRowHeightInPoints = rowSpanHeightRequirement;
		}

		if (EmitterServices.booleanOption(state.getRenderOptions(), element, ExcelEmitter.FREEZE_PANES, false)) {
			if (state.currentSheet.getPaneInformation() == null) {
				state.currentSheet.createFreezePane(state.colNum, state.rowNum);
			}
		}

		lastValue = null;
		lastElement = null;
		richTextRuns.clear();
	}

	/**
	 * Calculate the width of a set of columns, in millimetres.
	 *
	 * @param startCol The first column to consider (inclusive).
	 * @param endCol   The last column to consider (inclusive).
	 * @return The sum of the widths of all columns between startCol and endCol
	 *         (inclusive) in millimetres.
	 */
	private double spanWidthMillimetres(Sheet sheet, int startCol, int endCol) {
		int result = 0;
		for (int columnIndex = startCol; columnIndex <= endCol; ++columnIndex) {
			result += sheet.getColumnWidth(columnIndex);
		}
		return ClientAnchorConversions.widthUnits2Millimetres(result);
	}

	/**
	 * Set the contents of an empty cell. This should now be the only way in which a
	 * cell value is set (cells should not be modified).
	 *
	 * @param value   The value to set.
	 * @param element The BIRT element supplying the value, used to set the style of
	 *                the cell.
	 */
	private <T> void setCellContents(Cell cell, Object value) {
		log.debug("Setting cell[", cell.getRow().getRowNum(), ",", cell.getColumnIndex(), "] value to ", value);
		if (value instanceof Double) {
			// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue((Double) value);
			lastValue = value;
		} else if (value instanceof Integer) {
			// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue((Integer) value);
			lastValue = value;
		} else if (value instanceof Long) {
			// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue((Long) value);
			lastValue = value;
		} else if (value instanceof Date) {
			// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue((Date) value);
			lastValue = value;
		} else if (value instanceof Boolean) {
			// cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
			cell.setCellValue(((Boolean) value).booleanValue());
			lastValue = value;
		} else if (value instanceof BigDecimal) {
			// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(((BigDecimal) value).doubleValue());
			lastValue = value;
		} else if (value instanceof String) {
			// cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue((String) value);
			lastValue = value;
		} else if (value instanceof RichTextString) {
			// cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue((RichTextString) value);
			lastValue = value;
		} else if (value != null) {
			log.debug("Unhandled data: ", (value == null ? "<null>" : value));
			// cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(value.toString());
			lastValue = value;
		}
	}

	/**
	 * Set the style of the current cell based on the style of a BIRT element.
	 *
	 * @param element The BIRT element to take the style from.
	 */
	@SuppressWarnings("deprecation")
	private void setCellStyle(StyleManager sm, Cell cell, BirtStyle birtStyle, Object value) {

		if ((StyleManagerUtils.getNumberFormat(birtStyle) == null)
				&& (StyleManagerUtils.getDateFormat(birtStyle) == null)
				&& (StyleManagerUtils.getDateTimeFormat(birtStyle) == null)
				&& (StyleManagerUtils.getTimeFormat(birtStyle) == null) && (value != null)) {
			if (value instanceof Date) {
				long time = ((Date) value).getTime();
				time = time - ((Date) value).getTimezoneOffset() * 60000;
				if (time % oneDay == 0) {
					StyleManagerUtils.setDateFormat(birtStyle, "Short Date", null);
				} else if (time < oneDay) {
					StyleManagerUtils.setTimeFormat(birtStyle, "Short Time", null);
				} else {
					StyleManagerUtils.setDateTimeFormat(birtStyle, "General Date", null);
				}
			}
		}

		// log.debug( "BirtStyle: ", birtStyle);
		CellStyle cellStyle = sm.getStyle(birtStyle);
		cell.setCellStyle(cellStyle);
	}

	private CSSValue preferredAlignment(BirtStyle elementStyle) {
		CSSValue newAlign = elementStyle.getProperty(StyleConstants.STYLE_TEXT_ALIGN);
		if (newAlign == null) {
			newAlign = new StringValue(StringValue.CSS_STRING, CSSConstants.CSS_LEFT_VALUE);
		}
		if (preferredAlignment == null) {
			return newAlign;
		}
		if (CSSConstants.CSS_LEFT_VALUE.equals(newAlign.getCssText())) {
			return newAlign;
		} else if (CSSConstants.CSS_RIGHT_VALUE.equals(newAlign.getCssText())) {
			if (CSSConstants.CSS_CENTER_VALUE.equals(preferredAlignment.getCssText())) {
				return newAlign;
			} else {
				return preferredAlignment;
			}
		} else {
			return preferredAlignment;
		}
	}

	/**
	 * Set the contents of the current cell. If the current cell is empty this will
	 * format the cell optimally for the new value, if the current cell already has
	 * some contents this will simply append the text value to the current contents.
	 *
	 * @param value The value to put into the current cell.
	 */
	protected void emitContent(HandlerState state, IContent element, Object value, boolean asBlock) {
		if (value == null) {
			return;
		}
		if (element.getBookmark() != null) {
			createName(state, prepareName(element.getBookmark()), state.rowNum, state.colNum, state.rowNum,
					state.colNum);
		}

		if (lastValue == null) {
			lastValue = value;
			lastElement = element;
			lastCellContentsWasBlock = asBlock;

			IHyperlinkAction birtHyperlink = element.getHyperlinkAction();
			if (birtHyperlink != null) {
				switch (birtHyperlink.getType()) {
				case IHyperlinkAction.ACTION_HYPERLINK:
					hyperlinkUrl = birtHyperlink.getHyperlink();
					break;
				case IHyperlinkAction.ACTION_BOOKMARK:
					hyperlinkBookmark = birtHyperlink.getBookmark();
					break;
				case IHyperlinkAction.ACTION_DRILLTHROUGH:
					IHTMLActionHandler handler = state.getRenderOptions().getActionHandler();
					if (handler != null) {
						hyperlinkUrl = handler.getURL(new Action(null, birtHyperlink),
								element.getReportContent().getReportContext());
					}
					break;
				default:
					log.debug("Unhandled hyperlink type: {}", birtHyperlink.getType());
				}
			}

			return;
		}

		StyleManager sm = state.getSm();

		// Both to be improved to include formatting
		String oldValue = lastValue.toString();
		String newComponent = value.toString();

		if (lastCellContentsWasBlock && !newComponent.startsWith("\n") && !oldValue.endsWith("\n")) {
			oldValue = oldValue + "\n";
			lastCellContentsWasBlock = false;
		}
		if (lastCellContentsRequiresSpace && !newComponent.startsWith("\n") && !oldValue.endsWith("\n")) {
			oldValue = oldValue + " ";
			lastCellContentsRequiresSpace = false;
		}

		String newValue = oldValue + newComponent;
		lastValue = newValue;

		if (element != null) {
			BirtStyle elementStyle = new BirtStyle(element);
			Font newFont = sm.getFontManager().getFont(elementStyle);
			richTextRuns.add(new RichTextRun(oldValue.length(), newFont));

			preferredAlignment = preferredAlignment(elementStyle);
		}

		lastCellContentsWasBlock = asBlock;
		hyperlinkUrl = null;
	}

	public void recordImage(HandlerState state, Coordinate location, IImageContent image, boolean spanColumns)
			throws BirtException {
		byte[] data = image.getData();
		log.debug("startImage: " + "[" + image.getMIMEType() + "] " + "{" + image.getWidth() + " x " + image.getHeight()
				+ "} " + (data == null ? "(no data) " : "(" + data.length + " bytes) ") + image.getURI());

		StyleManagerUtils smu = state.getSmu();
		Workbook wb = state.getWb();
		String mimeType = image.getMIMEType();
		if ((data == null) && (image.getURI() != null)) {
			String stringURI = image.getURI().toString().toLowerCase();
			if (stringURI.startsWith(DATA_PROTOCOL) && stringURI.contains(DATA_PROTOCOL_BASE)) {
				String base64[] = image.getURI().toString().split(DATA_PROTOCOL_BASE);
				if (base64.length >= 2) {
					data = Base64.decodeBase64(base64[1]);
				}
			} else {
				try {
					URL imageUrl = new URL(image.getURI());
					URLConnection conn = imageUrl.openConnection();
					conn.connect();
					mimeType = conn.getContentType();
					int imageType = smu.poiImageTypeFromMimeType(mimeType, null);
					if (imageType == 0) {
						log.debug("Unrecognised/unhandled image MIME type: " + mimeType);
					} else {
						data = smu.downloadImage(conn);
						image.setData(data);
					}
				} catch (IOException ex) {
					log.debug(ex.getClass(), ": ", ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
		if (data != null) {
			int imageType = smu.poiImageTypeFromMimeType(mimeType, data);
			if (imageType == 0) {
				log.debug("Unrecognised/unhandled image MIME type: ", image.getMIMEType());
			} else {
				int imageIdx = wb.addPicture(data, imageType);

				if ((image.getHeight() == null) || (image.getWidth() == null)) {
					Image birtImage = new Image();
					birtImage.setInput(data);
					birtImage.check();
					log.debug("Calculated image dimensions " + birtImage.getWidth() + " (@"
							+ birtImage.getPhysicalWidthDpi() + "dpi=" + birtImage.getPhysicalWidthInch() + "in) x "
							+ birtImage.getHeight() + " (@" + birtImage.getPhysicalHeightDpi() + "dpi="
							+ birtImage.getPhysicalHeightInch() + "in)");
					if (image.getWidth() == null) {
						DimensionType Width = new DimensionType(
								(birtImage.getPhysicalWidthInch() > 0) ? birtImage.getPhysicalWidthInch()
										: birtImage.getWidth() / 96.0,
								"in");
						image.setWidth(Width);
					}
					if (image.getHeight() == null) {
						DimensionType Height = new DimensionType(
								(birtImage.getPhysicalHeightInch() > 0) ? birtImage.getPhysicalHeightInch()
										: birtImage.getHeight() / 96.0,
								"in");
						image.setHeight(Height);
					}
				}

				state.images.add(new CellImage(location, imageIdx, image, spanColumns));
				lastElement = image;
			}
		}
	}

	protected void removeMergedCell(HandlerState state, int row, int col) {
		for (int mergeNum = 0; mergeNum < state.currentSheet.getNumMergedRegions(); ++mergeNum) {
			CellRangeAddress region = state.currentSheet.getMergedRegion(mergeNum);
			if ((region.getFirstRow() == row) && (region.getFirstColumn() == col)) {
				state.currentSheet.removeMergedRegion(mergeNum);
				break;
			}
		}

		for (Iterator<Area> iter = state.rowSpans.iterator(); iter.hasNext();) {
			Area area = iter.next();
			Coordinate topLeft = area.getX();
			if ((topLeft.getRow() == row) || (topLeft.getCol() == col)) {
				iter.remove();
			}
		}
	}

}
