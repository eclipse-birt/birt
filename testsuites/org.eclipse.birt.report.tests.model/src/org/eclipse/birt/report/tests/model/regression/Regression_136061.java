/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * <p>
 * Details: Create the styles with the same name
 * <p>
 * Step:
 * <p>
 * <ol>
 * <li>New a library
 * <li>New style twice in theme with the same name of "st1"
 * </ol>
 * <p>
 * <b>Actual result:</b> At the second time to add the style, it pop up
 * NewStyle1 is exist.
 * <p>
 * <b>Exception result:</b> It pop up a message to ask user "st1" is exist
 * <p>
 * Test descrption:
 * <p>
 * Add two styles with the same name, exception should be thrown out
 */

public class Regression_136061 extends BaseTestCase {

	/**
	 * @throws ContentException
	 * @throws NameException
	 */

	public void test_regression_136061() throws ContentException, NameException {
		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		libraryHandle = session.createLibrary();
		SharedStyleHandle style = libraryHandle.getElementFactory().newStyle("s1"); //$NON-NLS-1$
		ThemeHandle theme = libraryHandle.findTheme("defaultTheme"); //$NON-NLS-1$
		assertNotNull(theme);
		theme.getStyles().add(style);

		style = libraryHandle.getElementFactory().newStyle("s1"); //$NON-NLS-1$

		try {
			theme.getStyles().add(style);
			fail();
		} catch (NameException e) {
			assertEquals(NameException.DESIGN_EXCEPTION_DUPLICATE, e.getErrorCode());
		}

	}
}
