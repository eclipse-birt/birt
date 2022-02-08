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

package org.eclipse.birt.report.model.core;

import java.util.List;

import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The Test Case of Class SingleElementSlot.
 * 
 * The SingleElementSlot is container of the design elements. It has only one
 * content. We test the insert-remove operation and container-content
 * relationship in test case.
 * <p>
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testFindPosn}</td>
 * <td>insert one label and find its position</td>
 * <td>find it and get 0</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>find label's position which is not inserted</td>
 * <td>cann't find it and get -1</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testInsertRemove}</td>
 * <td>check initial state</td>
 * <td>doesn't contain</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>insert label and check it</td>
 * <td>contain</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>remove label and check it</td>
 * <td>doesn't contain</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testCanDrop}</td>
 * <td>insert one label and justify if it can drop</td>
 * <td>can drop</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>justify another label which is not inserted</td>
 * <td>cann't drop</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetContents}</td>
 * <td>insert one label</td>
 * <td>contain it and can get it</td>
 * </tr>
 * 
 * </table>
 * 
 */
public class SingleElementSlotTest extends BaseTestCase {
	SingleElementSlot slot = null;
	Label label = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		slot = new SingleElementSlot();
		label = new Label();
	}

	/**
	 * Test findPosn( DesignElement ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>insert one label and find its position</li>
	 * <li>find label's position which is not inserted</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>find it and get 0</li>
	 * <li>cann't find it and get -1</li>
	 * </ul>
	 * 
	 */
	public void testFindPosn() {
		slot.insert(label, 0);
		assertEquals(0, slot.findPosn(label));

		Label label1 = new Label();
		assertEquals(-1, slot.findPosn(label1));
	}

	/**
	 * Test insert( DesignElement, int ), getCount(), contains( DesignElement ) and
	 * remove( DesignElement ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>check initial state</li>
	 * <li>insert label and check it</li>
	 * <li>remove label and check it</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>doesn't contain</li>
	 * <li>contain</li>
	 * <li>doesn't contain</li>
	 * </ul>
	 * 
	 */
	public void testInsertRemove() {
		assertEquals(0, slot.getCount());
		assertFalse(slot.contains(label));

		slot.insert(label, 0);
		assertEquals(1, slot.getCount());
		assertTrue(slot.contains(label));

		slot.remove(label);
		assertFalse(slot.contains(label));
		assertEquals(0, slot.getCount());

	}

	/**
	 * Test canDrop( DesignElement ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>insert one label and justify if it can drop</li>
	 * <li>justify another label which is not inserted</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>can drop</li>
	 * <li>cann't drop</li>
	 * </ul>
	 */
	public void testCanDrop() {
		slot.insert(label, 0);
		assertTrue(slot.canDrop(label));

	}

	/**
	 * Test getContents() , getContent().
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>insert one label</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>contain it and can get it</li>
	 * </ul>
	 */
	public void testGetContents() {
		List list = slot.getContents();
		assertTrue(list.isEmpty());

		slot.insert(label, 0);
		list = slot.getContents();
		assertTrue(list.contains(label));
		Object o = slot.getContent(0);
		assertEquals(label, o);
	}
}
