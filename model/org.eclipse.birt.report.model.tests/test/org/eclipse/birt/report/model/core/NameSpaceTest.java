/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.core;

import java.util.List;

import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * The Test Case of NameSpace.
 * 
 * The operation in NameSpace is all about the HashMap of elements, the key is
 * name of the elements.
 * <p>
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testInsert}</td>
 * <td>insert one element</td>
 * <td>object is equal to original element</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>insert another new element</td>
 * <td>size of space is become two</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testInsert}</td>
 * <td>insert one element</td>
 * <td>object is equal to original element</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>insert another new element</td>
 * <td>size of space is become two</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testRemove}</td>
 * <td>the simple insert-remove</td>
 * <td>first contain design element ,after remove it doesn't contain</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>insert the same element with new name</td>
 * <td>when insert size of space is two</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testRename}</td>
 * <td>insert element</td>
 * <td>contain</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>rename element</td>
 * <td>name changes</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>rename element when name of design is null</td>
 * <td>don't contain</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testContains}</td>
 * <td>insert element and check if it contains or not</td>
 * <td>contain</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetElement}</td>
 * <td>insert one and get it</td>
 * <td>result is equal to original element</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetCount}</td>
 * <td>insert one element</td>
 * <td>1</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>remove one element</td>
 * <td>0</td>
 * </tr>
 * 
 * </table>
 * 
 */

public class NameSpaceTest extends BaseTestCase {

	NameSpace nameSpace;

	private static final String reportString = "report"; //$NON-NLS-1$
	private static final String newReportString = "new report"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();

		SessionHandle sessionHandle = DesignEngine.newSession((ULocale) null);
		designHandle = sessionHandle.createDesign("myDesign"); //$NON-NLS-1$
		design = designHandle.getDesign();

		assertNotNull(design);

		nameSpace = new NameSpace();
	}

	/**
	 * test Insert().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>insert one element</li>
	 * <li>insert another new element</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>object is equal to original element</li>
	 * <li>size of space is become two</li>
	 * </ul>
	 * 
	 */
	public void testInsert() {
		design.setName(reportString);
		nameSpace.insert(design);
		assertTrue(nameSpace.getCount() == 1);
		Object o = nameSpace.getElement(reportString);
		assertEquals(design, o);

		// insert the same element with new name
		// and then the operation succeed.
		// so we need the function rename() to delete
		// the old element with the old name.

		design.setName(newReportString);
		nameSpace.insert(design);
		assertTrue(nameSpace.getCount() == 2);
		o = nameSpace.getElement(newReportString);
		assertEquals(design, o);
		o = nameSpace.getElement(reportString);
		assertEquals(design, o);
	}

	/**
	 * test remove().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>the simple insert-remove</li>
	 * <li>insert the same element with new name</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>first contain design element ,after remove it doesn't contain</li>
	 * <li>when insert size of space is two</li>
	 * </ul>
	 * 
	 */
	public void testRemove() {
		// the simple insert-remove test

		design.setName(reportString);
		nameSpace.insert(design);
		assertTrue(nameSpace.contains(reportString));
		nameSpace.remove(design);
		assertFalse(nameSpace.contains(reportString));

		// the complex situation test

		design.setName(reportString);
		nameSpace.insert(design);
		assertTrue(nameSpace.getCount() == 1);
		Object o = nameSpace.getElement(reportString);
		assertEquals(design, o);

		// insert the same element with new name

		design.setName(newReportString);
		nameSpace.insert(design);
		assertTrue(nameSpace.getCount() == 2);
		o = nameSpace.getElement(newReportString);
		assertEquals(design, o);
		nameSpace.remove(design);
		assertTrue(nameSpace.contains(reportString));
		assertFalse(nameSpace.contains(newReportString));

	}

	/**
	 * test rename().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>insert element</li>
	 * <li>rename element</li>
	 * <li>rename element when name of design is null</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>contain</li>
	 * <li>name changes</li>
	 * <li>don't contain</li>
	 * </ul>
	 * 
	 */
	public void testRename() {
		// set the name of the element and insert into HashMap

		design.setName(reportString);
		nameSpace.insert(design);
		assertTrue(nameSpace.contains(reportString));

		// we rename the element and the key(name) of the element in
		// HashMap changes with the rename operation

		design.setName(newReportString);
		nameSpace.rename(design, reportString, newReportString);
		assertTrue(nameSpace.contains(newReportString));
		assertFalse(nameSpace.contains(reportString));
		assertTrue(nameSpace.getCount() == 1);
		Object o = nameSpace.getElement(newReportString);
		assertEquals(design, o);

		// if name of design is null, rename method is the same as remove method
		design.setName(null);
		nameSpace.rename(design, newReportString, null);
		assertFalse(nameSpace.contains(newReportString));
	}

	/**
	 * test contains().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>insert element and check if it contains or not</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>contain</li>
	 * </ul>
	 */

	public void testContains() {
		assertFalse(nameSpace.contains(reportString));
		design.setName(reportString);
		nameSpace.insert(design);
		assertTrue(nameSpace.contains(reportString));

		// we call setName to change the name of elements
		// but the new name is not in NameSpace
		// and the function rename() is to solve the problem

		design.setName(newReportString);
		assertFalse(nameSpace.contains(newReportString));
		assertTrue(nameSpace.contains(reportString));

		nameSpace.insert(design);
		assertTrue(nameSpace.contains(newReportString));
	}

	/**
	 * test getElement(). Test Case:
	 * <ul>
	 * <li>insert one and get it</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>result is equal to original element</li>
	 * </ul>
	 * 
	 */
	public void testGetElement() {
		design.setName(reportString);
		nameSpace.insert(design);
		assertTrue(nameSpace.getCount() == 1);
		Object o = nameSpace.getElement(reportString);
		assertEquals(design, o);

		o = nameSpace.getElement(reportString.toUpperCase());
		assertNull(o);
	}

	/**
	 * test getCount().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>insert one element</li>
	 * <li>remove one element</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>1</li>
	 * <li>0</li>
	 * </ul>
	 * 
	 */
	public void testGetCount() {
		assertTrue(nameSpace.getCount() == 0);

		design.setName(reportString);
		nameSpace.insert(design);
		assertTrue(nameSpace.getCount() == 1);

		nameSpace.remove(design);
		assertTrue(nameSpace.getCount() == 0);
	}

	/**
	 * test getElements( ). Test Case:
	 * <ul>
	 * <li>insert three by default names and get them one by one</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>The order excepted is the same as the creation order.</li>
	 * </ul>
	 * 
	 * @throws Exception
	 * 
	 */

	public void testGetElementsSequence() throws Exception {
		DataSourceHandle ds = (DataSourceHandle) designHandle.getElementFactory().newOdaDataSource("Data Source", null); //$NON-NLS-1$
		designHandle.getDataSources().add(ds);

		ds = (DataSourceHandle) designHandle.getElementFactory().newOdaDataSource("Data Source1", null); //$NON-NLS-1$
		designHandle.getDataSources().add(ds);

		ds = (DataSourceHandle) designHandle.getElementFactory().newOdaDataSource("Data Source2", null); //$NON-NLS-1$
		designHandle.getDataSources().add(ds);

		List dataSources = designHandle.getAllDataSources();
		assertTrue(dataSources.size() == 3);
		assertEquals("Data Source", ((DataSourceHandle) dataSources.get(0)) //$NON-NLS-1$
				.getName());
		assertEquals("Data Source1", ((DataSourceHandle) dataSources.get(1)) //$NON-NLS-1$
				.getName());
		assertEquals("Data Source2", ((DataSourceHandle) dataSources.get(2)) //$NON-NLS-1$
				.getName());
	}
}