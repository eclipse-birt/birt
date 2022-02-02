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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * PropertyDescriptor manages input text control & combo choice control.
 */
public class UnitPropertyDescriptor extends PropertyDescriptor {

	protected static final String ERROR_MESSAGE = Messages.getString("UnitPropertyDescriptor.error.message"); //$NON-NLS-1$

	protected static final String ERROR_BOX_TITLE = Messages.getString("UnitPropertyDescriptor.error.title"); //$NON-NLS-1$

	protected Text text;

	protected CCombo combo;

	protected Composite container;

	protected String deMeasureValue, deUnitValue;

	private boolean hasError = false;

	private boolean dirty = false;

	/**
	 * constructor
	 * 
	 * @param propertyHandle The model instance
	 */
	public UnitPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.attributes.widget.PropertyDescriptor
	 * #resetUIData()
	 */
	public void load() {
		String value = getDescriptorProvider().load().toString();

		boolean stateFlag = ((value == null) == text.getEnabled());
		if (stateFlag) {
			text.setEnabled(value != null);
			combo.setEnabled(value != null);
		}

		if (getDescriptorProvider() instanceof UnitPropertyDescriptorProvider) {
			deMeasureValue = ((UnitPropertyDescriptorProvider) getDescriptorProvider()).getMeasureValue();

			if (deMeasureValue == null)
				deMeasureValue = ""; //$NON-NLS-1$
			if (!deMeasureValue.equals(text.getText())) {
				text.setText(deMeasureValue);
			}
			String[] items = ((UnitPropertyDescriptorProvider) getDescriptorProvider()).getUnitItems();

			if (!Arrays.equals(combo.getItems(), items)) {
				combo.setItems(items);
			}
			String deUnitValue;
			try {
				deUnitValue = ((UnitPropertyDescriptorProvider) getDescriptorProvider())
						.getUnitDisplayName(((UnitPropertyDescriptorProvider) getDescriptorProvider()).getUnit());
			} catch (Exception e) {
				ExceptionUtil.handle(e);
				return;
			}
			if (deUnitValue == null)
				combo.deselectAll();
			else if (!deUnitValue.equals(combo.getText())) {
				combo.select(Arrays.asList(items).indexOf(deUnitValue));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * PropertyDescriptor#getControl()
	 */
	public Control getControl() {
		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#
	 * createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 2;
		layout.marginWidth = 2;
		layout.horizontalSpacing = 4;
		layout.numColumns = 2;
		container.setLayout(layout);

		SelectionListener listener = new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				handleEvent();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				handleEvent();
			}
		};

		if (isFormStyle()) {
			text = FormWidgetFactory.getInstance().createText(container, "", //$NON-NLS-1$
					SWT.SINGLE | SWT.RIGHT);
		} else
			text = new Text(container, SWT.SINGLE | SWT.RIGHT);
		text.addSelectionListener(listener);
		text.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (combo.getItemCount() > 0 && (combo.getText() == null || combo.getText().length() == 0)) {
					if (getDescriptorProvider() instanceof UnitPropertyDescriptorProvider) {
						String unit = ((UnitPropertyDescriptorProvider) getDescriptorProvider()).getDefaultUnit();
						if (unit != null) {
							combo.setText(unit);
						} else {
							combo.setText(combo.getItem(0));
						}
					}

				}
				dirty = true;
			}
		});
		text.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				dirty = false;
			}

			public void focusLost(FocusEvent e) {
				if (!hasError) {
					if (dirty)
						handleEvent();
				}
			}
		});

		combo = new CCombo(container, SWT.FLAT | SWT.READ_ONLY);
		combo.setVisibleItemCount(30);
		combo.addSelectionListener(listener);

		GridData data = new GridData();
		data.widthHint = (int) (combo.computeSize(SWT.DEFAULT, SWT.DEFAULT).x * 1.5);
		if (text.computeSize(SWT.DEFAULT, SWT.DEFAULT).y < combo.computeSize(SWT.DEFAULT, SWT.DEFAULT).y)
			data.heightHint = combo.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - 2;
		text.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		combo.setLayoutData(data);

		return container;
	}

	protected boolean validateDimensionValue() {
		if (combo.isEnabled() && combo.getSelectionIndex() != -1) {
			String value = text.getText();
			String unit = combo.getText();
			if (getDescriptorProvider() instanceof UnitPropertyDescriptorProvider) {
				return ((UnitPropertyDescriptorProvider) getDescriptorProvider()).validateDimensionValue(value, unit);
			}
		}
		return true;
	}

	protected void handleEvent() {
		if (!validateDimensionValue()) {
			setError();
			ExceptionUtil.openError(ERROR_BOX_TITLE, ERROR_MESSAGE);
			load();
			clearError();
			return;
		}

		String value = text.getText().trim();
		if (value.equals("")) //$NON-NLS-1$
		{
			value = null;
		} else {
			if (getDescriptorProvider() instanceof UnitPropertyDescriptorProvider) {
				String unitName = ((UnitPropertyDescriptorProvider) getDescriptorProvider())
						.getUnitName(combo.getText());
				if (unitName != null)
					value += unitName;
			}
			if (value.equals(deMeasureValue + deUnitValue))
				return;
		}
		try {
			save(value);
		} catch (SemanticException e) {
			text.setText(deMeasureValue);
			WidgetUtil.processError(combo.getShell(), e);

		}
		dirty = false;
	}

	protected void setError() {
		hasError = true;
	}

	protected void clearError() {
		hasError = false;
	}

	public void save(Object obj) throws SemanticException {
		if (!isReadOnly)
			getDescriptorProvider().save(obj);
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(container, isHidden);
	}

	public void setVisible(boolean isVisible) {
		container.setVisible(isVisible);
	}

	private boolean isReadOnly = false;

	public void setReadOnly(boolean isReadOnly) {
		text.setEditable(!isReadOnly);
		combo.setEnabled(!isReadOnly);
		this.isReadOnly = isReadOnly;
	}

}
