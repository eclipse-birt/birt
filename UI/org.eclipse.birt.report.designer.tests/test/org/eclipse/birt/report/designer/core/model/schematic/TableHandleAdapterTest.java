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

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.Iterator;
import java.util.Locale;

import junit.framework.TestCase;

import org.eclipse.birt.report.designer.core.model.ReportItemtHandleAdapter;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;

/**
 * Tests for TableHandleAdapter
 */
public class TableHandleAdapterTest extends TestCase
{

	private SessionHandle sessionHandle;

	private ReportDesignHandle designHandle;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		sessionHandle = DesignEngine.newSession( Locale.getDefault( ) );

		designHandle = sessionHandle.createDesign( );

	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
	}

	public void testGetBackGroundColor( )
	{
		TableHandle table = designHandle.getElementFactory( )
				.newTableItem( "table" );
		StyleHandle styleHandle = designHandle.getElementFactory( )
				.newStyle( "wang" );

		//table.setStyleElement(styleHandle.getElement());
		( (StyledElement) table.getElement( ) ).setStyle( (StyleElement) styleHandle.getElement( ) );

		styleHandle = table.getStyle( );

		ReportItemtHandleAdapter adapter = new TableHandleAdapter( table, null );
		;

		int color = adapter.getBackgroundColor( adapter.getHandle( ) );
		// if the back ground was not set , return 0xffffff
		assertEquals( 0xffffff, color );

		try
		{
			styleHandle.setProperty( Style.BACKGROUND_COLOR_PROP, "0xff1234" );
		}
		catch ( SemanticException e )
		{
			fail( "error set background color" );
			e.printStackTrace( );
		}

		adapter = new TableHandleAdapter( table, null );
		;
		color = adapter.getBackgroundColor( adapter.getHandle( ) );
		assertEquals( 0xff1234, color );
	}

	public void testGetForeGroundColor( )
	{
		TableHandle table = designHandle.getElementFactory( )
				.newTableItem( "table" );
		StyleHandle styleHandle = designHandle.getElementFactory( )
				.newStyle( "wang" );

		//table.setStyleElement(styleHandle.getElement());
		( (StyledElement) table.getElement( ) ).setStyle( (StyleElement) styleHandle.getElement( ) );

		styleHandle = table.getStyle( );

		ReportItemtHandleAdapter adapter = new TableHandleAdapter( table, null );

		int color = adapter.getForegroundColor( adapter.getHandle( ) );
		// if the foreground was not set , return 0x000000
		assertEquals( 0x00, color );

		try
		{
			styleHandle.setProperty( "color", "0xff1234" );
		}
		catch ( SemanticException e )
		{
			fail( "error set  color" );
			e.printStackTrace( );
		}

		adapter = new TableHandleAdapter( table, null );
		;

		color = adapter.getForegroundColor( adapter.getHandle( ) );
		assertEquals( 0xff1234, color );
	}

	/*
	 * public void testGetLocation( ) { TableHandle table =
	 * designHandle.getElementFactory( ) .newTableItem( "table" );
	 * ReportItemtHandleAdapter adapter = new TableHandleAdapter( table, null );
	 * assertEquals( 0, adapter.getLocation( ).x ); assertEquals( 0,
	 * adapter.getLocation( ).y );
	 * 
	 * table = designHandle.getElementFactory( ).newTableItem( "table" );
	 * 
	 * table.setX( 100.456 ); table.setY( 100.456 ); adapter = new
	 * TableHandleAdapter( table, null );
	 * 
	 * assertEquals( MetricUtility.inchToPixel( 100.456, 100.456 ).x,
	 * adapter.getLocation( ).x ); assertEquals( MetricUtility.inchToPixel(
	 * 100.456, 100.456 ).y, adapter.getLocation( ).y ); }
	 */
	/*
	 * public void testGetDimension( ) { TableHandle table =
	 * designHandle.getElementFactory( ) .newTableItem( "table" );
	 * ReportItemtHandleAdapter adapter = new TableHandleAdapter( table, null );
	 * //if no size was set, return default value assertEquals(
	 * MetricUtility.inchToPixel( 100, 100 ).x, adapter.getSize( ).width );
	 * assertEquals( MetricUtility.inchToPixel( 100, 100 ).y, adapter.getSize(
	 * ).height );
	 * 
	 * table = designHandle.getElementFactory( ).newTableItem( "table" );
	 * 
	 * table.setWidth( 100.456 ); table.setHeight( 200.456 ); adapter = new
	 * TableHandleAdapter( table, null );
	 * 
	 * assertEquals( MetricUtility.inchToPixel( 100.456, 200.456 ).x,
	 * adapter.getSize( ).width ); assertEquals( MetricUtility.inchToPixel(
	 * 100.456, 200.456 ).y, adapter.getSize( ).height ); }
	 */

	public void testGetChildren( )
	{
		TableHandle table = designHandle.getElementFactory( )
				.newTableItem( "table" );
		ReportItemtHandleAdapter adapter = new TableHandleAdapter( table, null );
		SlotHandle slot = table.getSlot( ListingElement.HEADER_SLOT );
		RowHandle row = designHandle.getElementFactory( ).newTableRow( );
		CellHandle cell = designHandle.getElementFactory( ).newCell( );
		try
		{
			slot.add( row );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );
		}
		catch ( Exception ex )
		{

			ex.printStackTrace( );
			fail( "error add cell to table" );
		}

		slot = table.getSlot( ListingElement.GROUP_SLOT );

		TableGroup group = new TableGroup( );
		try
		{
			row = designHandle.getElementFactory( ).newTableRow( );
			cell = designHandle.getElementFactory( ).newCell( );

			slot.add( group.handle( designHandle.getDesign( ) ) );

			//			group.getSlot(GroupElement.HEADER_SLOT).add(row.getElement());
			//			row.getSlot(TableRow.CONTENT_SLOT).add(cell);

			row = designHandle.getElementFactory( ).newTableRow( );
			cell = designHandle.getElementFactory( ).newCell( );

			group.getSlot( GroupElement.FOOTER_SLOT ).add( row.getElement( ) );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );

			cell = designHandle.getElementFactory( ).newCell( );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );

		}
		catch ( Exception ex )
		{

			ex.printStackTrace( );
			fail( "error add cell to table" );
		}

		slot = table.getSlot( ListingElement.DETAIL_SLOT );
		row = designHandle.getElementFactory( ).newTableRow( );
		cell = designHandle.getElementFactory( ).newCell( );
		try
		{
			slot.add( row );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( "error add cell to table" );
		}

		slot = table.getSlot( ListingElement.FOOTER_SLOT );
		row = designHandle.getElementFactory( ).newTableRow( );
		cell = designHandle.getElementFactory( ).newCell( );
		try
		{
			slot.add( row );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );
		}
		catch ( Exception ex )
		{

			ex.printStackTrace( );
			fail( "error add cell to table" );
		}

		adapter = new TableHandleAdapter( table, null );

		for ( Iterator it = adapter.getChildren( ).iterator( ); it.hasNext( ); )
		{
			Object obj = it.next( );
			assertTrue( obj instanceof CellHandle );
		}
	}

	/*
	 * public void testGetRows( ) { TableHandle table =
	 * designHandle.getElementFactory( ) .newTableItem( "table" );
	 * TableHandleAdapter adapter = new TableHandleAdapter( table, null );
	 * SlotHandle slot = table.getSlot( ListingElement.HEADER_SLOT ); RowHandle
	 * row = designHandle.getElementFactory( ).newTableRow( ); CellHandle cell =
	 * designHandle.getElementFactory( ).newCell( ); try { slot.add( row ); }
	 * catch ( Exception ex ) {
	 * 
	 * ex.printStackTrace( ); fail( "error add cell to table" ); }
	 * 
	 * slot = table.getSlot( ListingElement.GROUP_SLOT );
	 * 
	 * TableGroup group = new TableGroup( ); try { row =
	 * designHandle.getElementFactory( ).newTableRow( );
	 * 
	 * slot.add( group.handle( designHandle.getDesign( ) ) ); //
	 * group.getSlot(GroupElement.HEADER_SLOT).add(row.getElement()); //
	 * row.getSlot(TableRow.CONTENT_SLOT).add(cell);
	 * 
	 * row = designHandle.getElementFactory( ).newTableRow( );
	 * 
	 * group.getSlot( GroupElement.FOOTER_SLOT ).add( row.getElement( ) ); }
	 * catch ( Exception ex ) {
	 * 
	 * ex.printStackTrace( ); fail( "error add cell to table" ); }
	 * 
	 * slot = table.getSlot( ListingElement.DETAIL_SLOT ); row =
	 * designHandle.getElementFactory( ).newTableRow( ); try { slot.add( row ); }
	 * catch ( Exception ex ) {
	 * 
	 * ex.printStackTrace( ); fail( "error add cell to table" ); }
	 * 
	 * slot = table.getSlot( ListingElement.FOOTER_SLOT ); try { row =
	 * designHandle.getElementFactory( ).newTableRow( ); log.debug( "the row is " +
	 * row.getID( ) ); slot.add( row ); row = designHandle.getElementFactory(
	 * ).newTableRow( ); log.debug( "the row is " + row.getID( ) ); slot.add(
	 * row ); } catch ( Exception ex ) {
	 * 
	 * ex.printStackTrace( ); fail( "error add cell to table" ); }
	 * 
	 * for ( Iterator it = adapter.getRows( ).iterator( ); it.hasNext( ); ) {
	 * log.debug( "the row is " + ( (TableRow) it.next( ) ).getID( ) ); } }
	 */
	/*
	 * public void testGetRow( ) { TableHandle table =
	 * designHandle.getElementFactory( ) .newTableItem( "table" );
	 * TableHandleAdapter adapter = new TableHandleAdapter( table, null );
	 * SlotHandle slot = table.getSlot( ListingElement.HEADER_SLOT ); RowHandle
	 * row = designHandle.getElementFactory( ).newTableRow( ); CellHandle cell =
	 * designHandle.getElementFactory( ).newCell( ); try { slot.add( row ); }
	 * catch ( Exception ex ) {
	 * 
	 * ex.printStackTrace( ); fail( "error add cell to table" ); }
	 * 
	 * slot = table.getSlot( ListingElement.GROUP_SLOT );
	 * 
	 * TableGroup group = new TableGroup( ); try { row =
	 * designHandle.getElementFactory( ).newTableRow( );
	 * 
	 * slot.add( group.handle( designHandle.getDesign( ) ) ); //
	 * group.getSlot(GroupElement.HEADER_SLOT).add(row.getElement()); //
	 * row.getSlot(TableRow.CONTENT_SLOT).add(cell);
	 * 
	 * row = designHandle.getElementFactory( ).newTableRow( );
	 * 
	 * group.getSlot( GroupElement.FOOTER_SLOT ).add( row.getElement( ) ); }
	 * catch ( Exception ex ) {
	 * 
	 * ex.printStackTrace( ); fail( "error add cell to table" ); }
	 * 
	 * slot = table.getSlot( ListingElement.DETAIL_SLOT ); row =
	 * designHandle.getElementFactory( ).newTableRow( ); try { slot.add( row ); }
	 * catch ( Exception ex ) {
	 * 
	 * ex.printStackTrace( ); fail( "error add cell to table" ); }
	 * 
	 * slot = table.getSlot( ListingElement.FOOTER_SLOT ); try { row =
	 * designHandle.getElementFactory( ).newTableRow( ); log.debug( "the row is " +
	 * row.getID( ) ); slot.add( row ); row = designHandle.getElementFactory(
	 * ).newTableRow( ); log.debug( "the row is " + row.getID( ) ); slot.add(
	 * row ); } catch ( Exception ex ) {
	 * 
	 * ex.printStackTrace( ); fail( "error add cell to table" ); }
	 * assertNotNull( adapter.getRow( 2 ) ); log.debug( "the row is " +
	 * adapter.getRow( 2 ) ); assertNull( "how can you get row?",
	 * adapter.getRow( 100 ) ); }
	 */
	public void testGetColumns( )
	{
		TableHandle table = designHandle.getElementFactory( )
				.newTableItem( "table" );
		TableHandleAdapter adapter = new TableHandleAdapter( table, null );
		SlotHandle slot = table.getSlot( ListingElement.HEADER_SLOT );
		RowHandle row = designHandle.getElementFactory( ).newTableRow( );
		CellHandle cell = designHandle.getElementFactory( ).newCell( );

		try
		{
			cell.setIntProperty( Cell.COLUMN_PROP, 1 );
			slot.add( row );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( "error add cell to table" );
		}

		slot = table.getSlot( ListingElement.GROUP_SLOT );

		TableGroup group = new TableGroup( );
		try
		{
			row = designHandle.getElementFactory( ).newTableRow( );
			cell = designHandle.getElementFactory( ).newCell( );

			slot.add( group.handle( designHandle.getDesign( ) ) );

			//			group.getSlot(GroupElement.HEADER_SLOT).add(row.getElement());
			//			row.getSlot(TableRow.CONTENT_SLOT).add(cell);

			row = designHandle.getElementFactory( ).newTableRow( );
			cell = designHandle.getElementFactory( ).newCell( );
			cell.setIntProperty( Cell.COLUMN_PROP, 1 );

			group.getSlot( GroupElement.FOOTER_SLOT ).add( row.getElement( ) );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );

			cell = designHandle.getElementFactory( ).newCell( );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );
			cell.setIntProperty( Cell.COLUMN_PROP, 1 );

		}
		catch ( Exception ex )
		{

			ex.printStackTrace( );
			fail( "error add cell to table" );
		}

		slot = table.getSlot( ListingElement.DETAIL_SLOT );
		ColumnHandle column = designHandle.getElementFactory( )
				.newTableColumn( );
		row = designHandle.getElementFactory( ).newTableRow( );
		cell = designHandle.getElementFactory( ).newCell( );

		SlotHandle columnSlot = table.getSlot( TableItem.COLUMN_SLOT );
		try
		{
			slot.add( row );
			columnSlot.add( column );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );
			cell.setIntProperty( Cell.COLUMN_PROP, 1 );
		}
		catch ( Exception ex )
		{

			ex.printStackTrace( );
			fail( "error add cell to table" );
		}

		slot = table.getSlot( ListingElement.FOOTER_SLOT );
		row = designHandle.getElementFactory( ).newTableRow( );
		cell = designHandle.getElementFactory( ).newCell( );
		try
		{
			slot.add( row );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );
			cell.setIntProperty( Cell.COLUMN_PROP, 1 );

		}
		catch ( Exception ex )
		{

			ex.printStackTrace( );
			fail( "error add cell to table" );
		}

		adapter = new TableHandleAdapter( table, null );

		for ( Iterator it = adapter.getChildren( ).iterator( ); it.hasNext( ); )
		{
			Object obj = it.next( );
			assertTrue( obj instanceof CellHandle );
		}

		assertEquals( 1, adapter.getColumns( ).size( ) );

	}

	public void testGetColumn( )
	{
		TableHandle table = designHandle.getElementFactory( )
				.newTableItem( "table" );
		TableHandleAdapter adapter = new TableHandleAdapter( table, null );
		SlotHandle slot = table.getSlot( ListingElement.HEADER_SLOT );
		RowHandle row = designHandle.getElementFactory( ).newTableRow( );
		CellHandle cell = designHandle.getElementFactory( ).newCell( );

		try
		{
			cell.setIntProperty( Cell.COLUMN_PROP, 1 );
			slot.add( row );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );
		}
		catch ( Exception ex )
		{

			ex.printStackTrace( );
			fail( "error add cell to table" );
		}

		slot = table.getSlot( ListingElement.GROUP_SLOT );

		TableGroup group = new TableGroup( );
		try
		{
			row = designHandle.getElementFactory( ).newTableRow( );
			cell = designHandle.getElementFactory( ).newCell( );

			slot.add( group.handle( designHandle.getDesign( ) ) );

			//			group.getSlot(GroupElement.HEADER_SLOT).add(row.getElement());
			//			row.getSlot(TableRow.CONTENT_SLOT).add(cell);

			row = designHandle.getElementFactory( ).newTableRow( );
			cell = designHandle.getElementFactory( ).newCell( );
			cell.setIntProperty( Cell.COLUMN_PROP, 1 );

			group.getSlot( GroupElement.FOOTER_SLOT ).add( row.getElement( ) );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );

			cell = designHandle.getElementFactory( ).newCell( );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );
			cell.setIntProperty( Cell.COLUMN_PROP, 1 );

		}
		catch ( Exception ex )
		{

			ex.printStackTrace( );
			fail( "error add cell to table" );
		}

		slot = table.getSlot( ListingElement.DETAIL_SLOT );
		ColumnHandle column = designHandle.getElementFactory( )
				.newTableColumn( );
		row = designHandle.getElementFactory( ).newTableRow( );
		cell = designHandle.getElementFactory( ).newCell( );

		SlotHandle columnSlot = table.getSlot( TableItem.COLUMN_SLOT );
		try
		{
			slot.add( row );
			columnSlot.add( column );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );
			cell.setIntProperty( Cell.COLUMN_PROP, 1 );

		}
		catch ( Exception ex )
		{

			ex.printStackTrace( );
			fail( "error add cell to table" );
		}

		slot = table.getSlot( ListingElement.FOOTER_SLOT );
		row = designHandle.getElementFactory( ).newTableRow( );
		cell = designHandle.getElementFactory( ).newCell( );
		try
		{
			slot.add( row );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );
			cell.setIntProperty( Cell.COLUMN_PROP, 1 );

		}
		catch ( Exception ex )
		{

			ex.printStackTrace( );
			fail( "error add cell to table" );
		}

		adapter = new TableHandleAdapter( table, null );

		for ( Iterator it = adapter.getChildren( ).iterator( ); it.hasNext( ); )
		{
			Object obj = it.next( );
			assertTrue( obj instanceof CellHandle );
		}

		assertNotNull( adapter.getColumn( 1 ) );
		assertNull( "how can you get this column", adapter.getColumn( 2 ) );
	}

}