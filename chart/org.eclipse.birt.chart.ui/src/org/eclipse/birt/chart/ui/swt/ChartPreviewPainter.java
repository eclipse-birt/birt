/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

/**
 * The class is responsible for computing and painting chart in chart builder.  
 */
public class ChartPreviewPainter
		implements
			PaintListener,
			ControlListener,
			IUpdateNotifier
{
	
	/** The delay millisecond of chart painting. */
	private static final int PAINT_DELAY = 200;

	/** The timer is responsible for chart painting. */
	private Timer fPaintTimer = null;
	
	private Canvas preview = null;

	private Chart chart = null;

	private boolean bIsPainting = false;

	private static boolean enableProcessor = true;

	private static boolean isLivePreview = false;

	private Image buffer;

	private String errorMessage = null;

	private ChartWizardContext wizardContext;

	private GeneratedChartState gcs = null;

	private static int X_OFFSET = 3;
	private static int Y_OFFSET = 3;
	
	public ChartPreviewPainter( ChartWizardContext wizardContext )
	{
		super( );
		this.wizardContext = wizardContext;
	}

	private void updateBuffer( )
	{
		if ( bIsPainting )
		{
			return;
		}
		Throwable paintError = null;
		if ( chart == null )
		{
			return;
		}

		if ( isDisposedPreviewCanvas() )
		{
			return;
		}
		Rectangle re = preview.getClientArea( );

		final Rectangle adjustedRe = new Rectangle( 0, 0, re.width, re.height );

		if ( adjustedRe.width - 2 * X_OFFSET <= 0
				|| adjustedRe.height - 2 * Y_OFFSET <= 0 )
		{
			if ( buffer != null && !buffer.isDisposed( ) )
			{
				buffer.dispose( );
				buffer = null;
			}
			return;
		}

		bIsPainting = true;

		Image oldBuffer = null;

		if ( buffer == null )
		{
			buffer = new Image( Display.getDefault( ), adjustedRe );
		}
		else
		{
			Rectangle ore = buffer.getBounds( );

			oldBuffer = buffer;

			if ( !adjustedRe.equals( ore ) )
			{
				buffer = new Image( Display.getDefault( ), adjustedRe );
			}
		}

		GC gc = new GC( buffer );

		// fill default backgournd as white.
		gc.setForeground( Display.getDefault( )
				.getSystemColor( SWT.COLOR_WHITE ) );
		gc.fillRectangle( buffer.getBounds( ) );

		final Bounds bo = BoundsImpl.create( X_OFFSET,
				Y_OFFSET,
				adjustedRe.width - 2 * X_OFFSET,
				adjustedRe.height - 2 * Y_OFFSET );

		IDeviceRenderer deviceRenderer = null;

		try
		{
			deviceRenderer = ChartEngine.instance( ).getRenderer( "dv.SWT" ); //$NON-NLS-1$
			
			// The repaintChart should be improved, not to rebuild the whole chart, for the interactivity to work
			// correctly. repaintchart should just call render - David
			//deviceRenderer.setProperty( IDeviceRenderer.UPDATE_NOTIFIER, this );
			deviceRenderer.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, gc );
			bo.scale( 72d / deviceRenderer.getDisplayServer( )
					.getDpiResolution( ) ); 
			// CONVERT TO POINTS

			// GENERATE AND RENDER THE CHART
			final Generator gr = Generator.instance( );

			RunTimeContext rtc = new RunTimeContext( );
			rtc.setScriptingEnabled( false );
			rtc.setMessageLookup( new ChartBuilderMessageLookup( this.wizardContext.getUIServiceProvider( ) ) );
			rtc.setRightToLeft( wizardContext.isRtL( ) );
			rtc.setRightToLeftText( wizardContext.isTextRtL( ) );

			gcs = gr.build( deviceRenderer.getDisplayServer( ),
					chart,
					bo,
					null,
					rtc,
					enableProcessor ? wizardContext.getProcessor( ) : null );
			gr.render( deviceRenderer, gcs );
		}
		catch ( Exception ex )
		{
			paintError = ex;
		}
		finally
		{
			gc.dispose( );
			if ( deviceRenderer != null )
			{
				deviceRenderer.dispose( );
			}
		}

		boolean bException = false;
		if ( paintError != null )
		{
			buffer = oldBuffer;
			bException = true;
			if ( WizardBase.getErrors( ) == null )
			{
				errorMessage = paintError.getLocalizedMessage( );
				WizardBase.showException( errorMessage );
			}
		}

		if ( !bException
				&& ( ( WizardBase.getErrors( ) == null ) || ( WizardBase.getErrors( ).equals( errorMessage ) ) ) )
		{
			WizardBase.removeException( );
		}

		if ( oldBuffer != null && oldBuffer != buffer )
		{
			oldBuffer.dispose( );
		}
		bIsPainting = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */
	public void paintControl( PaintEvent pev )
	{
		GC gc = pev.gc;
		if ( buffer != null )
		{
			gc.drawImage( buffer, 0, 0 );
		}
	}

	/**
	 * @param previewCanvas
	 */
	public void setPreview( Canvas previewCanvas )
	{
		this.preview = previewCanvas;
	}

	public void renderModel( Chart chart )
	{
		if ( chart == null )
		{
			return;
		}
		this.chart = chart;
		ignoreNotifications( true );

		// If not use live preview, use sample data to create runtime series
		if ( !( isLivePreview && isLivePreviewEnabled( ) ) )
		{
			chart.createSampleRuntimeSeries( );
		}

		if ( !isDisposedPreviewCanvas() )
		{
			clearPreviewCanvas( );
			repaintChartAsync( );
		}
		ignoreNotifications( false );
	}

	private void ignoreNotifications( boolean bIgnoreNotifications )
	{
		ChartAdapter.ignoreNotifications( bIgnoreNotifications );
	}

	public static void enableProcessor( boolean isEnabled )
	{
		enableProcessor = isEnabled;
	}

	public static boolean isProcessorEnabled( )
	{
		return enableProcessor;
	}

	public void controlMoved( ControlEvent e )
	{

	}

	public void controlResized( ControlEvent e )
	{
		repaintChart( );
	}

	public void dispose( )
	{
		isLivePreview = false;
		if ( buffer != null )
		{
			buffer.dispose( );
			buffer = null;
		}
		if ( fPaintTimer != null )
		{
			fPaintTimer.cancel( );
			fPaintTimer = null;
		}		
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

	/**
	 * Checks whether Live Preview is enabled
	 */
	private boolean isLivePreviewEnabled( )
	{
		return wizardContext.getDataServiceProvider( ).isLivePreviewEnabled( );
	}


	public Chart getDesignTimeModel( )
	{
		return wizardContext.getModel( );
	}

	public Chart getRunTimeModel( )
	{
		if ( gcs != null )
		{
			return gcs.getChartModel( );
		}
		return null;
	}

	public Object peerInstance( )
	{
		// Preview canvas is used for receiving interactivity events
		return preview;
	}

	public void regenerateChart( )
	{
		repaintChartAsync( );
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#repaintChart()
	 */
	public void repaintChart( )
	{
		if ( fPaintTimer != null )
		{
			fPaintTimer.cancel( );
		}

		fPaintTimer = new Timer( );

		TimerTask task = new TimerTask( ) {

			public void run( )
			{		
				repaintChartAsync( );
			}
		};

		fPaintTimer.schedule( task, PAINT_DELAY );
	}

	/**
	 * Generate whole chart and paint it.
	 */
	private void repaintChartAsync( )
	{
		if ( !isDisposedPreviewCanvas() )
		{
			// Invoke it later and prevent freezing UI .
			Display.getDefault( ).asyncExec( new Runnable( ) {

				public void run( )
				{
					updateBuffer( );
					if ( !isDisposedPreviewCanvas() )
					{
						preview.redraw( );
					}
				}
			} );
		} 
	}
	
	/**
	 * Clear preview canvas area with white color. 
	 */
	private void clearPreviewCanvas( )
	{
		if ( isDisposedPreviewCanvas() )
		{
			return;
		}
		Rectangle re = preview.getClientArea( );
		
		Rectangle adjustedRe = new Rectangle( 0, 0, re.width, re.height );
		Image oldBuffer = null;

		if ( buffer == null )
		{
			if (adjustedRe.width <= 0 || adjustedRe.height <= 0) {
				return;
			}
			buffer = new Image( Display.getDefault( ), adjustedRe );
		}
		else
		{
			Rectangle ore = buffer.getBounds( );

			oldBuffer = buffer;

			if ( !adjustedRe.equals( ore ) )
			{
				if (adjustedRe.width <= 0 || adjustedRe.height <= 0) {
					return;
				}
				buffer = new Image( Display.getDefault( ), adjustedRe );
			}
		}

		GC gc = new GC( buffer );

		// fill default backgournd as white.
		gc.setForeground( Display.getDefault( )
				.getSystemColor( SWT.COLOR_WHITE ) );
		gc.fillRectangle( buffer.getBounds( ) );
		
		gc.dispose( );
		
		if ( oldBuffer != null && oldBuffer != buffer )
		{
			oldBuffer.dispose( );
		}

		if ( isDisposedPreviewCanvas() )
		{
			return;
		}
		preview.redraw( );
	}
	
	/**
	 * @return
	 * @since 2.3
	 */
	private boolean isDisposedPreviewCanvas()
	{
		return ( preview == null || preview.isDisposed( ) );
	}
}
