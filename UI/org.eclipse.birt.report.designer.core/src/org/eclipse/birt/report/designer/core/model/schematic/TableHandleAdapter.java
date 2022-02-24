/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.core.model.ITableAdapterHelper;
import org.eclipse.birt.report.designer.core.model.ReportItemtHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement CellHandleAdapter responds to model CellHandle
 * 
 */

public class TableHandleAdapter extends ReportItemtHandleAdapter {

	// private static Log log = LogFactory.getLog( TableHandleAdapter.class );
	private static final String TRANS_LABEL_INSERT_ROW = Messages.getString("TableHandleAdapter.transLabel.insertRow"); //$NON-NLS-1$
	private static final String NAME_NULL = ""; //$NON-NLS-1$
	private static final String NAME_DETAIL = Messages.getString("TableHandleAdapter.name.detail"); //$NON-NLS-1$
	private static final String NAME_FOOTER = Messages.getString("TableHandleAdapter.name.footer"); //$NON-NLS-1$
	private static final String NAME_HEADRER = Messages.getString("TableHandleAdapter.name.header"); //$NON-NLS-1$
	private static final String TRANS_LABEL_NOT_INCLUDE = Messages
			.getString("TableHandleAdapter.transLabel.notInclude"); //$NON-NLS-1$
	private static final String TRANS_LABEL_INCLUDE = Messages.getString("TableHandleAdapter.transLabel.include"); //$NON-NLS-1$
	private static final String TRANS_LABEL_INSERT_GROUP = Messages
			.getString("TableHandleAdapter.transLabel.insertGroup"); //$NON-NLS-1$
	private static final String TRANS_LABEL_SPLIT_CELLS = Messages
			.getString("TableHandleAdapter.transLabel.splitCells"); //$NON-NLS-1$
	private static final String TRANS_LABEL_DELETE_ROW = Messages.getString("TableHandleAdapter.transLabel.deleteRow"); //$NON-NLS-1$
	private static final String TRANS_LABEL_DELETE_ROWS = Messages
			.getString("TableHandleAdapter.transLabel.deleteRows"); //$NON-NLS-1$
	private static final String TRANS_LABEL_DELETE_COLUMN = Messages
			.getString("TableHandleAdapter.transLabel.deleteColumn"); //$NON-NLS-1$
	private static final String TRANS_LABEL_DELETE_COLUMNS = Messages
			.getString("TableHandleAdapter.transLabel.deleteColumns"); //$NON-NLS-1$
	private static final String TRANS_LABEL_INSERT_COLUMN = Messages
			.getString("TableHandleAdapter.transLabel.insertColumn"); //$NON-NLS-1$
	private static final String TRANS_LABEL_DELETE_GROUP = Messages
			.getString("TableHandleAdapter.transLable.deleteGroup"); //$NON-NLS-1$
	public static final int HEADER = TableHandle.HEADER_SLOT;
	public static final int DETAIL = TableHandle.DETAIL_SLOT;
	public static final int FOOTER = TableHandle.FOOTER_SLOT;

	public static final String TABLE_HEADER = "H"; //$NON-NLS-1$
	public static final String TABLE_FOOTER = "F"; //$NON-NLS-1$
	public static final String TABLE_DETAIL = "D"; //$NON-NLS-1$
	public static final String TABLE_GROUP_HEADER = "gh"; //$NON-NLS-1$
	public static final String TABLE_GROUP_FOOTER = "gf"; //$NON-NLS-1$

	public static final String DEFAULT_WIDTH = "100.0" //$NON-NLS-1$
			+ DesignChoiceConstants.UNITS_PERCENTAGE;
	/* the name model should support */
	private HashMap rowInfo = new HashMap();
	private List rows = new ArrayList();

	/**
	 * Constructor
	 * 
	 * @param table The handle of report item.
	 * 
	 * @param mark
	 */
	public TableHandleAdapter(ReportItemHandle table, IModelAdapterHelper mark) {
		super(table, mark);
	}

	/**
	 * Gets the Children list.
	 * 
	 * @return Children iterator
	 */
	public List getChildren() {
		List children = new ArrayList();

		SlotHandle header = getTableHandle().getHeader();

		for (Iterator headerIter = header.iterator(); headerIter.hasNext();) {
			children.addAll(((RowHandle) headerIter.next()).getCells().getContents());
		}

		SlotHandle group = getTableHandle().getGroups();

		for (Iterator groupIter = group.iterator(); groupIter.hasNext();) {
			TableGroupHandle tableGroups = (TableGroupHandle) groupIter.next();
			SlotHandle groupHeaders = tableGroups.getHeader();
			for (Iterator groupHeaderIter = groupHeaders.iterator(); groupHeaderIter.hasNext();) {
				children.addAll(((RowHandle) groupHeaderIter.next()).getCells().getContents());
			}
		}

		SlotHandle detail = getTableHandle().getDetail();

		for (Iterator detailIter = detail.iterator(); detailIter.hasNext();) {
			children.addAll(((RowHandle) detailIter.next()).getCells().getContents());
		}

		for (ListIterator groupIter = group.getContents().listIterator(group.getCount()); groupIter.hasPrevious();) {
			TableGroupHandle tableGroups = (TableGroupHandle) groupIter.previous();
			SlotHandle groupFooters = tableGroups.getFooter();
			for (Iterator groupFooterIter = groupFooters.iterator(); groupFooterIter.hasNext();) {
				children.addAll(((RowHandle) groupFooterIter.next()).getCells().getContents());
			}
		}

		SlotHandle footer = getTableHandle().getFooter();
		for (Iterator footerIter = footer.iterator(); footerIter.hasNext();) {
			children.addAll(((RowHandle) footerIter.next()).getCells().getContents());
		}

		removePhantomCells(children);
		return children;
	}

	/**
	 * Some cells might not be relevant, because overriden by column span/row span
	 * of other cells Example in a three columns table: <row><cell>
	 * <property name="colSpan">3 </property> <property name="rowSpan">1 </property>
	 * <cell><cell/><cell/><row>
	 * 
	 * The last two cells are phantom, the layout cannot handle them so we remove
	 * them at that stage. Ideally the model should not return those cells.
	 * 
	 * @param children
	 */
	protected void removePhantomCells(List children) {

		ArrayList phantomCells = new ArrayList();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Object cell = iter.next();
			CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(cell);
			if (cellAdapt.getRowNumber() == 0 || cellAdapt.getColumnNumber() == 0) {
				phantomCells.add(cell);
			}
		}
		for (Iterator iter = phantomCells.iterator(); iter.hasNext();) {
			children.remove(iter.next());
		}

	}

	/**
	 * Inserts a slot into the row info list
	 * 
	 * @param rowInfoList The list of row info
	 * @param slotHandle  The slot to insert
	 * @param displayName The display name of the slot
	 * @param type        the type of the slot
	 */
	protected void insertRowInfo(SlotHandle slotHandle, String displayName, String type) {
		for (Iterator it = slotHandle.iterator(); it.hasNext();) {
			RowHandle handle = (RowHandle) it.next();
			rows.add(handle);
			TableHandleAdapter.RowUIInfomation info = new TableHandleAdapter.RowUIInfomation(getColumnCount());
			info.setType(type);
			info.setRowDisplayName(displayName);
			info.addChildren(handle.getCells().getContents());
			rowInfo.put(handle, info);
		}

	}

	/**
	 * Get rows
	 * 
	 * @return The list of rows
	 */
	public List getRows() {
		if (checkDirty() || rowInfo.isEmpty()) {
			reload();
		}
		return rows;
	}

	/**
	 * Clear the buffer.
	 * 
	 */
	protected void clearBuffer() {
		rowInfo.clear();
		rows.clear();
	}

	/**
	 * Gets all rows list.
	 * 
	 * @return The rows list.
	 */
	public void initRowsInfo() {
		clearBuffer();
		buildRowInfo();
		caleRowInfo(rows);
	}

	protected void buildRowInfo() {
		insertRowInfo(getTableHandle().getHeader(), TABLE_HEADER, TABLE_HEADER);

		SlotHandle groups = getTableHandle().getGroups();

		int number = 0;
		for (Iterator itor = groups.iterator(); itor.hasNext();) {
			number++;
			TableGroupHandle tableGroup = (TableGroupHandle) itor.next();
			insertRowInfo(tableGroup.getHeader(), Integer.toString(number), TABLE_GROUP_HEADER);
		}

		insertRowInfo(getTableHandle().getDetail(), TABLE_DETAIL, TABLE_DETAIL);

		number = groups.getCount();

		for (ListIterator itor = groups.getContents().listIterator(number); itor.hasPrevious();) {
			TableGroupHandle tableGroup = (TableGroupHandle) itor.previous();
			insertRowInfo(tableGroup.getFooter(), Integer.toString(number), TABLE_GROUP_FOOTER);
			number--;
		}

		insertRowInfo(getTableHandle().getFooter(), TABLE_FOOTER, TABLE_FOOTER);
	}

	/**
	 * @param children
	 */
	protected void caleRowInfo(List children) {
		int size = children.size();

		for (int i = 0; i < size; i++) {
			RowHandleAdapter adapt = HandleAdapterFactory.getInstance().getRowHandleAdapter(children.get(i));
			List cellChildren = adapt.getChildren();
			int len = cellChildren.size();

			TableHandleAdapter.RowUIInfomation info = (TableHandleAdapter.RowUIInfomation) rowInfo.get(children.get(i));
			for (int j = 0; j < len; j++) {
				CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance()
						.getCellHandleAdapter(cellChildren.get(j));
				int cellIndex = info.getAllChildren().indexOf(cellChildren.get(j));

				if (cellAdapt.getColumnSpan() != 1) {
					if (cellIndex + 2 <= info.getAllChildren().size() && cellIndex >= 0) {
						fillRowInfoChildrenList(children.get(i), cellIndex + 2, cellAdapt.getColumnSpan() - 1,
								cellChildren.get(j));
					}

				}
				if (cellAdapt.getRowSpan() != 1) {
					for (int k = i + 1; k < i + cellAdapt.getRowSpan(); k++) {
						if (cellIndex < 0 || cellIndex + cellAdapt.getColumnSpan() > info.getAllChildren().size()) {
							continue;
						}
						fillRowInfoChildrenList(children.get(k), cellIndex + 1, cellAdapt.getColumnSpan(),
								cellChildren.get(j));
					}
				}
			}
		}
	}

	private void fillRowInfoChildrenList(Object row, int columnNumber, int colSpan, Object cell) {
		TableHandleAdapter.RowUIInfomation info = (TableHandleAdapter.RowUIInfomation) rowInfo.get(row);
		if (info == null) {
			return;
		}

		for (int i = 0; i < colSpan; i++) {
			info.addChildren(cell, columnNumber + i - 1);
		}
	}

	/**
	 * Get GUI information of row. For CSS table support auto layout, the GUI info
	 * is different with model info.
	 * 
	 * @param row
	 * @return
	 */
	public TableHandleAdapter.RowUIInfomation getRowInfo(Object row) {
		if (checkDirty()) {
			reload();
		}
		return (TableHandleAdapter.RowUIInfomation) rowInfo.get(row);
	}

	/**
	 * 
	 * @see org.eclipse.birt.designer.core.facade.DesignElementHandleAdapter#reload()
	 */
	public void reload() {
		super.reload();
		initRowsInfo();
		if (getModelAdaptHelper() != null) {
			getModelAdaptHelper().markDirty(false);
		}

	}

	/**
	 * Gets the all columns list.
	 * 
	 * @return The columns list.
	 */
	public List getColumns() {
		return getTableHandle().getColumns().getContents();
	}

	/**
	 * Gets the special row
	 * 
	 * @param i The row number.
	 * @return The special row.
	 */
	public Object getRow(int i) {
		List list = getRows();
		if (i >= 1 && i <= list.size()) {
			return list.get(i - 1);
		}
		return null;
	}

	/**
	 * Gets the special column
	 * 
	 * @param i The column number.
	 * @return The special column.
	 */
	public Object getColumn(int i) {

		List list = getColumns();
		if (i >= 1 && i <= list.size()) {
			return list.get(i - 1);
		}
		return null;
	}

	/**
	 * Gets the special cell.
	 * 
	 * @param rowNumber    The row number.
	 * @param columnNumber The column number.
	 * @param bool
	 * @return The special cell.
	 */
	public Object getCell(int rowNumber, int columnNumber, boolean bool) {
		Object obj = getRow(rowNumber);
		TableHandleAdapter.RowUIInfomation info = getRowInfo(obj);
		Object retValue = null;
		if (info != null && columnNumber >= 1 && info.getAllChildren().size() >= columnNumber) {
			retValue = info.getAllChildren().get(columnNumber - 1);
		}
		if (bool) {
			return retValue;
		}

		if (HandleAdapterFactory.getInstance().getCellHandleAdapter(retValue).getRowNumber() != rowNumber
				|| HandleAdapterFactory.getInstance().getCellHandleAdapter(retValue)
						.getColumnNumber() != columnNumber) {
			retValue = null;
		}

		return retValue;

	}

	/**
	 * Gets the special cell.
	 * 
	 * @param i The row number.
	 * @param j The column number.
	 * @return The special cell.
	 */
	public Object getCell(int i, int j) {
		return getCell(i, j, true);
	}

	/**
	 * Calculates table layout size. For table supports auto layout, the layout size
	 * need to be calculated when drawing.
	 * 
	 * @return
	 */
	public Dimension calculateSize() {
		if (!(getModelAdaptHelper() instanceof ITableAdapterHelper)) {
			return new Dimension();
		}

		ITableAdapterHelper tableHelper = (ITableAdapterHelper) getModelAdaptHelper();

		int columnCount = getColumnCount();
		int samColumnWidth = 0;
		for (int i = 0; i < columnCount; i++) {
			samColumnWidth = samColumnWidth + tableHelper.caleVisualWidth(i + 1);
		}

		int rowCount = getRowCount();
		int samRowHeight = 0;
		for (int i = 0; i < rowCount; i++) {
			samRowHeight = samRowHeight + tableHelper.caleVisualHeight(i + 1);
		}

		return new Dimension(samColumnWidth, samRowHeight).expand(tableHelper.getInsets().getWidth(),
				tableHelper.getInsets().getHeight());
	}

	public void ajustSize(Dimension size) throws SemanticException {
		if (isFixLayout()) {
			adjustFixSize(size);
		} else {
			adjustAutoSize(size);
		}
	}

	private boolean isFixLayout() {
		if (getHandle().getModuleHandle() instanceof ReportDesignHandle) {
			return DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT
					.equals(((ReportDesignHandle) getHandle().getModuleHandle()).getLayoutPreference());
		}
		return false;
	}

	private void adjustFixSize(Dimension size) throws SemanticException {
		if (!(getModelAdaptHelper() instanceof ITableAdapterHelper)) {
			return;
		}

		ITableAdapterHelper tableHelper = (ITableAdapterHelper) getModelAdaptHelper();

		int width = size.width - tableHelper.getInsets().getWidth();
		int height = size.height - tableHelper.getInsets().getHeight();
		int columnCount = getColumnCount();
		int rowCount = getRowCount();
		Dimension parentSize = new Dimension();
		for (int i = 0; i < columnCount; i++) {
			int temp = tableHelper.caleVisualWidth(i + 1);
			parentSize.width = parentSize.width + temp;
		}

		for (int i = 0; i < rowCount; i++) {
			int temp = tableHelper.caleVisualHeight(i + 1);
			parentSize.height = parentSize.height + temp;
		}

		// Dimension parentSize = tableHelper.getClientAreaSize( );

		double[] columns = new double[columnCount];
		double[] rows = new double[rowCount];
		if (width >= 0) {
			int totalWidth = 0;
			for (int i = 0; i < columnCount; i++) {
				double columnWidth = tableHelper.caleVisualWidth(i + 1);
				columns[i] = columnWidth / parentSize.width * width;
				if (Math.round(columns[i]) < 1) {
					columns[i] = 1.0;
				}
				totalWidth = totalWidth + (int) Math.round(columns[i]);
			}
			columns[0] = columns[0] + width - totalWidth;

			for (int i = 0; i < columnCount; i++) {
				HandleAdapterFactory.getInstance().getColumnHandleAdapter(getColumn(i + 1))
						.setWidth((int) Math.round(columns[i]));
			}
		} else if (size.width > 0) {
			for (int i = 0; i < columnCount; i++) {
				HandleAdapterFactory.getInstance().getColumnHandleAdapter(getColumn(i + 1)).setWidth(Math.round(1));
			}
		}

		if (height >= 0) {
			int totalHeight = 0;
			for (int i = 0; i < rowCount; i++) {
				double rowHeight = tableHelper.caleVisualHeight(i + 1);
				rows[i] = rowHeight / parentSize.height * height;
				if (Math.round(rows[i]) < 1) {
					rows[i] = 1.0;
				}
				totalHeight = totalHeight + (int) Math.round(rows[i]);
			}
			rows[0] = rows[0] + height - totalHeight;

			for (int i = 0; i < rowCount; i++) {
				HandleAdapterFactory.getInstance().getRowHandleAdapter(getRow(i + 1))
						.setHeight((int) Math.round(rows[i]));
			}
		} else if (size.height > 0) {
			for (int i = 0; i < rowCount; i++) {
				HandleAdapterFactory.getInstance().getRowHandleAdapter(getRow(i + 1)).setHeight(Math.round(1));
			}
		}

		setSize(new Dimension(width, height));
	}

	/**
	 * Adjust size of table layout.
	 * 
	 * @param size is all figure size
	 * @throws SemanticException
	 */
	private void adjustAutoSize(Dimension size) throws SemanticException {
		if (!(getModelAdaptHelper() instanceof ITableAdapterHelper)) {
			return;
		}

		ITableAdapterHelper tableHelper = (ITableAdapterHelper) getModelAdaptHelper();

		int width = size.width;
		int height = size.height;

		size = size.shrink(width < 0 ? 0 : tableHelper.getInsets().getWidth(),
				height < 0 ? 0 : tableHelper.getInsets().getHeight());

		if (width >= 0) {
			int columnCount = getColumnCount();
			int samColumnWidth = 0;
			for (int i = 0; i < columnCount; i++) {
				if (i != columnCount - 1) {
					samColumnWidth = samColumnWidth + tableHelper.caleVisualWidth(i + 1);
				}
			}
			int lastColumnWidth = size.width - samColumnWidth;
			if (lastColumnWidth < tableHelper.getMinWidth(columnCount)) {
				lastColumnWidth = tableHelper.getMinWidth(columnCount);
				HandleAdapterFactory.getInstance().getColumnHandleAdapter(getColumn(columnCount))
						.setWidth(lastColumnWidth);
			} else if (lastColumnWidth != tableHelper.caleVisualWidth(columnCount)) {
				HandleAdapterFactory.getInstance().getColumnHandleAdapter(getColumn(columnCount))
						.setWidth(lastColumnWidth);
			}
			width = samColumnWidth + lastColumnWidth;
		}

		if (height >= 0) {
			int rowCount = getRowCount();
			int samRowHeight = 0;
			for (int i = 0; i < rowCount; i++) {
				if (i != rowCount - 1) {
					samRowHeight = samRowHeight + tableHelper.caleVisualHeight(i + 1);
				}
			}
			int lastRowHeight = size.height - samRowHeight;

			if (lastRowHeight < tableHelper.getMinHeight(rowCount)) {
				lastRowHeight = tableHelper.getMinHeight(rowCount);
				HandleAdapterFactory.getInstance().getRowHandleAdapter(getRow(rowCount)).setHeight(lastRowHeight);
			} else if (lastRowHeight != tableHelper.caleVisualHeight(rowCount)) {
				HandleAdapterFactory.getInstance().getRowHandleAdapter(getRow(rowCount)).setHeight(lastRowHeight);
			}
			height = samRowHeight + lastRowHeight;
		}

		setSize(new Dimension(width, height).expand(width < 0 ? 0 : tableHelper.getInsets().getWidth(),
				height < 0 ? 0 : tableHelper.getInsets().getHeight()));
	}

	/**
	 * Get the minimum height.of a specific row.
	 * 
	 * @param rowNumber
	 * @return The minimum height.
	 */
	public int getMinHeight(int rowNumber) {
		// TODO: The value may need to dynamic calculated or user definable
		return RowHandleAdapter.DEFAULT_MINHEIGHT;
	}

	/**
	 * Get the minimum width a specific row.
	 * 
	 * @param columnNumber
	 * @return The minimum width.
	 */
	public int getMinWidth(int columnNumber) {

		// TODO: The value may need to dynamic calculated or user definable
		return ColumnHandleAdapter.DEFAULT_MINWIDTH;
	}

	/**
	 * @return client area
	 */
	public Dimension getClientAreaSize() {
		if (getModelAdaptHelper() instanceof ITableAdapterHelper) {
			return ((ITableAdapterHelper) getModelAdaptHelper()).getClientAreaSize();
		}

		return new Dimension();
	}

	private TableHandle getTableHandle() {
		return (TableHandle) getHandle();
	}

	/**
	 * Returns the defined width in model in Pixel.
	 * 
	 * @return
	 */
	public String getDefinedWidth() {
		DimensionHandle handle = ((ReportItemHandle) getHandle()).getWidth();

		if (handle.getUnits() == null || handle.getUnits().length() == 0) {
			// TODO The default value is 100.0% to fix the bug 124051, but it is
			// a temp solution.
			// default value is 100.0%
			return DEFAULT_WIDTH;
			// return null;
		} else if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(handle.getUnits())) {
			return handle.getMeasure() + DesignChoiceConstants.UNITS_PERCENTAGE;
		} else {
			int px = (int) DEUtil.convertoToPixel(handle);

			if (px <= 0) {
				return null;
			}

			return String.valueOf(px);
		}
	}

	/**
	 * If the width is force width
	 * 
	 * @return
	 */
	public boolean isForceWidth() {
		DimensionHandle handle = ((ReportItemHandle) getHandle()).getWidth();
		return handle.isSet();
	}

	/**
	 * Get the default width.
	 * 
	 * @param colNumber The column number.
	 * @return The default width.
	 */
	public int getDefaultWidth(int colNumber) {
		Dimension size = getDefaultSize();
		Object obj = getRow(1);
		if (obj == null) {
			return size.width;
		}

		int allNumbers = getColumnCount();
		if (allNumbers <= 0) {
			return size.width;
		}
		if (colNumber <= 0) {
			return size.width;
		}
		int width = size.width;
		int columnNumber = allNumbers;
		for (int i = 1; i < columnNumber + 1; i++) {
			Object column = getColumn(i);
			ColumnHandleAdapter adapt = HandleAdapterFactory.getInstance().getColumnHandleAdapter(column);
			if (adapt.isCustomWidth()) {
				allNumbers = allNumbers - 1;
				width = width - adapt.getWidth();
			}
		}

		if (colNumber == allNumbers) {
			return width / allNumbers + width % allNumbers;
		}
		return (width / allNumbers);
	}

	/**
	 * Gets the row count
	 * 
	 * @return The row count.
	 */
	public int getRowCount() {
		return getRows().size();
	}

	/**
	 * Gets the column count
	 * 
	 * @return The column count.
	 */
	public int getColumnCount() {
		return getColumns().size();
	}

	/**
	 * @return The data set.
	 */
	public Object getDataSet() {
		return getTableHandle().getDataSet();
	}

	/**
	 * Insert a row to a specific position.
	 * 
	 * @param rowNumber       The row number. 1 insert after position. -1 insert
	 *                        before position
	 * @param parentRowNumber The row number in the table.
	 * @throws SemanticException
	 */
	public void insertRow(int rowNumber, int parentRowNumber) throws SemanticException {
		transStar(TRANS_LABEL_INSERT_ROW);
		assert rowNumber != 0;
		int realRowNumber = rowNumber > 0 ? parentRowNumber + rowNumber : parentRowNumber + rowNumber + 1;
		int shiftPos = rowNumber > 0 ? rowNumber : rowNumber + 1;
		RowHandle row = (RowHandle) getRow(parentRowNumber);
		RowHandleAdapter adapt = HandleAdapterFactory.getInstance().getRowHandleAdapter(row);

		RowHandle copy = (RowHandle) adapt.copy();

		TableHandleAdapter.RowUIInfomation rowInfo = getRowInfo(row);
		List rowList = rowInfo.getAllChildren();
		int rowSize = rowList.size();
		for (int i = 0; i < rowSize; i++) {
			CellHandle parentCell = (CellHandle) rowList.get(i);
			CellHandle cell = getCellHandleCopy(parentCell);
			copy.getSlot(RowHandle.CONTENT_SLOT).add(cell);
		}

		SlotHandle parentHandle = row.getContainerSlotHandle();
		parentHandle.add((copy));

		int pos = parentHandle.findPosn(row);
		parentHandle.shift(copy, pos + shiftPos);

		RowHandleAdapter copyAdapt = HandleAdapterFactory.getInstance().getRowHandleAdapter(copy);
		List copyChildren = copyAdapt.getChildren();

		if (getModelAdaptHelper() != null) {
			getModelAdaptHelper().markDirty(true);
		}
		TableHandleAdapter.RowUIInfomation info = getRowInfo(copy);
		List list = info.getAllChildren();

		List temp = new ArrayList();
		int size = list.size();

		List hasAdjust = new ArrayList();
		for (int i = 0; i < size; i++) {
			Object fillCell = list.get(i);
			CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(fillCell);
			if (cellAdapt.getRowNumber() != realRowNumber) {
				if (!hasAdjust.contains(fillCell)) {
					cellAdapt.setRowSpan(cellAdapt.getRowSpan() + 1);
					hasAdjust.add(fillCell);
				}
				temp.add(Integer.valueOf(i));
			}
		}

		int copyRowSize = copyChildren.size();
		for (int i = 0; i < copyRowSize; i++) {
			if (temp.contains(Integer.valueOf(i))) {
				((CellHandle) copyChildren.get(i)).drop();
			}
		}
		transEnd();
	}

	/**
	 * Insert multiple rows(or a single row) to a specific position.
	 * 
	 * @param rowCount      The row insert count. Positive number will insert after
	 *                      position, negative insert before the position
	 * @param baseRowNumber The row position in the table.
	 * @throws SemanticException
	 */
	public void insertRows(int rowCount, int baseRowNumber) throws SemanticException {
		transStar(TRANS_LABEL_INSERT_ROW);
		int absoluteCount = Math.abs(rowCount);
		int sign = rowCount / absoluteCount;
		for (int i = 0; i < absoluteCount; i++) {
			insertRow(sign, baseRowNumber);
		}
		transEnd();
	}

	/**
	 * Insert a column to a specific position.
	 * 
	 * @param columnNumber       The column insert sign. 1 insert after position. -1
	 *                           insert before position
	 * @param parentColumnNumber The column number of parent.
	 * @throws SemanticException
	 */
	public void insertColumn(int columnNumber, int parentColumnNumber) throws SemanticException {
		transStar(TRANS_LABEL_INSERT_COLUMN);
		assert columnNumber != 0;
		int realColumnNumber = columnNumber > 0 ? parentColumnNumber + columnNumber
				: parentColumnNumber + columnNumber + 1;
		int shiftPos = columnNumber > 0 ? columnNumber : columnNumber + 1;
		ColumnHandle column = (ColumnHandle) getColumn(parentColumnNumber);
		ColumnHandleAdapter adapt = HandleAdapterFactory.getInstance().getColumnHandleAdapter(column);

		ColumnHandle copy = (ColumnHandle) adapt.copy();

		int rowNumber = getRowCount();
		List copyChildren = new ArrayList();
		for (int i = 0; i < rowNumber; i++) {
			TableHandleAdapter.RowUIInfomation rowInfo = getRowInfo(getRow(i + 1));
			List rowList = rowInfo.getAllChildren();

			CellHandle parentCell = (CellHandle) rowList.get(parentColumnNumber - 1);
			CellHandle cell = getCellHandleCopy(parentCell);

			copyChildren.add(cell);
		}

		int copyRowSize = copyChildren.size();
		for (int i = 0; i < copyRowSize; i++) {
			RowHandle row = (RowHandle) getRow(i + 1);
			int number = getReallyRowNumber(row, realColumnNumber);
			row.getSlot(RowHandle.CONTENT_SLOT).add((CellHandle) (copyChildren.get(i)), number - 1);

		}
		SlotHandle parentHandle = column.getContainerSlotHandle();
		parentHandle.add((copy));

		int pos = parentHandle.findPosn(column);
		parentHandle.shift(copy, pos + shiftPos);

		List temp = new ArrayList();

		List hasAdjust = new ArrayList();
		// There are some logic error, but is the framework error after change
		// the event dispatch framework.
		// same to the insertrow method.
		reload();
		for (int i = 0; i < rowNumber; i++) {
			TableHandleAdapter.RowUIInfomation rowInfo = getRowInfo(getRow(i + 1));
			List rowList = rowInfo.getAllChildren();

			Object fillCell = rowList.get(realColumnNumber - 1);
			CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(fillCell);
			if (cellAdapt.getColumnNumber() != realColumnNumber) {
				if (!hasAdjust.contains(fillCell)) {
					cellAdapt.setColumnSpan(cellAdapt.getColumnSpan() + 1);
					hasAdjust.add(fillCell);
				}
				temp.add(Integer.valueOf(i));
			}
		}

		for (int i = 0; i < copyRowSize; i++) {
			if (temp.contains(Integer.valueOf(i))) {
				((CellHandle) copyChildren.get(i)).drop();
			}
		}

		transEnd();
	}

	private int getReallyRowNumber(RowHandle rowHandle, int number) {
		TableHandleAdapter.RowUIInfomation rowInfo = getRowInfo(rowHandle);
		int rowNumber = HandleAdapterFactory.getInstance().getRowHandleAdapter(rowHandle).getRowNumber();
		List rowList = rowInfo.getAllChildren();

		int retValue = number;
		List hasAdjust = new ArrayList();
		for (int i = 0; i < number - 1; i++) {
			Object fillCell = rowList.get(i);
			CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(fillCell);
			if (hasAdjust.contains(fillCell)) {
				retValue = retValue - 1;
				continue;
			}
			if (cellAdapt.getRowNumber() != rowNumber) {
				retValue = retValue - 1;
			}
			hasAdjust.add(fillCell);
		}
		return retValue;

	}

	/**
	 * Insert multiple columns to a specific position.
	 * 
	 * @param columnCount      The column insert count. Positive number will insert
	 *                         after position, negative insert before the position
	 * @param baseColumnNumber The column position in the table.
	 * @throws SemanticException
	 */
	public void insertColumns(int columnCount, int baseColumnNumber) throws SemanticException {
		transStar(TRANS_LABEL_INSERT_COLUMN);
		int absoluteCount = Math.abs(columnCount);
		int sign = columnCount / absoluteCount;
		for (int i = 0; i < absoluteCount; i++) {
			insertColumn(sign, baseColumnNumber);
		}
		transEnd();
	}

	/**
	 * @param model The object to be removed.
	 * @throws SemanticException
	 */
	public void removeChild(Object model) throws SemanticException {
		assert (model instanceof DesignElementHandle);
		DesignElementHandle ele = (DesignElementHandle) model;
		ele.drop();
	}

	/**
	 * Delete specific columns from the current table.
	 * 
	 * @param columns The columns to be deleted.
	 * @throws SemanticException
	 */
	public void deleteColumn(int[] columns) throws SemanticException {

		if (columns == null) {
			return;
		}
		transStar(TRANS_LABEL_DELETE_COLUMNS);
		if (getColumnCount() == columns.length) {
			getHandle().drop();
			transEnd();
			return;
		}
		Arrays.sort(columns);
		List temp = new ArrayList();
		int len = columns.length;
		for (int i = 0; i < len; i++) {
			temp.add(getColumn(columns[i]));
		}

		for (int i = 0; i < len; i++) {
			// deleteColumn( columns[i] );
			deleteColumn(HandleAdapterFactory.getInstance().getColumnHandleAdapter(temp.get(i)).getColumnNumber());
		}
		transEnd();
	}

	/**
	 * Delete a specific column from the current table.
	 * 
	 * @param columnNumber The column to be deleted.
	 * @throws SemanticException
	 */
	public void deleteColumn(int columnNumber) throws SemanticException {
		transStar(TRANS_LABEL_DELETE_COLUMN);
		int rowCount = getRowCount();
		ColumnHandle column = (ColumnHandle) getColumn(columnNumber);

		List deleteCells = new ArrayList();
		for (int i = 0; i < rowCount; i++) {
			Object row = getRow(i + 1);
			TableHandleAdapter.RowUIInfomation info = getRowInfo(row);
			deleteCells.add(info.getAllChildren().get(columnNumber - 1));
		}

		List trueDeleteCells = new ArrayList();
		int size = deleteCells.size();
		for (int i = 0; i < size; i++) {
			Object cell = deleteCells.get(i);
			CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(cell);
			if (cellAdapt.getColumnNumber() == columnNumber && cellAdapt.getColumnSpan() == 1
					&& !trueDeleteCells.contains(cell)) {
				trueDeleteCells.add(cell);
			}
		}
		List temp = new ArrayList();
		for (int i = 0; i < size; i++) {
			Object cell = deleteCells.get(i);
			if (!trueDeleteCells.contains(cell) && !temp.contains(cell)) {
				CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(cell);
				cellAdapt.setColumnSpan(cellAdapt.getColumnSpan() - 1);
				temp.add(cell);
			}

		}

		size = trueDeleteCells.size();
		for (int i = 0; i < size; i++) {
			CellHandle cell = (CellHandle) trueDeleteCells.get(i);
			cell.drop();
		}
		column.drop();

		transEnd();
		reload();
	}

	/**
	 * Delete specific rows from the current table.
	 * 
	 * @param rows The rows to be deleted.
	 * @throws SemanticException
	 */
	public void deleteRow(int[] rows) throws SemanticException {

		if (rows == null) {
			return;
		}
		transStar(TRANS_LABEL_DELETE_ROWS);
		if (getRowCount() == rows.length) {
			getHandle().drop();
			transEnd();
			return;
		}

		Arrays.sort(rows);
		List temp = new ArrayList();
		int len = rows.length;
		for (int i = 0; i < len; i++) {
			temp.add(getRow(rows[i]));
		}

		for (int i = len - 1; i >= 0; i--) {
			// deleteRow( rows[i] );
			deleteRow(HandleAdapterFactory.getInstance().getRowHandleAdapter(temp.get(i)).getRowNumber());
		}
		transEnd();
	}

	/**
	 * Delete a specific row from the current table.
	 * 
	 * @param rowsNumber The row to be deleted.
	 * @throws SemanticException
	 */
	public void deleteRow(int rowsNumber) throws SemanticException {
		transStar(TRANS_LABEL_DELETE_ROW);
		int rowCount = getRowCount();
		RowHandle row = (RowHandle) getRow(rowsNumber);

		RowHandleAdapter rowAdapt = HandleAdapterFactory.getInstance().getRowHandleAdapter(row);

		List temp = new ArrayList();
		RowHandle nextRow = null;

		List shiftCellInfo = new ArrayList();
		if (rowsNumber + 1 <= rowCount) {
			List trueChildren = rowAdapt.getChildren();
			int cellSize = trueChildren.size();

			nextRow = (RowHandle) getRow(rowsNumber + 1);
			TableHandleAdapter.RowUIInfomation nextRowInfo = getRowInfo(nextRow);
			List nextRowChildren = nextRowInfo.getAllChildren();

			for (int i = 0; i < cellSize; i++) {
				Object cellHandle = trueChildren.get(i);
				CellHandleAdapter adapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(cellHandle);

				if (adapt.getRowSpan() != 1) {
					int numberInfo = 0;
					int index = nextRowChildren.indexOf(cellHandle);
					for (int j = 0; j < index; j++) {
						Object nextRowCell = nextRowChildren.get(j);
						CellHandleAdapter nextRowCellAdapt = HandleAdapterFactory.getInstance()
								.getCellHandleAdapter(nextRowCell);

						if (nextRowCellAdapt.getRowNumber() == rowsNumber + 1 && !temp.contains(nextRowCell)) {
							numberInfo = numberInfo + 1;
						}
						temp.add(nextRowCell);
					}
					numberInfo = numberInfo + shiftCellInfo.size();
					shiftCellInfo.add(new ShiftNexRowInfo(numberInfo, cellHandle));

				}
			}
		}

		TableHandleAdapter.RowUIInfomation info = getRowInfo(row);
		List cells = info.getAllChildren();
		temp.clear();

		int cellSize = cells.size();
		for (int j = 0; j < cellSize; j++) {
			Object cellHandle = cells.get(j);
			CellHandleAdapter adapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(cellHandle);

			if (adapt.getRowNumber() != rowsNumber && !temp.contains(cellHandle)) {
				adapt.setRowSpan(adapt.getRowSpan() - 1);
				temp.add(cellHandle);
			}
		}
		// row.drop( );
		for (int i = 0; i < shiftCellInfo.size(); i++) {
			ShiftNexRowInfo shiftInfo = (ShiftNexRowInfo) shiftCellInfo.get(i);
			CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(shiftInfo.cell);
			cellAdapt.setRowSpan(cellAdapt.getRowSpan() - 1);
			SlotHandle slotHandle = row.getCells();
			slotHandle.move((DesignElementHandle) shiftInfo.cell, nextRow, RowHandle.CONTENT_SLOT, shiftInfo.index);
		}

		row.drop();

		transEnd();
		reload();
	}

	static class ShiftNexRowInfo {

		protected int index;
		protected Object cell;

		/**
		 * @param index
		 * @param cell
		 */
		public ShiftNexRowInfo(int index, Object cell) {
			super();
			this.index = index;
			this.cell = cell;
		}
	}

	public static class RowUIInfomation {

		protected static final String GRID_ROW = NAME_NULL;

		private String type = ""; //$NON-NLS-1$
		private String rowDisplayName = ""; //$NON-NLS-1$
		Object[] cells = null;
		int[] infactAdd = new int[0];

		private RowUIInfomation(int columnMunber) {
			cells = new Object[columnMunber];
		}

		/**
		 * Get row display name
		 * 
		 * @return display name
		 */
		public String getRowDisplayName() {
			return rowDisplayName;
		}

		/**
		 * Set row display name
		 * 
		 * @param rowDisplayName Display name
		 */
		public void setRowDisplayName(String rowDisplayName) {
			this.rowDisplayName = rowDisplayName;
		}

		/**
		 * Get type
		 * 
		 * @return type The type gotten
		 */
		public String getType() {
			return type;
		}

		/**
		 * Set type
		 * 
		 * @param type The type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * Add children
		 * 
		 * @param obj
		 * @param index
		 */
		public void addChildren(Object obj, int index) {
			int cellSize = cells.length;
			if (index >= cellSize) {
				return;
			}
			ArrayList list = new ArrayList();
			if (cells[index] != null) {
				Object[] newArray = new Object[cellSize];
				for (int i = 0; i < cellSize; i++) {
					if (containIndex(i)) {
						newArray[i] = cells[i];
					} else if (cells[i] != null) {
						list.add(cells[i]);
					}
				}
				newArray[index] = obj;
				int listSize = list.size();

				for (int i = 0; i < listSize; i++) {
					for (int j = 0; j < cellSize; j++) {
						if (newArray[j] == null) {
							newArray[j] = list.get(i);
							break;
						}
					}
				}
				cells = newArray;
			} else {
				cells[index] = obj;
			}

			int lenegth = infactAdd.length;
			int[] temp = new int[lenegth + 1];

			System.arraycopy(infactAdd, 0, temp, 0, lenegth);
			temp[lenegth] = index;
			infactAdd = temp;
		}

		private boolean containIndex(int index) {
			int length = infactAdd.length;
			for (int i = 0; i < length; i++) {
				if (infactAdd[i] == index) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Add children
		 * 
		 * @param c Children in collection
		 */
		public void addChildren(Collection c) {
			Iterator itor = c.iterator();
			int cellSize = cells.length;
			while (itor.hasNext()) {
				Object obj = itor.next();
				for (int i = 0; i < cellSize; i++) {
					if (cells[i] == null) {
						cells[i] = obj;
						break;
					}
				}
			}
		}

		/**
		 * Get all the children in list
		 * 
		 * @return
		 */
		public List getAllChildren() {
			ArrayList retValue = new ArrayList();
			int cellSize = cells.length;
			for (int i = 0; i < cellSize; i++) {
				retValue.add(cells[i]);
			}
			return retValue;
		}
	}

	/**
	 * @param list
	 * @return If can merge return true, else false.
	 */
	public boolean canMerge(List list) {
		if (!getModuleHandle().canEdit()) {
			return false;
		}
		assert list != null;
		int size = list.size();
		if (size <= 1) {
			return false;
		}
		RowUIInfomation rowInfo = getRowInfo(((CellHandle) list.get(0)).getContainer());
		if (rowInfo == null) {
			// when delete row, this can be null.
			return false;
		}

		String first = rowInfo.getRowDisplayName();
		for (int i = 1; i < size; i++) {
			CellHandle cell = (CellHandle) list.get(i);

			if (!cell.canDrop()) {
				return false;
			}

			RowUIInfomation info = getRowInfo(cell.getContainer());
			if (info == null) {
				return false;
			}
			String str = info.getRowDisplayName();
			if (!first.equals(str)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Split a cell to cells.
	 * 
	 * @param model
	 * @throws SemanticException
	 * @throws NameException
	 * @throws ContentException
	 */
	public void splitCell(Object model) throws ContentException, NameException, SemanticException {
		transStar(TRANS_LABEL_SPLIT_CELLS);
		assert model instanceof CellHandle;

		CellHandle cellHandle = (CellHandle) model;
		CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(cellHandle);
		int rowNumber = cellAdapt.getRowNumber();
		int rowSpan = cellAdapt.getRowSpan();
		int colSpan = cellAdapt.getColumnSpan();

		// fill the cell row
		if (colSpan != 1) {
			int index = getIndexofParent(cellHandle);
			RowHandle rowHandle = (RowHandle) cellHandle.getContainer();
			for (int i = 1; i < colSpan; i++) {
				rowHandle.addElement(getCellHandleCopy(cellHandle), RowHandle.CONTENT_SLOT, i + index);
			}
		}
		if (rowSpan != 1) {
			for (int i = rowNumber + 1; i < rowNumber + rowSpan; i++) {
				RowHandle rowHandle = (RowHandle) getRow(i);
				int index = getIndexofParent(cellHandle);
				for (int j = 0; j < colSpan; j++) {
					rowHandle.addElement(getCellHandleCopy(cellHandle), RowHandle.CONTENT_SLOT, j + index);
				}
			}

		}
		cellAdapt.setRowSpan(1);
		cellAdapt.setColumnSpan(1);
		transEnd();
	}

	private int getIndexofParent(CellHandle cellHandle) {
		CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(cellHandle);
		TableHandleAdapter.RowUIInfomation info = getRowInfo(cellHandle.getContainer());
		List list = info.getAllChildren();
		int index = list.indexOf(cellHandle);
		List temp = new ArrayList();
		int number = 0;
		for (int j = 0; j < index; j++) {
			CellHandleAdapter childCellAdapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(list.get(j));
			if (childCellAdapt.getRowNumber() == cellAdapt.getRowNumber() && !temp.contains(list.get(j))) {
				number = number + 1;
			}
			temp.add(list.get(j));
		}
		return number;
	}

	/**
	 * Gets the cell handle copy to support row/column insert.
	 * 
	 * @param cellHandle
	 * @return
	 * @throws SemanticException
	 */
	public CellHandle getCellHandleCopy(CellHandle cellHandle) throws SemanticException {
		if (cellHandle == null) {
			return null;
		}
		CellHandle cell = cellHandle.getElementFactory().newCell();
		Iterator iter = cellHandle.getPropertyIterator();
		while (iter.hasNext()) {
			PropertyHandle handle = (PropertyHandle) iter.next();
			if (handle.getDefn().getValueType() == IPropertyDefn.USER_PROPERTY) {
				continue;
			}
			String key = handle.getDefn().getName();
			if (handle.isLocal() && (!(CellHandle.COL_SPAN_PROP.equals(key) || CellHandle.ROW_SPAN_PROP.equals(key)))) {
				// cell.setProperty( key, cellHandle.getProperty( key ) );
				cellHandle.copyPropertyTo(key, cell);
			}
		}
		return cell;
	}

	/**
	 * Provides insert group function.
	 * 
	 * @return
	 * @throws ContentException
	 * @throws NameException
	 */
	public TableGroupHandle insertGroup() throws ContentException, NameException {
		if (DEUtil.getDataSetList(getTableHandle()).isEmpty()) {
			return null;
		}
		transStar(TRANS_LABEL_INSERT_GROUP);

		RowHandle header = getTableHandle().getElementFactory().newTableRow();
		RowHandle footer = getTableHandle().getElementFactory().newTableRow();
		addCell(header);
		addCell(footer);

		TableGroupHandle groupHandle = getTableHandle().getElementFactory().newTableGroup();
		groupHandle.getSlot(TableGroupHandle.HEADER_SLOT).add(header);
		groupHandle.getSlot(TableGroupHandle.FOOTER_SLOT).add(footer);

		SlotHandle handle = getTableHandle().getGroups();
		handle.add(groupHandle);

		transEnd();
		return groupHandle;
	}

	/**
	 * Provides remove group function
	 * 
	 * @throws SemanticException
	 * 
	 */
	public void removeGroup(Object group) throws SemanticException {
		transStar(TRANS_LABEL_DELETE_GROUP);

		((RowHandle) group).getContainer().drop();
		if (getRows().size() == 0) {
			getHandle().drop();
		}
		transEnd();
	}

	protected void addCell(RowHandle handle) throws ContentException, NameException {
		int count = getColumnCount();
		for (int i = 0; i < count; i++) {
			CellHandle cell = handle.getElementFactory().newCell();
			handle.addElement(cell, RowHandle.CONTENT_SLOT);
		}
	}

	/**
	 * Insert row in model
	 * 
	 * @param id
	 * @throws ContentException
	 * @throws NameException
	 */
	public void insertRowInSlotHandle(int id) throws ContentException, NameException {
		transStar(TRANS_LABEL_INCLUDE + getOperationName(id));
		RowHandle rowHandle = getTableHandle().getElementFactory().newTableRow();
		addCell(rowHandle);
		getTableHandle().getSlot(id).add(rowHandle);
		transEnd();
	}

	/**
	 * Delete row in model
	 * 
	 * @param id
	 * @throws SemanticException
	 */
	public void deleteRowInSlotHandle(int id) throws SemanticException {
		transStar(TRANS_LABEL_NOT_INCLUDE + getOperationName(id));
		int[] rows = new int[0];
		Iterator itor = getTableHandle().getSlot(id).iterator();
		while (itor.hasNext()) {
			Object obj = itor.next();
			RowHandleAdapter adapt = HandleAdapterFactory.getInstance().getRowHandleAdapter(obj);
			int lenegth = rows.length;
			int[] temp = new int[lenegth + 1];

			System.arraycopy(rows, 0, temp, 0, lenegth);
			temp[lenegth] = adapt.getRowNumber();
			rows = temp;
		}
		deleteRow(rows);
		transEnd();
	}

	protected static String getOperationName(int id) {
		switch (id) {
		case HEADER:
			return NAME_HEADRER;
		case FOOTER:
			return NAME_FOOTER;
		case DETAIL:
			return NAME_DETAIL;
		default:
			return NAME_NULL;
		}
	}

	/**
	 * Check if the slot handle contains specified id.
	 * 
	 * @param id
	 * @return
	 */
	public boolean hasSlotHandleRow(int id) {
		Iterator itor = getTableHandle().getSlot(id).iterator();
		while (itor.hasNext()) {
			return true;
		}
		return false;
	}

	public void setSize(Dimension size) throws SemanticException {
		if (size.width >= 0) {
			MetricUtility.updateDimension(getReportItemHandle().getWidth(), size.width);
		}

		if (size.height >= 0 && isSupportHeight()) {
			MetricUtility.updateDimension(getReportItemHandle().getHeight(), size.height);
		}
	}

	public boolean isSupportHeight() {
		return false;
	}
}
