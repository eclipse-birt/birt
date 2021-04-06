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
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Table/list should not be able to be inserted in Master Page mode.
 * </p>
 * Test description:
 * <p>
 * Insert a table and a list into master page header, make sure that exception
 * will throwed.
 * </p>
 */
public class Regression_122850 extends BaseTestCase {

	/**
	 * @throws NameException
	 * @throws ContentException
	 * 
	 */
	public void test_regression_122850() throws ContentException, NameException {
		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = sessionHandle.createDesign();

		ElementFactory factory = designHandle.getElementFactory();

		MasterPageHandle newpage = factory.newSimpleMasterPage("page"); //$NON-NLS-1$
		designHandle.getMasterPages().add(newpage);

		TableHandle table = factory.newTableItem("t1"); //$NON-NLS-1$
		ListHandle list = factory.newList("li"); //$NON-NLS-1$

		MasterPageHandle page = (MasterPageHandle) designHandle.getMasterPages().get(0);
		SlotHandle pageHeader = page.getSlot(SimpleMasterPageHandle.PAGE_HEADER_SLOT);
		try {
			pageHeader.add(table);
			fail();
		} catch (Exception e) {
			// success
		}

		try {
			pageHeader.add(list);
			fail();
		} catch (Exception e) {
			// success
		}

	}
}
