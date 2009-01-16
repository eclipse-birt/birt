package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.util.Properties;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;


public class OdaConnectionProvider
{
	private DataSourceDesign dataSourceDesign;
	private IConnection connection;
	
	OdaConnectionProvider( DataSourceDesign dataSourceDesign )
	{
		this.dataSourceDesign = dataSourceDesign;
	}
	
	IConnection openConnection( ) throws OdaException
	{
		if ( connection != null )
		{
			return connection;
		}
		IDriver jdbcDriver = new OdaJdbcDriver( );
		try 
		{
			connection = jdbcDriver.getConnection( dataSourceDesign.getOdaExtensionId( ) );
			Properties prop = DesignSessionUtil.getEffectiveDataSourceProperties( dataSourceDesign );
			connection.open( prop );
		}
		catch ( OdaException e)
		{
			connection = null;
			throw e;
		}
		return connection;
	}
	
	void release( )
	{
		if ( connection != null )
		{
			try
			{
				connection.close( );
			}
			catch ( OdaException e )
			{
				
			}
			finally 
			{
				connection = null;
			}
		}
	}
	
}
