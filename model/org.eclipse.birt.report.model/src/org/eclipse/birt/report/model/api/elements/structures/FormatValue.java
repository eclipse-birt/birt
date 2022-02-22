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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.FormatValueHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Structure;

import com.ibm.icu.util.ULocale;

/**
 * The abstract value for the format of string, data-time and number.
 *
 */

public class FormatValue extends Structure {

	/**
	 * Name of the format value structure.
	 */
	public static final String FORMAT_VALUE_STRUCT = "FormatValue"; //$NON-NLS-1$
	/**
	 * Name of the config variable category member.
	 */

	public static final String CATEGORY_MEMBER = "category"; //$NON-NLS-1$

	/**
	 * Name of the config variable pattern member.
	 */

	public static final String PATTERN_MEMBER = "pattern"; //$NON-NLS-1$

	/**
	 * Name of the config variable locale member.
	 */
	public static final String LOCALE_MEMBER = "locale"; //$NON-NLS-1$

	/**
	 * The config variable category.
	 */

	private String category = null;

	/**
	 * The config variable pattern.
	 */

	private String pattern = null;

	/**
	 * The config variable ulocale.
	 */
	private ULocale locale = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	@Override
	protected Object getIntrinsicProperty(String memberName) {
		if (CATEGORY_MEMBER.equals(memberName)) {
			return category;
		}
		if (PATTERN_MEMBER.equals(memberName)) {
			return pattern;
		}
		if (LOCALE_MEMBER.equals(memberName)) {
			return locale;
		}

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	@Override
	protected void setIntrinsicProperty(String memberName, Object value) {
		if (CATEGORY_MEMBER.equals(memberName)) {
			category = (String) value;
		} else if (PATTERN_MEMBER.equals(memberName)) {
			this.pattern = (String) value;
		} else if (LOCALE_MEMBER.equals(memberName)) {
			this.locale = (ULocale) value;
		} else {
			assert false;
		}
	}

	/**
	 * Returns the variable name.
	 *
	 * @return the variable name
	 */

	public String getCategory() {
		return (String) getProperty(null, CATEGORY_MEMBER);
	}

	/**
	 * Sets the variable name.
	 *
	 * @param name the name to set
	 */

	public void setCategory(String name) {
		setProperty(CATEGORY_MEMBER, name);
	}

	/**
	 * Returns the variable value.
	 *
	 * @return the variable value
	 */

	public String getPattern() {
		return (String) getProperty(null, PATTERN_MEMBER);
	}

	/**
	 * Sets the variable value.
	 *
	 * @param value the value to set
	 */

	public void setPattern(String value) {
		setProperty(PATTERN_MEMBER, value);
	}

	/**
	 * Gets the ULocale.
	 *
	 * @return the ULocale.
	 */
	public ULocale getLocale() {
		return (ULocale) getProperty(null, LOCALE_MEMBER);
	}

	/**
	 * Sets the ULocale.
	 *
	 * @param value the value of the ULocale.
	 */
	public void setLocale(ULocale value) {
		setProperty(LOCALE_MEMBER, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		if (!StringUtil.isEmpty(pattern)) {
			return pattern;
		}
		if (!StringUtil.isEmpty(category)) {
			return category;
		}
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.core.IStructure#getStructName()
	 */
	@Override
	public String getStructName() {
		return FORMAT_VALUE_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	@Override
	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#getHandle(org.eclipse.birt
	 * .report.model.api.SimpleValueHandle)
	 */
	@Override
	public StructureHandle getHandle(SimpleValueHandle valueHandle) {
		return new FormatValueHandle(valueHandle.getElementHandle(), getContext());
	}

}
