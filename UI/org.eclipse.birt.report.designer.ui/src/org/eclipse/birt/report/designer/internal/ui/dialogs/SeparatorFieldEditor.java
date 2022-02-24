/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * A trivial and faked FiedEditor, only show a horizontal separating line.
 */

public class SeparatorFieldEditor extends FieldEditor {

	private Label lb;

	/**
	 * Default constructor, show a separating line.
	 * 
	 * @param parent
	 */
	public SeparatorFieldEditor(Composite parent) {
		this(parent, true);
	}

	/**
	 * Constructor
	 * 
	 * @param parent
	 * @param showLine if show a separating line or only gain some space.
	 */
	public SeparatorFieldEditor(Composite parent, boolean showLine) {
		super(showLine ? "TRUE" : "FALSE", "", parent); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
	 */
	protected void adjustForNumColumns(int numColumns) {
		if (lb != null) {
			((GridData) lb.getLayoutData()).horizontalSpan = numColumns;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt.
	 * widgets.Composite, int)
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		if (getPreferenceName().equals("TRUE")) //$NON-NLS-1$
		{
			lb = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		} else {
			lb = new Label(parent, SWT.NONE);
		}
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.heightHint = 10;
		gdata.horizontalSpan = numColumns;
		lb.setLayoutData(gdata);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#getLabelControl()
	 */
	protected Label getLabelControl() {
		return lb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditor#getLabelControl(org.eclipse.swt.
	 * widgets.Composite)
	 */
	public Label getLabelControl(Composite parent) {
		return lb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	protected void doLoad() {
		/**
		 * Does nothing.
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault() {
		/**
		 * Does nothing.
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	protected void doStore() {
		/**
		 * Does nothing.
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls() {
		return 1;
	}

}
