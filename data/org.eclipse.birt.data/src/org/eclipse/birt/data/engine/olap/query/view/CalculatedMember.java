/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.query.view;

import org.eclipse.birt.data.engine.olap.util.CubeAggrDefn;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;

/**
 * A CalculatedMember is an Aggregation Object which need to be calculated in
 * olap.data
 * 
 */
public class CalculatedMember {

	private CubeAggrDefn aggrDefn;
	private int rsID;
	private IJSFacttableFilterEvalHelper filterEvalHelper;

	/**
	 * 
	 * @param aggrDefn
	 * @param rsID
	 */
	CalculatedMember(CubeAggrDefn aggrDefn, int rsID) {
		this.aggrDefn = aggrDefn;
		this.rsID = rsID;
	}

	/**
	 * 
	 * @param filterEvalHelper
	 */
	public void setFilterEvalHelper(IJSFacttableFilterEvalHelper filterEvalHelper) {
		this.filterEvalHelper = filterEvalHelper;
	}

	/**
	 * @return
	 */
	public IJSFacttableFilterEvalHelper getFilterEvalHelper() {
		return this.filterEvalHelper;
	}

	public CubeAggrDefn getCubeAggrDefn() {
		return aggrDefn;
	}

	/**
	 * 
	 * @return
	 */
	int getRsID() {
		return this.rsID;
	}
}
