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

package org.eclipse.birt.report.model.core;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.util.BaseTestCase;

public class ParameterNameTest extends BaseTestCase {

	private static final String fileName = "ParameterNameTest.xml"; //$NON-NLS-1$

	/**
	 * Tests the backward compatibility when the name of the parameter and parameter
	 * group is changed to be case insensitive since 3.2.21.
	 * 
	 * @throws Exception
	 */
	public void testCompatibility() throws Exception {
		openDesign(fileName);

		save();
		assertTrue(compareFile("ParameterNameTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testParser() throws Exception {
		try {
			openDesign("ParameterNameTest_1.xml"); //$NON-NLS-1$
		} catch (DesignFileException e) {
			List<ErrorDetail> errors = e.getErrorList();
			assertEquals(NameException.DESIGN_EXCEPTION_DUPLICATE, errors.get(0).getErrorCode());
		}

		openDesign(fileName);
		ElementFactory factory = designHandle.getElementFactory();

		DesignElementHandle paramHandle = factory.newScalarParameter("PARAM1"); //$NON-NLS-1$
		assertEquals("PARAM12", paramHandle.getName()); //$NON-NLS-1$

		paramHandle = factory.newParameterGroup("group"); //$NON-NLS-1$
		assertEquals("group", paramHandle.getName()); //$NON-NLS-1$
		paramHandle = factory.newParameterGroup("Group"); //$NON-NLS-1$
		assertEquals("Group1", paramHandle.getName()); //$NON-NLS-1$
	}
}
