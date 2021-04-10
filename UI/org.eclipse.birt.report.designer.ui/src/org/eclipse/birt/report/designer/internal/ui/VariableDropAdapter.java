/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.elements.interfaces.ISimpleMasterPageModel;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * 
 */

public class VariableDropAdapter implements IDropAdapter {

	public static final String TRANS_NAME = Messages.getString("VariableDropAdapter.TranasctionName");

	public int canDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (transfer instanceof VariableElementHandle && target instanceof EditPart) {
			EditPart editPart = (EditPart) target;
			editPart.getRoot().getModel();
			if (editPart.getModel() instanceof ReportDesignHandle || editPart.getModel() instanceof DesignElementHandle
					|| (editPart.getModel() instanceof SlotHandle)) {
				if (editPart.getModel() instanceof SlotHandle) {
					int slot_id = ((SlotHandle) editPart.getModel()).getSlotID();
					if (slot_id == ISimpleMasterPageModel.PAGE_HEADER_SLOT
							|| slot_id == ISimpleMasterPageModel.PAGE_FOOTER_SLOT) {
						if (((SlotHandle) editPart.getModel()).getCount() > 0)
							return DNDService.LOGIC_FALSE;
						else
							return DNDService.LOGIC_TRUE;
					} else if (slot_id == ISimpleMasterPageModel.PAGE_HEADER_SLOT) {
						return DNDService.LOGIC_TRUE;
					}
				}
				// variable can drop to gridin master page, bug 293121
				if (getMasterPageHandle(editPart) != null || DesignChoiceConstants.VARIABLE_TYPE_REPORT
						.equals(((VariableElementHandle) transfer).getType()))
					return DNDService.LOGIC_TRUE;
				else
					return DNDService.LOGIC_FALSE;
			}
		}

		return DNDService.LOGIC_UNKNOW;
	}

	private Object getMasterPageHandle(EditPart editPart) {
		if (editPart == null)
			return null;
		if (editPart.getParent() != null && (editPart.getParent().getModel() instanceof MasterPageHandle
				|| editPart.getParent().getModel() instanceof ModuleHandle))
			return editPart.getParent().getModel();
		return getMasterPageHandle(editPart.getParent());
	}

	public boolean performDrop(Object transfer, Object target, int operation, DNDLocation location) {

		EditPart editPart = (EditPart) target;

		VariableElementHandle variable = (VariableElementHandle) transfer;

		DataItemHandle dataHandle = DesignElementFactory.getInstance().newDataItem(null);

		try {
			ComputedColumn bindingColumn = StructureFactory.newComputedColumn(dataHandle, variable.getName());
			// FIXME currently variable does not support data type, so just set
			// string
			bindingColumn.setDataType("string");
			ExpressionUtility.setBindingColumnExpression(variable, bindingColumn, true);
			bindingColumn.setDisplayName(variable.getDisplayLabel());
			dataHandle.addColumnBinding(bindingColumn, false);
			dataHandle.setResultSetColumn(bindingColumn.getName());
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}

		CreateRequest request = new CreateRequest();
		request.getExtendedData().put(DesignerConstants.KEY_NEWOBJECT, dataHandle);
		request.setLocation(location.getPoint());
		Command command = editPart.getCommand(request);
		if (command != null && command.canExecute()) {
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			stack.startTrans(TRANS_NAME); // $NON-NLS-1$

			editPart.getViewer().getEditDomain().getCommandStack().execute(command);
			stack.commit();
			return true;
		} else
			return false;
	}

}
