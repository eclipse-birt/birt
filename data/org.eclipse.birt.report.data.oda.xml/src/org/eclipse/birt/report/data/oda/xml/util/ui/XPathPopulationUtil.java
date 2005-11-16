/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.xml.util.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a Utility class which is used to help UI to populate the list of possible 
 * XPath Expressions. 
 */

final public class XPathPopulationUtil
{
	/**
	 * This method is used to populate the possible root path expressions List
	 * @param absolutePath must be the absolute path of root path 
	 * @return
	 */
	public static List populateRootPath( String absolutePath )
	{
		assert absolutePath != null;
		
		List result = new ArrayList();
		result.add( absolutePath );
		if( absolutePath.startsWith("/") )
			absolutePath = absolutePath.replaceFirst("/","");
		String[] xPathFrags = absolutePath.split("/");
		
		for ( int i = 1; i < xPathFrags.length;i++)
		{
			String temp = "//";
			temp = addXPathFragsToAString( xPathFrags, i, temp );
			result.add( temp );
		}
		return result;
	}

	/**
	 * This method appends the items starting from certain index 
	 * in an array to a String to build an XPath expression
	 * 
	 * @param xPathFrags the String array
	 * @param i the index
	 * @param s the string
	 * @return
	 */
	private static String addXPathFragsToAString( String[] xPathFrags, int i, String s )
	{
		for( int j = i; j < xPathFrags.length;j++)
		{
			if( j < xPathFrags.length - 1 )
				s += xPathFrags[j] + "/";
			else
				s += xPathFrags[j];
		}
		return s;
	}
	
	/**
	 * This method is used to populate the possible column path expressions List
	 *  
	 * @param rootPath the root path of the table the column in, must be absolute path.
	 * @param columnPath the absolute column path.
	 * @return
	 */
	public static List populateColumnPath( String rootPath, String columnPath )
	{	
		assert rootPath != null;
		assert columnPath != null;
		List result = new ArrayList();
		if( columnPath.startsWith( rootPath ))
		{
			columnPath = columnPath.replaceFirst("\\Q"+rootPath+"/\\E", "");
			result.add( columnPath );
		}else
		{
			String[] rootPathFrags = rootPath.split("/");
			String[] columnPathFrags = columnPath.split("/");
			
			int indexOfFirstDifferentElement = findFirstDifferentItem( rootPathFrags, columnPathFrags );
			
		
			
			String temp = "";
			for( int i = 0; i < rootPathFrags.length - 1 - indexOfFirstDifferentElement; i ++)
			{
				temp += "../";
			}
			
			temp = addXPathFragsToAString( columnPathFrags, indexOfFirstDifferentElement+1, temp);
			result.add( temp );
		}
		
		return result;
	}

	/**
	 * @param rootPathFrags
	 * @param columnPathFrags
	 * @param k
	 * @return
	 */
	private static int findFirstDifferentItem( String[] rootPathFrags, String[] columnPathFrags )
	{
		int result = 0;
		
		//Start from 1 because the  XPathFrags[] always start with a "" item.
		for( int i = 1; i < rootPathFrags.length && i < columnPathFrags.length ; i++)
		{
			if( rootPathFrags[i].equals( columnPathFrags[i]))
			{
				result = i;
				break;
			}
		}
		return result;
	}
}
