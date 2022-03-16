/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.dom;

import org.eclipse.birt.report.engine.css.engine.BIRTCSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;

import junit.framework.TestCase;

public class StyleDeclarationTest extends TestCase {

	public void testCssText() {
		CSSEngine engine = new BIRTCSSEngine();
		StyleDeclaration style = new StyleDeclaration(engine);
		style.setNumberFormat("General Number");
		style.setFontFamily("'Arial New', 'Courier'");
		style.setFontWeight("bold");
		String cssText = style.getCssText();
		StyleDeclaration style1 = new StyleDeclaration(engine);
		style1.setCssText(cssText);
		String cssText1 = style1.getCssText();
		assertEquals(cssText, cssText1);
	}

	public void testFontFamilyCssText() {
		CSSEngine engine = new BIRTCSSEngine();
		StyleDeclaration style = new StyleDeclaration(engine);
		style.setFontFamily("\"Arial\",Courier New,\"Franklin Gothic Book\",'ABC{!}\"DEF',sans-serif");
		String golden = "Arial,\"Courier New\",\"Franklin Gothic Book\",'ABC{!}\"DEF',sans-serif";
		String output = style.getFontFamily();
		assertEquals(golden, output);
	}
}
