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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.validators.SimpleValueValidator;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.ErrorHandler;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.eclipse.birt.report.model.validators.AbstractSemanticValidator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * SAX handler for reading the XML meta data definition file.
 */

public class MetaDataHandlerImpl extends XMLParserHandler {

	/**
	 * Cache the singleton instance of the meta-data dictionary.
	 */
	MetaDataDictionary dictionary = MetaDataDictionary.getInstance();

	protected static final String PROPERTY_TAG = "Property"; //$NON-NLS-1$
	protected static final String ELEMENT_TAG = "Element"; //$NON-NLS-1$
	protected static final String NAME_ATTRIB = "name"; //$NON-NLS-1$
	protected static final String METHOD_TAG = "Method"; //$NON-NLS-1$
	protected static final String PROPERTY_GROUP_TAG = "PropertyGroup"; //$NON-NLS-1$

	private static final String ROOT_TAG = "ReportMetaData"; //$NON-NLS-1$
	private static final String STYLE_TAG = "Style"; //$NON-NLS-1$
	private static final String STYLE_PROPERTY_TAG = "StyleProperty"; //$NON-NLS-1$
	private static final String SLOT_TAG = "Slot"; //$NON-NLS-1$
	private static final String TYPE_TAG = "Type"; //$NON-NLS-1$
	private static final String DEFAULT_TAG = "Default"; //$NON-NLS-1$
	private static final String CHOICE_TAG = "Choice"; //$NON-NLS-1$
	private static final String CHOICE_TYPE_TAG = "ChoiceType"; //$NON-NLS-1$
	private static final String STRUCTURE_TAG = "Structure"; //$NON-NLS-1$
	private static final String ALLOWED_TAG = "Allowed"; //$NON-NLS-1$
	private static final String ALLOWED_UNITS_TAG = "AllowedUnits"; //$NON-NLS-1$
	private static final String MEMBER_TAG = "Member"; //$NON-NLS-1$
	private static final String VALUE_VALIDATOR_TAG = "ValueValidator"; //$NON-NLS-1$
	private static final String VALIDATORS_TAG = "Validators"; //$NON-NLS-1$
	private static final String ARGUMENT_TAG = "Argument"; //$NON-NLS-1$
	private static final String CLASS_TAG = "Class"; //$NON-NLS-1$
	private static final String CONSTRUCTOR_TAG = "Constructor"; //$NON-NLS-1$
	private static final String SEMANTIC_VALIDATOR_TAG = "SemanticValidator"; //$NON-NLS-1$
	private static final String TRIGGER_TAG = "Trigger"; //$NON-NLS-1$
	private static final String DEFAULT_UNIT_TAG = "DefaultUnit"; //$NON-NLS-1$
	private static final String PROPERTY_VISIBILITY_TAG = "PropertyVisibility"; //$NON-NLS-1$

	private static final String DISPLAY_NAME_ID_ATTRIB = "displayNameID"; //$NON-NLS-1$
	private static final String EXTENDS_ATTRIB = "extends"; //$NON-NLS-1$
	private static final String TYPE_ATTRIB = "type"; //$NON-NLS-1$
	private static final String SUB_TYPE_ATTRIB = "subType"; //$NON-NLS-1$
	private static final String HAS_STYLE_ATTRIB = "hasStyle"; //$NON-NLS-1$
	private static final String SELECTOR_ATTRIB = "selector"; //$NON-NLS-1$
	private static final String ALLOWS_USER_PROPERTIES_ATTRIB = "allowsUserProperties"; //$NON-NLS-1$
	private static final String CAN_EXTEND_ATTRIB = "canExtend"; //$NON-NLS-1$
	private static final String MULTIPLE_CARDINALITY_ATTRIB = "multipleCardinality"; //$NON-NLS-1$
	private static final String IS_MANAGED_BY_NAME_SPACE_ATTRIB = "isManagedByNameSpace"; //$NON-NLS-1$
	private static final String CAN_INHERIT_ATTRIBUTE = "canInherit"; //$NON-NLS-1$
	private static final String IS_INTRINSIC_ATTRIB = "isIntrinsic"; //$NON-NLS-1$
	private static final String IS_STYLE_PROPERTY_ATTRIB = "isStyleProperty"; //$NON-NLS-1$
	private static final String IS_LIST_ATTRIB = "isList"; //$NON-NLS-1$
	private static final String NAME_SPACE_ATTRIB = "nameSpace"; //$NON-NLS-1$
	private static final String IS_NAME_REQUIRED_ATTRIB = "isNameRequired"; //$NON-NLS-1$
	private static final String IS_ABSTRACT_ATTRIB = "isAbstract"; //$NON-NLS-1$
	private static final String DETAIL_TYPE_ATTRIB = "detailType"; //$NON-NLS-1$
	private static final String JAVA_CLASS_ATTRIB = "javaClass"; //$NON-NLS-1$
	private static final String TOOL_TIP_ID_ATTRIB = "toolTipID"; //$NON-NLS-1$
	private static final String RETURN_TYPE_ATTRIB = "returnType"; //$NON-NLS-1$
	private static final String TAG_ID_ATTRIB = "tagID"; //$NON-NLS-1$
	private static final String DATA_TYPE_ATTRIB = "dataType"; //$NON-NLS-1$
	private static final String IS_STATIC_ATTRIB = "isStatic"; //$NON-NLS-1$
	private static final String VALIDATOR_ATTRIB = "validator"; //$NON-NLS-1$
	private static final String CLASS_ATTRIB = "class"; //$NON-NLS-1$
	private static final String NATIVE_ATTRIB = "native"; //$NON-NLS-1$
	private static final String PRE_REQUISITE_ATTRIB = "preRequisite"; //$NON-NLS-1$
	private static final String TARGET_ELEMENT_ATTRIB = "targetElement"; //$NON-NLS-1$
	private static final String VALUE_REQUIRED_ATTRIB = "valueRequired"; //$NON-NLS-1$
	private static final String PROPERTY_VISIBILITY_ATTRIB = "visibility"; //$NON-NLS-1$
	private static final String SINCE_ATTRIB = "since"; //$NON-NLS-1$
	private static final String XML_NAME_ATTRIB = "xmlName"; //$NON-NLS-1$
	private static final String RUNTIME_SETTABLE_ATTRIB = "runtimeSettable"; //$NON-NLS-1$
	private static final String TRIM_OPTION_ATTRIB = "trimOption"; //$NON-NLS-1$
	private static final String CONTEXT_ATTRIB = "context"; //$NON-NLS-1$
	private static final String MODULES_ATTRIB = "modules"; //$NON-NLS-1$
	private static final String IS_BIDI_PROPERTY_ATTRIB = "isBidiProperty"; //$NON-NLS-1$
	private static final String ALLOW_EXPRESSION_ATTRIB = "allowExpression"; //$NON-NLS-1$

	/**
	 * The unique id for the slot.
	 */

	private static final String ID_ATTRIB = "id"; //$NON-NLS-1$

	private static final String THIS_KEYWORD = "this"; //$NON-NLS-1$

	private String groupNameID;

	// Cached state. Can be done here because nothing in this grammar is
	// recursive.

	protected ElementDefn elementDefn = null;
	protected SlotDefn slotDefn = null;
	protected SystemPropertyDefn propDefn = null;
	protected StructureDefn struct = null;
	protected ArrayList<Choice> choices = new ArrayList<>();

	/**
	 * The input string will not be trimmed.
	 */
	private static final String NO_TRIM = "noTrim"; //$NON-NLS-1$

	/**
	 * The space will be trimmed.
	 */
	private static final String TRIM_SPACE = "trimSpace"; //$NON-NLS-1$

	/**
	 * If the input string is empty, normalizes the string to an null string.
	 */
	private static final String TRIM_EMPTY_TO_NULL = "trimEmptyToNull"; //$NON-NLS-1$

	/**
	 * if the display ID could be empty
	 */
	protected boolean checkDisplayNameID = true;

	protected MetaDataBuilder builder;

	/**
	 * Constructor.
	 */

	public MetaDataHandlerImpl() {
		this(new MetaDataErrorHandler(), new MetaDataBuilder());
	}

	/**
	 * Constructs the meta data handler implementation with the specified error
	 * handler.
	 *
	 * @param errorHandler
	 * @param builder
	 */
	public MetaDataHandlerImpl(ErrorHandler errorHandler, MetaDataBuilder builder) {
		super(errorHandler);
		this.builder = builder;
	}

	@Override
	public AbstractParseState createStartState() {
		return new StartState();
	}

	/**
	 * Convert the array list of choices to an array.
	 *
	 * @return an array of the choices.
	 */

	private Choice[] getChoiceArray() {
		Choice[] choiceArray = new Choice[choices.size()];
		for (int i = 0; i < choices.size(); i++) {
			choiceArray[i] = choices.get(i);
		}
		return choiceArray;
	}

	class StartState extends InnerParseState {

		@Override
		public AbstractParseState startElement(String tagName) {
			if (tagName.equalsIgnoreCase(ROOT_TAG)) {
				return new RootState();
			}
			return super.startElement(tagName);
		}
	}

	class RootState extends InnerParseState {

		@Override
		public AbstractParseState startElement(String tagName) {
			if (CHOICE_TYPE_TAG.equalsIgnoreCase(tagName)) {
				return new ChoiceTypeState();
			}
			if (STRUCTURE_TAG.equalsIgnoreCase(tagName)) {
				return new StructDefnState();
			}
			if (ELEMENT_TAG.equalsIgnoreCase(tagName)) {
				return new ElementDefnState();
			}
			if (STYLE_TAG.equalsIgnoreCase(tagName)) {
				return new StyleState();
			}
			if (CLASS_TAG.equalsIgnoreCase(tagName)) {
				return new ClassState();
			}
			if (VALIDATORS_TAG.equalsIgnoreCase(tagName)) {
				return new ValidatorsState();
			}
			return super.startElement(tagName);
		}
	}

	public class ChoiceTypeState extends InnerParseState {

		ChoiceSet choiceSet = null;

		@Override
		public void parseAttrs(Attributes attrs) throws XMLParserException {
			choices.clear();
			String name = attrs.getValue(NAME_ATTRIB);
			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
			} else {
				choiceSet = builder.createChoiceSet();
				choiceSet.setName(name);

				try {
					builder.addChoiceSet(choiceSet);
				} catch (MetaDataException e) {
					choiceSet = null;
					errorHandler.semanticError(e);
				}
			}
		}

		@Override
		public AbstractParseState startElement(String tagName) {
			if (tagName.equalsIgnoreCase(CHOICE_TAG)) {
				return new ChoiceState();
			}
			return super.startElement(tagName);
		}

		@Override
		public void end() throws SAXException {
			if (!choices.isEmpty() && choiceSet != null) {
				choiceSet.setChoices(getChoiceArray());
			}
		}
	}

	protected class StyleState extends InnerParseState {

		@Override
		public void parseAttrs(Attributes attrs) throws XMLParserException {
			String name = attrs.getValue(NAME_ATTRIB);
			String displayNameID = attrs.getValue(DISPLAY_NAME_ID_ATTRIB);
			String type = attrs.getValue(TYPE_ATTRIB);

			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
			} else if (StringUtil.isBlank(displayNameID)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED));
			} else {
				PredefinedStyle style = new PredefinedStyle();
				style.setName(name);
				style.setDisplayNameKey(displayNameID);
				style.setType(type);
				try {
					dictionary.addPredefinedStyle(style);
				} catch (MetaDataException e) {
					errorHandler.semanticError(
							new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED));
				}
			}
		}
	}

	protected class StructDefnState extends InnerParseState {

		@Override
		public void parseAttrs(Attributes attrs) throws XMLParserException {
			String name = attrs.getValue(NAME_ATTRIB);
			String displayNameID = attrs.getValue(DISPLAY_NAME_ID_ATTRIB);
			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
			}
			if (StringUtil.isBlank(displayNameID)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED));
			} else {
				struct = new StructureDefn(name);
				struct.setDisplayNameKey(attrs.getValue(DISPLAY_NAME_ID_ATTRIB));
				struct.setSince(attrs.getValue(SINCE_ATTRIB));

				try {
					dictionary.addStructure(struct);
				} catch (MetaDataException e) {
					errorHandler.semanticError(
							new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED));
				}
			}

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		@Override
		public void end() throws SAXException {
			super.end();
			struct = null;
		}

		@Override
		public AbstractParseState startElement(String tagName) {
			if (tagName.equalsIgnoreCase(MEMBER_TAG)) {
				return new MemberState();
			}
			return super.startElement(tagName);
		}
	}

	protected class MemberState extends InnerParseState {

		StructPropertyDefn memberDefn = null;

		@Override
		public void parseAttrs(Attributes attrs) {
			String name = getAttrib(attrs, NAME_ATTRIB);
			String displayNameID = getAttrib(attrs, DISPLAY_NAME_ID_ATTRIB);
			String type = getAttrib(attrs, TYPE_ATTRIB);
			String validator = getAttrib(attrs, VALIDATOR_ATTRIB);
			String subType = getAttrib(attrs, SUB_TYPE_ATTRIB);

			boolean ok = (struct != null);
			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
				ok = false;
			}
			if (StringUtil.isBlank(displayNameID)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED));
				ok = false;
			}
			if (StringUtil.isBlank(type)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_TYPE_REQUIRED));
				ok = false;
			}
			if (!ok) {
				return;
			}
			PropertyType typeDefn = dictionary.getPropertyType(type);

			if (typeDefn == null) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_INVALID_TYPE));
				return;
			}

			if (!ok) {
				return;
			}
			String detailName = getAttrib(attrs, DETAIL_TYPE_ATTRIB);
			ChoiceSet choiceSet = null;
			String structDefn = null;
			PropertyType subTypeDefn = null;
			switch (typeDefn.getTypeCode()) {

			case IPropertyType.DIMENSION_TYPE:
			case IPropertyType.DATE_TIME_TYPE:
			case IPropertyType.STRING_TYPE:
			case IPropertyType.LITERAL_STRING_TYPE:
			case IPropertyType.FLOAT_TYPE:
			case IPropertyType.INTEGER_TYPE:
			case IPropertyType.NUMBER_TYPE:

				if (detailName != null) {
					choiceSet = validateChoiceSet(detailName);
					if (choiceSet == null) {
						return;
					}
				}

				break;

			case IPropertyType.CHOICE_TYPE:

				if (detailName == null) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_CHOICE_TYPE_REQUIRED));
					return;
				}

				choiceSet = validateChoiceSet(detailName);
				if (choiceSet == null) {
					return;
				}

				break;

			case IPropertyType.COLOR_TYPE:

				choiceSet = validateChoiceSet(ColorPropertyType.COLORS_CHOICE_SET);
				if (choiceSet == null) {
					return;
				}

				break;

			case IPropertyType.STRUCT_TYPE:
			case IPropertyType.STRUCT_REF_TYPE:
				if (detailName == null) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_STRUCT_TYPE_REQUIRED));
					return;
				}
				structDefn = detailName;
				break;

			case IPropertyType.ELEMENT_REF_TYPE:
				if (detailName == null) {
					errorHandler.semanticError(new MetaDataParserException(
							MetaDataParserException.DESIGN_EXCEPTION_ELEMENT_REF_TYPE_REQUIRED));
					return;
				}
				if (detailName.equals(THIS_KEYWORD)) {
					detailName = elementDefn.getName();
				}
				break;
			case IPropertyType.LIST_TYPE:
				if (subType == null) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_MISSING_SUB_TYPE));
				} else {
					subTypeDefn = dictionary.getPropertyType(subType);
					if (subTypeDefn == null) {
						errorHandler.semanticError(
								new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_INVALID_TYPE));
						return;
					} else if (subTypeDefn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
						if (detailName == null) {
							errorHandler.semanticError(new MetaDataParserException(
									MetaDataParserException.DESIGN_EXCEPTION_ELEMENT_REF_TYPE_REQUIRED));
							return;
						}
						if (detailName.equals(THIS_KEYWORD)) {
							detailName = elementDefn.getName();
						}
					}

				}
				break;
			}

			memberDefn = new StructPropertyDefn();

			memberDefn.setName(name);
			memberDefn.setType(typeDefn);
			if (subTypeDefn != null && typeDefn.getTypeCode() == IPropertyType.LIST_TYPE) {
				memberDefn.setSubType(subTypeDefn);
			}
			memberDefn.setDisplayNameID(displayNameID);
			memberDefn.setValueRequired(getBooleanAttrib(attrs, VALUE_REQUIRED_ATTRIB, false));
			memberDefn.setSince(attrs.getValue(SINCE_ATTRIB));
			memberDefn.setRuntimeSettable(getBooleanAttrib(attrs, RUNTIME_SETTABLE_ATTRIB, true));
			String trimOption = attrs.getValue(TRIM_OPTION_ATTRIB);
			if (trimOption != null) {
				try {
					int value = handleTrimOption(trimOption);
					memberDefn.setTrimOption(value);
				} catch (MetaDataParserException e) {
					errorHandler.semanticError(e);
				}
			}
			memberDefn.setAllowExpression(getBooleanAttrib(attrs, ALLOW_EXPRESSION_ATTRIB, false));
			if (memberDefn.getTypeCode() == IPropertyType.EXPRESSION_TYPE) {
				memberDefn.setReturnType(attrs.getValue(RETURN_TYPE_ATTRIB));
				memberDefn.setContext(attrs.getValue(CONTEXT_ATTRIB));
			}

			if (!StringUtil.isBlank(validator)) {
				memberDefn.setValueValidator(validator);
			}

			if (typeDefn.getTypeCode() == IPropertyType.STRUCT_TYPE) {
				memberDefn.setIsList(getBooleanAttrib(attrs, IS_LIST_ATTRIB, false));
			}

			if (choiceSet != null) {
				memberDefn.setDetails(choiceSet);
			} else if (structDefn != null) {
				memberDefn.setDetails(structDefn);
			} else if (detailName != null) {
				memberDefn.setDetails(detailName);
			}

			memberDefn.setIntrinsic(getBooleanAttrib(attrs, IS_INTRINSIC_ATTRIB, false));
			try {
				struct.addProperty(memberDefn);
			} catch (MetaDataException e) {
				errorHandler.semanticError(e);
			}

		}

		@Override
		public AbstractParseState startElement(String tagName) {
			if (tagName.equalsIgnoreCase(DEFAULT_TAG)) {
				return new DefaultValueState(memberDefn);
			} else if (tagName.equalsIgnoreCase(ALLOWED_TAG)) {
				return new AllowedState(memberDefn);
			}
			return super.startElement(tagName);
		}

		@Override
		public void end() throws SAXException {
			if (memberDefn == null) {
				return;
			}
			// validate the default value. The default value must be validate
			// here as it needs choice set.
			if (memberDefn.getDefault() != null) {
				try {
					Object value = memberDefn.validateXml(null, null, memberDefn.getDefault());
					memberDefn.setDefault(value);
				} catch (PropertyValueException e) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_INVALID_DEFAULT));
				}
			}
			memberDefn = null;
		}
	}

	public class ElementDefnState extends InnerParseState {

		protected ElementDefn createElementDefn() {
			return builder.createElementDefn();
		}

		@Override
		public void parseAttrs(Attributes attrs) {
			String name = attrs.getValue(NAME_ATTRIB);
			String displayNameID = attrs.getValue(DISPLAY_NAME_ID_ATTRIB);

			boolean ok = true;
			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
				ok = false;
			}
			if (checkDisplayNameID && StringUtil.isBlank(displayNameID)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED));
				ok = false;
			}

			if (!ok) {
				return;
			}

			// use this method to create element definition instance and handle
			// the name, this will help to override and change the behavior for
			// different requirements

			elementDefn = createElementDefn();
			elementDefn.setName(name);
			elementDefn.setAbstract(getBooleanAttrib(attrs, IS_ABSTRACT_ATTRIB, false));
			elementDefn.setDisplayNameKey(displayNameID);
			elementDefn.setExtends(attrs.getValue(EXTENDS_ATTRIB));
			elementDefn.setHasStyle(getBooleanAttrib(attrs, HAS_STYLE_ATTRIB, false));
			elementDefn.setSelector(attrs.getValue(SELECTOR_ATTRIB));
			elementDefn.setAllowsUserProperties(getBooleanAttrib(attrs, ALLOWS_USER_PROPERTIES_ATTRIB, true));
			elementDefn.setJavaClass(attrs.getValue(JAVA_CLASS_ATTRIB));
			elementDefn.setCanExtend(getBooleanAttrib(attrs, CAN_EXTEND_ATTRIB, true));
			elementDefn.setSince(attrs.getValue(SINCE_ATTRIB));
			elementDefn.setXmlName(attrs.getValue(XML_NAME_ATTRIB));
			String nameRequired = attrs.getValue(IS_NAME_REQUIRED_ATTRIB);
			if (nameRequired != null) {
				boolean flag = parseBoolean(nameRequired, false);
				elementDefn.setNameOption(flag ? MetaDataConstants.REQUIRED_NAME : MetaDataConstants.OPTIONAL_NAME);
			}

			String ns = attrs.getValue(NAME_SPACE_ATTRIB);
			IElementDefn moduleDefn = dictionary.getElement(ReportDesignConstants.MODULE_ELEMENT);
			if (ns == null || ns.trim().length() == 0) {
				// Inherit default name space
			} else if (ns.equalsIgnoreCase(NameSpaceFactory.STYLE_NS_NAME)) {
				elementDefn.nameConfig.nameSpaceID = Module.STYLE_NAME_SPACE;
				elementDefn.nameConfig.holder = moduleDefn;
			} else if (ns.equalsIgnoreCase(NameSpaceFactory.THEME_NS_NAME)) {
				elementDefn.nameConfig.nameSpaceID = Module.THEME_NAME_SPACE;
				elementDefn.nameConfig.holder = moduleDefn;
			} else if (ns.equalsIgnoreCase(NameSpaceFactory.DATA_SET_NS_NAME)) {
				elementDefn.nameConfig.nameSpaceID = Module.DATA_SET_NAME_SPACE;
				elementDefn.nameConfig.holder = moduleDefn;
			} else if (ns.equalsIgnoreCase(NameSpaceFactory.DATA_SOURCE_NS_NAME)) {
				elementDefn.nameConfig.nameSpaceID = Module.DATA_SOURCE_NAME_SPACE;
				elementDefn.nameConfig.holder = moduleDefn;
			} else if (ns.equalsIgnoreCase(NameSpaceFactory.ELEMENT_NS_NAME)) {
				elementDefn.nameConfig.nameSpaceID = Module.ELEMENT_NAME_SPACE;
				elementDefn.nameConfig.holder = moduleDefn;
			} else if (ns.equalsIgnoreCase(NameSpaceFactory.PARAMETER_NS_NAME)) {
				elementDefn.nameConfig.nameSpaceID = Module.PARAMETER_NAME_SPACE;
				elementDefn.nameConfig.holder = moduleDefn;
			} else if (ns.equalsIgnoreCase(NameSpaceFactory.MASTER_PAGE_NS_NAME)) {
				elementDefn.nameConfig.nameSpaceID = Module.PAGE_NAME_SPACE;
				elementDefn.nameConfig.holder = moduleDefn;
			} else if (ns.equalsIgnoreCase(NameSpaceFactory.CUBE_NS_NAME)) {
				elementDefn.nameConfig.nameSpaceID = Module.CUBE_NAME_SPACE;
				elementDefn.nameConfig.holder = moduleDefn;
			} else if (ns.equalsIgnoreCase(NameSpaceFactory.TEMPLATE_PARAMETER_DEFINITION_NS_NAME)) {
				elementDefn.nameConfig.nameSpaceID = Module.TEMPLATE_PARAMETER_NAME_SPACE;
				elementDefn.nameConfig.holder = moduleDefn;
			} else if (ns.equalsIgnoreCase(NameSpaceFactory.DIMENSION_NS_NAME)) {
				elementDefn.nameConfig.nameSpaceID = Module.DIMENSION_NAME_SPACE;
				elementDefn.nameConfig.holder = moduleDefn;
			} else if (ns.equalsIgnoreCase(NameSpaceFactory.NO_NS_NAME)) {
				elementDefn.nameConfig.nameSpaceID = MetaDataConstants.NO_NAME_SPACE;
			} else if (ns.startsWith("(") && ns.endsWith(")")) //$NON-NLS-1$//$NON-NLS-2$
			{
				String nsValue = ns.substring(1, ns.length() - 1);
				String[] splitStrings = nsValue.split(","); //$NON-NLS-1$
				if (splitStrings == null || !(splitStrings.length == 2 || splitStrings.length == 3)) {
					errorHandler.semanticError(
							new MetaDataException(MetaDataException.DESIGN_EXCEPTION_INVALID_NAME_SPACE));
				} else {
					int length = splitStrings.length;

					assert length == 2 || length == 3;
					String holderName = StringUtil.trimString(splitStrings[0]);
					String nameSpace = StringUtil.trimString(splitStrings[1]);
					ElementDefn holderDefn = (ElementDefn) dictionary.getElement(holderName);
					if (holderDefn == null) {
						errorHandler.semanticError(
								new MetaDataParserException(MetaDataException.DESIGN_EXCEPTION_INVALID_NAME_SPACE));
					} else {
						// the name holder must be existing and name
						// required element or the module type
						elementDefn.nameConfig.holder = holderDefn;
						elementDefn.nameConfig.nameSpaceID = NameSpaceFactory.getInstance().getNameSpaceID(holderName,
								nameSpace);
						if (length == 3) {
							elementDefn.nameConfig.targetPropertyName = StringUtil.trimString(splitStrings[2]);
						}
					}
				}
			} else { // $NON-NLS-1$//$NON-NLS-2$
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataException.DESIGN_EXCEPTION_INVALID_NAME_SPACE));
			}

			try {
				builder.addElementDefn(elementDefn);
			} catch (MetaDataException e) {
				errorHandler.semanticError(
						new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED));
			}
		}

		@Override
		public AbstractParseState startElement(String tagName) {
			if (tagName.equalsIgnoreCase(PROPERTY_TAG)) {
				return new PropertyState();
			}
			if (tagName.equalsIgnoreCase(PROPERTY_GROUP_TAG)) {
				return new PropertyGroupState();
			}
			if (tagName.equalsIgnoreCase(STYLE_PROPERTY_TAG)) {
				return new StylePropertyState();
			}
			if (tagName.equalsIgnoreCase(SLOT_TAG)) {
				return new SlotState();
			}
			if (tagName.equalsIgnoreCase(METHOD_TAG)) {
				return new ElementMethodState(elementDefn);
			}
			if (tagName.equalsIgnoreCase(SEMANTIC_VALIDATOR_TAG)) {
				return new TriggerState();
			}
			if (tagName.equalsIgnoreCase(PROPERTY_VISIBILITY_TAG)) {
				return new PropertyVisibilityState();
			}

			return super.startElement(tagName);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		@Override
		public void end() throws SAXException {
			super.end();
			elementDefn = null;
		}

		/**
		 * Parses the property visiblity.
		 */

		private class PropertyVisibilityState extends InnerParseState {

			@Override
			public void parseAttrs(Attributes attrs) {
				String name = attrs.getValue(NAME_ATTRIB);
				String visible = attrs.getValue(PROPERTY_VISIBILITY_ATTRIB);

				if (StringUtil.isBlank(name)) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
					return;
				}

				elementDefn.addPropertyVisibility(name, visible);
			}
		}
	}

	class PropertyGroupState extends InnerParseState {

		@Override
		public void parseAttrs(Attributes attrs) {
			groupNameID = attrs.getValue(DISPLAY_NAME_ID_ATTRIB);
			if (StringUtil.isBlank(groupNameID)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_GROUP_NAME_ID_REQUIRED));
			}
		}

		@Override
		public AbstractParseState startElement(String tagName) {
			if (tagName.equalsIgnoreCase(PROPERTY_TAG)) {
				return new PropertyState();
			}
			return super.startElement(tagName);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		@Override
		public void end() throws SAXException {
			groupNameID = null;
		}
	}

	protected class PropertyState extends InnerParseState {

		List<String> propertyTypes = new ArrayList<>();

		@Override
		public void parseAttrs(Attributes attrs) {
			choices.clear();
			propDefn = null;
			String name = getAttrib(attrs, NAME_ATTRIB);
			String displayNameID = getAttrib(attrs, DISPLAY_NAME_ID_ATTRIB);
			String type = getAttrib(attrs, TYPE_ATTRIB);
			String validator = getAttrib(attrs, VALIDATOR_ATTRIB);
			String subType = getAttrib(attrs, SUB_TYPE_ATTRIB);
			boolean isList = getBooleanAttrib(attrs, IS_LIST_ATTRIB, false);
			String detailName = getAttrib(attrs, DETAIL_TYPE_ATTRIB);
			String namespace = getAttrib(attrs, NAME_SPACE_ATTRIB);

			boolean ok = (elementDefn != null);
			if (name == null) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
				ok = false;
			}

			if (checkDisplayNameID && StringUtil.isBlank(displayNameID)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED));
				ok = false;
			}
			if (type == null) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_TYPE_REQUIRED));
				ok = false;
			}

			if (!ok) {
				return;
			}

			// Look up the choice set name, if any.

			PropertyType typeDefn = dictionary.getPropertyType(type);
			if (typeDefn == null) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_INVALID_TYPE));
				return;
			}

			// list type only support types in supportedSubTypes
			// isList only support element/content/structure
			// here we convert isList to listType for simple properties
			int typeCode = typeDefn.getTypeCode();
			if (isList && typeCode != IPropertyType.LIST_TYPE) {
				if (PropertyDefn.isSupportedSubType(typeDefn)) {
					subType = type;
				}
			}

			ChoiceSet choiceSet = null;
			StructureDefn struct = null;
			PropertyType subTypeDefn = null;

			switch (typeDefn.getTypeCode()) {
			case IPropertyType.DIMENSION_TYPE:
			case IPropertyType.DATE_TIME_TYPE:
			case IPropertyType.STRING_TYPE:
			case IPropertyType.LITERAL_STRING_TYPE:
			case IPropertyType.FLOAT_TYPE:
			case IPropertyType.INTEGER_TYPE:
			case IPropertyType.NUMBER_TYPE:

				if (detailName != null) {
					choiceSet = validateChoiceSet(detailName);
					if (choiceSet == null) {
						return;
					}
				}

				break;

			case IPropertyType.CHOICE_TYPE:

				// the user can define the detail type either in detailType
				// attribute or type element, so valid it at the end of element
				if (detailName != null) {
					choiceSet = validateChoiceSet(detailName);
					if (choiceSet == null) {
						return;
					}
				}
				break;

			case IPropertyType.COLOR_TYPE:

				choiceSet = validateChoiceSet(ColorPropertyType.COLORS_CHOICE_SET);
				if (choiceSet == null) {
					return;
				}

				break;

			case IPropertyType.STRUCT_TYPE:
			case IPropertyType.STRUCT_REF_TYPE:
				if (detailName == null) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_STRUCT_TYPE_REQUIRED));
					return;
				}
				struct = (StructureDefn) dictionary.getStructure(detailName);
				if (struct == null) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_INVALID_STRUCT_TYPE));
					return;
				}
				break;

			case IPropertyType.ELEMENT_TYPE:
			case IPropertyType.CONTENT_ELEMENT_TYPE:
			case IPropertyType.ELEMENT_REF_TYPE:
				// the user can define the detail type either in detailType attribute or type
				// element.
				if (THIS_KEYWORD.equals(detailName)) {
					detailName = elementDefn.getName();
				}
				break;
			case IPropertyType.LIST_TYPE:
				if (subType == null) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_MISSING_SUB_TYPE));
				} else {
					subTypeDefn = dictionary.getPropertyType(subType);
					if (subTypeDefn == null) {
						errorHandler.semanticError(
								new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_INVALID_TYPE));
						return;
					} else if (subTypeDefn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
						if (detailName == null) {
							errorHandler.semanticError(new MetaDataParserException(
									MetaDataParserException.DESIGN_EXCEPTION_ELEMENT_REF_TYPE_REQUIRED));
							return;
						}
						if (detailName.equals(THIS_KEYWORD)) {
							detailName = elementDefn.getName();
						}
					}

				}
				break;
			default:
				// Ignore the detail name for other types.

				detailName = null;
			}

			// call the method to create property definition rather than create
			// it using constructor directly to satisfy different requirements
			// in different use-cases
			propDefn = builder.createPropertyDefn();

			propDefn.setName(name);
			propDefn.setDisplayNameID(displayNameID);
			propDefn.setType(typeDefn);
			if (typeDefn.getTypeCode() == IPropertyType.LIST_TYPE) {
				propDefn.setSubType(subTypeDefn);
			}
			propDefn.setGroupNameKey(groupNameID);
			propDefn.setCanInherit(getBooleanAttrib(attrs, CAN_INHERIT_ATTRIBUTE, true));
			propDefn.setIntrinsic(getBooleanAttrib(attrs, IS_INTRINSIC_ATTRIB, false));
			propDefn.setStyleProperty(getBooleanAttrib(attrs, IS_STYLE_PROPERTY_ATTRIB, false));
			propDefn.setBidiProperty(getBooleanAttrib(attrs, IS_BIDI_PROPERTY_ATTRIB, false));
			propDefn.setValueRequired(getBooleanAttrib(attrs, VALUE_REQUIRED_ATTRIB, false));
			propDefn.setSince(attrs.getValue(SINCE_ATTRIB));
			propDefn.setRuntimeSettable(getBooleanAttrib(attrs, RUNTIME_SETTABLE_ATTRIB, true));
			String trimOption = attrs.getValue(TRIM_OPTION_ATTRIB);
			if (trimOption != null) {
				try {
					int value = handleTrimOption(trimOption);
					propDefn.setTrimOption(value);
				} catch (MetaDataParserException e) {
					errorHandler.semanticError(e);
				}
			}
			propDefn.setAllowExpression(getBooleanAttrib(attrs, ALLOW_EXPRESSION_ATTRIB, false));
			if (propDefn.getTypeCode() == IPropertyType.EXPRESSION_TYPE) {
				propDefn.setReturnType(attrs.getValue(RETURN_TYPE_ATTRIB));
				propDefn.setContext(attrs.getValue(CONTEXT_ATTRIB));
			}

			if (!StringUtil.isBlank(validator)) {
				propDefn.setValueValidator(validator);
			}

			if (typeCode == IPropertyType.STRUCT_TYPE || propDefn.isElementType()) {
				propDefn.setIsList(isList);
			}

			if (choiceSet != null) {
				propDefn.setDetails(choiceSet);
			} else if (struct != null) {
				propDefn.setDetails(struct);
			} else if (detailName != null) {
				propDefn.setDetails(detailName);
			}

			if ("true".equalsIgnoreCase(namespace)) {
				NameConfig config = new NameConfig();
				config.holder = elementDefn;
				config.nameSpaceID = NameSpaceFactory.getInstance().getNameSpaceID(elementDefn.getName(),
						propDefn.getName());
				propDefn.setNameConfig(config);
			}
		}

		@Override
		public AbstractParseState startElement(String tagName) {
			if (tagName.equalsIgnoreCase(DEFAULT_TAG)) {
				return new DefaultValueState(propDefn);
			} else if (tagName.equalsIgnoreCase(ALLOWED_TAG)) {
				return new AllowedState(propDefn);
			} else if (tagName.equalsIgnoreCase(ALLOWED_UNITS_TAG)) {
				return new AllowedUnitsState(propDefn);
			} else if (tagName.equalsIgnoreCase(TRIGGER_TAG)) {
				return new TriggerState();
			} else if (tagName.equalsIgnoreCase(DEFAULT_UNIT_TAG)) {
				return new DefaultUnitState();
			} else if (tagName.equalsIgnoreCase(TYPE_TAG)) {
				return new PropertyTypeState(propertyTypes);
			} else {
				return super.startElement(tagName);
			}
		}

		@Override
		public void end() throws SAXException {
			if (propDefn == null) {
				return;
			}
			// if property is element or choice type, then set list of allowed
			// type names to the details
			int typeCode = propDefn.getTypeCode();
			if (!propertyTypes.isEmpty()) {
				if (typeCode == IPropertyType.ELEMENT_TYPE || typeCode == IPropertyType.STRUCT_TYPE
						|| typeCode == IPropertyType.CONTENT_ELEMENT_TYPE) {
					propDefn.setDetails(propertyTypes);
				}
				if (typeCode == IPropertyType.CHOICE_TYPE && propDefn.getDetails() == null) {
					IChoiceSet choiceSet = validateChoiceSet(propertyTypes.get(0));
					if (choiceSet != null) {
						propDefn.setDetails(choiceSet);
					}
				}
			}

			if (isValidPropertyDefn()) {
				// add it to dictionary
				try {
					builder.addPropertyDefn(elementDefn, propDefn);
				} catch (MetaDataException e) {
					errorHandler.semanticError(
							new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED));
				}
			}

			propDefn = null;
		}

		protected boolean isValidPropertyDefn() {
			int typeCode = propDefn.getTypeCode();
			// check if the detail type has been set
			if (propDefn.getDetails() == null) {
				if (typeCode == IPropertyType.CHOICE_TYPE) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_CHOICE_TYPE_REQUIRED));
					return false;
				}
				if (typeCode == IPropertyType.ELEMENT_REF_TYPE) {
					errorHandler.semanticError(new MetaDataParserException(
							MetaDataParserException.DESIGN_EXCEPTION_ELEMENT_REF_TYPE_REQUIRED));
					return false;

				}
//				if ( typeCode == IPropertyType.CONTENT_ELEMENT_TYPE
//						|| typeCode == IPropertyType.ELEMENT_TYPE )
//				{
//					errorHandler
//							.semanticError( new MetaDataParserException(
//									MetaDataParserException.DESIGN_EXCEPTION_ELEMENT_REF_TYPE_REQUIRED );
//					return false;
//
//				}
			}

			// ----------

			// validate the default value. The default value must be validate
			// here as it needs choice set.
			if (propDefn.getDefault() != null) {
				try {
					Object value = propDefn.validateXml(null, null, propDefn.getDefault());
					propDefn.setDefault(value);
				} catch (PropertyValueException e) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_INVALID_DEFAULT));
					return false;
				}
			}
			return true;
		}
	}

	class DefaultUnitState extends InnerParseState {

		@Override
		public void end() throws SAXException {
			if (propDefn == null) {
				return;
			}

			int type = propDefn.getTypeCode();

			if (type != IPropertyType.DIMENSION_TYPE) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_DEFAULT_UNIT_NOT_ALLOWED));

				return;
			}
			propDefn.setDefaultUnit(text.toString());
		}
	}

	class AllowedState extends InnerParseState {

		PropertyDefn tmpPropDefn;

		AllowedState(PropertyDefn tmpPropDefn) {
			this.tmpPropDefn = tmpPropDefn;
		}

		@Override
		public void end() throws SAXException {
			if (tmpPropDefn == null) {
				return;
			}

			int type = tmpPropDefn.getTypeCode();

			if (type != IPropertyType.DIMENSION_TYPE && type != IPropertyType.CHOICE_TYPE) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_RESTRICTION_NOT_ALLOWED));

				return;
			}

			ChoiceSet allowedChoices = builder.createChoiceSet();
			ArrayList<IChoice> allowedList = new ArrayList<>();

			String choicesStr = StringUtil.trimString(text.toString());

			// blank string.

			if (choicesStr == null) {
				return;
			}

			String[] nameArray = choicesStr.split(","); //$NON-NLS-1$

			if (type == IPropertyType.DIMENSION_TYPE) {
				// units restriction on a dimension property.

				IChoiceSet units = dictionary.getChoiceSet(DesignChoiceConstants.CHOICE_UNITS);
				assert units != null;

				for (int i = 0; i < nameArray.length; i++) {
					IChoice unit = units.findChoice(nameArray[i].trim());

					if (unit != null) {
						allowedList.add(unit);
					} else {
						errorHandler.semanticError(new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_INVALID_RESTRICTION));

						return;
					}
				}
			} else {
				// choices type restriction.

				IChoiceSet choices = tmpPropDefn.getChoices();
				assert choices != null;

				for (int i = 0; i < nameArray.length; i++) {
					IChoice choice = choices.findChoice(nameArray[i].trim());

					if (choice != null) {
						allowedList.add(choice);
					} else {
						errorHandler.semanticError(new MetaDataParserException(
								MetaDataParserException.DESIGN_EXCEPTION_INVALID_RESTRICTION));

						return;
					}
				}

			}

			allowedChoices.setChoices(allowedList.toArray(new Choice[allowedList.size()]));

			tmpPropDefn.setAllowedChoices(allowedChoices);
		}
	}

	private class AllowedUnitsState extends InnerParseState {

		PropertyDefn tmpPropDefn;

		AllowedUnitsState(PropertyDefn tmpPropDefn) {
			this.tmpPropDefn = tmpPropDefn;
		}

		@Override
		public void end() throws SAXException {
			if (tmpPropDefn == null) {
				return;
			}

			int type = tmpPropDefn.getTypeCode();

			if (type != IPropertyType.DIMENSION_TYPE && !(type == IPropertyType.LIST_TYPE
					&& tmpPropDefn.getSubTypeCode() == IPropertyType.DIMENSION_TYPE)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_RESTRICTION_NOT_ALLOWED));

				return;
			}

			ChoiceSet allowedChoices = builder.createChoiceSet();
			ArrayList<IChoice> allowedList = new ArrayList<>();

			String choicesStr = StringUtil.trimString(text.toString());

			// blank string.

			if (choicesStr == null) {
				return;
			}

			String[] nameArray = choicesStr.split(","); //$NON-NLS-1$

			// units restriction on a dimension property.

			IChoiceSet units = dictionary.getChoiceSet(DesignChoiceConstants.CHOICE_UNITS);

			assert units != null;

			for (int i = 0; i < nameArray.length; i++) {
				IChoice unit = units.findChoice(nameArray[i].trim());

				if (unit != null) {
					allowedList.add(unit);
				} else {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_INVALID_RESTRICTION));

					return;
				}
			}

			allowedChoices.setChoices(allowedList.toArray(new Choice[allowedList.size()]));

			tmpPropDefn.setAllowedUnits(allowedChoices);
		}
	}

	class ValidatorsState extends InnerParseState {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement
		 * (java.lang.String)
		 */
		@Override
		public AbstractParseState startElement(String tagName) {
			if (VALUE_VALIDATOR_TAG.equalsIgnoreCase(tagName)) {
				return new ValueValidatorState();
			}
			if (SEMANTIC_VALIDATOR_TAG.equalsIgnoreCase(tagName)) {
				return new SemanticValidatorState();
			}
			return super.startElement(tagName);
		}
	}

	class ValueValidatorState extends InnerParseState {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(
		 * org.xml.sax.Attributes)
		 */

		@Override
		public void parseAttrs(Attributes attrs) throws XMLParserException {
			String name = getAttrib(attrs, NAME_ATTRIB);
			String className = getAttrib(attrs, CLASS_ATTRIB);

			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_VALIDATOR_NAME_REQUIRED));
				return;
			}

			if (StringUtil.isBlank(className)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_CLASS_NAME_REQUIRED));
				return;
			}

			try {
				Class<? extends Object> c = builder.loadClass(className);
				SimpleValueValidator validator = (SimpleValueValidator) c.newInstance();
				validator.setName(name);

				try {
					builder.addValueValidator(validator);
				} catch (MetaDataException e) {
					errorHandler.semanticError(
							new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED));
				}

			} catch (Exception e) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_INVALID_META_VALIDATOR));
			}
		}
	}

	class SemanticValidatorState extends InnerParseState {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(
		 * org.xml.sax.Attributes)
		 */

		@Override
		public void parseAttrs(Attributes attrs) throws XMLParserException {
			String name = getAttrib(attrs, NAME_ATTRIB);
			String modules = getAttrib(attrs, MODULES_ATTRIB);
			String className = getAttrib(attrs, CLASS_ATTRIB);

			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_VALIDATOR_NAME_REQUIRED));
				return;
			}
			if (StringUtil.isBlank(className)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_CLASS_NAME_REQUIRED));
				return;
			}

			try {
				Class<? extends Object> c = Class.forName(className);
				Method m = c.getMethod("getInstance", (Class[]) null); //$NON-NLS-1$
				AbstractSemanticValidator validator = (AbstractSemanticValidator) m.invoke(null, (Object[]) null);
				validator.setName(name);
				validator.setModules(modules);

				try {
					dictionary.addSemanticValidator(validator);
				} catch (MetaDataException e) {
					errorHandler.semanticError(
							new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED));
				}
			} catch (Exception e) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_INVALID_META_VALIDATOR));
			}
		}

	}

	class DefaultValueState extends InnerParseState {

		/**
		 * Reference to a member or a property.
		 */

		PropertyDefn propertyDefn = null;

		DefaultValueState(PropertyDefn propDefn) {
			this.propertyDefn = propDefn;
		}

		@Override
		public void end() throws SAXException {
			if (this.propertyDefn == null) {
				return;
			}
			// validation should be done in property.end or member.end as
			// the choice set may not defined yet
			propertyDefn.setDefault(text.toString());
		}
	}

	class ChoiceState extends InnerParseState {

		@Override
		public void parseAttrs(Attributes attrs) throws XMLParserException {
			String displayNameID = attrs.getValue(DISPLAY_NAME_ID_ATTRIB);
			String xmlName = attrs.getValue(NAME_ATTRIB);
			if (checkDisplayNameID && StringUtil.isBlank(displayNameID)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED));
			} else if (StringUtil.isBlank(xmlName)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_XML_NAME_REQUIRED));
			} else {
				Choice choice = builder.createChoice();
				choice.setName(xmlName);
				choice.setDisplayNameKey(displayNameID);

				boolean found = false;
				Iterator<Choice> iter = choices.iterator();
				while (iter.hasNext()) {
					Choice tmpChoice = iter.next();
					if (tmpChoice.getName().equalsIgnoreCase(choice.getName())) {
						found = true;
						break;
					}
				}
				if (found) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataException.DESIGN_EXCEPTION_DUPLICATE_CHOICE_NAME));
				} else {
					choices.add(choice);
				}
			}
		}
	}

	class StylePropertyState extends InnerParseState {

		@Override
		public void parseAttrs(Attributes attrs) throws XMLParserException {
			String name = attrs.getValue(NAME_ATTRIB);

			boolean ok = (elementDefn != null);
			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
				ok = false;
			}

			if (ok) {
				elementDefn.addStyleProperty(name);
			}

		}
	}

	class SlotState extends InnerParseState {

		@Override
		public void parseAttrs(Attributes attrs) throws XMLParserException {
			String name = attrs.getValue(NAME_ATTRIB);
			String displayNameID = attrs.getValue(DISPLAY_NAME_ID_ATTRIB);
			String multipleCardinality = attrs.getValue(MULTIPLE_CARDINALITY_ATTRIB);
			String tmpID = attrs.getValue(ID_ATTRIB);
			String namespace = getAttrib(attrs, NAME_SPACE_ATTRIB);

			boolean ok = (elementDefn != null);
			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
				ok = false;
			} else if (StringUtil.isBlank(displayNameID)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED));
				ok = false;
			} else if (StringUtil.isBlank(multipleCardinality)) {
				errorHandler.semanticError(new MetaDataParserException(
						MetaDataParserException.DESIGN_EXCEPTION_MULTIPLE_CARDINALITY_REQUIRED));
				ok = false;
			}

			if (!ok) {
				return;
			}

			slotDefn = new SlotDefn();
			slotDefn.setName(name);
			slotDefn.setDisplayNameID(displayNameID);
			slotDefn.setManagedByNameSpace(getBooleanAttrib(attrs, IS_MANAGED_BY_NAME_SPACE_ATTRIB, true));
			slotDefn.setMultipleCardinality(parseBoolean(multipleCardinality, true));
			slotDefn.setSelector(attrs.getValue(SELECTOR_ATTRIB));
			slotDefn.setSince(attrs.getValue(SINCE_ATTRIB));
			slotDefn.setXmlName(attrs.getValue(XML_NAME_ATTRIB));
			if (!StringUtil.isBlank(tmpID)) {
				try {
					slotDefn.setSlotID(Integer.parseInt(tmpID));
				} catch (NumberFormatException e) {
					// just ignore the error. the slot id is reset later.
				}
			}
			if ("true".equalsIgnoreCase(namespace)) {
				NameConfig nameConfig = new NameConfig();
				nameConfig.holder = elementDefn;
				nameConfig.nameSpaceID = NameSpaceFactory.getInstance().getNameSpaceID(elementDefn.getName(),
						slotDefn.getName());
				slotDefn.setNameConfig(nameConfig);
			}

			elementDefn.addSlot(slotDefn);
		}

		@Override
		public AbstractParseState startElement(String tagName) {
			if (tagName.equalsIgnoreCase(TYPE_TAG)) {
				return new SlotTypeState();
			}
			if (tagName.equalsIgnoreCase(TRIGGER_TAG)) {
				return new TriggerState();
			}
			return super.startElement(tagName);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		@Override
		public void end() throws SAXException {
			super.end();
			slotDefn = null;
		}

	}

	class PropertyTypeState extends InnerParseState {

		protected List<String> types = null;

		/**
		 * Constructs the property type state with a list to hold all the type names.
		 *
		 * @param propertyTypes
		 */
		public PropertyTypeState(List<String> propertyTypes) {
			this.types = propertyTypes;
		}

		@Override
		public void parseAttrs(Attributes attrs) throws XMLParserException {
			boolean ok = (propDefn != null);
			String name = attrs.getValue(NAME_ATTRIB);
			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
				ok = false;
			}

			if (ok) {
				types.add(name);
			}
		}
	}

	class SlotTypeState extends InnerParseState {

		@Override
		public void parseAttrs(Attributes attrs) throws XMLParserException {
			boolean ok = (slotDefn != null);
			String name = attrs.getValue(NAME_ATTRIB);
			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
				ok = false;
			}

			if (ok) {
				slotDefn.addType(name);
			}
		}
	}

	/**
	 * The state to parse a method under a class.
	 */

	class ClassMethodState extends AbstractMethodState {

		private boolean isConstructor = false;

		ClassMethodState(Object obj, boolean isConstructor) {
			super(obj);
			this.isConstructor = isConstructor;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @seeorg.eclipse.birt.report.model.metadata.MetaDataHandler.
		 * AbstractMethodState#getMethodInfo()
		 */

		@Override
		MethodInfo getMethodInfo(String name) {
			ClassInfo classInfo = (ClassInfo) owner;

			if (classInfo != null) {
				if (isConstructor) {
					methodInfo = (MethodInfo) classInfo.getConstructor();
				} else {
					methodInfo = classInfo.findMethod(name);
				}
			}

			if (methodInfo == null) {
				methodInfo = builder.createMethodInfo(isConstructor);
			}

			return methodInfo;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @seeorg.eclipse.birt.report.model.metadata.MetaDataHandler.
		 * AbstractMethodState#addDefnTo()
		 */

		@Override
		void addDefnTo() {
			assert owner instanceof ClassInfo;

			ClassInfo classInfo = (ClassInfo) owner;
			try {
				builder.addMethodInfo(classInfo, methodInfo);
			} catch (MetaDataException e) {
				errorHandler.semanticError(
						new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED));
			}

		}
	}

	/**
	 * The state to parse a method under an element.
	 */

	class ElementMethodState extends AbstractMethodState {

		SystemPropertyDefn localPropDefn = null;

		/*
		 * (non-Javadoc)
		 *
		 * @seeorg.eclipse.birt.report.model.metadata.MetaDataHandler.
		 * AbstractMethodState#getMethodInfo()
		 */

		@Override
		MethodInfo getMethodInfo(String name) {
			return new MethodInfo(false);
		}

		ElementMethodState(Object obj) {
			super(obj);
			localPropDefn = builder.createPropertyDefn();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(
		 * org.xml.sax.Attributes)
		 */
		@Override
		public void parseAttrs(Attributes attrs) {
			super.parseAttrs(attrs);
			localPropDefn.setValueRequired(getBooleanAttrib(attrs, VALUE_REQUIRED_ATTRIB, false));
			localPropDefn.setSince(attrs.getValue(SINCE_ATTRIB));
			localPropDefn.setContext(attrs.getValue(CONTEXT_ATTRIB));
			localPropDefn.setReturnType(attrs.getValue(RETURN_TYPE_ATTRIB));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @seeorg.eclipse.birt.report.model.metadata.MetaDataHandler.
		 * AbstractMethodState#addDefnTo()
		 */

		@Override
		final void addDefnTo() {
			assert owner instanceof ElementDefn;

			PropertyType typeDefn = dictionary.getPropertyType(IPropertyType.SCRIPT_TYPE);

			String name = methodInfo.getName();
			String displayNameID = methodInfo.getDisplayNameKey();

			localPropDefn.setName(name);
			localPropDefn.setDisplayNameID(displayNameID);
			localPropDefn.setType(typeDefn);
			localPropDefn.setGroupNameKey(null);
			localPropDefn.setCanInherit(true);
			localPropDefn.setIntrinsic(false);
			localPropDefn.setStyleProperty(false);
			localPropDefn.setDetails(methodInfo);

			try {
				builder.addPropertyDefn((ElementDefn) owner, localPropDefn);
			} catch (MetaDataException e) {
				errorHandler.semanticError(
						new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED));
			}
		}
	}

	/**
	 * Parses an method state either under an element or class tag.
	 */

	abstract class AbstractMethodState extends InnerParseState {

		/**
		 * The element contains this state. Can be either a <code>ElementDefn</code> or
		 * <code>ClassInfo</code>.
		 */

		protected Object owner = null;

		/**
		 * The cached <code>MethodInfo</code> for the state.
		 */

		protected MethodInfo methodInfo = null;

		/**
		 * The cached argument list.
		 */

		private ArgumentInfoList argumentList = null;

		/**
		 * Constructs a <code>MethodState</code> with the given owner.
		 *
		 * @param obj the parent object of this state
		 */

		AbstractMethodState(Object obj) {
			assert obj != null;
			this.owner = obj;
		}

		/**
		 * Adds method information to the ElementDefn or ClassInfo.
		 */

		abstract void addDefnTo();

		/**
		 * Returns method information with the given method name.
		 *
		 * @param name the method name
		 * @return the <code>MethodInfo</code> object
		 */

		abstract MethodInfo getMethodInfo(String name);

		@Override
		public void parseAttrs(Attributes attrs) {
			String name = attrs.getValue(NAME_ATTRIB);
			String displayNameID = attrs.getValue(DISPLAY_NAME_ID_ATTRIB);
			String toolTipID = attrs.getValue(TOOL_TIP_ID_ATTRIB);
			String returnType = attrs.getValue(RETURN_TYPE_ATTRIB);
			boolean isStatic = getBooleanAttrib(attrs, IS_STATIC_ATTRIB, false);

			boolean ok = true;
			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
				ok = false;
			}
			if (checkDisplayNameID && StringUtil.isBlank(displayNameID)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED));
				ok = false;
			}

			if (!ok) {
				return;
			}

			// Note that here ROM supports overloadding, while JavaScript not.
			// finds the method info if it has been parsed.

			methodInfo = getMethodInfo(name);

			methodInfo.setName(name);
			methodInfo.setDisplayNameKey(displayNameID);
			methodInfo.setReturnType(returnType);
			methodInfo.setToolTipKey(toolTipID);
			methodInfo.setStatic(isStatic);

			addDefnTo();
		}

		@Override
		public AbstractParseState startElement(String tagName) {
			if (tagName.equalsIgnoreCase(ARGUMENT_TAG)) {
				return new ArgumentState();
			}
			return super.startElement(tagName);
		}

		@Override
		public void end() throws SAXException {
			if (argumentList == null) {
				argumentList = new ArgumentInfoList();
			}

			methodInfo.addArgumentList(argumentList);

			methodInfo = null;
			propDefn = null;
		}

		class ArgumentState extends InnerParseState {

			@Override
			public void parseAttrs(Attributes attrs) {
				String name = attrs.getValue(NAME_ATTRIB);
				String tagID = attrs.getValue(TAG_ID_ATTRIB);
				String type = attrs.getValue(TYPE_ATTRIB);
				// for class member, we use data type, support dataType to make it consistent
				if (type == null) {
					type = attrs.getValue(DATA_TYPE_ATTRIB);
				}

				if (name == null) {
					return;
				}

				ArgumentInfo argument = builder.createArgumentInfo();
				argument.setName(name);
				argument.setType(type);
				argument.setDisplayNameKey(tagID);

				if (argumentList == null) {
					argumentList = new ArgumentInfoList();
				}

				try {
					argumentList.addArgument(argument);
				} catch (MetaDataException e) {
					errorHandler.semanticError(
							new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED));
				}
			}
		}
	}

	protected class ClassState extends InnerParseState {

		protected ClassInfo classInfo = null;

		@Override
		public void parseAttrs(Attributes attrs) {
			String name = attrs.getValue(NAME_ATTRIB);
			String displayNameID = attrs.getValue(DISPLAY_NAME_ID_ATTRIB);
			String toolTipID = attrs.getValue(TOOL_TIP_ID_ATTRIB);
			String isNative = attrs.getValue(NATIVE_ATTRIB);

			boolean ok = true;
			if (StringUtil.isBlank(name)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
				ok = false;
			}
			if (checkDisplayNameID && StringUtil.isBlank(displayNameID)) {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED));
				ok = false;
			}

			if (!ok) {
				return;
			}

			classInfo = builder.createClassInfo();
			classInfo.setName(name);
			classInfo.setDisplayNameKey(displayNameID);
			classInfo.setToolTipKey(toolTipID);

			if (Boolean.TRUE.toString().equalsIgnoreCase(isNative)) {
				classInfo.setNative(true);
			} else if (Boolean.FALSE.toString().equalsIgnoreCase(isNative)) {
				classInfo.setNative(false);
			}

			try {
				builder.addClassInfo(classInfo);
			} catch (MetaDataException e) {
				errorHandler.semanticError(
						new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED));
			}
		}

		@Override
		public AbstractParseState startElement(String tagName) {
			if (tagName.equalsIgnoreCase(CONSTRUCTOR_TAG)) {
				return new ClassMethodState(classInfo, true);
			}
			if (tagName.equalsIgnoreCase(MEMBER_TAG)) {
				return new MemberState();
			}
			if (tagName.equalsIgnoreCase(METHOD_TAG)) {
				return new ClassMethodState(classInfo, false);
			}

			return super.startElement(tagName);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		@Override
		public void end() throws SAXException {
			super.end();
			classInfo = null;
		}

		protected class MemberState extends InnerParseState {

			@Override
			public void parseAttrs(Attributes attrs) {
				String name = attrs.getValue(NAME_ATTRIB);
				String displayNameID = attrs.getValue(DISPLAY_NAME_ID_ATTRIB);
				String toolTipID = attrs.getValue(TOOL_TIP_ID_ATTRIB);
				String dataType = attrs.getValue(DATA_TYPE_ATTRIB);

				boolean ok = true;
				if (StringUtil.isBlank(name)) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
					ok = false;
				}
				if (checkDisplayNameID && StringUtil.isBlank(displayNameID)) {
					errorHandler.semanticError(new MetaDataParserException(
							MetaDataParserException.DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED));
					ok = false;
				}
				if (dataType == null) {
					errorHandler.semanticError(
							new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_DATA_TYPE_REQUIRED));
					ok = false;
				}

				if (!ok) {
					return;
				}

				MemberInfo memberDefn = builder.createMemberInfo();
				memberDefn.setName(name);
				memberDefn.setDisplayNameKey(displayNameID);
				memberDefn.setToolTipKey(toolTipID);
				memberDefn.setDataType(dataType);
				memberDefn.setStatic(getBooleanAttrib(attrs, IS_STATIC_ATTRIB, false));

				try {
					builder.addMemberInfo(classInfo, memberDefn);
				} catch (MetaDataException e) {
					errorHandler.semanticError(
							new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED));
				}
			}
		}
	}

	class TriggerState extends InnerParseState {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(
		 * org.xml.sax.Attributes)
		 */
		@Override
		public void parseAttrs(Attributes attrs) throws XMLParserException {
			assert propDefn != null || slotDefn != null;

			String validatorName = attrs.getValue(VALIDATOR_ATTRIB);
			String targetElement = attrs.getValue(TARGET_ELEMENT_ATTRIB);

			if (!StringUtil.isBlank(validatorName)) {
				SemanticTriggerDefn triggerDefn = new SemanticTriggerDefn(validatorName);

				triggerDefn.setPreRequisite(getBooleanAttrib(attrs, PRE_REQUISITE_ATTRIB, false));
				if (!StringUtil.isBlank(targetElement)) {
					triggerDefn.setTargetElement(targetElement);
				}

				if (propDefn != null) {
					propDefn.getTriggerDefnSet().add(triggerDefn);
				}

				if (slotDefn != null) {
					slotDefn.getTriggerDefnSet().add(triggerDefn);
				}
			} else {
				errorHandler.semanticError(
						new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_VALIDATOR_NAME_REQUIRED));
			}
		}
	}

	/**
	 * Checks if dictionary contains a specified ChoiceSet with the name
	 * <code>choiceSetName</code>.
	 *
	 * @param choiceSetName the name of ChoiceSet to be checked.
	 * @return the validated choiceSet. If not found, return null.
	 */

	private ChoiceSet validateChoiceSet(String choiceSetName) {
		IChoiceSet choiceSet = dictionary.getChoiceSet(choiceSetName);
		if (choiceSet == null) {
			errorHandler.semanticError(
					new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_INVALID_CHOICE_TYPE));
			return null;
		}

		return (ChoiceSet) choiceSet;
	}

	/**
	 * Transfers trim option string to trim option value. The input value is defined
	 * in <code>ModelUtil</code> and can be one of:
	 *
	 * <ul>
	 * <li>NO_TRIM</li>
	 * <li>TRIM_EMPTY</li>
	 * <li>TRIM_NULL</li>
	 * <li>TRIM_EMPTY&TRIM_NULL</li>
	 * <li>NULL</li>
	 * </ul>
	 *
	 * @param trimOption the trim option.
	 * @return the trim option value.
	 */
	private int handleTrimOption(String trimOption) throws MetaDataParserException {

		// TODO: do some enhancement to enable textualPropertyType could
		// not trim, trim string space or trim empty space to null according to
		// the trim option.
		String[] options = trimOption.split(";"); //$NON-NLS-1$

		int value = XMLPropertyType.NO_VALUE;
		for (int i = 0; i < options.length; i++) {
			String option = options[i];

			if (NO_TRIM.equals(option)) {
				value |= XMLPropertyType.NO_TRIM_VALUE;
			} else if (TRIM_SPACE.equals(option)) {
				value |= XMLPropertyType.TRIM_SPACE_VALUE;
			} else if (TRIM_EMPTY_TO_NULL.equals(option)) {
				value |= XMLPropertyType.TRIM_EMPTY_TO_NULL_VALUE;
			} else {
				// invalid trim option.
				throw new MetaDataParserException(MetaDataParserException.DESIGN_EXCEPTION_INVALID_TRIM_OPTION);
			}
		}
		return value;
	}

	/**
	 * Does some actions when the meta data file is end.
	 *
	 * @throws MetaDataParserException
	 */

	@Override
	public void endDocument() throws MetaDataParserException {
		//
		if (!errorHandler.getErrors().isEmpty()) {
			throw new MetaDataParserException(errorHandler.getErrors());
		}

		try {
			dictionary.build();
		} catch (MetaDataException e) {
			errorHandler.semanticError(
					new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_BUILD_FAILED));
			throw new MetaDataParserException(errorHandler.getErrors());
		}
	}

}
