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

package org.eclipse.birt.report.model.i18n;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test message file consistency.
 * 
 * 1. Test to check if all the resourcekeys( value for the "displayNameID" attr )
 * needed by the "rom.def" are contained in the message files.
 * 
 * 2.Test to see if all the resourceKeys defined as contants in
 * <code>MessageConstants</code> are contained in the message file.
 * 
 * This test will test the messages based on "Messages.properties."
 *  
 */
public class MessageFileTest extends BaseTestCase
{

	Properties props = new Properties( );

	final static String DISPLAY_NAME_ID_ATTRIB = "displayNameID"; //$NON-NLS-1$
	final static String TOOL_TIP_ID_ATTRIB = "toolTipID"; //$NON-NLS-1$
	final static String TAG_ID_ATTRIB = "tagID"; //$NON-NLS-1$
	final static String ROM_FILE = "rom.def"; //$NON-NLS-1$
	final static String MESSAGE_FILE = "Messages.properties"; //$NON-NLS-1$
	final static String CHARSET = "8859_1"; //$NON-NLS-1$

	Map displayNameMap = new LinkedHashMap( );
	Map toolTipMap = new LinkedHashMap( );
	Map tagMap = new LinkedHashMap( );

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		props.load( ModelResourceHandle.class.getResourceAsStream( MESSAGE_FILE ) );
		this.gatherDisplayNameIDs( );
	}

	/**
	 * Gather all the displayNameIDs defined in Rom.def into a map, keyed by its
	 * line number.
	 * 
	 * @throws IOException
	 */

	private void gatherDisplayNameIDs( ) throws IOException
	{
		BufferedReader br = new BufferedReader( new InputStreamReader(
				ReportDesign.class.getResourceAsStream( ROM_FILE ) ) );

		String line = null;
		int lineCount = 0;

		while ( ( line = br.readLine( ) ) != null )
		{
			++lineCount;

			String displayNameId = getResourceKey( line, DISPLAY_NAME_ID_ATTRIB );
			if ( displayNameId != null )
				displayNameMap.put( String.valueOf( lineCount ), displayNameId );

			String toolTipId = getResourceKey( line, TOOL_TIP_ID_ATTRIB );
			if ( toolTipId != null )
				toolTipMap.put( String.valueOf( lineCount ), toolTipId );

			String tagId = getResourceKey( line, TAG_ID_ATTRIB );
			if ( tagId != null )
				tagMap.put( String.valueOf( lineCount ), tagId );
		}

		br.close( );
	}

	/**
	 * Find the resource key from a string.
	 * 
	 * @param line
	 *            the input line
	 * @param name
	 *            the name of the resource key
	 * @return the id of the resource key
	 */

	private String getResourceKey( String line, String name )
	{
		int index1 = line.indexOf( name );
		if ( index1 == -1 )
			return null;

		// check to see if the first none-blank char after "displayNameID" is
		// '='
		// e.g. displayNameID ="abc"

		int index2 = line.indexOf( '=', index1 );
		if ( index2 == -1 )
			return null;

		String str = line.substring( index1, index2 );
		if ( !name.equalsIgnoreCase( str.trim( ) ) )
			return null;

		int start = line.indexOf( '"', index1 );
		int end = line.indexOf( '"', start + 1 );

		String id = line.substring( start + 1, end );

		return id;
	}

	/**
	 * Test if all the resourceKeys needed by "Rom.def" are contained in the
	 * message file.
	 *  
	 */

	public void testRom( )
	{
		boolean success = true;

		success = checkResourceKeyMap( displayNameMap, props );
		success &= checkResourceKeyMap( toolTipMap, props );
		success &= checkResourceKeyMap( tagMap, props );

		assertTrue( success );
	}

	/**
	 * Check whether all resource keys in map is in message file.
	 * 
	 * @param map
	 *            the resource key map to check
	 * @param props
	 *            BIRT-defined resource messages.
	 * @return false if any resource key is not defined in message file.
	 */

	private static boolean checkResourceKeyMap( Map map, Properties props )
	{
		boolean success = true;

		Iterator iter = map.keySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String lineNo = (String) iter.next( );
			String resourceKey = (String) map.get( lineNo );

			if ( !props.containsKey( resourceKey ) )
			{
				System.out.println( "line " + lineNo //$NON-NLS-1$
						+ ":" + resourceKey + " not exist in message file" ); //$NON-NLS-1$//$NON-NLS-2$
				success = false;
			}
		}

		return success;
	}

	/**
	 * Tests whether there is any duplicate message in "Messages.properties".
	 * 
	 * @throws IOException
	 *             if errors occur when open/read file.
	 *  
	 */

	public void testDuplicateMessages( ) throws IOException
	{
		boolean success = true;

		InputStream is = ModelResourceHandle.class
				.getResourceAsStream( MESSAGE_FILE );
		BufferedReader in = new BufferedReader( new InputStreamReader( is,
				CHARSET ) );

		Hashtable collection = new Hashtable( );

		String line = in.readLine( );
		int lineIndex = 1;

		while ( line != null )
		{
			if ( StringUtil.isBlank( line ) || line.startsWith( "#" ) ) //$NON-NLS-1$
			{
				line = in.readLine( );
				lineIndex++;
				continue;
			}

			String[] data = line.split( "=" ); //$NON-NLS-1$
			if ( data.length != 2 )
			{
				System.out.println( "errors of i18n in line " + lineIndex ); //$NON-NLS-1$
				line = in.readLine( );
				lineIndex++;
				continue;
			}

			if ( collection.containsKey( data[0] ) )
			{
				System.out.println( "duplicate messages in line " + lineIndex ); //$NON-NLS-1$
				success = false;
			}
			else
				collection.put( data[0], data[1] );

			line = in.readLine( );
			lineIndex++;
		}

		assertTrue( success );
	}

	/**
	 * Test to see if all the resourceKeys defined in
	 * <code>MessageConstants</code> are contained in the message file.
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */

	public void testMessageConstants( ) throws IllegalArgumentException,
			IllegalAccessException
	{
		int PUBLIC_FINAL_STATIC = Modifier.PUBLIC | Modifier.FINAL
				| Modifier.STATIC;
		boolean success = true;
		Field[] fields = MessageConstants.class.getFields( );
		String resourceKey = null;

		for ( int i = 0; i < fields.length; i++ )
		{
			Field field = fields[i];

			if ( PUBLIC_FINAL_STATIC == field.getModifiers( ) )
			{
				resourceKey = (String) fields[i].get( null );
				if ( !props.containsKey( resourceKey ) )
				{
					System.out
							.println( "ResourceKey: " + resourceKey + " not exist in message file." ); //$NON-NLS-1$ //$NON-NLS-2$
					success = false;
				}
			}
		}

		assertTrue( success );
	}

}

