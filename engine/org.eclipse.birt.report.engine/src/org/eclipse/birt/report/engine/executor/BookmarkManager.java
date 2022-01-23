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
package org.eclipse.birt.report.engine.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.eclipse.birt.core.btree.BTree;
import org.eclipse.birt.core.btree.BTreeOption;
import org.eclipse.birt.core.btree.BTreeSerializer;
import org.eclipse.birt.core.btree.BTreeUtils;
import org.eclipse.birt.core.btree.FileBTreeFile;
import org.eclipse.birt.report.engine.api.EngineException;

public class BookmarkManager {

	static final Integer VALUE = new Integer(0);

	int sequenceID = 0;
	BookmarkHashSet hashset;
	ExecutionContext context;

	public BookmarkManager(ExecutionContext context, int size) {
		this.context = context;
		hashset = new BookmarkHashSet(context, size);
	}

	public void close() {
		hashset.close();
	}

	public boolean exist(String bookmark) {
		try {
			return hashset.exist(bookmark);
		} catch (IOException ex) {
			context.addException(new EngineException(ex.getMessage(), ex));
		}
		return false;

	}

	public void addBookmark(String bookmark) {
		try {
			hashset.addBookmark(bookmark);
		} catch (IOException ex) {
			context.addException(new EngineException(ex.getMessage(), ex));
		}
	}

	public String createBookmark(String bookmark) {
		return "_recreated__bookmark__" + (++sequenceID);
	}

	static private class StringSerializer implements BTreeSerializer<String> {

		public byte[] getBytes(String object) throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			DataOutput oo = new DataOutputStream(out);
			oo.writeUTF(object);
			return out.toByteArray();
		}

		public String getObject(byte[] bytes) throws IOException, ClassNotFoundException {
			DataInput input = new DataInputStream(new ByteArrayInputStream(bytes));
			return input.readUTF();
		}
	}

	static private class IntegerSerializer implements BTreeSerializer<Integer> {

		public byte[] getBytes(Integer object) throws IOException {
			byte[] bytes = new byte[4];
			BTreeUtils.integerToBytes(object.intValue(), bytes);
			return bytes;
		}

		public Integer getObject(byte[] bytes) throws IOException, ClassNotFoundException {
			return new Integer(BTreeUtils.bytesToInteger(bytes));
		}
	}

	private class BookmarkHashSet {

		protected HashMap<String, Integer> inlineMap = new HashMap<String, Integer>();
		protected BTree<String, Integer> btree = null;
		protected String fileName;
		protected ExecutionContext context;
		protected int size;

		public BookmarkHashSet(ExecutionContext context, int size) {
			this.context = context;
			this.size = size;
		}

		public boolean exist(String bookmark) throws IOException {
			if (inlineMap != null) {
				return inlineMap.containsKey(bookmark);
			} else {
				if (btree == null) {
					btree = createBtree();
				}
				return btree.exist(bookmark);
			}
		}

		public void addBookmark(String bookmark) throws IOException {
			if (inlineMap != null) {
				if (inlineMap.size() < size) {
					inlineMap.put(bookmark, VALUE);
				} else {
					flush();
					inlineMap = null;
					btree.insert(bookmark, VALUE);
				}
			} else {
				if (btree == null) {
					btree = createBtree();
				}
				btree.insert(bookmark, VALUE);
			}
		}

		protected void flush() throws IOException {
			if (btree == null) {
				btree = createBtree();
			}
			ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(
					inlineMap.entrySet());
			Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {

				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					return o1.getKey().compareTo(o2.getKey());
				}
			});

			for (Map.Entry<String, Integer> entry : entries) {
				btree.insert(entry.getKey(), VALUE);
			}
		}

		protected BTree<String, Integer> createBtree() throws IOException {
			String tmpdir = context.getEngine().getConfig().getTempDir();
			fileName = tmpdir + File.separator + UUID.randomUUID();
			FileBTreeFile file = new FileBTreeFile(fileName);
			BTreeOption<String, Integer> option = new BTreeOption<String, Integer>();
			option.setHasValue(true);
			option.setKeySerializer(new StringSerializer());
			option.setValueSerializer(new IntegerSerializer());
			option.setValueSize(4);
			option.setFile(file);
			return new BTree<String, Integer>(option);
		}

		public void close() {
			inlineMap = null;
			if (btree != null) {
				try {
					btree.close();
					File file = new File(fileName);
					if (file.exists()) {
						file.delete();
					}
				} catch (IOException e) {
					context.addException(new EngineException(e.getMessage(), e));
				}
			}
		}
	}
}
