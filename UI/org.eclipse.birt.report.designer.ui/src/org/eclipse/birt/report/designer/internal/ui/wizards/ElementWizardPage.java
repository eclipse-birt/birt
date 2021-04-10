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

package org.eclipse.birt.report.designer.internal.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Abstract super class of all element new/edit wizard page
 * 
 * 
 */

public abstract class ElementWizardPage extends WizardPage {

	private int style;

	/**
	 * Creates a new wizard page with the given name, title, and image.
	 * 
	 * @param pageName   the name of the page
	 * @param title      the title for this wizard page, or <code>null</code> if
	 *                   none
	 * @param titleImage the image descriptor for the title of this wizard page, or
	 *                   <code>null</code> if none
	 */
	protected ElementWizardPage(String pageName, String title, ImageDescriptor titleImage, int style) {
		super(pageName, title, titleImage);
		this.style = style;

	}

	/**
	 * Creates the top level control for this dialog page under the given parent
	 * composite.
	 * <p>
	 * Implementors are responsible for ensuring that the created control can be
	 * accessed via <code>getControl</code>
	 * </p>
	 * 
	 * @param parent the parent composite
	 */
	public void createControl(Composite composite) {
		setControl(composite);
		addListeners();
	}

	/**
	 * Adds event listeners
	 */
	protected abstract void addListeners();

	/**
	 * Sets the content of the page
	 * 
	 * @param model the model used to fill the page
	 */
	public abstract void setInput(Object model);

	/**
	 * Saves the result of the page
	 * 
	 * @param model the model used to save
	 */
	public abstract void saveTo(Object model);

	/**
	 * Creates a separator line. Expects a <code>GridLayout</code> with at least 1
	 * column.
	 * 
	 * @param composite the parent composite
	 * @param nColumns  number of columns to span
	 * @param hWidth    the minimum width of the separator
	 */
	protected void createSeparator(Composite composite, int nColumns) {
		Label sparator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = nColumns;
		sparator.setLayoutData(gd);
	}

	/**
	 * @return Returns the style.
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * Applies status
	 */
	protected void applyStatus() {
		setPageComplete(getMessageType() != ERROR);
	}
}