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
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * Border color descriptor provider
 *
 * @since 3.3
 *
 */
public class BorderColorDescriptorProvider extends BorderDescriptorProvider {

	/**
	 * Constructor
	 */
	public BorderColorDescriptorProvider() {
		super();
	}

	/**
	 * Get element choice set
	 *
	 * @return Return the element choice set
	 */
	public IChoiceSet getElementChoiceSet() {
		return ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
				IStyleModel.BORDER_TOP_COLOR_PROP);
	}

	private static final String LABEL_COLOR = Messages.getString("BordersPage.Label.Color"); //$NON-NLS-1$

	@Override
	public String getDisplayName() {
		return LABEL_COLOR;
	}

	@Override
	public Object load() {
		String value = getLocalStringValue(IStyleModel.BORDER_LEFT_COLOR_PROP);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(IStyleModel.BORDER_RIGHT_COLOR_PROP);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(IStyleModel.BORDER_TOP_COLOR_PROP);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(IStyleModel.BORDER_BOTTOM_COLOR_PROP);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(IStyleModel.BORDER_DIAGONAL_COLOR_PROP);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(IStyleModel.BORDER_ANTIDIAGONAL_COLOR_PROP);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}
		return indexText;
	}

	protected Object indexText = ""; //$NON-NLS-1$

	/**
	 * Set index
	 *
	 * @param index index object
	 */
	public void setIndex(Object index) {
		indexText = index;
	}

	@Override
	public void save(Object value) throws SemanticException {
		this.indexText = value == null ? "" : value; //$NON-NLS-1$
		if ((styleMap.get(IStyleModel.BORDER_TOP_STYLE_PROP)).booleanValue()) {
			save(IStyleModel.BORDER_TOP_COLOR_PROP, value);
		} else {
			save(IStyleModel.BORDER_TOP_COLOR_PROP, null);
		}

		if ((styleMap.get(IStyleModel.BORDER_BOTTOM_STYLE_PROP)).booleanValue()) {
			save(IStyleModel.BORDER_BOTTOM_COLOR_PROP, value);
		} else {
			save(IStyleModel.BORDER_BOTTOM_COLOR_PROP, null);
		}

		if ((styleMap.get(IStyleModel.BORDER_LEFT_STYLE_PROP)).booleanValue()) {
			save(IStyleModel.BORDER_LEFT_COLOR_PROP, value);
		} else {
			save(IStyleModel.BORDER_LEFT_COLOR_PROP, null);
		}

		if ((styleMap.get(IStyleModel.BORDER_RIGHT_STYLE_PROP)).booleanValue()) {
			save(IStyleModel.BORDER_RIGHT_COLOR_PROP, value);
		} else {
			save(IStyleModel.BORDER_RIGHT_COLOR_PROP, null);
		}

		if ((styleMap.get(IStyleModel.BORDER_DIAGONAL_STYLE_PROP)).booleanValue()) {
			save(IStyleModel.BORDER_DIAGONAL_COLOR_PROP, value);
		} else {
			save(IStyleModel.BORDER_DIAGONAL_COLOR_PROP, null);
		}

		if (styleMap.get(IStyleModel.BORDER_ANTIDIAGONAL_STYLE_PROP).booleanValue()) {
			save(IStyleModel.BORDER_ANTIDIAGONAL_COLOR_PROP, value);
		} else {
			save(IStyleModel.BORDER_ANTIDIAGONAL_COLOR_PROP, null);
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
