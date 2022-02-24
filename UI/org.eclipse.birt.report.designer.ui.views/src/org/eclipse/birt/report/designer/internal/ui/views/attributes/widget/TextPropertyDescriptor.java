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

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ITextDescriptorProvider;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class TextPropertyDescriptor extends PropertyDescriptor {

	private int style = SWT.NULL;

	protected Text text;

	private String deValue;

	public TextPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}

	public Control createControl(Composite parent) {
		if (isFormStyle()) {
			text = FormWidgetFactory.getInstance().createText(parent, "", //$NON-NLS-1$
					style);
		} else
			text = new Text(parent, style);
		if (textLimit > 0) {
			text.setTextLimit(textLimit);
		}
		SelectionAdapter defaultSelectListener = new SelectionAdapter() {

			public void widgetDefaultSelected(SelectionEvent e) {
				handleTextSelectEvent();
			}
		};

		FocusAdapter defaultFocusListener = new FocusAdapter() {

			public void focusLost(FocusEvent e) {
				handleTextFocusLostEvent();
			}
		};
		text.addSelectionListener(defaultSelectListener);
		text.addFocusListener(defaultFocusListener);
		return text;
	}

	public Control getControl() {
		return text;
	}

	public void setText(String text) {
		this.text.setText(text);
	}

	public String getText() {
		return this.text.getText();
	}

	private void refresh(String value) {
		text.setText(value);
	}

	protected void handleTextSelectEvent() {
		processAction();
	}

	protected void handleTextFocusLostEvent() {
		processAction();
	}

	public void save(Object value) throws SemanticException {
		descriptorProvider.save(value);
	}

	/**
	 * Processes the save action.
	 */
	private void processAction() {
		String value = text.getText();
		if (!value.equals(deValue)) {
			try {
				save(value);
			} catch (SemanticException e1) {
				refresh(deValue);
				WidgetUtil.processError(text.getShell(), e1);
			}
		}

	}

	public void load() {
		deValue = (String) getDescriptorProvider().load();
		boolean stateFlag = ((deValue == null) == text.getEnabled());
		if (stateFlag) {
			text.setEnabled(deValue != null);
		}
		if (!((ITextDescriptorProvider) getDescriptorProvider()).isEditable()) {
			text.setEditable(false);
		}

		if (deValue == null)
			deValue = ""; //$NON-NLS-1$
		if (!text.getText().equals(deValue)) {
			refresh(deValue);
		}
	}

	private int textLimit;

	public void setTextLimit(int limit) {
		textLimit = limit;
		if (text != null) {
			text.setTextLimit(limit);
		}
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(text, isHidden);
	}

	public void setVisible(boolean isVisible) {
		text.setVisible(isVisible);
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

}
