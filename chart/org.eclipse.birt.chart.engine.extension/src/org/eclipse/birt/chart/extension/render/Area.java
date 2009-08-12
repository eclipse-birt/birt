/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.Engine3D;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.Point;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints3D;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.Line3DRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.WrappedInstruction;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.render.CurveRenderer;
import org.eclipse.birt.chart.render.DeferredCache;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Area
 */
public class Area extends Line
{
	private final static String AREA_ENVELOPS = "Area.Envelops"; //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public Area( )
	{
		super( );
	}

	private Fill getSeriesPaletteEntry( )
	{
		Fill fPaletteEntry = null;
		SeriesDefinition sd = null;

		Series se = getSeries( );

		if ( se.eContainer( ) instanceof SeriesDefinition )
		{
			sd = (SeriesDefinition) se.eContainer( );
		}

		if ( sd != null )
		{
			int iThisSeriesIndex = sd.getRunTimeSeries( ).indexOf( se );
			if ( iThisSeriesIndex >= 0 )
			{
				EList<Fill> ePalette = sd.getSeriesPalette( ).getEntries( );
				fPaletteEntry = FillUtil.getPaletteFill( ePalette,
						iThisSeriesIndex );
				updateTranslucency( fPaletteEntry, se );
			}
		}

		return fPaletteEntry;
	}

	/**
	 * Return the index of first non-null value.
	 * 
	 * @param dpha
	 * @return
	 */
	private int getFirstNonNullIndex( DataPointHints[] dpha )
	{
		// Stacked series doesn't have null
		if ( se.isStacked( ) )
		{
			return 0;
		}
		
		for ( int i = 0; i < dpha.length; i++ )
		{
			if ( dpha[i].getOrthogonalValue( ) != null )
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * Returns the index of last non-null value.
	 * 
	 * @param dpha
	 * @return
	 */
	private int getLastNonNullIndex( DataPointHints[] dpha )
	{
		// Stacked series doesn't have null
		if ( se.isStacked( ) )
		{
			return dpha.length - 1;
		}
		
		for ( int i = dpha.length - 1; i >= 0; i-- )
		{
			if ( dpha[i].getOrthogonalValue( ) != null )
			{
				return i;
			}
		}

		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.Line#checkShowAsTape()
	 */
	protected boolean validateShowAsTape( )
	{
		ChartWithAxes cwa = (ChartWithAxes) getModel( );
		AreaSeries as = (AreaSeries) getSeries( );

		if ( !as.isStacked( ) ) // NOT STACKED
		{
			if ( getSeriesCount( ) > 2 && !isDimension3D( ) )
			{
				return false;
			}
		}
		else
		{
			final Axis[] axaOrthogonal = cwa.getOrthogonalAxes( cwa.getBaseAxes( )[0],
					true );
			if ( axaOrthogonal.length > 1 )
			{
				// If it is study layout, it should just check if it has another
				// series type in the same axis.
				if ( ChartUtil.isStudyLayout( cwa ))
				{
					Axis axis = ChartUtil.getAxisFromSeries( as );
					if ( axis != null )
					{
						for ( Iterator<SeriesDefinition> itr = axis.getSeriesDefinitions( )
								.iterator( ); itr.hasNext( ); )
						{
							SeriesDefinition sd = itr.next( );
							for ( Iterator<Series> sitr = sd.getRunTimeSeries( )
									.iterator( ); sitr.hasNext( ); )
							{
								Series se = sitr.next( );

								if ( !( se instanceof AreaSeries ) || !se.isStacked( ) )
								{
									return false;
								}
							}
						}
						
						return true;
					}
				}
				
				return false;
			}
			if ( getSeriesCount( ) > 2 && !isDimension3D( ) )
			{
				for ( Iterator<SeriesDefinition> itr = axaOrthogonal[0].getSeriesDefinitions( )
						.iterator( ); itr.hasNext( ); )
				{
					SeriesDefinition sd = itr.next( );
					for ( Iterator<Series> sitr = sd.getRunTimeSeries( )
							.iterator( ); sitr.hasNext( ); )
					{
						Series se = sitr.next( );

						if ( !( se instanceof AreaSeries ) || !se.isStacked( ) )
						{
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.Line#renderAsCurve(org.eclipse.birt.chart.device.IPrimitiveRenderer,
	 *      org.eclipse.birt.chart.model.attribute.LineAttributes, float[],
	 *      float[], boolean)
	 */
	protected void renderAsCurve( IPrimitiveRenderer ipr, LineAttributes lia,
			ISeriesRenderingHints isrh, Location[] loa, boolean bShowAsTape,
			double tapeWidth, Fill paletteEntry, boolean usePaletteLineColor )
			throws ChartException
	{
		Fill seriesPalette = getSeriesPaletteEntry( );

		double zeroLocation = 0;
		if ( isDimension3D( ) )
		{
			zeroLocation = ( (SeriesRenderingHints3D) isrh ).getPlotBaseLocation( );
		}
		else
		{
			SeriesRenderingHints srh = (SeriesRenderingHints) isrh;
			zeroLocation = srh.getZeroLocation( );

			final Bounds boClientArea = srh.getClientAreaBounds( true );
			final double dSeriesThickness = srh.getSeriesThickness( );
			if ( ( (ChartWithAxes) getModel( ) ).getDimension( ) == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL )
			{
				boClientArea.delta( -dSeriesThickness, dSeriesThickness, 0, 0 );
			}

			if ( ( (ChartWithAxes) getModel( ) ).isTransposed( ) )
			{
				if ( zeroLocation < boClientArea.getLeft( ) )
				{
					zeroLocation = boClientArea.getLeft( );
				}

				if ( zeroLocation > boClientArea.getLeft( )
						+ boClientArea.getWidth( ) )
				{
					zeroLocation = boClientArea.getLeft( )
							+ boClientArea.getWidth( );
				}
			}
			else
			{
				if ( zeroLocation < boClientArea.getTop( ) )
				{
					zeroLocation = boClientArea.getTop( );
				}

				if ( zeroLocation > boClientArea.getTop( )
						+ boClientArea.getHeight( ) )
				{
					zeroLocation = boClientArea.getTop( )
							+ boClientArea.getHeight( );
				}
			}
		}

		final CurveRenderer cr = new CurveRenderer( ( (ChartWithAxes) getModel( ) ),
				this,
				lia,
				loa,
				zeroLocation,
				bShowAsTape,
				tapeWidth,
				true,
				getSeries( ).isTranslucent( ),
				getSeries( ).isStacked( ) || getAxis( ).isPercent( ),
				true,
				true,
				seriesPalette != null ? seriesPalette : paletteEntry,
				usePaletteLineColor,
				true );
		cr.draw( ipr );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.Line#renderDataPoints(org.eclipse.birt.chart.device.IPrimitiveRenderer,
	 *      org.eclipse.birt.chart.model.layout.Plot,
	 *      org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints,
	 *      org.eclipse.birt.chart.computation.DataPointHints[],
	 *      org.eclipse.birt.chart.model.attribute.LineAttributes, float[],
	 *      float[], boolean)
	 */
	protected void renderDataPoints( IPrimitiveRenderer ipr, Plot p,
			ISeriesRenderingHints isrh, DataPointHints[] dpha,
			LineAttributes lia, Location[] loa, boolean bShowAsTape,
			double dTapeWidth, Fill paletteEntry, boolean usePaletteLineColor )
			throws ChartException
	{
		Envelop[] envelops = (Envelop[]) getRunTimeContext( ).getState( AREA_ENVELOPS );

		// **********************************************
		// consider 3 policies for rendering area chart
		// 1. connect intermedia null points
		// 2. skip null points at left and right
		// 3. normal(treat all null points as 0)
		// **********************************************

		// TODO we use 2+3 currently,
		// need supporting other combination later.

		Fill seriesPalette = getSeriesPaletteEntry( );
		if ( seriesPalette != null )
		{
			paletteEntry = seriesPalette;
		}

		Location[] loaPlane = null;
		Location3D[] loaPlane3d = null;

		double[] lastX = null;
		double[] lastY = null;
		double[] lastFixedX = null;
		double[] lastFixedY = null;

		Location3D[] loa3d = null;

		boolean isTransposed = ( (ChartWithAxes) getModel( ) ).isTransposed( );
		boolean bRendering3d = isDimension3D( );
		double zeroLocation = 0;
		double plotBaseLocation = 0, plotHeight = 0;
		Series as = getSeries( );

		SeriesRenderingHints srh = null;
		SeriesRenderingHints3D srh3d = null;
		if ( bRendering3d )
		{
			srh3d = (SeriesRenderingHints3D) isrh;
			loa3d = (Location3D[]) loa;
			plotBaseLocation = srh3d.getPlotBaseLocation( );
			plotHeight = srh3d.getPlotHeight( );

			zeroLocation = shear( plotBaseLocation,
					plotHeight,
					srh3d.getPlotZeroLocation( ) );
		}
		else
		{
			srh = (SeriesRenderingHints) isrh;
			zeroLocation = srh.getZeroLocation( );
			
			// Adjusts zero location for study layout case. 
			if ( ChartUtil.isStudyLayout( getModel( ) ) )
			{
				double dStart = Methods.asDouble( srh.getOrthogonalScale( )
						.getMinimum( ) ).doubleValue( );
				zeroLocation = srh.getLocationOnOrthogonal( new Double( dStart ) );
			}
			
			final Bounds boClientArea = srh.getClientAreaBounds( true );
			final double dSeriesThickness = srh.getSeriesThickness( );
			if ( ( (ChartWithAxes) getModel( ) ).getDimension( ) == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL )
			{
				boClientArea.delta( -dSeriesThickness, dSeriesThickness, 0, 0 );
			}

			if ( isTransposed )
			{
				if ( zeroLocation < boClientArea.getLeft( ) )
				{
					zeroLocation = boClientArea.getLeft( );
				}

				if ( zeroLocation > boClientArea.getLeft( )
						+ boClientArea.getWidth( ) )
				{
					zeroLocation = boClientArea.getLeft( )
							+ boClientArea.getWidth( );
				}
			}
			else
			{
				if ( zeroLocation < boClientArea.getTop( ) )
				{
					zeroLocation = boClientArea.getTop( );
				}

				if ( zeroLocation > boClientArea.getTop( )
						+ boClientArea.getHeight( ) )
				{
					zeroLocation = boClientArea.getTop( )
							+ boClientArea.getHeight( );
				}
			}
		}

		LineRenderEvent lre = bRendering3d
				? null
				: (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createSeries( as ),
						LineRenderEvent.class );
		PolygonRenderEvent pre = bRendering3d
				? null
				: (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createSeries( as ),
						PolygonRenderEvent.class );
		Line3DRenderEvent lre3d = bRendering3d
				? (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createSeries( as ),
						Line3DRenderEvent.class ) : null;
		Polygon3DRenderEvent pre3d = bRendering3d
				? (Polygon3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createSeries( as ),
						Polygon3DRenderEvent.class ) : null;

		boolean isUseLastState = getSeries( ).isStacked( )
				|| getAxis( ).isPercent( );
		DeferredCache dc = getDeferredCache( );
		dc.setPlaneShadowsComparator( WrappedInstruction.getDefaultComarator( ) );
		dc.setPlanesComparator( WrappedInstruction.getDefaultComarator( ) );

		if ( usePaletteLineColor )
		{
			lia = goFactory.copyOf( lia );
			lia.setColor( FillUtil.getColor( paletteEntry ) );
		}

		if ( !bRendering3d )
		{
			dTapeWidth = srh.getSeriesThickness( );
		}

		List<double[]> points = new ArrayList<double[]>( );
		List<double[]> fixedPoints = new ArrayList<double[]>( );

		if ( isUseLastState )
		{
			Object obj = getRunTimeContext( ).getState( STACKED_SERIES_LOCATION_KEY );
			lastX = null;
			lastY = null;

			if ( obj instanceof List && ( (List) obj ).size( ) > 0 )
			{
				List lst = (List) obj;

				for ( int i = 0; i < lst.size( ); i++ )
				{
					Object o = lst.get( i );

					if ( o instanceof double[] )
					{
						if ( lastX == null )
						{
							lastX = new double[lst.size( )];
							lastY = new double[lastX.length];
						}

						lastX[i] = ( (double[]) o )[0];
						lastY[i] = ( (double[]) o )[1];
					}
					else
					{
						lastX = null;
						lastY = null;
						break;
					}
				}
			}

			// check last fixed_stacked values.
			obj = getRunTimeContext( ).getState( FIXED_STACKED_SERIES_INDEX_KEY );
			if ( obj instanceof Integer
					&& ( (Integer) obj ).intValue( ) == getPrevVisibleSiblingSeriesIndex( iSeriesIndex ) )
			{
				// only search very previous values.

				obj = getRunTimeContext( ).getState( FIXED_STACKED_SERIES_LOCATION_KEY );
				lastFixedX = null;
				lastFixedY = null;

				if ( obj instanceof List && ( (List) obj ).size( ) > 0 )
				{
					List lst = (List) obj;

					for ( int i = 0; i < lst.size( ); i++ )
					{
						Object o = lst.get( i );

						if ( o instanceof double[] )
						{
							if ( lastFixedX == null )
							{
								lastFixedX = new double[lst.size( )];
								lastFixedY = new double[lastFixedX.length];
							}

							lastFixedX[i] = ( (double[]) o )[0];
							lastFixedY[i] = ( (double[]) o )[1];
						}
						else
						{
							lastFixedX = null;
							lastFixedY = null;
							break;
						}
					}
				}
			}
		}

		if ( bRendering3d )
		{
			lre3d.setLineAttributes( lia );
		}
		else
		{
			lre.setLineAttributes( lia );
		}

		ColorDefinition tapeColor = FillUtil.getBrighterColor( paletteEntry );
		ColorDefinition sideColor = FillUtil.getDarkerColor( paletteEntry );
		Fill fillColor = paletteEntry;

		if ( getSeries( ).isTranslucent( ) )
		{
			if ( fillColor instanceof ColorDefinition )
			{
				fillColor = goFactory.translucent( (ColorDefinition) fillColor );
			}
			tapeColor = tapeColor.translucent( );
			sideColor = sideColor.translucent( );
		}

		int findex = getFirstNonNullIndex( dpha );
		int lindex = getLastNonNullIndex( dpha );
		
		if ( findex < 0 )
		{
			// No data points to render
			return;
		}

		if ( bRendering3d )
		{
			loaPlane3d = new Location3D[4];
			loaPlane3d[0] = goFactory.createLocation3D( 0, 0, 0 );
			loaPlane3d[1] = goFactory.createLocation3D( 0, 0, 0 );
			loaPlane3d[2] = goFactory.createLocation3D( 0, 0, 0 );
			loaPlane3d[3] = goFactory.createLocation3D( 0, 0, 0 );

			double y;

			// back face
			for ( int i = 1; i < dpha.length; i++ )
			{
				int pindex = getPreviousNonNullIndex( i, dpha );
				if ( pindex == -1 )
				{
					continue;
				}

				y = shear( plotBaseLocation, plotHeight, loa3d[pindex].getY( ) );

				if ( zeroLocation <= y )
				{
					loaPlane3d[0].set( loa3d[pindex].getX( ),
							y,
							loa3d[pindex].getZ( ) - dTapeWidth );
					loaPlane3d[1].set( loa3d[i].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[i].getY( ) ),
							loa3d[i].getZ( ) - dTapeWidth );
					loaPlane3d[2].set( loa3d[i].getX( ),
							zeroLocation,
							loa3d[i].getZ( ) - dTapeWidth );
					loaPlane3d[3].set( loa3d[pindex].getX( ),
							zeroLocation,
							loa3d[pindex].getZ( ) - dTapeWidth );
				}
				else
				{
					loaPlane3d[0].set( loa3d[pindex].getX( ),
							zeroLocation,
							loa3d[pindex].getZ( ) - dTapeWidth );
					loaPlane3d[1].set( loa3d[i].getX( ),
							zeroLocation,
							loa3d[i].getZ( ) - dTapeWidth );
					loaPlane3d[2].set( loa3d[i].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[i].getY( ) ),
							loa3d[i].getZ( ) - dTapeWidth );
					loaPlane3d[3].set( loa3d[pindex].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[pindex].getY( ) ),
							loa3d[pindex].getZ( ) - dTapeWidth );
				}
				pre3d.setOutline( null );
				pre3d.setPoints3D( loaPlane3d );
				pre3d.setBackground( fillColor );
				pre3d.setSourceObject( WrappedStructureSource.createSeriesDataPoint( as,
						dpha[i] ) );
				dc.addPlane( pre3d, PrimitiveRenderEvent.FILL );
			}

			if ( findex != -1 )
			{
				// left side plane
				if ( zeroLocation < loa3d[findex].getY( ) )
				{
					loaPlane3d[0] = loa3d[findex].copyInstance( );
					loaPlane3d[0].setY( shear( plotBaseLocation,
							plotHeight,
							loaPlane3d[0].getY( ) ) );
					loaPlane3d[1] = goFactory.createLocation3D( loa3d[findex].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[findex].getY( ) ),
							loa3d[findex].getZ( ) - dTapeWidth );
					loaPlane3d[2] = goFactory.createLocation3D( loa3d[findex].getX( ),
							zeroLocation,
							loa3d[findex].getZ( ) - dTapeWidth );
					loaPlane3d[3] = goFactory.createLocation3D( loa3d[findex].getX( ),
							zeroLocation,
							loa3d[findex].getZ( ) );
				}
				else
				{
					loaPlane3d[0] = loa3d[findex].copyInstance( );
					loaPlane3d[0].setY( shear( plotBaseLocation,
							plotHeight,
							loaPlane3d[0].getY( ) ) );
					loaPlane3d[1] = goFactory.createLocation3D( loa3d[findex].getX( ),
							zeroLocation,
							loa3d[findex].getZ( ) );
					loaPlane3d[2] = goFactory.createLocation3D( loa3d[findex].getX( ),
							zeroLocation,
							loa3d[findex].getZ( ) - dTapeWidth );
					loaPlane3d[3] = goFactory.createLocation3D( loa3d[findex].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[findex].getY( ) ),
							loa3d[findex].getZ( ) - dTapeWidth );
				}
				pre3d.setOutline( null );
				pre3d.setBackground( tapeColor );
				pre3d.setPoints3D( loaPlane3d );
				pre3d.setSourceObject( StructureSource.createSeries( as ) );
				dc.addPlane( pre3d, PrimitiveRenderEvent.FILL );

				if ( lindex != -1 && findex != lindex )
				{
					// bottom face
					loaPlane3d[0].set( loa3d[findex].getX( ),
							zeroLocation,
							loa3d[findex].getZ( ) );
					loaPlane3d[1].set( loa3d[findex].getX( ),
							zeroLocation,
							loa3d[findex].getZ( ) - dTapeWidth );
					loaPlane3d[2].set( loa3d[lindex].getX( ),
							zeroLocation,
							loa3d[lindex].getZ( ) - dTapeWidth );
					loaPlane3d[3].set( loa3d[lindex].getX( ),
							zeroLocation,
							loa3d[lindex].getZ( ) );
					pre3d.setOutline( null );
					pre3d.setDoubleSided( true );
					pre3d.setPoints3D( loaPlane3d );
					pre3d.setBackground( fillColor );
					pre3d.setSourceObject( StructureSource.createSeries( as ) );
					dc.addPlane( pre3d, PrimitiveRenderEvent.FILL );
					pre3d.setDoubleSided( false );
				}
			}
		}

		for ( int i = 0; i < dpha.length; i++ )
		{		
			if ( bRendering3d )
			{
				points.add( new double[]{
						loa3d[i].getX( ),
						shear( plotBaseLocation, plotHeight, loa3d[i].getY( ) ),
						loa3d[i].getZ( )
				} );
			}
			else
			{
				points.add( new double[]{
						loa[i].getX( ), loa[i].getY( )
				} );

				if ( isTransposed )
				{
					if ( i == findex && i > 0 )
					{
						fixedPoints.add( new double[]{
								fixNaN( zeroLocation,
										Double.NaN,
										i - 1,
										findex,
										lindex,
										loa,
										lastX,
										false ),
								loa[i - 1].getY( )
						} );
					}

					fixedPoints.add( new double[]{
							fixNaN( zeroLocation,
									loa[i].getX( ),
									i,
									findex,
									lindex,
									loa,
									lastX,
									false ),
							loa[i].getY( )
					} );

					if ( i == lindex && i != dpha.length - 1 )
					{
						fixedPoints.add( new double[]{
								fixNaN( zeroLocation,
										Double.NaN,
										i + 1,
										findex,
										lindex,
										loa,
										lastX,
										false ),
								loa[i + 1].getY( )
						} );
					}

				}
				else
				{
					if ( i == findex && i > 0 )
					{
						fixedPoints.add( new double[]{
								loa[i - 1].getX( ),
								fixNaN( zeroLocation,
										Double.NaN,
										i - 1,
										findex,
										lindex,
										loa,
										lastY,
										true )
						} );
					}

					fixedPoints.add( new double[]{
							loa[i].getX( ),
							fixNaN( zeroLocation,
									loa[i].getY( ),
									i,
									findex,
									lindex,
									loa,
									lastY,
									true )
					} );

					if ( i == lindex && i != dpha.length - 1 )
					{
						fixedPoints.add( new double[]{
								loa[i + 1].getX( ),
								fixNaN( zeroLocation,
										Double.NaN,
										i + 1,
										findex,
										lindex,
										loa,
										lastY,
										true )
						} );
					}

				}
			}

			int pindex = getPreviousNonNullIndex( i, dpha );
			// skip very left empty data points
			if ( i == 0 || pindex < 0 && dpha[i].getOrthogonalValue( ) == null )
			{
				continue;
			}

			if ( pindex < 0 )
			{
				pindex = i - 1;
			}

			if ( bShowAsTape )
			{
				if ( bRendering3d )
				{
					// top tape plane
					loaPlane3d[0].set( loa3d[pindex].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[pindex].getY( ) ),
							loa3d[pindex].getZ( ) );
					loaPlane3d[1].set( loa3d[i].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[i].getY( ) ),
							loa3d[i].getZ( ) );
					loaPlane3d[2].set( loa3d[i].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[i].getY( ) ),
							loa3d[i].getZ( ) - dTapeWidth );
					loaPlane3d[3].set( loa3d[pindex].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[pindex].getY( ) ),
							loa3d[pindex].getZ( ) - dTapeWidth );
					pre3d.setOutline( null );
					pre3d.setBackground( tapeColor );
					pre3d.setPoints3D( loaPlane3d );
					pre3d.setSourceObject( WrappedStructureSource.createSeriesDataPoint( as,
							dpha[i] ) );
					dc.addPlane( pre3d, PrimitiveRenderEvent.FILL );

					if ( i == lindex )
					{
						// right side plane
						if ( shear( plotBaseLocation,
								plotHeight,
								loa3d[i].getY( ) ) > zeroLocation )
						{
							loaPlane3d[0].set( loa3d[i].getX( ),
									shear( plotBaseLocation,
											plotHeight,
											loa3d[i].getY( ) ),
									loa3d[i].getZ( ) );
							loaPlane3d[1].set( loa3d[i].getX( ),
									zeroLocation,
									loa3d[i].getZ( ) );
							loaPlane3d[2].set( loa3d[i].getX( ),
									zeroLocation,
									loa3d[i].getZ( ) - dTapeWidth );
							loaPlane3d[3].set( loa3d[i].getX( ),
									shear( plotBaseLocation,
											plotHeight,
											loa3d[i].getY( ) ),
									loa3d[i].getZ( ) - dTapeWidth );
						}
						else
						{
							loaPlane3d[0].set( loa3d[i].getX( ),
									shear( plotBaseLocation,
											plotHeight,
											loa3d[i].getY( ) ),
									loa3d[i].getZ( ) );
							loaPlane3d[1].set( loa3d[i].getX( ),
									shear( plotBaseLocation,
											plotHeight,
											loa3d[i].getY( ) ),
									loa3d[i].getZ( ) - dTapeWidth );
							loaPlane3d[2].set( loa3d[i].getX( ),
									zeroLocation,
									loa3d[i].getZ( ) - dTapeWidth );
							loaPlane3d[3].set( loa3d[i].getX( ),
									zeroLocation,
									loa3d[i].getZ( ) );
						}

						pre3d.setOutline( null );
						pre3d.setBackground( sideColor );
						pre3d.setPoints3D( loaPlane3d );
						pre3d.setSourceObject( WrappedStructureSource.createSeriesDataPoint( as,
								dpha[i] ) );
						dc.addPlane( pre3d,	PrimitiveRenderEvent.FILL );
					}
				}
				else
				{
					// 2D+ mode
					if ( envelops == null )
					{
						envelops = new Envelop[dpha.length];
					}

					double loX = loa[i].getX( );
					double loY = loa[i].getY( );
					double loXp = loa[i - 1].getX( );
					double loYp = loa[i - 1].getY( );

					if ( loaPlane == null )
					{
						loaPlane = new Location[4];
						loaPlane[0] = goFactory.createLocation( 0, 0 );
						loaPlane[1] = goFactory.createLocation( 0, 0 );
						loaPlane[2] = goFactory.createLocation( 0, 0 );
						loaPlane[3] = goFactory.createLocation( 0, 0 );
					}

					if ( isTransposed )
					{
						loX = fixNaN( zeroLocation,
								loX,
								i,
								findex,
								lindex,
								loa,
								lastX,
								false );
						loXp = fixNaN( zeroLocation,
								loXp,
								i - 1,
								findex,
								lindex,
								loa,
								lastX,
								false );
						
						if ( envelops[i] == null )
						{
							envelops[i] = new Envelop( loYp, loY, zeroLocation, isTransposed );
						}

						envelops[i].addLine( iSeriesIndex, loXp, loX );

					}
					else
					{
						loY = fixNaN( zeroLocation,
								loY,
								i,
								findex,
								lindex,
								loa,
								lastY,
								true );
						loYp = fixNaN( zeroLocation,
								loYp,
								i - 1,
								findex,
								lindex,
								loa,
								lastY,
								true );

						if ( envelops[i] == null )
						{
							envelops[i] = new Envelop( loXp, loX, zeroLocation, isTransposed );
						}

						envelops[i].addLine( iSeriesIndex, loYp, loY );
					}
					
					// ------ Render the top tape.
					List<Location[]> tops = envelops[i].getTopChanges( iSeriesIndex,
							dTapeWidth );
					for ( Location[] polygon : tops )
					{
						pre.setOutline( null );
						pre.setBackground( tapeColor );
						pre.setPoints( polygon );
						pre.setSourceObject( WrappedStructureSource.createSeriesDataPoint( as,
								dpha[i] ) );
						dc.addPlaneShadow( pre,
								PrimitiveRenderEvent.FILL,
								dpha.length + i );
					}

					// Render the bottom tape.
					// Only if the current or previous value is negative
					List<Location[]> bottoms = envelops[i].getBottomChanges( iSeriesIndex,
							dTapeWidth );
					for ( Location[] polygon : bottoms )
					{
						pre.setOutline( null );
						pre.setBackground( tapeColor );
						pre.setPoints( polygon );
						pre.setSourceObject( WrappedStructureSource.createSeriesDataPoint( as,
								dpha[i] ) );
						dc.addPlaneShadow( pre, PrimitiveRenderEvent.FILL, i );
					}
					
					
					if ( i == lindex && !isRightToLeft( ) )
					{
						loaPlane[0].set( loX, loY );
						loaPlane[1].set( loX + dTapeWidth, loY - dTapeWidth );
						if ( isTransposed )
						{
							double lastLocation = getLastFixedValue( zeroLocation,
									loX,
									loY,
									lastFixedX,
									lastFixedY,
									false );
							loaPlane[2].set( lastLocation + dTapeWidth, loY
									- dTapeWidth );
							loaPlane[3].set( lastLocation, loY );
						}
						else
						{
							double lastLocation = getLastFixedValue( zeroLocation,
									loX,
									loY,
									lastFixedX,
									lastFixedY,
									true );
							loaPlane[2].set( loX + dTapeWidth, lastLocation
									- dTapeWidth );
							loaPlane[3].set( loX, lastLocation );
						}
						pre.setOutline( null );
						pre.setBackground( sideColor );
						pre.setPoints( loaPlane );
						pre.setSourceObject( WrappedStructureSource.createSeriesDataPoint( as,
								dpha[i] ) );
						// Rightmost shadow must be in the top of z-order, so
						// it's not addPlaneShadow
						dc.addPlane( pre, PrimitiveRenderEvent.FILL );
					}
					else if ( pindex == findex && isRightToLeft( ) )
					{
						loaPlane[0].set( loa[pindex].getX( ),
								loa[pindex].getY( ) );
						loaPlane[1].set( loa[pindex].getX( ) + dTapeWidth,
								loa[pindex].getY( ) - dTapeWidth );
						if ( isTransposed )
						{
							double lastLocation = getLastFixedValue( zeroLocation,
									loa[pindex].getX( ),
									loa[pindex].getY( ),
									lastFixedX,
									lastFixedY,
									false );
							loaPlane[2].set( lastLocation + dTapeWidth,
									loa[pindex].getY( ) - dTapeWidth );
							loaPlane[3].set( lastLocation, loa[pindex].getY( ) );
						}
						else
						{
							double lastLocation = getLastFixedValue( zeroLocation,
									loa[pindex].getX( ),
									loa[pindex].getY( ),
									lastFixedX,
									lastFixedY,
									true );
							loaPlane[2].set( loa[pindex].getX( ) + dTapeWidth,
									lastLocation - dTapeWidth );
							loaPlane[3].set( loa[pindex].getX( ), lastLocation );
						}
						pre.setOutline( null );
						pre.setBackground( sideColor );
						pre.setPoints( loaPlane );
						dc.addPlaneShadow( pre, PrimitiveRenderEvent.FILL );
					}
				}
			}
		}

		if ( bRendering3d )
		{
			for ( int i = 1; i < dpha.length; i++ )
			{
				int pindex = getPreviousNonNullIndex( i, dpha );
				if ( pindex == -1 )
				{
					continue;
				}

				// front face
				if ( zeroLocation <= shear( plotBaseLocation,
						plotHeight,
						loa3d[pindex].getY( ) ) )
				{
					loaPlane3d[0].set( loa3d[pindex].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[pindex].getY( ) ),
							loa3d[pindex].getZ( ) );
					loaPlane3d[1].set( loa3d[pindex].getX( ),
							zeroLocation,
							loa3d[pindex].getZ( ) );
					loaPlane3d[2].set( loa3d[i].getX( ),
							zeroLocation,
							loa3d[i].getZ( ) );
					loaPlane3d[3].set( loa3d[i].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[i].getY( ) ),
							loa3d[i].getZ( ) );
				}
				else
				{
					loaPlane3d[0].set( loa3d[pindex].getX( ),
							zeroLocation,
							loa3d[pindex].getZ( ) );
					loaPlane3d[1].set( loa3d[pindex].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[pindex].getY( ) ),
							loa3d[pindex].getZ( ) );
					loaPlane3d[2].set( loa3d[i].getX( ),
							shear( plotBaseLocation,
									plotHeight,
									loa3d[i].getY( ) ),
							loa3d[i].getZ( ) );
					loaPlane3d[3].set( loa3d[i].getX( ),
							zeroLocation,
							loa3d[i].getZ( ) );
				}
				pre3d.setOutline( null );
				pre3d.setPoints3D( loaPlane3d );
				pre3d.setBackground( fillColor );
				pre3d.setSourceObject( WrappedStructureSource.createSeriesDataPoint( as,
						dpha[i] ) );
				Object eventFront = dc.addPlane( pre3d,
						PrimitiveRenderEvent.FILL );

				if ( lia.isVisible( ) )
				{
					Location3D lstart = loa3d[pindex].copyInstance( );
					Location3D lend = loa3d[i].copyInstance( );

					lstart.setY( shear( plotBaseLocation,
							plotHeight,
							lstart.getY( ) ) );
					lend.setY( shear( plotBaseLocation, plotHeight, lend.getY( ) ) );

					lre3d.setStart3D( lstart );
					lre3d.setEnd3D( lend );
					lre3d.setObject3DParent( Engine3D.getObjectFromEvent( eventFront ) );
					dc.addLine( lre3d );
				}
			}
		}
		
		// #SCR 99383
		int[] realIndexes = getRealIndexList( fixedPoints, isTransposed );
		int iFirstDupIndex = getFirstDupIndex( fixedPoints, isTransposed );

		if ( lastX != null )
		{
			// only render when have non-null values
			if ( findex != -1 )
			{
				List<Location> lst = new ArrayList<Location>( );

				for ( int i = 0; i < fixedPoints.size( ); i++ )
				{
					double[] pt = fixedPoints.get( i );
					lst.add( goFactory.createLocation( pt[0], pt[1] ) );
				}

				if ( lastFixedY != null )
				{
					for ( int i = lastFixedY.length - 1; i >= 0; i-- )
					{
						lst.add( goFactory.createLocation( lastFixedX[i],
								lastFixedY[i] ) );
					}
				}
				else
				{
					for ( int i = lastY.length - 1; i >= 0; i-- )
					{
						lst.add( goFactory.createLocation( lastX[i], lastY[i] ) );
					}
				}

				Location[] pa = lst.toArray( new Location[lst.size( )] );

				pa = filterNull( pa );

				if ( pa.length > 3 )
				{
					pre.setOutline( null );
					pre.setPoints( pa );
					pre.setBackground( fillColor );
					pre.setSourceObject( StructureSource.createSeries( as ) );
					dc.addPlane( pre, PrimitiveRenderEvent.FILL );
				}

				if ( lia.isVisible( ) )
				{
					List<double[]> flst = fixedPoints;

					Location start = goFactory.createLocation( 0, 0 );
					Location end = goFactory.createLocation( 0, 0 );

					for ( int i = 0; i < flst.size( ) - 1; i++ )
					{
						int rdx = realIndexes[i];
						if ( rdx < findex - 1
								|| rdx > lindex || i == iFirstDupIndex )
						{
							continue;
						}

						double[] pt = flst.get( i );
						start.set( pt[0], pt[1] );
						pt = flst.get( i + 1 );
						end.set( pt[0], pt[1] );

						lre.setStart( start );
						lre.setEnd( end );
						lre.setSourceObject( WrappedStructureSource.createSeriesDataPoint( as,
								dpha[rdx] ) );
						dc.addLine( lre );
					}
				}
			}
		}
		else
		{
			if ( !bRendering3d )
			{
				List<Location> lst = new ArrayList<Location>( );

				for ( int i = 0; i < fixedPoints.size( ); i++ )
				{
					double[] pt = fixedPoints.get( i );
					lst.add( goFactory.createLocation( pt[0], pt[1] ) );
				}

				if ( fixedPoints.size( ) > 0 )
				{
					if ( isTransposed )
					{
						lst.add( goFactory.createLocation( zeroLocation,
								( fixedPoints.get( fixedPoints.size( ) - 1 ) )[1] ) );
						lst.add( goFactory.createLocation( zeroLocation,
								( fixedPoints.get( 0 ) )[1] ) );
					}
					else
					{
						lst.add( goFactory.createLocation( ( fixedPoints.get( fixedPoints.size( ) - 1 ) )[0],
								zeroLocation ) );
						lst.add( goFactory.createLocation( ( fixedPoints.get( 0 ) )[0],
								zeroLocation ) );
					}
				}

				Location[] pa = lst.toArray( new Location[lst.size( )] );

				pre.setOutline( null );
				pre.setPoints( pa );
				pre.setBackground( fillColor );
				pre.setSourceObject( StructureSource.createSeries( as ) );
				dc.addPlane( pre, PrimitiveRenderEvent.FILL );

				if ( lia.isVisible( ) )
				{
					List<double[]> flst = fixedPoints;

					Location start = goFactory.createLocation( 0, 0 );
					Location end = goFactory.createLocation( 0, 0 );
					
					for ( int i = 0; i < flst.size( ) - 1; i++ )
					{
						int rdx = realIndexes[i];						
						if ( rdx < findex - 1
								|| rdx > lindex
								|| i == iFirstDupIndex )
						{
							continue;
						}

						double[] pt = flst.get( i );
						start.set( pt[0], pt[1] );
						pt = flst.get( i + 1 );
						end.set( pt[0], pt[1] );

						lre.setStart( start );
						lre.setEnd( end );
						lre.setSourceObject( WrappedStructureSource.createSeriesDataPoint( as,
								dpha[rdx] ) );
						dc.addLine( lre );
					}
				}
			}
		}

		if ( isLastRuntimeSeriesInAxis( ) )
		{
			// clean stack state.
			getRunTimeContext( ).putState( STACKED_SERIES_LOCATION_KEY, null );
			getRunTimeContext( ).putState( FIXED_STACKED_SERIES_LOCATION_KEY,
					null );
			getRunTimeContext( ).putState( FIXED_STACKED_SERIES_INDEX_KEY, null );
			getRunTimeContext( ).putState( AREA_ENVELOPS, null );
		}
		else
		{
			getRunTimeContext( ).putState( STACKED_SERIES_LOCATION_KEY, points );
			getRunTimeContext( ).putState( FIXED_STACKED_SERIES_LOCATION_KEY,
					fixedPoints );
			getRunTimeContext( ).putState( FIXED_STACKED_SERIES_INDEX_KEY,
					Integer.valueOf( iSeriesIndex ) );
			getRunTimeContext( ).putState( AREA_ENVELOPS, envelops );
		}
		
		
	}

	private int getFirstDupIndex( List<double[]> fixedPoints, boolean checkX )
	{
		double lastValue = Double.NaN;
		for ( int i = 0; i < fixedPoints.size( ) - 1; i++ )
		{
			double[] pt = fixedPoints.get( i );

			if ( Double.isNaN( lastValue ) )
			{
				lastValue = checkX ? pt[1] : pt[0];
			}
			else
			{
				double currentValue = checkX ? pt[1] : pt[0];

				if ( currentValue == lastValue )
				{
					return ( i - 1 );
				}

				lastValue = currentValue;
			}
		}

		return -1;
	}
	
	
	/**
	 * Return the value in same position of last series.
	 * 
	 * @param zeroLocation
	 * @param x
	 * @param y
	 * @param lastX
	 * @param lastY
	 * @param checkY
	 * @return
	 */
	private double getLastFixedValue( double zeroLocation, double x, double y,
			double[] lastX, double[] lastY, boolean checkY )
	{
		if ( lastX == null )
		{
			return zeroLocation;
		}

		if ( checkY )
		{
			for ( int i = 0; i < lastX.length; i++ )
			{
				if ( x == lastX[i] )
				{
					return lastY[i];
				}
			}
		}
		else
		{
			for ( int i = 0; i < lastY.length; i++ )
			{
				if ( y == lastY[i] )
				{
					return lastX[i];
				}
			}
		}

		return zeroLocation;
	}
	
	private int[] getRealIndexList( List<double[]> fixedPoints, boolean checkX )
	{
		int length = fixedPoints.size( );
		int[] realIndexes = new int[length];
		double lastValue = Double.NaN;
		int realid = 0;
		
		for ( int i = 0; i < length; i++ )
		{
			double[] pt = fixedPoints.get( i );

			if ( Double.isNaN( lastValue ) )
			{
				lastValue = checkX ? pt[1] : pt[0];
			}
			else
			{
				double currentValue = checkX ? pt[1] : pt[0];

				if ( currentValue != lastValue )
				{
					realid++;
				}

				lastValue = currentValue;
			}

			realIndexes[i] = realid;
		}

		return realIndexes;
	}

	
	/**
	 * Fix the NaN value for stacked case
	 * 
	 * @param baseValue
	 * @param currentValue
	 * @param currentIndex
	 * @param length
	 * @param lastValues
	 * @return
	 */
	private double fixNaN( double baseValue, double currentValue,
			int currentIndex, int firstNonNullIndex, int lastNonNullIndex,
			Location[] currentXY, double[] lastValues, boolean fixY )
	{
		// only fix NaN values
		if ( !Double.isNaN( currentValue ) )
		{
			return currentValue;
		}

		// use baseValue if this is the first series
		if ( lastValues == null )
		{
			return baseValue;
		}

		// use last value if it's not NaN
		if ( !Double.isNaN( lastValues[currentIndex] ) )
		{
			return lastValues[currentIndex];
		}

		// search first previous non-NaN index
		int pindex = -1;
		for ( int i = currentIndex - 1; i >= 0; i-- )
		{
			if ( !Double.isNaN( lastValues[i] ) )
			{
				pindex = i;
				break;
			}
		}

		if ( pindex == -1 )
		{
			return baseValue;
		}

		// search first next non-NaN index
		int nindex = -1;
		for ( int i = currentIndex + 1; i < currentXY.length; i++ )
		{
			if ( !Double.isNaN( lastValues[i] ) )
			{
				nindex = i;
				break;
			}
		}

		if ( nindex == -1 )
		{
			return baseValue;
		}

		// extrapolate intermedia last value.
		return lastValues[pindex]
				+ ( lastValues[nindex] - lastValues[pindex] )
				* ( currentIndex - pindex ) / ( nindex - pindex );
	}

	private double shear( double plotBase, double plotHeight, double y )
	{
		if ( y < plotBase )
		{
			y = plotBase;
		}
		if ( y > plotBase + plotHeight )
		{
			y = plotBase + plotHeight;
		}
		return y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.Line#renderShadow(org.eclipse.birt.chart.device.IPrimitiveRenderer,
	 *      org.eclipse.birt.chart.model.layout.Plot,
	 *      org.eclipse.birt.chart.model.attribute.LineAttributes, float[],
	 *      float[], boolean)
	 */
	protected void renderShadow( IPrimitiveRenderer ipr, Plot p,
			LineAttributes lia, Location[] loa, boolean bShowAsTape )
	{
		// AREA DONT RENDER A SHADOW
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

		final PolygonRenderEvent pre = ( (EventObjectCache) ipr ).getEventObject( StructureSource.createLegend( lg ),
				PolygonRenderEvent.class );

		Location[] loa = new Location[5];
		loa[0] = goFactory.createLocation( bo.getLeft( ) + 1 * getDeviceScale( ),
				bo.getTop( ) + bo.getHeight( ) - 2 * getDeviceScale( ) );
		loa[1] = goFactory.createLocation( bo.getLeft( )
				+ bo.getWidth( ) - 1 * getDeviceScale( ), bo.getTop( )
				+ bo.getHeight( ) - 2 * getDeviceScale( ) );
		loa[2] = goFactory.createLocation( bo.getLeft( )
				+ bo.getWidth( )
				* 5
				/ 6,
				bo.getTop( ) + bo.getHeight( ) / 3 );
		loa[3] = goFactory.createLocation( bo.getLeft( )
				+ bo.getWidth( )
				* 2
				/ 3,
				bo.getTop( ) + bo.getHeight( ) / 2 );
		loa[4] = goFactory.createLocation( bo.getLeft( ) + bo.getWidth( ) / 2,
				bo.getTop( ) + 1 * getDeviceScale( ) );

		pre.setBackground( fPaletteEntry );
		pre.setPoints( loa );
		ipr.fillPolygon( pre );

		// render outline
		LineAttributes liaMarker = ls.getLineAttributes( );
		if ( liaMarker.isVisible( ) )
		{
			if ( ls.isPaletteLineColor( ) )
			{
				liaMarker = goFactory.copyOf( liaMarker );
				liaMarker.setColor( FillUtil.getColor( fPaletteEntry ) );
			}

			pre.setOutline( liaMarker );
			ipr.drawPolygon( pre );
		}

	}
	
	protected int getPreviousNonNullIndex( int currentIndex,
			DataPointHints[] dpha )
	{
		// Always render null points
		return currentIndex - 1;
	}
	
	protected Location[] filterNull( Location[] ll )
	{
		// Fix null values to use base line instead
		final Bounds boClientArea = getPlotBounds( );
		List<Location> al = new ArrayList<Location>( );
		for ( int i = 0; i < ll.length; i++ )
		{
			if ( Double.isNaN( ll[i].getX( ) ) )
			{
				ll[i].setX( boClientArea.getLeft( ) );
			}
			if ( Double.isNaN( ll[i].getY( ) ) )
			{
				ll[i].setY( boClientArea.getTop( ) + boClientArea.getHeight( ) );
			}

			al.add( ll[i] );
		}

		if ( ll instanceof Location3D[] )
		{
			return al.toArray( new Location3D[al.size( )] );
		}
		return al.toArray( new Location[al.size( )] );
	}
	
	
	private static class StraightLine
	{
		private double x0, y0;
		private double k;

		public StraightLine( double x0, double y0, double x1, double y1 )
		{
			this.x0 = x0;
			this.y0 = y0;
			k = ( y1 - y0 ) / ( x1 - x0 );
		}
		
		/*
		 * return the cross point of this line and linesegment(pt0-pt1)
		 * if there is any, null otherwise
		 */
		public Point getCrossPoint( Point pt0, Point pt1 )
		{
			Point pt = null;
			
			double xst = pt0.x;
			double yst0 = pt0.y;
			double yst1 = getYfromX( xst );
			
			double xed = pt1.x;
			double yed0 = pt1.y;
			double yed1 = getYfromX( xed );
			
			if ( xed != xst )
			{
				if ( ( yst0 - yst1 ) * ( yed0 - yed1 ) < 0 )
				{
					double x, y;
					
					if ( yed1 != yst1 )
					{
						double rate = ( yed0 - yst0 ) / ( yed1 - yst1 );
						y = ( yst0 - rate * yst1 ) / ( 1 - rate );
						x = getXfromY( y );
					}
					else
					{
						y = yst1;
						double rate = (xed - xst) / (yed0 - yst0);
						x = xst + rate * (y - yst0);
					}

					pt = new Point( x, y );
				}
				else if ( yst0 == yst1 )
				{
					pt = new Point( xst, yst0 );
				}
				else if ( yed0 == yed1)
				{
					pt = new Point( xed, yed0 );
				}
				else if ( yst0 == yed0 )
				{
					if (k!=0)
					{
						double y = yst0;
						double x = getXfromY( y );
						pt = new Point( x, y );
					}
					
				}
				else
				{
					pt = null;
				}
			}
			
			if ( pt != null )
			{
				if (pt.x < Math.min( pt0.x, pt1.x ) || pt.x >Math.max( pt0.x, pt1.x ) )
				{
					pt = null;
				}
			}
			
			return pt;
		}
		
		public double getYfromX( double x )
		{
			return y0 + k * ( x - x0 ); 
		}
		
		public double getXfromY( double y )
		{
			return x0 + ( y - y0 ) / k;
		}
	}
	
	
	private static class Envelop
	{

		private class IndexedPoint
		{
			public int index;
			public Point pt;

			public IndexedPoint( int index, double x, double y )
			{
				this.index = index;
				this.pt = new Point( x, y );
			}

			public IndexedPoint( int index, Point pt )
			{
				this.index = index;
				this.pt = pt;
			}
			
			public IndexedPoint copy( )
			{
				return new IndexedPoint( index, pt.x, pt.y );
			}
			
//			public void copyFrom( IndexedPoint ipt )
//			{
//				this.index = ipt.index;
//				this.pt.x = ipt.pt.x;
//				this.pt.y = ipt.pt.y;
//			}

			public double getX( )
			{
				return pt.getX( );
			}

			public double getY( )
			{
				return pt.getY( );
			}

//			public void setX( double x )
//			{
//				pt.setX( x );
//			}

			public void setY( double y )
			{
				pt.setY( y );
			}

			@Override
			public String toString( )
			{
				StringBuilder sb = new StringBuilder( "[" ); //$NON-NLS-1$
				sb.append( index );
				sb.append( ", " ); //$NON-NLS-1$
				sb.append( pt.x );
				sb.append( ", " ); //$NON-NLS-1$
				sb.append( pt.y );
				sb.append( "]" ); //$NON-NLS-1$
				return sb.toString( );
			}
		}
		
		protected final static IGObjectFactory _goFactory = GObjectFactory.instance( );
		private List<IndexedPoint> top = new ArrayList<IndexedPoint>();
		private List<IndexedPoint> bottom = new ArrayList<IndexedPoint>();
		private double baseStart;
		private double baseEnd;
		private boolean bTransposed;
		
		public Envelop( double baseStart, double baseEnd, double zeroLocation, boolean bTransposed )
		{
			this.bTransposed = bTransposed;
			this.baseStart = baseStart;
			this.baseEnd = baseEnd;
			IndexedPoint ipt0 = new IndexedPoint(0, baseStart, zeroLocation);
			IndexedPoint ipt1 = new IndexedPoint(0, baseEnd, zeroLocation);
			top.add( ipt0 );
			top.add( ipt1 );
			IndexedPoint ipt2 = ipt0.copy( );
			IndexedPoint ipt3 = ipt1.copy( );
			bottom.add( ipt2 );
			bottom.add( ipt3 );
		}
		
		
		private List<IndexedPoint> merge( List<IndexedPoint> list, int index, StraightLine sl, boolean bLessThan )
		{
			int len = list.size( );
			if ( len < 2 )
			{
				return list;
			}
			
			List<IndexedPoint> list_new = new ArrayList<IndexedPoint>();
			
			IndexedPoint ipt0 = list.get( 0 );
			
			for ( int i = 1; i < len; i++ )
			{
				IndexedPoint ipt1 = list.get( i );

				double y0 = sl.getYfromX( ipt0.getX( ) );
				double y1 = sl.getYfromX( ipt1.getX( ) );
				
				boolean bUpdate0 = bLessThan ? ( y0 < ipt0.getY( ) ) : ( y0 > ipt0.getY( ) );
				boolean bUpdate1 = bLessThan ? ( y1 < ipt1.getY( ) ) : ( y1 > ipt1.getY( ) );

				Point pt = sl.getCrossPoint( ipt0.pt, ipt1.pt );
				IndexedPoint ipn = new IndexedPoint( bUpdate0 ? ipt0.index : index, pt);
				
				if (i == 1 )
				{
					if ( bUpdate0 ) 
					{
						ipt0.index = index;
						ipt0.setY( y0 );
					}
					list_new.add( ipt0 );
				}
				else
				{
					if ( !bUpdate0 ) 
					{
						list_new.add( ipt0 );
					}
				}
				
				
				if ( pt != null )
				{
					list_new.add( ipn );
				}

				
				if ( i == len - 1 )
				{
					if ( bUpdate1)
					{
						ipt1.setY( y1 );
					}
					list_new.add( ipt1 );
				}
				
				ipt0 = ipt1;
			}

			return list_new;
		}
		
		private void mergeTop( int index, StraightLine sl )
		{
			this.top = merge( this.top, index, sl, !bTransposed );
		}

		private void mergeBottom( int index, StraightLine sl )
		{
			this.bottom = merge( this.bottom, index, sl, bTransposed );
		}
		
		public static Location[] createPolygonFromLine( double x0, double y0,
				double x1, double y1, double dTapeWidth )
		{
			Location[] loa = new Location[4];
			loa[0] = _goFactory.createLocation( x0, y0 );
			loa[1] = _goFactory.createLocation( x1, y1 );
			loa[2] = _goFactory.createLocation( x1 + dTapeWidth, y1 - dTapeWidth );
			loa[3] = _goFactory.createLocation( x0 + dTapeWidth, y0 - dTapeWidth );
			return loa;
		}

		public List<Location[]> getTopChanges( int index, double dTapeWidth )
		{
			List<Location[]> list = new ArrayList<Location[]>( );
			int len = top.size( );
			IndexedPoint[] top_a = new IndexedPoint[len];
			top_a = top.toArray( top_a );

			if ( len > 0 )
			{
				for ( int i = 1; i < len; i++ )
				{
					IndexedPoint ipt0 = top_a[i-1];
					IndexedPoint ipt1 = top_a[i];

					if ( ipt0.index == index - 1 || ipt0.index == index )
					{
						if ( !bTransposed )
						{
							Location[] loa = createPolygonFromLine( ipt0.getX( ),
									ipt0.getY( ),
									ipt1.getX( ),
									ipt1.getY( ),
									dTapeWidth );
							list.add( loa );
						}
						else
						{
							Location[] loa = createPolygonFromLine( ipt0.getY( ),
									ipt0.getX( ),
									ipt1.getY( ),
									ipt1.getX( ),
									dTapeWidth );
							list.add( loa );
						}
						
					}
				}
			}
			return list;
		}
		
		public List<Location[]> getBottomChanges( int index, double dTapeWidth )
		{
			List<Location[]> list = new ArrayList<Location[]>( );
			int len = bottom.size( );
			IndexedPoint[] bottom_a = new IndexedPoint[len];
			bottom_a = bottom.toArray( bottom_a );
			
			if ( len > 0 )
			{
				for ( int i = 1; i < len; i++ )
				{
					IndexedPoint ipt0 = bottom_a[i-1];
					IndexedPoint ipt1 = bottom_a[i];
					
					if ( ipt0.index == index - 1 || ipt0.index == index )
					{
						if ( !bTransposed )
						{
							Location[] loa = createPolygonFromLine( ipt0.getX( ),
									ipt0.getY( ),
									ipt1.getX( ),
									ipt1.getY( ),
									dTapeWidth );

							list.add( loa );
						}
						else
						{
							Location[] loa = createPolygonFromLine( ipt0.getY( ),
									ipt0.getX( ),
									ipt1.getY( ),
									ipt1.getX( ),
									dTapeWidth );

							list.add( loa );
						}
					}
				}
			}
			
			return list;
		}
		
		public void addLine( int index, double valueStart, double valueEnd )
		{
			StraightLine sl = new StraightLine( baseStart,
					valueStart,
					baseEnd,
					valueEnd );
			
			mergeTop( index, sl );
			mergeBottom( index, sl );
		}
	}
	
}
