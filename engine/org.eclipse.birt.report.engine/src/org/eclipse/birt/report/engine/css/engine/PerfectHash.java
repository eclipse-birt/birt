
package org.eclipse.birt.report.engine.css.engine;

public class PerfectHash
{

	final static int TOTAL_KEYWORDS = 57;

	final static int MIN_WORD_LENGTH = 5;

	final static int MAX_WORD_LENGTH = 21;

	final static int MIN_HASH_VALUE = 10;

	final static int MAX_HASH_VALUE = 101;

	/* maximum key range = 91, duplicates = 5 */

	static int asso_values[] = {102, 102, 102, 102, 102, 102, 102, 102, 102,
			102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102,
			102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102,
			102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102,
			102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102,
			102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102,
			102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102,
			102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 0, 5, 11,
			15, 35, 25, 20, 102, 102, 10, 5, 40, 35, 0, 45, 102, 25, 55, 0,
			102, 0, 35, 55, 35, 102, 102, 102, 102, 102, 102};

	public static int hash( String str )
	{
		int len = str.length( );
		return len + asso_values[str.charAt( len - 1 )]
				+ asso_values[str.charAt( 0 )];
	}

	static int lengthtable[] = {11, 14, 11, 17, 21, 11, 10, 13, 14, 16, 16, 17,
			18, 19, 5, 16, 16, 17, 18, 19, 16, 16, 17, 18, 19, 14, 10, 11, 12,
			13, 14, 11, 12, 7, 14, 21, 12, 13, 9, 10, 11, 7, 11, 13, 12, 21,
			17, 17, 13, 11, 12, 16, 13, 10, 6, 14, 11};

	static String wordlist[] = {
			"text-indent" /* hash value = 11, index = 0 */,
			"visible-format" /* hash value = 14, index = 1 */,
			"line-height" /* hash value = 16, index = 2 */,
			"background-repeat" /* hash value = 17, index = 3 */,
			"background-attachment" /* hash value = 21, index = 4 */,
			"date-format" /* hash value = 22, index = 5 */,
			"can-shrink" /* hash value = 25, index = 6 */,
			"text-overline" /* hash value = 28, index = 7 */,
			"text-underline" /* hash value = 29, index = 8 */,
			"background-image" /* hash value = 31, index = 9 */,
			"border-top-style" /* hash value = 0, index = 10 */,
			"border-left-style" /* hash value = 32, index = 11 */,
			"border-right-style" /* hash value = 33, index = 12 */,
			"border-bottom-style" /* hash value = 34, index = 13 */,
			"color" /* hash value = 35, index = 14 */,
			"border-top-width" /* hash value = 36, index = 15 */,
			"text-linethrough" /* hash value = 36, index = 16 */,
			"border-left-width" /* hash value = 37, index = 17 */,
			"border-right-width" /* hash value = 38, index = 18 */,
			"border-bottom-width" /* hash value = 39, index = 19 */,
			"background-color" /* hash value = 41, index = 20 */,
			"border-top-color" /* hash value = 0, index = 21 */,
			"border-left-color" /* hash value = 42, index = 22 */,
			"border-right-color" /* hash value = 43, index = 23 */,
			"border-bottom-color" /* hash value = 44, index = 24 */,
			"letter-spacing" /* hash value = 44, index = 25 */,
			"text-align" /* hash value = 45, index = 26 */,
			"font-weight" /* hash value = 46, index = 27 */,
			"font-variant" /* hash value = 47, index = 28 */,
			"number-format" /* hash value = 48, index = 29 */,
			"vertical-align" /* hash value = 49, index = 30 */,
			"margin-left" /* hash value = 51, index = 31 */,
			"margin-right" /* hash value = 52, index = 32 */,
			"display" /* hash value = 53, index = 33 */,
			"text-transform" /* hash value = 54, index = 34 */,
			"background-position-y" /* hash value = 56, index = 35 */,
			"padding-left" /* hash value = 57, index = 36 */,
			"padding-right" /* hash value = 58, index = 37 */,
			"font-size" /* hash value = 59, index = 38 */,
			"font-style" /* hash value = 60, index = 39 */,
			"white-space" /* hash value = 61, index = 40 */,
			"orphans" /* hash value = 62, index = 41 */,
			"master-page" /* hash value = 66, index = 42 */,
			"string-format" /* hash value = 68, index = 43 */,
			"word-spacing" /* hash value = 72, index = 44 */,
			"background-position-x" /* hash value = 76, index = 45 */,
			"page-break-before" /* hash value = 77, index = 46 */,
			"page-break-inside" /* hash value = 0, index = 47 */,
			"show-if-blank" /* hash value = 78, index = 48 */,
			"font-family" /* hash value = 81, index = 49 */,
			"number-align" /* hash value = 82, index = 50 */,
			"page-break-after" /* hash value = 86, index = 51 */,
			"margin-bottom" /* hash value = 93, index = 52 */,
			"margin-top" /* hash value = 95, index = 53 */,
			"widows" /* hash value = 96, index = 54 */,
			"padding-bottom" /* hash value = 99, index = 55 */, 
			"padding-top" /*hash value = 101, index = 56 */
	};

	static short lookup[] = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1,
			-1, 1, -1, 2, 3, -1, -1, -1, 4, 5, -1, -1, 6, -1, -1, 7, 8, -1,
			-141, 11, 12, 13, 14, -131, 17, 18, 19, -1, -127, 22, 23, -121, 26,
			27, 28, 29, 30, -1, 31, 32, 33, 34, -1, 35, 36, 37, 38, 39, 40, 41,
			-33, -2, -1, 42, -1, 43, -37, -2, -1, 44, -42, -2, -1, 45, -137,
			48, -11, -2, 49, 50, -48, -2, -1, 51, -1, -1, -1, -1, -1, -1, 52,
			-1, 53, 54, -1, -1, 55, -1, 56};

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
				else if ( index < -TOTAL_KEYWORDS )
				{
					int offset = -1 - TOTAL_KEYWORDS - index;
					int lengthptr = TOTAL_KEYWORDS + lookup[offset];
					int wordptr = TOTAL_KEYWORDS + lookup[offset];
					int wordendptr = wordptr + -lookup[offset + 1];

					while ( wordptr < wordendptr )
					{
						if ( len == lengthtable[lengthptr] )
						{
							String s = wordlist[wordptr];

							if ( s.equals( str ) )
								return wordptr;
						}
						lengthptr++;
						wordptr++;
					}
				}
			}
		}
		return -1;
	}
}
