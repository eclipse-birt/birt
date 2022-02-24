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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.ElementReferenceValidator;
import org.eclipse.birt.report.model.api.validators.SimpleListValidator;
import org.eclipse.birt.report.model.api.validators.StructureListValidator;
import org.eclipse.birt.report.model.api.validators.StructureReferenceValidator;
import org.eclipse.birt.report.model.api.validators.StructureValidator;
import org.eclipse.birt.report.model.api.validators.ValueRequiredValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.validators.ISemanticTriggerDefnSetProvider;

import com.ibm.icu.util.ULocale;

/**
 * Base class for both element property and structure member definitions.
 */

public abstract class PropertyDefn implements IPropertyDefn, ISemanticTriggerDefnSetProvider {

	/**
	 * Cache this instance to avoid method calls.
	 */

	protected final static PropertyType expressionType = MetaDataDictionary.getInstance()
			.getPropertyType(IPropertyType.EXPRESSION_TYPE);

	/**
	 * Supported sub-types for list property type.
	 */

	private static final List<IPropertyType> supportedSubTypes;

	protected IMessages messages;

	/**
	 * Where the property is defined.
	 */

	protected ObjectDefn definedBy = null;

	/**
	 * The cached property type.
	 */

	protected PropertyType type = null;

	/**
	 * The sub-type of the property definition. This is required if property type is
	 * "list".
	 */

	protected PropertyType subType = null;

	/**
	 * The internal (non-localized) name for the property. This name is used in
	 * code.
	 */

	protected String name = null;

	/**
	 * The message catalog ID for the property display name.
	 */

	protected String displayNameID = null;

	/**
	 * The trim option value.
	 * 
	 */
	protected int trimOption = XMLPropertyType.NO_VALUE;

	/**
	 * Default unit of the dimension property. Some properties have special default
	 * unit, sucha as font-size, margin and so on. Other properties share the
	 * default unit set on the report design.
	 */

	protected String defaultUnit = DimensionValue.DEFAULT_UNIT;

	protected NameConfig nameConfig;

	/**
	 * Optional detailed information for the property type. The type of this object
	 * depends on the property type:
	 * 
	 * <p>
	 * <dl>
	 * <dt><strong>Choice set </strong></dt>
	 * <dd>details holds an object of type <code>ChoiceSet</code> that holds the
	 * list of available choices.</dd>
	 * 
	 * <dt><strong>Extended Choice set </strong></dt>
	 * <dd>details holds an object of type <code>ChoiceSet</code> that holds the
	 * list of available extended choices.</dd>
	 * 
	 * <dt><strong>User Defined Choice set </strong></dt>
	 * <dd>details holds an object of type <code>ChoiceSet</code> that holds the
	 * list of user defined choices.</dd>
	 * 
	 * <dt><strong>Element Reference </strong></dt>
	 * <dd>details holds an object of type <code>ElementDefn</code> that identifies
	 * the type of element that is referenced.</dd>
	 * 
	 * <dt><strong>Structure definition </strong></dt>
	 * <dd>details holds an object of type <code>StructureDefn</code> that defines
	 * the structures in the list.</dd>
	 * 
	 * <dt><strong>Argument List </strong></dt>
	 * <dd>details holds a list of argument <code>ArgumentDefn</code>.</dd>
	 * </dl>
	 */

	protected Object details = null;

	/**
	 * Whether this is an intrinsic property.
	 */

	protected boolean intrinsic = false;

	/**
	 * The default value, if any, for this property.
	 */

	protected Object defaultValue = null;

	/**
	 * Choice sets containing an allowed choices for a choice type, or containing an
	 * allowed units set for a dimension type.
	 */

	protected ChoiceSet allowedChoices = null;

	/**
	 * Choice sets containing an allowed choices for a choice type, or containing an
	 * allowed units set for a dimension type.
	 */

	protected ChoiceSet allowedUnits = null;

	/**
	 * Indicates if this whether this property is a list. This property is useful
	 * only when the property type is a structure type.
	 * 
	 */

	protected boolean isList = false;

	/**
	 * Reference to the name of value validator applied to this property.
	 */

	protected String valueValidator = null;

	/**
	 * The collection of semantic validatin triggers.
	 */

	protected SemanticTriggerDefnSet triggers = null;

	/**
	 * Whether the value of this property is required.
	 */

	protected boolean valueRequired = false;

	/**
	 * Whether the value of this property should be protected.
	 */

	protected boolean isEncryptable = false;

	/**
	 * The BIRT release when this property was introduced.
	 */

	protected String since;

	/**
	 * Whether the property can be set in the Factory or Presentation engine. If
	 * false, the property is read-only at runtime.
	 */

	protected boolean runtimeSettable;

	/**
	 * The context for a method.
	 */

	protected String context;

	/**
	 * The return type for an expression or method.
	 */

	protected String returnType;

	/**
	 * The value can be presented as <code>Expression</code>.
	 */

	protected boolean allowExpression;

	static {
		supportedSubTypes = new ArrayList<IPropertyType>();
		Iterator<IPropertyType> iter = MetaDataDictionary.getInstance().getPropertyTypes().iterator();
		while (iter.hasNext()) {
			IPropertyType propType = iter.next();
			int type = propType.getTypeCode();
			switch (type) {
			case IPropertyType.STRING_TYPE:
			case IPropertyType.BOOLEAN_TYPE:
			case IPropertyType.DATE_TIME_TYPE:
			case IPropertyType.FLOAT_TYPE:
			case IPropertyType.INTEGER_TYPE:
			case IPropertyType.EXPRESSION_TYPE:
			case IPropertyType.ELEMENT_REF_TYPE:
			case IPropertyType.LITERAL_STRING_TYPE:
				supportedSubTypes.add(propType);
				break;
			default:
				break;
			}
		}
	}

	public static boolean isSupportedSubType(IPropertyType type) {
		return supportedSubTypes.contains(type);
	}

	/**
	 * Constructs a Property Definition.
	 */

	public PropertyDefn() {
		since = "none"; //$NON-NLS-1$
	}

	/**
	 * Sets the owner definition of the property. It may be <code>ElementDefn</code>
	 * or <code>StructureDefn</code>.
	 * 
	 * @param owner the owner definition to set
	 */

	public void setOwner(ObjectDefn owner) {
		definedBy = owner;
	}

	/**
	 * Gets the owner that defines this property. It may be <code>ElementDefn</code>
	 * or <code>StructureDefn</code>.
	 * 
	 * @return the owner definition
	 */

	public ObjectDefn definedBy() {
		return definedBy;
	}

	/**
	 * Returns the type of this value. The return can be one of the following
	 * constants:
	 * <p>
	 * <ul>
	 * <li>SYSTEM_PROPERTY</li>
	 * <li>USER_PROPERTY</li>
	 * <li>STRUCT_PROPERTY</li>
	 * <li>EXTENSION_PROPERTY</li>
	 * </ul>
	 * 
	 * @return the type of this definition
	 */

	public abstract int getValueType();

	/**
	 * Builds the semantic information for this property. Called once while loading
	 * the meta-data. The build must succeed, or a programming error has occurred.
	 * 
	 * @throws MetaDataException if the property definition is inconsistent.
	 */

	protected void build() throws MetaDataException {
		buildDefn();

		buildTriggerDefnSet();
	}

	/**
	 * Builds the trigger definition set. This method cached all validators defined
	 * in property definition and slot definition. The cached validators are used to
	 * perform full validation of one element instance.
	 * 
	 * @throws MetaDataException if the validator is not found.
	 */

	protected final void buildTriggerDefnSet() throws MetaDataException {
		// build trigger definition.

		getTriggerDefnSet().build();
	}

	/**
	 * Validates the property definition and adds validator for the property
	 * definition. Called once while loading the meta-data. The build must succeed,
	 * or a programming error has occurred.
	 * 
	 * @throws MetaDataException if the property definition is inconsistent.
	 */

	final protected void buildDefn() throws MetaDataException {
		// Ensure we can find the property type.

		if (getType() == null)
			throw new MetaDataException(new String[] { name }, MetaDataException.DESIGN_EXCEPTION_PROP_TYPE_ERROR);

		displayNameID = StringUtil.trimString(displayNameID);

		// Perform type-specific initialization.
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		int tmpTypeCode = type.getTypeCode();

		switch (tmpTypeCode) {
		case IPropertyType.CHOICE_TYPE:

			// Build the set of choices. The list is required if this
			// property
			// is a choice property, and is not allowed otherwise.

			IChoiceSet choiceSet = getChoices();
			if (choiceSet == null || choiceSet.getChoices() == null)
				throw new MetaDataException(new String[] { name },
						MetaDataException.DESIGN_EXCEPTION_MISSING_PROP_CHOICES);
			break;

		case IPropertyType.STRUCT_TYPE:

			// A structure definition must be provided.

			if (details == null)
				throw new MetaDataException(new String[] { name, definedBy().getName() },
						MetaDataException.DESIGN_EXCEPTION_MISSING_STRUCT_DEFN);

			// Look up a string name reference.

			if (details instanceof String) {
				StructureDefn structDefn = (StructureDefn) dd.getStructure(StringUtil.trimString((String) details));
				details = structDefn;
			}

			if (isList()) {
				StructureListValidator validator = StructureListValidator.getInstance();
				SemanticTriggerDefn triggerDefn = new SemanticTriggerDefn(StructureListValidator.NAME);
				triggerDefn.setPropertyName(getName());
				triggerDefn.setValidator(validator);
				getTriggerDefnSet().add(triggerDefn);
			} else {
				StructureValidator validator = StructureValidator.getInstance();
				SemanticTriggerDefn triggerDefn = new SemanticTriggerDefn(StructureValidator.NAME);
				triggerDefn.setPropertyName(getName());
				triggerDefn.setValidator(validator);
				getTriggerDefnSet().add(triggerDefn);
			}
			break;

		case IPropertyType.ELEMENT_REF_TYPE:
			buildElementType();
			break;

		case IPropertyType.STRUCT_REF_TYPE:

			// A structure definition must be provided.

			if (details == null)
				throw new MetaDataException(new String[] { name },
						MetaDataException.DESIGN_EXCEPTION_MISSING_STRUCT_DEFN);

			// Look up a string name reference.

			if (details instanceof String) {
				StructureDefn structDefn = (StructureDefn) dd.getStructure(StringUtil.trimString((String) details));
				details = structDefn;
			}

			// the structure must be defined in the ReportDesign

			IElementDefn report = dd.getElement(ReportDesignConstants.REPORT_DESIGN_ELEMENT);
			List<IElementPropertyDefn> properties = report.getProperties();
			boolean isFound = false;
			for (int i = 0; i < properties.size(); i++) {
				IPropertyDefn property = properties.get(i);
				if (property.getTypeCode() == IPropertyType.STRUCT_TYPE) {
					if (property.getStructDefn() == getStructDefn()) {
						isFound = true;
						break;
					}
				}
			}
			if (!isFound)
				throw new MetaDataException(new String[] { name },
						MetaDataException.DESIGN_EXCEPTION_UNREFERENCABLE_STRUCT_DEFN);

			SemanticTriggerDefn triggerDefn = new SemanticTriggerDefn(StructureReferenceValidator.NAME);
			triggerDefn.setPropertyName(getName());
			triggerDefn.setValidator(StructureReferenceValidator.getInstance());
			getTriggerDefnSet().add(triggerDefn);

			break;

		case IPropertyType.LIST_TYPE:

			// list property must provide the subtype

			if (subType == null)
				throw new MetaDataException(new String[] { name }, MetaDataException.DESIGN_EXCEPTION_MISSING_SUB_TYPE);

			// check the subtype, not all simple types are supported;
			// furthermore, a structure member not support element-ref
			// subtype, just simple types, such as int, float and...
			if (this instanceof StructPropertyDefn && subType.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE)
				throw new MetaDataException(new String[] { name, subType.getName() },
						MetaDataException.DESIGN_EXCEPTION_UNSUPPORTED_SUB_TYPE);

			else if (!supportedSubTypes.contains(subType))
				throw new MetaDataException(new String[] { name, subType.getName() },
						MetaDataException.DESIGN_EXCEPTION_UNSUPPORTED_SUB_TYPE);

			// add the simple list validator

			SimpleListValidator validator = SimpleListValidator.getInstance();
			triggerDefn = new SemanticTriggerDefn(SimpleListValidator.NAME);
			triggerDefn.setPropertyName(getName());
			triggerDefn.setValidator(validator);
			getTriggerDefnSet().add(triggerDefn);

			// sub-type is element, then do some checks for it

			if (subType.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE)
				buildElementType();

			break;
		case IPropertyType.ELEMENT_TYPE:
		case IPropertyType.CONTENT_ELEMENT_TYPE:
			// must define detail types
			if (!(details instanceof List))
				throw new MetaDataException(new String[] { name, type.getName() },
						MetaDataException.DESIGN_EXCEPTION_MISSING_ELEMENT_TYPE);
			List<String> elementNames = (List<String>) details;
			if (elementNames.isEmpty())
				throw new MetaDataException(new String[] { name, type.getName() },
						MetaDataException.DESIGN_EXCEPTION_MISSING_ELEMENT_TYPE);
			List<ElementDefn> elementTypes = new ArrayList<ElementDefn>();
			for (int i = 0; i < elementNames.size(); i++) {
				String elementName = elementNames.get(i);
				ElementDefn type = (ElementDefn) dd.getElement(elementName);
				if (type == null)
					throw new MetaDataException(new String[] { elementName, name },
							MetaDataException.DESIGN_EXCEPTION_UNDEFINED_ELEMENT_TYPE);
				elementTypes.add(type);
			}
			details = elementTypes;
			break;

		}

		if (isValueRequired()) {
			SemanticTriggerDefn triggerDefn = new SemanticTriggerDefn(ValueRequiredValidator.NAME);
			triggerDefn.setPropertyName(getName());
			triggerDefn.setValidator(ValueRequiredValidator.getInstance());
			getTriggerDefnSet().add(triggerDefn);
		}

		if (tmpTypeCode != IPropertyType.LIST_TYPE && subType != null) {
			// only when the type is list, the subtype is set

			throw new MetaDataException(new String[] { name }, MetaDataException.DESIGN_EXCEPTION_SUB_TYPE_FORBIDDEN);
		}

		if (tmpTypeCode != IPropertyType.STRUCT_TYPE && tmpTypeCode != IPropertyType.ELEMENT_TYPE
				&& tmpTypeCode != IPropertyType.CONTENT_ELEMENT_TYPE && isList == true) {
			// only support list of structures.

			throw new MetaDataException(new String[] { getType().getName() },
					MetaDataException.DESIGN_EXCEPTION_INVALID_LIST_TYPE);
		}

		// if the property has a defalut value, validate it again. At this time,
		// it will be validated against the allowed choices.

		if (defaultValue != null) {
			try {
				validateXml(null, null, defaultValue.toString());
			} catch (PropertyValueException e) {
				throw new MetaDataException(new String[] { name, defaultValue.toString() },
						MetaDataException.DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE);
			}
		}

		// ensure that the validator is defined in the dictionary.

		if (valueValidator != null) {
			MetaDataDictionary dict = MetaDataDictionary.getInstance();
			if (dict.getValueValidator(valueValidator) == null)
				throw new MetaDataException(new String[] { valueValidator, name },
						MetaDataException.DESIGN_EXCEPTION_VALIDATOR_NOT_FOUND);
		}

		// default unit check

		if (tmpTypeCode == IPropertyType.DIMENSION_TYPE
				|| (tmpTypeCode == IPropertyType.LIST_TYPE && subType.getTypeCode() == IPropertyType.DIMENSION_TYPE)) {
			String defaultUnit = getDefaultUnit();
			if (!StringUtil.isBlank(defaultUnit)) {

				IChoiceSet units = getAllowedUnits();
				IChoice choice = units.findChoice(defaultUnit);
				if (choice == null) {
					setDefaultUnit(DimensionValue.DEFAULT_UNIT);
					throw new MetaDataException(new String[] { getName(), defaultUnit },
							MetaDataException.DESIGN_EXCEPTION_INVALID_UNIT);
				}
			}
		}

		buildTrimOption(tmpTypeCode);

	}

	/**
	 * Build trim option according to the type code. If the trim option value of the
	 * property is not defined in rom, the default value will be set according to
	 * the property type.
	 * 
	 * @param typeCode the type code.
	 */
	protected void buildTrimOption(int typeCode) {
		// if the trim option value of the property is not defined in rom, the
		// default value will be set according to the property type.
		if (trimOption == TextualPropertyType.NO_VALUE) {
			if (typeCode == IPropertyType.XML_TYPE || typeCode == IPropertyType.STRING_TYPE
					|| typeCode == IPropertyType.HTML_TYPE || typeCode == IPropertyType.URI_TYPE
					|| typeCode == IPropertyType.MEMBER_KEY_TYPE || typeCode == IPropertyType.NAME_TYPE) {
				setTrimOption(TextualPropertyType.TRIM_SPACE_VALUE | TextualPropertyType.TRIM_EMPTY_TO_NULL_VALUE);
			} else if (typeCode == IPropertyType.EXPRESSION_TYPE || typeCode == IPropertyType.SCRIPT_TYPE
					|| typeCode == IPropertyType.RESOURCE_KEY_TYPE) {
				setTrimOption(TextualPropertyType.TRIM_EMPTY_TO_NULL_VALUE);
			} else if (typeCode == IPropertyType.LITERAL_STRING_TYPE) {
				setTrimOption(TextualPropertyType.NO_TRIM_VALUE);
			}
		}
	}

	/**
	 * Builds the semantic checks for the element reference type.
	 * 
	 * @throws MetaDataException
	 */

	private void buildElementType() throws MetaDataException {
		if (details == null)
			throw new MetaDataException(new String[] { name, type.getName() },
					MetaDataException.DESIGN_EXCEPTION_MISSING_ELEMENT_TYPE);

		// Look up a string name reference.

		if (details instanceof String) {
			MetaDataDictionary dd = MetaDataDictionary.getInstance();
			ElementDefn elementDefn = (ElementDefn) dd.getElement(StringUtil.trimString((String) details));
			// the detail can not be 'extended-item'
			if (elementDefn == null || ReportDesignConstants.EXTENDED_ITEM.equalsIgnoreCase((String) details))
				throw new MetaDataException(new String[] { (String) details, name },
						MetaDataException.DESIGN_EXCEPTION_UNDEFINED_ELEMENT_TYPE);
			if (elementDefn.getNameSpaceID() == MetaDataConstants.NO_NAME_SPACE)
				throw new MetaDataException(new String[] { (String) details, name },
						MetaDataException.DESIGN_EXCEPTION_UNNAMED_ELEMENT_TYPE);
			details = elementDefn;
		}

		// Otherwise, an element definition must be provided.

		else if (getTargetElementType() == null)
			throw new MetaDataException(new String[] { name, type.getName() },
					MetaDataException.DESIGN_EXCEPTION_MISSING_ELEMENT_TYPE);

		if (!name.equalsIgnoreCase(IStyledElementModel.STYLE_PROP)) {
			SemanticTriggerDefn triggerDefn = new SemanticTriggerDefn(ElementReferenceValidator.NAME);
			triggerDefn.setPropertyName(getName());
			triggerDefn.setValidator(ElementReferenceValidator.getInstance());
			getTriggerDefnSet().add(triggerDefn);
		}
	}

	/**
	 * Determines whether this is a system-defined property. Must be overridden by
	 * derived classes.
	 * 
	 * @return true if a system-defined property, otherwise false
	 */

	public boolean isSystemProperty() {
		return getValueType() == SYSTEM_PROPERTY;
	}

	/**
	 * Determines whether this is a user-defined property.
	 * 
	 * @return True if a user-defined property
	 */

	public boolean isUserProperty() {
		return getValueType() == USER_PROPERTY;
	}

	/**
	 * Determines whether this is a structure member. The subclass will override
	 * this method if necessary.
	 * 
	 * @return true if a structure member, otherwise false
	 * @see StructPropertyDefn
	 */

	public boolean isStructureMember() {
		return false;
	}

	/**
	 * Returns the internal name for the property.
	 * 
	 * @return the internal (non-localized) name for the property
	 */

	public String getName() {
		return name;
	}

	/**
	 * Returns the property type. See the list in MetaDataConstants.
	 * 
	 * @return he property type code
	 */

	public int getTypeCode() {
		assert type != null;
		return type.getTypeCode();
	}

	/**
	 * Gets the property type object for this property.
	 * 
	 * @return the property type object
	 */

	public PropertyType getType() {
		return type;
	}

	/**
	 * Checks whether <code>value</code> exists in the choice set for an extended
	 * choice property type. If <code>value</code> exists in the choice set, return
	 * this value. Otherwise, return null.
	 * 
	 * @param value the candidate value
	 * @return the internal choice name if found. Otherwise, return
	 *         <code>null</code>.
	 */

	private String validateExtendedChoicesByName(Object value) {
		if (value == null || hasChoices() == false)
			return null;

		IChoiceSet choiceSet = getChoices();
		IChoice choice = choiceSet.findChoice(value.toString());

		if (choice != null)
			return choice.getName();

		return null;
	}

	/**
	 * Checks whether <code>displayName</code> matches any items in the choice set
	 * for an extended choice property type. If <code>displayName</code> exists in
	 * the choice set, return the name of this choice. Otherwise, return
	 * <code>null</code>.
	 * 
	 * @param module      the report design
	 * @param displayName the candidate display name
	 * @return the choice name if found. Otherwise, return <code>null</code>.
	 */

	protected String validateExtendedChoicesByDisplayName(Module module, String displayName) {
		if (displayName == null || hasChoices() == false)
			return null;

		IChoiceSet choiceSet = getChoices();
		IChoice choice = choiceSet.findChoiceByDisplayName(displayName);

		if (choice != null)
			return choice.getName();

		return null;
	}

	/**
	 * Validates a value to be stored for this value definition. This method checks
	 * names of choice properties first. Then, checks display names of choice
	 * properties. Then uses type to validate value.
	 * 
	 * @param module the report design
	 * @param value  the candidate value
	 * @return the translated value to be stored
	 * @throws PropertyValueException if the value is not valid
	 */

	public Object validateValue(Module module, DesignElement element, Object value) throws PropertyValueException {

		Object retValue = null;

		// Validates from extended choices.

		if (hasChoices() && getTypeCode() != IPropertyType.CHOICE_TYPE) {
			retValue = validateExtendedChoicesByName(value);

			if (retValue == null && value != null)
				retValue = validateExtendedChoicesByDisplayName(module, value.toString());

			if (retValue != null)
				return retValue;
		}

		retValue = doValidateValueWithExpression(module, element, type, value);

		// Per-property validations using a specific validator.

		if (valueValidator != null)
			MetaDataDictionary.getInstance().getValueValidator(valueValidator).validate(module, this, retValue);

		return retValue;
	}

	/**
	 * Checks whether the value is Expression and validate the value accordingly. If
	 * the value is an expression and its type is "constant", extract the raw value
	 * and do the validation.
	 * 
	 * @param module  the root
	 * @param tmpType the property type to validate
	 * @param value   the value to validate
	 * @return the validated value
	 * @throws PropertyValueException
	 */

	protected Object doValidateValueWithExpression(Module module, DesignElement element, PropertyType tmpType,
			Object value) throws PropertyValueException {
		Object[] tmpValues = getCompatibleTypeAndValue(tmpType, value);

		Object retValue = ((PropertyType) tmpValues[0]).validateValue(module, element, this, tmpValues[1]);

		return pushBackExpressionValues(value, retValue);
	}

	/**
	 * Validates an XML value to be stored for this value definition. This method
	 * checks names of predefined choice properties first. Then uses type to
	 * validate value. If the property definition has a validator, uses this
	 * validator to validate the value.
	 * 
	 * @param module the report design
	 * @param value  the candidate value
	 * @return the translated value to be stored
	 * @throws PropertyValueException if the value is not valid
	 */

	public Object validateXml(Module module, DesignElement element, Object value) throws PropertyValueException {
		Object retValue = null;

		// Validates from extended choices.

		if (hasChoices() && getTypeCode() != IPropertyType.CHOICE_TYPE) {
			retValue = validateExtendedChoicesByName(value);

			if (retValue != null)
				return retValue;
		}

		// Property type validation

		retValue = doValidateXMLWithExpression(module, element, value);

		// Per-property validations using a specific validator.

		if (valueValidator != null)
			MetaDataDictionary.getInstance().getValueValidator(valueValidator).validate(module, this, retValue);

		return retValue;
	}

	/**
	 * Checks whether the value is Expression and validate the value accordingly. If
	 * the value is an expression and its type is "constant", extract the raw value
	 * and do the validation.
	 * 
	 * @param module  the root
	 * @param tmpType the property type to validate
	 * @param value   the value to validate
	 * @return the validated value
	 * @throws PropertyValueException
	 */

	private Object doValidateXMLWithExpression(Module module, DesignElement element, Object value)
			throws PropertyValueException {
		Object[] tmpValues = getCompatibleTypeAndValue(type, value);

		Object retValue = ((PropertyType) tmpValues[0]).validateXml(module, element, this, tmpValues[1]);

		return pushBackExpressionValues(value, retValue);
	}

	/**
	 * Returns the display name for the property.
	 * 
	 * @return the user-visible, localized display name for the property
	 */

	public String getDisplayName() {
		if (displayNameID != null) {
			String displayName = null;
			if (messages == null) {
				displayName = ModelMessages.getMessage(this.displayNameID);
			} else {
				ULocale locale = ThreadResources.getLocale();
				displayName = messages.getMessage(displayNameID, locale);
			}
			if (displayName != null) {
				return displayName;
			}
		}
		return name;
	}

	/**
	 * Sets the internal name of the property.
	 * 
	 * @param theName the internal property name
	 */

	public void setName(String theName) {
		name = theName;
	}

	/**
	 * Gets the list of choices for the property.
	 * 
	 * @return the list of choices
	 */

	public IChoiceSet getChoices() {
		if (details instanceof ChoiceSet)
			return (ChoiceSet) details;
		return null;
	}

	/**
	 * Checks if a property has a set of choices whatever choice is choice, extended
	 * choice or user defined choice.
	 * 
	 * @return true if it has, otherwise false.
	 */

	public boolean hasChoices() {
		return getChoices() != null;
	}

	/**
	 * Returns the message id for the display name.
	 * 
	 * @return The display name message ID.
	 */

	public String getDisplayNameID() {
		return displayNameID;
	}

	/**
	 * Sets the detailed information for the property.
	 * <p>
	 * <ul>
	 * <li>Choice: details holds an object of type <code>ChoiceSet</code> that holds
	 * the list of available choices.</li>
	 * <li>Element Ref: details holds an object of type <code>ElementDefn</code>
	 * that identifies the type of element that can be referenced.</li>
	 * <li>Structure List: details holds an object of type
	 * <code>StructureDefn</code> that defines the structures in the list.</li>
	 * </ul>
	 * 
	 * @param obj the details object to set
	 */

	public void setDetails(Object obj) {
		details = obj;
	}

	public Object getDetails() {
		return details;
	}

	/**
	 * Sets the message ID for the display name.
	 * 
	 * @param id message ID for the display name
	 */

	public void setDisplayNameID(String id) {
		displayNameID = id;
	}

	/**
	 * Gets the XML value for a value of this type.
	 * 
	 * This method checks the predefined choice properties first. If has not, then
	 * uses type to return the value.
	 * 
	 * @param module the report design
	 * @param value  the internal value
	 * @return the XML value string
	 */

	public String getXmlValue(Module module, Object value) {
		if (value == null)
			return null;

		Object[] tmps = getCompatibleTypeAndValue(type, value);

		String retValue = validateExtendedChoicesByName(tmps[1]);
		if (retValue != null)
			return retValue;

		return ((PropertyType) tmps[0]).toXml(module, this, tmps[1]);
	}

	/**
	 * Returns a value as a locale independent string.
	 * 
	 * @param module the report design
	 * @param value  the internal value
	 * @return the XML value string
	 */

	public String getStringValue(Module module, Object value) {
		Object[] tmps = getCompatibleTypeAndValue(type, value);

		return ((PropertyType) tmps[0]).toString(module, this, tmps[1]);

	}

	/**
	 * Returns a value as a <code>double</code>.
	 * 
	 * Uses type to return the value.
	 * 
	 * @param module the module
	 * @param value  the internal value
	 * @return the value as <code>double</code>
	 */

	public double getFloatValue(Module module, Object value) {
		Object[] tmps = getCompatibleTypeAndValue(type, value);

		return ((PropertyType) tmps[0]).toDouble(module, tmps[1]);
	}

	/**
	 * Returns a value as a <code>int</code>.
	 * 
	 * Uses type to return the value.
	 * 
	 * @param module the module
	 * @param value  the internal value
	 * @return the value as <code>int</code>
	 */

	public int getIntValue(Module module, Object value) {
		Object[] tmps = getCompatibleTypeAndValue(type, value);

		return ((PropertyType) tmps[0]).toInteger(module, tmps[1]);
	}

	/**
	 * Returns a value as a <code>BigDecimal</code>.
	 * 
	 * Uses type to return the value.
	 * 
	 * @param module the module
	 * @param value  the internal value
	 * @return the value as <code>BigDecimal</code>
	 */

	public BigDecimal getNumberValue(Module module, Object value) {
		Object[] tmps = getCompatibleTypeAndValue(type, value);

		return ((PropertyType) tmps[0]).toNumber(module, tmps[1]);
	}

	/**
	 * Returns a value as a <code>boolean</code>.
	 * 
	 * Uses type to return the value.
	 * 
	 * @param module the module
	 * @param value  the internal value
	 * @return the value as <code>boolean</code>
	 */

	public boolean getBooleanValue(Module module, Object value) {
		Object[] tmps = getCompatibleTypeAndValue(type, value);

		return ((PropertyType) tmps[0]).toBoolean(module, tmps[1]);
	}

	/**
	 * Returns the localized string value of a property.
	 * 
	 * @param module the report design
	 * @param value  the internal value
	 * @return the property as a localized string
	 */

	public String getDisplayValue(Module module, Object value) {
		if (value == null)
			return null;

		Object[] tmps = getCompatibleTypeAndValue(type, value);

		String retValue = validateExtendedChoicesByName(tmps[1]);
		if (retValue == null)
			return ((PropertyType) tmps[0]).toDisplayString(module, this, tmps[1]);

		return getChoices().findChoice(tmps[1].toString()).getDisplayName();
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
	 * Returns the structure definition for this value.
	 * 
	 * @return the structure definition, or null if this value is not a list of
	 *         structures
	 */

	public IStructureDefn getStructDefn() {
		if (details instanceof StructureDefn)
			return (StructureDefn) details;
		return null;
	}

	/**
	 * Returns the default value for the property.
	 * 
	 * @return The default value.
	 */

	public Object getDefault() {
		return defaultValue;
	}

	/**
	 * Sets the default value for the property.
	 * 
	 * @param value The default value to set.
	 */

	protected void setDefault(Object value) {
		defaultValue = value;
	}

	/**
	 * Indicates whether the property is intrinsic or not. An intrinsic property is
	 * a system one represented by a member variable.
	 * 
	 * @return true if the property is intrinsic, false if it is a "normal" property
	 */

	public boolean isIntrinsic() {
		return intrinsic;
	}

	/**
	 * Sets the property as intrinsic.
	 * 
	 * @param flag true if the property is intrinsic, false otherwise
	 */

	void setIntrinsic(boolean flag) {
		intrinsic = flag;
	}

	/**
	 * Return the element type associated with this property.
	 * 
	 * @return the element type associated with the property
	 */

	public IElementDefn getTargetElementType() {
		if (details instanceof ElementDefn)
			return (ElementDefn) details;
		return null;
	}

	/**
	 * Returns the allowed choices for this property. It contains allowed choices
	 * for a choice type, or containing an allowed units set for a dimension type.
	 * <p>
	 * If a property has not defined the restriction, then whole set will be
	 * returned.
	 * 
	 * @return Returns the allowed choices of this property.
	 */

	public IChoiceSet getAllowedChoices() {
		if (allowedChoices != null)
			return allowedChoices;

		return getChoices();
	}

	/**
	 * Sets the allowed choices for this property
	 * 
	 * @param allowedChoices The allowed choices to set.
	 */

	void setAllowedChoices(ChoiceSet allowedChoices) {
		this.allowedChoices = allowedChoices;
	}

	/**
	 * Sets the allowed choices for this property
	 * 
	 * @param allowedUnits The allowed choices to set.
	 */

	void setAllowedUnits(ChoiceSet allowedUnits) {
		this.allowedUnits = allowedUnits;
	}

	/**
	 * Returns the allowed choices for this property. It contains allowed choices
	 * for a choice type, or containing an allowed units set for a dimension type.
	 * <p>
	 * If a property has not defined the restriction, then whole set will be
	 * returned.
	 * 
	 * @return Returns the allowed choices of this property.
	 */

	public IChoiceSet getAllowedUnits() {
		if (allowedUnits != null)
			return allowedUnits;

		return MetaDataDictionary.getInstance().getChoiceSet(DesignChoiceConstants.CHOICE_UNITS);
	}

	/**
	 * Set a validator.
	 * 
	 * @param validator
	 */

	void setValueValidator(String validator) {
		this.valueValidator = validator;
	}

	/**
	 * Indicates whether the property is defined by the extended element.
	 * 
	 * @return true if the property is defined by the extended element, false if the
	 *         property is BIRT system-defined or user-defined
	 */

	public boolean isExtended() {
		return getValueType() == EXTENSION_MODEL_PROPERTY || getValueType() == EXTENSION_PROPERTY;
	}

	/**
	 * Indicates whether this property is a list. It is useful only when the
	 * property type is a structure type.
	 * 
	 * @return whether the property is a list or not.
	 */

	public boolean isList() {
		return isList;
	}

	/**
	 * Set if the property is a list.
	 * 
	 * @param isList whether the property is a list or not.
	 */

	protected void setIsList(boolean isList) {
		this.isList = isList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.ISemanticTriggerProvider#
	 * getTriggerDefnSet()
	 */
	public SemanticTriggerDefnSet getTriggerDefnSet() {
		if (triggers == null)
			triggers = new SemanticTriggerDefnSet();

		return triggers;
	}

	/**
	 * Returns whether the value of this property is required. Generally, this flag
	 * is not applied for style property. That means the value of style property is
	 * not required anyway.
	 * 
	 * @return <code>true</code>, if the value of this property is required.
	 */

	public boolean isValueRequired() {
		return valueRequired;
	}

	/**
	 * Sets the flag for indicating whether the value of this property is required.
	 * 
	 * @param valueRequired the flag to set
	 */

	void setValueRequired(boolean valueRequired) {
		this.valueRequired = valueRequired;
	}

	/**
	 * Gets the default unit if the property is dimension type. The default unit of
	 * dimension property type can not be null or empty string, it must be an
	 * effective unit string.
	 * 
	 * @return the default unit if the property is dimension type, otherwise empty
	 *         string
	 */

	public String getDefaultUnit() {
		if (getTypeCode() != IPropertyType.DIMENSION_TYPE)
			return DimensionValue.DEFAULT_UNIT;

		return defaultUnit;
	}

	/**
	 * Sets the default unit of the dimension property.
	 * 
	 * @param defaultUnit the default unit to set
	 */

	void setDefaultUnit(String defaultUnit) {
		assert getTypeCode() == IPropertyType.DIMENSION_TYPE;
		this.defaultUnit = defaultUnit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#isEncrypted()
	 */

	public boolean isEncryptable() {
		return isEncryptable;
	}

	/**
	 * Sets this property encrypted or not.
	 * 
	 * @param isEncryptable flag indicating whether this property is encrypted.
	 */

	void setIsEncryptable(boolean isEncryptable) {
		this.isEncryptable = isEncryptable;
	}

	/**
	 * Set the release in which this object was introduced.
	 * 
	 * @param value the release value
	 */

	public void setSince(String value) {
		if (!StringUtil.isBlank(value))
			since = value;
	}

	/**
	 * @return the release in which this object was introduced. A value of "none"
	 *         means that the feature is experimental and is not yet released.
	 */

	public String getSince() {
		return since;
	}

	/**
	 * Set the indication of whether this property can be set at runtime.
	 * 
	 * @param flag true if it can be set, false if it is read-only
	 */

	public void setRuntimeSettable(boolean flag) {
		runtimeSettable = flag;
	}

	/**
	 * Indicates whether this property can be set at runtime.
	 * 
	 * @return true if it can be set, false if it is read-only
	 */

	public boolean isRuntimeSettable() {
		return runtimeSettable;
	}

	/**
	 * Set the trim option value.
	 * 
	 * @param value trim option value.
	 */
	protected void setTrimOption(int trimOption) {
		this.trimOption = trimOption;
	}

	/**
	 * Gets the trim option value.
	 * 
	 * @return trim option value.
	 */
	int getTrimOption() {
		return trimOption;
	}

	/**
	 * Set the context for a method or expression.
	 * 
	 * @param value the context to set
	 */
	public void setContext(String value) {
		context = value;
	}

	/**
	 * Return the context for a method or expression.
	 * 
	 * @return the expression or method context
	 */

	public String getContext() {
		return context;
	}

	/**
	 * Sets the return type of an expression or method.
	 * 
	 * @param type the return type to set
	 */

	public void setReturnType(String type) {
		returnType = type;
	}

	/**
	 * Returns the return type of an expression or method. A null type for an
	 * expression means that return type is any type. A null type for a method means
	 * that the method does not return anything.
	 * 
	 * @return the method or property return type
	 */

	public String getReturnType() {
		return returnType;
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

	/**
	 * Sets the sub-type of this property definition.
	 * 
	 * @param subType the sub-type to set
	 */

	void setSubType(PropertyType subType) {
		this.subType = subType;
	}

	/**
	 * Gets the sub-type of this property definition.
	 * 
	 * @return the sub-type of this property definition
	 */

	public PropertyType getSubType() {
		return subType;
	}

	/**
	 * Gets the sub-type code of this property definition. This method returns an
	 * effective type code only when the type is list; otherwise return
	 * <code>-1</code>.
	 * 
	 * @return the sub-type code of this property defintion
	 */

	public int getSubTypeCode() {
		if (subType != null)
			return subType.getTypeCode();
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getAllowedElements
	 * ()
	 */
	public List<IElementDefn> getAllowedElements() {
		return getAllowedElements(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.IContainerDefn#getAllowedElements
	 * (boolean)
	 */
	public List<IElementDefn> getAllowedElements(boolean extractExtensions) {
		if (details instanceof List && isElementType()) {
			// if not extract extension definitions, return details directly
			if (!extractExtensions)
				return Collections.unmodifiableList((List<IElementDefn>) details);

			// extract is true, then build extension definitions
			List<IElementDefn> allowedElements = (List<IElementDefn>) details;
			MetaDataDictionary dd = MetaDataDictionary.getInstance();
			IElementDefn extendItem = dd.getElement(ReportDesignConstants.EXTENDED_ITEM);

			ArrayList<IElementDefn> contentsWithExtensions = new ArrayList<IElementDefn>();
			contentsWithExtensions.addAll(allowedElements);

			if (allowedElements.contains(extendItem)) {
				contentsWithExtensions.remove(extendItem);

				for (int i = 0; i < dd.getExtensions().size(); i++) {
					ExtensionElementDefn extension = (ExtensionElementDefn) dd.getExtensions().get(i);
					if (extension.isKindOf(dd.getElement(ReportDesignConstants.REPORT_ITEM))
							&& PeerExtensionLoader.EXTENSION_POINT.equals(extension.extensionPoint)
							&& !contentsWithExtensions.contains(extension))
						contentsWithExtensions.add(extension);
				}
			}
			return Collections.unmodifiableList(contentsWithExtensions);
		}

		return Collections.emptyList();
	}

	/**
	 * Determines if this property can contain an element of the given type.
	 * 
	 * @param type the type to test
	 * @return true if the property can contain the type, false otherwise
	 */

	public final boolean canContain(IElementDefn type) {
		if (type == null)
			return false;

		List<IElementDefn> contentElements = getAllowedElements();
		assert contentElements != null;
		Iterator<IElementDefn> iter = contentElements.iterator();
		while (iter.hasNext()) {
			ElementDefn element = (ElementDefn) iter.next();

			// if element is not "extended-item", then do no conversion
			if (!ReportDesignConstants.EXTENDED_ITEM.equals(element.getName())) {
				if (type.isKindOf(element))
					return true;
			} else {
				// if element is "extended-item", then the type must be an
				// extension of "reportItemModel" and is kind of ReportItem.
				if (type instanceof ExtensionElementDefn) {
					ExtensionElementDefn extensionDefn = (ExtensionElementDefn) type;
					if (PeerExtensionLoader.EXTENSION_POINT.equals(extensionDefn.getExtensionPoint()) && extensionDefn
							.isKindOf(MetaDataDictionary.getInstance().getElement(ReportDesignConstants.REPORT_ITEM)))
						return true;
				}

				// type is "ExtendedItem" itself
				if (ReportDesignConstants.EXTENDED_ITEM.equals(type.getName()))
					return true;

			}
		}
		return false;
	}

	/**
	 * Determines if an element can reside within this property.
	 * 
	 * @param content the design element to check
	 * @return true if the element can reside in the property, false otherwise
	 */

	public final boolean canContain(DesignElement content) {
		return canContain(content.getDefn());
	}

	/**
	 * Checks whether the property type is a kind of element types.
	 * 
	 * @return <code>true</code> if the type is element/content element type.
	 *         Otherwise <code>false</code>.
	 */

	public final boolean isElementType() {
		int typeCode = getTypeCode();
		if (typeCode == IPropertyType.ELEMENT_TYPE || typeCode == IPropertyType.CONTENT_ELEMENT_TYPE)
			return true;

		return false;
	}

	/**
	 * Checks whether the property type is a kind of element types.
	 * 
	 * @return <code>true</code> if the type is element/content element type.
	 *         Otherwise <code>false</code>.
	 */

	public final boolean isListType() {
		if (isList)
			return true;

		int typeCode = type.getTypeCode();
		return typeCode == IPropertyType.LIST_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#allowExpression
	 * ()
	 */

	public boolean allowExpression() {
		return allowExpression || getTypeCode() == IPropertyType.EXPRESSION_TYPE
				|| getSubTypeCode() == IPropertyType.EXPRESSION_TYPE;
	}

	/**
	 * Sets the flag to indicate whether the property can be set with the expression
	 * value.
	 * 
	 * @param allowExpression the allowExpression to set
	 */

	void setAllowExpression(boolean allowExpression) {
		this.allowExpression = allowExpression;
	}

	/**
	 * Returns the correct type and the value for validation or the conversion.
	 * 
	 * @param tmpType the property type. For the list property type, should be
	 *                subType.
	 * @param value   the value to validate or convert
	 * @return a 2-item array. The first item is the property type and 2nd is the
	 *         value
	 */

	public Object[] getCompatibleTypeAndValue(PropertyType tmpType, Object value) {
		Object[] retValue = new Object[2];

		retValue[0] = tmpType;
		retValue[1] = value;

		// if the type is not expression and allowExpression = true, check the
		// value.

		if (allowExpression && ((PropertyType) retValue[0]).getTypeCode() != IPropertyType.EXPRESSION_TYPE
				&& value instanceof Expression) {
			if (!ExpressionType.CONSTANT.equalsIgnoreCase(((Expression) value).getUserDefinedType())) {
				retValue[0] = expressionType;
			} else {
				retValue[1] = ((Expression) value).getExpression();
			}

		}
		return retValue;
	}

	/**
	 * @param value
	 * @param validated
	 * @return
	 */

	private Object pushBackExpressionValues(Object value, Object validated) {
		// for example, if the type is integer and the type is constant, need to
		// update the input expression.

		Object retValue = validated;

		if (allowExpression && type.getTypeCode() != IPropertyType.EXPRESSION_TYPE && value instanceof Expression) {
			Expression tmpValue = (Expression) value;
			if (validated == null && tmpValue.getUserDefinedType() == null)
				retValue = null;
			else if (!(validated instanceof Expression))
				retValue = new Expression(validated, tmpValue.getUserDefinedType());
		}

		return retValue;
	}

	public void setMessages(IMessages messages) {
		this.messages = messages;
	}

	public IMessages getMessages() {
		return messages;
	}

	public void setNameConfig(NameConfig nameConfig) {
		this.nameConfig = nameConfig;
	}

	public NameConfig getNameConfig() {
		return nameConfig;
	}
}
