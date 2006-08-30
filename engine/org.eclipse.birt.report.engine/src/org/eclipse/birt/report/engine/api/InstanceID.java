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

package org.eclipse.birt.report.engine.api;


/**
 * a class that wraps around an identifier for a report element instance
 */
public class InstanceID
{

	protected InstanceID parentId;
	protected long designId;
	protected DataID dataId;

	public InstanceID( InstanceID parent, long designId, DataID dataId )
	{
		this.parentId = parent;
		this.designId = designId;
		this.dataId = dataId;
	}

	public InstanceID getParentID( )
	{
		return parentId;
	}

	/**
	 * returns the component id for the element
	 */
	public long getComponentID( )
	{
		return designId;
	}

	public DataID getDataID( )
	{
		return dataId;
	}

	protected void append( StringBuffer buffer )
	{
		buffer.append( "/" );
		buffer.append( designId );
		if ( dataId != null )
		{
			buffer.append( "(" );
			dataId.append( buffer );
			buffer.append( ")" );
		}
	}

	public String toString( )
	{
		StringBuffer buffer = new StringBuffer( );
		append( buffer );
		return buffer.toString( );
	}

	public static InstanceID parse( String instanceId )
	{
		if ( instanceId == null )
		{
			return null;
		}
		return parse( instanceId.toCharArray( ), 0, instanceId.length( ) );
	}

	public static InstanceID parse( char[] buffer, int offset, int length )
	{
		DataID dataId = null;
		// search the last '(' to see if we have data id
		int ptr = offset + length - 1;
		if ( ptr >= offset && buffer[ptr] == ')' )
		{
			ptr--; // skip the first ')'
			while ( ptr >= offset && buffer[ptr] != '(' )
			{
				ptr--;
			}
			if ( ptr < offset || buffer[ptr] != '(' )
			{
				// it should be a data Id but it isn't return null
				return null;
			}
			// we found the data Id
			dataId = DataID.parse( buffer, ptr + 1, offset + length - ptr - 2 );
			if ( dataId == null )
			{
				// it should be an data id, but it returns null,
				return null;
			}
			ptr--; // skip the current '('
			length = ptr - offset + 1;
		}
		// the remain characters are instance id.
		// search the parent
		while ( ptr >= offset && buffer[ptr] != '/' )
		{
			ptr--;
		}
		if ( ptr >= offset && buffer[ptr] == '/' )
		{
			String strDesignId = new String( buffer, ptr + 1, offset + length
					- ptr - 1 );
			long designId = Long.parseLong( strDesignId );
			ptr--; // skip the current '/'
			if ( ptr >= offset )
			{
				length = ptr - offset + 1;
				InstanceID parent = InstanceID.parse( buffer, offset, length );
				if ( parent != null )
				{
					return new InstanceID( parent, designId, dataId );
				}
			}
			else
			{
				return new InstanceID( null, designId, dataId );
			}
		}
		return null;
	}
}
