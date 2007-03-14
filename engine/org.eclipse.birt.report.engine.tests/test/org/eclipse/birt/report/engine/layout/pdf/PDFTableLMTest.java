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

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;


public class PDFTableLMTest extends PDFLayoutTest
{

	public void testFixedTableLayout() throws EngineException
	{
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/tableFixedLayout.xml";
		IReportRunnable report = openReportDesign( designFile );
		List pageAreas = getPageAreas( report );
		
 		assertEquals( 1, pageAreas.size( ) );
		PageArea pageArea = (PageArea)pageAreas.get( 0 );
		ContainerArea body = (ContainerArea)pageArea.getBody( );
		assertTrue(body.getChildrenCount( )==11);
		
		Iterator iter = body.getChildren( );
		
		
		TableArea table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{144, 288, 0});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{144, 108, 180});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{144, 177, 111});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{144, 72, 216});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{108, 108, 216});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{144, 208, 80});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{432, 0, 0});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{216, 432, 0});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{216, 432, 0});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{216, 216, 0});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{50, 10, 40});
	}
	
	
	private void validateColumnWidth(TableArea table, int[] cols)
	{
		assertTrue(table!=null);
		assertTrue(table.getChildrenCount( )>0);
		RowArea row = (RowArea)table.getChildren( ).next( );
		Iterator iter = row.getChildren( );
		for(int i=0; i<cols.length; i++)
		{
			CellArea cell = (CellArea)iter.next();
			assertEquals(new Integer(cols[i]), new Integer((cell.getWidth()+499)/1000));
		}
			
	}
	
}