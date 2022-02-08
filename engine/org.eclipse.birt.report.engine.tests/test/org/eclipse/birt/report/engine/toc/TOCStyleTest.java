/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.toc;

import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.ITOCTree;

import com.ibm.icu.util.ULocale;

/**
 * 
 */

public class TOCStyleTest extends TOCTestCase {

	static final String TOC_DESIGN = "org/eclipse/birt/report/engine/toc/tocStyle.xml";

	static final String TOC_STYLE_GOLDEN = "<toc nodeId=\"/\">"
			+ "    <toc nodeId=\"__TOC_0\" displayText=\"2.4512E02\"/>"
			+ "    <toc nodeId=\"__TOC_1\" displayText=\"LOWER CASE TOC\"/>"
			+ "    <toc nodeId=\"__TOC_2\" displayText=\"bold font\"/>" + "</toc>";

	public void testStyle() {
		try {
			copyResource(TOC_DESIGN, "utest/toc.rptdesign");
			createReportDocument("utest/toc.rptdesign", "utest/toc.rptdocument");
			IReportDocument document = engine.openReportDocument("utest/toc.rptdocument");
			try {
				ITOCTree tree = document.getTOCTree("html", ULocale.ENGLISH);
				String out = toString(tree.getRoot());
				assertEquals(TOC_STYLE_GOLDEN.replaceAll("\\s", ""), out.replaceAll("\\s", ""));
			} finally {
				document.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
		;
	}
}
