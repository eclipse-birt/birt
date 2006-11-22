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

package org.eclipse.birt.report.model.adapter.oda.util;

/**
 * Utility to handle the literal value specified in an oda.design so it can be
 * used as a JS expression in a ROM data set input parameter.
 */

public class ParameterValueUtil
{

	private static final String QUOTE_DELIMITER = "'"; //$NON-NLS-1$
	private static final String DOUBLE_QUOTE_DELIMITER = "\""; //$NON-NLS-1$
	private static final String ESCAPE_QUOTE_CHAR = "\\"; //$NON-NLS-1$
	private static final String ESCAPED_LITERAL_QUOTE = ESCAPE_QUOTE_CHAR
			+ QUOTE_DELIMITER;

	/**
	 * Converts the specified string value to a JS expression so its evaluation
	 * gets handled as a literal value.
	 * 
	 * @param literalValue
	 *            the string constant
	 * @return the js expression.
	 */
	public static String toJsExprValue( String literalValue )
	{
		if ( literalValue == null || literalValue.length( ) == 0 )
			return literalValue;

		StringBuffer value = new StringBuffer( literalValue );

		// escape any literal quote character
		int index = 0;
		while ( ( index = value.indexOf( QUOTE_DELIMITER, index ) ) >= 0 )
		{
			value.insert( index, ESCAPE_QUOTE_CHAR );
			index += 2; // skip the escaped literal quote characters for
			// next search
		}

		// wraps value with begin and end quote delimiters
		value.insert( 0, QUOTE_DELIMITER );
		value.append( QUOTE_DELIMITER );

		return value.toString( );
	}

	/**
	 * Converts the specified JS expression to a literal string value if quote
	 * delimiters are found.
	 * 
	 * @param jsExprValue
	 * @return the literal value without quotation marks.
	 */

	public static String toLiteralValue( String jsExprValue )
	{
		if ( !isQuoted( jsExprValue ) )
			return jsExprValue;

		// remove quote delimiters
		StringBuffer value = new StringBuffer( jsExprValue );
		value.deleteCharAt( jsExprValue.length( ) - 1 );
		value.deleteCharAt( 0 );

		// remove escape quote character
		int index = 0;
		while ( ( index = value.indexOf( ESCAPED_LITERAL_QUOTE, index ) ) >= 0 )
		{
			value.deleteCharAt( index );
			index += 1; // skip the literal quote char for next search
		}

		return value.toString( );
	}

	/**
	 * Checks whether the expression is the string constant. If it is the string
	 * constant, it must be quoted with single/double quotation marks.
	 * 
	 * @param jsExprValue
	 *            the js expression value
	 * @return <code>true</code> if it is string constant.
	 */

	public static boolean isQuoted( String jsExprValue )
	{
		if ( jsExprValue == null || jsExprValue.length( ) == 0 )
			return false;

		boolean isDoubleQuoted = jsExprValue
				.startsWith( DOUBLE_QUOTE_DELIMITER );
		boolean isSingleQuoted = jsExprValue.startsWith( QUOTE_DELIMITER );

		if ( !isDoubleQuoted && !isSingleQuoted )
			return false;

		if ( isDoubleQuoted )
			return jsExprValue.endsWith( DOUBLE_QUOTE_DELIMITER );

		// has start quote, checks if it ends with quote delimiter
		return jsExprValue.endsWith( QUOTE_DELIMITER );
	}
}