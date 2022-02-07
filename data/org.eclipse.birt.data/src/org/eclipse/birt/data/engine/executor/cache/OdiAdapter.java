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

package org.eclipse.birt.data.engine.executor.cache;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.executor.dscache.DataSetFromCache;
import org.eclipse.birt.data.engine.executor.dscache.DataSetToCache;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Adapt Oda and Odi interface to a single class, which will provide a uniform
 * method to retrieve data.
 */
public class OdiAdapter {
	// from Oda
	private ResultSet odaResultSet;

	// from data set whose result set needs to be cached
	private DataSetToCache datasetToCache;

	// from odi
	private ICustomDataSet customDataSet;

	// from IResultIterator
	private IResultIterator resultIterator;

	// The behavior of "next" method in IResultIterator is slightly
	// different from that of "fetch" method.To mimic the behavior of
	// fetch method we define a boolean to mark the beginning of an IResultIterator
	boolean riStarted = false;

	// from parent query in sub query
	private ResultSetCache resultSetCache;

	// from input stream
	private ResultObjectReader roReader;

	// from Joint data set
	private IDataSetPopulator populator;

	// from data set whose result is loaded from cache
	private DataSetFromCache datasetFromCache;

	private IResultClass resultClass;

	private Set columnIndexListForTypeConvert = null;

	/**
	 * Construction
	 * 
	 * @param odaResultSet
	 */
	public OdiAdapter(ResultSet odaResultSet, IResultClass resultClass) {
		assert odaResultSet != null;
		this.odaResultSet = odaResultSet;
		this.resultClass = resultClass;

		for (int i = 1; i <= resultClass.getFieldCount(); i++) {
			try {
				if ((resultClass.getFieldMetaData(i).getDriverProvidedDataType() == null)
						|| (resultClass.getFieldMetaData(i).getDataType() != resultClass.getFieldMetaData(i)
								.getDriverProvidedDataType())) {
					if (columnIndexListForTypeConvert == null)
						columnIndexListForTypeConvert = new HashSet();
					columnIndexListForTypeConvert.add(Integer.valueOf(i));
				}
			} catch (DataException e) {
			}
		}
	}

	/**
	 * Construction
	 * 
	 * @param datasetCacheResultSet
	 */
	public OdiAdapter(DataSetToCache datasetToCache) {
		assert datasetToCache != null;
		this.datasetToCache = datasetToCache;
	}

	public OdiAdapter(DataSetFromCache datasetFromCache) {
		assert datasetFromCache != null;
		this.datasetFromCache = datasetFromCache;
	}

	/**
	 * Construction
	 * 
	 * @param customDataSet
	 */
	public OdiAdapter(ICustomDataSet customDataSet) {
		assert customDataSet != null;
		this.customDataSet = customDataSet;
	}

	/**
	 * Construction
	 * 
	 * @param customDataSet
	 */
	OdiAdapter(ResultSetCache resultSetCache) {
		assert resultSetCache != null;
		this.resultSetCache = resultSetCache;
	}

	/**
	 * Construction
	 * 
	 * @param customDataSet
	 */
	public OdiAdapter(IResultIterator resultSetCache) {
		assert resultSetCache != null;
		this.resultIterator = resultSetCache;
	}

	/**
	 * Construction
	 * 
	 * @param roReader
	 */
	OdiAdapter(ResultObjectReader roReader) {
		assert roReader != null;
		this.roReader = roReader;
	}

	/**
	 * Construction
	 * 
	 */
	public OdiAdapter(IDataSetPopulator populator) {
		assert populator != null;
		this.populator = populator;
	}

	private IResultObject getConvertedResultObject(IResultObject resultObject) throws DataException {
		if (resultObject == null)
			return null;
		if (columnIndexListForTypeConvert == null)
			return resultObject;
		Object[] obj = new Object[resultClass.getFieldCount()];
		for (int i = 1; i <= resultClass.getFieldCount(); i++) {
			if (columnIndexListForTypeConvert.contains(i)) {
				try {
					obj[i - 1] = DataTypeUtil.convert(resultObject.getFieldValue(i),
							DataTypeUtil.toApiDataType(resultClass.getFieldMetaData(i).getDataType()));
				} catch (BirtException e) {
					throw DataException.wrap(e);
				}
			} else {
				obj[i - 1] = resultObject.getFieldValue(i);
			}
		}
		IResultObject result = new ResultObject(resultClass, obj);
		return result;
	}

	/**
	 * Fetch data from Oda or Odi. After the fetch is done, the cursor must stay at
	 * the row which is fetched.
	 * 
	 * @param stopSign
	 * @return
	 * @throws DataException
	 */
	IResultObject fetch() throws DataException {
		if (odaResultSet != null) {
			return getConvertedResultObject(odaResultSet.fetch());
		} else if (datasetToCache != null) {
			return datasetToCache.fetch();
		} else if (datasetFromCache != null) {
			return datasetFromCache.fetch();
		} else if (customDataSet != null) {
			return customDataSet.fetch();
		} else if (resultIterator != null) {
			if (!riStarted)
				riStarted = true;
			else
				this.resultIterator.next();

			return this.resultIterator.getCurrentResult();
		} else if (roReader != null) {
			return roReader.fetch();
		} else if (populator != null) {
			return populator.next();
		} else {
			return resultSetCache.fetch();
		}
	}

}
