/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public String getColumn() {
		return column;
	}

	public int getDisplayType() {
		return displayType;
	}

	public List<String> getFilterOperatorList() {
		return operators;
	}

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
			List<String> newList = new ArrayList<String>();
			for (String str : strList) {
				newList.add(str);
			}
			para.setFilterOperatorList(newList);
		}

		// localizedOperators
		strList = para.getFilterOperatorDisplayList();
		if (strList != null) {
			List<String> newList = new ArrayList<String>();
			for (String str : strList) {
				newList.add(str);
			}
			para.setFilterOperatorDisplayList(newList);
		}

		return para;
	}
}
