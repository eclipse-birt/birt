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

import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * 
 */
public class SmartCacheRequest {
	private int maxRow;
	private List eventList;
	private OdiAdapter odiAdpater;
	private IResultClass resultClass;
	private boolean distinctValueFlag;

	/**
	 * @param maxRow
	 * @param eventList
	 * @param odiAdpater
	 * @param resultClass
	 * @param distinctValueFlag
	 */
	public SmartCacheRequest(int maxRow, List eventList, OdiAdapter odiAdpater, IResultClass resultClass,
			boolean distinctValueFlag) {
		this.maxRow = maxRow;
		this.eventList = eventList;
		this.odiAdpater = odiAdpater;
		this.resultClass = resultClass;
		this.distinctValueFlag = distinctValueFlag;
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
	public List getEventList() {
		return eventList;
	}

	/**
	 * @return
	 */
	public OdiAdapter getOdiAdapter() {
		return odiAdpater;
	}

	/**
	 * @return
	 */
	public IResultClass getResultClass() {
		return resultClass;
	}

	/**
	 * @return
	 */
	public boolean getDistinctValueFlag() {
		return distinctValueFlag;
	}

}
