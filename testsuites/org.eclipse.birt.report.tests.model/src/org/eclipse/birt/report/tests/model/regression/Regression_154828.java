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

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * Item from library contant user properties can't delete
 * <p>
 * <b>Step:</b>
 * <ol>
 * <li>New a library.
 * <li>Add a label and add a user properites.
 * <li>Public the library.
 * <li>New a report design and use the library
 * <li>Drop the label from library explorer into layout.
 * <li>Try to delete it.
 * </ol>
 * <p>
 * <b>Actual result:</b>
 * <p>
 * The label from library contant user properties can't be delete.
 * <p>
 * <b>Exception reuslt:</b>
 * <p>
 * The label can be delete.
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Can delete extended label with user property
 */
public class Regression_154828 extends BaseTestCase {

	private String filename = "Regression_154828.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( filename , filename );
		copyInputToFile(INPUT_FOLDER + "/" + filename);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_154828() throws DesignFileException, SemanticException {
		openDesign(filename);
		libraryHandle = designHandle.getLibrary("lib"); //$NON-NLS-1$
		LabelHandle label = (LabelHandle) libraryHandle.findElement("label"); //$NON-NLS-1$
		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle newlabel = (LabelHandle) factory.newElementFrom(label, "newlabel"); //$NON-NLS-1$
		designHandle.getBody().add(newlabel);

		assertTrue(newlabel.canDrop());
		designHandle.getBody().drop(newlabel);
		assertEquals(0, designHandle.getBody().getContents().size());

	}
}
