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
import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSetCacheConfig;
import org.eclipse.birt.data.engine.executor.DataSetCacheConfig.DataSetCacheMode;
import org.eclipse.birt.data.engine.executor.cache.CacheUtil;

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
	 * @return
	 * @throws DataException
	 */
	public static DataSetCacheConfig getJVMDataSetCacheConfig(
			Map appContext,
			DataEngineContext context, 
			DataEngineSession session,
			IBaseDataSetDesign dataSetDesign) throws DataException
	{	
		String sessionTempDir = session.getTempDir( );
		if (dataSetDesign != null && dataSetDesign instanceof IIncreCacheDataSetDesign)
		{
			IIncreCacheDataSetDesign icDataSetDesign = (IIncreCacheDataSetDesign) dataSetDesign;
			String cacheDir = CacheUtil.createIncrementalTempDir(session, icDataSetDesign);
			return DataSetCacheConfig.getInstance( DataSetCacheMode.IN_DISK, Integer.MAX_VALUE, true, cacheDir );
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
				return DataSetCacheConfig.getInstacne( DataSetCacheMode.IN_DISK, rowLimit,
						CacheUtil.createSessionTempDir( CacheUtil.createTempRootDir(sessionTempDir) ));
			}
		}
		
		int cacheOption = context.getCacheOption( );
		if ( cacheOption == DataEngineContext.CACHE_USE_ALWAYS )
		{
			return DataSetCacheConfig.getInstacne( DataSetCacheMode.IN_DISK, context.getCacheCount( ),
					CacheUtil.createSessionTempDir( CacheUtil.createTempRootDir(sessionTempDir) ));
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
				return DataSetCacheConfig.getInstacne( DataSetCacheMode.IN_DISK, cacheCount,
						CacheUtil.createSessionTempDir( CacheUtil.createTempRootDir(sessionTempDir) ));
			}
		}
		return null;
	}
	
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
				return DataSetCacheConfig.getInstacne( DataSetCacheMode.IN_DISK, Integer.MAX_VALUE,
						CacheUtil.createSessionTempDir( CacheUtil.createTempRootDir(session.getTempDir( )) ));
			}
			else
			{
				return null;
			}
		}
	}
	
	/**
	 * Delete folder
	 * 
	 * @param dirStr
	 */
	public static void deleteDir( String dirStr )
	{
		File curDir = new File( dirStr );
		if ( !curDir.exists( ) )
			return;
		File[] files = curDir.listFiles( );
		for ( int i = 0; i < files.length; i++ )
			files[i].delete( );
		File parentDir = curDir.getParentFile( );
		curDir.delete( );
		parentDir.delete( );
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


