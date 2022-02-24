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

import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

public class ComboPropertyDescriptorProvider extends PropertyDescriptorProvider implements IComboDescriptorProvider {

	public ComboPropertyDescriptorProvider(String property, String element) {
		super(property, element);
	}

	private IChoiceSet choiceSet;
	private String[] items;

	public String[] getItems() {
		if (choiceSet == null) {
			choiceSet = ChoiceSetFactory.getElementChoiceSet(getElement(), getProperty());
			String[] displayNames = ChoiceSetFactory.getDisplayNamefromChoiceSet(choiceSet,
					new AlphabeticallyComparator());
			if (getProperty().equals(StyleHandle.FONT_FAMILY_PROP)) {
				String[] sysFont = DEUtil.getSystemFontNames();
				String[] items = new String[displayNames.length + sysFont.length + 1];
				items[0] = ChoiceSetFactory.CHOICE_AUTO;
				System.arraycopy(displayNames, 0, items, 1, displayNames.length);
				System.arraycopy(sysFont, 0, items, displayNames.length + 1, sysFont.length);
				for (int i = 0; i < items.length; i++) {
					DEUtil.removeQuote(items[i]);
				}
				this.items = items;
			} else if (StyleHandle.TEXT_ALIGN_PROP.equals(getProperty())
					|| StyleHandle.VERTICAL_ALIGN_PROP.equals(getProperty())
					|| StyleHandle.WHITE_SPACE_PROP.equals(getProperty())) {
				String[] items = new String[displayNames.length + 1];
				items[0] = ChoiceSetFactory.CHOICE_AUTO;

				System.arraycopy(displayNames, 0, items, 1, displayNames.length);

				this.items = items;
			} else {
				this.items = displayNames;
			}
		}
		return this.items;
	}

	public Object load() {
		if (StyleHandle.FONT_FAMILY_PROP.equals(getProperty())) {
			return DEUtil.removeQuote(super.load().toString());
		} else if (StyleHandle.TEXT_ALIGN_PROP.equals(getProperty())
				|| StyleHandle.VERTICAL_ALIGN_PROP.equals(getProperty())
				|| StyleHandle.WHITE_SPACE_PROP.equals(getProperty())) {
			Object rt = super.load();

			if (rt == null || (rt instanceof String && ((String) rt).length() == 0) || !hasLocalValue()) {
				return ChoiceSetFactory.CHOICE_AUTO;
			}

			return rt;
		} else {
			return super.load();
		}
	}

	public String getDisplayName(String key) {
		IChoice choice = null;
		if (choiceSet != null)
			choice = choiceSet.findChoice(key);

		if (choice == null) {
			return null;
		} else
			return choice.getDisplayName();
	}

	public void save(Object value) throws SemanticException {
		if ("".equals(value))//$NON-NLS-1$
		{
			value = null;
		}
		String pName = getProperty();
		if (ChoiceSetFactory.CHOICE_AUTO.equals(value) && (StyleHandle.FONT_FAMILY_PROP.equals(pName)
				|| StyleHandle.TEXT_ALIGN_PROP.equals(pName) || StyleHandle.VERTICAL_ALIGN_PROP.equals(pName)
				|| StyleHandle.WHITE_SPACE_PROP.equals(pName))) {
			value = null;
		}
		if (ChoiceSetFactory.CHOICE_NONE.equals(value) && ReportDesignHandle.INCLUDE_RESOURCE_PROP.equals(pName)) {
			value = null;
		}

		if (value != null) {
			value = getSaveValue(value);
			if (value == null) {
				return;
			}
		}

		if (StyleHandle.FONT_FAMILY_PROP.equals(pName) && needAddQuote(value == null ? null : value.toString())) {

			super.save(DEUtil.addQuote(value == null ? null : value.toString()));
		} else {
			super.save(value);
		}

	}

	protected Object getSaveValue(Object value) {
		if (items != null && Arrays.asList(items).contains(value)) {
			choiceSet = ChoiceSetFactory.getElementChoiceSet(getElement(), getProperty());
			for (int i = 0; i < choiceSet.getChoices().length; i++) {
				if (choiceSet.getChoices()[i].getDisplayName().equals(value)) {
					value = choiceSet.getChoices()[i].getName();
					Object oldValue = super.load();
					if (!oldValue.equals("") && oldValue.equals(value)) {
						return null;
					}
					return value;
				}
			}
		}
		return value;
	}

	private boolean needAddQuote(String value) {
		IChoiceSet choice = ChoiceSetFactory.getElementChoiceSet(getElement(), getProperty());
		String[] displayNames = ChoiceSetFactory.getDisplayNamefromChoiceSet(choice, new AlphabeticallyComparator());
		for (int i = 0; i < displayNames.length; i++) {
			if (displayNames[i].equals(value)) {
				return false;
			}
		}
		return true;
	}
}
