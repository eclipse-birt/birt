/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.metadata;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.MetaDataReaderException;
import org.eclipse.birt.report.model.api.scripts.IScriptableObjectClassInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.validators.IValueValidator;
import org.eclipse.birt.report.model.validators.AbstractSemanticValidator;

/**
 * Global, shared dictionary of design meta data. Meta-data describes each
 * design element and its properties. The information is shared because all
 * designs share the same BIRT-provided set of design elements. See the
 * {@link IElementDefn}class for more detailed information.
 * 
 * <h2>Meta-data Information</h2> The application must first populate the
 * elements from a meta-data XML file using a parser defined in
 * <code>MetaDataReader</code>. The meta-data defined here includes:
 * 
 * <p>
 * <dl>
 * <dt><strong>Property Types </strong></dt>
 * <dd>The set of data types supported for properties. BIRT supports a rich
 * variety of property types that include the basics such as strings and
 * numbers, as well as specialized types such as dimensions, points and colors.
 * See the {@link PropertyType PropertyType}class.</dd>
 * 
 * <dt><strong>Element Definitions </strong></dt>
 * <dd>Describes the BIRT-defined elements. The element definition includes the
 * list of properties defined on that type, and optional properties "inherited"
 * from the style. See the {@link IElementDefn}class.</dd>
 * 
 * <dt><strong>Standard Styles </strong></dt>
 * <dd>BIRT defines a set of standard styles. The set of styles goes along with
 * the set of elements. For example, a list header has a standard style as does
 * a list footer.</dd>
 * 
 * <dt><strong>Class Definitions </strong></dt>
 * <dd>Describes the object types that are defined by JavaScript and BIRT. The
 * class definition includes constructor, members and methods. See the
 * {@link ClassInfo ClassDefn}class.</dd>
 * </dl>
 * <p>
 * 
 * <h2>Enabling Object IDs</h2> The model may be used in the web environment in
 * which it is necessary to identify elements using a unique ID separate from
 * their object pointer. The
 * {@link org.eclipse.birt.report.model.core.Module}class maintains the object
 * ID counter, as well as an id-to-element map. Because the map is costly, it is
 * enabled only if ID support is enabled in the data dictionary object.
 * 
 * <h2>Lifecycle</h2>
 * 
 * Meta-data is built-up in a three-step process.
 * <p>
 * <ul>
 * <li><strong>Internal tables </strong>-- Some of the meta-data comes from
 * tables defined in code. Such tables exist to define parts of the system that
 * must match Java code, and which are needed to bootstrap the next step.</li>
 * <p>
 * <li><strong>Meta-data file </strong>-- Most of the data comes from the
 * meta-data XML file. This information includes the list of elements, their
 * propValues, and more. The meta-data file contains message catalog IDs for any
 * strings that need to be displayed to the user, so that they can be localized
 * in the next step.</li>
 * <p>
 * <li><strong>Build </strong>-- The build step completes the process. The build
 * step finds and caches the base type for each element, uses the message IDs to
 * read the actual message from a message catalog, and so on.</li>
 * </ul>
 */

public final class MetaDataDictionary implements IMetaDataDictionary {

	/**
	 * The file name of ROM.DEF
	 */

	private static final String ROM_DEF_FILE_NAME = "rom.def"; //$NON-NLS-1$

	/**
	 * The one and only metadata dictionary.
	 */

	private static MetaDataDictionary instance = new MetaDataDictionary();

	private static boolean initialized = false;

	/**
	 * Provides the list of design elements keyed by their internal names.
	 */

	private HashMap<String, IElementDefn> elementNameMap = new LinkedHashMap<String, IElementDefn>();

	/**
	 * Provides the list of design elements keyed by their xml names.
	 */

	private Map<String, IElementDefn> elementXmlNameMap = new HashMap<String, IElementDefn>();

	/**
	 * Cached link to the style definition. The style definition is frequently used,
	 * and so caching it saves unnecessary lookups.
	 */

	private ElementDefn style = null;

	/**
	 * Information about property types. Keyed by the property type ID numeric
	 * value. See MetaDataConstants for a list of the property types.
	 * <p>
	 * The order and number of items in this list must match the corresponding
	 * constants in the MetaDataConstants class.
	 */

	private PropertyType[] propertyTypes = new PropertyType[IPropertyType.TYPE_COUNT];

	/**
	 * Map of choice types. A choice type is a named set of property choices. It
	 * provides additional information for properties of the type choice.
	 */

	private HashMap<String, IChoiceSet> choiceSets = new HashMap<String, IChoiceSet>();

	/**
	 * Map of structures. A structure represents an object in Java. It describes the
	 * members of the object that are to be visible to the UI. Such objects are
	 * generally kept property defined as a structure list.
	 */

	private HashMap<String, IStructureDefn> structures = new HashMap<String, IStructureDefn>();

	/**
	 * Map of classes. A class represents an Object in Script. It describes the
	 * constructors, members and methods.
	 */

	private Map<String, IClassInfo> classes = new LinkedHashMap<String, IClassInfo>();

	/**
	 * Whether to apply element ids to newly created elements. This feature is used
	 * for the web environment, but not for the Eclipse environment.
	 * 
	 * @deprecated
	 */

	private boolean useElementID = false;

	/**
	 * The list of predefined styles. This list only identifies the styles
	 * themselves, but not give their properties.
	 * <p>
	 * Contents are of type PredefinedStyle.
	 */

	private HashMap<String, IPredefinedStyle> predefinedStyles = new HashMap<String, IPredefinedStyle>();

	/**
	 * The map to store the type and value list. The key is the type of predefined
	 * style. Value is the list of predefined styles whose type is the specified key
	 * value.
	 */
	private HashMap<String, List<IPredefinedStyle>> predefinedStyleTypes = new HashMap<String, List<IPredefinedStyle>>();

	/**
	 * The map to store the element internal name and corresponding theme type. The
	 * key is the internal name of the element. Value is the report item theme type
	 * that can apply on the element.
	 */
	private HashMap<String, String> themeTypes = new HashMap<String, String>();

	/**
	 * Map of property value validators, holding the validator name as key. Each of
	 * this map is the instance of <code>IValueValidator</code>.
	 */

	private Map<String, IValueValidator> valueValidators = new HashMap<String, IValueValidator>();

	/**
	 * Map of semantic validators, holding the validator name as key. Each of this
	 * map is the instance of <code>AbstractSemanticValidator</code>.
	 */

	private Map<String, AbstractSemanticValidator> semanticValidators = new HashMap<String, AbstractSemanticValidator>();

	/**
	 * Whether to use validation trigger. This feature will perform validation once
	 * one property or slot is changed.
	 */

	private boolean useValidationTrigger = false;

	/**
	 * 
	 */

	private Map<String, IMethodInfo> functions = null;

	/**
	 * Singleton class, constructor is private.
	 */

	private MetaDataDictionary() {
		// Create the list of property types.
		//
		// The meta-data file will provide additional information for these
		// types.
		addPropertyType(new StringPropertyType());
		addPropertyType(new LiteralStringPropertyType());
		addPropertyType(new NumberPropertyType());
		addPropertyType(new IntegerPropertyType());
		addPropertyType(new DimensionPropertyType());
		addPropertyType(new ColorPropertyType());
		addPropertyType(new ChoicePropertyType());
		addPropertyType(new BooleanPropertyType());
		addPropertyType(new ExpressionPropertyType());
		addPropertyType(new HTMLPropertyType());
		addPropertyType(new ResourceKeyPropertyType());
		addPropertyType(new URIPropertyType());
		addPropertyType(new DateTimePropertyType());
		addPropertyType(new XMLPropertyType());
		addPropertyType(new NamePropertyType());
		addPropertyType(new FloatPropertyType());
		addPropertyType(new ElementRefPropertyType());
		addPropertyType(new StructPropertyType());
		addPropertyType(new ExtendsPropertyType());
		addPropertyType(new ScriptPropertyType());
		addPropertyType(new StructRefPropertyType());
		addPropertyType(new ListPropertyType());
		addPropertyType(new MemberKeyPropertyType());
		addPropertyType(new ElementPropertyType());
		addPropertyType(new ContentElementPropertyType());
		addPropertyType(new ULocalePropertyType());
	}

	/**
	 * Adds a property type to the dictionary.
	 * 
	 * @param propType the property type to add
	 */

	private void addPropertyType(PropertyType propType) {
		int typeCode = propType.getTypeCode();
		assert propertyTypes[typeCode] == null;
		propertyTypes[typeCode] = propType;
	}

	/**
	 * Returns the meta-data dictionary. This dictionary is shared by all open
	 * designs.
	 * 
	 * @return The meta-data dictionary.
	 */

	public static MetaDataDictionary getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#getElement
	 * (java.lang.String)
	 */
	public IElementDefn getElement(String name) {
		return elementNameMap.get(name) == null ? ExtensionManager.getInstance().getElement(name)
				: elementNameMap.get(name);
	}

	/**
	 * Finds the element definition by its xm name.
	 * 
	 * @param name The element xml name.
	 * @return The element definition, or null if the name was not found in the
	 *         dictionary.
	 */

	public IElementDefn getElementByXmlName(String name) {

		return elementXmlNameMap.get(name) == null ? ExtensionManager.getInstance().getElementByXmlName(name)
				: elementXmlNameMap.get(name);
	}

	/**
	 * Internal method to build the cached semantic data for the dictionary. Looks
	 * up "extends" references, looks up display names given message IDs, and so on.
	 * 
	 * @throws MetaDataException if any build error occurs
	 */

	void build() throws MetaDataException {
		buildPropertyTypes();
		buildElementDefinitions();
		buildXmlNameMaps();
		validateConstants();
		buildStructures();
	}

	/**
	 * Creates a map. The key is the element xml-name. The value is the element
	 * definition.
	 */

	private void buildXmlNameMaps() throws MetaDataException {
		// Build the element metadata.

		// for extension element, the tag name is always "extended-item".

		Iterator<IElementDefn> iter = elementNameMap.values().iterator();
		while (iter.hasNext()) {
			ElementDefn tmpDefn = (ElementDefn) iter.next();
			String tmpXmlName = tmpDefn.getXmlName();

			// TODO: also check the validation of the element XML name and
			// whether it is an extended item. If it is ROM elements, throw
			// exception.Otherwise, just ignore extension XML name.

			elementXmlNameMap.put(tmpXmlName, tmpDefn);
		}
	}

	/**
	 * Private method to validate the various meta-data constants used in this
	 * build.
	 * 
	 * @throws MetaDataException if any of the validation fails.
	 */

	private void validateConstants() throws MetaDataException {
		for (int i = 0; i < propertyTypes.length; i++)
			assert propertyTypes[i].getTypeCode() == i;

		validateElement(MetaDataConstants.STYLE_NAME);
		validateElement(MetaDataConstants.REPORT_ELEMENT_NAME);
		validateElement(MetaDataConstants.REPORT_DESIGN_NAME);
	}

	/**
	 * Validates that an element name is valid. Throws an exception if the name is
	 * not valid.
	 * 
	 * @param name the element name to validate
	 * @throws MetaDataException if the name is not valid
	 */

	private void validateElement(String name) throws MetaDataException {
		if (getElement(name) == null)
			throw new MetaDataException(new String[] { name }, MetaDataException.DESIGN_EXCEPTION_ELEMENT_NAME_CONST);
	}

	/**
	 * Builds the list of property types.
	 */

	private void buildPropertyTypes() {
		// Build the property type information.

		for (int i = 0; i < propertyTypes.length; i++)
			propertyTypes[i].build();

	}

	/**
	 * Builds the list of element definitions.
	 * 
	 * @throws MetaDataException if the build of the element definition fails.
	 */

	private void buildElementDefinitions() throws MetaDataException {
		// Build the style first, since most other elements will
		// reference it.

		style = (ElementDefn) getElement(MetaDataConstants.STYLE_NAME);
		if (style == null)
			throw new MetaDataException(MetaDataException.DESIGN_EXCEPTION_STYLE_TYPE_MISSING);
		style.build();

		ElementDefn report = (ElementDefn) getElement(ReportDesignConstants.REPORT_DESIGN_ELEMENT);
		if (report == null)
			throw new MetaDataException(MetaDataException.DESIGN_EXCEPTION_CONSTRUCTOR_EXISTING);
		report.build();

		// Build the element metadata.

		Iterator<IElementDefn> iter = elementNameMap.values().iterator();
		while (iter.hasNext()) {
			ElementDefn tmpDefn = (ElementDefn) iter.next();
			tmpDefn.build();
		}
	}

	/**
	 * Private method to build the structure definitions.
	 * 
	 * @throws MetaDataException
	 */

	private void buildStructures() throws MetaDataException {
		Iterator<IStructureDefn> iter = structures.values().iterator();
		while (iter.hasNext()) {
			StructureDefn type = (StructureDefn) iter.next();
			type.build();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#
	 * getPropertyType(int)
	 */
	public PropertyType getPropertyType(int type) {
		assert type >= 0 && type < propertyTypes.length;
		return propertyTypes[type];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#
	 * getPropertyTypes()
	 */
	public List<IPropertyType> getPropertyTypes() {
		List<IPropertyType> values = new ArrayList<IPropertyType>();
		values.addAll(Arrays.asList(propertyTypes));
		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#
	 * getPropertyType(java.lang.String)
	 */
	public PropertyType getPropertyType(String xmlName) {
		for (int i = 0; i < propertyTypes.length; i++)
			if (propertyTypes[i].getName().equalsIgnoreCase(xmlName))
				return propertyTypes[i];

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#getStyle()
	 */
	public IElementDefn getStyle() {
		return style;
	}

	/**
	 * Resets the dictionary. Clears the cached data and the resources in extension
	 * manager. Used primarily for testing.
	 */

	public synchronized static void reset() {
		System.out.println("resetting MetaDataDictionary");
		instance = new MetaDataDictionary();
		ExtensionManager.releaseInstance();
		initialized = false;
	}

	/**
	 * Gets the status that identifies whether the extensions defined by all Model
	 * extension points are loaded or not.
	 * 
	 * @return true if extensions are loaded, otherwise false
	 */
	public synchronized static boolean isIntialized() {
		return initialized;
	}

	/**
	 * Initializes the meta-data system and loads all extensions which implements
	 * the extension pointers the model defines. The application must call this
	 * method once (and only once) before opening or creating a design. It is the
	 * application's responsibility because the application will choose the location
	 * to store the definition file, and that location may differ for different
	 * applications.
	 * 
	 * @param is stream for reading the "rom.def" file that provides the meta-data
	 *           for the system
	 * @throws MetaDataParserException
	 * @throws MetaDataReaderException if error occurs during read the meta-data
	 *                                 file.
	 */

	public synchronized static void initialize() {
		if (initialized) {
			return;
		}

		System.out.println("initializing MetaDataDictionary from " + ROM_DEF_FILE_NAME);
		try {
			InputStream input = (ReportDesign.class.getResourceAsStream(ROM_DEF_FILE_NAME));
			MetaDataReader.read(input);
			ExtensionManager.getInstance().initialize();
		} catch (MetaDataParserException e) {
			// we provide logger, so do not assert.
		} finally {
			initialized = true;
			MetaLogManager.shutDown();
		}
	}

	/**
	 * Adds an element type to the dictionary. Must be done before the build step.
	 * The element type name must be unique.
	 * 
	 * @param type the element type to add
	 * @throws MetaDataException if exception occurs when adding the element
	 *                           definition.
	 * 
	 */

	void addElementDefn(ElementDefn type) throws MetaDataException {
		String name = type.getName();
		System.out.println("adding ElementDefn '" + name + "' to MetaDataDictionary");
		if (StringUtil.isBlank(name))
			throw new MetaDataException(MetaDataException.DESIGN_EXCEPTION_MISSING_ELEMENT_NAME);
		if (elementNameMap.containsKey(name))
			throw new MetaDataException(new String[] { name },
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_ELEMENT_NAME);
		elementNameMap.put(name, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#
	 * enableElementID()
	 */
	public void enableElementID() {
		useElementID = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#useID()
	 */
	public boolean useID() {
		return useElementID;
	}

	/**
	 * Adds a predefined style to the dictionary.
	 * 
	 * @param style the predefined style
	 * @throws MetaDataException if exception occur when adding the style, it may be
	 *                           because the style missing its name or an style with
	 *                           the same name already exists.
	 */

	void addPredefinedStyle(PredefinedStyle style) throws MetaDataException {
		String name = style.getName();

		if (StringUtil.isBlank(name))
			throw new MetaDataException(MetaDataException.DESIGN_EXCEPTION_MISSING_STYLE_NAME);

		String key = name.toLowerCase();
		if (predefinedStyles.get(key) != null)
			throw new MetaDataException(new String[] { name }, MetaDataException.DESIGN_EXCEPTION_DUPLICATE_STYLE_NAME);
		predefinedStyles.put(key, style);

		String type = style.getType();
		if (!StringUtil.isBlank(type)) {
			List<IPredefinedStyle> styles = predefinedStyleTypes.get(type);
			if (styles == null) {
				styles = new ArrayList<IPredefinedStyle>();
				predefinedStyleTypes.put(type, styles);

				// from ROM defined elements. The theme type is the internal
				// element name.
				if (elementNameMap.get(type) != null) {
					themeTypes.put(type, type);
				}
			}
			if (!styles.contains(style))
				styles.add(style);
		}
	}

	/**
	 * Finds a predefined style definition.
	 * 
	 * @param name the internal name of the predefined style
	 * @return the predefined style, or null if the style is not defined
	 */

	public IPredefinedStyle getPredefinedStyle(String name) {
		if (StringUtil.isBlank(name))
			return null;
		String key = name.toLowerCase();
		return predefinedStyles.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#isEmpty()
	 */
	public boolean isEmpty() {
		return elementNameMap.isEmpty() && predefinedStyles.isEmpty();
	}

	/**
	 * Adds a choice set to the dictionary.
	 * 
	 * @param choiceSet the choice set to add
	 * @throws MetaDataException if the choice set is not valid
	 */

	void addChoiceSet(ChoiceSet choiceSet) throws MetaDataException {

		String name = choiceSet.getName();

		if (StringUtil.isBlank(name))
			throw new MetaDataException(MetaDataException.DESIGN_EXCEPTION_MISSING_CHOICE_SET_NAME);
		if (choiceSets.containsKey(name))
			throw new MetaDataException(new String[] { name },
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_CHOICE_SET_NAME);

		choiceSets.put(name, choiceSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#getChoiceSet
	 * (java.lang.String)
	 */
	public IChoiceSet getChoiceSet(String choiceSetName) {
		// for the backward compatibility issue

		String newName = choiceSetName;
		if (DesignChoiceConstants.CHOICE_AGGREGATION_FUNCTION.equalsIgnoreCase(newName)) {
			newName = DesignChoiceConstants.CHOICE_AGGREGATION_FUNCTION;
		}

		IChoiceSet choiceSet = choiceSets.get(newName);
		if (choiceSet != null) {
			return choiceSet;
		}
		return ExtensionManager.getInstance().getChoiceSet(choiceSetName);
	}

	/**
	 * Adds a structure definition to the dictionary.
	 * 
	 * @param struct the structure definition to add
	 * @throws MetaDataException if the structure definition is not valid
	 */

	void addStructure(StructureDefn struct) throws MetaDataException {
		String name = struct.getName();
		if (StringUtil.isBlank(name))
			throw new MetaDataException(MetaDataException.DESIGN_EXCEPTION_MISSING_STRUCT_NAME);
		if (structures.containsKey(name))
			throw new MetaDataException(new String[] { name },
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_STRUCT_NAME);
		structures.put(name, struct);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#getStructure
	 * (java.lang.String)
	 */
	public IStructureDefn getStructure(String name) {
		return structures.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#getElements ()
	 */
	public List<IElementDefn> getElements() {
		return new ArrayList<IElementDefn>(elementNameMap.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#getStructures
	 * ()
	 */
	public List<IStructureDefn> getStructures() {
		return new ArrayList<IStructureDefn>(structures.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#
	 * getPredefinedStyles()
	 */
	public List<IPredefinedStyle> getPredefinedStyles() {
		return new ArrayList<IPredefinedStyle>(predefinedStyles.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#getClasses ()
	 */
	public List<IClassInfo> getClasses() {
		return new ArrayList<IClassInfo>(classes.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#getClass
	 * (java.lang.String)
	 */
	public IClassInfo getClass(String name) {
		IClassInfo classInfo = classes.get(name);
		if (classInfo != null) {
			return classInfo;
		}
		return ExtensionManager.getInstance().getClassInfo(name);
	}

	/**
	 * Adds the class definition to the dictionary.
	 * 
	 * @param classDefn the definition of the class to add
	 * @throws MetaDataException if the class name is not provided or duplicate.
	 */

	void addClass(ClassInfo classDefn) throws MetaDataException {
		if (StringUtil.isBlank(classDefn.getName()))
			throw new MetaDataException(MetaDataException.DESIGN_EXCEPTION_MISSING_CLASS_NAME);

		if (classes.get(classDefn.getName()) != null)
			throw new MetaDataException(new String[] { classDefn.getName() },
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_CLASS_NAME);

		classes.put(classDefn.getName(), classDefn);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#getExtensions
	 * ()
	 */
	public List<IElementDefn> getExtensions() {
		return ExtensionManager.getInstance().getExtensions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#getExtension
	 * (java.lang.String)
	 */
	public IElementDefn getExtension(String name) {
		return ExtensionManager.getInstance().getElement(name);
	}

	/**
	 * Adds the extension definition to the dictionary.
	 * 
	 * @param extDefn the definition of the extension element to add
	 * @throws MetaDataException if the extension name is not provided or duplicate.
	 */

	void addExtension(ExtensionElementDefn extDefn) throws MetaDataException {
		ExtensionManager.getInstance().addExtension(extDefn);
	}

	/**
	 * Caches and builds oda extension definition.
	 * 
	 * @param extDefn
	 */
	public void cacheOdaExtension(String extensionID, ExtensionElementDefn extDefn) throws MetaDataException {
		ExtensionManager.getInstance().cacheOdaExtension(extensionID, extDefn);
	}

	/**
	 * Add a new validator to the dictionary.
	 * 
	 * @param validator a new validator.
	 * @throws MetaDataException if the validator missing its name or its name
	 *                           duplicates with an exsiting one.
	 */

	void addValueValidator(IValueValidator validator) throws MetaDataException {
		String name = validator.getName();
		if (StringUtil.isBlank(name))
			throw new MetaDataException(MetaDataException.DESIGN_EXCEPTION_MISSING_VALIDATOR_NAME);
		if (valueValidators.containsKey(name))
			throw new MetaDataException(new String[] { name },
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_VALIDATOR_NAME);

		valueValidators.put(name, validator);
	}

	/**
	 * Return a property value validator given its name.
	 * 
	 * @param name name of the value validator.
	 * @return A property value validator.
	 */

	public IValueValidator getValueValidator(String name) {
		IValueValidator validator = valueValidators.get(name);
		if (validator != null) {
			return validator;
		}
		return ExtensionManager.getInstance().getValueValidator(name);

	}

	/**
	 * Adds the semantic validator.
	 * 
	 * @param validator the validator to add
	 * @throws MetaDataException if the validator name is missing or duplicates.
	 */

	public void addSemanticValidator(AbstractSemanticValidator validator) throws MetaDataException {
		String name = validator.getName();
		if (StringUtil.isBlank(name))
			throw new MetaDataException(MetaDataException.DESIGN_EXCEPTION_MISSING_VALIDATOR_NAME);

		if (semanticValidators.containsKey(name))
			throw new MetaDataException(new String[] { name },
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_VALIDATOR_NAME);

		semanticValidators.put(name, validator);
	}

	/**
	 * Returns the semantic validator given the name.
	 * 
	 * @param name the validator name
	 * @return the semantic validator with the given name. Or return
	 *         <code>null</code>, the there is no validator with the given name.
	 */

	public AbstractSemanticValidator getSemanticValidator(String name) {
		return semanticValidators.get(name);
	}

	/**
	 * Returns whether to use validation trigger feature.
	 * 
	 * @return whether to use validation trigger feature
	 */

	public boolean useValidationTrigger() {
		return useValidationTrigger;
	}

	/**
	 * Enables the validation trigger feature.
	 * 
	 * @param useValidationTrigger the flag to set
	 */

	public void setUseValidationTrigger(boolean useValidationTrigger) {
		this.useValidationTrigger = useValidationTrigger;
	}

	/**
	 * Returns the encryption helper with the extension id.
	 * 
	 * @param id the extension id for the encryption helper to find
	 * @return the encryption helper if found, otherwise false.
	 */

	public IEncryptionHelper getEncryptionHelper(String id) {
		return ExtensionManager.getInstance().getEncryptionHelper(id);
	}

	/**
	 * Gets all the encryption helpers.
	 * 
	 * @return the list of all the encryption helpers
	 */
	public List<IEncryptionHelper> getEncryptionHelpers() {
		return ExtensionManager.getInstance().getEncryptionHelpers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#
	 * getEncryptionHelperIDs()
	 */
	public List<String> getEncryptionHelperIDs() {
		return ExtensionManager.getInstance().getEncryptionHelperIDs();
	}

	/**
	 * Returns the encryption helper with the extension id.
	 * 
	 * @param id the extension id for the encryption helper to find
	 * @return the encryption helper if found, otherwise false.
	 */

	public IEncryptionHelper getDefaultEncryptionHelper() {
		return ExtensionManager.getInstance().getDefaultEncryptionHelper();
	}

	/**
	 * Gets the default encryption id.
	 * 
	 * @return the ID of the default encryption helper
	 */
	public String getDefaultEncryptionHelperID() {
		return ExtensionManager.getInstance().getDefaultEncryptionHelperID();
	}

	/**
	 * Sets the default encryption id.
	 * 
	 * @param encryptionID
	 */
	public void setDefaultEncryptionHelper(String encryptionID) {
		ExtensionManager.getInstance().setDefaultEncryptionHelper(encryptionID);
	}

	/**
	 * Sets the encryption helper.
	 * 
	 * @param id               the extension id
	 * @param encryptionHelper the encryption helper to set
	 * @throws MetaDataException
	 */

	void addEncryptionHelper(String id, IEncryptionHelper encryptionHelper) throws MetaDataException {
		ExtensionManager.getInstance().addEncryptionHelper(id, encryptionHelper);
	}

	/**
	 * Returns the factory to create scriptable class for ROM defined elements.
	 * 
	 * @return the scriptable factory
	 */

	public IScriptableObjectClassInfo getScriptableFactory() {
		return ExtensionManager.getInstance().getScriptableFactory();
	}

	/**
	 * Sets the factory to create scriptable class for ROM defined elements.
	 * 
	 * @param scriptableFactory the scriptable factory to set
	 */

	void setScriptableFactory(IScriptableObjectClassInfo scriptableFactory) {
		ExtensionManager.getInstance().setScriptableFactory(scriptableFactory);
	}

	/**
	 * return the predefined styls instance of the extension element.
	 * 
	 * @return the list of style intance for the extension element.
	 */
	public List<Style> getExtensionFactoryStyles() {
		return ExtensionManager.getInstance().getExtensionFactoryStyles();
	}

	/**
	 * add the predefined style into the list.
	 * 
	 * @param style
	 */
	void addExtensionFactoryStyle(Style style) {
		ExtensionManager.getInstance().addExtensionFactoryStyle(style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#getFunctions
	 * ()
	 */

	public List<IMethodInfo> getFunctions() {
		if (functions == null) {
			functions = new LinkedHashMap<String, IMethodInfo>();
			List<String> names = new ArrayList<String>();
			IChoice[] choices = getChoiceSet(DesignChoiceConstants.CHOICE_AGGREGATION_FUNCTION).getChoices();
			for (int i = 0; i < choices.length; i++) {
				IChoice choice = choices[i];
				names.add(choice.getName());
			}

			IClassInfo clazz = getClass(TOTAL_CLASS_NAME);
			addMatchedFunctions(functions, clazz.getMethods(), names);

			names.clear();
		}

		assert functions != null;

		List<IMethodInfo> retList = new ArrayList<IMethodInfo>();
		retList.addAll(functions.values());
		return retList;
	}

	/**
	 * Adds functions in <code>clazzMethods</code> to <code>methods</code> if
	 * methods names exist in <code>names</code>.
	 * 
	 * @param methods      the return methods
	 * @param clazzMethods the methods on the class
	 * @param names        the possible method names
	 */

	private static void addMatchedFunctions(Map<String, IMethodInfo> methods, List<IMethodInfo> clazzMethods,
			List<String> names) {
		for (int i = 0; i < clazzMethods.size(); i++) {
			IMethodInfo info = clazzMethods.get(i);
			if (names.contains(info.getName()))
				methods.put(info.getName(), info);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#
	 * getPredefinedStyles(java.lang.String)
	 */
	public List<IPredefinedStyle> getPredefinedStyles(String type) {
		if (predefinedStyleTypes == null || type == null)
			return Collections.emptyList();

		List<IPredefinedStyle> styles = predefinedStyleTypes.get(type);
		if (styles == null)
			return Collections.emptyList();

		return styles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#
	 * getReportItemThemeTypes()
	 */
	public List<String> getReportItemThemeTypes() {
		if (predefinedStyleTypes == null || predefinedStyleTypes.isEmpty())
			return Collections.emptyList();

		List<String> types = new ArrayList<String>();
		types.addAll(predefinedStyleTypes.keySet());
		return types;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary#
	 * findElementByThemeType(java.lang.String)
	 */
	public IElementDefn findElementByThemeType(String themeType) {
		if (themeTypes == null || themeTypes.isEmpty() || StringUtil.isBlank(themeType))
			return null;
		Iterator<String> iter = themeTypes.keySet().iterator();
		while (iter.hasNext()) {
			String elementName = iter.next();
			if (themeType.equals(themeTypes.get(elementName))) {
				return getElement(elementName);
			}
		}
		return null;
	}

	/**
	 * Adds a theme type for a given element.
	 * 
	 * @param elementDefn
	 * @param themeType
	 * 
	 */

	void addThemeType(ElementDefn elementDefn, String themeType) {
		if (StringUtil.isBlank(themeType))
			return;

		themeTypes.put(elementDefn.getName(), themeType);
	}

	/**
	 * Gets a theme type for a given element.
	 * 
	 * @param elementDefn
	 * @param themeType
	 * 
	 */

	public String getThemeType(IElementDefn elementDefn) {
		return themeTypes.get(elementDefn.getName());
	}

}