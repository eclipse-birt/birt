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
package org.eclipse.birt.data.engine.executor.cache;

import java.util.List;

import org.eclipse.birt.data.engine.odi.IEventHandler;

/**
 * Wrap the data which is used for SmartCache
 */
public class CacheRequest
{
	private int maxRow;
	private List fetchEvents;
	private SortSpec sortSpec;
	private IEventHandler eventHandler;

	/**
	 * @param maxRow
	 * @param fetchEvents
	 * @param sortSpec
	 */
	public CacheRequest( int maxRow, List fetchEvents, SortSpec sortSpec,
			IEventHandler eventHandler )
	{
		this.maxRow = maxRow;
		this.fetchEvents = fetchEvents;
		this.sortSpec = sortSpec;
		this.eventHandler = eventHandler;
	}

	/**
	 * @return
	 */
	public int getMaxRow( )
	{
		return this.maxRow;
	}
	
	/**
	 * @return
	 */
	public void setMaxRow( int maxRow )
	{
		this.maxRow = maxRow;
	}
	
	/**
	 * @return
	 */
	public List getFetchEvents( )
	{
		return fetchEvents;
	}
	
	/**
	 * @return
	 */
	public SortSpec getSortSpec( )
	{
		return sortSpec;
	}
	
	/**
	 * @return
	 */
	public IEventHandler getEventHandler( )
	{
		return eventHandler;
	}
	
}
