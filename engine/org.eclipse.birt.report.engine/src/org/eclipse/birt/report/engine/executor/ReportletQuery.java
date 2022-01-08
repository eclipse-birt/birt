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

import java.util.LinkedList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

public class ReportletQuery {

	LinkedList<Query> queries = new LinkedList<Query>();
	ExecutionContext context;
	InstanceID iid;

	public ReportletQuery(ExecutionContext context, InstanceID iid) {
		this.context = context;
		this.iid = iid;
	}

	public IBaseResultSet[] getQueryResults() {
		if (!queries.isEmpty()) {
			Query query = queries.getLast();
			return query.rsets;
		}
		return null;
	}

	public void openReportletQueries() throws BirtException {
		// get all the parents
		LinkedList<InstanceID> parents = new LinkedList<InstanceID>();
		InstanceID parentId = iid.getParentID();
		while (parentId != null) {
			parents.addFirst(parentId);
			parentId = parentId.getParentID();
		}

		Report report = context.getReport();
		for (InstanceID pid : parents) {
			DataID dataId = pid.getDataID();
			if (dataId != null) {
				if (!queries.isEmpty()) {
					Query lastQuery = queries.getLast();
					lastQuery.rowId = dataId.getRowID();
					lastQuery.cellId = dataId.getCellID();
				}
			}
			// we need add the parent query to the query
			ReportElementDesign design = report.getReportItemByID(pid.getComponentID());
			// set the parents
			if (design instanceof ReportItemDesign) {
				IDataQueryDefinition[] qs = ((ReportItemDesign) design).getQueries();
				if (qs != null) {
					queries.add(new Query(qs));
				}
			}
		}

		DataID dataId = iid.getDataID();
		if (!queries.isEmpty() && dataId != null) {
			Query lastQuery = queries.getLast();
			lastQuery.rowId = dataId.getRowID();
			lastQuery.cellId = dataId.getCellID();
		}

		executeQueries(queries);
	}

	void executeQueries(LinkedList<Query> queries) throws BirtException {
		IBaseResultSet rset = null;
		for (Query query : queries) {
			query.rsets = new IBaseResultSet[query.queries.length];
			for (int i = 0; i < query.queries.length; i++) {
				query.rsets[i] = context.executeQuery(rset, query.queries[i], null, false);
			}

			rset = query.rsets[0];
			if (query.cellId != null) {
				((ICubeResultSet) rset).skipTo(query.cellId);
			}
			if (query.rowId != -1) {
				if (rset.getType() == IBaseResultSet.QUERY_RESULTSET) {
					((IQueryResultSet) rset).skipTo(query.rowId);
				}
			}
		}
	}

	public void closeReportletQueries() throws EngineException {
		for (Query query : queries) {
			if (query.rsets != null) {
				for (IBaseResultSet rset : query.rsets) {
					if (rset != null)
						rset.close();
				}
			}
		}
		queries.clear();
	}

	private static class Query {

		IDataQueryDefinition[] queries;
		IBaseResultSet[] rsets;
		long rowId;
		String cellId;

		Query(IDataQueryDefinition[] qs) {
			this.queries = qs;
		}
	}
}
