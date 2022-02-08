/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleGroupElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.metadata.BooleanPropertyType;
import org.eclipse.birt.report.model.metadata.StringPropertyType;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Description: User property values can't be set in properties view.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Define a user property.
 * <li>Open properties view, find this user property.
 * <li>Its value can't be set.
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Set a user property value on a label, ensure it is properly stored.
 * </p>
 */
public class Regression_117823 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 */
	public void test_regression_117823() throws SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle label = factory.newLabel("label"); //$NON-NLS-1$

		UserPropertyDefn userProp1 = new UserPropertyDefn();
		userProp1.setName("prop1"); //$NON-NLS-1$
		userProp1.setType(new StringPropertyType());
		userProp1.setDefault("default value"); //$NON-NLS-1$

		UserPropertyDefn userProp2 = new UserPropertyDefn();
		userProp2.setName("prop2"); //$NON-NLS-1$
		userProp2.setType(new BooleanPropertyType());
		userProp2.setDefault(Boolean.FALSE);

		label.addUserPropertyDefn(userProp1);
		label.addUserPropertyDefn(userProp2);

		List elements = new ArrayList();
		elements.add(label);

		SimpleGroupElementHandle groupHandle = new SimpleGroupElementHandle(designHandle, elements);

		// set property on label

		groupHandle.setStringProperty("prop1", "Learning abc");//$NON-NLS-1$//$NON-NLS-2$
		groupHandle.setStringProperty("prop2", "true");//$NON-NLS-1$//$NON-NLS-2$

		// ensure the value is properly set.

		assertEquals("Learning abc", groupHandle.getStringProperty("prop1")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("true", groupHandle.getStringProperty("prop2")); //$NON-NLS-1$//$NON-NLS-2$
	}
}
