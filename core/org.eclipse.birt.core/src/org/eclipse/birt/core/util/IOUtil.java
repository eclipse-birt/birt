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

package org.eclipse.birt.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UTFDataFormatException;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

/**
 * A util class to read or write primitive Java data type. Please notice, every
 * method has a stream which might be input stream or output stream as
 * parameters. This stream should be already added a buffered layer underlying
 * it.
 */
public class IOUtil {

	public static final int INT_LENGTH = 4;

	public static final int LONG_LENGTH = 8;

	public static final int RA_STREAM_BUFFER_LENGTH = 8192;

	public static final int MAX_NUMBER_OF_STREAM_BUFFER = 128;

	protected static Logger logger = Logger.getLogger(IOUtil.class.getName());

	public final static <T> T read(DataInputStream inputStream, Class<T> clazz) throws IOException {
		Object result = null;
		if (clazz == short.class || clazz == Short.class) {
			result = readShort(inputStream);
		} else if (clazz == boolean.class || clazz == Boolean.class) {
			result = readBool(inputStream);
		} else if (clazz == byte[].class) {
			result = readBytes(inputStream);
		} else if (clazz == double.class || clazz == Double.class) {
			result = readDouble(inputStream);
		} else if (clazz == float.class || clazz == Float.class) {
			result = readFloat(inputStream);
		} else if (clazz == int.class || clazz == Integer.class) {
			result = readInt(inputStream);
		} else if (List.class.isAssignableFrom(clazz)) {
			result = readList(inputStream);
		} else if (clazz == long.class || clazz == Short.class) {
			result = readLong(inputStream);
		} else if (Map.class.isAssignableFrom(clazz)) {
			result = readMap(inputStream);
		} else if (clazz == String.class) {
			result = readString(inputStream);
		} else {
			result = readObject(inputStream);
		}
		return (T) result;
	}

	public final static <T> void write(DataOutputStream out, T object, Class<T> clazz) throws IOException {
		// debug( object.toString( ) );
		if (clazz == short.class || clazz == Short.class) {
			writeShort(out, (Short) object);
		} else if (clazz == boolean.class || clazz == Boolean.class) {
			writeBool(out, (Boolean) object);
		} else if (clazz == byte[].class) {
			writeBytes(out, (byte[]) object);
		} else if (clazz == double.class || clazz == Double.class) {
			writeDouble(out, (Double) object);
		} else if (clazz == float.class || clazz == Float.class) {
			writeFloat(out, (Float) object);
		} else if (clazz == int.class || clazz == Integer.class) {
			writeInt(out, (Integer) object);
		} else if (List.class.isAssignableFrom(clazz)) {
			writeList(out, (List) object);
		} else if (clazz == long.class || clazz == Long.class) {
			writeLong(out, (Long) object);
		} else if (Map.class.isAssignableFrom(clazz)) {
			writeMap(out, (Map) object);
		} else if (clazz == String.class) {
			writeString(out, (String) object);
		} else {
			writeObject(out, object);
		}
	}

	/**
	 * Read an int value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static short readShort(InputStream inputStream) throws IOException {
		int ch1 = inputStream.read();
		int ch2 = inputStream.read();
		if (ch1 == -1 || ch2 == -1) {
			throw new EOFException();
		}

		return (short) ((ch1 << 8) + ch2);
	}

	/**
	 * Write an int value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeShort(OutputStream outputStream, short value) throws IOException {
		outputStream.write((value >>> 8) & 0xFF);
		outputStream.write((value >>> 0) & 0xFF);
	}

	/**
	 * Read an int value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static int readInt(InputStream inputStream) throws IOException {
		int ch1 = inputStream.read();
		int ch2 = inputStream.read();
		int ch3 = inputStream.read();
		int ch4 = inputStream.read();
		if (ch1 == -1 || ch2 == -1 || ch3 == -1 || ch4 == -1) {
			throw new EOFException();
		}

		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	/**
	 * Write an int value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeInt(OutputStream outputStream, int value) throws IOException {
		outputStream.write((value >>> 24) & 0xFF);
		outputStream.write((value >>> 16) & 0xFF);
		outputStream.write((value >>> 8) & 0xFF);
		outputStream.write((value >>> 0) & 0xFF);
	}

	/**
	 * Assemble four bytes to an int value, make sure that the passed bytes length
	 * is 4.
	 * 
	 * @param bytes
	 * @return int value of bytes
	 */
	public final static int getInt(byte[] bytes) {
		assert bytes.length == 4;

		int ch1 = bytes[0] & 0xFF;
		int ch2 = bytes[1] & 0xFF;
		int ch3 = bytes[2] & 0xFF;
		int ch4 = bytes[3] & 0xFF;

		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	/**
	 * Assemble four bytes to an int value, make sure that the passed bytes length
	 * is larger than 4.
	 * 
	 * @param bytes
	 * @return int value of bytes
	 */
	public final static int bytesToInteger(byte[] b) {
		assert b.length >= 4;
		return ((b[0] & 0xFF) << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8) + ((b[3] & 0xFF) << 0);
	}

	/**
	 * Assemble eight bytes to an long value, make sure that the passed bytes length
	 * larger than 8.
	 * 
	 * @param bytes
	 * @return int value of bytes
	 */
	public final static long bytesToLong(byte[] b) {
		assert b.length >= 8;
		return ((b[0] & 0xFFL) << 56) + ((b[1] & 0xFFL) << 48) + ((b[2] & 0xFFL) << 40) + ((b[3] & 0xFFL) << 32)
				+ ((b[4] & 0xFFL) << 24) + ((b[5] & 0xFFL) << 16) + ((b[6] & 0xFFL) << 8) + ((b[7] & 0xFFL) << 0);

	}

	public final static void integerToBytes(int v, byte[] b) {
		assert b.length >= 4;
		b[0] = (byte) ((v >>> 24) & 0xFF);
		b[1] = (byte) ((v >>> 16) & 0xFF);
		b[2] = (byte) ((v >>> 8) & 0xFF);
		b[3] = (byte) ((v >>> 0) & 0xFF);
	}

	public final static void longToBytes(long v, byte[] b) {
		assert b.length >= 8;
		b[0] = (byte) ((v >>> 56) & 0xFF);
		b[1] = (byte) ((v >>> 48) & 0xFF);
		b[2] = (byte) ((v >>> 40) & 0xFF);
		b[3] = (byte) ((v >>> 32) & 0xFF);
		b[4] = (byte) ((v >>> 24) & 0xFF);
		b[5] = (byte) ((v >>> 16) & 0xFF);
		b[6] = (byte) ((v >>> 8) & 0xFF);
		b[7] = (byte) ((v >>> 0) & 0xFF);
	}

	/**
	 * Read a bool value from an input stream
	 * 
	 * @param inputStream
	 * @return boolean value
	 * @throws IOException
	 */
	public final static boolean readBool(InputStream inputStream) throws IOException {
		int v = inputStream.read();
		if (v == -1) {
			throw new EOFException();
		}
		return v == 0 ? false : true;
	}

	/**
	 * Write a boolean value to an output stream
	 * 
	 * @param outputStream
	 * @param bool
	 * @throws IOException
	 */
	public final static void writeBool(OutputStream outputStream, boolean bool) throws IOException {
		outputStream.write(bool == false ? 0 : 1);
	}

	/**
	 * Read a float value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static float readFloat(DataInputStream inputStream) throws IOException {
		return inputStream.readFloat();
	}

	/**
	 * Write a float value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeFloat(DataOutputStream outputStream, float value) throws IOException {
		outputStream.writeFloat(value);
	}

	/**
	 * Read a double value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static double readDouble(DataInputStream inputStream) throws IOException {
		return inputStream.readDouble();
	}

	/**
	 * Write a double value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeDouble(DataOutputStream outputStream, double value) throws IOException {
		outputStream.writeDouble(value);
	}

	/**
	 * Read a long value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static long readLong(DataInputStream inputStream) throws IOException {
		return inputStream.readLong();
	}

	/**
	 * Write a long value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeLong(DataOutputStream outputStream, long value) throws IOException {
		outputStream.writeLong(value);
	}

	/**
	 * Write a byte array to an output stream only with its raw content.
	 * 
	 * @param dos
	 * @param bytes , it can not be null
	 * @throws IOException
	 */
	public final static void writeRawBytes(DataOutputStream dos, byte[] bytes) throws IOException {
		assert bytes != null;
		dos.write(bytes);
	}

	// ------------for object read/write-------------------

	private static Map type2IndexMap;

	private static final int TYPE_NULL = 0;
	private static final int TYPE_INT = 1;
	private static final int TYPE_FLOAT = 2;
	private static final int TYPE_DOUBLE = 3;
	private static final int TYPE_BIG_DECIMAL = 4;
	private static final int TYPE_DATE_TIME = 5;
	private static final int TYPE_TIME = 6;
	private static final int TYPE_TIME_STAMP = 7;
	private static final int TYPE_BOOLEAN = 8;
	private static final int TYPE_STRING = 9;
	private static final int TYPE_BYTES = 10;
	private static final int TYPE_LIST = 11;
	private static final int TYPE_MAP = 12;
	private static final int TYPE_SERIALIZABLE = 13;

	private static final int TYPE_JSObject = 14;
	private static final int TYPE_LONG_STRING = 15;
	private static final int TYPE_DATE = 16;

	static {
		type2IndexMap = new HashMap();
		type2IndexMap.put(Integer.class, Integer.valueOf(TYPE_INT));
		type2IndexMap.put(Float.class, Integer.valueOf(TYPE_FLOAT));
		type2IndexMap.put(Double.class, Integer.valueOf(TYPE_DOUBLE));
		type2IndexMap.put(BigDecimal.class, Integer.valueOf(TYPE_BIG_DECIMAL));
		type2IndexMap.put(Date.class, Integer.valueOf(TYPE_DATE_TIME));
		type2IndexMap.put(Time.class, Integer.valueOf(TYPE_TIME));
		type2IndexMap.put(Timestamp.class, Integer.valueOf(TYPE_TIME_STAMP));
		type2IndexMap.put(Boolean.class, Integer.valueOf(TYPE_BOOLEAN));
		type2IndexMap.put(String.class, Integer.valueOf(TYPE_STRING));
		type2IndexMap.put(byte[].class, Integer.valueOf(TYPE_BYTES));
		type2IndexMap.put(List.class, Integer.valueOf(TYPE_LIST));
		type2IndexMap.put(Map.class, Integer.valueOf(TYPE_MAP));
		type2IndexMap.put(Serializable.class, Integer.valueOf(TYPE_SERIALIZABLE));
		type2IndexMap.put(null, Integer.valueOf(TYPE_NULL));
		type2IndexMap.put(java.sql.Date.class, Integer.valueOf(TYPE_DATE));

		type2IndexMap.put(IdScriptableObject.class, Integer.valueOf(TYPE_JSObject));
	}

	/**
	 * from object class to its type index value
	 * 
	 * @param obValue
	 * @return
	 */
	public static int getTypeIndex(Object obValue) {
		if (obValue == null)
			return TYPE_NULL;

		if (obValue instanceof String) {
			if (isLongString((String) obValue)) {
				return TYPE_LONG_STRING;
			} else {
				return TYPE_STRING;
			}
		}

		Integer indexOb = (Integer) type2IndexMap.get(obValue.getClass());
		if (indexOb == null) {
			if (obValue instanceof Map) {
				return TYPE_MAP;
			}
			if (obValue instanceof List) {
				return TYPE_LIST;
			}
			if (obValue instanceof Scriptable) {
				return TYPE_JSObject;
			}
			if (Timestamp.class.isAssignableFrom(obValue.getClass())) {
				return TYPE_TIME_STAMP;
			}
			if (Time.class.isAssignableFrom(obValue.getClass())) {
				return TYPE_TIME;
			}
			if (java.sql.Date.class.isAssignableFrom(obValue.getClass())) {
				return TYPE_DATE;
			}
			if (Date.class.isAssignableFrom(obValue.getClass())) {
				return TYPE_DATE_TIME;
			}
			if (obValue instanceof Serializable) {
				return TYPE_SERIALIZABLE;
			}
			return -1;
		}
		return indexOb.intValue();
	}

	/**
	 * Currently these data types are supported.
	 * 
	 * Integer Float Double BigDecimal Date Time Timestamp Boolean String byte[]
	 * List Map
	 * 
	 * @return
	 * @throws IOException
	 */
	public final static Object readObject(DataInputStream dis) throws IOException {
		return readObject(dis, null);
	}

	/**
	 * Currently these data types are supported.
	 * 
	 * Integer Float Double BigDecimal Date Time Timestamp Boolean String byte[]
	 * List Map
	 * 
	 * @return
	 * @throws IOException
	 */
	public final static Object readObject(DataInputStream dis, ClassLoader classLoader) throws IOException {
		// read data type from its index value
		int typeIndex = readInt(dis);
		// read real data
		Object obValue = null;
		switch (typeIndex) {
		case TYPE_NULL:
			break;
		case TYPE_INT:
			obValue = new Integer(dis.readInt());
			break;
		case TYPE_FLOAT:
			obValue = new Float(dis.readFloat());
			break;
		case TYPE_DOUBLE:
			obValue = new Double(dis.readDouble());
			break;
		case TYPE_BIG_DECIMAL:
			obValue = new BigDecimal(dis.readUTF());
			break;
		case TYPE_DATE_TIME:
			obValue = new Date(dis.readLong());
			break;
		case TYPE_TIME:
			obValue = new Time(dis.readLong());
			break;
		case TYPE_DATE:
			obValue = new java.sql.Date(dis.readLong());
			break;
		case TYPE_TIME_STAMP:
			obValue = new Timestamp(dis.readLong());
			break;
		case TYPE_BOOLEAN:
			obValue = Boolean.valueOf(dis.readBoolean());
			break;
		case TYPE_STRING:
			obValue = dis.readUTF();
			break;
		case TYPE_LONG_STRING:
			obValue = readUTF(dis);
			break;
		case TYPE_BYTES:
			int len = readInt(dis);
			byte[] bytes = new byte[len];
			if (len > 0)
				dis.readFully(bytes);
			obValue = bytes;
			break;
		case TYPE_LIST:
			obValue = readList(dis, classLoader);
			break;
		case TYPE_MAP:
			obValue = readMap(dis, classLoader);
			break;
		case TYPE_SERIALIZABLE:
			len = readInt(dis);
			if (len != 0) {
				bytes = new byte[len];
				dis.readFully(bytes);
				final ClassLoader loader = classLoader;
				ObjectInputStream oo = new ObjectInputStream(new ByteArrayInputStream(bytes)) {

					protected Class resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
						return Class.forName(desc.getName(), false, loader);
					}
				};
				try {
					obValue = oo.readObject();
				} catch (ClassNotFoundException ex) {
					IOException ie = new IOException(ex.getMessage());
					ie.initCause(ex);
					throw ie;
				}
			}
			break;
		case TYPE_JSObject:
			Object ob = IOUtil.readObject(dis, classLoader);
			obValue = JavascriptEvalUtil.convertToJavascriptValue(ob);
			break;
		default:
			assert false;
		}

		return obValue;
	}

	/**
	 * When obValue is not supported te be serialized, an IOException will be
	 * thrown.
	 * 
	 * @param dos
	 * @param obValue
	 * @throws IOException
	 */
	public final static void writeObject(DataOutputStream dos, Object obValue) throws IOException {
		// write data type index first
		int typeIndex = getTypeIndex(obValue);
		if (typeIndex == -1) {
			writeInt(dos, TYPE_NULL);
			throw new NotSerializableException(CoreMessages.getString(ResourceConstants.NOT_SERIALIZABLE));
		}

		writeInt(dos, typeIndex);

		// write real data
		switch (typeIndex) {
		case TYPE_NULL:
			break;
		case TYPE_INT:
			dos.writeInt(((Integer) obValue).intValue());
			break;
		case TYPE_FLOAT:
			dos.writeFloat(((Float) obValue).floatValue());
			break;
		case TYPE_DOUBLE:
			dos.writeDouble(((Double) obValue).doubleValue());
			break;
		case TYPE_BIG_DECIMAL:
			dos.writeUTF(((BigDecimal) obValue).toString());
			break;
		case TYPE_DATE_TIME:
			dos.writeLong(((Date) obValue).getTime());
			break;
		case TYPE_TIME:
			dos.writeLong(((Time) obValue).getTime());
			break;
		case TYPE_DATE:
			dos.writeLong(((java.sql.Date) obValue).getTime());
			break;
		case TYPE_TIME_STAMP:
			dos.writeLong(((Timestamp) obValue).getTime());
			break;
		case TYPE_BOOLEAN:
			dos.writeBoolean(((Boolean) obValue).booleanValue());
			break;
		case TYPE_STRING:
			dos.writeUTF(obValue.toString());
			break;
		case TYPE_LONG_STRING:
			writeUTF(dos, obValue.toString());
			break;
		case TYPE_BYTES:
			byte[] bytes = (byte[]) obValue;
			int length = bytes.length;
			writeInt(dos, length);
			if (length > 0)
				dos.write(bytes);
			break;
		case TYPE_LIST:
			writeList(dos, (List) obValue);
			break;
		case TYPE_MAP:
			writeMap(dos, (Map) obValue);
			break;
		case TYPE_SERIALIZABLE:
			bytes = null;
			try {
				ByteArrayOutputStream buff = new ByteArrayOutputStream();
				ObjectOutputStream oo = new ObjectOutputStream(buff);
				oo.writeObject(obValue);
				oo.close();
				bytes = buff.toByteArray();
			} catch (Exception ex) {
			}
			if (bytes == null || bytes.length == 0) {
				writeInt(dos, 0);
			} else {
				writeInt(dos, bytes.length);
				dos.write(bytes);
			}
			break;
		case TYPE_JSObject:
			if (obValue instanceof IdScriptableObject) {
				IdScriptableObject jsObject = ((IdScriptableObject) obValue);
				if (jsObject.getClassName().equals("Date")) {
					Date date = (Date) JavascriptEvalUtil.convertJavascriptValue(obValue);
					writeObject(dos, date);
				} else {
					// other data types are not supported yet.
					writeObject(dos, null);
					logger.log(Level.WARNING, CoreMessages.getString(ResourceConstants.UNSUPPORTED_DATA_TYPE));
				}
			} else if (obValue instanceof NativeJavaObject) {
				obValue = JavascriptEvalUtil.convertJavascriptValue(obValue);
				writeObject(dos, obValue);
			} else {
				// other data types are not supported yet.
				writeObject(dos, null);
				logger.log(Level.WARNING, CoreMessages.getString(ResourceConstants.UNSUPPORTED_DATA_TYPE));
			}
			break;
		default:
			assert false;
		}
	}

	/**
	 * Read a String from an input stream
	 * 
	 * @param inputStream
	 * @return an String
	 * @throws IOException
	 */
	public final static String readString(DataInputStream dis) throws IOException {
		try {
			int type = readInt(dis);
			if (type == TYPE_NULL) {
				return null;
			} else if (type == TYPE_STRING) {
				return dis.readUTF();
			} else if (type == TYPE_LONG_STRING) {
				return readUTF(dis);
			}
			throw new EOFException();
		} catch (OutOfMemoryError e) {
			IOException ie = new IOException(e.getMessage());
			ie.initCause(e);
			throw ie;
		} catch (NegativeArraySizeException e) {
			IOException ie = new IOException(e.getMessage());
			ie.initCause(e);
			throw ie;
		}
	}

	/**
	 * Write a String value to an output stream
	 * 
	 * @param outputStream
	 * @param str
	 * @throws IOException
	 */
	public final static void writeString(DataOutputStream dos, String str) throws IOException {
		if (str == null) {
			writeInt(dos, TYPE_NULL);
			return;
		} else {
			if (isLongString(str)) {
				writeInt(dos, TYPE_LONG_STRING);
				writeUTF(dos, str);
			} else {
				writeInt(dos, TYPE_STRING);
				dos.writeUTF(str);
			}
		}
	}

	/**
	 * Read a list from an input stream
	 * 
	 * @param dos
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static byte[] readBytes(DataInputStream dis) throws IOException {
		// check null
		if (readInt(dis) == TYPE_NULL)
			return null;

		// read bytes size
		int size = readInt(dis);
		byte[] bytes = new byte[size];
		if (size != 0)
			dis.readFully(bytes);

		return bytes;
	}

	/**
	 * Write a bytes to an output stream
	 * 
	 * @param dos
	 * @param dataMap
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static void writeBytes(DataOutputStream dos, byte[] bytes) throws IOException {
		// check null
		if (bytes == null) {
			writeInt(dos, TYPE_NULL);
			return;
		} else {
			writeInt(dos, TYPE_BYTES);
		}

		// write byte size and its content
		int size = bytes.length;
		writeInt(dos, size);
		if (size == 0)
			return;
		dos.write(bytes);
	}

	/**
	 * Read a list from an input stream
	 * 
	 * @param dos
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static List readList(DataInputStream dis) throws IOException {
		return readList(dis, null);
	}

	/**
	 * Read a list from an input stream
	 * 
	 * @param dos
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static List readList(DataInputStream dis, ClassLoader classLoader) throws IOException {
		// check null
		if (readInt(dis) == TYPE_NULL)
			return null;

		// read map size
		List dataList = new ArrayList();
		int size = readInt(dis);
		if (size == 0)
			return dataList;

		// write real data
		for (int i = 0; i < size; i++)
			dataList.add(readObject(dis, classLoader));

		return dataList;
	}

	public final static List readIntList(DataInputStream dis) throws IOException {
		// read map size
		List dataList = new ArrayList();
		int size = readInt(dis);
		if (size == 0)
			return dataList;

		// write real data
		for (int i = 0; i < size; i++)
			dataList.add(readInt(dis));

		return dataList;
	}

	/**
	 * Write a list to an output stream
	 * 
	 * @param dos
	 * @param dataMap
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static void writeList(DataOutputStream dos, List list) throws IOException {
		if (list == null) {
			writeInt(dos, TYPE_NULL);
			return;
		} else {
			writeInt(dos, TYPE_LIST);
		}

		// write map size
		int size = list.size();
		writeInt(dos, size);
		if (size == 0)
			return;

		// write real data
		for (int i = 0; i < size; i++)
			writeObject(dos, list.get(i));
	}

	public final static void writeIntList(DataOutputStream dos, List list) throws IOException {
		if (list == null || list.size() == 0) {
			writeInt(dos, 0);
			return;
		}

		// write map size
		int size = list.size();
		writeInt(dos, size);

		// write real data
		for (int i = 0; i < size; i++)
			writeInt(dos, (Integer) list.get(i));
	}

	/**
	 * Read a Map from an input stream
	 * 
	 * @param dos
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static Map readMap(DataInputStream dis) throws IOException {
		return readMap(dis, null);
	}

	/**
	 * Read a Map from an input stream
	 * 
	 * @param dos
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static Map readMap(DataInputStream dis, ClassLoader loader) throws IOException {
		// check null
		if (readInt(dis) == TYPE_NULL)
			return null;

		// read map size
		Map dataMap = new HashMap();
		int size = readInt(dis);
		if (size == 0)
			return dataMap;

		// write real data
		for (int i = 0; i < size; i++) {
			Object key = readObject(dis, loader);
			Object value = readObject(dis, loader);
			dataMap.put(key, value);
		}

		return dataMap;
	}

	/**
	 * Write a Map to an output stream
	 * 
	 * @param dos
	 * @param map
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static void writeMap(DataOutputStream dos, Map map) throws IOException {
		// check null
		if (map == null) {
			writeInt(dos, TYPE_NULL);
			return;
		} else {
			writeInt(dos, TYPE_MAP);
		}

		// write map size
		int size = map.size();
		writeInt(dos, size);
		if (size == 0)
			return;

		// write real data
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			writeObject(dos, key);
			writeObject(dos, value);
		}
	}

	/**
	 * private utility method to check whether it is a long string
	 * 
	 * @param str
	 * @return true if it is a long string
	 */
	private static boolean isLongString(String str) {
		int strlen = str.length();

		if (strlen > 65535) {
			return true;
		} else if (strlen < 21845) {
			return false;
		}

		int utflen = getBytesSize(str);

		if (utflen > 65535) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * private utility method to write a UTF String to a DataOutputStream
	 * 
	 * @param str
	 * @throws UTFDataFormatException
	 */
	private static void writeUTF(DataOutputStream dos, String str) throws IOException {
		int strlen = str.length();
		int c = 0;
		int utflen = getBytesSize(str);
		dos.writeInt(utflen);

		int i = 0;
		for (; i < strlen; i++) {
			c = str.charAt(i);
			if (!((c >= 0x0001) && (c <= 0x007F)))
				break;
			dos.writeByte((byte) c);
		}

		for (; i < strlen; i++) {
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				dos.writeByte((byte) c);
			} else if (c > 0x07FF) {
				dos.writeByte((byte) (0xE0 | ((c >> 12) & 0x0F)));
				dos.writeByte((byte) (0x80 | ((c >> 6) & 0x3F)));
				dos.writeByte((byte) (0x80 | ((c >> 0) & 0x3F)));
			} else {
				dos.writeByte((byte) (0xC0 | ((c >> 6) & 0x1F)));
				dos.writeByte((byte) (0x80 | ((c >> 0) & 0x3F)));
			}
		}
	}

	/**
	 * private utility method to read a UTF String
	 * 
	 * @param str
	 * @throws UTFDataFormatException
	 */
	private static String readUTF(DataInputStream dis) throws IOException {
		try {
			int length = dis.readInt();
			byte[] ret = new byte[length];
			dis.readFully(ret, 0, length);
			return convertBytes2String(ret);
		} catch (OutOfMemoryError e) {
			IOException ie = new IOException(e.getMessage());
			ie.initCause(e);
			throw ie;
		} catch (NegativeArraySizeException e) {
			IOException ie = new IOException(e.getMessage());
			ie.initCause(e);
			throw ie;
		}
	}

	/**
	 * private utility method to the size of a string in bytes
	 * 
	 * @param str
	 * @throws UTFDataFormatException
	 */
	private static int getBytesSize(String str) {
		int c, utflen = 0;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				utflen++;
			} else if (c > 0x07FF) {
				utflen += 3;
			} else {
				utflen += 2;
			}
		}
		return utflen;
	}

	/**
	 * private utility method helping to convert byte[] to a String
	 * 
	 * @param str
	 * @throws UTFDataFormatException
	 */
	private static int generateCharArray(char[] chararr, byte[] bytearr, int count, int chararr_count)
			throws UTFDataFormatException {
		int c, char2, char3;
		int utflen = bytearr.length;
		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			switch (c >> 4) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				// 0xxxxxxx
				count++;
				chararr[chararr_count++] = (char) c;
				break;
			case 12:
			case 13:
				// 110x xxxx 10xx xxxx
				count += 2;
				if (count > utflen)
					throw new UTFDataFormatException(CoreMessages.getString(ResourceConstants.MALFORMED_INPUT_ERROR));
				char2 = (int) bytearr[count - 1];
				if ((char2 & 0xC0) != 0x80)
					throw new UTFDataFormatException(CoreMessages
							.getFormattedString(ResourceConstants.MALFORMED_INPUT_AROUND_BYTE, new Object[] { count }));
				chararr[chararr_count++] = (char) (((c & 0x1F) << 6) | (char2 & 0x3F));
				break;
			case 14:
				// 1110 xxxx 10xx xxxx 10xx xxxx
				count += 3;
				if (count > utflen)
					throw new UTFDataFormatException(CoreMessages.getString(ResourceConstants.MALFORMED_INPUT_ERROR));
				char2 = (int) bytearr[count - 2];
				char3 = (int) bytearr[count - 1];
				if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
					throw new UTFDataFormatException(CoreMessages.getFormattedString(
							ResourceConstants.MALFORMED_INPUT_AROUND_BYTE, new Object[] { count - 1 }));
				chararr[chararr_count++] = (char) (((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
				break;
			default:
				// 10xx xxxx, 1111 xxxx
				throw new UTFDataFormatException(CoreMessages
						.getFormattedString(ResourceConstants.MALFORMED_INPUT_AROUND_BYTE, new Object[] { count }));
			}
		}
		return chararr_count;
	}

	/**
	 * private utility method to convert a byte[] to String
	 * 
	 * @param bytearre
	 * @throws UTFDataFormatException
	 */
	private static String convertBytes2String(byte[] bytearr) throws UTFDataFormatException {
		int utflen = bytearr.length;
		char[] chararr = new char[utflen];
		int c;
		int chararr_count = 0;

		int count = 0;
		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			if (c > 127)
				break;
			count++;
			chararr[chararr_count++] = (char) c;
		}
		chararr_count = generateCharArray(chararr, bytearr, count, chararr_count);

		// The number of chars produced may be less than utflen
		return new String(chararr, 0, chararr_count);
	}
}
