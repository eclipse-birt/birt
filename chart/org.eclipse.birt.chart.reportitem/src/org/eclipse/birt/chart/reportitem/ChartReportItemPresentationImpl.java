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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.ReportItemPresentationBase;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * ChartReportItemPresentationImpl
 */
public final class ChartReportItemPresentationImpl extends
		ReportItemPresentationBase
{

	private File fChartImage = null;

	private FileInputStream fis = null;

	private String imageMap = null;

	private String sExtension = null;

	private String sSupportedFormats = null;

	private String outputFormat = null;

	private Chart cm = null;

	private IDeviceRenderer idr = null;

	private DesignElementHandle handle;

	private RunTimeContext rtc = null;

	private IBaseQueryDefinition[] ibqda = null;

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
				if ( isOutputRendererSupported( formats ) ) //$NON-NLS-1$
				{
					return formats;
				}
			}
			else
			{
				String ext = formats.substring( 0, idx );

				if ( isOutputRendererSupported( ext ) ) //$NON-NLS-1$
				{
					return ext;
				}
				else
				{
					return getFirstSupportedFormat( formats.substring( idx + 1 ) );
				}
			}
		}

		// GIF as default.
		return "GIF"; //$NON-NLS-1$
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
			}
			catch ( ExtendedElementException eeex )
			{
				logger.log( eeex );
			}
			item = ( (ExtendedItem) eih.getElement( ) ).getExtendedElement( );
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
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setResolution(int)
	 */
	public void setResolution( int iDPI )
	{
		this.dpi = iDPI;

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
			// TODO remove when Report Engine passes correct resolution for PDF.
			this.dpi = 192;
		}
		else
		{
			sExtension = sOutputFormat;
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
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setReportQueries(org.eclipse.birt.data.engine.api.IBaseQueryDefinition[])
	 */
	public void setReportQueries( IBaseQueryDefinition[] ibqda )
	{
		this.ibqda = ibqda;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#onRowSets(org.eclipse.birt.report.engine.extension.IRowSet[])
	 */
	public Object onRowSets( IRowSet[] irsa ) throws BirtException
	{
		// BIND RESULTSET TO CHART DATASETS
		if ( irsa == null
				|| irsa.length != 1
				|| ibqda == null
				|| ibqda.length != 1
				|| irsa[0] == null )
		{
			// if the Data rows are null/empty, just log the error and returns
			// null gracefully.
			logger.log( new ChartException( ChartReportItemPlugin.ID,
					ChartException.GENERATION,
					"ChartReportItemPresentationImpl.error.NoData", //$NON-NLS-1$
					ResourceBundle.getBundle( Messages.REPORT_ITEM,
							rtc.getLocale( ) ) ) );
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

			BIRTDataRowEvaluator rowAdapter = new BIRTDataRowEvaluator( irsa[0],
					ibqda[0] );
			Generator.instance( ).bindData( rowAdapter,
					new BIRTActionEvaluator( ),
					cm,
					rtc );
			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsBuilding" ) ); //$NON-NLS-1$

			// SETUP A TEMP FILE FOR STREAMING
			try
			{
				fChartImage = File.createTempFile( "chart", "." + sExtension ); //$NON-NLS-1$ //$NON-NLS-2$
				logger.log( ILogger.INFORMATION,
						Messages.getString( "ChartReportItemPresentationImpl.log.WritingFile", //$NON-NLS-1$
								new Object[]{
										sExtension, fChartImage.getPath( )
								} ) );
			}
			catch ( IOException ioex )
			{
				throw new ChartException( ChartReportItemPlugin.ID,
						ChartException.GENERATION,
						ioex );
			}

			// FETCH A HANDLE TO THE DEVICE RENDERER
			idr = PluginSettings.instance( ).getDevice( "dv." //$NON-NLS-1$
					+ sExtension.toUpperCase( Locale.US ) );

			idr.setProperty( IDeviceRenderer.DPI_RESOLUTION, new Integer( dpi ) );
			// BUILD THE CHART
			final Bounds originalBounds = cm.getBlock( ).getBounds( );

			// we must copy the bounds to avoid that setting it on one object
			// unsets it on its precedent container

			final Bounds bo = (Bounds) EcoreUtil.copy( originalBounds );

			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.PresentationUsesBoundsBo", bo ) ); //$NON-NLS-1$

			final Generator gr = Generator.instance( );
			GeneratedChartState gcs = null;
			rtc.setActionRenderer( new BIRTActionRenderer( ) );

			gcs = gr.build( idr.getDisplayServer( ),
					cm,
					bo,
					new BIRTExternalContext( context ),
					rtc,
					new ChartReportStyleProcessor( handle ) );

			// WRITE TO THE IMAGE FILE
			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsRendering" ) ); //$NON-NLS-1$
			idr.setProperty( IDeviceRenderer.FILE_IDENTIFIER,
					fChartImage.getPath( ) );
			idr.setProperty( IDeviceRenderer.UPDATE_NOTIFIER,
					new EmptyUpdateNotifier( cm, gcs.getChartModel( ) ) );

			gr.render( idr, gcs );

			// RETURN A STREAM HANDLE TO THE NEWLY CREATED IMAGE
			try
			{
				fis = new FileInputStream( fChartImage.getPath( ) );
			}
			catch ( IOException ioex )
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
		catch ( BirtException ex )
		{
			if ( ex.getCause( ) instanceof ChartException
					&& ( (ChartException) ex.getCause( ) ).getType( ) == ChartException.ZERO_DATASET )
			{
				// if the Data set has zero lines, just log the error and
				// returns null gracefully.
				logger.log( ex );
				return null;
			}

			if ( ( ex.getCause( ) instanceof ChartException && ( (ChartException) ex.getCause( ) ).getType( ) == ChartException.INVALID_IMAGE_SIZE )
					|| ( ex instanceof ChartException && ( (ChartException) ex ).getType( ) == ChartException.INVALID_IMAGE_SIZE ) )
			{
				// if the image size is invalid, this may caused by
				// Display=None, lets ignore it.
				logger.log( ex );
				return null;
			}

			logger.log( ILogger.ERROR,
					Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsFailed" ) ); //$NON-NLS-1$
			logger.log( ex );
			throw ex;
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

		// DELETE THE TEMP CHART IMAGE FILE CREATED
		if ( fChartImage != null )
		{
			if ( !fChartImage.delete( ) )
			{
				logger.log( ILogger.ERROR,
						Messages.getString( "ChartReportItemPresentationImpl.log.CouldNotDeleteTemp", //$NON-NLS-1$
								new Object[]{
										sExtension, fChartImage.getPath( )
								} ) );
			}
			else
			{
				logger.log( ILogger.INFORMATION,
						Messages.getString( "ChartReportItemPresentationImpl.log.SuccessfullyDeletedTemp", //$NON-NLS-1$
								new Object[]{
										sExtension, fChartImage.getPath( )
								} ) );
			}
		}
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemPresentationImpl.log.finishEnd" ) ); //$NON-NLS-1$
	}

}