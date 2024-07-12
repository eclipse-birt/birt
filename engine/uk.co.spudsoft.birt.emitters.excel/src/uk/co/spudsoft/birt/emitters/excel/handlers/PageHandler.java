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
 *
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.handlers;

import java.util.Collection;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType;

import uk.co.spudsoft.birt.emitters.excel.CellImage;
import uk.co.spudsoft.birt.emitters.excel.ClientAnchorConversions;
import uk.co.spudsoft.birt.emitters.excel.Coordinate;
import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.StyleManagerUtils;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

/**
 * Representation of the excel page
 *
 * @since 3.3
 *
 */
public class PageHandler extends AbstractHandler {

	/**
	 * Constructor
	 *
	 * @param log  logger object
	 * @param page page content
	 */
	public PageHandler(Logger log, IPageContent page) {
		super(log, null, page);
	}

	private void setupPageSize(HandlerState state, IPageContent page) {
		PrintSetup printSetup = state.currentSheet.getPrintSetup();
		printSetup.setPaperSize(state.getSmu().getPaperSizeFromString(page.getPageType()));
		if (page.getOrientation() != null) {
			if ("landscape".equals(page.getOrientation())) {
				printSetup.setLandscape(true);
			}
		}
	}

	private String contentAsString(HandlerState state, Object obj) throws BirtException {

		StringCellHandler stringCellHandler = new StringCellHandler(state.getEmitter(), log, this,
				obj instanceof CellContent ? (CellContent) obj : null);

		state.setHandler(stringCellHandler);

		stringCellHandler.visit(obj);

		state.setHandler(this);

		return stringCellHandler.getString();
	}

	@SuppressWarnings("rawtypes")
	private void processHeaderFooter(HandlerState state, Collection birtHeaderFooter, HeaderFooter poiHeaderFooter)
			throws BirtException {
		boolean handledAsGrid = false;
		for (Object ftrObject : birtHeaderFooter) {
			if (ftrObject instanceof ITableContent) {
				ITableContent ftrTable = (ITableContent) ftrObject;
				if (ftrTable.getChildren().size() == 1) {
					Object child = ftrTable.getChildren().toArray()[0];
					if (child instanceof IRowContent) {
						IRowContent row = (IRowContent) child;
						if (ftrTable.getColumnCount() <= 3) {
							Object[] cellObjects = row.getChildren().toArray();
							if (ftrTable.getColumnCount() == 1) {
								poiHeaderFooter.setLeft(contentAsString(state, cellObjects[0]));
								handledAsGrid = true;
							} else if (ftrTable.getColumnCount() == 2) {
								poiHeaderFooter.setLeft(contentAsString(state, cellObjects[0]));
								poiHeaderFooter.setRight(contentAsString(state, cellObjects[1]));
								handledAsGrid = true;
							} else if (ftrTable.getColumnCount() == 3) {
								poiHeaderFooter.setLeft(contentAsString(state, cellObjects[0]));
								poiHeaderFooter.setCenter(contentAsString(state, cellObjects[1]));
								poiHeaderFooter.setRight(contentAsString(state, cellObjects[2]));
								handledAsGrid = true;
							}
						}
					}
				}
			}
			if (!handledAsGrid) {
				poiHeaderFooter.setLeft(contentAsString(state, ftrObject));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void outputStructuredHeaderFooter(HandlerState state, Collection birtHeaderFooter) throws BirtException {
		ContentEmitterVisitor visitor = new ContentEmitterVisitor(state.getEmitter());
		for (Object content : birtHeaderFooter) {
			if (content instanceof IContent) {
				visitor.visit((IContent) content, null);
			}
		}

	}

	@Override
	public void startPage(HandlerState state, IPageContent page) throws BirtException {

		if (state.getWb().getNumberOfSheets() > 0) {
			if (EmitterServices.booleanOption(state.getRenderOptions(), page, ExcelEmitter.SINGLE_SHEET_PAGE_BREAKS,
					false)) {
				state.currentSheet.setRowBreak(state.rowNum - 1);
			}
			if (EmitterServices.booleanOption(state.getRenderOptions(), page, ExcelEmitter.SINGLE_SHEET, false)) {
				return;
			}
		}

		state.currentSheet = state.getWb().createSheet();
		log.debug("Page type: ", page.getPageType());

		if (page.getPageType() != null) {
			setupPageSize(state, page);
		}

		if (EmitterServices.booleanOption(state.getRenderOptions(), page, ExcelEmitter.DISPLAYFORMULAS_PROP, false)) {
			state.currentSheet.setDisplayFormulas(true);
		}
		if (!EmitterServices.booleanOption(state.getRenderOptions(), page, ExcelEmitter.DISPLAYGRIDLINES_PROP, true)) {
			state.currentSheet.setDisplayGridlines(false);
		}
		if (!EmitterServices.booleanOption(state.getRenderOptions(), page, ExcelEmitter.DISPLAYROWCOLHEADINGS_PROP,
				true)) {
			state.currentSheet.setDisplayRowColHeadings(false);
		}
		if (!EmitterServices.booleanOption(state.getRenderOptions(), page, ExcelEmitter.DISPLAYZEROS_PROP, true)) {
			state.currentSheet.setDisplayZeros(false);
		}
		if (EmitterServices.booleanOption(state.getRenderOptions(), page, ExcelEmitter.PRINTGRIDLINES_PROP, false)) {
			state.currentSheet.setPrintGridlines(true);
		}
		if (EmitterServices.booleanOption(state.getRenderOptions(), page, ExcelEmitter.PRINTROWCOLHEADINGS_PROP,
				false)) {
			state.currentSheet.setPrintRowAndColumnHeadings(true);
		}
		if (EmitterServices.booleanOption(state.getRenderOptions(), page, ExcelEmitter.PRINTFITTOPAGE_PROP, false)) {
			state.currentSheet.setFitToPage(true);
		}
		int displayZoom = EmitterServices.integerOption(state.getRenderOptions(), page,
				ExcelEmitter.DISPLAY_SHEET_ZOOM, -1);
		if ((displayZoom >= ExcelEmitter.poiExcelDisplaySheetZoomScaleMin)
				&& (displayZoom <= ExcelEmitter.poiExcelDisplaySheetZoomScaleMax)) {
			state.currentSheet.setZoom(displayZoom);
		}
		String pagePreview = EmitterServices.stringOption(state.getRenderOptions(), page, ExcelEmitter.PAGE_PREVIEW,
				null);
		if (pagePreview != null) {
			if (pagePreview.equalsIgnoreCase(ExcelEmitter.poiExcelPreviewPageLayout)) {
				CTSheetView view = ((XSSFSheet) state.currentSheet).getCTWorksheet().getSheetViews()
						.getSheetViewArray(0);
				view.setView(STSheetViewType.PAGE_LAYOUT);

			} else if (pagePreview.equalsIgnoreCase(ExcelEmitter.poiExcelPreviewPageBreak)) {
				CTSheetView view = ((XSSFSheet) state.currentSheet).getCTWorksheet().getSheetViews()
						.getSheetViewArray(0);
				view.setView(STSheetViewType.PAGE_BREAK_PREVIEW);
			}
		}
		int pagesHigh = EmitterServices.integerOption(state.getRenderOptions(), page, ExcelEmitter.PRINT_PAGES_HIGH,
				-1);
		if ((pagesHigh > 0) && (pagesHigh < Short.MAX_VALUE)) {
			state.currentSheet.getPrintSetup().setFitHeight((short) pagesHigh);
			state.currentSheet.setAutobreaks(true);
		}
		int pagesWide = EmitterServices.integerOption(state.getRenderOptions(), page, ExcelEmitter.PRINT_PAGES_WIDE,
				-1);
		if ((pagesWide > 0) && (pagesWide < Short.MAX_VALUE)) {
			state.currentSheet.getPrintSetup().setFitWidth((short) pagesWide);
			state.currentSheet.setAutobreaks(true);
		}
		int printScale = EmitterServices.integerOption(state.getRenderOptions(), page, ExcelEmitter.PRINT_SCALE, -1);
		if ((printScale >= ExcelEmitter.poiExcelPrintScaleMin) && (printScale <= ExcelEmitter.poiExcelPrintScaleMax)) {
			state.currentSheet.getPrintSetup().setScale((short) printScale);
		}

		if (EmitterServices.booleanOption(state.getRenderOptions(), page, ExcelEmitter.STRUCTURED_HEADER, false)) {
			outputStructuredHeaderFooter(state, page.getHeader());
		} else {
			processHeaderFooter(state, page.getHeader(), state.currentSheet.getHeader());
			processHeaderFooter(state, page.getFooter(), state.currentSheet.getFooter());
		}

		state.getSmu().prepareMarginDimensions(state.currentSheet, page);
	}

	@Override
	public void endPage(HandlerState state, IPageContent page) throws BirtException {

		if (EmitterServices.booleanOption(state.getRenderOptions(), page, ExcelEmitter.SINGLE_SHEET, false)
				&& !state.reportEnding) {
			return;
		}

		if (EmitterServices.booleanOption(state.getRenderOptions(), page, ExcelEmitter.STRUCTURED_HEADER, false)) {
			outputStructuredHeaderFooter(state, page.getFooter());
		}

		String sheetName = state.prepareSheetName();
		if (sheetName != null) {
			log.debug("Attempting to name sheet ", (state.getWb().getNumberOfSheets() - 1), " \"", sheetName, "\" ");
			int existingSheetIndex = -1;
			for (int i = 0; i < state.getWb().getNumberOfSheets() - 1; ++i) {
				if (state.getWb().getSheetName(i).equals(sheetName)) {
					log.debug("Found matching sheet at ", i, " \"", state.getWb().getSheetName(i), "\"");
					existingSheetIndex = i;
					break;
				}
			}
			if (existingSheetIndex >= 0) {
				log.debug("Deleting sheet at ", existingSheetIndex, " \"",
						state.getWb().getSheetName(existingSheetIndex), "\"");
				state.getWb().removeSheetAt(existingSheetIndex);
			}
			state.getWb().setSheetName(state.getWb().getNumberOfSheets() - 1, sheetName);
			if (existingSheetIndex >= 0) {
				state.getWb().setSheetOrder(sheetName, existingSheetIndex);
			}
			state.sheetName = null;
		}
		if (state.sheetPassword != null) {
			log.debug("Attempting to protect sheet ", (state.getWb().getNumberOfSheets() - 1));
			state.currentSheet.protectSheet(state.sheetPassword);
			state.sheetPassword = null;
		}

		Drawing<?> drawing = null;
		if (!state.images.isEmpty()) {
			drawing = state.currentSheet.createDrawingPatriarch();
		}

		boolean scaleSmallImage = EmitterServices.booleanOption(state.getRenderOptions(), page,
				ExcelEmitter.IMAGE_SCALING_CELL_DIMENSION, false);

		// pre-processing to calculate the row height based on the images
		for (CellImage cellImage : state.images) {
			processCellImage(state, drawing, cellImage, scaleSmallImage, false);
		}
		// draw-processing of the images
		for (CellImage cellImage : state.images) {
			processCellImage(state, drawing, cellImage, scaleSmallImage, true);
		}
		state.images.clear();
		state.rowNum = 0;
		state.colNum = 0;
		state.clearRowSpans();
		state.areaBorders.clear();

		state.currentSheet = null;
	}

	private CellRangeAddress getMergedRegionBegunBy(Sheet sheet, int row, int col) {
		for (int i = 0; i < sheet.getNumMergedRegions(); ++i) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			if ((range.getFirstColumn() == col) && (range.getFirstRow() == row)) {
				return range;
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Process a CellImage from the images list and place the image on the sheet.
	 * </p>
	 * <p>
	 * This involves changing the row height as necesssary and determining the
	 * column spread of the image.
	 * </p>
	 *
	 * @param cellImage The image to be placed on the sheet.
	 */
	private void processCellImage(HandlerState state, Drawing<?> drawing, CellImage cellImage,
			boolean scaleSmallImage, boolean drawImage) {
		Coordinate location = cellImage.location;

		Cell cell = state.currentSheet.getRow(location.getRow()).getCell(location.getCol());

		IImageContent image = cellImage.image;

		StyleManagerUtils smu = state.getSmu();
		float ptHeight = cell.getRow().getHeightInPoints();
		if (image.getHeight() != null) {
			ptHeight = smu.fontSizeInPoints(image.getHeight().toString());
		}

		// Get image width
		int endCol = cell.getColumnIndex();
		double lastColWidth = ClientAnchorConversions
				.widthUnits2Millimetres(state.currentSheet.getColumnWidth(endCol)) + 2.0;
		int dx = smu.anchorDxFromMM(lastColWidth, lastColWidth);
		double mmWidth = 0.0;
		if (smu.isAbsolute(image.getWidth())) {
			mmWidth = image.getWidth().convertTo(DimensionType.UNITS_MM);
		} else if (smu.isPixels(image.getWidth())) {
			mmWidth = ClientAnchorConversions.pixels2Millimetres(image.getWidth().getMeasure());
		}

		// Allow image to span multiple columns
		CellRangeAddress mergedRegion = getMergedRegionBegunBy(state.currentSheet, location.getRow(),
				location.getCol());
		if ((cellImage.spanColumns) || (mergedRegion != null)) {
			log.debug("Image size: ", image.getWidth(), " translates as mmWidth = ", mmWidth);
			if (mmWidth > 0) {
				double mmAccumulatedWidth = 0;
				int endColLimit = cellImage.spanColumns ? 256 : mergedRegion.getLastColumn();
				for (endCol = cell.getColumnIndex(); mmAccumulatedWidth < mmWidth && endCol < endColLimit; ++endCol) {
					lastColWidth = ClientAnchorConversions
							.widthUnits2Millimetres(state.currentSheet.getColumnWidth(endCol)) + 2.0;
					mmAccumulatedWidth += lastColWidth;
					log.debug("lastColWidth = ", lastColWidth, "; mmAccumulatedWidth = ", mmAccumulatedWidth);
				}
				if (mmAccumulatedWidth > mmWidth) {
					mmAccumulatedWidth -= lastColWidth;
					--endCol;
					double mmShort = mmWidth - mmAccumulatedWidth;
					dx = smu.anchorDxFromMM(mmShort, lastColWidth);
				}
			}
		} else {
			float widthRatio = (float) (mmWidth / lastColWidth);

			// scale the image to cell if the image dimension are larger like cell dimension
			if (scaleSmallImage) {
				ptHeight = ptHeight / widthRatio;
			} else {
				// avoid scaling for small images only resize of large images
				if (widthRatio > 1.0) {
					ptHeight = ptHeight / widthRatio;
				} else {
					dx = smu.anchorDxFromMM(mmWidth, lastColWidth);
				}
			}
		}

		int rowsSpanned = state.findRowsSpanned(cell.getRowIndex(), cell.getColumnIndex());
		float neededRowHeightPoints = ptHeight;

		for (int i = 0; i < rowsSpanned; ++i) {
			int rowIndex = cell.getRowIndex() + 1 + i;
			neededRowHeightPoints -= state.currentSheet.getRow(rowIndex).getHeightInPoints();
		}

		if (neededRowHeightPoints > cell.getRow().getHeightInPoints()) {
			cell.getRow().setHeightInPoints(neededRowHeightPoints);
		}

		if (drawImage) {
			int rowHeight = smu.anchorDyFromPoints(cell.getRow().getHeightInPoints(),
					cell.getRow().getHeightInPoints());
			int imageHeight = smu.anchorDyFromPoints(ptHeight, cell.getRow().getHeightInPoints());

			// vertical alignment, top - default
			int dy1 = 0;
			int dy2 = smu.anchorDyFromPoints(ptHeight, cell.getRow().getHeightInPoints());

			if (cellImage.verticalAlignment != null) {
				int moveY = (rowHeight - imageHeight);
				if (cellImage.verticalAlignment.equals(CSSConstants.CSS_MIDDLE_VALUE)) {
					// vertical alignment, middle - half of empty area added at the top
					moveY = moveY / 2;
					dy1 += moveY;
					dy2 += moveY;
				} else if (cellImage.verticalAlignment.equals(CSSConstants.CSS_BOTTOM_VALUE)) {
					// vertical alignment, bottom - full empty area added at the top
					dy1 += moveY;
					dy2 += moveY;
				}
			}
			int imageWidth = smu.anchorDxFromMM(mmWidth, mmWidth);
			int colWidth = smu.anchorDxFromMM(lastColWidth, lastColWidth);

			// horizontal alignment, left - default
			int dx1 = 0;
			int dx2 = dx;

			if (cellImage.horizontalAlignment != null) {
				int moveX = (colWidth - imageWidth);
				if (cellImage.horizontalAlignment.equals(CSSConstants.CSS_CENTER_VALUE)) {
					// horizontal alignment, center - half of empty area added at left hand side
					moveX = moveX / 2;
					dx1 += moveX;
					dx2 += moveX;
				} else if (cellImage.horizontalAlignment.equals(CSSConstants.CSS_RIGHT_VALUE)) {
					// horizontal alignment, right - full empty area added at left hand side
					dx1 += moveX;
					dx2 += moveX;
				}
			}

			// ClientAnchor anchor = wb.getCreationHelper().createClientAnchor();
			ClientAnchor anchor = state.getWb().getCreationHelper().createClientAnchor();
			anchor.setCol1(cell.getColumnIndex());
			anchor.setRow1(cell.getRowIndex());
			anchor.setCol2(endCol);
			anchor.setRow2(cell.getRowIndex() + rowsSpanned);
			anchor.setDx1(dx1);
			anchor.setDx2(dx2);
			anchor.setDy1(dy1);
			anchor.setDy2(dy2);
			anchor.setAnchorType(AnchorType.MOVE_DONT_RESIZE /* ClientAnchor.MOVE_DONT_RESIZE */);
			drawing.createPicture(anchor, cellImage.imageIdx);
		}

	}

	@Override
	public void startList(HandlerState state, IListContent list) throws BirtException {
		state.setHandler(new TopLevelListHandler(log, this, list));
		state.getHandler().startList(state, list);
	}

	@Override
	public void startTable(HandlerState state, ITableContent table) throws BirtException {
		state.setHandler(new TopLevelTableHandler(log, this, table));
		state.getHandler().startTable(state, table);
	}

	@Override
	public void emitText(HandlerState state, ITextContent text) throws BirtException {
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitText(state, text);
	}

	@Override
	public void emitData(HandlerState state, IDataContent data) throws BirtException {
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitData(state, data);
	}

	@Override
	public void emitLabel(HandlerState state, ILabelContent label) throws BirtException {
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitLabel(state, label);
	}

	@Override
	public void emitAutoText(HandlerState state, IAutoTextContent autoText) throws BirtException {
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitAutoText(state, autoText);
	}

	@Override
	public void emitForeign(HandlerState state, IForeignContent foreign) throws BirtException {
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitForeign(state, foreign);
	}

	@Override
	public void emitImage(HandlerState state, IImageContent image) throws BirtException {
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitImage(state, image);
	}

}
