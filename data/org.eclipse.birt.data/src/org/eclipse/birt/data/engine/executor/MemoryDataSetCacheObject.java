
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

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * The data set cache object which serve for Memory based data set cache.
 */
public class MemoryDataSetCacheObject implements IDataSetCacheObject
{
	private IResultClass rs;
	private SoftReference softCachedResult;
	
	public MemoryDataSetCacheObject( )
	{
		this.softCachedResult = new SoftReference( new ArrayList());
	}

	private List getCachedResult( )
	{
		if ( this.softCachedResult.get( ) == null )
		{
			this.softCachedResult = new SoftReference( new ArrayList());
		}
		return (List)this.softCachedResult.get( );
	}
	
	public int getSize()
	{
		return this.getCachedResult( ).size( );
	}
	
	public IResultClass getResultClass( )
	{
		return this.rs;
	}
	
	public IResultObject getResultObject( int index )
	{
		return (IResultObject)this.getCachedResult( ).get( index );
	}
	
	public boolean needPopulateResult( )
	{
		return this.getCachedResult( ).size( ) == 0;
	}
	
	public void setResultClass( IResultClass rs )
	{
		this.rs = rs;
	}
	
	public void populateResult( IResultObject ro )
	{
		if ( ro != null )
			this.getCachedResult( ).add( ro );
	}
}
