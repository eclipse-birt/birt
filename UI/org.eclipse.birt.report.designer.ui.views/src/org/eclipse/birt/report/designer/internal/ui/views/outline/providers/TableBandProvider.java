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
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Provider for the Table item node
 *
 *
 */
public class TableBandProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the given object.
	 *
	 * @param menu   the menu
	 * @param object the object
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		if (!(object instanceof SlotHandle)) {
			return;
		}
		SlotHandle model = (SlotHandle) object;
		if (((ReportElementHandle) model.getElementHandle()).isValidLayoutForCompoundElement()) {
			if (model.getElementHandle() instanceof TableHandle && model.getSlotID() == TableHandle.GROUP_SLOT) {
				InsertAction insertAction = new InsertAction(object,
						Messages.getString("TableBandProvider.action.text.group"), //$NON-NLS-1$
						ReportDesignConstants.TABLE_GROUP_ELEMENT);
				menu.add(insertAction);

			} else {
				menu.add(new InsertAction(object, Messages.getString("TableBandProvider.action.text.row"), //$NON-NLS-1$
						ReportDesignConstants.ROW_ELEMENT));
			}
		}
		super.createContextMenu(sourceViewer, object, menu);

	}

	/**
	 * Gets the display name of the node
	 *
	 * @param object the object
	 */
	@Override
	public String getNodeDisplayName(Object object) {
		SlotHandle model = (SlotHandle) object;
		if (model.getElementHandle() instanceof TableHandle) {
			switch (model.getSlotID()) {
			case TableHandle.HEADER_SLOT:
				return HEADER_DISPALYNAME;
			case TableHandle.FOOTER_SLOT:
				return FOOTER_DISPALYNAME;
			case TableHandle.DETAIL_SLOT:
				return DETAIL_DISPALYNAME;
			case TableHandle.GROUP_SLOT:
				return GROUPS_DISPALYNAME;
			}
		} else if (model.getElementHandle() instanceof TableGroupHandle) {
			switch (model.getSlotID()) {
			case TableGroupHandle.HEADER_SLOT:
				return HEADER_DISPALYNAME;
			case TableGroupHandle.FOOTER_SLOT:
				return FOOTER_DISPALYNAME;
			}
		}
		return super.getNodeDisplayName(model);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getIconName(
	 * java.lang.Object)
	 */
	@Override
	public String getIconName(Object object) {
		SlotHandle model = (SlotHandle) object;
		if (model.getElementHandle() instanceof TableHandle) {
			switch (model.getSlotID()) {
			case TableHandle.HEADER_SLOT:
				return IReportGraphicConstants.ICON_NODE_HEADER;
			case TableHandle.FOOTER_SLOT:
				return IReportGraphicConstants.ICON_NODE_FOOTER;
			case TableHandle.DETAIL_SLOT:
				return IReportGraphicConstants.ICON_NODE_DETAILS;
			case TableHandle.GROUP_SLOT:
				return IReportGraphicConstants.ICON_NODE_GROUPS;
			}
		} else if (model.getElementHandle() instanceof TableGroupHandle) {
			switch (model.getSlotID()) {
			case TableGroupHandle.HEADER_SLOT:
				return IReportGraphicConstants.ICON_NODE_GROUP_HEADER;
			case TableGroupHandle.FOOTER_SLOT:
				return IReportGraphicConstants.ICON_NODE_GROUP_FOOTER;
			}
		}
		return super.getIconName(model);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * performInsert(java.lang.Object, org.eclipse.birt.model.api.SlotHandle,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	protected boolean performInsert(Object model, SlotHandle slotHandle, String type, String position, Map extendData)
			throws Exception {

		if (ReportDesignConstants.ROW_ELEMENT.equals(type)) {
			TableHandleAdapter adapter = HandleAdapterFactory.getInstance()
					.getTableHandleAdapter(getRoot(((SlotHandle) model).getElementHandle()));
			if (slotHandle.getCount() > 0) {
				int rowNumber = HandleAdapterFactory.getInstance()
						.getRowHandleAdapter(slotHandle.get(slotHandle.getCount() - 1)).getRowNumber();

				adapter.insertRow(1, rowNumber);

				// get TableHandle and add it to extendData
				Object obj = adapter.getRow(rowNumber + 1);
				if ((obj == null) || (!(obj instanceof RowHandle))) {
					return true;
				}
				RowHandle rowHandle = (RowHandle) obj;
				if (extendData != null) {
					extendData.put(IRequestConstants.REQUEST_KEY_RESULT, rowHandle);
				}

			} else {
				DesignElementHandle elementHandle = createElement(slotHandle, type);

				if (extendData != null) {
					extendData.put(IRequestConstants.REQUEST_KEY_RESULT, elementHandle);
				}

				slotHandle.add(elementHandle);
			}
			return true;
		} else if (ReportDesignConstants.TABLE_GROUP_ELEMENT.equals(type)) {
			return UIUtil.createGroup(((SlotHandle) model).getElementHandle());
		}
		return super.performInsert(model, slotHandle, type, position, extendData);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * createElement(java.lang.Object, java.lang.String)
	 */
	protected DesignElementHandle createElement(SlotHandle slotHandle, String type) throws Exception {
		DesignElementHandle handle = (DesignElementHandle) getRoot(slotHandle.getElementHandle());
		if (type.equals(ReportDesignConstants.ROW_ELEMENT)) {
			// return handle.getElementFactory( )
			// .newTableRow( ( (TableHandle) handle ).getColumnCount( ) );
			return DesignElementFactory.getInstance(handle.getModuleHandle())
					.newTableRow(((TableHandle) handle).getColumnCount());
		}
		return super.createElement(slotHandle, type);
	}

	/**
	 * Gets the root element of the row
	 *
	 * @param model the mode
	 * @return the root element of the row
	 */
	private Object getRoot(Object model) {
		// if handle is Table,already get the root
		// stop the search.
		if (model instanceof TableHandle) {
			return model;
		}
		DesignElementHandle handle = ((DesignElementHandle) model).getContainer();
		if (handle instanceof GroupHandle) {
			return getRoot(handle);
		}
		return handle;
	}
}
