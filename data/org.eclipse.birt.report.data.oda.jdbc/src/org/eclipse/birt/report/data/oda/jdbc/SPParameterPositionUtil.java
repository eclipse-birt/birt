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

package org.eclipse.birt.report.data.oda.jdbc;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Utility class for stored procedure's parameter position
 */
public final class SPParameterPositionUtil
{

	private int[] position;
	private SPElement[] spElements;
	private boolean containsReturnValue = false;
	
	//Currently, we only support that identifierQuoteString is only a char.
	private String identifierQuoteString;

	/**
	 * 
	 * @param sqlTxt
	 * @param identifierQuoteString
	 * @throws OdaException
	 */
	public SPParameterPositionUtil( String sqlTxt, String identifierQuoteString )
			throws OdaException
	{
		try
		{
			assert sqlTxt!= null;
			this.identifierQuoteString = identifierQuoteString;
			if (this.identifierQuoteString == null || this.identifierQuoteString.equals( " " ))
			{
				this.identifierQuoteString = ""; //Identifier Quote not supported
			} 
			else if ( this.identifierQuoteString.length( ) > 0 )
			{
				//if identifierQuoteString is a string with several chars, we replace it with ";
				sqlTxt = sqlTxt.replaceAll( "\\Q" + this.identifierQuoteString + "\\E", "\"" );
				this.identifierQuoteString = "\"";
			}
				
			int[] point = this.getPosition( sqlTxt );
			String paramTxt = getParameterDefinitionChars( sqlTxt, point );
			parseQueryText( paramTxt );
		    parseProcedureName( sqlTxt, point );
		}
		catch ( Throwable e )
		{
			// do nothing;
		}
	}

	/**
	 * 
	 * @param queryText
	 * @throws IOException
	 */
	private void parseQueryText( String queryText ) throws IOException
	{
		StringReader reader = new StringReader( queryText );
		int i, nextPosition = 1;
		List result = new ArrayList( );
		while ( ( i = reader.read( ) ) != -1 )
		{
			if ( String.valueOf( i ).equals( identifierQuoteString ) )
			{
				readNextQuote( reader, i );
			}
			else if ( i == '(' )
			{
				if ( readNextBracket( reader ) )
					result.add( Integer.valueOf( nextPosition ) );
			}
			else if ( i == ',' )
			{
				nextPosition++;
			}
			else if ( i == '?' )
			{
				result.add( Integer.valueOf( nextPosition ) );
			}
		}
		position = new int[result.size( )];
		for ( int k = 0; k < result.size( ); k++ )
		{
			position[k] = new Integer( result.get( k ).toString( ) ).intValue( );
		}
	}

	/**
	 * get the quoted string
	 * 
	 * @param strBuf
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private void readNextQuote( StringReader reader, int quote )
			throws IOException
	{
		int i = -1;
		while ( ( i = reader.read( ) ) != -1 )
		{
			if ( i != quote )
			{
				continue;
			}
			else
			{
				break;
			}
		}
	}

	/**
	 * 
	 * @param reader
	 * @throws IOException
	 */
	private boolean readNextBracket( StringReader reader )
			throws IOException
	{
		int i = -1;
		boolean hasPlaceHolder = false;
		while ( ( i = reader.read( ) ) != -1 )
		{
			if ( i == '?' )
			{
				hasPlaceHolder = true;
			}
			else if ( i != ')' )
			{
				continue;
			}
			else
			{
				break;
			}
		}
		return hasPlaceHolder;
	}

	/**
	 * get non-default parameters position is sql text
	 * 
	 * @return
	 */
	public int[] getParameterPositions( )
	{
		return this.position == null ? new int[0] : this.position;
	}

	/**
	 * 
	 * @return
	 * @throws JDBCException
	 */
	public SPElement getProcedure( ) throws JDBCException
	{
		if ( this.spElements != null && this.spElements.length > 0 )
		{
			return this.spElements[this.spElements.length - 1];
		}
		else
		{
			throw new JDBCException( ResourceConstants.INVALID_STORED_PRECEDURE,
					ResourceConstants.ERROR_INVALID_STATEMENT );
		}
	}

	/**
	 * 
	 * @return
	 */
	public SPElement getSchema( )
	{
		if ( this.spElements != null && this.spElements.length >= 2 )
		{
			return this.spElements[0];
		}
		else
		{
			return null;
		}
	}

	/**
	 * 
	 * @return
	 */
	public SPElement getPackage( )
	{
		if ( this.spElements != null && this.spElements.length > 2 )
		{
			return this.spElements[1];
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean containsReturnValue( )
	{
		return this.containsReturnValue;
	}
	
	private void parseProcedureName( String sqlTxt, int[] point ) throws JDBCException
	{
		int start = sqlTxt.toLowerCase( ).indexOf( "call" );
		int end = point[0];
		if( point[0] ==-1 && point[1] ==-1 )
		{
			end = sqlTxt.indexOf( "}" );
		}
		
		if ( start == -1 || start + 4 >= end )
			throw new JDBCException(ResourceConstants.INVALID_STORED_PRECEDURE,
					ResourceConstants.ERROR_INVALID_STATEMENT);
		if ( sqlTxt.substring( 0, start ).matches( ".*\\Q?\\E[ \t]*\\Q=\\E.*" ) )
		{
			this.containsReturnValue = true;
		}
		
		String name = sqlTxt.substring( start + 4, end ).trim( );

		// Split name into period-separated elements. Note that quotes can be used to enclose an element that contains periods. 
		// E.g.,   
		//   schema.package.function  ==>   ["schema", "package", "function"]
		//   schema."package.function"  ==>  ["schema", "package.function"]
		//   "schema"."package"."function"  ==>  ["schema", "package", "function"]
		// Some drivers such as DataDirect for Oracle handles packages by giving us composite name such as "package.proc"
		// as a stored proc name. In order to us to correctly parse the SQL, such composite names must always be identity quoted.
		
		String idq = Pattern.quote(identifierQuoteString);
		// To explain the following pattern: the group matches either of these:
		//   (a) a unquoted name (doesn't start with quote, and greedily matches all characters until the next period)
		//   (b) a quoted name (starts with a quote, then reluctantly matches all characters until the next quote)
		// Groups are separated by period character.
		String patternStr =   "([^" + idq + "][^\\.]*|"  + idq + ".*?"  + idq + ")\\.{0,1}";

		Matcher matcher = Pattern.compile(patternStr).matcher(name);
		ArrayList<SPElement> elemList= new ArrayList<SPElement>();
		while ( matcher.find() ) {
			String elem = matcher.group(1);
			if ( elem.startsWith(identifierQuoteString) && elem.endsWith(identifierQuoteString) ) {
				// Strip quotes at both ends
				elem = elem.substring(identifierQuoteString.length(), 
							elem.length() - identifierQuoteString.length());
				elemList.add( new SPElement(elem, true) );
			} else {
				elemList.add( new SPElement(elem, false) );
			}
		}	

		spElements = elemList.toArray( new SPElement[elemList.size()]);	}
	
	
	/**
	 * put sqlText to char array
	 * @param sqlTxt
	 * @param escaper
	 * @return
	 * @throws OdaException
	 */
	private String getParameterDefinitionChars( String sqlTxt, int[] point )
			throws OdaException
	{
		int startPoint = point[0];
		int endPoint = point[1];
		if ( startPoint == -1 && endPoint == -1 )
			return "";
		else if ( startPoint >= endPoint || startPoint == -1 )
			throw new JDBCException( ResourceConstants.INVALID_STORED_PRECEDURE,
					ResourceConstants.ERROR_INVALID_STATEMENT );
		return sqlTxt.substring( startPoint + 1, endPoint );
	}
	
	private int[] getPosition( String queryText )
	{
		char[] temp = queryText.toCharArray( );
		int[] point = new int[2];
		point[0] = -1; //startPoint
		point[1] = -1; //endPoint
		boolean validBracket = true;
		for ( int i = 0; i < temp.length; i++ )
		{
			if ( validBracket )
			{
				if ( '(' == temp[i] )
				{
					point[0] = i;
					break;
				}
			}
			if ( identifierQuoteString.equals( String.valueOf( temp[i] ) ) )
			{
				validBracket = !validBracket;
			}
		}

		validBracket = true;
		
		for ( int i = temp.length - 1; i >= 0; i-- )
		{
			if ( validBracket )
			{
				if ( ')' == temp[i] )
				{
					point[1] = i;
					break;
				}
			}
			if ( identifierQuoteString.equals( String.valueOf( temp[i] ) ) )
			{
				validBracket = !validBracket;
			}

		}
		return point;
	}
	
	public static class SPElement
	{
		private String name;
		private boolean isIdentifierQuoted;
		public SPElement( String name, boolean isIdentifierQuoted )
		{
			this.name = name;
			this.isIdentifierQuoted = isIdentifierQuoted;
		}
		
		public String getName( )
		{
			return name;
		}
		
		public boolean isIdentifierQuoted( )
		{
			return isIdentifierQuoted;
		}
	}
}
