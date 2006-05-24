/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.xml.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.eclipse.birt.report.data.oda.xml.Constants;
import org.eclipse.birt.report.data.oda.xml.i18n.Messages;
import org.eclipse.birt.report.data.oda.xml.util.XMLDataInputStreamCreator;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * This class is used to build an XML data source connection. 
 */
public class Connection implements IConnection
{
	//The file which server as data source.
	private XMLDataInputStreamCreator is;

	//The boolean indicate whether the connection is open.
	private boolean isOpen;
	
	private Map appContext;

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties )
			throws org.eclipse.datatools.connectivity.oda.OdaException
	{
		if( isOpen == true )
			return;
		String file = (String) connProperties.get( Constants.CONST_PROP_FILELIST );

		if ( appContext != null
				&& appContext.get( Constants.APPCONTEXT_INPUTSTREAM ) != null
				&& appContext.get( Constants.APPCONTEXT_INPUTSTREAM ) instanceof InputStream )
			is = XMLDataInputStreamCreator.getCreator( (InputStream) appContext.get( Constants.APPCONTEXT_INPUTSTREAM ) );
		else if ( file != null )
			is = XMLDataInputStreamCreator.getCreator( file );
		else
			throw new OdaException( Messages.getString( "Connection.PropertiesMissing" ) );
		isOpen = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close( )
			throws org.eclipse.datatools.connectivity.oda.OdaException
	{
		isOpen = false;
		try
		{
			if(appContext != null && appContext.get(Constants.APPCONTEXT_INPUTSTREAM)!= null)
			{
				Object closeInputStream = appContext.get(Constants.APPCONTEXT_CLOSEINPUTSTREAM);
				if( closeInputStream != null )
				{
					if( closeInputStream.toString().equalsIgnoreCase("true"))
						closeInputStreamFromAppContext( );
				}
			}
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.appContext = null;
	}

	/**
	 * @throws IOException
	 */
	private void closeInputStreamFromAppContext( ) throws IOException
	{
		((InputStream) appContext.get( Constants.APPCONTEXT_INPUTSTREAM )).close( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen( )
			throws OdaException
	{
		return isOpen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
		if( !( context instanceof Map ) )
			throw new OdaException( Messages.getString("Connection.InvalidAppContext") ); 
		this.appContext = (Map)context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMetaData(java.lang.String)
	 */
	public IDataSetMetaData getMetaData( String dataSetType )
			throws OdaException
	{
		return new DataSetMetaData( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang.String)
	 */
	public IQuery newQuery( String dataSetType ) throws OdaException
	{
		
		return new Query( this.is );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMaxQueries()
	 */
	public int getMaxQueries( ) throws OdaException
	{
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#commit()
	 */
	public void commit( ) throws OdaException
	{
		throw new UnsupportedOperationException( );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#rollback()
	 */
	public void rollback( ) throws OdaException
	{
		throw new UnsupportedOperationException( );

	}
}
