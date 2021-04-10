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

package org.eclipse.birt.report.designer.ui.widget;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.CCombo;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * The Expression Cell Editor of BIRT. The editor include a combo box and a
 * builder button. All system predefined and customer defined Expression are
 * listed in the combo box. User can select the Expression in that list, input
 * the RGB value into the combo box or click the builder button to open the
 * Expression Builder dialog to select the right Expression.
 */
public class ComboBoxExpressionCellEditor extends DialogCellEditor {

	/**
	 * The ComboBox to keep the system defined and customer defined Expression.
	 */
	private CCombo comboBox;

	/**
	 * The list of items to present in the combo box.
	 */
	private String[] items;

	/**
	 * The zero-based index of the selected item.
	 */
	int selection;

	/**
	 * Default ComboBoxCellEditor style
	 */
	protected static final int defaultStyle = SWT.NONE;

	/**
	 * The composite to keep the combo box and button together
	 */
	private Composite composite;

	private Button btnPopup;

	/**
	 * Creates a new dialog cell editor whose parent is given. The combo box lists
	 * is <code>null</code> initially.
	 * 
	 * @param parent the parent control
	 */
	public ComboBoxExpressionCellEditor(Composite parent) {
		super(parent);
		setStyle(defaultStyle);
	}

	/**
	 * Creates a new dialog cell editor whose parent is given. The combo box lists
	 * is initialized with the given items.
	 * 
	 * @param parent the parent control
	 * @param items  the combo box list to be initialized
	 */
	public ComboBoxExpressionCellEditor(Composite parent, String[] items) {
		this(parent, items, defaultStyle);
	}

	/**
	 * Creates a new dialog cell editor whose parent and style are given. The combo
	 * box lists is initialized with the given items.
	 * 
	 * @param parent the parent control
	 * @param items  the combo box list to be initialized
	 * @param style  the style of this editor
	 */
	public ComboBoxExpressionCellEditor(Composite parent, String[] items, int style) {
		super(parent, style);
		setItems(items);
	}

	/**
	 * Returns the list of choices for the combo box
	 * 
	 * @return the list of choices for the combo box
	 */
	public String[] getItems() {
		return items;
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

			for (int i = 0; i < items.length; i++)
				comboBox.add(items[i], i);

			setValueValid(true);
			selection = 0;
		}
	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	protected Control createContents(Composite cell) {
		Color bg = cell.getBackground();
		composite = new Composite(cell, getStyle());
		composite.setBackground(bg);

		composite.setLayout(new FillLayout());

		comboBox = new CCombo(composite, SWT.NONE);
		comboBox.setBackground(bg);
		comboBox.setFont(cell.getFont());
		comboBox.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				Object newValue = comboBox.getText();
				if (newValue != null) {
					boolean newValidState = isCorrect(newValue);
					if (newValidState) {
						markDirty();
						doSetValue(newValue);
					} else {
						// try to insert the current value into the error
						// message.
						setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { newValue.toString() }));
					}
					fireApplyEditorValue();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				Object newValue = comboBox.getText();
				if (newValue != null) {
					boolean newValidState = isCorrect(newValue);
					if (newValidState) {
						markDirty();
						doSetValue(newValue);
					} else {
						// try to insert the current value into the error
						// message.
						setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { newValue.toString() }));
					}
					fireApplyEditorValue();
				}
			}
		});
		comboBox.addFocusListener(new FocusAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.FocusAdapter#focusLost(org.eclipse.swt.events.
			 * FocusEvent)
			 */
			public void focusLost(FocusEvent e) {
				if (btnPopup != null && !btnPopup.isFocusControl()
						&& Display.getCurrent().getCursorControl() != btnPopup) {
					applyEditorValueAndDeactivate();
					super.focusLost(e);

				}
			}

		});
		return composite;
	}

	/**
	 * Apply the currently selected value and de-actiavate the cell editor.
	 */
	void applyEditorValueAndDeactivate() {
		// must set the selection before getting value
		selection = comboBox.getSelectionIndex();
		Object newValue = doGetValue();

		markDirty();
		boolean isValid = isCorrect(newValue);
		setValueValid(isValid);
		if (!isValid) {
			// try to insert the current value into the error message.
			setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { items[selection] }));
		}
		fireApplyEditorValue();
		deactivate();
	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
		ExpressionBuilder dialog = new ExpressionBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				comboBox.getText());
		dialog.setExpressionProvier(provider);
		if (dialog.open() == Dialog.OK) {
			return dialog.getResult();
		}
		setFocus();
		return null;
	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	protected void updateContents(Object value) {
		if (comboBox == null)
			return;

		String text = "";//$NON-NLS-1$
		if (value != null) {
			text = value.toString();
		}
		comboBox.setText(text);
	}

	public void setEnable(boolean flag) {
		comboBox.setEnabled(flag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	protected void doSetFocus() {
		comboBox.setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	protected Object doGetValue() {
		int selection = comboBox.getSelectionIndex();
		if (selection == -1) {
			return comboBox.getText();
		}
		return comboBox.getItem(selection);
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
		btnPopup = super.createButton(parent);
		return btnPopup;
	}

	private IExpressionProvider provider;

	public void setExpressionProvider(IExpressionProvider provider) {
		this.provider = provider;
	}

	/**
	 * @deprecated Please use setExpressionProvider ( IExpressionProvider provider )
	 *             instead
	 */

	public void addFilter(ExpressionFilter filter) {

	}

	/**
	 * @deprecated Please use setExpressionProvider ( IExpressionProvider provider )
	 *             instead
	 */
	public void setDataSetList(List list) {

	}
}