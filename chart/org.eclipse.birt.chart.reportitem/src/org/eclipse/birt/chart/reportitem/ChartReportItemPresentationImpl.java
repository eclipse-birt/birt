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

import org.eclipse.birt.chart.datafeed.ResultSetWrapper;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.ReportItemPresentationBase;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * 
 */
public final class ChartReportItemPresentationImpl extends
		ReportItemPresentationBase
{

	/**
	 * 
	 */
	private File fChartImage = null;

	/**
	 * 
	 */
	private FileInputStream fis = null;

	/**
	 * 
	 */
	private String sExtension = null;

	/**
	 * 
	 */
	private Chart cm = null;

	/**
	 * 
	 */
	private RunTimeContext rtc = null;

	/**
	 * 
	 */
	private IBaseQueryDefinition[] ibqda = null;

	/**
	 * 
	 */
	public ChartReportItemPresentationImpl( )
	{

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
			DefaultLoggerImpl.instance( ).log( e );
		}
		if ( item == null )
		{
			try
			{
				eih.loadExtendedElement( );
			}
			catch ( ExtendedElementException eeex )
			{
				DefaultLoggerImpl.instance( ).log( eeex );
			}
			item = ( (ExtendedItem) eih.getElement( ) ).getExtendedElement( );
			if ( item == null )
			{
				DefaultLoggerImpl.instance( )
						.log( ILogger.ERROR,
								Messages.getString( "ChartReportItemPresentationImpl.log.UnableToLocateWrapper" ) ); //$NON-NLS-1$
				return;
			}
		}
		cm = (Chart) ( (ChartReportItemImpl) item ).getProperty( "chart.instance" ); //$NON-NLS-1$
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
			sExtension = "PNG"; //$NON-NLS-1$
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
		// UNUSED BY CHART EXTENSION
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
		return OUTPUT_AS_IMAGE;
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
			// Data rows are empty
			throw new ChartException( ChartException.GENERATION,
					"ChartReportItemPresentationImpl.error.NoData", //$NON-NLS-1$
					ResourceBundle.getBundle( Messages.REPORT_ITEM,
							rtc.getLocale( ) ) );
		}

		DefaultLoggerImpl.instance( )
				.log( ILogger.INFORMATION,
						Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsStart" ) ); //$NON-NLS-1$

		try
		{
			final QueryHelper qh = QueryHelper.instance( rtc );
			// final ScriptHandler sh = rtc.getScriptHandler();
			// ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DATA_BINDING,
			// irsa[0]);
			final ResultSetWrapper rsw = qh.mapToChartResultSet( ibqda[0],
					irsa[0],
					cm );
			// ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DATA_BINDING,
			// rsw);

			// POPULATE THE CHART MODEL WITH THE RESULTSET
			qh.generateRuntimeSeries( cm, rsw );

			DefaultLoggerImpl.instance( )
					.log( ILogger.INFORMATION,
							Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsBuilding" ) ); //$NON-NLS-1$
			// SETUP A TEMP FILE FOR STREAMING
			try
			{
				fChartImage = File.createTempFile( "chart", "." + sExtension ); //$NON-NLS-1$ //$NON-NLS-2$
				DefaultLoggerImpl.instance( )
						.log( ILogger.INFORMATION,
								Messages.getString( "ChartReportItemPresentationImpl.log.WritingFile", //$NON-NLS-1$
										new Object[]{
												sExtension,
												fChartImage.getPath( )
										} ) );
			}
			catch ( IOException ioex )
			{
				throw new BirtException( Messages.getString( "ChartReportItemPresentationImpl.exception.tmpPngFileCreation" ), ioex ); //$NON-NLS-1$
			}

			// FETCH A HANDLE TO THE DEVICE RENDERER
			IDeviceRenderer idr = null;
			idr = PluginSettings.instance( ).getDevice( "dv." //$NON-NLS-1$
					+ sExtension.toUpperCase( Locale.US ) );

			// BUILD THE CHART
			// we must copy the bounds to avoid that setting it on one object
			// unsets it on its precedent container
			final Bounds bo = (Bounds) EcoreUtil.copy( cm.getBlock( )
					.getBounds( ) );
			DefaultLoggerImpl.instance( )
					.log( ILogger.INFORMATION,
							Messages.getString( "ChartReportItemPresentationImpl.log.PresentationUsesBoundsBo", bo ) ); //$NON-NLS-1$
			final Generator gr = Generator.instance( );
			GeneratedChartState gcs = null;
			gcs = gr.build( idr.getDisplayServer( ), cm, null, bo, rtc );

			// WRITE TO THE IMAGE FILE
			DefaultLoggerImpl.instance( )
					.log( ILogger.INFORMATION,
							Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsRendering" ) ); //$NON-NLS-1$
			idr.setProperty( IDeviceRenderer.FILE_IDENTIFIER,
					fChartImage.getPath( ) );

			gr.render( idr, gcs );

			// RETURN A STREAM HANDLE TO THE NEWLY CREATED IMAGE
			try
			{
				fis = new FileInputStream( fChartImage.getPath( ) );
			}
			catch ( IOException ioex )
			{
				throw new BirtException( Messages.getString( "ChartReportItemPresentationImpl.exception.inputStreamCreation" ), ioex ); //$NON-NLS-1$
			}
		}
		catch ( BirtException ex )
		{
			DefaultLoggerImpl.instance( )
					.log( ILogger.INFORMATION,
							Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsFailed" ) ); //$NON-NLS-1$
			DefaultLoggerImpl.instance( ).log( ex );
			throw ex;
		}
		catch ( RuntimeException ex )
		{
			DefaultLoggerImpl.instance( )
					.log( ILogger.INFORMATION,
							Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsFailed" ) ); //$NON-NLS-1$
			DefaultLoggerImpl.instance( ).log( ex );
			throw new BirtException( Messages.getString( "ChartReportItemPresentationImpl.exception.UnexpectedError" ), ex ); //$NON-NLS-1$
		}

		DefaultLoggerImpl.instance( )
				.log( ILogger.INFORMATION,
						Messages.getString( "ChartReportItemPresentationImpl.onRowSetsEnd" ) ); //$NON-NLS-1$
		return fis;
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
			DefaultLoggerImpl.instance( )
					.log( ILogger.INFORMATION,
							Messages.getString( "ChartReportItemPresentationImpl.log.getSizeStart" ) ); //$NON-NLS-1$
			final Size sz = new Size( );
			sz.setWidth( (float) cm.getBlock( ).getBounds( ).getWidth( ) );
			sz.setHeight( (float) cm.getBlock( ).getBounds( ).getHeight( ) );
			sz.setUnit( Size.UNITS_PT );
			DefaultLoggerImpl.instance( )
					.log( ILogger.INFORMATION,
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
		DefaultLoggerImpl.instance( )
				.log( ILogger.INFORMATION,
						Messages.getString( "ChartReportItemPresentationImpl.log.finishStart" ) ); //$NON-NLS-1$

		// CLOSE THE TEMP STREAM PROVIDED TO THE CALLER
		try
		{
			if ( fis != null )
				fis.close( );
		}
		catch ( IOException ioex )
		{
			DefaultLoggerImpl.instance( ).log( ioex );
		}

		// DELETE THE TEMP CHART IMAGE FILE CREATED
		if ( fChartImage != null )
		{
			if ( !fChartImage.delete( ) )
			{
				DefaultLoggerImpl.instance( )
						.log( ILogger.ERROR,
								Messages.getString( "ChartReportItemPresentationImpl.log.CouldNotDeleteTemp", //$NON-NLS-1$
										new Object[]{
												sExtension,
												fChartImage.getPath( )
										} ) );
			}
			else
			{
				DefaultLoggerImpl.instance( )
						.log( ILogger.INFORMATION,
								Messages.getString( "ChartReportItemPresentationImpl.log.SuccessfullyDeletedTemp", //$NON-NLS-1$
										new Object[]{
												sExtension,
												fChartImage.getPath( )
										} ) );
			}
		}
		DefaultLoggerImpl.instance( )
				.log( ILogger.INFORMATION,
						Messages.getString( "ChartReportItemPresentationImpl.log.finishEnd" ) ); //$NON-NLS-1$
	}

}