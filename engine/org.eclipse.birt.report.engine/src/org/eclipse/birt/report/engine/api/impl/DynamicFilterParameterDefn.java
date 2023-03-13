/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.api.IDynamicFilterParameterDefn;

public class DynamicFilterParameterDefn extends ParameterDefn implements IDynamicFilterParameterDefn {

	private String column;
	private int displayType;
	private List<String> operators;
	private List<String> localizedOperators;

	@Override
	public String getColumn() {
		return column;
	}

	@Override
	public int getDisplayType() {
		return displayType;
	}

	@Override
	public List<String> getFilterOperatorList() {
		return operators;
	}

	@Override
	public List<String> getFilterOperatorDisplayList() {
		return localizedOperators;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public void setDisplayType(int display) {
		this.displayType = display;
	}

	public void setFilterOperatorList(List<String> operators) {
		this.operators = operators;
	}

	public void setFilterOperatorDisplayList(List<String> operators) {
		this.localizedOperators = operators;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Object newObj = super.clone();
		DynamicFilterParameterDefn para = (DynamicFilterParameterDefn) newObj;
		// selectionList
		ArrayList list = para.getSelectionList();
		if (list != null) {
			ArrayList newList = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				ParameterSelectionChoice select = (ParameterSelectionChoice) list.get(i);
				newList.add(select.clone());
			}
			para.setSelectionList(newList);
		}

		// operators
		List<String> strList = para.getFilterOperatorList();
		if (strList != null) {
			List<String> newList = new ArrayList<>();
			for (String str : strList) {
				newList.add(str);
			}
			para.setFilterOperatorList(newList);
		}

		// localizedOperators
		strList = para.getFilterOperatorDisplayList();
		if (strList != null) {
			List<String> newList = new ArrayList<>();
			for (String str : strList) {
				newList.add(str);
			}
			para.setFilterOperatorDisplayList(newList);
		}

		return para;
	}
}
