/***********************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.awt.Toolkit;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Locale;

import javax.olap.OLAPException;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.computation.withaxes.SharedScaleContext;
import org.eclipse.birt.chart.device.EmptyUpdateNotifier;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IImageMapEmitter;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IActionEvaluator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.AttributeFactoryImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.script.ChartScriptContext;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.data.dte.CubeResultSet;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.ReportItemPresentationBase;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.mozilla.javascript.EvaluatorException;

/**
 * Base presentation implementation for Chart. This class can be extended for
 * various implementation.
 */
public class ChartReportItemPresentationBase extends ReportItemPresentationBase implements
		ChartReportItemConstants
{

	protected InputStream fis = null;

	protected String imageMap = null;

	protected String sExtension = null;

	protected String outputChartFormat = ""; //$NON-NLS-1$

	protected int outputType = -1;

	protected Chart cm = null;

	protected IDeviceRenderer idr = null;

	protected RunTimeContext rtc = null;

	private static List<String> registeredDevices = null;

	protected static final ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	private Bounds boundsRuntime = null;
	
	private boolean validCubeResultSet = true;

	protected int renderDpi = 96;
	
	protected final ExpressionCodec exprCodec = ChartModelHelper.instance( )
			.createExpressionCodec( );

	static
	{
		registeredDevices = new ArrayList<String>( );
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
	
	protected static IDataRowExpressionEvaluator EMPTY_CHART_EVALUATOR = new IDataRowExpressionEvaluator( ) {

		public void close( )
		{

		}

		public Object evaluate( String expression )
		{
			return null;
		}

		@SuppressWarnings("deprecation")
		public Object evaluateGlobal( String expression )
		{
			return null;
		}

		public boolean first( )
		{
			// No row
			return false;
		}

		public boolean next( )
		{
			return false;
		}
	};

	/**
	 * check if the format is supported by the browser and device renderer.
	 */
	protected boolean isOutputRendererSupported( String format )
	{
		if ( format != null
				&& supportedImageFormats != null
				&& ( supportedImageFormats.indexOf( format ) != -1 ) )
		{
			return registeredDevices.contains( format );
		}
		return false;
	}

	protected String getFirstSupportedFormat( )
	{
		if ( supportedImageFormats != null
				&& supportedImageFormats.trim( ).length( ) > 0 )
		{
			String[] array = supportedImageFormats.split( ";" ); //$NON-NLS-1$
			if ( array.length > 0 )
			{
				for ( int i = 0; i < array.length; i++ )
				{
					if ( isOutputRendererSupported( array[i] ) )
					{
						return array[i];
					}
				}
			}
		}

		// PNG as default.
		return "PNG"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.extension.IReportItemPresentation#
	 * setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject( ExtendedItemHandle eih )
	{
		super.setModelObject( eih );

		final IReportItem item = getReportItem( eih );
		if ( item == null )
		{
			return;
		}
		cm = (Chart) item.getProperty( PROPERTY_CHART );
		
		// Add lock to avoid concurrent exception from EMF. IReportItem has one
		// design time chart model that could be shared by multiple
		// presentation instance, but only allows one copy per item
		// concurrently.
		synchronized ( item )
		{
			// Must copy model here to generate runtime data later
			if ( cm != null )
			{
				try
				{
					cm = cm.copyInstance( );
				}
				catch ( ConcurrentModificationException e )
				{
					// Once concurrent exception is thrown, try again.
					cm = cm.copyInstance( );
				}
				catch ( NullPointerException e )
				{
					// Once NPE is thrown in concurrent case, try again.
					cm = cm.copyInstance( );
				}
			}
		}
		// #269935
		// If it is sharing chart case, copy expressions settings from referred
		// chart model into current.
		if ( cm != null
				&& eih.getDataBindingReference( ) != null
				&& ChartItemUtil.isChartHandle( eih.getDataBindingReference( ) ) )
		{
			ExtendedItemHandle refHandle = ChartItemUtil.getChartReferenceItemHandle( eih );
			if ( refHandle != null )
			{
				ChartReportItemUtil.copyChartSeriesDefinition( ChartItemUtil.getChartFromHandle( refHandle ),
						cm );
			}
		}

		setChartModelObject( item );
	}

	protected void setChartModelObject( IReportItem item )
	{
		Object of = modelHandle.getProperty( PROPERTY_OUTPUT );
		if ( of instanceof String )
		{
			outputChartFormat = (String) of;
		}

		of = item.getProperty( PROPERTY_SCALE );
		if ( of instanceof SharedScaleContext )
		{
			if ( rtc == null )
			{
				rtc = new RunTimeContext( );
			}
			rtc.setSharedScale( (SharedScaleContext) of );
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
	 * @see
	 * org.eclipse.birt.report.engine.extension.IReportItemPresentation#setLocale
	 * (java.util.Locale)
	 */
	@SuppressWarnings("deprecation")
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
	 * @seeorg.eclipse.birt.report.engine.extension.IReportItemPresentation#
	 * setOutputFormat(java.lang.String)
	 */
	public void setOutputFormat( String sOutputFormat )
	{
		super.setOutputFormat( sOutputFormat );
		if ( sOutputFormat.equalsIgnoreCase( "HTML" ) ) //$NON-NLS-1$
		{
			if ( isOutputRendererSupported( outputChartFormat ) )
			{
				sExtension = outputChartFormat;
			}
			else if ( outputChartFormat.equalsIgnoreCase( "GIF" ) && //$NON-NLS-1$ 
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
				sExtension = getFirstSupportedFormat( );
			}
		}
		else
		{
			if ( isOutputRendererSupported( outputChartFormat ) )
			{
				sExtension = outputChartFormat;
			}
			else
			{
				sExtension = getFirstSupportedFormat( );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.extension.IReportItemPresentation#deserialize
	 * (java.io.InputStream)
	 */
	public void deserialize( InputStream is )
	{
		try
		{
			ObjectInputStream ois = new ObjectInputStream( is ) {

				// Fix compatibility bug: the class ChartScriptContext is moved
				// from package
				// "org.eclipse.birt.chart.internal.script" to package
				// "org.eclipse.birt.chart.script", which causes the stored
				// instances of
				// ChartScriptContext can't be de-serialized.
				protected Class<?> resolveClass( ObjectStreamClass desc )
						throws IOException, ClassNotFoundException
				{
					if ( "org.eclipse.birt.chart.internal.script.ChartScriptContext".equals( desc.getName( ) ) ) //$NON-NLS-1$
					{
						return ChartScriptContext.class;
					}
					return super.resolveClass( desc );
				}
			};
			Object o = SecurityUtil.readObject( ois );

			if ( o instanceof RunTimeContext )
			{
				RunTimeContext drtc = (RunTimeContext) o;

				if ( rtc != null )
				{
					drtc.setULocale( rtc.getULocale( ) );
					drtc.setSharedScale( rtc.getSharedScale( ) );
				}

				rtc = drtc;
			}
			
			// Apply configuration to runtime context
			if ( cm != null && modelHandle != null )
			{
				IReportItem item = modelHandle.getReportItem( );
				( (ChartReportItemImpl) item ).setSharedScale( rtc.getSharedScale( ) );
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
			
			// Get time out setting from app context.
			Object chartConvertTimeOut = context.getAppContext( )
					.get( ChartItemUtil.BIRT_CHART_CONVERT_TO_IMAGE_TIME_OUT );
			if ( chartConvertTimeOut != null )
			{
				rtc.putState( ChartItemUtil.BIRT_CHART_CONVERT_TO_IMAGE_TIME_OUT, chartConvertTimeOut );
			}
			else
			{
				// Get time out setting if app context doesn't put it
				chartConvertTimeOut = context.getGlobalVariable( ChartItemUtil.BIRT_CHART_CONVERT_TO_IMAGE_TIME_OUT );
				if ( chartConvertTimeOut != null )
				{
					rtc.putState( ChartItemUtil.BIRT_CHART_CONVERT_TO_IMAGE_TIME_OUT, chartConvertTimeOut );
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
	 * @seeorg.eclipse.birt.report.engine.extension.IReportItemPresentation#
	 * getOutputType()
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
	 * @seeorg.eclipse.birt.report.engine.extension.IReportItemPresentation#
	 * getImageMIMEType()
	 */
	public String getImageMIMEType( )
	{
		if ( idr == null )
		{
			return null;
		}

		return idr.getMimeType( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.extension.IReportItemPresentation#
	 * getOutputContent()
	 */
	public Object getOutputContent( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.extension.IReportItemPresentation#finish()
	 */
	public void finish( )
	{
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
	}

	protected Bounds computeBounds( ) throws ChartException
	{
		// Standard computation for chart bounds
		final Bounds originalBounds = cm.getBlock( ).getBounds( );

		// we must copy the bounds to avoid that setting it on one object
		// unsets it on its precedent container

		Bounds bounds = originalBounds.copyInstance( );
		if ( !bounds.isSetHeight( ) )
		{
			bounds.setHeight( ChartReportItemConstants.DEFAULT_CHART_BLOCK_HEIGHT );
		}
		if ( !bounds.isSetWidth( ) )
		{
			bounds.setWidth( ChartReportItemConstants.DEFAULT_CHART_BLOCK_WIDTH );
		}
		return bounds;
	}

	protected IDataRowExpressionEvaluator createEvaluator( IBaseResultSet set )
			throws BirtException
	{
		if ( set instanceof IQueryResultSet )
		{
			// COMPATIBILITY REVISION: SCR#100059, 2008-04-10
			// Since the DtE integration, some old report documents are not
			// compatible with current evaluator of expressions, old report
			// documents don't contains additional aggregation binding which
			// is created by chart at runtime and is used to evaluate by. So
			// here checks the version number( <3.2.16, before BIRT 2.3 ) of
			// report model to use old evaluator logic for chart and chart
			// does group/aggregation by itself.
			if ( ChartReportItemUtil.isOldChartUsingInternalGroup( modelHandle,
					cm ) )
			{
				// Version is less than 3.2.16, directly return.
				return new BIRTQueryResultSetEvaluator( (IQueryResultSet) set );
			}

			boolean isSharingChart = modelHandle.getDataBindingReference( ) != null
					&& ChartItemUtil.isChartHandle( modelHandle.getDataBindingReference( ) );
			// Here, we must use chart model to check if grouping is defined. we
			// can't use grouping definitions in IQueryResultSet to check it,
			// because maybe chart inherits data set from container and the data
			// set contains grouping, but chart doesn't define grouping.
			if ( ChartItemUtil.isGroupingDefined( cm )
					|| ChartItemUtil.hasAggregation( cm )
					|| isSharingChart )
			{
				return createGroupedResultSetEvalator( set );
			}
			return new BIRTQueryResultSetEvaluator( (IQueryResultSet) set );
		}
		else if ( set instanceof ICubeResultSet )
		{
			if ( ChartCubeUtil.isPlotChart( modelHandle )
					|| ChartCubeUtil.isAxisChart( modelHandle ) )
			{
				return new BIRTChartXtabResultSetEvaluator( (ICubeResultSet) set,
						modelHandle );
			}

			// Sharing case/Multiple view case/in xtab header cell
			ReportItemHandle itemHandle = ChartItemUtil.getReportItemReference( modelHandle );
			boolean isChartCubeReference = ChartItemUtil.isChartReportItemHandle( itemHandle );
			CrosstabCellHandle xtabCell = ChartCubeUtil.getXtabContainerCell( modelHandle, false );
			if ( itemHandle != null
					&& !isChartCubeReference
					|| xtabCell != null
					&& !( xtabCell instanceof AggregationCellHandle ) )
			{
				return createSharedCubeRSEvaluator( set );
			}

			return ChartReportItemUtil.instanceCubeEvaluator( modelHandle,
					cm,
					(ICubeResultSet) set );
		}
		// Use empty evaluator if result set is null
		return EMPTY_CHART_EVALUATOR;
	}

	protected IDataRowExpressionEvaluator createGroupedResultSetEvalator(
			IBaseResultSet set ) throws ChartException
	{
		return new BIRTGroupedQueryResultSetEvaluator( (IQueryResultSet) set,
				ChartItemUtil.isSetSummaryAggregation( cm ),
				isSubQuery( ),
				cm,
				modelHandle );
	}

	protected IDataRowExpressionEvaluator createSharedCubeRSEvaluator(
			IBaseResultSet set )
	{
		return new SharedCubeResultSetEvaluator( (ICubeResultSet) set,
				cm );
	}

	protected boolean isSubQuery( )
	{
		return modelHandle.getDataSet( ) == null;
	}

	@SuppressWarnings("unchecked")
	protected SharedScaleContext createSharedScale( IBaseResultSet baseResultSet )
			throws BirtException
	{
		if ( baseResultSet instanceof IQueryResultSet )
		{
			Object min = baseResultSet.evaluate( "row._outer[\"" //$NON-NLS-1$
					+ NAME_QUERY_MIN
					+ "\"]" ); //$NON-NLS-1$
			Object max = baseResultSet.evaluate( "row._outer[\"" //$NON-NLS-1$
					+ NAME_QUERY_MAX
					+ "\"]" ); //$NON-NLS-1$
			return SharedScaleContext.createInstance( min, max );
		}
		else if ( baseResultSet instanceof CubeResultSet )
		{
			// Gets global min/max and set the value to shared scale
			try
			{
				List<EdgeCursor> edgeCursors = ( (CubeResultSet) baseResultSet ).getCubeCursor( )
						.getOrdinateEdge( );
				for ( EdgeCursor edge : edgeCursors )
				{
					edge.first( );
				}
				Axis xAxis = ( (ChartWithAxes) cm ).getAxes( ).get( 0 );
				SeriesDefinition sdValue = ( (ChartWithAxes) cm ).getOrthogonalAxes( xAxis,
						true )[0].getSeriesDefinitions( ).get( 0 );
				Query queryValue = sdValue.getDesignTimeSeries( )
						.getDataDefinition( )
						.get( 0 );
				String bindingValue = exprCodec.getCubeBindingName( queryValue.getDefinition( ),
						false );
				String maxBindingName = NAME_QUERY_MAX + bindingValue;
				String minBindingName = NAME_QUERY_MIN + bindingValue;
				Object min = baseResultSet.evaluate( ExpressionUtil.createJSDataExpression( minBindingName ) );
				Object max = baseResultSet.evaluate( ExpressionUtil.createJSDataExpression( maxBindingName ) );
				if ( min != null && max != null )
				{
					return SharedScaleContext.createInstance( min, max );
				}
			}
			catch ( OLAPException e )
			{
				// Skip shared scale, still running
				logger.log( e );
			}
			catch ( BirtException e )
			{
				// If chart doesn't use sub cube query, shared scale is not
				// required. No need to get min/max
			}
			catch ( EvaluatorException e )
			{
				// If chart doesn't use sub cube query, shared scale is not
				// required. No need to get min/max
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.extension.IReportItemPresentation#getSize
	 * ()
	 */
	public Size getSize( )
	{
		// Use actual bounds to set size
		if ( boundsRuntime != null )
		{
			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.getSizeStart" ) ); //$NON-NLS-1$
			final Size sz = new Size( );
			sz.setWidth( (float) boundsRuntime.getWidth( ) );
			sz.setHeight( (float) boundsRuntime.getHeight( ) );
			sz.setUnit( Size.UNITS_PT );
			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemPresentationImpl.log.getSizeEnd" ) ); //$NON-NLS-1$
			return sz;
		}
		return super.getSize( );
	}

	/*
	 * Wraps the logic to bind data. Returns true if the data set is not empty,
	 * otherwise false.
	 */
	protected boolean bindData( IDataRowExpressionEvaluator rowAdapter,
			IActionEvaluator evaluator ) throws BirtException
	{
		boolean bNotEmpty = true;
		try
		{
			// Bind Data to series
			Generator.instance( ).bindData( rowAdapter, evaluator, cm, rtc );
			bNotEmpty = true;
		}
		catch ( BirtException birtException )
		{
			// Check if the exception is caused by no data to display (in that
			// case skip gracefully)
			if ( isNoDataException( birtException ) )
			{
				bNotEmpty = false;
			}
			else
			{
				if ( !ChartReportItemUtil.validateCubeResultSetBinding( modelHandle,
						cm ) )
				{
					validCubeResultSet = false;
				}
				else
				{
					throw birtException;
				}
			}
		}

		rtc.putState( RunTimeContext.StateKey.DATA_EMPTY_KEY, !bNotEmpty );
		return bNotEmpty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.extension.IReportItemPresentation#onRowSets
	 * (org.eclipse.birt.report.engine.extension.IRowSet[])
	 */
	public Object onRowSets( IBaseResultSet[] baseResultSet )
			throws BirtException
	{
		if ( cm == null )
		{
			return null;
		}
		// Extract result set to render and check for null data
		IBaseResultSet resultSet = getDataToRender( baseResultSet );

		// Display blank if data empty.
		boolean bEmpty = false;
		boolean bEmptyWithUncompletedBindings  = false;
		boolean bEmptyWithEmptyResultSet = false;
		
		// Display alt text if binding is not complete
		if ( resultSet == null || !ChartItemUtil.checkChartBindingComplete( cm ) )
		{
			bEmpty = true;
			bEmptyWithUncompletedBindings = true;
		}
		if ( !bEmpty && ChartReportItemUtil.isEmpty( resultSet ) )
		{
			bEmpty = true;
			bEmptyWithEmptyResultSet = true;
		}

		// If width and height of chart is set to 0, doesn't process it.
		Bounds bo = cm.getBlock( ).getBounds( );
		if ( bo.getWidth( ) == 0 && bo.getHeight( ) == 0 && ( bo.isSetHeight( ) || bo.isSetWidth( ) ))
		{
			return null;
		}
		
		renderDpi = getRenderDpi( );

		try
		{
			rtc.setTimeZone( context.getTimeZone( ) );
			
			// Create and set shared scale if needed
			if ( rtc.getSharedScale( ) == null
					&& ChartReportItemUtil.canScaleShared( modelHandle, cm ) )
			{
				rtc.setSharedScale( createSharedScale( resultSet ) );
			}

			// Set sharing query flag.
			boolean isSharingQuery = false;
			if ( modelHandle.getDataBindingReference( ) != null
					|| modelHandle.getContainer( ) instanceof MultiViewsHandle
					|| ChartItemUtil.isChartInheritGroups( modelHandle ) )
			{
				isSharingQuery = true;
				// Here we will set isSharingQuery to false if it is sharing
				// chart case to ensure that chart generates correct expressions
				// to do evaluating in chart generator phase.
				isSharingQuery &= !ChartItemUtil.isChartHandle( ChartItemUtil.getReportItemReference( modelHandle ));
			}
			rtc.setSharingQuery( isSharingQuery );

			// Get the BIRT report context
			BIRTExternalContext externalContext = new BIRTExternalContext( context );

			// Initialize external context, which can be used by script during
			// binding data
			if ( rtc.getScriptContext( ) != null
					&& rtc.getScriptContext( ) instanceof ChartScriptContext )
			{
				( (ChartScriptContext) rtc.getScriptContext( ) ).setExternalContext( externalContext );
			}

			// Initialize script handler and register birt context in scope
			initializeScriptHandler( externalContext );

			// Prepare Data for Series
			IDataRowExpressionEvaluator rowAdapter = createEvaluator( resultSet );

			// Prepare data processor for hyperlinks/tooltips
			IActionEvaluator evaluator = new BIRTActionEvaluator( );
			
			ChartReportStyleProcessor styleProcessor = new ChartReportStyleProcessor( modelHandle,
					true,
					this.style,
					this.dpi,
					rtc.getULocale( ) );

			styleProcessor.applyDefaultHyperlink( this.cm );

			// Update chart model if needed
			updateChartModel( );

			// Bind Data to series
			boolean bEmptyData = false;
			if ( !bEmptyWithUncompletedBindings && !bindData( rowAdapter, evaluator ) )
			{
				bEmptyData = true;
			}

			if ( !validCubeResultSet )
			{
				this.outputType = OUTPUT_AS_HTML_TEXT;
				return "<p align='left'>" //$NON-NLS-1$
						+ Messages.getString( "ChartReportItemPresentationImpl.error.InvalidCubeBinding" ) //$NON-NLS-1$
						+ "</p>"; //$NON-NLS-1$
			}
			// Render chart
			Object renderObject = generateRenderObject( rowAdapter,
					externalContext,
					bEmpty || bEmptyData,
					styleProcessor );

			// Close the dataRow evaluator. It needs to stay opened until the
			// chart is fully rendered.
			rowAdapter.close( );
			
			// Process empty cases.
			if ( bEmpty )
			{
				if ( bEmptyWithUncompletedBindings && isAutoHide( ) )
				{
					// Set bounds and idr to null to avoid return wrong chart size to
					// report engine.
					boundsRuntime = null;
					idr = null;
					// Null result set
					return outputNullResultSet( );
				}
				if ( bEmptyWithEmptyResultSet && isAutoHide( ) )
				{
					// Set bounds and idr to null to avoid return wrong chart size to
					// report engine.
					boundsRuntime = null;
					idr = null;
					// Returns null for engine to display empty when the result
					// set is empty.
					return null;
				}
			}
			if ( bEmptyData && isAutoHide( ) )
			{
				// Set bounds and idr to null to avoid return wrong chart size to
				// report engine.
				boundsRuntime = null;
				idr = null;
				return null;
			}
			
			return renderObject;
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

	protected Object generateRenderObject(
			IDataRowExpressionEvaluator rowAdapter,
			BIRTExternalContext externalContext, boolean bEmpty,
			IStyleProcessor externalProcessor ) throws ChartException
	{
		if ( externalProcessor == null )
		{
			externalProcessor = new ChartReportStyleProcessor( modelHandle,
					true,
					this.style,
					this.dpi );
		}
		// Prepare Device Renderer
		prepareDeviceRenderer( );

		// Build the chart
		GeneratedChartState gcs = buildChart( rowAdapter,
				externalContext,
				externalProcessor );

		// Render the chart
		renderToImageFile( gcs );

		// Set the scale shared when scale has been computed, and store it
		// in the ReportItem
		if ( rtc.getSharedScale( ) != null && !rtc.getSharedScale( ).isShared( ) )
		{
			rtc.getSharedScale( ).setShared( true );
			( (ChartReportItemImpl) getReportItem( modelHandle ) ).setSharedScale( rtc.getSharedScale( ) );
		}

		// Returns the content to display (image or image+imagemap)
		return getImageToDisplay( );
	}

	/**
	 * Check there is some data to display in the chart.
	 * 
	 * @param baseResultSet
	 * @return null if nothing to render
	 */
	protected IBaseResultSet getDataToRender( IBaseResultSet[] baseResultSet )
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
		if ( resultSet == null )
		{
			// Do nothing when IBaseResultSet is empty or null
			return null;
		}

		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemPresentationImpl.log.onRowSetsStart" ) ); //$NON-NLS-1$

		// catch unwanted null handle case
		if ( modelHandle == null )
		{
			assert false; // should we throw an exception here instead?
			return null;
		}
		return resultSet;
	}

	@SuppressWarnings("deprecation")
	private void initializeScriptHandler( BIRTExternalContext externalContext )
			throws ChartException
	{
		String javaHandlerClass = modelHandle.getEventHandlerClass( );
		if ( javaHandlerClass != null && javaHandlerClass.length( ) > 0 )
		{
			// use java handler if available.
			cm.setScript( javaHandlerClass );
		}

		rtc.setScriptClassLoader( new BIRTScriptClassLoader( appClassLoader ) );
		// INITIALIZE THE SCRIPT HANDLER
		// UPDATE THE CHART SCRIPT CONTEXT

		ScriptHandler sh = (ScriptHandler) rtc.getScriptHandler( );

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
				sh.register( ModuleUtil.getScriptUID( modelHandle.getPropertyHandle( IReportItemModel.ON_RENDER_METHOD ) ),
						sScriptContent );
			}
		}
	}

	protected GeneratedChartState buildChart(
			IDataRowExpressionEvaluator rowAdapter,
			BIRTExternalContext externalContext,
			IStyleProcessor externalProcessor ) throws ChartException
	{
		final Bounds bo = computeBounds( );

		initializeRuntimeContext( rowAdapter, bo );

		GeneratedChartState gcs = Generator.instance( )
				.build( idr.getDisplayServer( ),
						cm,
						bo,
						externalContext,
						rtc,
						externalProcessor );
		boundsRuntime = gcs.getChartModel( ).getBlock( ).getBounds( );
		return gcs;
	}

	protected Object getImageToDisplay( )
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
			imageMap = getImageMap();
		}

	}
	
	/**
	 * Returns the HTML <area> contents of image hotspots.
	 * @return
	 */
	protected String getImageMap( ) throws ChartException {
		return ( (IImageMapEmitter) idr ).getImageMap( );
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
			IDataRowExpressionEvaluator rowAdapter, Bounds bo )
	{
		rtc.setActionRenderer( ChartReportItemUtil.instanceActionRenderer( modelHandle,
				this.ah,
				rowAdapter,
				this.context ) );
		rtc.setMessageLookup( new BIRTMessageLookup( context ) );

		// Set direction from model to chart runtime context
		rtc.setRightToLeftText( modelHandle.isDirectionRTL( ) );
		// Set text direction from StyleHandle to chart runtime context
		ChartReportItemImpl crii = (ChartReportItemImpl) getReportItem( modelHandle );
		rtc.setRightToLeft( crii.isLayoutDirectionRTL( ) );
		rtc.setResourceFinder( crii );
		rtc.setExternalizer( crii );
		
		if ( rtc.getSharedScale( ) != null && canUpdateScale( ) )
		{
			rtc.getSharedScale( ).updateBounds( bo );
		}
	}

	protected void prepareDeviceRenderer( ) throws ChartException
	{
		idr = ChartEngine.instance( ).getRenderer( "dv." //$NON-NLS-1$
				+ sExtension.toUpperCase( Locale.US ) );

		idr.setProperty( IDeviceRenderer.DPI_RESOLUTION, Integer.valueOf( dpi ) );
		
		idr.setProperty( "output.format", outputFormat ); //$NON-NLS-1$
		
		idr.getDisplayServer( ).setLocale( rtc.getULocale( ) );
		
		// Enable alt value in image map
		if ( isAreaAltEnabled( ) )
		{
			idr.setProperty( IDeviceRenderer.AREA_ALT_ENABLED, Boolean.TRUE );
		}
		
		// SVG resize
		if ( "SVG".equalsIgnoreCase( sExtension ) ) //$NON-NLS-1$
		{
			idr.setProperty( "resize.svg", Boolean.TRUE ); //$NON-NLS-1$
		}

	}
	
	protected final boolean isAreaAltEnabled( )
	{
		ExtendedProperty altEnabled = ChartUtil.getExtendedProperty( cm,
				IDeviceRenderer.AREA_ALT_ENABLED );
		return altEnabled != null && Boolean.valueOf( altEnabled.getValue( ) );
	}

	/**
	 * Updates chart model when something needs change
	 */
	protected void updateChartModel( )
	{
		// Update Triggers
		if ( isAreaAltEnabled( ) )
		{
			// If tooltips are not defined, must set a dummy one to output image
			// map with alt attribute. SVG doesn't need image map, so ignore it.
			if ( !"SVG".equalsIgnoreCase( sExtension ) ) //$NON-NLS-1$
			{
				for ( SeriesDefinition vsd : ChartUtil.getAllOrthogonalSeriesDefinitions( cm ) )
				{
					boolean bTooltipsFound = false;
					List<Trigger> triggers = vsd.getDesignTimeSeries( )
							.getTriggers( );
					for ( Trigger trigger : triggers )
					{
						if ( trigger.getAction( ).getType( ) == ActionType.SHOW_TOOLTIP_LITERAL )
						{
							bTooltipsFound = true;
							break;
						}
					}
					if ( !bTooltipsFound )
					{
						TooltipValue tv = AttributeFactoryImpl.init( )
								.createTooltipValue( );
						Trigger t = TriggerImpl.create( TriggerCondition.ONMOUSEOVER_LITERAL,
								ActionImpl.create( ActionType.SHOW_TOOLTIP_LITERAL,
										tv ) );
						triggers.add( t );
					}
				}
			}
		}
		
		// Update bounds from handle
		// Here use render dpi to convert pixels since it's used to locate in
		// report.
		Bounds b = ChartItemUtil.computeChartBounds( modelHandle, renderDpi );
		if ( b != null )
		{
			cm.getBlock( ).setBounds( b );
		}
	}
	
	/**
	 * Gets render dpi which is used to locate and size in report.
	 * 
	 * @return render dpi
	 */
	protected int getRenderDpi( )
	{
		Object renderDpi = context.getRenderOption( )
				.getOption( IRenderOption.RENDER_DPI );
		if ( renderDpi instanceof Integer )
		{
			return (Integer) renderDpi;
		}
		if ( modelHandle.getRoot( ) instanceof ReportDesignHandle )
		{
			int imageDpi = ( (ReportDesignHandle) modelHandle.getRoot( ) ).getImageDPI( );
			if ( imageDpi > 0 )
			{
				return imageDpi;
			}
		}
		try
		{
			int screenDpi = Toolkit.getDefaultToolkit( ).getScreenResolution( );
			if ( screenDpi > 0 )
			{
				return screenDpi;
			}
		}
		catch ( Exception e )
		{
			// Since there isn't related display device under some cases, the calling
			// 'Toolkit.getDefaultToolkit( ).getScreenResolution( );' will throw
			// HeadlessException. Here we just catch this exception to avoid
			// breaking program, and still return 96 as default DPI.
		}
		return 96;
	}
	
	protected boolean isAutoHide( )
	{
		return cm != null && !cm.getEmptyMessage( ).isVisible( );
	}
	
	/**
	 * Indicates whether shared scale can be updated.
	 * @return
	 */
	protected boolean canUpdateScale()
	{
		return false;
	}
	
	/**
	 * Outputs the content when result set is null.
	 * 
	 * @return the content to represent null result set
	 */
	protected Object outputNullResultSet( )
	{
		// Returns an object array for engine to display alt text
		// instead of image when result set is null.
		return new Object[]{
			new byte[]{
				0
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.ReportItemPresentationBase#isCacheable()
	 */
	@Override
	public boolean isCacheable( )
	{
		// If chart is set scripts, chart's properties may be changed in
		// scripts, the cache is not allowed for this case.
		if ( cm != null
				&& cm.getScript( ) != null
				&& cm.getScript( ).trim( ).length( ) > 0 )
		{
			return false;
		}
		
		return true;
	}
}
