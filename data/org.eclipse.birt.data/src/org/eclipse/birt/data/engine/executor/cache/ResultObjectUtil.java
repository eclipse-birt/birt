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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.io.ByteArrayInputStream;
import org.eclipse.birt.data.engine.executor.cache.io.ByteArrayOutputStream;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;

/**
 * Utility class for ResultObject for serialize and deserialize. Since available
 * memory is bound in a specific case, data needs to be export to file and
 * import from it. Therefore, the operation of serialization and deserialization
 * is necessary and important.
 */
public class ResultObjectUtil {
	// column count of current processed table
	private int columnCount;

	// data type array of result set
	private Class[] typeArray;

	// meta data of result set
	private IResultClass rsMetaData;

	private DataEngineSession session;

	/**
	 * In serializaing data to file and deserializing it from file, metadata
	 * information is necessary to know which data type a column is, and then proper
	 * read/write method will be called. This method must be called at first when
	 * any actual read/write action is taken. Since multi thread might call DtE at
	 * the same time, an instance needs to be new to correspond to different
	 * metadata.
	 *
	 * @param rsMetaData
	 * @throws DataException
	 */
	public static ResultObjectUtil newInstance(IResultClass rsMetaData, DataEngineSession session) {
		ResultObjectUtil instance = new ResultObjectUtil();
		int length = rsMetaData.getFieldCount();
		instance.typeArray = new Class[length];
		for (int i = 0; i < length; i++) {
			try {
				instance.typeArray[i] = rsMetaData.getFieldValueClass(i + 1);
			} catch (DataException e) {
				// the index will be always valid
			}
		}

		instance.columnCount = rsMetaData.getFieldCount();
		instance.rsMetaData = rsMetaData;
		instance.session = session;
		return instance;
	}

	/**
	 * Contruction, private
	 */
	private ResultObjectUtil() {
	}

	/**
	 * New a instance of ResultObject according to the parameter of object array
	 * plus the metadata stored before.
	 *
	 * @param ob
	 * @return RowData
	 */
	public ResultObject newResultObject(Object[] rowData) {
		return new ResultObject(rsMetaData, rowData);
	}

	/**
	 * Deserialze result object array from input stream. The reading procedure is
	 * strictly sequential, that means there is no random access.
	 *
	 * Datatype Corresponds to executor#setDataType
	 *
	 * @param br       input stream
	 * @param length   how many objects needs to be read
	 * @param stopSign
	 * @return result object array
	 * @throws IOException
	 * @throws DataException
	 */
	public IResultObject[] readData(InputStream bis, ClassLoader classLoader, int length)
			throws IOException, DataException {
		ResultObject[] rowDatas = new ResultObject[length];

		int rowLen;
		byte[] rowDataBytes;

		ByteArrayInputStream bais;
		DataInputStream dis;

		for (int i = 0; i < length; i++) {
			if (session.getStopSign().isStopped()) {
				break;
			}
			rowLen = IOUtil.readInt(bis);
			rowDataBytes = new byte[rowLen];
			int readSize = bis.read(rowDataBytes);
			int totalSize = readSize;
			while (readSize > 0 && totalSize < rowLen) {
				readSize = bis.read(rowDataBytes, totalSize, rowLen - totalSize);
				totalSize += readSize;
			}

			bais = new ByteArrayInputStream(rowDataBytes);
			dis = new DataInputStream(bais);

			Object[] obs = new Object[columnCount];
			for (int j = 0; j < columnCount; j++) {
				Class fieldType = typeArray[j];
				obs[j] = readObject(dis, fieldType, classLoader, VersionManager.getLatestVersion());
			}
			rowDatas[i] = newResultObject(obs);

			rowDataBytes = null;
			dis = null;
			bais = null;
		}

		return rowDatas;
	}

	public static Object readObject(DataInputStream dis, Class fieldType, ClassLoader classLoader, int version)
			throws IOException, DataException {
		Object obj = null;
		char leadingChar = (char) dis.read();
		if (leadingChar == 0) {
			obj = null;
			return obj;
		}

		if (fieldType.equals(Integer.class)) {
			obj = Integer.valueOf(dis.readInt());
		} else if (fieldType.equals(Double.class)) {
			obj = new Double(dis.readDouble());
		} else if (fieldType.equals(BigDecimal.class)) {
			obj = new BigDecimal(dis.readUTF());
		} else if (fieldType.equals(Time.class)) {
			obj = new Time(dis.readLong());
		} else if (fieldType.equals(Timestamp.class)) {
			obj = new Timestamp(dis.readLong());
		} else if (fieldType.equals(java.sql.Date.class)) {
			try {
				obj = DataTypeUtil.toSqlDate(new java.sql.Date(dis.readLong()));
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
		} else if (Date.class.isAssignableFrom(fieldType)) {
			obj = new Date(dis.readLong());
		} else if (fieldType.equals(Boolean.class)) {
			obj = Boolean.valueOf(dis.readBoolean());
		} else if (fieldType.equals(String.class)) {
			obj = IOUtil.readString(dis);
		} else if (fieldType.equals(IClob.class) || fieldType.equals(Clob.class)) {
			obj = IOUtil.readString(dis);
		} else if (fieldType.equals(IBlob.class) || fieldType.equals(Blob.class)) {
			if (version < VersionManager.VERSION_4_2_2) {
				int len = IOUtil.readInt(dis);
				if (len == 0) {
					obj = null;
				} else {
					byte[] bytes = new byte[len];
					dis.read(bytes);
					obj = bytes;
				}
			} else {
				int byteLength = leadingChar >> 1;

				if (byteLength >= 127) {
					byteLength = IOUtil.readInt(dis);
				}
				byte[] bytes = new byte[byteLength];
				dis.readFully(bytes);

				obj = bytes;
			}
		} else if (fieldType.equals(int[].class)) {
			int[] result = new int[IOUtil.readInt(dis)];
			for (int i = 0; i < result.length; i++) {
				result[i] = dis.readInt();
			}
			obj = result;
		} else if (fieldType.equals(Object.class) || fieldType.equals(DataType.getClass(DataType.ANY_TYPE))) {
			obj = IOUtil.readObject(dis, classLoader);
		} else {
			throw new DataException(ResourceConstants.BAD_DATA_TYPE, fieldType.toString());
		}
		return obj;
	}

	/**
	 * Serialze result object array to file. The serialize procedure is conversed
	 * with de-serialize(read) procedure.
	 *
	 * @param bos           output stream
	 * @param resultObjects result objects needs to be deserialized
	 * @param length        how many objects to be deserialized
	 * @param stopSign
	 * @throws IOException
	 * @throws DataException
	 */
	public void writeData(OutputStream bos, IResultObject[] resultObjects, int length)
			throws IOException, DataException {
		for (int i = 0; i < length; i++) {
			writeData(bos, resultObjects[i]);
			if (session.getStopSign().isStopped()) {
				return;
			}
		}
	}

	/**
	 *
	 * @param bos
	 * @param resultObject
	 * @throws IOException
	 * @throws DataException
	 */
	public void writeData(OutputStream bos, IResultObject resultObject) throws IOException, DataException {
		byte[] rowsDataBytes;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		for (int j = 0; j < columnCount; j++) {
			Object fieldValue = null;
			try {
				fieldValue = resultObject.getFieldValue(j + 1);
			} catch (DataException e) {
				// never get here since the index value is always value
			}

			Class fieldType = typeArray[j];
			writeObject(dos, fieldValue, fieldType, VersionManager.getLatestVersion());
		}
		dos.flush();

		rowsDataBytes = baos.toByteArray();
		IOUtil.writeInt(bos, rowsDataBytes.length);
		bos.write(rowsDataBytes);

		rowsDataBytes = null;
		dos = null;
		baos = null;
	}

	public static void writeObject(DataOutputStream dos, Object fieldValue, Class fieldType, int version)
			throws IOException, DataException {
		// No Version control needed. Previous we write byte 1 & 0, which is
		// perfectly convert to char 1 & 0.

		Object convertedObj = null;
		// process null object
		if (fieldValue != null) {
			if (fieldType.equals(Integer.class)) {
				convertedObj = convert(fieldValue, DataType.INTEGER_TYPE);
			} else if (fieldType.equals(Double.class)) {
				convertedObj = convert(fieldValue, DataType.DOUBLE_TYPE);
			} else if (fieldType.equals(BigDecimal.class)) {
				convertedObj = convert(fieldValue, DataType.DECIMAL_TYPE);
			} else if (Date.class.isAssignableFrom(fieldType)) {
				convertedObj = convert(fieldValue, DataType.DATE_TYPE);
			} else if (fieldType.equals(Boolean.class)) {
				convertedObj = convert(fieldValue, DataType.BOOLEAN_TYPE);
			} else {
				convertedObj = fieldValue;
			}
		}

		char leadingChar = 0;
		if (convertedObj == null) {
			dos.write(leadingChar);
			return;
		}
		leadingChar = 1;

		if (fieldType.equals(Integer.class)) {
			dos.write(leadingChar);
			dos.writeInt(((Integer) convertedObj).intValue());
		} else if (fieldType.equals(Double.class)) {
			dos.write(leadingChar);
			dos.writeDouble(((Double) convertedObj).doubleValue());
		} else if (fieldType.equals(BigDecimal.class)) {
			dos.write(leadingChar);
			dos.writeUTF(((BigDecimal) convertedObj).toString());
		} else if (Date.class.isAssignableFrom(fieldType)) {
			dos.write(leadingChar);
			dos.writeLong(((Date) convertedObj).getTime());
		} else if (fieldType.equals(Boolean.class)) {
			dos.write(leadingChar);
			dos.writeBoolean(((Boolean) convertedObj).booleanValue());
		} else if (fieldType.equals(String.class)) {
			dos.write(leadingChar);
			IOUtil.writeString(dos, fieldValue.toString());
		} else if (fieldType.equals(IClob.class) || fieldType.equals(Clob.class)) {
			dos.write(leadingChar);
			IOUtil.writeString(dos, fieldValue.toString());
		} else if (fieldType.equals(IBlob.class) || fieldType.equals(Blob.class)) {
			byte[] bytes = (byte[]) fieldValue;
			if (version < VersionManager.VERSION_4_2_2) {
				dos.write(leadingChar);
				if (bytes == null || bytes.length == 0) {
					IOUtil.writeInt(dos, 0);
				} else {
					IOUtil.writeInt(dos, bytes.length);
					dos.write((byte[]) fieldValue);
				}
			} else {
				int byteLength = bytes.length;

				if (byteLength < 127) {
					// Here populate the high 7 bits to the byte length
					leadingChar = (char) ((byteLength << 1) | leadingChar);
					dos.write(leadingChar);
				} else {
					// Here populate all 7 high bits to indicate the size is 127+, and need extra
					// integer to save actual size.
					leadingChar = (char) ((127 << 1) | leadingChar);
					dos.write(leadingChar);
					IOUtil.writeInt(dos, bytes.length);
				}

				dos.write((byte[]) fieldValue);
			}
		} else if (fieldType.equals(Object.class) || fieldType.equals(DataType.getClass(DataType.ANY_TYPE))) {
			if (!(fieldValue instanceof Serializable)) {
				throw new DataException(ResourceConstants.NOT_SERIALIZABLE_CLASS, fieldValue.getClass().getName());
			}
			dos.write(leadingChar);
			IOUtil.writeObject(dos, fieldValue);
		} else {
			throw new DataException(ResourceConstants.BAD_DATA_TYPE, fieldType.toString());
		}
	}

	private static Object convert(Object o, int type) throws DataException {
		try {
			return DataTypeUtil.convert(o, type);
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}
}
