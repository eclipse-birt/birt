/*
 *************************************************************************
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
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odi;

import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineSession;

/**
 * A factory for IDataSource instances that represent underlying data sources
 * and corresponding connection properties.
 */
public interface IDataSourceFactory {
	/**
	 * Instantiates a dedicated data source instance with the specified driver name.
	 * The driver name is required to find corresponding driver to load and submit
	 * data access requests.
	 * 
	 * @param driverName The name of a data source driver.
	 * @return A new instance of IDataSource.
	 */
	// public IDataSource newDataSource( String driverName );

	/**
	 */
	public IDataSource getEmptyDataSource(DataEngineSession session);

	/**
	 * Obtains a dedicated data source instance with the specified driver name and
	 * connection properties. A named property can be mapped to more than one
	 * values. <br>
	 * The property name is of String type. <br>
	 * The property value is a Set interface of string values.
	 * <p>
	 * An implementation might support a pool of connected but inactive data
	 * sources. <br>
	 * If no existing data source instance with the specified properties is
	 * available, a new instance will be instantiated.
	 * 
	 * @param driverName     The name of a data source driver.
	 * @param connProperties The connection properties as a Map of name-set pairs.
	 * @return An instance of IDataSource.
	 */
	public IDataSource getDataSource(String driverName, Map connProperties, DataEngineSession session)
			throws DataException;

}
