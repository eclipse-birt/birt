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
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;

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
		int realCacheOption;

		if ( cacheOption == DataEngineContext.CACHE_USE_ALWAYS )
		{
			realCacheOption = DataSetCacheManager.ALWAYS;
		}
		else if ( cacheOption == DataEngineContext.CACHE_USE_DISABLE )
		{
			realCacheOption = DataSetCacheManager.DISABLE;
		}
		else if ( appContext != null )
		{
			Object option = appContext.get( DataEngine.DATASET_CACHE_OPTION );
			if ( option != null && option.toString( ).equals( "true" ) )
				realCacheOption = DataSetCacheManager.DEFAULT;
			else
				realCacheOption = DataSetCacheManager.DISABLE;
		}
		else
		{
			realCacheOption = DataSetCacheManager.DISABLE;
		}

		return realCacheOption;
	}
	
}
