/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import java.util.Collection;
import java.util.Hashtable;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.graphics.Image;

/**
 * IChartType
 */
public interface IChartType
{

	/**
	 * Type constant of 2D.
	 */
	public static final String TWO_DIMENSION_TYPE = Messages.getString( "DimensionType.2D" ); //$NON-NLS-1$

	/**
	 * Type constant of 2D with depth.
	 */
	public static final String TWO_DIMENSION_WITH_DEPTH_TYPE = Messages.getString( "DimensionType.2DWithDepth" ); //$NON-NLS-1$

	/**
	 * Type constant of 3D.
	 */
	public static final String THREE_DIMENSION_TYPE = Messages.getString( "DimensionType.3D" ); //$NON-NLS-1$

	/**
	 * Returns the name of the chart type.
	 * 
	 * @return Chart type name.
	 */
	public String getName( );
	
	/**
	 * Returns the display name of the chart type. This is what appears in the selection
	 * list in the Chart Selector UI.
	 *
	 * @return Chart type display name.
	 * @since Version 2.1
	 */
	public String getDisplayName( );

	/**
	 * Returns the image icon of the chart type. This is what appears in the
	 * selection list in the Chart Selector UI.
	 * 
	 * @return Chart image icon.
	 */
	public Image getImage( );

	/**
	 * Returns the names of the chart sub-types available for this type. These
	 * names are used to build the sub-type selection panel in the Chart
	 * Selector UI.
	 * 
	 * @return Array of sub-type names.
	 */
	public Collection getChartSubtypes( String Dimension,
			Orientation orientation );

	/**
	 * Returns whether this type implementation can process the specified model.
	 * The first instance that returns true will be considered as the correct
	 * chart type. If all types return false, the chart type and subtype from
	 * the model will be used.
	 * 
	 * @param cModel
	 *            chart model representing an existing chart
	 * @param htModelHints
	 *            pre-computed 'hints' from the model to reduce computations
	 *            needed to be performed by each implementation.
	 * @return true if this chart type can adapt the specified model to its own
	 *         type. false if it cannot.
	 */
	public boolean canAdapt( Chart cModel, Hashtable htModelHints );

	/**
	 * Returns the Chart model for given parameters.
	 * 
	 * @param sType
	 * @param Orientation
	 * @param Dimension
	 * @param currentChart
	 */
	public Chart getModel( String sType, Orientation Orientation,
			String Dimension, Chart currentChart );

	/**
	 * Returns the dimension array this chart type supports.
	 * 
	 */
	public String[] getSupportedDimensions( );

	/**
	 * Returns the default dimension of this chart type.
	 * 
	 */
	public String getDefaultDimension( );

	/**
	 * Returns if this chart type supports transposition.
	 * 
	 */
	public boolean supportsTransposition( );

	/**
	 * Returns if this chart type supports transposition for given dimension.
	 * 
	 * @since 2.0
	 * 
	 */
	public boolean supportsTransposition( String dimension );
	
	/**
	 * Returns the default orientation.
	 * 
	 * @since 2.2
	 */
	public Orientation getDefaultOrientation( );

	/**
	 * Returns the help information.
	 * 
	 */
	public IHelpContent getHelp( );
	
	/**
	 * Returns base UI in data sheet
	 * 
	 * @param chart
	 * @param selectDataUI
	 * @param context
	 * @param sTitle
	 * @return UI componnet
	 * @since 2.0
	 */
	public ISelectDataComponent getBaseUI( Chart chart,
			ISelectDataCustomizeUI selectDataUI, ChartWizardContext context, String sTitle );
	
	/**
	 * Checks whether the dimension is valid with specified number of axes or
	 * series.
	 * 
	 * @param dimensionType
	 *            dimension type
	 * @param nbOfAxes
	 *            number of axes
	 * @param nbOfSeries
	 *            number of series
	 * @return dimension is valid or invalid
	 * @since 2.1
	 */
	public boolean isDimensionSupported( String dimensionType, int nbOfAxes,
			int nbOfSeries );
}