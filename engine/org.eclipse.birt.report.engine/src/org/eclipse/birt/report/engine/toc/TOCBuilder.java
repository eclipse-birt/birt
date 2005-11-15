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

public class TOCBuilder
{

	TOCNode root = new TOCNode( );
	TOCNode node;

	public TOCBuilder( )
	{
		root = new TOCNode( );
		root.setNodeID( "/" );
		root.setBookmark( "0" );
		root.setDisplayString( "/" );
		node = root;
	}

	public void openToc( String id, String display, String bookmark )
	{
		TOCNode child = new TOCNode( );
		child.setNodeID( id );
		child.setDisplayString( display );
		child.setBookmark( bookmark );
		child.setParent( node );
		node.getChildren( ).add( child );
		node = child;
	}

	public void closeTOC( )
	{
		assert node != null;
		assert node != root;
		node = node.getParent( );
	}

	public TOCNode getTOCNode( )
	{
		return root;
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
		writer.write( "<tocnode id='" );
		writer.write( node.getNodeID( ) );
		writer.write( "' href='" );
		writer.write( node.getBookmark( ) );
		writer.write( "'>" );
		writer.write( node.getDisplayString( ) );
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

		public TOCNode getRoot()
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
			node.setDisplayString( buffer.toString( ).trim() );
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
