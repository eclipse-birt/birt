/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
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
package org.eclipse.birt.report.tests.model.compatibility;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Test Case for Report Compatibility.
 * 
 * 
 */
public class CompatibilityTest extends BaseTestCase {

	String fileName = "DynamicTextExampleAfter_2.0.1.xml";
	String fileName1 = "DynamicTextExampleBefore_2.0.1.xml";
	String fileName2 = "GroupingExampleAfter_2.0.1.xml";
	String fileName3 = "GroupingExampleBefore_2.0.1.xml";
	String fileName4 = "HighlightingExampleAfter_2.0.1.xml";
	String fileName5 = "HighlightingExampleBefore_2.0.1.xml";
	String fileName6 = "HyperlinkingExampleBefore_2.0.1.xml";
	String fileName7 = "ImageExample_2.0.1.xml";
	String fileName8 = "ListingExampleAfter_2.0.1.xml";
	String fileName9 = "ListingExampleBefore_2.0.1.xml";
	String fileName10 = "MappingExampleAfter_2.0.1.xml";
	String fileName11 = "MappingExampleBefore_2.0.1.xml";
	String fileName12 = "ParallelReportExampleAfter_2.0.1.xml";
	String fileName13 = "ParametersExampleAfter_2.0.1.xml";
	String fileName14 = "ParametersExampleBefore_2.0.1.xml";
	String fileName15 = "ProductCatalog_2.0.1.xml";
	String fileName16 = "SalesInvoice_2.0.1.xml";
	String fileName17 = "SalesOfAproduct_2.0.1.xml";
	String fileName18 = "SubReportsExampleMainAfter_2.0.1.xml";
	String fileName19 = "SubReportsExampleMainBefore_2.0.1.xml";
	String fileName20 = "TopNPercent_2.0.1.xml";
	String fileName21 = "TopSellingProducts_2.0.1.xml";

	public CompatibilityTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static Test suite() {

		return new TestSuite(CompatibilityTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

	}

	/**
	 * Test compatibility by openning reports that contain variant features and
	 * designed in variant release versions
	 * 
	 * @throws DesignFileException
	 */
	public void testOpenFile() throws DesignFileException {
		openDesign(fileName);
		openDesign(fileName1);
		openDesign(fileName2);
		openDesign(fileName3);
		openDesign(fileName4);
		openDesign(fileName5);
		openDesign(fileName6);
		openDesign(fileName7);
		openDesign(fileName8);
		openDesign(fileName9);
		openDesign(fileName10);
		openDesign(fileName11);
		openDesign(fileName12);
		openDesign(fileName13);
		openDesign(fileName14);
		openDesign(fileName15);
		openDesign(fileName16);
		openDesign(fileName17);
		openDesign(fileName18);
		openDesign(fileName19);
		openDesign(fileName20);
		openDesign(fileName21);
	}

}
