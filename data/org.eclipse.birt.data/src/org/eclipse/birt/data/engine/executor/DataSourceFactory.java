/*************************************************************************
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
 *  
 *************************************************************************/
package org.eclipse.birt.data.engine.executor;

import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IDataSourceFactory;

/**
 * 
 */
public class DataSourceFactory implements IDataSourceFactory {
	/**
	 * volatile modifier is used here to ensure the DataSourceFactory, when being
	 * constructed by JVM, will be locked by current thread until the finish of
	 * construction.
	 */
	private static volatile DataSourceFactory instance = null;

	/**
	 * @return
	 */
	public static IDataSourceFactory getFactory() {
		if (instance == null) {
			synchronized (DataSourceFactory.class) {
				if (instance == null)
					instance = new DataSourceFactory();
			}
		}

		return instance;
	}

	/**
	 *
	 */
	private DataSourceFactory() {
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSourceFactory#getNullDataSource()
	 */
	public IDataSource getEmptyDataSource(DataEngineSession session) {
		// TODO: connection pooling
		return new DataSource(null, null, session);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.odi.IDataSourceFactory#getDataSource(java.lang.
	 * String, java.util.Map,
	 * org.eclipse.birt.data.engine.api.IBaseDataSourceDesign,
	 * org.eclipse.birt.data.engine.api.IBaseDataSetDesign, java.util.Collection,
	 * int, int)
	 */
	public IDataSource getDataSource(String driverName, Map connProperties, DataEngineSession session)
			throws DataException {
		return new DataSource(driverName, connProperties, session);
	}

}
