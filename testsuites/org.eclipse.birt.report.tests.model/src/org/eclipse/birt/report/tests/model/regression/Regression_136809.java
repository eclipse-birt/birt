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
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Steps to reproduce:
 * <ol>
 * <li>New a report
 * <li>Set Master Page Type to custom
 * <li>Set Width to 8in, Height to 100%
 * <li>Preview the report
 * </ol>
 * <b>Expected result: </b>
 * <p>
 * Give a default size for custom master page so that the percentage value won't
 * cause the report damaged
 * <p>
 * <b>Actual result:</b>
 * <p>
 * The design file "XX\XX.rptdesign" has error and can not be run.
 * <p>
 * Test description:
 * <p>
 * Can't set percentage choice for master page height& width
 * </p>
 */

public class Regression_136809 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 */
	public void test_regression_136809() throws SemanticException {
		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		designHandle = session.createDesign();
		ElementFactory factory = designHandle.getElementFactory();
		SimpleMasterPageHandle masterpage = factory.newSimpleMasterPage("masterpage"); //$NON-NLS-1$
		masterpage.setPageType(DesignChoiceConstants.PAGE_SIZE_CUSTOM);
		designHandle.getMasterPages().add(masterpage);
		try {
			masterpage.setProperty(SimpleMasterPage.WIDTH_PROP, "100%"); //$NON-NLS-1$

			// FIXME: The bug is not fixed, so % can be set into property in this case.
			// fail();
			System.err.println("THIS BUG WAS NOT FIXED");
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_UNIT_NOT_ALLOWED, e.getErrorCode());
		}
		try {
			masterpage.setProperty(SimpleMasterPage.HEIGHT_PROP, "80%"); //$NON-NLS-1$

			// FIXME: The bug is not fixed, so % can be set into property in this case.
			// fail();
			System.err.println("THIS BUG WAS NOT FIXED");
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_UNIT_NOT_ALLOWED, e.getErrorCode());
		}

	}

}
