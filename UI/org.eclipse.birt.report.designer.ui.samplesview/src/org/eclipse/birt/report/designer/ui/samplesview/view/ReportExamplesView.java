/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.samplesview.view;

import org.eclipse.birt.report.designer.ui.samplesview.action.ExportSampleReportAction;
import org.eclipse.birt.report.designer.ui.samplesview.action.IOpenSampleReportAction;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

public class ReportExamplesView extends ViewPart {

	ReportExamples instance = null;
	private IOpenSampleReportAction importAction;
	private ExportSampleReportAction exportAction;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.
	 * Composite)
	 */
	public void createPartControl(Composite parent) {
		instance = new ReportExamples(parent);
		registerActions();
		createContextMenus();
	}

	private void createContextMenus() {
		SampleContextMenuProvider menuManager = new SampleContextMenuProvider(ReportExamplesView.this);
		if (exportAction != null)
			menuManager.addAction(exportAction);
		if (importAction != null)
			menuManager.addAction((Action) importAction);
		Menu menu = menuManager.createContextMenu(instance.getTreeViewer().getControl());
		instance.getTreeViewer().getControl().setMenu(menu);
	}

	private void registerActions() {
		final IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolbarManager = actionBars.getToolBarManager();
		exportAction = new ExportSampleReportAction(instance);
		toolbarManager.add(exportAction);
		toolbarManager.add(new Separator());

		Object adapter = null;
		int status = Platform.getAdapterManager().queryAdapter(this, IAction.class.getName());

		if (status == IAdapterManager.LOADED) {
			adapter = Platform.getAdapterManager().getAdapter(this, IAction.class);
		} else if (status == IAdapterManager.NOT_LOADED) {
			// Cause the plug-in loading first
			adapter = Platform.getAdapterManager().loadAdapter(this, IAction.class.getName());
		}
		if (adapter != null) {
			importAction = ((IOpenSampleReportAction) adapter);
			importAction.setMainComposite(instance);
			toolbarManager.add((Action) importAction);
		}

		actionBars.updateActionBars();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
		instance.setFocus();
	}

	/**
	 * Called when the View is to be disposed
	 */
	public void dispose() {
		instance.dispose();
		instance = null;
		super.dispose();
	}
}
