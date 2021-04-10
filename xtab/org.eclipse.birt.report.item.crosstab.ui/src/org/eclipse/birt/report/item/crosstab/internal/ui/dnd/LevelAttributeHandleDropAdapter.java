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

package org.eclipse.birt.report.item.crosstab.internal.ui.dnd;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * Support drag the levelhandle to the crosstab.Gets the command from the
 * editpart to execute.
 */

public class LevelAttributeHandleDropAdapter implements IDropAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter#canDrop
	 * (java.lang.Object, java.lang.Object, int,
	 * org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation)
	 */
	public int canDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (transfer instanceof IAdaptable) {
			if (((IAdaptable) transfer).getAdapter(StructureHandle.class) instanceof LevelAttributeHandle) {
				transfer = ((IAdaptable) transfer).getAdapter(StructureHandle.class);
			}
		}
		if (!isLevelAttributeHandle(transfer) || !isSameLeveHandle(transfer)) {
			return DNDService.LOGIC_UNKNOW;
		}

		if (target instanceof EditPart) {
			EditPart editPart = (EditPart) target;
			if (editPart.getModel() instanceof IVirtualValidator) {
				if (((IVirtualValidator) editPart.getModel()).handleValidate(transfer))
					return DNDService.LOGIC_TRUE;
				else
					return DNDService.LOGIC_FALSE;
			}
		}
		return DNDService.LOGIC_UNKNOW;
	}

	private boolean isLevelAttributeHandle(Object transfer) {
		if (transfer instanceof Object[]) {
			Object[] items = (Object[]) transfer;
			for (int i = 0; i < items.length; i++) {
				if (!isLevelAttributeHandle(items[i]))
					return false;
			}
			return true;
		}
		return transfer instanceof LevelAttributeHandle;
	}

	private boolean isSameLeveHandle(Object transfer) {
		if (!(transfer instanceof Object[])) {
			return true;
		}
		Object[] items = (Object[]) transfer;
		DesignElementHandle levelHandle = null;
		for (int i = 0; i < items.length; i++) {
			if (!(items[i] instanceof LevelAttributeHandle)) {
				return false;
			}
			LevelAttributeHandle attributeHandle = (LevelAttributeHandle) items[i];
			if (levelHandle == null) {
				levelHandle = attributeHandle.getElementHandle();
			} else if (levelHandle != attributeHandle.getElementHandle()) {
				return false;
			}

		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter#performDrop
	 * (java.lang.Object, java.lang.Object, int,
	 * org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation)
	 */
	public boolean performDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (transfer instanceof IAdaptable) {
			if (((IAdaptable) transfer).getAdapter(StructureHandle.class) instanceof LevelAttributeHandle) {
				transfer = ((IAdaptable) transfer).getAdapter(StructureHandle.class);
			}
		}
		if (target instanceof EditPart)// drop on layout
		{
			EditPart editPart = (EditPart) target;

			if (editPart != null) {
				CreateRequest request = new CreateRequest();

				request.getExtendedData().put(DesignerConstants.KEY_NEWOBJECT, transfer);
				request.setLocation(location.getPoint());
				Command command = editPart.getCommand(request);
				if (command != null && command.canExecute()) {
					CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
					stack.startTrans(Messages.getString("LevelHandleDropAdapter.ActionText")); //$NON-NLS-1$

					editPart.getViewer().getEditDomain().getCommandStack().execute(command);
					CrosstabReportItemHandle crosstab = getCrosstab(editPart);
					if (crosstab != null) {
						AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper(crosstab);
						providerWrapper.updateAllAggregationCells(AggregationCellViewAdapter.SWITCH_VIEW_TYPE);
					}
					stack.commit();
					return true;
				} else
					return false;
			}
		}
		return false;
	}

	private CrosstabReportItemHandle getCrosstab(EditPart editPart) {
		CrosstabReportItemHandle crosstab = null;
		Object tmp = editPart.getModel();
		if (!(tmp instanceof CrosstabCellAdapter)) {
			return null;
		}
		if (tmp instanceof VirtualCrosstabCellAdapter) {
			return ((VirtualCrosstabCellAdapter) tmp).getCrosstabReportItemHandle();
		}

		CrosstabCellHandle handle = ((CrosstabCellAdapter) tmp).getCrosstabCellHandle();
		if (handle != null) {
			crosstab = handle.getCrosstab();
		}

		return crosstab;

	}
}
