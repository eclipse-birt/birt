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

package org.eclipse.birt.chart.ui.swt;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartSubType;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.graphics.Image;

/**
 * DefaultChartTypeImpl
 */
public class DefaultChartTypeImpl implements IChartType
{

	protected String chartTitle = ""; //$NON-NLS-1$
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getName()
	 */
	public String getName( )
	{
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getImage()
	 */
	public Image getImage( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(java.lang.String,
	 *      org.eclipse.birt.chart.model.attribute.Orientation)
	 */
	public Collection<IChartSubType> getChartSubtypes( String Dimension,
			Orientation orientation )
	{
		return new Vector<IChartSubType>( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#canAdapt(org.eclipse.birt.chart.model.Chart,
	 *      java.util.Hashtable)
	 */
	public boolean canAdapt( Chart cModel, Hashtable htModelHints )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getModel(java.lang.String,
	 *      org.eclipse.birt.chart.model.attribute.Orientation,
	 *      java.lang.String, org.eclipse.birt.chart.model.Chart)
	 */
	public Chart getModel( String sType, Orientation Orientation,
			String Dimension, Chart currentChart )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSupportedDimensions()
	 */
	public String[] getSupportedDimensions( )
	{
		return new String[]{
			TWO_DIMENSION_TYPE
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getDefaultDimension()
	 */
	public String getDefaultDimension( )
	{
		return TWO_DIMENSION_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition()
	 */
	public boolean supportsTransposition( )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition(java.lang.String)
	 */
	public boolean supportsTransposition( String dimension )
	{
		return supportsTransposition( );
	}

	public Orientation getDefaultOrientation( )
	{
		return Orientation.VERTICAL_LITERAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getHelp()
	 */
	public IHelpContent getHelp( )
	{
		return new HelpContentImpl( "{Title}", "{Description}" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public ISelectDataComponent getBaseUI( Chart chart,
			ISelectDataCustomizeUI selectDataUI, ChartWizardContext context,
			String sTitle )
	{
		return new DefaultSelectDataComponent( );
	}

	public boolean isDimensionSupported( String dimensionType,
			ChartWizardContext context, int nbOfAxes, int nbOfSeries )
	{
		boolean isSupported = false;

		// Check whether general dimension types include specified type
		String[] supportedDimensions = getSupportedDimensions( );
		for ( int i = 0; i < supportedDimensions.length; i++ )
		{
			if ( supportedDimensions[i].equals( dimensionType ) )
			{
				isSupported = true;
				break;
			}
		}

		if ( isSupported && THREE_DIMENSION_TYPE.equals( dimensionType ) )
		{
			if ( context.getDataServiceProvider( )
					.checkState( IDataServiceProvider.PART_CHART ) )
			{
				// Not support 3D in xtab
				return false;
			}
			isSupported = nbOfAxes <= 1;
		}

		return isSupported;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSeries()
	 */
	public Series getSeries( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Make the series the same type as the other one
	 * 
	 * @param series
	 * @param seriesIndex
	 * @param firtsSeries
	 * @return converted series
	 * @since 2.3
	 */
	protected Series getConvertedSeriesAsFirst( Series series, int seriesIndex,
			Series firstSeries )
	{
		// Do not convert base series
		if ( series.getClass( ).getName( ).equals( SeriesImpl.class.getName( ) ) )
		{
			return series;
		}

		Series tmpseries = ChartCacheManager.getInstance( )
				.findSeries( firstSeries.getClass( ).getName( ), seriesIndex );
		if ( tmpseries == null )
		{
			tmpseries = firstSeries.copyInstance( );
		}

		// Copy generic series properties
		ChartUIUtil.copyGeneralSeriesAttributes( series, tmpseries );

		if ( firstSeries instanceof BarSeriesImpl )
		{
			( (BarSeriesImpl) tmpseries ).setRiser( ( (BarSeriesImpl) firstSeries ).getRiser( ) );
		}

		return tmpseries;
	}

	public boolean canCombine( )
	{
		return false;
	}

	public String getDefaultTitle( )
	{
		return chartTitle;
	}
}