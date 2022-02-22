/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test case for ElementRefValue.
 *
 */
public class ElementRefValueTest extends BaseTestCase {
	private ElementRefValue value = null;
	private FreeForm container = null;

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		container = new FreeForm();
		container.setName("ContainerName"); //$NON-NLS-1$
	}

	/**
	 * test resolve, unresolve, and their transfer.
	 */
	public void testResolve() {
		value = new ElementRefValue(null, container);
		assertTrue(value.isResolved());
		assertEquals("ContainerName", value.getName()); //$NON-NLS-1$

		// unresolved -> resolved -> unresolved

		value = new ElementRefValue(null, "AnotherContainerName"); //$NON-NLS-1$
		assertFalse(value.isResolved());
		value.resolve(container);
		assertEquals("ContainerName", value.getName()); //$NON-NLS-1$
		assertTrue(value.isResolved());
		value.unresolved("ThirdContainerName"); //$NON-NLS-1$
		assertFalse(value.isResolved());

	}

}
