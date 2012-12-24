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
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.content.impl.BookmarkContent;

public class IndexReader implements IndexConstants
{

	public interface KeyListener
	{

		void onKey( String key );
	}

	public interface ValueListener
	{

		void onValue( Object value );
	}

	int valueType;
	HashMap<String, Object> map;
	BTreeMap btree;

	public IndexReader( IDocArchiveReader archive, String name )
			throws IOException
	{
		if ( archive.exists( name ) )
		{
			RAInputStream input = archive.getInputStream( name );
			try
			{
				int version = readVersion( input.readInt( ), name );

				switch ( version )
				{
					case VERSION_0 :
					{
						valueType = BTreeMap.LONG_VALUE;
						int type = input.readInt( );
						if ( type == INLINE_MAP )
						{
							DataInputStream di = new DataInputStream( input );
							// load it into the memory
							int entries = IOUtil.readInt( di );
							map = new HashMap<String, Object>( entries );
							for ( int i = 0; i < entries; i++ )
							{
								String key = IOUtil.readString( di );
								long offset = IOUtil.readLong( di );
								map.put( key, new Long( offset ) );
							}
						}
						else
						{
							btree = BTreeMap.openTreeMap( archive, name,
									valueType );
						}
						break;
					}
					case VERSION_1 :
					{
						valueType = BTreeMap.BOOKMARK_VALUE;
						int type = input.readInt( );
						if ( type == INLINE_MAP )
						{
							DataInputStream di = new DataInputStream( input );
							int entries = IOUtil.readInt( di );
							map = new HashMap<String, Object>( entries );
							for ( int index = 0; index < entries; index++ )
							{
								String key = IOUtil.readString( di );
								BookmarkContent bookmark = new BookmarkContent( );
								bookmark.readStream( di );
								map.put( key, bookmark );
							}
						}
						else
						{
							btree = BTreeMap.openTreeMap( archive, name,
									valueType );
						}
						break;
					}
					default :
					{
						throw new IOException( "unsupported index version "
								+ version );
					}
				}
			}
			finally
			{
				input.close( );
			}
		}
	}
	
	private int readVersion( int versionInDocument, String streamName )
	{
		// The "versionInDocument" is originally supposed to be the IndexReader
		// version. As it is not properly stored, use stream name to return
		// correct version.
		if ( versionInDocument == VERSION_0 || versionInDocument == VERSION_1 )
		{
			if ( ReportDocumentConstants.REPORTLET_ID_INDEX_STREAM
					.equals( streamName )
					|| ReportDocumentConstants.REPORTLET_BOOKMARK_INDEX_STREAM
							.equals( streamName ) )
			{
				return VERSION_0;
			}
			else if ( ReportDocumentConstants.BOOKMARK_STREAM
					.equals( streamName ) )
			{
				return VERSION_1;
			}
		}
		return versionInDocument;
	}


	int getValueType( )
	{
		return valueType;
	}

	private Object get( String key ) throws IOException
	{
		if ( map != null )
		{
			return map.get( key );
		}
		if ( btree != null )
		{
			return btree.getValue( key );
		}
		return null;
	}

	Long getLong( String key ) throws IOException
	{
		if ( valueType != BTreeMap.LONG_VALUE )
			return null;

		return (Long) get( key );
	}

	BookmarkContent getBookmarkContent( String key ) throws IOException
	{
		if ( valueType != BTreeMap.BOOKMARK_VALUE )
			return null;

		return (BookmarkContent) get( key );
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

			BTreeCursor<String, Object> cursor = btree.createCursor( );
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

	void forAllValues( ValueListener listener ) throws IOException
	{
		if ( valueType != BTreeMap.BOOKMARK_VALUE )
			return;

		if ( map != null )
		{
			for ( Object v : map.values( ) )
			{
				listener.onValue( (BookmarkContent) v );
			}
		}
		if ( btree != null )
		{
			BTreeCursor<String, Object> cursor = btree.createCursor( );
			try
			{
				while ( cursor.next( ) )
				{
					listener.onValue( cursor.getValue( ) );
				}
			}
			finally
			{
				cursor.close( );
			}
		}
	}
}
