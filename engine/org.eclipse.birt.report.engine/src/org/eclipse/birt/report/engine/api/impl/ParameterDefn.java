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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.api.IParameterDefn;

/**
 * Created on Oct 26, 2004 Base class for defining parameters.
 */

public class ParameterDefn extends ParameterDefnBase implements IParameterDefn {

	protected boolean isHidden;

	protected boolean isRequired;

	protected int dataType;

	protected int selectionListType;

	protected ArrayList selectionList;

	/**
	 * @param isHidden
	 */
	public void setIsHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public boolean isRequired() {
		return isRequired;
	}

	/**
	 * @param isRequired
	 */
	public void setIsRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#getDataType()
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * @param dataType
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api2.IScalarParameterDefn#getSelectionList ()
	 */
	public ArrayList getSelectionList() {
		return selectionList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api2.IScalarParameterDefn#getSelectionListType
	 * ()
	 */
	public int getSelectionListType() {
		return selectionListType;
	}

	/**
	 * @param selectionListType The selectionListType to set.
	 */
	public void setSelectionListType(int selectionListType) {
		this.selectionListType = selectionListType;
	}

	/**
	 * @param paramSelectionList The paramSelectionList to set.
	 */
	public void setSelectionList(ArrayList paramSelectionList) {
		this.selectionList = paramSelectionList;
	}
}
