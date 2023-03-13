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

package org.eclipse.birt.report.model.util;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Test the ContentIterator.
 */

public class ContentIteratorTest extends BaseTestCase {
	private final static String INPUT = "ContentIteratorTest.xml"; //$NON-NLS-1$

	DesignElement grid = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		openDesign(INPUT);
		this.grid = designHandle.findElement("My grid").getElement(); //$NON-NLS-1$
	}

	/**
	 *
	 * @throws DesignFileException
	 * @throws IOException
	 */

	public void testIterator() throws DesignFileException, IOException {
		Iterator contentIter = new ContentIterator(design, grid);

		DesignElement next;

		next = (DesignElement) contentIter.next();
		assertEquals("Column", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(4, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Column", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(5, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Row", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(6, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Cell", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(7, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Grid", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(8, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Row", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(9, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Cell", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(10, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Cell", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(11, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Label", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(12, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Row", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(13, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Cell", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(14, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Cell", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(15, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Label", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(16, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Row", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(17, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Cell", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(18, next.getID());

		next = (DesignElement) contentIter.next();
		assertEquals("Label", next.getDefn().getName()); //$NON-NLS-1$
		assertEquals(19, next.getID());
	}

	/**
	 * Ensure that the elements are returned with the same sequences in two rounds
	 * of iteration.
	 *
	 * @throws DesignFileException
	 * @throws IOException
	 */

	public void testEnsureConsistency() throws DesignFileException, IOException {
		testIterator();
		testIterator();
	}

}
