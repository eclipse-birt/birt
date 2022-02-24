/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.data.adapter.api;

import org.eclipse.birt.data.engine.olap.data.api.DimLevel;

//
public class DimensionLevel implements IDimensionLevel {

	private String dimName;
	private String lvlName;
	private String attrName;

	/**
	 * 
	 * @param dimName
	 * @param levelName
	 */
	public DimensionLevel(String dimName, String levelName) {
		this.dimName = dimName;
		this.lvlName = levelName;
	}

	/**
	 * 
	 * @param dimName
	 * @param levelName
	 * @param attrName
	 */
	public DimensionLevel(String dimName, String levelName, String attrName) {
		this(dimName, levelName);
		this.attrName = attrName;
	}

	/**
	 * 
	 * @param dimLevel
	 */
	public DimensionLevel(DimLevel dimLevel) {
		this(dimLevel.getDimensionName(), dimLevel.getLevelName(), dimLevel.getAttrName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.data.adapter.api.IDimensionLevel#getAttributeName()
	 */
	public String getAttributeName() {
		return this.attrName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.data.adapter.api.IDimensionLevel#getDimensionName()
	 */
	public String getDimensionName() {
		return this.dimName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.adapter.api.IDimensionLevel#getLevelName()
	 */
	public String getLevelName() {
		return this.lvlName;
	}

}
