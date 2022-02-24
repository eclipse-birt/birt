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
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;

import uk.co.spudsoft.birt.emitters.excel.CellImage;
import uk.co.spudsoft.birt.emitters.excel.ClientAnchorConversions;
import uk.co.spudsoft.birt.emitters.excel.Coordinate;
import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.StyleManagerUtils;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class PageHandler extends AbstractHandler {

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
		if ((printScale > 0) && (printScale < Short.MAX_VALUE)) {
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
		for (CellImage cellImage : state.images) {
			processCellImage(state, drawing, cellImage);
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
	private void processCellImage(HandlerState state, Drawing<?> drawing, CellImage cellImage) {
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
				.widthUnits2Millimetres((short) state.currentSheet.getColumnWidth(endCol)) + 2.0;
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
							.widthUnits2Millimetres((short) state.currentSheet.getColumnWidth(endCol)) + 2.0;
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
			ptHeight = ptHeight / widthRatio;
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

		// ClientAnchor anchor = wb.getCreationHelper().createClientAnchor();
		ClientAnchor anchor = state.getWb().getCreationHelper().createClientAnchor();
		anchor.setCol1(cell.getColumnIndex());
		anchor.setRow1(cell.getRowIndex());
		anchor.setCol2(endCol);
		anchor.setRow2(cell.getRowIndex() + rowsSpanned);
		anchor.setDx2(dx);
		anchor.setDy2(smu.anchorDyFromPoints(ptHeight, cell.getRow().getHeightInPoints()));
		anchor.setAnchorType(AnchorType.MOVE_DONT_RESIZE /* ClientAnchor.MOVE_DONT_RESIZE */);
		drawing.createPicture(anchor, cellImage.imageIdx);
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
