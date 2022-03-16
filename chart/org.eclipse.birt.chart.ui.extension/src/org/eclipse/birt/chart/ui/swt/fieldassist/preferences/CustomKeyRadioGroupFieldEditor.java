/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.fieldassist.preferences;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * A field editor for an enumeration type preference. The choices are presented
 * as a list of radio buttons. The last radio selection can enable to set custom
 * key.
 *
 * @since 2.5
 */
public class CustomKeyRadioGroupFieldEditor extends FieldEditor {

	/**
	 * List of radio button entries of the form [label,value].
	 */
	private String[][] labelsAndValues;

	/**
	 * Indent used for the first column of the radion button matrix.
	 */
	private int indent = HORIZONTAL_GAP;

	/**
	 * The current value, or <code>null</code> if none.
	 */
	private String value;

	/**
	 * The box of radio buttons, or <code>null</code> if none (before creation and
	 * after disposal).
	 */
	private Composite radioBox;

	/**
	 * The radio buttons, or <code>null</code> if none (before creation and after
	 * disposal).
	 */
	private Button[] radioButtons;

	/**
	 * Whether to use a Group control.
	 */
	private boolean useGroup;

	/** The custom key name. */
	private String customKeyName;

	/** The custom key text field. */
	private Text customKeyText;

	/**
	 * Creates a new radio group field editor
	 */
	protected CustomKeyRadioGroupFieldEditor() {
	}

	/**
	 * Creates a radio group field editor. This constructor does not use a
	 * <code>Group</code> to contain the radio buttons. It is equivalent to using
	 * the following constructor with <code>false</code> for the
	 * <code>useGroup</code> argument.
	 * <p>
	 * Example usage:
	 *
	 * <pre>
	 * RadioGroupFieldEditor editor = new RadioGroupFieldEditor("GeneralPage.DoubleClick", resName, 1,
	 * 		new String[][] { { "Open Browser", "open" }, { "Expand Tree", "expand" } }, parent);
	 * </pre>
	 * </p>
	 *
	 * @param name           the name of the preference this field editor works on
	 * @param labelText      the label text of the field editor
	 * @param numColumns     the number of columns for the radio button presentation
	 * @param labelAndValues list of radio button [label, value] entries; the value
	 *                       is returned when the radio button is selected
	 * @param parent         the parent of the field editor's control
	 */
	public CustomKeyRadioGroupFieldEditor(String name, String customKeyName, String labelText,
			String[][] labelAndValues, Composite parent) {
		this(name, customKeyName, labelText, labelAndValues, parent, false);
	}

	/**
	 * Creates a radio group field editor.
	 * <p>
	 * Example usage:
	 *
	 * <pre>
	 * RadioGroupFieldEditor editor = new RadioGroupFieldEditor("GeneralPage.DoubleClick", resName, 1,
	 * 		new String[][] { { "Open Browser", "open" }, { "Expand Tree", "expand" } }, parent, true);
	 * </pre>
	 * </p>
	 *
	 * @param name           the name of the preference this field editor works on
	 * @param labelText      the label text of the field editor
	 * @param numColumns     the number of columns for the radio button presentation
	 * @param labelAndValues list of radio button [label, value] entries; the value
	 *                       is returned when the radio button is selected
	 * @param parent         the parent of the field editor's control
	 * @param useGroup       whether to use a Group control to contain the radio
	 *                       buttons
	 */
	public CustomKeyRadioGroupFieldEditor(String name, String customKeyName, String labelText,
			String[][] labelAndValues, Composite parent, boolean useGroup) {
		init(name, labelText);
		this.customKeyName = customKeyName;
		Assert.isTrue(checkArray(labelAndValues));
		this.labelsAndValues = labelAndValues;
		this.useGroup = useGroup;
		createControl(parent);
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	protected void adjustForNumColumns(int numColumns) {
		Control control = getLabelControl();
		if (control != null) {
			((GridData) control.getLayoutData()).horizontalSpan = numColumns;
		}
		((GridData) radioBox.getLayoutData()).horizontalSpan = numColumns;
	}

	/**
	 * Checks whether given <code>String[][]</code> is of "type"
	 * <code>String[][2]</code>.
	 *
	 * @param table
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
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		if (useGroup) {
			Control control = getRadioBoxControl(parent);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			control.setLayoutData(gd);
		} else {
			Control control = getLabelControl(parent);
			GridData gd = new GridData();
			gd.horizontalSpan = numColumns;
			control.setLayoutData(gd);
			control = getRadioBoxControl(parent);
			gd = new GridData();
			gd.horizontalSpan = numColumns;
			gd.horizontalIndent = indent;
			control.setLayoutData(gd);
		}

	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	protected void doLoad() {
		updateValue(getPreferenceStore().getString(getPreferenceName()));
		if (hasCustomKeyName()) {
			updateCustomKeyValue(getPreferenceStore().getString(customKeyName));
			updateCustomKeyTextStatus();
		}
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	protected void doLoadDefault() {
		updateValue(getPreferenceStore().getDefaultString(getPreferenceName()));
		if (hasCustomKeyName()) {
			updateCustomKeyValue(getPreferenceStore().getDefaultString(customKeyName));
			updateCustomKeyTextStatus();
		}
	}

	/**
	 *
	 */
	private void updateCustomKeyTextStatus() {
		customKeyText.setEnabled(radioButtons[labelsAndValues.length - 1].getSelection());
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	protected void doStore() {
		if (value == null) {
			getPreferenceStore().setToDefault(getPreferenceName());
			return;
		}
		getPreferenceStore().setValue(getPreferenceName(), value);
		getPreferenceStore().setValue(customKeyName, customKeyText.getText());
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	public int getNumberOfControls() {
		return 1;
	}

	/**
	 * Returns this field editor's radio group control.
	 *
	 * @param parent The parent to create the radioBox in
	 * @return the radio group control
	 */
	public Composite getRadioBoxControl(Composite parent) {
		if (radioBox == null) {

			Font font = parent.getFont();

			if (useGroup) {
				Group group = new Group(parent, SWT.NONE);
				group.setFont(font);
				String text = getLabelText();
				if (text != null) {
					group.setText(text);
				}
				radioBox = group;
				GridLayout layout = new GridLayout();
				layout.horizontalSpacing = HORIZONTAL_GAP;
				layout.numColumns = customKeyName == null ? 1 : 2;
				radioBox.setLayout(layout);
			} else {
				radioBox = new Composite(parent, SWT.NONE);
				GridLayout layout = new GridLayout();
				layout.marginWidth = 0;
				layout.marginHeight = 0;
				layout.horizontalSpacing = HORIZONTAL_GAP;
				layout.numColumns = customKeyName == null ? 1 : 2;

				radioBox.setLayout(layout);
				radioBox.setFont(font);
			}

			radioBox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			radioButtons = new Button[labelsAndValues.length];
			for (int i = 0; i < labelsAndValues.length; i++) {
				Button radio = new Button(radioBox, SWT.RADIO | SWT.LEFT);
				if (hasCustomKeyName() && i != (labelsAndValues.length - 1)) {
					GridData gd = new GridData(GridData.FILL_HORIZONTAL);
					gd.horizontalSpan = 2;
					radio.setLayoutData(gd);
				}

				radioButtons[i] = radio;
				String[] labelAndValue = labelsAndValues[i];
				radio.setText(labelAndValue[0]);
				radio.setData(labelAndValue[1]);
				radio.setFont(font);
				radio.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent event) {
						String oldValue = value;
						value = (String) event.widget.getData();
						setPresentsDefaultValue(false);
						fireValueChanged(VALUE, oldValue, value);
					}
				});
			}

			if (hasCustomKeyName()) {
				customKeyText = new Text(radioBox, SWT.BORDER);
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				customKeyText.setLayoutData(gd);
				customKeyText.addKeyListener(new KeyListener() {

					@Override
					public void keyPressed(KeyEvent e) {

					}

					@Override
					public void keyReleased(KeyEvent e) {
						if (e.keyCode == SWT.CTRL || e.keyCode == SWT.ALT || e.keyCode == SWT.SHIFT) {
							return;
						}
						final IKeyLookup lookup = KeyLookupFactory.getDefault();
						String key = lookup.formalNameLookup(e.keyCode);

						if (key == null) {
							return;
						}

						StringBuilder txt = new StringBuilder();
						if ((e.stateMask & SWT.CTRL) != 0) {
							txt.append("Ctrl"); //$NON-NLS-1$
						}
						if ((e.stateMask & SWT.ALT) != 0) {
							if (txt.length() > 0) {
								txt.append("+"); //$NON-NLS-1$
							}
							txt.append("Alt"); //$NON-NLS-1$
						} else if ((e.stateMask & SWT.SHIFT) != 0) {
							if (txt.length() > 0) {
								txt.append("+"); //$NON-NLS-1$
							}
							txt.append("Shift"); //$NON-NLS-1$
						}
						if (txt.length() > 0) {
							txt.append("+"); //$NON-NLS-1$
							txt.append(key);
							customKeyText.setText(txt.toString());
						}
					}
				});
			}

			radioButtons[labelsAndValues.length - 1].addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					customKeyText.setEnabled(radioButtons[labelsAndValues.length - 1].getSelection());
				}
			});
			radioBox.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent event) {
					radioBox = null;
					radioButtons = null;
				}
			});
		} else {
			checkParent(radioBox, parent);
		}
		return radioBox;
	}

	/**
	 * Sets the indent used for the first column of the radion button matrix.
	 *
	 * @param indent the indent (in pixels)
	 */
	public void setIndent(int indent) {
		if (indent < 0) {
			this.indent = 0;
		} else {
			this.indent = indent;
		}
	}

	/**
	 * Select the radio button that conforms to the given value.
	 *
	 * @param selectedValue the selected value
	 */
	private void updateValue(String selectedValue) {
		this.value = selectedValue;
		if (radioButtons == null) {
			return;
		}

		if (this.value != null) {
			boolean found = false;
			for (int i = 0; i < radioButtons.length; i++) {
				Button radio = radioButtons[i];
				boolean selection = false;
				if (((String) radio.getData()).equals(this.value)) {
					selection = true;
					found = true;
				}
				radio.setSelection(selection);
			}
			if (found) {
				return;
			}
		}

		// We weren't able to find the value. So we select the first
		// radio button as a default.
		if (radioButtons.length > 0) {
			radioButtons[0].setSelection(true);
			this.value = (String) radioButtons[0].getData();
		}
	}

	/*
	 * @see FieldEditor.setEnabled(boolean,Composite).
	 */
	@Override
	public void setEnabled(boolean enabled, Composite parent) {
		if (!useGroup) {
			super.setEnabled(enabled, parent);
		}
		for (int i = 0; i < radioButtons.length; i++) {
			radioButtons[i].setEnabled(enabled);
		}

	}

	private boolean hasCustomKeyName() {
		return customKeyName != null;
	}

	private void updateCustomKeyValue(String customKey) {
		customKeyText.setText(customKey);
	}
}
