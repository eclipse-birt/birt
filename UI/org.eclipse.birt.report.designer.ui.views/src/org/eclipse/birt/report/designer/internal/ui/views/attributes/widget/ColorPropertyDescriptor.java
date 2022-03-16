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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderColorDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ColorPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.IPropertyDescriptor;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * ColorPropertyDescriptor manages Color choice control.
 */
public class ColorPropertyDescriptor extends PropertyDescriptor implements IPropertyDescriptor {

	protected ColorBuilder builder;

	/**
	 * @param propertyProcessor the property handle
	 */

	public ColorPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	@Override
	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * PropertyDescriptor#resetUIData()
	 */
	@Override
	public void load() {
		String strValue = getDescriptorProvider().load().toString();
		boolean stateFlag = ((strValue == null) == builder.getEnabled());
		if (stateFlag) {
			builder.setEnabled(strValue != null);
		}
		builder.setColorValue(strValue);
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

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#
	 * createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createControl(Composite parent) {
		builder = new ColorBuilder(parent, SWT.NONE, isFormStyle());
		if (getDescriptorProvider() instanceof ColorPropertyDescriptorProvider) {
			builder.setChoiceSet(((ColorPropertyDescriptorProvider) getDescriptorProvider()).getElementChoiceSet());
		} else if (getDescriptorProvider() instanceof BorderColorDescriptorProvider) {
			builder.setChoiceSet(((BorderColorDescriptorProvider) getDescriptorProvider()).getElementChoiceSet());
		}

		builder.addListener(SWT.Modify, new Listener() {

			@Override
			public void handleEvent(Event event) {
				handleColorBuilderModifyEvent();
			}
		});
		if (value != null) {
			builder.setColorValue(value);
		}
		return builder;
	}

	private String value;

	public void setColorValue(String value) {
		if (builder != null) {
			builder.setColorValue(value);
		}
		this.value = value;
	}

	public RGB getColorValue() {
		if (builder != null) {
			return builder.getRGB();
		} else {
			return null;
		}
	}

	/**
	 * Processes the save action.
	 */
	protected void handleColorBuilderModifyEvent() {
		int oldValue = ColorUtil.parseColor(getDescriptorProvider().load().toString());
		ColorUtil.getRGBs(oldValue);

		RGB rgb = builder.getRGB();

		int colorValue = -1;
		if (rgb != null) {
			colorValue = ColorUtil.formRGB(rgb.red, rgb.green, rgb.blue);
		}

		if (oldValue == colorValue) {
			String colorString = getDescriptorProvider().load().toString();
			builder.setColorValue(colorString);
			return;
		}

		String value = builder.getPredefinedColor();
		if (value == null && rgb != null) {
			value = ColorUtil.format(colorValue, ColorUtil.INT_FORMAT);
		}
		try {
			save(value);
		} catch (SemanticException e) {
			WidgetUtil.processError(builder.getShell(), e);
		}
		if (rgb == null) {
			String colorString = getDescriptorProvider().load().toString();
			builder.setColorValue(colorString);
		}
	}

	@Override
	public void save(Object value) throws SemanticException {
		descriptorProvider.save(value);
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(builder, isHidden);
	}

	public void setVisible(boolean isVisible) {
		builder.setVisible(isVisible);
	}
}
