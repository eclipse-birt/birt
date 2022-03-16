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

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;;

/**
 * TestCases for add library.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 *
 * <tr>
 * <td>{@link #testAddinLibrary()}</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testRemoveLibrary()}</td>
 * </tr>
 * </table>
 *
 */
public class LibraryAddTest extends BaseTestCase {
	private String fileName = "Library_Addin_Test.xml";
	private String LibImpFile = "Library_Import_Test.xml";
	private String inputLibraryName = "LibraryCreatLib.xml";
	private String libname = "LibA.xml";
	private String outFileName = "Library_Addin_Test_out.xml";
	private String goldenFileName = "Library_Addin_Test_golden.xml";
	String LibFile = inputLibraryName;
	String LibFileError1 = this.getTempFolder() + "/" + INPUT_FOLDER + "/" + "LibY.xml";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + fileName);
		copyInputToFile(INPUT_FOLDER + "/" + inputLibraryName);
		copyInputToFile(INPUT_FOLDER + "/" + libname);
		copyInputToFile(INPUT_FOLDER + "/" + LibImpFile);

		copyGoldenToFile(GOLDEN_FOLDER + "/" + goldenFileName);

	}

	@Override
	public void tearDown() {
		removeResource();
	}

	public LibraryAddTest(String name) {
		super(name);
	}

	public static Test suite() {

		return new TestSuite(LibraryAddTest.class);
	}

	/**
	 * Test add normal and invalid library
	 *
	 * @throws Exception
	 */
	public void testAddinLibrary() throws Exception {
		openDesign(LibImpFile);
		designHandle.includeLibrary(LibFile, "LibB");
		designHandle.includeLibrary(libname, "");
		// saveAs( outFileName );
		// assertTrue( compareTextFile( goldenFileName, outFileName ) );
		String TempFile = this.genOutputFile(outFileName);
		designHandle.saveAs(TempFile);
		assertTrue(compareTextFile(goldenFileName, outFileName));

		try {
			designHandle.includeLibrary(LibFileError1, "LibY");
		} catch (Exception e) {
			assertNotNull(e);
		}

		try {
			designHandle.includeLibrary("../inputLibZ.xml", "LibZ");
		} catch (Exception e) {
			assertNotNull(e);
		}

	}

	/**
	 * Test remove included library.
	 *
	 * @throws Exception
	 */
	public void testRemoveLibrary() throws Exception {
		openDesign(LibImpFile);
		designHandle.includeLibrary(LibFile, "LibB");
		designHandle.includeLibrary("../input/LibA.xml", "");
		LibraryHandle lib1 = designHandle.findLibrary("LibraryCreatLib.xml");
		LibraryHandle lib2 = designHandle.findLibrary("LibA.xml");
		assertNotNull(lib1);
		assertNotNull(lib2);

		designHandle.dropLibrary(lib1);
		assertEquals(1, designHandle.getListProperty(ReportDesign.LIBRARIES_PROP).size());

		designHandle.dropLibrary(lib2);
		assertNull(designHandle.getListProperty(ReportDesign.LIBRARIES_PROP));

	}
}
