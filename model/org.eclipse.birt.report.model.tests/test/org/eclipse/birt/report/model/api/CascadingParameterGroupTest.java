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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test cases for Cascading parameter group APIs.
 */

public class CascadingParameterGroupTest extends BaseTestCase {

	private final static String INPUT = "CascadingParameterGroupTest.xml"; //$NON-NLS-1$

	/**
	 * Tests get properties and get contents.
	 * 
	 * @throws DesignFileException
	 */

	public void testGetPropertiesAndContents() throws DesignFileException {
		openDesign(INPUT);
		CascadingParameterGroupHandle groupHandle = getGroupHandle("Country-State-City"); //$NON-NLS-1$
		assertEquals("Group for Country-State-City", groupHandle.getDisplayName()); //$NON-NLS-1$

		SlotHandle parameters = groupHandle.getParameters();
		assertEquals(3, parameters.getCount());

		ScalarParameterHandle p1 = (ScalarParameterHandle) parameters.get(0);

		assertEquals("dynamic", p1.getValueType()); //$NON-NLS-1$
		assertEquals("Country", p1.getName()); //$NON-NLS-1$
		assertEquals("ds1", p1.getDataSetName()); //$NON-NLS-1$
		assertEquals("row[\"country\"]", p1.getValueExpr()); //$NON-NLS-1$
		assertEquals("Enter country:", p1.getLabelExpr()); //$NON-NLS-1$
	}

	/**
	 * Returns the parameter group handle given the name of the parameter group.
	 * 
	 * @param name
	 * @return parameter group handle
	 */

	private CascadingParameterGroupHandle getGroupHandle(String name) {
		SlotHandle parameters = this.designHandle.getParameters();
		for (int i = 0; i < parameters.getCount(); i++) {
			DesignElementHandle elementHandle = parameters.get(i);
			if (elementHandle.getName().equals(name))
				return (CascadingParameterGroupHandle) elementHandle;
		}

		return null;
	}
}
