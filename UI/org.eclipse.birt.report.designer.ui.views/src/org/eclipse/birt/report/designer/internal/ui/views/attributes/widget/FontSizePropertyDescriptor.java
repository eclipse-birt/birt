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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FontSizePropertyDescriptorProvider;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * FontSizePropertyDescriptor manages Font size control.
 */
public class FontSizePropertyDescriptor extends PropertyDescriptor {

	private FontSizeBuilder builder;

	/**
	 * @param propertyProcessor the property handle
	 */
	public FontSizePropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * PropertyDescriptor#resetUIData()
	 */
	void refresh(String value) {
		String strValue = getDescriptorProvider().load().toString();
		boolean stateFlag = ((strValue == null) == builder.getEnabled());
		if (stateFlag) {
			builder.setEnabled(strValue != null);
		}

		if (getDescriptorProvider() instanceof FontSizePropertyDescriptorProvider) {
			if (((FontSizePropertyDescriptorProvider) getDescriptorProvider()).isReadOnly()) {
				builder.setEnabled(false);
			}
		}
		builder.setFontSizeValue(strValue);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * PropertyDescriptor#getControl()
	 */
	@Override
	public Control getControl() {
		return builder;
	}

	@Override
	public Control createControl(Composite parent) {
		builder = new FontSizeBuilder(parent, SWT.NONE, isFormStyle());
		builder.addListener(SWT.Modify, new Listener() {

			@Override
			public void handleEvent(Event event) {
				handleBuilderModifyEvent();
			}
		});
		return builder;
	}

	/**
	 * Processes the save action.
	 */
	private void handleBuilderModifyEvent() {
		if (getDescriptorProvider() instanceof FontSizePropertyDescriptorProvider) {
			builder.setDefaultUnit(((FontSizePropertyDescriptorProvider) getDescriptorProvider()).getDefaultUnit());
		}
		String oldValue = getDescriptorProvider().load().toString();

		String sizeValue = builder.getFontSizeValue();

		if (oldValue != null && oldValue.equals(sizeValue)) {
			return;
		}

		try {
			save(sizeValue);
		} catch (SemanticException e) {
			WidgetUtil.processError(builder.getShell(), e);
			builder.setFontSizeValue(oldValue);
			return;
		}
		if (sizeValue == null) {
			String fontSize = getDescriptorProvider().load().toString();
			builder.setFontSizeValue(fontSize);
		}
	}

	@Override
	public void save(Object value) throws SemanticException {
		descriptorProvider.save(value);
	}

	public String getFontSizeValue() {
		return builder.getFontSizeValue();
	}

	public void setFontSizeValue(String value) {
		builder.setFontSizeValue(value);
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(builder, isHidden);
	}

	public void setVisible(boolean isVisible) {
		builder.setVisible(isVisible);
	}

	private String oldValue;

	@Override
	public void load() {
		oldValue = getDescriptorProvider().load().toString();
		refresh(oldValue);
	}

	@Override
	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}
}
