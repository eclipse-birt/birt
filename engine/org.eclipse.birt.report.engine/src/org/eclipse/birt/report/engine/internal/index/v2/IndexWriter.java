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
import java.util.HashMap;
import java.util.Map;

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
		if ( entrySize < MAX_INLINE_ENTIRES )
		{
			if ( !inlineMap.containsKey( key ) )
			{
				inlineMap.put( key, value );
				entrySize++;
			}
		}
		else
		{
			if ( inlineMap != null )
			{
				btree = BTreeMap.createTreeMap( archive, name );
				for ( Map.Entry<String, Long> entry : inlineMap.entrySet( ) )
				{
					btree.insert( entry.getKey( ), entry.getValue( ) );
				}
				inlineMap = null;
			}
			if ( !btree.exist( key ) )
			{
				btree.insert( key, value );
				entrySize++;
			}
		}
	}

	void close( ) throws IOException
	{
		if ( inlineMap != null )
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

			}
			finally
			{
				stream.close( );
			}
		}
		if ( btree != null )
		{
			btree.close( );
		}
	}

}
