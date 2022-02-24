/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.CComboPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ComboAndButtonSection extends Section {

	public ComboAndButtonSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
	}

	private int width = -1;

	private boolean fillCCombo = false;

	protected CComboPropertyDescriptor combo;

	public void createSection() {
		if (buttonSelectList == null)
			buttonSelectList = new ArrayList();
		if (comboSelectList == null)
			comboSelectList = new ArrayList();
		getLabelControl(parent);
		getComboControl(parent);
		getButtonControl(parent);
		getGridPlaceholder(parent);
	}

	public void layout() {
		GridData gd = (GridData) combo.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - 2 - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 2 - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillCCombo;

		gd = (GridData) button.getLayoutData();

		if (buttonWidth > -1)
			gd.widthHint = Math.max(button.computeSize(-1, -1).x, buttonWidth);
	}

	protected CComboPropertyDescriptor getComboControl(Composite parent) {
		if (combo == null) {
			combo = DescriptorToolkit.createCComboPropertyDescriptor(true);
			if (getProvider() != null)
				combo.setDescriptorProvider(getProvider());
			combo.createControl(parent);
			combo.getControl().setLayoutData(new GridData());
			combo.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					combo = null;
				}
			});
			if (!comboSelectList.isEmpty()) {
				combo.addComboSelectionListener((SelectionListener) comboSelectList.get(0));
			}

		} else {
			checkParent(combo.getControl(), parent);
		}
		return combo;
	}

	public CComboPropertyDescriptor getComboControl() {
		return combo;
	}

	protected Button button;

	public Button getButtonControl() {
		return button;
	}

	protected Button getButtonControl(Composite parent) {
		if (button == null) {
			button = FormWidgetFactory.getInstance().createButton(parent, SWT.PUSH, isFormStyle);
			button.setFont(parent.getFont());
			String text = getButtonText();
			button.setLayoutData(new GridData());
			if (text != null) {
				button.setText(text);
			}
			button.addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					button = null;
				}
			});

			if (buttonSelectList.isEmpty()) {
				SelectionListener listener = new SelectionAdapter() {

					public void widgetSelected(SelectionEvent e) {
						onClickButton();
					}
				};
				buttonSelectList.add(listener);
			}
			button.addSelectionListener((SelectionListener) buttonSelectList.get(0));

		} else {
			checkParent(button, parent);
		}
		return button;
	}

	private String buttonText;

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (combo != null)
			combo.setDescriptorProvider(provider);
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
		if (button != null)
			button.setText(buttonText);
	}

	protected List buttonSelectList = new ArrayList();

	/**
	 * if use this method , you couldn't use the onClickButton method.
	 */
	public void addButtonSelectionListener(SelectionListener listener) {
		if (!buttonSelectList.contains(listener)) {
			if (!buttonSelectList.isEmpty())
				removeButtonSelectionListener((SelectionListener) buttonSelectList.get(0));
			buttonSelectList.add(listener);
			if (button != null)
				button.addSelectionListener(listener);
		}
	}

	public void removeButtonSelectionListener(SelectionListener listener) {
		if (buttonSelectList.contains(listener)) {
			buttonSelectList.remove(listener);
			if (button != null)
				button.removeSelectionListener(listener);
		}
	}

	protected List comboSelectList = new ArrayList();

	public void addComboSelectionListener(SelectionListener listener) {
		if (!comboSelectList.contains(listener)) {
			if (!comboSelectList.isEmpty())
				removeComboSelectionListener((SelectionListener) comboSelectList.get(0));
			comboSelectList.add(listener);
			if (combo != null)
				combo.addComboSelectionListener(listener);
		}
	}

	public void removeComboSelectionListener(SelectionListener listener) {
		if (comboSelectList.contains(listener)) {
			comboSelectList.remove(listener);
			if (combo != null)
				combo.removeComboSelectionListener(listener);
		}
	}

	protected void onClickButton() {
	};

	public void forceFocus() {
		combo.getControl().forceFocus();
	}

	public void setInput(Object input) {
		combo.setInput(input);
	}

	public void load() {
		if (combo != null && !combo.getControl().isDisposed())
			combo.load();
	}

	private int buttonWidth = 60;

	public void setButtonWidth(int buttonWidth) {
		this.buttonWidth = buttonWidth;
		if (button != null) {
			GridData data = new GridData();
			data.widthHint = Math.max(button.computeSize(-1, -1).x, buttonWidth);
			;
			data.grabExcessHorizontalSpace = false;
			button.setLayoutData(data);
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getButtonWidth() {
		return buttonWidth;
	}

	private String oldValue;

	public void setStringValue(String value) {
		if (combo != null) {
			if (value == null) {
				value = "";//$NON-NLS-1$
			}
			oldValue = combo.getStringValue();
			if (!oldValue.equals(value)) {
				combo.setStringValue(value);
			}
		}
	}

	public boolean isFillText() {
		return fillCCombo;
	}

	public void setFillText(boolean fillCCombo) {
		this.fillCCombo = fillCCombo;
	}

	public void setHidden(boolean isHidden) {
		if (displayLabel != null)
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		if (combo != null)
			combo.setHidden(isHidden);
		if (button != null)
			WidgetUtil.setExcludeGridData(button, isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (displayLabel != null)
			displayLabel.setVisible(isVisible);
		if (combo != null)
			combo.setVisible(isVisible);
		if (button != null)
			button.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}

}
