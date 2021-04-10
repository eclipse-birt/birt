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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A field editor for decoration that contains three check box button.
 * 
 */

public class DecorationFieldEditor extends AbstractFieldEditor {

	/**
	 * The parent Composite contains this field editor.
	 */
	private Composite parent;

	/**
	 * The field editor's label text.
	 */
	private String labelText;

	/**
	 * The <code>Button</code> widgets.
	 */

	private Composite compositeLine;

	private Button bUnderLine;

	private String propValue1;

	private String displayValue1;

	private boolean isDirty1;

	private Button bOverLine;

	private String propValue2;

	private String displayValue2;

	private boolean isDirty2;

	private Button bLineThrough;

	private String propValue3;

	private String displayValue3;

	private boolean isDirty3;

	/**
	 * The names of the preferences displayed in this field editor.
	 */
	private String underline_prop;

	private String underline_text;

	private String overline_prop;

	private String overline_text;

	private String line_through_prop;

	private String line_through_text;

	/**
	 * Constructs a new instance of decoration field editor.
	 * 
	 * @param prop_name1 preference name of underline_prop
	 * @param prop_name2 preference name of overline_prop
	 * @param prop_name3 preference name of line_through_prop
	 * @param label      label text of the preference
	 * @param parent     parent Composite
	 */
	public DecorationFieldEditor(String prop_name1, String prop_label1, String prop_name2, String prop_label2,
			String prop_name3, String prop_label3, String label, Composite parent) {
		super();
		underline_prop = prop_name1;
		underline_text = prop_label1;
		overline_prop = prop_name2;
		overline_text = prop_label2;
		line_through_prop = prop_name3;
		line_through_text = prop_label3;
		labelText = label;
		this.parent = parent;

		createControl(parent);
	}

	/*
	 * @see FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls() {
		return 4;
	}

	/*
	 * @see FieldEditor#adjustForNumColumns(int)
	 */
	protected void adjustForNumColumns(int numColumns) {
		Control control = getLabelControl();
		if (control != null) {
			((GridData) control.getLayoutData()).horizontalSpan = 1;
			numColumns--;
		}
		((GridData) getUnderLinePropControl(parent).getLayoutData()).horizontalSpan = 1;
		numColumns--;

		((GridData) getOverLinePropControl(parent).getLayoutData()).horizontalSpan = 1;
		numColumns--;

		((GridData) getLineThroughPropControl(parent).getLayoutData()).horizontalSpan = numColumns;
	}

	/*
	 * @see FieldEditor#doFillIntoGrid(Composite, int)
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {

		Control control = getLabelControl(parent);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		control.setLayoutData(gd);

		compositeLine = new Composite(parent, SWT.NULL);
		gd = new GridData(GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = numColumns - 1;
		compositeLine.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		compositeLine.setLayout(layout);

		bUnderLine = getUnderLinePropControl(compositeLine);
		gd = new GridData();
		bUnderLine.setLayoutData(gd);

		bOverLine = getOverLinePropControl(compositeLine);
		gd = new GridData();
		bOverLine.setLayoutData(gd);

		bLineThrough = getLineThroughPropControl(compositeLine);
		gd = new GridData();
		bLineThrough.setLayoutData(gd);
	}

	/**
	 * Lazily creates and returns the Button control.
	 * 
	 * @param parent The parent Composite contains the button.
	 * @return Button
	 */
	public Button getUnderLinePropControl(Composite parent) {
		if (bUnderLine == null) {
			bUnderLine = new Button(parent, SWT.CHECK);
			bUnderLine.setText(underline_text);
			bUnderLine.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent evt) {
					underLineChanged();
				}
			});
		}
		return bUnderLine;
	}

	/**
	 * Lazily creates and returns the Button button.
	 * 
	 * @param parent The parent Composite contains the control.
	 * @return Button
	 */
	public Button getOverLinePropControl(Composite parent) {
		if (bOverLine == null) {
			bOverLine = new Button(parent, SWT.CHECK);
			bOverLine.setText(overline_text);
			bOverLine.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent evt) {
					overLineChanged();
				}
			});
		}
		return bOverLine;
	}

	/**
	 * Lazily creates and returns the Button control.
	 * 
	 * @param parent The parent Composite contains the button.
	 * @return Button
	 */
	public Button getLineThroughPropControl(Composite parent) {
		if (bLineThrough == null) {
			bLineThrough = new Button(parent, SWT.CHECK);
			bLineThrough.setText(line_through_text);
			bLineThrough.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent evt) {
					lineThroughChanged();
				}
			});
		}
		return bLineThrough;
	}

	protected void setPropValue1(String newValue) {
		this.propValue1 = newValue;
		this.displayValue1 = newValue;
	}

	protected void setDefaultValue1(String newValue) {
		this.propValue1 = null;
		this.displayValue1 = newValue;
	}

	protected void setPropValue2(String newValue) {
		this.propValue2 = newValue;
		this.displayValue2 = newValue;
	}

	protected void setDefaultValue2(String newValue) {
		this.propValue2 = null;
		this.displayValue2 = newValue;
	}

	protected void setPropValue3(String newValue) {
		this.propValue3 = newValue;
		this.displayValue3 = newValue;
	}

	protected void setDefaultValue3(String newValue) {
		this.propValue3 = null;
		this.displayValue3 = newValue;
	}

	/*
	 * @see FieldEditor#doLoad()
	 */
	protected void doLoad() {
		if (bUnderLine != null) {
			String value = getPreferenceStore().getString(getUnderlinePropName());
			setPropValue1(value);
			bUnderLine.setSelection(DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals(displayValue1));
		}
		if (bOverLine != null) {
			String value = getPreferenceStore().getString(getOverLinePropName());
			setPropValue2(value);
			bOverLine.setSelection(DesignChoiceConstants.TEXT_OVERLINE_OVERLINE.equals(displayValue2));
		}
		if (bLineThrough != null) {
			String value = getPreferenceStore().getString(getLineThroughPropName());
			setPropValue3(value);
			bLineThrough.setSelection(DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH.equals(displayValue3));
		}
	}

	/*
	 * @see FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault() {
		if (bUnderLine != null) {
			String value = getPreferenceStore().getDefaultString(getUnderlinePropName());
			setDefaultValue1(value);
			bUnderLine.setSelection(DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals(displayValue1));

			if (this.getPreferenceStore() instanceof StylePreferenceStore) {
				StylePreferenceStore store = (StylePreferenceStore) this.getPreferenceStore();
				if (store.hasLocalValue(getUnderlinePropName()))
					isDirty1 = true;
				else
					isDirty1 = false;
			} else
				isDirty1 = true;

			fireValueChanged(VALUE, null, null);
		}
		if (bOverLine != null) {
			String value = getPreferenceStore().getDefaultString(getOverLinePropName());
			setDefaultValue2(value);
			bOverLine.setSelection(DesignChoiceConstants.TEXT_OVERLINE_OVERLINE.equals(displayValue2));

			if (this.getPreferenceStore() instanceof StylePreferenceStore) {
				StylePreferenceStore store = (StylePreferenceStore) this.getPreferenceStore();
				if (store.hasLocalValue(getOverLinePropName()))
					isDirty2 = true;
				else
					isDirty2 = false;
			} else
				isDirty2 = true;

			fireValueChanged(VALUE, null, null);
		}
		if (bLineThrough != null) {
			String value = getPreferenceStore().getDefaultString(getLineThroughPropName());
			setDefaultValue3(value);
			bLineThrough.setSelection(DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH.equals(displayValue3));

			if (this.getPreferenceStore() instanceof StylePreferenceStore) {
				StylePreferenceStore store = (StylePreferenceStore) this.getPreferenceStore();
				if (store.hasLocalValue(getLineThroughPropName()))
					isDirty3 = true;
				else
					isDirty3 = false;
			} else
				isDirty3 = true;

			fireValueChanged(VALUE, null, null);
		}
	}

	/*
	 * @see FieldEditor#doStore()
	 */
	protected void doStore() {
		if (isDirty1) {
			getPreferenceStore().setValue(underline_prop, propValue1);
		}
		if (isDirty2) {
			getPreferenceStore().setValue(overline_prop, propValue2);
		}
		if (isDirty3) {
			getPreferenceStore().setValue(line_through_prop, propValue3);
		}
	}

	/**
	 * Returns this field editor's label text.
	 * 
	 * @return the label text
	 */
	public String getLabelText() {
		return labelText;
	}

	/**
	 * Returns the name of the preference this field editor operates on.
	 * 
	 * @return the name of the preference
	 */
	public String getUnderlinePropName() {
		return underline_prop;
	}

	/**
	 * Returns the name of the preference this field editor operates on.
	 * 
	 * @return the name of the preference
	 */
	public String getOverLinePropName() {
		return overline_prop;
	}

	/**
	 * Returns the name of the preference this field editor operates on.
	 * 
	 * @return the name of the preference
	 */
	public String getLineThroughPropName() {
		return line_through_prop;
	}

	/**
	 * Gets values for the given property.
	 * 
	 * @return
	 */
	private String getUnderLinePropValue() {
		if (bUnderLine.getSelection()) {
			return DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE;
		}
		return DesignChoiceConstants.TEXT_UNDERLINE_NONE;
	}

	/**
	 * Gets values for the given property.
	 * 
	 * @return
	 */
	private String getOverLinePropValue() {
		if (bOverLine.getSelection()) {
			return DesignChoiceConstants.TEXT_OVERLINE_OVERLINE;
		}
		return DesignChoiceConstants.TEXT_OVERLINE_NONE;
	}

	/**
	 * Gets values for the given property.
	 * 
	 * @return
	 */
	private String getLineThroughPropValue() {
		if (bLineThrough.getSelection()) {
			return DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH;
		}
		return DesignChoiceConstants.TEXT_LINE_THROUGH_NONE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.AbstractFieldEditor
	 * #getValue()
	 */
	protected String getStringValue() {
		return null;
	}

	private void overLineChanged() {
		boolean isSelected = bOverLine.getSelection();
		setPresentsDefaultValue(false);
		boolean isSelected2 = DesignChoiceConstants.TEXT_OVERLINE_OVERLINE.equals(displayValue2);
		if (isSelected2 != isSelected) {
			setPropValue2(getOverLinePropValue());
			isDirty2 = true;
			fireValueChanged(VALUE, null, null);
			fireStateChanged(VALUE, isSelected2, isSelected);
		}
	}

	private void lineThroughChanged() {
		boolean isSelected = bLineThrough.getSelection();
		setPresentsDefaultValue(false);
		boolean isSelected3 = DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH.equals(displayValue3);
		if (isSelected3 != isSelected) {
			setPropValue3(getLineThroughPropValue());
			isDirty3 = true;
			fireValueChanged(VALUE, null, null);
			fireStateChanged(VALUE, isSelected3, isSelected);
		}

	}

	private void underLineChanged() {
		boolean isSelected = bUnderLine.getSelection();
		setPresentsDefaultValue(false);
		boolean isSelected1 = DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals(displayValue1);
		if (isSelected1 != isSelected) {
			setPropValue1(getUnderLinePropValue());
			isDirty1 = true;
			fireValueChanged(VALUE, null, null);
			fireStateChanged(VALUE, isSelected1, isSelected);
		}
	}

	private boolean hasLocaleValue1() {
		if (propValue1 == null)
			return false;
		else {
			if (isDirty1)
				return true;
			else {
				if (this.getPreferenceStore() instanceof StylePreferenceStore) {
					StylePreferenceStore store = (StylePreferenceStore) this.getPreferenceStore();
					if (store.hasLocalValue(getUnderlinePropName()))
						return true;
					else
						return false;
				} else
					return true;
			}
		}
	}

	private boolean hasLocaleValue2() {
		if (propValue2 == null)
			return false;
		else {
			if (isDirty2)
				return true;
			else {
				if (this.getPreferenceStore() instanceof StylePreferenceStore) {
					StylePreferenceStore store = (StylePreferenceStore) this.getPreferenceStore();
					if (store.hasLocalValue(getOverLinePropName()))
						return true;
					else
						return false;
				} else
					return true;
			}
		}
	}

	private boolean hasLocaleValue3() {
		if (propValue3 == null)
			return false;
		else {
			if (isDirty3)
				return true;
			else {
				if (this.getPreferenceStore() instanceof StylePreferenceStore) {
					StylePreferenceStore store = (StylePreferenceStore) this.getPreferenceStore();
					if (store.hasLocalValue(getLineThroughPropName()))
						return true;
					else
						return false;
				} else
					return true;
			}
		}
	}

	public boolean hasLocaleValue() {
		return hasLocaleValue1() || hasLocaleValue2() || hasLocaleValue3();
	}
}