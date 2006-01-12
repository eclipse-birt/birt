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

package org.eclipse.birt.report.engine.toc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.TOCNode;

/**
 * A class for building up TOC hierarchy
 */
public class TOCBuilder
{

	/**
	 * the current TOC entry in the tree
	 */
	private TOCNode currentEntry;

	/**
	 * the root TOC entry
	 */
	private TOCNode rootEntry;

	private Stack tocLevels = new Stack( );
	private Stack groups = new Stack( );

	/**
	 * @param root
	 *            the root for the TOC tree
	 */
	public TOCBuilder( TOCNode root )
	{
		rootEntry = root;
		currentEntry = rootEntry;
	}

	public void startGroupEntry( )
	{
		groups.push( tocLevels );
		groups.push( rootEntry );
		groups.push( currentEntry );
		tocLevels = new Stack( );
		rootEntry = currentEntry;
	}

	public void closeGroupEntry( )
	{
		assert tocLevels.isEmpty( );
		currentEntry = (TOCNode) groups.pop( );
		rootEntry = (TOCNode) groups.pop( );
		tocLevels = (Stack) groups.pop( );
	}

	/**
	 * @param displayString
	 *            display string for the TOC entry
	 * @param bookmark
	 */
	public String startEntry( String displayString, String bookmark )
	{
		if ( displayString == null )
		{
			tocLevels.push( Boolean.FALSE );
			return null;
		}
		tocLevels.push( Boolean.TRUE );
		TOCNode entry = new TOCNode( );
		String id = currentEntry.getNodeID( );
		if ( id == null )
		{
			id = "toc";
		}
		id = id + "_" + currentEntry.getChildren( ).size( );

		// entry.nodeid is null
		entry.setNodeID( id );
		entry.setDisplayString( displayString );
		entry.setBookmark( bookmark == null ? id : bookmark );
		entry.setParent( currentEntry );
		currentEntry.getChildren( ).add( entry );
		currentEntry = entry;
		return id;
	}

	public String createEntry( String label, String bookmark )
	{
		String id = startEntry( label, bookmark );
		closeEntry( );
		return id;
	}

	/**
	 * close the entry. for top level toc, all entry must be put into the root
	 * entry. for group toc, we must create a root entry, and put all others
	 * into the root entry.
	 */
	public void closeEntry( )
	{
		if ( tocLevels.pop( ) == Boolean.TRUE )
		{
			if ( groups.isEmpty( ) )
			{
				currentEntry = currentEntry.getParent( );
			}
			else
			{
				if ( currentEntry.getParent( ) != rootEntry )
				{
					currentEntry = currentEntry.getParent( );
				}
			}
		}
		assert currentEntry != null;
	}

	public TOCNode getTOCNode( )
	{
		return rootEntry;
	}

	static public void write( TOCNode root, DataOutputStream out )
			throws IOException
	{
		IOUtil.writeString( out, root.getNodeID( ) );
		IOUtil.writeString( out, root.getDisplayString( ) );
		IOUtil.writeString( out, root.getBookmark( ) );
		List children = root.getChildren( );
		IOUtil.writeInt( out, children.size( ) );
		Iterator iter = children.iterator( );
		while ( iter.hasNext( ) )
		{
			TOCNode child = (TOCNode) iter.next( );
			write( child, out );
		}
		out.flush( );
		return;
	}

	static public void read( TOCNode node, DataInputStream input )
			throws IOException
	{
		String nodeId = IOUtil.readString( input );
		String displayString = IOUtil.readString( input );
		String bookmark = IOUtil.readString( input );
		node.setNodeID( nodeId );
		node.setDisplayString( displayString );
		node.setBookmark( bookmark );
		int size = IOUtil.readInt( input );
		for ( int i = 0; i < size; i++ )
		{
			TOCNode child = new TOCNode( );
			read( child, input );
			child.setParent( node );
			node.getChildren( ).add( child );
		}
	}

}
