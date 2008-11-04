/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.index.v2;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.btree.BTreeCursor;
import org.eclipse.birt.core.util.IOUtil;

public class IndexReader implements IndexConstants
{

	public interface KeyListener
	{

		void onKey( String key );
	}

	HashMap<String, Long> map;
	BTreeMap btree;

	public IndexReader( IDocArchiveReader archive, String name )
			throws IOException
	{
		if ( archive.exists( name ) )
		{
			RAInputStream input = archive.getInputStream( name );
			try
			{
				int version = input.readInt( );
				if ( version != VERSION_0 )
				{
					throw new IOException( "unsupported index version "
							+ version );
				}
				int type = input.readInt( );
				if ( type == INLINE_MAP )
				{
					DataInputStream di = new DataInputStream( input );
					// load it into the memory
					int entries = IOUtil.readInt( di );
					map = new HashMap<String, Long>( entries );
					for ( int i = 0; i < entries; i++ )
					{
						String key = IOUtil.readString( di );
						long offset = IOUtil.readLong( di );
						map.put( key, new Long( offset ) );
					}
				}
				else
				{
					btree = BTreeMap.openTreeMap( archive, name );

				}
			}
			finally
			{
				input.close( );
			}
		}
	}

	long get( String key ) throws IOException
	{
		if ( map != null )
		{
			Long offset = map.get( key );
			if ( offset != null )
			{
				return offset.longValue( );
			}
			return -1L;
		}
		if ( btree != null )
		{
			Long offset = btree.getValue( key );
			if ( offset != null )
			{
				return offset.longValue( );
			}
			return -1L;
		}
		return -1L;
	}

	void close( ) throws IOException
	{
		map = null;
		if ( btree != null )
		{
			try
			{
				btree.close( );
			}
			finally
			{
				btree = null;
			}
		}
	}

	void forAllKeys( KeyListener listener ) throws IOException
	{
		if ( map != null )
		{
			for ( String bookmark : map.keySet( ) )
			{
				listener.onKey( bookmark );
			}
		}
		if ( btree != null )
		{

			BTreeCursor<String, Long> cursor = btree.createCursor( );
			try
			{
				while ( cursor.next( ) )
				{
					String key = cursor.getKey( );
					listener.onKey( key );
				}
			}
			finally
			{
				cursor.close( );
			}
		}
	}

}
