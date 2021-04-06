/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GroupElementFactory;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * Name is not necessary for extended element
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>New a label named "label" in library
 * <li>New a report, extend lib.label
 * </ol>
 * <p>
 * <b>Expected result:</b>
 * <p>
 * No name for the extended label
 * <p>
 * <b>Actual result:</b>
 * <p>
 * "label" is added as the name of label in report. It's not reasonable because
 * "Restore Properties" is enabled even without properties change.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Test the logic on GroupElmentHandle::hasLocalPropertiesForExtendedElements( )
 * <p>
 */
public class Regression_146481 extends BaseTestCase {

	private final static String REPORT = "regression_146481.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( REPORT , REPORT );
		// copyResource_INPUT( "regression_146481_lib" , "regression_146481_lib" );
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_146481() throws DesignFileException, SemanticException {
		openDesign(REPORT);
		LibraryHandle lib = designHandle.getLibrary("regression_146481_lib"); //$NON-NLS-1$
		LabelHandle label = (LabelHandle) lib.findElement("NewLabel"); //$NON-NLS-1$

		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle newLabel = (LabelHandle) factory.newElementFrom(label, "label1"); //$NON-NLS-1$

		designHandle.getBody().add(newLabel);

		LabelHandle label1 = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$
		List elements = new ArrayList();
		elements.add(label1);

		GroupElementHandle groupElementHandle = GroupElementFactory.newGroupElement(designHandle, elements);

		assertFalse(groupElementHandle.hasLocalPropertiesForExtendedElements());

		// change the name

		label1.setName("aa"); //$NON-NLS-1$

		assertFalse(groupElementHandle.hasLocalPropertiesForExtendedElements());
	}
}
