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
package org.eclipse.birt.data.engine.executor;

import java.util.Map;

import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IDataSourceFactory;

/**
 * 
 */
public class DataSourceFactory implements IDataSourceFactory
{
	private static DataSourceFactory instance = null;
	
	
	public static IDataSourceFactory getFactory()
	{
		if ( instance == null )
			instance = new DataSourceFactory();
		return instance;
	}
	
	private DataSourceFactory()
	{
	}
	
	/**
	 * @see org.eclipse.birt.data.engine.odi.IDataSourceFactory#newDataSource(java.lang.String)
	 */
	public IDataSource newDataSource(String driverName)
	{
		// TODO: connection pooling
		return new DataSource( driverName );
	}

	/**
	 * @see org.eclipse.birt.data.engine.odi.IDataSourceFactory#getDataSource(java.lang.String, java.util.Map)
	 */
	public IDataSource getDataSource(String driverName, Map connProperties)
	{
		// TODO: connection pooling
		return new DataSource( driverName, connProperties );
	}

}
