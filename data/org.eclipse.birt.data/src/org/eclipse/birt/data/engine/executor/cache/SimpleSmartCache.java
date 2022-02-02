/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.executor.cache.disk.SimpleDiskCache;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.StringTable;
import org.eclipse.birt.data.engine.impl.index.IAuxiliaryIndexCreator;
import org.eclipse.birt.data.engine.impl.index.IIndexSerializer;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

public class SimpleSmartCache implements ResultSetCache {

	private ResultSetCache resultSetCache;
	private boolean isOpen = false;
	private IEventHandler eventHandler;
	private int count;
	private long usedMemorySize;
	private long memoryCacheSize;
	private List<IResultObject> resultObjectsList;
	private IResultClass rsMeta;
	private SizeOfUtil sizeOfUtil;
	private int maxRows;
	private SimpleDiskCache diskCache;

	// log instance
	private static Logger logger = Logger.getLogger(SimpleSmartCache.class.getName());

	private DataEngineSession session;

	public SimpleSmartCache(DataEngineSession session, IEventHandler eventHandler, IResultClass rsMeta)
			throws DataException {
		this.session = session;
		this.eventHandler = eventHandler;
		this.count = 0;
		this.usedMemorySize = 0;
		this.memoryCacheSize = CacheUtil.computeMemoryBufferSize(eventHandler.getAppContext());
		this.resultObjectsList = new ArrayList<IResultObject>();
		this.rsMeta = rsMeta;
		this.sizeOfUtil = new SizeOfUtil(rsMeta);
		this.maxRows = CacheUtil.getMaxRows(eventHandler.getAppContext());
	}

	public void add(IResultObject odaObject) throws DataException {
		if (memoryCacheSize == 0 || usedMemorySize < memoryCacheSize) {
			count++;
			if (maxRows > 0 && count > maxRows) {
				throw new DataException(ResourceConstants.EXCEED_MAX_DATA_OBJECT_ROWS);
			}
			addToMemoryCache(odaObject);
		} else {
			count++;
			addToDiskCache(odaObject);
		}
	}

	private void addToDiskCache(IResultObject odaObject) throws DataException {
		addToMemoryCache(odaObject);

		IResultObject[] resultObjects = (IResultObject[]) resultObjectsList.toArray(new IResultObject[0]);
		resultObjectsList.clear();
		if (diskCache == null) {
			diskCache = new SimpleDiskCache(resultObjects, rsMeta, resultObjects.length, maxRows, this.session);
		}
		diskCache.add(resultObjects);
	}

	private void addToMemoryCache(IResultObject odaObject) throws DataException {
		// the followed variable is for performance
		int odaObjectFieldCount = odaObject.getResultClass().getFieldCount();
		int metaFieldCount = rsMeta.getFieldCount();
		if (odaObjectFieldCount < metaFieldCount) {
			// Populate Data according to the given meta data.
			Object[] obs = new Object[metaFieldCount];
			for (int i = 1; i <= odaObjectFieldCount; i++) {
				obs[i - 1] = odaObject.getFieldValue(i);
			}
			odaObject = new ResultObject(rsMeta, obs);
		}
		resultObjectsList.add(odaObject);
		if (memoryCacheSize != 0)
			usedMemorySize += sizeOfUtil.sizeOf(odaObject);
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCount()
	 */
	public int getCount() throws DataException {
		open();

		return resultSetCache.getCount();
	}

	public void open() {
		if (!isOpen) {
			if (diskCache == null) {
				logger.fine("MemoryCache is used");

				IResultObject[] resultObjects = (IResultObject[]) resultObjectsList.toArray(new IResultObject[0]);
				resultSetCache = new MemoryCache(resultObjects, rsMeta, null);
			} else {
				logger.fine("DisckCache is used");

				resultSetCache = diskCache;
			}
			isOpen = true;
		}
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCurrentIndex ()
	 */
	public int getCurrentIndex() throws DataException {
		open();

		return resultSetCache.getCurrentIndex();
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCurrentResult
	 * ()
	 */
	public IResultObject getCurrentResult() throws DataException {
		open();

		return resultSetCache.getCurrentResult();
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#nextRow()
	 */
	public boolean next() throws DataException {
		open();

		return resultSetCache.next();
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#fetch()
	 */
	public IResultObject fetch() throws DataException {
		open();

		return resultSetCache.fetch();
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#moveTo(int)
	 */
	public void moveTo(int destIndex) throws DataException {
		open();

		resultSetCache.moveTo(destIndex);
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#reset()
	 */
	public void reset() throws DataException {
		open();

		resultSetCache.reset();
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#close()
	 */
	public void close() throws DataException {
		open();

		resultSetCache.close();
		resultSetCache = null;
		isOpen = false;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#saveToStream
	 * (java.io.OutputStream)
	 */
	public void doSave(DataOutputStream outputStream, DataOutputStream rowLensStream,
			Map<String, StringTable> stringTable, Map<String, IIndexSerializer> index, List<IBinding> cacheRequestMap,
			int version, List<IAuxiliaryIndexCreator> auxiliaryIndexCreators, boolean saveRowId) throws DataException {
		open();

		this.resultSetCache.doSave(outputStream, rowLensStream, stringTable, index, cacheRequestMap, version,
				auxiliaryIndexCreators, saveRowId);
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#saveToStream
	 * (java.io.OutputStream)
	 */
	public void incrementalUpdate(OutputStream outputStream, OutputStream rowLensStream, int originalRowCount,
			Map<String, StringTable> stringTable, Map<String, IIndexSerializer> map, List<IBinding> cacheRequestMap,
			int version, List<IAuxiliaryIndexCreator> auxiliaryIndexCreators) throws DataException {
		open();

		this.resultSetCache.incrementalUpdate(outputStream, rowLensStream, originalRowCount, stringTable, map,
				cacheRequestMap, version, auxiliaryIndexCreators);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.executor.cache.ResultSetCache#setResultClass
	 * (org.eclipse.birt.data.engine.odi.IResultClass)
	 */
	public void setResultClass(IResultClass rsMeta) throws DataException {
		open();

		this.resultSetCache.setResultClass(rsMeta);
	}

}
