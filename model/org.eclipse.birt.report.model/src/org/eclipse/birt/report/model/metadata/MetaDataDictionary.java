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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.metadata.validators.IValueValidator;
import org.eclipse.birt.report.model.validators.AbstractSemanticValidator;

/**
 * Global, shared dictionary of design meta data. Meta-data describes each
 * design element and its properties. The information is shared because all
 * designs share the same BIRT-provided set of design elements. See the
 * {@link IElementDefn}class for more detailed information.
 * 
 * <h2>Meta-data Information</h2>
 * The application must first populate the elements from a meta-data XML file
 * using a parser defined in <code>MetaDataReader</code>. The meta-data
 * defined here includes:
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
 * <h2>Enabling Object IDs</h2>
 * The model may be used in the web environment in which it is necessary to
 * identify elements using a unique ID separate from their object pointer. The
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
 * <li><strong>Build </strong>-- The build step completes the process. The
 * build step finds and caches the base type for each element, uses the message
 * IDs to read the actual message from a message catalog, and so on.</li>
 * </ul>
 */

public final class MetaDataDictionary implements IMetaDataDictionary
{

	/**
	 * The one and only metadata dictionary.
	 */

	private static MetaDataDictionary instance = new MetaDataDictionary( );

	/**
	 * Provides the list of design elements keyed by their internal names.
	 */

	private HashMap elementNameMap = new HashMap( );

	/**
	 * Provides the list of extension elements registered in our meta-data keyed
	 * by their internal names.
	 */

	private HashMap extensionNameMap = null;

	/**
	 * Cached link to the style definition. The style definition is frequently
	 * used, and so caching it saves unnecessary lookups.
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

	private HashMap choiceSets = new HashMap( );

	/**
	 * Map of structures. A structure represents an object in Java. It describes
	 * the members of the object that are to be visible to the UI. Such objects
	 * are generally kept property defined as a structure list.
	 */

	private HashMap structures = new HashMap( );

	/**
	 * Map of classes. A class represents an Object in Script. It describes the
	 * constructors, members and methods.
	 */

	private Map classes = new LinkedHashMap( );

	/**
	 * Whether to apply element ids to newly created elements. This feature is
	 * used for the web environment, but not for the Eclipse environment.
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

	private HashMap predefinedStyles = new HashMap( );

	/**
	 * Map of property value validators, holding the validator name as key. Each
	 * of this map is the instance of <code>IValueValidator</code>.
	 */

	private Map valueValidators = new HashMap( );

	/**
	 * Map of semantic validators, holding the validator name as key. Each of
	 * this map is the instance of <code>AbstractSemanticValidator</code>.
	 */

	private Map semanticValidators = new HashMap( );

	/**
	 * Whether to use validation trigger. This feature will perform validation
	 * once one property or slot is changed.
	 */

	private boolean useValidationTrigger = false;

	/**
	 * The default encryption helper.
	 */

	private IEncryptionHelper encryptionHelper = SimpleEncryptionHelper
			.getInstance( );

	/**
	 * Singleton class, constructor is private.
	 */

	private MetaDataDictionary( )
	{
		// Create the list of property types.
		//
		// The meta-data file will provide additional information for these
		// types.
		addPropertyType( new StringPropertyType( ) );
		addPropertyType( new LiteralStringPropertyType( ) );
		addPropertyType( new NumberPropertyType( ) );
		addPropertyType( new IntegerPropertyType( ) );
		addPropertyType( new DimensionPropertyType( ) );
		addPropertyType( new ColorPropertyType( ) );
		addPropertyType( new ChoicePropertyType( ) );
		addPropertyType( new BooleanPropertyType( ) );
		addPropertyType( new ExpressionPropertyType( ) );
		addPropertyType( new HTMLPropertyType( ) );
		addPropertyType( new ResourceKeyPropertyType( ) );
		addPropertyType( new URIPropertyType( ) );
		addPropertyType( new DateTimePropertyType( ) );
		addPropertyType( new XMLPropertyType( ) );
		addPropertyType( new NamePropertyType( ) );
		addPropertyType( new FloatPropertyType( ) );
		addPropertyType( new ElementRefPropertyType( ) );
		addPropertyType( new StructPropertyType( ) );
		addPropertyType( new ExtendsPropertyType( ) );
		addPropertyType( new ScriptPropertyType( ) );
		addPropertyType( new StructRefPropertyType( ) );
		addPropertyType( new ListPropertyType( ) );
		addPropertyType( new MemberKeyPropertyType( ) );
		addPropertyType( new ElementPropertyType( ) );
		addPropertyType( new ContentElementPropertyType( ) );
	}

	/**
	 * Adds a property type to the dictionary.
	 * 
	 * @param propType
	 *            the property type to add
	 */

	private void addPropertyType( PropertyType propType )
	{
		int typeCode = propType.getTypeCode( );
		assert propertyTypes[typeCode] == null;
		propertyTypes[typeCode] = propType;
	}

	/**
	 * Returns the meta-data dictionary. This dictionary is shared by all open
	 * designs.
	 * 
	 * @return The meta-data dictionary.
	 */

	public static MetaDataDictionary getInstance( )
	{
		return instance;
	}

	/**
	 * Finds the element definition by its internal name.
	 * 
	 * @param name
	 *            The internal element definition name.
	 * @return The element definition, or null if the name was not found in the
	 *         dictionary.
	 */

	public IElementDefn getElement( String name )
	{
		return (IElementDefn) ( elementNameMap.get( name ) == null
				? ( extensionNameMap == null ? null : extensionNameMap
						.get( name ) )
				: elementNameMap.get( name ) );
	}

	/**
	 * Internal method to build the cached semantic data for the dictionary.
	 * Looks up "extends" references, looks up display names given message IDs,
	 * and so on.
	 * 
	 * @throws MetaDataException
	 *             if any build error occurs
	 */

	void build( ) throws MetaDataException
	{
		buildPropertyTypes( );
		buildElementDefinitions( );
		validateConstants( );
		buildStructures( );
	}

	/**
	 * Private method to validate the various meta-data constants used in this
	 * build.
	 * 
	 * @throws MetaDataException
	 *             if any of the validation fails.
	 */

	private void validateConstants( ) throws MetaDataException
	{
		for ( int i = 0; i < propertyTypes.length; i++ )
			assert propertyTypes[i].getTypeCode( ) == i;

		validateElement( MetaDataConstants.STYLE_NAME );
		validateElement( MetaDataConstants.REPORT_ELEMENT_NAME );
		validateElement( MetaDataConstants.REPORT_DESIGN_NAME );
	}

	/**
	 * Validates that an element name is valid. Throws an exception if the name
	 * is not valid.
	 * 
	 * @param name
	 *            the element name to validate
	 * @throws MetaDataException
	 *             if the name is not valid
	 */

	private void validateElement( String name ) throws MetaDataException
	{
		if ( getElement( name ) == null )
			throw new MetaDataException( new String[]{name},
					MetaDataException.DESIGN_EXCEPTION_ELEMENT_NAME_CONST );
	}

	/**
	 * Builds the list of property types.
	 */

	private void buildPropertyTypes( )
	{
		// Build the property type information.

		for ( int i = 0; i < propertyTypes.length; i++ )
			propertyTypes[i].build( );

	}

	/**
	 * Builds the list of element definitions.
	 * 
	 * @throws MetaDataException
	 *             if the build of the element definition fails.
	 */

	private void buildElementDefinitions( ) throws MetaDataException
	{
		// Build the style first, since most other elements will
		// reference it.

		style = (ElementDefn) getElement( MetaDataConstants.STYLE_NAME );
		if ( style == null )
			throw new MetaDataException(
					MetaDataException.DESIGN_EXCEPTION_STYLE_TYPE_MISSING );
		style.build( );

		ElementDefn report = (ElementDefn) getElement( ReportDesignConstants.REPORT_DESIGN_ELEMENT );
		if ( report == null )
			throw new MetaDataException(
					MetaDataException.DESIGN_EXCEPTION_CONSTRUCTOR_EXISTING );
		report.build( );

		// Build the element metadata.

		Iterator iter = elementNameMap.values( ).iterator( );
		while ( iter.hasNext( ) )
		{
			( (ElementDefn) iter.next( ) ).build( );
		}
	}

	/**
	 * Private method to build the structure definitions.
	 * 
	 * @throws MetaDataException
	 */

	private void buildStructures( ) throws MetaDataException
	{
		Iterator iter = structures.values( ).iterator( );
		while ( iter.hasNext( ) )
		{
			StructureDefn type = (StructureDefn) iter.next( );
			type.build( );
		}
	}

	/**
	 * Gets the metadata for a property type.
	 * 
	 * @param type
	 *            numeric type code
	 * @return property type definition
	 */

	public PropertyType getPropertyType( int type )
	{
		assert type >= 0 && type < propertyTypes.length;
		return propertyTypes[type];
	}

	/**
	 * Gets a list of rom-defined property types.
	 * 
	 * @return a list of rom-defined property types.
	 */

	public List getPropertyTypes( )
	{
		return Arrays.asList( propertyTypes );
	}

	/**
	 * Gets the metadata for a property type given the type's XML name.
	 * 
	 * @param xmlName
	 *            XML name for the property type
	 * 
	 * @return property type definition
	 */

	public PropertyType getPropertyType( String xmlName )
	{
		for ( int i = 0; i < propertyTypes.length; i++ )
			if ( propertyTypes[i].getName( ).equalsIgnoreCase( xmlName ) )
				return propertyTypes[i];

		return null;
	}

	/**
	 * Returns the meta-data element that defines the style element.
	 * 
	 * @return the definition of the style element
	 */

	public IElementDefn getStyle( )
	{
		return style;
	}

	/**
	 * Resets the dictionary. Clears the cached data. Used primarily for
	 * testing.
	 */

	public static void reset( )
	{
		instance = new MetaDataDictionary( );
	}

	/**
	 * Adds an element type to the dictionary. Must be done before the build
	 * step. The element type name must be unique.
	 * 
	 * @param type
	 *            the element type to add
	 * @throws MetaDataException
	 *             if exception occurs when adding the element definition.
	 * 
	 */

	void addElementDefn( ElementDefn type ) throws MetaDataException
	{
		String name = type.getName( );
		if ( StringUtil.isBlank( name ) )
			throw new MetaDataException(
					MetaDataException.DESIGN_EXCEPTION_MISSING_ELEMENT_NAME );
		if ( elementNameMap.containsKey( name ) )
			throw new MetaDataException( new String[]{name},
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_ELEMENT_NAME );
		elementNameMap.put( name, type );
	}

	/**
	 * Enables the use of element IDs.
	 */

	public void enableElementID( )
	{
		useElementID = true;
	}

	/**
	 * Reports whether element IDs are in use.
	 * 
	 * @return True if new elements should use element IDs.
	 */

	public boolean useID( )
	{
		return useElementID;
	}

	/**
	 * Adds a predefined style to the dictionary.
	 * 
	 * @param style
	 *            the predefined style
	 * @throws MetaDataException
	 *             if exception occur when adding the style, it may be because
	 *             the style missing its name or an style with the same name
	 *             already exists.
	 */

	void addPredefinedStyle( PredefinedStyle style ) throws MetaDataException
	{
		String name = style.getName( );

		if ( StringUtil.isBlank( name ) )
			throw new MetaDataException(
					MetaDataException.DESIGN_EXCEPTION_MISSING_STYLE_NAME );
		if ( predefinedStyles.get( name ) != null )
			throw new MetaDataException( new String[]{name},
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_STYLE_NAME );
		predefinedStyles.put( name, style );
	}

	/**
	 * Finds a predefined style definition.
	 * 
	 * @param name
	 *            the internal name of the predefined style
	 * @return the predefined style, or null if the style is not defined
	 */

	public PredefinedStyle getPredefinedStyle( String name )
	{
		return (PredefinedStyle) predefinedStyles.get( name );
	}

	/**
	 * Determines if the meta data dictionary is empty (uninitialized).
	 * 
	 * @return true if empty, false if it contains content
	 */

	public boolean isEmpty( )
	{
		return elementNameMap.isEmpty( ) && predefinedStyles.isEmpty( );
	}

	/**
	 * Adds a choice set to the dictionary.
	 * 
	 * @param choiceSet
	 *            the choice set to add
	 * @throws MetaDataException
	 *             if the choice set is not valid
	 */

	void addChoiceSet( ChoiceSet choiceSet ) throws MetaDataException
	{

		String name = choiceSet.getName( );

		if ( StringUtil.isBlank( name ) )
			throw new MetaDataException(
					MetaDataException.DESIGN_EXCEPTION_MISSING_CHOICE_SET_NAME );
		if ( choiceSets.containsKey( name ) )
			throw new MetaDataException(
					new String[]{name},
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_CHOICE_SET_NAME );

		choiceSets.put( name, choiceSet );
	}

	/**
	 * Finds a choice set by name.
	 * 
	 * @param choiceSetName
	 *            the name of the choice set
	 * @return the choice set, or null if the choice set was not found
	 */

	public IChoiceSet getChoiceSet( String choiceSetName )
	{
		return (ChoiceSet) choiceSets.get( choiceSetName );
	}

	/**
	 * Adds a structure definition to the dictionary.
	 * 
	 * @param struct
	 *            the structure definition to add
	 * @throws MetaDataException
	 *             if the structure definition is not valid
	 */

	void addStructure( StructureDefn struct ) throws MetaDataException
	{
		String name = struct.getName( );
		if ( StringUtil.isBlank( name ) )
			throw new MetaDataException(
					MetaDataException.DESIGN_EXCEPTION_MISSING_STRUCT_NAME );
		if ( structures.containsKey( name ) )
			throw new MetaDataException( new String[]{name},
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_STRUCT_NAME );
		structures.put( name, struct );
	}

	/**
	 * Finds a structure definition by name.
	 * 
	 * @param name
	 *            the structure name
	 * @return the structure, or null if the structure is not found
	 */

	public IStructureDefn getStructure( String name )
	{
		return (IStructureDefn) structures.get( name );
	}

	/**
	 * Returns the element list. Each one is the instance of
	 * <code>IElementDefn</code>.
	 * 
	 * @return the element list.
	 */

	public List getElements( )
	{
		return new ArrayList( elementNameMap.values( ) );
	}

	/**
	 * Returns the structure list. Each one is the instance of
	 * <code>IStructureDefn</code>.
	 * 
	 * @return the structure list.
	 */

	public List getStructures( )
	{
		return new ArrayList( structures.values( ) );
	}

	/**
	 * Gets the predefined style list. Each one is the instance of
	 * <code>PredefinedStyle</code>;
	 * 
	 * @return the predefined style list.
	 */

	public List getPredefinedStyles( )
	{
		return new ArrayList( predefinedStyles.values( ) );
	}

	/**
	 * Returns the class list. Each one is the instance of
	 * <code>ClassInfo</code>.
	 * 
	 * @return the class list.
	 */

	public List getClasses( )
	{
		return new ArrayList( classes.values( ) );
	}

	/**
	 * Returns the class definition given the class name.
	 * 
	 * @param name
	 *            name of the class to get.
	 * @return the class definition if found.
	 */

	public IClassInfo getClass( String name )
	{
		return (ClassInfo) classes.get( name );
	}

	/**
	 * Adds the class definition to the dictionary.
	 * 
	 * @param classDefn
	 *            the definition of the class to add
	 * @throws MetaDataException
	 *             if the class name is not provided or duplicate.
	 */

	void addClass( ClassInfo classDefn ) throws MetaDataException
	{
		if ( StringUtil.isBlank( classDefn.getName( ) ) )
			throw new MetaDataException(
					MetaDataException.DESIGN_EXCEPTION_MISSING_CLASS_NAME );

		if ( classes.get( classDefn.getName( ) ) != null )
			throw new MetaDataException( new String[]{classDefn.getName( )},
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_CLASS_NAME );

		classes.put( classDefn.getName( ), classDefn );

	}

	/**
	 * Returns the extension list. Each one is the instance of
	 * {@link IElementDefn}.
	 * 
	 * @return the extension definition list. Return empty list if no extension
	 *         is found.
	 */

	public List getExtensions( )
	{
		if ( extensionNameMap == null )
			return Collections.EMPTY_LIST;

		return new ArrayList( extensionNameMap.values( ) );
	}

	/**
	 * Returns the extension definition given the extension name.
	 * 
	 * @param name
	 *            name of the extension to get
	 * @return the extension definition if found
	 */

	public IElementDefn getExtension( String name )
	{
		if ( extensionNameMap == null )
			return null;
		return (IElementDefn) extensionNameMap.get( name );
	}

	/**
	 * Adds the extension definition to the dictionary.
	 * 
	 * @param extDefn
	 *            the definition of the extension element to add
	 * @throws MetaDataException
	 *             if the extension name is not provided or duplicate.
	 */

	void addExtension( ExtensionElementDefn extDefn ) throws MetaDataException
	{
		assert extDefn != null;
		if ( StringUtil.isBlank( extDefn.getName( ) ) )
			throw new MetaDataException(
					MetaDataException.DESIGN_EXCEPTION_MISSING_EXTENSION_NAME );
		if ( extensionNameMap == null )
			extensionNameMap = new HashMap( );
		if ( elementNameMap.get( extDefn.getName( ) ) != null
				|| extensionNameMap.get( extDefn.getName( ) ) != null )
			throw new MetaDataException( new String[]{extDefn.getName( )},
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_EXTENSION_NAME );

		extensionNameMap.put( extDefn.getName( ), extDefn );
	}

	/**
	 * Add a new validator to the dictionary.
	 * 
	 * @param validator
	 *            a new validator.
	 * @throws MetaDataException
	 *             if the validator missing its name or its name duplicates with
	 *             an exsiting one.
	 */

	void addValueValidator( IValueValidator validator )
			throws MetaDataException
	{
		String name = validator.getName( );
		if ( StringUtil.isBlank( name ) )
			throw new MetaDataException(
					MetaDataException.DESIGN_EXCEPTION_MISSING_VALIDATOR_NAME );
		if ( valueValidators.containsKey( name ) )
			throw new MetaDataException( new String[]{name},
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_VALIDATOR_NAME );

		valueValidators.put( name, validator );
	}

	/**
	 * Return a property value validator given its name.
	 * 
	 * @param name
	 *            name of the value validator.
	 * @return A property value validator.
	 */

	public IValueValidator getValueValidator( String name )
	{
		return (IValueValidator) valueValidators.get( name );
	}

	/**
	 * Adds the semantic validator.
	 * 
	 * @param validator
	 *            the validator to add
	 * @throws MetaDataException
	 *             if the validator name is missing or duplicates.
	 */

	public void addSemanticValidator( AbstractSemanticValidator validator )
			throws MetaDataException
	{
		String name = validator.getName( );
		if ( StringUtil.isBlank( name ) )
			throw new MetaDataException(
					MetaDataException.DESIGN_EXCEPTION_MISSING_VALIDATOR_NAME );

		if ( semanticValidators.containsKey( name ) )
			throw new MetaDataException( new String[]{name},
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_VALIDATOR_NAME );

		semanticValidators.put( name, validator );
	}

	/**
	 * Returns the semantic validator given the name.
	 * 
	 * @param name
	 *            the validator name
	 * @return the semantic validator with the given name. Or return
	 *         <code>null</code>, the there is no validator with the given
	 *         name.
	 */

	public AbstractSemanticValidator getSemanticValidator( String name )
	{
		return (AbstractSemanticValidator) semanticValidators.get( name );
	}

	/**
	 * Returns whether to use validation trigger feature.
	 * 
	 * @return whether to use validation trigger feature
	 */

	public boolean useValidationTrigger( )
	{
		return useValidationTrigger;
	}

	/**
	 * Enables the validation trigger feature.
	 * 
	 * @param useValidationTrigger
	 *            the flag to set
	 */

	public void setUseValidationTrigger( boolean useValidationTrigger )
	{
		this.useValidationTrigger = useValidationTrigger;
	}

	/**
	 * Returns the encryption helper.
	 * 
	 * @return the encryption helper which is registered on metadata dictionary.
	 */

	public IEncryptionHelper getEncryptionHelper( )
	{
		return encryptionHelper;
	}

	/**
	 * Sets the encryption helper.
	 * 
	 * @param encryptionHelper
	 *            the encryption helper to set
	 */

	void setEncryptionHelper( IEncryptionHelper encryptionHelper )
	{
		this.encryptionHelper = encryptionHelper;
	}
}