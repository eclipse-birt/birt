/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.tests.engine.BaseEmitter;

/**
 * <b>Test IColumn API methods</b>
 */
public class IColumnTest extends BaseEmitter {

	private String reportName = "IColumnTest.rptdesign";

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(reportName, reportName);
	}

	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	protected String getReportName() {
		return reportName;
	}

	/**
	 * Test all methods in IColumn API through generate report
	 *
	 * @throws EngineException
	 */
	public void testIColumn() throws EngineException {
		runandrender_emitter(EMITTER_HTML, false);
	}

	public void endTable(ITableContent table) {
		IColumn column = table.getColumn(0);
		IStyle inStyle = column.getInlineStyle();
		// TODO: find no way to set column inlinestyle.
		assertEquals("pdf", column.getVisibleFormat());

		assertTrue(column.hasDataItemsInDetail());
		assertEquals(15, column.getInstanceID().getComponentID());
		assertTrue(column.getGenerateBy() instanceof ColumnDesign);
		assertTrue(column.getWidth().getMeasure() == 1.5);
		assertEquals("in", column.getWidth().getUnits());
		System.out.println();

	}

	public void endCell(ICellContent cell) {
		assertEquals("rgb(128, 128, 128)", cell.getStyle().getBackgroundColor());
	}

}
