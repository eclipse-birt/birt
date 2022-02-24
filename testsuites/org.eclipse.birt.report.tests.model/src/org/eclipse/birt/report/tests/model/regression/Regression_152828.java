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
package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Add the resourceFolder on SessionHandle
 * <p>
 * Test description: Make sure that user can set/get resource folder on session
 * handle.
 * <p>
 * </p>
 */
public class Regression_152828 extends BaseTestCase {
	/**
	 * 
	 */
	public void test_regression_152828() {
		SessionHandle session = new DesignEngine(null).newSessionHandle(ULocale.ENGLISH);
		session.setResourceFolder("d:/resource"); //$NON-NLS-1$
		assertEquals("d:/resource", session.getResourceFolder()); //$NON-NLS-1$

	}
}
