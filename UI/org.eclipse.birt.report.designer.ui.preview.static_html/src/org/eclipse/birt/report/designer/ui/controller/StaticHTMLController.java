/*************************************************************************************
 * Copyright (c) 2006 Actuate Corporation and others.
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

package org.eclipse.birt.report.designer.ui.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.preview.editors.AbstractViewer;
import org.eclipse.birt.report.designer.ui.preview.editors.SWTAbstractViewer;
import org.eclipse.birt.report.designer.ui.preview.extension.IViewer;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Widget;

/**
 * This class provides a controller pane.
 */
public class StaticHTMLController implements IController {

	private final Display display = Display.getCurrent();
	private final List buttons = new ArrayList();
	private Composite pane = null;
	private ProgressBar progressBar = null;
	private IViewer viewer = null;
	private IEngineTask engineTask;
	private String reportFile;

	/**
	 * Creates controller.
	 */
	public StaticHTMLController() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.controller.IController#setViewer(org.
	 * eclipse.birt.report.designer.ui.preview.extension.IViewer)
	 */
	@Override
	public void setViewer(IViewer viewer) {
		this.viewer = viewer;

		if (!(viewer instanceof AbstractViewer)) {
			return;
		}

		Composite viewerUI = (Composite) ((SWTAbstractViewer) viewer).getUI();

		if (viewerUI == null) {
			return;
		}
		pane = new Composite(viewerUI, SWT.NONE);
		pane.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.HORIZONTAL;
		rowLayout.spacing = 5;
		pane.setLayout(rowLayout);

		for (Iterator iter = buttons.iterator(); iter.hasNext();) {
			ButtonInfo buttonInfo = (ButtonInfo) iter.next();
			Button button = new Button(pane, SWT.PUSH);

			button.setText(buttonInfo.text);
			button.setToolTipText(buttonInfo.toolTip);
			if (buttonInfo.selectionListener != null) {
				button.addSelectionListener(buttonInfo.selectionListener);
			}
		}

		GridData gd = new GridData(GridData.END, GridData.CENTER, false, false);
		gd.heightHint = 10;
		gd.widthHint = 100;
		progressBar = new ProgressBar(viewerUI, SWT.INDETERMINATE);
		progressBar.setLayoutData(gd);
		setBusy(true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.controller.IController#getPane()
	 */
	@Override
	public Widget getPane() {
		if (viewer != null) {
			return pane;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.controller.IController#setBusy(boolean)
	 */
	@Override
	public void setBusy(final boolean b) {
		if (display != null) {
			display.asyncExec(new Runnable() {

				/*
				 * (non-Javadoc)
				 *
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					if (progressBar != null && !progressBar.isDisposed()) {
						progressBar.setVisible(b);
					}
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.preview.extension.IController#addButton(
	 * java.lang.String, java.lang.String, org.eclipse.swt.events.SelectionListener)
	 */
	@Override
	public void addButton(String text, String toolTip, SelectionListener listener) {
		buttons.add(new ButtonInfo(text, toolTip, listener));
	}

	private static class ButtonInfo {

		String text = ""; //$NON-NLS-1$
		String toolTip = ""; //$NON-NLS-1$
		SelectionListener selectionListener = null;

		private ButtonInfo(String text, String toolTip, SelectionListener selectionListener) {
			this.text = text;
			this.toolTip = toolTip;
			this.selectionListener = selectionListener;
		}
	}

	public IEngineTask getEngineTask() {
		return engineTask;
	}

	public void setEngineTask(IEngineTask engindTask) {
		this.engineTask = engindTask;
	}

	public String getReportFile() {
		return reportFile;
	}

	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
}
