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

package org.eclipse.birt.report.model.core;

import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The Test Case of Class MultiElementSlot.
 * 
 * The MultiElementSlot is container of the design elements. It has multi
 * contents. We test the insert-remove operation and container-content
 * relationship in test case.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testInsertRemove}</td>
 * <td>insert two label elements into MultiElementSlot</td>
 * <td>MultiElementSlot contains two elements</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>find the position of the new inserted element</td>
 * <td>find special element</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>remove the first element in MultiElementSlot.</td>
 * <td>can't find element</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testMoveContent}</td>
 * <td>insert three lable elements</td>
 * <td>find three elements</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>move element position</td>
 * <td>position of elements changed</td>
 * </tr>
 * 
 * </table>
 * 
 */
public class MultiElementSlotTest extends BaseTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

	}

	/**
	 * Tests general slot operation, such as insert, drop, contains, findPos and so
	 * on.
	 * 
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>insert two label elements into MultiElementSlot</li>
	 * <li>find the position of the new inserted element</li>
	 * <li>remove the first element in MultiElementSlot.</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>MultiElementSlot contains two elements</li>
	 * <li>find special element</li>
	 * <li>cann't find element</li>
	 * </ul>
	 * 
	 */
	public void testInsertRemove() {
		MultiElementSlot slot = new MultiElementSlot();

		// Insert two labels into this slot

		Label label1 = new Label();
		slot.insert(label1, 0);

		Label label2 = new Label();
		slot.insert(label2, 1);

		assertEquals(2, slot.getCount());

		// Check the above two labels are really in this slot.

		assertEquals(0, slot.findPosn(label1));
		assertEquals(1, slot.findPosn(label2));
		assertTrue(slot.contains(label1));
		assertTrue(slot.contains(label2));

		// Remove the first label

		slot.remove(label1);

		assertEquals(1, slot.getCount());
		assertFalse(slot.contains(label1));
		assertEquals(0, slot.findPosn(label2));

		assertTrue(slot.canDrop(label2));
	}

	/**
	 * Tests moving content.
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>insert three lable elements</li>
	 * <li>move element position</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>find three elements</li>
	 * <li>position of elements changed</li>
	 * </ul>
	 */

	public void testMoveContent() {
		MultiElementSlot slot = new MultiElementSlot();

		// Insert three labels

		Label label1 = new Label();
		Label label2 = new Label();
		Label label3 = new Label();

		slot.insert(label1, 0);
		slot.insert(label2, 1);
		slot.insert(label3, 2);

		assertEquals(0, slot.findPosn(label1));
		assertEquals(1, slot.findPosn(label2));
		assertEquals(2, slot.findPosn(label3));

		// Move the first label behind the second one

		slot.moveContent(0, 2);

		assertEquals(1, slot.findPosn(label3));
		assertEquals(2, slot.findPosn(label1));
		assertEquals(0, slot.findPosn(label2));

	}
}
