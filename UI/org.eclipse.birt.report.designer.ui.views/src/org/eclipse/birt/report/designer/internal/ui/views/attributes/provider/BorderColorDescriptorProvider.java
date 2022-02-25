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
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

public class BorderColorDescriptorProvider extends BorderDescriptorProvider {

	public BorderColorDescriptorProvider() {
		super();
	}

	public IChoiceSet getElementChoiceSet() {
		return ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
				StyleHandle.BORDER_TOP_COLOR_PROP);
	}

	private static final String LABEL_COLOR = Messages.getString("BordersPage.Label.Color"); //$NON-NLS-1$

	@Override
	public String getDisplayName() {
		return LABEL_COLOR;
	}

	@Override
	public Object load() {
		String value = getLocalStringValue(StyleHandle.BORDER_LEFT_COLOR_PROP);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(StyleHandle.BORDER_RIGHT_COLOR_PROP);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(StyleHandle.BORDER_TOP_COLOR_PROP);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(StyleHandle.BORDER_BOTTOM_COLOR_PROP);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}
		return indexText;
	}

	protected Object indexText = ""; //$NON-NLS-1$

	public void setIndex(Object index) {
		indexText = index;
	}

	@Override
	public void save(Object value) throws SemanticException {
		this.indexText = value == null ? "" : value; //$NON-NLS-1$
		if (((Boolean) styleMap.get(StyleHandle.BORDER_TOP_STYLE_PROP)).booleanValue()) {
			save(StyleHandle.BORDER_TOP_COLOR_PROP, value);
		} else {
			save(StyleHandle.BORDER_TOP_COLOR_PROP, null);
		}

		if (((Boolean) styleMap.get(StyleHandle.BORDER_BOTTOM_STYLE_PROP)).booleanValue()) {
			save(StyleHandle.BORDER_BOTTOM_COLOR_PROP, value);
		} else {
			save(StyleHandle.BORDER_BOTTOM_COLOR_PROP, null);
		}

		if (((Boolean) styleMap.get(StyleHandle.BORDER_LEFT_STYLE_PROP)).booleanValue()) {
			save(StyleHandle.BORDER_LEFT_COLOR_PROP, value);
		} else {
			save(StyleHandle.BORDER_LEFT_COLOR_PROP, null);
		}

		if (((Boolean) styleMap.get(StyleHandle.BORDER_RIGHT_STYLE_PROP)).booleanValue()) {
			save(StyleHandle.BORDER_RIGHT_COLOR_PROP, value);
		} else {
			save(StyleHandle.BORDER_RIGHT_COLOR_PROP, null);
		}
	}

	@Override
	public void handleModifyEvent() {
		try {
			if (indexText != null) {
				save(indexText);
			}
		} catch (Exception e) {
		}
	}
}
