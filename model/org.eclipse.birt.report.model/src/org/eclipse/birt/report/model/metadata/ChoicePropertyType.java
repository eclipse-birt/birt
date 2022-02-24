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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Type for a property defined by a list of choices. The actual list of choices
 * is defined by the specific choice property. A choice name is stored as
 * <code>java.lang.String</code> internally.
 * 
 * @see ElementPropertyDefn
 */

public class ChoicePropertyType extends PropertyType {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(ChoicePropertyType.class.getName());

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.choice"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public ChoicePropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/**
	 * Validates a choice property. The choice property can be a string with one of
	 * the valid choices, or it can be a string that contains one of the localized
	 * names for the choices.
	 * 
	 * @return object of type String, Returns <code>null</code> if the
	 *         <code>value</code> parameter is null.
	 * @throws PropertyValueException if <code>value</code> is not found in the
	 *                                predefined choices, or it is not a valid
	 *                                localized choice name.
	 */

	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return validateInputString(module, element, defn, (String) value);
		}

		logger.log(Level.WARNING, "Invalid choice value type:" + value); //$NON-NLS-1$

		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, getTypeCode());
	}

	/**
	 * Validates an XML value within a choice set. This method throws an exception
	 * if choice properties cannot be validated in the predefined choice list.
	 * Otherwise return the validated value.
	 * 
	 * @return the choice name if it is contained in the predefined choice list;
	 *         Return <code>null/code> the value is null;
	 * @throws PropertyValueException if this value is not found in the predefined
	 *                                choice list.
	 */

	public Object validateXml(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		assert value == null || value instanceof String;
		String tmpValue = (String) value;

		tmpValue = StringUtil.trimString(tmpValue);
		if (tmpValue == null) {
			return null;
		}

		// if the property doesn't define the restrictions, the whole choice set
		// will be returned.

		IChoiceSet allowedChoices = defn.getAllowedChoices();
		assert allowedChoices != null;

		// Internal name of a choice.

		IChoice choice = allowedChoices.findChoice(tmpValue);
		if (choice != null) {
			return choice.getName();
		}

		IChoiceSet propChoices = defn.getChoices();
		if (propChoices.contains(tmpValue)) {
			// The is in the whole choice set, but not in the allowed list.

			logger.log(Level.SEVERE, "Not allowed choice " + tmpValue); //$NON-NLS-1$

			throw new PropertyValueException(tmpValue, PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_ALLOWED,
					getTypeCode());
		}
		if (!isDataTypeAny(propChoices, tmpValue))
			logger.log(Level.WARNING, "Not found choice: " + tmpValue); //$NON-NLS-1$

		throw new PropertyValueException(tmpValue, PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND,
				getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode() {
		return CHOICE_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getName()
	 */

	public String getName() {
		return CHOICE_TYPE_NAME;
	}

	/**
	 * Converts the choice property value to a locale-independent string. The
	 * <code>value</code> should be in the predefined choice list. If
	 * <code>value</code> is <code>null</code>, the return will be null, if the
	 * <code>value</code> is in the predefined choice list, the value will be
	 * returned as a String.
	 * 
	 * @return the value as a string if it is in the predefined choice list, return
	 *         null if the value is null;
	 */

	public String toString(Module module, PropertyDefn defn, Object value) {
		IChoiceSet propChoices = defn.getChoices();
		assert propChoices != null;

		if (value == null)
			return null;

		IChoice choice = propChoices.findChoice((String) value);
		if (choice != null)
			return choice.getName();

		// the special case to write out the invalid value.

		return value.toString();
	}

	/**
	 * Returns a localized choice display name according to its internal name.
	 * 
	 * @return the display string for its internal value.
	 */

	public String toDisplayString(Module module, PropertyDefn defn, Object name) {
		if (name == null)
			return null;

		IChoiceSet propChoices = defn.getChoices();
		assert propChoices != null;

		IChoice choice = propChoices.findChoice(name.toString());
		if (choice != null) {
			// Return localized choice name.

			return choice.getDisplayName();
		}

		// assert false for other cases.

		assert false;
		return null;
	}

	/**
	 * Validates a string according to predefined choice properties in
	 * locale-dependent way, the <code>name</code> can be either an internal choice
	 * name or it can be a localized choice name.
	 * 
	 * @return the internal choice name, if the <code>name</code> is an internal
	 *         choice name or a localized choice name, return <code>null</code> if
	 *         <code>name</code> is null.
	 * @throws PropertyValueException if the <code>name</code> is not valid.
	 */

	public Object validateInputString(Module module, DesignElement element, PropertyDefn defn, String name)
			throws PropertyValueException {
		name = StringUtil.trimString(name);
		if (name == null) {
			return null;
		}

		// if the property doesn't define the restrictions, the whole choice set
		// will be returned.

		IChoiceSet allowedChoices = defn.getAllowedChoices();
		assert allowedChoices != null;

		// 1. Internal name of a choice.

		IChoice choice = allowedChoices.findChoice(name);
		if (choice != null) {
			return choice.getName();
		}

		// 2. localized display name of a choice.
		// Convert the localized choice name into internal name.

		choice = null;
		if (!allowedChoices.isUserDefined())
			choice = allowedChoices.findChoiceByDisplayName(name);
		else
			choice = allowedChoices.findUserChoiceByDisplayName(module, name);

		if (choice != null) {
			return choice.getName();
		}

		IChoiceSet propChoices = defn.getChoices();
		if (propChoices.contains(name)) {
			// The is in the whole choice set, but not in the allowed list.

			logger.log(Level.SEVERE, "Not allowed choice " + name); //$NON-NLS-1$

			throw new PropertyValueException(name, PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_ALLOWED,
					getTypeCode());
		}

		if (!isDataTypeAny(propChoices, name))
			logger.log(Level.WARNING, "Invalid choice:" + name); //$NON-NLS-1$

		throw new PropertyValueException(name, PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, getTypeCode());

	}

	public static boolean isDataTypeAny(IChoiceSet choiceSet, Object value) {
		if (choiceSet != null && DesignChoiceConstants.CHOICE_COLUMN_DATA_TYPE.equals(choiceSet.getName())
				&& DesignChoiceConstants.COLUMN_DATA_TYPE_ANY.equals(value))
			return true;
		return false;
	}
}
