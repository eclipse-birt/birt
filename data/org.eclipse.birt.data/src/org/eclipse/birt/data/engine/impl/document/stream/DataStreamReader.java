
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

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * 
 */

public class DataStreamReader extends StreamReader {

	/**
	 * 
	 * @param context
	 * @param id
	 * @throws DataException
	 */
	public DataStreamReader(DataEngineContext context, StreamID id) throws DataException {
		try {
			this.streamMap = new HashMap();
			this.id = id;
			this.context = context;
			RAInputStream is = context.getInputStream(id.getStartStream(), id.getSubQueryStream(),
					DataEngineContext.DATASET_DATA_STREAM);

			DataInputStream metaIndexStream = new DataInputStream(is);

			int type = is.readInt();
			int size = is.readInt();
			long offset = is.getOffset();
			this.streamMap.put(Integer.valueOf(type),
					new WrapperedRAInputStream((RAInputStream) context.getInputStream(id.getStartStream(),
							id.getSubQueryStream(), DataEngineContext.DATASET_DATA_STREAM), offset, size));

			metaIndexStream.close();

		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.document.stream.StreamReader#
	 * getRAInputStream(int)
	 */
	public RAInputStream getRAInputStream(int streamType) throws DataException {
		Object temp = this.streamMap.get(Integer.valueOf(streamType));
		if (temp == null) {
			throw new DataException(ResourceConstants.DOCUMENT_ERROR_CANNOT_LOAD_STREAM,
					DataEngineContext.getPath(id.getStartStream(), id.getSubQueryStream(), streamType));
		} else {
			return (RAInputStream) temp;

		}
	}

}
