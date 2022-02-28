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

package org.eclipse.birt.report.designer.internal.ui.views.property.widgets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.util.NumberUtil;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ibm.icu.util.ULocale;

/**
 * The Color Cell Editor of IARD. The editor inlucde a combo box and a builder
 * button. All system predefined and customer defined color are listed in the
 * combobox. User can select the color in that list, input the RGB value into
 * the comobox or click the builder button to open the color dialog to select
 * the right color.
 */
public class ComboBoxDimensionCellEditor extends CDialogCellEditor {

	/**
	 * The ComboBox to keep the system defined and customer defined colors
	 */
	private CCombo comboBox;

	/**
	 * The list of items to present in the combo box.
	 */
	private String[] items;

	private Map itemKeyMap;
	private Map valueKeyMap;

	/**
	 * The zero-based index of the selected item.
	 */
	int selection;

	/**
	 * Default ComboBoxCellEditor style
	 */
	private static final int defaultStyle = SWT.NONE;

	/**
	 * The composite to keep the combobox and button together
	 */
	private Composite composite;

	private String unitName;

	private String[] units;

	private int inProcessing = 0;

	/**
	 * Creates a new dialog cell editor parented under the given control. The
	 * combobox lists is <code>null</code> initially
	 *
	 * @param parent the parent control
	 */
	public ComboBoxDimensionCellEditor(Composite parent) {
		super(parent);
		setStyle(defaultStyle);
	}

	/**
	 * Creates a new dialog cell editor parented under the given control. The combo
	 * box box lists is initialized with the items parameter
	 *
	 * @param parent the parent control
	 * @param items  the initilizing combobox list
	 */
	public ComboBoxDimensionCellEditor(Composite parent, String[] items) {
		this(parent, items, defaultStyle);
	}

	public ComboBoxDimensionCellEditor(Composite parent, String[] items, String[] values) {
		this(parent, items, values, defaultStyle);
	}

	/**
	 * Creates a new dialog cell editor parented under the given control and givend
	 * style. The combo box box lists is initialized with the items parameter
	 *
	 * @param parent the parent control
	 * @param items  the initilizing combobox list
	 * @param style  the style of this editor
	 */
	public ComboBoxDimensionCellEditor(Composite parent, String[] items, int style) {
		this(parent, items, null, style);
	}

	public ComboBoxDimensionCellEditor(Composite parent, String[] items, String[] values, int style) {
		super(parent, style);
		if (items != null) {
			if (values != null) {
				assert (values.length == items.length);
				itemKeyMap = new HashMap();
				valueKeyMap = new HashMap();
				for (int i = 0; i < items.length; i++) {
					itemKeyMap.put(items[i], values[i]);
					valueKeyMap.put(values[i], items[i]);
				}

			}
			Arrays.sort(items);
		}
		setItems(items);
	}

	/**
	 * Returns the list of choices for the combo box
	 *
	 * @return the list of choices for the combo box
	 */
	public String[] getItems() {
		return this.items;
	}

	/**
	 * Sets the list of choices for the combo box
	 *
	 * @param items the list of choices for the combo box
	 */
	public void setItems(String[] items) {
		Assert.isNotNull(items);
		this.items = items;
		populateComboBoxItems();
	}

	/**
	 * Updates the list of choices for the combo box for the current control.
	 */
	private void populateComboBoxItems() {
		if (comboBox != null && items != null) {
			comboBox.removeAll();
			for (int i = 0; i < items.length; i++) {
				comboBox.add(items[i], i);
			}

			setValueValid(true);
			selection = 0;
		}
	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	@Override
	protected Control createContents(final Composite cell) {
		Color bg = cell.getBackground();
		cell.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				ComboBoxDimensionCellEditor.this.focusLost();
			}
		});
		composite = new Composite(cell, getStyle());
		composite.setBackground(bg);
		composite.setLayout(new FillLayout());

		comboBox = new CCombo(composite, SWT.NONE);
		comboBox.setVisibleItemCount(30);
		comboBox.setBackground(bg);
		comboBox.setFont(cell.getFont());

		comboBox.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				applyEditorValueAndDeactivate();
			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				selection = comboBox.getSelectionIndex();
			}
		});

		comboBox.addKeyListener(new KeyAdapter() {

			// hook key pressed - see PR 14201
			@Override
			public void keyPressed(KeyEvent e) {
				keyReleaseOccured(e);
			}
		});

		comboBox.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});

		comboBox.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				ComboBoxDimensionCellEditor.this.focusLost();
			}
		});
		comboBox.getListeners(SWT.Verify);
		return composite;
	}

	/**
	 * Applies the currently selected value and deactiavates the cell editor
	 */
	void applyEditorValueAndDeactivate() {
		inProcessing = 1;
		doValueChanged();
		fireApplyEditorValue();
		deactivate();
		inProcessing = 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.
	 * swt.widgets.Control)
	 */
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		DimensionBuilderDialog dialog = new DimensionBuilderDialog(cellEditorWindow.getShell());

		DimensionValue value;
		try {
			value = StringUtil.parseInput((String) comboBox.getText(), ULocale.getDefault());
		} catch (PropertyValueException e) {
			value = null;
		}

		dialog.setUnitNames(units);
		dialog.setUnitName(unitName);

		if (value != null) {
			dialog.setMeasureData(new Double(value.getMeasure()));
		}

		if (Window.OK == dialog.open()) {
			deactivate();

			String newValue = null;
			Double doubleValue = 0.0;
			if (dialog.getMeasureData() instanceof Double) {
				doubleValue = (Double) dialog.getMeasureData();
			} else if (dialog.getMeasureData() instanceof DimensionValue) {
				doubleValue = ((DimensionValue) dialog.getMeasureData()).getMeasure();
			}
			DimensionValue dValue = new DimensionValue(doubleValue, dialog.getUnitName());
			if (dValue != null) {
				newValue = dValue.toDisplayString();
			}
			return newValue;

		} else {
			comboBox.setFocus();
			return null;
		}
	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	@Override
	protected void updateContents(Object value) {
		if (comboBox == null) {
			return;
		}

		String text = "";//$NON-NLS-1$
		if (value != null) {
			if (value instanceof String) {
				DimensionValue dValue;
				try {
					dValue = StringUtil.parseInput((String) value, ULocale.getDefault());
				} catch (PropertyValueException e) {
					dValue = null;
				}

				if (dValue != null) {
					text = NumberUtil.double2LocaleNum(dValue.getMeasure()) + dValue.getUnits();
				} else {
					text = value.toString();
				}

			} else {
				text = value.toString();
			}
		}

		int index = -1;
		if (valueKeyMap != null) {
			String item = (String) valueKeyMap.get(value);
			if (item != null) {
				index = comboBox.indexOf(item);
			}

		}
		if (index >= 0) {
			text = comboBox.getItem(index);
		}

		comboBox.setText(text);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#keyReleaseOccured(org.eclipse.swt
	 * .events.KeyEvent)
	 */
	@Override
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		if (keyEvent.character == '\u001b') { // Escape character
			fireCancelEditor();
		} else if (keyEvent.character == '\t') { // tab key
			applyEditorValueAndDeactivate();
		} else if (keyEvent.character == '\r') { // Return key
			applyEditorValueAndDeactivate();
		}
	}

	/**
	 * Set current units
	 *
	 * @param units
	 */
	public void setUnits(String units) {
		this.unitName = units;
	}

	/**
	 * Set units list the needed by dimension dialog
	 *
	 * @param unitsList
	 */
	public void setUnitsList(String[] unitsList) {
		this.units = unitsList;
	}

	/**
	 * Processes a focus lost event that occurred in this cell editor.
	 * <p>
	 * The default implementation of this framework method applies the current value
	 * and deactivates the cell editor. Subclasses should call this method at
	 * appropriate times. Subclasses may also extend or reimplement.
	 * </p>
	 */
	@Override
	protected void focusLost() {
		if (inProcessing == 1) {
			return;
		}
		super.focusLost();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	@Override
	protected void doSetFocus() {
		comboBox.setFocus();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.property.widgets.
	 * CDialogCellEditor#doValueChanged()
	 */
	@Override
	protected void doValueChanged() {
		String comboText = comboBox.getText();
		if (comboBox.indexOf(comboText) >= 0) {
			int index = comboBox.indexOf(comboText);
			comboBox.select(index);
		}
		if (selection != comboBox.getSelectionIndex()) {
			markDirty();
		}
		// must set the selection before getting value
		selection = comboBox.getSelectionIndex();
		Object newValue = null;
		if (selection == -1) {

			Object oldValue = doGetValue();
			if (oldValue instanceof String) {
				oldValue = parseString2dValue((String) oldValue);
			}

			newValue = comboBox.getText();
			DimensionValue dValue = parseString2dValue((String) newValue);
			if (dValue != null) {
				newValue = dValue.toDisplayString();
			}

			if (dValue != null && (!dValue.equals(oldValue))) {
				markDirty();
				doSetValue(newValue);
				return;
			}

		} else if (itemKeyMap != null) {
			newValue = itemKeyMap.get(comboBox.getItem(selection));
		} else {
			newValue = comboBox.getItem(selection);
		}

		if (newValue != null) {
			boolean newValidState = isCorrect(newValue);
			if (newValidState) {
				doSetValue(newValue);
				markDirty();
			} else {
			}
		}
	}

	private DimensionValue parseString2dValue(String strValue) {
		DimensionValue dValue = null;
		try {
			dValue = StringUtil.parseInput((String) strValue, ULocale.getDefault());
		} catch (PropertyValueException e) {
			dValue = null;
		}

		return dValue;
	}
}
