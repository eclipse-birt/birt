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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test <code>ClientsIterator</code> and <code>DerivedIterator</code>.
 *
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testDerivedIterator()}</td>
 * <td>Iterate the derived list of one element.</td>
 * <td>Two derived element should be found.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testClientsIterator()}</td>
 * <td>Iterate the clients list of one element.</td>
 * <td>Three client element should be found.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Iterate the element that can not be refered.</td>
 * <td>No client should be found.</td>
 * </tr>
 * </table>
 *
 */
public class ClientsDerivedIteratorTest extends BaseTestCase {

	private String fileName = "ClientsDerivedIteratorTest.xml"; //$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		openDesign(fileName);

		assertEquals(0, design.getErrorList().size());

	}

	/**
	 * Test iterating the derived list of an element.
	 *
	 * @throws Exception any exception met
	 */

	public void testDerivedIterator() throws Exception {
		SlotHandle slotHandle = designHandle.getComponents();
		LabelHandle lblHandle = (LabelHandle) slotHandle.get(0);

		int derivedCount = 0;

		assertEquals("Label One", lblHandle.getElement().getName()); //$NON-NLS-1$
		Iterator iter = lblHandle.derivedIterator();

		while (iter.hasNext()) {
			DesignElementHandle derived = (DesignElementHandle) iter.next();
			assertTrue(derived.getElement() instanceof Label);
			derivedCount++;
		}
		assertEquals(2, derivedCount);
	}

	/**
	 * Test iterating the client list of an element.
	 *
	 * @throws Exception any exception met
	 */

	public void testClientsIterator() throws Exception {
		DesignElement style = design.findStyle("My-Style"); //$NON-NLS-1$

		// Test the referable element

		int clientsCount = 0;
		Iterator iter = style.getHandle(design).clientsIterator();
		while (iter.hasNext()) {
			DesignElementHandle client = (DesignElementHandle) iter.next();
			assertTrue(client.getElement() instanceof TextItem);
			clientsCount++;
		}
		assertEquals(3, clientsCount);

		// Iterate the element that can not be refered.

		DesignElement text = design.findElement("My Text"); //$NON-NLS-1$
		iter = text.getHandle(design).clientsIterator();
		assertFalse(iter.hasNext());
	}
}
