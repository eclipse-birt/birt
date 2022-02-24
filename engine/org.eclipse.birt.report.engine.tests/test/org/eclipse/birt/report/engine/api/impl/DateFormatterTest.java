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

package org.eclipse.birt.report.engine.api.impl;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.dataextraction.CSVDataExtractionOption;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class DateFormatterTest extends EngineCase {

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/impl/date_formatter.xml";

	IReportDocument document;
	IDataExtractionTask dataExTask;

	public void setUp() throws Exception {
		super.setUp();
		removeFile(REPORT_DOCUMENT);
		removeFile(REPORT_DESIGN);
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);
		createReportDocument();
		document = engine.openReportDocument(REPORT_DOCUMENT);
		dataExTask = engine.createDataExtractionTask(document);
	}

	public void tearDown() throws Exception {
		dataExTask.close();
		document.close();
		removeFile(REPORT_DESIGN);
		removeFile(REPORT_DOCUMENT);
		super.tearDown();
	}

	public void testDateFormatterExtraction() throws Exception {
		dataExTask.selectResultSet("ELEMENT_69");
		String[] columnNames = new String[] { "CUSTOMERNUMBER", "CHECKNUMBER", "PAYMENTDATE", "now", "now" };
		dataExTask.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		dataExTask.selectColumns(columnNames);
		dataExTask.setLocale(ULocale.CHINESE);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		CSVDataExtractionOption option = new CSVDataExtractionOption();
		option.setTimeZone(java.util.TimeZone.getTimeZone("GMT+5"));
		option.setLocale(Locale.ENGLISH);
		option.setOutputFormat("csv");
		option.setOutputStream(out);
		Map<Object, String> formatters = new HashMap<Object, String>();
		formatters.put(1, "Fixed");
		formatters.put(2, "<");
		formatters.put(3, "yyyy-MM-dd");
		formatters.put("now", "yyyy-MM-dd HH:mm:ss.sss ZZZ");
		formatters.put(5, "Long Date");
		option.setFormatter(formatters);
		dataExTask.extract(option);
		String result = new String(out.toByteArray());
		System.out.println(result);
	}
}
