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
