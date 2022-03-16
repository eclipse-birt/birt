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
import org.eclipse.birt.report.designer.data.ui.dataset.JointDataSetWizard;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * New Joint dataset action for creating joint dataset from two datasets
 * Preferences - Java - Code Style - Code Templates
 */
public class NewJointDataSetAction extends Action {

	public static final String ID = "org.eclipse.birt.report.designer.ui.actions.NewJointDataSetAction"; //$NON-NLS-1$
	private DataSetHandle dataSetHandle;

	/**
	 *
	 */
	public NewJointDataSetAction() {
		super();
		setId(ID);
	}

	/**
	 * @param text
	 */
	public NewJointDataSetAction(String text) {
		super(text);
		setId(ID);
	}

	/**
	 * @param text
	 * @param style
	 */
	public NewJointDataSetAction(String text, int style) {
		super(text, style);
		setId(ID);
	}

	/**
	 * @param text
	 * @param image
	 */
	public NewJointDataSetAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	@Override
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("New joint data set action >> Run ..."); //$NON-NLS-1$
		}
		if (SessionHandleAdapter.getInstance().getReportDesignHandle() == null) {
			return;
		}
		// Get the list of data sets before inserting a new Data Set
		List existingDataSets = getDataSets();

		// Check if data sets are available
		if (existingDataSets.isEmpty()) {
			MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					Messages.getString("JointDataSetPage.error.nodataset.title"), //$NON-NLS-1$
					Messages.getString("JointDataSetPage.error.nodataset.title"));//$NON-NLS-1$
			notifyResult(false);
		} else {
			if (SessionHandleAdapter.getInstance().getReportDesignHandle() == null) {
				return;
			}
			HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().getModuleHandle().getCommandStack()
					.startPersistentTrans(Messages.getString("dataset.join.new")); //$NON-NLS-1$

			JointDataSetWizard wizard = new JointDataSetWizard();
			wizard.setWindowTitle(Messages.getString("dataset.join.new"));//$NON-NLS-1$
			WizardDialog dialog = new BaseWizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					wizard) {

				@Override
				protected void configureShell(Shell newShell) {
					super.configureShell(newShell);
					newShell.setSize(750, 650);
					Rectangle rect = Display.getDefault().getBounds();
					newShell.setLocation((rect.width - 750) / 2, (rect.height - 650) / 2);
				}

			};

			if (dialog.open() == WizardDialog.OK) {
				// Get the list of data sets after inserting a new Data Set
				List newDataSets = getDataSets();

				editDataSet(existingDataSets, newDataSets);

				HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().getModuleHandle().getCommandStack()
						.commit();

				ReportRequest request = new ReportRequest(ReportRequest.CREATE_ELEMENT);
				List selectionObjects = new ArrayList();
				selectionObjects.add(dataSetHandle);
				request.setSelectionObject(selectionObjects);
				SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);

				notifyResult(true);
			}

			else {
				HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().getModuleHandle().getCommandStack()
						.rollback();
				notifyResult(false);
			}
		}
	}

	private List getDataSets() {

		List dataSets = HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().getModuleHandle()
				.getVisibleDataSets();

		return dataSets;

	}

	private boolean editDataSet(List existingDataSets, List newDataSets) {
		if (existingDataSets == null || newDataSets == null || (newDataSets.size() <= existingDataSets.size())) {
			return false;
		}

		dataSetHandle = findNewDataSet(existingDataSets, newDataSets);

		if (dataSetHandle == null) {
			return false;
		}
		// The last element was the One added
		// DataSetHandle dataSetHandle = (DataSetHandle) newDataSets.get(
		// newDataSets.size( ) - 1 );
		// Edit the added DataSet if it is not a script data set.
		if (dataSetHandle instanceof ScriptDataSetHandle) {
			return false;
		}
		DataSetEditor dialog = new AdvancedDataSetEditor(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				dataSetHandle, false, true);
		return (dialog.open() == Window.OK);
	}

	private DataSetHandle findNewDataSet(List existingDataSets, List newDataSets) {
		for (int i = 0; i < newDataSets.size(); i++) {
			if (!existingDataSets.contains(newDataSets.get(i))) {
				return (DataSetHandle) newDataSets.get(i);
			}
		}
		return null;
	}
}
