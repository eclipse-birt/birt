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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * CheckPropertyDescriptor manages CheckBox control.
 */
public class CheckPropertyDescriptor extends PropertyDescriptor {

	protected Button button;

	public CheckPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * PropertyDescriptor#resetUIData()
	 */
	public void load() {
		String value = getDescriptorProvider().load().toString();

		boolean stateFlag = ((value == null) == button.getEnabled());
		if (stateFlag)
			button.setEnabled(value != null);

		boolean boolValue = "true".equalsIgnoreCase(value); //$NON-NLS-1$
		if (button.getSelection() != boolValue) {
			button.setSelection(boolValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * PropertyDescriptor#getControl()
	 */
	public Control getControl() {
		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#
	 * createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl(Composite parent) {
		if (isFormStyle()) {
			button = FormWidgetFactory.getInstance().createButton(parent, SWT.CHECK, true);
		} else
			button = new Button(parent, SWT.CHECK);
		button.setText(getDescriptorProvider().getDisplayName());
		if (!selectList.isEmpty())
			button.addSelectionListener((SelectionListener) selectList.get(0));
		else {
			SelectionListener listener = new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					onClickButton();
				}
			};
			selectList.add(listener);
		}
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				onClickButton();
			}
		});
		return button;
	}

	private List selectList = new ArrayList();

	/**
	 * if use this method , you couldn't use the onClickButton method.
	 */
	public void addSelectionListener(SelectionListener listener) {
		if (!selectList.contains(listener)) {
			if (!selectList.isEmpty())
				removeSelectionListener((SelectionListener) selectList.get(0));
			selectList.add(listener);
			if (button != null)
				button.addSelectionListener(listener);
		}
	}

	public void removeSelectionListener(SelectionListener listener) {
		if (selectList.contains(listener)) {
			selectList.remove(listener);
			if (button != null)
				button.removeSelectionListener(listener);
		}
	}

	/**
	 * Processes the save action.
	 */
	private void onClickButton() {
		String value = button.getSelection() ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$
		try {
			save(value);
		} catch (SemanticException e1) {
			WidgetUtil.processError(button.getShell(), e1);
		}
	}

	public void save(Object obj) throws SemanticException {
		descriptorProvider.save(obj);
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(button, isHidden);
	}

	public void setVisible(boolean isVisible) {
		button.setVisible(isVisible);
	}

	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}

}
