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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.extension.IStyleDeclaration;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Style;

/**
 * Represents the extension loader for peer extension.
 */

public class PeerExtensionLoader extends ExtensionLoader {

	/**
	 * The name of extension point.
	 */

	public static final String EXTENSION_POINT = "org.eclipse.birt.report.model.reportItemModel"; //$NON-NLS-1$

	private static final String ELEMENT_TAG = "reportItem"; //$NON-NLS-1$

	/**
	 * Constructs the extension loader for peer extension.
	 */

	public PeerExtensionLoader() {
		super(EXTENSION_POINT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ExtensionLoader#doLoad()
	 */
	protected void doLoad() {
		super.doLoad();

		// build all the extension definitions
		List<IElementDefn> extensions = MetaDataDictionary.getInstance().getExtensions();
		if (extensions == null || extensions.isEmpty())
			return;

		for (int i = 0; i < extensions.size(); i++) {
			ElementDefn defn = (ElementDefn) extensions.get(i);
			try {
				defn.build();
			} catch (MetaDataException e) {
				handleError(new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_EXTENSION_ERROR)
						.getMessage());
			}
		}
	}

	/**
	 * Load one extension.
	 * 
	 * @param extension one extension which extends the model extension point.
	 * @throws ExtensionException if error is found when loading extension
	 * @throws MetaDataException  if error encountered when adding the element to
	 *                            metadata dictionary.
	 */

	protected void loadExtension(IExtension extension) {
		IConfigurationElement[] configElements = extension.getConfigurationElements();

		PeerExtensionElementLoader loader = new PeerExtensionElementLoader();
		for (int i = 0; i < configElements.length; i++) {
			IConfigurationElement currentTag = configElements[i];
			if (ELEMENT_TAG.equals(currentTag.getName())) {
				loader.loadElement(currentTag);
			}
		}
	}

	class PeerExtensionElementLoader extends ExtensionElementLoader {

		protected static final String PROPERTY_TAG = "property"; //$NON-NLS-1$
		protected static final String CHOICE_TAG = "choice"; //$NON-NLS-1$
		protected static final String PROPERTY_GROUP_TAG = "propertyGroup"; //$NON-NLS-1$
		protected static final String PROPERTY_VISIBILITY_TAG = "propertyVisibility"; //$NON-NLS-1$

		protected static final String STYLE_PROPERTY_TAG = "styleProperty"; //$NON-NLS-1$
		protected static final String METHOD_TAG = "method"; //$NON-NLS-1$
		protected static final String ARGUMENT_TAG = "argument"; //$NON-NLS-1$
		protected static final String STYLE_TAG = "style"; //$NON-NLS-1$
		protected static final String ELEMENT_TYPE_TAG = "elementType"; //$NON-NLS-1$
		protected static final String OVERRIDE_PROPERTY_TAG = "overrideProperty";//$NON-NLS-1$
		protected static final String JAVA_DOC_TAG = "javaDoc"; //$NON-NLS-1$

		protected static final String NAME_ATTRIB = "name"; //$NON-NLS-1$
		protected static final String PROPERTY_NAME_ATTRIB = "propertyName";//$NON-NLS-1$
		protected static final String ALLOWEDCHOICES_ATTRIB = "allowedChoices";//$NON-NLS-1$
		protected static final String ALLOWEDUNITS_ATTRIB = "allowedUnits";//$NON-NLS-1$
		protected static final String DISPLAY_NAME_ID_ATTRIB = "displayNameID"; //$NON-NLS-1$
		protected static final String TYPE_ATTRIB = "type"; //$NON-NLS-1$
		protected static final String CAN_INHERIT_ATTRIB = "canInherit"; //$NON-NLS-1$
		protected static final String DEFAULT_VALUE_ATTRIB = "defaultValue"; //$NON-NLS-1$
		protected static final String VALUE_ATTRIB = "value"; //$NON-NLS-1$
		protected static final String VISIBILITY_ATTRIB = "visibility"; //$NON-NLS-1$
		protected static final String DEFAULT_DISPLAY_NAME_ATTRIB = "defaultDisplayName"; //$NON-NLS-1$
		protected static final String IS_ENCRYPTABLE_ATTRIB = "isEncryptable"; //$NON-NLS-1$

		protected static final String TOOL_TIP_ID_ATTRIB = "toolTipID"; //$NON-NLS-1$
		protected static final String RETURN_TYPE_ATTRIB = "returnType"; //$NON-NLS-1$
		protected static final String TAG_ID_ATTRIB = "tagID"; //$NON-NLS-1$
		protected static final String IS_STATIC_ATTRIB = "isStatic"; //$NON-NLS-1$
		protected static final String DEFAULT_STYLE_ATTRIB = "defaultStyle"; //$NON-NLS-1$
		protected static final String HAS_STYLE = "hasStyle"; //$NON-NLS-1$
		protected static final String IS_NAME_REQUIRED_ATTRIB = "isNameRequired"; //$NON-NLS-1$
		protected static final String EXTENDS_FROM_ATTRIB = "extendsFrom"; //$NON-NLS-1$
		protected static final String DETAIL_TYPE_ATTRIB = "detailType"; //$NON-NLS-1$
		protected static final String SUB_TYPE_ATTRIB = "subType"; //$NON-NLS-1$
		protected static final String IS_LIST_ATTRIB = "isList"; //$NON-NLS-1$
		private static final String HAS_OWN_MODEL = "hasOwnModel"; //$NON-NLS-1$
		private static final String USE_OWN_SEARCH = "useOwnSearch"; //$NON-NLS-1$

		private static final String CONTEXT_ATTRIB = "context"; //$NON-NLS-1$
		private static final String ALLOW_EXPRESSION_ATTRIB = "allowExpression"; //$NON-NLS-1$

		private static final String THEME_TYPE_ATTRIB = "themeType"; //$NON-NLS-1$

		/**
		 * List of the property types that are allowed for the extensions.
		 */

		List<IPropertyType> allowedPropertyTypes = null;

		/**
		 * List of the property types that are allowed for sub-type for the extensions.
		 */

		List<IPropertyType> allowedSubPropertyTypes = null;

		/**
		 * Loads the extended element and its properties.
		 * 
		 * @param elementTag the element tag
		 * @throws MetaDataException  if error encountered when adding the element to
		 *                            metadata dictionary.
		 * @throws ExtensionException if the class some attribute specifies can not be
		 *                            instanced.
		 */

		void loadElement(IConfigurationElement elementTag) {
			// load required parts
			String extensionName = elementTag.getAttribute(EXTENSION_NAME_ATTRIB);
			String className = elementTag.getAttribute(CLASS_ATTRIB);
			if (!checkRequiredAttribute(EXTENSION_NAME_ATTRIB, extensionName)
					|| !checkRequiredAttribute(CLASS_ATTRIB, className))
				return;

			// load optional parts
			String displayNameID = elementTag.getAttribute(DISPLAY_NAME_ID_ATTRIB);

			String defaultStyle = elementTag.getAttribute(DEFAULT_STYLE_ATTRIB);
			boolean hasStyle = getBooleanAttrib(elementTag, HAS_STYLE, true);
			boolean isNameRequired = getBooleanAttrib(elementTag, IS_NAME_REQUIRED_ATTRIB, false);
			String extendsFrom = elementTag.getAttribute(EXTENDS_FROM_ATTRIB);
			if (StringUtil.isBlank(extendsFrom))
				extendsFrom = ReportDesignConstants.EXTENDED_ITEM;

			IReportItemFactory factory = null;
			PeerExtensionElementDefn elementDefn = null;
			try {
				factory = (IReportItemFactory) elementTag.createExecutableExtension(CLASS_ATTRIB);

				elementDefn = new PeerExtensionElementDefn(extensionName, factory);
				elementDefn.setAbstract(false);
				elementDefn.setAllowsUserProperties(false);
				elementDefn.setCanExtend(true);
				elementDefn.setDisplayNameKey(displayNameID);
				elementDefn.setExtends(extendsFrom);
				elementDefn.setJavaClass(null);
				elementDefn.setSelector(defaultStyle);
				elementDefn.setHasStyle(hasStyle);

				if (isNameRequired)
					elementDefn.setNameOption(MetaDataConstants.REQUIRED_NAME);
				else
					elementDefn.setNameOption(MetaDataConstants.OPTIONAL_NAME);
				elementDefn.setNameSpaceID(Module.ELEMENT_NAME_SPACE);

				IConfigurationElement[] elements = elementTag.getChildren();
				for (int i = 0; i < elements.length; i++) {
					try {
						if (PROPERTY_TAG.equalsIgnoreCase(elements[i].getName())) {
							SystemPropertyDefn extPropDefn = loadProperty(elementTag, elements[i], elementDefn);
							// Unique check is performed in addProperty()
							if (extPropDefn != null)
								elementDefn.addProperty(extPropDefn);
						} else if (PROPERTY_VISIBILITY_TAG.equalsIgnoreCase(elements[i].getName())) {
							loadPropertyVisibility(elements[i], elementDefn);
						} else if (PROPERTY_GROUP_TAG.equalsIgnoreCase(elements[i].getName())) {
							loadPropertyGroup(elementTag, elements[i], elementDefn);
						} else if (STYLE_PROPERTY_TAG.equalsIgnoreCase(elements[i].getName())) {
							// StyleProperty
						} else if (METHOD_TAG.equalsIgnoreCase(elements[i].getName())) {
							ExtensionPropertyDefn extPropDefn = loadMethod(elementTag, elements[i], elementDefn);

							if (extPropDefn == null)
								continue;

							// Unique check is performed in addProperty()
							elementDefn.addProperty(extPropDefn);
						} else if (STYLE_TAG.equalsIgnoreCase(elements[i].getName())) {
							PredefinedStyle style = loadStyle(elementTag, elements[i]);

							MetaDataDictionary.getInstance().addPredefinedStyle(style);
						} else if (OVERRIDE_PROPERTY_TAG.equalsIgnoreCase(elements[i].getName())) {
							loadOverrideProperty(elements[i], elementDefn);
						}
					} catch (ExtensionException e) {
						handleError(e);
						continue;
					} catch (MetaDataException e) {
						handleError(e);
						continue;
					}
				}
			} catch (FrameworkException e) {
				handleError(new ExtensionException(new String[] { className },
						ExtensionException.DESIGN_EXCEPTION_FAILED_TO_CREATE_INSTANCE));
				return;
			}
			elementDefn.extensionPoint = EXTENSION_POINT;
			try {
				MetaDataDictionary.getInstance().addExtension(elementDefn);
			} catch (MetaDataException e) {
				handleError(e);
			}
			// extension side can define some style with values for their
			// extension element. They return those style values by
			// IStyleDeclaration. Model needs to convert the IStyleDeclaration
			// to Style instance and save those styles in metadata dictionary.
			IStyleDeclaration[] styles = factory.getFactoryStyles(extensionName);
			if (styles != null) {
				for (int i = 0; i < styles.length; i++) {
					if (styles[i] != null) {
						String styleName = styles[i].getName();

						if (StringUtil.isBlank(styleName)) {
							handleError(new ExtensionException(new String[] { extensionName },
									ExtensionException.DESIGN_EXCEPTION_EMPTY_STYLE_NAME));
							continue;
						}

						addDefaultStyleToMeta(styles[i]);
					}

				}
			}

			String themeType = elementTag.getAttribute(THEME_TYPE_ATTRIB);
			MetaDataDictionary.getInstance().addThemeType(elementDefn, themeType);
		}

		/**
		 * convert the IStyleDeclaration to Style instance, and add it into
		 * MetaDataDictionary.
		 * 
		 * @param defaultStyle the IStyleDeclaration need to be converted and added.
		 */
		private void addDefaultStyleToMeta(IStyleDeclaration defaultStyle) {

			MetaDataDictionary dd = MetaDataDictionary.getInstance();
			IElementDefn styleDefn = dd.getElement(MetaDataConstants.STYLE_NAME);
			boolean hasLocalValues = false;

			Style style = new Style();
			style.setName(defaultStyle.getName());

			List<IElementPropertyDefn> stylePropDefn = styleDefn.getLocalProperties();
			String propName = null;

			for (int i = 0; i < stylePropDefn.size(); i++) {
				PropertyDefn propDefn = (PropertyDefn) stylePropDefn.get(i);
				propName = propDefn.getName();
				Object value = defaultStyle.getProperty(propName);
				if (value == null)
					continue;

				try {
					propDefn.validateValue(null, style, value);
					style.setProperty(propName, value);

					if (!hasLocalValues)
						hasLocalValues = true;

				} catch (PropertyValueException e) {
					handleError(e.getLocalizedMessage());
				}

			}
			if (hasLocalValues)
				dd.addExtensionFactoryStyle(style);

		}

		/**
		 * Add overridden property property to element definition.
		 * 
		 * @param elementTag  the element tag
		 * @param elementDefn element definition
		 */

		void loadOverrideProperty(IConfigurationElement elementTag, PeerExtensionElementDefn elementDefn) {
			// load required parts

			String name = elementTag.getAttribute(PROPERTY_NAME_ATTRIB);
			if (!checkRequiredAttribute(PROPERTY_NAME_ATTRIB, name))
				return;

			String units = elementTag.getAttribute(ALLOWEDUNITS_ATTRIB);
			String choices = elementTag.getAttribute(ALLOWEDCHOICES_ATTRIB);

			boolean useOwnSearch = getBooleanAttrib(elementTag, USE_OWN_SEARCH, false);

			OverridePropertyInfo propInfo = new OverridePropertyInfo();
			if (useOwnSearch)
				propInfo.setUseOwnSearch(useOwnSearch);
			propInfo.setAllowedUnits(units);
			propInfo.setAllowedChoices(choices);

			elementDefn.setOverridePropertyInfo(name, propInfo);
		}

		/**
		 * Loads one property definition of the given element.
		 * 
		 * @param elementTag  the element tag
		 * @param propTag     the property tag
		 * @param elementDefn element definition
		 * @return the property definition
		 */

		ExtensionPropertyDefn loadProperty(IConfigurationElement elementTag, IConfigurationElement propTag,
				ExtensionElementDefn elementDefn) {
			// load required parts
			String name = propTag.getAttribute(NAME_ATTRIB);
			String type = propTag.getAttribute(TYPE_ATTRIB);
			if (!checkRequiredAttribute(NAME_ATTRIB, name) || !checkRequiredAttribute(TYPE_ATTRIB, type))
				return null;

			// load optional parts
			String displayNameID = propTag.getAttribute(DISPLAY_NAME_ID_ATTRIB);
			String canInherit = propTag.getAttribute(CAN_INHERIT_ATTRIB);
			String defaultValue = propTag.getAttribute(DEFAULT_VALUE_ATTRIB);
			String isEncrypted = propTag.getAttribute(IS_ENCRYPTABLE_ATTRIB);
			String defaultDisplayName = propTag.getAttribute(DEFAULT_DISPLAY_NAME_ATTRIB);
			String subType = propTag.getAttribute(SUB_TYPE_ATTRIB);
			// by default set it to 'string' type
			if (StringUtil.isBlank(subType))
				subType = IPropertyType.STRING_TYPE_NAME;
			MetaDataDictionary dd = MetaDataDictionary.getInstance();
			PropertyType propType = dd.getPropertyType(type);

			// not well-recognized or not supported by extension, fire error
			if (propType == null || !getAllowedPropertyTypes().contains(propType)) {
				handleError(new ExtensionException(new String[] { type },
						MetaDataException.DESIGN_EXCEPTION_INVALID_PROPERTY_TYPE));
				return null;
			}
			PropertyType subPropType = null;
			if (propType.getTypeCode() == IPropertyType.LIST_TYPE) {
				subPropType = MetaDataDictionary.getInstance().getPropertyType(subType);
				if (subPropType == null || !getAllowedSubPropertyTypes().contains(subPropType)) {
					handleError(new ExtensionException(new String[] { name, subType },
							MetaDataException.DESIGN_EXCEPTION_UNSUPPORTED_SUB_TYPE));
					return null;
				}
			}

			ExtensionPropertyDefn extPropDefn = new ExtensionPropertyDefn(
					((PeerExtensionElementDefn) elementDefn).getReportItemFactory().getMessages());

			boolean hasOwnModel = getBooleanAttrib(propTag, HAS_OWN_MODEL, true);
			boolean allowExpression = getBooleanAttrib(propTag, ALLOW_EXPRESSION_ATTRIB, false);

			extPropDefn.setName(name);
			extPropDefn.setDisplayNameID(displayNameID);
			extPropDefn.setType(propType);
			extPropDefn.setSubType(subPropType);
			extPropDefn.setIntrinsic(false);
			extPropDefn.setStyleProperty(false);
			extPropDefn.setDefaultDisplayName(defaultDisplayName);
			extPropDefn.setHasOwnModel(hasOwnModel);
			extPropDefn.setAllowExpression(allowExpression);

			if (!StringUtil.isBlank(canInherit))
				extPropDefn.setCanInherit(Boolean.valueOf(canInherit).booleanValue());

			if (!StringUtil.isBlank(isEncrypted))
				extPropDefn.setIsEncryptable(Boolean.valueOf(isEncrypted).booleanValue());

			List<IChoice> choiceList = new ArrayList<IChoice>();
			List<String> elementTypes = new ArrayList<String>();

			IConfigurationElement[] elements = propTag.getChildren();
			for (int k = 0; k < elements.length; k++) {
				if (CHOICE_TAG.equalsIgnoreCase(elements[k].getName())) {
					ExtensionChoice choiceDefn = new ExtensionChoice(
							((PeerExtensionElementDefn) elementDefn).getReportItemFactory().getMessages());
					if (loadChoice(elements[k], choiceDefn, extPropDefn))
						choiceList.add(choiceDefn);
				} else if (ELEMENT_TYPE_TAG.equalsIgnoreCase(elements[k].getName())) {
					elementTypes.add(loadElementType(elements[k]));
				}
			}

			// do some checks about the detail type
			String detailType = propTag.getAttribute(DETAIL_TYPE_ATTRIB);
			switch (propType.getTypeCode()) {
			case IPropertyType.CHOICE_TYPE:
				// can not define detail-type and own choice list
				// synchronously, neither can be empty synchronously
				if ((!StringUtil.isBlank(detailType) && choiceList.size() > 0)
						|| (StringUtil.isBlank(detailType) && choiceList.size() <= 0)) {
					handleError(new ExtensionException(new String[] { detailType },
							ExtensionException.DESIGN_EXCEPTION_INVALID_CHOICE_PROPERTY));
					return null;
				}
				if (choiceList.size() > 0) {
					Choice[] choices = new Choice[choiceList.size()];
					choiceList.toArray(choices);
					ChoiceSet choiceSet = new ChoiceSet();
					choiceSet.setChoices(choices);
					extPropDefn.setDetails(choiceSet);
				} else if (!StringUtil.isBlank(detailType)) {
					extPropDefn.setDetails(dd.getChoiceSet(detailType));
				}
				break;
			case IPropertyType.STRUCT_TYPE:
				boolean isList = getBooleanAttrib(propTag, IS_LIST_ATTRIB, false);
				extPropDefn.setIsList(isList);
				extPropDefn.setDetails(detailType);
				break;
			case IPropertyType.ELEMENT_REF_TYPE:
			case IPropertyType.LIST_TYPE:
				extPropDefn.setDetails(detailType);
				break;
			case IPropertyType.ELEMENT_TYPE:
			case IPropertyType.CONTENT_ELEMENT_TYPE:
				isList = getBooleanAttrib(propTag, IS_LIST_ATTRIB, false);
				extPropDefn.setIsList(isList);
				extPropDefn.setDetails(elementTypes);
				break;
			}

			// after loading the choices, then validates the default value
			if (!StringUtil.isBlank(defaultValue)) {
				try {
					Object value = extPropDefn.validateXml(null, null, defaultValue);
					extPropDefn.setDefault(value);
				} catch (PropertyValueException e) {
					handleError(new ExtensionException(new String[] { name, elementDefn.getName() },
							MetaDataException.DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE));
					return null;
				}
			}

			return extPropDefn;
		}

		/**
		 * Loads one choice.
		 * 
		 * @param choiceTag the choice tag
		 * @param choice    the extension choice set load
		 * @param propDefn  the property definition in which the choices are inserted
		 * @return true if the choice is correctly loaded and no error, otherwise false
		 * @throws ExtensionException if the class some attribute specifies can not be
		 *                            instanced.
		 */

		protected boolean loadChoice(IConfigurationElement choiceTag, ExtensionChoice choice, PropertyDefn propDefn) {
			// read required first
			String name = choiceTag.getAttribute(NAME_ATTRIB);
			if (!checkRequiredAttribute(NAME_ATTRIB, name))
				return false;

			// read optional
			String value = choiceTag.getAttribute(VALUE_ATTRIB);
			String displayNameID = choiceTag.getAttribute(DISPLAY_NAME_ID_ATTRIB);
			String defaultDisplayName = choiceTag.getAttribute(DEFAULT_DISPLAY_NAME_ATTRIB);

			choice.setName(name);
			if (propDefn.getTypeCode() != IPropertyType.CHOICE_TYPE) {
				try {
					Object validateValue = propDefn.validateXml(null, null, value);
					choice.setValue(validateValue);
				} catch (PropertyValueException e) {
					handleError(new ExtensionException(new String[] { value, propDefn.getName() },
							ExtensionException.DESIGN_EXCEPTION_INVALID_CHOICE_VALUE));
					return false;
				}
			} else
				choice.setValue(value);

			choice.setDisplayNameKey(displayNameID);
			choice.setDefaultDisplayName(defaultDisplayName);

			return true;
		}

		/**
		 * Loads one visibility rule of a system property definition.
		 * 
		 * @param propTag     the property tag
		 * @param elementDefn element definition
		 * @throws ExtensionException if the class some attribute specifies can not be
		 *                            instanced.
		 */

		void loadPropertyVisibility(IConfigurationElement propTag, ExtensionElementDefn elementDefn)
				throws ExtensionException {
			// load required parts first
			String name = propTag.getAttribute(NAME_ATTRIB);
			if (!checkRequiredAttribute(NAME_ATTRIB, name))
				return;

			// load optional parts
			String visible = propTag.getAttribute(VISIBILITY_ATTRIB);
			elementDefn.addPropertyVisibility(name, visible);
		}

		/**
		 * Loads the properties of one group.
		 * 
		 * @param elementTag   the element tag
		 * @param propGroupTag the property group tag
		 * @param elementDefn  element definition
		 * @param propList     the property list into which the new property is added.
		 * 
		 * @throws MetaDataException
		 */

		void loadPropertyGroup(IConfigurationElement elementTag, IConfigurationElement propGroupTag,
				ExtensionElementDefn elementDefn) throws MetaDataException {
			// read required parts first
			String groupName = propGroupTag.getAttribute(NAME_ATTRIB);
			if (!checkRequiredAttribute(NAME_ATTRIB, groupName))
				return;

			// read optional parts
			String displayNameID = propGroupTag.getAttribute(DISPLAY_NAME_ID_ATTRIB);
			String defaultDisplayName = propGroupTag.getAttribute(DEFAULT_DISPLAY_NAME_ATTRIB);

			IConfigurationElement[] elements = propGroupTag.getChildren();
			for (int i = 0; i < elements.length; i++) {
				if (PROPERTY_TAG.equalsIgnoreCase(elements[i].getName())) {
					ExtensionPropertyDefn extPropDefn = loadProperty(elementTag, elements[i], elementDefn);
					if (extPropDefn == null)
						continue;
					extPropDefn.setGroupName(groupName);
					extPropDefn.setGroupNameKey(displayNameID);
					extPropDefn.setGroupDefauleDisplayName(defaultDisplayName);
					elementDefn.addProperty(extPropDefn);
				}
			}
		}

		/**
		 * Loads one property definition of the given element.
		 * 
		 * @param elementTag  the element tag
		 * @param propTag     the property tag
		 * @param elementDefn element definition
		 * @return the property definition
		 * @throws ExtensionException if the class some attribute specifies can not be
		 *                            instanced.
		 */

		ExtensionPropertyDefn loadMethod(IConfigurationElement elementTag, IConfigurationElement propTag,
				ExtensionElementDefn elementDefn) {
			String name = propTag.getAttribute(NAME_ATTRIB);
			String displayNameID = propTag.getAttribute(DISPLAY_NAME_ID_ATTRIB);
			String toolTipID = propTag.getAttribute(TOOL_TIP_ID_ATTRIB);
			String returnType = propTag.getAttribute(RETURN_TYPE_ATTRIB);
			String context = propTag.getAttribute(CONTEXT_ATTRIB);
			boolean isStatic = getBooleanAttrib(propTag, IS_STATIC_ATTRIB, false);

			if (name == null) {
				handleError(new ExtensionException(new String[] {},
						MetaDataException.DESIGN_EXCEPTION_MISSING_METHOD_NAME));
				return null;
			}

			// Note that here ROM supports overloadding, while JavaScript not.
			// finds the method info if it has been parsed.

			MethodInfo methodInfo = new MethodInfo(false);

			methodInfo.setName(name);
			methodInfo.setDisplayNameKey(displayNameID);
			methodInfo.setReturnType(returnType);
			methodInfo.setToolTipKey(toolTipID);
			methodInfo.setStatic(isStatic);
			methodInfo.setContext(context);
			methodInfo.setElementDefn(elementDefn);

			IConfigurationElement[] elements = propTag.getChildren();
			ArgumentInfoList argumentList = null;

			for (int i = 0; i < elements.length; i++) {
				if (ARGUMENT_TAG.equalsIgnoreCase(elements[i].getName())) {
					ArgumentInfo argument = loadArgument(elementTag, elements[i], elementDefn);

					// cache the definition here so that the scriptable factory
					// can be retrieved later.

					argument.setElementDefn(elementDefn);

					if (argumentList == null)
						argumentList = new ArgumentInfoList();

					try {
						argumentList.addArgument(argument);
					} catch (MetaDataException e) {
						handleError(new ExtensionException(new String[] {},
								MetaDataException.DESIGN_EXCEPTION_DUPLICATE_ARGUMENT_NAME));
						return null;
					}
					continue;
				}

				if (JAVA_DOC_TAG.equalsIgnoreCase(elements[i].getName())) {
					String javaDoc = elements[i].getValue();
					methodInfo.setJavaDoc(javaDoc);
				}
			}

			methodInfo.addArgumentList(argumentList);
			return addDefnTo(elementDefn, methodInfo);
		}

		/**
		 * Loads one property definition of the given element.
		 * 
		 * @param elementTag  the element tag
		 * @param propTag     the property tag
		 * @param elementDefn element definition
		 * @return the property definition
		 */

		ArgumentInfo loadArgument(IConfigurationElement elementTag, IConfigurationElement propTag,
				ExtensionElementDefn elementDefn) {
			String name = propTag.getAttribute(NAME_ATTRIB);
			String tagID = propTag.getAttribute(TAG_ID_ATTRIB);
			String type = propTag.getAttribute(TYPE_ATTRIB);

			if (name == null)
				return null;

			ArgumentInfo argument = new ArgumentInfo();
			argument.setName(name);
			argument.setType(type);
			argument.setDisplayNameKey(tagID);

			return argument;
		}

		/**
		 * Loads one property definition of the given element.
		 * 
		 * @param elementTag the element tag
		 * @param propTag    the property tag
		 * @return the property definition
		 * @throws ExtensionException if the class some attribute specifies can not be
		 *                            instanced.
		 */

		PredefinedStyle loadStyle(IConfigurationElement elementTag, IConfigurationElement propTag) {
			// when add the style to the meta-data, checks will be done, such as
			// the unique and non-empty of the name, so do nothing here

			String name = propTag.getAttribute(NAME_ATTRIB);
			String displayNameID = propTag.getAttribute(DISPLAY_NAME_ID_ATTRIB);
			String type = propTag.getAttribute(TYPE_ATTRIB);

			PredefinedStyle style = new PredefinedStyle();
			style.setName(name);
			style.setDisplayNameKey(displayNameID);
			style.setType(type);
			return style;
		}

		/**
		 * Loads one element type name of the given element.
		 * 
		 * @param elementTag     the element tag
		 * @param elementTypeTag the element type tag
		 * @param elementDefn    element definition
		 * @return name of the element type
		 * @throws ExtensionException if the class some attribute specifies can not be
		 *                            instanced.
		 */

		String loadElementType(IConfigurationElement elementTypeTag) {
			// read required parts
			String name = elementTypeTag.getAttribute(NAME_ATTRIB);
			if (!checkRequiredAttribute(NAME_ATTRIB, name))
				return null;

			return name;
		}

		/**
		 * Determines whether the element type is invalid or not. Now only
		 * support(ReportItem, Column, Row, Cell, ListingGroup).
		 * 
		 * @param type the type
		 * @return true if the type is valid, otherwise false
		 */

		boolean isValidElementType(String type) {
			if (ReportDesignConstants.EXTENDED_ITEM.equalsIgnoreCase(type)
					|| ReportDesignConstants.COLUMN_ELEMENT.equalsIgnoreCase(type)
					|| ReportDesignConstants.ROW_ELEMENT.equalsIgnoreCase(type)
					|| ReportDesignConstants.CELL_ELEMENT.equalsIgnoreCase(type)
					|| ReportDesignConstants.GROUP_ELEMENT.equalsIgnoreCase(type))
				return true;
			return false;
		}

		/**
		 * Gets the allowed property types for the extensions.
		 * 
		 * @return the allowed property types for the extensions
		 */

		List<IPropertyType> getAllowedPropertyTypes() {
			if (allowedPropertyTypes != null)
				return allowedPropertyTypes;

			allowedPropertyTypes = new ArrayList<IPropertyType>();
			Iterator<IPropertyType> iter = MetaDataDictionary.getInstance().getPropertyTypes().iterator();
			while (iter.hasNext()) {
				IPropertyType propType = iter.next();
				int type = propType.getTypeCode();
				switch (type) {
				case IPropertyType.STRING_TYPE:
				case IPropertyType.NUMBER_TYPE:
				case IPropertyType.INTEGER_TYPE:
				case IPropertyType.DIMENSION_TYPE:
				case IPropertyType.COLOR_TYPE:
				case IPropertyType.CHOICE_TYPE:
				case IPropertyType.BOOLEAN_TYPE:
				case IPropertyType.EXPRESSION_TYPE:
				case IPropertyType.HTML_TYPE:
				case IPropertyType.RESOURCE_KEY_TYPE:
				case IPropertyType.URI_TYPE:
				case IPropertyType.DATE_TIME_TYPE:
				case IPropertyType.XML_TYPE:
				case IPropertyType.NAME_TYPE:
				case IPropertyType.FLOAT_TYPE:
				case IPropertyType.LITERAL_STRING_TYPE:
				case IPropertyType.LIST_TYPE:
				case IPropertyType.STRUCT_TYPE:
				case IPropertyType.ELEMENT_REF_TYPE:
				case IPropertyType.ELEMENT_TYPE:
				case IPropertyType.CONTENT_ELEMENT_TYPE:
					allowedPropertyTypes.add(propType);
					break;
				default:
					break;
				}
			}

			return allowedPropertyTypes;
		}

		/**
		 * Gets the allowed property types for the extensions.
		 * 
		 * @return the allowed property types for the extensions
		 */

		List<IPropertyType> getAllowedSubPropertyTypes() {
			if (allowedSubPropertyTypes != null && !allowedSubPropertyTypes.isEmpty())
				return allowedSubPropertyTypes;

			allowedSubPropertyTypes = new ArrayList<IPropertyType>();
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
					allowedSubPropertyTypes.add(propType);
				}
			}

			return allowedSubPropertyTypes;
		}
	}

	/**
	 * Generates a property with the given method info.
	 * 
	 * @param elementDefn the element definition to handler
	 * @param methodInfo  the method info to add
	 * @return the generated property definition
	 */

	private ExtensionPropertyDefn addDefnTo(ExtensionElementDefn elementDefn, MethodInfo methodInfo) {
		ExtensionPropertyDefn extPropDefn = new ExtensionPropertyDefn(
				((PeerExtensionElementDefn) elementDefn).getReportItemFactory().getMessages());

		PropertyType typeDefn = MetaDataDictionary.getInstance().getPropertyType(IPropertyType.SCRIPT_TYPE);

		String name = methodInfo.getName();
		String displayNameID = methodInfo.getDisplayNameKey();

		extPropDefn.setName(name);
		extPropDefn.setDisplayNameID(displayNameID);
		extPropDefn.setType(typeDefn);
		extPropDefn.setGroupNameKey(null);
		extPropDefn.setCanInherit(true);
		extPropDefn.setIntrinsic(false);
		extPropDefn.setStyleProperty(false);
		extPropDefn.setDetails(methodInfo);
		extPropDefn.setContext(methodInfo.getContext());

		return extPropDefn;
	}

}
