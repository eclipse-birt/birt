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

package org.eclipse.birt.report.tests.model.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.table.LayoutTable;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * The test case of the method <code>getColumnCount</code> in
 * <code>TableItemHandle</code>.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testGetColumnCount()}</td>
 * <td>Gets the maximal column count of tables with dropping headers.</td>
 * <td>Results match with the expected.</td>
 * </tr>
 * 
 * 
 * @see TableItem
 */

public class TableItemHandleTest extends BaseTestCase
{

	/**
	 * @param name
	 */
	public TableItemHandleTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	String fileName = "TableItemHandleTest.xml"; //$NON-NLS-1$

	
	public static Test suite(){
		return new TestSuite(TableItemHandleTest.class);
		
	}
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		copyResource_INPUT( fileName , fileName );
	}
	
	public void tearDown( )
	{
		removeResource( );
	}

	/**
	 * 
	 * @throws Exception
	 *  
	 */

	public void testGetColumnCount( ) throws Exception
	{
		openDesign( fileName );

		TableHandle table = (TableHandle) designHandle
				.findElement( "My table1" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table2" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table3" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table4" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table5" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table7" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		// one with semantic error.

		table = (TableHandle) designHandle.findElement( "My table8" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 5, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table9" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table10" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table11" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table12" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table13" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table14" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table15" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table16" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table17" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table18" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 1, table.getColumnCount( ) );

		// one with semantic error.

		table = (TableHandle) designHandle.findElement( "My table19" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 1, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table20" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 6, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table21" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 6, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table22" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table23" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table24" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table25" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table26" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table27" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table28" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table29" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 20, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table30" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 20, table.getColumnCount( ) );

		assertEquals( 0, design.getErrorList( ).size( ) ); // in my table 8
	}

	/**
	 * Test the table layout.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testInsertGroup( ) throws Exception
	{
		SessionHandle session = DesignEngine.newSession( ULocale.ENGLISH );
		designHandle = session.createDesign( );
		design = (ReportDesign) designHandle.getModule( );

		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"testTable", 3, 1, 1, 1 ); //$NON-NLS-1$

		// normal transaction without filtering events.

		try
		{
			designHandle.getCommandStack( ).startTrans( null );

			TableGroupHandle group = newTableGroup( 1, 1, 3 );

			// the layout has been updated.

			table.getGroups( ).add( group );

			// set the color of the first column as blue

			table.getColumns( ).get( 0 ).setProperty( IStyleModel.COLOR_PROP,
					ColorPropertyType.BLUE );

			CellHandle cell = (CellHandle) ( (RowHandle) group.getHeader( )
					.get( 0 ) ).getCells( ).get( 0 );
			assertEquals( ColorPropertyType.BLUE, cell
					.getProperty( IStyleModel.COLOR_PROP ) );

			designHandle.getCommandStack( ).commit( );
		}
		catch ( Exception e )
		{
			assert false;
		}

	}

	public void testSuppressDuplicatesProp( ) throws Exception
	{
		SessionHandle session = DesignEngine.newSession( ULocale.ENGLISH );
		designHandle = session.createDesign( );
		design = (ReportDesign) designHandle.getModule( );

		RowHandle row = designHandle.getElementFactory( ).newTableRow(3);
		assertFalse(row.suppressDuplicates());
		row.setSuppressDuplicates(true);
	    assertTrue(row.suppressDuplicates());
	    designHandle.getCommandStack().undo();
	    assertFalse(row.suppressDuplicates());
	    designHandle.getCommandStack().redo();
	    assertTrue(row.suppressDuplicates());
		
	}
	
	/**
	 * Returns a newly created the table group with the given header row number,
	 * footer row number and the column number.
	 * 
	 * @param headerRowNum
	 *            header row number
	 * @param footerRowNum
	 *            footer row number
	 * @param columnNum
	 *            the column number
	 * @return a <code>TableGroupHandle</code>
	 * @throws SemanticException
	 */

	private TableGroupHandle newTableGroup( int headerRowNum, int footerRowNum,
			int columnNum ) throws SemanticException
	{
		assert headerRowNum > 0 && footerRowNum > 0 && columnNum > 0;
		TableGroupHandle group = designHandle.getElementFactory( )
				.newTableGroup( );

		for ( int i = 0; i < headerRowNum; i++ )
			group.getHeader( ).add(
					designHandle.getElementFactory( ).newTableRow( columnNum ) );

		for ( int i = 0; i < footerRowNum; i++ )
			group.getFooter( ).add(
					designHandle.getElementFactory( ).newTableRow( columnNum ) );

		return group;
	}
}