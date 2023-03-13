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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.ColumnHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Handle;

/**
 * ColumnTracker
 */
public class ColumnTracker extends TableSelectionGuideTracker {

	IContainer container;

	/**
	 * Constructor
	 *
	 * @param sourceEditPart
	 */
	public ColumnTracker(TableEditPart sourceEditPart, int column, IContainer container) {
		super(sourceEditPart, column);

		this.container = container;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * TableSelectionGuideTracker#select()
	 */
	@Override
	public void select() {
		if (container.isSelect() && getCurrentInput().isMouseButtonDown(3)) {
			return;
		}
		TableEditPart part = (TableEditPart) getSourceEditPart();

		if (getCurrentInput().isShiftKeyDown()) {

			int columnNumber = getNumber();
			int number = part.getOriColumnNumber();
			List list = part.getViewer().getSelectedEditParts();
			if (list.size() == 0) {
				number = 1;
			}
			EditPart child = (EditPart) list.get(0);

			if (!(child.getModel() instanceof org.eclipse.birt.report.model.api.ColumnHandle)
					|| !((org.eclipse.birt.report.model.api.ColumnHandle) child.getModel()).getContainer()
							.equals(part.getModel())) {
				number = 1;
			}
			selectColumns(number, columnNumber);
		} else {

			part.selectColumn(new int[] { getNumber() });
			part.setOriColumnNumber(getNumber());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * TableSelectionGuideTracker#handleButtonUp(int)
	 */
	@Override
	protected boolean handleButtonUp(int button) {
		boolean rlt = super.handleButtonUp(button);

		if (button == 1 && container != null && container.contains(getLocation())) {
			getSourceEditPart().getViewer().getContextMenu().getMenu().setVisible(true);
		}

		return rlt;
	}

	@Override
	public boolean isDealwithDrag() {
		Handle handle = getHandleUnderMouse();
		if (handle instanceof ColumnHandle) {
			return ((ColumnHandle) handle).getOwner() == getSourceEditPart();
		}
		return false;
		// EditPart part = getEditPartUnderMouse();
		// return part instanceof TableEditPart.DummyColumnEditPart ||
		// isSameTable();
	}

	@Override
	public void selectDrag() {
		ColumnHandle handle = (ColumnHandle) getHandleUnderMouse();

		int columnNumber = handle.getColumnNumber();
		int number = getNumber();
		selectColumns(number, columnNumber);
	}

	private void selectColumns(int number, int columnNumber) {
		int[] columns = {};
		for (int i = number; i <= number + Math.abs(number - columnNumber); i++) {
			int lenegth = columns.length;
			int[] temp = new int[lenegth + 1];

			System.arraycopy(columns, 0, temp, 0, lenegth);
			temp[lenegth] = number > columnNumber ? number - (i - number) : i;
			columns = temp;
		}
		if (columns.length > 0) {
			TableEditPart tableEditpart = (TableEditPart) getSourceEditPart();
			tableEditpart.selectColumn(columns);
		}
	}
}
