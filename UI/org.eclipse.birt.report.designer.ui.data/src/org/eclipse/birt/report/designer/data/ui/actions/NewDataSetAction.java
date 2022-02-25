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

package org.eclipse.birt.report.designer.data.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.data.ui.dataset.AdvancedDataSetEditor;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetEditor;
import org.eclipse.birt.report.designer.data.ui.dataset.DefaultDataSetWizard;
import org.eclipse.birt.report.designer.data.ui.datasource.DefaultDataSourceWizard;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

/**
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewDataSetAction extends Action implements UpdateAction {

	public static final String ID = "org.eclipse.birt.report.designer.ui.actions.NewDataSetAction"; //$NON-NLS-1$
	private DataSetHandle dataSetHandle;

	/**
	 * Constructor
	 */
	public NewDataSetAction() {
		super();
		setId(ID);
	}

	/**
	 * @param text
	 */
	public NewDataSetAction(String text) {
		super(text);
		setId(ID);
	}

	/**
	 * @param text
	 * @param style
	 */
	public NewDataSetAction(String text, int style) {
		super(text, style);
		setId(ID);
	}

	/**
	 * @param text
	 * @param image
	 */
	public NewDataSetAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.actions.UpdateAction#update()
	 */
	@Override
	public void update() {
		setEnabled(SessionHandleAdapter.getInstance().getReportDesignHandle() != null);
	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	@Override
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
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {

		if (!isEnabled()) {
			MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					Messages.getString("dataset.error.title.noReportDesign"), //$NON-NLS-1$
					Messages.getString("dataset.error.msg.noReportDesign"));//$NON-NLS-1$
			return;
		}

		if (Policy.TRACING_ACTIONS) {
			System.out.println("New data set action >> Run ..."); //$NON-NLS-1$
		}
		// Fix Bugzilla Bug 116583
		// Start a persistent.
		SessionHandleAdapter.getInstance().getCommandStack().startPersistentTrans(Messages.getString("dataset.new")); //$NON-NLS-1$

		// Check if data Sources are available
		if (HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().getModuleHandle().getVisibleDataSources()
				.isEmpty()) {
			boolean createNewDataSource = MessageDialog.openQuestion(
					PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					Messages.getString("dataset.error.title.noDataSources"), //$NON-NLS-1$
					Messages.getString("dataset.error.noDataSources"));//$NON-NLS-1$

			if (createNewDataSource) {
				DefaultDataSourceWizard wizard = new DefaultDataSourceWizard();
				String wizardTitle = Messages.getString("datasource.new");//$NON-NLS-1$
				wizard.setWindowTitle(wizardTitle);
				WizardDialog dialog = new BaseWizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
						wizard);
				if (dialog.open() == WizardDialog.CANCEL) {
					notifyResult(false);
					SessionHandleAdapter.getInstance().getCommandStack().rollback();
				} else {
					createNewDataSet();
				}
			} else {
				notifyResult(false);
				SessionHandleAdapter.getInstance().getCommandStack().rollback();
			}
		} else {
			createNewDataSet();
		}
	}

	private void createNewDataSet() {
		DefaultDataSetWizard wizard = new DefaultDataSetWizard();
		wizard.setWindowTitle(Messages.getString("dataset.new"));//$NON-NLS-1$
		WizardDialog dialog = new BaseWizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), wizard);

		if (dialog.open() == WizardDialog.OK) {
			DataSetHandle ds = wizard.getNewCreateDataSetHandle();
			if (editDataSet(ds)) {
				notifyResult(true);
			} else {
				notifyResult(false);
			}
			ReportRequest request = new ReportRequest(ReportRequest.CREATE_ELEMENT);
			List selectionObjects = new ArrayList();
			selectionObjects.add(dataSetHandle);
			request.setSelectionObject(selectionObjects);
			SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);
			SessionHandleAdapter.getInstance().getCommandStack().commit();
		} else {
			notifyResult(false);
			SessionHandleAdapter.getInstance().getCommandStack().rollback();
		}
	}

	private boolean editDataSet(DataSetHandle ds) {
		dataSetHandle = ds;

		// The last element was the One added
		// DataSetHandle dataSetHandle = (DataSetHandle) newDataSets.get(
		// newDataSets.size( ) - 1 );
		// Edit the added DataSet if it is not a script data set.
		if ((dataSetHandle == null) || (dataSetHandle instanceof ScriptDataSetHandle)) {
			return false;
		}
		DataSetEditor dialog = new AdvancedDataSetEditor(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				dataSetHandle, true, true);
		return (dialog.open() == Window.OK);
	}
}
