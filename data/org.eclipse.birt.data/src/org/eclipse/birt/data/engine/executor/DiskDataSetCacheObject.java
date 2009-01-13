
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
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;

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
	private static Integer count = new Integer( 0 );
	
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
		if( cacheDir.endsWith( File.separator ))
		{
			this.cacheDir = cacheDir + "DataSetCacheObject_" + this.hashCode( ) + "_" + getCount() ;	
		}
		else
		{
			this.cacheDir = cacheDir + File.separator + "DataSetCacheObject_" + this.hashCode( ) + "_" + getCount();
		}
		AccessController.doPrivileged( new PrivilegedAction<Object>()
		{
		  public Object run()
		  {
		    return new Boolean(new File(DiskDataSetCacheObject.this.cacheDir).mkdirs());
		  }
		});
		
		this.cacheCapability = cacheCapability;
	}
	
	/**
	 * 
	 * @return
	 */
	private int getCount()
	{
		synchronized( count )
		{
			count = (count + 1) % 100000;
			return count.intValue( );
		}
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

	public boolean isCachedDataReusable( final int requiredCapability )
	{
		assert requiredCapability > 0;
		try
		{
			return (Boolean) AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
				{
					return getDataFile().exists( )
					&& getMetaFile().exists( )
					&& cacheCapability >= requiredCapability;
				}
			} );
		}
		catch ( Exception e )
		{
			return false;
		}
		
		
	}


	public boolean needUpdateCache( int requiredCapability )
	{
		return !isCachedDataReusable(requiredCapability);
	}


	public void release( )
	{
		DataSetCacheUtil.deleteFile( cacheDir );
	}


	public IResultClass getResultClass( ) throws DataException
	{
		try
		{
			return (IResultClass) AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
				{

					IResultClass rsClass;
					FileInputStream fis1 = null;
					BufferedInputStream bis1 = null;
					fis1 = new FileInputStream( getMetaFile( ) );
					bis1 = new BufferedInputStream( fis1 );
					IOUtil.readInt( bis1 );
					rsClass = new ResultClass( bis1 );
					bis1.close( );
					fis1.close( );

					return rsClass;
				}
			} );
		}
		catch ( Exception e )
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
