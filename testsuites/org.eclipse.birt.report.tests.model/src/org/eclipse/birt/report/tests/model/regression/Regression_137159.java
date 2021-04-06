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

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * the cascading parameter group needs a new string property "Prompt Text"
 * </p>
 * Test description:
 * <p>
 * Set/get "prompt text" for cascading parameter group
 * </p>
 */

public class Regression_137159 extends BaseTestCase {

	/**
	 * 
	 * @throws SemanticException
	 */

	public void test_regression_137159() throws SemanticException {
		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		designHandle = session.createDesign();
		ElementFactory factory = designHandle.getElementFactory();
		CascadingParameterGroupHandle group = factory.newCascadingParameterGroup("group"); //$NON-NLS-1$
		group.setProperty(CascadingParameterGroup.PROMPT_TEXT_PROP, "prompttext"); //$NON-NLS-1$
		assertEquals("prompttext", group.getPromptText()); //$NON-NLS-1$

	}
}
