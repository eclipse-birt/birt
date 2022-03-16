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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.FormatStringPattern;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

import com.ibm.icu.util.ULocale;

/**
 * FormatStringAdapter
 */
public class FormatStringAdapter extends FormatAdapter {

	public static final String PRESERVE_WHITE_SPACES = Messages.getString("FormatStringPage.Label.PreserveWhiteSpaces"); //$NON-NLS-1$

	public static final String STRING_FORMAT_TYPE_PRESERVE_SPACE = "^"; //$NON-NLS-1$

	private static String[][] choiceArray = null;

	private static String[] formatTypes = null;

	static {
		IChoiceSet set = ChoiceSetFactory.getStructChoiceSet(StringFormatValue.FORMAT_VALUE_STRUCT,
				StringFormatValue.CATEGORY_MEMBER);
		IChoice[] choices = set.getChoices();
		if (choices.length > 0) {
			choiceArray = new String[4][2];
			for (int i = 0, j = 0; i < choices.length; i++) {
				if (choices[i].getName().equals(DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED)
						|| choices[i].getName().equals(DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE)
						|| choices[i].getName().equals(DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE)
						|| choices[i].getName().equals(DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM)) {
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
	public String getCategory4DisplayName(String displayName) {
		if (choiceArray != null) {
			for (int i = 0; i < choiceArray.length; i++) {
				if (choiceArray[i][0].equals(displayName)) {
					return choiceArray[i][1];
				}
			}
		}

		if (displayName.equals(PRESERVE_WHITE_SPACES)) {
			return STRING_FORMAT_TYPE_PRESERVE_SPACE;
		}

		return displayName;
	}

	@Override
	public String getDisplayName4Category(String category) {
		if (category.equals(STRING_FORMAT_TYPE_PRESERVE_SPACE)) {
			return PRESERVE_WHITE_SPACES;
		}

		return ChoiceSetFactory.getStructDisplayName(StringFormatValue.FORMAT_VALUE_STRUCT,
				StringFormatValue.CATEGORY_MEMBER, category);
	}

	@Override
	public String[] getFormatTypes(ULocale locale) {
		return formatTypes;
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
	public String[][] initChoiceArray() {
		return choiceArray;
	}

	@Override
	public String getPattern4DisplayName(String displayName, ULocale locale) {
		String category = null;

		if (displayName.equals(PRESERVE_WHITE_SPACES)) {
			category = STRING_FORMAT_TYPE_PRESERVE_SPACE;
		} else {
			category = ChoiceSetFactory.getStructPropValue(StringFormatValue.FORMAT_VALUE_STRUCT,
					StringFormatValue.CATEGORY_MEMBER, displayName);
		}

		return FormatStringPattern.getPatternForCategory(category, locale);
	}
}
