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
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.tests.engine.BaseEmitter;

/**
 * <b> Test IBandContent API methods
 */
public class IBandContentTest extends BaseEmitter {

	private String reportName = "IBandContentTest.rptdesign";

	protected String getReportName() {
		return reportName;
	}

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(reportName, reportName);
	}

	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	/**
	 * Test set/getBandType method
	 */
	public void testBandType() {
		IBandContent bandContent = new ReportContent().createTableBandContent();
		bandContent.setBandType(IBandContent.BAND_DETAIL);
		assertEquals(IBandContent.BAND_DETAIL, bandContent.getBandType());

		bandContent.setBandType(IBandContent.BAND_FOOTER);
		assertEquals(IBandContent.BAND_FOOTER, bandContent.getBandType());

		bandContent.setBandType(IBandContent.BAND_GROUP_FOOTER);
		assertEquals(IBandContent.BAND_GROUP_FOOTER, bandContent.getBandType());

		bandContent.setBandType(IBandContent.BAND_GROUP_HEADER);
		assertEquals(IBandContent.BAND_GROUP_HEADER, bandContent.getBandType());

		bandContent.setBandType(IBandContent.BAND_HEADER);
		assertEquals(IBandContent.BAND_HEADER, bandContent.getBandType());
	}

	/**
	 * Test IBandContent methods with report
	 *
	 * @throws EngineException
	 */
	public void testBandFromReport() throws EngineException {
		runandrender_emitter(EMITTER_HTML, false);
	}

	public void endTableGroup(ITableGroupContent group) {
		IBandContent groupHeader = group.getHeader();
		IBandContent groupFooter = group.getFooter();
		assertEquals(IBandContent.BAND_GROUP_HEADER, groupHeader.getBandType());
		// TODO:getGroupID() method is not implemented.
		assertNull(groupFooter);
		// TODO:footer is not
		// returned.assertEquals(IBandContent.BAND_GROUP_FOOTER,footer.getBandType
		// (
		// ));
	}

	public void endTable(ITableContent table) {
		IBandContent header = table.getHeader();
		IBandContent footer = table.getFooter();
		assertEquals(IBandContent.BAND_HEADER, header.getBandType());
		// TODO:getGroupID() method is not implemented.
		assertNull(footer);
		// TODO:footer is not
		// returned.assertEquals(IBandContent.BAND_FOOTER,footer.getBandType(
		// ));
	}

}
