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

package org.eclipse.birt.report.model.library;

import com.ibm.icu.util.ULocale;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the properties of library in reading and writing. This test case also
 * includes testing semantic check.
 */

public class LibraryCompoundElementTest extends BaseTestCase
{

	private final static String INPUT1 = "DesignWithLibraryCompoundElement.xml"; //$NON-NLS-1$
	private final static String OUTPUT_FILE = "DesignWithLibraryCompoundElement_out.xml"; //$NON-NLS-1$
	private final static String GOLDEN_FILE = "DesignWithLibraryCompoundElement_golden.xml"; //$NON-NLS-1$

	/**
	 * Tests all properties and slots. Design extends an element from library.
	 * 
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testParser( ) throws Exception
	{
		openDesign( INPUT1, ULocale.ENGLISH );

		// 1. test child element properties.

		TableHandle bodyTable = (TableHandle) designHandle
				.findElement( "table1" ); //$NON-NLS-1$
		assertEquals( "New Design Table", bodyTable.getCaption( ) ); //$NON-NLS-1$
		assertEquals(
				"blue", bodyTable.getStringProperty( StyleHandle.COLOR_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"bolder", bodyTable.getStringProperty( StyleHandle.FONT_WEIGHT_PROP ) ); //$NON-NLS-1$

		// 2. test virtual element properties.

		TableRow row = (TableRow) bodyTable.getElement( ).getSlot(
				TableItem.HEADER_SLOT ).getContent( 0 );

		GroupHandle group1 = (GroupHandle) bodyTable.getGroups( ).get( 0 );
		assertEquals( "libTable1Group1", group1.getName( ) ); //$NON-NLS-1$
		
		// Get property from it self.
		assertEquals( "blue", row.getStringProperty( design, //$NON-NLS-1$
				Style.COLOR_PROP ) );

		// Get property from local style
		assertEquals(
				"Arial", row.getStringProperty( design, StyleHandle.FONT_FAMILY_PROP ) ); //$NON-NLS-1$

		// Get property from virtual parent
		assertEquals(
				"20pt", row.getStringProperty( design, RowHandle.HEIGHT_PROP ) ); //$NON-NLS-1$

		// 3. Table inside a cell.

		CellHandle bodyCell = (CellHandle) bodyTable.getDetail( ).get( 1 )
				.getSlot( TableRow.CONTENT_SLOT ).get( 0 );

		TableHandle bodyInnerTable = (TableHandle) bodyCell.getContent( ).get(
				0 );
		RowHandle bodyInnerRow = (RowHandle) bodyInnerTable.getHeader( )
				.get( 0 );
		CellHandle bodyInnerCell = (CellHandle) bodyInnerRow.getCells( )
				.get( 0 );
		assertEquals( ColorPropertyType.LIME, bodyInnerCell.getElement( )
				.getLocalProperty( design, Style.COLOR_PROP ) );

		// test properties related to the table layout.

		assertEquals( "Arial", bodyInnerCell //$NON-NLS-1$
				.getProperty( Style.FONT_FAMILY_PROP ) );

		// color should be aqua

		bodyCell = (CellHandle) bodyTable.getDetail( ).get( 1 ).getSlot(
				TableRow.CONTENT_SLOT ).get( 1 );
		assertEquals( ColorPropertyType.AQUA, bodyCell
				.getProperty( Style.COLOR_PROP ) );

		// test a lable that is a virtual element. And its virtual parent
		// extends from a library label.

		ListHandle list = (ListHandle) designHandle.findElement( "list1" ); //$NON-NLS-1$
		LabelHandle label = (LabelHandle) list.getDetail( ).get( 0 );

		assertEquals( "base label in library", label.getText( ) ); //$NON-NLS-1$
	}

	/**
	 * Tests writing the properties.
	 * 
	 * @throws Exception
	 *             if any error found.
	 */

	public void testWriter( ) throws Exception
	{
		openDesign( INPUT1, ULocale.ENGLISH );

		// verify the overridden color in the design.

		TableHandle bodyTable = (TableHandle) designHandle
				.findElement( "table1" ); //$NON-NLS-1$
		assertEquals( "New Design Table", bodyTable.getCaption( ) ); //$NON-NLS-1$

		RowHandle bodyRow = (RowHandle) bodyTable.getDetail( ).get( 1 );
		bodyRow.getPrivateStyle( ).getColor( ).setStringValue(
				ColorPropertyType.FUCHSIA );
		bodyRow.getHeight( ).setAbsolute( 1.1 );
		bodyRow.setBookmark( "http://www.eclipse.org/birt" ); //$NON-NLS-1$

		CellHandle bodyCell = (CellHandle) bodyRow.getCells( ).get( 0 );
		bodyCell.getPrivateStyle( ).getColor( ).setStringValue(
				ColorPropertyType.RED );

		TableHandle bodyInnerTable = (TableHandle) bodyCell.getContent( ).get(
				0 );
		bodyInnerTable.setName( "New Table" ); //$NON-NLS-1$
		bodyInnerTable.setStyleName( "new_style" ); //$NON-NLS-1$
		RowHandle bodyInnerRow = (RowHandle) bodyInnerTable.getHeader( )
				.get( 0 );
		CellHandle bodyInnerCell = (CellHandle) bodyInnerRow.getCells( )
				.get( 0 );

		bodyInnerCell.getPrivateStyle( ).getColor( ).setStringValue(
				ColorPropertyType.NAVY );

		// ensure that style and name is written out.

		saveAs( OUTPUT_FILE );
		assertTrue( compareTextFile( GOLDEN_FILE, OUTPUT_FILE ) );
	}
}