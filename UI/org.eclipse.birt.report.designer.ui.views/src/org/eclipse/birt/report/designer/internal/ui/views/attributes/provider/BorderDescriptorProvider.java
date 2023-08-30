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

import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * Border description provider
 *
 * @since 3.3
 *
 */
public abstract class BorderDescriptorProvider extends AbstractDescriptorProvider {

	protected Object input;

	protected HashMap<String, Boolean> styleMap = new HashMap<String, Boolean>();

	/**
	 * Constructor
	 */
	public BorderDescriptorProvider() {
		styleMap.put(IStyleModel.BORDER_LEFT_STYLE_PROP, Boolean.FALSE);
		styleMap.put(IStyleModel.BORDER_RIGHT_STYLE_PROP, Boolean.FALSE);
		styleMap.put(IStyleModel.BORDER_TOP_STYLE_PROP, Boolean.FALSE);
		styleMap.put(IStyleModel.BORDER_BOTTOM_STYLE_PROP, Boolean.FALSE);
		styleMap.put(IStyleModel.BORDER_DIAGONAL_STYLE_PROP, Boolean.FALSE);
		styleMap.put(IStyleModel.BORDER_ANTIDIAGONAL_STYLE_PROP, Boolean.FALSE);
	}

	/**
	 * Set style property
	 *
	 * @param style style property name
	 * @param value property value
	 */
	public void setStyleProperty(String style, Boolean value) {
		styleMap.put(style, value);
	}

	@Override
	public void setInput(Object input) {
		this.input = input;
	}

	protected String getLocalStringValue(String property) {
		GroupElementHandle handle = null;
		if (input instanceof List) {
			handle = DEUtil.getGroupElementHandle((List<?>) input);
		}
		if (handle == null) {
			return ""; //$NON-NLS-1$
		}
		String value = handle.getLocalStringProperty(property);
		if (value == null)
		// && multiSelectionHandle.shareSameValue( property ) )
		{
			value = ""; //$NON-NLS-1$
		}
		return value;
	}

	protected String getStringValue(String property) {
		GroupElementHandle handle = null;
		if (input instanceof List) {
			handle = DEUtil.getGroupElementHandle((List<?>) input);
		}
		if (handle == null) {
			return ""; //$NON-NLS-1$
		}
		String value = handle.getStringProperty(property);
		if (value == null)
		// && multiSelectionHandle.shareSameValue( property ) )
		{
			value = ""; //$NON-NLS-1$
		}
		return value;
	}

	protected String getDisplayValue(String property) {
		GroupElementHandle handle = null;
		if (input instanceof List) {
			handle = DEUtil.getGroupElementHandle((List<?>) input);
		}
		if (handle == null) {
			return ""; //$NON-NLS-1$
		}
		if (getLocalStringValue(property).equals("")) {
			String value = "";
			if (handle.getPropertyHandle(property) != null) {
				value = handle.getPropertyHandle(property).getStringValue();
				if (value == null) {
					value = ""; //$NON-NLS-1$
				}
			}
			return value;
		}
		return ""; //$NON-NLS-1$
	}

	protected String getDefaultStringValue(String property) {
		GroupElementHandle handle = null;
		if (input instanceof List) {
			handle = DEUtil.getGroupElementHandle((List<?>) input);
		}
		if (handle == null) {
			return ""; //$NON-NLS-1$
		}
		if (getLocalStringValue(property).equals("")) {
			String value = handle.getStringProperty(property);
			if (value == null)
			// && multiSelectionHandle.shareSameValue( property ) )
			{
				value = ""; //$NON-NLS-1$
			}
			return value;
		}
		return ""; //$NON-NLS-1$
	}

	protected void save(String property, Object value) throws SemanticException {
		GroupElementHandle groupElementHandle = null;

		if (input instanceof GroupElementHandle) {
			groupElementHandle = (GroupElementHandle) input;
		} else if (input instanceof List) {
			groupElementHandle = DEUtil.getGroupElementHandle((List<?>) input);
		}

		if (groupElementHandle != null) {
			GroupPropertyHandle handle = groupElementHandle.getPropertyHandle(property);
			if (handle != null && handle.getLocalStringValue() != null) {
				if (handle.getLocalStringValue().equals(value)) {
					return;
				}
			}
			groupElementHandle.setProperty(property, value);
		}
	}

	abstract void handleModifyEvent();
}
