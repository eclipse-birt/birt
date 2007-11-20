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
		if ( resultSet == null || isEmpty( resultSet ) )
		{
			// Do nothing when IBaseResultSet is empty or null
			return null;
		}

		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsStart" ) ); //$NON-NLS-1$

		// catch unwanted null handle case
		if ( handle == null )
		{
			return null;
		}

		try
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
			BIRTExternalContext externalContext = new BIRTExternalContext( context );
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
						&& sScriptContent.length( ) > 0
						&& rtc.isScriptingEnabled( ) )
				{
					sh.register( sScriptContent );
				}
			}

			// Create evaluator
			IDataRowExpressionEvaluator rowAdapter = createEvaluator( resultSet );
			BIRTActionEvaluator evaluator = new BIRTActionEvaluator( );

			Generator.instance( ).bindData( rowAdapter, evaluator, cm, rtc );

			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsBuilding" ) ); //$NON-NLS-1$

			// FETCH A HANDLE TO THE DEVICE RENDERER
			idr = ChartEngine.instance( ).getRenderer( "dv." //$NON-NLS-1$
					+ sExtension.toUpperCase( Locale.US ) );

			idr.setProperty( IDeviceRenderer.DPI_RESOLUTION, new Integer( dpi ) );

			if ( "SVG".equalsIgnoreCase( sExtension ) ) //$NON-NLS-1$
			{
				idr.setProperty( "resize.svg", Boolean.TRUE ); //$NON-NLS-1$
			}

			// BUILD THE CHART
			final Bounds bo = computeBounds( );

			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.PresentationUsesBoundsBo", bo ) ); //$NON-NLS-1$

			final Generator gr = Generator.instance( );
			GeneratedChartState gcs = null;

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

			gcs = gr.build( idr.getDisplayServer( ),
					cm,
					bo,
					externalContext,
					rtc,
					new ChartReportStyleProcessor( handle, this.style ) );

			// WRITE TO THE IMAGE FILE
			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsRendering" ) ); //$NON-NLS-1$

			ByteArrayOutputStream baos = new ByteArrayOutputStream( );
			BufferedOutputStream bos = new BufferedOutputStream( baos );

			idr.setProperty( IDeviceRenderer.FILE_IDENTIFIER, bos );
			idr.setProperty( IDeviceRenderer.UPDATE_NOTIFIER,
					new EmptyUpdateNotifier( cm, gcs.getChartModel( ) ) );

			gr.render( idr, gcs );

			// cleanup the dataRow evaluator.
			rowAdapter.close( );

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

			if ( !"SVG".equals( sExtension ) && idr instanceof IImageMapEmitter ) //$NON-NLS-1$
			{
				imageMap = ( (IImageMapEmitter) idr ).getImageMap( );
			}

		}
		catch ( BirtException birtException )
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
				return null;
			}

			if ( ex instanceof ChartException
					&& ( (ChartException) ex ).getType( ) == ChartException.ALL_NULL_DATASET )
			{
				// if the Data set contains all null values, just
				// returns null gracefully and render nothing.
				return null;
			}

			if ( ( ex instanceof ChartException && ( (ChartException) ex ).getType( ) == ChartException.INVALID_IMAGE_SIZE ) )
			{
				// if the image size is invalid, this may caused by
				// Display=None, lets ignore it.
				logger.log( birtException );
				return null;
			}

			logger.log( ILogger.ERROR,
					Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsFailed" ) ); //$NON-NLS-1$
			logger.log( birtException );
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

		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemPresentationImpl.onRowSetsEnd" ) ); //$NON-NLS-1$

		if ( "SVG".equals( sExtension ) ) //$NON-NLS-1$
		{
			return fis;
		}
		else
		{
			return new Object[]{
					fis, imageMap
			};
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
