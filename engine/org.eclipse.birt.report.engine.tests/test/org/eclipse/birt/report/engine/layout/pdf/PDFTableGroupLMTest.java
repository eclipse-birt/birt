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
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;

public class PDFTableGroupLMTest extends PDFLayoutTest {
	/**
	 * Test case for bugzilla bug
	 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=173601">173601</a> :
	 * [regression]Group should begin from the second page when setting group page
	 * break before to always[0001]
	 * 
	 * @throws EngineException
	 */
	public void testGroupPageBreakBeforeAways() throws EngineException {
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/173601.xml"; //$NON-NLS-1$
		IReportRunnable report = openReportDesign(designFile);
		List pageAreas = getPageAreas(report);

		assertEquals(2, pageAreas.size());
		PageArea pageArea = (PageArea) pageAreas.get(0);
		ContainerArea body = (ContainerArea) pageArea.getBody();
		assertTrue(body.getChildrenCount() == 1);

		Iterator iter = body.getChildren();
		TableArea table = (TableArea) iter.next();
		assertTrue(table.getChildrenCount() == 1);

		pageArea = (PageArea) pageAreas.get(1);
		body = (ContainerArea) pageArea.getBody();
		assertTrue(body.getChildrenCount() == 1);

		iter = body.getChildren();
		table = (TableArea) iter.next();
		assertTrue(table.getChildrenCount() == 2);

		iter = table.getChildren();
		iter.next();// table header row
		ContainerArea group = (ContainerArea) iter.next();
		assertTrue(group.getChildrenCount() == 6);
	}
}
