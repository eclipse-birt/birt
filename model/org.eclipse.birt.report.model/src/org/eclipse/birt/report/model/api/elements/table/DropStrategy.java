/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api.elements.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;

/**
 * The strategy to apply drop to the table layout. A drop heading follows these
 * rules:
 * <ul>
 * <li>A drop heading can be defined only for a group header, but not for a
 * group footer or column heading.
 * <li>A drop heading is defined as one or more cells that vertically span the
 * group of table rows that represent the detail data rows for a group. That is,
 * the drop property works much like the vertical span, except that the exact
 * size of the span is computed at run time.
 * <li>A row with drop columns appears in a group header band.
 * <li>The vertical span starts with the group header. If, however, all other
 * cells within the group header table row are empty, then the vertical span
 * instead starts with the first detail row.
 * <li>A group header can contain two or more rows. In such a case, the drop can
 * be defined in any of these rows. The drop will merge group header cells as
 * well as detail cells as long as these header cells are 1) empty, and 2)
 * appears after the row with the drop cell.
 * </ul>
 * <p>
 * Note that, by definition, each group will contain at least one detail row,
 * and so a drop header will always appear. Some special conditions require
 * consideration:
 * <ul>
 * <li>The header row that contains a drop must be the last row within a group
 * header drop for the drop to take effect. If it is not the last row in the
 * band, then the drop is ignored.
 * <li>The above rule applies at runtime, not design time. For example, a header
 * may have three different group headers for a group, each with a different
 * condition. Only one is selected for any given report group. In this case, the
 * drop column is in effect.
 * <li>The drop setting is honored only if the detail table row has an empty
 * cell in the same position as the group header drop column. If the detail cell
 * is not empty, then the drop setting in the group header is ignored for that
 * column. This decision is made on a column-by-column basis.
 * <li>A blank detail cell is one that contains no report items.
 * <li>If a drop is defined on the inner-most group header, that drop cell spans
 * the (optional) group header row, and all detail rows for that group (with the
 * above caveats.) The drop property specifies if the drop should also span into
 * the group footer. If it does span into the group footer, the same caveats
 * above apply to the footer cells: they must be blank.
 * <li>A drop can apply to any group level. If the group is not the inner-most,
 * then the drop cell will span all the group headers for any nested groups, and
 * will span all detail items for all nested groups.
 * <li>A drop cannot be applied to a group footer.
 * </ul>
 */

class DropStrategy {

	/**
	 * The layout table to apply the drop effects.
	 */

	private LayoutTable layoutTable;

	/**
	 * Constructs a <code>DropStrategy</code> with the given table.
	 * 
	 * @param layoutTable the layout table
	 * 
	 */

	public DropStrategy(LayoutTable layoutTable) {
		this.layoutTable = layoutTable;
	}

	/**
	 * Applies this strategy.
	 */

	public void applyStrategy() {
		TableItem table = layoutTable.table;
		List groups = table.getGroups();

		for (int i = 0; i < groups.size(); i++) {
			TableGroup group = (TableGroup) groups.get(i);
			int groupLevel = group.getGroupLevel();

			resolveDropInSlot(group, groupLevel, layoutTable.getGroupHeaders().getLayoutSlotWithGroupLevel(groupLevel));
		}
	}

	/**
	 * Resolves drop in the given group header slot.
	 * 
	 * @param group      the table group
	 * @param groupLevel the group level
	 * @param layoutGH   the layout slot that maps the corresponding group header
	 *                   slot
	 */

	private void resolveDropInSlot(TableGroup group, int groupLevel, LayoutSlot layoutGH) {
		ContainerSlot groupHeader = group.getSlot(IGroupElementModel.HEADER_SLOT);
		if (groupHeader.getCount() < 1)
			return;

		int rowId = groupHeader.getCount() - 1;
		TableRow row = (TableRow) groupHeader.getContent(rowId);
		resolveDropInRow(row, rowId, groupLevel, layoutGH.getLayoutRow(rowId));

	}

	/**
	 * Resolves drop in the given row.
	 * 
	 * @param row        the table row element
	 * @param rowId      the 0-based row index
	 * @param groupLevel the group level
	 * @param layoutRow  the layout row that maps the corresponding row
	 */

	private void resolveDropInRow(TableRow row, int rowId, int groupLevel, LayoutRow layoutRow) {
		List cells = new ContainerContext(row, ITableRowModel.CONTENT_SLOT).getContents(layoutTable.getModule());
		for (int i = 0; i < cells.size(); i++) {
			Cell cell = (Cell) cells.get(i);
			resolveDropForCell(cell, rowId, groupLevel, layoutRow);
		}
	}

	/**
	 * Resolves drop for the given cell.
	 * 
	 * @param cell       the cell element to resolve
	 * @param rowId      the 0-based row index
	 * @param groupLevel the group level
	 * @param layoutRow  the layout row that maps the corresponding row
	 */

	private void resolveDropForCell(Cell cell, int rowId, int groupLevel, LayoutRow layoutRow) {
		String drop = (String) cell.getLocalProperty(null, ICellModel.DROP_PROP);
		if (drop == null || DesignChoiceConstants.DROP_TYPE_NONE.equalsIgnoreCase(drop))
			return;

		int colId = layoutRow.findCellColumnPos(cell) - 1;
		LayoutCell original = layoutRow.getLayoutCell(colId);
		if (!original.isCellStartPosition())
			return;

		int colSpan = cell.getColSpan(layoutTable.getModule());
		List layoutSlots = getSpanSlots(groupLevel, colId, colSpan, drop);

		updateUsedLayoutCell(layoutRow, colId, cell, layoutSlots);
		updateSpannedLayoutCell(layoutSlots, cell, colId, original.getCellId());
	}

	/**
	 * Returns layout rows that can be spanned by the "drop" of a cell element.
	 * 
	 * @param groupLevel the 1-based group level
	 * @param colId      the 0-based column index
	 * @param colSpan    the column span
	 * @param drop       the drop value of the cell element
	 * @return the list containing layout slot that can be spanned by "drop"
	 */

	private List getSpanSlots(int groupLevel, int colId, int colSpan, String drop) {
		List layoutSlots = new ArrayList();

		// check the group header

		LayoutGroupBand groups = layoutTable.getGroupHeaders();
		for (int i = 0; i < groups.getGroupCount(); i++) {
			LayoutSlot slot = groups.getLayoutSlot(i);
			if (slot.getGroupLevel() <= groupLevel)
				continue;

			if (isConflictArea(slot, colId, colSpan, true))
				return Collections.EMPTY_LIST;

			layoutSlots.add(slot);
		}

		// check the detail

		LayoutSlot detail = layoutTable.getDetail();
		if (isConflictArea(detail, colId, colSpan, false))
			return Collections.EMPTY_LIST;

		layoutSlots.add(detail);

		// check the group footer

		groups = layoutTable.getGroupFooters();
		if (DesignChoiceConstants.DROP_TYPE_ALL.equalsIgnoreCase(drop)) {
			for (int i = 0; i < groups.getGroupCount(); i++) {
				LayoutSlot slot = groups.getLayoutSlot(i);
				if (slot.getGroupLevel() < groupLevel)
					continue;

				if (isConflictArea(slot, colId, colSpan, false))
					return Collections.EMPTY_LIST;

				layoutSlots.add(slot);
			}
		}

		return layoutSlots;
	}

	/**
	 * Updates information of <code>LayoutCells</code>s that is used by the
	 * <code>cell</code>. <code>rowSpanForDrop</code> is set in this method.
	 * 
	 * @param row          the list containing layout rows that can be spanned by
	 *                     "drop"
	 * @param colId        the 0-based column index
	 * @param cell         the cell element
	 * @param spannedSlots slots that a drop cell can span to
	 */

	private void updateUsedLayoutCell(LayoutRow row, int colId, Cell cell, List spannedSlots) {
		int rowSpanForDrop = 0;
		for (int i = 0; i < spannedSlots.size(); i++)
			rowSpanForDrop += ((LayoutSlot) spannedSlots.get(i)).getRowCount();

		for (int i = 0; i < cell.getColSpan(layoutTable.getModule()); i++) {
			LayoutCell layoutCell = (LayoutCell) row.getLayoutCell(i + colId);

			assert layoutCell.isUsed();

			layoutCell.setEffectualDrop(true);
			layoutCell.setRowSpanForDrop(rowSpanForDrop);
		}
	}

	/**
	 * Updates information of <code>LayoutCells</code>s that is spanned by the
	 * <code>cell</code>.
	 * 
	 * @param layoutSlots slots that a drop cell can span into
	 * @param colId       the 0-based column index
	 * @param cell        the cell element
	 * @param cellId      the index of the cell that causes the "drop" span
	 */

	private void updateSpannedLayoutCell(List layoutSlots, Cell cell, int colId, int cellId) {
		for (int i = 0; i < layoutSlots.size(); i++) {
			LayoutSlot slot = (LayoutSlot) layoutSlots.get(i);
			slot.addDropSpannedCells(cellId, colId, cell.getColSpan(layoutTable.getModule()), i + 1, cell);
		}
	}

	/**
	 * Checks whether the area in <code>slot</code> is good for "drop" spanned
	 * effect or not.
	 * 
	 * @param slot    the layout slot
	 * @param colId   the 0-based column index
	 * @param colSpan the 1-based column span
	 * @param inGH    <code>true</code> if the slot is in Group Header. Otherwise
	 *                <code>false</code>.
	 * @return <code>true</code> if the area is good for "drop" spanning. Otherwise
	 *         <code>false</code>.
	 */

	private boolean isConflictArea(LayoutSlot slot, int colId, int colSpan, boolean inGH) {
		List retValue = slot.checkOverlappedLayoutCells(0, colId, slot.getRowCount(), colSpan);
		for (int i = 0; i < retValue.size(); i++) {
			LayoutCell cell = (LayoutCell) retValue.get(i);
			if (!cell.isEmptyContent())
				return true;

			if (inGH && cell.isDropSet())
				return true;
		}

		return false;
	}
}
