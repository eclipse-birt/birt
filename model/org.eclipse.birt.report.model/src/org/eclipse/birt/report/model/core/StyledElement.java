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

package org.eclipse.birt.report.model.core;

import java.util.List;

import org.eclipse.birt.report.model.command.StyleException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Base class for all report elements with a style. Implements operations that
 * are specific to styled elements.
 *  
 */

public abstract class StyledElement extends DesignElement
{

	/**
	 * The shared style which this element references, if any.
	 */

	protected ElementRefValue style = null;

	/**
	 * Property name for the reference to the shared style.
	 */

	public static final String STYLE_PROP = "style"; //$NON-NLS-1$

	/**
	 * Default constructor.
	 */

	public StyledElement( )
	{
	}

	/**
	 * Constructs the styled element with an optional name.
	 * 
	 * @param theName
	 *            the element name
	 */

	public StyledElement( String theName )
	{
		super( theName );
	}

	/**
	 * Makes a clone of this styled element. The style that was referenced by
	 * this element if any, will be set to a unresolved element reference for
	 * the cloned one.
	 * 
	 * @return Object the cloned styled element.
	 * 
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object clone( ) throws CloneNotSupportedException
	{
		StyledElement element = (StyledElement) super.clone( );
		if ( style != null )
		{
			ElementRefValue newRefValue = new ElementRefValue( );
			newRefValue.unresolved( style.getName( ) );
			element.style = newRefValue;
		}
		else
			element.style = null;
		return element;
	}

	/**
	 * Gets the style which defined on this element itself.
	 * 
	 * @return style element. Null if the style is not defined on this element
	 *         itself.
	 *  
	 */
	public StyleElement getLocalStyle( )
	{
		if ( style == null )
			return null;
		return (StyleElement) style.getElement( );
	}

	/**
	 * Gets the name of the referenced style on this element.
	 * 
	 * @return style name. null if the style is not defined on the element.
	 */

	public String getStyleName( )
	{
		if ( style == null )
			return null;
		return style.getName( );
	}

	/**
	 * Gets the style which defined on this element. The style element can be
	 * retrieved from this element extends hierarchy.
	 * 
	 * @return style element. null if this element didn't define a style on it.
	 * 
	 *  
	 */
	public StyleElement getStyle( )
	{
		StyledElement e = this;
		while ( e != null )
		{
			if ( e.style != null && e.style.isResolved( ) )
				return (StyleElement) e.style.getElement( );
			e = (StyledElement) e.getExtendsElement( );
		}
		return null;
	}

	/**
	 * Sets the style. If null, the style is cleared.
	 * 
	 * @param newStyle
	 *            the style to set
	 */

	public void setStyle( StyleElement newStyle )
	{
		StyleElement oldStyle = null;
		if ( style != null )
			oldStyle = (StyleElement) style.getElement( );
		if ( oldStyle == newStyle )
			return;
		if ( oldStyle != null )
			oldStyle.dropClient( this );
		if ( newStyle != null )
		{
			if ( style == null )
				style = new ElementRefValue( );
			style.resolve( newStyle );
			newStyle.addClient( this, null );
		}
		else
			style = null;
	}

	/**
	 * Sets the shared style by name. If null, the style is cleared. Use this
	 * form to represent an "unresolved" style: a reference to an undefined
	 * style, or a forward reference while parsing a design file.
	 * 
	 * @param theName
	 *            the style name
	 */

	public void setStyleName( String theName )
	{
		if ( style == null && theName == null )
			return;
		setStyle( null );
		if ( style == null )
			style = new ElementRefValue( );
		style.unresolved( theName );
	}

	/**
	 * Returns the value of an intrinsic property.If the property name is
	 * <code>style</code> then return the style element.
	 * 
	 * @param propName
	 *            name of the intrinsic property
	 * @return intrinsic property
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( propName.equals( STYLE_PROP ) )
			return style;
		return super.getIntrinsicProperty( propName );
	}

	/**
	 * If the style name is represented as a name, then attempts to resolve the
	 * style name to obtain the referenced style.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		// Resolve style

		if ( style != null && !style.isResolved( ) )
		{
			NameSpace ns = design.getNameSpace( RootElement.STYLE_NAME_SPACE );
			StyleElement theStyle = (StyleElement) ns.getElement( style
					.getName( ) );
			if ( theStyle == null )
			{
				list.add( new StyleException( this, style.getName( ),
						StyleException.NOT_FOUND ) );
			}
			else
			{
				setStyle( theStyle );
			}
		}

		list.addAll( Style.validateStyleProperties( design, this ) );

		return list;
	}

	/**
	 * Gets a property value by its definition. The search will not search the
	 * container element extends hierarchy.
	 * 
	 * @param design
	 *            the report design
	 * @param prop
	 *            definition of the property to get
	 * 
	 * @return The property value, or null if no value is set.
	 */

	public Object getFactoryProperty( ReportDesign design,
			ElementPropertyDefn prop )
	{
		if ( !prop.isStyleProperty( ) )
			return getProperty( design, prop );

		StyledElement e = this;
		Object value;
		// 1). If we can find the value here, return it.

		value = e.getLocalProperty( design, prop );
		if ( value != null )
			return value;

		// 2). Does the style provide the value of this property ?

		StyleElement style = e.getLocalStyle( );
		if ( style != null )
		{
			value = style.getLocalProperty( design, prop );
			if ( value != null )
				return value;
		}

		// 3). All the style properties can inherit the value from the
		// ancestors.
		// Does the parent provide the value of this property?

		value = e.getPropertyFromParent( design, prop );
		if ( value != null )
			return value;

		// If the style property can not cascade, then we
		// need not the context search, and returns null

		if ( !prop.canInherit( ) )
			return null;

		// 4). Check if this element predefined style provides
		// the property value

		String selector = e.getDefn( ).getSelector( );
		value = e.getPropertyFromSelector( design, prop, selector );
		if ( value != null )
			return value;

		// Check if the container/slot predefined style provides
		// the property value

		if ( e.getContainer( ) != null )
		{
			// The predefined style of container/slot combination or slot
			// provides the property value.

			String[] selectors = e.getContainer( ).getSelectors(
					e.getContainerSlot( ) );
			for ( int i = 0; i < selectors.length; i++ )
			{
				value = e.getPropertyFromSelector( design, prop, selectors[i] );
				if ( value != null )
					return value;
			}
		}

		return null;
	}
}