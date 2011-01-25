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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * This is the thread safe implementation of XMLParserPool. This implementation
 * is tuned for caching parsers and handlers created using same loading options.
 * To avoid possible memory leak (in case user is trying to parse documents
 * using different options for every parse), there is a restriction on the size
 * of the pool. The key used for handler caching is based on the option map
 * passed to load.
 */

class XMLParserPoolImpl implements XMLParserPool
{

	/**
	 * 
	 */
	
	private final static int SAXPARSER_DEFAULT_SIZE = 300;

	/**
	 * Cache the factory to reduce time to create factory.
	 */

	private final SAXParserFactory factory = SAXParserFactory.newInstance( );

	/**
	 * Map to save cached parsers. The key is the parser properties key sets.
	 * The value is the parser.
	 */

	private final Map<Set<?>, List<SAXParser>> parserCache = new HashMap<Set<?>, List<SAXParser>>( );

	private int sizeLimit;

	/**
	 * Creates an instance that caches parsers and caches handlers as specified.
	 * 
	 * @param size
	 *            indicates the maximum number of instances parser or handler
	 *            instances that will be cached.
	 */
	public XMLParserPoolImpl( int size )
	{
		this.sizeLimit = size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.XMLParserPool#get(java.util.Map)
	 */

	public synchronized SAXParser get( Map<String, ?> properties )
			throws ParserConfigurationException, SAXException
	{
		Map<Object, Object> map = new HashMap<Object, Object>( );
		if ( properties != null )
			map.putAll( properties );

		// if exceeds the limit size, increase the size automatically
		if ( parserCache.size( ) > sizeLimit )
		{
			parserCache.clear( );
		}

		Set<Object> keys = null;
		if ( map != null )
			keys = map.keySet( );

		List<SAXParser> list = parserCache.get( keys );
		if ( list != null )
		{
			int size = list.size( );
			if ( size > 0 )
			{
				return list.remove( size - 1 );
			}

			return createParser( properties );
		}

		parserCache.put( keys, new ArrayList<SAXParser>( ) );
		return createParser( properties );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.XMLParserPool#release(javax.xml.parsers
	 * .SAXParser, java.util.Map)
	 */

	public synchronized void release( SAXParser parser,
			Map<String, ?> properties )
	{
		Map<Object, Object> map = new HashMap<Object, Object>( );
		if ( properties != null )
			map.putAll( properties );

		Set<Object> keys = null;
		if ( map != null )
			keys = map.keySet( );

		List<SAXParser> list = parserCache.get( keys );
		int currentSize = list.size( );

		if ( currentSize < SAXPARSER_DEFAULT_SIZE )
		{
			list.add( parser );
		}
	}

	/**
	 * @param properties
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */

	private SAXParser createParser( Map<String, ?> properties )
			throws ParserConfigurationException, SAXException
	{
	    
	    ClassLoader savedClassloader = Thread.currentThread().getContextClassLoader();
	    Thread.currentThread().setContextClassLoader( this.getClass().getClassLoader() );
	    
		SAXParser parser = factory.newSAXParser( );

		if ( properties != null )
		{
			for ( Map.Entry<String, ?> entry : properties.entrySet( ) )
			{
				parser.getXMLReader( ).setProperty( entry.getKey( ),
						entry.getValue( ) );
			}
		}
		
		Thread.currentThread().setContextClassLoader( savedClassloader );
		
		return parser;
	}
}
