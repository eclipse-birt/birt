/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Canvas;

/**
 * The class is responsible for computing and painting chart in chart builder.
 */

public abstract class ChartPreviewPainterBase implements
		IChartPreviewPainter
{

	/** The delay millisecond of chart painting. */
	private static final int PAINT_DELAY = 200;

	/** The timer is responsible for chart painting. */
	private Timer fPaintTimer = null;

	protected Canvas preview = null;

	protected IChartObject chart = null;

	private static boolean enableProcessor = true;

	private static boolean isLivePreview = false;

	protected ChartWizardContext wizardContext;

	protected ChartPreviewPainterBase( ChartWizardContext wizardContext )
	{
		this.wizardContext = wizardContext;
		this.chart = wizardContext.getModel( );
	}

	public void dispose( )
	{
		activateLivePreview( false );
		if ( fPaintTimer != null )
		{
			fPaintTimer.cancel( );
			fPaintTimer = null;
		}
	}

	protected void doRenderModel( Chart chart )
	{
		ignoreNotifications( true );

		// If not use live preview, use sample data to create runtime series
		if ( !( isLivePreviewActive( ) && isLivePreviewEnabled( ) ) )
		{
			chart.createSampleRuntimeSeries( );
		}

		if ( !isDisposedPreviewCanvas( ) )
		{
			clearPreviewCanvas( );
			repaintChartInTimer( );
		}
		ignoreNotifications( false );
	}

	public void renderModel( IChartObject chart )
	{
		if ( chart == null )
		{
			return;
		}
		this.chart = chart;

		doRenderModel( (Chart) chart );
	}

	public void setPreview( Canvas previewCanvas )
	{
		this.preview = previewCanvas;
	}

	public void controlMoved( ControlEvent e )
	{

	}

	public void controlResized( ControlEvent e )
	{
		repaintChartInTimer( );
	}

	protected void repaintChartInTimer( )
	{
		if ( fPaintTimer != null )
		{
			fPaintTimer.cancel( );
		}

		fPaintTimer = new Timer( );

		TimerTask task = new TimerTask( ) {

			public void run( )
			{
				paintChart( );
			}
		};

		fPaintTimer.schedule( task, PAINT_DELAY );
	}

	/**
	 * Generate whole chart and paint it.
	 */
	abstract protected void paintChart( );

	protected void clearPreviewCanvas( )
	{
		// TO clean canvas if needed
	}

	protected boolean isDisposedPreviewCanvas( )
	{
		return ( preview == null || preview.isDisposed( ) );
	}

	/**
	 * Checks whether Live Preview is enabled
	 */
	protected boolean isLivePreviewEnabled( )
	{
		return true;
	}

	protected void ignoreNotifications( boolean bIgnoreNotifications )
	{
		ChartAdapter.ignoreNotifications( bIgnoreNotifications );
	}

	/**
	 * Checks whether Live Preview is active
	 */
	public static boolean isLivePreviewActive( )
	{
		return isLivePreview;
	}

	/**
	 * Activates Live Preview when the data bindings are complete. The final
	 * result depends on whether Live Preview is enabled.
	 * 
	 * @param canLive
	 *            activate Live Preview or not
	 */
	public static void activateLivePreview( boolean canLive )
	{
		isLivePreview = canLive;
	}

	public static void enableProcessor( boolean isEnabled )
	{
		enableProcessor = isEnabled;
	}

	public static boolean isProcessorEnabled( )
	{
		return enableProcessor;
	}

}
