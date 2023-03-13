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

package org.eclipse.birt.report.designer.ui.widget;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * ExpressionDialogCellEditor contains a Label and a Button control for
 * presenting an Expression builder UI.
 */
public class ExpressionDialogCellEditor extends DialogCellEditor {

	/**
	 *
	 */
	public ExpressionDialogCellEditor() {
		super();
	}

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
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.
	 * widgets.Control)
	 */
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		String oldValue = (String) getValue();
		ExpressionBuilder dialog = new ExpressionBuilder(cellEditorWindow.getShell(), oldValue);
		dialog.setExpressionProvier(provider);

		if (dialog.open() == Dialog.OK) {
			String newValue = dialog.getResult();
			if (!newValue.equals(oldValue)) {
				deactivate();
				return newValue;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.property.widgets.
	 * CDialogCellEditor#doValueChanged()
	 */
	protected void doValueChanged() {
		// TODO Auto-generated method stub

	}

	private IExpressionProvider provider;

	public void setExpressionProvider(IExpressionProvider provider) {
		this.provider = provider;
	}
}
