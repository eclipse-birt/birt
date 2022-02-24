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

package org.eclipse.birt.report.designer.ui.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.LibRootModel;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.IReportEditor;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ReportCreationTool;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.EditpartExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.PaletteEntryExtension;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.AbstractMultiPageEditor;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Abstract class for insert report element. Provides basic support for insert
 * actions.
 */

public abstract class BaseInsertMenuAction extends SelectionAction {

	protected static final String STACK_MSG_INSERT_ELEMENT = Messages
			.getString("BaseInsertMenuAction.stackMsg.insertElement"); //$NON-NLS-1$

	private String insertType;

	protected SlotHandle slotHandle;

	protected PropertyHandle propertyHandle;

	private Object model;

	/**
	 * The constructor.
	 * 
	 * @param part parent workbench part
	 * @param type insert element type
	 */
	public BaseInsertMenuAction(IWorkbenchPart part, String type) {
		super(part);

		this.insertType = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		slotHandle = getDefaultSlotHandle(insertType);
		propertyHandle = getDefaultPropertyHandle(insertType);
		Object obj = slotHandle;
		if (obj == null) {
			obj = model;
		}
		// return DNDUtil.handleValidateTargetCanContainType( slotHandle,
		// insertType )
		// && DNDUtil.handleValidateTargetCanContainMore( slotHandle, 0 );
		return DNDUtil.handleValidateTargetCanContainType(obj, insertType)
				&& DNDUtil.handleValidateTargetCanContainMore(obj, 0);
	}

	private PropertyHandle getDefaultPropertyHandle(String insertType) {
		IStructuredSelection models = InsertInLayoutUtil.editPart2Model(getSelection());
		if (models.isEmpty()) {
			return null;
		}
		model = DNDUtil.unwrapToModel(models.getFirstElement());
		if (model instanceof DesignElementHandle) {
			DesignElementHandle handle = (DesignElementHandle) model;
			String contentName = DEUtil.getDefaultContentName(handle);
			if (handle.canContain(contentName, insertType)) {
				return handle.getPropertyHandle(contentName);
			} else {
				model = handle.getContainer();
				return handle.getContainerPropertyHandle();
			}
		}
		return null;
	}

	/**
	 * Returns the container slotHandle.
	 * 
	 */
	private SlotHandle getDefaultSlotHandle(String insertType) {
		IStructuredSelection models = InsertInLayoutUtil.editPart2Model(getSelection());
		if (models.isEmpty()) {
			return null;
		}
		// model = models.getFirstElement( );
		model = DNDUtil.unwrapToModel(models.getFirstElement());
		if (model instanceof LibRootModel) {
			model = ((LibRootModel) model).getModel();
		}
		if (model instanceof SlotHandle) {
			return (SlotHandle) model;
		} else if (model instanceof DesignElementHandle) {
			DesignElementHandle handle = (DesignElementHandle) model;

			if (handle.getDefn().isContainer()) {
				int slotId = DEUtil.getDefaultSlotID(handle);
				if (handle.canContain(slotId, insertType)) {
					return handle.getSlot(slotId);
				}
			}
			return handle.getContainerSlotHandle();
		}
		return null;
	}

	protected Request insertElement() throws Exception {
		Request request = new Request(IRequestConstants.REQUEST_TYPE_INSERT);
		Map extendsData = new HashMap();
		if (slotHandle != null) {
			extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_SLOT, slotHandle);
		} else if (propertyHandle != null) {
			extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_PROPERTY, propertyHandle);
		}

		extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_TYPE, insertType);

		extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_POSITION, InsertAction.BELOW);

		request.setExtendedData(extendsData);

		// if ( ProviderFactory.createProvider( slotHandle.getElementHandle( ) )
		// .performRequest( model, request ) )
		Object obj = model;
		if (slotHandle != null) {
			obj = slotHandle.getElementHandle();
		}
		if (ProviderFactory.createProvider(obj).performRequest(model, request)) {
			return request;
		}
		return null;
	}

	protected void selectElement(final Object element, final boolean edit) {
		if (element instanceof ReportItemHandle) {
			IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().getActivePart();
			IEditorPart epart = null;
			if (part instanceof AbstractMultiPageEditor) {
				epart = ((AbstractMultiPageEditor) part).getActivePageInstance();
			} else if (part instanceof IReportEditor) {
				IEditorPart activeEditor = ((IReportEditor) part).getEditorPart();
				if (activeEditor instanceof AbstractMultiPageEditor) {
					epart = ((AbstractMultiPageEditor) activeEditor).getActivePageInstance();
				}
			}

			if (epart instanceof GraphicalEditorWithFlyoutPalette) {
				GraphicalViewer viewer = ((GraphicalEditorWithFlyoutPalette) epart).getGraphicalViewer();
				ReportCreationTool.selectAddedObject(element, viewer, edit);
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Insert action >> Run ..."); //$NON-NLS-1$
		}

		// experimental
		PaletteEntryExtension[] entries = EditpartExtensionManager.getPaletteEntries();
		for (int i = 0; i < entries.length; i++) {
			if (entries[i].getItemName().equals(this.insertType)) {
				try {
					selectElement(entries[i].executeCreate(), false);
				} catch (Exception e) {
					ExceptionHandler.handle(e);
				}
				return;
			}
		}
		if (SessionHandleAdapter.getInstance().getReportDesignHandle() == null) {
			return;
		}
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(STACK_MSG_INSERT_ELEMENT);

		try {
			final Request req = insertElement();
			if (req != null) {
				stack.commit();
				selectElement(req.getExtendedData().get(IRequestConstants.REQUEST_KEY_RESULT), true);
				return;
			}
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
		stack.rollback();
	}
}
