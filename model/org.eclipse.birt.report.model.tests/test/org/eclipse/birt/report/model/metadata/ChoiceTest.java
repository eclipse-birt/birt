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
package org.eclipse.birt.report.model.metadata;

import com.ibm.icu.util.ULocale;

import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test case for ChoiceType.
 * 
 */
public class ChoiceTest extends BaseTestCase {
	private Choice choice = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ThreadResources.setLocale(ULocale.ENGLISH);
	}

	/**
	 * test getters and setters.
	 */
	public void testGetterAndSetters() {
		choice = new Choice("Name", "Choices.colors.maroon"); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("Name", choice.getName()); //$NON-NLS-1$

		assertEquals("Maroon", choice.getDisplayName()); //$NON-NLS-1$
	}
}
