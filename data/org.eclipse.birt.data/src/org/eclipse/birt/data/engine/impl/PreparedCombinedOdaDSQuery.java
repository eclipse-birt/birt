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
package org.eclipse.birt.data.engine.impl;

import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.datatools.connectivity.oda.spec.BaseQuery;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;
import org.eclipse.datatools.connectivity.oda.spec.basequery.AtomicQuery;
import org.eclipse.datatools.connectivity.oda.spec.basequery.CombinedQuery;

public class PreparedCombinedOdaDSQuery extends PreparedOdaDSQuery {

	PreparedCombinedOdaDSQuery(DataEngineImpl dataEngine, IQueryDefinition queryDefn, IBaseDataSetDesign dataSetDesign,
			Map appContext, IQueryContextVisitor visitor) throws DataException {
		super(dataEngine, queryDefn, dataSetDesign, appContext, visitor);
	}

	protected QueryExecutor newExecutor() throws DataException {
		return new CombinedDSQueryExecutor();
	}

	public class CombinedDSQueryExecutor extends OdaDSQueryExecutor {
		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiQuery()
		 */
		protected IQuery createOdiQuery() throws DataException {
			CombinedOdaDataSetRuntime extDataSet = (CombinedOdaDataSetRuntime) dataSet;
			assert extDataSet != null;

			QueryOptimizeHints queryOptimizeHints = (QueryOptimizeHints) this.getAppContext()
					.get(IQueryOptimizeHints.QUERY_OPTIMIZE_HINT);
			Map<String, QuerySpecification> optimizedDataSets = queryOptimizeHints.getOptimizedCombinedQuerySpec();
			QuerySpecification combinedQuerySpec = optimizedDataSets.get(extDataSet.getName());
			if (combinedQuerySpec != null) {
				reconstructQuerySpec(combinedQuerySpec, queryOptimizeHints, extDataSet);
			}

			return super.createOdiQuery();
		}

		private void reconstructQuerySpec(QuerySpecification querySpec, QueryOptimizeHints optimizeHints,
				CombinedOdaDataSetRuntime dataSet) {
			BaseQuery baseQuery = querySpec.getBaseQuery();
			if (baseQuery instanceof CombinedQuery) {
				reconstructQuerySpec(((CombinedQuery) baseQuery).getLeftQuery(), optimizeHints, dataSet);
				reconstructQuerySpec(((CombinedQuery) baseQuery).getRightQuery(), optimizeHints, dataSet);
			}
			if (baseQuery instanceof AtomicQuery) {
				String dataSetName = optimizeHints.getDataSetForAtomicQuery((AtomicQuery) baseQuery);
				String queryText = dataSet.getQueryText(dataSetName);
				if (queryText != null) {
					querySpec.setBaseQuery(new AtomicQuery(queryText));
				}
			}
		}
	}
}
