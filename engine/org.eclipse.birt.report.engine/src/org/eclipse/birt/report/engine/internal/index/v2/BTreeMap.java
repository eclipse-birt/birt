/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.index.v2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.btree.BTree;
import org.eclipse.birt.core.btree.BTreeFile;
import org.eclipse.birt.core.btree.BTreeOption;
import org.eclipse.birt.core.btree.BTreeSerializer;
import org.eclipse.birt.report.engine.content.impl.BookmarkContent;

class BTreeMap extends BTree<String, Object> {

	static final int LONG_VALUE = 1;
	static final int BOOKMARK_VALUE = 2;

	int indexVersion;
	int indexType;

	static public BTreeMap openTreeMap(IDocArchiveReader archive, String name, int valueType) throws IOException {
		BTreeOption<String, Object> option = new BTreeOption<>();
		option.setReadOnly(true);
		option.setKeySerializer(new StringSerializer());
		option.setHasValue(true);
		option.setAllowDuplicate(false);
		option.setValueSerializer(new ObjectSerializer(valueType));
		option.setFile(new ArchiveInputFile(archive, name));
		return new BTreeMap(option, valueType);
	}

	static public BTreeMap createTreeMap(IDocArchiveWriter archive, String name, int valueType) throws IOException {
		BTreeOption<String, Object> option = new BTreeOption<>();
		option.setKeySerializer(new StringSerializer());
		option.setHasValue(true);
		option.setAllowDuplicate(false);
		option.setValueSerializer(new ObjectSerializer(valueType));
		option.setFile(new ArchiveOutputFile(archive, name));
		return new BTreeMap(option, valueType);
	}

	private BTreeMap(BTreeOption<String, Object> option, int valueType) throws IOException {
		super(option);

		indexVersion = IndexConstants.VERSION_0;

		if (valueType == BTreeMap.BOOKMARK_VALUE) {
			indexVersion = IndexConstants.VERSION_1;
		}
		indexType = IndexConstants.BTREE_MAP;
	}

	@Override
	protected void readTreeHead(DataInput in) throws IOException {
		indexVersion = in.readInt();
		indexType = in.readInt();
		super.readTreeHead(in);
	}

	@Override
	protected void writeTreeHead(DataOutput out) throws IOException {
		out.writeInt(indexVersion);
		out.writeInt(indexType);
		super.writeTreeHead(out);
	}

	@Override
	public void close() throws IOException {
		super.close();
	}

	static private class StringSerializer implements BTreeSerializer<String> {

		@Override
		public byte[] getBytes(String object) throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			DataOutput oo = new DataOutputStream(out);
			oo.writeUTF(object);
			return out.toByteArray();
		}

		@Override
		public String getObject(byte[] bytes) throws IOException, ClassNotFoundException {
			DataInput input = new DataInputStream(new ByteArrayInputStream(bytes));
			return input.readUTF();
		}
	}

	static private class ObjectSerializer implements BTreeSerializer<Object> {

		int valueType;

		ObjectSerializer(int type) {
			valueType = type;
		}

		@Override
		public byte[] getBytes(Object object) throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			DataOutput oo = new DataOutputStream(out);
			if (valueType == LONG_VALUE) {
				oo.writeLong(((Long) object).longValue());
			} else if (valueType == BOOKMARK_VALUE) {
				((BookmarkContent) object).writeStream(oo);
			}
			return out.toByteArray();
		}

		@Override
		public Object getObject(byte[] bytes) throws IOException, ClassNotFoundException {
			DataInput input = new DataInputStream(new ByteArrayInputStream(bytes));
			if (valueType == LONG_VALUE) {
				return Long.valueOf(input.readLong());
			} else if (valueType == BOOKMARK_VALUE) {
				BookmarkContent content = new BookmarkContent();
				content.readStream(input);
				return content;
			}
			return null;
		}
	}

	static private class ArchiveInputFile implements BTreeFile {

		IDocArchiveReader archive;
		String name;
		RAInputStream input;

		ArchiveInputFile(IDocArchiveReader archive, String name) throws IOException {
			this.archive = archive;
			this.name = name;
			this.input = archive.getInputStream(name);
		}

		@Override
		public int allocBlock() throws IOException {
			throw new IOException("read only stream");
		}

		@Override
		public int getTotalBlock() throws IOException {
			return (int) ((input.length() + BLOCK_SIZE - 1) / BLOCK_SIZE);
		}

		@Override
		public Object lock() throws IOException {
			return archive.lock(name);
		}

		@Override
		public void readBlock(int blockId, byte[] bytes) throws IOException {
			input.seek((long) blockId * BLOCK_SIZE);
			input.read(bytes);
		}

		@Override
		public void unlock(Object lock) throws IOException {
			archive.unlock(lock);
		}

		@Override
		public void writeBlock(int blockId, byte[] bytes) throws IOException {
			throw new IOException("read only stream");
		}

		@Override
		public void close() throws IOException {
			input.close();
		}
	}

	static private class ArchiveOutputFile implements BTreeFile {

		IDocArchiveWriter archive;
		String name;
		RAOutputStream output;
		RAInputStream input;
		int totalBlock;

		ArchiveOutputFile(IDocArchiveWriter archive, String name) throws IOException {
			this.archive = archive;
			this.name = name;
			output = archive.createOutputStream(name);
			input = archive.getInputStream(name);
			totalBlock = 0;
		}

		@Override
		public void close() throws IOException {
			if (output != null) {
				output.close();
			}
			if (input != null) {
				input.close();
			}
		}

		@Override
		public int allocBlock() throws IOException {
			return totalBlock++;
		}

		@Override
		public int getTotalBlock() throws IOException {
			return totalBlock;
		}

		@Override
		public Object lock() throws IOException {
			return archive.lock(name);
		}

		@Override
		public void readBlock(int blockId, byte[] bytes) throws IOException {
			input.refresh();
			input.seek((long) blockId * BLOCK_SIZE);
			input.read(bytes);
		}

		@Override
		public void unlock(Object lock) throws IOException {
			archive.unlock(lock);
		}

		@Override
		public void writeBlock(int blockId, byte[] bytes) throws IOException {
			if (blockId >= totalBlock) {
				totalBlock = blockId + 1;
			}
			output.seek((long) blockId * BLOCK_SIZE);
			output.write(bytes);
			output.flush();
		}
	}

}
