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
import org.eclipse.birt.report.engine.nLayout.area.impl.CellArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.RowArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;

public class PDFTableLMTest extends PDFLayoutTest {

	public void testFixedTableLayout() throws EngineException {
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/tableFixedLayout.xml";
		List pageAreas = getpageAreas(designFile);

		assertEquals(1, pageAreas.size());
		PageArea pageArea = (PageArea) pageAreas.get(0);
		ContainerArea body = (ContainerArea) pageArea.getBody();
		assertTrue(body.getChildrenCount() == 11);

		Iterator iter = body.getChildren();

		TableArea table = (TableArea) iter.next();
		validateColumnWidth(table, new int[] { 144, 288, 0 });

		table = (TableArea) iter.next();
		validateColumnWidth(table, new int[] { 144, 108, 180 });

		table = (TableArea) iter.next();
		validateColumnWidth(table, new int[] { 144, 177, 111 });

		table = (TableArea) iter.next();
		validateColumnWidth(table, new int[] { 144, 72, 216 });

		table = (TableArea) iter.next();
		validateColumnWidth(table, new int[] { 108, 108, 216 });

		table = (TableArea) iter.next();
		validateColumnWidth(table, new int[] { 144, 208, 80 });

		table = (TableArea) iter.next();
		validateColumnWidth(table, new int[] { 0, 432, 0 });

		table = (TableArea) iter.next();
		validateColumnWidth(table, new int[] { 216, 432, 0 });

		table = (TableArea) iter.next();
		validateColumnWidth(table, new int[] { 216, 432, 0 });

		table = (TableArea) iter.next();
		validateColumnWidth(table, new int[] { 216, 216 });

		table = (TableArea) iter.next();
		validateColumnWidth(table, new int[] { 50, 10, 40 });
	}

	/**
	 * Test case for bugzilla bug
	 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=176794">176794</a> :
	 * Border in pdf is overlapped.
	 *
	 * @throws EngineException
	 */
	public void testBorder() throws EngineException {
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/176794.xml";
		List pageAreas = getpageAreas(designFile);

		// 17847
		PageArea page = (PageArea) pageAreas.get(0);
		Iterator children = page.getBody().getChildren();
		TableArea table1 = (TableArea) children.next();
		TableArea table2 = (TableArea) children.next();
		assertTrue(table1.getHeight() <= table2.getY());
	}

	private void validateColumnWidth(TableArea table, int[] cols) {
		assertTrue(table != null);
		assertTrue(table.getChildrenCount() > 0);
		RowArea row = (RowArea) table.getChildren().next();
		Iterator iter = row.getChildren();
		for (int i = 0; i < cols.length; i++) {
			CellArea cell = (CellArea) iter.next();
			assertEquals(new Integer(cols[i]), new Integer((cell.getWidth() + 499) / 1000));
		}
	}

}
