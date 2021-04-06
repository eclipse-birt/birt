/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * TabPage is the abstract superclass of every page in the tab folder of BIRT.
 * Each page in the tab folder describes a control.
 * 
 */

public abstract class TabPage {

	/**
	 * The name of the tab page
	 */
	private String name;

	private int style;

	private TabDialog container;

	/**
	 * Creates a tab page with the given name and style
	 * 
	 * @param name  the name of the page
	 * @param style the style of the page
	 */
	public TabPage(String name, int style) {
		this.name = name;
		this.style = style;
	}

	/**
	 * Creates the top level control of the page under the given parent
	 * 
	 * @param parent the parent composite
	 * 
	 * @return Returns the control
	 */
	public Composite createControl(Composite parent) {
		Composite composite = new Composite(parent, style);
		setLayout(composite);
		createWidgets(composite);
		return composite;
	}

	/**
	 * Sets the top level layout of the page.The default implementation of this
	 * framework method is to sets a grid layout with two columns
	 * 
	 * @param composite the top level composite of the page
	 */
	protected void setLayout(Composite composite) {
		composite.setLayout(new GridLayout(2, false));
	}

	/**
	 * Creates the widgets of this tab page.Subclasses must implement this method.
	 * 
	 * @param composite the top level composite of the page
	 * 
	 */

	abstract protected void createWidgets(Composite composite);

	/**
	 * Sets the input of the page
	 * 
	 * @param input the input to set
	 */
	abstract public void setInput(Object input);

	/**
	 * Saves the result of the page
	 * 
	 * @param result the object to save the result
	 * @throws SemanticException
	 */
	abstract public void saveTo(Object result) throws SemanticException;

	abstract public boolean isPageComplete();

	/**
	 * Gets the name of the page
	 * 
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	void setContainer(TabDialog dialog) {
		container = dialog;
	}

	protected TabDialog getContainer() {
		return container;
	}

	protected void applyDialog() {
		getContainer().updateButtons();
	}
}