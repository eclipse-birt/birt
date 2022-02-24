/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.StructureContext;

import com.ibm.icu.util.ULocale;

/**
 * Represents a format value in the style or the highlight rule.
 * 
 */

public class FormatValueHandle extends StructureHandle {

	/**
	 * Construct an handle to deal with the action structure.
	 * 
	 * @param element the element that defined the action.
	 * @param context context to the format value property.
	 */

	public FormatValueHandle(DesignElementHandle element, StructureContext context) {
		super(element, context);
	}

	/**
	 * Construct an handle to deal with the action structure.
	 * 
	 * @param element the element that defined the action.
	 * @param context context to the format value property
	 * @deprecated
	 */

	public FormatValueHandle(DesignElementHandle element, MemberRef context) {
		super(element, context);
	}

	/**
	 * Returns the category of the format.
	 * 
	 * @return the category of the format
	 */

	public String getCategory() {
		return getStringProperty(FormatValue.CATEGORY_MEMBER);
	}

	/**
	 * Sets the category of the format.
	 * 
	 * @param pattern the category of the format
	 * @throws SemanticException if <code>pattern</code> is not one of the BIRT
	 *                           defined.
	 * 
	 */

	public void setCategory(String pattern) throws SemanticException {
		setProperty(FormatValue.CATEGORY_MEMBER, pattern);
	}

	/**
	 * Returns the pattern of the format.
	 * 
	 * @return the pattern of the format
	 */

	public String getPattern() {
		return getStringProperty(FormatValue.PATTERN_MEMBER);
	}

	/**
	 * Sets the pattern of the format.
	 * 
	 * @param value the pattern of the format
	 */

	public void setPattern(String value) {
		setPropertySilently(FormatValue.PATTERN_MEMBER, value);
	}

	/**
	 * Sets the locale of the format.
	 * 
	 * @param locale the locale of the format.
	 */
	public void setLocale(ULocale locale) throws SemanticException {
		setProperty(FormatValue.LOCALE_MEMBER, locale);
	}

	/**
	 * Gets the locale of the format.
	 * 
	 * @return the locale of the format.
	 */
	public ULocale getLocale() {
		return (ULocale) getProperty(FormatValue.LOCALE_MEMBER);
	}
}
