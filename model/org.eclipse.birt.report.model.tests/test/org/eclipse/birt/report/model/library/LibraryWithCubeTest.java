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

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests cube functionality in the library and report design.
 */

public class LibraryWithCubeTest extends BaseTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests load cube extended from library.
	 *
	 * @throws Exception
	 */

	public void testLoadCube() throws Exception {
		openDesign("LibraryWithCubeTest.xml"); //$NON-NLS-1$
		CubeHandle cubeHandle = (CubeHandle) designHandle.getCubes().get(0);
		assertNotNull(cubeHandle.getMeasure("QUANTITY_PRICE"));//$NON-NLS-1$
		assertNotNull(cubeHandle.getMeasure("QUANTITY"));//$NON-NLS-1$
	}

	/**
	 * Tests write extended cube.
	 *
	 * @throws Exception
	 */
	public void writeExtendedCube() throws Exception {
		openLibrary("LibraryWithCubeTest_Lib.xml");//$NON-NLS-1$
		CubeHandle cubeHandle = (CubeHandle) libraryHandle.getAllCubes().get(0);

		openDesign("BlankLibraryWithCubeTest.xml");//$NON-NLS-1$

		CubeHandle newCubeHandle = (CubeHandle) designHandle.getElementFactory().newElementFrom(cubeHandle,
				"extenedCube");//$NON-NLS-1$
		designHandle.getCubes().add(newCubeHandle);

		save();
		assertTrue(compareFile("LibraryWithCubeTest.xml"));//$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */

	public void resolveLibraryOLAP() throws Exception {
		openDesign("LibraryWithCubeTest1.xml");//$NON-NLS-1$

		ExtendedItemHandle tmpElement = (ExtendedItemHandle) designHandle.findElement("NewDimension View11"); //$NON-NLS-1$
		ElementRefValue refValue = (ElementRefValue) tmpElement.getElement().getProperty(design, "dimension"); //$NON-NLS-1$
		assertTrue(refValue.isResolved());
		assertNotNull(refValue.getLibraryNamespace());

		tmpElement = (ExtendedItemHandle) designHandle.findElement("NewLevel View11"); //$NON-NLS-1$
		refValue = (ElementRefValue) tmpElement.getElement().getProperty(design, "dimension"); //$NON-NLS-1$
		assertTrue(refValue.isResolved());
		assertNotNull(refValue.getLibraryNamespace());

	}
}
