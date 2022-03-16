
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
package org.eclipse.birt.data.engine.olap.data.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.executor.cache.SizeOfUtil;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;

/**
 *
 */

public class AggregationResultSetSaveUtil {
	private static String PREFIX_RESULTSET = "_ar_";

	/**
	 *
	 * @param name
	 * @param resultSets
	 * @param writer
	 * @throws IOException
	 */
	public static void save(String name, IAggregationResultSet[] resultSets, IDocArchiveWriter writer)
			throws IOException {
		if (writer == null || name == null) {
			return;
		}
		RAOutputStream outputStream = writer.createRandomAccessStream(name);
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		// write resultset length
		if (resultSets == null) {
			dataOutputStream.writeInt(-1);
			return;
		}
		dataOutputStream.writeInt(resultSets.length);
		dataOutputStream.close();

		resultSets = sortRsBeforeSaving(resultSets);
		for (int i = 0; i < resultSets.length; i++) {
			outputStream = writer.createRandomAccessStream(name + PREFIX_RESULTSET + i);
			dataOutputStream = new DataOutputStream(outputStream);
			saveOneResultSet(dataOutputStream, resultSets[i]);
			dataOutputStream.close();
		}
	}

	private static IAggregationResultSet[] sortRsBeforeSaving(IAggregationResultSet[] rs) {
		List<IAggregationResultSet> sortedAggregateRs = new ArrayList<>();
		for (IAggregationResultSet result : rs) {
			if (result.getAggregationDefinition().getDrilledInfo() == null) {
				sortedAggregateRs.add(result);
			}
		}
		for (IAggregationResultSet result : rs) {
			if (result.getAggregationDefinition().getDrilledInfo() != null) {
				sortedAggregateRs.add(result);
			}
		}
		return sortedAggregateRs.toArray(new IAggregationResultSet[0]);
	}

	/**
	 *
	 * @param name
	 * @param reader
	 * @throws IOException
	 */
	public static IAggregationResultSet[] load(String name, IDocArchiveReader reader, int version, long memoryCacheSize)
			throws IOException {
		DataInputStream dataInputStream = null;
		try {
			RAInputStream inputStream = reader.getStream(name);
			dataInputStream = new DataInputStream(inputStream);

			int size = dataInputStream.readInt();
			inputStream.close();
			if (size <= 0) {
				return null;
			}
			IAggregationResultSet[] result = new IAggregationResultSet[size];
			for (int i = 0; i < size; i++) {
				// Only in version 2_2_1 we save aggregation result set without
				// PREFIX_RESULTSET.
				if (version != VersionManager.VERSION_2_2_1) {
					inputStream = reader.getStream(name + PREFIX_RESULTSET + i);
				} else {
					inputStream = reader.getStream(name + i);
				}
				dataInputStream = new DataInputStream(inputStream);
				if (size < 3) {
					result[i] = loadOneResultSet(dataInputStream, memoryCacheSize / size);
				} else if (size >= 3 && size < 5) {
					result[i] = loadOneResultSet(dataInputStream, memoryCacheSize * 2 / size);
				} else if (size >= 5) {
					result[i] = loadOneResultSet(dataInputStream, memoryCacheSize * 3 / size);
				}
				dataInputStream.close();
			}
			return result;

		} catch (IOException ex) {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
			throw ex;
		}
	}

	private static IAggregationResultSet loadOneResultSet(DataInputStream dataInputStream, long memoryCacheSize)
			throws IOException {
		// read level
		String[] dimNames = convertToStringArray(readObjectArray(dataInputStream));
		String[] levelNames = convertToStringArray(readObjectArray(dataInputStream));
		DimLevel[] levels = null;
		if (dimNames != null && levelNames != null) {
			levels = new DimLevel[levelNames.length];
			for (int i = 0; i < levels.length; i++) {
				levels[i] = new DimLevel(dimNames[i], levelNames[i]);
			}
		}
		// read keys
		String[][] keyNames = convertToMDStringArray(readMDObjectArray(dataInputStream));
		int[][] keyDataTypes = readMDIntArray(dataInputStream);
		// read attribute
		String[][] attributeNames = convertToMDStringArray(readMDObjectArray(dataInputStream));
		int[][] attributeDataTypes = readMDIntArray(dataInputStream);
		// read sortType
		int[] sortTypes = readIntArray(dataInputStream);

		// read aggregation
		String[] aggregationNames = convertToStringArray(readObjectArray(dataInputStream));
		int[] aggregationDataType = readIntArray(dataInputStream);

		// read row size
		int size = IOUtil.readInt(dataInputStream);
		int keySize = 0;
		if (keyDataTypes != null) {
			for (int i = 0; i < keyDataTypes.length; i++) {
				if (keyDataTypes[i] != null) {
					keySize += SizeOfUtil.getObjectSize(keyDataTypes[i]);
				}
			}
		}
		int attributeSize = 0;
		if (attributeDataTypes != null) {
			for (int i = 0; i < attributeDataTypes.length; i++) {
				if (attributeDataTypes[i] != null) {
					attributeSize += SizeOfUtil.getObjectSize(attributeDataTypes[i]);
				}
			}
		}
		int aggregationSize = 0;
		if (aggregationDataType != null) {
			aggregationSize += SizeOfUtil.getObjectSize(aggregationDataType);
		}
		int rowSize = 16 + (4 + (keySize + attributeSize + aggregationSize) - 1) / 8 * 8;
		int bufferSize = (int) (memoryCacheSize / rowSize);

		return new CachedAggregationResultSet(dataInputStream, size, levels, sortTypes, keyNames, attributeNames,
				keyDataTypes, attributeDataTypes, aggregationNames, aggregationDataType, bufferSize);
	}

	/**
	 *
	 * @param objs
	 * @return
	 */
	private static String[] convertToStringArray(Object[] objs) {
		if (objs == null) {
			return null;
		}
		String[] result = new String[objs.length];
		System.arraycopy(objs, 0, result, 0, objs.length);
		return result;
	}

	/**
	 *
	 * @param objs
	 * @return
	 */
	private static String[][] convertToMDStringArray(Object[][] objs) {
		if (objs == null) {
			return null;
		}
		String[][] result = new String[objs.length][];
		for (int i = 0; i < result.length; i++) {
			result[i] = convertToStringArray(objs[i]);
		}
		return result;
	}

	/**
	 *
	 * @param outputStream
	 * @param resultSet
	 * @throws IOException
	 */
	private static void saveOneResultSet(DataOutputStream outputStream, IAggregationResultSet resultSet)
			throws IOException {
		saveMetaData(outputStream, resultSet);
		saveAggregationRowSet(outputStream, resultSet);
	}

	/**
	 *
	 * @param outputStream
	 * @param resultSet
	 * @throws IOException
	 */
	private static void saveAggregationRowSet(DataOutputStream outputStream, IAggregationResultSet resultSet)
			throws IOException {
		IOUtil.writeInt(outputStream, resultSet.length());
		for (int i = 0; i < resultSet.length(); i++) {
			resultSet.seek(i);
			saveAggregationRow(outputStream, resultSet.getCurrentRow());

		}
	}

	/**
	 *
	 * @param outputStream
	 * @param resultRow
	 * @throws IOException
	 */
	private static void saveAggregationRow(DataOutputStream outputStream, IAggregationResultRow resultRow)
			throws IOException {
		writeObjectArray(outputStream, resultRow.getFieldValues());
	}

	/*
	 *
	 */
	public static AggregationResultRow loadAggregationRow(DataInputStream inputStream) throws IOException {
		Object[] objects = readObjectArray(inputStream);
		if (objects == null || objects.length == 0) {
			return null;
		}
		return (AggregationResultRow) AggregationResultRow.getCreator().createInstance(objects);
	}

	/**
	 *
	 * @param outputStream
	 * @param aggregationDef
	 * @throws IOException
	 */
	private static void saveMetaData(DataOutputStream outputStream, IAggregationResultSet resultSet)
			throws IOException {
		// write level
		// level names
		DimLevel[] levels = resultSet.getAllLevels();
		String[] dimensionNames = null;
		String[] levelNames = null;
		if (levels != null) {
			dimensionNames = new String[levels.length];
			levelNames = new String[levels.length];
			for (int i = 0; i < levels.length; i++) {
				dimensionNames[i] = levels[i].getDimensionName();
				levelNames[i] = levels[i].getLevelName();
			}
		}
		// write DimLevels as seperate string arrays
		writeObjectArray(outputStream, dimensionNames);
		writeObjectArray(outputStream, levelNames);

		// level keys
		writeObjectArray(outputStream, resultSet.getLevelKeys());
		writeIntArray(outputStream, resultSet.getLevelKeyDataType());
		// level attribute
		writeObjectArray(outputStream, resultSet.getLevelAttributes());
		writeIntArray(outputStream, resultSet.getLevelAttributeDataType());
		// level sortType
		writeIntArray(outputStream, resultSet.getSortType());

		// write aggregation
		String[] aggregationNames = new String[resultSet.getAggregationCount()];
		for (int i = 0; i < aggregationNames.length; i++) {
			aggregationNames[i] = resultSet.getAggregationName(i);
		}
		writeObjectArray(outputStream, aggregationNames);

		writeIntArray(outputStream, resultSet.getAggregationDataType());
	}

	/**
	 *
	 * @param outputStream
	 * @param objects
	 * @throws IOException
	 */
	private static void writeObjectArray(DataOutputStream outputStream, Object[] objects) throws IOException {
		if (objects == null) {
			IOUtil.writeInt(outputStream, -1);
			return;
		}
		IOUtil.writeInt(outputStream, objects.length);
		for (int i = 0; i < objects.length; i++) {
			IOUtil.writeObject(outputStream, objects[i]);
		}
	}

	/**
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static Object[] readObjectArray(DataInputStream inputStream) throws IOException {
		int size = IOUtil.readInt(inputStream);
		if (size == -1) {
			return null;
		}
		Object[] result = new Object[size];
		for (int i = 0; i < result.length; i++) {
			result[i] = IOUtil.readObject(inputStream,
					org.eclipse.birt.data.engine.impl.DataEngineSession.getCurrentClassLoader());
		}
		return result;
	}

	/**
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static Object[][] readMDObjectArray(DataInputStream inputStream) throws IOException {
		int size = IOUtil.readInt(inputStream);
		if (size == -1) {
			return null;
		}
		Object[][] result = new Object[size][];
		for (int i = 0; i < result.length; i++) {
			result[i] = readObjectArray(inputStream);
		}
		return result;
	}

	/**
	 *
	 * @param outputStream
	 * @param objects
	 * @throws IOException
	 */
	private static void writeObjectArray(DataOutputStream outputStream, Object[][] objects) throws IOException {
		if (objects == null) {
			IOUtil.writeInt(outputStream, -1);
			return;
		}
		IOUtil.writeInt(outputStream, objects.length);
		for (int i = 0; i < objects.length; i++) {
			writeObjectArray(outputStream, objects[i]);
		}
	}

	/**
	 *
	 * @param outputStream
	 * @return
	 * @throws IOException
	 */
	private static int[] readIntArray(DataInputStream inputStream) throws IOException {
		int size = IOUtil.readInt(inputStream);
		if (size == -1) {
			return null;
		}
		int[] result = new int[size];
		for (int i = 0; i < result.length; i++) {
			result[i] = IOUtil.readInt(inputStream);
		}
		return result;
	}

	/**
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static int[][] readMDIntArray(DataInputStream inputStream) throws IOException {
		int size = IOUtil.readInt(inputStream);
		if (size == -1) {
			return null;
		}
		int[][] result = new int[size][];
		for (int i = 0; i < result.length; i++) {
			result[i] = readIntArray(inputStream);
		}
		return result;
	}

	/**
	 *
	 * @param outputStream
	 * @param objects
	 * @throws IOException
	 */
	private static void writeIntArray(DataOutputStream outputStream, int[] iA) throws IOException {
		if (iA == null) {
			IOUtil.writeInt(outputStream, -1);
			return;
		}
		IOUtil.writeInt(outputStream, iA.length);
		for (int i = 0; i < iA.length; i++) {
			IOUtil.writeInt(outputStream, iA[i]);
		}
	}

	/**
	 *
	 * @param outputStream
	 * @param iA
	 * @throws IOException
	 */
	private static void writeIntArray(DataOutputStream outputStream, int[][] iA) throws IOException {
		if (iA == null) {
			IOUtil.writeInt(outputStream, -1);
			return;
		}
		IOUtil.writeInt(outputStream, iA.length);
		for (int i = 0; i < iA.length; i++) {
			writeIntArray(outputStream, iA[i]);
		}
	}
}
