/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.widget;

import java.util.List;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.4 $ $Date: 2007/08/28 03:28:27 $
 */
public class ExpressionCellEditor extends DialogCellEditor {

	private transient Text editor;
	private transient Button theButton;

	/**
	 * 
	 */
	public ExpressionCellEditor() {
		super();
	}

	/**
	 * @param parent
	 */
	public ExpressionCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public ExpressionCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.
	 * widgets.Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
		ExpressionBuilder dialog = new ExpressionBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				(String) getValue());
		dialog.setExpressionProvier(provider);

		if (dialog.open() != Window.OK) {
			// If editor dialog canceled, we need reset the focus to the text
			// control, avoiding the button to gain focus.
			editor.setFocus();
		}

		return dialog.getResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.DialogCellEditor#createContents(org.eclipse.swt.
	 * widgets.Composite)
	 */
	protected Control createContents(Composite cell) {
		editor = new Text(cell, SWT.NONE);
		editor.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				keyReleaseOccured(e);
			}
		});
		editor.addSelectionListener(new SelectionAdapter() {

			public void widgetDefaultSelected(SelectionEvent e) {
				fireApplyEditorValue();
				deactivate();
			}
		});
		editor.addTraverseListener(new TraverseListener() {

			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});
		editor.addFocusListener(new FocusAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.FocusAdapter#focusLost(org.eclipse.swt.events.
			 * FocusEvent)
			 */
			public void focusLost(FocusEvent e) {
				ExpressionCellEditor.this.focusLost();
			}

		});
		setValueValid(true);

		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.DialogCellEditor#updateContents(java.lang.Object)
	 */
	protected void updateContents(Object value) {
		if (editor != null && value != null) {
			editor.setText((String) value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	protected void doSetFocus() {
		if (editor != null) {
			editor.setFocus();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	protected Object doGetValue() {
		if (editor != null) {
			return editor.getText();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
	 */
	protected void doSetValue(Object value) {
		if (editor != null && value != null) {
			editor.setText((String) value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#focusLost()
	 */
	protected void focusLost() {
		if (theButton != null && !theButton.isFocusControl() && Display.getCurrent().getCursorControl() != theButton) {
			super.focusLost();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#createButton(org.eclipse.swt.
	 * widgets.Composite)
	 */
	protected Button createButton(Composite parent) {
		theButton = super.createButton(parent);

		return theButton;
	}

	private IExpressionProvider provider;

	public void setExpressionProvider(IExpressionProvider provider) {
		this.provider = provider;
	}

	/**
	 * @deprecated Please use setExpressionProvider( IExpressionProvider ) instead
	 */
	public void addFilter(ExpressionFilter filter) {
	}

	/**
	 * @deprecated Please use setExpressionProvider( IExpressionProvider ) instead
	 */
	public void setDataSetList(List dataSetList) {

	}
}
