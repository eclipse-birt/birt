/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Actuate Corporation
 * 
 */
public interface ISeriesUIProvider {

	/**
	 * Fetches the composite used to set attribute properties for a series.
	 * 
	 * @param parent container for the series attribute composite series the series
	 *               for which this attribute composite is being provided
	 * @return the series attribute composite
	 * @deprecated use getSeriesAttributeSheet( Composite parent, Series series,
	 *             IUIServiceProvider builder, Object oContext )
	 */
	public Composite getSeriesAttributeSheet(Composite parent, Series series);

	/**
	 * Fetches the composite used to set attribute properties for a series.
	 * 
	 * @param parent container for the series attribute composite series the series
	 *               for which this attribute composite is being provided
	 * @return the series attribute composite
	 * @since 2.1
	 */
	public Composite getSeriesAttributeSheet(Composite parent, Series series, ChartWizardContext context);

	/**
	 * Gets custom buttons to add after built-in buttons.
	 * 
	 * @param context wizard context
	 * @param sd      value series definition
	 * @since 2.6.2
	 */
	public List<ISeriesButtonEntry> getCustomButtons(ChartWizardContext context, SeriesDefinition sd);

	/**
	 * Fetches the composite used to set data properties for a series.
	 * 
	 * @param parent container for the series data composite series the series for
	 *               which this data composite is being provided
	 * @return the series data composite
	 * @deprecated
	 * @see #getSeriesDataComponent(int, SeriesDefinition, ChartWizardContext,
	 *      String)
	 */
	public Composite getSeriesDataSheet(Composite parent, SeriesDefinition seriesdefinition, IUIServiceProvider builder,
			Object oContext);

	/**
	 * Fetches the class of the series for which this class provides services
	 * 
	 * @return the fully qualified class name of the series class in the model.
	 *         (This class has to extend the
	 *         org.eclipse.birt.chart.model.component.Series interface.)
	 */
	public String getSeriesClass();

	public ISelectDataComponent getSeriesDataComponent(int seriesType, SeriesDefinition seriesDefn,
			ChartWizardContext context, String sTitle);

	/**
	 * Validate whether the series can contain the data type.
	 * 
	 * @param series
	 * @param idsp
	 * @since 2.2
	 */
	public void validateSeriesBindingType(Series series, IDataServiceProvider idsp) throws ChartException;

	/**
	 * Validate the aggregation type.
	 * 
	 * @param series
	 * @param orthSD
	 * @param baseSD
	 * @since 2.5
	 * @return true if valid
	 */
	public boolean isValidAggregationType(Series series, SeriesDefinition orthSD, SeriesDefinition baseSD);

	/**
	 * Get the compatible axis type according to series type.
	 * 
	 * @param series
	 * @return An array containing all possible axis types for the designated
	 *         series.
	 * @since 2.2
	 */
	public AxisType[] getCompatibleAxisType(Series series);

	/**
	 * 
	 * @param series
	 * @return An array containing the index of all data definitions not allowed to
	 *         be null.
	 * @deprecated to use {@link Series#getDefinedDataDefinitionIndex()} instead.
	 */
	public int[] validationIndex(Series series);
}