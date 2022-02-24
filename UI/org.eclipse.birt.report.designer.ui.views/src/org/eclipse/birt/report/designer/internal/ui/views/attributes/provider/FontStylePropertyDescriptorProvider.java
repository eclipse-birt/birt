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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.AttributeConstant;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.AttributeValueConstant;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class FontStylePropertyDescriptorProvider extends PropertyDescriptorProvider
		implements IToggleDescriptorProvider {

	private String defaltValue = "", toggleValue = ""; //$NON-NLS-1$ //$NON-NLS-2$

	public FontStylePropertyDescriptorProvider(String property, String element) {
		super(property, element);
		if (property.equals(AttributeConstant.FONT_WIDTH)) {
			defaltValue = AttributeValueConstant.FONT_BOLD_NORMAL;
			toggleValue = AttributeValueConstant.FONT_BOLD;
		}
		if (property.equals(AttributeConstant.FONT_STYLE)) {
			defaltValue = AttributeValueConstant.FONT_BOLD_NORMAL;
			toggleValue = AttributeValueConstant.FONT_ITALIC;
		}
		if (property.equals(AttributeConstant.TEXT_UNDERLINE)) {
			defaltValue = AttributeValueConstant.TEXT_UNDERLINE_NORMAL;
			toggleValue = AttributeValueConstant.TEXT_UNDERLINE;
		}
		if (property.equals(AttributeConstant.TEXT_LINE_THROUGH)) {
			defaltValue = AttributeValueConstant.TEXT_LINE_THROUGH_NORMAL;
			toggleValue = AttributeValueConstant.TEXT_LINE_THROUGH;
		}
	}

	public String getTooltipText() {
		if (toggleValue.equals("bold")) //$NON-NLS-1$
			return (Messages.getString("TogglePropertyDescriptor.toolTipText.Bold")); //$NON-NLS-1$
		if (toggleValue.equals("italic")) //$NON-NLS-1$
			return (Messages.getString("TogglePropertyDescriptor.toolTipText.Italic")); //$NON-NLS-1$
		if (toggleValue.equals("underline")) //$NON-NLS-1$
			return (Messages.getString("TogglePropertyDescriptor.toolTipText.Underline")); //$NON-NLS-1$
		if (toggleValue.equals("line-through")) //$NON-NLS-1$
			return (Messages.getString("TogglePropertyDescriptor.toolTipText.Text_Line_Through")); //$NON-NLS-1$
		if (toggleValue.equals("bold")) //$NON-NLS-1$
			return (Messages.getString("TogglePropertyDescriptor.toolTipText.Bold")); //$NON-NLS-1$
		if (toggleValue.equals("italic")) //$NON-NLS-1$
			return (Messages.getString("TogglePropertyDescriptor.toolTipText.Italic")); //$NON-NLS-1$
		if (toggleValue.equals("underline")) //$NON-NLS-1$
			return (Messages.getString("TogglePropertyDescriptor.toolTipText.Underline")); //$NON-NLS-1$
		if (toggleValue.equals("line-through")) //$NON-NLS-1$
			return (Messages.getString("TogglePropertyDescriptor.toolTipText.Text_Line_Through")); //$NON-NLS-1$

		return ""; //$NON-NLS-1$
	}

	public String getImageName() {
		return getProperty();
	}

	public String getToogleValue() {
		return toggleValue;
	}

	public void save(Object value) throws SemanticException {
		if (value instanceof Boolean) {
			value = ((Boolean) value).booleanValue() ? toggleValue : defaltValue;
		}
		super.save(value);
	}

}
