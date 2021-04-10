/***********************************************************************
 * Copyright (c) 2004, 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartWizardContext;
import org.eclipse.birt.core.ui.frameworks.taskwizard.CompoundTask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * UI constants for chart builder
 * 
 */
public class SubtaskSheetBase<C extends IChartObject, CX extends IChartWizardContext<C>> implements ISubtaskSheet {

	private String sNodePath = ""; //$NON-NLS-1$

	private String sTitle = ""; //$NON-NLS-1$

	private int subtaskIndex = 0;

	protected Composite cmpContent = null;

	private CX context = null;

	private WizardBase wizard;

	private ITask parentTask;

	public SubtaskSheetBase() {
		super();
	}

	public void createControl(Composite parent) {
		cmpContent = new Composite(parent, SWT.NONE);
		FillLayout fillLayout = new FillLayout();
		cmpContent.setLayout(fillLayout);
	}

	public Object onHide() {
		if (cmpContent != null) {
			cmpContent.dispose();
		}
		return getContext();
	}

	@SuppressWarnings("unchecked")
	public void onShow(Object context, Object container) {
		this.context = (CX) context;
		this.wizard = (WizardBase) container;
	}

	protected C getChart() {
		return context.getModel();
	}

	protected CX getContext() {
		return context;
	}

	protected void setContext(CX context) {
		this.context = context;
	}

	protected WizardBase getWizard() {
		return wizard;
	}

	protected void setWizard(WizardBase wizard) {
		this.wizard = wizard;
	}

	public void setIndex(int index) {
		subtaskIndex = index;
	}

	protected int getIndex() {
		return subtaskIndex;
	}

	public void setParentTask(ITask parentTask) {
		this.parentTask = parentTask;
	}

	protected ITask getParentTask() {
		return parentTask;
	}

	protected void switchTo(String subtaskPath) {
		if (parentTask instanceof CompoundTask) {
			((CompoundTask) parentTask).switchTo(subtaskPath);
		}
	}

	public void setNodePath(String nodePath) {
		this.sNodePath = nodePath;
	}

	public String getNodePath() {
		return sNodePath;
	}

	public void dispose() {
		// To be overridden
	}

	public Control getControl() {
		return cmpContent;
	}

	/**
	 * @deprecated For later use
	 */
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @deprecated For later use
	 */
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @deprecated For later use
	 */
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @deprecated For later use
	 */
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		return this.sTitle;
	}

	/**
	 * @deprecated For later use
	 */
	public void performHelp() {
		// TODO Auto-generated method stub

	}

	/**
	 * @deprecated For later use
	 */
	public void setDescription(String description) {
		// TODO Auto-generated method stub

	}

	/**
	 * @deprecated For later use
	 */
	public void setImageDescriptor(ImageDescriptor image) {
		// TODO Auto-generated method stub

	}

	public void setTitle(String title) {
		this.sTitle = title;
	}

	public void setVisible(boolean visible) {
		getControl().setVisible(visible);
	}

	public boolean attachPopup(String popupID) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean detachPopup() {
		return false;
	}
}