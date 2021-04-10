/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MarignPropertyDescriptorProvider;
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

/**
 * PropertyDescriptor manages combo & combo choice control.
 */
public class MarignPropertyDescriptor extends PropertyDescriptor {

	private static final String ERROR_MESSAGE = Messages.getString("MarignPropertyDescriptor.error.message"); //$NON-NLS-1$

	private static final String ERROR_BOX_TITLE = Messages.getString("MarignPropertyDescriptor.error.title"); //$NON-NLS-1$

	protected CCombo valueCombo, unitCombo;

	protected Composite container;

	private String deMesurementValue, deUnitValue;

	private boolean hasError = false;

	private boolean dirty = false;

	/**
	 * @param propertyHandle the property handle
	 */
	public MarignPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
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
				handleSelectedEvent();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				handleSelectedEvent();
			}
		};

		valueCombo = new CCombo(container, SWT.FLAT);
		valueCombo.setVisibleItemCount(30);
		valueCombo.addSelectionListener(listener);
		valueCombo.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				unitCombo.setEnabled(valueCombo.indexOf(valueCombo.getText()) == -1);
				if (unitCombo.isEnabled() && unitCombo.getItemCount() > 0
						&& (unitCombo.getText() == null || unitCombo.getText().length() == 0)
						&& marignProvider != null) {
					String unit = marignProvider.getDefaultUnit();
					if (unit != null) {
						unitCombo.setText(unit);
					} else {
						unitCombo.setText(unitCombo.getItem(0));
					}
				}
				dirty = true;
			}
		});
		valueCombo.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				dirty = false;
			}

			public void focusLost(FocusEvent e) {
				if (!hasError) {
					if (dirty)
						handleSelectedEvent();
				}
			}
		});

		unitCombo = new CCombo(container, SWT.FLAT | SWT.READ_ONLY);
		unitCombo.addSelectionListener(listener);

		// GridData data = new GridData( );
		// data.grabExcessHorizontalSpace = true;
		// data.horizontalAlignment = GridData.FILL;
		// valueCombo.setLayoutData( data );
		// data = new GridData( );
		//
		// data.grabExcessHorizontalSpace = true;
		// data.horizontalAlignment = GridData.FILL;
		// data.widthHint = valueCombo.getSize( ).x + 4;
		// unitCombo.setLayoutData( data );

		GridData data = new GridData();
		data.widthHint = (int) (unitCombo.computeSize(SWT.DEFAULT, SWT.DEFAULT).x * 1.5);
		data.widthHint = data.widthHint < 126 ? 126 : data.widthHint;
		if (valueCombo.computeSize(SWT.DEFAULT, SWT.DEFAULT).y < unitCombo.computeSize(SWT.DEFAULT, SWT.DEFAULT).y)
			data.heightHint = unitCombo.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - 2;
		valueCombo.setLayoutData(data);
		unitCombo.setVisibleItemCount(30);
		data = new GridData(GridData.FILL_HORIZONTAL);
		unitCombo.setLayoutData(data);

		return container;
	}

	protected boolean validateDimensionValue() {
		if (unitCombo.isEnabled() && unitCombo.getSelectionIndex() != -1
				&& getDescriptorProvider() instanceof MarignPropertyDescriptorProvider) {
			return ((MarignPropertyDescriptorProvider) getDescriptorProvider())
					.validateDimensionValue(valueCombo.getText(), unitCombo.getText());
		}
		return true;
	}

	protected void handleSelectedEvent() {
		if (!validateDimensionValue()) {
			setError();
			ExceptionUtil.openError(ERROR_BOX_TITLE, ERROR_MESSAGE);
			load();
			clearError();
			return;
		}

		String value = valueCombo.getText();

		if (value.equals("")) //$NON-NLS-1$
		{
			value = null;
		} else if (marignProvider != null) {
			String valueName = marignProvider.getValueDisplayName(value);
			if (valueName == null) {
				value = value + marignProvider.getUnitDisplayName(unitCombo.getText());
			} else
				value = valueName;
		}

		try {
			save(value);
		} catch (SemanticException e) {
			valueCombo.setText(deMesurementValue);
			unitCombo.setText(deUnitValue);
			WidgetUtil.processError(unitCombo.getShell(), e);
		}
		dirty = false;
	} /*
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
	 * resetUIData()
	 */
	public void load() {
		if (marignProvider != null) {
			String value = marignProvider.load().toString();
			deMesurementValue = value;

			// special case for null value.
			if (value == null) {
				valueCombo.deselectAll();
				valueCombo.setEnabled(false);
				unitCombo.deselectAll();
				unitCombo.setEnabled(false);
				return;
			}
			boolean stateFlag = ((value == null) == valueCombo.getEnabled());
			if (stateFlag) {
				valueCombo.setEnabled(value != null);
				unitCombo.setEnabled(value != null);
			}

			valueCombo.setItems(marignProvider.getValueItems());
			unitCombo.setItems(marignProvider.getUnitItems());

			String valueName = marignProvider.getValueName(value);
			if (valueName != null) {
				valueCombo.setText(valueName);
				deMesurementValue = valueName;
				unitCombo.deselectAll();
				unitCombo.setEnabled(false);
				return;
			}

			try {
				deUnitValue = marignProvider.getUnit();
			} catch (Exception e) {
				ExceptionUtil.handle(e);
			}
			String unitName = marignProvider.getUnitName(deUnitValue);
			if (unitName != null) {
				unitCombo.setText(unitName);
				deUnitValue = unitName;
			}

			deMesurementValue = marignProvider.getMeasureValue();
			valueCombo.setText(deMesurementValue);
		}
	}

	private void setError() {
		hasError = true;
	}

	private void clearError() {
		hasError = false;
	}

	public void save(Object obj) throws SemanticException {
		getDescriptorProvider().save(obj);
	}

	private MarignPropertyDescriptorProvider marignProvider;

	public void setDescriptorProvider(IDescriptorProvider provider) {
		super.setDescriptorProvider(provider);
		if (provider instanceof MarignPropertyDescriptorProvider)
			this.marignProvider = (MarignPropertyDescriptorProvider) provider;
	}

	public void setHidden(boolean isHidden) {
		if (container != null)
			WidgetUtil.setExcludeGridData(container, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (container != null)
			container.setVisible(isVisible);
	}

	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}

}