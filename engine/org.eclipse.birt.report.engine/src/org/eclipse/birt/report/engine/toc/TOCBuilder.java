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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.birt.report.engine.api.TOCNode;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
	
	/**
	 * @param root the root for the TOC tree
	 */
	public TOCBuilder( TOCNode root )
	{
		rootEntry = root;
		currentEntry = rootEntry;
	}

	/**
	 * start a TOC entry. It is used for container elements
	 */
	public void startEntry( )
	{
		TOCNode entry = new TOCNode( );
		entry.setParent( currentEntry );
		currentEntry = entry;
	}

	/**
	 * @param id instance ID for the entry
	 * @param displayString display string for the TOC entry
	 * @param bookmark 
	 */
	public void startEntry( String id, String displayString, String bookmark )
	{
		startEntry( );
		TOCNode entry = currentEntry;
		TOCNode parent = entry.getParent( );
		while ( parent != rootEntry && parent.getNodeID( ) == null )
		{
			entry = parent;
			parent = entry.getParent( );
		}
		// entry.nodeid is null
		entry.setNodeID( id );
		entry.setDisplayString( displayString );
		entry.setBookmark( bookmark );
		entry.getParent( ).getChildren( ).add( entry );
	}

	public void createEntry( String id, String label, String bookmark )
	{
		startEntry( id, label, bookmark );
		closeEntry( );

	}

	public void closeEntry( )
	{
		currentEntry = currentEntry.getParent( );
		assert currentEntry != null;
	}

	// public void createTOCEntry( DesignElementHandle design, String id,
	// String label, String bookmark )
	// {
	// TOCEntry parent = currentEntry;
	// Object tocDesign = design;
	//
	// // search if we should add it into the parent
	// do
	// {
	// // test if the current design is the child of current TOC entry.
	// // the condition is true if:
	// // the container's parent == parent.design
	// tocDesign = getDirectChildOf( design, parent.design );
	// if ( tocDesign != null )
	// {
	// break;
	// }
	//
	// parent = parent.parent;
	//
	// } while ( parent != null );
	//
	// // the parent is the parent Entry, the tocDesign is the design level
	// TOCNode node = new TOCNode( );
	// node.setNodeID( id );
	// node.setDisplayString( label );
	// node.setBookmark( bookmark );
	//
	// TOCEntry entry = new TOCEntry( );
	// entry.design = tocDesign;
	// entry.node = node;
	// entry.setParent( parent );
	// currentEntry = entry;
	// }
	//
	// private Object getContainer( Object child )
	// {
	// if ( child == null )
	// {
	// return null;
	// }
	// if ( child instanceof SlotHandle )
	// {
	// // it must be a group header, group footer, detail, header or footer
	// SlotHandle childSlot = (SlotHandle) child;
	// int slotId = childSlot.getSlotID( );
	// DesignElementHandle container = childSlot.getElementHandle( );
	// if ( container instanceof ListingHandle
	// && slotId == ListingHandle.DETAIL_SLOT )
	// {
	// ListingHandle listing = (ListingHandle) container;
	// SlotHandle groupSlot = listing.getGroups( );
	// int groupCount = groupSlot.getCount( );
	// if ( groupCount != 0 )
	// {
	// return groupSlot.get( groupCount - 1 );
	// }
	// }
	// return container;
	// }
	// else if ( child instanceof GroupHandle )
	// {
	// GroupHandle group = (GroupHandle) child;
	// ListingHandle listing = (ListingHandle) group.getContainer( );
	// SlotHandle groups = listing.getGroups( );
	// int groupId = groups.findPosn( group );
	// if ( groupId > 0 )
	// {
	// // return the previous groupo
	// return groups.get( groupId - 1 );
	// }
	// return listing;
	// }
	// else
	// {
	// assert child instanceof DesignElementHandle;
	// DesignElementHandle childElement = (DesignElementHandle) child;
	// DesignElementHandle container = childElement.getContainer( );
	// if ( container == null || container.getContainer( ) == null )
	// {
	// // this is the top level report element
	// return null;
	// }
	// if ( container instanceof GroupHandle
	// || container instanceof ListingHandle )
	// {
	// // group header / footer, listing header, listing footer or
	// // listing detail
	// return childElement.getContainerSlotHandle( );
	// }
	// return container;
	// }
	// }
	//
	// /**
	// * return a object which is the ancestor of child and direct child of
	// * parent.
	// *
	// * @param child
	// * child object
	// * @param parent
	// * parent object
	// * @return object if we found one, NULL oterwise.
	// */
	// public Object getDirectChildOf( Object child, Object parent )
	// {
	// while ( child != null )
	// {
	// Object container = getContainer( child );
	// if ( container == parent )
	// {
	// return child;
	// }
	// if ( container == null )
	// {
	// return null;
	// }
	// child = container;
	// container = getContainer( child );
	// }
	// return null;
	// }

	public TOCNode getTOCNode( )
	{
		return rootEntry;
	}

	static public void write( TOCNode root, OutputStream out )
			throws IOException
	{
		OutputStreamWriter writer = new OutputStreamWriter( out, "utf-8" );
		writer.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" );

		writeNode( root, writer );
		writer.flush( );
		return;
	}

	private static void writeNode( TOCNode node, Writer writer )
			throws IOException
	{
		String id = node.getNodeID( );
		String label = node.getDisplayString( );
		String bookmark = node.getBookmark( );
		writer.write( "<tocnode" );
		if ( id != null )
		{
			writer.write( " id='" );
			writer.write( node.getNodeID( ) );
			writer.write( "'" );
		}
		if ( bookmark != null )
		{
			writer.write( " href='" );
			writer.write( node.getBookmark( ) );
			writer.write( "'" );
		}
		writer.write( ">" );
		if ( label != null )
		{
			writer.write( label );
		}
		List children = node.getChildren( );
		Iterator iter = children.iterator( );
		while ( iter.hasNext( ) )
		{
			TOCNode child = (TOCNode) iter.next( );
			writeNode( child, writer );
		}
		writer.write( "</tocnode>\n" );
	}

	static public TOCNode read( InputStream input )
	{
		try
		{
			SAXParser parser = SAXParserFactory.newInstance( ).newSAXParser( );
			InputSource is = new InputSource( input );
			TOCHandler handle = new TOCHandler( );
			parser.parse( is, handle );
			return handle.getRoot( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
		}
		return null;
	}

	private static class TOCHandler extends DefaultHandler
	{

		public TOCHandler( )
		{
		}

		private TOCNode root = null;
		private TOCNode node;

		public TOCNode getRoot( )
		{
			return this.root;
		}

		public void characters( char[] ch, int start, int length )
				throws SAXException
		{
			StringBuffer buffer = new StringBuffer( );
			if ( node.getDisplayString( ) != null )
			{
				buffer.append( node.getDisplayString( ) );
			}
			buffer.append( ch, start, length );
			node.setDisplayString( buffer.toString( ).trim( ) );
		}

		public void startElement( String uri, String localName, String qName,
				Attributes attributes ) throws SAXException
		{
			String id = attributes.getValue( "id" );
			String href = attributes.getValue( "href" );
			TOCNode child = new TOCNode( );
			child.setNodeID( id );
			child.setBookmark( href );
			child.setParent( node );
			node = child;
		}

		public void endElement( String uri, String localName, String qName )
				throws SAXException
		{
			if ( node.getParent( ) != null )
			{
				node.getParent( ).getChildren( ).add( node );
			}
			else
			{
				this.root = node;
			}
			node = node.getParent( );
		}

	}
}
