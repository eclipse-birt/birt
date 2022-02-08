/*******************************************************************************
 * Copyright (c) 2014 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  Krzysztof Kazmierczyk (IBM) - [423106] text wrapping problem in merged cells
 *******************************************************************************/

package org.eclipse.birt.report.model.elements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.validators.InconsistentColumnsValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IGridItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElement;
import org.eclipse.birt.report.model.elements.interfaces.ITableColumnModel;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;

/**
 * This class represents a Grid item. A grid item contains a set of report
 * items, but the items are arranged into a grid. Each cell in the grid can
 * contain a single item. However, the user can easily add multiple items by
 * placing a container into a cell, and placing other items into the container.
 * Grid layout is familiar to anyone who has used HTML tables, Word tables or
 * Excel: data is divided into a series of rows and columns. Items can span both
 * rows and columns. The grid layout is ideal for many simple reports and
 * dashboards. Grids help align report items for a clean layout. The grid
 * section is divided into rows and columns. Rows grow or shrink depending on
 * content. Columns ensure that items line up vertically. Columns can also grow
 * or shrink depending on their contents. The following terminology applies to
 * grids:
 * 
 * <p>
 * <dl>
 * <dt><strong>Grid </strong></dt>
 * <dd>a tabular layout with a fixed set of columns and variable number of rows.
 * (Contrast this with a matrix that can have a variable number of
 * columns.)</dd>
 * 
 * <dt><strong>Column </strong></dt>
 * <dd>a vertical slice though the grid. Columns help organize the layout, but
 * do not represent a specific bit of data as they do in matrices.</dd>
 * 
 * <dt><strong>Row </strong></dt>
 * <dd>a horizontal slice through the grid.</dd>
 * 
 * <dt><strong>Cell </strong></dt>
 * <dd>a point at which a row and column intersect. A cell can span rows and
 * columns.</dd>
 * </dl>
 * <p>
 * 
 * The grid layout is ideal for reports that will be exported to Excel or shown
 * on the web. The grid layout can be applied to a list to align column headings
 * with detail rows. It can be applied to a dashboard to create a clean,
 * organized layout. It can also be applied to the entire report to align data
 * in a group of dashboards and lists. Use the
 * {@link org.eclipse.birt.report.model.api.GridHandle}class to set a number of
 * properties for the grid as a whole.
 * 
 * <p>
 * <dl>
 * <dt><strong>Style </strong></dt>
 * <dd>The style defines the font to use within grid cells, the border style for
 * the grid, fill color, and so on.</dd>
 * 
 * <dt><strong>Fixed or variable size </strong></dt>
 * <dd>A grid will normally adjust based on the available space on the page.
 * When viewed on the web, the grid columns will expand to make use of the full
 * width of the browser window in the expected way.</dd>
 * 
 * <dt><strong>Row alignment </strong></dt>
 * <dd>how to align items within a row. Options are top, middle, bottom or
 * baseline.</dd>
 * </dl>
 * 
 */

public class GridItem extends ReportItem implements IGridItemModel, ISupportThemeElement {

	/**
	 * Caches the column, the key is the cell id and the value is the column where
	 * the cell locates in.
	 */
	private Map<Long, TableColumn> cachedColumn = null;

	/**
	 * Default Constructor.
	 */

	public GridItem() {
		super();
		initSlots();
	}

	/**
	 * Constructs the grid with the name for it.
	 * 
	 * @param theName the optional name of the grid
	 */

	public GridItem(String theName) {
		super(theName);
		initSlots();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitGrid(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.GRID_ITEM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the report design for the grid
	 * 
	 * @return an API handle for this element.
	 */

	public GridHandle handle(Module module) {
		if (handle == null) {
			handle = new GridHandle(module, this);
		}
		return (GridHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot(int slot) {
		assert (slot >= 0 && slot < SLOT_COUNT);
		return slots[slot];
	}

	/**
	 * Computes the number of columns in the Grid. The number is defined as the sum
	 * of columns describe in the Columns slot.
	 * 
	 * @param module the report design
	 * @return the number of columns in the Grid
	 */

	public int getColumnCount(Module module) {
		// Method 1: sum columns in the column slot.

		int colCount = getColDefnCount(module);
		if (colCount != 0)
			return colCount;

		// Method 2: find the widest row.

		return findMaxCols(module);
	}

	/**
	 * Gets the number of columns described in the column definition section.
	 * 
	 * @param module the report design
	 * @return the number of columns described by column definitions
	 */

	public int getColDefnCount(Module module) {
		int colCount = 0;
		ContainerSlot cols = getSlot(COLUMN_SLOT);
		int colDefnCount = cols.getCount();
		for (int i = 0; i < colDefnCount; i++) {
			TableColumn col = (TableColumn) cols.getContent(i);
			colCount += col.getIntProperty(module, ITableColumnModel.REPEAT_PROP);
		}
		return colCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ReportItem#cacheValues()
	 */
	public void cacheValues() {
		ContainerSlot columnSlot = getSlot(IGridItemModel.COLUMN_SLOT);
		if (columnSlot.getCount() == 0)
			return;

		Module module = getRoot();
		cachedColumn = new HashMap<Long, TableColumn>();

		// The array which caches the table column in the grid, if the column
		// repeat this array will record accordingly.

		TableColumn[] cachedColumnArray = ColumnHelper.getTableColumnArray(module, columnSlot);

		ContainerSlot rowSlot = getSlot(IGridItemModel.ROW_SLOT);

		List<Cell> list = CellHelper.getCells(rowSlot);

		for (int i = 0; i < list.size(); i++) {
			Cell cell = list.get(i);

			int columnNum = getCellPositionInColumn(module, cell);

			assert columnNum > 0;

			TableColumn column = ColumnHelper.getColumnInArray(cachedColumnArray, columnNum);

			// if the column could be found accroding to the column number of
			// the cell,
			// then cache it.

			if (column != null) {
				cachedColumn.put(Long.valueOf(cell.getID()), column);

			}

		}
	}

	/**
	 * Finds the maximum column width for this grid.
	 * 
	 * @param module the report design
	 * @return the maximum number of columns
	 */

	public int findMaxCols(Module module) {
		ContainerSlot rows = getSlot(ROW_SLOT);
		int maxCols = 0;
		int count = rows.getCount();
		for (int i = 0; i < count; i++) {
			TableRow row = (TableRow) rows.getContent(i);
			int cols = row.getColumnCount(module);
			if (cols > maxCols)
				maxCols = cols;
		}
		return maxCols;
	}

	/**
	 * Gets column in grid item according to the cell.
	 * 
	 * @param module     the module.
	 * @param columnSlot the column slot.
	 * @param target     the cell.
	 * @return the column.
	 */
	public TableColumn getColumn(Module module, ContainerSlot columnSlot, Cell target) {
		// in preview mode, we should cache the relationship between the cell
		// and column using cell id and column number as key for performance
		// issue. as for the UI mode, we should avoid to cache.

		if (module.isCached()) {
			if (cachedColumn == null)
				return null;

			return cachedColumn.get(Long.valueOf(target.getID()));

		}
		int columnNum = getCellPositionInColumn(module, target);

		assert columnNum > 0;

		return ColumnHelper.findColumn(module, columnSlot, columnNum);

	}

	/**
	 * Returns the column number for the cell that has no "column" property defined.
	 * 
	 * @param module the report design
	 * @param target the cell to find
	 * 
	 * @return the column position
	 */

	public int getCellPositionInColumn(Module module, Cell target) {
		int pos = target.getColumn(module);
		if (pos > 0)
			return pos;

		// the first column is 1.

		pos = 1;

		TableRow row = (TableRow) target.getContainer();
		List<DesignElement> list = row.getContentsSlot();

		for (Iterator<DesignElement> iter = list.iterator(); iter.hasNext();) {
			Cell cell = (Cell) iter.next();
			int cellPos = cell.getColumn(module);
			if (cellPos > 0)
				pos = cellPos;

			if (cell == target)
				break;

			pos = pos + cell.getColSpan(module);

		}

		// calculating spanned rows - see eclipse bug 423106
		int cellRowNum = getRowNumber(row);
		ContainerSlot rows = getSlot(ROW_SLOT);
		for (int currRowNum = 0; currRowNum < rows.getCount(); currRowNum++) {
			TableRow currRow = (TableRow) rows.getContent(currRowNum);
			if (row == currRow)
				break;
			List<DesignElement> cells = currRow.getContentsSlot();
			if (cells == null)
				continue;

			int maxSize = Math.min(pos, cells.size());
			for (int j = 0; j < maxSize; j++) {
				Cell cell = (Cell) cells.get(j);
				if (cell.getRowSpan(module) + currRowNum >= cellRowNum)
					pos++;
			}
		}

		return pos;
	}

	/**
	 * 
	 * Returns the number of the row in a grid
	 * 
	 * @param row row to find
	 * @return row position
	 */
	private int getRowNumber(TableRow row) {
		ContainerSlot rows = getSlot(ROW_SLOT);
		for (int i = 0; i < rows.getCount(); i++) {
			TableRow curr = (TableRow) rows.getContent(i);
			if (curr == row)
				return ++i;
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	public List<SemanticException> validate(Module module) {
		List<SemanticException> list = super.validate(module);

		list.addAll(InconsistentColumnsValidator.getInstance().validate(module, this));

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getDisplayLabel(org.
	 * eclipse.birt.report.model.elements.ReportDesign, int)
	 */

	public String getDisplayLabel(Module module, int level) {
		String displayLabel = super.getDisplayLabel(module, level);
		if (level == IDesignElementModel.FULL_LABEL) {
			GridHandle handle = handle(module);
			int rows = handle.getRows().getCount();
			int cols = handle.getColumns().getCount();
			displayLabel += "(" + rows + " x " + cols + ")"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		}
		return displayLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ReferencableStyledElement#doClone(
	 * org.eclipse.birt.report.model.elements.strategy.CopyPolicy)
	 */

	public Object doClone(CopyPolicy policy) throws CloneNotSupportedException {
		GridItem clonedGrid = (GridItem) super.doClone(policy);
		clonedGrid.cachedColumn = null;

		return clonedGrid;
	}

}
