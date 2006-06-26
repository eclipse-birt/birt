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

package org.eclipse.birt.chart.reportitem;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.birt.chart.device.EmptyUpdateNotifier;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IImageMapEmitter;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.ReportItemPresentationBase;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * ChartReportItemPresentationImpl
 */
public final class ChartReportItemPresentationImpl extends
		ReportItemPresentationBase
{

	private InputStream fis = null;

	private String imageMap = null;

	private String sExtension = null;

	private String sSupportedFormats = null;

	private String outputFormat = null;

	private Chart cm = null;

	private IDeviceRenderer idr = null;

	private ExtendedItemHandle handle;

	private RunTimeContext rtc = null;

	private List registeredDevices = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public ChartReportItemPresentationImpl( )
	{
		registeredDevices = new ArrayList( );
		try
		{
			String[][] formats = PluginSettings.instance( )
					.getRegisteredOutputFormats( );
			for ( int i = 0; i < formats.length; i++ )
			{
				registeredDevices.add( formats[i][0] );
			}
		}
		catch ( ChartException e )
		{
			logger.log( e );
		}
	}

	/**
	 * check if the format is supported by the browser and device renderer.
	 */
	private boolean isOutputRendererSupported( String format )
	{
		if ( format != null )
		{
			if ( sSupportedFormats != null
					&& ( sSupportedFormats.indexOf( format ) != -1 ) )
			{
				return registeredDevices.contains( format );
			}
		}
		return false;
	}

	private String getFirstSupportedFormat( String formats )
	{
		if ( formats != null && formats.length( ) > 0 )
		{
			int idx = formats.indexOf( ';' );
			if ( idx == -1 )
			{
				if ( isOutputRendererSupported( formats ) )
				{
					return formats;
				}
			}
			else
			{
				String ext = formats.substring( 0, idx );

				if ( isOutputRendererSupported( ext ) )
				{
					return ext;
				}
				else
				{
					return getFirstSupportedFormat( formats.substring( idx + 1 ) );
				}
			}
		}

		// PNG as default.
		return "PNG"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject( ExtendedItemHandle eih )
	{
		IReportItem item = null;
		try
		{
			item = eih.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( e );
		}
		if ( item == null )
		{
			try
			{
				eih.loadExtendedElement( );
				item = eih.getReportItem( );
			}
			catch ( ExtendedElementException eeex )
			{
				logger.log( eeex );
			}
			if ( item == null )
			{
				logger.log( ILogger.ERROR,
						Messages.getString( "ChartReportItemPresentationImpl.log.UnableToLocateWrapper" ) ); //$NON-NLS-1$
				return;
			}
		}
		cm = (Chart) ( (ChartReportItemImpl) item ).getProperty( "chart.instance" ); //$NON-NLS-1$
		handle = eih;

		Object of = handle.getProperty( "outputFormat" ); //$NON-NLS-1$

		if ( of instanceof String )
		{
			outputFormat = (String) of;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setLocale(java.util.Locale)
	 */
	public final void setLocale( Locale lcl )
	{
		if ( rtc == null )
		{
			rtc = new RunTimeContext( );
		}
		rtc.setLocale( lcl );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setOutputFormat(java.lang.String)
	 */
	public void setOutputFormat( String sOutputFormat )
	{
		if ( sOutputFormat.equalsIgnoreCase( "HTML" ) ) //$NON-NLS-1$
		{
			if ( isOutputRendererSupported( outputFormat ) )
			{
				sExtension = outputFormat;
			}
			else if ( outputFormat != null
					&& outputFormat.toUpperCase( ).equals( "GIF" ) && //$NON-NLS-1$ 
					isOutputRendererSupported( "PNG" ) ) //$NON-NLS-1$
			{
				// render old GIF charts as PNG
				sExtension = "PNG"; //$NON-NLS-1$
			}
			else if ( isOutputRendererSupported( "SVG" ) ) //$NON-NLS-1$
			{
				// SVG is the preferred output for HTML
				sExtension = "SVG"; //$NON-NLS-1$
			}
			else
			{
				sExtension = getFirstSupportedFormat( sSupportedFormats );
			}
		}
		else if ( sOutputFormat.equalsIgnoreCase( "PDF" ) ) //$NON-NLS-1$
		{
			if ( isOutputRendererSupported( outputFormat ) )
			{
				sExtension = outputFormat;
			}
			else if ( isOutputRendererSupported( "PNG" ) ) //$NON-NLS-1$
			{
				// PNG is the preferred output for PDF
				sExtension = "PNG"; //$NON-NLS-1$
			}
			else
			{
				sExtension = getFirstSupportedFormat( sSupportedFormats );
			}
		}
		else
		{
			if ( isOutputRendererSupported( outputFormat ) )
			{
				sExtension = outputFormat;
			}
			else
			{
				sExtension = getFirstSupportedFormat( sSupportedFormats );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setSupportedImageFormats(java.lang.String)
	 */
	public void setSupportedImageFormats( String sSupportedFormats )
	{
		this.sSupportedFormats = sSupportedFormats;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#deserialize(java.io.InputStream)
	 */
	public void deserialize( InputStream is )
	{
		try
		{
			ObjectInputStream ois = new ObjectInputStream( is );
			Object o = ois.readObject( );

			if ( o instanceof RunTimeContext )
			{
				RunTimeContext drtc = (RunTimeContext) o;

				if ( rtc != null )
				{
					drtc.setLocale( rtc.getLocale( ) );
				}

				rtc = drtc;
				cm = rtc.getScriptContext( ).getChartInstance( );
				// Set back the cm into the handle from the engine, so that the
				// chart inside the
				// reportdesignhandle is the same as the one used during
				// presentation. 
				// No command should be executed, since it's a runtime operation
				// Set the model directly through setModel and not setProperty
				if ( cm != null && handle != null )
				{
					IReportItem item = handle.getReportItem( );
					((ChartReportItemImpl)item).setModel( cm );
				}
			}
			ois.close( );
		}
		catch ( Exception e )
		{
			logger.log( e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#getOutputType()
	 */
	public int getOutputType( )
	{
		if ( "SVG".equals( sExtension ) ) //$NON-NLS-1$
		{
			return OUTPUT_AS_IMAGE;
		}
		else
		{
			return OUTPUT_AS_IMAGE_WITH_MAP;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#getImageMIMEType()
	 */
	public String getImageMIMEType( )
	{
		if ( idr instanceof IImageMapEmitter )
		{
			return ( (IImageMapEmitter) idr ).getMimeType( );
		}
		else if ( "SVG".equals( sExtension ) ) //$NON-NLS-1$
		{
			return "image/svg+xml"; //$NON-NLS-1$
		}
		else
		{
			return "image"; //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#getOutputContent()
	 */
	public Object getOutputContent( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#onRowSets(org.eclipse.birt.report.engine.extension.IRowSet[])
	 */
	public Object onRowSets( IRowSet[] irsa ) throws BirtException
	{
		// BIND RESULTSET TO CHART DATASETS
		if ( irsa == null || irsa.length != 1 || irsa[0] == null )
		{
			// if the Data rows are null/empty, just log the error and returns
			// null gracefully.
			logger.log( new ChartException( ChartReportItemPlugin.ID,
					ChartException.GENERATION,
					"ChartReportItemPresentationImpl.error.NoData", //$NON-NLS-1$
					Messages.getResourceBundle( rtc.getULocale( ) ) ) );
			return null;
		}

		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsStart" ) ); //$NON-NLS-1$

		try
		{
			String javaHandlerClass = handle.getEventHandlerClass( );
			if ( javaHandlerClass != null && javaHandlerClass.length( ) > 0 )
			{
				// use java handler if available.
				cm.setScript( javaHandlerClass );
			}

			BIRTDataRowEvaluator rowAdapter = new BIRTDataRowEvaluator( irsa[0] );
			Generator.instance( ).bindData( rowAdapter,
					new BIRTActionEvaluator( ),
					cm,
					rtc );
			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsBuilding" ) ); //$NON-NLS-1$

			// FETCH A HANDLE TO THE DEVICE RENDERER
			idr = PluginSettings.instance( ).getDevice( "dv." //$NON-NLS-1$
					+ sExtension.toUpperCase( Locale.US ) );

			idr.setProperty( IDeviceRenderer.DPI_RESOLUTION, new Integer( dpi ) );

			if ( "SVG".equalsIgnoreCase( sExtension ) ) //$NON-NLS-1$
			{
				idr.setProperty( "resize.svg", Boolean.TRUE ); //$NON-NLS-1$
			}

			// BUILD THE CHART
			final Bounds originalBounds = cm.getBlock( ).getBounds( );

			// we must copy the bounds to avoid that setting it on one object
			// unsets it on its precedent container

			final Bounds bo = (Bounds) EcoreUtil.copy( originalBounds );

			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.PresentationUsesBoundsBo", bo ) ); //$NON-NLS-1$

			final Generator gr = Generator.instance( );
			GeneratedChartState gcs = null;
			rtc.setScriptClassLoader( new BIRTScriptClassLoader( ) );
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
					new BIRTExternalContext( context ),
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
			while ( ex.getCause() != null )
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#finish()
	 */
	public void finish( )
	{
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemPresentationImpl.log.finishStart" ) ); //$NON-NLS-1$

		// CLOSE THE TEMP STREAM PROVIDED TO THE CALLER
		try
		{
			// clean up the image map.
			imageMap = null;

			if ( fis != null )
			{
				fis.close( );
				fis = null;
			}
		}
		catch ( IOException ioex )
		{
			logger.log( ioex );
		}

		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemPresentationImpl.log.finishEnd" ) ); //$NON-NLS-1$
	}
}
