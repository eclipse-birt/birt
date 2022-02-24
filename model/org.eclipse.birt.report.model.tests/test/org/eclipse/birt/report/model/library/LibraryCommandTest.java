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

package org.eclipse.birt.report.model.library;

import java.util.List;

import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * @author Administrator
 * 
 */
public class LibraryCommandTest extends BaseTestCase {

	/**
	 * Tests droping a library with different namespace but same source library file
	 * name of the already included library in report design
	 * 
	 * @throws Exception if any exception
	 */

	public void testDropTheSecondTimeIncludedLibrary() throws Exception {
		openDesign("DesignWithOneLibrary.xml"); //$NON-NLS-1$

		designHandle.includeLibrary("LibraryToBeDrop.xml", "stay"); //$NON-NLS-2$ //$NON-NLS-1$
		designHandle.includeLibrary("LibraryToBeDrop_1.xml", "delete"); //$NON-NLS-2$ //$NON-NLS-1$
		LibraryHandle secLibHandle = designHandle.getLibrary("delete"); //$NON-NLS-1$

		designHandle.dropLibrary(secLibHandle);
		assertTrue(designHandle.getLibrary("stay") != null); //$NON-NLS-1$
	}

	/**
	 * Tests a library recursively include library, including the case a library
	 * include itself.
	 * 
	 * @throws DesignFileException
	 * 
	 * @throws Exception           if any exception
	 */

	public void testLibraryRecursivelyIncludeLibrary() throws DesignFileException {
		openLibrary("LibraryIncludingTwoLibraries.xml"); //$NON-NLS-1$

		// test library include itself

		try {
			libraryHandle.includeLibrary("LibraryIncludingTwoLibraries.xml", null); //$NON-NLS-1$
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}

		// test two library recursively include.

		libraryHandle = null;
		openLibrary("Library_1.xml"); //$NON-NLS-1$
		assertNotNull(libraryHandle);
		try {
			libraryHandle.includeLibrary("LibraryIncludingTwoLibraries.xml", null); //$NON-NLS-1$
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	/**
	 * test when the library elements is used in report, exception will throw out
	 * before the drop operation.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testExceptionWhenDropUsedLibrary() throws DesignFileException, SemanticException {

		openDesign("DesignWithOneLibrary.xml"); //$NON-NLS-1$

		LibraryHandle lib = designHandle.getLibrary("Lib1"); //$NON-NLS-1$
		DataSourceHandle libDataSource = lib.findDataSource("dataSource1"); //$NON-NLS-1$
		DataSourceHandle designDataSource = (DataSourceHandle) designHandle.getElementFactory()
				.newElementFrom(libDataSource, "designDataSource"); //$NON-NLS-1$

		designHandle.getDataSources().add(designDataSource);

		// can not be dropped because the library datasource is referenced by
		// the report.
		try {
			designHandle.dropLibrary(lib);
			fail();
		} catch (Exception e) {
			assertEquals(LibraryException.DESIGN_EXCEPTION_LIBRARY_HAS_DESCENDENTS,
					((LibraryException) e).getErrorCode());
		}
		// remove the library datasource link
		designHandle.getDataSources().drop(designDataSource);

		// should can be drop properly
		try {
			designHandle.dropLibrary(lib);

		} catch (Exception e) {
			fail();
		}

		// include the library
		designHandle.includeLibrary("Library_1.xml", "Lib1"); //$NON-NLS-1$//$NON-NLS-2$
		lib = designHandle.getLibrary("Lib1"); //$NON-NLS-1$

		ParameterHandle parameter = lib.findParameter("para"); //$NON-NLS-1$
		assertNotNull(parameter);
		// add the library parameter reference in the report
		ParameterHandle designParameter = (ParameterHandle) designHandle.getElementFactory().newElementFrom(parameter,
				"designPara"); //$NON-NLS-1$

		designHandle.getParameters().add(designParameter);
		// can not be dropped
		try {
			designHandle.dropLibrary(lib);
			fail();
		} catch (Exception e) {
			assertEquals(LibraryException.DESIGN_EXCEPTION_LIBRARY_HAS_DESCENDENTS,
					((LibraryException) e).getErrorCode());
		}
	}

	/**
	 * Tests dropping library with relative path.
	 * 
	 * @throws Exception
	 */

	public void testDropLibraryWithRelativePath() throws Exception {
		openDesign("DesignCopyPaste.xml"); //$NON-NLS-1$

		designHandle.includeLibrary("../golden/LibraryParseTest_golden.xml", "testDrop"); //$NON-NLS-1$//$NON-NLS-2$
		designHandle.dropLibrary(designHandle.getLibrary("testDrop")); //$NON-NLS-1$

		assertNull(designHandle.getLibrary("testDrop")); //$NON-NLS-1$
		assertNull(design.findIncludedLibrary("testDrop")); //$NON-NLS-1$
	}

	/**
	 * Tests the library add command. Test the management of the name space and
	 * id-map about the compound element with inheritance.
	 * 
	 * @throws Exception
	 */

	public void testNameSpaceWithAddLibrary() throws Exception {
		openDesign("DesignToAddLibrary.xml"); //$NON-NLS-1$

		// first, there are a table and grid that define inheritance, table is
		// resolved and grid is not and table has four children have names

		NameSpace ns = design.getNameHelper().getNameSpace(ReportDesign.ELEMENT_NAME_SPACE);
		List elements = ns.getElements();
		TableItem designTable = (TableItem) ns.getElement("designTable"); //$NON-NLS-1$
		GridItem designGrid = (GridItem) ns.getElement("designGrid"); //$NON-NLS-1$
		assertEquals(6, elements.size());
		assertTrue(elements.contains(designTable));
		assertTrue(elements.contains(designGrid));
		for (int i = 1; i < 5; i++)
			assertTrue(((DesignElement) elements.get(i)).isContentOf(designTable));

		// add a library and let the grid to be resolved, and one child of grid
		// has name
		designHandle.includeLibrary("Library_1.xml", "lib2"); //$NON-NLS-1$//$NON-NLS-2$
		assertNotNull(designGrid.getExtendsElement());
		elements = ns.getElements();
		assertEquals(7, elements.size());
		assertTrue(((DesignElement) elements.get(6)).isContentOf(designGrid));
	}
}
