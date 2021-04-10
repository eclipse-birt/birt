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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * Dialog used to display tabs
 */

public class TabDialog extends BaseTitleAreaDialog {

	/**
	 * The list of tab pages
	 */
	private ArrayList<TabPage> tabList = new ArrayList<TabPage>();

	/**
	 * The input object
	 */
	private Object input;

	/**
	 * The constructor.
	 * 
	 * @param parentShell
	 */
	public TabDialog(Shell parentShell, String title) {
		super(parentShell);
		this.title = title;
	}

	/**
	 * Adds a new tab page to this dialog. The page is inserted at the end of the
	 * page list.
	 * 
	 * @param tabPage the new tab page to add
	 * 
	 */
	public void addTabPage(TabPage tabPage) {
		tabPage.setContainer(this);
		tabList.add(tabPage);
	}

	/**
	 * Adds multiple tab pages to this dialog.The <code>TabDialog</code>
	 * implementation of this method does nothing.method does nothing. Subclasses
	 * should extend if extra pages need to be added before the dialog opens. New
	 * tab pages should be added by calling <code>addTab</code>.
	 */
	public void addTabPages() {// Do nothing
	}

	/**
	 * Sets the input for this dialog
	 * 
	 * @param input the new input to set
	 */
	public void setInput(Object input) {
		assert input != null;
		this.input = input;
	}

	/**
	 * The <code>TabDialog</code> overrides this method to set input for all pages
	 * after content has been created.
	 */
	protected boolean initDialog() {
		for (Iterator<TabPage> iterator = tabList.iterator(); iterator.hasNext();) {
			TabPage page = iterator.next();
			page.setInput(input);
		}

		return true;
	}

	/**
	 * Creates and returns the contents of the upper part of this dialog (above the
	 * button bar).
	 * <p>
	 * The <code>TabDialog</code> overrides this framework method to create and
	 * return a new <code>Composite</code> with an empty tab folder.
	 * </p>
	 * 
	 * @param parent the parent composite to contain the dialog area
	 * @return the dialog area control
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		TabFolder tabFolder = new TabFolder(composite, SWT.TOP);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		addTabPages(); // add pages
		for (Iterator<TabPage> iterator = tabList.iterator(); iterator.hasNext();) {
			TabPage page = iterator.next();
			TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setControl(page.createControl(tabFolder));
			tabItem.setText(page.getName());
		}
		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		try {
			saveAll();
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
			return;
		}
		super.okPressed();
	}

	/**
	 * Updates button states.
	 */
	public void updateButtons() {
		for (Iterator<TabPage> iterator = tabList.iterator(); iterator.hasNext();) {
			TabPage page = iterator.next();
			if (!page.isPageComplete()) {
				getOkButton().setEnabled(false);
				return;
			}
		}
		getOkButton().setEnabled(true);
	}

	protected void saveAll() throws SemanticException {
		setResult(input);
		for (Iterator<TabPage> iterator = tabList.iterator(); iterator.hasNext();) {
			TabPage page = iterator.next();
			page.saveTo(getResult());
		}
	}

}