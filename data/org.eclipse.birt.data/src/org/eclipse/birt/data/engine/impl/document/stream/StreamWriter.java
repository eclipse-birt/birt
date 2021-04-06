/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		this.cachedStreams = new HashMap<Integer, OutputStream>();
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