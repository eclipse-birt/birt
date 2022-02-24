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

package org.eclipse.birt.report.model.api;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.ResourceLocatorImpl;

import com.ibm.icu.util.ULocale;

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

	private ResourceLocatorImpl rl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ULocale locale = new ULocale("en_US");//$NON-NLS-1$

		createDesign(locale);
		rl = new ResourceLocatorImpl();
	}

	/**
	 * Copies a bunch of design/library files to the temporary folder.
	 * 
	 * @param fileNames the design/library file names. The first item is the main
	 *                  design file.
	 * @return the file path of the design file
	 * @throws Exception
	 */

	private List dumpJarContentsToFile(List fileNames) throws Exception {
		List filePaths = new ArrayList();
		for (int i = 0; i < fileNames.size(); i++) {
			String resourceName = (String) fileNames.get(i);
			filePaths.add(copyContentToFile(resourceName));
		}

		return filePaths;
	}

	/**
	 * Tests the 'findResource' method of DefaultSearchFileAlgorithm.
	 * <P>
	 * for the file protocol and url protocol.
	 * 
	 * @throws Exception if the test fails.
	 */

	public void testFindFile() throws Exception {
		List fileNames = new ArrayList();
		fileNames.add(INPUT_FOLDER + "MasterPageHandleTest.xml"); //$NON-NLS-1$
		fileNames.add(GOLDEN_FOLDER + "CustomColorHandleTest_golden.xml"); //$NON-NLS-1$

		List diskFiles = dumpJarContentsToFile(fileNames);

		File f = new File((String) diskFiles.get(0));
		designHandle.getModule().setSystemId(f.getParentFile().toURL());

		URL url = rl.findResource(designHandle, "1.xml", IResourceLocator.IMAGE); //$NON-NLS-1$
		assertNull(url);

		url = rl.findResource(designHandle, "MasterPageHandleTest.xml", IResourceLocator.IMAGE); //$NON-NLS-1$
		assertNotNull(url);

		url = rl.findResource(designHandle, "1.xml", IResourceLocator.IMAGE); //$NON-NLS-1$
		assertNull(url);

		f = new File((String) diskFiles.get(1));
		designHandle.getModule().setSystemId(f.getParentFile().toURL());

		url = rl.findResource(designHandle, "CustomColorHandleTest_golden.xml", IResourceLocator.IMAGE); //$NON-NLS-1$
		assertNotNull(url);
		url = rl.findResource(designHandle, url.toString(), IResourceLocator.IMAGE);
		assertNotNull(url);
	}

	/**
	 * Finds the message file from default resource locator.
	 * 
	 * @throws Exception
	 */

	public void testFindMessageFiles() throws Exception {
		String testFile = "ResourceLocator"; //$NON-NLS-1$

		URL url = getResource(INPUT_FOLDER);
		designHandle.getModule().setSystemId(url);

		URL resource = designHandle.findResource(testFile, IResourceLocator.MESSAGE_FILE);
		String strResource = resource.toString();
		assertTrue(strResource.indexOf("en_US") != -1); //$NON-NLS-1$
	}

	/**
	 * Tests the 'findFile' method of DefaultSearchFileAlgorithm.
	 * 
	 * @throws Exception if the test fails.
	 */

	public void testFindResourceInJar() throws Exception {

		String jarPath = copyContentToFile(INPUT_FOLDER + "testRead.jar"); //$NON-NLS-1$

		String resource = "jar:file:" + jarPath + "!/test/testRead.rptdesign"; //$NON-NLS-1$ //$NON-NLS-2$

		URL url = rl.findResource(designHandle, resource, IResourceLocator.LIBRARY);
		assertNotNull(url);

		URLConnection jarConnection = url.openConnection();
		jarConnection.connect();

		InputStream inputStream = jarConnection.getInputStream();
		assertNotNull(inputStream);

		inputStream.close();
	}

	/**
	 * Tests search resources under fragments.
	 * 
	 * <ul>
	 * <li>
	 * <li>case 1:
	 * <li>open a report which used resource in fragments.
	 * </ul>
	 * 
	 * <ul>
	 * <li>case 2:
	 * <li>open a library with url in the form of "bundleresource://".
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testFindResourceInFragments() throws Exception {
		openDesign("SearchFragmentsTest.xml"); //$NON-NLS-1$

		assertNotNull(designHandle);
		LabelHandle labelFromLib = (LabelHandle) designHandle.findElement("labelFromLib"); //$NON-NLS-1$
		assertNotNull(labelFromLib);
		assertEquals("library text", labelFromLib.getDisplayText()); //$NON-NLS-1$

		LabelHandle externalizedLabel = (LabelHandle) designHandle.findElement("externalizedLabel"); //$NON-NLS-1$
		assertNotNull(externalizedLabel);
		assertEquals("label_localized", externalizedLabel.getDisplayText()); //$NON-NLS-1$

		ImageHandle image = (ImageHandle) designHandle.findElement("image"); //$NON-NLS-1$

		URL url = rl.findResource(designHandle, image.getURI(), IResourceLocator.IMAGE);
		assertEquals("bundleresource", url.getProtocol()); //$NON-NLS-1$
		assertEquals("/images/20063201445066811.gif", url.getPath()); //$NON-NLS-1$

		url = rl.findResource(designHandle, "libs/lib.rptlibrary", //$NON-NLS-1$
				IResourceLocator.LIBRARY);
		assertNotNull(url);
		assertNotNull(sessionHandle);
		libraryHandle = sessionHandle.openLibrary(url.toString());
		assertNotNull(libraryHandle);
	}

	/**
	 * 
	 * @throws Exception
	 */

	public void testResourceFolder() throws Exception {
		String testFile = "CustomColorHandleTest_golden.xml"; //$NON-NLS-1$
		URL resource = null;

		// set resource folder only in module

		designHandle.setResourceFolder(getResource(INPUT_FOLDER).toString());

		resource = rl.findResource(designHandle, testFile, 1);
		assertNull(resource);

		// set in the session

		sessionHandle.setResourceFolder(getResource(GOLDEN_FOLDER).toString());

		resource = rl.findResource(designHandle, testFile, 1);
		assertNotNull(resource);
		assertTrue(resource.toString().endsWith(testFile));

	}

	/**
	 * Finds the jar file from default resource locator.
	 * 
	 * @throws Exception
	 */

	public void testFindJarFiles() throws Exception {
		sessionHandle.setResourceFolder(getResource(INPUT_FOLDER).toString());

		String testFile = "Resourcelocator_test.jar"; //$NON-NLS-1$
		URL resource = rl.findResource(designHandle, testFile, IResourceLocator.JAR_FILE);
		assertNotNull(resource);

	}

	/**
	 * Tests handle mail protocol.
	 * 
	 * @throws Exception
	 */
	public void testMail() throws Exception {
		String mail = "mailto:dmurphy@classicmodelcars.com"; //$NON-NLS-1$
		URL resource = rl.findResource(designHandle, mail, -1);
		assertEquals(mail, resource.toString());
	}
}
