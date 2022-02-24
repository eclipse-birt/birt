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

import java.math.BigDecimal;
import java.util.Locale;

import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Base class for the meta-data for property types. Every property has a
 * property type. The property type provides a display name, data validation
 * methods, an XML name, and more.
 * <p>
 * Note that the property type information is a partial description of a
 * property. Some types (such as choice) require further information specific to
 * the property, such as the actual list of choices.
 * <p>
 * The conversion and validation methods require a handle to the report design.
 * The design provides additional information for those conversions that require
 * it. For example, dimensions require knowledge of the default units for the
 * design. Colors require access to the custom colors defined on the design.
 */

public abstract class PropertyType implements IPropertyType {

	/**
	 * The resource key for the localized property display name.
	 */

	private String displayNameKey = null;

	/**
	 * The default locale of all the BIRT meta-data.
	 */

	protected static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	/**
	 * Constructs a property type given its display name id.
	 * <p>
	 * Instances of this class should be created only when creating the meta-data
	 * dictionary.
	 * 
	 * @param displayNameID the resource key for this property type's display name
	 */

	protected PropertyType(String displayNameID) {
		setDisplayNameKey(displayNameID);
	}

	/**
	 * Returns the localized display name.
	 * 
	 * @return the localized display name
	 */

	public String getDisplayName() {
		assert displayNameKey != null;
		return ModelMessages.getMessage(displayNameKey);

	}

	/**
	 * Returns the numeric code for this type.
	 * 
	 * @return the internal type code
	 */

	public abstract int getTypeCode();

	/**
	 * Returns the name to use in the XML design and XML metadata files.
	 * 
	 * @return the type name used in the XML design file
	 */

	public abstract String getName();

	/**
	 * Internal method to build the property type. Should be called only from
	 * <code>MetaDataDictionary</code>.
	 */

	protected void build() {
	}

	/**
	 * Sets the display name resource key.
	 * 
	 * @param key message key to set
	 */

	void setDisplayNameKey(String key) {
		displayNameKey = key;
	}

	/**
	 * Sets the set of choices.
	 * 
	 * @param theChoices the choices to set
	 */

	void setChoices(ChoiceSet theChoices) {
		if (theChoices == null)
			return;

		// Default implementation does nothing. Must be overridden
		// by derived types that support choices.

		assert false;
	}

	/**
	 * Gets the set of choices for this type.
	 * 
	 * @return the set of choices, or null if no choices are available
	 */

	public IChoiceSet getChoices() {
		return null;
	}

	/**
	 * Gets the display name resource key.
	 * 
	 * @return the display name message key
	 */

	public String getDisplayNameKey() {
		return displayNameKey;
	}

	/**
	 * Validates a value for this property. The value is one that comes from the
	 * user or program. A string value is assumed to be in the user's locale. Many
	 * properties accept values of several types; see the specific property type for
	 * details.
	 * <p>
	 * This method also does any necessary conversions. For example, it converts a
	 * string representation of a number into the standard internal type; converts
	 * string dimension values into the internal dimension object, etc. The return
	 * value is what should be stored in the property list when setting a property.
	 * 
	 * @param module the design used to resolve expressions, names, unit
	 *               conversions, custom colors, etc.
	 * @param defn   optional property definition that provides additional
	 *               information such as a choice list
	 * @param value  the value to be validated
	 * @return the validated value if the value is valid
	 * @throws PropertyValueException if the value is not valid
	 */

	abstract public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException;

	/**
	 * Validate a user input value for this property, the value is one that comes
	 * from the user input. The string value user entered is assumed to be in the
	 * user's locale. This method convert the locale-depedent user input into a
	 * standard internal type. The return value is what the specific type should be
	 * stored internally.
	 * 
	 * @param module the design used to resolve expressions, names, unit
	 *               conversions, custom colors, etc.
	 * @param defn   optional property definition that provides additional
	 *               information such as a choice list
	 * @param value  the input string value to be validated
	 * @return the validated value if the input value is valid.
	 * @throws PropertyValueException
	 */
	public Object validateInputString(Module module, DesignElement element, PropertyDefn defn, String value)
			throws PropertyValueException {
		return validateValue(module, element, defn, value);
	}

	/**
	 * Validates the XML representation of the value of a property of this property
	 * type. The XML value is given as a string read from an XML file.
	 * <p>
	 * The default implementation does the same validation as for user input.
	 * Derived type classes override this when XML-specific behavior is required,
	 * especially for dates and numbers.
	 * 
	 * @param module the design used to resolve expressions, names, unit
	 *               conversions, custom colors, etc.
	 * @param defn   optional property definition that provides additional
	 *               information such as a choice list
	 * @param value  the value read from the XML file
	 * @return the object to store for the property value
	 * @throws PropertyValueException if the value is not valid
	 */

	public Object validateXml(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		return validateValue(module, element, defn, value);
	}

	/**
	 * Converts a property of this type to a double value. If the value does not
	 * convert to a double, return 0.
	 * 
	 * @param module the report design
	 * @param value  the value of a parameter of this type
	 * @return the value as a double, or 0 if the value does not convert to a
	 *         double.
	 */

	public double toDouble(Module module, Object value) {
		return toInteger(module, value);
	}

	/**
	 * Converts a property of this type to a integer value. If the value does not
	 * convert to a integer, return 0.
	 * 
	 * @param module the report design
	 * @param value  The value of a parameter of this type.
	 * @return the value as an integer, or 0 if the value does not convert to an
	 *         integer
	 */

	public int toInteger(Module module, Object value) {
		// Default implementation is for types that cannot
		// be converted to integer.

		return 0;
	}

	/**
	 * Converts the property value to an XML string.
	 * <p>
	 * Use this form when providing an additional property-specific choice set to
	 * use in the conversion.
	 * 
	 * @param module the report design
	 * @param defn   optional property definition that provides additional
	 *               information such as a choice list
	 * @param value  the property value. Must be valid.
	 * @return the XML representation of the property.
	 */

	public String toXml(Module module, PropertyDefn defn, Object value) {
		return toString(module, defn, value);
	}

	/**
	 * Converts the property value to a locale-independent string.
	 * <p>
	 * Use this form when providing an additional property-specific choice set to
	 * use in the conversion.
	 * 
	 * @param module the report design
	 * @param defn   optional property definition that provides additional
	 *               information such as a choice list
	 * @param value  the property value. Must be valid.
	 * @return the XML representation of the property.
	 */

	public abstract String toString(Module module, PropertyDefn defn, Object value);

	/**
	 * Returns the localized string value of a property.
	 * 
	 * @param module the report design
	 * @param defn   optional property definition that provides additional
	 *               information such as a choice list
	 * @param value  the internal value
	 * @return the property as a localized string
	 */

	public String toDisplayString(Module module, PropertyDefn defn, Object value) {
		return toString(module, defn, value);
	}

	/**
	 * Converts the property value to a number (<code>BigDecimal</code>).
	 * 
	 * @param module the report design
	 * @param value  the property value. Must be valid.
	 * @return the value of the property as a <code>BigDecimal</code>
	 */

	public BigDecimal toNumber(Module module, Object value) {
		return new BigDecimal(toDouble(module, value));
	}

	/**
	 * Converts the property value to a Boolean.
	 * 
	 * @param module the report design
	 * @param value  the property value. Must be valid.
	 * @return the value of the property as a Boolean
	 */

	public boolean toBoolean(Module module, Object value) {
		return toInteger(module, value) != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString() {
		if (!StringUtil.isBlank(getName()))
			return getName();
		return super.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */

	public boolean equals(Object obj) {
		if (obj instanceof IPropertyType) {
			if (getTypeCode() == ((IPropertyType) obj).getTypeCode())
				return true;
		}

		return false;
	}
}
