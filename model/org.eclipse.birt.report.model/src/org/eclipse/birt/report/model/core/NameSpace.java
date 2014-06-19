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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * A name space organizes a set of named elements. The name space allows quick
 * lookup of elements by name, and ensures that each element has a unique name.
 * Operations include insert, delete and lookup. The clients of this class
 * provide the context in which the name space is used, and enforce uniqueness
 * rules.
 * <p>
 * Names are case sensitive.
 * <p>
 * A name space is meant to be used in conjunction with a command. Most of the
 * operations here represent inconsistent states in the model that can obtain
 * only in while a command is executing. For example, to insert an element in
 * the name space, the element's name must have already been set. This
 * precondition represents an inconsistent model state: an element has a name
 * but does not appear in the proper name space. Similarly, all error checking
 * must be done before calling these methods as part of checking the
 * preconditions for a command. Once a command starts, it must not fail because
 * the model does not provide means to roll back a partially complete command.
 */

public class NameSpace implements Cloneable
{

	/**
	 * The actual name space.
	 */

	protected HashMap<String, DesignElement> names = new LinkedHashMap<String, DesignElement>(
			ModelUtil.MAP_CAPACITY_MEDIUM );

	/**
	 * Constructor.
	 */

	public NameSpace( )
	{
	}

	/**
	 * Inserts an element into the name space. The caller must have validated
	 * that the name is unique.
	 * 
	 * @param element
	 *            The element to insert.
	 */

	public void insert( DesignElement element )
	{
		assert element.getName( ) != null;
		assert names.get( element.getName( ) ) == null;
		names.put( element.getName( ), element );
	}

	public void insert( String name, DesignElement element )
	{
		names.put( name, element );
	}

	/**
	 * Removes an element from the name space. The caller must have validated
	 * that the element is in the name space.
	 * 
	 * @param element
	 *            The element to be removed from the namespace
	 */

	public void remove( DesignElement element )
	{
		assert element.getName( ) != null;
		assert names.get( element.getName( ) ) == element;
		names.remove( element.getName( ) );
	}

	public void remove( String name )
	{
		names.remove( name );
	}

	/**
	 * Renames an element in the name space. Rename is done in the context of a
	 * command: it represents a temporary period in which the model is
	 * inconsistent. The element name has already changed, we are now updating
	 * the name space. The caller must have validated that the rename is valid,
	 * and that the new name is unique. This form also handles the case in which
	 * an element with an optional name either obtains or drops its name.
	 * 
	 * @param element
	 *            The element which is renamed or will be renamed.
	 * @param oldName
	 *            The previous name in the name space.
	 * @param newName
	 *            The new name in the name space.
	 */

	public void rename( DesignElement element, String oldName, String newName )
	{
		if ( oldName != null )
		{
			assert names.get( oldName ) == element;
			names.remove( oldName );
		}
		if ( newName != null )
		{
			assert names.get( newName ) == null;
			names.put( newName, element );
		}
	}

	/**
	 * Checks if the name appears within the name space.
	 * 
	 * @param name
	 *            The name of the searched
	 * @return Returns true if the name is in the namespace, else returns false
	 */

	public boolean contains( String name )
	{
		return names.containsKey( name );
	}

	/**
	 * Gets the named element from name space.
	 * 
	 * @param name
	 *            The name of the report element
	 * @return Returns the report element.
	 */

	public DesignElement getElement( String name )
	{
		return names.get( name );
	}

	/**
	 * Returns the number of items in the name space.
	 * 
	 * @return The element count.
	 */

	public final int getCount( )
	{
		return names.size( );
	}

	/**
	 * Returns the element list in this name space. The elements are unsorted.
	 * 
	 * @return the element list
	 */

	public final List<DesignElement> getElements( )
	{
		return new ArrayList<DesignElement>( names.values( ) );
	}

	public final Object clone( ) throws CloneNotSupportedException
	{

		NameSpace ns = (NameSpace) super.clone( );
		ns.names = new LinkedHashMap<String, DesignElement>(
				ModelUtil.MAP_CAPACITY_MEDIUM );

		return ns;
	}
}