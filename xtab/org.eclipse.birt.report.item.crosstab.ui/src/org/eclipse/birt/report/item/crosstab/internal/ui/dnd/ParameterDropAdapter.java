/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.internal.ui.dnd;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 *
 */

public class ParameterDropAdapter implements IDropAdapter {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter#canDrop(java.
	 * lang.Object, java.lang.Object, int,
	 * org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation)
	 */
	@Override
	public int canDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (!isScalarParameterHandle(transfer)) {
			return DNDService.LOGIC_UNKNOW;
		}
		if (target instanceof EditPart) {
			EditPart editPart = (EditPart) target;
			if (editPart.getModel() instanceof IVirtualValidator) {
				if (((IVirtualValidator) editPart.getModel()).handleValidate(transfer)) {
					return DNDService.LOGIC_TRUE;
				} else {
					return DNDService.LOGIC_FALSE;
				}
			}
		}
		return DNDService.LOGIC_UNKNOW;
	}

	private boolean isScalarParameterHandle(Object transfer) {
		if (transfer instanceof Object[]) {
			DesignElementHandle container = null;
			Object[] items = (Object[]) transfer;
			for (int i = 0; i < items.length; i++) {
				if (!(items[i] instanceof ScalarParameterHandle)) {
					return false;
				}

			}
			return true;
		}
		return transfer instanceof ScalarParameterHandle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter#performDrop(
	 * java.lang.Object, java.lang.Object, int,
	 * org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation)
	 */
	@Override
	public boolean performDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (target instanceof EditPart)// drop on layout
		{
			EditPart editPart = (EditPart) target;

			if (editPart != null) {
				CreateRequest request = new CreateRequest();
				if (transfer instanceof Object[]) {
					Object[] newObjs = (Object[]) transfer;
					transfer = UIUtil.getInsertPamaterElements(newObjs);
				}
				try {
					if (transfer instanceof ScalarParameterHandle) {
						transfer = InsertInLayoutUtil.performInsertParameter((ScalarParameterHandle) transfer);
					} else if (transfer instanceof Object[]) {
						Object[] objs = (Object[]) transfer;
						Object[] copys = new Object[objs.length];
						for (int i = 0; i < objs.length; i++) {
							if (objs[i] instanceof ScalarParameterHandle) {
								copys[i] = InsertInLayoutUtil.performInsertParameter((ScalarParameterHandle) objs[i]);
							} else {
								// Return now , don't support the other type
								return false;
							}

						}

						transfer = copys;
					}

				} catch (SemanticException e) {
					// do nothing
					return false;
				}
				request.getExtendedData().put(DesignerConstants.KEY_NEWOBJECT, transfer);
				request.setLocation(location.getPoint());
				Command command = editPart.getCommand(request);
				if (command != null && command.canExecute()) {
					CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
					stack.startTrans(Messages.getString("LevelHandleDropAdapter.ActionText")); //$NON-NLS-1$

					editPart.getViewer().getEditDomain().getCommandStack().execute(command);
					stack.commit();
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

}
