/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
public class CacheRequest {
	private int maxRow;
	private List fetchEvents;
	private SortSpec sortSpec;
	private IEventHandler eventHandler;

	private boolean distinctValueFlag;
	private long cacheSize;

	/**
	 * @param maxRow
	 * @param fetchEvents
	 * @param sortSpec
	 */
	public CacheRequest(int maxRow, List fetchEvents, SortSpec sortSpec, IEventHandler eventHandler) {
		this.maxRow = maxRow;
		this.fetchEvents = fetchEvents;
		this.sortSpec = sortSpec;
		this.eventHandler = eventHandler;
	}

	/**
	 * @param maxRow
	 * @param fetchEvents
	 * @param sortSpec
	 * @param eventHandler
	 * @param distinctValueFlag
	 */
	public CacheRequest(int maxRow, List fetchEvents, SortSpec sortSpec, IEventHandler eventHandler,
			boolean distinctValueFlag) {
		this(maxRow, fetchEvents, sortSpec, eventHandler);
		this.distinctValueFlag = distinctValueFlag;
	}

	public CacheRequest(int maxRow, List fetchEvents, SortSpec sortSpec, IEventHandler eventHandler,
			boolean distinctValueFlag, long cacheSize) {
		this(maxRow, fetchEvents, sortSpec, eventHandler);
		this.distinctValueFlag = distinctValueFlag;
		this.cacheSize = cacheSize;
	}

	/**
	 * @return
	 */
	public int getMaxRow() {
		return this.maxRow;
	}

	/**
	 * @return
	 */
	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}

	/**
	 * @return
	 */
	public List getFetchEvents() {
		return fetchEvents;
	}

	/**
	 * @return
	 */
	public SortSpec getSortSpec() {
		return sortSpec;
	}

	/**
	 * @return
	 */
	public IEventHandler getEventHandler() {
		return eventHandler;
	}

	/**
	 * @return
	 */
	public boolean getDistinctValueFlag() {
		return distinctValueFlag;
	}

	/**
	 * @return
	 */
	public void setDistinctValueFlag(boolean distinctValueFlag) {
		this.distinctValueFlag = distinctValueFlag;
	}

	public long getCacheSize() {
		return this.cacheSize;
	}
}
