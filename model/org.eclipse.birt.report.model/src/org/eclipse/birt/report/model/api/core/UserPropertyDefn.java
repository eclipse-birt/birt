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

package org.eclipse.birt.report.model.api.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.IObjectDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.metadata.UserChoice;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ChoiceSet;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataException;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;

/**
 * Represents a user-defined property. User-defined properties are created by
 * the user and reside on elements. If element E has a user-defined property,
 * and element C extends E, then element C also has all the user-defined
 * properties defined on element E.
 * <p>
 * The user property definition implements the <code>IStructure</code> interface
 * so that it can be accessed generically, and changes can be done though the
 * command mechanism to allow undo/redo of style changes.
 * 
 */

public final class UserPropertyDefn extends ElementPropertyDefn implements IStructure {

	/**
	 * Display name for the property.
	 */

	private String displayName = null;

	/**
	 * Mark property is visible or not.
	 */
	private Boolean isVisible = null;

	/**
	 * Name of the type member.
	 */

	public static final String TYPE_MEMBER = "type"; //$NON-NLS-1$

	/**
	 * Name of the name member.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * Name of the isVisible member.
	 */
	public static final String ISVISIBLE_MEMBER = "isVisible";//$NON-NLS-1$

	/**
	 * Name of the default member.
	 */

	public static final String DEFAULT_MEMBER = "default"; //$NON-NLS-1$

	/**
	 * Name of the display name member.
	 */

	public static final String DISPLAY_NAME_MEMBER = "displayName"; //$NON-NLS-1$

	/**
	 * Name of the display name ID member.
	 */

	public static final String DISPLAY_NAME_ID_MEMBER = "displayNameID"; //$NON-NLS-1$

	/**
	 * Name of the structure itself. This is the name used to identify the structure
	 * in the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "UserProperty"; //$NON-NLS-1$

	/**
	 * Name of the choices member.
	 */

	public static final String CHOICES_MEMBER = "choices"; //$NON-NLS-1$

	/**
	 * All the allowed types of UserProeprtyDefn. Now, "extends", elementRef,
	 * structRef, structure are not supported.
	 */

	private static List<IPropertyType> allowedTypes = null;

	static {
		allowedTypes = new ArrayList<IPropertyType>();
		Iterator<IPropertyType> iter = MetaDataDictionary.getInstance().getPropertyTypes().iterator();
		while (iter.hasNext()) {
			IPropertyType propType = iter.next();
			int type = propType.getTypeCode();
			switch (type) {
			case IPropertyType.STRING_TYPE:
			case IPropertyType.LITERAL_STRING_TYPE:
			case IPropertyType.BOOLEAN_TYPE:
			case IPropertyType.DATE_TIME_TYPE:
			case IPropertyType.FLOAT_TYPE:
			case IPropertyType.INTEGER_TYPE:
			case IPropertyType.EXPRESSION_TYPE:
			case IPropertyType.XML_TYPE:
				allowedTypes.add(propType);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Default constructor.
	 */

	public UserPropertyDefn() {
		PropertyType typeDefn = MetaDataDictionary.getInstance().getPropertyType(IPropertyType.STRING_TYPE_NAME);
		setType(typeDefn);
	}

	/**
	 * Gets valid types for user property. Each one in the list is an instance of
	 * <code>IPropertyType</code>.
	 * 
	 * @return the list of allowed property types for user property.
	 */

	public static List<IPropertyType> getAllowedTypes() {
		return allowedTypes;
	}

	/**
	 * Gets the value of property by the given property definition.
	 * 
	 * @param module the module
	 * @param prop   definition of the property to get
	 * 
	 * @return value of the property.
	 */

	public Object getProperty(Module module, PropertyDefn prop) {
		Object value = getLocalProperty(module, prop);
		return value == null ? prop.getDefault() : value;
	}

	/**
	 * Sets the value for the given property definition.
	 * 
	 * @param prop  definition of the property to set
	 * @param value value to set
	 */

	public void setProperty(PropertyDefn prop, Object value) {
		assert prop != null;
		String memberName = prop.getName();
		if (memberName.equals(TYPE_MEMBER)) {
			type = MetaDataDictionary.getInstance().getPropertyType((String) value);
		} else if (memberName.equals(NAME_MEMBER)) {
			if (value == null)
				name = null;
			else
				name = value.toString();
		} else if (memberName.equals(DISPLAY_NAME_MEMBER)) {
			if (value == null)
				displayName = null;
			else
				displayName = value.toString();
		} else if (memberName.equals(DISPLAY_NAME_ID_MEMBER)) {
			if (value == null)
				displayNameID = null;
			else
				displayNameID = value.toString();
		} else if (memberName.equals(ISVISIBLE_MEMBER)) {
			assert value instanceof Boolean;
			isVisible = (Boolean) value;
		} else if (memberName.equals(DEFAULT_MEMBER)) {
			setDefault(value);
		}
	}

	/**
	 * Gets the name predefined for this structure.
	 * 
	 * @return structure name "UserProperty".
	 * 
	 */

	public String getStructName() {
		return STRUCTURE_NAME;
	}

	/**
	 * Gets the property type.
	 * 
	 * @return integer represented user property type.
	 * 
	 */

	public int getValueType() {
		return USER_PROPERTY;
	}

	/**
	 * Makes a copy of this user property definition.
	 * 
	 * @return IStructure of this property definition, or null if this property
	 *         definition can not be cloned.
	 */

	public IStructure copy() {
		try {
			UserPropertyDefn uDefn = (UserPropertyDefn) clone();
			if (details instanceof ChoiceSet) {
				uDefn.details = ((ChoiceSet) details).clone();
			}
			return uDefn;
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		return null;
	}

	/**
	 * Gets the definition of the structure which represents the user property
	 * definition.
	 * 
	 * @return structure definition.
	 */

	public IStructureDefn getDefn() {
		return MetaDataDictionary.getInstance().getStructure(STRUCTURE_NAME);
	}

	/**
	 * Gets the object definition of the user property definition.
	 * 
	 * @return object definition.
	 * 
	 */

	public IObjectDefn getObjectDefn() {
		return MetaDataDictionary.getInstance().getStructure(STRUCTURE_NAME);
	}

	/**
	 * Gets the display name of this user property definition. The search will check
	 * the translation dictionary firstly, then look at the instance itself. If no
	 * display name defined, the XML name will be returned.
	 * 
	 * @return display name of this user property.
	 * 
	 */
	public String getDisplayName() {
		if (!StringUtil.isBlank(displayName)) {
			// 2. return displayName set on the instance.
			return displayName;
		}

		// 3. return XML name instead.
		return name;
	}

	/**
	 * Sets the display name of the property. Use this only for testing; you should
	 * normally set the display name message ID so that the name can be retrieved
	 * from a message catalog and localized.
	 * 
	 * @param theName the display name to set
	 */

	public void setDisplayName(String theName) {
		displayName = theName;
	}

	/**
	 * Sets the (anonymous) set of choices for a property. The choices are stored
	 * here directly, they are not named and stored in the data dictionary as are
	 * choices for system properties.
	 * 
	 * @param choiceArray choice array to be set.
	 */

	public void setChoices(UserChoice[] choiceArray) {
		if (choiceArray == null) {
			details = null;
			return;
		}

		// Create an anonymous extended choice set to hold the choices.

		ChoiceSet choices = new ChoiceSet(null);
		choices.setChoices(choiceArray);
		details = choices;
	}

	/**
	 * Checks whether <code>displayName</code> matches any items in the choice set
	 * for an extended choice property type on a user defined choice set. If
	 * <code>displayName</code> exists in the choice set, return the name of this
	 * choice. Otherwise, return <code>null</code>.
	 * 
	 * @param module      the module
	 * @param displayName the candidate display name
	 * @return the choice name if found. Otherwise, return <code>null</code>.
	 */

	protected String validateExtendedChoicesByDisplayName(Module module, String displayName) {
		if (displayName == null || hasChoices() == false)
			return null;

		IChoiceSet choiceSet = getChoices();
		UserChoice choice = choiceSet.findUserChoiceByDisplayName(module, displayName);

		if (choice != null)
			return choice.getName();

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getStructDefn()
	 */

	public IStructureDefn getStructDefn() {
		return MetaDataDictionary.getInstance().getStructure(STRUCTURE_NAME);
	}

	/**
	 * User-defined methods are not supported.
	 * 
	 * @return <code>null</code>
	 */

	public IMethodInfo getMethodInfo() {
		assert false;
		return null;
	}

	/**
	 * Sets the property type.
	 * 
	 * @param typeDefn the property type
	 */

	public void setType(PropertyType typeDefn) {
		type = typeDefn;
	}

	/**
	 * Checks whether the element can take the given user property definition and
	 * the definition is valid.
	 * 
	 * @param module  the module
	 * @param element the design element that holds the user-defined property
	 * 
	 * @throws UserPropertyException if the element is not allowed to have user
	 *                               property or the user property definition is
	 *                               invalid.
	 * @throws MetaDataException     if the user property definition is
	 *                               inconsistent.
	 */

	public void checkUserPropertyDefn(Module module, DesignElement element)
			throws UserPropertyException, MetaDataException {
		// Does the element allow user properties?

		String name = getName();
		if (!element.getDefn().allowsUserProperties())
			throw new UserPropertyException(element, name, UserPropertyException.DESIGN_EXCEPTION_USER_PROP_DISALLOWED);

		// Validate the name.

		if (StringUtil.isBlank(name))
			throw new UserPropertyException(element, name, UserPropertyException.DESIGN_EXCEPTION_NAME_REQUIRED);

		if (element.getPropertyDefn(name) != null)
			throw new UserPropertyException(element, name, UserPropertyException.DESIGN_EXCEPTION_DUPLICATE_NAME);

		// Validate the property type is provided and not structure or element
		// reference.

		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		if (dd.getPropertyType(getTypeCode()) == null || getTypeCode() == IPropertyType.ELEMENT_REF_TYPE
				|| getTypeCode() == IPropertyType.STRUCT_TYPE)
			throw new UserPropertyException(element, name, UserPropertyException.DESIGN_EXCEPTION_INVALID_TYPE);

		// TODO: Check the display name or id.

		// Ensure choices exist if this is a choice typeCode.

		if (getTypeCode() == IPropertyType.CHOICE_TYPE) {
			IChoiceSet choices = getChoices();
			if (choices == null || choices.getChoices().length == 0)
				throw new UserPropertyException(element, name, UserPropertyException.DESIGN_EXCEPTION_MISSING_CHOICES);
		}

		// if the user-defined property has choices and its type is not
		// "choice", validate the value of the user choice according to the
		// type.

		if (hasChoices()) {
			IChoiceSet choiceSet = getChoices();
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
				if (getTypeCode() != IPropertyType.CHOICE_TYPE) {
					try {
						value = validateValue(module, element, value);
					} catch (PropertyValueException e) {
						throw new UserPropertyException(element, name,
								UserPropertyException.DESIGN_EXCEPTION_INVALID_CHOICE_VALUE);
					}
				}
			}
		}

		// Build the cached semantic data.

		this.build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.IStructure#getLocalProperty(org.eclipse
	 * .birt.report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	public Object getLocalProperty(Module module, PropertyDefn propDefn) {
		assert propDefn != null;
		String memberName = propDefn.getName();
		if (memberName.equals(TYPE_MEMBER))
			return type.getName();
		if (memberName.equals(NAME_MEMBER))
			return name;
		if (memberName.equals(DISPLAY_NAME_MEMBER))
			return displayName;
		if (memberName.equals(DISPLAY_NAME_ID_MEMBER))
			return displayNameID;
		if (memberName.equals(ISVISIBLE_MEMBER))
			return isVisible;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.core.IStructure#getProperty(org.eclipse
	 * .birt.report.model.core.Module, java.lang.String)
	 */

	public Object getProperty(Module module, String memberName) {
		PropertyDefn prop = (PropertyDefn) getDefn().getMember(memberName);
		if (prop == null)
			return null;

		return getProperty(module, prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IStructure#isReferencable()
	 */

	public boolean isReferencable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setDefault(java.lang
	 * .Object)
	 */

	public void setDefault(Object value) {
		if (value != null && getTypeCode() == IPropertyType.EXPRESSION_TYPE && !(value instanceof Expression)) {
			value = new Expression(value, IExpressionType.CONSTANT);
		}
		super.setDefault(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#build()
	 */

	public void build() throws MetaDataException {
		// Check consistency of options. A style property that is meant
		// to be used by multiple elements cannot also be intrinsic:
		// defined by a member variable.

		if (isIntrinsic() && isStyleProperty())
			throw new MetaDataException(new String[] { name },
					MetaDataException.DESIGN_EXCEPTION_INCONSISTENT_PROP_TYPE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IStructure#isDesignTime()
	 */
	public boolean isDesignTime() {
		return true;
	}

	/**
	 * Checks whether the property is visible to the property sheet.
	 * 
	 * @return <code>true</code> if property is visible.
	 */

	public boolean isVisible() {
		Boolean value = (Boolean) getProperty(null, ISVISIBLE_MEMBER);
		return value == null ? true : value.booleanValue();
	}

	/**
	 * Sets whether the property is visible to the property sheet.
	 * 
	 * @param isVisible
	 */

	public void setVisible(boolean isVisible) {
		this.isVisible = Boolean.valueOf(isVisible);
	}

}
