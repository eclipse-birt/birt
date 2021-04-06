/*******************************************************************************
 * Copyright (c) {DATE} Actuate Corporation {ADD OTHER COPYRIGHT OWNERS}.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  {ADD SUBSEQUENT AUTHOR & CONTRIBUTION}
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor.css;

import java.io.StringReader;
import java.util.HashMap;

import junit.framework.TestCase;

/**
 * Test case
 * 
 * 
 */
public class CssParserTest extends TestCase {

	public void testBackground() throws Exception {
		String style = "background: url(\"chess.png\") gray 50% repeat fixed ";
		CssParser parser = new CssParser(new StringReader(style));
		parser.parse();
		HashMap result = parser.getCssProperties();
		assertEquals(result.get("background-image"), "url(\"chess.png\")");
		assertEquals(result.get("background-color"), "gray");
		assertEquals(result.get("background-position"), "50%");
		assertEquals(result.get("background-repeat"), "repeat");
		assertEquals(result.get("background-attachment"), "fixed");
	}

	public void testFont() throws Exception {
		String style = "font: 12px/14px sans-serif ";
		CssParser parser = new CssParser(new StringReader(style));
		parser.parse();
		HashMap result = parser.getCssProperties();
		// assertEquals( result.get( "font-weight" ), "bold" );
		// assertEquals( result.get( "font-style" ), "italic" );
		assertEquals(result.get("font-size"), "12px");
		assertEquals(result.get("line-height"), "14px");
		assertEquals(result.get("font-family"), "sans-serif");

		parser.ReInit(new StringReader("font: 80% sans-serif "));
		parser.parse();
		result = parser.getCssProperties();
		// assertEquals( result.get( "font-weight" ), "bold" );
		// assertEquals( result.get( "font-style" ), "italic" );
		assertEquals(result.get("font-size"), "80%");
		assertEquals(result.get("font-family"), "sans-serif");

		parser.ReInit(new StringReader(" font: x-large/110% \"New Century Schoolbook\", serif "));
		parser.parse();
		result = parser.getCssProperties();
		assertEquals(result.get("font-size"), "x-large");
		assertEquals(result.get("line-height"), "110%");
		assertEquals(result.get("font-family"), "\"New Century Schoolbook\",serif");

		parser.ReInit(new StringReader("font: bold italic large Palatino, serif "));
		parser.parse();
		result = parser.getCssProperties();
		assertEquals(result.get("font-weight"), "bold");
		assertEquals(result.get("font-style"), "italic");
		assertEquals(result.get("font-size"), "large");
		assertEquals(result.get("font-family"), "Palatino,serif");

		parser.ReInit(new StringReader("font: normal small-caps 120%/120% fantasy "));
		parser.parse();
		result = parser.getCssProperties();
		assertEquals(result.get("font-weight"), "normal");
		assertEquals(result.get("font-style"), "normal");
		assertEquals(result.get("font-variant"), "small-caps");
		assertEquals(result.get("font-size"), "120%");
		assertEquals(result.get("line-height"), "120%");
		assertEquals(result.get("font-family"), "fantasy");

	}

	public void testFunctionAndHex() throws Exception {
		String style = "color:rgb(200,100,50)";
		CssParser parser = new CssParser(new StringReader(style));
		parser.parse();
		HashMap result = parser.getCssProperties();
		assertEquals(result.get("color"), "rgb(200,100,50)");

		parser.ReInit(new StringReader("color:#ffeedd"));
		parser.parse();
		result = parser.getCssProperties();
		assertEquals(result.get("color"), "#ffeedd");

	}

	public void testBorderWidth() throws Exception {
		String style = "border-width: medium thin";
		CssParser parser = new CssParser(new StringReader(style));
		parser.parse();
		HashMap result = parser.getCssProperties();
		assertEquals(result.get("border-top-width"), "medium");
		assertEquals(result.get("border-right-width"), "thin");
		assertEquals(result.get("border-bottom-width"), "medium");
		assertEquals(result.get("border-left-width"), "thin");
	}

	public void testwithException1() throws Exception {
		String style = "border:1p red double";
		CssParser parser = new CssParser(new StringReader(style));
		parser.parse();
		HashMap result = parser.getCssProperties();
		assertTrue(result.isEmpty());
	}

	public void testwithException() throws Exception {
		String style = "border:1p red double;display:inline;";
		CssParser parser = new CssParser(new StringReader(style));
		parser.parse();
		HashMap result = parser.getCssProperties();
		assertEquals(1, result.size());
		assertEquals("inline", result.get("display"));
	}
}