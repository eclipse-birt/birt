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

import org.eclipse.birt.report.data.oda.xml.util.SaxParserUtil;
import org.eclipse.birt.report.data.oda.xml.util.UtilConstants;

/**
 * This class is a Utility class which is used to help UI to populate the list of possible 
 * XPath Expressions. 
 */

final public class XPathPopulationUtil
{
	private static final String XPATH_WILDCARD = "*";
	private static final String XPATH_ATTR_HEADER_WITH_SLASH = "/@";
	private static final String XPATH_ATTR_HEADER_WITH_SQUARE_PATTERN = "\\Q[@\\E";
	
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
		if( absolutePath.startsWith(UtilConstants.XPATH_SLASH) )
			absolutePath = absolutePath.replaceFirst(UtilConstants.XPATH_SLASH,"");
		String[] xPathFrags = absolutePath.split(UtilConstants.XPATH_SLASH);
		
		for ( int i = 1; i < xPathFrags.length;i++)
		{
			String temp = UtilConstants.XPATH_DOUBLE_SLASH;
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
				s += xPathFrags[j] + UtilConstants.XPATH_SLASH;
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
	public static String populateColumnPath( String rootPath, String columnPath )
	{	
		assert rootPath != null;
		assert columnPath != null;
	
		rootPath = SaxParserUtil.processParentAxis( rootPath );
		if( rootPath.equals( "//" )||rootPath.equals( "//*" ))
		{
			String[] temp = columnPath.split( "\\Q/\\E" );
			if( temp.length <=2 )
				return null;
			else
			{
				String result = "";
				for(int i = 2; i < temp.length; i++)
				{
					result+="/"+temp[i];
				}
				return result;
			}
		}
			
		if( columnPath.startsWith( rootPath ))
		{
			return columnPath.replaceFirst("\\Q"+rootPath+"\\E", "");
		}else
		{
			return getXPathExpression( rootPath, columnPath );
		}
	}

	/**
	 * @param rootPath
	 * @param columnPath
	 * @return
	 */
	private static String getXPathExpression( String rootPath, String columnPath )
	{
		String[] rootPathFrags = rootPath.replaceAll(UtilConstants.XPATH_ELEM_INDEX_PATTERN,"").split(UtilConstants.XPATH_SLASH);
		String[] columnPathFrags = columnPath.replaceAll(UtilConstants.XPATH_ELEM_INDEX_PATTERN,"").split(UtilConstants.XPATH_SLASH);
		
		//The length of rootPathFrags and columnPathFrags should larger than 2,
		//for the simplest path would be /elementName, which, if being splitted by "/",
		//would produces a 2 element string array.
		if( rootPathFrags.length < 2 || columnPathFrags.length < 2 )
			return null;
		
		//The position which starting the common part of root path and column path in columnPathFrags array.
		int startingIndex = 0;
		
		//The position which ending the common part of root path and column path in columnPathFrags array 
		int endingIndex = 0;
		
		//If rootPath starting with "//", then mean the rootPath is a relative path, else,
		//the rootPath is an absolute path
		if( !rootPath.startsWith(UtilConstants.XPATH_DOUBLE_SLASH))
		{
			//If rootPath is absolute path, then the startingIndex must be 1.If not then
			//the rootPath and columnPath has nothing in common.
			if( !is2FragmentsEqual(columnPathFrags[1],rootPathFrags[1]))
				return null;
			else
			{
				rootPathFrags = (UtilConstants.XPATH_SLASH+rootPath).split(UtilConstants.XPATH_SLASH);
			}
		}
		
		assert rootPathFrags.length >= 3;
		
		String commonRoot = rootPathFrags[2];
		
		startingIndex = getStartingIndex( columnPathFrags, commonRoot );
		
		//If startingIndex == 0, that means the given column path do not have common
		if( columnPathFrags.length < startingIndex+1 || startingIndex == 0)
			return null;

		endingIndex = getEndingIndex( rootPathFrags, columnPathFrags, startingIndex );
		
		return populateXpathExpression( columnPath, rootPathFrags, startingIndex, endingIndex );
	}

	/**
	 * @param columnPathFrags
	 * @param startingIndex
	 * @param commonRoot
	 * @return
	 */
	private static int getStartingIndex( String[] columnPathFrags, String commonRoot )
	{
		int startingIndex = 0;
		for( int i = 1; i < columnPathFrags.length; i++)
		{
			if( is2FragmentsEqual(commonRoot,columnPathFrags[i]))
			{
				startingIndex = i;
				break;
			}
		}
		return startingIndex;
	}
	
	/**
	 * 
	 * @param frag1
	 * @param frag2
	 * @return
	 */
	private static boolean is2FragmentsEqual(String frag1, String frag2)
	{
		if( frag1.equals(XPATH_WILDCARD)||frag2.equals(XPATH_WILDCARD))
			return true;
		else
			return frag1.equals(frag2);
	}
	
	/**
	 * @param rootPathFrags
	 * @param columnPathFrags
	 * @param startingIndex
	 * @param endingIndex
	 * @return
	 */
	private static int getEndingIndex( String[] rootPathFrags, String[] columnPathFrags, int startingIndex )
	{
		int start = startingIndex;
		int endingIndex = 0;
		for( int i = startingIndex+1; i < columnPathFrags.length && i - startingIndex + 2< rootPathFrags.length; i++ )
		{
			if( !is2FragmentsEqual(columnPathFrags[i],rootPathFrags[i - startingIndex + 2]))
			{
				endingIndex = i - 1;
				break;
			}
			start = i;
		}

		if( endingIndex == 0 && startingIndex!= 0)
		{
			endingIndex = start;
		}
		return endingIndex;
	}

	/**
	 * @param columnPath
	 * @param rootPathFrags
	 * @param startingIndex
	 * @param endingIndex
	 * @return
	 */
	private static String populateXpathExpression( String columnPath, String[] rootPathFrags, int startingIndex, int endingIndex )
	{
		String result = "";
		
		int fetchBackLevel = rootPathFrags.length - 3 - (endingIndex - startingIndex);
		for( int i = 0; i < fetchBackLevel; i ++)
		{
			result += "../";
		}
		
		return addXPathFragsToAString( columnPath.replaceAll(XPATH_ATTR_HEADER_WITH_SQUARE_PATTERN,XPATH_ATTR_HEADER_WITH_SLASH).split(UtilConstants.XPATH_SLASH), endingIndex+1, result);
	}
}
