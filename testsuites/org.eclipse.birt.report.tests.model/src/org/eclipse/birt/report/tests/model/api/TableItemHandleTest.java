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

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.table.LayoutTable;
import org.eclipse.birt.report.tests.model.BaseTestCase;

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
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table4" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 6, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table5" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 7, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table7" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		// one with semantic error.

		table = (TableHandle) designHandle.findElement( "My table8" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 5, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table9" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table10" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

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
		assertEquals( 5, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table15" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 6, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table16" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 8, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table17" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 7, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table18" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 1, table.getColumnCount( ) );

		// one with semantic error.

		table = (TableHandle) designHandle.findElement( "My table19" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 1, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table20" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 11, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table21" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 6, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table22" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 5, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table23" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 5, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table24" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table25" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table26" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table27" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

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

	public void testContentLayout( ) throws Exception
	{
		openDesign( fileName );

		PrintStream out = getOutputStream( "TableItemLayout_output.txt" );//$NON-NLS-1$

		TableHandle table = (TableHandle) designHandle
				.findElement( "My table1" ); //$NON-NLS-1$
		assertNotNull( table );

		LayoutTable layout = ( (TableItem) table.getElement( ) )
				.getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table2" ); //$NON-NLS-1$
		assertNotNull( table );
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table3" ); //$NON-NLS-1$
		assertNotNull( table );
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table10" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table11" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table16" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table17" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table20" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table21" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table22" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table24" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table25" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table28" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table30" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table31" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table32" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		table = (TableHandle) designHandle.findElement( "My table33" ); //$NON-NLS-1$
		layout = ( (TableItem) table.getElement( ) ).getRenderModel( design );
		out.print( layout.getLayoutString( table.getName( ) ) );

		assertTrue( compareTextFile( "TableItemLayout_golden.txt", //$NON-NLS-1$
				"TableItemLayout_output.txt" ) ); //$NON-NLS-1$.

		out.close( );
	}

	/**
	 * Returns a print stream for a given output file name. Note the output file
	 * is flushed in the default output directory.
	 * 
	 * @param filename
	 *            the file name
	 * @return the print stream for the given file
	 * @throws IOException
	 *             if any IO error occurs.
	 */

	private PrintStream getOutputStream( String filename ) throws IOException
	{
		String outputPath = PLUGIN_PATH + getClassFolder( ) + OUTPUT_FOLDER;
		File outputFolder = new File( outputPath );
		if ( !outputFolder.exists( ) && !outputFolder.mkdir( ) )
		{
			throw new IOException( "Can not create the output folder" ); //$NON-NLS-1$
		}

		File file = new File( outputFolder, filename );
		FileOutputStream fout = new FileOutputStream( file );
		PrintStream pout = new PrintStream( fout );
		return pout;

	}

	/**
	 * Inserts a new group into the table. This is one smoke test. If no
	 * exception is thrown, this operation is OK.
	 * 
	 */

	public void testInsertGroup( ) throws Exception
	{
		SessionHandle session = DesignEngine.newSession( null );
		designHandle = session.createDesign( );
		design = designHandle.getDesign( );

		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"testTable", 3, 1, 1, 1 );
		TableGroupHandle group = designHandle.getElementFactory( )
				.newTableGroup( );
		table.getGroups( ).add( group );
	}

}