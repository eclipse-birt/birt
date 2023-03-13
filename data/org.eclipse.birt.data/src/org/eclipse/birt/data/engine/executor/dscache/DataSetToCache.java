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
package org.eclipse.birt.data.engine.executor.dscache;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;
import org.eclipse.birt.data.engine.executor.IDataSetCacheObject;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.IIncreCacheDataSetDesign;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * A data set whose result set needs to be cached. If several queries are using
 * a same data set, the data set result set should be calculated only once
 * during first query execution, then cached by
 * {@code saveDataSetResult(IResultIterator)} method and reused for other
 * queries.
 */
public class DataSetToCache {

	// result set metadata
	private IResultClass rsMeta;

	// oda data set or custom data set whose result set needs to be cached
	private ResultSet odaDataSet;
	private ICustomDataSet customDataSet;
	private IDataSetPopulator populator;

	// cache requirement
	private int cacheCount;

	// save util instance
	private ISaveUtil saveUtil;

	private DataEngineSession session;

	private Integer increCacheMode;

	/**
	 * @param odaDataSet
	 * @param resultSet
	 * @throws DataException
	 */
	public DataSetToCache(ResultSet odaDataSet, IResultClass rsMeta, DataEngineSession session) throws DataException {
		assert odaDataSet != null;
		assert rsMeta != null;

		this.odaDataSet = odaDataSet;
		this.init(rsMeta, session);
	}

	/**
	 * @param customDataSet
	 * @param rsMeta
	 * @throws DataException
	 */
	public DataSetToCache(IDataSetPopulator populator, IResultClass rsMeta, DataEngineSession session)
			throws DataException {
		assert populator != null;
		assert rsMeta != null;

		this.populator = populator;
		this.init(rsMeta, session);
	}

	/**
	 * @param customDataSet
	 * @param rsMeta
	 * @throws DataException
	 */
	public DataSetToCache(ICustomDataSet customDataSet, IResultClass rsMeta, DataEngineSession session)
			throws DataException {
		assert customDataSet != null;
		assert rsMeta != null;

		this.customDataSet = customDataSet;
		this.init(rsMeta, session);
	}

	/**
	 * Initialize
	 *
	 * @throws DataException
	 */
	private void init(IResultClass rsMeta, DataEngineSession session) throws DataException {
		this.rsMeta = rebuildResultClass(rsMeta);
		this.session = session;
		this.cacheCount = getCacheCapability();
		populateCacheMode(session);
	}

	/**
	 * Remove all temp columns.
	 *
	 * @param meta
	 * @return
	 * @throws DataException
	 */
	private static IResultClass rebuildResultClass(IResultClass meta) throws DataException {
		List projectedColumns = new ArrayList();

		for (int i = 1; i <= meta.getFieldCount(); i++) {
			if (!meta.getFieldName(i).matches("\\Q_{$TEMP\\E.*\\d*\\Q$}_\\E")) {
				ResultFieldMetadata field = new ResultFieldMetadata(0, meta.getFieldName(i), meta.getFieldLabel(i),
						meta.getFieldValueClass(i), meta.getFieldNativeTypeName(i), meta.isCustomField(i),
						meta.getAnalysisType(i), meta.getAnalysisColumn(i), meta.isIndexColumn(i),
						meta.isCompressedColumn(i));
				field.setAlias(meta.getFieldAlias(i));

				projectedColumns.add(field);
			}
		}
		IResultClass result = new ResultClass(projectedColumns);
		return result;
	}

	/**
	 * @param session
	 */
	private void populateCacheMode(DataEngineSession session) {
		DataSetCacheManager cacheManager = session.getDataSetCacheManager();
		IBaseDataSetDesign dataSetDesign = cacheManager.getCurrentDataSetDesign();
		if (dataSetDesign instanceof IIncreCacheDataSetDesign) {
			IIncreCacheDataSetDesign icDataSetDesign = (IIncreCacheDataSetDesign) dataSetDesign;
			increCacheMode = icDataSetDesign.getCacheMode();
		}
	}

	/**
	 *
	 * @param stopSign
	 * @return next data
	 * @throws DataException
	 */
	public IResultObject fetch() throws DataException {
		return fetchFromDataSet();
	}

	public void saveDataSetResult(IResultIterator itr) throws DataException {
		try {
			int saved = 0;
			IResultObject resultObject = null;
			IDataSetCacheObject dataSetCachedObject = getCacheObject();
			this.saveInit(dataSetCachedObject);
			itr.first(0);
			while (itr.getCurrentResult() != null) {
				resultObject = itr.getCurrentResult();
				saveUtil.saveObject(resultObject);
				saved++;

				// normally cacheCount rows of the data set will be cached,
				// however, all rows should be cached in persistent cache
				if (increCacheMode == null && saved >= cacheCount) {
					break;
				}
				if (session.getStopSign().isStopped()) {
					removeCacheObject();
					break;
				}
				itr.next();
			}
			this.saveClose(dataSetCachedObject);
		} catch (DataException de) {
			removeCacheObject();
			throw de;
		} finally {
			try {
				itr.first(0);
			} catch (DataException e) {

			}
		}
	}

	/**
	 * @throws DataException
	 *
	 */
	private void removeCacheObject() throws DataException {
		DataSetCacheManager dataSetCacheManager = getDataSetCacheManager();
		dataSetCacheManager.clearCache(dataSetCacheManager.getCurrentDataSourceDesign(),
				dataSetCacheManager.getCurrentDataSetDesign());
	}

	/**
	 * @return
	 * @throws DataException
	 */
	private IResultObject fetchFromDataSet() throws DataException {
		IResultObject resultObject = null;
		if (odaDataSet != null) {
			resultObject = odaDataSet.fetch();
		} else if (customDataSet != null) {
			resultObject = customDataSet.fetch();
		} else if (populator != null) {
			resultObject = this.populator.next();
		}
		return resultObject;
	}

	/**
	 * @throws DataException
	 */
	public void close() throws DataException {
		// when in save status, close might be done automatically
	}

	/**
	 * Init save util
	 *
	 * @throws DataException
	 */
	private void saveInit(IDataSetCacheObject dataSetCachedObject) throws DataException {
		saveUtil = CacheUtilFactory.createSaveUtil(dataSetCachedObject, this.rsMeta, this.session);
	}

	/**
	 * @throws DataException
	 */
	private void saveClose(IDataSetCacheObject dataSetCachedObject) throws DataException {
		if (saveUtil != null) {
			getDataSetCacheManager().saveFinished(dataSetCachedObject);
			saveUtil.close();
			saveUtil = null;
		}
	}

	/**
	 *
	 * @return
	 */
	private DataSetCacheManager getDataSetCacheManager() {
		return this.session.getDataSetCacheManager();
	}

	/**
	 * @return
	 * @throws DataException
	 */
	private int getCacheCapability() throws DataException {
		return getDataSetCacheManager().getCacheCapability();
	}

	/**
	 * @return
	 * @throws DataException
	 */
	private IDataSetCacheObject getCacheObject() throws DataException {
		return getDataSetCacheManager().getSavedCacheObject();
	}
}
