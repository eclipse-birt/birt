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
import java.util.Stack;

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
			writer.write( " id=\"" );
			writer.write( node.getNodeID( ) );
			writer.write( "\"" );
		}
		if ( bookmark != null )
		{
			writer.write( " href=\"" );
			writer.write( node.getBookmark( ) );
			writer.write( "\"" );
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
