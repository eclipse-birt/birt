/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.property.widgets;

import java.util.logging.Logger;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

abstract public class CDialogCellEditor extends DialogCellEditor {

	protected static final Logger logger = Logger.getLogger(CDialogCellEditor.class.getName());

	private Button result;

	/**
	 * @param parent
	 */
	public CDialogCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public CDialogCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * 
	 */
	public CDialogCellEditor() {
		super();
	}

	/**
	 * Returns whether the given value is valid for this cell editor. This cell
	 * editor's validator (if any) makes the actual determination.
	 * 
	 * @return <code>true</code> if the value is valid, and <code>false</code> if
	 *         invalid
	 */
	protected boolean isCorrect(Object value) {
		if (value == null || doGetValue() == null) {
			return true;
		}
		if (doGetValue().equals(value)) {
			setErrorMessage("");//$NON-NLS-1$
			return false;
		}
		return super.isCorrect(value);
	}

	/**
	 * Creates the button for this cell editor under the given parent control.
	 * <p>
	 * The default implementation of this framework method creates the button
	 * display on the right hand side of the dialog cell editor. Subclasses may
	 * extend or reimplement.
	 * </p>
	 * 
	 * @param parent the parent control
	 * @return the new button control
	 */
	protected Button createButton(Composite parent) {
		result = new Button(parent, SWT.PUSH);
		result.setText("..."); //$NON-NLS-1$
		return result;
	}

	protected Button getButton() {
		return result;
	}

	/**
	 * Processes a focus lost event that occurred in this cell editor.
	 * <p>
	 * The default implementation of this framework method applies the current value
	 * and deactivates the cell editor. Subclasses should call this method at
	 * appropriate times. Subclasses may also extend or reimplement.
	 * </p>
	 */

	private boolean checkFocusControl(Control control) {
		if (control.isFocusControl())
			return true;
		if (control instanceof Composite) {
			Control[] children = ((Composite) control).getChildren();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					if (checkFocusControl(children[i]))
						return true;
				}
			}
		}
		return false;
	}

	protected void focusLost() {
		if (!checkFocusControl(getControl())) {
			doValueChanged();
			super.focusLost();
		}
	}

	protected abstract void doValueChanged();
}