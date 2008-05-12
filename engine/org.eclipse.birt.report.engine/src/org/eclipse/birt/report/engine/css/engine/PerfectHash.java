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

package org.eclipse.birt.report.engine.css.engine;

public class PerfectHash
{
	// bidi_hcg: Aligned with generated token.cpp after the "direction" keyword
	// was added. 

	final static int TOTAL_KEYWORDS = 60;

	final static int MIN_WORD_LENGTH = 5;

	final static int MAX_WORD_LENGTH = 21;

	final static int MIN_HASH_VALUE = 9;

	final static int MAX_HASH_VALUE = 107;
	
	/* maximum key range = 99, duplicates = 0 */
	
	static int asso_values[] = {108, 108, 108, 108, 108, 108, 108, 108, 108,
			108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
			108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
			108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 45, 108, 108,
			108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
			108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
			108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
			108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 20, 20, 10, 35,
			5, 47, 40, 20, 25, 108, 5, 20, 20, 0, 0, 35, 108, 0, 0, 0, 5, 5,
			61, 10, 25, 108, 108, 108, 108, 108, 108};

	public static int hash( String str )
	{
		int hashValue = str.length( );
		switch ( hashValue )
		{
			default :
				hashValue += asso_values[str.charAt( 11 )];
			case 11 :
			case 10 :
			case 9 :
			case 8 :
			case 7 :
			case 6 :
				hashValue += asso_values[str.charAt( 5 )];
			case 5 :
			case 4 :
			case 3 :
				hashValue += asso_values[str.charAt( 2 )];
				break;
		}
		return hashValue + asso_values[str.charAt( str.length( ) - 1 )];
	}
	
	static int lengthtable[] = {9, 11, 12, 9, 10, 11, 12, 18, 19, 16, 18, 19,
			5, 16, 17, 13, 11, 12, 13, 10, 16, 18, 19, 10, 6, 7, 13, 14, 10,
			11, 12, 14, 21, 7, 13, 14, 15, 16, 11, 14, 15, 17, 16, 17, 13, 14,
			11, 13, 21, 14, 11, 17, 11, 11, 21, 12, 16, 16, 17, 17};

	static String wordlist[] = {
	     "direction" /* hash value = 9, index = 0 */,
			"margin-left" /* hash value = 11, index = 1 */,
			"margin-right" /* hash value = 12, index = 2 */,
			"font-size" /* hash value = 14, index = 3 */,
			"font-style" /* hash value = 15, index = 4 */,
			"master-page" /* hash value = 16, index = 5 */,
			"font-variant" /* hash value = 17, index = 6 */,
			"border-right-color" /* hash value = 18, index = 7 */,
			"border-bottom-color" /* hash value = 19, index = 8 */,
			"border-top-style" /* hash value = 21, index = 9 */,
			"border-right-style" /* hash value = 23, index = 10 */,
			"border-bottom-style" /* hash value = 24, index = 11 */,
			"color" /* hash value = 25, index = 12 */,
			"border-top-color" /* hash value = 26, index = 13 */,
			"background-repeat" /* hash value = 27, index = 14 */,
			"text-overline" /* hash value = 28, index = 15 */,
			"line-height" /* hash value = 31, index = 16 */,
			"number-align" /* hash value = 32, index = 17 */,
			"margin-bottom" /* hash value = 33, index = 18 */,
			"can-shrink" /* hash value = 35, index = 19 */,
			"background-color" /* hash value = 36, index = 20 */,
			"border-right-width" /* hash value = 38, index = 21 */,
			"border-bottom-width" /* hash value = 39, index = 22 */,
			"text-align" /* hash value = 40, index = 23 */,
			"widows" /* hash value = 41, index = 24 */,
			"orphans" /* hash value = 42, index = 25 */,
			"show-if-blank" /* hash value = 43, index = 26 */,
			"text-transform" /* hash value = 44, index = 27 */,
			"margin-top" /* hash value = 45, index = 28 */,
			"text-indent" /* hash value = 46, index = 29 */,
			"padding-left" /* hash value = 47, index = 30 */,
			"vertical-align" /* hash value = 49, index = 31 */,
			"background-attachment" /* hash value = 51, index = 32 */,
			"display" /* hash value = 52, index = 33 */,
			"number-format" /* hash value = 53, index = 34 */,
			"visible-format" /* hash value = 54, index = 35 */,
			"sql-date-format" /* hash value = 55, index = 36 */,
			"background-image" /* hash value = 56, index = 37 */,
			"date-format" /* hash value = 58, index = 38 */,
			"text-underline" /* hash value = 59, index = 39 */,
			"sql-time-format" /* hash value = 60, index = 40 */,
			"border-left-color" /* hash value = 62, index = 41 */,
			"text-linethrough" /* hash value = 66, index = 42 */,
			"border-left-style" /* hash value = 67, index = 43 */,
			"padding-right" /* hash value = 68, index = 44 */,
			"padding-bottom" /* hash value = 69, index = 45 */,
			"font-weight" /* hash value = 72, index = 46 */,
			"string-format" /* hash value = 73, index = 47 */,
			"background-position-x" /* hash value = 76, index = 48 */,
			"letter-spacing" /* hash value = 79, index = 49 */,
			"padding-top" /* hash value = 81, index = 50 */,
			"border-left-width" /* hash value = 82, index = 51 */,
			"font-family" /* hash value = 83, index = 52 */,
			"white-space" /* hash value = 86, index = 53 */,
			"background-position-y" /* hash value = 91, index = 54 */,
			"word-spacing" /* hash value = 92, index = 55 */,
			"page-break-after" /* hash value = 96, index = 56 */,
			"border-top-width" /* hash value = 97, index = 57 */,
			"page-break-before" /* hash value = 102, index = 58 */,
			"page-break-inside" /* hash value = 107, index = 59 */
	 };

	static short lookup[] = {-1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, 1, 2,
			-1, 3, 4, 5, 6, 7, 8, -1, 9, -1, 10, 11, 12, 13, 14, 15, -1, -1,
			16, 17, 18, -1, 19, 20, -1, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
			-1, 31, -1, 32, 33, 34, 35, 36, 37, -1, 38, 39, 40, -1, 41, -1, -1,
			-1, 42, 43, 44, 45, -1, -1, 46, 47, -1, -1, 48, -1, -1, 49, -1, 50,
			51, 52, -1, -1, 53, -1, -1, -1, -1, 54, 55, -1, -1, -1, 56, 57, -1,
			-1, -1, -1, 58, -1, -1, -1, -1, 59};

	public static int in_word_set( String str )
	{
		int len = str.length( );

		if ( len <= MAX_WORD_LENGTH && len >= MIN_WORD_LENGTH )
		{
			int key = hash( str );

			if ( key <= MAX_HASH_VALUE && key >= 0 )
			{
				int index = lookup[key];

				if ( index >= 0 )
				{
					if ( len == lengthtable[index] )
					{
						String s = wordlist[index];

						if ( s.equals( str ) )
							return index;
					}
				}

			}
		}
		return -1;
	}
}
