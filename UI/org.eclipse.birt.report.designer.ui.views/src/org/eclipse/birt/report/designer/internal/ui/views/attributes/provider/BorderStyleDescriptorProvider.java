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

import java.util.Arrays;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * Border style description provider to describe the border of the element
 *
 * @since 3.3
 *
 */
public class BorderStyleDescriptorProvider extends StyleComboProvider {

	private String[] nameChoices;
	private String[] displayChoices;

	/**
	 * Constructor
	 */
	public BorderStyleDescriptorProvider() {
		super();
	}

	private static final String LABEL_STYLE = Messages.getString("BordersPage.Label.Style"); //$NON-NLS-1$

	@Override
	public String getDisplayName() {
		return LABEL_STYLE;
	}

	@Override
	public Object load() {
		String value = getLocalStringValue(IStyleModel.BORDER_LEFT_STYLE_PROP);
		value = convertNameToDisplayName(value);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(IStyleModel.BORDER_RIGHT_STYLE_PROP);
		value = convertNameToDisplayName(value);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(IStyleModel.BORDER_TOP_STYLE_PROP);
		value = convertNameToDisplayName(value);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(IStyleModel.BORDER_BOTTOM_STYLE_PROP);
		value = convertNameToDisplayName(value);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}
		return indexText;
	}

	private String convertNameToDisplayName(String name) {
		if (nameChoices == null) {
			getItems();
		}
		int index = Arrays.asList(nameChoices).indexOf(name);
		if (index >= 0 && index < displayChoices.length) {
			return displayChoices[index];
		}
		return ""; //$NON-NLS-1$
	}

	private String convertDisplayNameToName(Object displayName) {
		if (displayName == null) {
			return null;
		}
		int index = Arrays.asList(getItems()).indexOf(displayName);
		if (index >= 0 && index < nameChoices.length) {
			return nameChoices[index];
		}
		return null;
	}

	private String[] getStyles(IChoiceSet choiceSet, Object[] items) {
		nameChoices = new String[items.length];
		displayChoices = new String[items.length];

		for (int i = 0; i < items.length; i++) {
			nameChoices[i] = choiceSet.findChoice((String) items[i]).getName();
			displayChoices[i] = choiceSet.findChoice((String) items[i]).getDisplayName();
		}

		return nameChoices;
	}

	@Override
	public Object[] getItems() {
		if (nameChoices == null) {
			return getStyles(DEUtil.getMetaDataDictionary().getChoiceSet(DesignChoiceConstants.CHOICE_LINE_STYLE),
					super.getItems());
		}
		return nameChoices;
	}

	@Override
	public Object[] getDisplayItems() {
		if (displayChoices == null) {
			getStyles(DEUtil.getMetaDataDictionary().getChoiceSet(DesignChoiceConstants.CHOICE_LINE_STYLE),
					super.getItems());
		}
		return displayChoices;
	}

	@Override
	public void save(Object value) throws SemanticException {
		this.indexText = value == null ? "" : value; //$NON-NLS-1$
		String saveValue = convertDisplayNameToName(value);
		if (styleMap.get(IStyleModel.BORDER_TOP_STYLE_PROP).booleanValue()) {
			save(IStyleModel.BORDER_TOP_STYLE_PROP, saveValue);
		} else {
			save(IStyleModel.BORDER_TOP_STYLE_PROP, null);
		}

		if ((styleMap.get(IStyleModel.BORDER_BOTTOM_STYLE_PROP)).booleanValue()) {
			save(IStyleModel.BORDER_BOTTOM_STYLE_PROP, saveValue);
		} else {
			save(IStyleModel.BORDER_BOTTOM_STYLE_PROP, null);
		}

		if ((styleMap.get(IStyleModel.BORDER_LEFT_STYLE_PROP)).booleanValue()) {
			save(IStyleModel.BORDER_LEFT_STYLE_PROP, saveValue);
		} else {
			save(IStyleModel.BORDER_LEFT_STYLE_PROP, null);
		}

		if ((styleMap.get(IStyleModel.BORDER_RIGHT_STYLE_PROP)).booleanValue()) {
			save(IStyleModel.BORDER_RIGHT_STYLE_PROP, saveValue);
		} else {
			save(IStyleModel.BORDER_RIGHT_STYLE_PROP, null);
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
