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

package org.eclipse.birt.report.designer.ui.samplesview.action;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.samplesview.util.PlaceResources;
import org.eclipse.birt.report.designer.ui.samplesview.view.ReportExamples;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

public class ExportSampleReportAction extends Action implements Listener {

	private static final String DRILL_TO_DETAILS_CATEGORY = "Drill to Details"; //$NON-NLS-1$
	private static final String[] REPORTDESIGN_FILENAME_PATTERN = { "*.rptdesign" //$NON-NLS-1$
	};

	private static final String ACTION_TEXT = Messages.getString("SampleReportsView.Action.exportSampleReport"); //$NON-NLS-1$

	private ReportExamples composite;

	public ExportSampleReportAction(ReportExamples composite) {
		super(ACTION_TEXT);
		setToolTipText(Messages.getString("SampleReportsView.Action.exportSampleReport.toolTipText")); //$NON-NLS-1$
		setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_ENABLE_EXPORT));
		setDisabledImageDescriptor(
				ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_DISABLE_EXPORT));
		setEnabled(false);
		this.composite = composite;
		composite.addSelectedListener(this);
	}

	@Override
	public void run() {
		Object selectedElement = ((TreeItem) composite.getSelectedElement()).getData();
		if (selectedElement == null || !(selectedElement instanceof ReportDesignHandle)) {
			return;
		}

		String filename = ((ReportDesignHandle) selectedElement).getFileName();
		String reportName = filename.substring(filename.lastIndexOf("/") + 1); //$NON-NLS-1$
		final FileDialog saveDialog = new FileDialog(composite.getShell(), SWT.SAVE);
		saveDialog.setFilterExtensions(REPORTDESIGN_FILENAME_PATTERN);
		saveDialog.setFileName(reportName);
		if (saveDialog.open() == null) {
			return;
		}

		PlaceResources.copy(composite.getShell(), saveDialog.getFilterPath(), saveDialog.getFileName(), filename);

		PlaceResources.copyExcludedRptDesignes(composite.getShell(), saveDialog.getFilterPath(), filename, true);

		if (((TreeItem) composite.getSelectedElement()).getParentItem().getText().equals(DRILL_TO_DETAILS_CATEGORY)) {
			PlaceResources.copyDrillThroughReport(composite.getShell(), saveDialog.getFilterPath(), reportName);
		}
	}

	@Override
	public void handleEvent(Event event) {
		if (event.widget == null || !(event.widget instanceof TreeItem)) {
			setEnabled(false);
		}
		TreeItem item = (TreeItem) event.widget;
		if (item == null) {
			super.setEnabled(false);
			return;
		}
		Object selectedElement = item.getData();
		if (selectedElement == null) {
			super.setEnabled(false);
		} else {
			super.setEnabled(selectedElement instanceof ReportDesignHandle);
		}
	}
}
