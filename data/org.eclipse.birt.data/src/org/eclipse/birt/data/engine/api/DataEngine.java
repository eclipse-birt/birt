/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 

package org.eclipse.birt.data.engine.api;

import java.io.File;

import org.mozilla.javascript.Scriptable;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;

/**
 * Data Engine API class.
 * <br>
 * Provides methods to define data sources and data sets, and to prepare a
 * {@link org.eclipse.birt.data.engine.api.IQueryDefinition}. An application
 * typically needs only one instance of this class, which can be used to 
 * prepare and execute multiple data queries.
 * <p>
 * User of this class must always call the <code>shutdown</code> method when it is
 * done with an instance of this class to ensure release of all data source 
 * connections and related resources.
 */
abstract public class DataEngine
{

    /**
     * Creates a new instance of DataEngine, using the specified Javascript scope and
     * home directory setting. 
     * @param sharedScope a Javascript scope to be used as the "shared" scope to evaluate
     *    Javascript expressions by the data engine. 
     * @param homeDir The data engine's home directory. The home directory is where the
     *    data engine will look for its configuration resources.
     * 
     */
    public static DataEngine newDataEngine( Scriptable sharedScope, File homeDir )
    {
        return new DataEngineImpl( sharedScope, homeDir );
    }
	
	/**
	 * Provides the definition of a data source to Data Engine. A data source must be
	 * defined using this method prior to preparing any report query that uses such data source.
	 * <br>
	 * Data sources are uniquely identified name. If specified data source has already
	 * been defined, its definition will be updated with the content of the provided definition object.
	 */
	abstract public void defineDataSource( IBaseDataSourceDesign dataSource ) 
			throws DataException;

	/**
	 * Provides the definition of a data set to Data Engine. A data set must be
	 * defined using this method prior to preparing any report query that uses such data set.
	 * <br>
	 * Data sets are uniquely identified name. If specified data set has already
	 * been defined, its definition will be updated with the content of the provided definition object.
	 */
	abstract public void defineDataSet( IBaseDataSetDesign dataSet ) 
			throws DataException;
	
	/**
	 * Verifies the elements of a report query spec
	 * and provides a hint to the query to prepare and optimize 
	 * an execution plan.
	 * The given querySpec could be a <code>IQueryDefinition</code> 
	 * (raw data transform) spec  
	 * based on static definition found in a report design.
	 * <p> 
	 * This report query spec could be further refined 
	 * during engine execution after having resolved any related
	 * runtime condition.
	 * For example, a nested report item might not be rendered based
	 * on a runtime condition.  Thus its associated data expression
	 * could be removed from the report query defn given to 
	 * DtE to prepare.
	 * <p>
	 * During prepare, the DTE does not open a data set. 
	 * In other words, any <code>beforeOpen</code> script on a data set will not be
	 * evaluated at this stage. 
	 * @param	querySpec	Specifies
	 * 				the data access and data transforms services
	 * 				needed from DtE to produce a set of query results.
	 * @return		The <code>IPreparedQuery</code> object that contains a prepared 
	 * 				query ready for execution.
	 * @throws 		DataException if error occurs during the preparation of querySpec
	 */
	abstract public IPreparedQuery prepare( IQueryDefinition querySpec )
			throws DataException;
	
	/**
	 * Provides a hint to DtE that the consumer is done with the given 
	 * data source, and 
	 * that its resources can be safely released as appropriate.
	 * This tells DtE that there is to be no more query
	 * that uses such data source.
	 * @param	dataSourceName	The name of a data source. The named data source
	 *          must have been previously defined.
	 */
	abstract public void closeDataSource( String dataSourceName )
			throws DataException;
	
	/**
	 * Shuts down this instance of data engine, and releases all associated resources.
	 * This method should be called when the caller is done with an instance of the data engine.
	 */
	abstract public void shutdown();
	
}

