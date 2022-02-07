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
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * <p>
 * Description: Group can't be added in layout editor. Steps to reproduce:
 * <ol>
 * <li>Add a table.
 * <li>Right click on the table and find that "insert group" is grayed out.
 * </ol>
 * <p>
 * Test description:
 * <p>
 * Model provide canContain() checks, make sure it works in this case.
 * <p>
 */
public class Regression_119216 extends BaseTestCase {

	/**
	 * 
	 */

	public void test_regression_119216() {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		TableHandle table = factory.newTableItem("table", 3); //$NON-NLS-1$
		assertEquals(true, table.canContain(TableHandle.GROUP_SLOT, ReportDesignConstants.TABLE_GROUP_ELEMENT));
	}
}
