
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
package org.eclipse.birt.data.engine.executor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataSetCacheUtil;
import org.eclipse.birt.data.engine.odi.IResultClass;


/**
 * The data set cache object which serve for disk based data set cache.
 */
public class DiskDataSetCacheObject implements IDataSetCacheObject
{
	//
	private String cacheDir;
	
	//the most row count this cache can save
	private int cacheCapability;

	/**
	 * 
	 * @param tempFolder
	 * @param appContext 
	 * @param parameterHints 
	 * @param baseDataSetDesign 
	 * @param baseDataSourceDesign 
	 */
	public DiskDataSetCacheObject( String cacheDir, int cacheCapability )
	{
		assert cacheCapability > 0;
		this.cacheDir = cacheDir;
		this.cacheCapability = cacheCapability;
	}

	
	/**
	 * 
	 * @return
	 */
	public File getDataFile()
	{
		return new File( cacheDir + File.separator + "data.data");
	}
	
	/**
	 * 
	 * @return
	 */
	public File getMetaFile()
	{
		return new File( cacheDir + File.separator + "meta.data");
	}

	public boolean isCachedDataReusable( int requiredCapability )
	{
		assert requiredCapability > 0;
		return getDataFile().exists( )
				&& getMetaFile().exists( )
				&& cacheCapability >= requiredCapability;
	}


	public boolean needUpdateCache( int requiredCapability )
	{
		return !isCachedDataReusable(requiredCapability);
	}


	public void release( )
	{
		if (cacheDir != null)
		{
			DataSetCacheUtil.deleteDir( cacheDir );	
		}
	}


	public IResultClass getResultClass( ) throws DataException
	{
		IResultClass rsClass;
		FileInputStream fis1 = null;
		BufferedInputStream bis1 = null;
		try
		{
			fis1 = new FileInputStream( getMetaFile( ) );
			bis1 = new BufferedInputStream( fis1 );
			IOUtil.readInt( bis1 );
			rsClass = new ResultClass( bis1 );
			bis1.close( );
			fis1.close( );

			return rsClass;
		}
		catch ( FileNotFoundException e )
		{
			throw new DataException( ResourceConstants.DATASETCACHE_LOAD_ERROR,
					e );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.DATASETCACHE_LOAD_ERROR,
					e );
		}
	}

	public String getCacheDir( )
	{
		return cacheDir;
	}
	
}
