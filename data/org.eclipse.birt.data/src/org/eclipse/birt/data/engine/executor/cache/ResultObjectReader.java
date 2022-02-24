/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.data.engine.executor.cache;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Used to read result object from input stream
 */
public class ResultObjectReader {
	private ResultObjectUtil roUtil;
	private InputStream intputStream;

	private int dataCount;
	private int curIndex;
	private ClassLoader loader;

	/**
	 * @param rsMetaData
	 * @param inputStream
	 * @param dataCount
	 * @return
	 */
	static ResultObjectReader newInstance(IResultClass rsMetaData, InputStream intputStream, int dataCount,
			DataEngineSession session) {
		assert rsMetaData != null;
		assert intputStream != null;
		assert dataCount >= 0;

		ResultObjectReader roReader = new ResultObjectReader();

		roReader.intputStream = intputStream;
		roReader.roUtil = ResultObjectUtil.newInstance(rsMetaData, session);
		roReader.dataCount = dataCount;
		roReader.curIndex = 0;
		roReader.loader = session.getEngineContext().getClassLoader();

		return roReader;
	}

	/**
	 * Construction, private
	 */
	private ResultObjectReader() {
	}

	/**
	 * @return fetched ResultObject
	 * @throws IOException
	 */
	public IResultObject fetch() throws DataException {
		if (curIndex < dataCount) {
			try {
				curIndex++;
				return roUtil.readData(intputStream, this.loader, 1)[0];
			} catch (IOException e) {
				throw new DataException("loader error", e);
			}
		}

		return null;
	}

}
