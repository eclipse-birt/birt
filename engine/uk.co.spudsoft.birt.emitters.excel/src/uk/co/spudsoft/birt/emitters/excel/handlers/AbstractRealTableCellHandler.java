/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013, 2024, 2025 James Talbut and others
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;

import uk.co.spudsoft.birt.emitters.excel.Area;
import uk.co.spudsoft.birt.emitters.excel.Coordinate;
import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

/**
 * Abstract real table cell handler
 *
 * @since 3.3
 *
 */
public class AbstractRealTableCellHandler extends CellContentHandler {

	protected int column;
	private AbstractRealTableRowHandler parentRow;
	private boolean containsTable;

	/**
	 * Constructor
	 *
	 * @param emitter content emitter
	 * @param log     log object
	 * @param parent  parent handler
	 * @param cell    cell content
	 */
	public AbstractRealTableCellHandler(IContentEmitter emitter, Logger log, IHandler parent, ICellContent cell) {
		super(emitter, log, parent, cell);
		column = cell.getColumn();
	}

	@Override
	public void notifyHandler(HandlerState state) {
		if (parentRow != null) {
			parentRow.resumeRow(state);
			resumeCell(state);
			parentRow = null;
		}
	}

	@Override
	public void startCell(HandlerState state, ICellContent cell) throws BirtException {
		log.debug("Cell - " + "BIRT[" + cell.getRow()
				+ (cell.getRowSpan() > 1 ? "-" + (cell.getRow() + cell.getRowSpan() - 1) : "") + "," + cell.getColumn()
				+ (cell.getColSpan() > 1 ? "-" + (cell.getColumn() + cell.getColSpan() - 1) : "") + "]" + " state["
				+ state.rowNum + "," + state.colNum + "]");
		resumeCell(state);
	}

	@Override
	public void endCell(HandlerState state, ICellContent cell) throws BirtException {
		if (cell.getBookmark() != null) {
			createName(state, prepareName(cell.getBookmark()), state.rowNum, state.colNum, state.rowNum, state.colNum);
		}

		interruptCell(state, !containsTable);
	}

	/**
	 * Resume cell
	 *
	 * @param state handler state
	 */
	public void resumeCell(HandlerState state) {
	}

	/**
	 * Interrupt cell
	 *
	 * @param state             handler state
	 * @param includeFormatOnly include format only
	 * @throws BirtException
	 */
	public void interruptCell(HandlerState state, boolean includeFormatOnly) throws BirtException {

		if (state == null) {
			System.err.println("state == null");
		} else if (state.currentSheet == null) {
			System.err.println("state.currentSheet == null");
		} else if (state.currentSheet.getRow(state.rowNum) == null) {
			System.err.println("state.currentSheet.getRow(" + state.rowNum + ") == null");
		}

		if ((lastValue != null) || includeFormatOnly) {
			Cell currentCell = state.currentSheet.getRow(state.rowNum).getCell(column);
			if (currentCell == null) {
				log.debug("Creating cell[", state.rowNum, ",", column, "]");
				currentCell = state.currentSheet.getRow(state.rowNum).createCell(column);
			}

			ICellContent cell = (ICellContent) element;
			Area area = null;

			if ((cell.getColSpan() > 1) || (cell.getRowSpan() > 1)) {

				int endRow = state.rowNum + cell.getRowSpan() - 1;
				int endCol = state.colNum + cell.getColSpan() - 1;

				if (cell.getRowSpan() > 1) {
					log.debug("Adding row span [", state.rowNum, ",", state.colNum, "] to [", endRow, ",", endCol, "]");
					area = state.addRowSpan(state.rowNum, state.colNum, endRow, endCol);
				}

				int offset = state.computeNumberSpanBefore(state.rowNum, state.colNum);
				log.debug("Offset for [", state.rowNum, ",", state.colNum, "] calculated as ", offset);
				log.debug("Merging [", state.rowNum, ",", state.colNum + offset, "] to [", endRow, ",", endCol + offset,
						"]");
				log.debug("Should be merging ? [", state.rowNum, ",", column, "] to [", endRow, ",",
						column + cell.getColSpan() - 1, "]");
				CellRangeAddress newMergedRegion = new CellRangeAddress(state.rowNum, endRow, column,
						column + cell.getColSpan() - 1);

				// excel merge region, avoid registration of overlapped merge regions
				Boolean newAddressRange = true;
				for (CellRangeAddress registeredMergedRegion : state.currentSheet.getMergedRegions()) {
					if (newMergedRegion.getFirstColumn() >= registeredMergedRegion.getFirstColumn()
							&& newMergedRegion.getFirstColumn() <= registeredMergedRegion.getLastColumn()
							&& newMergedRegion.getFirstRow() >= registeredMergedRegion.getFirstRow()
							&& newMergedRegion.getFirstRow() <= registeredMergedRegion.getLastRow()
							|| registeredMergedRegion.getFirstRow() == newMergedRegion.getFirstRow()
									&& registeredMergedRegion.getFirstColumn() == newMergedRegion.getFirstColumn()
									&& registeredMergedRegion.getLastRow() == newMergedRegion.getLastRow()
									&& registeredMergedRegion.getLastColumn() == newMergedRegion.getLastColumn()) {
						newAddressRange = false;
						break;
					}
				}
				if (newAddressRange) {
					try {
						state.currentSheet.addMergedRegion(newMergedRegion);
					} catch (IllegalStateException ise) {
						log.error(0,
								"Error of merged regions: " + ise.getLocalizedMessage(),
								ise);
					}
				}

				colSpan = cell.getColSpan();
			}

			endCellContent(state, cell, lastElement, currentCell, area);
		}

		if (state.cellIsMergedWithBorders(state.rowNum, column)) {
			int absoluteColumn = column;
			++state.colNum;
			--colSpan;
			while (colSpan > 0) {
				++absoluteColumn;
				log.debug("Creating cell[", state.rowNum, ",", absoluteColumn, "]");
				Cell currentCell = state.currentSheet.getRow(state.rowNum).createCell(absoluteColumn);
				endCellContent(state, null, null, currentCell, null);
				++state.colNum;
				--colSpan;
			}
		} else {
			state.colNum += colSpan;
		}

		state.setHandler(parent);
	}

	@Override
	public void startContainer(HandlerState state, IContainerContent container) throws BirtException {
		// log.debug( "Container display = " + getStyleProperty( container,
		// StyleConstants.STYLE_DISPLAY, "block") );
		if (!"inline".equals(getStyleProperty(container, StyleConstants.STYLE_DISPLAY, "block"))) {
			lastCellContentsWasBlock = true;
		}
	}

	@Override
	public void endContainer(HandlerState state, IContainerContent container) throws BirtException {
		// log.debug( "Container display = " + getStyleProperty( container,
		// StyleConstants.STYLE_DISPLAY, "block") );
		if (!"inline".equals(getStyleProperty(container, StyleConstants.STYLE_DISPLAY, "block"))) {
			lastCellContentsWasBlock = true;
		}
	}

	private TableContent cellDesignsTableContent() {
		IElement current = element;
		while ((current != null) && !(current instanceof TableContent)) {
			current = current.getParent();
		}
		return (TableContent) current;
	}

	@Override
	public void startTable(HandlerState state, ITableContent table) throws BirtException {
		int colSpan = ((ICellContent) element).getColSpan();
		int rowSpan = ((ICellContent) element).getRowSpan();
		ITableHandler tableHandler = getAncestor(ITableHandler.class);
		TableContent myTableContent = cellDesignsTableContent();

		if ((tableHandler != null) && (tableHandler.getColumnCount() == colSpan)
				&& table.getParent() instanceof CellContent
				&& (1 == ((CellDesign) ((CellContent) table.getParent()).getGenerateBy()).getContentCount())) {
			// Parent row contains only one item

			containsTable = true;
			parentRow = getAncestor(AbstractRealTableRowHandler.class);
			interruptCell(state, false);
			parentRow.interruptRow(state);

			state.setHandler(new NestedTableHandler(log, this, table, rowSpan));
			state.getHandler().startTable(state, table);
		} else if ((tableHandler != null) && (myTableContent != null) && (table.getParent() instanceof CellContent)
				&& (((CellContent) table.getParent()).getGenerateBy() instanceof CellDesign)
				&& (((CellDesign) ((CellContent) table.getParent()).getGenerateBy()).getColumn() + 1 == myTableContent
						.getColumnCount())
				&& EmitterServices.booleanOption(state.getRenderOptions(), table, ExcelEmitter.NEST_TABLE_IN_LAST_CELL,
						false)) {
			// This is last cell in row

			colSpan = table.getColumnCount();
			containsTable = true;
			parentRow = getAncestor(AbstractRealTableRowHandler.class);
			interruptCell(state, false);
			// parentRow.interruptRow(state);

			state.setHandler(new NestedTableHandler(log, this, table, rowSpan));
			state.getHandler().startTable(state, table);
		} else if ((tableHandler != null) && (table.getColumnCount() <= colSpan)) {
			// This cell is merged over same number of columns as new table

			containsTable = true;
			parentRow = getAncestor(AbstractRealTableRowHandler.class);
			interruptCell(state, false);
			removeMergedCell(state, state.rowNum, state.colNum);

			NestedTableHandler nestedTableHandler = new NestedTableHandler(log, this, table, rowSpan);
			nestedTableHandler.setInserted(true);
			state.setHandler(nestedTableHandler);

			state.getHandler().startTable(state, table);
		} else {
			state.setHandler(new FlattenedTableHandler(this, log, this, table));
			state.getHandler().startTable(state, table);
		}
	}

	@Override
	public void startList(HandlerState state, IListContent list) throws BirtException {
		int colSpan = ((ICellContent) element).getColSpan();
		ITableHandler tableHandler = getAncestor(ITableHandler.class);
		if ((tableHandler != null) && (tableHandler.getColumnCount() == colSpan)
				&& list.getParent() instanceof CellContent
				&& (1 == ((CellDesign) ((CellContent) list.getParent()).getGenerateBy()).getContentCount())) {

			containsTable = true;
			parentRow = getAncestor(AbstractRealTableRowHandler.class);
			interruptCell(state, false);
			parentRow.interruptRow(state);

			state.colNum = column;
			state.setHandler(new NestedListHandler(log, this, list));
			state.getHandler().startList(state, list);
		} else {
			state.setHandler(new FlattenedListHandler(this, log, this, list));
			state.getHandler().startList(state, list);
		}
	}

	@Override
	public void emitText(HandlerState state, ITextContent text) throws BirtException {
		String textText = text.getText();
		log.debug("text:", textText);
		emitContent(state, text, textText,
				(!"inline".equals(getStyleProperty(text, StyleConstants.STYLE_DISPLAY, "block"))));
	}

	@Override
	public void emitData(HandlerState state, IDataContent data) throws BirtException {
		emitContent(state, data, data.getValue(),
				(!"inline".equals(getStyleProperty(data, StyleConstants.STYLE_DISPLAY, "block"))));
	}

	@Override
	public void emitLabel(HandlerState state, ILabelContent label) throws BirtException {
		// String labelText = ( label.getLabelText() != null ) ? label.getLabelText() :
		// label.getText();
		String labelText = (label.getText() != null) ? label.getText() : label.getLabelText();
		log.debug("labelText:", labelText);
		emitContent(state, label, labelText,
				(!"inline".equals(getStyleProperty(label, StyleConstants.STYLE_DISPLAY, "block"))));
	}

	@Override
	public void emitAutoText(HandlerState state, IAutoTextContent autoText) throws BirtException {
		emitContent(state, autoText, autoText.getText(),
				(!"inline".equals(getStyleProperty(autoText, StyleConstants.STYLE_DISPLAY, "block"))));
	}

	@Override
	public void emitForeign(HandlerState state, IForeignContent foreign) throws BirtException {

		log.debug("Handling foreign content of type ", foreign.getRawType());
		if (IForeignContent.HTML_TYPE.equalsIgnoreCase(foreign.getRawType())) {
			HTML2Content.html2Content(foreign);
			contentVisitor.visitChildren(foreign, null);
		}
	}

	@Override
	public void emitImage(HandlerState state, IImageContent image) throws BirtException {
		boolean imageCanSpan = false;

		int colSpan = ((ICellContent) element).getColSpan();
		ITableHandler tableHandler = getAncestor(ITableHandler.class);
		if ((tableHandler != null) && (tableHandler.getColumnCount() == colSpan)
				&& image.getParent() instanceof CellContent
				&& (1 == ((CellDesign) ((CellContent) image.getParent()).getGenerateBy()).getContentCount())) {
			imageCanSpan = true;
		}
		recordImage(state, new Coordinate(state.rowNum, column), image, imageCanSpan);
		lastElement = image;
	}

}
