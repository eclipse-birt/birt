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
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;

/**
 * Represents a representation to a structure. This class is the reference
 * property value. It can represent either a <em>resolved</em> or
 * <em>unresolved</em> value. A resolved value is one in which we've used a
 * name to look up the actual structure. An unresolved reference is one that has
 * a name, but has not been resolved to a structure.
 * <p>
 * The class holds either a name or a pointer to the target structure, never
 * both. By dropping the name for resolved elements, we avoid the need to fix up
 * references when the name of target structure changes.
 * <p>
 * If a structure can be the target of a reference, then that structure contains
 * a "back pointer" list of the references. This allows the system to perform
 * semantic checks, to clean up references to deleted structures, etc.
 * <p>
 * The structure reference is used in only one way. The use is to record the
 * value of a structure reference property (<code>StructRefPropertyType</code>).
 * In this case, the target must be derived from
 * <code>ReferencableStructure</code> so that the referenced class can cache a
 * back-pointer to the referencing element.
 * 
 */

public class StructRefValue
{

	/**
	 * Library namespace that indicats which library this reference is using
	 */

	String libraryNamespace;

	/**
	 * Unresolved name of the target element.
	 */

	public String name;

	/**
	 * Resolved pointer to the target element.
	 */

	public Structure resolved;

	/**
	 * Constructor of an unresolved reference.
	 * 
	 * @param namespace
	 *            the library name space	 
	 * @param theName
	 *            the unresolved name
	 */

	public StructRefValue( String namespace, String theName )
	{
		assert theName != null;
		name = theName;
		libraryNamespace = namespace;
	}

	/**
	 * Constructor of a resolved reference.
	 * 
	 * @param namespace
	 *            the library name space	
	 * @param structure
	 *            the resolved structure
	 */

	public StructRefValue( String namespace, Structure structure )
	{
		assert structure != null;
		resolved = structure;
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
			return resolved.getReferencableProperty( );
		assert false;
		return null;
	}

	/**
	 * Returns the referenced structure, if the structure is resolved.
	 * 
	 * @return the referenced structure, or null if this reference is not set,
	 *         or is unresolved
	 */

	public Structure getStructure( )
	{
		return resolved;
	}

	/**
	 * Returns the target structure as a referenceable structure. This form is
	 * used when caching references.
	 * 
	 * @return the target structure as a referencable structure
	 */

	public ReferencableStructure getTargetStructure( )
	{
		return (ReferencableStructure) resolved;
	}

	/**
	 * Sets the resolved structure.
	 * 
	 * @param structure
	 *            the resolved structure
	 */

	public void resolve( Structure structure )
	{
		name = null;
		resolved = structure;
	}

	/**
	 * Sets the unresolved structure name.
	 * 
	 * @param theName
	 *            the unresolved structure name
	 */

	public void unresolved( String theName )
	{
		resolved = null;
		name = theName;
	}

	/**
	 * Determines if this reference is resolved.
	 * 
	 * @return true if this structure is resolved, false if it is unset, or set
	 *         to an unresolved name
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString( )
	{
		if ( !StringUtil.isBlank( getName( ) ) )
			return getName( );
		return super.toString( );
	}
}
