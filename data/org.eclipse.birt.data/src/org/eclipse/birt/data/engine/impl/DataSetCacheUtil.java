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

import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;

/**
 * 
 */
class DataSetCacheUtil
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
}
