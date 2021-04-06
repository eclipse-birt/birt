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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;

public class PDFPageLMTest extends PDFLayoutTest {
	private List getPages(boolean fitToPage, boolean pagebreakPaginationOnly) throws EngineException {
		PDFRenderOption options = createRenderOption();
		if (fitToPage) {
			options.setOption(IPDFRenderOption.PAGE_OVERFLOW, new Integer(IPDFRenderOption.FIT_TO_PAGE_SIZE));
		}
		options.setOption(IPDFRenderOption.PAGEBREAK_PAGINATION_ONLY, new Boolean(pagebreakPaginationOnly));

		return getPages(options);
	}

	private List getPages(int pageOverflow) throws EngineException {
		PDFRenderOption options = createRenderOption();
		options.setOption(IPDFRenderOption.PAGE_OVERFLOW, pageOverflow);
		return getPages(options);
	}

	private List getPages(PDFRenderOption options) throws EngineException {
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/fitToPage.xml";
		IReportRunnable report = openReportDesign(designFile);
		List pageAreas = new ArrayList();
		IEmitterMonitor monitor = new PageMonitor(pageAreas);
		IRunAndRenderTask runAndRenderTask = new TestRunAndRenderTask(engine, report, monitor);
		runAndRenderTask.setRenderOption(options);
		runAndRenderTask.run();
		runAndRenderTask.close();
		return pageAreas;
	}

	public void testPagebreakPaginationOnlyFalse() throws EngineException {
		assertEquals(8, getPages(false, false).size());
	}

	public void testPagebreakPaginationOnlyTrue() throws EngineException {
		assertEquals(4, getPages(false, true).size());
	}

	public void testPageBreakWithPageOverflow() throws EngineException {
		assertEquals(8, getPages(PDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES).size());
		assertEquals(4, getPages(PDFRenderOption.ENLARGE_PAGE_SIZE).size());
		assertEquals(4, getPages(PDFRenderOption.FIT_TO_PAGE_SIZE).size());
	}

	public void testFitToPageFalse() throws EngineException {
		List pages = getPages(false, false);
		for (int i = 0; i < pages.size(); i++) {
			PageArea page = (PageArea) pages.get(i);
			assertEquals(new Float(1.0f), new Float(page.getScale()));
		}

		pages = getPages(false, true);
		for (int i = 0; i < pages.size(); i++) {
			PageArea page = (PageArea) pages.get(i);
			assertEquals(new Float(1.0f), new Float(page.getScale()));
		}
	}

	public void testFitToPageTrue() throws EngineException {
		/*
		 * FIXME support this case List pages = getPages(true, false); float[] scales =
		 * new float[]{0.75f, 0.75f, 0.75f, 0.75f, 0.75f, 0.75f, 0.75f, 0.75f};
		 * assertTrue(pages.size( )==scales.length); for(int i=0; i<pages.size( ); i++)
		 * { PageArea page = (PageArea) pages.get( i ); assertEquals(new
		 * Float(scales[i]), new Float(page.getScale( ))); }
		 */

		List pages = getPages(true, true);
		float[] scales = new float[] { 0.75f, 0.75f, 0.75f, 0.24f };
		assertTrue(pages.size() == scales.length);
		for (int i = 0; i < pages.size(); i++) {
			PageArea page = (PageArea) pages.get(i);
			float delta = scales[i] - page.getScale();
			assertTrue(delta > -0.01 && delta < 0.01);
		}
	}

	/**
	 * Tests page break interval works in PDF.
	 * 
	 * @throws EngineException
	 */
	public void testPageBreakInterval() throws EngineException {
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/PageBreakIntervalTest.xml";
		List pageAreas = getPageAreas(designFile);
		assertEquals(3, pageAreas.size());
		int[] recordNumberInEachPage = { 3, 3, 1 };
		for (int i = 0; i < recordNumberInEachPage.length; i++) {
			TableArea table = getTableArea((ContainerArea) pageAreas.get(i));
			assertNotNull(table);
			assertEquals(recordNumberInEachPage[i], table.getChildrenCount());
		}
	}

	/**
	 * Tests page break interval count is reset when page is broken by other page
	 * break events.
	 * 
	 * In this case, a page break interval 3 is set on table, while the page break
	 * set on table group will broken page every 2 records. So the page break
	 * interval takes no effect.
	 * 
	 * @throws EngineException
	 */
	public void testPageBreakInterval2() throws EngineException {
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/PageBreakIntervalAndGroupPageBreak.xml";
		List pageAreas = getPageAreas(designFile);
		assertEquals(3, pageAreas.size());
	}

	private TableArea getTableArea(ContainerArea container) {
		Iterator children = container.getChildren();
		while (children.hasNext()) {
			Object child = children.next();
			if (child instanceof TableArea) {
				return (TableArea) child;
			}
			TableArea result = getTableArea((ContainerArea) child);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
}
