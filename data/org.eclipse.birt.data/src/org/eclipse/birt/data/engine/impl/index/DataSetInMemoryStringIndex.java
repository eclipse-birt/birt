/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.index;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;

public class DataSetInMemoryStringIndex extends HashMap
		implements
			IDataSetIndex
{

	public DataSetInMemoryStringIndex( RAInputStream indexStream,
			RAInputStream valueStream ) throws IOException
	{
		super( );
		DataInputStream dis = new DataInputStream( indexStream );
		int size = IOUtil.readInt( indexStream );
		for ( int i = 0; i < size; i++ )
		{
			long offset = IOUtil.readLong( dis );
			if ( SerializableBirtHash.NULL_VALUE_OFFSET == offset )
			{
				super.put( null, new WrapperedValue( null,
						IOUtil.readIntList( dis ) ) );
			}
			else if ( SerializableBirtHash.NOT_HASH_VALUE_OFFSET == offset )
			{
				String keyValue = IOUtil.readString( dis );
				super.put( keyValue, new WrapperedValue( keyValue,
						IOUtil.readIntList( dis ) ) );
			}
			else
			{
				Integer keyValue = IOUtil.readInt( dis );
				super.put( keyValue, new WrapperedValue( valueStream,
						IOUtil.readIntList( dis ),
						offset ) );
			}
		}
	}

	public Set<Integer> getKeyIndex( Object key, int searchType )
			throws DataException
	{
		if ( searchType != IConditionalExpression.OP_EQ
				&& searchType != IConditionalExpression.OP_IN )
			throw new UnsupportedOperationException( );
		if ( searchType == IConditionalExpression.OP_EQ )
			return getKeyIndex( key );
		else
		{
			List candidate = (List) key;
			Set<Integer> result = new HashSet<Integer>( );
			for ( Object eachKey : candidate )
			{
				result.addAll( getKeyIndex( eachKey ) );
			}
			return result;
		}
	}

	private Set<Integer> getKeyIndex( Object key ) throws DataException
	{
		Object result = getWrappedKey( key );
		if ( result == null )
			return new HashSet( );
		else
			return ( (WrapperedValue) result ).getIndex( );
	}

	public String getKeyValue( Object key )
	{
		try
		{
			Object result = getWrappedKey( key );
			if ( result == null )
				return null;
			else

				return ( (WrapperedValue) result ).getKeyValue( );
		}
		catch ( DataException e )
		{
			return null;
		}
	}

	private Object getWrappedKey( Object key ) throws DataException
	{
		Object result = null;
		if ( key == null )
			result = this.get( null );
		else if ( key instanceof String )
		{
			result = this.get( key );
			if ( result == null )
			{
				result = this.get( key.hashCode( ) );
				if ( result instanceof WrapperedValue )
				{
					// Detect hash conflicting
					if ( key.equals( ( (WrapperedValue) result ).getKeyValue( ) ) )
					{
						return result;
					}
					else
					{
						result = null;
					}
				}
			}
		}
		if ( result == null )
			result = this.get( key );
		return result;
	}

	private static class WrapperedValue
	{

		private long keyOffset;
		private RAInputStream keyStream;
		private Set index = new HashSet();
		private Object keyValue;

		WrapperedValue( RAInputStream keyStream, List index, long keyOffset )
		{
			this.keyOffset = keyOffset;
			this.keyStream = keyStream;
			this.index.addAll( index );
		}

		WrapperedValue( String keyValue, List index )
		{
			this.keyValue = keyValue;
			this.index.addAll( index );
		}

		public Set getIndex( )
		{
			return this.index;
		}

		public String getKeyValue( ) throws DataException
		{
			try
			{
				if ( keyValue != null )
				{
					if ( keyValue instanceof String )
						return (String) this.keyValue;
					if ( keyValue instanceof SoftReference )
					{
						String result = ( (SoftReference<String>) keyValue ).get( );
						if ( result != null )
							return result;
					}
				}
				if ( keyStream == null )
					return null;
				synchronized ( this.keyStream )
				{
					if ( keyValue != null )
					{
						if ( keyValue instanceof String )
							return (String) this.keyValue;
						if ( keyValue instanceof SoftReference )
						{
							String result = ( (SoftReference<String>) keyValue ).get( );
							if ( result != null )
								return result;
						}
					}
					this.keyStream.seek( this.keyOffset );
					this.keyValue = new SoftReference<String>( IOUtil.readString( new DataInputStream( this.keyStream ) ) );
				}
				
				return ( (SoftReference<String>) this.keyValue ).get( );
			}
			catch ( IOException e )
			{
				throw new DataException( e.getLocalizedMessage( ), e );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.index.IDataSetIndex#supportFilter(int)
	 */
	public boolean supportFilter( int filterType ) throws DataException
	{
		if ( filterType != IConditionalExpression.OP_EQ
				&& filterType != IConditionalExpression.OP_IN )
		{
			return false;
		}
		return true;
	}

	public Object[] getAllKeyValues() throws DataException
	{
		Object[] values = this.values( ).toArray( );
		Object[] keys = new Object[values.length];
		for( int i = 0; i < values.length; i++ )
		{
			keys[i] = ( ( WrapperedValue )values[i] ).getKeyValue( );
		}
		return keys;
	}
	
	public Set<Integer> getAllKeyRows( ) throws DataException
	{
		Set<Integer> rowID = new HashSet<Integer>();
		Object[] values = this.values( ).toArray( );
		for( int i = 0; i < values.length; i++ )
		{
			Iterator iterator = ( ( WrapperedValue )values[i] ).getIndex( ).iterator( );
			rowID.add(  (Integer) iterator.next( ) );
		}
		return rowID;
	}
}
