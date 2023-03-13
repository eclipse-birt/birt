/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl.index;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;

public class SerializableBirtHash extends HashMap implements IIndexSerializer {

	private static final long serialVersionUID = 1L;
	public static int NULL_VALUE_OFFSET = -2;
	public static int NOT_HASH_VALUE_OFFSET = -3;

	private boolean closed = false;
	private HashSet valueSet = new HashSet();
	private StreamManager manager;
	private String indexName;
	private String valueName;

	public SerializableBirtHash(String indexName, String valueName, StreamManager manager) {
		super();
		this.indexName = indexName;
		this.valueName = valueName;
		this.manager = manager;
	}

	@Override
	public Object put(Object key, Object value) {
		if (key == null) {
			this.valueSet.add(null);
		} else {
			int hash = key.hashCode();
			if (this.valueSet.contains(hash)) {
				this.valueSet.add(key);
			} else {
				this.valueSet.add(key.hashCode());
			}
		}
		return super.put(key, value);
	}

	public Object getKeyValue(Object key) {
		if (key == null) {
			return null;
		}
		if (this.valueSet.contains(key)) {
			return key;
		}
		return key.hashCode();
	}

	@Override
	public void close() throws DataException {
		if (closed) {
			return;
		}
		this.closed = true;

		this.doSave();

	}

	private void doSave() throws DataException {
		try {
			if (this.keySet().size() == 0) {
				return;
			}
			RAOutputStream indexStream = this.manager.getOutStream(indexName);
			RAOutputStream valueStream = this.manager.getOutStream(valueName);
			DataOutputStream dis = new DataOutputStream(indexStream);
			DataOutputStream dvs = new DataOutputStream(valueStream);
			IOUtil.writeInt(dis, this.keySet().size());
			Iterator entryIterator = this.entrySet().iterator();
			while (entryIterator.hasNext()) {
				Map.Entry entry = (Map.Entry) entryIterator.next();
				// For null value, we do not write the value to value stream
				if (entry.getKey() == null) {
					IOUtil.writeLong(dis, NULL_VALUE_OFFSET);
					IOUtil.writeIntList(dis, (List) entry.getValue());
					continue;
				}
				int hash = entry.getKey() == null ? 0 : entry.getKey().hashCode();
				if (!this.valueSet.contains(entry.getKey())) {
					IOUtil.writeLong(dis, valueStream.getOffset());
					IOUtil.writeInt(dis, hash);
					IOUtil.writeIntList(dis, (List) entry.getValue());
					IOUtil.writeString(dvs, entry.getKey().toString());
				} else {
					IOUtil.writeLong(dis, NOT_HASH_VALUE_OFFSET);
					IOUtil.writeString(dis, entry.getKey().toString());
					IOUtil.writeIntList(dis, (List) entry.getValue());
				}
			}
			indexStream.close();
			valueStream.close();
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}
}
