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

package org.eclipse.birt.report.model.command;

import java.util.List;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.metadata.UserChoice;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataException;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Creates, modifies and deletes user-defined property, which is also known as
 * user property.
 * 
 */

public class UserPropertyCommand extends AbstractElementCommand {

	/**
	 * Constructor.
	 * 
	 * @param module the module
	 * @param obj    the element to modify
	 */

	public UserPropertyCommand(Module module, DesignElement obj) {
		super(module, obj);
	}

	/**
	 * Adds a user property.
	 * 
	 * @param prop the user property to add
	 * @throws UserPropertyException if the element is not allowed to have user
	 *                               property or the user property definition is
	 *                               invalid, or if the value of the user-defined
	 *                               choice is invalid for the type of user property
	 *                               definition, or the user property definition is
	 *                               inconsistent.
	 */

	public void addUserProperty(UserPropertyDefn prop) throws UserPropertyException {
		if (prop == null)
			return;

		assert element != null;

		checkUserPropertyDefn(prop);

		String name = prop.getName();
		if (element.getPropertyDefn(name) != null)
			throw new UserPropertyException(element, name, UserPropertyException.DESIGN_EXCEPTION_DUPLICATE_NAME);

		// Make the changes. If the property has a default value, set that
		// value.

		ActivityStack stack = getActivityStack();
		UserPropertyRecord propCmd = new UserPropertyRecord(element, prop, true);
		stack.execute(propCmd);
	}

	/**
	 * Drops a user property.
	 * 
	 * @param propName the name of the property to drop
	 * @throws UserPropertyException if the property is not found, is not a user
	 *                               property, or is not defined on this element.
	 */

	public void dropUserProperty(String propName) throws UserPropertyException {
		assert element != null;

		if (StringUtil.isBlank(propName))
			return;

		// Does the element allow user properties?

		if (!element.getDefn().allowsUserProperties())
			throw new UserPropertyException(element, propName,
					UserPropertyException.DESIGN_EXCEPTION_USER_PROP_DISALLOWED);

		UserPropertyDefn prop = element.getLocalUserPropertyDefn(propName);
		if (prop == null)
			throw new UserPropertyException(element, propName, UserPropertyException.DESIGN_EXCEPTION_NOT_FOUND);

		// Create the command to remove the property. Start a transaction
		// using the label of this command.

		UserPropertyRecord propCmd = new UserPropertyRecord(element, prop, false);
		ActivityStack stack = getActivityStack();
		stack.startTrans(propCmd.getLabel());

		// remove the value of it self.

		if (element.getLocalProperty(module, propName) != null) {
			PropertyRecord valueCmd = new PropertyRecord(element, propName, null);
			stack.execute(valueCmd);
		}

		// Get the list of elements that extend this one. Remove the value
		// from all these descendents.

		List descendents = element.getDescendents();
		for (int i = 0; i < descendents.size(); i++) {
			DesignElement child = (DesignElement) descendents.get(i);
			if (child.getLocalProperty(module, propName) != null) {
				PropertyRecord valueCmd = new PropertyRecord(child, propName, null);
				stack.execute(valueCmd);
			}
		}
		stack.execute(propCmd);
		stack.commit();
	}

	/**
	 * Sets the definition of the user-defined property.
	 * 
	 * @param oldPropDefn the old user-defined property to set
	 * @param newPropDefn the new definition to set
	 * @throws UserPropertyException  if the element is not allowed to have user
	 *                                property or the user property definition is
	 *                                invalid, or if the value of the user-defined
	 *                                choice is invalid for the type of user
	 *                                property definition, or the user property
	 *                                definition is inconsistent.
	 * @throws PropertyValueException if the property value is invalid for the new
	 *                                user-defined property
	 */

	public void setPropertyDefn(UserPropertyDefn oldPropDefn, UserPropertyDefn newPropDefn)
			throws UserPropertyException, PropertyValueException {

		assert element != null;
		assert oldPropDefn != null;

		if (newPropDefn == null) {
			// need this?

			dropUserProperty(oldPropDefn.getName());
			return;
		}

		String propName = oldPropDefn.getName();

		if (element.getLocalUserPropertyDefn(propName) == null)
			throw new UserPropertyException(element, propName, UserPropertyException.DESIGN_EXCEPTION_NOT_FOUND);

		checkUserPropertyDefn(newPropDefn);
		String name = newPropDefn.getName();

		int oldType = oldPropDefn.getType().getTypeCode();
		int newType = newPropDefn.getType().getTypeCode();

		// Create the command to rename the property. Start a transaction
		// using the label of this command.

		ActivityStack stack = getActivityStack();

		String label = CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_DEFINITION_MESSAGE,
				new String[] { oldPropDefn.getDisplayName() });
		stack.startTrans(label);

		UserPropertyRecord propCmd = new UserPropertyRecord(element, newPropDefn, true);
		stack.execute(propCmd);

		// Get the list of elements that extend this one. Add the element
		// itself into the list and validate value.

		List descendents = element.getDescendents();
		descendents.add(element);
		for (int i = 0; i < descendents.size(); i++) {
			DesignElement child = (DesignElement) descendents.get(i);
			Object value = child.getLocalProperty(module, propName);

			// If the type of the definition is changed, then we will validate
			// the old value. Another situation is that the type of the
			// definition
			// has no change and is "Choice", and then we change the choices for
			// it. In this situation, we still validate the old value.

			if (oldType != newType || (oldType == IPropertyType.CHOICE_TYPE && newType == IPropertyType.CHOICE_TYPE)) {
				if (value != null) {
					try {
						value = validateValue(newPropDefn, value.toString());
					} catch (PropertyValueException ex) {
						stack.rollback();
						throw ex;
					}
					if (value != null) {
						PropertyRecord valueCmd = new PropertyRecord(child, name, value);
						stack.execute(valueCmd);
					}

					// if the name is unchanged, we need not drop the old value.

					if (!name.equals(propName)) {
						PropertyRecord valueCmd = new PropertyRecord(child, propName, null);
						stack.execute(valueCmd);
					}
				}
			}

			// the type is unchanged and name is changed, we will delete the
			// the old value and add a new value.

			else if (!name.equals(propName)) {
				if (value != null) {

					PropertyRecord valueCmd = new PropertyRecord(child, name, value);
					stack.execute(valueCmd);
					valueCmd = new PropertyRecord(child, propName, null);
					stack.execute(valueCmd);
				}
			}
		}

		if (!name.equals(propName)) {
			propCmd = new UserPropertyRecord(element, oldPropDefn, false);
			stack.execute(propCmd);
		}
		stack.commit();
	}

	/**
	 * Checks whether the element can take the given user property definition.
	 * 
	 * @param prop the user property definition
	 * @throws UserPropertyException if the element is not allowed to have user
	 *                               property or the user property definition is
	 *                               invalid, or if the value of the user-defined
	 *                               choice is invalid for the type of user property
	 *                               definition, or the user property definition is
	 *                               inconsistent.
	 */

	private void checkUserPropertyDefn(UserPropertyDefn prop) throws UserPropertyException {
		// Does the element allow user properties?

		String name = prop.getName();
		if (!element.getDefn().allowsUserProperties())
			throw new UserPropertyException(element, name, UserPropertyException.DESIGN_EXCEPTION_USER_PROP_DISALLOWED);

		// Validate the name.

		if (StringUtil.isBlank(name))
			throw new UserPropertyException(element, name, UserPropertyException.DESIGN_EXCEPTION_NAME_REQUIRED);

		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		List supportedTypes = UserPropertyDefn.getAllowedTypes();
		assert supportedTypes != null;
		if (!supportedTypes.contains(dd.getPropertyType(prop.getTypeCode())))
			throw new UserPropertyException(element, name, UserPropertyException.DESIGN_EXCEPTION_INVALID_TYPE);

		// Check the display name or id.

		String msgID = prop.getDisplayNameID();
		String displayName = prop.getDisplayName();
		if (!StringUtil.isBlank(msgID)) {
			displayName = module.getMessage(msgID);
			if (StringUtil.isBlank(displayName))
				throw new UserPropertyException(element, name,
						UserPropertyException.DESIGN_EXCEPTION_INVALID_DISPLAY_ID);
		}

		// Ensure choices exist if this is a choice typeCode.

		if (prop.getTypeCode() == IPropertyType.CHOICE_TYPE) {
			IChoiceSet choices = prop.getChoices();
			if (choices == null || choices.getChoices().length == 0)
				throw new UserPropertyException(element, name, UserPropertyException.DESIGN_EXCEPTION_MISSING_CHOICES);
		}

		// if the user-defined property has choices and its type is not
		// "choice", validate the value of the user choice according to the
		// type.

		if (prop.hasChoices()) {
			IChoiceSet choiceSet = prop.getChoices();
			IChoice[] choices = choiceSet.getChoices();
			for (int i = 0; i < choices.length; i++) {
				UserChoice choice = (UserChoice) choices[i];
				Object value = choice.getValue();
				if (StringUtil.isBlank(choice.getName())) {
					throw new UserPropertyException(element, name,
							UserPropertyException.DESIGN_EXCEPTION_CHOICE_NAME_REQUIRED);
				}
				if (value == null)
					throw new UserPropertyException(element, name,
							UserPropertyException.DESIGN_EXCEPTION_CHOICE_VALUE_REQUIRED);
				if (prop.getTypeCode() != IPropertyType.CHOICE_TYPE) {
					try {
						value = prop.validateValue(module, element, value);
					} catch (PropertyValueException e) {
						throw new UserPropertyException(element, name,
								UserPropertyException.DESIGN_EXCEPTION_INVALID_CHOICE_VALUE);
					}
				}
			}
		}

		// build the default value

		Object defaultValue = prop.getDefault();
		if (defaultValue != null) {
			try {
				prop.validateValue(module, element, defaultValue);
			} catch (PropertyValueException e) {
				throw new UserPropertyException(element, name,
						UserPropertyException.DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE, e,
						new String[] { defaultValue.toString(), prop.getType().getName() });
			}
		}

		// Build the cached semantic data.

		try {
			prop.build();
		} catch (MetaDataException e) {
			throw new UserPropertyException(element, name, UserPropertyException.DESIGN_EXCEPTION_INVALID_DEFINITION,
					e);
		}

	}

	/**
	 * Private method to validate the value of a property.
	 * 
	 * @param prop  definition of the property to validate
	 * @param value the value to validate
	 * @return the value to store for the property
	 * @throws PropertyValueException if the value is not valid
	 */

	private Object validateValue(ElementPropertyDefn prop, String value) throws PropertyValueException {
		// Validate the value.

		if (value == null)
			return null;

		try {
			return prop.validateValue(module, element, value);
		} catch (PropertyValueException ex) {
			ex.setElement(element);
			ex.setPropertyName(prop.getName());
			throw ex;
		}
	}
}
