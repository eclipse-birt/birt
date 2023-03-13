/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class SimpleTask implements ITask {

	protected transient Composite topControl = null;
	protected transient IWizardContext context = null;
	protected transient WizardBase container = null;
	private transient String sDesc = ""; //$NON-NLS-1$
	private transient String sTitle = ""; //$NON-NLS-1$
	private transient List errorList = new ArrayList();

	private void placeComponents(Composite parent) {
		Label lbl = new Label(parent, SWT.SHADOW_IN | SWT.CENTER);
		lbl.setText("This is a placeholder for the task : " //$NON-NLS-1$
				+ getTitle());
	}

	public SimpleTask() {
		setTitle("Task"); //$NON-NLS-1$
	}

	public SimpleTask(String title) {
		setTitle(title);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#setContext(org.
	 * eclipse.birt.frameworks.taskwizard.interfaces.IWizardContext)
	 */
	@Override
	public void setContext(IWizardContext context) {
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#getContext()
	 */
	@Override
	public IWizardContext getContext() {
		return this.context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#setUIProvider(org.
	 * eclipse.birt.frameworks.taskwizard.WizardBase)
	 */
	@Override
	public void setUIProvider(WizardBase wizard) {
		this.container = wizard;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask#getErrors()
	 */
	@Override
	public String[] getErrors() {
		return (String[]) errorList.toArray(new String[errorList.size()]);
	}

	protected void addError(String errorInfo) {
		if (!errorList.contains(errorInfo)) {
			errorList.add(errorInfo);
		}
	}

	protected void removeError(String errorInfo) {
		errorList.remove(errorInfo);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask#setErrorHints
	 * (java.lang.Object[])
	 */
	@Override
	public void setErrorHints(Object[] errorHints) {
		errorList.clear();
		for (int i = 0; i < errorHints.length; i++) {
			errorList.add(errorHints[i].toString());
		}
	}

	@Override
	public void dispose() {
		topControl = null;
		context = null;
		container = null;
		errorList.clear();
	}

	@Override
	public void createControl(Composite parent) {
		if (topControl == null || topControl.isDisposed()) {
			topControl = new Composite(parent, SWT.NONE);
			topControl.setLayout(new FillLayout());
			placeComponents(topControl);
		}
	}

	@Override
	public Control getControl() {
		return topControl;
	}

	@Override
	public String getDescription() {
		return sDesc;
	}

	@Override
	public String getErrorMessage() {
		return sDesc;
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public String getMessage() {
		return sDesc;
	}

	@Override
	public String getTitle() {
		return sTitle;
	}

	/**
	 * @deprecated For later use
	 */
	@Deprecated
	@Override
	public void performHelp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDescription(String description) {
		this.sDesc = description;
	}

	/**
	 * @deprecated For later use
	 */
	@Deprecated
	@Override
	public void setImageDescriptor(ImageDescriptor image) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTitle(String title) {
		this.sTitle = title;
	}

	@Override
	public void setVisible(boolean visible) {
		getControl().setVisible(visible);
	}
}
