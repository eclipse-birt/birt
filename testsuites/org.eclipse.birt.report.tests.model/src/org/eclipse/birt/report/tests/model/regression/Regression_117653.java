/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExpressionPropertyType;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Description: User properties/named expressions are listed out of order.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Create two named expressions.
 * <li>Then switch to "user properties".
 * <li>Switch back to "named expressions".
 * <li>Add a named expression.
 * <li>These three named expressions are displayed in random
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Add serveral named expression on a label, retrieve them back, make sure they
 * are in adding order.
 * </p>
 */
public class Regression_117653 extends BaseTestCase {
	/**
	 * @throws UserPropertyException
	 * 
	 */

	public void test_regression_117653() throws UserPropertyException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle label = factory.newLabel("label"); //$NON-NLS-1$

		UserPropertyDefn expr1 = new UserPropertyDefn();
		expr1.setName("ID"); //$NON-NLS-1$
		expr1.setType(new ExpressionPropertyType());
		label.addUserPropertyDefn(expr1);

		UserPropertyDefn expr2 = new UserPropertyDefn();
		expr2.setName("Assignee"); //$NON-NLS-1$
		expr2.setType(new ExpressionPropertyType());
		label.addUserPropertyDefn(expr2);

		UserPropertyDefn expr3 = new UserPropertyDefn();
		expr3.setName("Pri"); //$NON-NLS-1$
		expr3.setType(new ExpressionPropertyType());
		label.addUserPropertyDefn(expr3);

		List userProps = label.getUserProperties();
		assertEquals("ID", ((UserPropertyDefn) userProps.get(0)).getName()); //$NON-NLS-1$
		assertEquals("Assignee", ((UserPropertyDefn) userProps.get(1)).getName()); //$NON-NLS-1$
		assertEquals("Pri", ((UserPropertyDefn) userProps.get(2)).getName()); //$NON-NLS-1$

	}
}
