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

import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Utility class for stored procedure's parameter position
 */
public final class SPParameterPositionUtil
{

	private int[] position;
	private char escaper;

	/**
	 * 
	 * @param sqlTxt
	 * @param escaper
	 * @throws OdaException
	 */
	public SPParameterPositionUtil( String sqlTxt, char escaper )
			throws OdaException
	{
		try
		{
			this.escaper = escaper;
			String paramTxt = getParameterDefinitionChars( sqlTxt );
			parseQueryText( paramTxt );
		}
		catch ( IOException e )
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
		boolean escaped = false;
		List result = new ArrayList( );
		while ( ( i = reader.read( ) ) != -1 )
		{
			if ( escaped )
			{
				escaped = false;
				continue;
			}
			if ( i == this.escaper )
			{
				escaped = true;
				continue;
			}
			else if ( i == '\"' || i == '\'' )
			{
				readNextQuote( reader, i );
			}
			else if ( i == '(' )
			{
				if ( readNextBracket( reader ) )
					result.add( new Integer( nextPosition ) );
			}
			else if ( i == ',' )
			{
				nextPosition++;
			}
			else if ( i == '?' )
			{
				result.add( new Integer( nextPosition ) );
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
	private static void readNextQuote( StringReader reader, int quote )
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
	private static boolean readNextBracket( StringReader reader )
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
		return this.position == null? new int[0]:this.position;
	}
	
	/**
	 * put sqlText to char array
	 * @param sqlTxt
	 * @param escaper
	 * @return
	 * @throws OdaException
	 */
	private String getParameterDefinitionChars( String sqlTxt )
			throws OdaException
	{
		char[] temp = sqlTxt.toCharArray( );
		int startPoint = -1;
		int endPoint = -1;
		boolean validBracket = true;
		for ( int i = 0; i < temp.length; i++ )
		{
			if ( i > 0 && temp[i - 1] == escaper )
				continue;
			if ( validBracket )
			{
				if ( '(' == temp[i] )
				{
					startPoint = i;
					break;
				}
			}
			if ( '"' == temp[i] )
			{
				validBracket = !validBracket;
			}

		}

		for ( int i = temp.length - 1; i >= 0; i-- )
		{
			if ( i > 0 && temp[i - 1] == escaper )
				continue;
			if ( validBracket )
			{
				if ( ')' == temp[i] )
				{
					endPoint = i;
					break;
				}
			}
			if ( '"' == temp[i] )
			{
				validBracket = !validBracket;
			}

		}
		if ( startPoint == -1 && endPoint == -1 )
			return "";
		else if ( startPoint >= endPoint || startPoint == -1 )
			throw new JDBCException( ResourceConstants.INVALID_STORED_PRECEDURE,
					ResourceConstants.ERROR_INVALID_STATEMENT );
		return sqlTxt.substring( startPoint + 1, endPoint );
	}
}
