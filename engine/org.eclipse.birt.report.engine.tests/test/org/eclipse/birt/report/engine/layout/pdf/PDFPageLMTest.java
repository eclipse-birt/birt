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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;


public class PDFPageLMTest extends PDFLayoutTest
{
	private List getPages(boolean fitToPage, boolean pagebreakPaginationOnly ) throws EngineException
	{
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/fitToPage.xml";
		IReportRunnable report = openReportDesign( designFile );
		List pageAreas = new ArrayList();
		IEmitterMonitor monitor = new PageMonitor(pageAreas);
		IRunAndRenderTask runAndRenderTask = new TestRunAndRenderTask( engine,
				report, monitor );
		PDFRenderOption options = createRenderOption();
		options.setOption( IPDFRenderOption.FIT_TO_PAGE, new Boolean(fitToPage) );
		options.setOption( IPDFRenderOption.PAGEBREAK_PAGINATION_ONLY, new Boolean(pagebreakPaginationOnly) );
		runAndRenderTask.setRenderOption( options );
		runAndRenderTask.run( );
		runAndRenderTask.close( );
		return pageAreas;
	}
	
	
	public void testPagebreakPaginationOnlyFalse() throws EngineException
	{
		assertEquals( 9, getPages(false, false).size( ) );
	}
	
	public void testPagebreakPaginationOnlyTrue() throws EngineException
	{
		assertEquals( 4, getPages(false, true).size( ) );
	}
	
	
	
	
	
	
	public void testFitToPageFalse() throws EngineException
	{
		List pages = getPages(false, false);
		for(int i=0; i<pages.size( ); i++)
		{
			PageArea page = (PageArea) pages.get( i );
			assertEquals(new Float(1.0f), new Float(page.getScale()));
		}
		
		pages = getPages(false, true);
		for(int i=0; i<pages.size( ); i++)
		{
			PageArea page = (PageArea) pages.get( i );
			assertEquals(new Float(1.0f), new Float(page.getScale()));
		}
	}
	
	public void testFitToPageTrue() throws EngineException
	{
		List pages = getPages(true, false);
		float[] scales = new float[]{0.75f, 0.75f, 0.75f, 0.75f, 0.75f, 0.75f, 0.75f, 0.75f, 0.75f};
		assertTrue(pages.size( )==scales.length);
		for(int i=0; i<pages.size( ); i++)
		{
			PageArea page = (PageArea) pages.get( i );
			assertEquals(new Float(scales[i]), new Float(page.getScale( )));
		}
		
		pages = getPages(true, true);
		scales = new float[]{0.75f, 0.75f, 0.75f, 0.227f};
		assertTrue(pages.size( )==scales.length);
		for(int i=0; i<pages.size( ); i++)
		{
			PageArea page = (PageArea) pages.get( i );
			float delta = scales[i]-page.getScale( );
			assertTrue(delta>-0.001 && delta<0.001);
		}
	}


}
