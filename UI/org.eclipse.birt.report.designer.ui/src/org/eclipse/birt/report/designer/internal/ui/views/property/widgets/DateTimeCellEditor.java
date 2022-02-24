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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * A cell editor that manages a date-time property.
 */
public class DateTimeCellEditor extends CDialogCellEditor {

	/**
	 * Creates a new date-time cell editor parented under the given control.
	 * 
	 * @param parent the parent control
	 */
	public DateTimeCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * Creates a new date-time cell editor parented under the given control.
	 * 
	 * @param parent the parent control
	 * @param style  the style bits
	 */
	public DateTimeCellEditor(Composite parent, int style) {
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
		TimeOptionDialog dialog = new TimeOptionDialog(cellEditorWindow.getShell());
		Object value = getValue();
		Date dateValue = new Date();
		try {
			if (value != null) {
				TimeDialogInfo time = new TimeDialogInfo();
				if (value instanceof String && !value.toString().trim().equals("")) //$NON-NLS-1$
				{
					dateValue = new SimpleDateFormat(time.getFormat()).parse(value.toString());
				} else if (value instanceof Date) {
					dateValue = (Date) value;
				}
				time.setTime(dateValue.getTime());
				dialog.setInfo(time);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		dialog.open();
		if (dialog.getReturnCode() == SelectionDialog.OK) {
			TimeDialogInfo result = (TimeDialogInfo) dialog.getInfo();
			dateValue = new Date(result.getTime());
		}
		return dateValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.property.widgets.
	 * CDialogCellEditor#doValueChanged()
	 */
	protected void doValueChanged() {
		// nothing

	}

}
