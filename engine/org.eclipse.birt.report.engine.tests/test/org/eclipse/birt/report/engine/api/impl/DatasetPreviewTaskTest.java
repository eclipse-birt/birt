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

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IDatasetPreviewTask;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SessionHandle;

import com.ibm.icu.util.ULocale;

/**
 * in the report design, we define four listing elements: 219: table with query.
 * 277: list of query 280: a table with sub query 289: a table with nest query.
 */
public class DatasetPreviewTaskTest extends EngineCase {

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/impl/TestDataExtractionTask.xml";
	static final String REPORT_LIBRARY_RESOURCE = "org/eclipse/birt/report/engine/api/impl/library.xml";

	IReportDocument document;
	IDatasetPreviewTask previewTask;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeFile(REPORT_DESIGN);

	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	protected ModuleHandle getHandle(String fileName) throws Exception {
		ModuleHandle designHandle;
		// Create new design session
		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.getDefault());
		designHandle = sessionHandle.openModule(fileName);
		return designHandle;
	}

	public void testPreviewDatasetInLib() throws Exception {
		copyResource(REPORT_LIBRARY_RESOURCE, REPORT_DESIGN);
		previewTask = engine.createDatasetPreviewTask();
		ModuleHandle muduleHandle = getHandle(REPORT_DESIGN);
		List ds = muduleHandle.getAllDataSets();
		for (Object obj : ds) {
			DataSetHandle dataset = (DataSetHandle) obj;
			if (dataset.getName().equals("Data Set")) {
				previewTask.setDataSet(dataset);
			}
		}

		previewTask.setMaxRow(20);
		IExtractionResults results = previewTask.execute();
		int rowCount = checkExtractionResults(results);
		assertTrue(rowCount == 20);
		previewTask.close();
		removeFile(REPORT_DESIGN);
	}

	public void testPreviewDatasetInReport() throws Exception {
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);
		previewTask = engine.createDatasetPreviewTask();
		IReportRunnable reportDesign = engine.openReportDesign(REPORT_DESIGN);
		List ds = reportDesign.getDesignHandle().getModuleHandle().getAllDataSets();
		for (Object obj : ds) {
			DataSetHandle dataset = (DataSetHandle) obj;
			if (dataset.getName().equals("DataSet")) {
				previewTask.setDataSet(dataset);
			}
		}

		previewTask.setMaxRow(5);
		IExtractionResults results = previewTask.execute();
		int rowCount = checkExtractionResults(results);
		assertTrue(rowCount == 5);
		previewTask.close();
		removeFile(REPORT_DESIGN);
	}

	/**
	 * access all the data in the results, no exception should be throw out.
	 *
	 * @param results
	 * @return row count in the result.
	 * @throws BirtException
	 */
	protected int checkExtractionResults(IExtractionResults results) throws Exception {
		int rowCount = 0;
		IDataIterator dataIter = results.nextResultIterator();
		if (dataIter != null) {
			while (dataIter.next()) {
				IResultMetaData resultMeta = dataIter.getResultMetaData();
				for (int i = 0; i < resultMeta.getColumnCount(); i++) {
					Object obj = dataIter.getValue(resultMeta.getColumnName(i));
					String type = resultMeta.getColumnTypeName(i);
					assertTrue(type != null);
					System.out.print(obj + " ");
				}
				rowCount++;
				System.out.println();
			}
			dataIter.close();
		}
		results.close();
		return rowCount;
	}
}
