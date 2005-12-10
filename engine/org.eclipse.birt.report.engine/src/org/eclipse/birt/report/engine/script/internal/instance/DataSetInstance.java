/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.script.internal.instance;

import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;
import org.eclipse.birt.report.engine.api.script.instance.IDataSourceInstance;

public class DataSetInstance implements IDataSetInstance
{

	private IDataSetInstanceHandle dataSet;

	public DataSetInstance( IDataSetInstanceHandle dataSet )
	{
		this.dataSet = dataSet;
	}

	public String getName( )
	{
		return dataSet.getName( );
	}

	public IDataSourceInstance getDataSource( )
	{
		return new DataSourceInstance( dataSet.getDataSource( ) );
	}

	public String getExtensionID( )
	{
		return dataSet.getExtensionID( );
	}

	public String getQueryText( )
	{
		try
		{
			return dataSet.getQueryText( );
		} catch ( Exception e )
		{
			e.printStackTrace( );
		}
		return null;
	}

	public void setQueryText( String queryText )
	{
		try
		{
			dataSet.setQueryText( queryText );
		} catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}

}
