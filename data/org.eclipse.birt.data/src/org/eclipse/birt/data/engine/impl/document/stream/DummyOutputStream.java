
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
package org.eclipse.birt.data.engine.impl.document.stream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * This DummyOutputStream is used to cache user output to Memory.
 *
 * @deprecated
 */
@Deprecated
public class DummyOutputStream extends OutputStream {
	private static final int BUFF_SIZE = 4000;

	private List cachedByteArray;
	private int currentListIndex;
	private int nextArrayIndex;
	private byte[] currentArray;
	private DataEngineContext context;
	private StreamID id;
	private int type;
	private boolean isClosed;

	DummyOutputStream(DataEngineContext context, StreamID id, int type) {
		this.cachedByteArray = new ArrayList();
		this.currentArray = new byte[BUFF_SIZE];
		this.cachedByteArray.add(this.currentArray);
		this.nextArrayIndex = 0;
		this.currentListIndex = 0;
		this.context = context;
		this.id = id;
		this.type = type;
		this.isClosed = false;
	}

	@Override
	public void write(int b) throws IOException {
		if (this.nextArrayIndex < BUFF_SIZE) {
			this.currentArray[this.nextArrayIndex] = (byte) b;
			this.nextArrayIndex++;
		} else {
			this.currentArray = new byte[BUFF_SIZE];
			this.cachedByteArray.add(this.currentArray);
			this.currentListIndex++;
			this.nextArrayIndex = 0;
			this.write(b);
		}
	}

	/**
	 *
	 * @return
	 */
	public byte[] toByteArray() {
		byte[] result = new byte[this.currentListIndex * BUFF_SIZE + this.nextArrayIndex];
		for (int i = 0; i < this.cachedByteArray.size(); i++) {
			byte[] temp = (byte[]) this.cachedByteArray.get(i);
			int count = BUFF_SIZE;
			if (i == this.cachedByteArray.size() - 1) {
				count = this.nextArrayIndex;
			}
			for (int j = 0; j < count; j++) {
				result[i * BUFF_SIZE + j] = temp[j];
			}
		}
		return result;
	}

	public long getOffset() {
		return BUFF_SIZE * (cachedByteArray.size() - 1) + this.nextArrayIndex - 1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		if (this.isClosed) {
			return;
		} else {
			this.isClosed = true;
		}
		try {
			if (this.type == DataEngineContext.DATASET_DATA_STREAM
					|| this.type == DataEngineContext.DATASET_META_STREAM) {
				saveToData();
			} else {
				saveToMeta();
			}

		} catch (Exception e) {
			throw new IOException(e.getLocalizedMessage());
		}

	}

	/**
	 *
	 * @throws DataException
	 * @throws IOException
	 */
	private void saveToData() throws DataException, IOException {

		RAOutputStream data = context.getOutputStream(id.getStartStream(), id.getSubQueryStream(),
				DataEngineContext.DATASET_DATA_STREAM);
		data.seek(data.length());
		DataOutputStream dos = new DataOutputStream(data);
		byte[] temp = this.toByteArray();
		dos.writeInt(type);
		dos.writeInt(temp.length);
		dos.write(temp);
		dos.close();
		data.close();

	}

	/**
	 *
	 * @throws DataException
	 * @throws IOException
	 */
	private void saveToMeta() throws DataException, IOException {
		RAOutputStream raMetaOs = context.getOutputStream(id.getStartStream(), id.getSubQueryStream(),
				DataEngineContext.META_STREAM);
		DataOutputStream metaOs = new DataOutputStream(raMetaOs);

		if (context.hasOutStream(id.getStartStream(), id.getSubQueryStream(), DataEngineContext.META_STREAM)) {
			raMetaOs.seek(raMetaOs.length());
		}

		RAOutputStream raMetaIndexOs = context.getOutputStream(id.getStartStream(), id.getSubQueryStream(),
				DataEngineContext.META_INDEX_STREAM);

		DataOutputStream metaIndexOs = new DataOutputStream(raMetaIndexOs);
		if (context.hasOutStream(id.getStartStream(), id.getSubQueryStream(), DataEngineContext.META_INDEX_STREAM)) {
			raMetaIndexOs.seek(raMetaIndexOs.length());
		}

		long offset = raMetaOs.length();

		Integer streamType = this.type;

		byte[] temp = this.toByteArray();
		int size = temp.length;

		IOUtil.writeInt(metaIndexOs, streamType.intValue());
		IOUtil.writeLong(metaIndexOs, offset);
		IOUtil.writeInt(metaIndexOs, size);
		metaOs.write(temp);

		metaOs.close();
		metaIndexOs.close();
	}
}
