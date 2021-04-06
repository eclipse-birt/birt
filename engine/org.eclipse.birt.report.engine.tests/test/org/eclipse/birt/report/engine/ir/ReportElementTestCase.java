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

package org.eclipse.birt.report.engine.ir;

import junit.framework.TestCase;

/**
 * base class of all elements in the report design test case
 * 
 */
public abstract class ReportElementTestCase extends TestCase {

	protected ReportElementDesign element;

	public ReportElementTestCase(ReportElementDesign e) {
		element = e;
	}

	/**
	 * Test all get/set accessors in base class
	 * 
	 * set values of the element
	 * 
	 * then get the values one by one to test if they work correctly
	 */
	public void testBaseElement() {
		// Set
		element.setExtends("extends");
		element.setID(1);
		element.setName("name");
		element.setJavaClass("javaClass");

		// Get
		assertEquals(element.getExtends(), "extends");
		assertEquals(element.getID(), 1);
		assertEquals(element.getName(), "name");
		assertEquals(element.getJavaClass(), "javaClass");
	}
}
