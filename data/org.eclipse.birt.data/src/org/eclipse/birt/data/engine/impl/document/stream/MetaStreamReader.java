
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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 *
 */

public class MetaStreamReader extends StreamReader {
	private static Logger logger = Logger.getLogger(MetaStreamReader.class.getName());

	/**
	 *
	 * @param context
	 * @param id
	 * @throws DataException
	 */
	public MetaStreamReader(DataEngineContext context, StreamID id) throws DataException {
		try {
			this.streamMap = new HashMap();
			this.id = id;
			this.context = context;
			RAInputStream is = context.getInputStream(id.getStartStream(), id.getSubQueryStream(),
					DataEngineContext.META_INDEX_STREAM);

			DataInputStream metaIndexStream = new DataInputStream(is);

			while (is.getOffset() != is.length()) {
				int type = IOUtil.readInt(metaIndexStream);
				long offset = IOUtil.readLong(metaIndexStream);
				int size = IOUtil.readInt(metaIndexStream);

				this.streamMap.put(Integer.valueOf(type), new OffsetInfo(offset, size));
			}

			metaIndexStream.close();
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage());
		}
	}

	/**
	 *
	 * @param streamType
	 * @return
	 * @throws DataException
	 */
	@Override
	public RAInputStream getRAInputStream(int streamType) throws DataException {
		Object temp = this.streamMap.get(Integer.valueOf(streamType));
		if (temp == null) {
			throw new DataException(ResourceConstants.DOCUMENT_ERROR_CANNOT_LOAD_STREAM,
					DataEngineContext.getPath(id.getStartStream(), id.getSubQueryStream(), streamType));
		} else {
			try {

				OffsetInfo oi = (OffsetInfo) temp;
				long offset = oi.offset;
				int size = oi.size;
				RAInputStream metaStream = new WrapperedRAInputStream((RAInputStream) context
						.getInputStream(id.getStartStream(), id.getSubQueryStream(), getCollectionStreamType()), offset,
						size);
				return metaStream;
			} catch (Exception e) {
				String log = "Meta Info:\n";
				for (Object o : this.streamMap.keySet()) {
					log += (o + ":" + this.streamMap.get(o) + "\n");
				}

				log += " Error while load (" + streamType + "):" + temp;
				logger.warning(log);

				throw new DataException(e.getLocalizedMessage(), e);
			}
		}

	}

	/**
	 *
	 */
	protected int getCollectionStreamType() {
		return DataEngineContext.META_STREAM;
	}

	private static class OffsetInfo {
		private long offset;
		private int size;

		public OffsetInfo(long offset, int size) {
			this.offset = offset;
			this.size = size;
		}

		@Override
		public String toString() {
			return "[" + this.offset + "," + this.size + "]";
		}
	}
}
