/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.script.api;

import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.attribute.IText;
import org.eclipse.birt.chart.script.api.component.ICategory;
import org.eclipse.birt.chart.script.api.component.ILegend;
import org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem;

/**
 * Represents the design of a Chart in the scripting environment
 */

public interface IChart extends IMultiRowItem {

	/**
	 * Gets the description of Chart
	 * 
	 * @return description
	 */
	IText getDescription();

	/**
	 * Gets the title of Chart
	 * 
	 * @return title
	 */
	ILabel getTitle();

	/**
	 * Gets Legend in Chart model
	 * 
	 * @return Legend component
	 */
	ILegend getLegend();

	/**
	 * Gets the Category that represents category(X) series in Chart model
	 * 
	 * @return category series
	 */
	ICategory getCategory();

	/**
	 * Checks if color in value series is ordered by category
	 * 
	 * @return true by category, false by series
	 */
	boolean isColorByCategory();

	/**
	 * Sets the color in value series is ordered by category
	 * 
	 * @param byCategory true by category, false by series
	 */
	void setColorByCategory(boolean byCategory);

	/**
	 * Gets the output type of Chart
	 * 
	 * @return output type
	 */
	String getOutputType();

	/**
	 * Sets the output type of Chart. Supported output types include SVG, PNG, JPG,
	 * BMP and PDF. Default value is SVG.
	 * 
	 * @param type output type
	 */
	void setOutputType(String type);

	/**
	 * Gets the name of ChartDimension. Return values are an enumeration including
	 * "TwoDimensional", "TwoDimensionalWithDepth" and "ThreeDimensional". Default
	 * value is "TwoDimensional".
	 * 
	 * @return dimension name
	 * @see org.eclipse.birt.chart.model.attribute.ChartDimension
	 */
	String getDimension();

	/**
	 * Sets ChartDimension by dimension name. Dimension names are an enumeration
	 * including "TwoDimensional", "TwoDimensionalWithDepth" and "ThreeDimensional".
	 * Default value is "TwoDimensional". If dimension name is invalid, will set the
	 * default value.
	 * 
	 * @param dimensionName dimension name
	 * @see org.eclipse.birt.chart.model.attribute.ChartDimension
	 */
	void setDimension(String dimensionName);

	/**
	 * Gets factory to create simple API classes.
	 * 
	 * @return factory class
	 */
	IComponentFactory getFactory();

}
