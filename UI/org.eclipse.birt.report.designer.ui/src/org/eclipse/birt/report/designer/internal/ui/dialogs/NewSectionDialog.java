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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * This class represents new section dialog.
 * 
 */

public class NewSectionDialog extends SelectionDialog {

	/**
	 * The label string.
	 */
	public static final String LABEL_PRE = Messages.getString("NewSectionDialog.text.Prefix"); //$NON-NLS-1$

	/**
	 * The title string.
	 */
	public static final String TITLE = Messages.getString("NewSectionDialog.Title");//$NON-NLS-1$

	private List list;

	private java.util.List contents = null;

	/**
	 * The constructor.
	 * 
	 * @param parent the parent
	 */

	public NewSectionDialog(Shell parent) {
		super(parent);
		setHelpAvailable(false);
		setTitle(TITLE);
	}

	/**
	 * The constructor.
	 * 
	 * @param parent the parent
	 */

	public NewSectionDialog(Shell parent, java.util.List list) {
		this(parent);
		assert list != null;
		setContents(list);
	}

	/**
	 * Creates dialog area, including List prefix label, suffix label.
	 * 
	 * @return the dialog area
	 */

	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.NEW_SECTION_DIALOG);
		Composite compo = (Composite) super.createDialogArea(parent);
		GridLayout layout = (GridLayout) compo.getLayout();
		layout.numColumns = 1;
		Label preLabel = new Label(compo, SWT.NONE);
		list = new List(compo, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				getOkButton().setEnabled(list.getSelectionCount() > 0);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				if (list.getSelectionCount() > 0) {
					okPressed();
				}
			}

		});
		list.setLayoutData(new GridData(GridData.FILL_BOTH));

		// preLabel.setFont( FontManager.getFont( "Dialog", 8, SWT.NORMAL ) );
		// //$NON-NLS-1$
		preLabel.setText(LABEL_PRE);
		initList();
		return compo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse
	 * .swt.widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getOkButton().setEnabled(false);
	}

	/**
	 * Initializes the list. Sets the list data.
	 */
	private void initList() {
		// list.setFont( FontManager.getFont( "Dialog", 8, SWT.NORMAL ) );//$NON-NLS-1$
		for (Iterator itor = contents.iterator(); itor.hasNext();) {
			IElementDefn defn = (IElementDefn) itor.next();
			list.add(defn.getDisplayName());
		}
	}

	/**
	 * Stores the selection data to the result list.
	 */

	public void okPressed() {
		ArrayList arrayList = new ArrayList();

		arrayList.add(((IElementDefn) contents.get(list.getSelectionIndex())).getName());
		setResult(arrayList);
		super.okPressed();
	}

	/**
	 * Sets the show contents
	 * 
	 * @param cons
	 */
	public void setContents(java.util.List cons) {
		assert cons != null;
		this.contents = cons;
	}
}