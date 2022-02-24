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

package org.eclipse.birt.report.designer.ui.preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.PixelConverter;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.ControlEnableState;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Abstract options configuration block providing a general implementation for
 * setting up an options configuration page.
 * 
 * @since 2.1
 */
public abstract class OptionsConfigurationBlock {

	public static final class Key {

		private String fQualifier;
		private String fKey;
		private String value;

		public Key(String qualifier, String key) {
			fQualifier = qualifier;
			fKey = key;
		}

		public String getName() {
			return fKey;
		}

		public void apply(IPreferences preference) {
			if (value != null)
				preference.setValue(fKey, value);
		}

		public String getStoredValue(IPreferences preference) {
			if (value == null)
				value = preference.getString(fKey);
			return value;
		}

		public String getDefaultValue(IPreferences preference) {
			return preference.getDefaultString(fKey);
		}

		public void setStoredValue(IPreferences preference, String value) {
			if (value != null) {
				this.value = value;
			}
		}

		public void setToDefault(IPreferences preference) {
			preference.setToDefault(fKey);
			this.value = preference.getString(fKey);
		}

		public String toString() {
			return fQualifier + '/' + fKey;
		}

		public String getQualifier() {
			return fQualifier;
		}

		public void clear() {
			value = null;
		}

	}

	protected static class ControlData {

		private Key fKey;
		private String[] fValues;

		public ControlData(Key key, String[] values) {
			fKey = key;
			fValues = values;
		}

		public Key getKey() {
			return fKey;
		}

		public String getValue(boolean selection) {
			int index = selection ? 0 : 1;
			return fValues[index];
		}

		public String getValue(int index) {
			return fValues[index];
		}

		public int getSelection(String value) {
			if (value != null) {
				for (int i = 0; i < fValues.length; i++) {
					if (value.equals(fValues[i])) {
						return i;
					}
				}
			}
			return fValues.length - 1; // assume the last option is the least
			// severe
		}
	}

	protected final ArrayList fCheckBoxes;
	protected final ArrayList fComboBoxes;
	protected final ArrayList fTextBoxes;
	protected final ArrayList fRadioButtons;
	protected final HashMap fLabels;
	protected final ArrayList fExpandedComposites;

	private SelectionListener fSelectionListener;
	private ModifyListener fTextModifyListener;

	protected IStatusChangeListener fContext;
	protected IProject fProject; // project or null
	protected Key[] fAllKeys;
	protected Key[] ignoreKeys;
	protected AbstractUIPlugin fPlugin;

	protected IPreferences fPref;

	private Shell fShell;

	private Map fDisabledProjectSettings; // null when project specific

	public OptionsConfigurationBlock(IStatusChangeListener context, AbstractUIPlugin plugin, IProject project) {
		fContext = context;
		fProject = project;

		fPlugin = plugin;
		fPref = PreferenceFactory.getInstance().getPreferences(fPlugin, fProject);

		fCheckBoxes = new ArrayList();
		fComboBoxes = new ArrayList();
		fTextBoxes = new ArrayList(2);
		fRadioButtons = new ArrayList();
		fLabels = new HashMap();
		fExpandedComposites = new ArrayList();
	}

	public void setKeys(Key[] keys) {
		fAllKeys = keys;
		if (fProject == null || hasProjectSpecificOptions(fProject)) {
			fDisabledProjectSettings = null;
		} else {
			/*
			 * set default show value
			 */
			fDisabledProjectSettings = new IdentityHashMap();
			for (int i = 0; i < keys.length; i++) {
				Key curr = keys[i];
				fDisabledProjectSettings.put(curr, curr.getStoredValue(fPref));
			}
		}
		settingsUpdated();
	}

	public void setIngoreProjectSettingKeys(Key[] keys) {
		ignoreKeys = keys;
	}

	protected void settingsUpdated() {
		// TODO Auto-generated method stub

	}

	protected Key getKey(String plugin, String key) {
		return new Key(plugin, key);
	}

	protected final Key getReportKey(String key) {
		return getKey(ReportPlugin.REPORT_UI, key);
	}

	public void selectOption(String key, String qualifier) {
		for (int i = 0; i < fAllKeys.length; i++) {
			Key curr = fAllKeys[i];
			if (curr.getName().equals(key) && curr.getQualifier().equals(qualifier)) {
				selectOption(curr);
			}
		}
	}

	public void selectOption(Key key) {
		Control control = findControl(key);
		if (control != null) {
			control.setFocus();
		}
	}

	protected Shell getShell() {
		return fShell;
	}

	protected void setShell(Shell shell) {
		fShell = shell;
	}

	protected abstract Control createContents(Composite parent);

	public static class RadioComposite extends Group {

		List<Button> radioBtns = new ArrayList<Button>();;

		public RadioComposite(Composite parent, int style) {
			super(parent, style);
		}

		protected void checkSubclass() {
			/* Do nothing to make sure subclassing is allowed */
		}

		private void IniRadioButtons() {
			if (radioBtns.size() > 0) {
				return;
			}
			Control[] controls = this.getChildren();
			for (int i = 0; i < controls.length; i++) {
				if (controls[i] != null && controls[i] instanceof Button) {
					radioBtns.add((Button) controls[i]);
				}
			}
		}

		protected int getBtnIndex(Button btn) {
			int index = radioBtns.indexOf(btn);
			return index;
		}

		protected void setSelection(int index) {
			IniRadioButtons();
			if (radioBtns.size() <= 0)
				return;
			if (index < 0 || index >= radioBtns.size()) {
				index = 0;
			}

			for (int i = 0; i < radioBtns.size(); i++) {
				Button btn = radioBtns.get(i);
				if (i == index) {
					btn.setSelection(true);
				} else {
					btn.setSelection(false);
				}
			}
		}

		public void addSelectionListener(SelectionListener selectionListener) {
			IniRadioButtons();
			if (radioBtns.size() <= 0)
				return;
			for (int i = 0; i < radioBtns.size(); i++) {
				radioBtns.get(i).addSelectionListener(selectionListener);
			}

		}
	}

	protected RadioComposite addRadioButton(Composite parent, String[] labels, Key key, String[] values, int indent) {
		ControlData data = new ControlData(key, values);

		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 3;
		gd.horizontalIndent = indent;

		RadioComposite composite = new RadioComposite(parent, SWT.NONE);
		composite.setLayoutData(gd);
		GridLayout layout = new GridLayout(3, false);
		composite.setLayout(layout);
		composite.setText(labels[0]);

		for (int i = 1; i < labels.length; i++) {
			Button radioBtn = new Button(composite, SWT.RADIO);
			radioBtn.setFont(JFaceResources.getDialogFont());
			radioBtn.setLayoutData(new GridData());
			radioBtn.setText(labels[i]);
		}

		composite.setData(data);
		composite.addSelectionListener(getSelectionListener());
		String currValue = getValue(key);
		composite.setSelection(data.getSelection(currValue));
		fRadioButtons.add(composite);

		updateRadioComposite(composite);

		return composite;
	}

	protected Button addCheckBox(Composite parent, String label, Key key, String[] values, int indent) {
		ControlData data = new ControlData(key, values);

		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 3;
		gd.horizontalIndent = indent;

		Button checkBox = new Button(parent, SWT.CHECK);
		checkBox.setFont(JFaceResources.getDialogFont());
		checkBox.setText(label);
		checkBox.setData(data);
		checkBox.setLayoutData(gd);
		checkBox.addSelectionListener(getSelectionListener());

		String currValue = getValue(key);
		checkBox.setSelection(data.getSelection(currValue) == 0);

		fCheckBoxes.add(checkBox);

		updateCheckBox(checkBox);

		return checkBox;
	}

	protected Button addCheckBoxWithLink(Composite parent, String label, Key key, String[] values, int indent,
			int widthHint, SelectionListener listener) {
		ControlData data = new ControlData(key, values);

		GridData gd = new GridData(GridData.FILL, GridData.FILL, true, false);
		gd.horizontalSpan = 3;
		gd.horizontalIndent = indent;

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(gd);

		Button checkBox = new Button(composite, SWT.CHECK);
		checkBox.setFont(JFaceResources.getDialogFont());
		checkBox.setData(data);
		checkBox.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		checkBox.addSelectionListener(getSelectionListener());

		gd = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gd.widthHint = widthHint;

		Link link = new Link(composite, SWT.NONE);
		link.setText(label);
		link.setLayoutData(gd);
		if (listener != null) {
			link.addSelectionListener(listener);
		}

		String currValue = getValue(key);
		checkBox.setSelection(data.getSelection(currValue) == 0);

		fCheckBoxes.add(checkBox);

		updateCheckBox(checkBox);

		return checkBox;
	}

	protected Combo addComboBox(Composite parent, String label, Key key, String[] values, String[] valueLabels,
			int indent) {
		GridData gd = new GridData();
		gd.horizontalIndent = indent;

		Label labelControl = new Label(parent, SWT.NONE);
		labelControl.setFont(JFaceResources.getDialogFont());
		labelControl.setText(label);
		labelControl.setLayoutData(gd);

		Combo comboBox = newComboControl(parent, key, values, valueLabels);
		gd = new GridData();
		gd.widthHint = 200;
		comboBox.setLayoutData(gd);

		fLabels.put(comboBox, labelControl);

		updateCombo(comboBox);

		return comboBox;
	}

	protected Combo addInversedComboBox(Composite parent, String label, Key key, String[] values, String[] valueLabels,
			int indent) {
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent = indent;
		gd.horizontalSpan = 3;

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(gd);

		Combo comboBox = newComboControl(composite, key, values, valueLabels);
		comboBox.setFont(JFaceResources.getDialogFont());
		comboBox.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		Label labelControl = new Label(composite, SWT.LEFT | SWT.WRAP);
		labelControl.setText(label);
		labelControl.setLayoutData(new GridData());

		fLabels.put(comboBox, labelControl);

		updateCombo(comboBox);

		return comboBox;
	}

	protected Combo newComboControl(Composite composite, Key key, String[] values, String[] valueLabels) {
		ControlData data = new ControlData(key, values);

		Combo comboBox = new Combo(composite, SWT.READ_ONLY);
		comboBox.setVisibleItemCount(30);
		comboBox.setItems(valueLabels);
		comboBox.setData(data);
		comboBox.addSelectionListener(getSelectionListener());
		comboBox.setFont(JFaceResources.getDialogFont());

		String currValue = getValue(key);
		comboBox.select(data.getSelection(currValue));

		fComboBoxes.add(comboBox);

		return comboBox;
	}

	protected Text addTextField(Composite parent, String label, Key key, int indent, int widthHint) {
		return addTextField(parent, label, key, indent, widthHint, SWT.BORDER | SWT.SINGLE);
	}

	protected Text addTextField(Composite parent, String label, Key key, int indent, int widthHint, int style) {
		Label labelControl = null;
		if (label != null && label.length() > 0) {
			labelControl = new Label(parent, SWT.WRAP);
			labelControl.setText(label);
			labelControl.setFont(JFaceResources.getDialogFont());
			labelControl.setLayoutData(new GridData());
		}
		Text textBox = new Text(parent, style);
		textBox.setData(key);
		textBox.setLayoutData(new GridData());

		if (labelControl != null)
			fLabels.put(textBox, labelControl);

		String currValue = getValue(key);
		if (currValue != null) {
			textBox.setText(currValue);
		}
		textBox.addModifyListener(getTextModifyListener());

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		if (widthHint != 0) {
			data.widthHint = widthHint;
		} else
			data.grabExcessHorizontalSpace = true;
		data.horizontalIndent = indent;
		if (labelControl != null)
			data.horizontalSpan = 2;
		else
			data.horizontalSpan = 3;
		textBox.setLayoutData(data);

		fTextBoxes.add(textBox);

		updateText(textBox);

		return textBox;
	}

	protected SelectionListener getSelectionListener() {
		if (fSelectionListener == null) {
			fSelectionListener = new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					controlChanged(e.widget);
				}
			};
		}
		return fSelectionListener;
	}

	protected ModifyListener getTextModifyListener() {
		if (fTextModifyListener == null) {
			fTextModifyListener = new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					textChanged((Text) e.widget);
				}
			};
		}
		return fTextModifyListener;
	}

	protected void controlChanged(Widget widget) {

		ControlData data = (ControlData) widget.getData();
		String newValue = null;
		if (widget instanceof Button) {
			if ((((Button) widget).getStyle() & SWT.RADIO) != 0) {
				RadioComposite parent = (RadioComposite) ((Button) widget).getParent();
				data = (ControlData) parent.getData();
				newValue = data.getValue(parent.getBtnIndex((Button) widget));
			} else {
				newValue = data.getValue(((Button) widget).getSelection());
			}

		} else if (widget instanceof Combo) {
			newValue = data.getValue(((Combo) widget).getSelectionIndex());
		} else {
			return;
		}
		String oldValue = setValue(data.getKey(), newValue);
		validateSettings(data.getKey(), oldValue, newValue);
	}

	protected void textChanged(Text textControl) {
		Key key = (Key) textControl.getData();
		String number = textControl.getText();
		String oldValue = setValue(key, number);
		validateSettings(key, oldValue, number);
	}

	protected boolean checkValue(Key key, String value) {
		return value.equals(getValue(key));
	}

	protected String getValue(Key key) {
		if (fDisabledProjectSettings != null) {
			return (String) fDisabledProjectSettings.get(key);
		}
		return key.getStoredValue(fPref);
	}

	protected boolean getBooleanValue(Key key) {
		return Boolean.valueOf(getValue(key)).booleanValue();
	}

	protected String setValue(Key key, String value) {
		if (fDisabledProjectSettings != null) {
			return (String) fDisabledProjectSettings.put(key, value);
		}
		String oldValue = getValue(key);
		key.setStoredValue(fPref, value);
		return oldValue;
	}

	protected String setValue(Key key, boolean value) {
		return setValue(key, String.valueOf(value));
	}

	/**
	 * Returns the value as actually stored in the preference store.
	 * 
	 * @param key
	 * @return the value as actually stored in the preference store.
	 */
	protected String getStoredValue(Key key) {
		return key.getStoredValue(fPref);
	}

	public void useProjectSpecificSettings(boolean enable) {
		boolean hasProjectSpecificOption = fDisabledProjectSettings == null;
		if (enable != hasProjectSpecificOption && fProject != null) {
			if (enable) {
				for (int i = 0; i < fAllKeys.length; i++) {
					Key curr = fAllKeys[i];
					String val = (String) fDisabledProjectSettings.get(curr);
					curr.setStoredValue(fPref, val);
				}
				fDisabledProjectSettings = null;
				updateControls();
				validateSettings(null, null, null);
			} else {
				fDisabledProjectSettings = new IdentityHashMap();
				for (int i = 0; i < fAllKeys.length; i++) {
					Key curr = fAllKeys[i];
					String oldSetting = curr.getStoredValue(fPref);
					fDisabledProjectSettings.put(curr, oldSetting);
				}
			}
		}
	}

	public boolean hasProjectSpecificOptions(IProject project) {
		if (project != null) {
			Key[] allKeys = fAllKeys;
			IPreferences prefs = PreferenceFactory.getInstance().getPreferences(this.fPlugin, project);
			for (int i = 0; i < allKeys.length; i++) {
				if (prefs.isDefault(allKeys[i].getName())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean areSettingsEnabled() {
		return fDisabledProjectSettings == null || fProject == null;
	}

	public boolean performOk() {
		return performApply();
	}

	public boolean performApply() {
		return processChanges(); // apply directly
	}

	protected boolean processChanges() {
		if (fDisabledProjectSettings != null) {
			for (int i = 0; i < fAllKeys.length; i++) {
				if (fProject != null) {
					if (ignoreKeys != null && Arrays.asList(ignoreKeys).contains(fAllKeys[i])) {
						continue;
					}
				}
				fAllKeys[i].setToDefault(fPref);
			}
		} else {

			for (int i = 0; i < fAllKeys.length; i++) {
				if (fProject != null) {
					if (ignoreKeys != null && Arrays.asList(ignoreKeys).contains(fAllKeys[i])) {
						continue;
					}
				}
				fAllKeys[i].apply(fPref);
			}
		}
		try {
			fPref.save();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void performDefaults() {
		for (int i = 0; i < fAllKeys.length; i++) {
			Key curr = fAllKeys[i];
			String defValue = fAllKeys[i].getDefaultValue(fPref);
			setValue(curr, defValue);
		}

		settingsUpdated();
		updateControls();
		validateSettings(null, null, null);
	}

	public void dispose() {
	}

	protected void updateControls() {
		// update the UI
		for (int i = fCheckBoxes.size() - 1; i >= 0; i--) {
			updateCheckBox((Button) fCheckBoxes.get(i));
		}
		for (int i = fComboBoxes.size() - 1; i >= 0; i--) {
			updateCombo((Combo) fComboBoxes.get(i));
		}
		for (int i = fTextBoxes.size() - 1; i >= 0; i--) {
			updateText((Text) fTextBoxes.get(i));
		}

		for (int i = fRadioButtons.size() - 1; i >= 0; i--) {
			updateRadioComposite((RadioComposite) fRadioButtons.get(i));
		}
	}

	protected void updateCombo(Combo curr) {
		ControlData data = (ControlData) curr.getData();

		String currValue = getValue(data.getKey());
		curr.select(data.getSelection(currValue));

		if (fProject != null) {
			if (ignoreKeys != null && Arrays.asList(ignoreKeys).contains(data.getKey())) {
				ControlEnableState.disable(curr);
				Control label = (Control) fLabels.get(curr);
				if (label != null)
					ControlEnableState.disable(label);
			}
		}
	}

	protected void updateCheckBox(Button curr) {
		ControlData data = (ControlData) curr.getData();

		String currValue = getValue(data.getKey());
		curr.setSelection(data.getSelection(currValue) == 0);

		if (fProject != null) {
			if (ignoreKeys != null && Arrays.asList(ignoreKeys).contains(data.getKey())) {
				ControlEnableState.disable(curr);
			}
		}
	}

	protected void updateRadioComposite(RadioComposite curr) {
		ControlData data = (ControlData) curr.getData();

		String currValue = getValue(data.getKey());
		curr.setSelection(data.getSelection(currValue));

		if (fProject != null) {
			if (ignoreKeys != null && Arrays.asList(ignoreKeys).contains(data.getKey())) {
				ControlEnableState.disable(curr);
			}
		}
	}

	protected void updateText(Text curr) {
		Key key = (Key) curr.getData();

		String currValue = getValue(key);
		if (currValue != null) {
			curr.setText(currValue);
		}

		if (fProject != null) {
			if (ignoreKeys != null && Arrays.asList(ignoreKeys).contains(key)) {
				ControlEnableState.disable(curr);
				Control label = (Control) fLabels.get(curr);
				if (label != null)
					ControlEnableState.disable(label);
			}
		}
	}

	protected Button getCheckBox(Key key) {
		for (int i = fCheckBoxes.size() - 1; i >= 0; i--) {
			Button curr = (Button) fCheckBoxes.get(i);
			ControlData data = (ControlData) curr.getData();
			if (key.equals(data.getKey())) {
				return curr;
			}
		}
		return null;
	}

	protected Combo getComboBox(Key key) {
		for (int i = fComboBoxes.size() - 1; i >= 0; i--) {
			Combo curr = (Combo) fComboBoxes.get(i);
			ControlData data = (ControlData) curr.getData();
			if (key.equals(data.getKey())) {
				return curr;
			}
		}
		return null;
	}

	protected Text getTextControl(Key key) {
		for (int i = fTextBoxes.size() - 1; i >= 0; i--) {
			Text curr = (Text) fTextBoxes.get(i);
			ControlData data = (ControlData) curr.getData();
			if (key.equals(data.getKey())) {
				return curr;
			}
		}
		return null;
	}

	protected Control findControl(Key key) {
		Combo comboBox = getComboBox(key);
		if (comboBox != null) {
			return comboBox;
		}
		Button checkBox = getCheckBox(key);
		if (checkBox != null) {
			return checkBox;
		}
		Text text = getTextControl(key);
		if (text != null) {
			return text;
		}
		return null;
	}

	protected void setComboEnabled(Key key, boolean enabled) {
		Combo combo = getComboBox(key);
		Label label = (Label) fLabels.get(combo);
		combo.setEnabled(enabled);
		label.setEnabled(enabled);
	}

	protected Text addLabelledTextField(Composite parent, String label, Key key, int textlimit, int indent,
			boolean dummy) {
		PixelConverter pixelConverter = new PixelConverter(parent);

		Label labelControl = new Label(parent, SWT.WRAP);
		labelControl.setText(label);
		labelControl.setLayoutData(new GridData());

		Text textBox = new Text(parent, SWT.BORDER | SWT.SINGLE);
		textBox.setData(key);
		textBox.setLayoutData(new GridData());

		fLabels.put(textBox, labelControl);

		String currValue = getValue(key);
		if (currValue != null) {
			textBox.setText(currValue);
		}
		textBox.addModifyListener(getTextModifyListener());

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		if (textlimit != 0) {
			textBox.setTextLimit(textlimit);
			data.widthHint = pixelConverter.convertWidthInCharsToPixels(textlimit + 1);
		}
		data.horizontalIndent = indent;
		data.horizontalSpan = 2;
		textBox.setLayoutData(data);

		fTextBoxes.add(textBox);
		return textBox;
	}

	/*
	 * (non-javadoc) Update fields and validate. @param changedKey Key that changed,
	 * or null, if all changed.
	 */
	protected void validateSettings(Key changedKey, String oldValue, String newValue) {

	}
}
