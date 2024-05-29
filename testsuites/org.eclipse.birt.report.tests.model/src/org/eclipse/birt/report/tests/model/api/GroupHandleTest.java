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

import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests GroupHandle.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * <tr>
 * <td>{@link #testIntervalRange()}</td>
 * <td>Tests group setintervalrange method.</td>
 * <td>Set succeed.</td>
 * </tr>
 * <tr>
 * <td>{@link #testACL()}</td>
 * <td>Test group ACLExpression and cascadeACL used for security.</td>
 * <td>Values are set correctly.</td>
 * </tr>
 * </table>
 */

public class GroupHandleTest extends BaseTestCase {

	/**
	 * @param name
	 */
	public GroupHandleTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return
	 */
	public static Test suite() {
		return new TestSuite(GroupHandleTest.class);

	}

	/**
	 * Tests to read and set properties on a GroupElement.
	 *
	 * @throws Exception if errors occur when opens the design file
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT("GroupHandleTest.xml", "GroupHandleTest.xml");
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * refer to bug #161174, support setIntervalRange(String)
	 *
	 * @throws SemanticException
	 */
	public void testIntervalRange() throws SemanticException {
		createDesign();
		ElementFactory factory = designHandle.getElementFactory();
		TableHandle table = factory.newTableItem("table"); //$NON-NLS-1$
		designHandle.getBody().add(table);

		TableGroupHandle group = factory.newTableGroup();
		table.getGroups().add(group);

		group.setKeyExpr("row[\"abc\"]"); //$NON-NLS-1$
		group.setIntervalRange("1.234567"); //$NON-NLS-1$

		assertEquals("1.234567", group //$NON-NLS-1$
				.getStringProperty(GroupHandle.INTERVAL_RANGE_PROP));

		group.setIntervalRange("1234567E-6"); //$NON-NLS-1$
		assertEquals("1.234567", group //$NON-NLS-1$
				.getStringProperty(GroupHandle.INTERVAL_RANGE_PROP));

		try {
			group.setIntervalRange("abc"); //$NON-NLS-1$
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

	}

	public void testIntervalRangeWithLocale() throws SemanticException {
		createDesign(ULocale.GERMANY);
		ElementFactory factory = designHandle.getElementFactory();
		TableHandle table = factory.newTableItem("table"); //$NON-NLS-1$
		designHandle.getBody().add(table);

		TableGroupHandle group = factory.newTableGroup();
		table.getGroups().add(group);

		group.setKeyExpr("row[\"abc\"]"); //$NON-NLS-1$
		group.setIntervalRange("1.234567"); //$NON-NLS-1$
		// Interval range is locale-dependent
		group.setIntervalRange("6.0"); //$NON-NLS-1$
		assertEquals("6.0", group.getStringProperty(GroupHandle.INTERVAL_RANGE_PROP)); //$NON-NLS-1$

		group.setIntervalRange("1,234.567"); //$NON-NLS-1$
		assertEquals("1.234", group //$NON-NLS-1$
				.getStringProperty(GroupHandle.INTERVAL_RANGE_PROP));

		group.setIntervalRange("1234567E-6"); //$NON-NLS-1$
		assertEquals("1.234567", group.getStringProperty(GroupHandle.INTERVAL_RANGE_PROP)); //$NON-NLS-1$
	}

	/**
	 * Test group ACLExpression and cascadeACL used for security
	 *
	 * @throws SemanticException
	 */
	public void testACL() throws SemanticException {
		createDesign();
		ElementFactory factory = designHandle.getElementFactory();
		TableHandle table = factory.newTableItem("table"); //$NON-NLS-1$
		designHandle.getBody().add(table);

		TableGroupHandle group = factory.newTableGroup();
		table.getGroups().add(group);

		group.setACLExpression("group");
		assertEquals("group", ((GroupHandle) table.getGroups().get(0)).getACLExpression());
		assertTrue(((GroupHandle) table.getGroups().get(0)).cascadeACL());
	}
}
