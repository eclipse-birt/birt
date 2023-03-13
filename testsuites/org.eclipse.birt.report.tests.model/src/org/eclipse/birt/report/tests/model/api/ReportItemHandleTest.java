/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.api;

import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test ReportItemHandle.
 *
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 *
 * <tr>
 * <td>{@link #testACL()}</td>
 * </tr>
 * </table>
 */

public class ReportItemHandleTest extends BaseTestCase {

	/**
	 * @param name
	 */
	public ReportItemHandleTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	DesignElement element;
	InnerReportItemHandle innerHandle;

	public static Test suite() {
		return new TestSuite(ReportItemHandleTest.class);
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT("ReportItemHandleTest.xml", "ReportItemHandleTest.xml");
		openDesign("ReportItemHandleTest.xml"); //$NON-NLS-1$

	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 *
	 */

	class InnerReportItemHandle extends ReportItemHandle {

		InnerReportItemHandle(ReportDesign design, DesignElement element) {
			super(design, element);
		}
	}

	/**
	 * Tests ACLExpression for report item
	 *
	 * @throws SemanticException
	 */
	public void testACL() throws SemanticException {
		TextItemHandle textHandle = (TextItemHandle) designHandle.findElement("myText");
		String aclExp = "rule1:root";
		textHandle.setACLExpression(aclExp);
		assertEquals(aclExp, textHandle.getACLExpression());
		assertFalse(textHandle.cascadeACL());
		textHandle.setCascadeACL(true);
		assertFalse(textHandle.cascadeACL());
		assertFalse(((Boolean) textHandle.getProperty(IReportItemModel.CASCADE_ACL_PROP)).booleanValue());

		aclExp = "sid1,sid2";
		textHandle.setACLExpression(aclExp);
		assertEquals(aclExp, textHandle.getACLExpression());

//		aclExp=null;
//		textHandle.setACLExpression( aclExp );
//		assertEquals("__all",textHandle.getACLExpression( ));

		aclExp = "普通用户~!@#$%^&*()_+=-`{}|:;.?'";
		textHandle.setACLExpression(aclExp);
		assertEquals(aclExp, textHandle.getACLExpression());

		aclExp = " ";
		textHandle.setACLExpression(aclExp);
		assertEquals(aclExp, textHandle.getACLExpression());

	}

}
