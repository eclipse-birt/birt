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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TextAndButtonDescriptor extends PropertyDescriptor {

	public TextAndButtonDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	private int buttonWidth = 60;

	@Override
	public Control createControl(Composite parent) {
		Composite composite = FormWidgetFactory.getInstance().createComposite(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 1;
		layout.horizontalSpacing = 8;
		layout.verticalSpacing = 0;

		composite.setLayout(layout);
		text = DescriptorToolkit.createTextPropertyDescriptor(isFormStyle());
		if (provider != null) {
			text.setDescriptorProvider(provider);
		}
		text.createControl(composite);
		if (textText != null) {
			text.setText(textText);
		}
		WidgetUtil.setGridData(text.getControl(), 1, true);
		button = FormWidgetFactory.getInstance().createButton(composite, SWT.PUSH, isFormStyle());
		if (buttonText != null) {
			button.setText(buttonText);
		}
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
		GridData data = new GridData();
		data.widthHint = Math.max(button.computeSize(-1, -1).x, buttonWidth);

		data.grabExcessHorizontalSpace = false;
		button.setLayoutData(data);
		descriptorContainer.add(text);
		return composite;
	}

	/**
	 * At the default select button operation,you could use this method.
	 *
	 */
	protected void onClickButton() {
	}

	private List selectList = new ArrayList();

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

	public void forceFocus() {
		text.getControl().forceFocus();
	}

	@Override
	public void setInput(Object input) {
		text.setInput(input);
	}

	@Override
	public Object getInput() {
		return text.getInput();
	}

	@Override
	public void load() {
		text.load();
	}

	private String textText;

	public void setText(String aText) {
		if (text.getControl() != null) {
			text.setText(aText);
		}
		this.textText = aText;
	}

	private String buttonText;

	public void setButtonText(String aText) {
		if (button != null) {
			button.setText(aText);
		}
		this.buttonText = aText;
	}

	@Override
	public void save(Object obj) throws SemanticException {
		text.save(obj);
	}

	private TextPropertyDescriptor text;
	private Button button;

	IDescriptorProvider provider;

	@Override
	public void setDescriptorProvider(IDescriptorProvider provider) {
		if (this.text != null) {
			this.text.setDescriptorProvider(provider);
		}
		this.provider = provider;
	}

	public int getButtonWidth() {
		return buttonWidth;
	}

	public void setButtonWidth(int buttonWidth) {
		this.buttonWidth = buttonWidth;
		if (button != null) {
			GridData data = new GridData();
			data.widthHint = Math.max(button.computeSize(-1, -1).x, buttonWidth);

			data.grabExcessHorizontalSpace = false;
			button.setLayoutData(data);
		}
	}

}
