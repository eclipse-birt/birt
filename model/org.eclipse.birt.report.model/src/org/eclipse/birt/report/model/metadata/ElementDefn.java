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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.StyleReferenceValidator;
import org.eclipse.birt.report.model.api.validators.UnsupportedElementValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.validators.AbstractSemanticValidator;

/**
 * Describes a report element. This class represents "meta-data" about an
 * element. This class does not represent the element itself. Rather, it
 * describes a "class" of elements. The definition provides information about
 * the set of valid properties, their validation rules, and more.
 * <p>
 * To understand this class, it helps to understand some concepts behind the
 * design of the model.
 * 
 * <h3>Meta-data vs. Elements</h3>
 * 
 * A design is made up of <em>elements</em>. BIRT defines a fixed-set of
 * <em>element types</em> such as List or Data or Image. The user defines the
 * elements in the report. However, element types are defined by the development
 * team and are fixed for any given release of the product. The class
 * DesignElement (and its subclasses) describe the elements that users create.
 * The class ElementDefn describes element types.
 * <p>
 * Elements are part of a design, and each design has its own set of elements
 * depending on what the user wants to build. Element types, however, are the
 * same for all open designs. Hence, type information is stored in a shared
 * <em>data dictionary</em>. Every concrete element type has a Java class that
 * implements the element.
 * <p>
 * Meta-data serves two key roles. First, it provides information that the core
 * implementation needs to correctly model the various elements. Second, it is
 * useful to the UI to provide information about elements. For example, the
 * property sheet can use the meta-data to determine the set of properties for
 * an element, the display names of each property, the data type, and any
 * special UI builders required.
 * 
 * <h3>Properties and Property Types</h3>
 * 
 * Most elements define properties. A property is simply something that the user
 * can set about the element such as its name, or size, or color. Property
 * information appears in three forms, which are easy to confuse.
 * <dl>
 * <dt><strong>Property type </strong></dt>
 * <dd>A description of the type of a property. Many properties can share the
 * same type. For example, foreground color and background color are both
 * properties of type color. Property types are defined by the implementation
 * team and are defined in the data dictionary.</dd>
 * 
 * <dt><strong>Property </strong></dt>
 * <dd>A property is the definition of an attribute of an element, and consists
 * of a name and a property type. Properties are of several kinds as described
 * below. The property is also a definition: all elements share the same set of
 * property definitions. The element definition (ElementDefn) defines the set of
 * properties available on that element.</dd>
 * 
 * <dt><strong>Property value </strong></dt>
 * <dd>A property value is the actual value that the user sets for a property. A
 * property value is associated with a element, and is stored in the
 * <code>DesignElement</code> class, or one of its subclasses. For convenience,
 * we often use the word "property" to mean a property value. Property values
 * can be in two states: set or unset. An unset value generally causes the model
 * to inherit the property value from the parent element (if any). Properties in
 * BIRT work much like properties in JavaScript objects.</dd>
 * </dl>
 * 
 * <h3>System and User Properties</h3>
 * 
 * BIRT defines a wide range of element types, each with a wide range of
 * properties. Properties defined by BIRT itself are called
 * <em>system properties</em>. They are defined as part of the element type
 * definition in the meta-data file. System properties are of two types: normal
 * or <em>intrinsic</em>. An intrinsic property is one defined by a member
 * variable the DesignElement class or one of its subclasses. An normal property
 * is stored as a name/value pair in the DesignElement class.
 * <p>
 * BIRT also allows users to define their own properties. Such properties are
 * called, unsurprisingly, user properties. User properties are defined on an
 * element and are available to that element, and any elements that derive from
 * that element (see below.)
 * <p>
 * For the most part, system and user properties are defined in the same way as
 * implemented by the {@link ElementPropertyDefn}class. The few differences are
 * represented by the {@link SystemPropertyDefn}and
 * {@link org.eclipse.birt.report.model.api.core.UserPropertyDefn}classes.
 * 
 * <h3>Element Inheritance</h3>
 * 
 * Elements inherit from one another. There are two subtly different forms of
 * inheritance used in the system. First, the definition of element types uses
 * inheritance to simplify the description of the set of element types. For
 * example, the Data and Label elements are both types of the ReportItem
 * element.
 * <p>
 * Inheritance in the type system is described in this class (ElementDefn). Such
 * inheritance is constant for all designs. Type system inheritance, by
 * definition, allows one type of element to inherit from another.
 * <p>
 * Like any OO hierarchy, the element type system starts with certain abstract
 * base types such as ReportElement or ReportItem. While these are important for
 * describing the type system, they are irrelevant (or worse, confusing) to the
 * user. Thus, they are private to the implementation and not generally visible
 * to the user. (It is not a secret that they exist -- the XML design format is
 * open -- but it is not relevant to the business of building a report.) Such
 * elements are marked as abstract.
 * <p>
 * The second form of inheritance is defined by the user. A user can define a
 * Label, for example, that has many special attributes. The user can then
 * create other labels that derive from the first. User-defined inheritance is
 * subtly different from type-system inheritance. User cannot define new element
 * types; they can only set properties and customize existing types.
 * <p>
 * Said another way, type system inheritance uses Java-like class inheritance.
 * User-element inheritance uses JavaScript-like prototype inheritance.
 * <p>
 * BIRT does define and "extended element" that the user can define from
 * scratch, but even this follows the same rules. An extended element is not a
 * new type, it is just a particular set of customizations of the predefined
 * "extended" type.
 * <p>
 * User-defined inheritance is limited to elements of the same type. A label can
 * extend another label, but it cannot extend a data element, say.
 * <p>
 * System inheritance is used to define properties in a base class that should
 * be inherited by derived classes. User-inheritance implicitly inherits
 * properties, but focuses on inheriting property values. That is, user
 * inheritance allows Label B to inherit a font color of red from Label A. As
 * noted above, elements can define user properties. These property definitions
 * also are inherited with user inheritance. that is, if Label A defines a new
 * "Blink Rate" property, then label B (which extends label A) also has this
 * property.
 * 
 * <h3>Styles</h3>
 * 
 * BIRT includes the idea of styles. A style is simply a set of visual
 * information defined once in a design but used by many elements. At the core
 * level that we are discussing here, styles introduce a couple of interesting
 * twists to the design of the meta-data and element systems.
 * <p>
 * First some element types are defined to "have" a style. Having a style means
 * two things. First, the user can specify the name of a shared style to
 * associate with the element. Second, means that the element logically has a
 * "private style" that includes all the style properties relevant to that
 * element. In practice, any element type that has a style treats style
 * properties as though they were actual properties of the element type itself.
 * <p>
 * The meta-data file defines the style element, and this definition includes a
 * set of properties. Most of these represent properties that are to be applied
 * to elements that have that style (color, font, highlighting, etc.) A very few
 * apply to the style element itself (name, extends). To represent this, the
 * "shared" style properties are marked with an "isStyleProperty" attribute.
 * Anything so marked will associate with elements that have a style.
 * <p>
 * Other elements indicate if they have a style. In general, only the most base
 * element in a hierarchy sub-tree has this indication. All elements derived
 * from such an element also implicitly have a style. That is, a derived element
 * cannot "turn off" the style if one of its ancestors has a style. Let's use
 * the term <em>styled element</em> for one that has a style.
 * <p>
 * A styled element will present the style properties as though they were
 * properties of that element itself. This is done during the meta-data setup
 * process by copying each of the style properties from the style into the
 * styled element. The copy is done only for the most-base element in an element
 * type subtree that has a style. (Elements further down the tree automatically
 * inherit these properties in the normal way.) In practice, the implementation
 * copies a reference to the property, rather than the property itself.
 * <p>
 * However, styles have a wide range of properties, not all of which are
 * relevant to each element. Indeed, there are few elements that use all the
 * style properties. It would be confusing to the user to see a set of
 * irrelevant properties. Therefore, the meta-data file identifies the style
 * properties that are relevant to any given styled component. Only these are
 * copied from the style into the element type.
 * 
 * <h3>Containment</h3>
 * 
 * <em>Container</em> elements contain <em>content</em> elements. Some
 * containers store just one set of contents, others store multiple sets. Each
 * set of contents is called a <em>slot</em>. Each slot has a cardinality (one
 * or many), and a type. More specifically, each slot can contain a set of
 * element types. The {@link SlotDefn}class holds details of a slot.
 * <p>
 * Slots are indexed using a slot ID. The ID is a zero-based index, allowing the
 * caller to easily iterate over the slots.
 * 
 * <h3>Names and Name Spaces</h3>
 * 
 * Elements can have a name. Some elements require a name, for some the name is
 * optional.
 * <p>
 * The element name is meant to be unique within a <em>name space</em>. Name
 * spaces reside on the root element. Name spaces include elements, styles, data
 * sources, data sets and so on. Each element type identifies its name space
 * using a name space ID.
 * 
 * <h3>Method</h3>
 * 
 * Elements can have methods that defines the script. All methods are defined in
 * metadata. The element can inherit the methods defined in parent. Property
 * mask can change the method visibility.
 */

public class ElementDefn extends ObjectDefn implements IElementDefn
{

	private static Logger logger = Logger.getLogger( ElementDefn.class
			.getName( ) );

	/**
	 * The property is hidden in the property sheet.
	 */

	protected final static String HIDDEN_IN_PROPERTY_SHEET = "hide"; //$NON-NLS-1$

	/**
	 * The property will be unhidden in the property sheet.
	 */
	protected final static String UNHIDDEN_IN_PROPERTY_SHEET = "unhide"; //$NON-NLS-1$

	/**
	 * The property is shown in the property sheet but read-only.
	 */

	protected final static String READONLY_IN_PROPERTY_SHEET = "readonly"; //$NON-NLS-1$

	/**
	 * The key that specifies that there is no visibility set for the property.
	 */
	protected final static int NO_VISIBILITY_KEY = 0x00;

	/**
	 * The key that specifies that this property will be hidden in property
	 * sheet and editor.
	 */
	protected final static int HIDDEN_IN_PROPERTY_SHEET_KEY = 0x01;

	/**
	 * The key that specifies that this property will be read-only and can not
	 * be changed by Model public API.
	 */
	protected final static int READONLY_KEY = 0x02;

	/**
	 * The separator that is used to delimit multiple visibility options.
	 */
	protected final static String VISIBILITY_SEPERATOR = ","; //$NON-NLS-1$

	/**
	 * Whether this definition represents an abstract element that the user
	 * normally does not see. Abstract elements exist to organize the element
	 * system.
	 */

	protected boolean abstractElement = false;

	/**
	 * The name of base element, if any, from which this element extends.
	 */

	protected String extendsFrom = null;

	/**
	 * The parent Element meta-data object (specified by the "extendsFrom"
	 * value).
	 */

	protected ElementDefn parent = null;

	/**
	 * specify if the element has a style.
	 */

	protected boolean hasStyle = false;

	/**
	 * Flag used when caching meta data.
	 */

	protected boolean isBuilt = false;

	/**
	 * The predefined style, if any, for this element. The style name is the
	 * internal name.
	 */

	protected String selector = null;

	/**
	 * Whether this element allows the definition of user-defined properties. If
	 * a given element does not allow user-properties, then none of its derived
	 * elements supports them either.
	 */

	protected boolean supportsUserProperties = true;

	/**
	 * List of style properties that apply to this element. Only those
	 * properties listed here are available to this element. Contents are
	 * strings.
	 */

	protected ArrayList<String> stylePropertyNames = null;

	/**
	 * Whether this element acts as a container. If so, which slots it
	 * implements.
	 */

	protected List<ISlotDefn> slots = null;

	/**
	 * Whether this element can be extended. If true, elements of this type can
	 * extend from another element of the same type. If false, elements cannot
	 * extend.
	 */

	protected boolean allowExtend = true;

	/**
	 * Determines which class that this element belongs to.
	 */

	protected String javaClass = null;

	/**
	 * The collection of semantic validation trigger definition.
	 */

	private SemanticTriggerDefnSet triggerDefnSet = null;

	/**
	 * The list contains the information that how the property sheet shows an
	 * property for an extension element.
	 */

	protected Map<String, Integer> propVisibilites = null;

	/**
	 * Cached property visibility. It contains local defined and parents'
	 * defined property visibility. The key is property name, the value is the
	 * integer visibility of the property.
	 */
	protected Map<String, Integer> cachedPropVisibilites = null;

	/**
	 * The name of the XML element used when serializing this ROM element.
	 */

	protected String xmlName;

	/**
	 * Cached property definitions. It contains local defined and parents'
	 * defined property definitions. After parsing ROM, property definition
	 * should not be added/removed. Otherwise, cachedProperties can be
	 * un-synchronized.
	 */

	protected Map<String, IElementPropertyDefn> cachedProperties = new LinkedHashMap<String, IElementPropertyDefn>( );

	/**
	 * Justifies whether this element definition is container or not. True if
	 * this element defines slot or any element type property.
	 */
	protected boolean isContainer = false;

	/**
	 * 
	 */
	protected List<IElementPropertyDefn> cachedContainerProperties = null;

	/**
	 * Configuration information about the element name management.
	 */
	protected NameConfig nameConfig = new NameConfig( );

	/**
	 * Whether the slot id is defined by slot definition in ROM.
	 */

	private boolean isSlotIDSpecifiedByROM = false;

	/**
	 * Sets the Java class which implements this element.
	 * 
	 * @param theClass
	 *            the Java class to set
	 */

	public void setJavaClass( String theClass )
	{
		assert !isBuilt;
		javaClass = theClass;
	}

	/**
	 * Sets the name of the style which "selects" this element.
	 * 
	 * @param value
	 *            The predefined style name.
	 */

	public void setSelector( String value )
	{
		selector = value;
	}

	/**
	 * Sets the "extends" attribute.
	 * 
	 * @param base
	 *            The name of the element type which this element extends.
	 */

	public void setExtends( String base )
	{
		assert !isBuilt;
		extendsFrom = base;
	}

	/**
	 * Sets the "supports user properties" attribute.
	 * 
	 * @param flag
	 *            True if the element allows user-defined properties, false
	 *            otherwise.
	 */

	public void setSupportsUserProperties( boolean flag )
	{
		assert !isBuilt;
		supportsUserProperties = flag;
	}

	/**
	 * Gets the name of the parent element, if any. The parent element is the
	 * one that this element extends.
	 * 
	 * @return The name of the base element, if any.
	 */

	public String getExtends( )
	{
		return extendsFrom;
	}

	/**
	 * Gets the java class of this element.
	 * 
	 * @return The java class of this element.
	 */

	public String getJavaClass( )
	{
		return javaClass;
	}

	/**
	 * Indicates if this element has a style.
	 * 
	 * @return Returns whether the element has a style.
	 */

	public boolean hasStyle( )
	{
		return hasStyle;
	}

	/**
	 * Sets whether the element has style properties.
	 * 
	 * @param flag
	 *            True if this element supports style properties, false if not.
	 */

	public void setHasStyle( boolean flag )
	{
		assert !isBuilt;
		hasStyle = flag;
	}

	/**
	 * Returns properties definitions as a list.
	 * 
	 * @return list of locally-defined properties.
	 */

	public List<IElementPropertyDefn> getLocalProperties( )
	{
		return new ArrayList<IElementPropertyDefn>(
				(Collection<? extends IElementPropertyDefn>) properties
						.values( ) );
	}

	/**
	 * Returns the properties defined on this element.
	 * 
	 * @return list of properties defined in this element and and all its parent
	 *         elements.
	 */

	public List<IElementPropertyDefn> getProperties( )
	{
		return new ArrayList<IElementPropertyDefn>( cachedProperties.values( ) );
	}

	/**
	 * Gets a property definition given a property name.
	 * 
	 * @param propName
	 *            The name of the property to get.
	 * @return The property with that name, or null if the property cannot be
	 *         found.
	 */

	public IElementPropertyDefn getProperty( String propName )
	{
		assert propName != null;

		return cachedProperties.get( propName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IElementDefn#getContainers()
	 */

	public List<IElementPropertyDefn> getContents( )
	{
		return cachedContainerProperties;
	}

	/**
	 * Copies element type property definitions to the cached map.
	 */

	protected final void buildContainerProperties( )
	{
		cachedContainerProperties = new ArrayList<IElementPropertyDefn>( );
		Iterator<IElementPropertyDefn> iter = cachedProperties.values( )
				.iterator( );
		while ( iter.hasNext( ) )
		{
			IElementPropertyDefn defn = iter.next( );
			if ( defn.getTypeCode( ) == IPropertyType.ELEMENT_TYPE )
				cachedContainerProperties.add( defn );
		}

		if ( cachedContainerProperties.isEmpty( ) )
			cachedContainerProperties = Collections.emptyList( );
	}

	/**
	 * Returns the method definition list of this element definition and parent
	 * definition. Each one is the instance of <code>PropertyDefn</code>.
	 * 
	 * @return the method definition list.
	 */

	public List<IElementPropertyDefn> getMethods( )
	{
		return getPropertyListWithType( getProperties( ),
				IPropertyType.SCRIPT_TYPE );
	}

	/**
	 * Returns the method definition list of this element definition. Each one
	 * is the instance of <code>PropertyDefn</code>.
	 * 
	 * @return the method definition list.
	 */

	public List<IElementPropertyDefn> getLocalMethods( )
	{
		return getPropertyListWithType( getLocalProperties( ),
				IPropertyType.SCRIPT_TYPE );
	}

	/**
	 * Returns the expression property definition list of this element
	 * definition and parent definition. Each one is the instance of
	 * <code>PropertyDefn</code>.
	 * 
	 * @return the expression property definition list.
	 */

	public List<IElementPropertyDefn> getExpressions( )
	{
		return getPropertyListWithType( getProperties( ),
				IPropertyType.EXPRESSION_TYPE );
	}

	/**
	 * Returns the expression property definition list of this element
	 * definition. Each one is the instance of <code>PropertyDefn</code>.
	 * 
	 * @return the expression property definition list.
	 */

	public List<IElementPropertyDefn> getLocalExpressions( )
	{
		return getPropertyListWithType( getLocalProperties( ),
				IPropertyType.EXPRESSION_TYPE );
	}

	/**
	 * Returns the property definition list each of which is defined in given
	 * property definition list with the given type.
	 * 
	 * @param propList
	 *            the property definition to search
	 * @param type
	 *            property type code, It's the constant defined in
	 *            <code>PropertyType</code>.
	 * @return the property definition list
	 */

	private List<IElementPropertyDefn> getPropertyListWithType(
			List<IElementPropertyDefn> propList, int type )
	{
		List<IElementPropertyDefn> props = new ArrayList<IElementPropertyDefn>( );

		Iterator<IElementPropertyDefn> iter = propList.iterator( );
		while ( iter.hasNext( ) )
		{
			IElementPropertyDefn propDefn = iter.next( );

			if ( propDefn.getTypeCode( ) == type )
			{
				props.add( propDefn );
			}
		}

		return props;
	}

	/**
	 * Caches meta-data for this element. Resolves the parent name and so on.
	 * 
	 * @throws MetaDataException
	 *             if any build process failed.
	 * 
	 */

	protected void build( ) throws MetaDataException
	{
		if ( isBuilt )
			return;

		buildDefn( );

		buildProperties( );

		buildPropertiesVisibility( );

		// check if the javaClass and xml name is valid for concrete element
		// type

		if ( !isAbstract( ) )
		{
			checkJavaClass( );
			checkXmlName( );
		}

		buildContainerProperties( );

		buildSlots( );

		buildTriggerDefnSet( );

		isBuilt = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ObjectDefn#buildDefn()
	 */

	protected void buildDefn( ) throws MetaDataException
	{
		// Handle parent-specific tasks.

		MetaDataDictionary dd = MetaDataDictionary.getInstance( );

		if ( extendsFrom != null )
		{
			parent = (ElementDefn) dd.getElement( extendsFrom );
			if ( parent == null )
				throw new MetaDataException(
						new String[]{extendsFrom, name},
						MetaDataException.DESIGN_EXCEPTION_ELEMENT_PARENT_NOT_FOUND );
			parent.build( );

			// Cascade the setting of whether this element has a style.
			// That is, once an element has a style, all derived elements
			// have that style whether the meta data file explicitly indicated
			// this or not.

			if ( parent.hasStyle( ) && !isExtendedElement( ) )
				hasStyle = true;
		}

		// If this element has added a style, then add the intrinsic
		// style property.

		if ( ( parent == null || !parent.hasStyle( ) ) && hasStyle )
		{
			SystemPropertyDefn prop = new SystemPropertyDefn( );
			prop.setName( IStyledElementModel.STYLE_PROP );
			prop.setType( dd.getPropertyType( IPropertyType.ELEMENT_REF_TYPE ) );

			prop.setDisplayNameID( "Element.ReportElement.style" ); //$NON-NLS-1$
			prop.setDetails( MetaDataConstants.STYLE_NAME );
			prop.setIntrinsic( true );
			addProperty( prop );
		}

		// This element cannot forbid user-defined properties if
		// its parent supports them.

		if ( parent != null && parent.allowsUserProperties( ) )
			supportsUserProperties = true;

		// if parent is a container, this will be container too
		if ( parent != null && parent.isContainer )
			isContainer = true;

		// If this element is abstract and has a parent, then the parent
		// must also be abstract.

		if ( isAbstract( ) && parent != null && !parent.isAbstract( ) )
			throw new MetaDataException( new String[]{name, parent.getName( )},
					MetaDataException.DESIGN_EXCEPTION_ILLEGAL_ABSTRACT_ELEMENT );

		// do the build for the name config
		buildNameConfig( );

		// The user can't extend abstract elements (only the design schema
		// itself can extend abstract definitions. The user also cannot extend
		// items without a name because there is no way to reference such
		// elements.

		if ( nameConfig.nameOption == MetaDataConstants.NO_NAME || isAbstract( ) )
			allowExtend = false;
	}

	/**
	 * Builds the name config for this element.
	 * 
	 * @throws MetaDataException
	 */
	private void buildNameConfig( ) throws MetaDataException
	{
		
		if ( nameConfig.nameSpaceID != MetaDataConstants.NO_NAME_SPACE )
		{
			// this element has its own name space set
			if ( nameConfig.holder == null )
				nameConfig.holder = MetaDataDictionary.getInstance( )
						.getElement( ReportDesignConstants.MODULE_ELEMENT );
			if ( nameConfig.targetPropertyName != null )
			{
				nameConfig.targetProperty = (ElementPropertyDefn) MetaDataDictionary
						.getInstance( )
						.getElement(
								ReportDesignConstants.REPORT_DESIGN_ELEMENT )
						.getProperty( nameConfig.targetPropertyName );
				if ( nameConfig.targetProperty == null )
					throw new MetaDataException(
							MetaDataException.DESIGN_EXCEPTION_INVALID_NAME_SPACE );
			}
		}
		else
		{
			if ( parent != null )
			{
				//inherited from it's parent
				nameConfig.nameSpaceID = parent.getNameSpaceID( );
				nameConfig.holder = parent.nameConfig.holder;
				nameConfig.targetProperty = parent.nameConfig.targetProperty;
			}
		}
	 	// Validate that the name and name space options are consistent.

		if ( !isAbstract( ) )
		{
			//if namespaceId is null does't mean it sholdn't have name. it may be defined in container
//			if ( nameConfig.nameSpaceID == MetaDataConstants.NO_NAME_SPACE )
//				nameConfig.nameOption = MetaDataConstants.NO_NAME;
//			if ( nameConfig.nameSpaceID != MetaDataConstants.NO_NAME_SPACE
//					&& nameConfig.nameOption == MetaDataConstants.NO_NAME )
//				throw new MetaDataException( new String[]{name},
//						MetaDataException.DESIGN_EXCEPTION_INVALID_NAME_OPTION );

			// if name space is set, then holder must be not null
			if ( nameConfig.nameSpaceID != MetaDataConstants.NO_NAME_SPACE
					&& nameConfig.holder == null )
				throw new MetaDataException( new String[]{name},
						MetaDataException.DESIGN_EXCEPTION_INVALID_NAME_OPTION );

		}
		
//			if ( nameConfig.targetProperty == null
//					&& nameConfig.holder != null
//					&& !( nameConfig.holder.getNameOption( ) == MetaDataConstants.REQUIRED_NAME || nameConfig.holder
//							.isKindOf( MetaDataDictionary
//									.getInstance( )
//									.getElement(
//											ReportDesignConstants.MODULE_ELEMENT ) ) ) )
//			{
//				throw new MetaDataException(
//						MetaDataException.DESIGN_EXCEPTION_INVALID_NAME_SPACE );
//			}
	}

	/**
	 * Builds the style properties in this element.
	 * 
	 * @throws MetaDataException
	 */

	private void buildStyleProperties( ) throws MetaDataException
	{
		// If this item has a style, copy the relevant style properties onto
		// this element if it's leaf element or copy all the style properties
		// onto this element if it is a container.

		addStyleProperties( );

		if ( isExtendedElement( ) )
		{
			if ( hasStyle )
			{
				List<IElementPropertyDefn> styles = MetaDataDictionary
						.getInstance( ).getStyle( ).getLocalProperties( );
				for ( int i = 0; i < styles.size( ); i++ )
				{
					String propName = ( (SystemPropertyDefn) styles.get( i ) )
							.getName( );
					properties.put( propName, styles.get( i ) );
				}
			}
		}
		else
		{
			// The meta-data file should not define style property names
			// for a class without a style.

			if ( !hasStyle && stylePropertyNames != null || hasStyle
					&& isContainer( ) && stylePropertyNames != null )
				throw new MetaDataException( new String[]{this.name},
						MetaDataException.DESIGN_EXCEPTION_ILLEGAL_STYLE_PROPS );
		}

	}

	/**
	 * Builds the trigger definition set. This method cached all validators
	 * defined in property definition and slot definition. The cached validators
	 * are used to perform full validation of one element instance.
	 */

	protected void buildTriggerDefnSet( )
	{
		AbstractSemanticValidator validator = UnsupportedElementValidator
				.getInstance( );
		SemanticTriggerDefn triggerDefn = new SemanticTriggerDefn( validator
				.getName( ) );
		triggerDefn.setValidator( validator );
		getTriggerDefnSet( ).add( triggerDefn );

		if ( hasStyle )
		{
			validator = StyleReferenceValidator.getInstance( );
			triggerDefn = new SemanticTriggerDefn( StyleReferenceValidator.NAME );
			triggerDefn.setValidator( validator );
			getTriggerDefnSet( ).add( triggerDefn );
		}

		// Cache all triggers which are defined in property definition.

		List<IElementPropertyDefn> propList = getProperties( );
		Iterator<IElementPropertyDefn> iter = propList.iterator( );
		while ( iter.hasNext( ) )
		{
			PropertyDefn propDefn = (PropertyDefn) iter.next( );

			mergeTriggerDefnSet( propDefn.getTriggerDefnSet( ) );
		}

		// Cache all triggers which are defined in slot definition.

		Iterator<ISlotDefn> iter1 = slotsIterator( );
		while ( iter1.hasNext( ) )
		{
			SlotDefn slotDefn = (SlotDefn) iter1.next( );
			mergeTriggerDefnSet( slotDefn.getTriggerDefnSet( ) );
		}
	}

	/**
	 * Merges the trigger definition set with the given one. The duplicate
	 * trigger and the trigger whose target is not this element definition will
	 * not be merged.
	 * 
	 * @param toMerge
	 *            the trigger definition set to merge
	 */

	private void mergeTriggerDefnSet( SemanticTriggerDefnSet toMerge )
	{
		List<SemanticTriggerDefn> triggerDefns = toMerge.getTriggerList( );
		if ( triggerDefns == null || triggerDefns.isEmpty( ) )
			return;

		Iterator<SemanticTriggerDefn> iter = triggerDefns.iterator( );
		while ( iter.hasNext( ) )
		{
			SemanticTriggerDefn triggerDefn = iter.next( );

			String targetName = triggerDefn.getTargetElement( );

			if ( StringUtil.isBlank( targetName ) )
			{
				getTriggerDefnSet( ).add( triggerDefn );
			}
			else
			{
				ElementDefn targetDefn = (ElementDefn) MetaDataDictionary
						.getInstance( ).getElement( targetName );

				if ( isKindOf( targetDefn ) )
				{
					getTriggerDefnSet( ).add( triggerDefn );
				}
			}
		}
	}

	/**
	 * Checks the xml name for this element. Check the xml name is not empty and
	 * unique.
	 * 
	 * @throws MetaDataException
	 *             if the xml name of this element is not defined or not unique
	 */

	private void checkXmlName( ) throws MetaDataException
	{
		if ( StringUtil.isBlank( xmlName ) )
			throw new MetaDataException( new String[]{name},
					MetaDataException.DESIGN_EXCEPTION_MISSING_XML_NAME );
	}

	/**
	 * Check whether the java class specify a correct class which is defined for
	 * this element definition.
	 * 
	 * @throws MetaDataException
	 *             if there is loading error or instantiating error.
	 */

	private void checkJavaClass( ) throws MetaDataException
	{
		if ( StringUtil.isBlank( javaClass ) )
		{
			throw new MetaDataException( new String[]{name},
					MetaDataException.DESIGN_EXCEPTION_MISSING_JAVA_CLASS );
		}

		try
		{
			Class<? extends Object> c = Class.forName( javaClass );

			Class<? extends Object> clazz = c;
			while ( clazz.getSuperclass( ) != null )
			{
				if ( clazz == DesignElement.class )
					break;

				clazz = clazz.getSuperclass( );
			}
			if ( clazz != DesignElement.class )
				// if ( !( c.newInstance( ) instance of DesignElement ) )
				throw new MetaDataException(
						new String[]{javaClass},
						MetaDataException.DESIGN_EXCEPTION_INVALID_ELEMENT_JAVA_CLASS );
		}
		catch ( ClassNotFoundException e )
		{
			// can not load the class specified by javaclass name.

			throw new MetaDataException( new String[]{name, javaClass},
					MetaDataException.DESIGN_EXCEPTION_JAVA_CLASS_LOAD_ERROR );
		}
		// catch ( InstantiationException e )
		// {
		// // can not initalize the java class instance, either javaclass is
		// // not a valid DesignElement or the class
		// // itself does not provide a default constructor.
		//
		// throw new MetaDataException(
		// new String[]{name, javaClass},
		// MetaDataException.DESIGN_EXCEPTION_JAVA_CLASS_INITIALIZE_ERROR );
		// }
		// catch ( IllegalAccessException e )
		// {
		// // ignore
		// }
	}

	/**
	 * Builds cached meta-data for properties defined on this element.
	 * 
	 * @throws MetaDataException
	 *             if any exception occurs during build.
	 */

	protected void buildProperties( ) throws MetaDataException
	{
		// Cache data for properties defined here. Note, done here so
		// we don't repeat the work for any style properties copied below.

		buildLocalProperties( );

		buildStyleProperties( );

		buildCachedPropertyDefns( );

		buildOdaDataSetProperties( );
	}

	/**
	 * Builds some oda data set properties. Special handle is needed for
	 * resultsethints and parameters in it.
	 */
	private void buildOdaDataSetProperties( )
	{
		if ( ReportDesignConstants.ODA_DATA_SET.equalsIgnoreCase( name ) )
		{

			// change the details of 'resultSetHints' in OdaDataSet to
			// OdaResultSetColumn rather than ResultSetColumn
			ElementPropertyDefn resultSetHints = (ElementPropertyDefn) cachedProperties
					.get( OdaDataSet.RESULT_SET_HINTS_PROP );
			if ( resultSetHints == null )
				return;
			ElementPropertyDefn clonedDefn = (ElementPropertyDefn) reflectClass( resultSetHints );
			if ( clonedDefn == null )
				return;

			clonedDefn.details = MetaDataDictionary.getInstance( )
					.getStructure( OdaResultSetColumn.STRUCTURE_NAME );
			clonedDefn.definedBy = this;
			cachedProperties.put( OdaDataSet.RESULT_SET_HINTS_PROP, clonedDefn );

			// change the details of 'parameters' in odaDataSet to
			// OdaDataSetParam
			// rather than DataSetParam
			ElementPropertyDefn params = (ElementPropertyDefn) cachedProperties
					.get( OdaDataSet.PARAMETERS_PROP );
			if ( params == null )
				return;
			clonedDefn = (ElementPropertyDefn) reflectClass( params );
			if ( clonedDefn == null )
				return;

			clonedDefn.details = MetaDataDictionary.getInstance( )
					.getStructure( OdaDataSetParameter.STRUCT_NAME );
			clonedDefn.definedBy = this;
			cachedProperties.put( OdaDataSet.PARAMETERS_PROP, clonedDefn );
		}
	}

	/**
	 * Reflects to clone new instance of property definition.
	 * 
	 * @param defn
	 *            property definition
	 * @return shadow cloned property definition.
	 */

	protected PropertyDefn reflectClass( PropertyDefn defn )
	{
		ElementPropertyDefn retDefn = null;

		String className = defn.getClass( ).getName( );
		try
		{
			Class<? extends Object> clazz = Class.forName( className );
			retDefn = (ElementPropertyDefn) clazz.newInstance( );

			Class<? extends Object> ownerClass = defn.getClass( );
			Class<? extends Object> clonedClass = retDefn.getClass( );

			shadowCopyProperties( defn, retDefn, ownerClass, clonedClass );
		}
		catch ( InstantiationException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
			MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$				
		}
		catch ( IllegalAccessException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
			MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
		}
		catch ( ClassNotFoundException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
			MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
		}

		if ( retDefn == null )
			return null;

		shadowCopyProperties( defn, retDefn, defn.getClass( ),
				ExtensionPropertyDefn.class );

		return retDefn;
	}

	/**
	 * Shadow copy all properties to cloned property definition instance.
	 * 
	 * @param defn
	 *            property definition
	 * @param clonedDefn
	 *            cloned property definition
	 * @param ownerClass
	 *            property definition class
	 * @param clonedClass
	 *            cloned property definition class
	 */

	private void shadowCopyProperties( PropertyDefn defn,
			PropertyDefn clonedDefn, Class<? extends Object> ownerClass,
			Class<? extends Object> clonedClass )
	{
		if ( ownerClass == null || clonedClass == null )
			return;

		Field[] fields = ownerClass.getDeclaredFields( );
		for ( int i = 0; i < fields.length; ++i )
		{
			Field field = fields[i];
			if ( ( field.getModifiers( ) & Modifier.STATIC ) != 0 )
				continue;

			try
			{
				Object property = field.get( defn );
				Field clonedField = ownerClass.getDeclaredField( field
						.getName( ) );
				clonedField.set( clonedDefn, property );
			}
			catch ( IllegalArgumentException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
				MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	

				continue;
			}
			catch ( IllegalAccessException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
				MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
				continue;
			}
			catch ( SecurityException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
				MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
				continue;
			}
			catch ( NoSuchFieldException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
				MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
				continue;
			}
		}
		shadowCopyProperties( defn, clonedDefn, ownerClass.getSuperclass( ),
				clonedClass.getSuperclass( ) );
	}

	/**
	 * Builds cached meta-data for properties defined on this element.
	 * 
	 * @throws MetaDataException
	 *             if any exception occurs during build.
	 */

	protected void buildLocalProperties( ) throws MetaDataException
	{
		boolean isStyle = MetaDataConstants.STYLE_NAME.equals( name );
		Iterator<IPropertyDefn> iter = properties.values( ).iterator( );
		while ( iter.hasNext( ) )
		{
			ElementPropertyDefn prop = (ElementPropertyDefn) iter.next( );

			// Sanity check. Unless this is the style element, the
			// element cannot define style properties. (Note, this is
			// why we have to do this method BEFORE we add implicit
			// style properties.)

			if ( prop.isStyleProperty( ) && !isStyle )
				throw new MetaDataException(
						new String[]{name, prop.getName( )},
						MetaDataException.DESIGN_EXCEPTION_INVALID_STYLE_PROP_OPTION );

			// Build the property.

			prop.build( );
		}
	}

	/**
	 * Copies local and parent property definitions to the cached map. After
	 * parsing ROM, property definition should not be added/removed. Otherwise,
	 * cachedProperties can be un-synchronized.
	 */

	private void buildCachedPropertyDefns( )
	{
		// cached property definitions defined on parents

		ElementDefn tmpDefn = this;
		while ( tmpDefn != null )
		{
			cachedProperties
					.putAll( (Map<? extends String, ? extends IElementPropertyDefn>) tmpDefn.properties );
			tmpDefn = tmpDefn.parent;
		}
		// if hasStyle = true
		// cachedProperties has height( IStyleModel.HEIGHT_PROP ) or width ( IStyleModel.WIDTH_PROP )
		// meanwhile Element.Style has the two properties
		// and the two properties isn't styleProperty, need do: 
		// remove the two properties from cachedProperties, add Style's the two properties to cachedProperties
		// Ted 43511
		if ( hasStyle( ) )
		{
			if ( cachedProperties.get( IStyleModel.HEIGHT_PROP ) != null && !this.getName( ).equalsIgnoreCase( IStyleModel.MASTER_PAGE_PROP ) )
			{
				IPropertyDefn sourcePropertyDefn = cachedProperties.get( IStyleModel.HEIGHT_PROP );
				if ( sourcePropertyDefn instanceof IElementPropertyDefn
						&& !( (IElementPropertyDefn) sourcePropertyDefn ).isStyleProperty( ) )
				{
					ElementDefn style = (ElementDefn) MetaDataDictionary.getInstance( )
							.getStyle( );
					if ( style.getProperty( IStyleModel.HEIGHT_PROP ) != null )
					{
						cachedProperties.put( IStyleModel.HEIGHT_PROP,
								style.getProperty( IStyleModel.HEIGHT_PROP ) );
					}
				}
			}
			if ( cachedProperties.get( IStyleModel.WIDTH_PROP ) != null && !this.getName( ).equalsIgnoreCase( IStyleModel.MASTER_PAGE_PROP ) )
			{			
				IPropertyDefn sourcePropertyDefn = cachedProperties.get( IStyleModel.WIDTH_PROP );
				if ( sourcePropertyDefn instanceof IElementPropertyDefn
						&& !( (IElementPropertyDefn) sourcePropertyDefn ).isStyleProperty( ) )
				{
					ElementDefn style = (ElementDefn) MetaDataDictionary.getInstance( )
							.getStyle( );
					if ( style.getProperty( IStyleModel.WIDTH_PROP ) != null )
					{
						cachedProperties.put( IStyleModel.WIDTH_PROP,
								style.getProperty( IStyleModel.WIDTH_PROP ) );
					}
				}
			}
		}
	}

	/**
	 * Copies properties from the style onto this element. Note that the same
	 * definition object is referenced by both the style element and this
	 * element. That is, we copy the reference, not the data.
	 * <p>
	 * Containers are handled differently. Since the style system emulates CSS,
	 * containers can set all style property values, and the values cascade to
	 * the contents of the element.
	 * 
	 * @throws MetaDataException
	 *             if exception occurs when add style properties.
	 */

	private void addStyleProperties( ) throws MetaDataException
	{
		if ( !hasStyle( ) )
			return;

		if ( isContainer( ) )
		{
			// Add all style properties if this element is container and can
			// have style or the element is ExtendedItem.

			List<IElementPropertyDefn> styleProperties = MetaDataDictionary
					.getInstance( ).getStyle( ).getLocalProperties( );
			for ( int i = 0; i < styleProperties.size( ); i++ )
			{
				PropertyDefn prop = (PropertyDefn) styleProperties.get( i );
				// if current element is Row(ReportDesignConstants.ROW_ELEMENT), the width property is not allowed
				if( ReportDesignConstants.ROW_ELEMENT.equals( this.getName( ) ) && IStyleModel.WIDTH_PROP.equalsIgnoreCase( prop.getName( ) ) )
				{
					// do nothing
					;
				}
				// if current element is Column(ReportDesignConstants.COLUMN_ELEMENT), the height property is not allowed
				else if( ReportDesignConstants.COLUMN_ELEMENT.equals( this.getName( ) ) && IStyleModel.HEIGHT_PROP.equalsIgnoreCase( prop.getName( ) ) )
				{
					// do nothing
					;
				}
				else
				{
					properties.put( prop.getName( ), prop );
				}
		
			}
		}
		else
		{
			if ( stylePropertyNames == null )
				return;

			ElementDefn style = (ElementDefn) MetaDataDictionary.getInstance( )
					.getStyle( );

			// Get the style property list this element can access if it's
			// leaf element.

			for ( int i = 0; i < stylePropertyNames.size( ); i++ )
			{
				String propName = stylePropertyNames.get( i );

				// Ignore properties already defined.

				if ( cachedProperties.get( propName ) != null )
					continue;

				SystemPropertyDefn prop = (SystemPropertyDefn) style.cachedProperties
						.get( propName );

				// It is an implementation error if the style property list
				// includes
				// the name of a property that is not defined in the style
				// element,
				// or is defined, but is not one meant to be associated with an
				// element.

				if ( prop == null )
					throw new MetaDataException(
							new String[]{propName, name},
							MetaDataException.DESIGN_EXCEPTION_STYLE_PROP_NOT_FOUND );
				assert prop.isStyleProperty( );

				// Copy a reference to the style property into the property
				// list for this element.

				properties.put( prop.getName( ), prop );
			}
		}

	}

	/**
	 * Builds cached property visibility.
	 */
	protected void buildPropertiesVisibility( )
	{

		if ( parent != null && parent.cachedPropVisibilites != null )
		{
			if ( cachedPropVisibilites == null )
				cachedPropVisibilites = new HashMap<String, Integer>( );
			cachedPropVisibilites.putAll( parent.cachedPropVisibilites );
		}

		if ( propVisibilites != null )
		{
			if ( cachedPropVisibilites == null )
				cachedPropVisibilites = new HashMap<String, Integer>( );
			Iterator<String> propNames = propVisibilites.keySet( ).iterator( );

			while ( propNames.hasNext( ) )
			{
				String propName = propNames.next( );

				// Visibility should defined for an existing element property.

				if ( cachedProperties.get( propName ) == null )
				{
					String message = new MetaDataException(
							new String[]{name, propName},
							MetaDataException.DESIGN_EXCEPTION_VISIBILITY_PROPERTY_NOT_FOUND )
							.getMessage( );

					logger.log( Level.WARNING, message );
					MetaLogManager.log( message );

					continue;
				}

				Integer visibility = propVisibilites.get( propName );

				cachedPropVisibilites.put( propName, visibility );

			}
			propVisibilites = null;

		}
	}

	/**
	 * Builds the meta-data for each slotID in this container element.
	 * 
	 * @throws MetaDataException
	 *             if build of any slot failed.
	 */

	protected void buildSlots( ) throws MetaDataException
	{
		if ( slots != null && slots.size( ) == 0 )
			slots = null;
		if ( slots == null )
			return;
		for ( int i = 0; i < slots.size( ); i++ )
		{
			SlotDefn slot = (SlotDefn) slots.get( i );
			if ( slot.getSlotID( ) == DesignElement.NO_SLOT )
				slot.setSlotID( i );
			else
				isSlotIDSpecifiedByROM = true;
			slot.build( );
		}
	}

	/**
	 * Returns a list of the localized property group names defined by this
	 * element and its parents.
	 * <p>
	 * The UI uses property groups to organize properties within the generic
	 * property sheet.
	 * 
	 * @return The list of group names. If there is no groups defined on the
	 *         element, the list will has no content.
	 */

	public List<String> getGroupNames( )
	{
		// List of group names defined by this element and its parents.
		ArrayList<String> groupNames = new ArrayList<String>( );

		Iterator<IElementPropertyDefn> iter = getProperties( ).iterator( );
		while ( iter.hasNext( ) )
		{
			SystemPropertyDefn prop = (SystemPropertyDefn) iter.next( );
			String groupName = prop.getGroupName( );

			if ( groupName != null && !groupNames.contains( groupName ) )
			{
				groupNames.add( groupName );
			}
		}

		return groupNames;
	}

	/**
	 * Determines if this element allows user properties.
	 * 
	 * @return Returns true if the element supports user-defined properties,
	 *         false if not.
	 */

	public boolean allowsUserProperties( )
	{
		return supportsUserProperties;
	}

	/**
	 * Sets the option to allow user properties.
	 * 
	 * @param flag
	 *            Whether this element supports user-defined properties.
	 */

	public void setAllowsUserProperties( boolean flag )
	{
		assert !isBuilt;
		supportsUserProperties = flag;
	}

	/**
	 * Indicates whether this element is an abstract element type created as
	 * part of the element type system. Such elements are normally not visible
	 * to users -- they are used by the implementation to simplify the
	 * description of user-visible elements.
	 * 
	 * @return True if this is an invisible abstract element, false if this is a
	 *         concrete, user-visible element.
	 */

	public boolean isAbstract( )
	{
		return abstractElement;
	}

	/**
	 * Gets the predefined style for this element.
	 * 
	 * @return The predefined style .
	 */

	public String getSelector( )
	{
		return selector;
	}

	/**
	 * Gets the parent element. This is the element that this element extends.
	 * 
	 * @return Returns the parent element.
	 */

	public IElementDefn getParent( )
	{
		return parent;
	}

	public void setParent( ElementDefn parent )
	{
		this.parent = parent;
	}

	/**
	 * Adds the name of a style property that should be made visible to this
	 * element type. Must be done while creating the element type.
	 * 
	 * @param propName
	 *            The style property name to make visible.
	 */

	void addStyleProperty( String propName )
	{
		assert !isBuilt;
		if ( stylePropertyNames == null )
			stylePropertyNames = new ArrayList<String>( );
		stylePropertyNames.add( propName );
	}

	/**
	 * Gets the name space that holds this type of element.
	 * 
	 * @return The name space ID.
	 */

	public String getNameSpaceID( )
	{
		return nameConfig.nameSpaceID;
	}

	/**
	 * Gets the name option that says how the element type handles names. One of
	 * the following defined in {@link MetaDataConstants}:
	 * <ul>
	 * <li>{@link MetaDataConstants#NO_NAME}-- The element cannot have a name.
	 * (Probably not used.)</li>
	 * <li>{@link MetaDataConstants#OPTIONAL_NAME}-- The element can optionally
	 * have a name, but a name is not required.</li>
	 * <li>{@link MetaDataConstants#REQUIRED_NAME}-- The element must have a
	 * name.</li>
	 * </ul>
	 * 
	 * @return the name option
	 */

	public int getNameOption( )
	{
		return nameConfig.nameOption;
	}

	/**
	 * Determines if this element acts as a container.
	 * 
	 * @return True if this element is a container, false otherwise.
	 */

	public boolean isContainer( )
	{
		return slots != null || isContainer;
	}

	/**
	 * Returns the number of slots in this container.
	 * 
	 * @return The number of slots. Returns 0 if this element is not a
	 *         container.
	 */

	public int getSlotCount( )
	{
		if ( slots == null )
			return 0;
		return slots.size( );
	}

	/**
	 * Returns whether this element has the requested slot given the numeric
	 * identifier of the slot.
	 * 
	 * @param slotID
	 *            The slotID to check.
	 * @return True if the slotID exists, false otherwise.
	 */

	public boolean hasSlot( int slotID )
	{
		return getSlot( slotID ) != null;
	}

	/**
	 * Returns the iterator for slot defined on the element.
	 * 
	 * @return the iterator for <code>ISlotDefn</code>
	 * 
	 */

	public Iterator<ISlotDefn> slotsIterator( )
	{
		if ( slots == null )
			return Collections.<ISlotDefn> emptyList( ).iterator( );
		return slots.iterator( );
	}

	/**
	 * Returns the meta-data definition for a slot given its numeric slot
	 * identifier.
	 * 
	 * @param slotID
	 *            The slot identifier.
	 * @return The slot information. Returns null if this element is not a
	 *         container, or if the ID is not valid for this container.
	 */

	public ISlotDefn getSlot( int slotID )
	{
		if ( slots == null )
			return null;
		if ( slotID < 0 )
			return null;

		if ( !isSlotIDSpecifiedByROM )
		{
			if ( slotID >= slots.size( ) )
				return null;

			return slots.get( slotID );
		}

		// need to find the slot id since the slot id is not continuous in such
		// case

		for ( int i = 0; i < slots.size( ); i++ )
		{
			ISlotDefn tmpSlotDefn = slots.get( i );
			if ( tmpSlotDefn.getSlotID( ) == slotID )
				return tmpSlotDefn;
		}

		assert false;
		return null;
	}

	/**
	 * Reports whether the given slot can contain elements of the given type.
	 * 
	 * @param slot
	 *            The slot to check.
	 * @param type
	 *            The element type to check.
	 * @return True if the slot can contain that element type, false if the
	 *         element is not a container, if the slot does not exist, or if the
	 *         slot can't contain that type of element.
	 */

	public boolean canContain( int slot, IElementDefn type )
	{
		if ( slots == null )
			return false;
		if ( slot < 0 || slot > slots.size( ) - 1 )
			return false;
		return ( (SlotDefn) slots.get( slot ) ).canContain( type );
	}

	/**
	 * Determines if the given element type is a kind of this type. It is if
	 * either the given type is the same as this one, or if the given type
	 * derives from this type.
	 * 
	 * @param type
	 *            The element type to check.
	 * @return True if it is a kind of this element, false otherwise.
	 */

	public boolean isKindOf( IElementDefn type )
	{
		if ( type == this )
			return true;

		if ( type == null )
			return false;

		ElementDefn obj = this.parent;
		while ( obj != null )
		{
			if ( obj == type )
				return true;
			obj = obj.parent;
		}
		return false;
	}

	/**
	 * Adds a slot to this element.
	 * 
	 * @param slot
	 */

	void addSlot( SlotDefn slot )
	{
		if ( slot == null )
			return;
		if ( slots == null )
			slots = new ArrayList<ISlotDefn>( );

		slots.add( slot );
	}

	/**
	 * Sets the name space ID. Elements can have names. The names are placed
	 * into a name space on the report design. Different kinds of elements
	 * reside in different name spaces. The name space ID identifies which name
	 * space holds elements of this element type.
	 * 
	 * @param ns
	 *            The name space ID.
	 */

	void setNameSpaceID( String ns )
	{
		assert !isBuilt;
		nameConfig.nameSpaceID = ns;
		if ( MetaDataConstants.NO_NAME_SPACE.equals(  nameConfig.nameSpaceID ) )
			nameConfig.nameOption = MetaDataConstants.NO_NAME;
	}

	/**
	 * Sets the name option for the element. One of the following constants
	 * defined in {@link MetaDataConstants MetaDataConstants}:
	 * {@link MetaDataConstants#NO_NAME},
	 * {@link MetaDataConstants#OPTIONAL_NAME}, or
	 * {@link MetaDataConstants#REQUIRED_NAME}.
	 * 
	 * @param choice
	 *            The name option.
	 */

	void setNameOption( int choice )
	{
		nameConfig.nameOption = choice;
	}

	/**
	 * Sets whether the user can extend elements of this type.
	 * 
	 * @param flag
	 *            True if the element can be extended (default), or false if the
	 *            element cannot be extended.
	 */

	public void setCanExtend( boolean flag )
	{
		allowExtend = flag;
	}

	/**
	 * Returns whether elements of this class can be extended.
	 * 
	 * @return True if the element can be extended, false if not.
	 */

	public boolean canExtend( )
	{
		if ( nameConfig.getNameSpaceID( ) == MetaDataConstants.NO_NAME_SPACE )
			return allowExtend;

		// an element can extends if and only if allowExtend is true and its
		// name is unique in whole design tree
		IElementDefn holderDefn = getNameConfig( ).getNameContainer( );
		return allowExtend
				&& holderDefn != null
				&& holderDefn.isKindOf( MetaDataDictionary.getInstance( )
						.getElement( ReportDesignConstants.MODULE_ELEMENT ) );
	}

	/**
	 * Marks this element as abstract. Users never see abstract elements: they
	 * exist to simplify the description of other elements.
	 * 
	 * @param flag
	 *            true if the element is to be abstract, false if it is to be
	 *            concrete (user-visible)
	 */

	public void setAbstract( boolean flag )
	{
		abstractElement = flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.IObjectDefn#findProperty(java.
	 * lang.String)
	 */

	public IPropertyDefn findProperty( String propName )
	{
		return cachedProperties.get( propName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.ObjectDefn#addProperty(org.eclipse
	 * .birt.report.model.metadata.PropertyDefn)
	 */

	public void addProperty( PropertyDefn property ) throws MetaDataException
	{
		if ( property == null )
			return;
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );

		// Check whether parent element define the property with the same name.

		if ( dd.getElement( extendsFrom ) != null )
		{
			ElementDefn parentTemp = (ElementDefn) dd.getElement( extendsFrom );
			while ( parentTemp != null )
			{
				if ( parentTemp.properties.containsKey( property.getName( ) ) )
				{
					throw new MetaDataException(
							new String[]{property.getName( ), this.name},
							MetaDataException.DESIGN_EXCEPTION_DUPLICATE_PROPERTY );
				}
				parentTemp = (ElementDefn) dd.getElement( parentTemp
						.getExtends( ) );
			}
		}

		// Check whether style element define the property with the same name.

		if ( hasStyle( ) )
		{
			ElementDefn styleDefn = (ElementDefn) dd
					.getElement( ReportDesignConstants.STYLE_ELEMENT );

			// Style should be defined before any element that has a
			// style(styled element).

			if ( styleDefn == null )
			{
				throw new MetaDataException( new String[]{this.name},
						MetaDataException.DESIGN_EXCEPTION_STYLE_NOT_DEFINED );
			}

			if ( styleDefn.properties.containsKey( property.getName( ) ) )
			{
				// Ted 43511
				if( IStyleModel.HEIGHT_PROP.equals( property.getName( ) ) || IStyleModel.WIDTH_PROP.equals( property.getName( )))
				{
					// do nothing
					;
				}
				else
				{
					throw new MetaDataException( new String[]{property.getName( ),
							this.name},
							MetaDataException.DESIGN_EXCEPTION_DUPLICATE_PROPERTY );
				}
			}

		}
		super.addProperty( property );

		// to avoid label, data becomes container, check whether the current
		// property is multiple views. That is, multiple view property will not
		// determine whether an element is the container

		if ( property.getType( ) != null
				&& property.isElementType( )
				&& !IReportItemModel.MULTI_VIEWS_PROP
						.equalsIgnoreCase( property.getName( ) )
				&& !isContainer )
			isContainer = true;
	}

	/**
	 * Returns the semantic validation trigger definition collection.
	 * 
	 * @return the semantic validation trigger definition collection
	 */

	public SemanticTriggerDefnSet getTriggerDefnSet( )
	{
		if ( triggerDefnSet == null )
			triggerDefnSet = new SemanticTriggerDefnSet( );

		return triggerDefnSet;
	}

	/**
	 * Adds an invisible property to the list.
	 * 
	 * @param propName
	 *            the property name
	 * @param propVisibility
	 *            the level that how to show the property in the property sheet.
	 */

	public void addPropertyVisibility( String propName, String propVisibility )
	{
		if ( propVisibilites == null )
			propVisibilites = new HashMap<String, Integer>( );

		propVisibilites.put( propName, getIntVisibility( propVisibility ) );
	}

	/**
	 * Builds the string property visibility to get the integer values. The
	 * string is separated by ',' to multiple options.
	 * 
	 * @param propVisibility
	 *            the string property visibility
	 * @return
	 */
	private Integer getIntVisibility( String propVisibility )
	{
		propVisibility = StringUtil.trimString( propVisibility );
		if ( propVisibility == null )
			return Integer.valueOf( NO_VISIBILITY_KEY );

		int intVisibility = NO_VISIBILITY_KEY;

		String[] values = propVisibility.split( VISIBILITY_SEPERATOR );
		for ( int i = 0; i < values.length; i++ )
		{

			if ( HIDDEN_IN_PROPERTY_SHEET.equalsIgnoreCase( values[i] ) )
			{
				intVisibility |= HIDDEN_IN_PROPERTY_SHEET_KEY;
			}
			else if ( UNHIDDEN_IN_PROPERTY_SHEET.equalsIgnoreCase( values[i] ) )
			{
				intVisibility &= ~HIDDEN_IN_PROPERTY_SHEET_KEY;
			}
			else if ( READONLY_IN_PROPERTY_SHEET.equalsIgnoreCase( values[i] ) )
			{
				intVisibility |= READONLY_KEY;
			}
		}
		return Integer.valueOf( intVisibility );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IElementDefn#isPropertyReadOnly
	 * (java.lang.String)
	 */

	public boolean isPropertyReadOnly( String propName )
	{
		IPropertyDefn propDefn = cachedProperties.get( propName );
		if ( propDefn == null )
			return true;

		int visibility = getPropertyVisibility( propDefn.getName( ) );
		if ( ( visibility & READONLY_KEY ) != 0 )
			return true;

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IElementDefn#isPropertyVisible
	 * (java.lang.String)
	 */

	public boolean isPropertyVisible( String propName )
	{
		IPropertyDefn propDefn = cachedProperties.get( propName );
		if ( propDefn == null )
			return false;

		if ( propDefn.getTypeCode( ) == IPropertyType.STRUCT_TYPE )
			return false;

		if ( ( (PropertyDefn) propDefn ).isElementType( ) )
			return false;

		int visibility = getPropertyVisibility( propDefn.getName( ) );
		if ( ( HIDDEN_IN_PROPERTY_SHEET_KEY & visibility ) != 0 )
			return false;

		return true;
	}

	/**
	 * Returns the visibility of a given property.
	 * 
	 * @param propName
	 *            the property name
	 * @return the visibility of the property
	 */

	private int getPropertyVisibility( String propName )
	{
		if ( cachedPropVisibilites == null )
			return NO_VISIBILITY_KEY;

		return cachedPropVisibilites.get( propName ) == null
				? NO_VISIBILITY_KEY
				: cachedPropVisibilites.get( propName ).intValue( );
	}

	/**
	 * Set the name of the XML element for this ROM element.
	 * 
	 * @param value
	 *            the name of the XML element
	 */

	public void setXmlName( String value )
	{
		xmlName = value;
	}

	/**
	 * 
	 * @return the name of the XML element used to serialize this ROM element.
	 */

	public String getXmlName( )
	{
		return xmlName;
	}

	/**
	 * Justifies whether this definition is extension element.
	 * 
	 * @return true if it is extension element
	 */
	public boolean isExtendedElement( )
	{
		if ( name.equalsIgnoreCase( ReportDesignConstants.EXTENDED_ITEM )
				|| MetaDataDictionary.getInstance( ).getExtension( name ) != null )
			return true;
		return false;
	}

	/**
	 * Gets the name configuration of this element.
	 * 
	 * @return the name configuration of this element definition
	 */
	public NameConfig getNameConfig( )
	{
		return this.nameConfig;
	}
}
