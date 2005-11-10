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

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public ChartReportItemPresentationImpl( )
	{
	}

	private boolean isOutputRendererSupported( String format )
	{
		if ( format != null )
		{
			if ( sSupportedFormats != null
					&& ( sSupportedFormats.indexOf( format ) != -1 ) )
			{
				IDeviceRenderer renderer = null;

				try
				{
					renderer = PluginSettings.instance( )
							.getDevice( "dv." + format ); //$NON-NLS-1$
				}
				catch ( Exception e )
				{
					renderer = null;
				}

				return ( renderer != null );
			}
		}

		return false;
	}

	private String getFirstNonSVGFormat( String formats )
	{
		if ( formats != null && formats.length( ) > 0 )
		{
			int idx = formats.indexOf( ';' );
			if ( idx == -1 )
			{
				if ( !"SVG".equals( formats ) ) //$NON-NLS-1$
				{
					return formats;
				}
			}
			else
			{
				String ext = formats.substring( 0, idx );

				if ( !"SVG".equals( ext ) ) //$NON-NLS-1$
				{
					return ext;
				}
				else
				{
					return getFirstNonSVGFormat( formats.substring( idx + 1 ) );
				}
			}
		}

		// JPG as default.
		return "JPG"; //$NON-NLS-1$
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
		rtc = new RunTimeContext( );
		rtc.setLocale( lcl );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setResolution(int)
	 */
	public void setResolution( int iDPI )
	{
		// UNUSED BY CHART EXTENSION
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
				sExtension = "SVG"; //$NON-NLS-1$
			}
			else
			{
				sExtension = getFirstNonSVGFormat( sSupportedFormats );
			}
		}
		else if ( sOutputFormat.equalsIgnoreCase( "PDF" ) ) //$NON-NLS-1$
		{
			sExtension = "JPEG"; //$NON-NLS-1$
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
			BIRTDataRowEvaluator rowAdapter = new BIRTDataRowEvaluator(  irsa[0], ibqda[0]);
			Generator.instance().bindData( rowAdapter, cm, rtc);
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

			// BUILD THE CHART
			// we must copy the bounds to avoid that setting it on one object
			// unsets it on its precedent container
			final Bounds bo = (Bounds) EcoreUtil.copy( cm.getBlock( )
					.getBounds( ) );
			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.PresentationUsesBoundsBo", bo ) ); //$NON-NLS-1$
			final Generator gr = Generator.instance( );
			GeneratedChartState gcs = null;
			gcs = gr.build( idr.getDisplayServer( ),
					cm,
					null,
					bo,
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