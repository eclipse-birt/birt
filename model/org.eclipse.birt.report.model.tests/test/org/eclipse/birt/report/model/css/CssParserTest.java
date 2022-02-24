/*******************************************************************************
 * Copyright (c) {DATE} Actuate Corporation {ADD OTHER COPYRIGHT OWNERS}.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  {ADD SUBSEQUENT AUTHOR & CONTRIBUTION}
 *******************************************************************************/

package org.eclipse.birt.report.model.css;

import java.io.StringReader;
import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.birt.report.model.css.property.ParseException;
import org.eclipse.birt.report.model.css.property.PropertyParser;

/**
 * Test case
 * 
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/07 15:22:01 $
 */
public class CssParserTest extends TestCase {

	/**
	 * 
	 * @throws Exception
	 */
	public void testBackground() throws Exception {
		String style = "url(\"chess.png\") gray 50% repeat fixed "; //$NON-NLS-1$
		PropertyParser parser = new PropertyParser(new StringReader(style));
		parser.parseBackground();
		HashMap result = parser.getCssProperties();
		assertEquals(result.get("background-image"), "url(\"chess.png\")"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("background-color"), "gray"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("background-position"), "50%"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("background-repeat"), "repeat"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("background-attachment"), "fixed"); //$NON-NLS-1$//$NON-NLS-2$

		style = "#ccc"; //$NON-NLS-1$
		parser.ReInit(new StringReader(style));
		parser.parseBackground();
		result = parser.getCssProperties();
		assertEquals("#ccc", result.get("background-color")); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testFont() throws Exception {
		String style = "12px/14px sans-serif "; //$NON-NLS-1$
		PropertyParser parser = new PropertyParser(new StringReader(style));
		parser.parseFont();
		HashMap result = parser.getCssProperties();
		// assertEquals( result.get( "font-weight" ), "bold" );
		// assertEquals( result.get( "font-style" ), "italic" );
		assertEquals(result.get("font-size"), "12px"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("line-height"), "14px"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("font-family"), "sans-serif"); //$NON-NLS-1$//$NON-NLS-2$

		parser.ReInit(new StringReader(" 80% sans-serif ")); //$NON-NLS-1$
		parser.parseFont();
		result = parser.getCssProperties();
		// assertEquals( result.get( "font-weight" ), "bold" );
		// assertEquals( result.get( "font-style" ), "italic" );
		assertEquals(result.get("font-size"), "80%"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("font-family"), "sans-serif"); //$NON-NLS-1$//$NON-NLS-2$

		parser.ReInit(new StringReader(" x-large/110% \"New Century Schoolbook\", serif ")); //$NON-NLS-1$
		parser.parseFont();
		result = parser.getCssProperties();
		assertEquals(result.get("font-size"), "x-large"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("line-height"), "110%"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("font-family"), "\"New Century Schoolbook\",serif"); //$NON-NLS-1$//$NON-NLS-2$

		parser.ReInit(new StringReader(" bold italic large Palatino, serif ")); //$NON-NLS-1$
		parser.parseFont();
		result = parser.getCssProperties();
		assertEquals(result.get("font-weight"), "bold"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("font-style"), "italic"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("font-size"), "large"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("font-family"), "Palatino,serif"); //$NON-NLS-1$//$NON-NLS-2$

		parser.ReInit(new StringReader(" normal small-caps 120%/120% fantasy ")); //$NON-NLS-1$
		parser.parseFont();
		result = parser.getCssProperties();
		assertEquals(result.get("font-weight"), "normal"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("font-style"), "normal"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("font-variant"), "small-caps"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("font-size"), "120%"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("line-height"), "120%"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("font-family"), "fantasy"); //$NON-NLS-1$//$NON-NLS-2$

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testBorderWidth() throws Exception {
		String style = "medium thin"; //$NON-NLS-1$
		PropertyParser parser = new PropertyParser(new StringReader(style));
		parser.parseBorderWidth();
		HashMap result = parser.getCssProperties();
		assertEquals(result.get("border-top-width"), "medium"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("border-right-width"), "thin"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("border-bottom-width"), "medium"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("border-left-width"), "thin"); //$NON-NLS-1$//$NON-NLS-2$

		style = "1em"; //$NON-NLS-1$
		parser.ReInit(new StringReader(style));
		parser.parsePadding();
		result = parser.getCssProperties();
		assertEquals(result.get("padding-top"), "1em"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("padding-right"), "1em"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("padding-bottom"), "1em"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(result.get("padding-left"), "1em"); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testwithException1() throws Exception {
		try {
			String style = "1p red double"; //$NON-NLS-1$
			PropertyParser parser = new PropertyParser(new StringReader(style));
			parser.parseBorder();
			fail();
		} catch (ParseException e) {
			assertNotNull(e);
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testFont1() throws Exception {
		// \"Bitstream Vera Sans\", Tahoma, Verdana, \"Myriad Web\", Syntax,
		// sans-serif 2em/120% italic small-caps bold

		String style = "italic small-caps  bold 2em/120% \"Bitstream Vera Sans\", Tahoma, Verdana, \"Myriad Web\", Syntax, sans-serif"; //$NON-NLS-1$
		PropertyParser parser = new PropertyParser(style);
		parser.parseFont();
		HashMap result = parser.getCssProperties();
		assertEquals("\"Bitstream Vera Sans\",Tahoma,Verdana,\"Myriad Web\",Syntax,sans-serif", //$NON-NLS-1$
				result.get("font-family")); //$NON-NLS-1$
	}
}
