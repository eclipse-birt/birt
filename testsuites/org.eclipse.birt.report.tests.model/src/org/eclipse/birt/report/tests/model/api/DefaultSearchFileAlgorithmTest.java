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

package org.eclipse.birt.report.tests.model.api;

import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.ResourceLocatorImpl;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Test <code>DefaultSearchFileAlgorithm</code>
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>testFindFile</td>
 * <td>Get a <code>ReportDesign</code> instance, then find another file which
 * locates in the 'base' folder of the design.</td>
 * <td>If the file exists in the 'base' folder, returns the absolute path of
 * this file. If not, returns null.</td>
 * </tr>
 * 
 * </table>
 * 
 */
public class DefaultSearchFileAlgorithmTest extends BaseTestCase {

	public static Test suite() {

		return new TestSuite(DefaultSearchFileAlgorithmTest.class);
	}

	public DefaultSearchFileAlgorithmTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	final static String INPUT = "DefaultSearchFileAlgorithm.xml";
	// private final String fileName = "SimpleMasterPageHandleTest.xml";
	// //$NON-NLS-1$
	private ResourceLocatorImpl algorithm;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve input file(s) from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);

		openDesign(INPUT);
		algorithm = new ResourceLocatorImpl();
	}

	/**
	 * Tests the 'findFile' method of DefaultSearchFileAlgorithm.
	 * 
	 * @throws Exception if the test fails.
	 */

	public void testFindFile() throws Exception {
		URL url = algorithm.findResource(designHandle, "1.xml", IResourceLocator.IMAGE); //$NON-NLS-1$
		assertNull(url);

		url = algorithm.findResource(designHandle, INPUT, IResourceLocator.IMAGE); // $NON-NLS-1$
		assertNotNull(url);

		designHandle.setStringProperty(ReportDesign.BASE_PROP,
				PLUGIN_PATH + this.getFullQualifiedClassName() + GOLDEN_FOLDER);
		url = algorithm.findResource(designHandle, "1.xml", IResourceLocator.IMAGE); //$NON-NLS-1$
		assertNull(url);
	}
}