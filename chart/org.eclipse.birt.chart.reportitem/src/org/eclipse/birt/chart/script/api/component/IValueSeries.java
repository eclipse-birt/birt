/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script.api.component;

import org.eclipse.birt.chart.script.api.series.data.ISeriesData;
import org.eclipse.birt.report.model.api.simpleapi.IAction;

/**
 * Represents the value(Y) Series of a Chart in the scripting environment
 */

public interface IValueSeries {

	/**
	 * Gets query expression in the Series
	 * 
	 * @return query expression object
	 */
	ISeriesData getDataExpr();

	/**
	 * Checks if current series is visible
	 * 
	 * @return visible or not
	 */
	boolean isVisible();

	/**
	 * Sets if current series is visible
	 * 
	 * @param visible
	 */
	void setVisible(boolean visible);

	/**
	 * Gets the title of series
	 * 
	 * @return title string
	 */
	String getTitle();

	/**
	 * Sets the title of series
	 * 
	 * @param title title string
	 */
	void setTitle(String title);

	/**
	 * Gets aggregate expression in value series. If value series doesn't bind
	 * aggregate expression, get the expression from the category series. If
	 * category series doesn't bind as well, return blank string.
	 * 
	 * @return aggregate expression or blank expression if not found
	 */
	String getAggregateExpr();

	/**
	 * Sets aggregate expression in value series. Unsets the aggregate expression by
	 * setting null or sets the default from category series by setting blank
	 * string.
	 * 
	 * @param aggregateExpr aggregate expression
	 */
	void setAggregateExpr(String aggregateExpr);

	/**
	 * Gets Action for URL redirect of interactivity event in value series, or null
	 * when there's no URL redirect specified in interactivity events
	 * 
	 * @return Action
	 */
	IAction getAction();

	/**
	 * Checks if value is displayed as percentage
	 * 
	 * @return
	 */
	boolean isPercent();

	/**
	 * Sets if value is displayed as percentage
	 * 
	 * @param percent
	 */
	void setPercent(boolean percent);
}
