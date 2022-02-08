/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.IncreDataSetCacheObject;
import org.eclipse.birt.data.engine.executor.cache.CacheUtil;
import org.eclipse.birt.data.engine.odi.IQuery;

/**
 * 
 */

public class PreparedIncreCacheDSQuery extends PreparedOdaDSQuery implements IPreparedQuery {

	PreparedIncreCacheDSQuery(DataEngineImpl dataEngine, IQueryDefinition queryDefn, IBaseDataSetDesign dataSetDesign,
			Map appContext) throws DataException {
		super(dataEngine, queryDefn, dataSetDesign, appContext, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.PreparedOdaDSQuery#newExecutor()
	 */
	protected QueryExecutor newExecutor() throws DataException {
		IIncreCacheDataSetDesign icDataSetDesign = (IIncreCacheDataSetDesign) dataSetDesign;
		String cacheDir = CacheUtil.createIncrementalTempDir(dataEngine.getSession(), icDataSetDesign);
		logger.log(Level.INFO, "Create incremental cache directory: " + cacheDir);
		return new IncreCacheDSQueryExecutor(cacheDir);
	}

	/**
	 * 
	 */
	public class IncreCacheDSQueryExecutor extends OdaDSQueryExecutor {

		private String cacheDir;

		public IncreCacheDSQueryExecutor(String cacheDir) {
			this.cacheDir = cacheDir;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.impl.PreparedOdaDSQuery.OdaDSQueryExecutor#
		 * createOdiQuery()
		 */
		protected IQuery createOdiQuery() throws DataException {
			OdaDataSetRuntime extDataSet = (OdaDataSetRuntime) dataSet;
			assert extDataSet != null;
			assert odiDataSource != null;
			IIncreCacheDataSetDesign icDataSetDesign = (IIncreCacheDataSetDesign) dataSetDesign;
			String queryText = null;
			try {
				long lasttime = CacheUtil.getLastTimestamp(cacheDir);
				queryText = icDataSetDesign.getQueryForUpdate(lasttime);
			} catch (DataException e) {
				File dataFile = new File(cacheDir, IncreDataSetCacheObject.DATA_DATA);
				if (FileSecurity.fileExist(dataFile)) {
					FileSecurity.fileDelete(dataFile);
					logger.log(Level.WARNING, "Incremental cache data file was deleted! path: "
							+ FileSecurity.fileGetAbsolutePath(dataFile));
				}
				queryText = icDataSetDesign.getQueryText();
			}
			String dataSetType = extDataSet.getExtensionID();
			logger.log(Level.INFO, "Execute SQL: " + queryText);
			odiQuery = odiDataSource.newQuery(dataSetType, queryText, false, this.contextVisitor);
			return odiQuery;
		}
	}
}
