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

package org.eclipse.birt.report.designer.internal.ui;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementDetailHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.jface.window.Window;

/**
 * AggDataDropAdapter
 */
public class AggDataDropAdapter implements IDropAdapter {

	public static final String TEMPLATE = "DATA_AGG"; //$NON-NLS-1$
	public static final String TRANS_NAME = Messages.getString("AggDataDropAdapter.Trans.Name"); //$NON-NLS-1$

	public int canDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (transfer instanceof Object[]) {

		}
		if (transfer.equals(TEMPLATE)) {
			if (target instanceof TableCellEditPart) {
				CellHandle cellHandle = (CellHandle) ((TableCellEditPart) target).getModel();

				if (DEUtil.isReferenceElement(cellHandle.getContainer().getContainer()))
					return DNDService.LOGIC_FALSE;

				if (cellHandle.getContainer().getContainer() instanceof TableHandle
						|| cellHandle.getContainer().getContainer() instanceof TableGroupHandle) {
					return DNDService.LOGIC_TRUE;
				} else {
					if (DEUtil.getBindingHolder((CellHandle) ((TableCellEditPart) target).getModel()) != null)
						return DNDService.LOGIC_TRUE;
					else
						return DNDService.LOGIC_FALSE;
				}
			} else if (target instanceof ListBandEditPart) {
				DesignElementHandle list = ((ListBandProxy) ((ListBandEditPart) target).getModel()).getElemtHandle();
				if (list instanceof ListGroupHandle) {
					list = list.getContainer();
				}
				if (DEUtil.isReferenceElement(list))
					return DNDService.LOGIC_FALSE;
				return DNDService.LOGIC_TRUE;
			}
		}

		return DNDService.LOGIC_UNKNOW;
	}

	public boolean performDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (transfer instanceof Object[]) {

		}

		// create data item, and pass it to AggregationDataBindingDialog
		// start transaction
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

		stack.startTrans(TRANS_NAME);

		DataItemHandle dataHandle = DesignElementFactory.getInstance().newDataItem(null);
		try {
			DesignElementHandle targetElement = null;
			if (target instanceof TableCellEditPart) {
				CellHandle cellHandle = (CellHandle) ((TableCellEditPart) target).getModel();
				cellHandle.addElement(dataHandle, CellHandle.CONTENT_SLOT);
				targetElement = cellHandle;
			} else if (target instanceof ListBandEditPart) {
				ListBandProxy cellHandle = (ListBandProxy) ((ListBandEditPart) target).getModel();
				ElementDetailHandle slot = cellHandle.getSlotHandle();
				if (slot instanceof SlotHandle) {
					((SlotHandle) slot).add(dataHandle);
				} else {
					((PropertyHandle) slot).add(dataHandle);
				}

				targetElement = cellHandle.getElemtHandle();
			}

			DataColumnBindingDialog dialog = new DataColumnBindingDialog(true);
			dialog.setInput(dataHandle, null, targetElement);
			dialog.setAggreate(true);

			if (dialog.open() == Window.OK) {
				dataHandle.setResultSetColumn(dialog.getBindingColumn().getName());
				stack.commit();
			} else {
				stack.rollback();
			}
		} catch (Exception e) {
			stack.rollback();
			ExceptionHandler.handle(e);
		}
		return true;
	}

}
