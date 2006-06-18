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

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.TOCNode;

/**
 * A class for building up TOC hierarchy
 */
public class TOCBuilder
{
	/**
	 * the root TOC entry
	 */
	private TOCNode rootNode;
	private TOCEntry rootEntry;

	/**
	 * @param root
	 *            the root for the TOC tree
	 */
	public TOCBuilder( TOCNode root )
	{
		rootNode = root;
		rootEntry = new TOCEntry( null, rootNode, rootNode );
	}

	public TOCEntry startGroupEntry( TOCEntry parent )
	{
		if (parent == null)
		{
			parent = rootEntry;
		}
		TOCEntry group = new TOCEntry( parent, parent.getNode( ), parent
				.getNode( ) );
		return group;
	}

	public void closeGroupEntry( TOCEntry group )
	{
		assert group != null;
		TOCEntry parent = group.parent;
		if ( parent != null && parent != rootEntry )
		{
			if (parent.node == parent.root) 
			{
				// this is a group entry, and it is the first child of that group,
				// use that entry as parent of following entry of the same group.
				parent.node = group.node;			
			}
		}
	}

	/**
	 * @param displayString
	 *            display string for the TOC entry
	 * @param bookmark
	 */
	public TOCEntry startEntry( TOCEntry parent, String displayString, String bookmark )
	{
		assert displayString != null;

		if ( parent == null )
		{
			parent = rootEntry;
		}
		
		TOCNode parentNode = parent.node;
		TOCNode node = new TOCNode( );
		String id = parentNode.getNodeID( );
		if ( id == null )
		{
			id = "toc";
		}
		id = id + "_" + parentNode.getChildren( ).size( );

		// entry.nodeid is null
		node.setNodeID( id );
		node.setDisplayString( displayString );
		node.setBookmark( bookmark == null ? id : bookmark );
		node.setParent( parentNode );
		parentNode.getChildren( ).add( node );
		
		TOCEntry entry = new TOCEntry( parent, parent.getRoot( ), node );
		return entry;
	}

	public TOCEntry createEntry( TOCEntry parent, String label, String bookmark )
	{
		TOCEntry entry = startEntry( parent, label, bookmark );
		closeEntry(entry);
		return entry;
	}

	/**
	 * close the entry. for top level toc, all entry must be put into the root
	 * entry. for group toc, we must create a root entry, and put all others
	 * into the root entry.
	 */
	public void closeEntry( TOCEntry entry )
	{
		assert entry != null;
		TOCEntry parent = entry.parent;
		if ( parent != null && parent != rootEntry )
		{
			if (parent.node == parent.root) 
			{
				// this is a group entry, and it is the first child of that group,
				// use that entry as parent of following entry of the same group.
				parent.node = entry.node;
			}
		}
	}
	
	public TOCEntry getTOCEntry( )
	{
		return rootEntry;
	}
	
	public TOCNode getTOCNode( )
	{
		return rootNode;
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
