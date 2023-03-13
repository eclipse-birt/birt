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

import java.util.TimeZone;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * Test IDataIterator API methods
 */
public class IDataIteratorTest extends EngineCase {

	private String report = "IDataIteratorTest.rptdesign";
	private String output = "IDataIteratorTest.rptdocument";
	private IDataExtractionTask task;

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

	public void testIDataIterator() throws BirtException {

		TimeZone timeZone = TimeZone.getDefault();
		try {
			TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
			run(report, output);
			String outputFile = this.genOutputFile(output);
			IReportDocument reportDoc = engine.openReportDocument(outputFile);
			task = engine.createDataExtractionTask(reportDoc);
			task.selectResultSet("ELEMENT_6");
			IExtractionResults results = task.extract();
			IDataIterator iterator = results.nextResultIterator();

			checkGetQueryResults(iterator, results);
			checkGetResultMetaData(iterator);
			checkGetValue(iterator);
			checkNext(iterator);
		} finally {
			TimeZone.setDefault(timeZone);
		}
	}

	private void checkGetQueryResults(IDataIterator iterator, IExtractionResults results) {
		assertEquals(results, iterator.getQueryResults());
	}

	private void checkGetResultMetaData(IDataIterator iterator) {
		try {
			IResultMetaData metaData = iterator.getResultMetaData();
			assertEquals(4, metaData.getColumnCount());
		} catch (BirtException e) {
			e.printStackTrace();
			fail();
		}
	}

	private void checkGetValue(IDataIterator iterator) {
		// fail case. reference to bug #189397
		try {
			if (iterator.next()) {
				assertEquals(10334, Integer.parseInt(iterator.getValue(0).toString()));
				assertEquals("Mon Nov 19 00:00:00 UTC 2012", iterator.getValue(1).toString());
				assertEquals("On Hold", iterator.getValue(2).toString());
				assertEquals("0.9385160294251204", iterator.getValue(3).toString());
				assertEquals(10334, Integer.parseInt(iterator.getValue("ORDERNUMBER").toString()));
				assertEquals("Mon Nov 19 00:00:00 UTC 2012", iterator.getValue("ORDERDATE").toString());
				assertEquals("0.9385160294251204", iterator.getValue("col1").toString());
				assertEquals("On Hold", iterator.getValue("STATUS").toString());
			} else {
				fail();
			}
		} catch (NumberFormatException | BirtException e) {
			e.printStackTrace();
			fail();
		}
	}

	private void checkNext(IDataIterator iterator) {
		int count = 0;
		try {
			while (iterator.next()) {
				count++;
			}
		} catch (BirtException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(6, count);
	}
}
