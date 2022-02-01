/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.data.dte.DocumentDataSource;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

public class ReportletItemExecutor extends ReportItemExecutor {

	boolean hasNext = true;
	ReportletQuery reportletQuery;

	protected ReportletItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.REPORTLETITEM);
		DocumentDataSource ds = context.getDataSource();
		InstanceID instanceID2 = null;
		if (ds != null) {
			instanceID2 = ds.getInstanceID();
		}
		reportletQuery = new ReportletQuery(context, instanceID2);
	}

	// Since ReportletItemExecutor is a virtual parent executor, so the InstanceID
	// should return as null.(T30410)
	protected InstanceID getInstanceID() {
		return null;
	}

	public void close() throws BirtException {
		try {
			reportletQuery.closeReportletQueries();
		} catch (EngineException ex) {
			context.addException(ex);
		}
		super.close();
	}

	public IBaseResultSet[] getQueryResults() {
		return reportletQuery.getQueryResults();
	}

	/*
	 * protected InstanceID getInstanceID( ) { return null; }
	 */

	public IContent execute() {
		try {
			reportletQuery.openReportletQueries();
		} catch (BirtException ex) {
			context.addException(ex);
		}
		return null;
	}

	public boolean hasNextChild() {
		return hasNext;
	}

	public IReportItemExecutor getNextChild() {
		if (hasNext) {
			hasNext = false;
			DocumentDataSource ds = context.getDataSource();
			long designId = ds.getElementID();
			Report report = context.getReport();
			ReportItemDesign design = (ReportItemDesign) report.getReportItemByID(designId);

			return manager.createExecutor(this, design);
		}
		return null;
	}
}
