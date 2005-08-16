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

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.ReferenceableElement;

/**
 * Represents a representation to an element. This class is the reference
 * property value. It can represent either a <em>resolved</em> or
 * <em>unresolved</em> value. A resolved value is one in which we've used a
 * name to look up the actual element. An unresolved reference is one that has a
 * name, but has not been resolved to an element.
 * <p>
 * The class holds either a name or a pointer to the target element, never both.
 * By dropping the name for resolved elements, we avoid the need to fix up
 * references when the name of target element changes.
 * <p>
 * If an element can be the target of a reference, then that element contains a
 * "back pointer" list of the references. This allows the system to perform
 * semantic checks, to clean up references to deleted elements, etc.
 * <p>
 * The element reference is used in two key ways. First, it is used to record an
 * "extends" reference of a derived element to its parent element in the
 * inheritance hierarchy. In this case, the reference is to a generic
 * <code>DesignElement</code>. The second use is to record the value of an
 * element reference property (<code>ElementRefPropertyType</code>). In this
 * case, the target must be derived from <code>ReferenceableElement</code> so
 * that the referenced class can cache a back-pointer to the referencing
 * element.
 * 
 */

public class ElementRefValue
{

	/**
	 * Library namespace that indicats which library this reference is using
	 */

	String libraryNamespace;

	/**
	 * Unresolved name of the target element.
	 */

	String name;

	/**
	 * Resolved pointer to the target element.
	 */

	DesignElement resolved;

	/**
	 * Constructor of an unresolved reference.
	 * 
	 * @param namespace
	 *            the namespace to indicate which included library this value
	 *            refers to
	 * @param theName
	 *            the unresolved name
	 */

	public ElementRefValue( String namespace, String theName )
	{
		assert theName != null;
		name = theName;
		libraryNamespace = namespace;
	}

	/**
	 * Constructor of a resolved reference.
	 * 
	 * @param namespace
	 *            the namespace to indicate which included library this value
	 *            refers to
	 * @param element
	 *            the resolved element
	 */

	public ElementRefValue( String namespace, DesignElement element )
	{
		assert element != null;
		resolved = element;
		libraryNamespace = namespace;
	}

	/**
	 * Gets the reference name. The name is either the unresolved name, or the
	 * name of the resolved element.
	 * 
	 * @return the name of the referenced element, or null if this reference is
	 *         not set
	 */

	public String getName( )
	{
		if ( name != null )
			return name;
		if ( resolved != null )
			return resolved.getName( );
		assert false;
		return null;
	}

	/**
	 * Returns the referenced element, if the element is resolved.
	 * 
	 * @return the referenced element, or null if this reference is not set, or
	 *         is unresolved
	 */

	public DesignElement getElement( )
	{
		return resolved;
	}

	/**
	 * Returns the target element as a referenceable element. This form is used
	 * when caching references.
	 * 
	 * @return the target element as a referencable element
	 */

	public ReferenceableElement getTargetElement( )
	{
		return (ReferenceableElement) resolved;
	}

	/**
	 * Sets the resolved element.
	 * 
	 * @param element
	 *            the resolved element
	 */

	public void resolve( DesignElement element )
	{
		name = null;
		resolved = element;
	}

	/**
	 * Sets the unresolved element name.
	 * 
	 * @param theName
	 *            the unresolved element name
	 */

	public void unresolved( String theName )
	{
		resolved = null;
		name = theName;
	}

	/**
	 * Determines if this reference is resolved.
	 * 
	 * @return true if this element is resolved, false if it is unset, or set to
	 *         an unresolved name
	 */

	public boolean isResolved( )
	{
		assert !( name != null && resolved != null );
		return resolved != null;
	}

	/**
	 * Determines if this reference is set.
	 * 
	 * @return true if the reference is set, false if not
	 */

	public boolean isSet( )
	{
		return name != null || resolved != null;
	}

	/**
	 * Returns the library namespace.
	 * 
	 * @return the library namespace
	 */

	public String getLibraryNamespace( )
	{
		return libraryNamespace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */

	public boolean equals( Object obj )
	{
		if ( obj instanceof ElementRefValue )
		{
			ElementRefValue value = (ElementRefValue) obj;
			if ( this.isResolved( ) == value.isResolved( ) )
			{
				if ( value.isResolved( ) )
				{
					return getElement( ).equals( value.getElement( ) );
				}
				return getLibraryNamespace().equals( value.getLibraryNamespace()) && getName( ).equals( value.getName( ) );
			}
			return false;

		}
		return false;
	}

	/**
	 * Returns the qualified reference of this reference value, which is made up
	 * with library namespace and element name. If no library namespace is available,
	 * only element name is returned.
	 * <p>
	 * For example,
	 * <ul>
	 * <li>The library namespace is "LibA", and element name is "style1".
	 * "LibA:style1" is retured.
	 * <li>If it has no library namespace, 
	 * </ul>
	 * 
	 * @return the qualified reference
	 */

	public String getQualifiedReference( )
	{
		return StringUtil.buildQualifiedReference( getLibraryNamespace( ),
				getName( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString( )
	{
		if ( !StringUtil.isBlank( getName( ) ) )
			return getQualifiedReference( );
			
		return super.toString( );
	}
}
