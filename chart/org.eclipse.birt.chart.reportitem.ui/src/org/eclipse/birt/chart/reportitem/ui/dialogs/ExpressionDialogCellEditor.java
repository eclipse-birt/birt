/*******************************************************************************
 * Copyright (c) 2004,2005,2006,2007 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui.dialogs;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * ExpressionDialogCellEditor contains a Label and a Button control for
 * presenting an Expression builder UI.
 */
public class ExpressionDialogCellEditor extends DialogCellEditor implements ModifyListener {

	private DesignElementHandle itemHandle;

	private Text text;

	private Button btnPopup;

	/**
	 * @param parent
	 */
	public ExpressionDialogCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public ExpressionDialogCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	protected Control createContents(Composite cell) {
		Color bg = cell.getBackground();
		Composite composite = new Composite(cell, getStyle());
		composite.setBackground(bg);

		composite.setLayout(new FillLayout());

		text = new Text(composite, SWT.SINGLE);
		{
			text.setBackground(bg);
			text.setFont(cell.getFont());
			text.addModifyListener(this);
			text.addKeyListener(new KeyAdapter() {

				public void keyPressed(KeyEvent e) {
					if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
						applyEditorValueAndDeactivate();
					}
				}
			});
			text.addFocusListener(new FocusAdapter() {

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

					}
				}

			});
		}

		return composite;
	}

	protected Button createButton(Composite parent) {
		btnPopup = super.createButton(parent);
		return btnPopup;
	}

	/**
	 * Apply the currently selected value and de-actiavate the cell editor.
	 */
	void applyEditorValueAndDeactivate() {
		// must set the selection before getting value
		Object newValue = doGetValue();

		markDirty();
		boolean isValid = isCorrect(newValue);
		setValueValid(isValid);
		fireApplyEditorValue();
		deactivate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	protected Object doGetValue() {
		return text.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	protected void doSetFocus() {
		text.setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.
	 * widgets.Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
		return openDialogBox(cellEditorWindow, (String) getValue());
	}

	String openDialogBox(Control cellEditorWindow, String oldValue) {
		ExpressionBuilder dialog = new ExpressionBuilder(cellEditorWindow.getShell(), oldValue);
		dialog.setExpressionProvier(new ExpressionProvider(itemHandle));

		if (dialog.open() == Dialog.OK) {
			String newValue = dialog.getResult();
			if (!newValue.equals(oldValue)) {
				return newValue;
			}
		}
		setFocus();
		return null;
	}

	public void setItemHandle(DesignElementHandle handle) {
		itemHandle = handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.
	 * ModifyEvent)
	 */
	public void modifyText(ModifyEvent e) {

	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	protected void updateContents(Object value) {
		if (text == null)
			return;

		String displayText = "";//$NON-NLS-1$
		if (value != null) {
			displayText = value.toString();
		}
		text.setText(displayText);
	}
}
