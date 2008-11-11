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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;

public class IndexWriter implements IndexConstants
{

	IDocArchiveWriter archive;
	String name;
	HashMap<String, Long> inlineMap = new HashMap<String, Long>( );
	BTreeMap btree;
	int entrySize;

	public IndexWriter( IDocArchiveWriter archive, String name )
	{
		this.archive = archive;
		this.name = name;
	}

	void add( String key, long value ) throws IOException
	{
		if ( inlineMap.size( ) >= MAX_INLINE_ENTIRES )
		{
			flushBtree( );
			inlineMap.clear( );
		}
		if ( !inlineMap.containsKey( key ) )
		{
			inlineMap.put( key, value );
			entrySize++;
		}
	}

	void close( ) throws IOException
	{
		if ( btree == null )
		{
			RAOutputStream stream = archive.createOutputStream( name );
			try
			{
				DataOutputStream output = new DataOutputStream( stream );
				IOUtil.writeInt( output, VERSION_0 );
				IOUtil.writeInt( output, INLINE_MAP );
				IOUtil.writeInt( output, inlineMap.size( ) );
				for ( Map.Entry<String, Long> entry : inlineMap.entrySet( ) )
				{
					IOUtil.writeString( output, entry.getKey( ) );
					IOUtil.writeLong( output, entry.getValue( ) );
				}
				inlineMap.clear( );
			}
			finally
			{
				stream.close( );
			}
		}
		if ( btree != null )
		{
			flushBtree( );
			btree.close( );
		}
	}

	protected void flushBtree( ) throws IOException
	{
		if ( btree == null )
		{
			btree = BTreeMap.createTreeMap( archive, name );
		}
		ArrayList<Map.Entry<String, Long>> entries = new ArrayList<Map.Entry<String, Long>>(
				inlineMap.entrySet( ) );
		Collections.sort( entries, new Comparator<Map.Entry<String, Long>>( ) {

			public int compare( Entry<String, Long> o1, Entry<String, Long> o2 )
			{
				return o1.getKey( ).compareTo( o2.getKey( ) );
			}
		} );

		for ( Map.Entry<String, Long> entry : entries )
		{
			btree.insert( entry.getKey( ), entry.getValue( ) );
		}
	}
}
