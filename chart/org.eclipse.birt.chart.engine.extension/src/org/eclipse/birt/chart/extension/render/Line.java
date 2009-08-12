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

package org.eclipse.birt.chart.extension.render;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.withaxes.AxisSubUnit;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints3D;
import org.eclipse.birt.chart.computation.withaxes.StackedSeriesLookup;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.Line3DRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.Text3DRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.render.AxesRenderer;
import org.eclipse.birt.chart.render.CurveRenderer;
import org.eclipse.birt.chart.render.DeferredCache;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Line
 */
public class Line extends AxesRenderer
{

	protected static final ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine.extension/render" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public Line( )
	{
		super( );
	}

	/**
	 * Computes the end values for stacked lines
	 */
	protected double computeStackPosition( AxisSubUnit au, double dValue,
			Axis ax )
	{
		if ( ax.isPercent( ) )
		{
			dValue = au.valuePercentage( dValue );
		}

		return au.stackValue( dValue );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.AxesRenderer#renderSeries(org.eclipse.birt.chart.output.IRenderer,
	 *      Chart.Plot)
	 */
	@SuppressWarnings("deprecation")
	public void renderSeries( IPrimitiveRenderer ipr, Plot p,
			ISeriesRenderingHints isrh ) throws ChartException
	{

		// VALIDATE CONSISTENT DATASET COUNT BETWEEN BASE AND ORTHOGONAL
		try
		{
			validateDataSetCount( isrh );
		}
		catch ( ChartException vex )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.RENDERING,
					vex );
		}

		boolean bRendering3D = isDimension3D( );

		// SCALE VALIDATION
		SeriesRenderingHints srh = null;
		SeriesRenderingHints3D srh3d = null;

		if ( bRendering3D )
		{
			srh3d = (SeriesRenderingHints3D) isrh;
		}
		else
		{
			srh = (SeriesRenderingHints) isrh;
		}

		// SCALE VALIDATION
		// if ( ( !bRendering3D && !srh.isCategoryScale( ) )
		// || ( bRendering3D && !srh3d.isXCategoryScale( ) ) )
		// {
		// throw new ChartException( ChartEngineExtensionPlugin.ID,
		// ChartException.RENDERING,
		// "exception.xvalue.scale.lines", //$NON-NLS-1$
		// Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
		// }

		// OBTAIN AN INSTANCE OF THE CHART (TO RETRIEVE GENERAL CHART PROPERTIES
		// IF ANY)
		ChartWithAxes cwa = (ChartWithAxes) getModel( );
		logger.log( ILogger.INFORMATION,
				Messages.getString( "info.render.series", //$NON-NLS-1$
						new Object[]{
								getClass( ).getName( ),
								Integer.valueOf( iSeriesIndex + 1 ),
								Integer.valueOf( iSeriesCount )
						},
						getRunTimeContext( ).getULocale( ) ) ); // i18n_CONCATENATIONS_REMOVED

		final Bounds boClientArea = isrh.getClientAreaBounds( true );
		final double dSeriesThickness = bRendering3D ? 0
				: srh.getSeriesThickness( );
		if ( cwa.getDimension( ) == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL )
		{
			boClientArea.delta( -dSeriesThickness, dSeriesThickness, 0, 0 );
		}

		// OBTAIN AN INSTANCE OF THE SERIES MODEL (AND SOME VALIDATION)
		LineSeries ls = (LineSeries) getSeries( );
		if ( !ls.isVisible( ) )
		{
			return;
		}

		// SETUP VARIABLES NEEDED TO RENDER THE LINES/CURVE
		ChartDimension cd = cwa.getDimension( );
		final AbstractScriptHandler<?> sh = getRunTimeContext( ).getScriptHandler( );
		DataPointHints[] dpha = isrh.getDataPoints( );
		validateNullDatapoint( dpha );

		double fX = 0, fY = 0, fZ = 0, fWidth = 0, fWidthZ = 0, fHeight = 0;
		Location lo = null;
		Location3D lo3d = null;

		// DETERMINE IF THE LINES SHOULD BE SHOWN AS TAPES OR 2D LINES
		// Line chart has no 2D+, so just 3D is shown as tape.
		boolean isAreaSeries = ( getSeries() instanceof AreaSeries && !(getSeries() instanceof DifferenceSeries));
		boolean bShowAsTape = ( cd.getValue( ) == ChartDimension.THREE_DIMENSIONAL )
				|| ( isAreaSeries && cd.getValue( ) == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH );
		
		if ( bShowAsTape ) 
		{
			bShowAsTape = validateShowAsTape( );
		}

		// SETUP VARIABLES NEEDED IN STACKED COMPUTATIONS AND GROUPING
		AxisSubUnit au;
		Axis ax = getAxis( );
		double dValue, dEnd;

		StackedSeriesLookup ssl = null;
		if ( !bRendering3D )
		{
			ssl = srh.getStackedSeriesLookup( );
		}

		// SETUP VARIABLES NEEDED TO COMPUTE CO-ORDINATES
		LineAttributes lia = ls.getLineAttributes( );

		double[] faX = new double[dpha.length];
		double[] faY = new double[dpha.length];
		double[] faZ = new double[dpha.length];

		// SETUP THE MARKER FILL COLOR FROM THE SERIES DEFINITION PALETTE (BY
		// CATEGORIES OR BY SERIES)
		SeriesDefinition sd = getSeriesDefinition( );
		final EList<Fill> elPalette = sd.getSeriesPalette( ).getEntries( );
		if ( elPalette.isEmpty( ) )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.RENDERING,
					"exception.empty.palette", //$NON-NLS-1$
					new Object[]{
						ls
					},
					Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
		}

		final boolean bPaletteByCategory = isPaletteByCategory( );

		if ( bPaletteByCategory && ls.eContainer( ) instanceof SeriesDefinition )
		{
			sd = (SeriesDefinition) ls.eContainer( );
		}

		int iThisSeriesIndex = sd.getRunTimeSeries( ).indexOf( ls );
		if ( iThisSeriesIndex < 0 )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.RENDERING,
					"exception.missing.series.for.palette.index", //$NON-NLS-1$ 
					new Object[]{
							ls, sd
					},
					Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
		}
		Marker m = null;
		if ( ls.getMarkers( ).size( ) > 0 )
		{
			m = ls.getMarkers( ).get( iThisSeriesIndex
					% ls.getMarkers( ).size( ) );
		}

		Fill fPaletteEntry = null;
		if ( !bPaletteByCategory )
		{
			fPaletteEntry = FillUtil.getPaletteFill( elPalette,
					iThisSeriesIndex );
		}
		else if ( iSeriesIndex > 0 )
		{
			// Here eliminate the position for one base series.
			// NOTE only one base series allowed now.
			fPaletteEntry = FillUtil.getPaletteFill( elPalette,
					iSeriesIndex - 1 );
		}

		updateTranslucency( fPaletteEntry, ls );

		double dTapeWidth = -1;
		double dUnitSpacingZ = 0;

		// THE MAIN LOOP THAT WALKS THROUGH THE DATA POINT HINTS ARRAY 'dpha'
		for ( int i = 0; i < dpha.length; i++ )
		{
			if ( bRendering3D )
			{
				lo3d = dpha[i].getLocation3D( );

				if ( ChartUtil.mathEqual( dTapeWidth, -1 ) )
				{
					final double dUnitSpacing = ( !cwa.isSetUnitSpacing( ) ) ? 50
							: cwa.getUnitSpacing( ); // AS A PERCENTAGE OF
					// ONE
					dTapeWidth = dpha[i].getSize2D( ).getHeight( )
							* ( 100 - dUnitSpacing )
							/ 100;

					dUnitSpacingZ = dpha[i].getSize2D( ).getHeight( )
							* dUnitSpacing
							/ 200;
				}
			}
			else
			{
				lo = dpha[i].getLocation( ); // TBD: CHECK FOR NULL VALUES
			}

			if ( cwa.isTransposed( ) )
			{
				if ( srh.isCategoryScale( ) )
				{
					fHeight = dpha[i].getSize( );
				}
				fY = ( lo.getY( ) + fHeight / 2.0 );
				// shouldn't update DataPointHints that may affect the next
				// rendering without re-computation
				// lo.setY( fY );
				faY[i] = fY;

				if ( ls.isStacked( ) || ax.isPercent( ) ) // SPECIAL
				// PROCESSING
				// FOR STACKED OR
				// PERCENT SERIES
				{
					au = ssl.getUnit( ls, i ); // UNIT POSITIONS (MAX, MIN) FOR
					// INDEX = 'i'
					dValue = isNaN( dpha[i].getOrthogonalValue( ) ) ? 0
							: ( (Double) dpha[i].getOrthogonalValue( ) ).doubleValue( );
					dEnd = computeStackPosition( au, dValue, ax );

					try
					{
						// NOTE: FLOORS DONE TO FIX ROUNDING ERRORS IN GFX
						// CONTEXT (DOUBLE EDGES)
						faX[i] = Math.floor( srh.getLocationOnOrthogonal( new Double( dEnd ) ) );
						// Add following statement to correct painting the stacked flip chart when negative value exists. 
						dpha[i].setStackOrthogonalValue( new Double( dEnd ) );
						
						if ( faX[i] < srh.getPlotBaseLocation( ) )
						{
							faX[i] = srh.getPlotBaseLocation( );
						}
						
						au.setLastPosition( dValue, faX[i], 0 );
					}
					catch ( Exception ex )
					{
						throw new ChartException( ChartEngineExtensionPlugin.ID,
								ChartException.RENDERING,
								ex );
					}

				}
				else
				{
					faX[i] = lo.getX( );
				}

			}
			else
			{
				if ( bRendering3D )
				{
					fWidth = dpha[i].getSize2D( ).getWidth( );
					fWidthZ = dpha[i].getSize2D( ).getHeight( );
					fX = lo3d.getX( ) + fWidth / 2;
					fZ = lo3d.getZ( ) + fWidthZ - dUnitSpacingZ;
					// shouldn't update DataPointHints that may affect the next
					// rendering without re-computation
					// lo3d.setX( fX );
					// lo3d.setZ( fZ );
					faX[i] = fX;
					faZ[i] = fZ;
				}
				else
				{
					if ( srh.isCategoryScale( ) )
					{
						fWidth = dpha[i].getSize( );
					}
					fX = ( lo.getX( ) + fWidth / 2.0 );
					// shouldn't update DataPointHints that may affect the next
					// rendering without re-computation
					// lo.setX( fX );
					faX[i] = fX;
				}

				if ( ls.isStacked( ) || ax.isPercent( ) ) // SPECIAL
				// PROCESSING
				// FOR STACKED OR
				// PERCENT SERIES
				{
					if ( bRendering3D )
					{
						// Not support stack/percent for 3D chart.
						throw new ChartException( ChartEngineExtensionPlugin.ID,
								ChartException.COMPUTATION,
								"exception.no.stack.percent.3D.chart", //$NON-NLS-1$
								Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
					}

					au = ssl.getUnit( ls, i ); // UNIT POSITIONS (MAX, MIN) FOR
					// INDEX = 'i'
					dValue = isNaN( dpha[i].getOrthogonalValue( ) ) ? 0
							: ( (Double) dpha[i].getOrthogonalValue( ) ).doubleValue( );
					dEnd = computeStackPosition( au, dValue, ax );

					try
					{
						// NOTE: FLOORS DONE TO FIX ROUNDING ERRORS IN GFX
						// CONTEXT (DOUBLE EDGES)
						faY[i] = Math.floor( srh.getLocationOnOrthogonal( new Double( dEnd ) ) );
						dpha[i].setStackOrthogonalValue( new Double( dEnd ) );
						
						au.setLastPosition( dValue, faY[i], 0 );
					}
					catch ( Exception ex )
					{
						throw new ChartException( ChartEngineExtensionPlugin.ID,
								ChartException.RENDERING,
								ex );
					}
				}
				else
				{
					if ( bRendering3D )
					{
						faY[i] = lo3d.getY( );
					}
					else
					{
						faY[i] = lo.getY( );
					}

				}

				// Range check.
				if ( bRendering3D )
				{
					double plotBaseLocation = srh3d.getPlotBaseLocation( );

					// RANGE CHECK (WITHOUT CLIPPING)
					if ( faY[i] < plotBaseLocation ) // TOP EDGE
					{
						faY[i] = plotBaseLocation; // - This causes
						// clipping in output
					}

					if ( faY[i] > plotBaseLocation + srh3d.getPlotHeight( ) ) // BOTTOM
					// EDGE
					{
						faY[i] = plotBaseLocation + srh3d.getPlotHeight( );
					}
				}

			}
		}

		if ( !bRendering3D )
		{
			// Area does not support show outside
			handleOutsideDataPoints( ipr, srh, faX, faY, bShowAsTape );
		}

		if ( ls.isCurve( ) )
		{
			// RENDER AS CURVE
			renderAsCurve( ipr,
					ls.getLineAttributes( ),
					bRendering3D ? (ISeriesRenderingHints) srh3d : srh,
					bRendering3D ? goFactory.createLocation3Ds( faX, faY, faZ )
							: goFactory.createLocations( faX, faY ),
					bShowAsTape,
					dTapeWidth,
					fPaletteEntry,
					ls.isPaletteLineColor( ) );

			renderShadowAsCurve( ipr,
					lia,
					bRendering3D ? (ISeriesRenderingHints) srh3d : srh,
					bRendering3D ? goFactory.createLocation3Ds( faX, faY, faZ )
							: goFactory.createLocations( faX, faY ),
					bShowAsTape,
					dTapeWidth );

			// RENDER THE MARKERS NEXT
			if ( m != null )
			{
				for ( int i = 0; i < dpha.length; i++ )
				{
					if ( dpha[i].isOutside( ) )
					{
						continue;
					}

					if ( bPaletteByCategory )
					{
						fPaletteEntry = FillUtil.getPaletteFill( elPalette, i );
					}
					else
					{
						fPaletteEntry = FillUtil.getPaletteFill( elPalette,
								iThisSeriesIndex );
					}
					updateTranslucency( fPaletteEntry, ls );

					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_ELEMENT,
							dpha[i],
							fPaletteEntry );
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_DATA_POINT,
							dpha[i],
							fPaletteEntry,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_ELEMENT,
							dpha[i] );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT,
							dpha[i] );
					renderMarker( ls,
							ipr,
							m,
							bRendering3D ? goFactory.createLocation3D( faX[i],
									faY[i],
									faZ[i] )
									: goFactory.createLocation( faX[i],
									faY[i] ),
							ls.getLineAttributes( ),
							fPaletteEntry,
							dpha[i],
							null,
							true,
							true );

					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_ELEMENT,
							dpha[i],
							fPaletteEntry );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_DATA_POINT,
							dpha[i],
							fPaletteEntry,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_ELEMENT,
							dpha[i] );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_DATA_POINT,
							dpha[i] );
				}
			}
		}
		else
		{
			// RENDER THE SHADOW OF THE LINE IF APPLICABLE
			renderShadow( ipr,
					p,
					lia,
					bRendering3D ? goFactory.createLocation3Ds( faX, faY, faZ )
							: goFactory.createLocations( faX, faY ),
					bShowAsTape,
					dpha );

			// RENDER THE SERIES DATA POINTS
			renderDataPoints( ipr,
					p,
					bRendering3D ? (ISeriesRenderingHints) srh3d : srh,
					dpha,
					lia,
					bRendering3D ? goFactory.createLocation3Ds( faX, faY, faZ )
							: goFactory.createLocations( faX, faY ),
					bShowAsTape,
					dTapeWidth,
					fPaletteEntry,
					ls.isPaletteLineColor( ) );

			// RENDER THE MARKERS NEXT
			if ( m != null )
			{
				for ( int i = 0; i < dpha.length; i++ )
				{
					if ( bPaletteByCategory )
					{
						fPaletteEntry = FillUtil.getPaletteFill( elPalette, i );
					}
					else
					{
						fPaletteEntry = FillUtil.getPaletteFill( elPalette,
								iThisSeriesIndex );
					}
					updateTranslucency( fPaletteEntry, ls );

					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_ELEMENT,
							dpha[i],
							fPaletteEntry );
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_DATA_POINT,
							dpha[i],
							fPaletteEntry,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_ELEMENT,
							dpha[i] );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT,
							dpha[i] );
					renderMarker( ls,
							ipr,
							m,
							bRendering3D ? goFactory.createLocation3D( faX[i],
									faY[i],
									faZ[i] )
									: goFactory.createLocation( faX[i],
									faY[i] ),
							ls.getLineAttributes( ),
							fPaletteEntry,
							dpha[i],
							null,
							true,
							true );

					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_ELEMENT,
							dpha[i],
							fPaletteEntry );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_DATA_POINT,
							dpha[i],
							fPaletteEntry,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_ELEMENT,
							dpha[i] );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_DATA_POINT,
							dpha[i] );
				}
			}
		}

		// DATA POINT RELATED VARIABLES ARE INITIALIZED HERE
		Label laDataPoint = null;
		Position pDataPoint = null;
		Location loDataPoint = null;
		Location3D loDataPoint3d = null;
		try
		{
			if ( bRendering3D )
			{
				laDataPoint = srh3d.getLabelAttributes( ls );
				if ( laDataPoint.isVisible( ) ) // ONLY COMPUTE IF NECESSARY
				{
					pDataPoint = srh3d.getLabelPosition( ls );
					loDataPoint3d = goFactory.createLocation3D( 0, 0, 0 );
				}
			}
			else
			{
				laDataPoint = srh.getLabelAttributes( ls );
				if ( laDataPoint.isVisible( ) ) // ONLY COMPUTE IF NECESSARY
				{
					pDataPoint = srh.getLabelPosition( ls );
					loDataPoint = goFactory.createLocation( 0, 0 );
				}
			}
		}
		catch ( Exception ex )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.RENDERING,
					ex );
		}

		if ( laDataPoint.isVisible( ) )
		{
			final double dSize = m == null ? 0 : m.getSize( );
			for ( int i = 0; i < dpha.length; i++ )
			{
				if ( isNaN( dpha[i].getOrthogonalValue( ) )
						|| dpha[i].isOutside( ) )
				{
					continue;
				}
				laDataPoint = bRendering3D ? srh3d.getLabelAttributes( ls )
						: srh.getLabelAttributes( ls );
				laDataPoint.getCaption( ).setValue( dpha[i].getDisplayValue( ) );

				ScriptHandler.callFunction( sh,
						ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL,
						dpha[i],
						laDataPoint,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT_LABEL,
						laDataPoint );

				if ( laDataPoint.isVisible( ) )
				{
					if ( bRendering3D )
					{
						switch ( pDataPoint.getValue( ) )
						{
							case Position.ABOVE :
								loDataPoint3d.set( faX[i], faY[i]
										+ dSize
										+ p.getVerticalSpacing( ), faZ[i] + 1 );
								break;
							case Position.BELOW :
								loDataPoint3d.set( faX[i], faY[i]
										- dSize
										- p.getVerticalSpacing( ), faZ[i] + 1 );
								break;
							case Position.LEFT :
								loDataPoint3d.set( faX[i]
										- dSize
										- p.getHorizontalSpacing( ),
										faY[i],
										faZ[i] + 1 );
								break;
							case Position.RIGHT :
								loDataPoint3d.set( faX[i]
										+ dSize
										+ p.getHorizontalSpacing( ),
										faY[i],
										faZ[i] + 1 );
								break;
							default :
								throw new ChartException( ChartEngineExtensionPlugin.ID,
										ChartException.RENDERING,
										"exception.illegal.datapoint.position.line",//$NON-NLS-1$
										new Object[]{
											pDataPoint.getName( )
										},
										Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
						}

						final Text3DRenderEvent tre3d = ( (EventObjectCache) ipr ).getEventObject( WrappedStructureSource.createSeriesDataPoint( ls,
								dpha[i] ),
								Text3DRenderEvent.class );
						tre3d.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
						tre3d.setLabel( laDataPoint );
						tre3d.setTextPosition( Methods.getLabelPosition( pDataPoint ) );
						tre3d.setLocation3D( loDataPoint3d );

						getDeferredCache( ).addLabel( tre3d );
					}
					else
					{
						switch ( pDataPoint.getValue( ) )
						{
							case Position.ABOVE :
								loDataPoint.set( faX[i], faY[i]
										- dSize
										- p.getVerticalSpacing( ) );
								break;
							case Position.BELOW :
								loDataPoint.set( faX[i], faY[i]
										+ dSize
										+ p.getVerticalSpacing( ) );
								break;
							case Position.LEFT :
								loDataPoint.set( faX[i]
										- dSize
										- p.getHorizontalSpacing( ), faY[i] );
								break;
							case Position.RIGHT :
								loDataPoint.set( faX[i]
										+ dSize
										+ p.getHorizontalSpacing( ), faY[i] );
								break;
							default :
								throw new ChartException( ChartEngineExtensionPlugin.ID,
										ChartException.RENDERING,
										"exception.illegal.datapoint.position.line",//$NON-NLS-1$
										new Object[]{
											pDataPoint.getName( )
										},
										Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
						}
						renderLabel( WrappedStructureSource.createSeriesDataPoint( ls,
								dpha[i] ),
								TextRenderEvent.RENDER_TEXT_AT_LOCATION,
								laDataPoint,
								pDataPoint,
								loDataPoint,
								null );
					}
				}

				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_DATA_POINT_LABEL,
						dpha[i],
						laDataPoint,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_DATA_POINT_LABEL,
						laDataPoint );
			}
		}

		// Render the fitting curve.
		if ( !bRendering3D && getSeries( ).getCurveFitting( ) != null )
		{
			Location[] larray = new Location[faX.length];
			for ( int i = 0; i < larray.length; i++ )
			{
				larray[i] = goFactory.createLocation( faX[i], faY[i] );
			}
			larray = filterNull( larray );
			renderFittingCurve( ipr,
					larray,
					getSeries( ).getCurveFitting( ),
					false,
					true );
		}

		if ( !bRendering3D )
		{
			restoreClipping( ipr );
		}
	}

	/**
	 * Check if to show as tape.
	 * 
	 * @return
	 */
	protected boolean validateShowAsTape( )
	{
		ChartWithAxes cwa = (ChartWithAxes) getModel( );
		LineSeries ls = (LineSeries) getSeries( );

		if ( !ls.isStacked( ) ) // NOT STACKED
		{
			if ( getSeriesCount( ) > 2 && !isDimension3D( ) ) // (2 = BASE + 1
			// LINE SERIES);
			// OVERLAY OF MULTIPLE SERIES COULD
			// CAUSE TAPE INTERSECTIONS
			{
				return false;
			}
		}
		else
		{
			final Axis[] axaOrthogonal = cwa.getOrthogonalAxes( cwa.getBaseAxes( )[0],
					true );
			if ( axaOrthogonal.length > 1 ) // MULTIPLE Y-AXES CAN'T SHOW
			// TAPES DUE TO POSSIBLE TAPE
			// INTERSECTIONS
			{
				return false;
			}
			else
			{
				if ( getSeriesCount( ) > 2 && !isDimension3D( ) ) // (2 = BASE
				// + 1 LINE
				// SERIES);
				// OVERLAY OF MULTIPLE
				// 'STACKED' SERIES COULD ALSO
				// CAUSE TAPE INTERSECTIONS
				{
					return false;
				}
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.BaseRenderer#renderLegendGraphic(org.eclipse.birt.chart.device.IPrimitiveRenderer,
	 *      org.eclipse.birt.chart.model.layout.Legend,
	 *      org.eclipse.birt.chart.model.attribute.Fill,
	 *      org.eclipse.birt.chart.model.attribute.Bounds)
	 */
	public void renderLegendGraphic( IPrimitiveRenderer ipr, Legend lg,
			Fill fPaletteEntry, Bounds bo ) throws ChartException
	{
		if ( ( bo.getWidth( ) == 0 ) && ( bo.getHeight( ) == 0 ) )
		{
			return;
		}
		final ClientArea ca = lg.getClientArea( );
		final LineAttributes lia = ca.getOutline( );
		final LineSeries ls = (LineSeries) getSeries( );
		if ( fPaletteEntry == null ) // TEMPORARY PATCH: WILL BE REMOVED SOON
		{
			fPaletteEntry = goFactory.RED( );
		}

		final RectangleRenderEvent rre = ( (EventObjectCache) ipr ).getEventObject( StructureSource.createLegend( lg ),
				RectangleRenderEvent.class );
		rre.setBackground( ca.getBackground( ) );
		rre.setOutline( lia );
		rre.setBounds( bo );
		ipr.fillRectangle( rre );

		LineAttributes liaMarker = ls.getLineAttributes( );
		if ( liaMarker.isVisible( ) )
		{
			final LineRenderEvent lre = ( (EventObjectCache) ipr ).getEventObject( StructureSource.createLegend( lg ),
					LineRenderEvent.class );

			if ( ls.isPaletteLineColor( ) )
			{
				liaMarker = goFactory.copyOf( liaMarker );
				liaMarker.setColor( FillUtil.getColor( fPaletteEntry ) );
			}

			lre.setLineAttributes( liaMarker );
			lre.setStart( goFactory.createLocation( bo.getLeft( ) + 1,
					bo.getTop( )
					+ bo.getHeight( )
					/ 2 ) );
			lre.setEnd( goFactory.createLocation( bo.getLeft( )
					+ bo.getWidth( )
					- 1,
					bo.getTop( ) + bo.getHeight( ) / 2 ) );
			ipr.drawLine( lre );
		}

		SeriesDefinition sd = getSeriesDefinition( );

		final boolean bPaletteByCategory = isPaletteByCategory( );

		if ( bPaletteByCategory && ls.eContainer( ) instanceof SeriesDefinition )
		{
			sd = (SeriesDefinition) ls.eContainer( );
		}

		int iThisSeriesIndex = sd.getRunTimeSeries( ).indexOf( ls );
		if ( iThisSeriesIndex < 0 )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.RENDERING,
					"exception.missing.series.for.palette.index", //$NON-NLS-1$ 
					new Object[]{
							ls, sd
					},
					Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
		}

		Marker m = null;
		if ( ls.getMarkers( ).size( ) > 0 )
		{
			m = ls.getMarkers( ).get( iThisSeriesIndex
					% ls.getMarkers( ).size( ) );
		}

		double width = bo.getWidth( ) / getDeviceScale( );
		double height = bo.getHeight( ) / getDeviceScale( );
		int markerSize = (int) ( ( ( width > height ? height : width ) - 2 ) / 2 );
		if ( markerSize <= 0 )
		{
			markerSize = 1;
		}

		if ( m != null )
		{
			DataPointHints dph = createDummyDataPointHintsForLegendItem( );
			renderMarker( lg,
					ipr,
					m,
					goFactory.createLocation( bo.getLeft( )
							+ bo.getWidth( )
							/ 2, bo.getTop( ) + bo.getHeight( ) / 2 ),
					ls.getLineAttributes( ),
					fPaletteEntry,
					dph,
					Integer.valueOf( markerSize ),
					false,
					false );
		}
	}

	/**
	 * render series as curve.
	 */
	protected void renderAsCurve( IPrimitiveRenderer ipr, LineAttributes lia,
			ISeriesRenderingHints srh, Location[] loa, boolean bShowAsTape,
			double tapeWidth, Fill paletteEntry, boolean usePaletteLineColor )
			throws ChartException
	{
		final CurveRenderer cr = new CurveRenderer( ( (ChartWithAxes) getModel( ) ),
				this,
				lia,
				loa,
				bShowAsTape,
				tapeWidth,
				true,
				!isDimension3D( ),
				paletteEntry,
				usePaletteLineColor,
				( (LineSeries) this.getSeries( ) ).isConnectMissingValue( ) );
		cr.draw( ipr );
	}

	/**
	 * render the shadow as curve.
	 */
	protected void renderShadowAsCurve( IPrimitiveRenderer ipr,
			LineAttributes lia, ISeriesRenderingHints srh, Location[] loa,
			boolean bShowAsTape, double tapeWidth ) throws ChartException
	{
		final ColorDefinition cLineShadow = ( (LineSeries) getSeries( ) ).getShadowColor( );

		if ( !bShowAsTape
				&& cLineShadow != null
				&& cLineShadow.getTransparency( ) != goFactory.TRANSPARENT( )
						.getTransparency( )
				&& lia.isVisible( ) )
		{
			final Location positionDelta = ( ( (ChartWithAxes) getModel( ) ).isTransposed( ) ) ? goFactory.createLocation( -2
					* getDeviceScale( ),
					0 )
					: goFactory.createLocation( 0, 2 * getDeviceScale( ) );

			double[] shX = new double[loa.length];
			double[] shY = new double[loa.length];
			for ( int i = 0; i < loa.length; i++ )
			{
				shX[i] = loa[i].getX( ) + positionDelta.getX( );
				shY[i] = loa[i].getY( ) + positionDelta.getY( );
			}

			LineAttributes liaShadow = goFactory.copyOf( lia );
			liaShadow.setColor( cLineShadow );

			renderAsCurve( ipr, liaShadow, srh, goFactory.createLocations( shX,
					shY ), bShowAsTape, tapeWidth, liaShadow.getColor( ), false );
		}
	}

	/**
	 * @param ipr
	 * @param p
	 * @param srh
	 * @param dpha
	 * @param lia
	 * @param faX
	 * @param faY
	 * @param bShowAsTape
	 * @param paletteEntry
	 * @throws ChartException
	 */
	protected void renderDataPoints( IPrimitiveRenderer ipr, Plot p,
			ISeriesRenderingHints srh, DataPointHints[] dpha,
			LineAttributes lia, Location[] loa, boolean bShowAsTape,
			double dTapeWidth, Fill paletteEntry, boolean usePaletteLineColor )
			throws ChartException
	{
		if ( !lia.isVisible( ) )
		{
			return;
		}

		Location[] loaPlane = null;
		Location[] loaLine = null;

		Location3D[] loa3d = null;
		Location3D[] loaPlane3d = null;
		Location3D[] loaLine3d = null;

		Series ls = getSeries( );

		boolean bRendering3D = isDimension3D( );

		LineRenderEvent lre = ( (EventObjectCache) ipr ).getEventObject( StructureSource.createSeries( ls ),
				LineRenderEvent.class );
		PolygonRenderEvent pre = bShowAsTape ? (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createSeries( ls ),
				PolygonRenderEvent.class )
				: null;
		Line3DRenderEvent lre3d = ( bRendering3D ) ? (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createSeries( ls ),
				Line3DRenderEvent.class )
				: null;
		Polygon3DRenderEvent pre3d = ( bShowAsTape && bRendering3D ) ? (Polygon3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createSeries( ls ),
				Polygon3DRenderEvent.class )
				: null;

		DeferredCache dc = getDeferredCache( );

		if ( usePaletteLineColor )
		{
			lia = goFactory.copyOf( lia );
			lia.setColor( FillUtil.getColor( paletteEntry, false ) );
		}

		if ( !bRendering3D )
		{
			dTapeWidth = ( (SeriesRenderingHints) srh ).getSeriesThickness( );
		}

		List<double[]> points = new ArrayList<double[]>( );

		if ( bRendering3D )
		{
			loa3d = (Location3D[]) loa;
			pre3d.setDoubleSided( true );
		}

		if ( dpha.length > 0 )
		{

			for ( int i = 0; i < dpha.length; i++ )
			{
				if ( isNaN( dpha[i].getOrthogonalValue( ) ) )
				{
					continue;
				}

				if ( bRendering3D )
				{
					points.add( new double[]{
							loa3d[i].getX( ), loa3d[i].getY( ), loa3d[i].getZ( )
					} );
				}
				else
				{
					points.add( new double[]{
							loa[i].getX( ), loa[i].getY( )
					} );
				}

				if ( !( (LineSeries) ls ).isConnectMissingValue( )
						&& ( ( i == 0 ) || ( ( i > 0 ) && isNaN( dpha[i - 1].getOrthogonalValue( ) ) ) ) )
				{
					if ( ( i == dpha.length - 1 )
							|| isNaN( dpha[i + 1].getOrthogonalValue( ) ) )
					{
						double iSize = lia.getThickness( ) * 2.0;
						PrimitiveRenderEvent event;
						if ( bRendering3D )
						{
							Line3DRenderEvent lre3dValue = ( (EventObjectCache) ipr ).getEventObject( WrappedStructureSource.createSeriesDataPoint( getSeries( ),
									dpha[i] ),
									Line3DRenderEvent.class );
							Location3D[] loa3dValue = new Location3D[2];
							loa3dValue[0] = goFactory.createLocation3D( loa3d[i].getX( ),
									loa3d[i].getY( ),
									loa3d[i].getZ( ) );
							loa3dValue[1] = goFactory.createLocation3D( loa3d[i].getX( ),
									loa3d[i].getY( ),
									loa3d[i].getZ( ) - dTapeWidth );
							lre3dValue.setStart3D( loa3dValue[0] );
							lre3dValue.setEnd3D( loa3dValue[1] );
							lre3dValue.setLineAttributes( lia );

							dc.addLine( lre3dValue );
							event = lre3dValue;
						}
						else
						{
							final OvalRenderEvent ore = ( (EventObjectCache) ipr ).getEventObject( WrappedStructureSource.createSeriesDataPoint( getSeries( ),
									dpha[i] ),
									OvalRenderEvent.class );
							ore.setBounds( goFactory.createBounds( loa[i].getX( )
									- iSize
									/ 2,
									loa[i].getY( ) - iSize / 2,
									iSize,
									iSize ) );
							ore.setOutline( lia );

							ipr.drawOval( ore );
							event = ore;
						}
						
						
						addInteractivity( ipr, dpha[i], event);
					}

					continue;
				}

				int pindex = getPreviousNonNullIndex( i, dpha );
				if ( pindex == -1 )
				{
					continue;
				}

				if ( bRendering3D )
				{
					if ( loaLine3d == null )
					{
						loaLine3d = new Location3D[2];
						loaLine3d[0] = goFactory.createLocation3D( loa3d[pindex].getX( ),
								loa3d[pindex].getY( ),
								loa3d[pindex].getZ( ) );
						loaLine3d[1] = goFactory.createLocation3D( loa3d[i].getX( ),
								loa3d[i].getY( ),
								loa3d[i].getZ( ) );
						lre3d.setStart3D( loaLine3d[0] );
						lre3d.setEnd3D( loaLine3d[1] );
						lre3d.setLineAttributes( lia );
						
					}
					else
					{
						loaLine3d[0].set( loa3d[pindex].getX( ),
								loa3d[pindex].getY( ),
								loa3d[pindex].getZ( ) );
						loaLine3d[1].set( loa3d[i].getX( ),
								loa3d[i].getY( ),
								loa3d[i].getZ( ) );
						lre3d.setStart3D( loaLine3d[0] );
						lre3d.setEnd3D( loaLine3d[1] );
					}
					addInteractivity( ipr, dpha[i], lre3d);
				}
				else
				{
					if ( loaLine == null )
					{
						loaLine = new Location[2];
						loaLine[0] = goFactory.createLocation( loa[pindex].getX( ),
								loa[pindex].getY( ) );
						loaLine[1] = goFactory.createLocation( loa[i].getX( ),
								loa[i].getY( ) );
						lre.setStart( loaLine[0] );
						lre.setEnd( loaLine[1] );
						lre.setLineAttributes( lia );						
					}
					else
					{
						loaLine[0].set( loa[pindex].getX( ), loa[pindex].getY( ) );
						loaLine[1].set( loa[i].getX( ), loa[i].getY( ) );
					}
					addInteractivity( ipr, dpha[i], lre);
				}

				if ( bShowAsTape )
				{
					if ( bRendering3D )
					{
						if ( loaPlane3d == null )
						{
							loaPlane3d = new Location3D[4];
							loaPlane3d[0] = goFactory.createLocation3D( loa3d[pindex].getX( ),
									loa3d[pindex].getY( ),
									loa3d[pindex].getZ( ) );
							loaPlane3d[1] = goFactory.createLocation3D( loa3d[i].getX( ),
									loa3d[i].getY( ),
									loa3d[i].getZ( ) );
							loaPlane3d[2] = goFactory.createLocation3D( loa3d[i].getX( ),
									loa3d[i].getY( ),
									loa3d[i].getZ( ) - dTapeWidth );
							loaPlane3d[3] = goFactory.createLocation3D( loa3d[pindex].getX( ),
									loa3d[pindex].getY( ),
									loa3d[pindex].getZ( ) - dTapeWidth );
							pre3d.setOutline( null );
							pre3d.setPoints3D( loaPlane3d );
							pre3d.setBackground( goFactory.brighter( lia.getColor( ) ) );
							
						}
						else
						{
							loaPlane3d[0].set( loa3d[pindex].getX( ),
									loa3d[pindex].getY( ),
									loa3d[pindex].getZ( ) );
							loaPlane3d[1].set( loa3d[i].getX( ),
									loa3d[i].getY( ),
									loa3d[i].getZ( ) );
							loaPlane3d[2].set( loa3d[i].getX( ),
									loa3d[i].getY( ),
									loa3d[i].getZ( ) - dTapeWidth );
							loaPlane3d[3].set( loa3d[pindex].getX( ),
									loa3d[pindex].getY( ),
									loa3d[pindex].getZ( ) - dTapeWidth );
							pre3d.setPoints3D( loaPlane3d );
							
						}
						addInteractivity( ipr, dpha[i], pre3d);
						dc.addPlane( pre3d, PrimitiveRenderEvent.FILL );
					}
					else
					{
						if ( loaPlane == null )
						{
							loaPlane = new Location[4];
							loaPlane[0] = goFactory.createLocation( loa[pindex].getX( ),
									loa[pindex].getY( ) );
							loaPlane[1] = goFactory.createLocation( loa[i].getX( ),
									loa[i].getY( ) );
							loaPlane[2] = goFactory.createLocation( loa[i].getX( )
									+ dTapeWidth, loa[i].getY( ) - dTapeWidth );
							loaPlane[3] = goFactory.createLocation( loa[pindex].getX( )
									+ dTapeWidth,
									loa[pindex].getY( ) - dTapeWidth );
							pre.setOutline( null );
							pre.setPoints( loaPlane );
							pre.setBackground( goFactory.brighter( lia.getColor( ) ) );
						}
						else
						{
							loaPlane[0].set( loa[pindex].getX( ),
									loa[pindex].getY( ) );
							loaPlane[1].set( loa[i].getX( ), loa[i].getY( ) );
							loaPlane[2].set( loa[i].getX( ) + dTapeWidth,
									loa[i].getY( ) - dTapeWidth );
							loaPlane[3].set( loa[pindex].getX( ) + dTapeWidth,
									loa[pindex].getY( ) - dTapeWidth );
						}
						dc.addPlane( pre, PrimitiveRenderEvent.FILL );
						addInteractivity( ipr, dpha[i], pre);
					}
				}

				if ( bRendering3D )
				{
					dc.addLine( lre3d );
				}
				else
				{
					dc.addLine( lre );
				}
			}

			points = filterNull( points );

			if ( isLastRuntimeSeriesInAxis( ) )
			{
				// clean stack state.
				getRunTimeContext( ).putState( STACKED_SERIES_LOCATION_KEY,
						null );
			}
			else
			{
				getRunTimeContext( ).putState( STACKED_SERIES_LOCATION_KEY,
						points );
			}
		}
	}

	
	/**
	 * Render series shadow if applicable.
	 * 
	 * @param ipr
	 * @param p
	 * @param lia
	 * @param faX
	 * @param faY
	 * @param bShowAsTape
	 * @param dpha
	 */
	protected void renderShadow( IPrimitiveRenderer ipr, Plot p,
			LineAttributes lia, Location[] loa, boolean bShowAsTape,
			DataPointHints[] dpha ) throws ChartException
	{
		final ColorDefinition cLineShadow = ( (LineSeries) getSeries( ) ).getShadowColor( );

		if ( !bShowAsTape
				&& cLineShadow != null
				&& cLineShadow.getTransparency( ) != goFactory.TRANSPARENT( )
						.getTransparency( )
				&& lia.isVisible( ) )
		{
			Location[] loaShadow = null;

			final Location positionDelta = ( ( (ChartWithAxes) getModel( ) ).isTransposed( ) ) ? goFactory.createLocation( -3,
					0 )
					: goFactory.createLocation( 0, 3 );
			LineRenderEvent lre = ( (EventObjectCache) ipr ).getEventObject( StructureSource.createSeries( getSeries( ) ),
					LineRenderEvent.class );

			DeferredCache dc = getDeferredCache( );
			
			Series ls = getSeries( );

			if ( dpha.length > 0 )
			{
				for ( int i = 0; i < dpha.length; i++ )
				{
					if ( isNaN( dpha[i].getOrthogonalValue( ) ) )
					{
						continue;
					}

					if ( !( (LineSeries) ls ).isConnectMissingValue( )
							&& ( ( i == 0 ) || ( ( i > 0 ) && isNaN( dpha[i - 1].getOrthogonalValue( ) ) ) ) )
					{
						if ( ( i == dpha.length - 1 )
								|| isNaN( dpha[i + 1].getOrthogonalValue( ) ) )
						{
							double iSize = lia.getThickness( );

							final OvalRenderEvent ore = ( (EventObjectCache) ipr ).getEventObject( WrappedStructureSource.createSeriesDataPoint( getSeries( ),
									dpha[i] ),
									OvalRenderEvent.class );
							ore.setBounds( goFactory.createBounds( loa[i].getX( ),
									loa[i].getY( ) + 3,
									iSize,
									iSize ) );
							LineAttributes liaShadow = goFactory.copyOf( lia );
							liaShadow.setColor( cLineShadow );
							ore.setOutline( liaShadow );
							ore.setBackground( liaShadow.getColor( ) );
							ipr.drawOval( ore );
							ipr.fillOval( ore );
						}

						continue;
					}

					int pindex = getPreviousNonNullIndex( i, dpha );
					if ( pindex == -1 )
					{
						continue;
					}

					if ( loaShadow == null )
					{
						loaShadow = new Location[2];
						loaShadow[0] = goFactory.createLocation( loa[pindex].getX( )
								+ positionDelta.getX( ), loa[pindex].getY( )
								+ positionDelta.getY( ) );
						loaShadow[1] = goFactory.createLocation( loa[i].getX( )
								+ positionDelta.getX( ), loa[i].getY( )
								+ positionDelta.getY( ) );
					}
					else
					{
						loaShadow[0].set( loa[pindex].getX( )
								+ positionDelta.getX( ), loa[pindex].getY( )
								+ positionDelta.getY( ) );
						loaShadow[1].set( loa[i].getX( ) + positionDelta.getX( ),
								loa[i].getY( ) + positionDelta.getY( ) );
					}
					lre.setStart( loaShadow[0] );
					lre.setEnd( loaShadow[1] );
					LineAttributes liaShadow = goFactory.copyOf( lia );
					liaShadow.setColor( cLineShadow );
					lre.setLineAttributes( liaShadow );
					dc.addLine( lre );
				}
			}

		}

	}

	protected int getPreviousNonNullIndex( int currentIndex,
			DataPointHints[] dpha )
	{
		for ( int i = currentIndex - 1; i >= 0; i-- )
		{
			if ( dpha[i].getOrthogonalValue( ) == null
					|| isNaN( dpha[i].getOrthogonalValue( ) ) )
			{
				continue;
			}
			return i;
		}

		return -1;
	}

	protected int getPreviousNonNullIndex( int currentIndex, Location[] loa )
	{
		for ( int i = currentIndex - 1; i >= 0; i-- )
		{
			if ( Double.isNaN( loa[i].getX( ) )
					|| Double.isNaN( loa[i].getY( ) ) )
			{
				continue;
			}

			return i;
		}

		return -1;
	}

	protected boolean isNaN( Location lo )
	{
		return ( lo == null || Double.isNaN( lo.getX( ) ) || Double.isNaN( lo.getY( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.BaseRenderer#compute(org.eclipse.birt.chart.model.attribute.Bounds,
	 *      org.eclipse.birt.chart.model.layout.Plot,
	 *      org.eclipse.birt.chart.render.ISeriesRenderingHints)
	 */

	public void compute( Bounds bo, Plot p, ISeriesRenderingHints isrh )
			throws ChartException
	{
		// NOTE: This method is not used by the Line Renderer
	}

}