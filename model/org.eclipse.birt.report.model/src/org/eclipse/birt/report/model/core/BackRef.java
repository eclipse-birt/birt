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


/**
 * Represents the back reference for the referencable element. The back
 * reference provides the capability that the referencable element knows
 * what element is referring it. The element referring it is called "client
 * element". It contains the client element and element reference property
 * name.
 */

public final class BackRef
{

	/**
	 * The client element that refers to one referencable element.
	 */

	public final DesignElement element;

	/**
	 * The name of the property that refers to one referencable element.
	 */

	public final String propName;
	
	
	private final CachedMemberRef cachedMemberRef;

	/**
	 * Constructs the back reference with the client element and the element
	 * reference property name.
	 * 
	 * @param obj
	 *            client element
	 * @param prop
	 *            name of the property which refers to another element
	 */

	public BackRef( DesignElement obj, String prop )
	{
		element = obj;
		propName = prop;
		cachedMemberRef = null;
	}
	
	/**
	 * Constructs the back reference with the client element and the element
	 * reference property name.
	 * 
	 * @param obj
	 *            client element
	 * @param memberRef
	 *            member reference
	 */

	public BackRef( DesignElement obj, MemberRef memberRef )
	{
		element = obj;
		cachedMemberRef = new CachedMemberRef( memberRef );
		propName = null;
	}
	
	/**
	 * Gets the client element of the back reference.
	 * @return the client element
	 */
	
	public DesignElement getElement( )
	{
		return this.element;
	}
	
	/**
	 * Gets the property name that refers to one referencable element.
	 * @return the property name of the back reference
	 */
	
	public String getPropertyName( )
	{
		return this.propName;
	}
	
	/**
	 * Gets the member reference.
	 * @return member reference
	 */
	
	public CachedMemberRef getCachedMemberRef()
	{
		return this.cachedMemberRef;
	}
}
