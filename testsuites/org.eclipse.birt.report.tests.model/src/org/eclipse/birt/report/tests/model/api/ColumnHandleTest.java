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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Test cases for property search algorithm of cell elements.
 *  
 */

public class ColumnHandleTest extends BaseTestCase
{
 
//	 define two input files
	final static String INPUT = "ColumnHandleTest.xml";
	
	
	//String fileName = "ColumnHandleTest.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */

	public ColumnHandleTest(String name){
		super(name);
	}
	public static Test suite(){
		
		return new TestSuite(ColumnHandleTest.class);
	}
	
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		
		// retrieve two input files from tests-model.jar file
		copyResource_INPUT( INPUT , INPUT );
	}

	/**
	 * Tests to get a style property of a cell element in the table.
	 * 
	 * @throws Exception
	 */

	public void testGetColumnProperty( ) throws Exception
	{

		openDesign( INPUT );

		// style property inherited from cell, row, column, table element.

		// color defined on the cell.

		TableHandle table = (TableHandle) designHandle
				.findElement( "My Table" ); //$NON-NLS-1$
		assertNotNull("should not be null", table);
		
		ColumnHandle column = (ColumnHandle)table.getColumns().get(0);
		column.setRepeatCount(2);
		assertEquals(2,column.getRepeatCount());
		
		DimensionHandle dh = column.getWidth();
		assertEquals(100, dh.getMeasure(),0);
		assertEquals("pt",dh.getUnits());
        
        //suppressDuplicates Property
        assertFalse(column.suppressDuplicates());
        column.setSuppressDuplicates(true);
        assertTrue(column.suppressDuplicates());
        designHandle.getCommandStack().undo();
        assertFalse(column.suppressDuplicates());
        designHandle.getCommandStack().redo();
        assertTrue(column.suppressDuplicates());
        
        ElementFactory factory = new ElementFactory(designHandle.getModule());
        GridHandle grid = factory.newGridItem("mygrid",3,3);
        ColumnHandle gridcolumn = (ColumnHandle) grid.getColumns().get(0);
        assertFalse(gridcolumn.suppressDuplicates());
    
 }
}