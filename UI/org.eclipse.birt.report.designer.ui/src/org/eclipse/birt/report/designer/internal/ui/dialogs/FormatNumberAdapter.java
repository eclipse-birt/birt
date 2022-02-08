/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.FormatNumberPattern;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

import com.ibm.icu.util.ULocale;

/**
 * FormatNumberAdapter
 */
public class FormatNumberAdapter extends FormatAdapter {

	private static String[][] choiceArray = null;

	private static String[] formatTypes = null;

	static {
		IChoiceSet set = ChoiceSetFactory.getStructChoiceSet(NumberFormatValue.FORMAT_VALUE_STRUCT,
				NumberFormatValue.CATEGORY_MEMBER);
		IChoice[] choices = set.getChoices();
		if (choices.length > 0) {
			// excludes "standard".
			choiceArray = new String[choices.length - 1][2];
			for (int i = 0, j = 0; i < choices.length; i++) {
				if (!choices[i].getName().equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_STANDARD)) {
					choiceArray[j][0] = choices[i].getDisplayName();
					choiceArray[j][1] = choices[i].getName();
					j++;
				}
			}
		} else {
			choiceArray = new String[0][0];
		}

		if (choiceArray != null) {
			formatTypes = new String[choiceArray.length];
			for (int i = 0; i < choiceArray.length; i++) {
				formatTypes[i] = choiceArray[i][0];
			}
		} else {
			formatTypes = new String[0];
		}
	}

	@Override
	public String[] getFormatTypes(ULocale locale) {
		return formatTypes;
	}

	@Override
	public String[][] initChoiceArray() {
		return choiceArray;
	}

	@Override
	public int getIndexOfCategory(String name) {
		if (choiceArray != null) {
			for (int i = 0; i < choiceArray.length; i++) {
				if (choiceArray[i][1].equals(name)) {
					return i;
				}
			}
		}
		return 0;
	}

	@Override
	public String getCategory4DisplayName(String displayName) {
		if (choiceArray != null) {
			for (int i = 0; i < choiceArray.length; i++) {
				if (choiceArray[i][0].equals(displayName)) {
					return choiceArray[i][1];
				}
			}
		}
		return displayName;
	}

	@Override
	public String getDisplayName4Category(String category) {
		return ChoiceSetFactory.getStructDisplayName(NumberFormatValue.FORMAT_VALUE_STRUCT,
				NumberFormatValue.CATEGORY_MEMBER, category);

	}

	@Override
	public String getPattern4DisplayName(String displayName, ULocale locale) {
		String category = ChoiceSetFactory.getStructPropValue(NumberFormatValue.FORMAT_VALUE_STRUCT,
				NumberFormatValue.CATEGORY_MEMBER, displayName);

		return FormatNumberPattern.getPatternForCategory(category, locale);
	}
}
