/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import org.eclipse.birt.report.designer.internal.ui.swt.custom.ComplexUnit;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.IDimensionValueChangedListener;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 
 */

public class ComplexUnitPropertyDescriptor extends PropertyDescriptor {

	protected ComplexUnit complexUnit;
	private int style = SWT.BORDER;
	protected static final String ERROR_MESSAGE = Messages.getString("UnitPropertyDescriptor.error.message"); //$NON-NLS-1$
	protected static final String ERROR_BOX_TITLE = Messages.getString("UnitPropertyDescriptor.error.title"); //$NON-NLS-1$

	protected String deMeasureValue;
	protected String deUnitValue;

	public ComplexUnitPropertyDescriptor(boolean formStyle) {
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
		return complexUnit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#
	 * createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl(Composite parent) {
		if (isFormStyle()) {
			complexUnit = FormWidgetFactory.getInstance().createComplexUnit(parent);
		} else
			complexUnit = new ComplexUnit(parent, style);

		complexUnit.addValueChangeListener(new IDimensionValueChangedListener() {

			public void valueChanged(String newValue, String unit) {
				handleEvent(newValue, unit);
			}

		});
		complexUnit.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (complexUnit.getUnits() != null && complexUnit.getUnits().length > 0
						&& (complexUnit.getUnit() == null || complexUnit.getUnit().length() == 0)) {
					if (getDescriptorProvider() instanceof UnitPropertyDescriptorProvider) {
						String unit = ((UnitPropertyDescriptorProvider) getDescriptorProvider()).getDefaultUnit();
						if (unit != null) {
							complexUnit.setUnit(unit);
						} else {
							complexUnit.setUnit(complexUnit.getUnits()[0]);
						}
					}

				}
			}
		});
		return complexUnit;
	}

	protected boolean validateDimensionValue(String value, String unit) {
		if (getDescriptorProvider() instanceof UnitPropertyDescriptorProvider) {
			return ((UnitPropertyDescriptorProvider) getDescriptorProvider()).validateDimensionValue(value, unit);
		}
		return true;
	}

	protected void handleEvent(String value, String unit) {
		if (!validateDimensionValue(value, unit)) {
			ExceptionUtil.openError(ERROR_BOX_TITLE, ERROR_MESSAGE);
			load();
			return;
		}

		if (value == null || value.equals("")) //$NON-NLS-1$
		{
			value = null;
		} else {
			if (getDescriptorProvider() instanceof UnitPropertyDescriptorProvider) {
				String unitName = ((UnitPropertyDescriptorProvider) getDescriptorProvider()).getUnitName(unit);
				if (unitName != null)
					value += unitName;
			}
			if (value.equals(deMeasureValue + deUnitValue))
				return;
		}
		try {
			save(value);
		} catch (SemanticException e) {
			complexUnit.setValue(Double.parseDouble(deMeasureValue));
			WidgetUtil.processError(complexUnit.getShell(), e);

		}
	}

	public void load() {
		if (getDescriptorProvider() instanceof UnitPropertyDescriptorProvider) {
			deMeasureValue = ((UnitPropertyDescriptorProvider) getDescriptorProvider()).getMeasureValue();

			if (deMeasureValue == null)
				deMeasureValue = ""; //$NON-NLS-1$
			if (!deMeasureValue.equals(String.valueOf(complexUnit.getValue()))) {
				try {
					complexUnit.setValue(deMeasureValue);
				} catch (NumberFormatException e) {
				}
			}
			String[] items = ((UnitPropertyDescriptorProvider) getDescriptorProvider()).getUnitItems();
			if (!Arrays.equals(complexUnit.getUnits(), items)) {
				complexUnit.setUnits(items);
			}
			try {
				deUnitValue = ((UnitPropertyDescriptorProvider) getDescriptorProvider())
						.getUnitDisplayName(((UnitPropertyDescriptorProvider) getDescriptorProvider()).getUnit());
			} catch (Exception e) {
				ExceptionUtil.handle(e);
				return;
			}
			if (deUnitValue == null)
				complexUnit.deselectUnit();
			else if (!deUnitValue.equals(complexUnit.getUnit())) {
				complexUnit.selectUnit(Arrays.asList(items).indexOf(deUnitValue));
			}
		}
	}

	public void save(Object obj) throws SemanticException {
		if (!isReadOnly)
			getDescriptorProvider().save(obj);
	}

	private boolean isReadOnly = false;

	public void setReadOnly(boolean isReadOnly) {
		complexUnit.setReadOnly(isReadOnly);
		this.isReadOnly = isReadOnly;
	}

	public void setVisible(boolean isVisible) {
		complexUnit.setVisible(isVisible);
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(complexUnit, isHidden);
	}

}
