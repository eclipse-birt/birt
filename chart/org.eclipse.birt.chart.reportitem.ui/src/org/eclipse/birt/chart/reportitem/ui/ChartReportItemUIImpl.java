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
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportElementFigure;
import org.eclipse.birt.report.designer.ui.extensions.ReportItemFigureProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
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
			final DesignerRepresentation dr = new DesignerRepresentation( iri );
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
						if ( !axisChart.getElementProperty( ChartReportItemConstants.PROPERTY_HOST_CHART )
								.equals( eih ) )
							// Update the handle property in async process
							Display.getCurrent( ).asyncExec( new Runnable( ) {

								public void run( )
								{
									try
									{
										axisChart.setProperty( ChartReportItemConstants.PROPERTY_HOST_CHART,
												eih );
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
			}

			return dr;
		}
		catch ( BirtException e )
		{
			logger.log( e );
			return null;
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
			final ChartReportItemImpl crii = (ChartReportItemImpl) eih.getReportItem( );
			// UPDATE THE MODEL
			crii.setHandle( eih );
			
			refreshBackgroundImage( eih, (ReportElementFigure) ifg );

			final boolean bAxisChart = ChartCubeUtil.isAxisChart( eih );
			final ExtendedItemHandle hostChart;
			final Chart cm;
			if ( bAxisChart )
			{
				hostChart = (ExtendedItemHandle) eih.getElementProperty( ChartReportItemConstants.PROPERTY_HOST_CHART );
				cm = ChartCubeUtil.getChartFromHandle( hostChart );
			}
			else
			{
				hostChart = null;
				cm = (Chart) crii.getProperty( ChartReportItemConstants.PROPERTY_CHART );
			}
			if ( cm == null )
			{
				return;
			}

			Bounds defaultBounds = ChartItemUtil.createDefaultChartBounds( eih,
					cm );

			// Default size for null dimension
			double dHeightInPoints = defaultBounds.getHeight( );
			double dWidthInPoints = defaultBounds.getWidth( );

			final DimensionHandle dhHeight;
			final DimensionHandle dhWidth;
			if ( bAxisChart )
			{
				// Use plot chart's size as axis chart's. Even if model sizes
				// are different, the output size are same
				if ( ChartXTabUIUtil.isTransposedChartWithAxes( cm ) )
				{
					dhHeight = eih.getHeight( );
					dhWidth = hostChart.getWidth( );
				}
				else
				{
					dhHeight = hostChart.getHeight( );
					dhWidth = eih.getWidth( );
				}
			}
			else
			{
				dhHeight = eih.getHeight( );
				dhWidth = eih.getWidth( );
			}

			double dOriginalHeight = dhHeight.getMeasure( );
			String sHeightUnits = dhHeight.getUnits( );

			double dOriginalWidth = dhWidth.getMeasure( );
			String sWidthUnits = dhWidth.getUnits( );

			// USE THE SWT DISPLAY SERVER TO CONVERT POINTS TO PIXELS
			final IDisplayServer idsSWT = ChartUIUtil.getDisplayServer( );

			if ( sHeightUnits != null )
			{
				// Convert from pixels to points first...since DimensionUtil
				// does not provide conversion services to and from Pixels
				if ( sHeightUnits == DesignChoiceConstants.UNITS_PX )
				{
					dOriginalHeight = ChartUtil.convertPixelsToPoints( idsSWT,
							dOriginalHeight );
					sHeightUnits = DesignChoiceConstants.UNITS_PT;
				}
				// convert percentage to points
				if ( sHeightUnits == DesignChoiceConstants.UNITS_PERCENTAGE )
				{
					IFigure parentFigure = ifg.getParent( );
					if ( parentFigure != null )
					{
						int height = (int) ( ( parentFigure.getSize( ).height - parentFigure.getInsets( )
								.getHeight( ) )
								* dOriginalHeight / 100 );
						dOriginalHeight = ChartUtil.convertPixelsToPoints( idsSWT,
								height );
						sHeightUnits = DesignChoiceConstants.UNITS_PT;
					}
				}
				dHeightInPoints = DimensionUtil.convertTo( dOriginalHeight,
						sHeightUnits,
						DesignChoiceConstants.UNITS_PT ).getMeasure( );
			}

			if ( sWidthUnits != null )
			{
				// Convert from pixels to points first...since DimensionUtil
				// does not provide conversion services to and from Pixels
				if ( sWidthUnits == DesignChoiceConstants.UNITS_PX )
				{
					dOriginalWidth = ( dOriginalWidth * 72d )
							/ idsSWT.getDpiResolution( );
					sWidthUnits = DesignChoiceConstants.UNITS_PT;
				}
				// convert percentage to points
				if ( sWidthUnits == DesignChoiceConstants.UNITS_PERCENTAGE )
				{
					IFigure parentFigure = ifg.getParent( );
					if ( parentFigure != null )
					{

						int width = (int) ( ( parentFigure.getSize( ).width - parentFigure.getInsets( )
								.getWidth( ) )
								* dOriginalWidth / 100 );
						dOriginalWidth = ChartUtil.convertPixelsToPoints( idsSWT,
								width );
						sWidthUnits = DesignChoiceConstants.UNITS_PT;
					}
				}
				dWidthInPoints = DimensionUtil.convertTo( dOriginalWidth,
						sWidthUnits,
						DesignChoiceConstants.UNITS_PT ).getMeasure( );
			}
			
			// Set default width/height if the value is zero.
			final ExtendedItemHandle fEih = eih;
			if ( dWidthInPoints == 0 )
			{
				dWidthInPoints = ChartReportItemConstants.DEFAULT_CHART_BLOCK_WIDTH;
				final double w = DimensionUtil.convertTo( dWidthInPoints,
						DesignChoiceConstants.UNITS_PT,
						sWidthUnits ).getMeasure( );
				final String dimension = String.valueOf(w) + sWidthUnits;
				Display.getDefault( ).asyncExec( new Runnable( ) {

					public void run( )
					{
						try
						{
							fEih.setWidth( dimension );
						}
						catch ( SemanticException e )
						{
							logger.log( e );
						}
					}
				} );

			}
			if ( dHeightInPoints == 0 )
			{
				dHeightInPoints = ChartReportItemConstants.DEFAULT_CHART_BLOCK_HEIGHT;
				final double h = DimensionUtil.convertTo( dHeightInPoints,
						DesignChoiceConstants.UNITS_PT,
						sHeightUnits ).getMeasure( );
				final String dimension = String.valueOf(h) + sHeightUnits;
				Display.getDefault( ).asyncExec( new Runnable( ) {

					public void run( )
					{
						try
						{
							fEih.setHeight( dimension );
						}
						catch ( SemanticException e )
						{
							logger.log( e );
						}
					}
				} );
			}
			
			final double dHeightInPixels = ( idsSWT.getDpiResolution( ) * dHeightInPoints ) / 72d;
			final double dWidthInPixels = ( idsSWT.getDpiResolution( ) * dWidthInPoints ) / 72d;

			// Do not modify size for axis chart since it uses reference as
			// model
			if ( cm != null && !bAxisChart )
			{
				if ( dWidthInPoints >= 0 )
					cm.getBlock( ).getBounds( ).setWidth( dWidthInPoints );
				if ( dHeightInPoints >= 0 )
					cm.getBlock( ).getBounds( ).setHeight( dHeightInPoints );
			}
			if ( crii.getDesignerRepresentation( ) != null )
			{
				( (DesignerRepresentation) crii.getDesignerRepresentation( ) ).setDirty( true );
			}

			// UPDATE THE FIGURE
			Dimension newSize = ifg.getBounds( ).getCopy( ).getSize( );
			if ( dWidthInPixels >= 0 )
				newSize.width = (int) dWidthInPixels;
			if ( dHeightInPixels >= 0 )
				newSize.height = (int) dHeightInPixels;
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
		String backGroundImage = getBackgroundImage( handle );

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

			Object[] backGroundPosition = getBackgroundPosition( handle );
			int backGroundRepeat = getBackgroundRepeat( handle );

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
	
	/**
	 * Get background image.
	 * 
	 * @param handle
	 *            The handle of design element.
	 * @return background image
	 */
	private String getBackgroundImage( DesignElementHandle handle )
	{
		return handle.getStringProperty( StyleHandle.BACKGROUND_IMAGE_PROP );
	}

	/**
	 * Get background position.
	 * 
	 * @param handle
	 *            The handle of design element.
	 * @return background position
	 */
	private Object[] getBackgroundPosition( DesignElementHandle handle )
	{
		Object x = null;
		Object y = null;

		if ( handle != null )
		{
			Object px = handle.getProperty( StyleHandle.BACKGROUND_POSITION_X_PROP );
			Object py = handle.getProperty( StyleHandle.BACKGROUND_POSITION_Y_PROP );

			if ( px instanceof String )
			{
				x = px;
			}
			else if ( px instanceof DimensionValue )
			{
				// {0%,0%}
				if ( DesignChoiceConstants.UNITS_PERCENTAGE.equals( ( (DimensionValue) px ).getUnits( ) ) )
				{
					x = px;
				}
				else
				{
					// {1cm,1cm}
					x = Integer.valueOf( (int) DEUtil.convertoToPixel( px ) );
				}
			}

			if ( py instanceof String )
			{
				y = py;
			}
			else if ( py instanceof DimensionValue )
			{
				// {0%,0%}
				if ( DesignChoiceConstants.UNITS_PERCENTAGE.equals( ( (DimensionValue) py ).getUnits( ) ) )
				{
					y = py;
				}
				else
				{
					// {1cm,1cm}
					y = Integer.valueOf( (int) DEUtil.convertoToPixel( py ) );
				}
			}
		}
		return new Object[]{
				x, y
		};
	}

	/**
	 * Get background repeat property.
	 * 
	 * @param handle
	 *            The handle of design element.
	 * @return background repeat property
	 */
	private int getBackgroundRepeat( DesignElementHandle handle )
	{
		return getRepeat( handle.getStringProperty( StyleHandle.BACKGROUND_REPEAT_PROP ) );
	}
	
	/**
	 *  Get reppeat value
	 * @param repeat
	 * 	Given string
	 * @return
	 * 	The repeat value
	 */
	private int getRepeat( String repeat )
	{
		if ( DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT_X.equals( repeat ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT_Y.equals( repeat ) )
		{
			return 2;
		}
		else if ( DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT.equals( repeat ) )
		{
			return 3;
		}
		return 0;
	}
}