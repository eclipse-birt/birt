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
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * details: cascading parameter default name will be generated with existing
 * parameter name
 * 
 * steps to reproduce:
 * <ol>
 * <li>create two parameters with default name as its geven name,say "new
 * parameter" and "new parameter1"
 * <li>delete the parameter with name "new parameter"
 * <li>create a cascading parameter group with at least two parameters
 * </ol>
 * <b>actual result:</b>
 * <p>
 * second cascading parameter is generated with name "new parameter1"
 * <p>
 * <b>expected result:</b>
 * <p>
 * second cascading parameter is expected to generate another default name to
 * avoid same name
 * </p>
 * Test description:
 * <p>
 * Create a two parameters with given default name, and then add a cascading
 * parameter group, adding another two parameters in the group, make sure that
 * name will not duplicate.
 * </p>
 */
public class Regression_118006 extends BaseTestCase {

	/**
	 * @throws NameException
	 * @throws ContentException
	 * 
	 */

	public void test_regression_118006() throws ContentException, NameException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		ScalarParameterHandle p1 = factory.newScalarParameter(null);
		ScalarParameterHandle p2 = factory.newScalarParameter(null);

		designHandle.getParameters().add(p1);
		designHandle.getParameters().add(p2);

		ParameterGroupHandle parameterGroupHandle = factory.newParameterGroup("group"); //$NON-NLS-1$
		ScalarParameterHandle p3 = factory.newScalarParameter(null);
		parameterGroupHandle.getParameters().add(p3);

		// make sure that the default names will not duplicate.

		assertEquals("NewParameter", p1.getName()); //$NON-NLS-1$
		assertEquals("NewParameter1", p2.getName()); //$NON-NLS-1$
		assertEquals("NewParameter2", p3.getName()); //$NON-NLS-1$
	}
}
