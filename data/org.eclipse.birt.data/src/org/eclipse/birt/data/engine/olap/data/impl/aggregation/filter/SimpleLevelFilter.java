
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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter;

import java.util.logging.Logger;

import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;

/**
 * 
 */

public class SimpleLevelFilter {
	private String dimensionName;
	private String levelName;
	private ISelection[] selections;

	private static Logger logger = Logger.getLogger(LevelFilter.class.getName());

	/**
	 * @param level
	 * @param selections
	 */
	public SimpleLevelFilter(DimLevel level, ISelection[] selections) {
		Object[] params = { level, selections };
		logger.entering(LevelFilter.class.getName(), "LevelFilter", params);
		this.dimensionName = level.getDimensionName();
		this.levelName = level.getLevelName();
		this.selections = selections;
		logger.exiting(LevelFilter.class.getName(), "LevelFilter");
	}

	/**
	 * 
	 * @return
	 */
	public String getDimensionName() {
		return dimensionName;
	}

	/**
	 * 
	 * @param dimensionName
	 */
	public void setDimensionName(String dimensionName) {
		this.dimensionName = dimensionName;
	}

	/**
	 * 
	 * @return
	 */
	public String getLevelName() {
		return levelName;
	}

	/**
	 * 
	 * @param levelName
	 */
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	/**
	 * 
	 * @return
	 */
	public ISelection[] getSelections() {
		return selections;
	}

	/**
	 * 
	 * @param selections
	 */
	public void setSelections(ISelection[] selections) {
		this.selections = selections;
	}

}
