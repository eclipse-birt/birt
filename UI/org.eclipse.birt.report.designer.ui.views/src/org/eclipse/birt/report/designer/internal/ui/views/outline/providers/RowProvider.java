/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import java.util.Map;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Deals with the table row provider
 * 
 * 
 */
public class RowProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry for the given object and adds them to the menu
	 * 
	 * @param menu   the menu
	 * @param object the object
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		menu.add(new InsertAction(object, Messages.getString("RowProvider.action.text.above"), //$NON-NLS-1$
				ReportDesignConstants.ROW_ELEMENT, InsertAction.ABOVE));
		menu.add(new InsertAction(object, Messages.getString("RowProvider.action.text.below"), //$NON-NLS-1$
				ReportDesignConstants.ROW_ELEMENT, InsertAction.BELOW));
		super.createContextMenu(sourceViewer, object, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.outline.providers.
	 * INodeProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object model) {
		RowHandle tableRow = (RowHandle) model;
		return this.getChildrenBySlotHandle(tableRow.getCells());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * performInsert(java.lang.Object, org.eclipse.birt.model.api.SlotHandle,
	 * java.lang.String, java.lang.String)
	 */
	protected boolean performInsert(Object model, SlotHandle slotHandle, String type, String position, Map extendedData)
			throws Exception {
		Assert.isLegal(ReportDesignConstants.ROW_ELEMENT.equals(type));
		TableHandleAdapter adapter = HandleAdapterFactory.getInstance().getTableHandleAdapter(getRoot(model));
		if (!StringUtil.isEqual(position, InsertAction.CURRENT)) {
			int rowNumber = HandleAdapterFactory.getInstance().getRowHandleAdapter(model).getRowNumber();
			if (StringUtil.isEqual(position, InsertAction.ABOVE)) {
				adapter.insertRow(-1, rowNumber);
			} else if (StringUtil.isEqual(position, InsertAction.BELOW)) {
				adapter.insertRow(1, rowNumber);
			} else {
				return false;
			}
		} else {
			adapter.insertRowInSlotHandle(slotHandle.getSlotID());
		}
		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * performDelete(org.eclipse.birt.model.api.DesignElementHandle)
	 */
	protected boolean performDelete(DesignElementHandle handle) throws SemanticException {
		int rowNumber = HandleAdapterFactory.getInstance().getRowHandleAdapter(handle).getRowNumber();
		TableHandleAdapter adapter = HandleAdapterFactory.getInstance().getTableHandleAdapter(getRoot(handle));
		adapter.deleteRow(rowNumber);
		return true;
	}

	/**
	 * Gets the root element of the row
	 * 
	 * @param model the mode
	 * @return the root element of the row
	 */
	private Object getRoot(Object model) {
		DesignElementHandle handle = ((DesignElementHandle) model).getContainer();
		if (handle instanceof GroupHandle) {
			return getRoot(handle);
		}
		return handle;
	}
}
