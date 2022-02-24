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

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StringTable;
import org.eclipse.birt.data.engine.impl.index.IAuxiliaryIndexCreator;
import org.eclipse.birt.data.engine.impl.index.IIndexSerializer;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * A class caches the data of result set, in which filter and sorting on row
 * will be done. This class enables that external caller can do further data
 * process such as data grouping without caring how data is cached and retrieved
 * from memory, disk file or other data source.
 */
public interface ResultSetCache {

	/**
	 * @return current result index, 0-based
	 * @throws DataException
	 */
	int getCurrentIndex() throws DataException;

	/**
	 * @return current result object
	 * @throws DataException
	 */
	IResultObject getCurrentResult() throws DataException;

	/**
	 * Follows the convention of java.sql. The currRowIndex is initialized to -1,
	 * and only after next is called once, the pointer will move to the real data.
	 *
	 * @return true, if the new current row is valid
	 * @throws DataException
	 */
	boolean next() throws DataException;

	/**
	 * Move the cursor to the next result object, and then fetch its data
	 *
	 * @return next result object, null indicates beyond the end of result set
	 * @throws DataException
	 */
	IResultObject fetch() throws DataException;

	/**
	 * Move row index to specified position. this function should be called with
	 * care, since it might need to consume a lot of time when disk-based data
	 * manuipulation is used.
	 *
	 * @param destIndex
	 * @throws DataException
	 */
	void moveTo(int destIndex) throws DataException;

	/**
	 * @return count of result objects
	 */
	int getCount() throws DataException;

	/**
	 * Reset the current index to -1
	 */
	void reset() throws DataException;

	/**
	 * Close result cache, and do clean up work here. As for DiskCache, the
	 * temporary file will be deleted. So it is important to call this method when
	 * this cache will not be used any more.
	 */
	void close() throws DataException;

	/**
	 * Serialize to an output stream
	 *
	 * @param outputStream
	 * @param auxiliaryIndexCreators
	 */
	void doSave(DataOutputStream outputStream, DataOutputStream lensStream, Map<String, StringTable> stringTable,
			Map<String, IIndexSerializer> index, List<IBinding> cacheRequestMapping, int version,
			List<IAuxiliaryIndexCreator> auxiliaryIndexCreators, boolean saveRowId) throws DataException;

	/**
	 * Add incremental rows to output stream
	 *
	 * @param outputStream
	 * @param rowLensStream
	 * @param rowCount
	 * @param stringTable
	 * @param map
	 * @param cacheRequestMap
	 * @throws DataException
	 */
	void incrementalUpdate(OutputStream outputStream, OutputStream rowLensStream, int rowCount,
			Map<String, StringTable> stringTable, Map<String, IIndexSerializer> map, List<IBinding> cacheRequestMap,
			int version, List<IAuxiliaryIndexCreator> auxiliaryIndexCreators) throws DataException;

	/**
	 * Set the result class of the current result set cache.
	 *
	 * @throws DataException
	 */
	void setResultClass(IResultClass rsMeta) throws DataException;
}
