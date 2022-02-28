/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * reproduce steps:
 * <p>
 * <ol>
 * <li>insert text item,input " text1 text2"
 * <li>preview
 * </ol>
 * <p>
 * <b>result:</b>
 * <p>
 * The whitespaces between "text1" and "text2" are displayed while the spaces
 * before text1 are ignored
 * </p>
 * Test description:
 * <p>
 * Set text with spaces to the text, get it
 * </p>
 */

public class Regression_76643 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 */

	public void test_regression_76643() throws SemanticException {
		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		designHandle = session.createDesign();
		ElementFactory factory = designHandle.getElementFactory();
		TextItemHandle text = factory.newTextItem("text"); //$NON-NLS-1$
		text.setContent(" text1 text2"); //$NON-NLS-1$
		assertEquals(" text1 text2", text.getContent()); //$NON-NLS-1$

	}
}
