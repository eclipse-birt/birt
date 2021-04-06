/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.table.LayoutHelper;
import org.eclipse.birt.report.model.api.elements.table.LayoutTable;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.validators.InconsistentColumnsValidator;
import org.eclipse.birt.report.model.api.validators.TableHeaderContextContainmentValidator;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableColumnModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableItemModel;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;

/**
 * This class represents a table in design.A table is a list that is structured
 * into a rows and columns.The columns are defined for the entire table. Rows
 * are created in response to the same events as for a list.Like a list, a table
 * is defined by a series of bands. A table defines the same bands as a list.
 * Like a list, each band is divided into a number of sections. Each section
 * contains one or more rows. Each row is further divided into a set of cells.
 * 
 */

public class TableItem extends ListingElement implements ITableItemModel {

	/**
	 * The table model.
	 */

	private LayoutTable table = null;

	/**
	 * Caches the column, the key is the cell id and the value is the column where
	 * the cell locates in.
	 */
	private Map<Long, TableColumn> cachedColumn = null;

	/**
	 * Default constructor.
	 */

	public TableItem() {
		super();
	}

	/**
	 * Constructs the table item with an optional name.
	 * 
	 * @param theName the optional name of the table
	 */

	public TableItem(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitTable(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.TABLE_ITEM;
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
	 * @param module the module
	 * @return an API handle for this element.
	 */

	public TableHandle handle(Module module) {
		if (handle == null) {
			handle = new TableHandle(module, this);
		}
		return (TableHandle) handle;
	}

	/**
	 * Computes the number of columns in the table. The number is defined as 1) the
	 * sum of columns describe in the Columns slot, or 2) the widest row defined in
	 * the other slots.
	 * 
	 * @param module the module
	 * @return the number of columns in the table
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
	 * Computes the maximum column count in the table.
	 * 
	 * @param module the module
	 * @return the maximum column count in the table
	 */

	public int findMaxCols(Module module) {
		if (table == null)
			refreshRenderModel(module);

		return table.getColumnCount();
	}

	/**
	 * Gets the number of columns described in the column definition section.
	 * 
	 * @param module the module
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

	/**
	 * Returns the column number with a specified <code>Cell</code>.
	 * 
	 * @param module the module
	 * @param target the cell to find
	 * @return 1-based the column number
	 */

	public int getColumnPosition4Cell(Module module, Cell target) {
		if (target == null)
			return 0;

		if (table == null)
			refreshRenderModel(module);

		int slotId = target.getContainer().getContainerInfo().getSlotID();

		TableRow row = (TableRow) target.getContainer();
		DesignElement grandPa = row.getContainer();
		int rowId = grandPa.getSlot(slotId).findPosn(row);

		if (grandPa instanceof TableItem) {
			assert grandPa == this;
			return table.getColumnPos(slotId, rowId, target);
		}

		return table.getColumnPos(((TableGroup) grandPa).getGroupLevel(), slotId, rowId, target);
	}

	/**
	 * Gets column in table item according to the cell.
	 * 
	 * @param module     the module.
	 * @param columnSlot the column slot.
	 * @param target     the cell.
	 * @return the column.
	 */
	public TableColumn getColumn(Module module, ContainerSlot columnSlot, Cell target) {

		// in engine mode, we should cache the relationship between the cell
		// and column using cell id as key for performance issue. as for the UI
		// mode, we should avoid to cache.

		if (module.isCached()) {
			if (cachedColumn == null)
				return null;

			return cachedColumn.get(Long.valueOf(target.getID()));

		}

		int columnNum = getColumnNum(module, target);

		// if the layout still not updated yet.

		if (columnNum == 0)
			return null;

		return ColumnHelper.findColumn(module, columnSlot, columnNum);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ReportItem#cacheValues()
	 */
	public void cacheValues() {

		ContainerSlot columnSlot = getSlot(ITableItemModel.COLUMN_SLOT);
		if (columnSlot.getCount() == 0)
			return;

		Module module = getRoot();
		cachedColumn = new HashMap<Long, TableColumn>();

		// The array which caches the table column in the table, if the column
		// repeat this array will record it accordingly.

		TableColumn[] cachedColumnArray = ColumnHelper.getTableColumnArray(module, columnSlot);

		ContainerSlot slot = getSlot(IListingElementModel.HEADER_SLOT);
		List<Cell> cellList = CellHelper.getCells(slot);

		slot = getSlot(IListingElementModel.DETAIL_SLOT);
		cellList.addAll(CellHelper.getCells(slot));

		slot = getSlot(IListingElementModel.FOOTER_SLOT);
		cellList.addAll(CellHelper.getCells(slot));

		slot = getSlot(IListingElementModel.GROUP_SLOT);
		cellList.addAll(CellHelper.getCellsInTableGroup(slot));

		for (int i = 0; i < cellList.size(); i++) {

			Cell cell = cellList.get(i);

			int columnNum = getColumnNum(module, cell);

			// if the layout still not updated yet.

			if (columnNum == 0)
				continue;

			TableColumn column = ColumnHelper.getColumnInArray(cachedColumnArray, columnNum);

			// if the column could be found using the column number of the cell,
			// then caches.
			if (column != null) {
				cachedColumn.put(Long.valueOf(cell.getID()), column);
			}

		}

	}

	public List validate(Module module) {
		List list = super.validate(module);

		// If column definitions are defined, then they must describe the
		// number of columns actually used by the table. It is legal to
		// have a table with zero columns.

		list.addAll(InconsistentColumnsValidator.getInstance().validate(module, this));

		// Check table's slot context containment.

		list.addAll(TableHeaderContextContainmentValidator.getInstance().validate(module, this));

		return list;
	}

	/**
	 * Returns the table model of <code>TableItem</code>. This model is different
	 * from the natural of <code>TableItem</code> since "colSpan", "rowSpan" and
	 * "dropping cells" are applied. Mainly uses this model to render the
	 * <code>TableItem</code>.
	 * 
	 * @param module the module
	 * @return the table model for rendering
	 */

	public LayoutTable getLayoutModel(Module module) {
		if (table == null)
			refreshRenderModel(module);

		return table;
	}

	/**
	 * Refreshes the table model of <code>TableItem</code>.
	 * 
	 * @param module the module
	 */

	public void refreshRenderModel(Module module) {
		table = LayoutHelper.applyLayout(module, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#checkContent(org.eclipse
	 * .birt.report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement, int,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List checkContent(Module module, ContainerContext containerInfo, DesignElement content) {
		List errors = super.checkContent(module, containerInfo, content);
		if (!errors.isEmpty())
			return errors;

		errors.addAll(
				TableHeaderContextContainmentValidator.getInstance().validateForAdding(module, containerInfo, content));

		return errors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#checkContent(org.eclipse
	 * .birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.ContainerInfo,
	 * org.eclipse.birt.report.model.api.metadata.IElementDefn)
	 */
	public List checkContent(Module module, ContainerContext containerInfo, IElementDefn defn) {
		List errors = super.checkContent(module, containerInfo, defn);
		if (!errors.isEmpty())
			return errors;

		errors.addAll(TableHeaderContextContainmentValidator.getInstance().validateForAdding(module,
				containerInfo.getElement(), defn));

		return errors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ListingElement#clone()
	 */

	public Object doClone(CopyPolicy policy) throws CloneNotSupportedException {
		TableItem clonedTable = (TableItem) super.doClone(policy);
		clonedTable.refreshRenderModel(table.getTable().getModule());
		clonedTable.cachedColumn = null;

		return clonedTable;
	}

	/**
	 * Gets the column number of the cell.
	 * 
	 * @param module the module.
	 * @param cell   the cell.
	 * @return the column number of the cell.
	 */
	private int getColumnNum(Module module, Cell cell) {
		int columnNum = cell.getColumn(module);
		if (columnNum == 0)
			columnNum = getColumnPosition4Cell(module, cell);
		return columnNum;
	}

}