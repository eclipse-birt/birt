
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

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;


/**
 * The data set cache object which serve for disk based data set cache.
 */
public class DiskDataSetCacheObject implements IDataSetCacheObject
{
	//
	private String tempFolder;

	/**
	 * 
	 * @param tempFolder
	 * @param appContext 
	 * @param parameterHints 
	 * @param baseDataSetDesign 
	 * @param baseDataSourceDesign 
	 */
	public DiskDataSetCacheObject( String tempFolder,
			IBaseDataSourceDesign baseDataSourceDesign,
			IBaseDataSetDesign baseDataSetDesign, Collection parameterHints,
			Map appContext )
	{
		this.tempFolder = tempFolder;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTempDir()
	{
		return tempFolder;
	}
	
	/**
	 * 
	 * @return
	 */
	public File getDataFile()
	{
		return new File( tempFolder + File.separator + "data.data");
	}
	
	/**
	 * 
	 * @return
	 */
	public File getMetaFile()
	{
		return new File( tempFolder + File.separator + "meta.data");
	}
}
