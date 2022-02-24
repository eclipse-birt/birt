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

package org.eclipse.birt.report.model.metadata;

import java.util.regex.Pattern;

import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.StyleUtil;

/**
 * String property type.
 * <p>
 * All string values are valid. However, if the caller provides a type other
 * than a string, the value is converted to a string using default conversion
 * rules.
 * 
 */

public class StringPropertyType extends TextualPropertyType {

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.string"; //$NON-NLS-1$

	private static final String HIDE_RULE_FORMAT_PATTERN = "[$_a-zA-Z][\\.$_a-zA-Z0-9]*"; //$NON-NLS-1$
	private static final Pattern hideRuleFormatPattern = Pattern.compile(HIDE_RULE_FORMAT_PATTERN,
			Pattern.CASE_INSENSITIVE);

	/**
	 * Constructor.
	 */

	public StringPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#validateValue(org
	 * .eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */

	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		if (value == null)
			return null;

		String stringValue = trimString(value.toString(), defn.getTrimOption());

		if (IStyleModel.FONT_FAMILY_PROP.equals(defn.getName())) {
			return StyleUtil.handleFontFamily(defn, stringValue);
		}

		if (HideRule.FORMAT_MEMBER.equals(defn.getName())) {
			IStructureDefn hideRuleStruct = MetaDataDictionary.getInstance().getStructure(HideRule.STRUCTURE_NAME);
			IPropertyDefn formatProperty = null;
			if (hideRuleStruct != null)
				formatProperty = hideRuleStruct.getMember(HideRule.FORMAT_MEMBER);
			if (defn == formatProperty) {
				return validateHideRuleFormat(stringValue);
			}
		}

		return stringValue;
	}

	/**
	 * 
	 * @param value
	 * @return
	 * @throws PropertyValueException
	 */
	private Object validateHideRuleFormat(String value) throws PropertyValueException {
		if (StringUtil.isBlank(value)) {
			return value;
		}

		if (!hideRuleFormatPattern.matcher(value).matches())
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					getTypeCode());
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode() {
		return STRING_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName() {
		return STRING_TYPE_NAME;
	}

	/**
	 * Converts the string property value to a double, this method will always
	 * return 0.
	 */

	public double toDouble(Module module, Object value) {
		// Strings cannot be converted to doubles because the conversion
		// rules are locale-dependent.

		return 0;
	}

	/**
	 * Converts the string property value to an integer.
	 * 
	 * @return integer value of the string representation, return <code>0</code> if
	 *         <code>value</code> is null.
	 */

	public int toInteger(Module module, Object value) {
		if (value == null)
			return 0;

		try {
			return Integer.decode((String) value).intValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}

}
