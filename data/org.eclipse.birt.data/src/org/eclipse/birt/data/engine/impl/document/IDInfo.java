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
package org.eclipse.birt.data.engine.impl.document;

/**
 * 
 */
public class IDInfo {
	// the id of query result, includes: rootQueryResultID and selfQueryResultID
	private String queryResultID;
	// the name of sub query
	private String subQueryName;
	// the group level
	private int groupLevel;
	// the index of sub query in its corresponding group level
	private int subQueryIndex;
	// the group information of its sub query
	private int[] subQueryInfo;

	/**
	 * @param queryResultID
	 */
	public IDInfo(String queryResultID) {
		this.queryResultID = queryResultID;
	}

	/**
	 * 
	 * @param queryResultID
	 * @param subQueryName
	 */
	public IDInfo(String queryResultID, String subQueryName) {
		this.queryResultID = queryResultID;
		this.subQueryName = subQueryName;
	}

	/**
	 * @param queryResultID
	 * @param subQueryName
	 * @param groupLevel
	 * @param subQueryIndex
	 * @param subQueryInfo
	 */
	public IDInfo(String queryResultID, String subQueryName, int groupLevel, int subQueryIndex, int[] subQueryInfo) {
		this.queryResultID = queryResultID;
		this.subQueryName = subQueryName;
		this.groupLevel = groupLevel;
		this.subQueryIndex = subQueryIndex;
		this.subQueryInfo = subQueryInfo;
	}

	/**
	 * @return
	 */
	public String getQueryResultID() {
		return this.queryResultID;
	}

	/**
	 * @return
	 */
	public String getsubQueryName() {
		return this.subQueryName;
	}

	/**
	 * @return
	 */
	public int getGroupLevel() {
		return this.groupLevel;
	}

	/**
	 * @return
	 */
	public int getsubQueryIndex() {
		return this.subQueryIndex;
	}

	/**
	 * @return
	 */
	public int[] getSubQueryInfo() {
		return this.subQueryInfo;
	}

	// ------------------------------------------------------

	public String buildSubQueryID(String parentQueryID) {
		// if self is a sub query
		if (subQueryName == null)
			return parentQueryID;
		else
			// support sub query of sub query
			return parentQueryID + "/" + QueryResultIDUtil.buildSubQueryID(this.subQueryName, this.subQueryIndex);
	}

	/**
	 * Generate sub query definition for such a sub query which is applied to each
	 * row of parent query.
	 * 
	 * @param count
	 * @return [0, 1, 1, 2, 2, 3...]
	 */
	public static int[] getSpecialSubQueryInfo(int count) {
		int[] subQueryInfo = new int[count * 2];
		for (int i = 0; i < count; i++) {
			subQueryInfo[2 * i] = i;
			subQueryInfo[2 * i + 1] = i + 1;
		}
		return subQueryInfo;
	}

}
