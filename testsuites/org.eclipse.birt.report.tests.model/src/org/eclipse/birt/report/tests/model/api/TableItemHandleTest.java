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

package org.eclipse.birt.report.tests.model.api;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test case for TableItemHandle class.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <tr>
 * <td>{@link #testSuppressDuplicatesProp()}</td>
 * </tr>
 * <tr>
 * <td>{@link #testACL_table()}</td>
 * </tr>
 *
 * @see TableItem
 */

public class TableItemHandleTest extends BaseTestCase {

	/**
	 * @param name
	 */
	public TableItemHandleTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(TableItemHandleTest.class);

	}

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test suppressduplicate in table row
	 *
	 * @throws Exception
	 */
	public void testSuppressDuplicatesProp() throws Exception {
		SessionHandle session = DesignEngine.newSession(ULocale.ENGLISH);
		designHandle = session.createDesign();
		design = (ReportDesign) designHandle.getModule();

		RowHandle row = designHandle.getElementFactory().newTableRow(3);
		assertFalse(row.suppressDuplicates());
		row.setSuppressDuplicates(true);
		assertTrue(row.suppressDuplicates());
		designHandle.getCommandStack().undo();
		assertFalse(row.suppressDuplicates());
		designHandle.getCommandStack().redo();
		assertTrue(row.suppressDuplicates());

	}

	/**
	 * Test ACLExpression and cascadeACL in table
	 *
	 * @throws SemanticException
	 */
	public void testACL_table() throws SemanticException {
		SessionHandle session = DesignEngine.newSession(ULocale.ENGLISH);
		designHandle = session.createDesign();
		design = (ReportDesign) designHandle.getModule();

		// table1 with label in header, detail and footer
		TableHandle table1 = designHandle.getElementFactory().newTableItem("table1", 1, 1, 1, 1);
		designHandle.getBody().add(table1);

		String acl = "rule1";
		table1.setACLExpression(acl);
		table1.setCascadeACL(true);

		assertTrue(table1.cascadeACL());
		assertEquals(acl, table1.getACLExpression());
	}

}
