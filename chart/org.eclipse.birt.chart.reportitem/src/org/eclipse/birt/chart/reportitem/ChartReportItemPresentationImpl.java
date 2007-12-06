/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Locale;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.EmptyUpdateNotifier;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IImageMapEmitter;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Standard presentation implementation for Chart
 */
public final class ChartReportItemPresentationImpl
		extends
			ChartReportItemPresentationBase
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#onRowSets(org.eclipse.birt.report.engine.extension.IRowSet[])
	 */
	public Object onRowSets( IBaseResultSet[] baseResultSet )
			throws BirtException
	{
		// Extract result set to render and check for null data
		IBaseResultSet resultSet = getDataToRender( baseResultSet );

		// Skip gracefully if there is no data
		if ( resultSet == null )
			return null;

		try
		{
			// Create shared scale if needed
			if ( rtc.getScale( ) == null
					&& ChartReportItemUtil.canScaleShared( handle, cm ) )
			{
				rtc.setScale( createSharedScale( resultSet ) );
			}

			// Get the BIRT report context
			BIRTExternalContext externalContext = new BIRTExternalContext( context );

			// Initialize script handler and register birt context in scope
			initializeScriptHandler( externalContext );

			// Prepare Data for Series
			IDataRowExpressionEvaluator rowAdapter = createEvaluator( resultSet );

			// Prepare data processor for hyperlinks/tooltips
			BIRTActionEvaluator evaluator = new BIRTActionEvaluator( );

			// Bind Data to series
			Generator.instance( ).bindData( rowAdapter, evaluator, cm, rtc );

			// Prepare Device Renderer
			prepareDeviceRenderer( );

			// Build the chart
			GeneratedChartState gcs = buildChart( rowAdapter, externalContext );

			// Render the chart
			renderToImageFile( gcs );

			// Close the dataRow evaluator. It needs to stay opened until the
			// chart is fully rendered.
			rowAdapter.close( );

			// Set the scale shared when scale has been computed, and store it
			// in the ReportItem
			if ( rtc.getScale( ) != null && !rtc.getScale( ).isShared( ) )
			{
				rtc.getScale( ).setShared( true );
				( (ChartReportItemImpl) getReportItem( handle ) ).setScale( rtc.getScale( ) );
			}

			// Returns the content to display (image or image+imagemap)
			return getImageToDisplay( );
		}
		catch ( BirtException birtException )
		{
			// Check if the exception is caused by no data to display (in that
			// case skip gracefully)
			if ( isNoDataException( birtException ) )
				return null;
			else
				throw birtException;
		}
		catch ( RuntimeException ex )
		{
			logger.log( ILogger.ERROR,
					Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsFailed" ) ); //$NON-NLS-1$
			logger.log( ex );
			throw new ChartException( ChartReportItemPlugin.ID,
					ChartException.GENERATION,
					ex );
		}
	}

	/**
	 * Check there is some data to display in the chart.
	 * 
	 * @param baseResultSet
	 * @return null if nothing to render
	 */
	private IBaseResultSet getDataToRender( IBaseResultSet[] baseResultSet )
			throws BirtException
	{
		// BIND RESULTSET TO CHART DATASETS
		if ( baseResultSet == null || baseResultSet.length < 1 )
		{
			// if the Data rows are null/empty, just log it and returns
			// null gracefully.
			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.error.NoData" ) ); //$NON-NLS-1$
			return null;
		}

		IBaseResultSet resultSet = baseResultSet[0];
		if ( resultSet == null || ChartReportItemUtil.isEmpty( resultSet ) )
		{
			// Do nothing when IBaseResultSet is empty or null
			return null;
		}

		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsStart" ) ); //$NON-NLS-1$

		// catch unwanted null handle case
		if ( handle == null )
		{
			assert false; // should we throw an exception here instead?
			return null;
		}
		return resultSet;
	}

	private void initializeScriptHandler( BIRTExternalContext externalContext )
			throws ChartException
	{
		String javaHandlerClass = handle.getEventHandlerClass( );
		if ( javaHandlerClass != null && javaHandlerClass.length( ) > 0 )
		{
			// use java handler if available.
			cm.setScript( javaHandlerClass );
		}

		rtc.setScriptClassLoader( new BIRTScriptClassLoader( appClassLoader ) );
		// INITIALIZE THE SCRIPT HANDLER
		// UPDATE THE CHART SCRIPT CONTEXT

		ScriptHandler sh = rtc.getScriptHandler( );

		if ( sh == null ) // IF NOT PREVIOUSLY DEFINED BY
		// REPORTITEM ADAPTER
		{
			sh = new ScriptHandler( );
			rtc.setScriptHandler( sh );

			sh.setScriptClassLoader( rtc.getScriptClassLoader( ) );
			sh.setScriptContext( rtc.getScriptContext( ) );

			final String sScriptContent = cm.getScript( );
			if ( externalContext != null
					&& externalContext.getScriptable( ) != null )
			{
				sh.init( externalContext.getScriptable( ) );
			}
			else
			{
				sh.init( null );
			}
			sh.setRunTimeModel( cm );

			if ( sScriptContent != null
					&& sScriptContent.length( ) > 0 && rtc.isScriptingEnabled( ) )
			{
				sh.register( sScriptContent );
			}
		}
	}

	private GeneratedChartState buildChart(
			IDataRowExpressionEvaluator rowAdapter,
			BIRTExternalContext externalContext ) throws ChartException
	{
		final Bounds bo = computeBounds( );

		initializeRuntimeContext( rowAdapter );

		return Generator.instance( ).build( idr.getDisplayServer( ),
				cm,
				bo,
				externalContext,
				rtc,
				new ChartReportStyleProcessor( handle, this.style ) );
	}

	private Object getImageToDisplay( )
	{
		if ( getOutputType( ) == OUTPUT_AS_IMAGE )
		{
			return fis;
		}
		else if ( getOutputType( ) == OUTPUT_AS_IMAGE_WITH_MAP )
		{
			return new Object[]{
					fis, imageMap
			};
		}
		else
			throw new IllegalArgumentException( );
	}

	private void renderToImageFile( GeneratedChartState gcs )
			throws ChartException
	{
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsRendering" ) ); //$NON-NLS-1$

		ByteArrayOutputStream baos = new ByteArrayOutputStream( );
		BufferedOutputStream bos = new BufferedOutputStream( baos );

		idr.setProperty( IDeviceRenderer.FILE_IDENTIFIER, bos );
		idr.setProperty( IDeviceRenderer.UPDATE_NOTIFIER,
				new EmptyUpdateNotifier( cm, gcs.getChartModel( ) ) );

		Generator.instance( ).render( idr, gcs );

		// RETURN A STREAM HANDLE TO THE NEWLY CREATED IMAGE
		try
		{
			bos.close( );
			fis = new ByteArrayInputStream( baos.toByteArray( ) );
		}
		catch ( Exception ioex )
		{
			throw new ChartException( ChartReportItemPlugin.ID,
					ChartException.GENERATION,
					ioex );
		}

		if ( getOutputType( ) == OUTPUT_AS_IMAGE_WITH_MAP )
		{
			imageMap = ( (IImageMapEmitter) idr ).getImageMap( );
		}

	}

	private boolean isNoDataException( BirtException birtException )
	{
		Throwable ex = birtException;
		while ( ex.getCause( ) != null )
		{
			ex = ex.getCause( );
		}

		if ( ex instanceof ChartException
				&& ( (ChartException) ex ).getType( ) == ChartException.ZERO_DATASET )
		{
			// if the Data set has zero lines, just
			// returns null gracefully.
			return true;
		}

		if ( ex instanceof ChartException
				&& ( (ChartException) ex ).getType( ) == ChartException.ALL_NULL_DATASET )
		{
			// if the Data set contains all null values, just
			// returns null gracefully and render nothing.
			return true;
		}

		if ( ( ex instanceof ChartException && ( (ChartException) ex ).getType( ) == ChartException.INVALID_IMAGE_SIZE ) )
		{
			// if the image size is invalid, this may caused by
			// Display=None, lets ignore it.
			logger.log( birtException );
			return true;
		}

		logger.log( ILogger.ERROR,
				Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsFailed" ) ); //$NON-NLS-1$
		logger.log( birtException );
		return false;

	}

	private void initializeRuntimeContext(
			IDataRowExpressionEvaluator rowAdapter )
	{
		rtc.setActionRenderer( new BIRTActionRenderer( this.handle,
				this.ah,
				rowAdapter,
				this.context ) );
		rtc.setMessageLookup( new BIRTMessageLookup( context ) );
		Object renderContext = context.getAppContext( )
				.get( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT );

		// read RtL flag from engine
		if ( renderContext instanceof HTMLRenderContext )
		{
			IRenderOption renderOption = ( (HTMLRenderContext) renderContext ).getRenderOption( );
			if ( renderOption instanceof HTMLRenderOption )
			{
				if ( ( (HTMLRenderOption) renderOption ).getHtmlRtLFlag( ) )
				{
					rtc.setRightToLeft( true );
				}
			}
		}

	}

	private void prepareDeviceRenderer( ) throws ChartException
	{
		idr = ChartEngine.instance( ).getRenderer( "dv." //$NON-NLS-1$
				+ sExtension.toUpperCase( Locale.US ) );

		idr.setProperty( IDeviceRenderer.DPI_RESOLUTION, new Integer( dpi ) );

		if ( "SVG".equalsIgnoreCase( sExtension ) ) //$NON-NLS-1$
		{
			idr.setProperty( "resize.svg", Boolean.TRUE ); //$NON-NLS-1$
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#getSize()
	 */
	public Size getSize( )
	{
		if ( cm != null )
		{
			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.getSizeStart" ) ); //$NON-NLS-1$
			final Size sz = new Size( );
			sz.setWidth( (float) cm.getBlock( ).getBounds( ).getWidth( ) );
			sz.setHeight( (float) cm.getBlock( ).getBounds( ).getHeight( ) );
			sz.setUnit( Size.UNITS_PT );
			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.getSizeEnd" ) ); //$NON-NLS-1$
			return sz;
		}
		return super.getSize( );
	}

	protected Bounds computeBounds( )
	{
		final Bounds originalBounds = cm.getBlock( ).getBounds( );

		// we must copy the bounds to avoid that setting it on one object
		// unsets it on its precedent container

		Bounds bounds = (Bounds) EcoreUtil.copy( originalBounds );
		return bounds;
	}

}
