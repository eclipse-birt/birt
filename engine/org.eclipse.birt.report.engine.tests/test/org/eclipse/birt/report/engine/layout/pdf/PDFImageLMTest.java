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
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;

public class PDFImageLMTest extends PDFLayoutTest
{
	/**
	 * Test case for bugzilla bug <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=168899">168899</a> :
	 * Report does not output to PDF with a chart inside of a grid.
	 * 
	 * @throws EngineException
	 */
	public void testOversizedImageInGrid() throws EngineException
	{
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/168899.xml";
		IReportRunnable report = openReportDesign( designFile );
		List pageAreas = getPageAreas( report );
		
 		assertEquals( 1, pageAreas.size( ) );
		PageArea pageArea = (PageArea)pageAreas.get( 0 );
		Iterator logicContainers = pageArea.getBody( ).getChildren( );
		assertTrue(logicContainers.hasNext());
		ContainerArea blockContainer = (ContainerArea) logicContainers
					.next( );
		assertTrue("Page body is not empty",!isEmpty( blockContainer ));
	}
	
	
}