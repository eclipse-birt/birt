/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * 73708: text can't display some special character such as ',"
 * </p>
 * 73771: no error for negative value with label/text width
 * </p>
 * Test description:
 * </p>
 * 73708: set '," as text value
 * </p>
 * 73771: check error for negative value with label/text width
 */

public class Regression_73708and73771 extends BaseTestCase {

	private String filename = "Regression_73708and73771.xml"; //$NON-NLS-1$

	/**
	 * SpecialCharacter
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);

	}

	public void test_regression_73708() throws DesignFileException, SemanticException {
		openDesign(filename);
		TextItemHandle text = (TextItemHandle) designHandle.findElement("text"); //$NON-NLS-1$
		// set special format content to text
		text.setContent("Contents" //$NON-NLS-1$
				+ "1. INTRODUCTION    3"//$NON-NLS-1$
				+ "2. MINOR ENHANCEMENTS    3"//$NON-NLS-1$
				+ "2.1 PROVIDE PROPERTY GROUP INFORMATION ON ELEMETNDEFN CLASS    3"//$NON-NLS-1$
				+ "2.2 COMPLETE THE TODO TASKS    3"//$NON-NLS-1$
				+ "2.3 REVIEW RESULT    3"//$NON-NLS-1$
				+ "2.4 MESSAGE FILE ENHANCEMENT    4"//$NON-NLS-1$
				+ "CURRENTLY WE STORE THE ERROR MESSAGE FOR EXCEPTIONS IN THE MESSAGE.PROPERTY FILE. SOME OF THE ERROR MESSAGE DOESN???T HAVE ENOUGH INFORMATION FOR USER TO IDENTIFY THE ERROR. THE PARAMETER FOR ELEMENT NAME AND PROPERTY NAME SHOULD BE PROVIDED. SOME OF THE ERROR MESSAGES ARE DUPLICATE.    4"//$NON-NLS-1$
				+ "2.5 ADD JAVASCRIPT OBJECT    4"//$NON-NLS-1$
				+ "2.6 SUPPORT DROP IN THE GROUP HEADER    4"//$NON-NLS-1$
				+ "2.7 PROVIDE UNDOABLE TRANSACTION    4"//$NON-NLS-1$
				+ "2.8 USER PROPERTY REVISION    5"//$NON-NLS-1$
				+ "2.9 HELP GUI TO FILTRATE THE UNNECESSARY NOTIFICATIONS    5"//$NON-NLS-1$
				+ "3. NEW FEATURES    5"//$NON-NLS-1$
				+ "3.1 RHINO EXPRESSION PARSER    5"//$NON-NLS-1$
				+ "3.2 STRUCTURE TYPE SUPPORT    6"//$NON-NLS-1$
				+ "3.3 NEW SYSTEM    6"//$NON-NLS-1$
				+ "4. ELEMENT EXTENSION    6"//$NON-NLS-1$
				+ "5. QA TEST SUPPORTING    7");//$NON-NLS-1$
		assertEquals("Contents"//$NON-NLS-1$
				+ "1. INTRODUCTION    3"//$NON-NLS-1$
				+ "2. MINOR ENHANCEMENTS    3"//$NON-NLS-1$
				+ "2.1 PROVIDE PROPERTY GROUP INFORMATION ON ELEMETNDEFN CLASS    3"//$NON-NLS-1$
				+ "2.2 COMPLETE THE TODO TASKS    3"//$NON-NLS-1$
				+ "2.3 REVIEW RESULT    3"//$NON-NLS-1$
				+ "2.4 MESSAGE FILE ENHANCEMENT    4"//$NON-NLS-1$
				+ "CURRENTLY WE STORE THE ERROR MESSAGE FOR EXCEPTIONS IN THE MESSAGE.PROPERTY FILE. SOME OF THE ERROR MESSAGE DOESN???T HAVE ENOUGH INFORMATION FOR USER TO IDENTIFY THE ERROR. THE PARAMETER FOR ELEMENT NAME AND PROPERTY NAME SHOULD BE PROVIDED. SOME OF THE ERROR MESSAGES ARE DUPLICATE.    4"//$NON-NLS-1$
				+ "2.5 ADD JAVASCRIPT OBJECT    4"//$NON-NLS-1$
				+ "2.6 SUPPORT DROP IN THE GROUP HEADER    4"//$NON-NLS-1$
				+ "2.7 PROVIDE UNDOABLE TRANSACTION    4"//$NON-NLS-1$
				+ "2.8 USER PROPERTY REVISION    5"//$NON-NLS-1$
				+ "2.9 HELP GUI TO FILTRATE THE UNNECESSARY NOTIFICATIONS    5"//$NON-NLS-1$
				+ "3. NEW FEATURES    5"//$NON-NLS-1$
				+ "3.1 RHINO EXPRESSION PARSER    5"//$NON-NLS-1$
				+ "3.2 STRUCTURE TYPE SUPPORT    6"//$NON-NLS-1$
				+ "3.3 NEW SYSTEM    6" + "4. ELEMENT EXTENSION    6"//$NON-NLS-1$ //$NON-NLS-2$
				+ "5. QA TEST SUPPORTING    7", text.getContent());//$NON-NLS-1$

		// set special character to text
		text.setContent("',\"");//$NON-NLS-1$
		assertEquals("',\"", text.getContent()); //$NON-NLS-1$

	}

	/**
	 * NegativeValue
	 * 
	 * @throws DesignFileException
	 */

	public void test_regression_73771() throws DesignFileException {
		openDesign(filename);
		TextItemHandle text = (TextItemHandle) designHandle.findElement("text");//$NON-NLS-1$
		try {
			text.setWidth("-10points");//$NON-NLS-1$
		} catch (SemanticException e) {
			assertNotNull(e);
		}

	}
}
