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

package org.eclipse.birt.chart.render;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.DeferredCache;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

/**
 * CurveRenderer
 */
final class CurveRenderer
{

	/**
	 * 
	 */
	private final float kError = 0.5f;

	/**
	 * 
	 */
	private int iNumberOfPoints = 0;

	/**
	 * 
	 */
	private Spline spX = null, spY = null;

	/**
	 * 
	 */
	private float[] fa, faX, faY, lastX, lastY;

	/**
	 * 
	 */
	private final float zeroLocation;

	/**
	 * 
	 */
	private final Object oSource;

	/**
	 * 
	 */
	private final LineAttributes lia;

	/**
	 * 
	 */
	private final Location loStart, loEnd;

	/**
	 * 
	 */
	private final DeferredCache dc;

	/**
	 * 
	 */
	private final ChartWithAxes cwa;
	/**
	 * 
	 */
	private final boolean bShowAsTape;

	/**
	 * 
	 */
	private final boolean bFillArea;

	/**
	 * 
	 */
	private final boolean bUseLastState;

	/**
	 * 
	 */
	private final boolean bKeepState;

	/**
	 * 
	 */
	private final boolean bTranslucent;

	/**
	 * 
	 */
	private final boolean bDeferred;

	/**
	 * 
	 */
	private final Location[] loa;

	/**
	 * 
	 */
	private final double dSeriesThickness;

	/**
	 * 
	 */
	private ColorDefinition fillColor, sideColor, tapeColor;

	/**
	 * 
	 */
	private final BaseRenderer iRender;

	/**
	 * 
	 * @param _render
	 * @param _lia
	 * @param _faX
	 * @param _faY
	 */
	CurveRenderer( ChartWithAxes _cwa, BaseRenderer _render,
			LineAttributes _lia, float[] _faX, float[] _faY,
			boolean _bShowAsTape, boolean _bDeferred, boolean _bKeepState )
	{
		this( _cwa,
				_render,
				_lia,
				_faX,
				_faY,
				0,
				_bShowAsTape,
				false,
				false,
				false,
				_bDeferred,
				_bKeepState );
	}

	/**
	 * @param _cwa
	 * @param _render
	 * @param _lia
	 * @param _faX
	 * @param _faY
	 * @param _bShowAsTape
	 */
	CurveRenderer( ChartWithAxes _cwa, BaseRenderer _render,
			LineAttributes _lia, float[] _faX, float[] _faY,
			float _zeroLocation, boolean _bShowAsTape, boolean _bFillArea,
			boolean _bTranslucent, boolean _bUseLastState, boolean _bDeferred,
			boolean _bKeepState )
	{
		bFillArea = _bFillArea;
		bShowAsTape = _bShowAsTape;
		bDeferred = _bDeferred;
		loa = ( bShowAsTape || bFillArea ) ? new Location[4] : null;
		dSeriesThickness = _cwa.getSeriesThickness( );
		if ( loa != null )
		{
			for ( int i = 0; i < 4; i++ )
			{
				loa[i] = LocationImpl.create( 0, 0 );
			}
		}

		cwa = _cwa;
		faX = _faX;
		faY = _faY;
		lia = _lia;
		zeroLocation = _zeroLocation;
		bTranslucent = _bTranslucent;
		oSource = _render.getSeries( );
		dc = _render.getDeferredCache( );
		this.iRender = _render;
		loStart = LocationImpl.create( 0, 0 );
		loEnd = LocationImpl.create( 0, 0 );

		bUseLastState = _bUseLastState;
		bKeepState = _bKeepState;

		fillColor = lia.getColor( );
		tapeColor = lia.getColor( ).brighter( );
		sideColor = lia.getColor( ).darker( );
		if ( bTranslucent )
		{
			fillColor = fillColor.translucent( );
			tapeColor = tapeColor.translucent( );
			sideColor = sideColor.translucent( );
		}
	}

	/**
	 * 
	 * @param ipr
	 * @throws RenderingException
	 */
	public final void draw( IPrimitiveRenderer ipr ) throws ChartException
	{
		if ( !lia.isSetVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.RENDERING,
					"exception.curve.visibility.unset",//$NON-NLS-1$ 
					ResourceBundle.getBundle( Messages.ENGINE,
							iRender.getRunTimeContext( ).getLocale( ) ) );
		}

		if ( !lia.isVisible( ) )
		{
			return;
		}

		iNumberOfPoints = faX.length;

		if ( iNumberOfPoints <= 1 )
		{
			return;
		}

		// X-CORDINATES
		spX = new Spline( faX ); // X-SPLINE

		// Y-CORDINATES
		spY = new Spline( faY ); // Y-SPLINE

		fa = new float[iNumberOfPoints];
		for ( int i = 0; i < iNumberOfPoints; i++ )
		{
			fa[i] = i;
		}

		renderCurve( ipr, 0, 0 ); // ACTUAL CURVE
	}

	/**
	 * 
	 * @param ipr
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @throws RenderingException
	 */
	private final void plotPlane( IPrimitiveRenderer ipr, float x1, float y1,
			float x2, float y2, boolean drawSide ) throws ChartException
	{
		final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( oSource,
				PolygonRenderEvent.class );
		pre.setOutline( null );
		pre.setBackground( tapeColor );
		loa[0].set( x1 + kError, y1 + kError );
		loa[1].set( x2 + kError, y2 + kError );
		loa[2].set( x2 + kError + dSeriesThickness, y2
				+ kError
				- dSeriesThickness );
		loa[3].set( x1 + kError + dSeriesThickness, y1
				+ kError
				- dSeriesThickness );
		pre.setPoints( loa );

		if ( bDeferred )
		{
			dc.addPlane( pre, PrimitiveRenderEvent.FILL );
		}
		else
		{
			ipr.fillPolygon( pre );
		}

		if ( drawSide )
		{
			pre.setBackground( sideColor );
			loa[0].set( x2 + kError, y2 + kError );
			loa[1].set( x2 + kError + dSeriesThickness, y2
					+ kError
					- dSeriesThickness );
			if ( cwa.isTransposed( ) )
			{
				loa[2].set( zeroLocation, y2 + kError - dSeriesThickness );
				loa[3].set( zeroLocation, y2 + kError );
			}
			else
			{
				loa[2].set( x2 + kError + dSeriesThickness, zeroLocation );
				loa[3].set( x2 + kError, zeroLocation );
			}
			pre.setPoints( loa );

			if ( bDeferred )
			{
				dc.addPlane( pre, PrimitiveRenderEvent.FILL );
			}
			else
			{
				ipr.fillPolygon( pre );
			}
		}
	}

	/**
	 * 
	 * @param ipr
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @throws RenderingException
	 */
	private final void plotLine( IPrimitiveRenderer ipr, float x1, float y1,
			float x2, float y2 ) throws ChartException
	{
		final LineRenderEvent lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( oSource,
				LineRenderEvent.class );
		lre.setLineAttributes( lia );
		loStart.set( x1 + kError, y1 + kError );
		loEnd.set( x2 + kError, y2 + kError );
		lre.setStart( loStart );
		lre.setEnd( loEnd );

		if ( bDeferred )
		{
			dc.addLine( lre );
		}
		else
		{
			ipr.drawLine( lre );
		}
	}

	/**
	 * 
	 * @param t
	 * @param faXY
	 * @return
	 */
	private final boolean computeSpline( float t, float[] faXY )
	{
		if ( spX == null || spY == null )
		{
			return false;
		}
		faXY[0] = spX.computeValue( t );
		faXY[1] = spY.computeValue( t );
		return true;
	}

	/**
	 * @param ipr
	 * @param points
	 * @throws ChartException
	 */
	private final void plotArea( IPrimitiveRenderer ipr, List points )
			throws ChartException
	{
		if ( points == null || points.size( ) < 1 )
		{
			return;
		}

		final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( oSource,
				PolygonRenderEvent.class );

		if ( bUseLastState )
		{
			Object obj = iRender.getRunTimeContext( ).getState( AreaSeries.class );
			lastX = null;
			lastY = null;

			if ( obj instanceof List && ( (List) obj ).size( ) > 0 )
			{
				List lst = (List) obj;

				for ( int i = 0; i < lst.size( ); i++ )
				{
					Object o = lst.get( i );

					if ( o instanceof float[] )
					{
						if ( lastX == null )
						{
							lastX = new float[lst.size( )];
							lastY = new float[lastX.length];
						}

						lastX[i] = ( (float[]) o )[0];
						lastY[i] = ( (float[]) o )[1];
					}
					else
					{
						lastX = null;
						lastY = null;
						break;
					}
				}
			}

			if ( lastX != null )
			{

				Location[] pa = new Location[points.size( ) + lastX.length];

				for ( int i = 0; i < points.size( ); i++ )
				{
					float[] pt = (float[]) points.get( i );
					pa[i] = LocationImpl.create( pt[0], pt[1] );
				}

				int l = points.size( );
				for ( int i = lastX.length - 1; i >= 0; i-- )
				{
					pa[l + lastX.length - 1 - i] = LocationImpl.create( lastX[i],
							lastY[i] );
				}

				pre.setOutline( null );
				pre.setPoints( pa );
				pre.setBackground( fillColor );

				if ( bDeferred )
				{
					dc.addPlane( pre, PrimitiveRenderEvent.FILL );
				}
				else
				{
					ipr.fillPolygon( pre );
				}

				return;
			}
		}

		Location[] pa = new Location[points.size( ) + 2];

		for ( int i = 0; i < points.size( ); i++ )
		{
			float[] pt = (float[]) points.get( i );
			pa[i] = LocationImpl.create( pt[0], pt[1] );
		}

		if ( cwa.isTransposed( ) )
		{
			pa[pa.length - 2] = LocationImpl.create( zeroLocation,
					pa[pa.length - 3].getY( ) );
			pa[pa.length - 1] = LocationImpl.create( zeroLocation, pa[0].getY( ) );
		}
		else
		{
			pa[pa.length - 2] = LocationImpl.create( pa[pa.length - 3].getX( ),
					zeroLocation );
			pa[pa.length - 1] = LocationImpl.create( pa[0].getX( ),
					zeroLocation );
		}

		pre.setOutline( null );
		pre.setPoints( pa );
		pre.setBackground( fillColor );

		if ( bDeferred )
		{
			dc.addPlane( pre, PrimitiveRenderEvent.FILL );
		}
		else
		{
			ipr.fillPolygon( pre );
		}
	}

	/**
	 * 
	 * @param ipr
	 * @param fXOffset
	 * @param fYOffset
	 * @throws RenderingException
	 */
	private final void renderCurve( IPrimitiveRenderer ipr, float fXOffset,
			float fYOffset ) throws ChartException
	{
		final float[] faKnotXY1 = new float[2];
		final float[] faKnotXY2 = new float[2];
		if ( !computeSpline( fa[0], faKnotXY1 ) )
		{
			return;
		}

		int iNumberOfDivisions;
		float fX, fY;
		float[] faXY1, faXY2;
		float fT;

		final ArrayList stateList = new ArrayList( );

		for ( int i = 0; i < iNumberOfPoints - 1; i++ )
		{
			if ( !computeSpline( fa[i + 1], faKnotXY2 ) )
			{
				continue;
			}
			fX = faKnotXY2[0] - faKnotXY1[0];
			fY = faKnotXY2[1] - faKnotXY1[1];
			iNumberOfDivisions = (int) ( Math.sqrt( fX * fX + fY * fY ) / 5.0f ) + 1;

			faXY1 = new float[2];
			faXY2 = new float[2];
			if ( !computeSpline( fa[i], faXY1 ) )
			{
				continue;
			}

			for ( int j = 0; j < iNumberOfDivisions; j++ )
			{
				fT = fa[i]
						+ ( fa[i + 1] - fa[i] )
						* (float) ( j + 1 )
						/ (float) iNumberOfDivisions;
				if ( !computeSpline( fT, faXY2 ) )
				{
					continue;
				}
				if ( bShowAsTape )
				{
					// TODO user a single surface to draw the tape.
					boolean drawSide = ( i == iNumberOfPoints - 2 )
							&& ( j == iNumberOfDivisions - 1 && bKeepState);

					plotPlane( ipr,
							faXY1[0] + fXOffset,
							faXY1[1] + fYOffset,
							faXY2[0] + fXOffset,
							faXY2[1] + fYOffset,
							drawSide );
				}

				if ( !bFillArea )
				{
					// if fill area, defer to the loop end.
					plotLine( ipr,
							faXY1[0] + fXOffset,
							faXY1[1] + fYOffset,
							faXY2[0] + fXOffset,
							faXY2[1] + fYOffset );
				}

				// TODO remove the duplicate points.
				stateList.add( new float[]{
						faXY1[0] + fXOffset, faXY1[1] + fYOffset
				} );
				stateList.add( new float[]{
						faXY2[0] + fXOffset, faXY2[1] + fYOffset
				} );

				faXY1[0] = faXY2[0];
				faXY1[1] = faXY2[1];
			}

			faKnotXY1[0] = faKnotXY2[0];
			faKnotXY1[1] = faKnotXY2[1];
		}

		if ( bFillArea )
		{
			plotArea( ipr, stateList );
		}

		if ( bKeepState )
		{
			iRender.getRunTimeContext( ).putState( AreaSeries.class, stateList );
		}
	}

	/**
	 * 
	 */
	private static class Spline
	{

		/**
		 * 
		 */
		private final int iNumberOfPoints;

		/**
		 * 
		 */
		private final float[] fa;

		/**
		 * 
		 */
		private final float[] faA;

		/**
		 * 
		 */
		private final float[] faB;

		/**
		 * 
		 */
		private final float[] faC;

		/**
		 * 
		 * @param _fa
		 */
		public Spline( float[] _fa )
		{
			iNumberOfPoints = _fa.length;
			fa = new float[iNumberOfPoints];

			faA = new float[iNumberOfPoints - 1];
			faB = new float[iNumberOfPoints - 1];
			faC = new float[iNumberOfPoints - 1];

			for ( int i = 0; i < iNumberOfPoints; i++ )
			{
				fa[i] = _fa[i];
			}
			computeCoefficients( );
		}

		/**
		 * 
		 */
		private final void computeCoefficients( )
		{
			float p, dy1, dy2;
			dy1 = fa[1] - fa[0];
			for ( int i = 1; i < iNumberOfPoints - 1; i++ )
			{
				dy2 = fa[i + 1] - fa[i];
				faC[i] = 0.5f;
				faB[i] = 1.0f - faC[i];
				faA[i] = 3.0f * ( dy2 - dy1 );
				dy1 = dy2;
			}

			faC[0] = 0.0f;
			faB[0] = 0.0f;
			faA[0] = 0.0f;

			for ( int i = 1; i < iNumberOfPoints - 1; i++ )
			{
				p = faB[i] * faC[i - 1] + 2.0f;
				faC[i] = -faC[i] / p;
				faB[i] = ( faA[i] - faB[i] * faB[i - 1] ) / p;
			}

			dy1 = 0;
			for ( int i = iNumberOfPoints - 2; i >= 0; i-- )
			{
				dy2 = faC[i] * dy1 + faB[i];
				faA[i] = ( dy1 - dy2 ) / 6.0f;
				faB[i] = dy2 / 2.0f;
				faC[i] = ( fa[i + 1] - fa[i] ) - 1 * ( faB[i] + faA[i] );
				dy1 = dy2;
			}
		}

		/**
		 * 
		 * @param x
		 * @return
		 */
		private final float computeValue( float x )
		{
			if ( iNumberOfPoints < 2 )
			{
				return 0.0f;
			}

			int i = 0, iMiddle;
			int iRight = iNumberOfPoints - 1;

			while ( i + 1 < iRight )
			{
				iMiddle = ( i + iRight ) / 2;
				if ( iMiddle <= x )
				{
					i = iMiddle;
				}
				else
				{
					iRight = iMiddle;
				}
			}
			final float t = ( x - i );
			return faA[i] * t * t * t + faB[i] * t * t + faC[i] * t + fa[i];
		}
	}
}