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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;

/**
 * 
 */
public class DataSetCacheUtil
{
	/**
	 * @param context
	 * @param appContext
	 * @return
	 */
	public static int getCacheOption( DataEngineContext context, Map appContext )
	{
		int cacheOption = context.getCacheOption( );
		
		if ( appContext != null )
		{
	
			Object option = appContext.get( DataEngine.MEMORY_DATA_SET_CACHE );
			if( option != null )
			{
				int rowLimit = getIntValueFromString(option);
				if( rowLimit > 0 )
					return DataEngineContext.CACHE_USE_ALWAYS;
			}
			
			option = appContext.get( DataEngine.DATA_SET_CACHE_ROW_LIMIT );
			if( option != null )
			{
				int rowLimit = getIntValueFromString(option);
				if( rowLimit == 0 )
				{
					return DataEngineContext.CACHE_USE_DISABLE;
				}
				else
				{
					return DataEngineContext.CACHE_USE_ALWAYS;
				}
			}
			
		}
		
		int realCacheOption;
		
		if ( cacheOption == DataEngineContext.CACHE_USE_ALWAYS )
		{
			realCacheOption = DataEngineContext.CACHE_USE_ALWAYS;
		}
		else if ( cacheOption == DataEngineContext.CACHE_USE_DISABLE )
		{
			realCacheOption = DataEngineContext.CACHE_USE_DISABLE;
		}
		else if ( appContext != null )
		{
			Object option = appContext.get( DataEngine.DATASET_CACHE_OPTION );
			if ( option != null && option.toString( ).equals( "true" ) )
				realCacheOption = DataEngineContext.CACHE_USE_DEFAULT;
			else
				realCacheOption = DataEngineContext.CACHE_USE_DISABLE;
		}
		else
		{
			realCacheOption = DataEngineContext.CACHE_USE_DISABLE;
		}

		return realCacheOption;
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
	
	/**
	 * 
	 * @param context
	 * @param appContext
	 * @return 
	 */
	public static int getCacheCount( DataEngineContext context, Map appContext )
	{
		if ( appContext != null )
		{
			Object option = appContext.get( DataEngine.MEMORY_DATA_SET_CACHE );
			if( option != null )
			{
				int rowLimit = getIntValueFromString(option);
				if( rowLimit > 0 )
					return rowLimit;
			}
			option = appContext.get( DataEngine.DATA_SET_CACHE_ROW_LIMIT );
			if( option != null )
			{
				return getIntValueFromString(option);
			}
		}
		
		return context.getCacheCount();
	}
	
	/**
	 * 
	 * @param appContext
	 * @return
	 */
	public static int getCacheMode( Map appContext )
	{	
		if ( appContext != null )
		{
			Object option = appContext.get( DataEngine.MEMORY_DATA_SET_CACHE );
			if( option != null )
			{
				int rowLimit = getIntValueFromString(option);
				if( rowLimit > 0 )
					return DataEngineContext.CACHE_MODE_IN_MEMORY;
			}
		}
		return DataEngineContext.CACHE_MODE_IN_DISK;
	}
	
	/**
	 * the specified configure value can be a path or an URL object represents
	 * the location of the configure file, but the final returned value must be
	 * an URL object or null if fails to parse it.
	 * 
	 * @param appContext
	 * @return
	 */
	public static URL getCacheConfig( Map appContext )
	{
		if ( appContext != null )
		{
			Object configValue = appContext.get( DataEngine.INCREMENTAL_CACHE_CONFIG );
			URL url = null;
			if ( configValue instanceof URL )
			{
				url = (URL) configValue;
			}
			else if ( configValue instanceof String )
			{
				String configPath = configValue.toString( );
				try
				{
					url = new URL( configPath );
				}
				catch ( MalformedURLException e )
				{
					try
					{// try to use file protocol to parse configPath
						url = new URL( "file", "/", configPath );
					}
					catch ( MalformedURLException e1 )
					{
						return null;
					}
				}
			}
			return url;
		}
		return null;
	}
	
	/**
	 * 
	 * @param dataSetDesign
	 * @param cacheOption
	 * @param alwaysCacheRowCount
	 * @return
	 */
	public static boolean needsToCache( IBaseDataSetDesign dataSetDesign,
			int cacheOption, int alwaysCacheRowCount )
	{
		if ( dataSetDesign == null )
			return false;

		if ( dataSetDesign instanceof IIncreCacheDataSetDesign )
		{
			return true;
		}
		if ( cacheOption == DataEngineContext.CACHE_USE_DISABLE )
		{
			return false;
		}
		else if ( cacheOption == DataEngineContext.CACHE_USE_ALWAYS )
		{
			if ( alwaysCacheRowCount == 0 )
				return false;
		}
		else if ( dataSetDesign.getCacheRowCount( ) == 0 )
		{
			return false;
		}

		return true;
	}
	
	/**
	 * 
	 * @param cacheOption
	 * @param alwaysCacheRowCount
	 * @param cacheRowCount
	 * @return
	 */
	public static int getCacheRowCount( int cacheOption,
			int alwaysCacheRowCount, int cacheRowCount )
	{
		if ( cacheOption == DataEngineContext.CACHE_USE_ALWAYS )
		{
			if ( alwaysCacheRowCount <= 0 )
				return Integer.MAX_VALUE;
			else
				return alwaysCacheRowCount;
		}
		else if ( cacheOption == DataEngineContext.CACHE_USE_DISABLE )
		{
			return Integer.MAX_VALUE;
		}
		else
		{
			if ( cacheRowCount == -1 )
				return Integer.MAX_VALUE;
			else
				return cacheRowCount;
		}
	}
}
