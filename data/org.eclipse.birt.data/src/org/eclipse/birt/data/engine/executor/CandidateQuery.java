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

package org.eclipse.birt.data.engine.executor;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.QueryExecutionStrategyUtil.Strategy;
import org.eclipse.birt.data.engine.executor.dscache.DataSetToCache;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.executor.transform.SimpleResultSet;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * Implementation of ICandidateQuery
 */
public class CandidateQuery extends BaseQuery implements ICandidateQuery {

	private ICustomDataSet customDataSet;

	private IResultIterator resultObjsIterator;
	private int groupingLevel;

	private IResultClass resultMetadata;
	private DataEngineSession session;

	public CandidateQuery(DataEngineSession session) {
		this.session = session;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.odi.ICandidateQuery#setCandidates(org.eclipse.
	 * birt.data.engine.odi.IResultIterator, int)
	 */
	public void setCandidates(IResultIterator resultObjsIterator, int groupingLevel) throws DataException {
		assert resultObjsIterator != null;
		this.resultObjsIterator = resultObjsIterator;
		this.groupingLevel = groupingLevel;

		resultMetadata = resultObjsIterator.getResultClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.odi.ICandidateQuery#setCandidates(org.eclipse.
	 * birt.data.engine.odi.ICustomDataSet)
	 */
	public void setCandidates(ICustomDataSet customDataSet) throws DataException {
		assert customDataSet != null;
		this.customDataSet = customDataSet;

		resultMetadata = customDataSet.getResultClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#getResultClass()
	 */
	public IResultClass getResultClass() {
		return resultMetadata;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#execute()
	 */
	public IResultIterator execute(IEventHandler eventHandler) throws DataException {
		if (customDataSet == null) // sub query
		{
			// resultObjsIterator
			// for sub query, the event handler is no use
			return new CachedResultSet(this, resultMetadata, resultObjsIterator, groupingLevel, eventHandler, session);
		} else
		// scripted query
		{
			if (this.session.getDataSetCacheManager().doesSaveToCache() == false) {
				if (((session.getEngineContext().getMode() == DataEngineContext.DIRECT_PRESENTATION
						|| session.getEngineContext().getMode() == DataEngineContext.MODE_GENERATION))
						&& this.getQueryDefinition() instanceof IQueryDefinition) {
					IQueryDefinition queryDefn = (IQueryDefinition) this.getQueryDefinition();

					Strategy strategy = QueryExecutionStrategyUtil.getQueryExecutionStrategy(this.session, queryDefn,
							queryDefn.getDataSetName() == null ? null
									: ((DataEngineImpl) this.session.getEngine())
											.getDataSetDesign(queryDefn.getDataSetName()));
					if (strategy != Strategy.Complex) {
						SimpleResultSet simpleResult = new SimpleResultSet(this, customDataSet, resultMetadata,
								eventHandler, this.getGrouping(), this.session,
								strategy == Strategy.SimpleLookingFoward);

						return simpleResult.getResultSetIterator();
					}
				}

				return new CachedResultSet(this, customDataSet, eventHandler, session);
			} else
				return new CachedResultSet(this, resultMetadata,
						new DataSetToCache(customDataSet, resultMetadata, session), eventHandler, session);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#close()
	 */
	public void close() {
		// nothing

	}

}
