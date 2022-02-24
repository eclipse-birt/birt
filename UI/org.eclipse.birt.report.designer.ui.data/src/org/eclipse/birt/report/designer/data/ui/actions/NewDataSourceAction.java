/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.data.ui.datasource.DefaultDataSourceWizard;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

public class NewDataSourceAction extends Action implements UpdateAction {

	public static final String ID = "org.eclipse.birt.report.designer.ui.actions.NewDataSourceAction"; //$NON-NLS-1$

	/**
	 * 
	 */
	public NewDataSourceAction() {
		super();
		setId(ID);
	}

	/**
	 * @param text
	 */
	public NewDataSourceAction(String text) {
		super(text);
		setId(ID);
	}

	/**
	 * @param text
	 * @param style
	 */
	public NewDataSourceAction(String text, int style) {
		super(text, style);
		setId(ID);
	}

	/**
	 * @param text
	 * @param image
	 */
	public NewDataSourceAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	public boolean isEnabled() {
		ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		if (moduleHandle == null) {
			return false;
		}
		return super.isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		if (!isEnabled()) {
			MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					Messages.getString("datasource.error.title.noReportDesign"), //$NON-NLS-1$
					Messages.getString("datasource.error.msg.noReportDesign"));//$NON-NLS-1$
			return;
		}

		if (Policy.TRACING_ACTIONS) {
			System.out.println("New data source action >> Run ..."); //$NON-NLS-1$
		}

		// Get the list of data sets before inserting a new Data Set
		List existingDataSources = getDataSources();

		// Fix Bugzilla Bug 192360
		// Start a persistent transaction. This is to make sure transaction
		// of creating data source is consistent with creating data set
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

		stack.startPersistentTrans(Messages.getString("datasource.new")); //$NON-NLS-1$
		DefaultDataSourceWizard wizard = new DefaultDataSourceWizard();
		String wizardTitle = Messages.getString("datasource.new");//$NON-NLS-1$
		wizard.setWindowTitle(wizardTitle);
		WizardDialog dialog = new BaseWizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), wizard);

		if (dialog.open() == WizardDialog.OK) {
			List newDataSources = getDataSources();
			DataSourceHandle dataSource = findNewDataSource(existingDataSources, newDataSources);

			stack.commit();

			ReportRequest request = new ReportRequest(ReportRequest.CREATE_ELEMENT);
			List selectionObjects = new ArrayList();
			selectionObjects.add(dataSource);
			request.setSelectionObject(selectionObjects);
			SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);
			notifyResult(true);
		} else {
			stack.rollback();
			notifyResult(false);
		}

	}

	private List getDataSources() {
		return DEUtil.getDataSources();
	}

	private DataSourceHandle findNewDataSource(List existingDataSources, List newDataSources) {
		for (int i = 0; i < newDataSources.size(); i++) {
			if (!existingDataSources.contains(newDataSources.get(i))) {
				return (DataSourceHandle) newDataSources.get(i);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.UpdateAction#update()
	 */
	public void update() {
		setEnabled(SessionHandleAdapter.getInstance().getReportDesignHandle() != null);
	}
}
