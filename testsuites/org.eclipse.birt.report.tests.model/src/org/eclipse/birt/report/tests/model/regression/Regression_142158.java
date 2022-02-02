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

package org.eclipse.birt.report.tests.model.regression;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Steps to reproduce:
 * <ol>
 * <li>New a library, add a sample data source and data set with table
 * "PRODUCTLINES"
 * <li>Add a dynamic image, add a column binding on it "a=dataSetRow["IMAGE"]"
 * <li>New a report, extends lib.image. In the pops up image dialog, click "OK"
 * <li>Save the report, select the image, open Property Editor->Binding
 * </ol>
 * <b>Expected result:</b>
 * </p>
 * Column binding on the extended image is "a=dataSetRow["IMAGE"]"
 * </p>
 * <b>Actual result:</b>
 * </p>
 * Column binding on the extended image is "a=dataSetRow["a"]"
 * </p>
 * Test description:
 * <p>
 * Extends a lib.dyamicimage, check its expression name
 * </p>
 */
public class Regression_142158 extends BaseTestCase {

	private String filename = "Regression_142158.xml"; //$NON-NLS-1$
	private String libraryname = "Regression_142158_lib.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		// copyResource_INPUT( filename , filename );
		// copyResource_INPUT( libraryname , libraryname );

		copyInputToFile(INPUT_FOLDER + "/" + filename);
		copyInputToFile(INPUT_FOLDER + "/" + libraryname);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_142158() throws DesignFileException, SemanticException {
		openLibrary(libraryname, true);
		ImageHandle libImage = (ImageHandle) libraryHandle.findElement("NewImage"); //$NON-NLS-1$

		openDesign(filename);
		designHandle.includeLibrary(libraryname, "Lib"); //$NON-NLS-1$
		ImageHandle image = (ImageHandle) designHandle.getElementFactory().newElementFrom(libImage, "image"); //$NON-NLS-1$
		designHandle.getBody().add(image);
		List list = image.getListProperty(ImageItem.BOUND_DATA_COLUMNS_PROP);
		ComputedColumn boundcolumn = (ComputedColumn) list.get(0);
		assertEquals("dataSetRow[\"IMAGE\"]", boundcolumn.getExpression()); //$NON-NLS-1$

	}
}
