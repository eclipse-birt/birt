/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Set a custom masterpage height and width, switch its type to another one,
 * error is send out
 * </p>
 * Test description:
 * <p>
 * Clear height & width property of Masterpage when change page type from Custom
 * to any other predefined. Change back to custom, height & width value is kept
 * </p>
 */

public class Regression_77901 extends BaseTestCase {

	private String INPUT = "Regression_77901.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
		// copyResource_INPUT( INPUT2, INPUT2 );
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_77901() throws DesignFileException, SemanticException {
		openDesign(INPUT);
		MasterPageHandle masterpage = designHandle.findMasterPage("masterpage"); //$NON-NLS-1$
		assertNotNull(masterpage);
		masterpage.setPageType(DesignChoiceConstants.PAGE_SIZE_CUSTOM);
		masterpage.setStringProperty(MasterPage.HEIGHT_PROP, "10pt"); //$NON-NLS-1$
		masterpage.setStringProperty(MasterPage.WIDTH_PROP, "20in"); //$NON-NLS-1$

		// Master page height and width change to A4 default height and width

		masterpage.setPageType(DesignChoiceConstants.PAGE_SIZE_A4);
		assertEquals(MasterPage.A4_HEIGHT, masterpage.getStringProperty(MasterPage.HEIGHT_PROP));
		assertEquals(MasterPage.A4_WIDTH, masterpage.getStringProperty(MasterPage.WIDTH_PROP));

		// Change back to Custom, height and width value is kept

		masterpage.setPageType(DesignChoiceConstants.PAGE_SIZE_CUSTOM);
		assertEquals("10pt", masterpage //$NON-NLS-1$
				.getStringProperty(MasterPage.HEIGHT_PROP));
		assertEquals("20in", masterpage //$NON-NLS-1$
				.getStringProperty(MasterPage.WIDTH_PROP));

	}
}
