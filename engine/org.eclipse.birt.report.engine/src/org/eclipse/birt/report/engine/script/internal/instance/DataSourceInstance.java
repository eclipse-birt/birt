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

import org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle;
import org.eclipse.birt.report.engine.api.script.instance.IDataSourceInstance;

public class DataSourceInstance implements IDataSourceInstance
{

	private IDataSourceInstanceHandle dataSource;

	public DataSourceInstance( IDataSourceInstanceHandle dataSource )
	{
		this.dataSource = dataSource;
	}

	public String getName( )
	{
		return dataSource.getName( );
	}

	public String getExtensionID( )
	{
		return dataSource.getExtensionID( );
	}

}
