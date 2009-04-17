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

	final static int TOTAL_KEYWORDS = 58;

	final static int MIN_WORD_LENGTH = 5;

	final static int MAX_WORD_LENGTH = 21;

	final static int MIN_HASH_VALUE = 11;

	final static int MAX_HASH_VALUE = 116;
	
	/* maximum key range = 106, duplicates = 0 */
	
	static int asso_values[] = {117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
	      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
	      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
	      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
	      117, 117, 117, 117, 117,  60, 117, 117, 117, 117,
	      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
	      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
	      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
	      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
	      117, 117, 117, 117, 117, 117, 117,  22,   0,  15,
	        5,  20,  40,   0,   5,  30, 117,   5,   5,   0,
	        5,  20,   5, 117,   0,  30,   0, 117,  35,  55,
	       30,  45, 117, 117, 117, 117, 117, 117
	    };

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
	
	static int lengthtable[] = {
		11, 12, 10, 11, 18,  9, 11, 12, 18, 14,  5, 11, 12, 13,
	      16, 17, 13, 10, 17, 18, 19, 11, 17, 16, 19, 10, 16, 14,
	      16, 11, 12, 21, 19, 11,  7, 14, 16, 12, 13, 21, 17, 13,
	       9, 10, 16, 17, 14, 16, 17, 14, 21,  7, 16, 14,  6, 17,
	      11, 11
	    };
	static String wordlist[] = {
		"margin-left" /* hash value = 11, index = 0 */,
	      "margin-right" /* hash value = 12, index = 1 */,
	      "margin-top" /* hash value = 15, index = 2 */,
	      "data-format" /* hash value = 16, index = 3 */,
	      "border-right-color" /* hash value = 18, index = 4 */,
	      "direction" /* hash value = 19, index = 5 */,
	      "line-height" /* hash value = 21, index = 6 */,
	      "padding-left" /* hash value = 22, index = 7 */,
	      "border-right-width" /* hash value = 23, index = 8 */,
	      "padding-bottom" /* hash value = 24, index = 9 */,
	      "color" /* hash value = 25, index = 10 */,
	      "padding-top" /* hash value = 26, index = 11 */,
	      "number-align" /* hash value = 27, index = 12 */,
	      "padding-right" /* hash value = 28, index = 13 */,
	      "border-top-color" /* hash value = 31, index = 14 */,
	      "background-repeat" /* hash value = 32, index = 15 */,
	      "margin-bottom" /* hash value = 33, index = 16 */,
	      "can-shrink" /* hash value = 35, index = 17 */,
	      "background-height" /* hash value = 37, index = 18 */,
	      "border-right-style" /* hash value = 38, index = 19 */,
	      "border-bottom-color" /* hash value = 39, index = 20 */,
	      "text-indent" /* hash value = 41, index = 21 */,
	      "page-break-before" /* hash value = 42, index = 22 */,
	      "page-break-after" /* hash value = 43, index = 23 */,
	      "border-bottom-width" /* hash value = 44, index = 24 */,
	      "text-align" /* hash value = 45, index = 25 */,
	      "background-color" /* hash value = 46, index = 26 */,
	      "letter-spacing" /* hash value = 49, index = 27 */,
	      "text-linethrough" /* hash value = 51, index = 28 */,
	      "font-weight" /* hash value = 56, index = 29 */,
	      "font-variant" /* hash value = 57, index = 30 */,
	      "background-attachment" /* hash value = 58, index = 31 */,
	      "border-bottom-style" /* hash value = 59, index = 32 */,
	      "master-page" /* hash value = 61, index = 33 */,
	      "orphans" /* hash value = 62, index = 34 */,
	      "text-transform" /* hash value = 64, index = 35 */,
	      "border-top-style" /* hash value = 66, index = 36 */,
	      "word-spacing" /* hash value = 67, index = 37 */,
	      "text-overline" /* hash value = 68, index = 38 */,
	      "background-position-x" /* hash value = 71, index = 39 */,
	      "page-break-inside" /* hash value = 72, index = 40 */,
	      "show-if-blank" /* hash value = 73, index = 41 */,
	      "font-size" /* hash value = 74, index = 42 */,
	      "font-style" /* hash value = 75, index = 43 */,
	      "border-top-width" /* hash value = 76, index = 44 */,
	      "border-left-color" /* hash value = 77, index = 45 */,
	      "visible-format" /* hash value = 79, index = 46 */,
	      "background-image" /* hash value = 81, index = 47 */,
	      "border-left-width" /* hash value = 82, index = 48 */,
	      "vertical-align" /* hash value = 84, index = 49 */,
	      "background-position-y" /* hash value = 86, index = 50 */,
	      "display" /* hash value = 87, index = 51 */,
	      "background-width" /* hash value = 91, index = 52 */,
	      "text-underline" /* hash value = 94, index = 53 */,
	      "widows" /* hash value = 96, index = 54 */,
	      "border-left-style" /* hash value = 97, index = 55 */,
	      "font-family" /* hash value = 101, index = 56 */,
	      "white-space" /* hash value = 116, index = 57 */
	};

	static short lookup[] = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  1, -1,
	      -1,  2,  3, -1,  4,  5, -1,  6,  7,  8,  9, 10, 11, 12,
	      13, -1, -1, 14, 15, 16, -1, 17, -1, 18, 19, 20, -1, 21,
	      22, 23, 24, 25, 26, -1, -1, 27, -1, 28, -1, -1, -1, -1,
	      29, 30, 31, 32, -1, 33, 34, -1, 35, -1, 36, 37, 38, -1,
	      -1, 39, 40, 41, 42, 43, 44, 45, -1, 46, -1, 47, 48, -1,
	      49, -1, 50, 51, -1, -1, -1, 52, -1, -1, 53, -1, 54, 55,
	      -1, -1, -1, 56, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	      -1, -1, -1, -1, 57
	    };

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
