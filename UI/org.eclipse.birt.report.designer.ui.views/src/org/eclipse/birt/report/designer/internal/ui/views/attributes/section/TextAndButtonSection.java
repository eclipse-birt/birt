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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.TextPropertyDescriptor;
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

public class TextAndButtonSection extends Section {

	public TextAndButtonSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
	}

	protected int width = -1;

	protected boolean fillText = false;

	protected TextPropertyDescriptor textField;

	@Override
	public void createSection() {
		if (selectList == null) {
			selectList = new ArrayList();
		}
		getLabelControl(parent);
		getTextControl(parent);
		getButtonControl(parent);
		getGridPlaceholder(parent);
	}

	@Override
	public void layout() {
		GridData gd = (GridData) textField.getControl().getLayoutData();
		if (getLayoutNum() > 0) {
			gd.horizontalSpan = getLayoutNum() - 2 - placeholder;
		} else {
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 2 - placeholder;
		}
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else {
			gd.grabExcessHorizontalSpace = fillText;
		}

		gd = (GridData) button.getLayoutData();

		if (buttonWidth > -1) {
			if (!isComputeSize) {
				gd.widthHint = Math.max(button.computeSize(-1, -1).x, buttonWidth);
			} else {
				gd.widthHint = button.computeSize(-1, -1).x;
			}
		}
	}

	public TextPropertyDescriptor getTextControl() {
		return textField;
	}

	protected TextPropertyDescriptor getTextControl(Composite parent) {
		if (textField == null) {
			textField = DescriptorToolkit.createTextPropertyDescriptor(true);
			if (getProvider() != null) {
				textField.setDescriptorProvider(getProvider());
			}
			textField.createControl(parent);
			textField.getControl().setLayoutData(new GridData());
			textField.getControl().addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					textField = null;
				}
			});
		} else {
			checkParent(textField.getControl(), parent);
		}
		return textField;
	}

	protected Button button;

	public Button getButtonControl() {
		return button;
	}

	protected Button getButtonControl(Composite parent) {
		if (button == null) {
			button = FormWidgetFactory.getInstance().createButton(parent, SWT.PUSH, isFormStyle);
			button.setFont(parent.getFont());

			button.setLayoutData(new GridData());
			String text = getButtonText();
			if (text != null) {
				button.setText(text);
			}

			text = getButtonTooltipText();
			if (text != null) {
				button.setToolTipText(text);
			}

			button.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					button = null;
				}
			});

			if (!selectList.isEmpty()) {
				button.addSelectionListener((SelectionListener) selectList.get(0));
			} else {
				SelectionListener listener = new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						onClickButton();
					}
				};
				selectList.add(listener);
			}

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
		if (textField != null) {
			textField.setDescriptorProvider(provider);
		}
	}

	protected List selectList = new ArrayList();

	/**
	 * if use this method , you couldn't use the onClickButton method.
	 */
	public void addSelectionListener(SelectionListener listener) {
		if (!selectList.contains(listener)) {
			if (!selectList.isEmpty()) {
				removeSelectionListener((SelectionListener) selectList.get(0));
			}
			selectList.add(listener);
			if (button != null) {
				button.addSelectionListener(listener);
			}
		}
	}

	public void removeSelectionListener(SelectionListener listener) {
		if (selectList.contains(listener)) {
			selectList.remove(listener);
			if (button != null) {
				button.removeSelectionListener(listener);
			}
		}
	}

	protected void onClickButton() {
	}

	public void forceFocus() {
		textField.getControl().forceFocus();
	}

	@Override
	public void setInput(Object input) {
		textField.setInput(input);
	}

	@Override
	public void load() {
		if (textField != null && !textField.getControl().isDisposed()) {
			textField.load();
		}
		if (button != null && !button.isDisposed()) {
			button.setEnabled(!isReadOnly());
		}
	}

	protected int buttonWidth = 60;

	public void setButtonWidth(int buttonWidth) {
		this.buttonWidth = buttonWidth;
		if (button != null) {
			GridData data = new GridData();
			data.widthHint = Math.max(button.computeSize(-1, -1).x, buttonWidth);

			data.grabExcessHorizontalSpace = false;
			button.setLayoutData(data);
		}
	}

	protected boolean isComputeSize = false;

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
		if (textField != null) {
			if (value == null) {
				value = "";//$NON-NLS-1$
			}
			oldValue = textField.getText();
			if (!oldValue.equals(value)) {
				textField.setText(value);
			}
		}
	}

	public boolean isFillText() {
		return fillText;
	}

	public void setFillText(boolean fillText) {
		this.fillText = fillText;
	}

	@Override
	public void setHidden(boolean isHidden) {
		if (displayLabel != null) {
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		}
		if (textField != null) {
			textField.setHidden(isHidden);
		}
		if (button != null) {
			WidgetUtil.setExcludeGridData(button, isHidden);
		}
		if (placeholderLabel != null) {
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
		}
	}

	@Override
	public void setVisible(boolean isVisible) {
		if (displayLabel != null) {
			displayLabel.setVisible(isVisible);
		}
		if (textField != null) {
			textField.setVisible(isVisible);
		}
		if (button != null) {
			button.setVisible(isVisible);
		}
		if (placeholderLabel != null) {
			placeholderLabel.setVisible(isVisible);
		}
	}

	private String buttonTooltipText;

	public void setButtonTooltipText(String string) {
		this.buttonTooltipText = string;
		if (button != null) {
			button.setText(buttonTooltipText);
		}

	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
		if (button != null) {
			button.setText(buttonText);
		}
	}

	public String getButtonTooltipText() {
		return buttonTooltipText;
	}

	public boolean buttonIsComputeSize() {
		return isComputeSize;
	}

	public void setButtonIsComputeSize(boolean isComputeSize) {
		this.isComputeSize = isComputeSize;
	}

}
