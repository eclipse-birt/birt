/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class SvgFileTest {

	@Test
	public void testIsSvgByExtension() {
		assertTrue(SvgFile.isSvg(null, null, ".svg"));
		assertTrue(SvgFile.isSvg(null, null, ".SVG"));
		assertTrue(SvgFile.isSvg(null, null, ".SvG"));
	}

	@Test
	public void testIsSvgByMimeType() {
		assertTrue(SvgFile.isSvg("image/svg+xml", null, null));
		assertTrue(SvgFile.isSvg("IMAGE/SVG+XML", null, null));
	}

	@Test
	public void testIsSvgByUriExtension() {
		assertTrue(SvgFile.isSvg(null, "http://example.com/image.svg", null));
		assertTrue(SvgFile.isSvg(null, "/path/to/file.SVG", null));
	}

	@Test
	public void testIsSvgByUriMimeType() {
		assertTrue(SvgFile.isSvg(null, "data:image/svg+xml;base64,PHN2Zz4=", null));
	}

	@Test
	public void testIsSvgByContent() {
		String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\"><rect/></svg>";
		byte[] svgData = svg.getBytes(StandardCharsets.UTF_8);
		assertTrue(SvgFile.isSvg(null, null, null, svgData));
	}

	@Test
	public void testIsSvgByContentWithXmlDeclaration() {
		String svg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns=\"http://www.w3.org/2000/svg\"><rect/></svg>";
		byte[] svgData = svg.getBytes(StandardCharsets.UTF_8);
		assertTrue(SvgFile.isSvg(null, null, null, svgData));
	}

	@Test
	public void testIsSvgByContentWithDoctype() {
		String svg = "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"><svg xmlns=\"http://www.w3.org/2000/svg\"><rect/></svg>";
		byte[] svgData = svg.getBytes(StandardCharsets.UTF_8);
		assertTrue(SvgFile.isSvg(null, null, null, svgData));
	}

	@Test
	public void testIsSvgByContentWithXmlAndDoctype() {
		String svg = "<?xml version=\"1.0\"?>\n<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"\">\n<svg xmlns=\"http://www.w3.org/2000/svg\"><rect/></svg>";
		byte[] svgData = svg.getBytes(StandardCharsets.UTF_8);
		assertTrue(SvgFile.isSvg(null, null, null, svgData));
	}

	@Test
	public void testIsSvgByContentWithXmlAndDoctypeInUtf16() {
		String svg = """
				<?xml version="1.0" encoding="UTF-16"?>
				<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "">
				<svg xmlns="http://www.w3.org/2000/svg">
				  <rect/>
				</svg>
				""";
		byte[] svgData = svg.getBytes(StandardCharsets.UTF_16);
		assertTrue(SvgFile.isSvg(null, null, null, svgData));
	}

	@Test
	public void testIsSvgByContentWithXmlAndDoctypeAndComment() {
		String svg = """
				<?xml version="1.0"?>
				<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "">
				<!-- Test comment -->
				
				<svg xmlns="http://www.w3.org/2000/svg">
					<rect/>
				</svg>
				""";
		byte[] svgData = svg.getBytes(StandardCharsets.UTF_8);
		assertTrue(SvgFile.isSvg(null, null, null, svgData));
	}

	@Test
	public void testIsSvgByContentWithLeadingWhitespace() {
		String svg = "   \n  <svg xmlns=\"http://www.w3.org/2000/svg\"><rect/></svg>";
		byte[] svgData = svg.getBytes(StandardCharsets.UTF_8);
		assertTrue(SvgFile.isSvg(null, null, null, svgData));
	}

	@Test
	public void testIsSvgByContentCaseInsensitive() {
		assertTrue(SvgFile.isSvg(null, null, null,
				"<SVG xmlns=\"http://www.w3.org/2000/svg\"><rect/></SVG>".getBytes(StandardCharsets.UTF_8)));
		assertTrue(SvgFile.isSvg(null, null, null,
				"<Svg xmlns=\"http://www.w3.org/2000/svg\"><rect/></Svg>".getBytes(StandardCharsets.UTF_8)));
	}

	@Test
	public void testIsSvgByContentNotSvg() {
		String png = "\211PNG\r\n\032\n";
		assertFalse(SvgFile.isSvg(null, null, null, png.getBytes(StandardCharsets.UTF_8)));
	}

	@Test
	public void testIsNotSvgNonXmlContent() {
		String jpeg = "\377\330\377";
		assertFalse(SvgFile.isSvg(null, null, null, jpeg.getBytes(StandardCharsets.UTF_8)));
	}

	@Test
	public void testIsNotSvgRandomXml() {
		String xml = "<?xml version=\"1.0\"?><data><value>test</value></data>";
		assertFalse(SvgFile.isSvg(null, null, null, xml.getBytes(StandardCharsets.UTF_8)));
	}

	@Test
	public void testIsNotSvgNullData() {
		assertFalse(SvgFile.isSvg(null, null, null, null));
	}

	@Test
	public void testIsNotSvgEmptyData() {
		assertFalse(SvgFile.isSvg(null, null, null, new byte[0]));
	}

	@Test
	public void testIsNotSvgTooShortData() {
		assertFalse(SvgFile.isSvg(null, null, null, "abc".getBytes(StandardCharsets.UTF_8)));
	}

	@Test
	public void testIsSvgByContentNotDetectedWhenNoneOfTheOthers() {
		String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\"><circle cx=\"50\" cy=\"50\" r=\"40\"/></svg>";
		byte[] svgData = svg.getBytes(StandardCharsets.UTF_8);
		assertTrue(SvgFile.isSvg(null, null, ".png", svgData));
	}
}
