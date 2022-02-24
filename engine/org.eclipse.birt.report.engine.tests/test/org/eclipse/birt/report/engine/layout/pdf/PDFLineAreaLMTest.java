/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.LineArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;

public class PDFLineAreaLMTest extends PDFLayoutTest {
	/**
	 * Test case for bugzilla bug
	 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=168804">168804</a> :
	 * Text only containing multiple line break are ignored in pdf
	 * 
	 * @throws EngineException
	 */
	public void testMutipleLineBreakHeight() throws EngineException {
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/168804.xml";
		IReportRunnable report = openReportDesign(designFile);
		List pageAreas = getPageAreas(report);

		assertEquals(1, pageAreas.size());
		PageArea pageArea = (PageArea) pageAreas.get(0);
		Iterator logicContainers = pageArea.getBody().getChildren();
		IContainerArea blockContainer = (IContainerArea) logicContainers.next();
		assertTrue("3 lines", blockContainer.getChildrenCount() == 3);
		assertTrue("Line height is right", blockContainer.getHeight() >= 27000);
	}

	/**
	 * Test case for bugzilla bug
	 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=157189">157189</a> :
	 * HTML BR tags cease to work for text element after page break in PDF
	 * 
	 * @throws EngineException
	 */
	public void estForeignContent() throws EngineException {
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/LineAreaLMTest-157189.xml";
		IReportRunnable report = openReportDesign(designFile);
		List pageAreas = getPageAreas(report);

		assertEquals(2, pageAreas.size());
		PageArea pageArea = (PageArea) pageAreas.get(1);
		Iterator logicContainers = pageArea.getBody().getChildren();
		IContainerArea blockContains = (IContainerArea) logicContainers.next();
		logicContainers = blockContains.getChildren();
		IContainerArea blockContains1 = (IContainerArea) logicContainers.next();
		Iterator lineAreas = blockContains1.getChildren();
		lineAreas.next();
		LineArea emptyLine = (LineArea) lineAreas.next();
		assertTrue("Second line is not an empty line.", isEmpty(emptyLine));
		LineArea lineArea = (LineArea) lineAreas.next();
		assertEquals(" ", getText(lineArea, 2));
		assertEquals("paragraph 22.", getText(lineArea, 5));
	}

}
