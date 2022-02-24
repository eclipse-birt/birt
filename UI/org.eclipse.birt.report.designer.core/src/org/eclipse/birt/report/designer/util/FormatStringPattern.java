/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.util;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.ULocale;

public class FormatStringPattern {

	/**
	 * Retrieves format pattern from arrays given format type categorys.
	 * 
	 * @param category Given format type category.
	 * @return The corresponding format pattern string.
	 */

	private static String getPatternForCategory(String category) {
		String pattern;

		if (DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE.equals(category)) {
			pattern = ">"; //$NON-NLS-1$
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE.equals(category)) {
			pattern = "<"; //$NON-NLS-1$
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE.equals(category)) {
			pattern = Messages.getString("FormatStringPage.simpleTexZipCodeFormat"); //$NON-NLS-1$
			// pattern = "@@@@@"; //$NON-NLS-1$
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4.equals(category)) {
			pattern = Messages.getString("FormatStringPage.simpleTexZipCode4Format"); //$NON-NLS-1$
			// pattern = "@@@@@-@@@@"; //$NON-NLS-1$
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER.equals(category)) {
			pattern = Messages.getString("FormatStringPage.phoneNumberFormat"); //$NON-NLS-1$
			// pattern = "(@@@)@@@-@@@@"; //$NON-NLS-1$
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER.equals(category)) {
			pattern = Messages.getString("FormatStringPage.securityNumberFormat"); //$NON-NLS-1$
			// pattern = "@@@-@@-@@@@"; //$NON-NLS-1$
		} else if (category.equals("^")) //$NON-NLS-1$
		{
			pattern = category;
		} else {
			pattern = ""; //$NON-NLS-1$
		}
		return pattern;
	}

	public static String getPatternForCategory(String category, ULocale uLocale) {
		if (uLocale == null) {
			return getPatternForCategory(category);
		}

		String pattern;

		if (DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE.equals(category)) {
			pattern = ">"; //$NON-NLS-1$
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE.equals(category)) {
			pattern = "<"; //$NON-NLS-1$
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE.equals(category)) {
			pattern = Messages.getString("FormatStringPage.simpleTexZipCodeFormat", uLocale.toLocale()); //$NON-NLS-1$
			// pattern = "@@@@@"; //$NON-NLS-1$
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4.equals(category)) {
			pattern = Messages.getString("FormatStringPage.simpleTexZipCode4Format", uLocale.toLocale()); //$NON-NLS-1$
			// pattern = "@@@@@-@@@@"; //$NON-NLS-1$
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER.equals(category)) {
			pattern = Messages.getString("FormatStringPage.phoneNumberFormat", uLocale.toLocale()); //$NON-NLS-1$
			// pattern = "(@@@)@@@-@@@@"; //$NON-NLS-1$
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER.equals(category)) {
			pattern = Messages.getString("FormatStringPage.securityNumberFormat", uLocale.toLocale()); //$NON-NLS-1$
			// pattern = "@@@-@@-@@@@"; //$NON-NLS-1$
		} else if (category.equals("^")) //$NON-NLS-1$
		{
			pattern = category;
		} else {
			pattern = ""; //$NON-NLS-1$
		}
		return pattern;
	}
}
