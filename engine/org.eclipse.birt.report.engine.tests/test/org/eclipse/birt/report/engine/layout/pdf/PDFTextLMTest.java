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
import java.util.Locale;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.nLayout.area.impl.CellArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.RowArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;

public class PDFTextLMTest extends PDFLayoutTest {
	/**
	 * Test case for bugzilla bug
	 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=181578">181578</a> :
	 * Exception is thrown when preview attached report in PDF[1102]
	 * 
	 * @throws EngineException
	 */
	public void testHyphenation() throws EngineException {
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/181578.xml";
		try {
			IReportRunnable report = openReportDesign(designFile);
			List pageAreas = new ArrayList();
			IEmitterMonitor monitor = new PageMonitor(pageAreas);
			IRunAndRenderTask runAndRenderTask = new TestRunAndRenderTask(engine, report, monitor);
			runAndRenderTask.setLocale(Locale.CHINA);
			runAndRenderTask.setRenderOption(createRenderOption());
			runAndRenderTask.run();
			runAndRenderTask.close();
		} catch (Throwable t) {
			System.out.println(t);
			assertTrue(false);
		}
		assertTrue(true);
	}

	/**
	 * test text wrapping alogrithm.
	 * 
	 * @throws EngineException
	 */
	public void testTextWrap() throws EngineException {
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/textWrap.xml";
		IReportRunnable report = openReportDesign(designFile);
		List pageAreas = getPageAreas(report);

		assertEquals(1, pageAreas.size());
		PageArea pageArea = (PageArea) pageAreas.get(0);
		ContainerArea body = (ContainerArea) pageArea.getBody();

		Iterator iter = body.getChildren();
		TableArea table = (TableArea) iter.next();

		iter = table.getChildren();
		RowArea row = (RowArea) iter.next();

		iter = row.getChildren();
		CellArea cell = (CellArea) iter.next();

		Iterator it = cell.getChildren();
		ContainerArea container = (ContainerArea) it.next();
		assertTrue(container.getChildrenCount() == 1);

		cell = (CellArea) iter.next();
		it = cell.getChildren();
		container = (ContainerArea) it.next();
		assertTrue(container.getChildrenCount() == 2);

		cell = (CellArea) iter.next();
		it = cell.getChildren();
		container = (ContainerArea) it.next();
		assertTrue(container.getChildrenCount() == 2);
	}
}
