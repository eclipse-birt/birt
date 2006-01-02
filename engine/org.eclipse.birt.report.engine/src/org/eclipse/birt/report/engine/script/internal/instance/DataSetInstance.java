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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.eclipse.birt.report.engine.api.script.ScriptException;
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

	public String getQueryText( ) throws ScriptException
	{
		try
		{
			return dataSet.getQueryText( );
		} catch ( BirtException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public void setQueryText( String queryText ) throws ScriptException
	{
		try
		{
			dataSet.setQueryText( queryText );
		} catch ( BirtException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public Set getExtensionProperty( String name )
	{
		Map m = dataSet.getPublicProperties( );
		if ( m == null )
			return null;
		return ( Set ) m.get( name );
	}

	public void setExtensionProperty( String name, Set value )
	{
		Map m = dataSet.getPublicProperties( );
		if ( m == null )
			return;
		m.put( name, value );
	}

	public void setExtensionProperty( String name, String value )
	{
		Map m = dataSet.getPublicProperties( );
		if ( m == null )
			return;
		Set s = ( Set ) m.get( name );
		if ( s == null )
			s = new HashSet( );
		s.add( value );
		m.put( name, s );
	}

	public Map getExtensionProperties( )
	{
		return dataSet.getPublicProperties( );
	}

}
