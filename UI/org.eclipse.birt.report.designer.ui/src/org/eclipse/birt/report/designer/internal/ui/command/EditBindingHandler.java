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
/**
 *
 */

package org.eclipse.birt.report.designer.internal.ui.command;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.DataBindingDialog;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

/**
 * @author Administrator
 *
 */
public class EditBindingHandler extends SelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		// Get the first item in the list and pass the model object to the
		// dialog
		TableEditPart editPart = getTableEditPart();

		ListEditPart listPart = getListEditPart();

		ReportElementEditPart part = getTableMultipleEditPart();

		if (editPart != null || listPart != null || part != null) {
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

			stack.startTrans(Messages.getString("DataEditPart.stackMsg.edit")); //$NON-NLS-1$
			DataBindingDialog dialog = new DataBindingDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					getSelectedElement());

			if (dialog.open() == Dialog.OK) {
				stack.commit();
			} else {
				stack.rollback();
			}
		}

		return Boolean.TRUE;
	}

	private ReportItemHandle getSelectedElement() {
		if (getTableEditPart() != null && getTableEditPart().getModel() instanceof ReportItemHandle) {
			return (ReportItemHandle) getTableEditPart().getModel();

		}

		if (getTableMultipleEditPart() != null && getTableMultipleEditPart().getModel() instanceof ReportItemHandle) {
			return (ReportItemHandle) getTableMultipleEditPart().getModel();

		}

		if (getListEditPart() != null && getListEditPart().getModel() instanceof ReportItemHandle) {
			return (ReportItemHandle) getListEditPart().getModel();

		}

		return null;
	}
}
