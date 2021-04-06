/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public IDataQueryDefinition[] createQuery(IDataQueryDefinition parent, ReportElementHandle handle) {
		design = report.findDesign(handle);
		return builder.build(parent, design);
	}

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
