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

package org.eclipse.birt.report.engine.extension.internal;

import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.dte.ReportQueryBuilder;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IQueryContext;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.ReportElementHandle;

public class QueryContext implements IQueryContext {

	Report report;

	ExecutionContext context;

	ReportQueryBuilder builder;

	ReportItemDesign design;

	public QueryContext(ExecutionContext context, ReportQueryBuilder builder) {
		this.report = context.getReport();
		this.context = context;
		this.builder = builder;
	}

	@Override
	public IDataQueryDefinition[] createQuery(IDataQueryDefinition parent, ReportElementHandle handle) {
		design = report.findDesign(handle);
		return builder.build(parent, design);
	}

	@Override
	public DataRequestSession getDataRequestSession() {
		if (context != null) {
			try {
				IDataEngine dataEngine = context.getDataEngine();
				return dataEngine.getDTESession();
			} catch (EngineException e) {
				context.addException(e);
			}

		}
		return null;
	}

}
