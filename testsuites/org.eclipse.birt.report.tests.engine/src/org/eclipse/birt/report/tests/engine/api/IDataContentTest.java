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
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.tests.engine.BaseEmitter;

/**
 * Test IDataContent API methods.
 */
public class IDataContentTest extends BaseEmitter {

	private IReportContent reportContent = new ReportContent();
	private IDataContent data;
	private String reportName = "IDataContentTest.rptdesign";

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(reportName, reportName);
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	@Override
	protected String getReportName() {
		return reportName;
	}

	/**
	 * Test set/getValue() methods.
	 */
	public void testValue() {
		data = reportContent.createDataContent();

		assertNull(data.getValue());

		Object value = new Object();
		data.setValue(value);
		assertEquals(value, data.getValue());
	}

	/**
	 * Test set/getLabelKey() methods.
	 */
	public void testLabelKey() {
		data = reportContent.createDataContent();
		assertNull(data.getLabelKey());
		data.setLabelKey("key");
		assertEquals("key", data.getLabelKey());
	}

	/**
	 * Test set/getLabelText() methods.
	 */
	public void testLabelText() {
		data = reportContent.createDataContent();
		assertNull(data.getLabelText());
		data.setLabelText("data");
		assertEquals("data", data.getLabelText());
	}

	/**
	 * Test IDataContent methods through report
	 *
	 * @throws EngineException
	 */
	public void testIDataContentFromReport() throws EngineException {
		runandrender_emitter(EMITTER_HTML, false);
	}

	@Override
	public void startData(IDataContent data) {
		assertEquals("data help", data.getHelpText());
		assertEquals("data help key", data.getHelpKey());
		assertEquals("my data", data.getValue().toString());
	}

}
