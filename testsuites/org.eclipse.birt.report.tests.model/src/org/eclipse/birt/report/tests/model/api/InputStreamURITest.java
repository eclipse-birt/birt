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
package org.eclipse.birt.report.tests.model.api;

import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.util.ResourceLocatorImpl;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * TestCases for ResourceLocator
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * 
 * <tr>
 * <td>{@link #testImportLibrary()}</td>
 * </tr>
 * </table>
 * 
 */
public class InputStreamURITest extends BaseTestCase {
	private final String fileName = "inputStream_uri_Test.xml";
	private ResourceLocatorImpl rl;

	public InputStreamURITest(String name) {
		super(name);
	}

	public static Test suite() {

		return new TestSuite(InputStreamURITest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + fileName);
		copyInputToFile(INPUT_FOLDER + "/" + "Library_Import_Test.xml");

		openDesign(fileName);
		rl = new ResourceLocatorImpl();
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * Test ResourceLocator findResource
	 * 
	 * @throws Exception
	 */
	public void testImportLibrary() throws Exception {

		URL url = rl.findResource(designHandle, "1.xml", IResourceLocator.IMAGE);
		assertNull(url);

		url = rl.findResource(designHandle, "1.xml", IResourceLocator.LIBRARY);
		assertNull(url);

		url = rl.findResource(designHandle, "Library_Import_Test.xml", IResourceLocator.LIBRARY);
		assertNotNull(url);

		designHandle.setFileName(getTempFolder() + "/" + GOLDEN_FOLDER + "/");
		url = rl.findResource(designHandle, "1_golden.xml", IResourceLocator.IMAGE);
		assertNull(url);

		// designHandle.setFileName( getClassFolder( ) +"/golden/" );
		designHandle.setFileName(getTempFolder() + "/" + GOLDEN_FOLDER + "/");
		url = rl.findResource(designHandle, "LibraryCreatLib.xml", IResourceLocator.IMAGE);
		assertNull(url);

		url = rl.findResource(designHandle, "http://www.actuate.com/logo.gif", IResourceLocator.IMAGE);
		assertNotNull(url);

	}
}
