/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleGroupElementHandle;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StringPropertyType;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Description:
 * <p>
 * User properties can't be found in properties view. Steps to reproduce:
 * <ol>
 * <li>Custom a user property in property editor view.
 * <li>Open properties view, user properties are not displayed.
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Add a user property to label, then get all properties defined on the label,
 * ensure that the user property is in the list.
 * </p>
 */
public class Regression_117648 extends BaseTestCase {

	/**
	 * @throws UserPropertyException
	 */

	public void test_regression_117648() throws UserPropertyException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle label = factory.newLabel("label"); //$NON-NLS-1$

		UserPropertyDefn userProp = new UserPropertyDefn();
		userProp.setName("prop1"); //$NON-NLS-1$
		userProp.setType(new StringPropertyType());
		userProp.setDefault("default value"); //$NON-NLS-1$

		label.addUserPropertyDefn(userProp);

		List elements = new ArrayList();
		elements.add(label);

		SimpleGroupElementHandle groupHandle = new SimpleGroupElementHandle(designHandle, elements);
		Iterator propIter = groupHandle.visiblePropertyIterator();

		GroupPropertyHandle userPropHandle = null;
		while (propIter.hasNext()) {
			GroupPropertyHandle handle = (GroupPropertyHandle) propIter.next();
			if ("prop1".equals(handle.getPropertyDefn().getName())) //$NON-NLS-1$
				;
			userPropHandle = handle;
		}

		assertNotNull(userPropHandle);
		assertEquals(PropertyType.STRING_TYPE, userPropHandle.getPropertyDefn().getTypeCode());
		assertEquals("default value", userPropHandle.getPropertyDefn().getDefault()); //$NON-NLS-1$
	}
}
