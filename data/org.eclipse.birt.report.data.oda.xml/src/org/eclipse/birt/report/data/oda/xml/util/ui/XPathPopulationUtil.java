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
	public static String populateColumnPath( String rootPath, String columnPath )
	{	
		assert rootPath != null;
		assert columnPath != null;
		String result = null;
		if( columnPath.startsWith( rootPath ))
		{
			columnPath = columnPath.replaceFirst("\\Q"+rootPath+"\\E", "");
			result = columnPath;
		}else
		{
			String[] rootPathFrags = rootPath.split("/");
			String[] columnPathFrags = columnPath.replaceAll("\\Q[@\\E","/@").split("/");
			
			if( rootPathFrags.length < 2 || columnPathFrags.length < 2 )
				return result;
			int startingIndex = 0;
			int endingIndex = 0;
			
	
			if( !rootPath.startsWith("//"))
			{
				if( !twoFragmentsEqual(columnPathFrags[1],rootPathFrags[1]))
					return result;
				else
				{
					rootPathFrags = ("/"+rootPath).split("/");
				}
			}
			assert rootPathFrags.length >= 3;
			String commonRoot = rootPathFrags[2];
			for( int i = 1; i < columnPathFrags.length; i++)
			{
				if( twoFragmentsEqual(commonRoot,columnPathFrags[i]))
				{
					startingIndex = i;
					break;
				}
			}
			//If startingIndex == 0, that means the given column path do not have common
			if( columnPathFrags.length < startingIndex+1 || startingIndex == 0)
				return result;
		
			int t = startingIndex;
			for( int i = startingIndex+1; i < columnPathFrags.length && i - startingIndex + 2< rootPathFrags.length; i++ )
			{
				if( !twoFragmentsEqual(columnPathFrags[i],rootPathFrags[i - startingIndex + 2]))
				{
					endingIndex = i - 1;
					break;
				}
				t = i;
			}

			if( endingIndex == 0 && startingIndex!= 0)
			{
				endingIndex = t;
			}
			
			String temp = "";
			int fetchBackLevel = rootPathFrags.length - 3 - (endingIndex - startingIndex);
			for( int i = 0; i < fetchBackLevel; i ++)
			{
				temp += "../";
			}
			
			temp = addXPathFragsToAString( columnPath.replaceAll("\\Q[@\\E","/@").split("/"), endingIndex+1, temp);
			result = temp;
		}
		
		return result.matches(".*\\Q]\\E")?result.replaceAll("\\Q/@\\E","/[@"):result;
	}
	
	private static boolean twoFragmentsEqual(String frag1, String frag2)
	{
		if( frag1.equals("*")||frag2.equals("*"))
			return true;
		else
			return frag1.equals(frag2);
	}
}
