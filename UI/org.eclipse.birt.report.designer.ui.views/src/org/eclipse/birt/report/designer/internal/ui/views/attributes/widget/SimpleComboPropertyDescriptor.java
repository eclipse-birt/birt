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

import java.util.Arrays;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ISimpleComboDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * ComboPropertyDescriptor manages Combo choice control.
 */
public class SimpleComboPropertyDescriptor extends PropertyDescriptor {

	protected CCombo combo;

	protected String oldValue;

	private int style = SWT.BORDER;

	private FocusAdapter focusListener;

	/**
	 * @param propertyProcessor
	 */
	public SimpleComboPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * PropertyDescriptor#getControl()
	 */
	public Control getControl() {
		return combo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#
	 * createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl(Composite parent) {
		if (isFormStyle()) {
			combo = FormWidgetFactory.getInstance().createCCombo(parent, false);
		} else {
			combo = new CCombo(parent, style);
			combo.setVisibleItemCount(30);
		}
		addListeners();
		return combo;
	}

	protected void addListeners() {
		combo.addControlListener(new ControlListener() {

			public void controlMoved(ControlEvent e) {
				combo.clearSelection();
			}

			public void controlResized(ControlEvent e) {
				combo.clearSelection();
			}
		});
		combo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				handleComboSelectEvent();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				handleComboSelectEvent();
			}
		});

		focusListener = new FocusAdapter() {

			public void focusLost(FocusEvent e) {
				if (combo.isEnabled()) {
					handleComboSelectEvent();
				}
			}

		};

	}

	/**
	 * Processes Combo Select Event.
	 */
	protected void handleComboSelectEvent() {
		String newValue = combo.getText();
		if (ChoiceSetFactory.CHOICE_NONE.equals(newValue)) {
			newValue = null;
		}

		try {
			save(newValue);
		} catch (SemanticException e) {
			combo.setText(oldValue);
			combo.setSelection(new Point(0, oldValue.length()));
			WidgetUtil.processError(combo.getShell(), e);
		}

	}

	/**
	 * @return Returns the SWT style.
	 */
	public int getStyle() {
		return style;
	}

	public void load() {
		oldValue = getDescriptorProvider().load().toString();
		refresh(oldValue);
	}

	/**
	 * Add a SWT style to the combo widget
	 * 
	 * @param style The SWT style to add.
	 */
	public void addStyle(int style) {
		this.style |= style;
	}

	protected void refresh(String value) {
		if (getDescriptorProvider() instanceof ISimpleComboDescriptorProvider) {
			String[] items = ((ISimpleComboDescriptorProvider) getDescriptorProvider()).getItems();
			combo.setItems(items);
			boolean stateFlag = ((value == null) == combo.getEnabled());
			if (stateFlag)
				combo.setEnabled(value != null);

			if (((PropertyDescriptorProvider) getDescriptorProvider()).isReadOnly()) {
				combo.setEnabled(false);
			}

			boolean isEditable = ((ISimpleComboDescriptorProvider) getDescriptorProvider()).isEditable();
			setComboEditable(isEditable);

			int sindex = Arrays.asList(items).indexOf(oldValue);

			if (((ISimpleComboDescriptorProvider) getDescriptorProvider()).isSpecialProperty() && sindex < 0) {
				if (value != null && value.length() > 0) {
					combo.setText(value);
					return;
				}

				if (combo.getItemCount() > 0) {
					combo.select(0);
					return;
				}
			}

			combo.select(sindex);
		}
	}

	protected void setComboEditable(boolean isEditable) {
		combo.setEditable(isEditable);
		if (focusListener != null) {
			combo.removeFocusListener(focusListener);
			if (combo.getEditable()) {
				combo.addFocusListener(focusListener);
			}
		}
	}

	public void save(Object value) throws SemanticException {
		descriptorProvider.save(value);
	}

	public String getStringValue() {
		return combo.getText();
	}

	public void setStringValue(String value) {
		combo.setText(value);
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(combo, isHidden);
	}

	public void setVisible(boolean isVisible) {
		combo.setVisible(isVisible);
	}
}
