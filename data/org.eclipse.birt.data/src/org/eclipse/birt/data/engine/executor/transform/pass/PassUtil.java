/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.executor.transform.pass;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.cache.CacheRequest;
import org.eclipse.birt.data.engine.executor.cache.OdiAdapter;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.executor.cache.SmartCache;
import org.eclipse.birt.data.engine.executor.cache.SortSpec;
import org.eclipse.birt.data.engine.executor.dscache.DataSetFromCache;
import org.eclipse.birt.data.engine.executor.dscache.DataSetToCache;
import org.eclipse.birt.data.engine.executor.transform.OdiResultSetWrapper;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * The pass util.
 */
class PassUtil {
	/**
	 *
	 */
	private static final String RESULT_SET_COMPUTED_COLUMN_NAME_PATTERN = "\\Q_{$TEMP\\E.*\\d*\\Q$}_\\E";

	/**
	 *
	 *
	 */
	private PassUtil() {
	}

	/**
	 * Pass the result source, create a new smartCache, do grouping.
	 *
	 * @param populator
	 * @param resultSource
	 * @param doGroup
	 * @param stopSign
	 * @throws DataException
	 */
	public static void pass(ResultSetPopulator populator, OdiResultSetWrapper resultSource, boolean doGroup)
			throws DataException {
		populateOdiResultSet(populator, resultSource,
				doGroup ? populator.getGroupProcessorManager().getGroupCalculationUtil().getSortSpec() : null);

		if (doGroup) {
			populator.getGroupProcessorManager().getGroupCalculationUtil().getGroupInformationUtil().doGrouping();
		}

		populator.getCache().next();

		populator.getExpressionProcessor().setResultIterator(populator.getResultIterator());
	}

	/**
	 *
	 * @param populator
	 * @param rsWrapper
	 * @param sortSpec
	 * @param stopSign
	 * @throws DataException
	 */
	private static void populateOdiResultSet(ResultSetPopulator populator, OdiResultSetWrapper rsWrapper,
			SortSpec sortSpec) throws DataException {
		Object resultSource = rsWrapper.getWrappedOdiResultSet();
		assert resultSource != null;

		ResultSetCache smartCache = null;
		BaseQuery query = populator.getQuery();
		IResultClass rsMeta = populator.getResultSetMetadata();

		if (resultSource instanceof ResultSet) {
			smartCache = new SmartCache(new CacheRequest(query.getMaxRows(), query.getFetchEvents(), sortSpec,
					populator.getEventHandler(), query.getDistinctValueFlag()), (ResultSet) resultSource, rsMeta,
					populator.getSession());
		} else if (resultSource instanceof ICustomDataSet) {
			smartCache = new SmartCache(
					new CacheRequest(query.getMaxRows(), query.getFetchEvents(), sortSpec, populator.getEventHandler(),
							query.getDistinctValueFlag()),
					new OdiAdapter((ICustomDataSet) resultSource), rsMeta, populator.getSession());
		} else if (resultSource instanceof IDataSetPopulator) {
			smartCache = new SmartCache(
					new CacheRequest(query.getMaxRows(), query.getFetchEvents(), sortSpec, populator.getEventHandler(),
							query.getDistinctValueFlag()),
					new OdiAdapter((IDataSetPopulator) resultSource), rsMeta, populator.getSession());
		} else if (resultSource instanceof DataSetToCache) {
			smartCache = new SmartCache(new CacheRequest(query.getMaxRows(), query.getFetchEvents(), sortSpec,
					populator.getEventHandler(), false), new OdiAdapter((DataSetToCache) resultSource), rsMeta,
					populator.getSession());
		} else if (resultSource instanceof DataSetFromCache) {
			smartCache = new SmartCache(new CacheRequest(query.getMaxRows(),
					// fetch events are needless since data set result set is already prepared in
					// cache
					null, sortSpec, populator.getEventHandler(), false),
					new OdiAdapter((DataSetFromCache) resultSource), rsMeta, populator.getSession());
		} else if (resultSource instanceof IResultIterator) {
			smartCache = new SmartCache(
					new CacheRequest(query.getMaxRows(), query.getFetchEvents(), sortSpec, populator.getEventHandler(),
							false), // must be true
					new OdiAdapter((IResultIterator) resultSource), rsMeta, populator.getSession());
		} else if (resultSource instanceof Object[]) {
			Object[] obs = (Object[]) resultSource;
			smartCache = new SmartCache(
					new CacheRequest(query.getMaxRows(), query.getFetchEvents(), sortSpec, populator.getEventHandler(),
							false), // must be true
					(ResultSetCache) obs[0], ((int[]) obs[1])[0], ((int[]) obs[1])[1], rsMeta, populator.getSession());
		}

		populator.getGroupProcessorManager().getGroupCalculationUtil().setResultSetCache(smartCache);
		// clear existing smart cache when reset the smart cache in populator
		if (populator.getCache() != null && populator.clearCacheResultSet()) {
			try {
				populator.getCache().close();
			} catch (DataException e) {
			}
		}
		populator.setCache(smartCache);
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public static boolean isTemporaryResultSetComputedColumn(String name) {
		return name.matches(RESULT_SET_COMPUTED_COLUMN_NAME_PATTERN);
	}
}
