/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.views.provider;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.extensions.ReportItemViewAdapter;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Provider for creating Chart view in multi-view
 */
public class ChartReportItemViewProvider extends ReportItemViewAdapter
{

	protected static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem.ui" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.ReportItemViewAdapter#createView(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	public DesignElementHandle createView( DesignElementHandle host )
			throws BirtException
	{
		// Create chart
		ChartWithAxes cm = ChartWithAxesImpl.createDefault( );
		cm.setType( "Bar Chart" );//$NON-NLS-1$
		cm.setSubType( "Side-by-side" );//$NON-NLS-1$
		
		// Add base series
		SeriesDefinition sdBase = SeriesDefinitionImpl.createDefault( );
		Series series = SeriesImpl.createDefault( );
		sdBase.getSeries( ).add( series );
		cm.getBaseAxes( )[0].getSeriesDefinitions( ).add( sdBase );

		// Add orthogonal series
		SeriesDefinition sdOrth = SeriesDefinitionImpl.createDefault( );
		series = BarSeriesImpl.createDefault( );
		sdOrth.getSeries( ).add( series );
		cm.getOrthogonalAxes( cm.getBaseAxes( )[0], true )[0].getSeriesDefinitions( )
				.add( sdOrth );
		ChartUIUtil.setSeriesName( cm );

		// Add sample data
		SampleData sampleData = DataFactory.eINSTANCE.createSampleData( );
		sampleData.getBaseSampleData( ).clear( );
		sampleData.getOrthogonalSampleData( ).clear( );
		// Create Base Sample Data
		BaseSampleData sampleDataBase = DataFactory.eINSTANCE.createBaseSampleData( );
		sampleDataBase.setDataSetRepresentation( "A, B, C" ); //$NON-NLS-1$
		sampleData.getBaseSampleData( ).add( sampleDataBase );
		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData sampleDataOrth = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		sampleDataOrth.setDataSetRepresentation( "5,4,12" ); //$NON-NLS-1$
		sampleDataOrth.setSeriesDefinitionIndex( 0 );
		sampleData.getOrthogonalSampleData( ).add( sampleDataOrth );
		cm.setSampleData( sampleData );

		// Create a new item handle.
		String name = ReportPlugin.getDefault( )
				.getCustomName( ChartReportItemConstants.CHART_EXTENSION_NAME );
		ExtendedItemHandle itemHandle = host.getElementFactory( )
				.newExtendedItem( name,
						ChartReportItemConstants.CHART_EXTENSION_NAME );

		itemHandle.getReportItem( )
				.setProperty( ChartReportItemConstants.PROPERTY_CHART, cm );

		return itemHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemViewProvider#getViewName()
	 */
	public String getViewName( )
	{
		return Messages.getString("ChartReportItemViewProvider.ChartViewName"); //$NON-NLS-1$
	}

}
