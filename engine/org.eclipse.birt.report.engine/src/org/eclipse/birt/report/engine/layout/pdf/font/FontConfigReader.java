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

package org.eclipse.birt.report.engine.layout.pdf.font;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class FontConfigReader
{

	/** The config xml file name */
	private static final String CONFIG_FILE_PATH = "/fontsConfig.xml"; //$NON-NLS-1$

	private static final String TAG_COMPOSITE_FONT = "composite-font"; //$NON-NLS-1$
	private static final String TAG_ALL_FONTS = "all-fonts"; //$NON-NLS-1$
	private static final String TAG_BLOCK = "block"; //$NON-NLS-1$
	private static final String TAG_MAPPING = "mapping"; //$NON-NLS-1$
	private static final String TAG_PATH = "path"; //$NON-NLS-1$
	private static final String TAG_ENCODING = "encoding"; //$NON-NLS-1$
	private static final String TAG_FONT_MAPPINGS = "font-mappings"; //$NON-NLS-1$
	private static final String TAG_FONT_ALIASES = "font-aliases"; //$NON-NLS-1$

	private static final String PROP_BLOCK_INDEX = "index"; //$NON-NLS-1$
	private static final String PROP_NAME = "name"; //$NON-NLS-1$
	private static final String PROP_FONT_FAMILY = "font-family"; //$NON-NLS-1$
	private static final String PROP_ENCODING = "encoding"; //$NON-NLS-1$
	private static final String PROP_PATH = "path"; //$NON-NLS-1$
	private static final String DEFAULT_BLOCK = "default";

	private FontMappingManager fontMappingManager = new FontMappingManager( );

	private ArrayList fontPaths = new ArrayList( );

	/** the logger logging the error, debug, warning messages. */
	protected static Logger logger = Logger.getLogger( FontConfigReader.class
			.getName( ) );

	public boolean parseConfigFile( )
	{
		try
		{
			Bundle bundle = Platform
					.getBundle( "org.eclipse.birt.report.engine.fonts" ); //$NON-NLS-1$
			if ( bundle == null )
				return false;

			URL fileURL = bundle.getEntry( CONFIG_FILE_PATH );
			return parseConfigFile( fileURL );

		}
		catch ( Exception se )
		{
			logger.log( Level.WARNING, se.getMessage( ), se );
		}
		return false;
	}

	public boolean parseConfigFile( URL fileURL ) throws IOException,
			FactoryConfigurationError, ParserConfigurationException,
			SAXException
	{
		if ( null == fileURL )
			return false;

		InputStream cfgFile = fileURL.openStream( );
		try
		{
			InputStreamReader r = new InputStreamReader(
					new BufferedInputStream( cfgFile ), Charset
							.forName( "UTF-8" ) ); //$NON-NLS-1$
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance( );
			DocumentBuilder db = dbf.newDocumentBuilder( );
			Document doc = db.parse( new InputSource( r ) );

			handleFontMappings( doc );
			handleCompositeFonts( doc );
			handleFontPaths( doc );
			handleFontEncodings( doc );
		}
		finally
		{
			cfgFile.close( );
		}
		return true;
	}

	public String getEmbededFontPath( )
	{
		Bundle bundle = Platform
				.getBundle( "org.eclipse.birt.report.engine.fonts" ); //$NON-NLS-1$
		Path path = new Path( "/fonts" ); //$NON-NLS-1$

		URL fileURL = FileLocator.find( bundle, path, null );
		if ( null == fileURL )
			return null;
		String fontPath = null;
		try
		{
			// 171369 patch provided by Arne Degenring <public@degenring.de>
			fontPath = FileLocator.toFileURL( fileURL ).getPath( );
			if ( fontPath != null && fontPath.length( ) >= 3
					&& fontPath.charAt( 2 ) == ':' )
			{
				// truncate the first '/';
				return fontPath.substring( 1 );
			}
			else
			{
				return fontPath;
			}
		}
		catch ( IOException ioe )
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
			return null;
		}
	}

	public List getTrueTypeFontPaths( )
	{
		return this.fontPaths;
	}

	public FontMappingManager getFontMappingManager( )
	{
		return fontMappingManager;
	}

	private void handleFontEncodings( Document doc )
	{
		HashMap fontEncoding = new HashMap( );
		NodeList encodings = doc.getDocumentElement( ).getElementsByTagName(
				TAG_ENCODING );
		for ( int i = 0; i < encodings.getLength( ); i++ )
		{
			Node node = encodings.item( i );
			String fontFamily = getProperty( node, PROP_FONT_FAMILY );
			String encoding = getProperty( node, PROP_ENCODING );
			if ( isValidValue( encoding ) && isValidValue( fontFamily ) )
				fontEncoding.put( fontFamily, encoding );
		}
		fontMappingManager.addFontEncoding( fontEncoding );
	}

	private void handleFontPaths( Document doc )
	{
		NodeList paths = doc.getDocumentElement( ).getElementsByTagName(
				TAG_PATH );
		for ( int i = 0; i < paths.getLength( ); i++ )
		{
			Node node = paths.item( i );
			String path = getProperty( node, PROP_PATH );
			if ( isValidValue( path ) )
				fontPaths.add( path );
		}
	}

	private void handleFontMappings( Document doc )
	{
		Element documentElement = doc.getDocumentElement( );
		processFontMappings( documentElement
				.getElementsByTagName( TAG_FONT_MAPPINGS ) );
		processFontMappings( documentElement
				.getElementsByTagName( TAG_FONT_ALIASES ) );
	}

	private void processFontMappings( NodeList fontMappings )
	{
		if ( fontMappings == null || fontMappings.getLength( ) <= 0 )
		{
			return;
		}
		Node fontMapping = fontMappings.item( 0 );
		NodeList childNodes = fontMapping.getChildNodes( );
		HashMap fontMappingResult = new HashMap( );
		for ( int i = 0; i < childNodes.getLength( ); i++ )
		{
			Node node = childNodes.item( i );
			if ( node.getNodeType( ) == Node.ELEMENT_NODE
					&& TAG_MAPPING.equals( node.getNodeName( ) ) )
			{
				String name = getProperty( node, PROP_NAME );
				String fontFamily = getProperty( node, PROP_FONT_FAMILY );
				if ( isValidValue( name ) && isValidValue( fontFamily ) )
					fontMappingResult.put( name, fontFamily );
			}
		}

		fontMappingManager.addFontMapping( fontMappingResult );
	}

	private void handleCompositeFonts( Document doc )
	{
		handleAllFontsNode( doc );
		handleCompositeFontsNode( doc );
	}

	private void handleCompositeFontsNode( Document doc )
	{
		NodeList allFonts = doc.getDocumentElement( ).getElementsByTagName(
				TAG_COMPOSITE_FONT );
		for ( int i = 0; i < allFonts.getLength( ); i++ )
		{
			Node node = allFonts.item( i );
			String fontName = getProperty( node, PROP_NAME );
			Map blockMap = parseComsiteFont( node );
			fontMappingManager.addCompositeFonts( fontName, blockMap );
		}
	}

	private void handleAllFontsNode( Document doc )
	{
		NodeList allFonts = doc.getDocumentElement( ).getElementsByTagName(
				TAG_ALL_FONTS );
		if ( allFonts.getLength( ) != 0 )
		{
			Node node = allFonts.item( 0 );
			NodeList blocks = node.getChildNodes( );
			for ( int i = 0; i < blocks.getLength( ); i++ )
			{
				Node blockNode = blocks.item( i );
				if ( !TAG_BLOCK.equals( blockNode.getNodeName( ) ) )
				{
					continue;
				}
				String blockIndex = getProperty( blockNode, PROP_BLOCK_INDEX );
				if ( isValidValue( blockIndex ) )
				{
					int index = Integer.parseInt( blockIndex );
					processAllFontsMapping( blockNode, new Integer( index ) );
					continue;
				}
				String blockName = getProperty( blockNode, PROP_NAME );
				if ( DEFAULT_BLOCK.equalsIgnoreCase( blockName ) )
				{
					processAllFontsMapping( blockNode, DEFAULT_BLOCK );
				}
			}
		}
	}

	private void processAllFontsMapping( Node blockNode, Object blockId )
	{
		NodeList mappings = blockNode.getChildNodes( );
		for ( int j = 0; j < mappings.getLength( ); j++ )
		{

			Node mapping = mappings.item( j );
			if ( mapping.getNodeType( ) != Node.ELEMENT_NODE )
				continue;

			String name = getProperty( mapping, PROP_NAME );
			String fontFamily = getProperty( mapping, PROP_FONT_FAMILY );
			if ( isValidValue( fontFamily ) )
			{
				fontMappingManager.addBlockToCompositeFont( name, blockId,
						fontFamily );
			}
		}
	}

	private Map parseComsiteFont( Node node )
	{
		NodeList blocks = node.getChildNodes( );
		Map blockMap = new HashMap( );
		for ( int i = 0; i < blocks.getLength( ); i++ )
		{
			Node blockNode = blocks.item( i );
			if ( !TAG_BLOCK.equals( blockNode.getNodeName( ) ) )
			{
				continue;
			}
			String blockIndex = getProperty( blockNode, PROP_BLOCK_INDEX );
			if ( isValidValue( blockIndex ) )
			{
				int index = Integer.parseInt( blockIndex );
				String font = getProperty( blockNode, PROP_FONT_FAMILY );
				blockMap.put( new Integer( index ), font );
				continue;
			}
			String blockName = getProperty( blockNode, PROP_NAME );
			if ( DEFAULT_BLOCK.equalsIgnoreCase( blockName ) )
			{
				String font = getProperty( blockNode, PROP_FONT_FAMILY );
				blockMap.put( DEFAULT_BLOCK, font );
			}
		}
		return blockMap;
	}

	private boolean isValidValue( String propertyName )
	{
		return ( null != propertyName && propertyName.length( ) != 0 );
	}

	private String getProperty( Node node, String propertyName )
	{
		if ( null == node )
			return null;
		NamedNodeMap atts = node.getAttributes( );
		if ( null != atts )
		{
			Node property = atts.getNamedItem( propertyName );
			if ( null != property )
				return property.getNodeValue( );
		}
		return null;
	}

}
