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
 * Wrap the ID infromation of a query result.
 */
public class QueryResultInfo {
	private String rootQueryResultID;
	private String parentQueryResultID;

	private String queryResultID;
	private String subQueryName;
	private int index;

	/**
	 * The index parameter has different meaning in write/read. In writing, it means
	 * the sub query index, but in reading, it means the parent row index.
	 * 
	 * @param rootQueryResultID
	 * @param parentQueryResultID
	 * @param selfQueryResultID
	 * @param subQueryName
	 * @param index
	 */
	public QueryResultInfo(String rootQueryResultID, String parentQueryResultID, String selfQueryResultID,
			String subQueryName, int index) {
		this(selfQueryResultID, subQueryName, index);
		this.rootQueryResultID = rootQueryResultID;
		this.parentQueryResultID = parentQueryResultID;
	}

	/**
	 * @param queryResultID
	 * @param subQueryName
	 * @param subQueryIndex
	 */
	public QueryResultInfo(String queryResultID, String subQueryName, int index) {
		this.queryResultID = queryResultID;
		this.subQueryName = subQueryName;
		this.index = index;
	}

	/**
	 * @return
	 */
	public String getRootQueryResultID() {
		return this.rootQueryResultID;
	}

	/**
	 * @return
	 */
	public String getParentQueryResultID() {
		return this.parentQueryResultID;
	}

	/**
	 * @return
	 */
	public String getSelfQueryResultID() {
		return this.queryResultID;
	}

	/**
	 * @return
	 */
	public String getSubQueryName() {
		return this.subQueryName;
	}

	/**
	 * @return
	 */
	public int getIndex() {
		return this.index;
	}

}
