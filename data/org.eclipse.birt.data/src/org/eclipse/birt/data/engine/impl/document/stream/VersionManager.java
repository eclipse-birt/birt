/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.document.stream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * Manager the version of report document.
 */
public class VersionManager
{
	// below value can not be changed
	public final static int VERSION_2_0 = 0;
	public final static int VERSION_2_1 = 1;
	public final static int VERSION_2_2 = 2;
	public final static int VERSION_2_2_0 = 10;
	public final static int VERSION_2_2_1 = 20;
	public final static int VERSION_2_2_1_1 = 25;
	public final static int VERSION_2_2_1_2 = 30;
	public final static int VERSION_2_2_1_3 = 50;
	
	
	//In version 2_3_1 the sort strength information is saved.
	public final static int VERSION_2_3_1 = 70;
	
	//In version 2_3_2 the PLS_GROUPLEVEL is saved.
	public final static int VERSION_2_3_2 = 90;
	
	//In version 2_3_2_1 the IQueryExecutionHints is saved.
	public final static int VERSION_2_3_2_1 = 95;
	
	//In version 2_5_0_1 the Sort strength is saved.
	public final static int VERSION_2_5_0_1 = 100;
	
	//In version 2_5_1_0 the aggregation value is saved separately
	public final static int VERSION_2_5_1_0 = 110;
	
	private DataEngineContext dataEngineContext;
	private static Logger logger = Logger.getLogger( VersionManager.class.getName( ) );
	
	public VersionManager( DataEngineContext context )
	{
		this.dataEngineContext = context;
	}
	
	/**
	 * @return
	 */
	public int getVersion( )
	{
		//Default is 2.2
		int version = getLatestVersion( );

		if ( dataEngineContext.hasInStream( null,
				null,
				DataEngineContext.VERSION_INFO_STREAM ) == false )
		{
			version = VERSION_2_0;
			return version;
		}

		try
		{
			DataInputStream is = new DataInputStream(dataEngineContext.getInputStream( null, null, DataEngineContext.VERSION_INFO_STREAM ));
			version = IOUtil.readInt( is );
			is.close( );
		}
		catch ( DataException e )
		{
			logger.log( Level.FINE, e.getMessage( ), e );
		}
		catch ( IOException e )
		{
			logger.log( Level.FINE, e.getMessage( ), e );
		}
	
		return version;
	}
	
	
	/**
	 * @param context
	 * @param string
	 * @throws DataException
	 */
	void setVersion( int version )
			throws DataException
	{
		OutputStream versionOs = this.dataEngineContext.getOutputStream( null,
				null,
				DataEngineContext.VERSION_INFO_STREAM );
		DataOutputStream versionDos = new DataOutputStream( versionOs );
		try
		{
			IOUtil.writeInt( versionDos, version );
			versionDos.close( );
			versionOs.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getLatestVersion( )
	{
		return VERSION_2_5_1_0;
	}

}
