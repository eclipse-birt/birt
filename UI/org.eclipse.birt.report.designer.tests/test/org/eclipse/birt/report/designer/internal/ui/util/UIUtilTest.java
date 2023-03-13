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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.swt.layout.GridLayout;

/**
 * Non-UI tests for UIUtil
 */

public class UIUtilTest extends BaseTestCase {

	/*
	 * Class under test for GridLayout createGridLayoutWithoutMargin()
	 */
	public void testCreateGridLayoutWithoutMargin() {
		GridLayout layout = UIUtil.createGridLayoutWithoutMargin();
		assertEquals(false, layout.makeColumnsEqualWidth);
		assertEquals(1, layout.numColumns);
		assertEquals(0, layout.marginHeight);
		assertEquals(0, layout.marginWidth);
	}

	/*
	 * Class under test for GridLayout createGridLayoutWithoutMargin(int, boolean)
	 */
	public void testCreateGridLayoutWithoutMarginintboolean() {
		GridLayout layout = UIUtil.createGridLayoutWithoutMargin(5, false);
		assertEquals(false, layout.makeColumnsEqualWidth);
		assertEquals(5, layout.numColumns);
		assertEquals(0, layout.marginHeight);
		assertEquals(0, layout.marginWidth);

		layout = UIUtil.createGridLayoutWithoutMargin(3, true);
		assertEquals(true, layout.makeColumnsEqualWidth);
		assertEquals(3, layout.numColumns);
		assertEquals(0, layout.marginHeight);
		assertEquals(0, layout.marginWidth);

	}

	public void testConvertToGUIString() {
		assertEquals("testString", UIUtil.convertToGUIString("testString"));
		assertEquals(" testString ", UIUtil.convertToGUIString(" testString "));
		assertEquals("", UIUtil.convertToGUIString(""));
		assertEquals("", UIUtil.convertToGUIString(null));
	}

	public void testConvertToModelString() {

		assertEquals(" testString ", UIUtil.convertToModelString(" testString ", false));
		assertEquals("testString", UIUtil.convertToModelString(" testString ", true));
		assertEquals(null, UIUtil.convertToModelString("", false));
		assertEquals(null, UIUtil.convertToModelString(null, true));
	}

//	public void testCreateGroup( )
//	{
//		TableHandle table = getReportDesignHandle( ).getElementFactory( )
//				.newTableItem( null, 3 );
//		assertTrue( UIUtil.createGroup( table ) );
//		assertTrue( UIUtil.createGroup( table ) );
//		assertTrue( UIUtil.createGroup( table, 2 ) );
//		assertEquals( 3, table.getGroups( ).getCount( ) );
//
//		ListHandle list = getReportDesignHandle( ).getElementFactory( )
//				.newList( null );
//		assertTrue( UIUtil.createGroup( list ) );
//		assertTrue( UIUtil.createGroup( list ) );
//		assertTrue( UIUtil.createGroup( list, 1 ) );
//		assertEquals( 3, list.getGroups( ).getCount( ) );
//	}
}
