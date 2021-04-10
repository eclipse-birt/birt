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

package org.eclipse.birt.chart.reportitem.ui.views.attributes.widget;

import java.util.Arrays;

import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChoicePropertyDescriptorProvider;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.PropertyDescriptor;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributesUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * ChoicePropertyDescriptor manages Combo choice control.
 */
public class ChoicePropertyDescriptor extends PropertyDescriptor {

	protected CCombo combo;

	private String oldValue;

	private int style = SWT.BORDER;

	public ChoicePropertyDescriptor(boolean isFormStyle) {
		super.setFormStyle(isFormStyle);
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
		if (!isFormStyle()) {
			combo = new CCombo(parent, style | SWT.READ_ONLY);
		} else {
			combo = FormWidgetFactory.getInstance().createCCombo(parent, true);
		}
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
				handleSelectEvent();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				handleSelectEvent();
			}
		});
		return combo;
	}

	/**
	 * Processes the save action.
	 */
	protected void handleSelectEvent() {
		String newValue = ChartUIUtil.getText(combo);
		if (ChoiceSetFactory.CHOICE_NONE.equals(newValue)) {
			newValue = null;
		}

		try {
			save(newValue);
		} catch (SemanticException e) {
			AttributesUtil.handleError(e);
			ChartUIUtil.setText(combo, oldValue);
			combo.setSelection(new Point(0, oldValue.length()));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * PropertyDescriptor#resetUIData()
	 */
	public void load() {
		String[] items = new String[0];
		String[] values = null;

		combo.setItems(provider.getItems());
		values = provider.getValues();

		oldValue = provider.load().toString();
		boolean stateFlag = ((oldValue == null) == combo.getEnabled());
		if (stateFlag)
			combo.setEnabled(oldValue != null);

		if (provider.isReadOnly()) {
			combo.setEnabled(false);
		}

		int sindex = values == null ? Arrays.asList(items).indexOf(oldValue) : Arrays.asList(values).indexOf(oldValue);

		if (provider.assertProperty() && sindex < 0) {
			if (oldValue != null && oldValue.length() > 0) {
				ChartUIUtil.setText(combo, oldValue);
				return;
			}

			if (combo.getItemCount() > 0) {
				combo.select(0);
				return;
			}
		}

		combo.select(sindex);
	}

	/**
	 * @return Returns the SWT style.
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * Add a SWT style to the combo widget
	 * 
	 * @param style The SWT style to add.
	 */
	public void addStyle(int style) {
		this.style |= style;
	}

	public void save(Object obj) throws SemanticException {
		provider.save(obj);
	}

	ChoicePropertyDescriptorProvider provider;

	public void setDescriptorProvider(IDescriptorProvider provider) {
		super.setDescriptorProvider(provider);
		if (provider instanceof ChoicePropertyDescriptorProvider)
			this.provider = (ChoicePropertyDescriptorProvider) provider;
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(combo, isHidden);
	}

	public void setVisible(boolean isVisible) {
		combo.setVisible(isVisible);
	}

	public void setInput(Object input) {
		super.setInput(input);
		getDescriptorProvider().setInput(input);
	}

}