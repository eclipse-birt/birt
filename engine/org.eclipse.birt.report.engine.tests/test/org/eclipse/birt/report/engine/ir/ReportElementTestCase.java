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
