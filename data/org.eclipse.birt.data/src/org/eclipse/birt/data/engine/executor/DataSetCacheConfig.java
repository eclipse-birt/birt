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
package org.eclipse.birt.data.engine.executor;

public class DataSetCacheConfig
{
	private DataSetCacheMode cacheMode;
	private int cacheCapability;
	private boolean isIncremental;
	private String cacheDir;
	
	private DataSetCacheConfig( DataSetCacheMode cacheMode, int cacheCapability, boolean isIncremental, String cacheDir)
	{
		assert cacheMode != null && cacheCapability > 0; 
		this.cacheMode = cacheMode;
		this.cacheCapability = cacheCapability;
		this.isIncremental = isIncremental;
		this.cacheDir = cacheDir;
	}
	
	
	public static DataSetCacheConfig getInstacne(DataSetCacheMode cacheMode, int cacheCapability, String cacheDir)
	{
		return getInstance(cacheMode, cacheCapability, false, cacheDir);
	}
	
	public static DataSetCacheConfig getInstance(DataSetCacheMode cacheMode, int cacheCapability, boolean isIncremental, String cacheDir)
	{
		if (cacheMode == null || cacheCapability == 0)
		{
			return null;
		}
		if (cacheCapability > 0)
		{
			return new DataSetCacheConfig(cacheMode, cacheCapability, isIncremental, cacheDir);
		}
		else
		{
			return new DataSetCacheConfig(cacheMode, Integer.MAX_VALUE, isIncremental, cacheDir);
		}
	}

	
	int getCacheCapability( )
	{
		return cacheCapability;
	}
	
	String getCacheDir( )
	{
		return cacheDir;
	}
	
	IDataSetCacheObject createDataSetCacheObject()
	{
		if (cacheMode == DataSetCacheMode.IN_MEMORY)
		{
			return new MemoryDataSetCacheObject(cacheCapability);
		} 
		else if (cacheMode == DataSetCacheMode.IN_DISK)
		{
			if (isIncremental)
			{
				return new IncreDataSetCacheObject(cacheDir);
			}
			else
			{
				return new DiskDataSetCacheObject(cacheDir, cacheCapability);
			}
		}
		assert false;
		return null;
	}
	
	public static class DataSetCacheMode
	{
		private DataSetCacheMode(){};
		public static final DataSetCacheMode IN_MEMORY = new DataSetCacheMode();
		public static final DataSetCacheMode IN_DISK = new DataSetCacheMode();
	}
}
