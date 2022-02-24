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

package org.eclipse.birt.report.model.extension;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests the extension pointer of org.eclipse.birt.report.model.reportItem.
 */

public class CompatibleReportItemExtensionTest extends BaseTestCase {

	private String fileName = "CompatibleExtensionTest.xml"; //$NON-NLS-1$
	private String goldenFileName = "CompatibleExtensionTest_golden.xml"; //$NON-NLS-1$

	private String fileName_1 = "CompatibleExtensionTest_1.xml"; //$NON-NLS-1$
	private String goldenFileName_1 = "CompatibleExtensionTest_golden_1.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();
		ThreadResources.setLocale(ULocale.ENGLISH);
	}

	/**
	 * Tests the backward compatibility for the extended item that in the design
	 * file of which the version is smaller than "3.1.0".
	 * 
	 * @throws Exception
	 * 
	 */

	public void testCompatibleBoundDataColumns() throws Exception {
		openDesign(fileName);

		save();
		assertTrue(compareFile(goldenFileName));
	}

	/**
	 * Tests to remove unused columns for extended item like matrix.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testRemoveBoundDataColumns() throws Exception {
		openDesign(fileName_1);

		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$

		extendedItem.removedUnusedColumnBindings();

		save();
		assertTrue(compareFile(goldenFileName_1));
	}
}
