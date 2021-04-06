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

package org.eclipse.birt.report.item.crosstab.internal.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.commands.CreateCommand;
import org.eclipse.birt.report.designer.core.model.LibraryHandleAdapter;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.CreateRequest;

/**
 * CreateCrosstabHandler
 */
public class CreateCrosstabHandler extends AbstractHandler {

	// private static String itemName = "Crosstab";

	public Object execute(ExecutionEvent event) throws ExecutionException {
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(Messages.getString("InsertAction.text")); //$NON-NLS-1$
		ExtendedItemHandle handle = null;

		try {
			String name = ReportPlugin.getDefault().getCustomName(ICrosstabConstants.CROSSTAB_EXTENSION_NAME);

			handle = CrosstabExtendedItemFactory
					.createCrosstabReportItem(SessionHandleAdapter.getInstance().getReportDesignHandle(), null, name);
		} catch (Exception e) {
			stack.rollback();

			throw new ExecutionException(e.getLocalizedMessage(), e);
		}

		EditPart targetEditPart = null;

		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		Object object = UIUtil.getVariableFromContext(context, "targetEditPart"); //$NON-NLS-1$

		if (object instanceof EditPart) {
			targetEditPart = (EditPart) object;
		} else {
			targetEditPart = UIUtil.getCurrentEditPart();
		}

		Object parentModel = DNDUtil.unwrapToModel(targetEditPart.getModel());

		Object request = UIUtil.getVariableFromContext(context, "request"); //$NON-NLS-1$

		if (request instanceof CreateRequest) {
			((CreateRequest) request).getExtendedData().put(DesignerConstants.KEY_NEWOBJECT, handle);

			try {
				targetEditPart.getCommand(((CreateRequest) request)).execute();
				stack.commit();
			} catch (Exception e) {
				stack.rollback();
			}
		} else {
			Map map = new HashMap();
			map.put(DesignerConstants.KEY_NEWOBJECT, handle);
			CreateCommand command = new CreateCommand(map);

			try {
				if (parentModel instanceof DesignElementHandle) {
					DesignElementHandle parentHandle = (DesignElementHandle) parentModel;
					if (parentHandle.getDefn().isContainer()
							&& (parentHandle.canContain(DEUtil.getDefaultSlotID(parentHandle), handle)
									|| parentHandle.canContain(DEUtil.getDefaultContentName(parentHandle), handle))) {
						command.setParent(parentHandle);
					} else {
						if (parentHandle.getContainerSlotHandle() != null) {
							command.setAfter(parentHandle.getContainerSlotHandle().get(parentHandle.getIndex() + 1));
						} else if (parentHandle.getContainerPropertyHandle() != null) {
							command.setAfter(
									parentHandle.getContainerPropertyHandle().get(parentHandle.getIndex() + 1));
						}

						DesignElementHandle container = parentHandle.getContainer();

						// special handling for list item, always use slothandle
						// as parent
						if (container instanceof ListHandle) {
							command.setParent(parentHandle.getContainerSlotHandle());
						} else {
							command.setParent(container);
						}
					}
				} else if (parentModel instanceof SlotHandle) {
					command.setParent(parentModel);
				} else {
					command.setParent(SessionHandleAdapter.getInstance().getReportDesignHandle());
				}
				command.execute();
				stack.commit();
			} catch (Exception e) {
				stack.rollback();
			}
		}

		// if parent is library, select new object
		if (parentModel instanceof LibraryHandle) {
			try {
				HandleAdapterFactory.getInstance().getLibraryHandleAdapter().setCurrentEditorModel(handle,
						LibraryHandleAdapter.CREATE_ELEMENT);
			} catch (Exception e) {
			}
		}
		return handle;
	}

}
