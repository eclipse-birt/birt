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

package org.eclipse.birt.chart.computation.withaxes;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.Engine3D;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.UserDataSetHints;
import org.eclipse.birt.chart.computation.Vector;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.Text3DRenderEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.Location3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.util.Calendar;

/**
 * This class is capable of computing the content of a chart (with axes) based
 * on preferred sizes, text rotation, fit ability, scaling, etc and prepares it
 * for rendering.
 * 
 * WARNING: This is an internal class and subject to change
 */
public class PlotWith3DAxes extends PlotWithAxes
{

	private final double SPACE_THRESHOLD;

	private Engine3D engine;

	/**
	 * @param _ids
	 * @param _cwa
	 * @param _rtc
	 * @throws IllegalArgumentException
	 * @throws ChartException
	 */
	public PlotWith3DAxes( IDisplayServer _ids, ChartWithAxes _cwa,
			RunTimeContext _rtc ) throws IllegalArgumentException,
			ChartException
	{
		cwa = _cwa;
		ids = _ids;
		rtc = _rtc;
		dPointToPixel = ids.getDpiResolution( ) / 72d;
		SPACE_THRESHOLD = 5 * dPointToPixel;

		if ( cwa.isTransposed( ) )
		{
			// Not support transposed for 3D chart.
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.COMPUTATION,
					"exception.no.transposed.3D.chart", //$NON-NLS-1$
					Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
		}

		buildAxes( ); // CREATED ONCE
	}

	private Bounds getAdjustedPlotBounds( )
	{
		final Bounds bo = getPlotBounds( ).adjustedInstance( getPlotInsets( ) );

		// double width = bo.getWidth( );
		// double height = bo.getHeight( );

		// TODO consider lable/title space. now only used a fixed top insets.
		// double spacing = getTitleSpacing( width, height );
		bo.adjust( InsetsImpl.create( 3 * dPointToPixel, 0, 0, 0 ) );
		return bo;
	}

	public final Location getPanningOffset( )
	{
		final Bounds bo = getPlotBounds( ).adjustedInstance( getPlotInsets( ) );

		double xOff = bo.getLeft( );
		double yOff = bo.getTop( );

		// TODO read from custom panning setting

		return LocationImpl.create( xOff, yOff );
	}

	/**
	 * Returns the 3D engine for this render.
	 */
	public final Engine3D get3DEngine( )
	{
		if ( engine == null )
		{
			// Use a fixed light direction here.
			Vector lightDirection = new Vector( -1, 1, -1, false );
			// Vector lightDirection = new Vector( 0, 0, -1, false );

			final Bounds bo = getPlotBounds( ).adjustedInstance( getPlotInsets( ) );

			double width = bo.getWidth( );
			double height = bo.getHeight( );

			engine = new Engine3D( cwa.getRotation( ),
					lightDirection,
					width,
					height,
					500 * dPointToPixel,
					1500 * dPointToPixel,
					10 * dPointToPixel,
					10000 * dPointToPixel,
					100 );
		}

		return engine;
	}

	private double detectZoomScale( Engine3D engine, double xdz, double xOff,
			double yOff, double width, double height )
	{
		double zlen = 1 * dPointToPixel;
		double xlen = xdz * dPointToPixel;
		double ylen = ( xdz + 1 ) * dPointToPixel / 2d;

		List vertexList = new ArrayList( );

		Location3D bbl = Location3DImpl.create( -xlen / 2, -ylen / 2, -zlen / 2 );
		Location3D bbr = Location3DImpl.create( xlen / 2, -ylen / 2, -zlen / 2 );
		Location3D bfl = Location3DImpl.create( -xlen / 2, -ylen / 2, zlen / 2 );
		Location3D bfr = Location3DImpl.create( xlen / 2, -ylen / 2, zlen / 2 );
		Location3D tbl = Location3DImpl.create( -xlen / 2, ylen / 2, -zlen / 2 );
		Location3D tbr = Location3DImpl.create( xlen / 2, ylen / 2, -zlen / 2 );
		Location3D tfl = Location3DImpl.create( -xlen / 2, ylen / 2, zlen / 2 );
		Location3D tfr = Location3DImpl.create( xlen / 2, ylen / 2, zlen / 2 );

		vertexList.add( bbl );
		vertexList.add( bbr );
		vertexList.add( bfl );
		vertexList.add( bfr );
		vertexList.add( tbl );
		vertexList.add( tbr );
		vertexList.add( tfl );
		vertexList.add( tfr );

		Text3DRenderEvent event = new Text3DRenderEvent( this );

		double maxLeft = Double.MAX_VALUE;
		double maxRight = -Double.MAX_VALUE;
		double maxTop = Double.MAX_VALUE;
		double maxBottom = -Double.MAX_VALUE;
		Location p2d;
		double x, y;

		for ( Iterator itr = vertexList.iterator( ); itr.hasNext( ); )
		{
			Location3D p3d = (Location3D) itr.next( );

			event.setLocation3D( Location3DImpl.create( p3d.getX( ),
					p3d.getY( ),
					p3d.getZ( ) ) );
			if ( engine.processEvent( event, xOff, yOff ) != null )
			{
				p2d = event.getLocation( );

				x = p2d.getX( );
				y = p2d.getY( );

				if ( x < maxLeft )
				{
					maxLeft = x;
				}
				if ( x > maxRight )
				{
					maxRight = x;
				}
				if ( y < maxTop )
				{
					maxTop = y;
				}
				if ( y > maxBottom )
				{
					maxBottom = y;
				}
			}
		}

		double vSpace = maxTop - yOff;
		double hSpace = maxLeft - xOff;

		if ( yOff + height - maxBottom < maxTop - yOff )
		{
			vSpace = yOff + height - maxBottom;
		}

		if ( xOff + width - maxRight < maxLeft - xOff )
		{
			hSpace = xOff + width - maxRight;
		}

		double minSpace = Math.min( hSpace, vSpace );
		double lastMinspace = 0;
		boolean fit = hSpace > 0 && vSpace > 0;
		double lastScale = 1;
		double scale = lastScale;
		boolean iterateStarted = false;

		if ( !fit )
		{
			// if even the minimum scale failed, return with no iteration
			return 1;
		}

		while ( ChartUtil.mathGT( Math.abs( minSpace - lastMinspace ), 0 )
				&& ( fit && minSpace > SPACE_THRESHOLD || !fit ) )
		{
			if ( fit && !iterateStarted )
			{
				// double zoomin
				scale = lastScale * 2;
			}
			else
			{
				// dichotomia zoomin/out
				scale = ( lastScale + scale ) / 2;
			}

			maxLeft = Double.MAX_VALUE;
			maxRight = -Double.MAX_VALUE;
			maxTop = Double.MAX_VALUE;
			maxBottom = -Double.MAX_VALUE;

			boolean forceBreak = false;

			// check all 8 points.
			for ( Iterator itr = vertexList.iterator( ); itr.hasNext( ); )
			{
				Location3D p3d = (Location3D) itr.next( );

				event.setLocation3D( Location3DImpl.create( p3d.getX( ) * scale,
						p3d.getY( ) * scale,
						p3d.getZ( ) * scale ) );
				if ( engine.processEvent( event, xOff, yOff ) != null )
				{
					p2d = event.getLocation( );

					x = p2d.getX( );
					y = p2d.getY( );

					// System.out.println( "x: " + x + ", y: " + y );

					if ( x < maxLeft )
					{
						maxLeft = x;
					}
					if ( x > maxRight )
					{
						maxRight = x;
					}
					if ( y < maxTop )
					{
						maxTop = y;
					}
					if ( y > maxBottom )
					{
						maxBottom = y;
					}
				}
				else
				{
					fit = false;
					forceBreak = true;
					break;
				}
			}

			if ( !forceBreak )
			{
				vSpace = maxTop - yOff;
				hSpace = maxLeft - xOff;

				if ( yOff + height - maxBottom < maxTop - yOff )
				{
					vSpace = yOff + height - maxBottom;
				}

				if ( xOff + width - maxRight < maxLeft - xOff )
				{
					hSpace = xOff + width - maxRight;
				}

				fit = vSpace > 0 && hSpace > 0;
			}

			if ( fit )
			{
				lastMinspace = minSpace;
				minSpace = Math.min( hSpace, vSpace );
				double nextScale = 2 * scale - lastScale;
				lastScale = scale;
				scale = nextScale;
			}
			else if ( !iterateStarted )
			{
				iterateStarted = true;
			}

		}

		return lastScale;
	}

	private double computeAxisZoomFactor( Engine3D engine, double start,
			double end, Location3D startVertext, Location3D endVertext,
			double xOff, double yOff )
	{
		Text3DRenderEvent event = new Text3DRenderEvent( this );
		Location p2d;
		double x1, y1, x2, y2;

		event.setLocation3D( startVertext );
		if ( engine.processEvent( event, xOff, yOff ) != null )
		{
			p2d = event.getLocation( );

			x1 = p2d.getX( );
			y1 = p2d.getY( );

			event.setLocation3D( endVertext );

			if ( engine.processEvent( event, xOff, yOff ) != null )
			{
				p2d = event.getLocation( );

				x2 = p2d.getX( );
				y2 = p2d.getY( );

				return Math.sqrt( ( y2 - y1 )
						* ( y2 - y1 )
						+ ( x2 - x1 )
						* ( x2 - x1 ) )
						/ ( end - start );
			}
		}

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.computation.withaxes.PlotWithAxes#compute(org.eclipse.birt.chart.model.attribute.Bounds)
	 */
	public void compute( Bounds bo ) throws ChartException,
			IllegalArgumentException
	{
		bo = bo.scaledInstance( dPointToPixel ); // CONVERSION

		boPlotBackground = BoundsImpl.copyInstance( bo );

		// MUST BE 3-D DIMENSION ONLY HERE.
		iDimension = getDimension( cwa.getDimension( ) );
		assert iDimension == IConstants.THREE_D;

		dXAxisPlotSpacing = cwa.getPlot( ).getHorizontalSpacing( )
				* dPointToPixel; // CONVERSION
		dYAxisPlotSpacing = cwa.getPlot( ).getVerticalSpacing( )
				* dPointToPixel; // CONVERSION
		dZAxisPlotSpacing = dXAxisPlotSpacing;

		final Axis[] axa = cwa.getPrimaryBaseAxes( );
		final Axis axPrimaryBase = axa[0];
		final Axis axPrimaryOrthogonal = cwa.getPrimaryOrthogonalAxis( axPrimaryBase );
		final Axis axAncillaryBase = cwa.getAncillaryBaseAxis( axPrimaryBase );

		// INITIALIZE AXES DATASETS
		Object oaData = null;

		// COMPUTE PRIMARY BASE DATA
		OneAxis oaxPrimaryBase = aax.getPrimaryBase( );
		int iPrimaryAxisType = getAxisType( axPrimaryBase );
		if ( iPrimaryAxisType == TEXT || oaxPrimaryBase.isCategoryScale( ) )
		{
			oaData = getTypedDataSet( axPrimaryBase, iPrimaryAxisType, 0 );
		}
		else if ( ( iPrimaryAxisType & NUMERICAL ) == NUMERICAL )
		{
			oaData = getMinMax( axPrimaryBase, iPrimaryAxisType );
		}
		else if ( ( iPrimaryAxisType & DATE_TIME ) == DATE_TIME )
		{
			oaData = getMinMax( axPrimaryBase, iPrimaryAxisType );
		}
		DataSetIterator dsiPrimaryBase = ( oaData instanceof DataSetIterator ) ? (DataSetIterator) oaData
				: new DataSetIterator( oaData, iPrimaryAxisType );
		oaData = null;

		// COMPUTE ANCILLARY BASE DATA
		OneAxis oaxAncillaryBase = aax.getAncillaryBase( );
		int iAncillaryAxisType = getAxisType( axAncillaryBase );
		if ( iAncillaryAxisType == TEXT || oaxAncillaryBase.isCategoryScale( ) )
		{
			oaData = getAncillaryDataSet( axAncillaryBase,
					axPrimaryOrthogonal,
					iAncillaryAxisType );
		}
		else if ( ( iAncillaryAxisType & NUMERICAL ) == NUMERICAL )
		{
			oaData = getMinMax( axAncillaryBase, iAncillaryAxisType );
		}
		else if ( ( iAncillaryAxisType & DATE_TIME ) == DATE_TIME )
		{
			oaData = getMinMax( axAncillaryBase, iAncillaryAxisType );
		}
		DataSetIterator dsiAncillary = ( oaData instanceof DataSetIterator ) ? (DataSetIterator) oaData
				: new DataSetIterator( oaData, iAncillaryAxisType );
		oaData = null;

		// COMPUTE ORTHOGONAL DATA
		OneAxis oaxPrimaryOrthogonal = aax.getPrimaryOrthogonal( );
		int iOrthogonalAxisType = getAxisType( axPrimaryOrthogonal );
		DataSetIterator dsiOrthogonal = null;
		if ( ( iOrthogonalAxisType & NUMERICAL ) == NUMERICAL
				|| ( iOrthogonalAxisType & DATE_TIME ) == DATE_TIME )
		{
			dsiOrthogonal = new DataSetIterator( getMinMax( axPrimaryOrthogonal,
					iOrthogonalAxisType ),
					iOrthogonalAxisType );
		}
		else
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_FORMAT,
					"exception.orthogonal.axis.numerical.datetime", //$NON-NLS-1$
					Messages.getResourceBundle( rtc.getULocale( ) ) );
		}

		// COMPUTE AUTOMATIC ZOOM SCALE AND FACTOR
		double dXDZ = dsiPrimaryBase.size( ) * 1d / dsiAncillary.size( );

		final Bounds adjustedBounds = getAdjustedPlotBounds( );

		double xOffset = adjustedBounds.getLeft( );
		double yOffset = adjustedBounds.getTop( );

		double zoomScale = detectZoomScale( get3DEngine( ),
				dXDZ,
				xOffset,
				yOffset,
				adjustedBounds.getWidth( ),
				adjustedBounds.getHeight( ) );

		double dWZ = zoomScale;
		double dW = dXDZ * zoomScale;
		double dH = ( dW + dWZ ) / 2;
		double dX = -dW / 2;
		double dY = -dH / 2;
		double dZ = -dWZ / 2;

		Location panningOffset = getPanningOffset( );

		double xZoom = computeAxisZoomFactor( get3DEngine( ),
				dX,
				dX + dW,
				Location3DImpl.create( dX, dY, dZ ),
				Location3DImpl.create( dX + dW, dY, dZ ),
				panningOffset.getX( ),
				panningOffset.getY( ) );
		double yZoom = computeAxisZoomFactor( get3DEngine( ),
				dY,
				dY + dH,
				Location3DImpl.create( dX, dY, dZ ),
				Location3DImpl.create( dX, dY + dH, dZ ),
				panningOffset.getX( ),
				panningOffset.getY( ) );
		double zZoom = computeAxisZoomFactor( get3DEngine( ),
				dZ,
				dZ + dWZ,
				Location3DImpl.create( dX, dY, dZ ),
				Location3DImpl.create( dX, dY, dZ + dWZ ),
				panningOffset.getX( ),
				panningOffset.getY( ) );

		double dStart, dEnd;

		// COMPUTE PRIMARY-BASE-AXIS PROPERTIES AND ITS SCALE
		AutoScale scPrimaryBase = null;
		dStart = dX;
		dEnd = dX + dW;
		Scale sc = axPrimaryBase.getScale( );
		scPrimaryBase = AutoScale.computeScale( ids,
				oaxPrimaryBase,
				dsiPrimaryBase,
				iPrimaryAxisType,
				dStart,
				dEnd,
				sc.getMin( ),
				sc.getMax( ),
				sc.isSetStep( ) ? new Double( sc.getStep( ) ) : null,
				axPrimaryBase.getFormatSpecifier( ),
				rtc,
				FORWARD,
				xZoom );
		oaxPrimaryBase.set( scPrimaryBase ); // UPDATE SCALE ON PRIMARY-BASE
		// AXIS

		// COMPUTE ANCILLARY-BASE-AXIS PROPERTIES AND ITS SCALE
		AutoScale scAncillaryBase = null;
		dStart = dZ;
		dEnd = dZ + dWZ;
		sc = axAncillaryBase.getScale( );
		scAncillaryBase = AutoScale.computeScale( ids,
				oaxAncillaryBase,
				dsiAncillary,
				iAncillaryAxisType,
				dStart,
				dEnd,
				sc.getMin( ),
				sc.getMax( ),
				sc.isSetStep( ) ? new Double( sc.getStep( ) ) : null,
				axAncillaryBase.getFormatSpecifier( ),
				rtc,
				FORWARD,
				zZoom );
		oaxAncillaryBase.set( scAncillaryBase ); // UPDATE SCALE ON
		// ANCILLARY-BASE AXIS

		// COMPUTE PRIMARY-ORTHOGONAL-AXIS PROPERTIES AND ITS SCALE
		AutoScale scPrimaryOrthogonal = null;
		dStart = dY;
		dEnd = dH;
		sc = axPrimaryOrthogonal.getScale( );
		scPrimaryOrthogonal = AutoScale.computeScale( ids,
				oaxPrimaryOrthogonal,
				dsiOrthogonal,
				iOrthogonalAxisType,
				dStart,
				dEnd,
				sc.getMin( ),
				sc.getMax( ),
				sc.isSetStep( ) ? new Double( sc.getStep( ) ) : null,
				axPrimaryOrthogonal.getFormatSpecifier( ),
				rtc,
				FORWARD,
				yZoom );
		oaxPrimaryOrthogonal.set( scPrimaryOrthogonal ); // UPDATE SCALE ON
		// PRIMARY-ORTHOGONAL AXIS

		// Here we ignore the intersection Value/Max setting, always
		// use the Intersection.Min for 3D chart.
		double dYAxisLocationOnX = dX;
		double dYAxisLocationOnZ = dZ;
		double dXAxisLocation = dY;
		double dZAxisLocation = dY;

		// SETUP THE FULL DATASET FOR THE PRIMARY ORTHOGONAL AXIS
		iOrthogonalAxisType = getAxisType( axPrimaryOrthogonal );
		oaData = getTypedDataSet( axPrimaryOrthogonal, iOrthogonalAxisType, 0 );
		scPrimaryOrthogonal.setData( dsiOrthogonal );

		// Setup the full dataset for the ancillary base axis.
		iAncillaryAxisType = getAxisType( axAncillaryBase );
		if ( iAncillaryAxisType != IConstants.TEXT )
		{
			scAncillaryBase.setData( getTypedDataSet( axAncillaryBase,
					iAncillaryAxisType,
					0 ) );
		}

		// SETUP THE FULL DATASET FOR THE PRIMARY ORTHOGONAL AXIS
		iPrimaryAxisType = getAxisType( axPrimaryBase );
		if ( iPrimaryAxisType != IConstants.TEXT )
		{
			scPrimaryBase.setData( getTypedDataSet( axPrimaryBase,
					iPrimaryAxisType,
					0 ) );
		}

		scPrimaryBase.resetShifts( );
		scAncillaryBase.resetShifts( );
		scPrimaryOrthogonal.resetShifts( );

		// UPDATE FOR OVERLAYS
		final OneAxis axPH = aax.getPrimaryBase( );
		final OneAxis axPV = aax.getPrimaryOrthogonal( );
		final OneAxis axAB = aax.getAncillaryBase( );

		// keep old invocation to ensure compatibility.
		axPH.setAxisCoordinate( dXAxisLocation );
		axPV.setAxisCoordinate( dYAxisLocationOnX );
		axAB.setAxisCoordinate( dZAxisLocation );

		// set new 3D axis coordinate. this coordinate has been normalized to
		// Zero-coordinates.
		axPH.setAxisCoordinate3D( Location3DImpl.create( 0,
				dXAxisLocation,
				dYAxisLocationOnZ ) );
		axPV.setAxisCoordinate3D( Location3DImpl.create( dYAxisLocationOnX,
				0,
				dYAxisLocationOnZ ) );
		axAB.setAxisCoordinate3D( Location3DImpl.create( dYAxisLocationOnX,
				dZAxisLocation,
				0 ) );
	}

	/**
	 * @param ax
	 * @param orthogonalAxis
	 * @param iType
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	protected final DataSetIterator getAncillaryDataSet( Axis ax,
			Axis orthogonalAxis, int iType ) throws ChartException,
			IllegalArgumentException
	{
		final Series[] sea = ax.getRuntimeSeries( );
		final Series[] osea = orthogonalAxis.getRuntimeSeries( );

		if ( sea.length == 0 || osea.length == 0 ) // TBD: PULL FROM SAMPLE
		// DATA
		{
			if ( ( iType & NUMERICAL ) == NUMERICAL )
			{
				// TODO consistent with orthogonal sereis length;
				return new DataSetIterator( new Double[]{
						new Double( 1 ), new Double( 2 )
				} );
			}
			else if ( ( iType & DATE_TIME ) == DATE_TIME )
			{
				// TODO consistent with orthogonal sereis length;
				return new DataSetIterator( new Calendar[]{
						new CDateTime( ), new CDateTime( )
				} );
			}
			else if ( ( iType & TEXT ) == TEXT )
			{
				// use orthogonal sereis identifier instead.
				List data = new ArrayList( );

				for ( int i = 0; i < osea.length; i++ )
				{
					data.add( String.valueOf( osea[i].getSeriesIdentifier( ) ) );
				}

				return new DataSetIterator( (String[]) data.toArray( new String[0] ) );
			}
		}

		// Assume always use the first ancillary axis.
		DataSetIterator dsi = getTypedDataSet( sea[0], iType );
		List data = new ArrayList( );

		for ( int i = 0; i < osea.length; i++ )
		{
			if ( dsi.hasNext( ) )
			{
				data.add( dsi.next( ) );
			}
			else if ( ( iType & NUMERICAL ) == NUMERICAL )
			{
				data.add( new Double( 0 ) );
			}
			else if ( ( iType & DATE_TIME ) == DATE_TIME )
			{
				data.add( new CDateTime( ) );
			}
			else if ( ( iType & TEXT ) == TEXT )
			{
				data.add( osea[i].getSeriesIdentifier( ) );
			}
		}

		if ( ( iType & NUMERICAL ) == NUMERICAL )
		{
			return new DataSetIterator( NumberDataSetImpl.create( data ) );
		}
		else if ( ( iType & DATE_TIME ) == DATE_TIME )
		{
			return new DataSetIterator( DateTimeDataSetImpl.create( data ) );
		}
		else if ( ( iType & TEXT ) == TEXT )
		{
			return new DataSetIterator( TextDataSetImpl.create( data ) );
		}

		return null;
	}

	// /**
	// * Goals: 1. Adjust the two ends of the vertical axis to fit start/end
	// * labels 2. Compute the horizontal co-ordinate for the axis
	// *
	// * @param dBlockX
	// * @param dBlockWidth
	// * @param aax
	// *
	// * @return
	// */
	// private final double adjustAncillary( double dBlockX, double dBlockWidth,
	// AllAxes aax ) throws ChartException, IllegalArgumentException
	// {
	// final OneAxis axAB = aax.getAncillaryBase( );
	// final OneAxis axPV = aax.areAxesSwapped( ) ? aax.getPrimaryBase( )
	// : aax.getPrimaryOrthogonal( );
	// final AutoScale scZ = axAB.getScale( );
	// final AutoScale scY = axPV.getScale( );
	// final int iZLabelLocation = axAB.getLabelPosition( );
	// final int iYLabelLocation = axPV.getLabelPosition( );
	//
	// final int iYTitleLocation = axPV.getTitlePosition( );
	// final Label laZAxisLabels = axAB.getLabel( );
	// final Label laYAxisLabels = axPV.getLabel( );
	// final Label laYAxisTitle = axPV.getTitle( );
	// final int iYTickStyle = axPV.getCombinedTickStyle( );
	// final IntersectionValue iv = axPV.getIntersectionValue( );
	//
	// // COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS AND
	// // AXIS-PLOT SPACING
	// final boolean bTicksLeft = ( iYTickStyle & TICK_LEFT ) == TICK_LEFT; //
	// 'boolean'
	// // FOR
	// // CONVENIENCE
	// // &
	// // READABILITY
	// final boolean bTicksRight = ( iYTickStyle & TICK_RIGHT ) == TICK_RIGHT;
	// // 'boolean'
	// // FOR
	// // CONVENIENCE
	// // &
	// // READABILITY
	// final double dAppliedYAxisPlotSpacing = ( iv.iType ==
	// IntersectionValue.MAX || iv.iType == IntersectionValue.MIN ) ?
	// dYAxisPlotSpacing
	// : 0;
	//
	// // UPDATE Y-AXIS ENDPOINTS DUE TO AXIS LABEL SHIFTS
	// double dStart = scY.getStart( ), dEnd = scY.getEnd( );
	// scY.computeTicks( ids,
	// laYAxisLabels,
	// iYLabelLocation,
	// VERTICAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( !scY.isStepFixed( ) )
	// {
	// final Object[] oaMinMax = scY.getMinMax( );
	// while ( !scY.checkFit( ids, laYAxisLabels, iYLabelLocation ) )
	// {
	// if ( !scY.zoomOut( ) )
	// {
	// break;
	// }
	// scY.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
	// int tickCount = scY.computeTicks( ids,
	// laYAxisLabels,
	// iYLabelLocation,
	// VERTICAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( scY.getUnit( ) != null
	// && asInteger( scY.getUnit( ) ) == Calendar.YEAR
	// && tickCount <= 3 )
	// {
	// break;
	// }
	// }
	// }
	//
	// double dYAxisLabelsThickness = scY.computeAxisLabelThickness( ids,
	// axPV.getLabel( ),
	// VERTICAL );
	// double dYAxisTitleThickness = 0;
	// if ( laYAxisTitle.isVisible( ) )
	// {
	// final String sPreviousValue = laYAxisTitle.getCaption( ).getValue( );
	// laYAxisTitle.getCaption( )
	// .setValue( rtc.externalizedMessage( sPreviousValue ) );
	// try
	// {
	// dYAxisTitleThickness = computeBox( ids,
	// iYTitleLocation,
	// laYAxisTitle,
	// 0,
	// 0 ).getWidth( );
	// }
	// catch ( IllegalArgumentException uiex )
	// {
	// throw new ChartException( ChartEnginePlugin.ID,
	// ChartException.GENERATION,
	// uiex );
	// }
	// finally
	// {
	// laYAxisTitle.getCaption( ).setValue( sPreviousValue );
	// }
	// }
	//
	// double dZ = getLocation( scZ, iv ), dZ1 = dZ, dZ2 = dZ; // Y-AXIS BAND
	// // HORIZONTAL
	// // CO-ORDINATES
	//
	// // COMPUTE VALUES FOR z1, z, z2
	// // z = HORIZONTAL LOCATION OF Y-AXIS ALONG PLOT
	// // z1 = LEFT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TITLE, TICKS &
	// // SPACING)
	// // z2 = RIGHT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TITLE, TICKS &
	// // SPACING)
	// if ( iv.iType == IntersectionValue.MIN ) // LEFT
	// {
	// // NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MAX'
	// dZ -= dAppliedYAxisPlotSpacing;
	// dZ1 = dZ;
	// dZ2 = dZ;
	//
	// if ( bTicksLeft )
	// {
	// dZ1 -= TICK_SIZE;
	// }
	// if ( iYLabelLocation == LEFT )
	// {
	// dZ1 -= dYAxisLabelsThickness;
	// dZ2 += Math.max( // IF LABELS ARE LEFT, THEN RIGHT SPACING IS
	// // MAX(RT_TICK_SIZE, HORZ_SPACING)
	// bTicksRight ? TICK_SIZE : 0,
	// dAppliedYAxisPlotSpacing );
	// }
	// else if ( iYLabelLocation == RIGHT )
	// {
	// dZ2 += Math.max( // IF LABELS ARE RIGHT, THEN RIGHT SPACING
	// // IS
	// // MAX(RT_TICK_SIZE+AXIS_LBL_THCKNESS,
	// // HORZ_SPACING)
	// ( bTicksRight ? TICK_SIZE : 0 ) + dYAxisLabelsThickness,
	// dAppliedYAxisPlotSpacing );
	// }
	//
	// if ( iYTitleLocation == LEFT )
	// {
	// dZ1 -= dYAxisTitleThickness;
	// }
	// else if ( iYTitleLocation == RIGHT )
	// {
	// dZ2 += dYAxisTitleThickness;
	// }
	//
	// // ENSURE THAT WE DON'T GO BEHIND THE LEFT PLOT BLOCK EDGE
	// if ( dZ1 < dBlockX )
	// {
	// final double dDelta = ( dBlockX - dZ1 );
	// dZ1 = dBlockX;
	// dZ += dDelta;
	// dZ2 += dDelta;
	// }
	// final double dDeltaZ1 = dZ - dZ1;
	// final double dDeltaZ2 = dZ2 - dZ;
	//
	// // COMPUTE THE Y-AXIS BAND THICKNESS AND ADJUST X2 FOR LABELS BELOW
	// if ( iYLabelLocation == RIGHT )
	// {
	// // Y-AXIS BAND IS (x1 -> (x+AxisPlotSpacing))
	// dZ2 = ( dZ + dAppliedYAxisPlotSpacing );
	// }
	// dYAxisLabelsThickness = dZ2 - dZ1; // REUSE VARIABLE
	//
	// // CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
	// // UPPER END
	// scZ.computeAxisStartEndShifts( ids,
	// laZAxisLabels,
	// HORIZONTAL,
	// iZLabelLocation,
	// aax );
	// if ( dYAxisLabelsThickness > scZ.getStartShift( ) )
	// {
	// // REDUCE scX's STARTPOINT TO FIT THE Y-AXIS ON THE LEFT
	// dStart = dZ2 - scZ.getStartShift( );
	// }
	// else
	// {
	// dStart = scZ.getStart( );
	// }
	// dEnd = scZ.getEnd( );
	// scZ.resetShifts( );
	//
	// // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS IF
	// // OVERLAPS OCCUR
	// scZ.setEndPoints( dStart, dEnd );
	// scZ.computeTicks( ids,
	// laZAxisLabels,
	// iZLabelLocation,
	// HORIZONTAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	//
	// if ( !scZ.isStepFixed( ) )
	// {
	// final Object[] oaMinMax = scZ.getMinMax( );
	//
	// while ( !scZ.checkFit( ids, laZAxisLabels, iZLabelLocation ) )
	// {
	// if ( !scZ.zoomOut( ) )
	// {
	// break;
	// }
	// scZ.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
	// int tickCount = scZ.computeTicks( ids,
	// laZAxisLabels,
	// iZLabelLocation,
	// HORIZONTAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( scZ.getUnit( ) != null
	// && asInteger( scZ.getUnit( ) ) == Calendar.YEAR
	// && tickCount <= 3 )
	// {
	// break;
	// }
	// }
	// }
	//
	// // MOVE THE Y-AXIS TO THE LEFT EDGE OF THE PLOT IF SLACK SPACE
	// // EXISTS
	// if ( dYAxisLabelsThickness < scZ.getStartShift( ) )
	// {
	// dZ = scZ.getStart( ) - ( dZ2 - dZ );
	// }
	// dZ -= insCA.getLeft( );
	// dZ2 = dZ + dDeltaZ2;
	// dZ1 = dZ - dDeltaZ1;
	//
	// axPV.setTitleCoordinate( ( iYTitleLocation == LEFT ) ? dZ1 - 1
	// : dZ2 + 1 - dYAxisTitleThickness );
	//
	// }
	// else if ( iv.iType == IntersectionValue.MAX ) // RIGHT
	// {
	// // NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'
	//
	// dZ += dAppliedYAxisPlotSpacing;
	// dZ1 = dZ;
	// dZ2 = dZ;
	// if ( bTicksRight )
	// {
	// dZ2 += TICK_SIZE;
	// }
	//
	// if ( iYLabelLocation == RIGHT )
	// {
	// dZ2 += dYAxisLabelsThickness;
	// dZ1 -= Math.max( bTicksLeft ? TICK_SIZE : 0,
	// dAppliedYAxisPlotSpacing );
	// }
	// else if ( iYLabelLocation == LEFT )
	// {
	// dZ1 -= Math.max( ( bTicksLeft ? TICK_SIZE : 0 )
	// + dYAxisLabelsThickness, dAppliedYAxisPlotSpacing );
	// }
	// if ( iYTitleLocation == RIGHT )
	// {
	// dZ2 += dYAxisTitleThickness;
	// }
	// else if ( iYTitleLocation == LEFT )
	// {
	// dZ1 -= dYAxisTitleThickness;
	// }
	//
	// // ENSURE THAT WE DON'T GO AHEAD OF THE RIGHT PLOT BLOCK EDGE
	// if ( dZ2 > dBlockX + dBlockWidth )
	// {
	// final double dDelta = dZ2 - ( dBlockX + dBlockWidth );
	// dZ2 = dBlockX + dBlockWidth;
	// dZ -= dDelta;
	// dZ1 -= dDelta;
	// }
	// final double dDeltaZ1 = dZ - dZ1;
	// final double dDeltaZ2 = dZ2 - dZ;
	//
	// // COMPUTE THE Y-AXIS BAND THICKNESS AND ADJUST X1 IF Y-AXIS LABELS
	// // ARE ON THE LEFT
	// if ( iYLabelLocation == LEFT )
	// {
	// // Y-AXIS BAND IS ((x-AxisPlotSpacing) -> x2)
	// dZ1 = ( dZ - dAppliedYAxisPlotSpacing );
	// }
	// dYAxisLabelsThickness = dZ2 - dZ1; // REUSE VARIABLE
	//
	// // CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
	// // UPPER END
	// scZ.computeAxisStartEndShifts( ids,
	// laZAxisLabels,
	// HORIZONTAL,
	// iZLabelLocation,
	// aax );
	// if ( dYAxisLabelsThickness > scZ.getEndShift( ) )
	// {
	// // REDUCE scX's ENDPOINT TO FIT THE Y-AXIS ON THE RIGHT
	// dEnd = dZ1 + scZ.getEndShift( );
	// }
	// else
	// {
	// dEnd = scZ.getEnd( );
	// }
	// dStart = scZ.getStart( );
	//
	// scZ.resetShifts( );
	//
	// // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS IF
	// // OVERLAPS OCCUR
	// scZ.setEndPoints( dStart, dEnd );
	// scZ.computeTicks( ids,
	// laZAxisLabels,
	// iZLabelLocation,
	// HORIZONTAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( !scZ.isStepFixed( ) )
	// {
	// final Object[] oaMinMax = scZ.getMinMax( );
	// while ( !scZ.checkFit( ids, laZAxisLabels, iZLabelLocation ) )
	// {
	// if ( !scZ.zoomOut( ) )
	// {
	// break;
	// }
	// scZ.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
	// int tickCount = scZ.computeTicks( ids,
	// laZAxisLabels,
	// iZLabelLocation,
	// HORIZONTAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( scZ.getUnit( ) != null
	// && asInteger( scZ.getUnit( ) ) == Calendar.YEAR
	// && tickCount <= 3 )
	// {
	// break;
	// }
	// }
	// }
	//
	// // MOVE THE Y-AXIS TO THE LEFT EDGE OF THE PLOT IF SLACK SPACE
	// // EXISTS
	// if ( dYAxisLabelsThickness < scZ.getEndShift( ) )
	// {
	// dZ = scZ.getEnd( ) - ( dZ1 - dZ );
	// }
	// dZ += insCA.getRight( );
	// dZ2 = dZ + dDeltaZ2;
	// dZ1 = dZ - dDeltaZ1;
	//
	// axPV.setTitleCoordinate( ( iYTitleLocation == LEFT ) ? dZ1 - 1
	// : dZ2 + 1 - dYAxisTitleThickness );
	//
	// }
	// else
	// {
	// double dDeltaX1 = 0, dDeltaX2 = 0;
	// if ( iYTitleLocation == RIGHT )
	// {
	// dZ2 += dYAxisTitleThickness;
	// }
	// else if ( iYTitleLocation == LEFT )
	// {
	// dZ1 -= dYAxisTitleThickness;
	// }
	//
	// if ( iYLabelLocation == LEFT )
	// {
	// dZ1 -= ( bTicksLeft ? TICK_SIZE : 0 ) + dYAxisLabelsThickness;
	// dZ2 += ( bTicksRight ? TICK_SIZE : 0 );
	// dDeltaX1 = dZ - dZ1;
	// dDeltaX2 = dZ2 - dZ;
	//
	// // CHECK IF LEFT EDGE OF Y-AXIS BAND GOES BEHIND THE PLOT LEFT
	// // EDGE
	// if ( dZ1 < dBlockX )
	// {
	// final Object[] oaMinMax = scZ.getMinMax( );
	// boolean bForceBreak = false;
	//
	// // A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE LEFT EDGE
	// // OF THE Y-AXIS LABELS WITH THE LEFT EDGE OF THE PLOT
	// // AND/OR
	// // ENSURE THAT THE START POINT OF THE X-AXIS SCALE IS
	// // SUITABLY POSITIONED
	//
	// // computeTicks(g2d, fm, iXLabelLocation, iXRotation,
	// // HORIZONTAL, scX.dStart, scX.dEnd, scX, true);
	// do
	// {
	// // CANCEL OUT THE ENDPOINT LABEL SHIFT COMPUTATIONS FROM
	// // THE X-AXIS
	// scZ.setEndPoints( scZ.getStart( ) - scZ.getStartShift( ),
	// scZ.getEnd( ) + scZ.getEndShift( ) ); // RESTORE
	// scZ.resetShifts( );
	//
	// // APPLY THE AXIS REDUCTION FORMULA W.R.T. X-AXIS
	// // STARTPOINT
	// double[] da = scZ.getEndPoints( );
	// double dT_RI = dBlockX - dZ1; // THRESHOLD -
	// // REQUESTEDINTERSECTION
	// double dAMin_AMax = da[1] - da[0];
	// double dAMax_RI = da[1] - dZ;
	// double dDelta = ( dT_RI / dAMax_RI ) * dAMin_AMax;
	// dStart = da[0] + dDelta;
	// dEnd = da[1];
	//
	// if ( dStart < dBlockX )
	// {
	// dStart = dBlockX;
	// bForceBreak = true; // ADJUST THE TOP EDGE OF THE
	// // Y-AXIS SCALE TO THE TOP EDGE
	// // OF THE PLOT BLOCK
	// }
	//
	// // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
	// // LABELS IF OVERLAPS OCCUR
	// scZ.setEndPoints( dStart, dEnd );
	// scZ.computeTicks( ids,
	// laZAxisLabels,
	// iZLabelLocation,
	// HORIZONTAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// while ( !scZ.checkFit( ids,
	// laZAxisLabels,
	// iZLabelLocation ) )
	// {
	// if ( !scZ.zoomOut( ) )
	// {
	// bForceBreak = true;
	// break;
	// }
	// scZ.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
	// int tickCount = scZ.computeTicks( ids,
	// laZAxisLabels,
	// iZLabelLocation,
	// HORIZONTAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( scZ.getUnit( ) != null
	// && asInteger( scZ.getUnit( ) ) == Calendar.YEAR
	// && tickCount <= 3 )
	// {
	// bForceBreak = true;
	// break;
	// }
	// }
	//
	// dZ = getLocation( scZ, iv );
	// dZ1 = dZ - dDeltaX1; // RE-CALCULATE X-AXIS BAND LEFT
	// // EDGE
	// } while ( Math.abs( dZ1 - dBlockX ) > 1 && !bForceBreak );
	// }
	// else
	// {
	// // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
	// // LABELS IF OVERLAPS OCCUR
	// dStart = scZ.getStart( );
	// dEnd = scZ.getEnd( );
	// scZ.setEndPoints( dStart, dEnd );
	// scZ.computeTicks( ids,
	// laZAxisLabels,
	// iZLabelLocation,
	// HORIZONTAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( !scZ.isStepFixed( ) )
	// {
	// final Object[] oaMinMax = scZ.getMinMax( );
	// while ( !scZ.checkFit( ids,
	// laZAxisLabels,
	// iZLabelLocation ) )
	// {
	// if ( !scZ.zoomOut( ) )
	// {
	// break;
	// }
	// scZ.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
	// int tickCount = scZ.computeTicks( ids,
	// laZAxisLabels,
	// iZLabelLocation,
	// HORIZONTAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( scZ.getUnit( ) != null
	// && asInteger( scZ.getUnit( ) ) == Calendar.YEAR
	// && tickCount <= 3 )
	// {
	// break;
	// }
	// }
	// }
	// dZ = getLocation( scZ, iv );
	// }
	// dZ1 = dZ - dDeltaX1;
	// dZ2 = dZ + dDeltaX2;
	// }
	// else if ( iYLabelLocation == RIGHT )
	// {
	// dZ2 += ( bTicksRight ? TICK_SIZE : 0 ) + dYAxisLabelsThickness;
	// dZ1 -= ( bTicksLeft ? TICK_SIZE : 0 );
	// dDeltaX1 = dZ - dZ1;
	// dDeltaX2 = dZ2 - dZ;
	//
	// // CHECK IF RIGHT EDGE OF Y-AXIS BAND GOES BEHIND THE PLOT RIGHT
	// // EDGE
	// if ( dZ2 > dBlockX + dBlockWidth )
	// {
	// final Object[] oaMinMax = scZ.getMinMax( );
	// boolean bForceBreak = false;
	//
	// // A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE RIGHT EDGE
	// // OF THE Y-AXIS LABELS WITH THE RIGHT EDGE OF THE PLOT
	// // AND/OR
	// // ENSURE THAT THE START POINT OF THE X-AXIS SCALE IS
	// // SUITABLY POSITIONED
	//
	// do
	// {
	// // CANCEL OUT THE ENDPOINT LABEL SHIFT COMPUTATIONS FROM
	// // THE X-AXIS
	// scZ.setEndPoints( scZ.getStart( ) - scZ.getStartShift( ),
	// scZ.getEnd( ) + scZ.getEndShift( ) ); // RESTORE
	// scZ.resetShifts( );
	//
	// // APPLY THE AXIS REDUCTION FORMULA W.R.T. X-AXIS
	// // ENDPOINT
	// double[] da = scZ.getEndPoints( );
	// double dT_RI = dZ2 - ( dBlockX + dBlockWidth ); // THRESHOLD
	// // -
	// // REQUESTEDINTERSECTION
	// double dAMin_AMax = da[1] - da[0];
	// double dAMin_RI = dZ - da[0];
	// double dDelta = ( dT_RI / dAMin_RI ) * dAMin_AMax;
	// dEnd = da[1] - dDelta;
	// dStart = da[0];
	//
	// if ( dEnd > dBlockX + dBlockWidth )
	// {
	// dEnd = dBlockX + dBlockWidth;
	// bForceBreak = true; // ADJUST THE TOP EDGE OF THE
	// // Y-AXIS SCALE TO THE TOP EDGE
	// // OF THE PLOT BLOCK
	// }
	//
	// // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
	// // LABELS IF OVERLAPS OCCUR
	// scZ.setEndPoints( dStart, dEnd );
	// scZ.computeTicks( ids,
	// laZAxisLabels,
	// iZLabelLocation,
	// HORIZONTAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( !scZ.isStepFixed( ) )
	// {
	// while ( !scZ.checkFit( ids,
	// laZAxisLabels,
	// iZLabelLocation ) )
	// {
	// if ( !scZ.zoomOut( ) )
	// {
	// bForceBreak = true;
	// break;
	// }
	// scZ.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
	// int tickCount = scZ.computeTicks( ids,
	// laZAxisLabels,
	// iZLabelLocation,
	// HORIZONTAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( scZ.getUnit( ) != null
	// && asInteger( scZ.getUnit( ) ) == Calendar.YEAR
	// && tickCount <= 3 )
	// {
	// bForceBreak = true;
	// break;
	// }
	// }
	// }
	// dZ = getLocation( scZ, iv );
	// dZ2 = dZ + dDeltaX2; // RE-CALCULATE X-AXIS BAND
	// // RIGHT
	// // EDGE
	// } while ( Math.abs( dZ2 - ( dBlockX + dBlockWidth ) ) > 1
	// && !bForceBreak );
	// }
	// else
	// {
	// // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
	// // LABELS IF OVERLAPS OCCUR
	// dStart = scZ.getStart( );
	// dEnd = scZ.getEnd( );
	// scZ.setEndPoints( dStart, dEnd );
	// scZ.computeTicks( ids,
	// laZAxisLabels,
	// iZLabelLocation,
	// HORIZONTAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( !scZ.isStepFixed( ) )
	// {
	// final Object[] oaMinMax = scZ.getMinMax( );
	// while ( !scZ.checkFit( ids,
	// laZAxisLabels,
	// iZLabelLocation ) )
	// {
	// if ( !scZ.zoomOut( ) )
	// {
	// break;
	// }
	// scZ.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
	// int tickCount = scZ.computeTicks( ids,
	// laZAxisLabels,
	// iZLabelLocation,
	// HORIZONTAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( scZ.getUnit( ) != null
	// && asInteger( scZ.getUnit( ) ) == Calendar.YEAR
	// && tickCount <= 3 )
	// {
	// break;
	// }
	// }
	// }
	// dZ = getLocation( scZ, iv );
	// }
	// dZ2 = dZ + dDeltaX2;
	// dZ1 = dZ - dDeltaX1;
	// }
	// axPV.setTitleCoordinate( ( iYTitleLocation == LEFT ) ? dZ1 - 1
	// : dZ2 + 1 - dYAxisTitleThickness );
	// }
	// return dZ;
	// }
	//
	// /**
	// *
	// * @param dBlockY
	// * @param dBlockHeight
	// * @param aax
	// *
	// * @return
	// */
	// private final double adjustVerticalDueToHorizontalPlane( double dBlockY,
	// double dBlockHeight, AllAxes aax ) throws ChartException,
	// IllegalArgumentException
	// {
	// final OneAxis axPH = aax.areAxesSwapped( ) ? aax.getPrimaryOrthogonal( )
	// : aax.getPrimaryBase( );
	// final OneAxis axPV = aax.areAxesSwapped( ) ? aax.getPrimaryBase( )
	// : aax.getPrimaryOrthogonal( );
	// final AutoScale scX = axPH.getScale( );
	// final AutoScale scY = axPV.getScale( );
	// final int iXLabelLocation = axPH.getLabelPosition( );
	// final int iYLabelLocation = axPV.getLabelPosition( );
	// final int iXTitleLocation = axPH.getTitlePosition( );
	//
	// final Label laXAxisTitle = axPH.getTitle( );
	// final Label laYAxisLabels = axPV.getLabel( );
	// final int iXTickStyle = axPH.getCombinedTickStyle( );
	// final IntersectionValue iv = axPH.getIntersectionValue( );
	//
	// // COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS AND
	// // AXIS-PLOT SPACING
	// double dXAxisLabelsThickness = scX.computeAxisLabelThickness( ids,
	// axPH.getLabel( ),
	// HORIZONTAL );
	// double dXAxisTitleThickness = 0;
	// if ( laXAxisTitle.isVisible( ) )
	// {
	// final String sPreviousValue = laXAxisTitle.getCaption( ).getValue( );
	// laXAxisTitle.getCaption( )
	// .setValue( rtc.externalizedMessage( sPreviousValue ) ); // EXTERNALIZE
	// try
	// {
	// dXAxisTitleThickness = computeBox( ids,
	// iXTitleLocation,
	// laXAxisTitle,
	// 0,
	// 0 ).getHeight( );
	// }
	// catch ( IllegalArgumentException uiex )
	// {
	// throw new ChartException( ChartEnginePlugin.ID,
	// ChartException.GENERATION,
	// uiex );
	// }
	// finally
	// {
	// laXAxisTitle.getCaption( ).setValue( sPreviousValue ); // RESTORE
	// }
	// }
	//
	// double dY = getLocation( scY, iv ), dY1 = dY, dY2 = dY; // X-AXIS BAND
	// // VERTICAL
	// // CO-ORDINATES
	// final boolean bTicksAbove = ( iXTickStyle & TICK_ABOVE ) == TICK_ABOVE;
	// // 'boolean'
	// // FOR
	// // CONVENIENCE
	// // &
	// // READABILITY
	// final boolean bTicksBelow = ( iXTickStyle & TICK_BELOW ) == TICK_BELOW;
	// // 'boolean'
	// // FOR
	// // CONVENIENCE
	// // &
	// // READABILITY
	// final double dAppliedXAxisPlotSpacing = ( iv.iType ==
	// IntersectionValue.MAX || iv.iType == IntersectionValue.MIN ) ?
	// dXAxisPlotSpacing
	// : 0;
	//
	// // COMPUTE VALUES FOR y1, y, y2
	// // y = VERTICAL LOCATION OF X-AXIS ALONG PLOT
	// // y1 = UPPER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS, SPACING)
	// // y2 = LOWER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS, SPACING)
	// if ( iv.iType == IntersectionValue.MAX )
	// {
	// // NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'
	//
	// dY += dAppliedXAxisPlotSpacing;
	// dY1 = dY;
	// dY2 = dY;
	// if ( bTicksAbove )
	// {
	// dY1 += TICK_SIZE;
	// }
	// if ( iXLabelLocation == ABOVE )
	// {
	// dY1 += dXAxisLabelsThickness;
	// dY2 -= Math.max( bTicksBelow ? TICK_SIZE : 0,
	// dAppliedXAxisPlotSpacing );
	// }
	// else if ( iXLabelLocation == BELOW )
	// {
	// dY2 -= Math.max( ( bTicksBelow ? TICK_SIZE : 0 )
	// + dXAxisLabelsThickness, dAppliedXAxisPlotSpacing );
	// }
	//
	// if ( iXTitleLocation == ABOVE )
	// {
	// dY1 += dXAxisTitleThickness;
	// }
	// else if ( iXTitleLocation == BELOW )
	// {
	// dY2 -= dXAxisTitleThickness;
	// }
	//
	// // // ENSURE THAT WE DON'T GO ABOVE THE UPPER PLOT BLOCK EDGE
	// // if ( dY1 < dBlockY )
	// // {
	// // final double dDelta = ( dBlockY - dY1 );
	// // dY1 = dBlockY;
	// // dY += dDelta;
	// // dY2 += dDelta;
	// // }
	// double dDeltaY1 = dY1 - dY;
	// double dDeltaY2 = dY - dY2;
	//
	// // COMPUTE THE X-AXIS BAND THICKNESS AND ADJUST Y2 FOR LABELS BELOW
	// dXAxisLabelsThickness = 0; // REUSE VARIABLE
	// if ( iXLabelLocation == ABOVE )
	// {
	// // X-AXIS BAND IS (y1 -> y2)
	// dXAxisLabelsThickness = dY1 - dY2;
	// }
	// else if ( iXLabelLocation == BELOW )
	// {
	// // X-AXIS BAND IS (y1 -> (y+AxisPlotSpacing))
	// dY2 = ( dY - dAppliedXAxisPlotSpacing );
	// dXAxisLabelsThickness = dY1 - dY2;
	// }
	//
	// // CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
	// // UPPER END
	// if ( dXAxisLabelsThickness > scY.getEndShift( ) )
	// {
	// // REDUCE scY's ENDPOINT TO FIT THE X-AXIS AT THE TOP
	// scY.setEndPoints( scY.getStart( ) + scY.getStartShift( ),
	// scY.getEnd( ) - scY.getEndShift( ) );
	// double dStart = scY.getStart( ), dEnd = dY2 - scY.getEndShift( );
	// scY.resetShifts( );
	//
	// // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
	// // IF OVERLAPS OCCUR
	// scY.setEndPoints( dStart, dEnd );
	// scY.computeTicks( ids,
	// laYAxisLabels,
	// iYLabelLocation,
	// VERTICAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( !scY.isStepFixed( ) )
	// {
	// final Object[] oaMinMax = scY.getMinMax( );
	// while ( !scY.checkFit( ids, laYAxisLabels, iYLabelLocation ) )
	// {
	// if ( !scY.zoomOut( ) )
	// {
	// break;
	// }
	// scY.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
	// int tickCount = scY.computeTicks( ids,
	// laYAxisLabels,
	// iYLabelLocation,
	// VERTICAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( scY.getUnit( ) != null
	// && asInteger( scY.getUnit( ) ) == Calendar.YEAR
	// && tickCount <= 3 )
	// {
	// break;
	// }
	// }
	// }
	// }
	//
	// dY += insCA.getTop( );
	// dY1 = dY + dDeltaY1;
	// dY2 = dY - dDeltaY2;
	// axPH.setTitleCoordinate( ( iXTitleLocation == ABOVE ) ? dY1 + 1
	// : dY2 - 1 + dXAxisTitleThickness );
	// }
	// else if ( iv.iType == IntersectionValue.MIN )
	// {
	// // NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MAX'
	//
	// dY -= dAppliedXAxisPlotSpacing;
	// dY1 = dY;
	// dY2 = dY;
	// if ( bTicksBelow )
	// {
	// dY2 -= TICK_SIZE;
	// }
	// if ( iXLabelLocation == ABOVE )
	// {
	// dY1 += Math.max( ( bTicksAbove ? TICK_SIZE : 0 )
	// + dXAxisLabelsThickness, dAppliedXAxisPlotSpacing );
	// }
	// else if ( iXLabelLocation == BELOW )
	// {
	// dY2 -= dXAxisLabelsThickness;
	// dY1 += Math.max( bTicksAbove ? TICK_SIZE : 0,
	// dAppliedXAxisPlotSpacing );
	// }
	// if ( iXTitleLocation == ABOVE )
	// {
	// dY1 += dXAxisTitleThickness;
	// }
	// else if ( iXTitleLocation == BELOW )
	// {
	// dY2 -= dXAxisTitleThickness;
	// }
	//
	// // // ENSURE THAT WE DON'T GO BELOW THE LOWER PLOT BLOCK EDGE
	// // if ( dY2 > dBlockY + dBlockHeight )
	// // {
	// // final double dDelta = ( dY2 - ( dBlockY + dBlockHeight ) );
	// // dY2 = dBlockY + dBlockHeight;
	// // dY -= dDelta;
	// // dY1 -= dDelta;
	// // }
	// double dDeltaY1 = dY1 - dY;
	// double dDeltaY2 = dY - dY2;
	//
	// // COMPUTE THE X-AXIS BAND THICKNESS AND ADJUST Y2 FOR LABELS BELOW
	// dXAxisLabelsThickness = 0; // REUSE VARIABLE
	// if ( iXLabelLocation == ABOVE )
	// {
	// // X-AXIS BAND IS ((y+AxisPlotSpacing) -> y2)
	// dY1 = ( dY + dAppliedXAxisPlotSpacing );
	// dXAxisLabelsThickness = dY1 - dY2;
	// }
	// else if ( iXLabelLocation == BELOW )
	// {
	// // X-AXIS BAND IS (y1 -> y2)
	// dXAxisLabelsThickness = dY1 - dY2;
	// }
	//
	// // CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
	// // LOWER END
	// if ( dXAxisLabelsThickness > scY.getStartShift( ) )
	// {
	// // REDUCE scY's STARTPOINT TO FIT THE X-AXIS AT THE TOP
	// scY.setEndPoints( scY.getStart( ) + scY.getStartShift( ),
	// scY.getEnd( ) - scY.getEndShift( ) ); // RESTORE
	// double dStart = dY1 + scY.getStartShift( ), dEnd = scY.getEnd( );
	// scY.resetShifts( );
	//
	// // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
	// // IF OVERLAPS OCCUR
	// scY.setEndPoints( dStart, dEnd );
	// scY.computeTicks( ids,
	// laYAxisLabels,
	// iYLabelLocation,
	// VERTICAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( !scY.isStepFixed( ) )
	// {
	// final Object[] oaMinMax = scY.getMinMax( );
	// while ( !scY.checkFit( ids, laYAxisLabels, iYLabelLocation ) )
	// {
	// if ( !scY.zoomOut( ) )
	// {
	// break;
	// }
	// scY.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
	// int tickCount = scY.computeTicks( ids,
	// laYAxisLabels,
	// iYLabelLocation,
	// VERTICAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( scY.getUnit( ) != null
	// && asInteger( scY.getUnit( ) ) == Calendar.YEAR
	// && tickCount <= 3 )
	// {
	// break;
	// }
	// }
	// }
	// }
	//
	// // MOVE THE BAND DOWNWARDS BY INSETS.BOTTOM
	// dY -= insCA.getBottom( );
	// dY1 = dY + dDeltaY1;
	// dY2 = dY - dDeltaY2;
	//
	// // SET THE AXIS TITLE's BOX TOP CO-ORDINATE
	// axPH.setTitleCoordinate( ( iXTitleLocation == ABOVE ) ? dY1 + 1
	// : dY2 - 1 + dXAxisTitleThickness );
	// }
	// else
	// {
	// double dDeltaY1 = 0, dDeltaY2 = 0;
	// if ( iXLabelLocation == ABOVE )
	// {
	// dY1 += ( bTicksAbove ? TICK_SIZE : 0 ) + dXAxisLabelsThickness;
	// dY2 -= ( bTicksBelow ? TICK_SIZE : 0 );
	//
	// if ( iXTitleLocation == ABOVE )
	// {
	// dY1 += dXAxisTitleThickness;
	// }
	// else if ( iXTitleLocation == BELOW )
	// {
	// dY2 -= dXAxisTitleThickness;
	// }
	// dDeltaY1 = dY1 - dY;
	// dDeltaY2 = dY - dY2;
	//
	// // CHECK IF UPPER EDGE OF X-AXIS BAND GOES ABOVE PLOT UPPER EDGE
	// if ( dY1 > dBlockY + dBlockHeight )
	// {
	// final Object[] oaMinMax = scY.getMinMax( );
	// boolean bForceBreak = false;
	//
	// // A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE TOP EDGE
	// // OF THE X-AXIS LABELS WITH THE TOP EDGE OF THE PLOT AND/OR
	// // ENSURE THAT THE END POINT OF THE Y-AXIS SCALE IS SUITABLY
	// // POSITIONED
	//
	// do
	// {
	// // CANCEL OUT THE END LABEL SHIFT COMPUTATIONS FROM THE
	// // Y-AXIS
	// scY.setEndPoints( scY.getStart( ) + scY.getStartShift( ),
	// scY.getEnd( ) - scY.getEndShift( ) ); // RESTORE
	// scY.resetShifts( );
	//
	// // APPLY THE AXIS REDUCTION FORMULA W.R.T. Y-AXIS
	// // ENDPOINT
	// double[] da = scY.getEndPoints( );
	// double dT_RI = dBlockY - dY1; // THRESHOLD -
	// // REQUESTEDINTERSECTION
	// double dAMin_AMax = da[0] - da[1];
	// double dAMin_RI = da[0] - dY;
	// double dStart = da[0];
	// double dEnd = ( dT_RI / dAMin_RI ) * dAMin_AMax + da[1];
	// if ( dEnd < dBlockY )
	// {
	// dEnd = dBlockY;
	// bForceBreak = true; // ADJUST THE TOP EDGE OF THE
	// // Y-AXIS SCALE TO THE TOP EDGE
	// // OF THE PLOT BLOCK
	// }
	//
	// // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
	// // LABELS IF OVERLAPS OCCUR
	// scY.setEndPoints( dStart, dEnd );
	// scY.computeTicks( ids,
	// laYAxisLabels,
	// iYLabelLocation,
	// VERTICAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( !scY.isStepFixed( ) )
	// {
	// while ( !scY.checkFit( ids,
	// laYAxisLabels,
	// iYLabelLocation ) )
	// {
	// if ( !scY.zoomOut( ) )
	// {
	// bForceBreak = true;
	// break;
	// }
	// scY.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
	// int tickCount = scY.computeTicks( ids,
	// laYAxisLabels,
	// iYLabelLocation,
	// VERTICAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( scY.getUnit( ) != null
	// && asInteger( scY.getUnit( ) ) == Calendar.YEAR
	// && tickCount <= 3 )
	// {
	// bForceBreak = true;
	// break;
	// }
	// }
	// }
	//
	// dY = getLocation( scY, iv );
	// dY1 = dY + dDeltaY1; // RE-CALCULATE X-AXIS BAND
	// // UPPER
	// // EDGE
	// dY2 = dY - dDeltaY2; // REDUNDANT: RE-CALCULATE
	// // X-AXIS
	// // BAND LOWER EDGE
	// } while ( Math.abs( dY1 - ( dBlockY + dBlockHeight ) ) > 1
	// && !bForceBreak );
	// }
	// }
	// else if ( iXLabelLocation == BELOW )
	// {
	// dY1 += ( bTicksAbove ? TICK_SIZE : 0 );
	// dY2 -= ( bTicksBelow ? TICK_SIZE : 0 ) + dXAxisLabelsThickness;
	//
	// if ( iXTitleLocation == ABOVE )
	// {
	// dY1 += dXAxisTitleThickness;
	// }
	// else if ( iXTitleLocation == BELOW )
	// {
	// dY2 -= dXAxisTitleThickness;
	// }
	// dDeltaY1 = dY1 - dY;
	// dDeltaY2 = dY - dY2;
	//
	// // CHECK IF LOWER EDGE OF X-AXIS BAND GOES BELOW PLOT LOWER EDGE
	// if ( dY2 < dBlockY )
	// {
	// final Object[] oaMinMax = scY.getMinMax( );
	// boolean bForceBreak = false;
	//
	// // A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE TOP EDGE
	// // OF THE X-AXIS LABELS WITH THE TOP EDGE OF THE PLOT AND/OR
	// // ENSURE THAT THE END POINT OF THE Y-AXIS SCALE IS SUITABLY
	// // POSITIONED
	//
	// do
	// {
	// // CANCEL OUT THE END LABEL SHIFT COMPUTATIONS FROM THE
	// // Y-AXIS
	// scY.setEndPoints( scY.getStart( ) + scY.getStartShift( ),
	// scY.getEnd( ) - scY.getEndShift( ) ); // RESTORE
	// scY.resetShifts( );
	//
	// // APPLY THE AXIS REDUCTION FORMULA W.R.T. Y-AXIS
	// // ENDPOINT
	// double[] da = scY.getEndPoints( );
	// double dX2_X1 = dY2 - ( dBlockY + dBlockHeight ); // THRESHOLD
	// // -
	// // REQUESTEDINTERSECTION
	// double dAMin_AMax = da[0] - da[1];
	// double dX2_AMax = dY - da[1];
	// double dStart = da[0]
	// - ( dX2_X1 / dX2_AMax )
	// * dAMin_AMax;
	// double dEnd = da[1];
	//
	// if ( dStart > dBlockY + dBlockHeight )
	// {
	// dStart = dBlockY + dBlockHeight;
	// bForceBreak = true; // ADJUST THE TOP EDGE OF THE
	// // Y-AXIS SCALE TO THE TOP EDGE
	// // OF THE PLOT BLOCK
	// }
	//
	// // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
	// // LABELS IF OVERLAPS OCCUR
	// scY.setEndPoints( dStart, dEnd );
	// scY.computeTicks( ids,
	// laYAxisLabels,
	// iYLabelLocation,
	// VERTICAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( !scY.isStepFixed( ) )
	// {
	// while ( !scY.checkFit( ids,
	// laYAxisLabels,
	// iYLabelLocation ) )
	// {
	// if ( !scY.zoomOut( ) )
	// {
	// bForceBreak = true;
	// break;
	// }
	// scY.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
	// int tickCount = scY.computeTicks( ids,
	// laYAxisLabels,
	// iYLabelLocation,
	// VERTICAL,
	// dStart,
	// dEnd,
	// true,
	// aax );
	// if ( scY.getUnit( ) != null
	// && asInteger( scY.getUnit( ) ) == Calendar.YEAR
	// && tickCount <= 3 )
	// {
	// bForceBreak = true;
	// break;
	// }
	// }
	// }
	//
	// dY = getLocation( scY, iv );
	// dY2 = dY - dDeltaY2; // RE-CALCULATE X-AXIS BAND
	// // LOWER
	// // EDGE
	// dY1 = dY + dDeltaY1; // RE-CALCULATE X-AXIS BAND
	// // LOWER
	// // EDGE
	// } while ( Math.abs( dY2 - dBlockY ) > 1 && !bForceBreak );
	// }
	// }
	//
	// axPH.setTitleCoordinate( ( iXTitleLocation == ABOVE ) ? dY1 + 1
	// : dY2 - 1 + dXAxisTitleThickness );
	// }
	//
	// return dY;
	// }

	private final Object getMinMax( Axis ax, int iType ) throws ChartException,
			IllegalArgumentException
	{
		final Series[] sea = ax.getRuntimeSeries( );
		final int iSeriesCount = sea.length;
		DataSet ds;

		Object oV1, oV2, oMin = null, oMax = null;

		PluginSettings ps = PluginSettings.instance( );
		IDataSetProcessor iDSP = null;

		for ( int i = 0; i < iSeriesCount; i++ )
		{
			if ( sea[i].isStacked( ) )
			{
				// 3D chart can't be stacked.
				throw new IllegalArgumentException( MessageFormat.format( Messages.getResourceBundle( rtc.getULocale( ) )
						.getString( "exception.unstackable.is.stacked" ), //$NON-NLS-1$
						new Object[]{
							sea[i]
						} ) );
			}

			iDSP = ps.getDataSetProcessor( sea[i].getClass( ) );
			ds = sea[i].getDataSet( );

			oV1 = iDSP.getMinimum( ds );
			oV2 = iDSP.getMaximum( ds );

			if ( ( iType & NUMERICAL ) == NUMERICAL )
			{
				if ( oV1 != null ) // SETUP THE MINIMUM VALUE FOR ALL DATASETS
				{
					if ( oMin == null )
					{
						oMin = oV1;
					}
					else
					{
						final double dV1 = asDouble( oV1 ).doubleValue( );
						if ( Math.min( asDouble( oMin ).doubleValue( ), dV1 ) == dV1 )
						{
							oMin = oV1;
						}
					}
				}

				if ( oV2 != null ) // SETUP THE MAXIMUM VALUE FOR ALL DATASETS
				{
					if ( oMax == null )
					{
						oMax = oV2;
					}
					else
					{
						final double dV2 = asDouble( oV2 ).doubleValue( );
						if ( Math.max( asDouble( oMax ).doubleValue( ), dV2 ) == dV2 )
						{
							oMax = oV2;
						}
					}
				}
			}
			else if ( ( iType & DATE_TIME ) == DATE_TIME )
			{
				if ( oV1 != null ) // SETUP THE MINIMUM VALUE FOR ALL DATASETS
				{
					if ( oMin == null )
					{
						oMin = oV1;
					}
					else
					{
						final CDateTime cdtV1 = asDateTime( oV1 );
						final CDateTime cdtMin = asDateTime( oMin );
						if ( cdtV1.before( cdtMin ) )
						{
							oMin = cdtV1;
						}
					}
				}

				if ( oV2 != null ) // SETUP THE MAXIMUM VALUE FOR ALL DATASETS
				{
					if ( oMax == null )
					{
						oMax = oV2;
					}
					else
					{
						final CDateTime cdtV2 = asDateTime( oV2 );
						final CDateTime cdtMax = asDateTime( oMax );
						if ( cdtV2.after( cdtMax ) )
						{
							oMax = cdtV2;
						}
					}
				}
			}
		}

		// ONLY NUMERIC VALUES ARE SUPPORTED IN STACKED ELEMENT COMPUTATIONS
		if ( ax.isPercent( ) )
		{
			// 3D Chart axis doesn't support Percent.
			throw new IllegalArgumentException( MessageFormat.format( Messages.getResourceBundle( rtc.getULocale( ) )
					.getString( "exception.no.stack.percent.3D.chart" ), //$NON-NLS-1$
					new Object[]{
						ax
					} ) );
		}

		// IF NO DATASET WAS FOUND BECAUSE NO SERIES WERE ATTACHED TO AXES,
		// SIMULATE MIN/MAX VALUES
		if ( oMin == null && oMax == null )
		{
			if ( iType == DATE_TIME )
			{
				oMin = new CDateTime( 1, 1, 2005 );
				oMax = new CDateTime( 1, 1, 2006 );
			}
			else if ( ( iType & NUMERICAL ) == NUMERICAL )
			{
				if ( ( iType & PERCENT ) == PERCENT )
				{
					oMin = new Double( 0 );
					oMax = new Double( 99.99 );
				}
				else if ( ( iType & LOGARITHMIC ) == LOGARITHMIC )
				{
					oMin = new Double( 1 );
					oMax = new Double( 999 );
				}
				else
				{
					oMin = new Double( -0.9 );
					oMax = new Double( 0.9 );
				}
			}
		}

		if ( iType == DATE_TIME )
		{
			try
			{
				return new Calendar[]{
						asDateTime( oMin ), asDateTime( oMax )
				};
			}
			catch ( ClassCastException ex )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_SET,
						"exception.numerical.data.datetime.axis", //$NON-NLS-1$ 
						Messages.getResourceBundle( rtc.getULocale( ) ) );

			}
		}
		else if ( ( iType & NUMERICAL ) == NUMERICAL )
		{
			try
			{
				return new double[]{
						asDouble( oMin ).doubleValue( ),
						asDouble( oMax ).doubleValue( )
				};
			}
			catch ( ClassCastException ex )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_SET,
						"exception.datetime.data.numerical.axis", //$NON-NLS-1$ 
						Messages.getResourceBundle( rtc.getULocale( ) ) );
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.computation.withaxes.PlotWithAxes#getSeriesRenderingHints(org.eclipse.birt.chart.model.data.SeriesDefinition,
	 *      org.eclipse.birt.chart.model.component.Series)
	 */
	public ISeriesRenderingHints getSeriesRenderingHints(
			SeriesDefinition sdOrthogonal, Series seOrthogonal )
			throws ChartException, IllegalArgumentException
	{
		if ( seOrthogonal == null
				|| seOrthogonal.getClass( ) == SeriesImpl.class ) // EMPTY
		// PLOT
		// RENDERING
		// TECHNIQUE
		{
			return null;
		}
		OneAxis oaxOrthogonal = findOrthogonalAxis( seOrthogonal );
		if ( oaxOrthogonal == null )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.NOT_FOUND,
					"exception.axis.series.link.broken", //$NON-NLS-1$
					new Object[]{
						seOrthogonal
					},
					Messages.getResourceBundle( rtc.getULocale( ) ) );
		}
		final OneAxis oaxBase = aax.getPrimaryBase( );
		final SeriesDefinition sdBase = (SeriesDefinition) oaxBase.getModelAxis( )
				.getSeriesDefinitions( )
				.get( 0 );

		final OneAxis oaxAncillaryBase = aax.getAncillaryBase( );
		final AutoScale scBase = oaxBase.getScale( );
		final AutoScale scOrthogonal = oaxOrthogonal.getScale( );
		final AutoScale scAncillary = oaxAncillaryBase.getScale( );
		final int iXTickCount = scBase.getTickCount( );
		final int iZTickCount = scAncillary.getTickCount( );
		final double dXUnitSize = scBase.getUnitSize( );
		final double dZUnitSize = scAncillary.getUnitSize( );
		final boolean bZCategoryTextStyle = scAncillary.isCategoryScale( )
				|| scAncillary.getType( ) == IConstants.TEXT;

		double[] daXTickCoordinates = scBase.getTickCordinates( );
		double[] daZTickCoordinates = scAncillary.getTickCordinates( );
		Object oDataBase = null;
		DataSetIterator dsiDataBase = scBase.getData( );
		Object oDataOrthogonal;
		DataSetIterator dsiDataOrthogonal = getTypedDataSet( seOrthogonal,
				oaxOrthogonal.getScale( ).getType( ) );
		DataSetIterator dsiDataAncillary = scAncillary.getData( );
		double dOrthogonalZero = 0;
		if ( ( scOrthogonal.getType( ) & NUMERICAL ) == NUMERICAL )
		{
			dOrthogonalZero = getLocation( scOrthogonal, 0 );
		}
		else
		{
			dOrthogonalZero = scOrthogonal.getStart( );
		}
		double dAncillaryZero = 0;
		if ( ( scAncillary.getType( ) & NUMERICAL ) == NUMERICAL )
		{
			dAncillaryZero = getLocation( scAncillary, 0 );
		}
		else
		{
			dAncillaryZero = scAncillary.getStart( );
		}

		double dX = 0, dY = 0, dZ = 0, dXLength = 0, dZLength = 0;
		Location3D lo3d;

		final int iBaseCount = dsiDataBase.size( );
		final int iOrthogonalCount = dsiDataOrthogonal.size( );
		DataPointHints[] dpa = null;

		int seriesIndex = 0;
		Series[] rss = oaxOrthogonal.getModelAxis( ).getRuntimeSeries( );
		for ( int i = 0; i < rss.length; i++ )
		{
			if ( rss[i] == seOrthogonal )
			{
				seriesIndex = i;
				break;
			}
		}

		Object oDataAncillary = null;
		for ( int i = 0; i < seriesIndex; i++ )
		{
			if ( dsiDataAncillary.hasNext( ) )
			{
				dsiDataAncillary.next( );
			}
		}
		if ( dsiDataAncillary.hasNext( ) )
		{
			oDataAncillary = dsiDataAncillary.next( );
		}

		if ( iBaseCount != iOrthogonalCount ) // DO NOT COMPUTE DATA POINT
		// HINTS
		// FOR OUT-OF-SYNC DATA
		{
			logger.log( ILogger.INFORMATION,
					Messages.getString( "exception.base.orthogonal.inconsistent.count", //$NON-NLS-1$
							new Object[]{
									new Integer( iBaseCount ),
									new Integer( iOrthogonalCount )
							},
							rtc.getULocale( ) ) );
		}
		else
		{
			dpa = new DataPointHints[iBaseCount];
			final boolean bScatter = ( oaxBase.getScale( ).getType( ) != IConstants.TEXT && !oaxBase.isCategoryScale( ) );

			FormatSpecifier fsSDAncillary = null;

			if ( oaxAncillaryBase.getModelAxis( )
					.getSeriesDefinitions( )
					.size( ) > 0 )
			{
				fsSDAncillary = ( (SeriesDefinition) oaxAncillaryBase.getModelAxis( )
						.getSeriesDefinitions( )
						.get( 0 ) ).getFormatSpecifier( );
			}

			// OPTIMIZED PRE-FETCH FORMAT SPECIFIERS FOR ALL DATA POINTS
			final DataPoint dp = seOrthogonal.getDataPoint( );
			final EList el = dp.getComponents( );
			DataPointComponent dpc;
			DataPointComponentType dpct;
			FormatSpecifier fsBase = null, fsOrthogonal = null, fsSeries = null, fsPercentile = null;
			for ( int i = 0; i < el.size( ); i++ )
			{
				dpc = (DataPointComponent) el.get( i );
				dpct = dpc.getType( );
				if ( dpct == DataPointComponentType.BASE_VALUE_LITERAL )
				{
					fsBase = dpc.getFormatSpecifier( );
					if ( fsBase == null ) // BACKUP
					{
						fsBase = sdBase.getFormatSpecifier( );
					}
				}
				else if ( dpct == DataPointComponentType.ORTHOGONAL_VALUE_LITERAL )
				{
					fsOrthogonal = dpc.getFormatSpecifier( );
					if ( fsOrthogonal == null
							&& seOrthogonal.eContainer( ) instanceof SeriesDefinition )
					{
						fsOrthogonal = ( (SeriesDefinition) seOrthogonal.eContainer( ) ).getFormatSpecifier( );
					}
				}
				else if ( dpct == DataPointComponentType.SERIES_VALUE_LITERAL )
				{
					fsSeries = dpc.getFormatSpecifier( );
					if ( fsSeries == null )
					{
						fsSeries = fsSDAncillary;
					}
				}
				else if ( dpct == DataPointComponentType.PERCENTILE_ORTHOGONAL_VALUE_LITERAL )
				{
					fsPercentile = dpc.getFormatSpecifier( );
				}
			}

			dsiDataBase.reset( );
			dsiDataOrthogonal.reset( );

			UserDataSetHints udsh = new UserDataSetHints( seOrthogonal.getDataSets( ) );
			udsh.reset( );

			double total = 0;
			boolean isZeroValue = true;

			// get total orthogonal value.
			for ( int i = 0; i < iOrthogonalCount; i++ )
			{
				Object v = dsiDataOrthogonal.next( );

				if ( v instanceof Number )
				{
					if ( ( (Number) v ).doubleValue( ) != 0.0 )
					{
						isZeroValue = false;
					}
					total += ( (Number) v ).doubleValue( );
				}
				else if ( v instanceof NumberDataElement )
				{
					if ( ( (NumberDataElement) v ).getValue( ) != 0.0 )
					{
						isZeroValue = false;
					}
					total += ( (NumberDataElement) v ).getValue( );
				}
			}

			dsiDataOrthogonal.reset( );

			for ( int i = 0; i < iBaseCount; i++ )
			{
				oDataBase = dsiDataBase.next( );
				oDataOrthogonal = dsiDataOrthogonal.next( );

				if ( !bScatter )
				{
					if ( aax.areAxesSwapped( ) )
					{
						dY = daXTickCoordinates[0] + dXUnitSize * i;

						if ( bZCategoryTextStyle )
						{
							dZ = daZTickCoordinates[0]
									+ dZUnitSize
									* seriesIndex;
						}
						else
						{
							try
							{
								dZ = getLocation( scAncillary, oDataAncillary );
							}
							catch ( IllegalArgumentException e )
							{
								dZ = dAncillaryZero;
							}
							catch ( ChartException e )
							{
								dZ = dAncillaryZero;
							}
						}

						try
						{
							dX = getLocation( scOrthogonal, oDataOrthogonal );
						}
						catch ( IllegalArgumentException nvex )
						{
							// dX = dOrthogonalZero;
							dX = Double.NaN;
						}
						catch ( ChartException dfex )
						{
							dX = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
						}
					}
					else
					{
						dX = daXTickCoordinates[0] + dXUnitSize * i;

						if ( bZCategoryTextStyle )
						{
							dZ = daZTickCoordinates[0]
									+ dZUnitSize
									* seriesIndex;
						}
						else
						{
							try
							{
								dZ = getLocation( scAncillary, oDataAncillary );
							}
							catch ( IllegalArgumentException e )
							{
								dZ = dAncillaryZero;
							}
							catch ( ChartException e )
							{
								dZ = dAncillaryZero;
							}
						}

						try
						{
							dY = getLocation( scOrthogonal, oDataOrthogonal );
						}
						catch ( IllegalArgumentException nvex )
						{
							// dY = dOrthogonalZero;
							dX = Double.NaN;
						}
						catch ( ChartException dfex )
						{
							dY = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
						}
					}
				}
				else
				{
					// Do not support scatter for 3D chart.
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.COMPUTATION,
							"exception.scatter.3D.not.supported", //$NON-NLS-1$ 
							Messages.getResourceBundle( rtc.getULocale( ) ) );
				}

				lo3d = Location3DImpl.create( dX, dY, dZ );
				dXLength = ( i < iXTickCount - 1 ) ? daXTickCoordinates[i + 1]
						- daXTickCoordinates[i] : 0;
				dZLength = ( seriesIndex < iZTickCount - 1 ) ? daZTickCoordinates[seriesIndex + 1]
						- daZTickCoordinates[seriesIndex]
						: 0;

				Object percentileValue = null;

				if ( total != 0 )
				{
					if ( oDataOrthogonal instanceof Number )
					{
						percentileValue = new Double( ( (Number) oDataOrthogonal ).doubleValue( )
								/ total );
					}
					else if ( oDataOrthogonal instanceof NumberDataElement )
					{
						percentileValue = new Double( ( (NumberDataElement) oDataOrthogonal ).getValue( )
								/ total );
					}
				}
				else if ( isZeroValue == true )
				{
					percentileValue = new Double( 1d / iOrthogonalCount );
				}

				dpa[i] = new DataPointHints( oDataBase,
						oDataOrthogonal,
						oDataAncillary,
						percentileValue,
						seOrthogonal.getDataPoint( ),
						fsBase,
						fsOrthogonal,
						fsSeries,
						fsPercentile,
						i,
						lo3d,
						new double[]{
								dXLength, dZLength
						},
						rtc );

				udsh.next( dpa[i] );
			}
		}

		return new SeriesRenderingHints3D( this,
				oaxBase.getAxisCoordinate( ),
				oaxAncillaryBase.getAxisCoordinate( ),
				scOrthogonal.getStart( ),
				dOrthogonalZero,
				scOrthogonal.getEnd( ) - scOrthogonal.getStart( ),
				daXTickCoordinates,
				daZTickCoordinates,
				dpa,
				scBase,
				scOrthogonal,
				scAncillary,
				dsiDataBase,
				dsiDataOrthogonal,
				dsiDataAncillary );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.computation.withaxes.PlotWithAxes#buildAxes()
	 */
	void buildAxes( ) throws IllegalArgumentException, ChartException
	{
		final Axis[] axa = cwa.getPrimaryBaseAxes( );
		final Axis axPrimaryBase = axa[0]; // NOTE: FOR REL 1 AXIS RENDERS, WE
		// SUPPORT A SINGLE PRIMARY BASE AXIS
		// ONLY
		if ( !axPrimaryBase.isSetOrientation( ) )
		{
			axPrimaryBase.setOrientation( Orientation.HORIZONTAL_LITERAL );
		}
		validateAxis( axPrimaryBase );

		final Axis axPrimaryOrthogonal = cwa.getPrimaryOrthogonalAxis( axPrimaryBase );
		if ( !axPrimaryOrthogonal.isSetOrientation( ) )
		{
			axPrimaryOrthogonal.setOrientation( Orientation.VERTICAL_LITERAL );
		}
		validateAxis( axPrimaryOrthogonal );

		final Axis axAncillaryBase = cwa.getAncillaryBaseAxis( axPrimaryBase );
		// if ( !axAncillaryBase.isSetOrientation( ) )
		{
			axAncillaryBase.setOrientation( Orientation.HORIZONTAL_LITERAL );
		}
		validateAxis( axAncillaryBase );

		if ( axPrimaryBase.getAssociatedAxes( ).size( ) > 1 )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.COMPUTATION,
					"exception.multi.orthogonal.3D.not.supported", //$NON-NLS-1$ 
					Messages.getResourceBundle( rtc.getULocale( ) ) );
		}

		aax = new AllAxes( cwa.getPlot( )
				.getClientArea( )
				.getInsets( )
				.scaledInstance( dPointToPixel ) ); // CONVERSION
		insCA = aax.getInsets( );

		aax.swapAxes( cwa.isTransposed( ) );

		// SETUP THE PRIMARY BASE-AXIS PROPERTIES AND ITS SCALE
		final OneAxis oaxPrimaryBase = new OneAxis( axPrimaryBase,
				IConstants.BASE_AXIS );
		oaxPrimaryBase.set( getOrientation( IConstants.BASE ),
				transposeLabelPosition( IConstants.BASE,
						getLabelPosition( axPrimaryBase.getLabelPosition( ) ) ),
				transposeLabelPosition( IConstants.BASE,
						getLabelPosition( axPrimaryBase.getTitlePosition( ) ) ),
				axPrimaryBase.isSetCategoryAxis( )
						&& axPrimaryBase.isCategoryAxis( ) );
		oaxPrimaryBase.setGridProperties( axPrimaryBase.getMajorGrid( )
				.getLineAttributes( ),
				axPrimaryBase.getMinorGrid( ).getLineAttributes( ),
				axPrimaryBase.getMajorGrid( ).getTickAttributes( ),
				axPrimaryBase.getMinorGrid( ).getTickAttributes( ),
				transposeTickStyle( IConstants.BASE,
						getTickStyle( axPrimaryBase, MAJOR ) ),
				transposeTickStyle( IConstants.BASE,
						getTickStyle( axPrimaryBase, MINOR ) ),
				axPrimaryBase.getScale( ).getMinorGridsPerUnit( ) );

		oaxPrimaryBase.set( axPrimaryBase.getLabel( ), axPrimaryBase.getTitle( ) );
		oaxPrimaryBase.set( getIntersection( axPrimaryBase ) );
		oaxPrimaryBase.set( axPrimaryBase.getLineAttributes( ) );
		aax.definePrimary( oaxPrimaryBase ); // ADD TO AXIS SET

		// SETUP THE PRIMARY ORTHOGONAL-AXIS PROPERTIES AND ITS SCALE
		final OneAxis oaxPrimaryOrthogonal = new OneAxis( axPrimaryOrthogonal,
				IConstants.ORTHOGONAL_AXIS );
		oaxPrimaryOrthogonal.set( getOrientation( IConstants.ORTHOGONAL ),
				transposeLabelPosition( IConstants.ORTHOGONAL,
						getLabelPosition( axPrimaryOrthogonal.getLabelPosition( ) ) ),
				transposeLabelPosition( IConstants.ORTHOGONAL,
						getLabelPosition( axPrimaryOrthogonal.getTitlePosition( ) ) ),
				axPrimaryOrthogonal.isSetCategoryAxis( )
						&& axPrimaryOrthogonal.isCategoryAxis( ) );
		oaxPrimaryOrthogonal.setGridProperties( axPrimaryOrthogonal.getMajorGrid( )
				.getLineAttributes( ),
				axPrimaryOrthogonal.getMinorGrid( ).getLineAttributes( ),
				axPrimaryOrthogonal.getMajorGrid( ).getTickAttributes( ),
				axPrimaryOrthogonal.getMinorGrid( ).getTickAttributes( ),
				transposeTickStyle( IConstants.ORTHOGONAL,
						getTickStyle( axPrimaryOrthogonal, MAJOR ) ),
				transposeTickStyle( IConstants.ORTHOGONAL,
						getTickStyle( axPrimaryOrthogonal, MINOR ) ),
				axPrimaryOrthogonal.getScale( ).getMinorGridsPerUnit( ) );

		oaxPrimaryOrthogonal.set( axPrimaryOrthogonal.getLabel( ),
				axPrimaryOrthogonal.getTitle( ) );
		oaxPrimaryOrthogonal.set( getIntersection( axPrimaryOrthogonal ) );
		oaxPrimaryOrthogonal.set( axPrimaryOrthogonal.getLineAttributes( ) );
		aax.definePrimary( oaxPrimaryOrthogonal ); // ADD TO AXIS SET

		// SETUP THE ANCILLARY BASE-AXIS PROPERTIES AND ITS SCALE
		final OneAxis oaxAncillaryBase = new OneAxis( axAncillaryBase,
				IConstants.ANCILLARY_AXIS );
		oaxAncillaryBase.set( IConstants.HORIZONTAL,
				getLabelPosition( axAncillaryBase.getLabelPosition( ) ),
				getLabelPosition( axAncillaryBase.getTitlePosition( ) ),
				axAncillaryBase.isSetCategoryAxis( )
						&& axAncillaryBase.isCategoryAxis( ) );
		oaxAncillaryBase.setGridProperties( axAncillaryBase.getMajorGrid( )
				.getLineAttributes( ),
				axAncillaryBase.getMinorGrid( ).getLineAttributes( ),
				axAncillaryBase.getMajorGrid( ).getTickAttributes( ),
				axAncillaryBase.getMinorGrid( ).getTickAttributes( ),
				getTickStyle( axAncillaryBase, MAJOR ),
				getTickStyle( axAncillaryBase, MINOR ),
				axAncillaryBase.getScale( ).getMinorGridsPerUnit( ) );
		oaxAncillaryBase.set( axAncillaryBase.getLabel( ),
				axAncillaryBase.getTitle( ) ); // ASSOCIATE FONT, ETC.

		oaxAncillaryBase.set( getIntersection( axAncillaryBase ) );
		oaxAncillaryBase.set( axAncillaryBase.getLineAttributes( ) );
		aax.defineAncillaryBase( oaxAncillaryBase ); // ADD TO AXIS SET
	}
}
