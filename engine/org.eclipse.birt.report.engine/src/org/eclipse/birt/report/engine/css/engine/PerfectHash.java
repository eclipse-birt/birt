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

	final static int TOTAL_KEYWORDS = 59;

	final static int MIN_WORD_LENGTH = 5;

	final static int MAX_WORD_LENGTH = 21;

	final static int MIN_HASH_VALUE = 11;

	final static int MAX_HASH_VALUE = 118;
	
	/* maximum key range = 108, duplicates = 0 */
	
	static int asso_values[] = {119, 119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 21, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 46, 0, 10, 10,
			20, 40, 15, 10, 10, 119, 15, 62, 0, 0, 5, 5, 119, 5, 45, 0, 119,
			15, 50, 35, 56, 119, 119, 119, 119, 119, 119};

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
			case 5 :
			case 4 :
			case 3 :
				hashValue += asso_values[str.charAt( 2 )];
			case 2 :
			case 1 :
				hashValue += asso_values[str.charAt( 0 )];
				break;
		}
		return hashValue + asso_values[str.charAt( str.length( ) - 1 )];
	}
	
	static int lengthtable[] = {11, 12, 11, 12, 10, 11, 13, 12, 18, 14, 15, 11,
			17, 18, 19, 10, 16, 13, 19, 16, 18, 14, 10, 11, 17, 19, 11, 12, 17,
			14, 16, 17, 13, 7, 17, 16, 17, 13, 9, 10, 21, 11, 14, 11, 21, 13,
			14, 16, 5, 16, 16, 11, 21, 12, 14, 11, 13, 6, 7};

	static String wordlist[] = {
	      "time-format" /* hash value = 11, index = 0 */,
	      "number-align" /* hash value = 12, index = 1 */,
	      "margin-left" /* hash value = 16, index = 2 */,
	      "margin-right" /* hash value = 17, index = 3 */,
	      "margin-top" /* hash value = 20, index = 4 */,
	      "date-format" /* hash value = 21, index = 5 */,
	      "margin-bottom" /* hash value = 23, index = 6 */,
	      "padding-left" /* hash value = 27, index = 7 */,
	      "border-right-color" /* hash value = 28, index = 8 */,
	      "padding-bottom" /* hash value = 29, index = 9 */,
	      "datetime-format" /* hash value = 30, index = 10 */,
	      "padding-top" /* hash value = 31, index = 11 */,
	      "background-repeat" /* hash value = 32, index = 12 */,
	      "border-right-width" /* hash value = 33, index = 13 */,
	      "border-bottom-color" /* hash value = 34, index = 14 */,
	      "can-shrink" /* hash value = 35, index = 15 */,
	      "border-top-color" /* hash value = 36, index = 16 */,
	      "padding-right" /* hash value = 38, index = 17 */,
	      "border-bottom-width" /* hash value = 39, index = 18 */,
	      "background-color" /* hash value = 41, index = 19 */,
	      "border-right-style" /* hash value = 43, index = 20 */,
	      "vertical-align" /* hash value = 44, index = 21 */,
	      "text-align" /* hash value = 45, index = 22 */,
	      "text-indent" /* hash value = 46, index = 23 */,
	      "border-left-color" /* hash value = 48, index = 24 */,
	      "border-bottom-style" /* hash value = 49, index = 25 */,
	      "font-weight" /* hash value = 51, index = 26 */,
	      "font-variant" /* hash value = 52, index = 27 */,
	      "border-left-width" /* hash value = 53, index = 28 */,
	      "text-transform" /* hash value = 54, index = 29 */,
	      "background-image" /* hash value = 56, index = 30 */,
	      "page-break-before" /* hash value = 57, index = 31 */,
	      "number-format" /* hash value = 59, index = 32 */,
	      "orphans" /* hash value = 62, index = 33 */,
	      "border-left-style" /* hash value = 63, index = 34 */,
	      "text-linethrough" /* hash value = 66, index = 35 */,
	      "page-break-inside" /* hash value = 67, index = 36 */,
	      "text-overline" /* hash value = 68, index = 37 */,
	      "font-size" /* hash value = 69, index = 38 */,
	      "font-style" /* hash value = 70, index = 39 */,
	      "background-position-x" /* hash value = 71, index = 40 */,
	      "line-height" /* hash value = 73, index = 41 */,
	      "visible-format" /* hash value = 74, index = 42 */,
	      "master-page" /* hash value = 76, index = 43 */,
	      "background-attachment" /* hash value = 77, index = 44 */,
	      "show-if-blank" /* hash value = 78, index = 45 */,
	      "text-underline" /* hash value = 79, index = 46 */,
	      "border-top-width" /* hash value = 81, index = 47 */,
	      "color" /* hash value = 82, index = 48 */,
	      "border-top-style" /* hash value = 86, index = 49 */,
	      "page-break-after" /* hash value = 87, index = 50 */,
	      "white-space" /* hash value = 91, index = 51 */,
	      "background-position-y" /* hash value = 92, index = 52 */,
	      "word-spacing" /* hash value = 97, index = 53 */,
	      "letter-spacing" /* hash value = 101, index = 54 */,
	      "font-family" /* hash value = 107, index = 55 */,
	      "string-format" /* hash value = 109, index = 56 */,
	      "widows" /* hash value = 111, index = 57 */,
	      "display" /* hash value = 118, index = 58 */
	};

	static short lookup[] = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1,
			-1, -1, -1, 2, 3, -1, -1, 4, 5, -1, 6, -1, -1, -1, 7, 8, 9, 10, 11,
			12, 13, 14, 15, 16, -1, 17, 18, -1, 19, -1, 20, 21, 22, 23, -1, 24,
			25, -1, 26, 27, 28, 29, -1, 30, 31, -1, 32, -1, -1, 33, 34, -1, -1,
			35, 36, 37, 38, 39, 40, -1, 41, 42, -1, 43, 44, 45, 46, -1, 47, 48,
			-1, -1, -1, 49, 50, -1, -1, -1, 51, 52, -1, -1, -1, -1, 53, -1, -1,
			-1, 54, -1, -1, -1, -1, -1, 55, -1, 56, -1, 57, -1, -1, -1, -1, -1,
			-1, 58};

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
