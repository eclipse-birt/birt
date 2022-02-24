/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.designer.internal.ui;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.palette.DesignerPaletteFactory;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.jface.window.Window;

public class TableDropAdapter implements IDropAdapter {

	public int canDrop(Object transfer, Object target, int operation, DNDLocation location) {
		// TODO Auto-generated method stub
		int result = DNDService.LOGIC_UNKNOW;
		if (transfer.equals(DesignerPaletteFactory.TIMEPERIOD_TEMPLATE) && target instanceof TableCellEditPart) {

			CellHandle cellHandle = (CellHandle) ((TableCellEditPart) target).getModel();
			if (DEUtil.getBindingHolder(cellHandle) instanceof TableHandle) {
				TableHandle tableHandle = (TableHandle) (DEUtil.getBindingHolder(cellHandle));
				if (ExtendedDataModelUIAdapterHelper.isBoundToExtendedData(tableHandle)) {
					result = DNDService.LOGIC_TRUE;
				}
			}
		}
		return result;
	}

	public boolean performDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (target instanceof TableCellEditPart) {

			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			if (DesignerPaletteFactory.TIMEPERIOD_TEMPLATE.equals(transfer)) {
				stack.startTrans("Add TimePeriod"); //$NON-NLS-1$
			}

			DataItemHandle dataHandle = DesignElementFactory.getInstance().newDataItem(null);
			try {
				DesignElementHandle targetElement = null;
				if (target instanceof TableCellEditPart) {
					CellHandle cellHandle = (CellHandle) ((TableCellEditPart) target).getModel();
					cellHandle.addElement(dataHandle, CellHandle.CONTENT_SLOT);
					targetElement = cellHandle;
				}

				DataColumnBindingDialog dialog = new DataColumnBindingDialog(true);
				dialog.setLinkedModelTimePeriod(true);
				dialog.setInput(dataHandle, null, targetElement);
				dialog.setAggreate(true);
				dialog.setTimePeriod(true);

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
		}
		return true;
	}

}
