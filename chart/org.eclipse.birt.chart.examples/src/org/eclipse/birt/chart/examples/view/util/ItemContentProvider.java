/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.view.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

import com.ibm.icu.util.StringTokenizer;
import org.eclipse.birt.chart.examples.ChartExamplesPlugin;
import org.osgi.framework.Bundle;

public class ItemContentProvider
{

	/**
	 * The only instance of ItemContentProvider
	 */
	private static ItemContentProvider content = null;
	
	/**
	 * When the view is open, the descriptor file is read (only once)
	 * and stored in a StringBuffer.
	 */
	private static StringBuffer dFile = new StringBuffer( "" ); //$NON-NLS-1$
	
	//Examples types for each category
	private ArrayList iTypes;	

	//Category types
	private ArrayList cTypes;
	
	//Chart model description for each example
	private String description;

	//Chart model class name for each example
	private String modelClassName;

	//Chart model method name for each example
	private String methodName;		

	/**
	 * All category types are stored in a string array 
	 */
	private static final String[] categoryTypes = new String[]{
		"Primitive Charts", "3D Charts", "Combination Charts",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		"Formatted Charts", "Scripted Charts", "Data Operations" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	};
	
	/**
	 * Provide the unique ItemContentProvider instance
	 */
	public static ItemContentProvider instance( )
	{
		if ( content == null )
		{
			content = new ItemContentProvider( );
			try
			{
				openDescriptorFile( );
			}
			catch ( IOException e )
			{
				e.printStackTrace( );
			}
		}
		return content;
	}

	/**
	 * Read the descriptor file from file system and stored it in a StringBuffer.
	 * 
	 * @throws IOException
	 */
	private static void openDescriptorFile( ) throws IOException
	{
		Bundle bundle = Platform.getBundle( ChartExamplesPlugin.ID );
		Path path = new Path( "/src/org/eclipse/birt/chart/examples/view/util/description.txt" ); //$NON-NLS-1$
		URL fileURL = FileLocator.find( bundle, path, null );

		if ( fileURL != null )
		{
			InputStream file = fileURL.openStream( );

			BufferedReader reader = null;
			reader = new BufferedReader( new InputStreamReader( new BufferedInputStream( file ) ) );

			while ( true )
			{
				String sTmp = reader.readLine( );
				if ( sTmp == null )
				{
					break;
				}
				else
				{
					dFile.append( sTmp.trim( ) );
				}
			}
		}
	}

	/**
	 * Retrieve all the item names belonging to a specific category from dFile.
	 * 
	 * @param categoryName Category name
	 */
	private void parseItems( String categoryName )
	{
		String sTmp = dFile.toString( );
		String startCategory = categoryName + ">"; //$NON-NLS-1$
		String endCategory = "/" + categoryName + ">"; //$NON-NLS-1$ //$NON-NLS-2$
		StringTokenizer tokens = new StringTokenizer( sTmp, "<" ); //$NON-NLS-1$
		boolean bThisCategory = false;
		iTypes = new ArrayList( );
		while ( tokens.hasMoreTokens( ) )
		{
			String token = tokens.nextToken( );
			if ( startCategory.equals( token ) )
			{
				bThisCategory = true;
			}
			else if ( bThisCategory && token.charAt( 0 ) != '/' )
			{
				iTypes.add( token.substring( 0, token.indexOf( ">" ) ) ); //$NON-NLS-1$
			}
			else if ( endCategory.equals( token ) )
			{
				break;
			}
		}
	}

	/**
	 * Retrieve the exampe description according to the example display name.
	 * 
	 * @param itemName Item name
	 */
	private void parseDescription( String itemName )
	{
		String sTmp = dFile.toString( );
		String startItem = "<" + itemName + ">"; //$NON-NLS-1$ //$NON-NLS-2$ 
		String endItem = "</" + itemName + ">"; //$NON-NLS-1$ //$NON-NLS-2$ 

		description = sTmp.substring( sTmp.indexOf( startItem  ) + startItem.length( ),
				sTmp.indexOf( endItem ) );
	}

	/**
	 * Retrieve the example class name according to the example display name.
	 * 
	 * @param itemName Item name
	 */
	private void parseClassName( String itemName )
	{
		modelClassName = itemName;
		if ( modelClassName != null )
		{
			StringTokenizer tokens = new StringTokenizer( modelClassName, " " ); //$NON-NLS-1$
			if ( tokens.countTokens( ) != 0 )
			{
				StringBuffer sb = new StringBuffer( );
				while ( tokens.hasMoreTokens( ) )
				{
					String token = tokens.nextToken( ).trim( );
					sb.append( token );
				}
				modelClassName = sb.toString( );
			}
		}
	}

	/**
	 * Retrieve the method name according to the example class name.
	 * 
	 * @param className Class name
	 */
	private void parseMethodName( String className )
	{
		if ( className != null )
		{
			StringBuffer sb = new StringBuffer( );
			sb.append( "create" ); //$NON-NLS-1$
			sb.append( className );
			methodName = sb.toString( );
		}
	}
	
	
	/**
	 * Retrieve the category names from the String array.
	 * 
	 * @return Category names list
	 */
	public ArrayList getCategoryTypes( )
	{
		cTypes = new ArrayList( );
		for ( int iC = 0; iC < categoryTypes.length; iC++ )
		{
			cTypes.add( categoryTypes[iC] );
		}
		return cTypes;
	}

	/**
	 * @param categoryName Category name
	 * 
	 * @return Example names list
	 */
	public ArrayList getItemTypes( String categoryName )
	{
		parseItems( categoryName );
		return iTypes;
	}
	
	/**
	 * @return Default description (If no example is selected)
	 */
	public String getDefaultDescription( )
	{
		return "Please select an example from the categories";//$NON-NLS-1$
	}

	/**
	 * @param itemName Item name
	 * 
	 * @return Description
	 */
	public String getDescription( String itemName )
	{
		parseDescription( itemName );
		if ( description == null )
		{
			return getDefaultDescription( );
		}
		else
		{
			return description;
		}
	}

	/**
	 * @param itemName Item name
	 * 
	 * @return Example class name
	 */
	public String getClassName( String itemName )
	{
		parseClassName( itemName );
		return modelClassName;
	}

	/**
	 * @param className Example class name
	 * 
	 * @return Chart generation method name
	 */
	public String getMethodName( String className )
	{
		parseMethodName( className );
		return methodName;
	}

}
