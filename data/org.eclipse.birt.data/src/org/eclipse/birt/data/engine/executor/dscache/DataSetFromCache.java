
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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.IDataSetCacheObject;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Data set whose result set is loaded from cache
 */

public class DataSetFromCache {
	// result set metadata
	private IResultClass rsMeta;

	private ILoadUtil loadUtil;
	private DataEngineSession session;

	// for computed column
	private List addedTempComputedColumn;
	private int realColumnCount;

	private int countLimit = 0, fetched = 0;

	public DataSetFromCache(DataEngineSession session) {
		this.session = session;
		try {
			countLimit = session.getDataSetCacheManager().getCacheCapability();
			session.getDataSetCacheManager().loadStart();
		} catch (DataException e) {
		}
	}

	/**
	 * 
	 * @param stopSign
	 * @return next data
	 * @throws DataException
	 */
	public IResultObject fetch() throws DataException {
		return loadObject();
	}

	/**
	 * @return cached object
	 * @throws DataException
	 */
	private IResultObject loadObject() throws DataException {
		if (rsMeta == null) {
			getResultClass();
		}
		if (loadUtil == null) {
			loadUtil = CacheUtilFactory.createLoadUtil(getCacheObject(), this.session);
		}

		IResultObject cacheObject = loadUtil == null ? null : loadUtil.loadObject();

		if (cacheObject == null) {
			return cacheObject;
		}

		fetched++;
		if (fetched <= countLimit || countLimit <= 0) {
			if (addedTempComputedColumn != null && addedTempComputedColumn.size() > 0) {
				ResultObject resultObject = new ResultObject(getResultClass(), getAllObjects(cacheObject));

				return resultObject;
			} else {
				return cacheObject;
			}
		} else
			return null;
	}

	/**
	 * get all new field objects from a cacheObject
	 * 
	 * @param cacheObject
	 * @return
	 * @throws DataException
	 */
	private Object[] getAllObjects(IResultObject cacheObject) throws DataException {
		Object[] objects = new Object[realColumnCount + addedTempComputedColumn.size()];
		for (int i = 0; i < realColumnCount; i++) {
			objects[i] = cacheObject.getFieldValue(i + 1);
		}
		return objects;
	}

	/**
	 * @return IResultClass
	 * @throws DataException
	 */
	private IResultClass loadResultClass() throws DataException {
		if (loadUtil == null)
			loadUtil = CacheUtilFactory.createLoadUtil(getCacheObject(), this.session);

		return loadUtil.loadResultClass();
	}

	/**
	 * @return
	 * @throws DataException
	 */
	private IDataSetCacheObject getCacheObject() throws DataException {
		return session.getDataSetCacheManager().getLoadedCacheObject();
	}

	/**
	 * @return result class
	 * @throws DataException
	 */
	public IResultClass getResultClass() throws DataException {
		if (rsMeta == null) {
			rsMeta = loadResultClass();
			if (addedTempComputedColumn != null && addedTempComputedColumn.size() > 0)
				processResultClass();
		}

		return rsMeta;
	}

	/**
	 * Remove old temp computed column metadatas from cache file and add new
	 * metadatas.
	 * 
	 * @throws DataException
	 */
	private void processResultClass() throws DataException {
		List metadataList = new ArrayList();
		this.realColumnCount = 0;

		ResultFieldMetadata metadata = null;
		int i = 0;
		for (i = 0; i < rsMeta.getFieldCount(); i++) {
			if (!isTempComputedColumn(rsMeta.getFieldName(i + 1))) {
				metadata = new ResultFieldMetadata(0, rsMeta.getFieldName(i + 1), rsMeta.getFieldLabel(i + 1),
						rsMeta.getFieldValueClass(i + 1), rsMeta.getFieldNativeTypeName(i + 1),
						rsMeta.isCustomField(i + 1), rsMeta.getAnalysisType(i + 1), rsMeta.getAnalysisColumn(i + 1),
						rsMeta.isIndexColumn(i + 1), rsMeta.isCompressedColumn(i + 1));
				metadata.setAlias(rsMeta.getFieldAlias(i + 1));
				metadataList.add(metadata);
				realColumnCount++;
			}
		}

		ComputedColumn tempComputedColumn = null;
		for (i = 0; i < addedTempComputedColumn.size(); i++) {
			tempComputedColumn = (ComputedColumn) (addedTempComputedColumn.get(i));
			metadata = new ResultFieldMetadata(0, tempComputedColumn.getName(), null,
					DataType.getClass(tempComputedColumn.getDataType()), null, true, -1);
			metadataList.add(metadata);
		}

		rsMeta = new ResultClass(metadataList);
	}

	/**
	 * Return whether a clumn is a temp computed column.
	 * 
	 * @param name
	 * @return
	 */
	private boolean isTempComputedColumn(String name) {
		return (name.matches("\\Q_{$TEMP_GROUP_\\E\\d*\\Q$}_\\E") || name.matches("\\Q_{$TEMP_SORT_\\E\\d*\\Q$}_\\E")
				|| name.matches("\\Q_{$TEMP_FILTER_\\E\\d*\\Q$}_\\E"));
	}

	/**
	 * set new temp computed columns
	 * 
	 * @param addedTempComputedColumn
	 */
	public void setTempComputedColumn(List addedTempComputedColumn) {
		this.addedTempComputedColumn = addedTempComputedColumn;
	}

	/**
	 * @throws DataException
	 */
	public void close() throws DataException {
		// when in save status, close might be done automatically
		if (loadUtil != null) {
			session.getDataSetCacheManager().loadFinished();
			loadUtil.close();
			loadUtil = null;
		}
	}

}
