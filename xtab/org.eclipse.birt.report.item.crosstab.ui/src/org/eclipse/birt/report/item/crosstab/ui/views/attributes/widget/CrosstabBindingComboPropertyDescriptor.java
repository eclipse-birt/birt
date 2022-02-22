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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.widget;

import java.util.Arrays;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.PropertyDescriptor;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabBindingComboPropertyDescriptorProvider;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Administrator
 *
 */
public class CrosstabBindingComboPropertyDescriptor extends PropertyDescriptor {

	protected CCombo combo;

	protected CubeHandle oldValue;

	private int style = SWT.BORDER;

	private FocusAdapter focusListener;

	/**
	 * @param propertyProcessor
	 */
	public CrosstabBindingComboPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	@Override
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
	@Override
	public Control getControl() {
		return combo;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#
	 * createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createControl(Composite parent) {
		if (isFormStyle()) {
			combo = FormWidgetFactory.getInstance().createCCombo(parent, true);
		} else {
			combo = new CCombo(parent, style | SWT.READ_ONLY);
			combo.setVisibleItemCount(30);
		}
		addListeners();
		return combo;
	}

	protected void addListeners() {
		combo.addControlListener(new ControlListener() {

			@Override
			public void controlMoved(ControlEvent e) {
				combo.clearSelection();
			}

			@Override
			public void controlResized(ControlEvent e) {
				combo.clearSelection();
			}
		});
		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				handleComboSelectEvent();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				handleComboSelectEvent();
			}
		});

		focusListener = new FocusAdapter() {

			@Override
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
		CubeHandle newValue = ((CrosstabBindingComboPropertyDescriptorProvider) getDescriptorProvider()).getItems()
				.get(combo.getSelectionIndex());
		try {
			save(newValue);
		} catch (SemanticException e) {
			int index = ((CrosstabBindingComboPropertyDescriptorProvider) getDescriptorProvider()).getItems()
					.indexOf(oldValue);
			if (index > -1) {
				combo.select(index);
			} else {
				combo.deselectAll();
			}
			WidgetUtil.processError(combo.getShell(), e);
		}

	}

	/**
	 * @return Returns the SWT style.
	 */
	public int getStyle() {
		return style;
	}

	@Override
	public void load() {
		oldValue = (CubeHandle) getDescriptorProvider().load();
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

	protected void setComboEditable(boolean isEditable) {
		combo.setEditable(isEditable);
		if (focusListener != null) {
			combo.removeFocusListener(focusListener);
			if (combo.getEditable()) {
				combo.addFocusListener(focusListener);
			}
		}
	}

	@Override
	public void save(Object value) throws SemanticException {
		getDescriptorProvider().save(value);
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

	protected void refresh(CubeHandle value) {
		String[] items = ((CrosstabBindingComboPropertyDescriptorProvider) getDescriptorProvider()).getItemNames();
		if (!Arrays.equals(combo.getItems(), items)) {
			combo.setItems(items);
		}
		int index = ((CrosstabBindingComboPropertyDescriptorProvider) getDescriptorProvider()).getItems()
				.indexOf(oldValue);
		if (index > -1) {
			combo.select(index);
		} else {
			combo.deselectAll();
		}
	}
}
