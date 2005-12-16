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
import org.eclipse.birt.report.engine.api.script.element.IDataSet;
import org.eclipse.birt.report.engine.api.script.element.IDataSource;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class DataSet implements IDataSet
{

	private DataSetHandle dataSet;

	public DataSet( DataSetHandle dataSet )
	{
		this.dataSet = dataSet;
	}

	public IDataSource getDataSource( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getQueryText( )
	{
		if ( dataSet instanceof OdaDataSetHandle )
			return ( ( OdaDataSetHandle ) dataSet ).getQueryText( );
		return null;
	}

	public void setQueryText( String query ) throws ScriptException
	{
		if ( dataSet instanceof OdaDataSetHandle )
		{
			try
			{
				( ( OdaDataSetHandle ) dataSet ).setQueryText( query );
			} catch ( SemanticException e )
			{
				throw new ScriptException( e.getLocalizedMessage( ) );
			}
		}
	}

	public String getPrivateDriverProperty( String name )
	{
		if ( dataSet instanceof OdaDataSetHandle )
			return ( ( OdaDataSetHandle ) dataSet )
					.getPrivateDriverProperty( name );
		return null;
	}

	public void setPrivateDriverProperty( String name, String value )
			throws ScriptException
	{
		if ( dataSet instanceof OdaDataSetHandle )
		{
			try
			{
				( ( OdaDataSetHandle ) dataSet ).setPrivateDriverProperty(
						name, value );
			} catch ( SemanticException e )
			{
				throw new ScriptException( e.getLocalizedMessage( ) );
			}
		}
	}

}
