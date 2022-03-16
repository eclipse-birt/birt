/*************************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation and others.
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

package org.eclipse.birt.report.taglib.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Specifies the report parameter.
 * <p>
 * There are the following parameter attributes:
 * <ol>
 * <li>name</li>
 * <li>pattern</li>
 * <li>value</li>
 * <li>displayText</li>
 * </ol>
 */
public class ParameterField extends ParamValueField implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8597978146893976259L;

	public static final String DEFAULT_DELIMITER = "|"; //$NON-NLS-1$

	private String name;
	private String pattern;
	private String isLocale;
	private String delimiter;

	/**
	 * Values specified through addValue().
	 */
	private List<Object> externalValues;

	/**
	 * Values specified through addValue().
	 */
	private List<String> externalDisplayTexts;

	/**
	 * Values specified through the value attribute.
	 */
	private List<Object> attributeValues;

	/**
	 * Values specified through the displayText attribute.
	 */
	private List<String> attributeDisplayTexts;

	public ParameterField() {
		externalValues = new ArrayList<>();
		externalDisplayTexts = new ArrayList<>();
		attributeValues = null;
		attributeDisplayTexts = null;
		delimiter = DEFAULT_DELIMITER;
	}

	/**
	 * validate parameter
	 *
	 * @return
	 */
	public boolean validate() {
		return name != null && name.length() > 0 ? true : false;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the isLocale
	 */
	public boolean isLocale() {
		// Set isLocale attribute
		if (this.isLocale != null) {
			return Boolean.parseBoolean(isLocale);
		}

		// If set pattern, return true
		if (this.pattern != null) {
			return true;
		}

		return false;
	}

	/**
	 * @param isLocale the isLocale to set
	 */
	public void setLocale(String isLocale) {
		this.isLocale = isLocale;
	}

	/**
	 * Sets the delimiters when specifying multiple values.
	 *
	 * @param delim delimiter
	 */
	public void setDelim(String delim) {
		this.delimiter = (delim == null || delim.length() < 1) ? DEFAULT_DELIMITER : delim;
	}

	/**
	 * @return delimiter
	 */
	public String getDelim() {
		return this.delimiter;
	}

	/**
	 * Adds a value to the value list.
	 *
	 * @param value
	 */
	public void addValue(ParamValueField valueField) {
		// adding external values will override the attribute ones
		attributeValues = null;
		attributeDisplayTexts = null;
		externalValues.add(valueField.getValue());
		externalDisplayTexts.add(valueField.getDisplayText());
	}

	/**
	 * Returns the values. The values added by addValue() have priority over the
	 * ones defined by setValue().
	 *
	 * @return values collection
	 */
	public Collection<Object> getValues() {
		if (!externalValues.isEmpty()) {
			return externalValues;
		} else {
			if (attributeValues == null) {
				attributeValues = extractValues(getValue());
			}
			return attributeValues;
		}
	}

	/**
	 * Returns the display texts. The display texts added by addValue() have
	 * priority over the ones defined by setDisplayText().
	 *
	 * @return display texts collection
	 */
	public Collection<String> getDisplayTexts() {
		if (!externalDisplayTexts.isEmpty()) {
			return externalDisplayTexts;
		} else {
			if (attributeDisplayTexts == null) {
				attributeDisplayTexts = extractStringValues(getDisplayText());
			}
			return attributeDisplayTexts;
		}
	}

	/**
	 * @see org.eclipse.birt.report.taglib.component.ParamValueField#setDisplayText(java.lang.String)
	 */
	@Override
	public void setDisplayText(String displayText) {
		attributeDisplayTexts = null;
		super.setDisplayText(displayText);
	}

	/**
	 * @see org.eclipse.birt.report.taglib.component.ParamValueField#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		attributeValues = null;
		super.setValue(value);
	}

	/**
	 * Extract values from the given value object. If the value object is already a
	 * collection, return it. If the value object is a string, split using the
	 * delimiter. Else, consider the object as a single value.
	 *
	 * @param value value object
	 * @return collection based on the value object
	 */
	private List<Object> extractValues(Object value) {
		if (value instanceof List) {
			return (List<Object>) value;
		} else if (value instanceof Object[]) {
			return Arrays.asList((Object[]) value);
		} else if (value instanceof String) {
			String valueString = (String) value;
			return Arrays.asList((Object[]) valueString.split(Pattern.quote(getDelim())));
		} else
		// single value
		{
			return Collections.singletonList(value);
		}
	}

	/**
	 * Same as extractValues() but only with strings.
	 *
	 * @see #extractValues(Object)
	 */
	private List<String> extractStringValues(Object value) {
		if (value instanceof List) {
			return (List<String>) value;
		} else if (value instanceof String[]) {
			return Arrays.asList((String[]) value);
		} else if (value instanceof String) {
			String valueString = (String) value;
			return Arrays.asList((String[]) valueString.split(Pattern.quote(getDelim())));
		} else
		// single value
		{
			return Collections.singletonList((String) value);
		}
	}
}
