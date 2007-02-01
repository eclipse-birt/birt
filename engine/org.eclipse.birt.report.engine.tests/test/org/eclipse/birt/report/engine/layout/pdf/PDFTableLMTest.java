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
		assertTrue(body.getChildrenCount( )==10);
		
		Iterator iter = body.getChildren( );
		
		
		TableArea table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{144000, 288000, 0});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{144000, 108000, 180000});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{144000, 177230, 110769});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{144000, 144000, 144000});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{108000, 108000, 216000});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{144000, 208000, 80000});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{432000, 0, 0});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{216000, 432000, 0});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{216000, 432000, 0});
		
		table = (TableArea) iter.next( );
		validateColumnWidth(table, new int[]{216000, 216000, 0});
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
			assertEquals(new Integer(cols[i]), new Integer(cell.getWidth()));
		}
			
	}
	
}