/*******************************************************************************
 * Copyright (c) 2004,2010 Actuate Corporation.
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

package org.eclipse.birt.report.engine.data;

import java.util.Map;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.impl.EngineTask;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.data.dte.DataGenerationEngine;
import org.eclipse.birt.report.engine.data.dte.DataInteractiveEngine;
import org.eclipse.birt.report.engine.data.dte.DataPresentationEngine;
import org.eclipse.birt.report.engine.data.dte.DocumentDataSource;
import org.eclipse.birt.report.engine.data.dte.DteDataEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A factory class to create data engines. For now, only DtE data engine is
 * created in this factory.
 *
 */
public class DataEngineFactory {
	/**
	 * static factory instance
	 */
	static protected DataEngineFactory sm_instance;

	/**
	 * private constractor
	 */
	protected DataEngineFactory() {
	}

	/**
	 * get instance of factory
	 *
	 * @return the factory instance
	 */
	synchronized public static DataEngineFactory getInstance() {
		if (sm_instance == null) {
			sm_instance = new DataEngineFactory();
		}
		return sm_instance;
	}

	/**
	 * create a <code>DataEngine</code> given an execution context
	 *
	 * @param context the execution context
	 * @return a data engine instance
	 */
	public IDataEngine createDataEngine(ExecutionContext context, boolean needCache) throws Exception {
		IReportDocument document = context.getReportDocument();
		if (document != null) {
			String buildNumber = document.getProperty(ReportDocumentConstants.BIRT_ENGINE_BUILD_NUMBER_KEY);
			Map appContext = context.getAppContext();
			if (appContext != null) {
				appContext.put(ReportDocumentConstants.BIRT_ENGINE_BUILD_NUMBER_KEY, buildNumber);
			}
		}
		// first we must test if we have the data source
		DocumentDataSource dataSource = context.getDataSource();
		if (dataSource != null) {
			ReportDocumentWriter writer = context.getReportDocWriter();
			IDocArchiveWriter archiverWriter = null;
			if (writer != null) {
				archiverWriter = writer.getArchive();
			}
			return new DataInteractiveEngine(this, context, dataSource.getDataSource(), archiverWriter);
		}
		// if get the report document writer is not null, that means we are in the g
		ReportDocumentWriter writer = context.getReportDocWriter();
		if (writer != null) {
			return new DataGenerationEngine(this, context, context.getReportDocWriter().getArchive());
		}

		if (document != null) {
			if (context.getEngineTask().getTaskType() == EngineTask.TASK_DATAEXTRACTION) {
				return new DataInteractiveEngine(this, context, context.getReportDocument().getArchive(), null);
			}
			return new DataPresentationEngine(this, context, context.getReportDocument().getArchive(), needCache);
		}
		return new DteDataEngine(this, context, needCache);
	}

	public void closeDataEngine(IDataEngine dataEngine) {
	}
}
