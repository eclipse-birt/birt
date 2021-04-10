
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
