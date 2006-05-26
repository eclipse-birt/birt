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

package org.eclipse.birt.report.model.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.api.command.TemplateException;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.command.ContentCommand;
import org.eclipse.birt.report.model.command.ExtendsCommand;
import org.eclipse.birt.report.model.command.NameCommand;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.command.StyleCommand;
import org.eclipse.birt.report.model.command.TemplateCommand;
import org.eclipse.birt.report.model.command.UserPropertyCommand;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TemplateElement;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;

/**
 * Base class for all report elements. Provides a high-level interface to the
 * BIRT report model. This class provides generic services for all elements.
 * Derived classes provide specialized methods for each element type.
 * <p>
 * Element handles are immutable. Once created, they always point to the same
 * element. Handles also cannot point to a null element. An assertion will be
 * raised if the derived classes are created with a null element.
 * <p>
 * For a complete description of the services available for any design element,
 * see the class description for the
 * {@link org.eclipse.birt.report.model.core.DesignElement}class.
 * 
 * @see SlotHandle
 * @see org.eclipse.birt.report.model.core.DesignElement
 */

public abstract class DesignElementHandle implements IDesignElementModel
{

	/**
	 * Provides overall information about the root element. If the element is on
	 * the design tree, the root is cooresponding library/design. If the element
	 * is not on the tree, the root is the module of ElementFactory that creates
	 * this <code>DesignElementHandle</code>.
	 */

	protected final Module module;

	/**
	 * API slot handle array for all slots of this element.
	 */

	private SlotHandle[] slotHandles = null;

	/**
	 * Constructs a handle with the given module.
	 * 
	 * @param module
	 *            the module
	 */

	public DesignElementHandle( Module module )
	{
		this.module = module;
	}

	/**
	 * Returns the report design only when the module is report design.
	 * Otherwise, null is returned. So this method can also be used to check
	 * whether this element is in report design or library.
	 * 
	 * @return the report design, or null if the module is not report design.
	 * @deprecated replaced by getModule()
	 */

	public ReportDesign getDesign( )
	{
		if ( module instanceof ReportDesign )
			return (ReportDesign) module;

		return null;
	}

	/**
	 * Returns the module on which this handle is attached. The returned module
	 * must be <code>ReportDesign</code> or <code>Library</code>.
	 * 
	 * @return the module on which this handle is attached.
	 */

	public Module getModule( )
	{
		return module;
	}

	/**
	 * Returns the handle of report design only when the module is report
	 * design. Otherwise, null is returned.
	 * 
	 * @return the handle of report design, or null if the module is not report
	 *         design.
	 * @deprecated replaced by getModuleHandle()
	 */

	public ReportDesignHandle getDesignHandle( )
	{
		if ( getDesign( ) != null )
			return (ReportDesignHandle) getDesign( ).getHandle( module );

		return null;
	}

	/**
	 * Returns the handle of module. The returned module must be
	 * <code>ReportDesignHandle</code> or <code>LibraryHandle</code>.
	 * 
	 * @return the handle of module
	 */

	public ModuleHandle getModuleHandle( )
	{
		return (ModuleHandle) getModule( ).getHandle( getModule( ) );
	}

	/**
	 * Returns the element to which this handle is attached.
	 * 
	 * @return The attached element. Will be null if the handle is not attached.
	 */

	public abstract DesignElement getElement( );

	/**
	 * Gets the definition of the element. The definition provides meta-data
	 * about the element, such as its type name, list of defined properties,
	 * list of slots and so on.
	 * 
	 * @return the meta-data definition of the element
	 */

	public IElementDefn getDefn( )
	{
		return getElement( ).getDefn( );
	}

	/**
	 * Returns the value of a property as a generic object. The value is the
	 * internal property value, it is not localized. For a property with the
	 * element reference type, the reference element name is returned.
	 * 
	 * @param propName
	 *            the name of the property of interest
	 * @return the value of the property. The type of the returned object
	 *         depends on the property type.
	 * @see #getIntProperty
	 * @see #getStringProperty
	 * @see #getFloatProperty
	 * @see #getNumberProperty
	 */

	public Object getProperty( String propName )
	{
		// Must be attached to use this method.

		DesignElement element = getElement( );
		Object value = element.getProperty( module, propName );

		if ( value instanceof ReferenceValue )
			return ReferenceValueUtil.needTheNamespacePrefix(
					(ReferenceValue) value, getModule( ) );

		PropertyDefn defn = (PropertyDefn) getPropertyDefn( propName );
		if ( value instanceof List && defn != null
				&& defn.getSubTypeCode( ) == PropertyType.LIST_TYPE )
		{
			List valueList = (List) value;
			List names = new ArrayList( );
			for ( int i = 0; i < valueList.size( ); i++ )
			{
				ElementRefValue item = (ElementRefValue) valueList.get( i );
				names.add( ReferenceValueUtil.needTheNamespacePrefix( item,
						getModule( ) ) );
			}
			return names;
		}

		return value;

	}

	/**
	 * Returns the value of a property as a string which is locale independent.
	 * 
	 * @param propName
	 *            the name of the property
	 * @return the internal string property value of the property.
	 */

	public String getStringProperty( String propName )
	{
		return getElement( ).getStringProperty( module, propName );
	}

	/**
	 * Return a localized string version of the property. This method will works
	 * for all property types except a list. Dates and numbers will be converted
	 * using the current locale. Display name will be returned for a choice or a
	 * color property.
	 * 
	 * @param propName
	 *            the name of the property
	 * @return a localized string version of the property.
	 */

	public String getDisplayProperty( String propName )
	{
		return getElement( ).getDisplayProperty( module, propName );
	}

	/**
	 * Returns the value of a property as a Boolean.
	 * 
	 * @param propName
	 *            the name of the property
	 * @return the value as a boolean
	 */

	public boolean getBooleanProperty( String propName )
	{
		return getElement( ).getBooleanProperty( module, propName );
	}

	/**
	 * Returns the value of a property as an integer.
	 * 
	 * @param propName
	 *            the name of the property.
	 * @return the value as an integer. Return 0 if the value cannot be
	 *         converted to an integer.
	 */

	public int getIntProperty( String propName )
	{
		return getElement( ).getIntProperty( module, propName );
	}

	/**
	 * Returns the value of a property as a double.
	 * 
	 * @param propName
	 *            the name of the property.
	 * @return the value as a double. Returns <code>null</code> if the value
	 *         cannot be converted to a double.
	 */

	public double getFloatProperty( String propName )
	{
		return getElement( ).getFloatProperty( module, propName );
	}

	/**
	 * Returns the value of a property as a number (BigDecimal).
	 * 
	 * @param propName
	 *            the name of the property.
	 * @return the value as a number. Returns <code>null</code> if the value
	 *         cannot be converted to a number.
	 */

	public BigDecimal getNumberProperty( String propName )
	{
		return getElement( ).getNumberProperty( module, propName );
	}

	/**
	 * Returns a handle to work with a Dimension property. Returns null if the
	 * given property is not defined or not dimension property.
	 * 
	 * @param propName
	 *            name of the property.
	 * @return a corresponding DimensionHandle to deal with the dimension
	 *         property. Return <code>null</code> if the property is defined
	 *         or not dimension property.
	 * 
	 * @see DimensionHandle
	 */

	public DimensionHandle getDimensionProperty( String propName )
	{
		ElementPropertyDefn propDefn = getElement( ).getPropertyDefn( propName );
		if ( propDefn == null )
			return null;

		if ( propDefn.getTypeCode( ) != PropertyType.DIMENSION_TYPE )
			return null;

		return new DimensionHandle( this, propDefn );
	}

	/**
	 * Returns a handle to work with a color property. Returns null if the given
	 * property is not defined or not color property.
	 * 
	 * @param propName
	 *            name of the property.
	 * @return a corresponding ColorHandle to with with the color property.
	 *         Return <code>null</code> if the given property is not defined
	 *         or not color property.
	 * 
	 * @see ColorHandle
	 */

	public ColorHandle getColorProperty( String propName )
	{
		ElementPropertyDefn propDefn = getElement( ).getPropertyDefn( propName );
		if ( propDefn == null )
			return null;

		if ( propDefn.getTypeCode( ) != PropertyType.COLOR_TYPE )
			return null;

		return new ColorHandle( this, propDefn );
	}

	/**
	 * Gets the font handle for the element. If this element defines a font
	 * family property, return a <code>FontHandle</code>. Otherwise, return
	 * <code>null</code>.
	 * 
	 * @return a corresponding FontHandle or <code>null</code>.
	 * 
	 * @see FontHandle
	 */

	protected FontHandle getFontProperty( )
	{
		ElementPropertyDefn propDefn = getElement( ).getPropertyDefn(
				Style.FONT_FAMILY_PROP );

		if ( propDefn == null )
			return null;

		return new FontHandle( this );
	}

	/**
	 * Returns the value of an element reference property. Returns a handle to
	 * the referenced element, or <code>null</code> if the reference is
	 * unresolved or unset.
	 * 
	 * @param propName
	 *            the name of the property.
	 * @return a corresponding DesignElement handle to the referenced element
	 */

	public DesignElementHandle getElementProperty( String propName )
	{
		DesignElement target = getElement( ).getReferenceProperty( module,
				propName );
		if ( target == null )
			return null;
		return target.getHandle( target.getRoot( ) );
	}

	/**
	 * Gets the value of a property as a list.
	 * 
	 * @param module
	 *            the module
	 * @param propName
	 *            the name of the property to get
	 * @return the value as an <code>ArrayList</code>, or null if the
	 *         property is not set or the value is not a list
	 * @deprecated
	 */

	public List getListProperty( Module module, String propName )
	{
		return getElement( ).getListProperty( module, propName );
	}

	/**
	 * Gets the value of a property as a list.
	 * 
	 * @param module
	 *            the module
	 * @param propName
	 *            the name of the property to get
	 * @return the value as an <code>ArrayList</code>, or null if the
	 *         property is not set or the value is not a list
	 */

	public List getListProperty( String propName )
	{
		return getElement( ).getListProperty( module, propName );
	}

	/**
	 * Sets the value of a property from a generic object. The value can be any
	 * of the supported types: String, Double, Integer, BigDecimal or one of the
	 * specialized property types. The type of object allowed depends on the
	 * type of the property.
	 * 
	 * @param propName
	 *            the property name
	 * @param value
	 *            the value to set
	 * @throws SemanticException
	 *             if the property is undefined on the element or the value is
	 *             invalid.
	 */

	public void setProperty( String propName, Object value )
			throws SemanticException
	{
		// Must be attached to use this method.

		DesignElement element = getElement( );
		PropertyCommand cmd = new PropertyCommand( module, element );
		cmd.setProperty( propName, value );
	}

	/**
	 * Sets the value of a property to an integer.
	 * 
	 * @param propName
	 *            the property name
	 * @param value
	 *            the value to set
	 * @throws SemanticException
	 *             If the property value cannot be converted from an integer, or
	 *             if the value of a choice is incorrect.
	 */

	public void setIntProperty( String propName, int value )
			throws SemanticException
	{
		setProperty( propName, new Integer( value ) );
	}

	/**
	 * Sets the value of a property to a string. Use this for properties such as
	 * expressions, labels, HTML, or XML. Also use it to set the value of a
	 * choice using the internal string name of the choice. Use it to set the
	 * value of a dimension when using specified units, such as "10pt".
	 * 
	 * @param propName
	 *            the property name
	 * @param value
	 *            the value to set
	 * @throws SemanticException
	 *             if the value of a choice or other property is incorrect.
	 */

	public void setStringProperty( String propName, String value )
			throws SemanticException
	{
		setProperty( propName, value );
	}

	/**
	 * Sets the value of a property to a double. When used for dimension
	 * properties, the units of the dimension are assumed to be in application
	 * units.
	 * 
	 * @param propName
	 *            the property name
	 * @param value
	 *            the value to set
	 * @throws SemanticException
	 *             If the property value cannot be converted from a double.
	 */

	public void setFloatProperty( String propName, double value )
			throws SemanticException
	{
		setProperty( propName, new Double( value ) );
	}

	/**
	 * Sets the value of a property to a number (BigDecimal).
	 * 
	 * @param propName
	 *            the property name
	 * @param value
	 *            the value to set
	 * @throws SemanticException
	 *             if the property value cannot be converted from a number.
	 */

	public void setNumberProperty( String propName, BigDecimal value )
			throws SemanticException
	{
		setProperty( propName, value );
	}

	/**
	 * Clears the value of a property. Clearing a property removes any value set
	 * for the property on this element. After this, the element will now
	 * inherit the property from its parent element, style, or from the default
	 * value for the property.
	 * 
	 * @param propName
	 *            the name of the property to clear.
	 * @throws SemanticException
	 *             if the property is not defined on this element
	 */

	public void clearProperty( String propName ) throws SemanticException
	{
		setProperty( propName, null );
	}

	/**
	 * Clears values of all properties. Clearing a property removes any value
	 * set for the property on this element. After this, the element will now
	 * inherit the property from its parent element, style, or from the default
	 * value for the property.
	 * 
	 * @throws SemanticException
	 *             if the property is not defined on this element
	 */

	public void clearAllProperties( ) throws SemanticException
	{
		List props = getDefn( ).getProperties( );

		for ( int i = 0; i < props.size( ); i++ )
		{
			PropertyDefn propDefn = (PropertyDefn) props.get( i );
			String propName = propDefn.getName( );

			if ( ( DesignElement.NAME_PROP.equals( propName ) ) )
			{
				NameCommand nameCmd = new NameCommand( module, getElement( ) );
				try
				{
					nameCmd.checkName( null );
				}
				catch ( NameException e )
				{
					continue;
				}
			}

			PropertyHandle propHandle = getPropertyHandle( propName );
			if ( propHandle.isLocal( ) )
				propHandle.clearValue( );
		}
	}

	/**
	 * Returns <code>true</code> if this element has any locally-defined
	 * property values. Returns <code>false</code> otherwise.
	 * 
	 * @return True if the element has property values, false if not.
	 */

	public boolean hasLocalProperties( )
	{
		return getElement( ).hasLocalPropertyValues( );
	}

	/**
	 * Adds a user-defined property definition. The property definition must
	 * have a valid name and property type. The other attributes can be set
	 * either before or after adding the property to the element. The
	 * application can modify the property definition directly before adding the
	 * property to an element. However, the application must use a structure
	 * handle to modify the definition after it has been added to an element.
	 * 
	 * @param prop
	 *            the user property definition to add. The name and property
	 *            type must be valid.
	 * @throws UserPropertyException
	 *             if the element is not allowed to have user property or the
	 *             user property definition is invalid, or if the value of the
	 *             user-defined choice is invalid for the type of user property
	 *             definition, the property type is incorrect.
	 */

	public void addUserPropertyDefn( UserPropertyDefn prop )
			throws UserPropertyException
	{
		DesignElement element = getElement( );
		UserPropertyCommand cmd = new UserPropertyCommand( module, element );
		cmd.addUserProperty( prop );
	}

	/**
	 * Adds a report item to the given slot at the given position. The item must
	 * be newly created and not yet added to the design.
	 * 
	 * @param child
	 *            handle to the newly created element
	 * @param slotId
	 *            slot id in which the child item will be added.
	 * @param pos
	 *            position in the slot that the child item will be added.
	 * @throws ContentException
	 *             if the element is not allowed in the slot
	 * @throws NameException
	 *             if the element has a duplicate or illegal name
	 * 
	 */

	public void addElement( DesignElementHandle child, int slotId, int pos )
			throws ContentException, NameException
	{
		SlotHandle slotHandle = getSlot( slotId );

		if ( slotHandle != null )
			slotHandle.add( child, pos );
	}

	/**
	 * Adds a report item to the end of the given slot. The item must be newly
	 * created and not yet added to the design.
	 * 
	 * @param child
	 *            handle to the newly created element
	 * @param slotId
	 *            slot id in which the child item will be added.
	 * @throws ContentException
	 *             if the element is not allowed in the slot
	 * @throws NameException
	 *             if the element has a duplicate or illegal name
	 * 
	 */

	public void addElement( DesignElementHandle child, int slotId )
			throws ContentException, NameException
	{
		SlotHandle slotHandle = getSlot( slotId );

		if ( slotHandle != null )
			slotHandle.add( child );
	}

	/**
	 * Returns the methods defined on the element definition.
	 * 
	 * @return List contains the methods.
	 * 
	 */
	public List getMethods( )
	{
		return getElement( ).getDefn( ).getMethods( );

	}

	/**
	 * Sets a bunch of property values on the element. If this operation should
	 * be treated as a whole, execution should be in a transaction.
	 * 
	 * @param properties
	 *            a Map store the property values keyed by the property name.
	 * @throws SemanticException
	 *             if the property is undefined on the element or the value is
	 *             invalid.
	 * @see #setProperty(String, Object)
	 */

	public void setProperties( Map properties ) throws SemanticException
	{
		if ( properties == null )
			return;

		for ( Iterator iter = properties.keySet( ).iterator( ); iter.hasNext( ); )
		{
			String propName = (String) iter.next( );
			setProperty( propName, properties.get( propName ) );
		}
	}

	/**
	 * Clears all the content within the given slot. If this operation should be
	 * treated as a whole, execution should be in a transaction.
	 * 
	 * @param slotId
	 *            id of the slot to be cleared.
	 * @throws SemanticException
	 */

	public void clearContents( int slotId ) throws SemanticException
	{
		SlotHandle slotHandle = getSlot( slotId );
		if ( slotHandle == null )
			return;

		for ( int i = slotHandle.getCount( ) - 1; i >= 0; i-- )
		{
			slotHandle.dropAndClear( i );
		}
	}

	/**
	 * Removes an existing user property definition from the element. This
	 * method will remove all existing values for the property, in both this
	 * element and all its derived elements.
	 * 
	 * @param propName
	 *            the name of the user property to remove
	 * @throws UserPropertyException
	 *             If the property is not found.
	 */

	public void dropUserPropertyDefn( String propName )
			throws UserPropertyException
	{
		DesignElement element = getElement( );
		UserPropertyCommand cmd = new UserPropertyCommand( module, element );
		cmd.dropUserProperty( propName );
	}

	/**
	 * Returns a handle to the element that this element extends.
	 * 
	 * @return the parent element. Returns <code>null</code> if this element
	 *         does not have a parent, or if the extends name does not resolve
	 *         to a valid element.
	 */

	public DesignElementHandle getExtends( )
	{
		DesignElement parent = getElement( ).getExtendsElement( );
		if ( parent == null )
			return null;
		return parent.getHandle( parent.getRoot( ) );
	}

	/**
	 * Sets this element to extend the given element.
	 * 
	 * @param parent
	 *            handle to the element that this element is to extend. If
	 *            <code>null</code>, then this element will no longer extend
	 *            another element.
	 * @throws ExtendsException
	 *             if the parent element is of the wrong type as this element.
	 */

	public void setExtends( DesignElementHandle parent )
			throws ExtendsException
	{
		ExtendsCommand cmd = new ExtendsCommand( module, getElement( ) );
		cmd.setExtendsElement( parent );
	}

	/**
	 * Sets the name of the element that this element extends. The name must
	 * resolve to an element of the same type as this element.
	 * 
	 * @param name
	 *            the name of the element that this one is to extend
	 * @throws ExtendsException
	 *             if no element exists with the given name, or if the element
	 *             is of the wrong type.
	 */

	public void setExtendsName( String name ) throws ExtendsException
	{
		DesignElement element = getElement( );
		ExtendsCommand cmd = new ExtendsCommand( module, element );
		cmd.setExtendsName( name );
	}

	/**
	 * Localize the element, break the parent/child relationship and set all the
	 * extended properties locally.
	 * 
	 * @throws SemanticException
	 *             the element can not be localized properly. It may be because
	 *             that the element is not extended from a parent, or that same
	 *             properties can not be localized on the element or the content
	 *             elements inside it.
	 */

	public void localize( ) throws SemanticException
	{
		DesignElement element = getElement( );
		ExtendsCommand cmd = new ExtendsCommand( module, element );
		cmd.localizeElement( );
	}

	/**
	 * Sets this element to extend the given element.
	 * 
	 * @param parent
	 *            the element that this element is to extend
	 * @throws ExtendsException
	 *             If the parent element is of the wrong type as this element.
	 * 
	 * @deprecated by {@link #setExtends(DesignElementHandle)}
	 */

	public void setExtendsElement( DesignElement parent )
			throws ExtendsException
	{
		ExtendsCommand cmd = new ExtendsCommand( module, getElement( ) );
		cmd.setExtendsElement( parent );
	}

	/**
	 * Returns the shared style, if any, as a style handle. A shared style is a
	 * style that defines in the "styles" slot of the report design.
	 * 
	 * @return a handle to the shared style used by this element. Returns null
	 *         if the style is not set, or if the element does not support a
	 *         style.
	 * 
	 * @see #setStyle(SharedStyleHandle)
	 * @see #setStyleElement(StyleElement)
	 * @see #setStyleName(String)
	 */

	public SharedStyleHandle getStyle( )
	{
		DesignElement style = getElement( ).getStyle( module );
		if ( style == null )
			return null;
		return (SharedStyleHandle) style.getHandle( module );
	}

	/**
	 * Sets the name of the shared style for this element.
	 * 
	 * @param name
	 *            the name of the shared style. If <code>null</code>, then
	 *            the shared style name is cleared.
	 * @throws StyleException
	 *             If the name is not valid, or if this element does not support
	 *             a style.
	 * 
	 * @see #getStyle()
	 */

	public void setStyleName( String name ) throws StyleException
	{
		DesignElement element = getElement( );
		StyleCommand cmd = new StyleCommand( module, element );
		cmd.setStyle( name );
	}

	/**
	 * Sets the shared style element for this element.
	 * 
	 * @param obj
	 *            the shared style. If <code>null</code>, then the shared
	 *            style is cleared.
	 * @throws StyleException
	 *             If this element does not support a style.
	 * @see #getStyle()
	 * 
	 * @deprecated by {@link #setStyle(SharedStyleHandle)}
	 */

	public void setStyleElement( StyleElement obj ) throws StyleException
	{
		DesignElement element = getElement( );
		StyleCommand cmd = new StyleCommand( module, element );
		cmd.setStyleElement( obj );
	}

	/**
	 * Sets the shared style element for this element.
	 * 
	 * @param style
	 *            the handle to the shared style. If null, then the shared style
	 *            is cleared.
	 * @throws StyleException
	 *             if this element does not support a style.
	 * @see #getStyle()
	 */

	public void setStyle( SharedStyleHandle style ) throws StyleException
	{
		if ( style == null )
			setStyleElement( null );
		else
			setStyleElement( (StyleElement) style.getElement( ) );
	}

	/**
	 * Returns a handle to work with the style properties of this element. Use a
	 * style handle to work with the specific getter/setter methods for each
	 * style property. The style handle is not necessary to work with style
	 * properties generically.
	 * <p>
	 * Note a key difference between this method and the
	 * <code>getStyle( )</code> method. This method returns a handle to the
	 * <em>this</em> element. The <code>getStyle( )</code> method returns a
	 * handle to the shared style, if any, that this element references.
	 * 
	 * @return a style handle to work with the style properties of this element.
	 *         Returns <code>null</code> if this element does not have style
	 *         properties.
	 */

	public StyleHandle getPrivateStyle( )
	{
		if ( getDefn( ).hasStyle( ) )
			return new PrivateStyleHandle( module, getElement( ) );
		return null;
	}

	/**
	 * Returns the name of this element. Returns <code>null</code> if the
	 * element does not have a name. Many elements do not require a name. The
	 * name does not inherit. If this element does not have a name, it will not
	 * inherit the name of its parent element.
	 * 
	 * @return the element name, or null if the name is not set
	 */

	public String getName( )
	{
		return getElement( ).getName( );
	}

	/**
	 * Gets the name of this element. The returned element name will be the same
	 * as <CODE>getName()</CODE>, plus the namespace of the module that the
	 * elment is contained, if any. If the element is existed in the current
	 * module,this method and <CODE>getName()</CODE> will return identical
	 * results.
	 * 
	 * @return the qualified name of thie element.
	 */

	public String getQualifiedName( )
	{
		String name = getName( );

		if ( name == null )
			return null;

		Module rootElement = getElement( ).getRoot( );
		if ( rootElement instanceof Library )
		{
			String namespace = ( (Library) rootElement ).getNamespace( );
			return StringUtil.buildQualifiedReference( namespace, name );
		}

		return name;
	}

	/**
	 * Sets the name of this element. If the name is <code>null</code>, then
	 * the name is cleared if this element does not require a name.
	 * 
	 * @param name
	 *            the new name
	 * @throws NameException
	 *             if the name is duplicate, or if the name is <code>null</code>
	 *             and this element requires a name.
	 */

	public void setName( String name ) throws NameException
	{
		NameCommand cmd = new NameCommand( module, getElement( ) );
		cmd.setName( name );

	}

	/**
	 * Returns the unique ID for this object. The ID is valid only within this
	 * one design session. IDs are available only if the application is
	 * configured to use IDs. In general, the web client requires IDs, but the
	 * Eclipse client does not.
	 * 
	 * @return the element ID
	 * @see ModuleHandle#getElementByID
	 * @see org.eclipse.birt.report.model.metadata.MetaDataDictionary#enableElementID
	 */

	public long getID( )
	{
		return getElement( ).getID( );
	}

	/**
	 * Returns the element factory for creating new report elements. After
	 * creating the element, add it to the design by calling the the
	 * <code>{@link SlotHandle#add( DesignElementHandle ) add}</code> method
	 * of the slot handle that represents the point in the design where the new
	 * element should appear.
	 * 
	 * @return a handle to the new element.
	 * @see SlotHandle
	 */

	public ElementFactory getElementFactory( )
	{
		return new ElementFactory( module );
	}

	/**
	 * Returns a property handle for a top-level property. A top-level property
	 * is a property that defines on an element.
	 * 
	 * @param propName
	 *            the name of the property to get
	 * @return The property handle, or <code>null</code> if the no property
	 *         exists with the given name.
	 * @see PropertyHandle
	 */

	public PropertyHandle getPropertyHandle( String propName )
	{
		DesignElement element = getElement( );
		ElementPropertyDefn propDefn = element.getPropertyDefn( propName );
		if ( propDefn == null )
			return null;
		return new PropertyHandle( this, propDefn );
	}

	/**
	 * Returns a user-defined property handle for a top-level property. A
	 * top-level property is a property that defines on an element.
	 * 
	 * @param propName
	 *            the name of the property to get
	 * @return the user property definition handle, or <code>null</code> if
	 *         the no property exists with the given name or it is not a
	 *         user-defined property.
	 */

	public UserPropertyDefnHandle getUserPropertyDefnHandle( String propName )
	{
		DesignElement element = getElement( );
		ElementPropertyDefn propDefn = element.getPropertyDefn( propName );
		if ( propDefn == null )
			return null;
		if ( propDefn.isUserProperty( ) )
		{
			return new UserPropertyDefnHandle( this,
					(UserPropertyDefn) propDefn );
		}

		return null;
	}

	/**
	 * Returns a list of user properties defined in this element and somewhere
	 * up the inheritance chain. Each object in the list is instance of
	 * <code>UserPropertyDefn</code>.
	 * 
	 * @return The list of user property definitions
	 */

	public List getUserProperties( )
	{
		return getElement( ).getUserProperties( );
	}

	/**
	 * Returns a handle for a top-level property for use in preparing the
	 * Factory data structures. This handle follows specialized rules:
	 * <p>
	 * <ul>
	 * <li>Optimized to get each property value only once.</li>
	 * <li>Indicates if the value is a style property.</li>
	 * <li>Indicates if a style property is set on the element's private style
	 * or a shared style.</li>
	 * <li>Performs property conversions as needed for the Factory context.
	 * </li>
	 * </ul>
	 * 
	 * @param propName
	 *            the name of the property to get
	 * @return the factory property handle, or <code>null</code> if either 1)
	 *         no property exists with the given name or 2) the property is a
	 *         style property and is not set in a private style.
	 */

	public FactoryPropertyHandle getFactoryPropertyHandle( String propName )
	{
		DesignElement element = getElement( );
		ElementPropertyDefn propDefn = element.getPropertyDefn( propName );
		if ( propDefn == null )
			return null;
		FactoryPropertyHandle handle = new FactoryPropertyHandle( this,
				propDefn );
		if ( handle.isSet( ) )
			return handle;
		return null;
	}

	/**
	 * Returns a handle to the element that contains this element. Every element
	 * except the report design has a container. The container of a parameter,
	 * shared style, data source or data set is the report design itself. The
	 * container of a report item is the section or other report item in which
	 * it appears.
	 * 
	 * @return a handle to the container element.
	 */

	public DesignElementHandle getContainer( )
	{
		DesignElement element = getElement( ).getContainer( );
		if ( element == null )
			return null;
		return element.getHandle( module );
	}

	/**
	 * Moves this element to a new location within the design.
	 * 
	 * @param newContainer
	 *            the new container element
	 * @param toSlot
	 *            the target slot within the new container
	 * @throws ContentException
	 *             If the element cannot be placed into the target element or
	 *             slot, perhaps because the element is of the wrong type, the
	 *             slot is full, or other error.
	 * @see SlotHandle
	 */

	public void moveTo( DesignElementHandle newContainer, int toSlot )
			throws ContentException
	{
		DesignElement element = getElement( );
		DesignElement oldContainer = element.getContainer( );
		if ( oldContainer == null )
			throw new ContentException( element, -1,
					ContentException.DESIGN_EXCEPTION_HAS_NO_CONTAINER );
		int oldSlot = element.getContainerSlot( );
		assert oldSlot != -1;
		ContentCommand cmd = new ContentCommand( module, oldContainer );
		cmd.move( element, oldSlot, newContainer.getElement( ), toSlot );
	}

	/**
	 * Drops this element from the design. Removes the element from its
	 * container and name space, if any.
	 * <p>
	 * Note: If this element is referenceable, the property referring it will be
	 * set null.
	 * 
	 * @throws SemanticException
	 *             if this element has no container or the element cannot be
	 *             dropped.
	 * @see SlotHandle
	 * @see #drop()
	 */

	public void dropAndClear( ) throws SemanticException
	{
		DesignElement element = getElement( );
		DesignElement container = element.getContainer( );
		if ( container == null )
			throw new ContentException( element, -1,
					ContentException.DESIGN_EXCEPTION_HAS_NO_CONTAINER );

		int slotID = element.getContainerSlot( );
		assert slotID != -1;
		ContentCommand cmd = new ContentCommand( module, container );
		cmd.remove( element, slotID, false );
	}

	/**
	 * Drops this element from the design. Removes the element from its
	 * container and name space, if any.
	 * <p>
	 * Note: If this element is referenceable, the property referring it will be
	 * unresolved.
	 * 
	 * @throws SemanticException
	 *             if this element has no container or the element cannot be
	 *             dropped.
	 * @see SlotHandle
	 * @see #dropAndClear()
	 */

	public void drop( ) throws SemanticException
	{
		DesignElement element = getElement( );
		DesignElement container = element.getContainer( );
		if ( container == null )
			throw new ContentException( element, -1,
					ContentException.DESIGN_EXCEPTION_HAS_NO_CONTAINER );

		int slotID = element.getContainerSlot( );
		assert slotID != -1;
		ContentCommand cmd = new ContentCommand( module, container );
		cmd.remove( element, slotID, true );
	}

	/**
	 * Finds the slot within this element that contains the given element.
	 * 
	 * @param content
	 *            handle to the content element to find
	 * @return The ID of the slot that contains the element, or
	 *         {@link DesignElement#NO_SLOT}if the element is not contained in
	 *         this element.
	 */

	public int findContentSlot( DesignElementHandle content )
	{
		if ( content.getContainer( ).getElement( ) == getElement( ) )
			return content.getElement( ).getContainerSlot( );

		return DesignElement.NO_SLOT;
	}

	/**
	 * Returns the a handle the the container's slot that holds this element.
	 * 
	 * @return the slot handle in which this element resides, <code>null</code>
	 *         if this element has no container.
	 */

	public SlotHandle getContainerSlotHandle( )
	{
		DesignElementHandle containerHandle = getContainer( );
		if ( containerHandle == null )
			return null;

		int slotID = containerHandle.findContentSlot( this );
		assert slotID != DesignElement.NO_SLOT;
		return containerHandle.getSlot( slotID );
	}

	/**
	 * Returns a handle to the given slot. If this element has no such slot,
	 * <code>null</code> will be returned.
	 * 
	 * @param slotID
	 *            the identifier of the slot for which to obtain a handle
	 * @return the handle for the requested slot
	 */

	public SlotHandle getSlot( int slotID )
	{
		int slotCount = getDefn( ).getSlotCount( );

		if ( slotCount == 0 )
		{
			return null;
		}

		assert 0 <= slotID && slotID < slotCount;

		if ( slotHandles == null )
		{
			slotHandles = new SlotHandle[slotCount];
			for ( int i = 0; i < slotCount; i++ )
			{
				slotHandles[i] = null;
			}
		}

		if ( slotHandles[slotID] == null )
		{
			slotHandles[slotID] = new SlotHandle( this, slotID );
		}

		return slotHandles[slotID];
	}

	/**
	 * Returns an iterator over the properties of this element. This handle
	 * returns all properties defined on this element, whether or not they are
	 * actually set on this element.
	 * 
	 * @return an iterator over the properties. Each call to
	 *         <code>getNext( )</code> returns an object of type
	 *         {@link PropertyHandle}.
	 * @see PropertyIterator
	 * @see PropertyHandle
	 * @see UserPropertyDefnHandle
	 */

	public Iterator getPropertyIterator( )
	{
		return new PropertyIterator( this );
	}

	/**
	 * Registers a change event listener. A listener receives notifications each
	 * time an element changes. A listener can be registered any number of
	 * times, but will receive each event only once.
	 * 
	 * @param obj
	 *            the listener to register
	 */

	public void addListener( Listener obj )
	{
		getElement( ).addListener( obj );
	}

	/**
	 * Removes a given listener. If the listener registered, then the request is
	 * silently ignored.
	 * 
	 * @param obj
	 *            the listener to de-register
	 */

	public void removeListener( Listener obj )
	{
		getElement( ).removeListener( obj );
	}

	/**
	 * Returns an iterator over the elements that derive from this one.
	 * 
	 * @return an iterator over the elements that derive from this one. Each
	 *         item returned by the iterator's <code>getNext( )</code> method
	 *         is of type {@link DesignElementHandle}.
	 */

	public Iterator derivedIterator( )
	{
		return new DerivedElementIterator( module, this );
	}

	/**
	 * Returns an iterator over the clients of this element. Useful only for
	 * styles. Returns a list of all the elements that use this style.
	 * 
	 * @return an iterator over the clients of this element. Each item returned
	 *         by the iterator's <code>getNext( )</code> method is of type
	 *         {@link DesignElementHandle}. Nothing will be iterated over an
	 *         element that is not <code>ReferenceableElement</code>.
	 */

	public Iterator clientsIterator( )
	{
		return new ClientIterator( this );
	}

	/**
	 * Returns a array of valid choices for a property.
	 * 
	 * @param propName
	 *            the property name
	 * @return a array containing choices for the given property. Return
	 *         <code>null</code>, if this property has no choice.
	 */

	public IChoice[] getChoices( String propName )
	{
		PropertyHandle propertyHandle = getPropertyHandle( propName );
		if ( propertyHandle == null )
			return null;
		return propertyHandle.getChoices( );
	}

	/**
	 * Returns the short display label for this element.
	 * 
	 * @return the display label of this element in SHORT_LABEL level.
	 * @see #getDisplayLabel( int )
	 */

	public String getDisplayLabel( )
	{
		return getDisplayLabel( DesignElement.SHORT_LABEL );
	}

	/**
	 * Returns the display label for this element. The display label is the
	 * localized display name to be shown in the UI. The display label is one of
	 * the following:
	 * <p>
	 * <ul>
	 * <li>The localized display name of this element, if the display name
	 * resource key is set and the localized string is available</li>
	 * <li>The static display name property text of this element, if set</li>
	 * <li>The name of element, if set</li>
	 * <li>The localized display name of this kind of element, which is defined
	 * in metadata, if set</li>
	 * <li>The name of this kind of element, which is also defined in metadata
	 * </li>
	 * </ul>
	 * <p>
	 * The user can also decide at which detail level the display label should
	 * be returned. The level can be one of the following options:
	 * <p>
	 * <ul>
	 * <li>USER_LABEL: Only the first 3 steps are used, if not found, return
	 * null</li>
	 * <li>SHORT_LABEL: All the above steps are used. This will ensure there
	 * will be a return value</li>
	 * <li>FULL_LABEL: Besides the return value of SHORT_LABEL, this option
	 * says we need to return additional information. This information is
	 * specific to each kind of element and my include row and column position,
	 * x and y position and so on. To get this, every child element needs to
	 * overwrite this method</li>
	 * </ul>
	 * 
	 * @param level
	 *            the display label detail level
	 * @return the display label of this element in a given level
	 */

	public String getDisplayLabel( int level )
	{
		assert level == DesignElement.USER_LABEL
				|| level == DesignElement.SHORT_LABEL
				|| level == DesignElement.FULL_LABEL;

		return getElement( ).getDisplayLabel( module, level );
	}

	/**
	 * Sorts a list of elements by localized display name.
	 * 
	 * @param list
	 *            the list to sort
	 */

	public static void doSort( List list )
	{
		Collections.sort( list, new Comparator( ) {

			public int compare( Object arg0, Object arg1 )
			{
				DesignElementHandle h1 = (DesignElementHandle) arg0;
				DesignElementHandle h2 = (DesignElementHandle) arg1;

				String s1 = h1.getDisplayLabel( );
				String s2 = h2.getDisplayLabel( );

				return s1.compareTo( s2 );
			}

		} );
	}

	/**
	 * Returns whether the element is valid or not. An element may be valid even
	 * though it has some semantic errors.
	 * 
	 * @return <code>true</code> if this element is valid, otherwise
	 *         <code>false</code>.
	 */

	public boolean isValid( )
	{
		return getElement( ).isValid( );
	}

	/**
	 * Determines whether to show an error item on the element or not. Show an
	 * error item if the element is invalid or has semantic errors, otherwise
	 * not.
	 * 
	 * @return true if the element has semantic error or the element is invalid
	 */

	public boolean showError( )
	{
		return hasSemanticError( ) || !isValid( );
	}

	/**
	 * Sets the status that identifies whether the element is valid or not.
	 * 
	 * @param isValid
	 *            the status to set
	 */

	public void setValid( boolean isValid )
	{
		getElement( ).setValid( isValid );
	}

	/**
	 * Justifies whether this element has any semantic error or not.
	 * 
	 * @return true if the element has any semantic error, otherwise false
	 */

	public boolean hasSemanticError( )
	{
		return !getSemanticErrors( ).isEmpty( );
	}

	/**
	 * Deeply clones the current design element which is wrapped by the handle.
	 * 
	 * @return the copy of the design element
	 */

	public IDesignElement copy( )
	{
		try
		{
			return (DesignElement) getElement( ).clone( );
		}
		catch ( CloneNotSupportedException e )
		{
			assert false;
		}

		return null;
	}

	/**
	 * Copies all properties to the target element. The following properties
	 * will not be copied.
	 * <ul>
	 * <li><code>DesignElement.NAME_PROP</code>
	 * <li><code>DesignElement.EXTENDS_PROP</code>
	 * </ul>
	 * 
	 * The <code>targetHandle</code> should be in the same report as this
	 * element. And this method should be called in one transaction.
	 * 
	 * @param propName
	 *            name of the property to copy
	 * @param targetHandle
	 *            the target element handle
	 * @throws SemanticException
	 *             if the target element type is not as same as this element
	 *             type, or property is not defined .
	 * @throws IllegalArgumentException
	 *             if the target element is not in the same report as this
	 *             element.
	 */

	public void copyPropertyTo( String propName,
			DesignElementHandle targetHandle ) throws SemanticException
	{
		assert ( targetHandle.getModule( ) == getModule( ) );

		if ( targetHandle.getModule( ) != getModule( ) )
			throw new IllegalArgumentException(
					"The target element should be in the same report !" ); //$NON-NLS-1$

		if ( targetHandle.getDefn( ) != getDefn( ) )
			throw new PropertyValueException(
					targetHandle.getDefn( ).getName( ),
					PropertyValueException.DESIGN_EXCEPTION_WRONG_ELEMENT_TYPE,
					PropertyType.ELEMENT_REF_TYPE );

		PropertyDefn propDefn = (ElementPropertyDefn) getDefn( ).getProperty(
				propName );
		if ( propDefn == null )
			throw new PropertyNameException( getElement( ), propName );

		propDefn = (ElementPropertyDefn) targetHandle.getDefn( ).getProperty(
				propName );
		if ( propDefn == null )
			throw new PropertyNameException( targetHandle.getElement( ),
					propName );

		Object value = getElement( ).getLocalProperty( module,
				propDefn.getName( ) );
		if ( value == null )
		{
			targetHandle.setProperty( propName, null );
			return;
		}

		if ( DesignElement.NAME_PROP.equals( propName )
				|| DesignElement.EXTENDS_PROP.equals( propName ) )
		{
			throw new SemanticError( getElement( ), new String[]{propName},
					SemanticError.DESIGN_EXCEPTION_PROPERTY_COPY_FORBIDDEN );
		}

		switch ( propDefn.getTypeCode( ) )
		{
			case PropertyType.ELEMENT_REF_TYPE :
				ElementRefValue refValue = (ElementRefValue) value;

				if ( refValue.isResolved( ) )
					targetHandle.setProperty( propDefn.getName( ), refValue
							.getElement( ) );
				else
				{
					String name = refValue.getName( );
					name = ReferenceValueUtil.needTheNamespacePrefix(
							(ReferenceValue) value, getModule( ) );
					targetHandle.setProperty( propDefn.getName( ), name );
				}
				break;

			case PropertyType.STRUCT_REF_TYPE :
				StructRefValue structRefValue = (StructRefValue) value;

				if ( structRefValue.isResolved( ) )
					targetHandle.setProperty( propDefn.getName( ),
							structRefValue.getStructure( ) );
				else
					targetHandle.setProperty( propDefn.getName( ),
							structRefValue.getName( ) );
				break;

			case PropertyType.STRUCT_TYPE :
				if ( propDefn.isList( ) )
				{
					PropertyHandle propHandle = targetHandle
							.getPropertyHandle( propName );

					Iterator strcutIter = ( (List) value ).iterator( );
					while ( strcutIter.hasNext( ) )
					{
						Structure struct = (Structure) strcutIter.next( );
						propHandle.addItem( struct.copy( ) );
					}
				}
				else
				{
					IStructure struct = (IStructure) value;
					targetHandle.setProperty( propName, struct.copy( ) );
				}
				break;

			case PropertyType.LIST_TYPE :

				assert value instanceof List;
				List valueList = (List) value;
				PropertyHandle propHandle = targetHandle
						.getPropertyHandle( propName );
				for ( int i = 0; i < valueList.size( ); i++ )
				{
					Object item = valueList.get( i );
					if ( propDefn.getSubTypeCode( ) != PropertyType.ELEMENT_REF_TYPE )
						propHandle.addItem( item );
					else
					{
						assert item instanceof ElementRefValue;
						refValue = (ElementRefValue) item;
						if ( refValue.isResolved( ) )
						{
							propHandle.addItem( refValue.getElement( ) );
						}
						else
						{
							propHandle.addItem( refValue
									.getQualifiedReference( ) );
						}
					}
				}
				break;

			default :
				targetHandle.setProperty( propName, value );
		}
	}

	/**
	 * Checks this element with semantic rules.
	 * 
	 * @return the list of errors, each of which is the <code>ErrorDetail</code>
	 *         object.
	 * @see ErrorDetail
	 */

	public List semanticCheck( )
	{
		// Validate this element.

		List exceptionList = getElement( ).validate( module );
		List errorDetailList = ErrorDetail.convertExceptionList( exceptionList );

		return errorDetailList;
	}

	/**
	 * Determines if this element can be dropped from its container.
	 * 
	 * @return <code>true</code> if it can be dropped. Returns
	 *         <code>false</code> otherwise.
	 */

	public boolean canDrop( )
	{
		boolean flag = getElement( ).canDrop( );
		if ( !flag )
			return false;

		return !getModule( ).isReadOnly( );
	}

	/**
	 * Determines if this element is editable or not. If the element comes from
	 * a library, it can not be edited in the report design.
	 * 
	 * @return true if it can be edited. false if it can't.
	 */

	public boolean canEdit( )
	{
		boolean flag = getElement( ).canEdit( );
		if ( !flag )
			return false;

		return !getModule( ).isReadOnly( );
	}

	/**
	 * Determines if the current element can be transformed to a template
	 * element. False will be returned if the element can not be dropped or the
	 * container of the current element can not contain the template element.
	 * 
	 * @return true if it can be transformed, otherwise false.
	 */

	public boolean canTransformToTemplate( )
	{
		boolean flag = getElement( ).canTransformToTemplate( getModule( ) );
		if ( !flag )
			return false;

		return !getModule( ).isReadOnly( );
	}

	/**
	 * Determines if the slot can contain an element with the type of
	 * <code>type</code>.
	 * 
	 * @param slotId
	 *            the slot id
	 * @param type
	 *            the name of the element type, like "Table", "List", etc.
	 * @return <code>true</code> if the slot can contain the an element with
	 *         <code>type</code> type, otherwise <code>false</code>.
	 * 
	 * @see #canContain(int, DesignElementHandle)
	 */

	public boolean canContain( int slotId, String type )
	{
		if ( type == null )
			return false;

		IElementDefn defn = MetaDataDictionary.getInstance( ).getElement( type );
		if ( defn == null )
			defn = MetaDataDictionary.getInstance( ).getExtension( type );

		if ( defn == null )
			return false;

		boolean flag = getElement( ).canContain( getModule( ), slotId, defn );
		if ( !flag )
			return false;

		return !getModule( ).isReadOnly( );
	}

	/**
	 * Determines if the given slot can contain the <code>content</code>.
	 * 
	 * @param slotId
	 *            the slot id
	 * @param content
	 *            the design element handle to check
	 * 
	 * @return <code>true</code> if the slot with the given
	 *         <code>slotId</code> can contain the <code>content</code>,
	 *         otherwise <code>false</code>.
	 * 
	 * @see #canContain(int, String)
	 */

	public boolean canContain( int slotId, DesignElementHandle content )
	{
		if ( content == null )
			return false;

		boolean flag = getElement( ).canContain( getModule( ), slotId,
				content.getElement( ) );
		if ( !flag )
			return false;

		return !getModule( ).isReadOnly( );
	}

	/**
	 * Returns the semantic error list, each of which is the instance of
	 * <code>ErrorDetail</code>.
	 * 
	 * @return the semantic error list.
	 */

	public List getSemanticErrors( )
	{
		List exceptionList = getElement( ).getErrors( );
		if ( exceptionList == null )
			return Collections.EMPTY_LIST;

		List errorDetailList = ErrorDetail.convertExceptionList( exceptionList );

		return ErrorDetail.getSemanticErrors( errorDetailList,
				DesignFileException.DESIGN_EXCEPTION_SEMANTIC_ERROR );
	}

	/**
	 * Returns the root container of this element. It must be Library or Report
	 * Design.
	 * 
	 * @return the handle of the root container.
	 */

	public ModuleHandle getRoot( )
	{
		Module module = getElement( ).getRoot( );
		if ( module != null )
			return (ModuleHandle) module.getHandle( module );

		return null;
	}

	/**
	 * Returns return the path corresponding to the current position of the
	 * element in the tree.
	 * 
	 * This path string helps user locate this element in user interface. It
	 * follows XPath syntax. Each node name indicates the name of the element
	 * definition and the 1-based element position in the slot. The position
	 * information is only available when the element is in the multicardinality
	 * slot.
	 * 
	 * <p>
	 * For example,
	 * <ul>
	 * <li>/report/Body[1]/Label[3] - The third label element in body slot
	 * <li>/report/Styles[1]/Style[1] - The first style in the styles slot
	 * <li>/report/page-setup[1]/Graphic Master Page - The master page in the
	 * page setup slot.
	 * </ul>
	 * <p>
	 * Note: the localized name is used for element type and slot name.
	 * 
	 * @return the path of this element
	 */

	public String getXPath( )
	{

		DesignElementHandle element = this;
		StringBuffer sb = new StringBuffer( );

		do
		{
			ElementDefn elementDefn = (ElementDefn) element.getDefn( );

			DesignElementHandle container = element.getContainer( );

			String slotInfo = null;
			String posnInfo = null;

			if ( container != null )
			{
				SlotHandle slot = element.getContainerSlotHandle( );
				ISlotDefn slotDefn = container.getElement( ).getDefn( )
						.getSlot( slot.getSlotID( ) );
				String slotDefnName = slotDefn.getXmlName( );

				int slotIndex = 1;

				slotInfo = "/" + slotDefnName + "[" + slotIndex //$NON-NLS-1$ //$NON-NLS-2$
						+ "]"; //$NON-NLS-1$

				int tagIndex = 1;

				for ( int i = 0; i < slot.findPosn( element ); i++ )
				{
					DesignElementHandle tmpElement = slot.get( i );
					if ( tmpElement.getDefn( ) == elementDefn )
						tagIndex++;
				}

				posnInfo = "[" + tagIndex + "]";//$NON-NLS-1$ //$NON-NLS-2$
			}

			String elementPath = ""; //$NON-NLS-1$

			if ( !StringUtil.isBlank( slotInfo ) )
				elementPath = slotInfo;

			elementPath = elementPath + "/" + elementDefn.getXmlElement( ); //$NON-NLS-1$

			if ( !StringUtil.isBlank( posnInfo ) )
				elementPath = elementPath + posnInfo;

			sb.insert( 0, elementPath );
			element = container;

		} while ( element != null );

		return sb.toString( );
	}

	/**
	 * Gets a string that defines the event handle class.
	 * 
	 * @return the expression as a string
	 * 
	 * @see #setEventHandlerClass(String)
	 */
	public String getEventHandlerClass( )
	{
		return getStringProperty( GroupElement.EVENT_HANDLER_CLASS_PROP );
	}

	/**
	 * Sets the group expression.
	 * 
	 * @param expr
	 *            the expression to set
	 * @throws SemanticException
	 *             If the expression is invalid.
	 * 
	 * @see #getEventHandlerClass()
	 */
	public void setEventHandlerClass( String expr ) throws SemanticException
	{
		setProperty( GroupElement.EVENT_HANDLER_CLASS_PROP, expr );
	}

	/**
	 * Creates a template element handle and transforms the current element
	 * handle to the created template element.
	 * 
	 * @param name
	 *            the name of created template element handle
	 * @return the template element handle
	 * @throws SemanticException
	 *             if the current element can not be transformed to a template
	 *             element, current module is not a report design or some
	 *             containing contexts don't match
	 */

	public TemplateElementHandle createTemplateElement( String name )
			throws SemanticException
	{
		if ( getRoot( ) == null )
			throw new TemplateException(
					getElement( ),
					TemplateException.DESIGN_EXCEPTION_CREATE_TEMPLATE_ELEMENT_FORBIDDEN );
		TemplateCommand cmd = new TemplateCommand( getModule( ), getContainer( )
				.getElement( ) );
		TemplateElement template = cmd.createTemplateElement( getElement( ),
				getElement( ).getContainerSlot( ), name );
		if ( template == null )
			return null;
		return (TemplateElementHandle) template.getHandle( module );
	}

	/**
	 * Creates a template element handle and transforms the current element
	 * handle to the created template element if the current element is based on
	 * a template parameter definition.
	 * 
	 * @param name
	 *            the name of created template element handle
	 * @return the template element handle
	 * @throws SemanticException
	 *             if the current element can not be transformed to a template
	 *             element, current element has no template parameter
	 *             definition, current module is not a report design or some
	 *             containing contexts don't match
	 */

	public TemplateElementHandle revertToTemplate( String name )
			throws SemanticException
	{
		if ( getRoot( ) == null )
			throw new TemplateException(
					getElement( ),
					TemplateException.DESIGN_EXCEPTION_CREATE_TEMPLATE_ELEMENT_FORBIDDEN );
		TemplateCommand cmd = new TemplateCommand( getModule( ), getContainer( )
				.getElement( ) );
		TemplateElement template = cmd.revertToTemplate( getElement( ),
				getElement( ).getContainerSlot( ), name );
		if ( template == null )
			return null;
		return (TemplateElementHandle) template.getHandle( module );
	}

	/**
	 * if this design element is based on a template definition
	 * {@link #isTemplateParameterValue( )},get rid of the template definition )
	 * 
	 * @throws SemanticException
	 */
	public void revertToReportItem( ) throws SemanticException
	{
		clearProperty( REF_TEMPLATE_PARAMETER_PROP );
	}

	/**
	 * Checks whether this element is based on a template parameter definition
	 * or not. Call this method before calling method
	 * {@link #revertToTemplate(String) }to assure that this element can be
	 * reverted to a template element. If this method returns false, method
	 * <code>revertToTemplate(String)</code> must fail too.
	 * 
	 * @return true if this element is based on a template parameter definition,
	 *         otherwise false
	 */

	public boolean isTemplateParameterValue( )
	{
		return getElement( ).isTemplateParameterValue( getModule( ) );
	}

	/**
	 * Gets the property data for either a system-defined or user-defined
	 * property.
	 * 
	 * @param propName
	 *            The name of the property to lookup.
	 * @return The property definition, or null, if the property is undefined.
	 */

	public IElementPropertyDefn getPropertyDefn( String propName )
	{
		// Look for the property defined on this element.

		return getElement( ).getPropertyDefn( propName );
	}

	/**
	 * Returns the effective module of the element. If the element is attached
	 * to the design/library, the design/library is returned. Otherwise, the
	 * module cached in the <code>DesignElementHandle</code> is returned.
	 * 
	 * @return the effective module of the element. Can be null.
	 */

	protected Module getEffectiveModule( )
	{
		return module;
	}

	/**
	 * Returns the overridden value of the specified property given its internal
	 * name.
	 * 
	 * @param propName
	 *            the name of the property to get. Can be a system-defined or
	 *            user-defined property name.
	 * 
	 * @return the property binding, or null if the overridden value is not set
	 */

	public String getPropertyBinding( String propName )
	{
		PropertyBinding propBinding = module.findPropertyBinding(
				getElement( ), propName );
		if ( propBinding == null )
			return null;

		return propBinding.getValue( );
	}

	/**
	 * Sets the mask of the specified property.
	 * 
	 * @param propName
	 *            the property name to get. Can be a system-defined or
	 *            user-defined property name.
	 * 
	 * @param value
	 *            the overridden value
	 * 
	 * @throws SemanticException
	 *             if the maskValue is not one of the above.
	 */

	public void setPropertyBinding( String propName, String value )
			throws SemanticException
	{
		// check whether the property is defined on this element

		ElementPropertyDefn defn = (ElementPropertyDefn) getPropertyDefn( propName );
		if ( defn == null )
			throw new SemanticError( getElement( ), new String[]{propName},
					SemanticError.DESIGN_EXCEPTION_INVALID_PROPERTY_NAME );
		if ( Module.PROPERTY_BINDINGS_PROP.equals( defn.getName( ) ) )
			return;

		// check the element is in the id-map of the root module

		Module root = getElement( ).getRoot( );
		if ( root == null )
			throw new SemanticError( getElement( ),
					SemanticError.DESIGN_EXCEPTION_PROPERTY_BINDING_FORBIDDEN );
		assert root.getElementByID( getID( ) ) == getElement( );

		ArrayList bindingList = (ArrayList) root.getLocalProperty( root,
				Module.PROPERTY_BINDINGS_PROP );

		PropertyBinding binding = root.findPropertyBinding( getElement( ),
				propName );

		// if the binding is not set, and the new value is null, returns

		if ( binding == null && value == null )
			return;

		if ( bindingList == null )
		{
			assert value != null;

			bindingList = new ArrayList( );
			root.setProperty( Module.PROPERTY_BINDINGS_PROP, bindingList );
		}

		defn = root.getPropertyDefn( Module.PROPERTY_BINDINGS_PROP );
		assert defn != null;

		PropertyCommand cmd = new PropertyCommand( module, root );

		if ( value == null && binding != null )
		{
			// maskValue is null, remove the item from the structure list.

			cmd.removeItem( new CachedMemberRef( defn ), bindingList
					.indexOf( binding ) );
		}
		else
		{
			/*
			 * If the property has no binding related to, adds a new binding
			 * item into the structure list.
			 */

			if ( binding == null )
			{
				binding = new PropertyBinding( );
				binding.setName( propName );
				binding.setID( getID( ) );
				binding.setValue( value );
				cmd.addItem( new CachedMemberRef( defn ), binding );
			}
			else
			{
				// changes the binding value.

				MemberRef memberRef = new CachedMemberRef( defn, bindingList
						.indexOf( binding ), PropertyBinding.VALUE_MEMBER );
				cmd.setMember( memberRef, value );
			}
		}
	}

	/**
	 * Returns externalized message.
	 * 
	 * @param textIDProp
	 *            TEXT_ID_PROP
	 * @param textProp
	 *            TEXT_PROP
	 * @return externalized message.
	 */

	public String getExternalizedValue( String textIDProp, String textProp )
	{
		return ModelUtil.getExternalizedValue( getElement( ), textIDProp,
				textProp );
	}

}