
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

import java.util.HashMap;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */
public abstract class StreamReader {
	protected HashMap streamMap;
	protected StreamID id;
	protected DataEngineContext context;

	/**
	 * 
	 * @param streamType
	 * @return
	 * @throws DataException
	 */
	public abstract RAInputStream getRAInputStream(int streamType) throws DataException;

	/**
	 * 
	 * @param streamType
	 * @return
	 */
	public boolean hasInputStream(int streamType) {
		return this.streamMap.get(Integer.valueOf(streamType)) != null;
	}

}
