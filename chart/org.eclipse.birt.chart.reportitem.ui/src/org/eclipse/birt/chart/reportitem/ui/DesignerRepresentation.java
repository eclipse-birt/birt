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

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.ChartReportStyleProcessor;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;

/**
 * 
 */
public final class DesignerRepresentation extends Figure
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	private final static String ERROR_MSG = Messages.getString( "DesignerRepresentation.error.Error" ); //$NON-NLS-1$

	/**
	 * 
	 */
	private IDeviceRenderer idr = null;

	/**
	 * 
	 */
	private final ChartReportItemImpl crii;

	/**
	 * 
	 */
	private Image imgChart = null;

	/**
	 * 
	 */
	private GC gc = null;

	/**
	 * 
	 */
	private transient boolean bRtL;

	/**
	 * 
	 */
	private transient boolean bDirty = true;

	/**
	 * 
	 */
	private static final PaletteData PALETTE_DATA = new PaletteData( 0xFF0000,
			0xFF00,
			0xFF );

	/**
	 * 
	 */
	private static final int TRANSPARENT_COLOR = 0x123456;

	/**
	 * Prevent re-entrancy of the paint method
	 */
	private boolean bPainting = false;

	/**
	 * 
	 * @param crii
	 */
	DesignerRepresentation( ChartReportItemImpl crii )
	{
		bRtL = ReportItemUIUtil.isRtl( );

		this.crii = crii;
		if ( crii != null )
		{
			final Chart cm = (Chart) crii.getProperty( "chart.instance" ); //$NON-NLS-1$
			// GET THE MODEL WRAPPED INSIDE THE REPORT ITEM IMPL
			if ( cm != null )
			{
				final IDisplayServer idsSWT = ChartUIUtil.getDisplayServer( ); // REUSE
				final Bounds bo = cm.getBlock( )
						.getBounds( )
						.scaledInstance( 72d / idsSWT.getDpiResolution( ) );
				setSize( (int) bo.getWidth( ), (int) bo.getHeight( ) );
			}
			else
			{
				setSize( (int) ChartWizard.DEFAULT_CHART_BLOCK_WIDTH,
						(int) ChartWizard.DEFAULT_CHART_BLOCK_HEIGHT );
			}
		}
		else
		{
			setSize( (int) ChartWizard.DEFAULT_CHART_BLOCK_WIDTH,
					(int) ChartWizard.DEFAULT_CHART_BLOCK_HEIGHT );
		}

		try
		{
			idr = PluginSettings.instance( ).getDevice( "dv.SWT" ); //$NON-NLS-1$
		}
		catch ( ChartException pex )
		{
			logger.log( pex );
		}
	}

	/**
	 * 
	 * @param bDirty
	 */
	final void setDirty( boolean bDirty )
	{
		this.bDirty = bDirty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#getMinimumSize(int, int)
	 */
	public Dimension getMinimumSize( int wHint, int hHint )
	{
		if ( minSize != null )
		{
			return minSize;
		}

		DimensionHandle dimWidth = ( (ExtendedItemHandle) crii.getHandle( ) ).getWidth( );
		DimensionHandle dimHeight = ( (ExtendedItemHandle) crii.getHandle( ) ).getHeight( );

		boolean isPerWidth = DesignChoiceConstants.UNITS_PERCENTAGE.equals( dimWidth.getUnits( ) );
		boolean isPerHeight = DesignChoiceConstants.UNITS_PERCENTAGE.equals( dimHeight.getUnits( ) );

		Dimension dim = new Dimension( );
		Dimension size = getSize( );

		if ( !isPerWidth )
		{
			dim.width = Math.max( wHint, size.width );
		}

		if ( !isPerHeight )
		{
			dim.height = Math.max( hHint, size.height );
		}

		return dim;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize( int wHint, int hHint )
	{
		if ( prefSize != null )
		{
			return prefSize;
		}

		DimensionHandle dimWidth = ( (ExtendedItemHandle) crii.getHandle( ) ).getWidth( );
		DimensionHandle dimHeight = ( (ExtendedItemHandle) crii.getHandle( ) ).getHeight( );

		boolean isPerWidth = DesignChoiceConstants.UNITS_PERCENTAGE.equals( dimWidth.getUnits( ) );
		boolean isPerHeight = DesignChoiceConstants.UNITS_PERCENTAGE.equals( dimHeight.getUnits( ) );

		Dimension dim = getSize( ).getCopy( );

		if ( isPerWidth && wHint != -1 )
		{
			// dim.width = Math.min( dim.width, wHint );
			// ?? TODO calculate the percentage value here?
			dim.width = (int) ( wHint * dimWidth.getMeasure( ) / 100d );
		}

		if ( isPerHeight && hHint != -1 )
		{
			// dim.height = Math.min( dim.height, hHint );
			// ?? TODO calculate the percentage value here?
			dim.height = (int) ( hHint * dimHeight.getMeasure( ) / 100d );
		}

		// ?? refresh the model size.
		// TODO this is a temp solution, better not refresh model here. and this
		// can not handle all the cases.
		setSize( dim.width, dim.height );
		Chart cm = (Chart) crii.getProperty( "chart.instance" ); //$NON-NLS-1$
		if ( cm != null )
		{
			IDisplayServer ids = ChartUIUtil.getDisplayServer( );
			cm.getBlock( )
					.getBounds( )
					.setWidth( ChartUtil.convertPixelsToPoints( ids, dim.width ) );
			cm.getBlock( )
					.getBounds( )
					.setHeight( ChartUtil.convertPixelsToPoints( ids,
							dim.height ) );
		}

		return dim;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintClientArea(org.eclipse.draw2d.Graphics)
	 */
	public final void paintClientArea( Graphics g )
	{
		if ( bPainting ) // PREVENT RE-ENTRANCY
		{
			return;
		}
		final Rectangle r = getClientArea( ).getCopy( );
		if ( r.width <= 0 || r.height <= 0 )
		{
			return;
		}
		bPainting = true;

		if ( bDirty )
		{
			bDirty = false;
			// GET THE MODEL WRAPPED INSIDE THE REPORT ITEM IMPL
			final Chart cm = (Chart) crii.getProperty( "chart.instance" ); //$NON-NLS-1$
			if ( cm == null )
			{
				bPainting = false;
				logger.log( ILogger.ERROR,
						Messages.getString( "DesignerRepresentation.log.UnableToFind" ) ); //$NON-NLS-1$
				return;
			}
			final Display d = Display.getCurrent( );
			Dimension dSize = r.getSize( );

			// OFFSCREEN IMAGE CREATION STRATEGY
			if ( imgChart == null
					|| imgChart.getImageData( ).width != dSize.width
					|| imgChart.getImageData( ).height != dSize.height )
			{
				if ( gc != null )
				{
					gc.dispose( );
				}
				if ( imgChart != null )
				{
					imgChart.dispose( );
				}
				bDirty = true;

				// FILL IMAGE WITH TRANSPARENCY
				final ImageData ida = new ImageData( dSize.width,
						dSize.height,
						32,
						PALETTE_DATA );
				ida.transparentPixel = TRANSPARENT_COLOR;
				/*
				 * for (int i = 0; i < ida.width; i++) { for (int j = 0; j <
				 * ida.height; j++) { ida.setPixel(i, j, TRANSPARENT_COLOR); } }
				 */
				imgChart = new Image( d, ida );
				gc = new GC( imgChart );
			}

			final Color clrBG = new Color( d, 0x12, 0x34, 0x56 ); // TRANSPARENT
			// COLOR INDEX
			// (REVISIT?)
			final Color clrPreviousBG = gc.getBackground( );
			gc.setBackground( clrBG );
			gc.fillRectangle( 0,
					0,
					imgChart.getImageData( ).width,
					imgChart.getImageData( ).height );
			clrBG.dispose( ); // DISPOSE
			gc.setBackground( clrPreviousBG ); // RESTORE

			// SETUP THE RENDERING CONTEXT
			Bounds bo = BoundsImpl.create( 0, 0, dSize.width, dSize.height );
			bo.scale( 72d / idr.getDisplayServer( ).getDpiResolution( ) );
			idr.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, gc );

			Generator gr = Generator.instance( );
			cm.clearSections( IConstants.RUN_TIME ); // REMOVE OLD TRANSIENT
			// RUNTIME SERIES
			cm.createSampleRuntimeSeries( ); // USING SAMPLE DATA STORED IN
			// MODEL
			try
			{
				RunTimeContext rtc = new RunTimeContext( );
				rtc.setScriptingEnabled( false );
				rtc.setMessageLookup( new BIRTDesignerMessageLookup( crii.getHandle( ) ) );
				if ( bRtL )
				{
					rtc.setRightToLeft( bRtL );
				}
				gr.render( idr,
						gr.build( idr.getDisplayServer( ),
								cm,
								bo,
								null,
								rtc,
								new ChartReportStyleProcessor( crii.getHandle( ),
										true ) ) );
			}
			catch ( ChartException gex )
			{
				showException( gc, gex );
			}
		}

		if ( imgChart != null )
		{
			g.drawImage( imgChart, r.x, r.y );
		}
		bPainting = false;
	}

	/**
	 * Show the exception message that prevented to draw the chart
	 * 
	 * @param g2d
	 * @param ex
	 *            The exception that occured
	 */
	private final void showException( GC g2d, Exception ex )
	{
		Point pTLC = new Point( 0, 0 );

		// String sWrappedException = ex.getClass( ).getName( );
		Throwable th = ex;

		String sMessage = null;
		if ( th instanceof BirtException )
		{
			sMessage = ( (BirtException) th ).getLocalizedMessage( );
		}
		else
		{
			sMessage = ex.getMessage( );
		}

		if ( sMessage == null )
		{
			sMessage = "<null>"; //$NON-NLS-1$
		}
		// StackTraceElement[] stea = ex.getStackTrace( );
		Dimension d = getSize( );

		Device dv = Display.getCurrent( );
		Font fo = new Font( dv, "Courier", SWT.BOLD, 12 ); //$NON-NLS-1$
		g2d.setFont( fo );
		FontMetrics fm = g2d.getFontMetrics( );
		g2d.setBackground( dv.getSystemColor( SWT.COLOR_WHITE ) );
		g2d.fillRectangle( pTLC.x + 20,
				pTLC.y + 20,
				d.width - 40,
				d.height - 40 );
		g2d.setForeground( dv.getSystemColor( SWT.COLOR_BLACK ) );
		g2d.drawRectangle( pTLC.x + 20,
				pTLC.y + 20,
				d.width - 40,
				d.height - 40 );
		Region rgPrev = new Region( );
		g2d.getClipping( rgPrev );
		g2d.setClipping( pTLC.x + 20, pTLC.y + 20, d.width - 40, d.height - 40 );
		int x = pTLC.x + 25, y = pTLC.y + 20 + fm.getHeight( );
		g2d.setForeground( dv.getSystemColor( SWT.COLOR_BLACK ) );
		g2d.drawString( ERROR_MSG, x, y );
		y += fm.getHeight( );
		g2d.setForeground( dv.getSystemColor( SWT.COLOR_RED ) );
		g2d.drawText( sMessage, x, y );

		g2d.setClipping( rgPrev );
		rgPrev.dispose( );
		fo.dispose( );
	}

	/**
	 * 
	 */
	public final void dispose( )
	{
		if ( imgChart != null )
		{
			gc.dispose( );
			imgChart.dispose( );
			idr.dispose( );
			gc = null;
			imgChart = null;
			bDirty = true;
			idr = null;
		}
	}
}