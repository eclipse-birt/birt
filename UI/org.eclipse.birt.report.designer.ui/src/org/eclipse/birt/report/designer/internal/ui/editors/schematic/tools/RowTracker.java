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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.RowHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.SelectionRequest;

/**
 * RowTracker
 */
public class RowTracker extends TableSelectionGuideTracker {

	IContainer container;

	/**
	 * Constructor
	 *
	 * @param sourceEditPart
	 */
	public RowTracker(TableEditPart sourceEditPart, int row, IContainer container) {
		super(sourceEditPart, row);

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
			int number = part.getOriRowNumner();

			List list = part.getViewer().getSelectedEditParts();
			if (list.size() == 0) {
				number = 1;
			}
			EditPart child = (EditPart) list.get(0);

			if (!(child.getModel() instanceof org.eclipse.birt.report.model.api.RowHandle)
					|| !((org.eclipse.birt.report.model.api.RowHandle) child.getModel()).getContainer()
							.equals(part.getModel())) {
				number = 1;
			}

			selectRows(number, columnNumber);
		} else {
			part.selectRow(new int[] { getNumber() });
			part.setOriRowNumner(getNumber());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.tools.AbstractTool#handleMove()
	 */
	@Override
	protected boolean handleMove() {
		// TODO Auto-generated method stub
		return super.handleMove();
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
		if (handle instanceof RowHandle) {
			return ((RowHandle) handle).getOwner() == getSourceEditPart();
		}
		return false;
		// EditPart part = getEditPartUnderMouse();
		// return part instanceof TableEditPart.DummyColumnEditPart ||
		// isSameTable();
	}

	@Override
	public void selectDrag() {
		RowHandle handle = (RowHandle) getHandleUnderMouse();

		int rowNumber = handle.getRowNumber();
		int number = getNumber();
		selectRows(number, rowNumber);
	}

	private void selectRows(int number, int rowNumber) {
		int[] rows = {};
		for (int i = number; i <= number + Math.abs(number - rowNumber); i++) {
			int lenegth = rows.length;
			int[] temp = new int[lenegth + 1];

			System.arraycopy(rows, 0, temp, 0, lenegth);
			temp[lenegth] = number > rowNumber ? number - (i - number) : i;
			rows = temp;
		}
		if (rows.length > 0) {
			TableEditPart tableEditpart = (TableEditPart) getSourceEditPart();
			tableEditpart.selectRow(rows);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.tools.SelectEditPartTracker#performOpen()
	 */
	@Override
	protected void performOpen() {
		SelectionRequest request = new SelectionRequest();
		request.setLocation(getLocation());
		request.setType(RequestConstants.REQ_OPEN);
		request.getExtendedData().put(DesignerConstants.TABLE_ROW_NUMBER, Integer.valueOf(getNumber()));
		if (getSourceEditPart().understandsRequest(request)) {
			getSourceEditPart().performRequest(request);
		}
	}
}
