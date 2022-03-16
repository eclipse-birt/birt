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

package org.eclipse.birt.data.engine.impl.document;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.CacheUtil;
import org.eclipse.birt.data.engine.impl.StringTable;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.index.IAuxiliaryIndexCreator;
import org.eclipse.birt.data.engine.impl.index.IIndexSerializer;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 *
 */
public class StreamWrapper {
	private OutputStream streamForResultClass;
	private DataOutputStream streamForDataSet;
	private OutputStream streamForGroupInfo;
	private OutputStream streamForRowIndexInfo;
	private OutputStream streamForParentIndex;
	private DataOutputStream streamForDataSetLens;
	private StreamManager manager;
	private boolean enableIndexStream;
	private Map<IResultClass, Map<String, IIndexSerializer>> cachedIndex = new HashMap<>();
	private Map<IResultClass, Map<String, StringTable>> cachedStringTable = new HashMap<>();
	private List<IAuxiliaryIndexCreator> auxiliaryIndexCreators = new ArrayList<>();

	/**
	 * @param streamForResultClass
	 * @param streamForDataSet
	 * @param streamForGroupInfo
	 * @param streamForRowIndexInfo
	 */
	public StreamWrapper(StreamManager manager, OutputStream streamForResultClass, OutputStream streamForGroupInfo,
			OutputStream streamForRowIndexInfo, OutputStream streamForParentIndex) {
		this.streamForResultClass = streamForResultClass;
		this.streamForGroupInfo = streamForGroupInfo;
		this.streamForRowIndexInfo = streamForRowIndexInfo;
		this.streamForParentIndex = streamForParentIndex;
		this.manager = manager;
	}

	public StreamWrapper(StreamManager manager, OutputStream streamForResultClass, OutputStream streamForGroupInfo,
			OutputStream streamForRowIndexInfo, OutputStream streamForParentIndex, boolean enableIndex) {
		this(manager, streamForResultClass, streamForGroupInfo, streamForRowIndexInfo, streamForParentIndex);
		this.enableIndexStream = enableIndex;

	}

	public StreamManager getStreamManager() {
		return this.manager;
	}

	/**
	 * @return
	 */
	public OutputStream getStreamForResultClass() {
		return this.streamForResultClass;
	}

	/**
	 * @return
	 * @throws DataException
	 */
	public DataOutputStream getStreamForDataSet() throws DataException {
		if (this.streamForResultClass != null && this.streamForDataSet == null) {
			this.streamForDataSet = new DataOutputStream(manager.getOutStream(DataEngineContext.DATASET_DATA_STREAM,
					StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE));
		}
		return this.streamForDataSet;
	}

	public Map<String, StringTable> getOutputStringTable(IResultClass resultClass) throws DataException {
		if (!this.enableIndexStream) {
			return new HashMap<>();
		}
		if (cachedStringTable.containsKey(resultClass)) {
			return cachedStringTable.get(resultClass);
		}
		Map<String, StringTable> result = new HashMap<>();
		for (int i = 1; i <= resultClass.getFieldCount(); i++) {
			if (!resultClass.isCompressedColumn(i)) {
				continue;
			}
			Class dataType = resultClass.getFieldValueClass(i);
			if (dataType == String.class) {
				String fieldName = resultClass.getFieldName(i);
				StringTable stringTable = new StringTable();
				stringTable.setStreamManager(manager, fieldName);
				result.put(fieldName, stringTable);
			}
		}
		cachedStringTable.put(resultClass, result);
		return result;
	}

	public Map<String, IIndexSerializer> getStreamForIndex(IResultClass resultClass, Map appContext)
			throws DataException {
		if (!this.enableIndexStream) {
			return new HashMap<>();
		}

		if (this.cachedIndex.containsKey(resultClass)) {
			return this.cachedIndex.get(resultClass);
		}
		Map<String, IIndexSerializer> result = new HashMap<>();

		int indexColumnCount = 0;
		for (int i = 1; i <= resultClass.getFieldCount(); i++) {
			if (resultClass.isIndexColumn(i)) {
				indexColumnCount++;
			}
		}
		for (int i = 1; i <= resultClass.getFieldCount(); i++) {
			if (!resultClass.isIndexColumn(i)) {
				continue;
			}
			Class dataType = resultClass.getFieldValueClass(i);
			String fieldName = resultClass.getFieldName(i);
			long memoryBufferSize = CacheUtil.computeMemoryBufferSize(appContext);

			IIndexSerializer index = DataSetIndexFactory.createIndex(memoryBufferSize / indexColumnCount,
					"Index/" + fieldName + "/btreeIndex", manager, dataType);
			if (index != null) {
				result.put(fieldName, index);
			}

//			if ( dataType == String.class )
//			{
//				result.put( fieldName, new SerializableBirtHash( "Index/"
//						+ fieldName + "/index",
//						"Index/" + fieldName + "/value",
//						manager ) );
//			}
//			else
//			{
//				String indexFileName = "Index/"
//						+ resultClass.getFieldName( i ) + "/numberIndex";
//				if ( dataType == BigDecimal.class )
//				{
//
//					result.put( fieldName,
//							new SerializableDataSetNumberIndex<BigDecimal>( indexFileName,
//									this.manager ) );
//				}
//				else if ( dataType == Integer.class )
//				{
//					result.put( fieldName,
//							new SerializableDataSetNumberIndex<Integer>( indexFileName,
//									this.manager ) );
//				}
//				else if ( dataType == Double.class )
//				{
//					result.put( fieldName,
//							new SerializableDataSetNumberIndex<Double>( indexFileName,
//									this.manager ) );
//				}
//				else if ( dataType == java.util.Date.class )
//				{
//					result.put( fieldName,
//							new SerializableDataSetNumberIndex<java.util.Date>( indexFileName,
//									this.manager ) );
//				}
//				else if ( dataType == java.sql.Date.class )
//				{
//					result.put( fieldName,
//							new SerializableDataSetNumberIndex<java.sql.Date>( indexFileName,
//									this.manager ) );
//				}
//				else if ( dataType == Time.class )
//				{
//					result.put( fieldName,
//							new SerializableDataSetNumberIndex<Time>( indexFileName,
//									this.manager ) );
//				}
//				else if ( dataType == Timestamp.class )
//				{
//					result.put( fieldName,
//							new SerializableDataSetNumberIndex<Timestamp>( indexFileName,
//									this.manager ) );
//				}
//			}
		}
		this.cachedIndex.put(resultClass, result);
		return result;
	}

	/**
	 * @return
	 * @throws DataException
	 */
	public DataOutputStream getStreamForDataSetRowLens() throws DataException {
		if (this.streamForResultClass != null && this.streamForDataSetLens == null) {
			this.streamForDataSetLens = new DataOutputStream(manager.getOutStream(
					DataEngineContext.DATASET_DATA_LEN_STREAM, StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE));
		}
		return this.streamForDataSetLens;
	}

	/**
	 * @return
	 */
	public OutputStream getStreamForGroupInfo() {
		return this.streamForGroupInfo;
	}

	/**
	 * @return
	 */
	public OutputStream getStreamForRowIndexInfo() {
		return streamForRowIndexInfo;
	}

	/**
	 * @return
	 */
	public OutputStream getStreamForParentIndex() {
		return streamForParentIndex;
	}

	public List<IAuxiliaryIndexCreator> getAuxiliaryIndexCreators() {
		return auxiliaryIndexCreators;
	}

	public void addAuxiliaryIndexCreator(IAuxiliaryIndexCreator auxIndexCreator) {
		auxiliaryIndexCreators.add(auxIndexCreator);
	}

}
