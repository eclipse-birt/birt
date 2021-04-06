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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.elements.ScriptDataSet;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The test case of <code>ScriptDataSet</code> parser and writer.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testParser()}</td>
 * <td>Test properties of ScriptDataSet after parsing design file</td>
 * <td>All properties are right</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testWriter()}</td>
 * <td>Set all properties and compare the written file with the golden file</td>
 * <td>Two files are same</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSemanticCheck()}</td>
 * <td>Test invalid data source reference error</td>
 * <td>Error found</td>
 * </tr>
 * </table>
 * 
 * @see ScriptDataSet
 */

public class ScriptDataSetParseTest extends BaseTestCase {

	String fileName = "ScriptDataSetTest.xml"; //$NON-NLS-1$
	String outFileName = "ScriptDataSetTest_out.xml"; //$NON-NLS-1$
	String goldenFileName = "ScriptDataSetTest_golden.xml"; //$NON-NLS-1$
	String semanticCheckFileName = "ScriptDataSetTest_1.xml"; //$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * This test reads the design file, and checks the properties and style
	 * properties of line.
	 * 
	 * @throws Exception if any exception.
	 */
	public void testParser() throws Exception {
		ScriptDataSetHandle dataSet = getDataSet();

		// Test ScriptDataSet property

		assertEquals("open script", dataSet //$NON-NLS-1$
				.getOpen());
		assertEquals("describe script", dataSet //$NON-NLS-1$
				.getDescribe());
		assertEquals("fetch script", dataSet //$NON-NLS-1$
				.getFetch());
		assertEquals("close script", dataSet //$NON-NLS-1$
				.getClose());

	}

	/**
	 * This test sets properties, writes the design file and compares it with golden
	 * file.
	 * 
	 * @throws Exception if any exception.
	 */
	public void testWriter() throws Exception {
		ScriptDataSetHandle dataSet = getDataSet();

		// Change JdbcSelectDataSet property

		dataSet.setOpen("New open script"); //$NON-NLS-1$
		dataSet.setDescribe("New describe script"); //$NON-NLS-1$
		dataSet.setFetch("New fetch script"); //$NON-NLS-1$
		dataSet.setClose("New close script"); //$NON-NLS-1$

		save();
		assertTrue(compareFile(goldenFileName));
	}

	/**
	 * Test semantic errors.
	 * 
	 * @throws Exception if any exception.
	 */
	public void testSemanticCheck() throws Exception {
		openDesign(semanticCheckFileName);
		assertEquals(0, design.getErrorList().size());
	}

	/**
	 * Returns the data set for testing.
	 * 
	 * @return the data set for testing.
	 * @throws Exception if any exception.
	 */
	private ScriptDataSetHandle getDataSet() throws Exception {
		openDesign(fileName);

		ScriptDataSetHandle dataSet = (ScriptDataSetHandle) designHandle.findDataSet("firstDataSet"); //$NON-NLS-1$
		assertNotNull(dataSet);

		return dataSet;
	}
}