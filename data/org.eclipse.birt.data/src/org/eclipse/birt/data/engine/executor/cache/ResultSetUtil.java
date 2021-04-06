/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.cache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.IPushedDownExpression;
import org.eclipse.birt.data.engine.impl.StringTable;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.impl.index.DataSetInMemoryStringIndex;
import org.eclipse.birt.data.engine.impl.index.IIndexSerializer;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * 
 */
public class ResultSetUtil {
	// ----------------------service for result object save and load--------------

	/**
	 * Write the result object value if it is used in column binding map
	 * 
	 * @param dos
	 * @param resultObject
	 * @param nameSet
	 * @throws DataException
	 * @throws IOException
	 */
	public static int writeResultObject(DataOutputStream dos, IResultObject resultObject, int count, Set nameSet,
			Map<String, StringTable> stringTableMap, Map<String, IIndexSerializer> index, int rowIndex, int version)
			throws DataException, IOException {
		return writeResultObject(dos, resultObject, count, nameSet, stringTableMap, index, rowIndex, version, false);
	}

	public static int writeResultObject(DataOutputStream dos, IResultObject resultObject, int count, Set nameSet,
			Map<String, StringTable> stringTableMap, Map<String, IIndexSerializer> index, int rowIndex, int version,
			boolean saveRowId) throws DataException, IOException {
		if (resultObject.getResultClass() == null)
			return 0;

		ByteArrayOutputStream tempBaos = new ByteArrayOutputStream();
		BufferedOutputStream tempBos = new BufferedOutputStream(tempBaos);
		DataOutputStream tempDos = new DataOutputStream(tempBos);

		for (int i = 1; i <= count; i++) {
			if (nameSet != null && (nameSet.contains(resultObject.getResultClass().getFieldName(i))
					|| nameSet.contains(resultObject.getResultClass().getFieldAlias(i)))) {
				IIndexSerializer currIndex = index.get(resultObject.getResultClass().getFieldName(i));
				Object candidate = resultObject.getFieldValue(i);
				if (currIndex != null) {
//					if ( currIndex instanceof SerializableBirtHash )
//					{
//						SerializableBirtHash hashIndex = (SerializableBirtHash) currIndex;
//						if ( hashIndex.containsKey( candidate ) )
//						{
//							( (List) hashIndex.get( candidate ) ).add( rowIndex );
//						}
//						else
//						{
//							List<Integer> rowIdList = new ArrayList<Integer>( );
//							rowIdList.add( rowIndex );
//							currIndex.put( candidate, rowIdList );
//						}
//						IOUtil.writeObject( tempDos,
//								hashIndex.getKeyValue( candidate ) );
//					}
//					else if ( currIndex instanceof SerializableDataSetNumberIndex )
					{
						currIndex.put(candidate, rowIndex);
					}
				}

				StringTable table = null;
				if (stringTableMap != null)
					table = stringTableMap.get(resultObject.getResultClass().getFieldName(i));
				if (table != null) {
					int stringIndex = table.getIndex((String) resultObject.getFieldValue(i));
//					IOUtil.writeObject( tempDos, stringIndex );
					IOUtil.writeInt(tempDos, stringIndex);
				} else {
					if (version > VersionManager.VERSION_3_7_2_1) {
						ResultObjectUtil.writeObject(tempDos, resultObject.getFieldValue(i),
								resultObject.getResultClass().getFieldValueClass(i), version);
					} else {
						IOUtil.writeObject(tempDos, resultObject.getFieldValue(i));
					}
				}
			}
		}
		if (saveRowId) {
			IOUtil.writeInt(tempDos, (Integer) resultObject.getFieldValue(ExprMetaUtil.POS_NAME));
		}
		tempDos.flush();
		tempBos.flush();
		tempBaos.flush();

		byte[] bytes = tempBaos.toByteArray();
		int rowBytes = bytes.length;
		IOUtil.writeRawBytes(dos, bytes);

		tempBaos = null;
		tempBos = null;
		tempDos = null;

		return rowBytes;
	}

	/**
	 * @param dis
	 * @param rsMeta
	 * @param count
	 * @return
	 * @throws IOException
	 */
	public static IResultObject readResultObject(DataInputStream dis, IResultClass rsMeta, int count,
			Map<String, StringTable> stringTableMap, Map<String, DataSetInMemoryStringIndex> index, int version,
			boolean rowIdSaved) throws DataException {
		int i = 0;
		try {
			Object[] obs = new Object[rsMeta.getFieldCount()];

			for (i = 0; i < count; i++) {
				Class<?> typeClazz = rsMeta.wasAnyType(i + 1) ? DataType.getClass(DataType.ANY_TYPE)
						: rsMeta.getFieldValueClass(i + 1);

				if (rsMeta.isIndexColumn(i + 1) && (index.containsKey(rsMeta.getFieldName(i + 1)))) {
					obs[i] = IOUtil.readObject(dis, DataEngineSession.getCurrentClassLoader());
					obs[i] = index.get(rsMeta.getFieldName(i + 1)).getKeyValue(obs[i]);
				} else if (rsMeta.getFieldMetaData(i + 1).getDataType() == String.class
						&& rsMeta.isCompressedColumn(i + 1)) {
					StringTable stringTable = null;
					if (stringTableMap != null) {
						stringTable = stringTableMap.get(rsMeta.getFieldName(i + 1));
					}
					if (stringTable != null) {
						obs[i] = stringTable.getStringValue(IOUtil.readInt(dis));
					} else {
						if (version > VersionManager.VERSION_3_7_2_1) {
							obs[i] = ResultObjectUtil.readObject(dis, typeClazz,
									DataEngineSession.getCurrentClassLoader(), version);
						} else {
							obs[i] = IOUtil.readObject(dis, DataEngineSession.getCurrentClassLoader());
						}
					}
				} else {
					if (version > VersionManager.VERSION_3_7_2_1) {
						obs[i] = ResultObjectUtil.readObject(dis, typeClazz, DataEngineSession.getCurrentClassLoader(),
								version);
					} else {
						obs[i] = IOUtil.readObject(dis, DataEngineSession.getCurrentClassLoader());
					}
				}
			}
			ResultObject resultObject = new ResultObject(rsMeta, obs);
			if (rowIdSaved) {
				resultObject.setCustomFieldValue(ExprMetaUtil.POS_NAME, IOUtil.readInt(dis));
			}
			return resultObject;
		} catch (IOException e) {
			Throwable t = e.getCause();
			if (t instanceof ClassNotFoundException) {
				throw new DataException(ResourceConstants.FAIL_LOAD_CLASS, e, new String[] { t.getMessage(),
						rsMeta.getFieldNativeTypeName(i + 1), rsMeta.getFieldName(i + 1), });
			} else {
				throw new DataException(ResourceConstants.FAIL_LOAD_COLUMN_VALUE, e,
						new String[] { rsMeta.getFieldNativeTypeName(i + 1), rsMeta.getFieldName(i + 1) });
			}
		}

	}

	/**
	 * Get result set column name collection from column binding map
	 * 
	 * @param cacheRequestMap
	 * @return
	 * @throws DataException
	 */
	public static Set getRsColumnRequestMap(List<IBinding> cacheRequestMap) throws DataException {
		Set resultSetNameSet = new HashSet();
		if (cacheRequestMap != null) {
			Iterator<IBinding> iter = cacheRequestMap.iterator();
			List<String> dataSetColumnList = null;
			while (iter.hasNext()) {
				IBinding binding = iter.next();
				dataSetColumnList = null;

				if (binding != null) {
					if (binding.getExpression() != null) {
						dataSetColumnList = ExpressionCompilerUtil
								.extractDataSetColumnExpression(binding.getExpression());
						if (binding.getExpression() instanceof IPushedDownExpression) {
							dataSetColumnList.addAll(ExpressionCompilerUtil.extractDataSetColumnExpression(
									((IPushedDownExpression) binding.getExpression()).getOriginalExpression()));
						}
					} else
						dataSetColumnList = ExpressionCompilerUtil
								.extractDataSetColumnExpression(getArgumentExpression(binding));
				}
				if (dataSetColumnList != null) {
					resultSetNameSet.addAll(dataSetColumnList);
				}
			}
		}
		return resultSetNameSet;
	}

	private static IBaseExpression getArgumentExpression(IBinding binding) throws DataException {
		List arguments = binding.getArguments();
		if (arguments != null && arguments.size() > 0) {
			return (IBaseExpression) arguments.get(0);
		}
		return null;
	}

}
