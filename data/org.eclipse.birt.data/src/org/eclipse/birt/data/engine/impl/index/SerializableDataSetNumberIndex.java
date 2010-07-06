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

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

public class SerializableDataSetNumberIndex<T> implements IIndexSerializer
{

	private static int BLOCKNUMBER = 5000;
	private Map<T, List<Integer>> numberAndIndex = new HashMap<T, List<Integer>>( );
	private String fileName = null;
	private StreamManager manager = null;
	public SerializableDataSetNumberIndex( String fileName, StreamManager manager )
	{
		this.fileName = fileName;
		this.manager = manager;
	}

	/**
	 * Put a value into index
	 * 
	 * @param number
	 * @param index
	 * @throws DataException
	 */
	public Object put( Object number, Object index ) throws DataException
	{
		if ( this.numberAndIndex.containsKey( number ) )
		{
			this.numberAndIndex.get( number ).add( (Integer)index );
		}
		else
		{
			List<Integer> list = new ArrayList<Integer>( );
			list.add( (Integer) index );
			this.numberAndIndex.put( (T)number, list );
		}
		return null;
	}

	public void close( ) throws DataException
	{
		try
		{
			List<T> keyList = new ArrayList<T>( );
			keyList.addAll( this.numberAndIndex.keySet( ) );
			if ( keyList.size( ) == 0 )
			{
				return;
			}
			RAOutputStream output = manager.getOutStream( fileName );
			Collections.sort( keyList, new NumberComparator<T>( ) );
			int segs = ( keyList.size( ) - 1 ) / BLOCKNUMBER + 1;
			IOUtil.writeInt( output, segs );
			long intOffset = output.getOffset( );
			DataOutputStream dout = new DataOutputStream( output );
			long[] offsets = new long[segs];
			Object[] boundaryValues = new Object[segs];
			for ( int i = 0; i < segs; i++ )
			{
				IOUtil.writeLong( dout, 0 );
				boundaryValues[i] = keyList.get( i * BLOCKNUMBER );
			}
			for ( int i = 0; i < boundaryValues.length; i++ )
			{
				IOUtil.writeObject( dout, boundaryValues[i] );
			}
			for ( int i = 0; i < segs; i++ )
			{
				offsets[i] = output.getOffset( );
				IOUtil.writeInt( dout, i == segs - 1? keyList.size( )%BLOCKNUMBER : BLOCKNUMBER );
				for ( int j = i * BLOCKNUMBER; j < ( i + 1 ) * BLOCKNUMBER && j < keyList.size( ); j++ )
				{
					IOUtil.writeObject( dout, keyList.get( j ) );
					IOUtil.writeList( dout,
							numberAndIndex.get( keyList.get( j ) ) );
				}
			}
			// Seek to the offset recording location;
			output.seek( intOffset );
			for ( int i = 0; i < offsets.length; i++ )
			{
				IOUtil.writeLong( dout, offsets[i] );
			}
			output.close( );
		}
		catch ( Exception e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
	}

	private class NumberComparator<T1> implements Comparator<T>
	{

		public int compare( T o1, T o2 )
		{
			try
			{
				return ScriptEvalUtil.compare( o1, o2 );
			}
			catch ( DataException e )
			{
				throw new RuntimeException( e );
			}
		}

	}
}
