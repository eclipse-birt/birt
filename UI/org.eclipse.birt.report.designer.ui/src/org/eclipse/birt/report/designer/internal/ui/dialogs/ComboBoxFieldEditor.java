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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A field editor for a combo box that allows the drop-down selection of one of
 * a list of items. XXX: Note this is a copy from
 * org.eclipse.debug.internal.ui.preferences This class was customized a little.
 */

public class ComboBoxFieldEditor extends AbstractFieldEditor {

	/**
	 * The <code>Combo</code> widget.
	 */
	private Combo fCombo;

	/**
	 * The names (labels) and underlying values to populate the combo widget. These
	 * should be arranged as: { {name1, value1}, {name2, value2}, ...}
	 */
	private String[][] fEntryNamesAndValues;

	/**
	 * Creates a editable combo field editor.
	 *
	 * @param name                the name of the preference this field editor works
	 *                            on
	 * @param labelText           the label text of the field editor
	 * @param entryNamesAndValues the entry name and value choices of the combo of
	 *                            the field editor
	 * @param parent              the parent of the field editor's control
	 */
	public ComboBoxFieldEditor(String name, String labelText, String[][] entryNamesAndValues, Composite parent) {
		init(name, labelText);
		assert checkArray(entryNamesAndValues);
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
	 * (non-Javadoc)
	 *
	 * @see FieldEditor#adjustForNumColumns(int)
	 */
	@Override
	protected void adjustForNumColumns(int numColumns) {
		Control control = getLabelControl();
		if (control != null) {
			((GridData) control.getLayoutData()).horizontalSpan = 1;
			numColumns--;
		}
		((GridData) fCombo.getLayoutData()).horizontalSpan = numColumns;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see FieldEditor#doFillIntoGrid(Composite, int)
	 */
	@Override
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
	 * (non-Javadoc)
	 *
	 * @see FieldEditor#doLoad()
	 */
	@Override
	protected void doLoad() {
		updateComboForValue(getPreferenceStore().getString(getPreferenceName()), true);
	}

	/*
	 * @see FieldEditor#doLoadDefault()
	 */
	@Override
	protected void doLoadDefault() {
		updateComboForValue(getPreferenceStore().getDefaultString(getPreferenceName()), false);
		if (this.getPreferenceStore() instanceof StylePreferenceStore) {
			StylePreferenceStore store = (StylePreferenceStore) this.getPreferenceStore();
			if (store.hasLocalValue(getPreferenceName())) {
				markDirty(true);
			} else {
				markDirty(false);
			}
		} else {
			markDirty(true);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see FieldEditor#getNumberOfControls()
	 */
	@Override
	public int getNumberOfControls() {
		return 2;
	}

	/**
	 * Lazily create and return the Combo control.
	 */
	public Combo getComboBoxControl(Composite parent) {
		if (fCombo == null) {
			fCombo = new Combo(parent, SWT.READ_ONLY);
			for (int i = 0; i < fEntryNamesAndValues.length; i++) {
				fCombo.add(fEntryNamesAndValues[i][0], i);
			}
			fCombo.setFont(parent.getFont());
			fCombo.setVisibleItemCount(30);
			fCombo.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent evt) {
					valueChanged(VALUE);
				}
			});
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

	/**
	 * Sets the name in the combo widget to match the specified value.
	 */
	protected void updateComboForValue(String value, boolean setOldValue) {
		if (setOldValue) {
			setOldValue(value);
		} else {
			setDefaultValue(value);
		}
		for (int i = 0; i < fEntryNamesAndValues.length; i++) {
			if ((value == null && (fEntryNamesAndValues[i][1] == null || fEntryNamesAndValues[i][1].length() == 0))
					|| fEntryNamesAndValues[i][1].equals(value)) {
				fCombo.setText(fEntryNamesAndValues[i][0]);
				return;
			}
		}
		if (value == null) {
			fCombo.setText(""); //$NON-NLS-1$
		} else {
			fCombo.setText(value);
		}
		if (setOldValue) {
			setOldValue(getStringValue());
		} else {
			setDefaultValue(getStringValue());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.AbstractFieldEditor
	 * #getValue()
	 */
	@Override
	protected String getStringValue() {
		if (fCombo != null) {
			return getValueForName(fCombo.getText());
		}
		return getPreferenceStore().getString(getPreferenceName());
	}
}
