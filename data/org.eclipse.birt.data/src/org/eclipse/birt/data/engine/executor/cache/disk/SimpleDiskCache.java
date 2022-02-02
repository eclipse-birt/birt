/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor.cache.disk;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

public class SimpleDiskCache extends DiskCache {

	private String tempFile;

	public SimpleDiskCache(IResultObject[] resultObjects, IResultClass rsMeta, int memoryCacheRowCount, int maxRows,
			DataEngineSession session) throws DataException {
		this.rsMeta = rsMeta;
		this.session = session;
		this.tempFile = session.getTempDir() + File.separator + "goalFile";
		this.diskBasedResultSet = new ManualDiskCacheResultSet(session, rsMeta, tempFile, memoryCacheRowCount);
	}

	public void add(IResultObject resultObject) throws DataException {
		((ManualDiskCacheResultSet) diskBasedResultSet).add(resultObject);
	}

	public void add(IResultObject[] resultObjects) throws DataException {
		((ManualDiskCacheResultSet) diskBasedResultSet).add(resultObjects);
	}

	static class ManualDiskCacheResultSet extends DiskCacheResultSet {

		private RowFile rowFile;

		ManualDiskCacheResultSet(DataEngineSession session, IResultClass rsMetaData, String file, int cacheSize) {
			super(new HashMap(), session);
			this.resultObjectUtil = ResultObjectUtil.newInstance(rsMetaData, session);
			this.rowFile = new RowFile(new File(file), resultObjectUtil, cacheSize);
		}

		public void add(IResultObject resultObject) throws DataException {
			try {
				rowFile.write(resultObject);
			} catch (IOException ioex) {
				throw new DataException(ioex.getLocalizedMessage(), ioex);
			}
		}

		public void add(IResultObject[] resultObjects) throws DataException {
			try {
				rowFile.writeRows(resultObjects, resultObjects.length);
			} catch (IOException ioex) {
				throw new DataException(ioex.getLocalizedMessage(), ioex);
			}
		}
	}

}
