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
package org.eclipse.birt.data.engine.impl;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSetCacheConfig;
import org.eclipse.birt.data.engine.executor.DataSetCacheConfig.DataSetCacheMode;

/**
 * 
 */
public class DataSetCacheUtil
{
	/**
	 * used to get DataSetCacheConfig from all boring options outside
	 * @param appContext
	 * @param context
	 * @param session
	 * @param dataSetDesign
	 * @return null if no JVM level data set cache settings
	 * @throws DataException
	 */
	public static DataSetCacheConfig getJVMDataSetCacheConfig(
			Map appContext,
			DataEngineContext context, 
			IBaseDataSetDesign dataSetDesign) throws DataException
	{	
		String tempDir = context.getTmpdir( );
		if (dataSetDesign != null && dataSetDesign instanceof IIncreCacheDataSetDesign)
		{
			return DataSetCacheConfig.getInstance( DataSetCacheMode.IN_DISK, -1, true, tempDir );
		}
		if ( appContext != null )
		{
			Object option = appContext.get( DataEngine.MEMORY_DATA_SET_CACHE );
			if( option != null )
			{
				int rowLimit = getIntValueFromString(option);
				return DataSetCacheConfig.getInstacne( DataSetCacheMode.IN_MEMORY, rowLimit, null );
			}
			
			option = appContext.get( DataEngine.DATA_SET_CACHE_ROW_LIMIT );
			if( option != null )
			{
				int rowLimit = getIntValueFromString(option);
				return DataSetCacheConfig.getInstacne( DataSetCacheMode.IN_DISK, rowLimit, tempDir);
			}
		}
		
		int cacheOption = context.getCacheOption( );
		if ( cacheOption == DataEngineContext.CACHE_USE_ALWAYS )
		{
			return DataSetCacheConfig.getInstacne( DataSetCacheMode.IN_DISK, context.getCacheCount( ), tempDir);
		}
		else if ( cacheOption == DataEngineContext.CACHE_USE_DISABLE )
		{
			return null;
		}
		else if ( appContext != null )
		{
			Object option = appContext.get( DataEngine.DATASET_CACHE_OPTION );
			if ( option != null && option.toString( ).equals( "true" ) )
			{
				int cacheCount = dataSetDesign.getCacheRowCount( );
				if (cacheCount == 0)
				{
					cacheCount = context.getCacheCount( );
				}
				return DataSetCacheConfig.getInstacne( DataSetCacheMode.IN_DISK, cacheCount, tempDir);
			}
		}
		return null;
	}
	
	
	/**
	 * @param queryExecutionHints
	 * @param dataSetDesign
	 * @param session
	 * @return null if no Dte level data set settings
	 * @throws DataException
	 */
	public static DataSetCacheConfig getDteDataSetCacheConfig(IEngineExecutionHints queryExecutionHints,
			IBaseDataSetDesign dataSetDesign,
			DataEngineSession session) throws DataException
	{
		if( queryExecutionHints == null || dataSetDesign == null )
		{
			return null;
		}
		else
		{
			if (queryExecutionHints.needCacheDataSet( dataSetDesign.getName( ) ))
			{
				return DataSetCacheConfig.getInstacne( DataSetCacheMode.IN_DISK, -1, session.getTempDir( ));
			}
			else
			{
				return null;
			}
		}
	}
	
	
	/**
	 * 
	 * @param dir
	 */
	public static void deleteFile( final String path )
	{
		if (path == null)
		{
			return;
		}
		AccessController.doPrivileged( new PrivilegedAction<Object>()
		{
		  public Object run()
		  {
		    deleteFile(new File(path));
		    return null;
		  }
		});
		
	}
	
	/**
	 * 
	 * @param dir
	 */
	public static void deleteFile( final File f )
	{
		try
		{
			AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
				{
					if ( f == null || !f.exists( ))
					{
						return null;
					}
					if (f.isFile( ))
					{
						safeDelete( f );
					}
					else
					{
						File[] childFiles = f.listFiles( );
						if( childFiles != null )
						{
							for (File child : childFiles)
							{
								deleteFile( child );
							}
						}
						safeDelete( f );
					}
					return null;
				}
				
				/**
				 * 
				 * @param file
				 */
				private void safeDelete( File file )
				{
					if( !file.delete( ) )
					{
						file.deleteOnExit( );
					}
				}
			} );
		}
		catch ( Exception e )
		{
			
		}
	}
	

	

	/**
	 * 
	 * @param option
	 * @return
	 */
	private static int getIntValueFromString(Object option) 
	{
		return Integer.valueOf(option.toString()).intValue();
	}
}


