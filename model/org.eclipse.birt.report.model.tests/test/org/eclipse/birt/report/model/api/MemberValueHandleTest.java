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

import java.util.List;

import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests for content element like memberValue that can have multiple levels.
 * 
 */

public class MemberValueHandleTest extends BaseTestCase {

	private static final String FILE_NAME = "MemberValueHandleTest.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_EXTENDS = "MemberValueHandleTest_1.xml"; //$NON-NLS-1$

	/**
	 * MemberValueHandle.add should not throw exception.
	 * 
	 * @throws Exception
	 */

	public void testMemberValue() throws Exception {
		openDesign(FILE_NAME);
		DesignElementHandle testTable = designHandle.findElement("testTable"); //$NON-NLS-1$
		assertNotNull(testTable);

		// test filter properties
		List valueList = testTable.getListProperty("filter"); //$NON-NLS-1$
		assertEquals(2, valueList.size());
		FilterConditionElementHandle filter = (FilterConditionElementHandle) valueList.get(0);

		// test member value in filter

		MemberValueHandle memberValue = filter.getMember();

		MemberValueHandle newValue = designHandle.getElementFactory().newMemberValue();
		newValue.setLevel(designHandle.findLevel("testDimension/testLevel_one")); //$NON-NLS-1$
		memberValue.add(MemberValueHandle.MEMBER_VALUES_PROP, newValue);

		filter.drop(FilterConditionElementHandle.MEMBER_PROP, 0);

		designHandle.getCommandStack().undo();

		newValue.drop();

	}

	/**
	 * When cube1 extends another cube2. Get access control from cube1.
	 * 
	 * <ul>
	 * <li>set permission
	 * <li>add role
	 * <li>remove role
	 * </ul>
	 * 
	 * will copies value access controls from the level of cube2 first. Then change
	 * the corresponding value. Undo is also tested.
	 * 
	 * @throws Exception
	 */

	public void testContentElementCommandOnMemberValue() throws Exception {
		openDesign(FILE_NAME_EXTENDS);

		DesignElementHandle testTable = designHandle.findElement("testTable"); //$NON-NLS-1$

		// test filter properties
		List valueList = testTable.getListProperty("filter"); //$NON-NLS-1$
		assertEquals(2, valueList.size());
		FilterConditionElementHandle filter = (FilterConditionElementHandle) valueList.get(0);

		// test member value in filter

		MemberValueHandle memberValue = filter.getMember();

		memberValue.setValue("new value 1"); //$NON-NLS-1$

		save();
		assertTrue(compareFile("MemberValueHandleTest_golden_1.xml"));//$NON-NLS-1$

		designHandle.getCommandStack().undo();

		save();
		assertTrue(compareFile("MemberValueHandleTest_golden_2.xml"));//$NON-NLS-1$

		valueList = (List) memberValue.getProperty(MemberValueHandle.MEMBER_VALUES_PROP);
		memberValue = (MemberValueHandle) valueList.get(0);

		memberValue.setValue("new nested value 1"); //$NON-NLS-1$

		save();
		assertTrue(compareFile("MemberValueHandleTest_golden_3.xml"));//$NON-NLS-1$

		designHandle.getCommandStack().undo();

		save();
		assertTrue(compareFile("MemberValueHandleTest_golden_4.xml"));//$NON-NLS-1$
	}

}
