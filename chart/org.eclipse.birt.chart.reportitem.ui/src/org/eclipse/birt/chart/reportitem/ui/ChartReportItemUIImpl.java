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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportElementFigure;
import org.eclipse.birt.report.designer.ui.extensions.ReportItemFigureProvider;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * 
 */
public class ChartReportItemUIImpl extends ReportItemFigureProvider
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * This map is used to keep one listener instance for each handle. So the
	 * listener will be added only once in handle
	 */
	private static Map<DesignElementHandle, Listener> listenerMap = new HashMap<DesignElementHandle, Listener>( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemUI#getFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public final IFigure createFigure( final ExtendedItemHandle eih )
	{
		try
		{
			eih.loadExtendedElement( );
		}
		catch ( ExtendedElementException eeex )
		{
			logger.log( eeex );
		}
		try
		{
			final ChartReportItemImpl iri = (ChartReportItemImpl) eih.getReportItem( );
			final DesignerRepresentation dr = ChartReportItemUIFactory.instance( ).createFigure( iri );
			refreshBackgroundImage( eih, dr );
			iri.setDesignerRepresentation( dr ); // UPDATE LINK

			// Update the hostChart reference once plot chart is copied
			if ( iri.isCopied( ) && ChartCubeUtil.isPlotChart( eih ) )
			{
				ChartWithAxes cwa = (ChartWithAxes) iri.getProperty( ChartReportItemConstants.PROPERTY_CHART );
				Axis yAxis = cwa.getAxes( )
						.get( 0 )
						.getAssociatedAxes( )
						.get( 0 );
				if ( yAxis.getLineAttributes( ).isVisible( )
						&& ChartCubeUtil.findReferenceChart( eih ) == null )
				{
					// Only update axis chart when axis is visible
					AggregationCellHandle containerCell = ChartCubeUtil.getXtabContainerCell( eih );
					AggregationCellHandle grandTotalCell = ChartXTabUIUtil.getGrandTotalAggregationCell( containerCell,
							cwa.isTransposed( ) );
					Object content = ChartCubeUtil.getFirstContent( grandTotalCell );
					if ( ChartCubeUtil.isAxisChart( (DesignElementHandle) content ) )
					{
						final ExtendedItemHandle axisChart = (ExtendedItemHandle) content;
						final DesignElementHandle oldPlotChart = axisChart.getElementProperty( ChartReportItemConstants.PROPERTY_HOST_CHART );
						// If old plot chart is deleted or not same with
						// hostChart instance, try to update it here.
						if ( oldPlotChart == null || !oldPlotChart.equals( eih ) )
							// Update the handle property in async process
							Display.getCurrent( ).asyncExec( new Runnable( ) {

								public void run( )
								{
									try
									{
										axisChart.setProperty( ChartReportItemConstants.PROPERTY_HOST_CHART,
												eih );
										// Reset listener since this process is deferred after adding listener.
										addDeleteListenerToHostChart( eih, listenerMap.get( axisChart ) );
									}
									catch ( SemanticException e )
									{
										logger.log( e );
									}
								}
							} );
					}
				}
			}
			else if ( ChartCubeUtil.isAxisChart( eih ) )
			{
				DesignElementHandle hostChart = eih.getElementProperty( ChartReportItemConstants.PROPERTY_HOST_CHART );
				if ( hostChart != null )
				{
					Listener listener = createDeleteChartListener( eih );
					addDeleteListenerToHostChart( hostChart, listener );
				}
			}

			return dr;
		}
		catch ( BirtException e )
		{
			logger.log( e );
			return null;
		}
	}
	
	private void addDeleteListenerToHostChart( DesignElementHandle hostChart,
			Listener listener )
	{
		// Add listener to container to listen when chart is deleted
		DesignElementHandle cell = hostChart.getContainer( );
		cell.addListener( listener );
		// Add listener to container's container to listen when
		// container is deleted
		if ( cell.getContainer( ) != null )
		{
			cell.getContainer( ).addListener( listener );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemUI#updateFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle,
	 *      org.eclipse.draw2d.IFigure)
	 */
	public final void updateFigure( ExtendedItemHandle eih, IFigure ifg )
	{
		try
		{
			eih.loadExtendedElement( );
			// UPDATE THE MODEL
			eih.getReportItem( ).setHandle( eih );
			
			refreshBackgroundImage( eih, (ReportElementFigure) ifg );
			
			// USE THE SWT DISPLAY SERVER TO CONVERT POINTS TO PIXELS
			final IDisplayServer idsSWT = ChartUIUtil.getDisplayServer( );
			final int dpi = idsSWT.getDpiResolution( );
			Bounds bounds = ChartItemUtil.computeChartBounds( eih, dpi );
			if ( bounds == null )
			{
				return;
			}
			
			final double dHeightInPixels = ( dpi * bounds.getHeight( ) ) / 72d;
			final double dWidthInPixels = ( dpi * bounds.getWidth( ) ) / 72d;			

			// UPDATE THE FIGURE
			( (DesignerRepresentation) ifg ).setDirty( true );
			
			Dimension newSize = ifg.getBounds( ).getCopy( ).getSize( );
			if ( dWidthInPixels >= 0 )
			{
				newSize.width = (int) dWidthInPixels;
			}
			if ( dHeightInPixels >= 0 )
			{
				newSize.height = (int) dHeightInPixels;
			}
			ifg.setSize( newSize );
		}
		catch ( BirtException ex )
		{
			logger.log( ex );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemUI#disposeFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle,
	 *      org.eclipse.draw2d.IFigure)
	 */
	public final void disposeFigure( ExtendedItemHandle eih, IFigure ifg )
	{
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemUIImpl.log.ReceivedNotification" ) ); //$NON-NLS-1$
		( (DesignerRepresentation) ifg ).dispose( );
		listenerMap.remove( eih );
	}

	private Listener createDeleteChartListener(
			final DesignElementHandle handleTarget )
	{
		if ( listenerMap.containsKey( handleTarget ) )
		{
			// Keep only one listener instance per handle
			return listenerMap.get( handleTarget );
		}

		Listener listener = new Listener( ) {

			public void elementChanged( DesignElementHandle focus,
					NotificationEvent ev )
			{
				if ( ev instanceof ContentEvent )
				{
					ContentEvent cv = (ContentEvent) ev;
					if ( cv.getAction( ) == ContentEvent.REMOVE )
					{
						DesignElementHandle handleSource = handleTarget.getElementProperty( ChartReportItemConstants.PROPERTY_HOST_CHART );
						// If the plot chart is null or it's the one to delete
						if ( handleSource == null
								|| cv.getContent( ) == handleSource.getElement( ) )
						{
							try
							{
								if ( handleTarget.getRoot( ) != null )
								{
									handleTarget.dropAndClear( );
								}
							}
							catch ( SemanticException e )
							{
								logger.log( e );
							}
						}
					}
				}
			}

		};
		listenerMap.put( handleTarget, listener );
		return listener;
	}

	/*
	 * Refresh Background: Color, Image, Repeat, PositionX, PositionY.
	 * 
	 */
	private void refreshBackgroundImage( DesignElementHandle handle, ReportElementFigure figure )
	{
		String backGroundImage = ChartReportItemUIUtil.getBackgroundImage( handle );

		if ( backGroundImage == null )
		{
			figure.setImage( null );
		}
		else
		{
			Image image = null;
			try
			{
				image = ImageManager.getInstance( )
						.getImage( handle.getModuleHandle( ),
								backGroundImage );
			}
			catch ( SWTException e )
			{
				// Should not be ExceptionHandler.handle(e), see SCR#73730
				image = null;
			}

			if ( image == null )
			{
				figure.setImage( null );
				return;
			}

			figure.setImage( image );

			Object[] backGroundPosition = ChartReportItemUIUtil.getBackgroundPosition( handle );
			int backGroundRepeat = ChartReportItemUIUtil.getBackgroundRepeat( handle );

			figure.setRepeat( backGroundRepeat );

			Object xPosition = backGroundPosition[0];
			Object yPosition = backGroundPosition[1];
			Rectangle area = figure.getClientArea( );
			org.eclipse.swt.graphics.Rectangle imageArea = image.getBounds( );
			Point position = new Point( -1, -1 );
			int alignment = 0;

			if ( xPosition instanceof Integer )
			{
				position.x = ( (Integer) xPosition ).intValue( );
			}
			else if ( xPosition instanceof DimensionValue )
			{
				int percentX = (int) ( (DimensionValue) xPosition ).getMeasure( );

				position.x = ( area.width - imageArea.width ) * percentX / 100;
			}
			else if ( xPosition instanceof String )
			{
				alignment |= DesignElementHandleAdapter.getPosition( (String) xPosition );
			}

			if ( yPosition instanceof Integer )
			{
				position.y = ( (Integer) yPosition ).intValue( );
			}
			else if ( yPosition instanceof DimensionValue )
			{
				int percentY = (int) ( (DimensionValue) yPosition ).getMeasure( );

				position.y = ( area.width - imageArea.width ) * percentY / 100;
			}
			else if ( yPosition instanceof String )
			{
				alignment |= DesignElementHandleAdapter.getPosition( (String) yPosition );
			}

			figure.setAlignment( alignment );
			figure.setPosition( position );
		}
	}
	
}