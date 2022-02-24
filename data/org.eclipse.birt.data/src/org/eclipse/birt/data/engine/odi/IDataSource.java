/*
 *************************************************************************
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
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odi;

import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.IQueryContextVisitor;

/**
 * Data source that represents underlying physical data source connection and
 * corresponding connection properties. <br>
 * Its instances are created by an IDataSourceFactory. <br>
 * It is used to create associated IQuery instances that can be executed to
 * retrieve and transform result data.
 * <p>
 * The knowledge of establishing an active connection to a data source is
 * transparent to the ODI consumer. An implementation might support using a
 * pooled connection to a data source.
 */
public interface IDataSource {
	/**
	 * Gets the name of the underlying data source driver.
	 * 
	 * @param driverName The driver name of this data source.
	 */
	// public String getDriverName();

	/**
	 * Gets the connection properties defined in this data source.
	 * 
	 * @return The connection properties as a Map of name-set pairs.
	 */
	/// public Map getProperties();

	/**
	 * Adds the specified value to the named property. Multiple calls using the same
	 * property name may be allowed. Its processing is implementation-dependent on
	 * the underlying data source.
	 * <p>
	 * Property must be set before open().
	 * 
	 * @param name  The name of property.
	 * @param value The value to add to the named property.
	 * @throws DataException if data source is already opened.
	 */
	public void addProperty(String name, String value) throws DataException;

	/**
	 * Sets the data source's context provided by an application, which is passed
	 * through to the underlying physical data source driver. <br>
	 * Its processing is implementation-dependent on the underlying data source.
	 * <p>
	 * The application context map being set here gets applied and passed through in
	 * subsequent calls to open() or newQuery(). <br>
	 * An optional method.
	 * 
	 * @param context Pass-through application context map; could be null to
	 *                override any previously set context.
	 * @throws DataException if data source error occurs
	 * @since 2.0
	 */
	public void setAppContext(Map context) throws DataException;

	/**
	 * Instantiates a new query instance that represents the specified a query text
	 * in the queryType.
	 * <p>
	 * A query text could be a native command text supported by the associated data
	 * source, such as a SQL select statement. <br>
	 * Such direct query text specification could have embedded parameters.
	 * 
	 * @param queryType The type of query supported by the data source driver.
	 * @param queryText The query text to be prepared and executed by the data
	 *                  source driver.
	 * @return A new query instance.
	 * @throws DataException if specified query definition has error(s).
	 */
	public IDataSourceQuery newQuery(String queryType, String queryText, boolean fromCache, IQueryContextVisitor qcv)
			throws DataException;

	/**
	 * Instantiates a new empty query for use with candidate result instances.
	 * 
	 * @return A new query instance.
	 */
	public ICandidateQuery newCandidateQuery(boolean fromCache) throws DataException;

	/**
	 * Indicates whether this data source is already opened.
	 * 
	 * @return true if this is already opened
	 */
	// public boolean isOpen();

	/**
	 * Opens the data source connection. <br>
	 * Optional call. Its call is implied when any associated query needs to access
	 * the underlying data source driver. If the data source is already opened,
	 * returns with no error.
	 * 
	 * @throws DataException if opening data source has error(s).
	 */
	public void open() throws DataException;

	/**
	 * @return
	 */
	public boolean canClose();

	/**
	 * Closes the data source and any associated resources. The data source and its
	 * query instances can no longer be used.
	 */
	public void close();
}
