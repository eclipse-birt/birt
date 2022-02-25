/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.report.model.activity;

import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test for Command.
 * <p>
 *
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td></td>
 * <td>input string only contains white-space</td>
 * <td>output is null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>threshold or end of input string is white-space</td>
 * <td>output trim threshold and end white-space</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetActivityStack()}</td>
 * <td>get activity stack from command</td>
 * <td>equals to activity stack which get from DesignElement</td>
 * </tr>
 *
 * </table>
 *
 */
public class CommandTest extends BaseTestCase {
	Command command = null;

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		createDesign();
		assertNotNull(design);

		this.command = new MockupCommand(design);
	}

	/**
	 * test getActivityStack().
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>get activity stack from command</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>equals to activity stack which get from DesignElement</li>
	 * </ul>
	 *
	 */
	public void testGetActivityStack() {
		ActivityStack ac1 = command.getActivityStack();

		assertEquals(design.getActivityStack(), ac1);
	}

	class MockupCommand extends Command {

		/**
		 * @param obj
		 */
		public MockupCommand(ReportDesign obj) {
			super(obj);
		}

	}
}
