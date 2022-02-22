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
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

public class BorderWidthDescriptorProvider extends StyleComboProvider {

	private static final String LABEL_WIDTH = Messages.getString("BordersPage.Label.Width"); //$NON-NLS-1$
	private String[] nameChoices;
	private String[] displayChoices;

	public BorderWidthDescriptorProvider() {
		super();
	}

	@Override
	public String getDisplayName() {
		return LABEL_WIDTH;
	}

	@Override
	public Object load() {
		String value = getLocalStringValue(StyleHandle.BORDER_LEFT_WIDTH_PROP);
		value = convertNameToDisplayName(value);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(StyleHandle.BORDER_RIGHT_WIDTH_PROP);
		value = convertNameToDisplayName(value);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(StyleHandle.BORDER_TOP_WIDTH_PROP);
		value = convertNameToDisplayName(value);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue(StyleHandle.BORDER_BOTTOM_WIDTH_PROP);
		value = convertNameToDisplayName(value);
		if (!"".equals(value)) //$NON-NLS-1$
		{
			this.indexText = value;
			return value;
		}
		return indexText;
	}

	@Override
	public void save(Object value) throws SemanticException {
		this.indexText = value == null ? "" : value; //$NON-NLS-1$
		String saveValue = convertDisplayNameToName(value);
		if (((Boolean) styleMap.get(StyleHandle.BORDER_TOP_STYLE_PROP)).booleanValue()) {
			save(StyleHandle.BORDER_TOP_WIDTH_PROP, saveValue);
		} else {
			save(StyleHandle.BORDER_TOP_WIDTH_PROP, null);
		}

		if (((Boolean) styleMap.get(StyleHandle.BORDER_BOTTOM_STYLE_PROP)).booleanValue()) {
			save(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, saveValue);
		} else {
			save(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, null);
		}

		if (((Boolean) styleMap.get(StyleHandle.BORDER_LEFT_STYLE_PROP)).booleanValue()) {
			save(StyleHandle.BORDER_LEFT_WIDTH_PROP, saveValue);
		} else {
			save(StyleHandle.BORDER_LEFT_WIDTH_PROP, null);
		}

		if (((Boolean) styleMap.get(StyleHandle.BORDER_RIGHT_STYLE_PROP)).booleanValue()) {
			save(StyleHandle.BORDER_RIGHT_WIDTH_PROP, saveValue);
		} else {
			save(StyleHandle.BORDER_RIGHT_WIDTH_PROP, null);
		}
	}

	private String convertNameToDisplayName(String name) {
		if (nameChoices == null) {
			getItems();
		}
		int index = Arrays.asList(nameChoices).indexOf(name);
		if (index >= 0 && index < displayChoices.length) {
			return displayChoices[index];
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	private String convertDisplayNameToName(Object displayName) {
		if (displayName == null) {
			return null;
		}
		int index = Arrays.asList(getItems()).indexOf(displayName);
		if (index >= 0 && index < nameChoices.length) {
			return nameChoices[index];
		} else {
			return null;
		}
	}

	private String[] getWidths(IChoiceSet choiceSet) {

		IChoice[] choices = choiceSet.getChoices();

		nameChoices = new String[choices.length + 10];
		displayChoices = new String[choices.length + 10];

		for (int i = 0; i < choices.length; i++) {
			nameChoices[i] = choices[i].getName();
			displayChoices[i] = choices[i].getDisplayName();
		}

		for (int i = choices.length; i < choices.length + 10; i++) {
			nameChoices[i] = (i + 1 - choices.length)
					+ DEUtil.getMetaDataDictionary().getChoiceSet(DesignChoiceConstants.CHOICE_UNITS)
							.findChoice(DesignChoiceConstants.UNITS_PX).getName();
			displayChoices[i] = (i + 1 - choices.length)
					+ DEUtil.getMetaDataDictionary().getChoiceSet(DesignChoiceConstants.CHOICE_UNITS)
							.findChoice(DesignChoiceConstants.UNITS_PX).getName();
		}

		return nameChoices;
	}

	@Override
	public Object[] getItems() {
		if (nameChoices == null) {
			return getWidths(ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
					StyleHandle.BORDER_TOP_WIDTH_PROP));
		} else {
			return nameChoices;
		}
	}

	@Override
	public Object[] getDisplayItems() {
		if (displayChoices == null) {
			getWidths(ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
					StyleHandle.BORDER_TOP_WIDTH_PROP));
		}
		return displayChoices;
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
