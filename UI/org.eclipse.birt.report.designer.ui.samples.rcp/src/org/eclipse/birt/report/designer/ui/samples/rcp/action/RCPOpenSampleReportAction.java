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

package org.eclipse.birt.report.designer.ui.samples.rcp.action;

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportEditorInput;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.samplesview.action.IOpenSampleReportAction;
import org.eclipse.birt.report.designer.ui.samplesview.util.PlaceResources;
import org.eclipse.birt.report.designer.ui.samplesview.view.ReportExamples;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class RCPOpenSampleReportAction extends Action implements IOpenSampleReportAction, Listener {

	private static final String ACTION_TEXT = Messages.getString("SampleReportsView.Action.openSampleReport"); //$NON-NLS-1$

	private static final String DRILL_TO_DETAILS_CATEGORY = "Drill to Details"; //$NON-NLS-1$

	private ReportExamples composite;

	public RCPOpenSampleReportAction() {
		super(ACTION_TEXT);
		setToolTipText(Messages.getString("SampleReportsView.Action.openSampleReport.toolTipText.rcp")); //$NON-NLS-1$
		setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_ENABLE_IMPORT));
		setDisabledImageDescriptor(
				ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_DISABLE_IMPORT));
		setEnabled(false);
	}

	public void setMainComposite(ReportExamples composite) {
		this.composite = composite;
		composite.addSelectedListener(this);
	}

	public void run() {
		TreeItem item = (TreeItem) composite.getSelectedElement();
		final Object selectedElement = item.getData();
		if (selectedElement == null || !(selectedElement instanceof ReportDesignHandle)) {
			return;
		}

		PlaceResources.copy(composite.getShell(), getDefaultLocation(), item.getText(),
				((ReportDesignHandle) selectedElement).getFileName());

		if (item.getParentItem().getText().equals(DRILL_TO_DETAILS_CATEGORY)) {
			PlaceResources.copyDrillThroughReport(composite.getShell(), getDefaultLocation(), item.getText());
		}

		PlaceResources.copyExcludedRptDesignes(composite.getShell(), getDefaultLocation(),
				((ReportDesignHandle) selectedElement).getFileName(), true);

		ISafeRunnable op = new ISafeRunnable() {

			public void run() {
				String fileName = ((ReportDesignHandle) selectedElement).getFileName();
				doFinish(getDefaultLocation(), fileName.substring(fileName.lastIndexOf('/') + 1));
			}

			public void handleException(Throwable exception) {
				ExceptionUtil.handle(exception);
			}
		};
		SafeRunner.run(op);

	}

	private void doFinish(String locationPath, String fileName) {

		final File file = new File(locationPath, fileName);
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

				IWorkbenchPage page = window.getActivePage();
				try {
					// sanity checks
					if (page == null) {
						throw new IllegalArgumentException();
					}

					// open the editor on the file
					page.openEditor(new ReportEditorInput(file), IReportEditorContants.DESIGN_EDITOR_ID, true);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		});
	}

	private String getDefaultLocation() {
		IPath defaultPath = Platform.getLocation();
		return defaultPath.toOSString();
	}

	public void handleEvent(Event event) {
		if (event.widget == null || !(event.widget instanceof TreeItem))
			setEnabled(false);
		TreeItem item = (TreeItem) event.widget;
		if (item == null) {
			super.setEnabled(false);
			return;
		}
		Object selectedElement = item.getData();
		if (selectedElement == null)
			super.setEnabled(false);
		else
			super.setEnabled(selectedElement instanceof ReportDesignHandle);
	}

}
