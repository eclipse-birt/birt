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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Utility class for stored procedure's parameter position
 */
public final class SPParameterPositionUtil
{

	private char[] parameterDefnChars;
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
		this.escaper = escaper;
		this.parameterDefnChars = getParameterDefinitionChars( sqlTxt, escaper );

	};

	/**
	 * exsit non-default parameter or not
	 * @return
	 */
	public boolean hasNonDefaultParameter( )
	{
		boolean nextDelimiterValid = true;
		for ( int i = 0; i < this.parameterDefnChars.length; i++ )
		{
			if ( i > 0 && this.parameterDefnChars[i - 1] == escaper )
				continue;
			if ( '"' == this.parameterDefnChars[i] )
			{
				nextDelimiterValid = !nextDelimiterValid;
			}
			if ( nextDelimiterValid )
			{
				if ( '?' == this.parameterDefnChars[i] )
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * get non-default parameters position is sql text
	 * @return
	 */
	public int[] getParameterPositions( )
	{
		int nextPosition = 1;
		List result = new ArrayList( );
		boolean nextDelimiterValid = true;
		for ( int i = 0; i < this.parameterDefnChars.length; i++ )
		{
			if ( i > 0 && this.parameterDefnChars[i - 1] == escaper )
				continue;
			if ( '"' == this.parameterDefnChars[i] )
			{
				nextDelimiterValid = !nextDelimiterValid;
			}
			if ( nextDelimiterValid )
			{
				if ( '?' == this.parameterDefnChars[i] )
				{
					result.add( new Integer( nextPosition ) );
				}
				if ( ',' == this.parameterDefnChars[i] )
				{
					nextPosition++;
				}
			}
		}
		int[] rt = new int[result.size( )];
		for ( int i = 0; i < result.size( ); i++ )
		{
			rt[i] = new Integer( result.get( i ).toString( ) ).intValue( );
		}
		return rt;
	}

	/**
	 * put sqlText to char array
	 * @param sqlTxt
	 * @param escaper
	 * @return
	 * @throws OdaException
	 */
	private char[] getParameterDefinitionChars( String sqlTxt, char escaper )
			throws OdaException
	{
		char[] temp = sqlTxt.toCharArray( );
		int startPoint = -1;
		int endPoint = -1;
		boolean validBracket = true;
		for ( int i = temp.length - 1; i >= 0; i-- )
		{
			if ( i > 0 && temp[i - 1] == escaper )
				continue;
			if ( validBracket )
			{
				if ( ')' == temp[i] && endPoint == -1 )
				{
					endPoint = i;
				}
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
		if ( startPoint == -1 && endPoint == -1 )
			return new char[0];
		else if ( startPoint >= endPoint || startPoint == -1 )
			throw new OdaException( "Illegal SP call" );
		return sqlTxt.substring( startPoint, endPoint ).toCharArray( );
	}
}
