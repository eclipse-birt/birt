/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter;

import java.util.logging.Logger;

import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper;

/**
 * 
 */
public class LevelFilter {

	private String dimensionName;
	private String levelName;
	private ISelection[] selections;
	private Member[] dimMembers;
	private IJSFilterHelper filterHelper;

	private static Logger logger = Logger.getLogger(LevelFilter.class.getName());

	/**
	 * @param level
	 * @param selections
	 */
	public LevelFilter(DimLevel level, ISelection[] selections) {
		Object[] params = { level, selections };
		logger.entering(LevelFilter.class.getName(), "LevelFilter", params);
		this.dimensionName = level.getDimensionName();
		this.levelName = level.getLevelName();
		this.selections = selections;
		logger.exiting(LevelFilter.class.getName(), "LevelFilter");
	}

	/**
	 * @param dimensionName
	 * @param levelName
	 * @param selections
	 */
	public LevelFilter(String dimensionName, String levelName, ISelection[] selections) {
		Object[] params = { dimensionName, levelName, selections };
		logger.entering(LevelFilter.class.getName(), "LevelFilter", params);
		this.dimensionName = dimensionName;
		this.levelName = levelName;
		this.selections = selections;
		logger.exiting(LevelFilter.class.getName(), "LevelFilter");
	}

	/**
	 * @return the dimensionName
	 */
	public String getDimensionName() {
		return dimensionName;
	}

	/**
	 * @param dimensionName the dimensionName to set
	 */
	public void setDimensionName(String dimensionName) {
		this.dimensionName = dimensionName;
	}

	/**
	 * @return the levelName
	 */
	public String getLevelName() {
		return levelName;
	}

	/**
	 * @param levelName the levelName to set
	 */
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	/**
	 * @return the selections
	 */
	public ISelection[] getSelections() {
		return selections;
	}

	/**
	 * @param selections the selections to set
	 */
	public void setSelections(ISelection[] selections) {
		this.selections = selections;
	}

	/**
	 * @return the dimensionMembers
	 */
	public Member[] getDimMembers() {
		return dimMembers;
	}

	/**
	 * @param dimensionMembers the dimensionMembers to set
	 */
	public void setDimMembers(Member[] dimensionMembers) {
		this.dimMembers = dimensionMembers;
	}

	/**
	 * @return the filterHelper
	 */
	public IJSFilterHelper getFilterHelper() {
		return filterHelper;
	}

	/**
	 * @param filterHelper the filterHelper to set
	 */
	public void setFilterHelper(IJSFilterHelper filterHelper) {
		this.filterHelper = filterHelper;
	}
}
