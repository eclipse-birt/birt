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
package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IDataSource;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class DataSource implements IDataSource
{

	private DataSourceHandle dataSource;

	public DataSource( DataSourceHandle dataSource )
	{
		this.dataSource = dataSource;
	}

	public String getExtensionID( )
	{
		if ( dataSource instanceof OdaDataSourceHandle )
			return ( ( OdaDataSourceHandle ) dataSource ).getExtensionID( );
		return null;
	}

	public String getPrivateDriverProperty( String name )
	{
		if ( dataSource instanceof OdaDataSourceHandle )
			return ( ( OdaDataSourceHandle ) dataSource )
					.getPrivateDriverProperty( name );
		return null;
	}

	public void setPrivateDriverProperty( String name, String value )
			throws ScriptException
	{
		if ( dataSource instanceof OdaDataSourceHandle )
		{
			try
			{
				( ( OdaDataSourceHandle ) dataSource )
						.setPrivateDriverProperty( name, value );
			} catch ( SemanticException e )
			{
				throw new ScriptException( e.getLocalizedMessage( ) );
			}
		}

	}

}
