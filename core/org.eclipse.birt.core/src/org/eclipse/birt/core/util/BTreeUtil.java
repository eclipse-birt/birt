/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.archive.compound.ArchiveEntry;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.core.btree.BTree;
import org.eclipse.birt.core.btree.BTreeFile;
import org.eclipse.birt.core.btree.BTreeOption;
import org.eclipse.birt.core.btree.BTreeSerializer;
import org.eclipse.birt.core.data.DataType;

public class BTreeUtil {

	static private void checkDataType(int dataType) throws IOException {
		if (dataType != DataType.INTEGER_TYPE || dataType != DataType.DOUBLE_TYPE || dataType != DataType.DATE_TYPE
				|| dataType != DataType.SQL_DATE_TYPE || dataType != DataType.SQL_TIME_TYPE
				|| dataType != DataType.DECIMAL_TYPE || dataType != DataType.STRING_TYPE)
			throw new IOException("unsupported datatype:" + dataType);
	}

	static public BTree<Object, Integer> openBTree(IArchiveFile archive, String entryName, int dataType)
			throws IOException {
		checkDataType(dataType);

		BTreeOption<Object, Integer> btreeOption = new BTreeOption<Object, Integer>();
		setupBTreeOption(btreeOption, dataType);
		btreeOption.setFile(new ArchiveTreeFile(archive, entryName));
		return new BTree<Object, Integer>(btreeOption);
	}

	static public BTree<Object, Integer> createBTree(IArchiveFile archive, String entryName, int dataType)
			throws IOException {
		checkDataType(dataType);

		if (archive.exists(entryName)) {
			archive.removeEntry(entryName);
		}

		BTreeOption<Object, Integer> btreeOption = new BTreeOption<Object, Integer>();
		setupBTreeOption(btreeOption, dataType);
		btreeOption.setFile(new ArchiveTreeFile(archive, entryName));
		return new BTree<Object, Integer>(btreeOption);
	}

	static public BTree<Object, Integer> openBTree(IDocArchiveReader reader, String entryName, int dataType)
			throws IOException {
		checkDataType(dataType);

		BTreeOption<Object, Integer> btreeOption = new BTreeOption<Object, Integer>();
		setupBTreeOption(btreeOption, dataType);
		btreeOption.setFile(new ReaderTreeFile(reader, entryName));
		return new BTree<Object, Integer>(btreeOption);
	}

	static public BTree<Object, Integer> openBTree(IDocArchiveWriter writer, String entryName, int dataType)
			throws IOException {
		checkDataType(dataType);

		BTreeOption<Object, Integer> btreeOption = new BTreeOption<Object, Integer>();
		setupBTreeOption(btreeOption, dataType);
		btreeOption.setFile(new WriterTreeFile(writer, entryName));
		return new BTree<Object, Integer>(btreeOption);
	}

	static public BTree<Object, Integer> createBTree(IDocArchiveWriter writer, String entryName, int dataType)
			throws IOException {
		checkDataType(dataType);

		if (writer.exists(entryName)) {
			writer.dropStream(entryName);
		}

		BTreeOption<Object, Integer> btreeOption = new BTreeOption<Object, Integer>();
		setupBTreeOption(btreeOption, dataType);
		btreeOption.setFile(new WriterTreeFile(writer, entryName));
		return new BTree<Object, Integer>(btreeOption);
	}

	private static class ArchiveTreeFile implements BTreeFile {

		protected IArchiveFile af;
		protected String name;
		protected ArchiveEntry rf;
		protected int totalBlock;

		ArchiveTreeFile(IArchiveFile archive, String entryName) throws IOException {
			this.af = archive;
			this.name = entryName;
			if (archive.exists(entryName)) {
				rf = archive.openEntry(entryName);
			} else {
				rf = archive.createEntry(entryName);
			}
			totalBlock = (int) ((rf.getLength() + BLOCK_SIZE - 1) / BLOCK_SIZE);
		}

		public void close() throws IOException {
			rf.close();
		}

		public int allocBlock() throws IOException {
			return totalBlock++;
		}

		public int getTotalBlock() throws IOException {
			return totalBlock;
		}

		public Object lock() throws IOException {
			return af.lockEntry(name);
		}

		public void readBlock(int block, byte[] bytes) throws IOException {
			rf.read((long) block * BLOCK_SIZE, bytes, 0, bytes.length);
		}

		public void unlock(Object lock) throws IOException {
			af.unlockEntry(lock);
		}

		public void writeBlock(int block, byte[] bytes) throws IOException {
			if (block >= totalBlock) {
				totalBlock = block + 1;
			}
			rf.write((long) block * BLOCK_SIZE, bytes, 0, bytes.length);
		}
	}

	static public class ReaderTreeFile implements BTreeFile {

		private IDocArchiveReader archive;
		private String name;
		private RAInputStream input;
		private int totalBlock;

		ReaderTreeFile(IDocArchiveReader archive, String name) throws IOException {
			this.archive = archive;
			this.name = name;
			this.input = archive.getInputStream(name);
			this.totalBlock = (int) ((input.length() + BLOCK_SIZE - 1) / BLOCK_SIZE);
		}

		public int allocBlock() throws IOException {
			throw new IOException("read only stream");
		}

		public int getTotalBlock() throws IOException {
			return totalBlock;
		}

		public Object lock() throws IOException {
			return archive.lock(name);
		}

		public void readBlock(int blockId, byte[] bytes) throws IOException {
			input.refresh();
			input.seek((long) blockId * BLOCK_SIZE);
			input.read(bytes);
		}

		public void unlock(Object lock) throws IOException {
			archive.unlock(lock);
		}

		public void writeBlock(int blockId, byte[] bytes) throws IOException {
			throw new IOException("read only stream");
		}

		public void close() throws IOException {
			input.close();
		}
	}

	static private class WriterTreeFile implements BTreeFile {

		private IDocArchiveWriter archive;
		private String name;
		private RAOutputStream output;
		private RAInputStream input;
		private int totalBlock;

		WriterTreeFile(IDocArchiveWriter archive, String name) throws IOException {
			this.archive = archive;
			this.name = name;
			if (archive.exists(name)) {
				output = archive.getOutputStream(name);
				input = archive.getInputStream(name);
			} else {
				output = archive.createOutputStream(name);
				input = archive.getInputStream(name);
			}
			totalBlock = (int) ((output.length() + BLOCK_SIZE - 1) / BLOCK_SIZE);
		}

		public void close() throws IOException {
			if (output != null) {
				output.close();
			}
			if (input != null) {
				input.close();
			}
		}

		public int allocBlock() throws IOException {
			return totalBlock++;
		}

		public int getTotalBlock() throws IOException {
			return totalBlock;
		}

		public Object lock() throws IOException {
			return archive.lock(name);
		}

		public void readBlock(int blockId, byte[] bytes) throws IOException {
			input.refresh();
			input.seek((long) blockId * BLOCK_SIZE);
			input.read(bytes);
		}

		public void unlock(Object lock) throws IOException {
			archive.unlock(lock);
		}

		public void writeBlock(int blockId, byte[] bytes) throws IOException {
			if (blockId >= totalBlock) {
				totalBlock = blockId + 1;
			}
			output.seek((long) blockId * BLOCK_SIZE);
			output.write(bytes);
			output.flush();
		}
	}

	static void setupBTreeOption(BTreeOption<Object, Integer> option, int type) {
		option.setValueSize(4);
		option.setValueSerializer(new IntSerializer());
		option.setAllowDuplicate(true);
		int keySize = getKeySize(type);
		if (keySize != -1) {
			option.setKeySize(keySize);
		}
		option.setKeySerializer(new KeySerializer(type));
		option.setComparator(new KeyComparator());
	}

	private static class IntSerializer implements BTreeSerializer<Integer> {

		public byte[] getBytes(Integer value) throws IOException {
			byte[] bytes = new byte[4];
			IOUtil.integerToBytes(value, bytes);
			return bytes;
		}

		public Integer getObject(byte[] bytes) throws IOException, ClassNotFoundException {
			return IOUtil.bytesToInteger(bytes);
		}
	}

	static int getKeySize(int dataType) {
		switch (dataType) {
		case DataType.INTEGER_TYPE:
			return 4;
		case DataType.DOUBLE_TYPE:
		case DataType.DATE_TYPE:
		case DataType.SQL_DATE_TYPE:
		case DataType.SQL_TIME_TYPE:
			return 8;
		case DataType.DECIMAL_TYPE:
		case DataType.STRING_TYPE:
			return -1;
		}
		return -1;
	}

	private static class KeySerializer implements BTreeSerializer<Object> {

		static final String UTF_8 = "utf-8";
		int dataType;

		KeySerializer(int type) {
			this.dataType = type;
		}

		public byte[] getBytes(Object value) throws IOException {
			byte[] bytes;
			switch (dataType) {
			case DataType.INTEGER_TYPE:
				bytes = new byte[4];
				IOUtil.integerToBytes(((Integer) value), bytes);
				return bytes;
			case DataType.DOUBLE_TYPE:
				bytes = new byte[8];
				long v = Double.doubleToLongBits((Double) value);
				IOUtil.longToBytes(v, bytes);
				return bytes;
			case DataType.DATE_TYPE:
			case DataType.SQL_DATE_TYPE:
			case DataType.SQL_TIME_TYPE:
				bytes = new byte[8];
				long time = ((java.util.Date) value).getTime();
				IOUtil.longToBytes(time, bytes);
				return bytes;
			case DataType.DECIMAL_TYPE:
				String dec = ((BigDecimal) value).toString();
				return dec.getBytes(UTF_8);
			case DataType.STRING_TYPE:
				return ((String) value).getBytes(UTF_8);
			}
			throw new IOException("unsupported data type");
		}

		public Object getObject(byte[] bytes) throws IOException, ClassNotFoundException {
			switch (dataType) {
			case DataType.INTEGER_TYPE:
				return IOUtil.bytesToInteger(bytes);
			case DataType.DOUBLE_TYPE:
				return Double.longBitsToDouble(IOUtil.bytesToLong(bytes));
			case DataType.DATE_TYPE:
				return new java.sql.Timestamp(IOUtil.bytesToLong(bytes));
			case DataType.SQL_DATE_TYPE:
				return new java.sql.Date(IOUtil.bytesToLong(bytes));
			case DataType.SQL_TIME_TYPE:
				return new java.sql.Time(IOUtil.bytesToLong(bytes));
			case DataType.DECIMAL_TYPE:
				return new BigDecimal(new String(bytes, UTF_8));
			case DataType.STRING_TYPE:
				return new String(bytes, UTF_8);
			}
			throw new IOException("unsupported data type");
		}
	}

	private static class KeyComparator implements Comparator<Object>, Serializable {
		private static final long serialVersionUID = 486084009828701292L;

		public int compare(Object v1, Object v2) {
			if (v1 == v2) {
				return 0;
			}
			if (v1 == null) {
				return -1;
			}
			if (v2 == null) {
				return 1;
			}
			return ((Comparable) v1).compareTo(v2);
		}
	}

}
