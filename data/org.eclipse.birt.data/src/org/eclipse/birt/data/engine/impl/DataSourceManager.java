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

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * Manager these data sources which are overwritten by the new data sources
 * definition.
 *
 * The method of DataEngineImpl#defineDataSource might be called more than once
 * for the same data source, so the new one will replace the existing defined
 * data source. There are two choices for the old one, closed immediatelly or in
 * future, which is decided by whther the old one is used or not.
 */
class DataSourceManager {
	// overwritten data source runtime list
	private List dataSourceRuntimeList = new ArrayList();

	// logger
	private Logger logger;

	/**
	 * @param logger
	 */
	DataSourceManager(Logger logger) {
		this.logger = logger;
	}

	/**
	 * @param dataSourceRuntime
	 * @throws DataException
	 */
	void addDataSource(DataSourceRuntime dataSourceRuntime) throws DataException {
		if (dataSourceRuntime.canClose()) {
			closeDataSource(dataSourceRuntime);
		} else {
			close(false);
		}

		dataSourceRuntimeList.add(dataSourceRuntime);
	}

	/**
	 *
	 */
	void close() {
		close(true);
	}

	/**
	 * close all data sources
	 *
	 * @param forceClose
	 */
	private void close(boolean forceClose) {
		Iterator it = this.dataSourceRuntimeList.iterator();
		while (it.hasNext()) {
			DataSourceRuntime ds = (DataSourceRuntime) it.next();
			try {
				if (!forceClose && !ds.canClose()) {
					continue;
				}

				closeDataSource(ds);
			} catch (DataException e) {
				if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.FINE, "The data source (" + ds + ") fails to shut down", e);
				}
			}
		}
	}

	/**
	 * @param ds
	 * @throws DataException
	 */
	private void closeDataSource(DataSourceRuntime ds) throws DataException {
		if (ds.isOpen()) {
			ds.beforeClose();
			ds.closeOdiDataSource();
			ds.afterClose();
		}
	}

}
