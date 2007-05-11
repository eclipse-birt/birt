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
	
	private DataEngineContext dataEngineContext;
	
	VersionManager( DataEngineContext context )
	{
		this.dataEngineContext = context;
	}
	
	/**
	 * @return
	 */
	int getVersion( )
	{
		int version = this.getLatestVersion( );

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
		}
		catch ( DataException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	int getLatestVersion( )
	{
		return VERSION_2_2_0;
	}

}
