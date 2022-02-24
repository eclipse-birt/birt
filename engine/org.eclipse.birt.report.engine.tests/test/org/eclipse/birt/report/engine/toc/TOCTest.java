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

package org.eclipse.birt.report.engine.toc;

import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.ITOCTree;

import com.ibm.icu.util.ULocale;

public class TOCTest extends TOCTestCase {

	static final String TOC_TEST_TOC = "org/eclipse/birt/report/engine/toc/TOCNodeTest.xml";
	static final String TOC_TEST_TOC_1 = "org/eclipse/birt/report/engine/toc/TOCNodeTest1.xml";
	static final String TOC_TEST_TOC_2 = "org/eclipse/birt/report/engine/toc/TOCNodeTest2.xml";
	static final String TOC_TEST_TOC_3 = "org/eclipse/birt/report/engine/toc/TOCNodeTest3.xml";
	static final String TOC_TEST_TOC_4 = "org/eclipse/birt/report/engine/toc/TOCNodeTest4.xml";

	static final String TOC_TEST_GOLDEN = "<toc nodeId=\"/\"/>";;

	static final String TOC_TEST_1_GOLDEN = "<toc nodeId=\"/\">" + "    <toc nodeId=\"__TOC_0\" displayText=\"leaf1\"/>"
			+ "    <toc nodeId=\"__TOC_1\" displayText=\"leaf2\"/>" + "</toc>";

	static final String TOC_TEST_2_GOLDEN = "<toc nodeId=\"/\">" + "    <toc nodeId=\"__TOC_0\" displayText=\"leaf2\">"
			+ "        <toc nodeId=\"__TOC_0_1\" displayText=\"leaf2\"/>"
			+ "        <toc nodeId=\"__TOC_0_2\" displayText=\"leaf3\"/>" + "    </toc>" + "</toc>";

	static final String TOC_TEST_3_GOLDEN = "<toc nodeId=\"/\"/>";

	static final String TOC_TEST_4_GOLDEN = "<toc nodeId=\"/\">" + "    <toc nodeId=\"__TOC_0\" displayText=\"leaf3\">"
			+ "        <toc nodeId=\"__TOC_0_1\" displayText=\"leaf3\"/>"
			+ "        <toc nodeId=\"__TOC_0_2\" displayText=\"leaf4\"/>" + "    </toc>" + "</toc>";

	public void testTOC() throws Exception {
		// doTest( TOC_TEST_TOC, TOC_TEST_GOLDEN );
		// doTest( TOC_TEST_TOC_1, TOC_TEST_1_GOLDEN );
		// doTest( TOC_TEST_TOC_2, TOC_TEST_2_GOLDEN );
		// doTest( TOC_TEST_TOC_3, TOC_TEST_3_GOLDEN );
		// doTest( TOC_TEST_TOC_4, TOC_TEST_4_GOLDEN );
	}

	protected void doTest(String design, String golden) throws Exception {
		copyResource(design, "utest/toc.rptdesign");
		createReportDocument("utest/toc.rptdesign", "utest/toc.rptdocument");
		IReportDocument document = engine.openReportDocument("utest/toc.rptdocument");
		try {
			ITOCTree tree = document.getTOCTree("html", ULocale.ENGLISH);
			String out = toString(tree.getRoot());
			assertEquals(golden.replaceAll("\\s", ""), out.replaceAll("\\s", ""));
		} finally {
			document.close();
		}
	}
}
