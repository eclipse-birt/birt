/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.birt.chart.computation.withaxes.ScaleContext;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.ReportItemPresentationBase;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Base presentation implementation for Chart. This class can be extended for
 * various implementation.
 */
public class ChartReportItemPresentationBase extends ReportItemPresentationBase
{

	protected InputStream fis = null;

	protected String imageMap = null;

	protected String sExtension = null;

	private String sSupportedFormats = null;

	private String outputFormat = null;

	private int outputType = -1;

	protected Chart cm = null;

	protected IDeviceRenderer idr = null;

	protected ExtendedItemHandle handle;

	protected RunTimeContext rtc = null;

	private static List registeredDevices = null;

	protected static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	static
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
		IReportItem item = getReportItem( eih );
		if ( item == null )
		{
			return;
		}
		cm = (Chart) ( (ChartReportItemImpl) item ).getProperty( ChartReportItemUtil.PROPERTY_CHART );
		handle = eih;

		Object of = handle.getProperty( ChartReportItemUtil.PROPERTY_OUTPUT );
		if ( of instanceof String )
		{
			outputFormat = (String) of;
		}

		of = ( (ChartReportItemImpl) item ).getProperty( ChartReportItemUtil.PROPERTY_SCALE );
		if ( of instanceof ScaleContext )
		{
			if ( rtc == null )
			{
				rtc = new RunTimeContext( );
			}
			rtc.setScale( (ScaleContext) of );
		}
	}

	protected IReportItem getReportItem( ExtendedItemHandle eih )
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
			}
		}
		return item;
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
			if ( outputFormat != null
					&& outputFormat.toUpperCase( ).equals( "SVG" ) ) //$NON-NLS-1$
			{
				// Since engine doesn't support embedding SVG, always embed PNG
				sExtension = "PNG"; //$NON-NLS-1$
			}
			else if ( isOutputRendererSupported( outputFormat ) )
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
					drtc.setULocale( rtc.getULocale( ) );
					drtc.setScale( rtc.getScale( ) );
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
					( (ChartReportItemImpl) item ).setModel( cm );
					( (ChartReportItemImpl) item ).setScale( rtc.getScale( ) );
				}

				// Get chart max row number from application context
				Object oMaxRow = context.getAppContext( )
						.get( EngineConstants.PROPERTY_EXTENDED_ITEM_MAX_ROW );
				if ( oMaxRow != null )
				{
					rtc.putState( ChartUtil.CHART_MAX_ROW, oMaxRow );
				}
				else
				{
					// Get chart max row number from global variables if app
					// context doesn't put it
					oMaxRow = context.getGlobalVariable( EngineConstants.PROPERTY_EXTENDED_ITEM_MAX_ROW );
					if ( oMaxRow != null )
					{
						rtc.putState( ChartUtil.CHART_MAX_ROW, oMaxRow );
					}
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
		if ( outputType == -1 )
		{
			if ( "SVG".equals( sExtension ) || "SWF".equals( sExtension ) ) //$NON-NLS-1$ //$NON-NLS-2$
			{
				outputType = OUTPUT_AS_IMAGE;
			}
			else
			{
				outputType = OUTPUT_AS_IMAGE_WITH_MAP;
			}
		}
		return outputType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#getImageMIMEType()
	 */
	public String getImageMIMEType( )
	{
		return idr.getMimeType( );
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

		// Dispose renderer resources
		if ( idr != null )
		{
			idr.dispose( );
			idr = null;
		}

		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemPresentationImpl.log.finishEnd" ) ); //$NON-NLS-1$
	}

	protected Bounds computeBounds( )
	{
		// Standard computation for chart bounds
		final Bounds originalBounds = cm.getBlock( ).getBounds( );

		// we must copy the bounds to avoid that setting it on one object
		// unsets it on its precedent container

		Bounds bounds = (Bounds) EcoreUtil.copy( originalBounds );
		return bounds;
	}

	protected IDataRowExpressionEvaluator createEvaluator( IBaseResultSet set )
	{
		if ( set instanceof IQueryResultSet )
		{
			List groups = ( (IQueryResultSet) set ).getResultIterator( )
					.getQueryResults( )
					.getPreparedQuery( )
					.getReportQueryDefn( )
					.getGroups( );

			if ( groups != null && groups.size( ) > 0 )
			{
				return new BIRTGroupedDataRowExpressionEvaluator( (IQueryResultSet) set );
			}
			return new BIRTQueryResultSetEvaluator( (IQueryResultSet) set );
		}
		// if ( set instanceof ICubeResultSet )
		// {
		// return new BIRTCubeResultSetEvaluator( (ICubeResultSet) set );
		// }
		return null;
	}

	protected boolean isEmpty( IBaseResultSet set )
	{
		if ( set instanceof IQueryResultSet )
		{
			try
			{
				return ( (IQueryResultSet) set ).isEmpty( );
			}
			catch ( BirtException e )
			{
				logger.log( e );
			}
		}
		// TODO add code to check empty for ICubeResultSet
		return false;
	}

	protected ScaleContext createSharedScale( IBaseResultSet baseResultSet )
			throws BirtException
	{
		Object min = baseResultSet.evaluate( "row._outer[\"" //$NON-NLS-1$
				+ ChartReportItemUtil.QUERY_MIN + "\"]" ); //$NON-NLS-1$
		Object max = baseResultSet.evaluate( "row._outer[\"" //$NON-NLS-1$
				+ ChartReportItemUtil.QUERY_MAX + "\"]" ); //$NON-NLS-1$
		return ScaleContext.createSimpleScale( min, max );
	}
}
