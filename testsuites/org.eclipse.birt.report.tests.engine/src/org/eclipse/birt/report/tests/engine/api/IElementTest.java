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
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.tests.engine.BaseEmitter;

/**
 * Test IElement API methods No case for getChildren() method because it's not
 * implemented completely and is not recommended to use.
 */
public class IElementTest extends BaseEmitter {

	private String report = "IElementTest.rptdesign";

	/**
	 * Test set/getParent() methods.
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(report, report);
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	public void testParent() {
		IElement element = new ReportContent().createContainerContent();
		IElement parent = new ReportContent().createContainerContent();
		element.setParent(parent);
		assertEquals(parent, element.getParent());

		element.setParent(null);
		assertNull(element.getParent());
	}

	public void testIElement() throws EngineException {
		runandrender_emitter(EMITTER_HTML, false);
	}

	@Override
	public void endContainer(IContainerContent container) {
		System.out.println(container);
	}

	@Override
	public void endCell(ICellContent cell) {
		assertTrue(cell.getParent() instanceof IRowContent);
	}

	@Override
	public void endRow(IRowContent row) {
		assertTrue(row.getParent() instanceof ITableContent);
	}

	@Override
	protected String getReportName() {
		return report;
	}

}
