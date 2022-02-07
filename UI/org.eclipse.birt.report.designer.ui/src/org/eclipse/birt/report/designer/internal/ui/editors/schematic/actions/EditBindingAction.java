/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.logging.Level;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Edit biding action
 */
public class EditBindingAction extends InsertRowAction {

	public static final String ID = "org.eclipse.birt.report.designer.action.editBinding"; //$NON-NLS-1$

	/**
	 * @param part
	 */
	public EditBindingAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(Messages.getString("DesignerActionBarContributor.menu.element.editDataBinding")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		return moduleHandle != null && (!moduleHandle.getVisibleDataSets().isEmpty()
				|| (getSelectedElement() != null && getSelectedElement().getDataSet() != null));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Edit binding action >> Run ..."); //$NON-NLS-1$
		}

		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.editBindingCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
