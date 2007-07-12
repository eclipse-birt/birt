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
import java.util.List;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * The structure context. It is used when establishes back reference.
 * 
 */

public class StructureContext
{

	private Object dataContainer;

	private IPropertyDefn propDefn;

	/**
	 * Constructs the structure context.
	 * 
	 * @param element
	 *            the design element
	 * @param elementPropName
	 *            the element property name
	 */

	public StructureContext( DesignElement element, String elementPropName )
	{
		this.dataContainer = element;
		propDefn = element.getPropertyDefn( elementPropName );
		if ( propDefn == null )
			throw new IllegalArgumentException( );
	}

	/**
	 * Constructs the structure context.
	 * 
	 * @param struct
	 *            the structure
	 * @param memberName
	 *            the member name
	 */

	public StructureContext( IStructure struct, String memberName )
	{
		this.dataContainer = struct;
		propDefn = struct.getDefn( ).getMember( memberName );
		if ( propDefn == null )
			throw new IllegalArgumentException( );
	}

	/**
	 * Adds the structure to the context.
	 * 
	 * @param struct
	 *            the structure
	 */

	public void add( Structure struct )
	{
		add( -1, struct );
	}

	/**
	 * Adds the structure to the context with the given position.
	 * 
	 * @param index
	 *            the position
	 * @param struct
	 *            the structure
	 */

	public void add( int index, Structure struct )
	{
		Object values = getLocalValue( );

		if ( propDefn.isList( ) )
		{
			if ( values == null )
			{
				values = new ArrayList( );
				setValue( values );
			}

			if ( index == -1 )
				index = ( (List) values ).size( );

			( (List) values ).add( index, struct );
		}
		else
		{
			// the property value is the structure, not the structure list.

			assert values == null;

			setValue( struct );
		}
		
		struct.setContext( this );
	}

	/**
	 * Removes the structure from the context.
	 * 
	 * @param struct
	 *            the structure
	 */

	public void remove( Structure struct )
	{
		Object values = getLocalValue( );

		assert values != null;

		if ( propDefn.isList( ) )
		{
			List list = (List) values;
			int index = list.indexOf( struct );
			assert index != -1;

			list.remove( index );
		}
		else
		{
			// the property value is the structure, not the structure list.

			assert values == struct;

			setValue( null );
		}

		struct.setContext( null );
	}

	/**
	 * Removes the structure from the context.
	 * 
	 * @param index
	 *            the position
	 */
	
	public void remove( int index )
	{
		Object values = getLocalValue( );

		assert values != null;

		Structure struct = null;

		if ( propDefn.isList( ) )
		{
			List list = (List) values;

			struct = (Structure) list.get( index );
			list.remove( index );
		}
		else
		{
			assert false;
		}

		assert struct != null;
		struct.setContext( null );
	}

	private void setValue( Object values )
	{
		if ( dataContainer instanceof DesignElement )
		{
			DesignElement tmpElement = (DesignElement) dataContainer;
			tmpElement.setProperty( propDefn.getName( ), values );
		}
		else if ( dataContainer instanceof Structure )
		{
			Structure tmpStruct = (Structure) dataContainer;
			tmpStruct.setProperty( propDefn.getName( ), values );
		}
	}

	/**
	 * @return the dataContainer
	 */

	public Object getValueContainer( )
	{
		return dataContainer;
	}

	/**
	 * @return the elementPropName
	 */

	public IPropertyDefn getPropDefn( )
	{
		return propDefn;
	}

	/**
	 * @return the elementPropName
	 */

	public IPropertyDefn getElementProp( )
	{
		StructureContext tmpContext = this;

		Object tmpValueContainer = tmpContext.getValueContainer( );

		while ( tmpValueContainer != null
				&& !( tmpValueContainer instanceof DesignElement ) )
		{
			tmpContext = ( (Structure) tmpValueContainer ).getContext( );
			if ( tmpContext == null )
				break;

			tmpValueContainer = tmpContext.getValueContainer( );
		}

		if ( tmpContext != null )
			return tmpContext.getPropDefn( );

		return null;
	}

	/**
	 * Returns the element that contains this context.
	 * 
	 * @return
	 */

	public DesignElement getElement( )
	{
		Object tmpValueContainer = getValueContainer( );

		while ( tmpValueContainer != null
				&& !( tmpValueContainer instanceof DesignElement ) )
		{
			tmpValueContainer = ( (Structure) tmpValueContainer ).getContext( )
					.getValueContainer( );
		}

		if ( tmpValueContainer instanceof DesignElement )
			return (DesignElement) tmpValueContainer;

		return null;
	}

	/**
	 * Returns the local value of the context.
	 * 
	 * @param root
	 *            the module
	 * @return the value
	 */

	public Object getLocalValue( Module root )
	{
		if ( dataContainer instanceof DesignElement )
		{
			DesignElement tmpElement = (DesignElement) dataContainer;
			return tmpElement.getLocalProperty( root, propDefn.getName( ) );
		}

		return ( (Structure) dataContainer ).getLocalProperty( root,
				(PropertyDefn) propDefn );
	}

	/**
	 * Returns the local value of the context.
	 * 
	 * @param root
	 *            the module
	 * @return the value
	 */

	private Object getLocalValue( )
	{
		if ( dataContainer instanceof DesignElement )
		{
			return getLocalValue( ( (DesignElement) dataContainer ).getRoot( ) );
		}

		return getLocalValue( null );
	}
}