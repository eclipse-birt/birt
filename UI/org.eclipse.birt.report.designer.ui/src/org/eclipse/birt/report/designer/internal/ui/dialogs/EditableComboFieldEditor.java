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

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * An editable version of ComboFieldEditor. Allows selection from the drop-down
 * list or direct input.
 */

public class EditableComboFieldEditor extends AbstractFieldEditor {

	/**
	 * The <code>Combo</code> widget.
	 */
	protected Combo fCombo;

	/**
	 * The names (labels) and underlying values to populate the combo widget. These
	 * should be arranged as: { {name1, value1}, {name2, value2}, ...}
	 */
	protected String[][] fEntryNamesAndValues;

	private ModifyListener modifyListener;

	/**
	 * Creates a editable combo field editor.
	 * 
	 * @param name                the name of the preference this field editor works
	 *                            on
	 * @param labelText           the label text of the field editor
	 * @param entryNamesAndValues the entry name and value choices of the combox of
	 *                            the field editor
	 * @param parent              the parent of the field editor's control
	 */
	public EditableComboFieldEditor(String name, String labelText, String[][] entryNamesAndValues, Composite parent) {
		init(name, labelText);
		Assert.isTrue(checkArray(entryNamesAndValues));
		fEntryNamesAndValues = entryNamesAndValues;
		createControl(parent);
	}

	/**
	 * Checks whether given <code>String[][]</code> is of "type"
	 * <code>String[][2]</code>.
	 * 
	 * @return <code>true</code> if it is ok, and <code>false</code> otherwise
	 */
	private boolean checkArray(String[][] table) {
		if (table == null) {
			return false;
		}
		for (int i = 0; i < table.length; i++) {
			String[] array = table[i];
			if (array == null || array.length != 2) {
				return false;
			}
		}
		return true;
	}

	/*
	 * @see FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls() {
		return 2;
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
		((GridData) fCombo.getLayoutData()).horizontalSpan = numColumns;
	}

	/*
	 * @see FieldEditor#doFillIntoGrid(Composite, int)
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		Control control = getLabelControl(parent);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		control.setLayoutData(gd);
		control = getComboBoxControl(parent);
		gd = new GridData();
		gd.horizontalSpan = 1;
		control.setLayoutData(gd);
	}

	/*
	 * @see FieldEditor#doLoad()
	 */
	protected void doLoad() {
		updateComboForValue(getPreferenceStore().getString(getPreferenceName()), true);
	}

	/*
	 * @see FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault() {
		fCombo.removeModifyListener(modifyListener);
		updateComboForValue(getPreferenceStore().getDefaultString(getPreferenceName()), false);

		if (this.getPreferenceStore() instanceof StylePreferenceStore) {
			StylePreferenceStore store = (StylePreferenceStore) this.getPreferenceStore();
			if (store.hasLocalValue(getPreferenceName()))
				markDirty(true);
			else
				markDirty(false);
		} else
			markDirty(true);

		fCombo.addModifyListener(modifyListener);
	}

	/**
	 * Sets the name in the combo widget to match the specified value.
	 */
	protected void updateComboForValue(String value, boolean setOldValue) {
		if (setOldValue)
			setOldValue(value);
		else
			setDefaultValue(value);
		for (int i = 0; i < fEntryNamesAndValues.length; i++) {
			if ((fEntryNamesAndValues[i][1] != null && fEntryNamesAndValues[i][1].equals(value))
					|| (fEntryNamesAndValues[i][1] == null && value == null)) {
				fCombo.setText(fEntryNamesAndValues[i][0]);
				return;
			}
		}

		if (value == null) {
			fCombo.setText(""); //$NON-NLS-1$
		} else {
			fCombo.setText(value);
		}
		if (setOldValue)
			setOldValue(getStringValue());
		else
			setDefaultValue(getStringValue());
	}

	/**
	 * Lazily creates and returns the Combo control.
	 * 
	 * @param parent The parent composite to contain the field editor
	 * @return Combo The combo box of the field editor
	 */
	public Combo getComboBoxControl(Composite parent) {
		if (fCombo == null) {
			fCombo = new Combo(parent, SWT.NONE);
			for (int i = 0; i < fEntryNamesAndValues.length; i++) {
				fCombo.add(fEntryNamesAndValues[i][0], i);
			}
			fCombo.setVisibleItemCount(30);
			fCombo.setFont(parent.getFont());
			fCombo.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent evt) {
					valueChanged(VALUE);
				}

			});

			modifyListener = new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					valueChanged(VALUE);
				}
			};
			fCombo.addModifyListener(modifyListener);
		}
		return fCombo;
	}

	/**
	 * Given the name (label) of an entry, return the corresponding value.
	 */
	protected String getValueForName(String name) {
		for (int i = 0; i < fEntryNamesAndValues.length; i++) {
			String[] entry = fEntryNamesAndValues[i];
			if (name.equals(entry[0])) {
				return entry[1];
			}
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.AbstractFieldEditor
	 * #getValue()
	 */
	protected String getStringValue() {
		if (fCombo != null) {
			return getValueForName(fCombo.getText());
		}
		return getPreferenceStore().getString(getPreferenceName());
	}
}
