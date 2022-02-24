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
package org.eclipse.birt.data.engine.impl.document.stream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;

/**
 * Manager the version of report document.
 */
public class VersionManager {
	// below value can not be changed
	public final static int VERSION_2_0 = 0;
	public final static int VERSION_2_1 = 1;
	public final static int VERSION_2_2 = 2;
	public final static int VERSION_2_2_0 = 10;
	public final static int VERSION_2_2_1 = 20;
	public final static int VERSION_2_2_1_1 = 25;
	public final static int VERSION_2_2_1_2 = 30;
	public final static int VERSION_2_2_1_3 = 50;

	// In version 2_3_1 the sort strength information is saved.
	public final static int VERSION_2_3_1 = 70;

	// In version 2_3_2 the PLS_GROUPLEVEL is saved.
	public final static int VERSION_2_3_2 = 90;

	// In version 2_3_2_1 the IQueryExecutionHints is saved.
	public final static int VERSION_2_3_2_1 = 95;

	// In version 2_5_0_1 the Sort strength is saved.
	public final static int VERSION_2_5_0_1 = 100;

	// In version 2_5_1_0 the aggregation value is saved separately
	public final static int VERSION_2_5_1_0 = 110;

	// In version 2_5_1_1 the summary table tag is added
	public final static int VERSION_2_5_1_1 = 120;

	// In version 2_5_2_0 the dimension index is added
	public final static int VERSION_2_5_2_0 = 130;

	// In version 2_5_2_1 the temp stream is removed.
	public final static int VERSION_2_5_2_1 = 140;

	// In version 2_6_2_1 the row ACL is BTree indexed
	// Also, the grouping storage is changed to support progressive viewing.
	public final static int VERSION_2_6_2_1 = 150;

	// Progressive Viewing Enhancement
	public final static int VERSION_2_6_2_2 = 160;

	// Empty nest query Enhancement
	public final static int VERSION_2_6_2_3 = 170;

	// Relative Time period function support
	public final static int VERSION_2_6_3_1 = 180;

	// Update aggregation flag support
	public final static int VERSION_2_6_3_2 = 190;

	// Enhance Data Set storage in BDO/Rptdocument. Warning:conflict version due to
	// merge issue
	public final static int VERSION_3_7_2_1 = 200;

	// Collation supported in BDO data set index. Warning:conflict version due to
	// merge issue
	public final static int VERSION_4_2_1_1 = 200;

	// Updated version after fixing version conflict
	public final static int VERSION_4_2_1_2 = 210;

	// filter target support
	public final static int VERSION_4_2_2_1 = 220;

	// Add Columnized Storage Support
	public final static int VERSION_4_2_2 = 300;

	// Materialize nested aggregation for xtab query
	public final static int VERSION_4_2_3 = 310;

	private DataEngineContext dataEngineContext;
	private static Logger logger = Logger.getLogger(VersionManager.class.getName());

	public VersionManager(DataEngineContext context) {
		this.dataEngineContext = context;
	}

	/**
	 * @return
	 */
	private int getVersion() {
		// Default is 2.2
		int version = getLatestVersion();

		if (dataEngineContext.hasInStream(null, null, DataEngineContext.VERSION_INFO_STREAM) == false) {
			version = VERSION_2_0;
			return version;
		}

		try {
			DataInputStream is = new DataInputStream(
					dataEngineContext.getInputStream(null, null, DataEngineContext.VERSION_INFO_STREAM));
			version = IOUtil.readInt(is);
			is.close();
		} catch (DataException e) {
			logger.log(Level.FINE, e.getMessage(), e);
		} catch (IOException e) {
			logger.log(Level.FINE, e.getMessage(), e);
		}

		return version;
	}

	/**
	 * @param context
	 * @param string
	 * @throws DataException
	 */
	private void setVersion(int version) throws DataException {
		OutputStream versionOs = this.dataEngineContext.getOutputStream(null, null,
				DataEngineContext.VERSION_INFO_STREAM);
		DataOutputStream versionDos = new DataOutputStream(versionOs);
		try {
			IOUtil.writeInt(versionDos, version);
			versionDos.close();
			versionOs.close();
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_SAVE_ERROR, e);
		}
	}

	// TODO: Enhance the performance -- current approach is not efficient in nested
	// query's case
	void setVersion(int version, String queryResultId) throws DataException {

		if (queryResultId == null || queryResultId.trim().length() == 0) {
			this.setVersion(version);
			return;
		}

		Map<String, Integer> idVersionMap = this.getQueryIdVersionMap();
		idVersionMap.put(queryResultId, version);
		DataEngineSession.getVersionForQuRsMap().put(queryResultId, version);
		this.dataEngineContext.dropStream(null, null, DataEngineContext.QUERY_ID_BASED_VERSIONING_STREAM);

		OutputStream versionOs = this.dataEngineContext.getOutputStream(null, null,
				DataEngineContext.QUERY_ID_BASED_VERSIONING_STREAM);
		DataOutputStream versionDos = new DataOutputStream(versionOs);
		try {
			IOUtil.writeMap(versionDos, idVersionMap);
			versionDos.close();
			versionOs.close();
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_SAVE_ERROR, e);
		}

	}

	private Map<String, Integer> getQueryIdVersionMap() throws DataException {
		Map<String, Integer> result = new HashMap<String, Integer>();

		if (!dataEngineContext.hasInStream(null, null, DataEngineContext.QUERY_ID_BASED_VERSIONING_STREAM))
			return result;

		try {
			DataInputStream is = new DataInputStream(
					dataEngineContext.getInputStream(null, null, DataEngineContext.QUERY_ID_BASED_VERSIONING_STREAM));
			result = IOUtil.readMap(is);
			is.close();
		} catch (DataException e) {
			logger.log(Level.FINE, e.getMessage(), e);
		} catch (IOException e) {
			logger.log(Level.FINE, e.getMessage(), e);
		}
		DataEngineSession.getVersionForQuRsMap().putAll(result);
		return DataEngineSession.getVersionForQuRsMap();
	}

	public int getVersion(String queryResultId) throws DataException {
		if (queryResultId != null && queryResultId.trim().length() > 0) {
			Integer version = this.getQueryIdVersionMap().get(queryResultId);
			if (version != null)
				return version;
		}

		return this.getVersion();
	}

	/**
	 * 
	 * @return
	 */
	public static int getLatestVersion() {
		return VERSION_4_2_3;
	}
}
