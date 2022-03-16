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

import java.io.OutputStream;
import java.util.HashMap;

import org.eclipse.birt.data.engine.api.DataEngineContext;

/**
 *
 */

public class StreamWriter {

	private StreamID id;
	private HashMap<Integer, OutputStream> cachedStreams;
	private DataEngineContext context;

	/**
	 *
	 * @param context
	 * @param id
	 */
	public StreamWriter(DataEngineContext context, StreamID id) {
		this.id = id;
		this.cachedStreams = new HashMap<>();
		this.context = context;
	}

	/**
	 *
	 * @param streamID
	 * @return
	 */
	public boolean hasOutputStream(StreamID streamID) {
		return this.cachedStreams.get(streamID) != null;
	}

	/**
	 *
	 * @param streamType
	 * @return
	 */
	public OutputStream getOutputStream(int streamType) {
		assert id != null;

		OutputStream os = new DummyOutputStream(context, id, streamType);
		this.cachedStreams.put(Integer.valueOf(streamType), os);
		return os;
	}
}
