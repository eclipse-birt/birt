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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IComboDescriptorProvider;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * ComboPropertyDescriptor manages Combo choice control.
 */
public class ComboPropertyDescriptor extends PropertyDescriptor {

	protected CCombo combo;

	protected IChoiceSet choiceSet;

	protected String oldValue;

	public ComboPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	@Override
	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}

	private int style = SWT.BORDER | SWT.READ_ONLY;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.attributes.widget.PropertyDescriptor
	 * #resetUIData()
	 */
	void refresh(String value) {

		if (getDescriptorProvider() instanceof IComboDescriptorProvider) {
			String[] items = ((IComboDescriptorProvider) getDescriptorProvider()).getItems();
			if (combo.getItems() == null || !Arrays.equals(combo.getItems(), items)) {
				combo.setItems(items);
			}

			oldValue = ((IComboDescriptorProvider) getDescriptorProvider()).load().toString();

			boolean stateFlag = ((oldValue == null) == combo.getEnabled());

			if (((IComboDescriptorProvider) getDescriptorProvider()).isReadOnly()) {
				combo.setEnabled(false);
			} else if (stateFlag) {
				combo.setEnabled(oldValue != null);
			}
			String displayName = ((IComboDescriptorProvider) getDescriptorProvider()).getDisplayName(oldValue);
			if (displayName == null) {
				if (oldValue != null && combo.indexOf(oldValue) > -1 && combo.getText().equals(oldValue)) {
					return;
				}
				combo.deselectAll();
				combo.setText(oldValue);
			} else {
				int index = Arrays.asList(items).indexOf(displayName);
				if (combo.getSelectionIndex() != index) {
					combo.select(index);
				}
			}
		}
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
			combo = FormWidgetFactory.getInstance().createCCombo(parent);
		} else {
			combo = new CCombo(parent, style);
			combo.setVisibleItemCount(30);
		}
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
		return combo;
	}

	/**
	 * Processes the save action.
	 */
	private void handleComboSelectEvent() {
		try {
			save(combo.getText());
		} catch (SemanticException e) {
			combo.setText(oldValue);
			WidgetUtil.processError(combo.getShell(), e);
		}
	}

	@Override
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

	@Override
	public void load() {
		oldValue = getDescriptorProvider().load().toString();
		refresh(oldValue);
	}

	public void addStyle(int style) {
		this.style |= style;
	}
}
