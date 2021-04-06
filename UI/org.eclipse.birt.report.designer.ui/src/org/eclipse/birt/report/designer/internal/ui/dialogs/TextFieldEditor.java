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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class TextFieldEditor extends AbstractFieldEditor {

	public static final int DEFAULT = SWT.BORDER;

	/**
	 * Text's Style.
	 */
	private int style;

	/**
	 * The text control, or <code>null</code> if none.
	 */
	private Text text = null;

	/**
	 * Creates a new boolean field editor
	 */
	protected TextFieldEditor() {
	}

	/**
	 * Creates a boolean field editor in the given style.
	 * 
	 * @param name      the name of the preference this field editor works on
	 * @param labelText the label text of the field editor
	 * @param style     the style, either <code>DEFAULT</code> or
	 *                  <code>SEPARATE_LABEL</code>
	 * @param parent    the parent of the field editor's control
	 */
	public TextFieldEditor(String name, String labelText, int style, Composite parent) {
		init(name, labelText);
		this.style = style;
		createControl(parent);
	}

	/**
	 * Creates a text field editor in the default style.
	 * 
	 * @param name   the name of the preference this field editor works on
	 * @param label  the label text of the field editor
	 * @param parent the parent of the field editor's control
	 */
	public TextFieldEditor(String name, String label, Composite parent) {
		this(name, label, DEFAULT, parent);
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void adjustForNumColumns(int numColumns) {
		((GridData) text.getLayoutData()).horizontalSpan = numColumns;
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		Control control = getLabelControl(parent);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		control.setLayoutData(gd);
		control = getTextControl(parent);
		gd = new GridData();
		gd.horizontalSpan = 1;
		control.setLayoutData(gd);
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor. Loads the value from the
	 * preference store and sets it to the check box.
	 */
	protected void doLoad() {
		updateTextForValue(getPreferenceStore().getString(getPreferenceName()), true);
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor. Loads the default value from
	 * the preference store and sets it to the check box.
	 */
	protected void doLoadDefault() {
		updateTextForValue(getPreferenceStore().getDefaultString(getPreferenceName()), false);
		if (this.getPreferenceStore() instanceof StylePreferenceStore) {
			StylePreferenceStore store = (StylePreferenceStore) this.getPreferenceStore();
			if (store.hasLocalValue(getPreferenceName()))
				markDirty(true);
			else
				markDirty(false);
		} else
			markDirty(true);
	}

	// /**
	// *
	// */
	// protected boolean isDirty( )
	// {
	// if ( wasSelected != isSelected )
	// {
	// return true;
	// }
	// return false;
	// }

	/**
	 * Returns the text for this field editor.
	 * 
	 * @param parent The Composite to create the receiver in.
	 * 
	 * @return the text
	 */
	protected Text getTextControl(Composite parent) {
		if (text == null) {
			text = new Text(parent, style);
			text.setFont(parent.getFont());
			text.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					valueChanged(VALUE);
				}
			});
			text.addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					text = null;
				}
			});
		} else {
			checkParent(text, parent);
		}
		return text;
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	public int getNumberOfControls() {
		return 1;
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	public void setFocus() {
		if (text != null) {
			text.setFocus();
		}
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	public void setLabelText(String text) {
		super.setLabelText(text);
		Label label = getLabelControl();
		if (label == null && text != null) {
			this.text.setText(text);
		}
	}

	/**
	 * Informs this field editor's listener, if it has one, about a change to the
	 * value (<code>VALUE</code> property) provided that the old and new values are
	 * different.
	 * 
	 * @param oldValue the old value
	 * @param newValue the new value
	 */
	protected void valueChanged(boolean oldValue, boolean newValue) {
		setPresentsDefaultValue(false);
		if (oldValue != newValue) {
			fireStateChanged(VALUE, oldValue, newValue);
			markDirty(true);
		}
	}

	/*
	 * @see FieldEditor.setEnabled
	 */
	public void setEnabled(boolean enabled, Composite parent) {
		getTextControl(parent).setEnabled(enabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.AbstractFieldEditor#
	 * getValue()
	 */
	protected String getStringValue() {
		if (text != null) {
			return text.getText();
		}
		return getPreferenceStore().getString(getPreferenceName());
	}

	protected void updateTextForValue(String value, boolean setOldValue) {
		if (setOldValue) {
			setOldValue(value);
		} else {
			setDefaultValue(value);
		}

		if (value == null) {
			text.setText(""); //$NON-NLS-1$
		} else {
			text.setText(value);
		}
		if (setOldValue) {
			setOldValue(getStringValue());
		} else {
			setDefaultValue(getStringValue());
		}
	}

}