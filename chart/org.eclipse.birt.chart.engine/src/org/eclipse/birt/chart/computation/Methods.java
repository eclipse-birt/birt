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

package org.eclipse.birt.chart.computation;

import java.text.MessageFormat;
import java.util.Date;

import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.IntersectionValue;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;

/**
 * Methods
 */
public class Methods implements IConstants
{

	/**
	 * Converts given object to a DateTime object.
	 * 
	 * @param o
	 * @return
	 */
	public static final CDateTime asDateTime( Object o )
	{
		if ( o == null )
		{
			return null;
		}
		else if ( o instanceof DateTimeDataElement )
		{
			return ( (DateTimeDataElement) o ).getValueAsCDateTime( );
		}
		else if ( o instanceof Calendar )
		{
			return new CDateTime( (Calendar) o );
		}
		else if ( o instanceof Date )
		{
			return new CDateTime( (Date) o );
		}
		return (CDateTime) o;
	}

	/**
	 * Converts the given object to a Double object.
	 * 
	 * @param o
	 * @return
	 */
	public static final Double asDouble( Object o )
	{
		if ( o == null )
		{
			return null;
		}
		else if ( o instanceof NumberDataElement )
		{
			return new Double( ( (NumberDataElement) o ).getValue( ) );
		}
		else if ( o instanceof Double )
		{
			return (Double) o;
		}
		return new Double( ( (Number) o ).doubleValue( ) );
	}

	/**
	 * Converts the given object to an Integer object.
	 * 
	 * @param o
	 * @return
	 */
	public static final int asInteger( Object o )
	{
		return ( (Number) o ).intValue( );
	}

	/**
	 * 
	 * @param sc
	 * @param dValue
	 * 
	 * @return
	 */
	public static final double getLocation( AutoScale sc, IntersectionValue iv )
	{
		double[] da = sc.getTickCordinates( );
		if ( iv.getType( ) == IntersectionValue.MIN )
		{
			return da[0];
		}
		else if ( iv.getType( ) == IntersectionValue.MAX )
		{
			return da[da.length - 1];
		}

		if ( ( sc.getType( ) & TEXT ) == TEXT || sc.isCategoryScale( ) )
		{
			double dValue = iv.getValueAsDouble( sc );
			return da[0] + ( da[1] - da[0] ) * dValue;
		}
		else if ( ( sc.getType( ) & DATE_TIME ) == DATE_TIME )
		{
			CDateTime cdtValue = asDateTime( iv.getValue( ) );
			CDateTime cdt = asDateTime( sc.getMinimum( ) ), cdtPrev = null;
			int iUnit = asInteger( sc.getUnit( ) );
			int iStep = asInteger( sc.getStep( ) );

			for ( int i = 0; i < da.length; i++ )
			{
				if ( cdt.after( cdtValue ) )
				{
					if ( cdtPrev == null )
					{
						return da[i];
					}
					/*
					 * SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy
					 * HH:mm:ss"); String sMin = sdf.format(cdtPrev.getTime());
					 * String sMax = sdf.format(cdt.getTime()); String sVal =
					 * sdf.format(cdtValue.getTime());
					 */

					long l1 = cdtPrev.getTimeInMillis( );
					long l2 = cdt.getTimeInMillis( );
					long l = cdtValue.getTimeInMillis( );
					double dUnitSize = da[i] - da[i - 1];

					double dOffset = ( dUnitSize / ( l2 - l1 ) ) * ( l - l1 );
					return da[i - 1] + dOffset;

				}
				cdtPrev = cdt;
				cdt = cdt.forward( iUnit, iStep );
			}
			return da[da.length - 1];
		}
		else if ( ( sc.getType( ) & LOGARITHMIC ) == LOGARITHMIC )
		{
			double dValue = iv.getValueAsDouble( sc );
			if ( dValue == 0 ) // CANNOT GO TO '0'
			{
				return sc.getStart( );
			}
			if ( dValue < 0 )
			{
				return sc.getStart( );
			}
			double dMinimumLog = Math.log( asDouble( sc.getMinimum( ) ).doubleValue( ) )
					/ LOG_10;
			double dStepLog = Math.log( asDouble( sc.getStep( ) ).doubleValue( ) )
					/ LOG_10;
			double dValueLog = Math.log( dValue ) / LOG_10;
			return da[0]
					- ( ( ( dValueLog - dMinimumLog ) / dStepLog ) * ( da[0] - da[1] ) );
		}
		else
		{
			double dValue = iv.getValueAsDouble( sc );
			double dMinimum = asDouble( sc.getMinimum( ) ).doubleValue( );
			double dMaximum = asDouble( sc.getMaximum( ) ).doubleValue( );
			double[] ea = sc.getEndPoints( );

			if ( dMaximum == dMinimum )
			{
				return ea[0];
			}
			{
				return ea[0]
						- ( ( ( dValue - dMinimum ) / ( dMaximum - dMinimum ) ) * ( ea[0] - ea[1] ) );
			}
		}
	}

	/**
	 * @param sc
	 * @param oValue
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	public static final double getLocation( AutoScale sc, Object oValue )
			throws ChartException, IllegalArgumentException
	{
		if ( oValue == null )
		{
			throw new IllegalArgumentException( MessageFormat.format( Messages.getResourceBundle( sc.getRunTimeContext( )
					.getULocale( ) )
					.getString( "exception.scale.null.location" ), //$NON-NLS-1$
					new Object[]{
						sc
					} )

			);
		}
		if ( oValue instanceof Double )
		{
			return getLocation( sc, ( (Double) oValue ).doubleValue( ) );
		}
		else if ( oValue instanceof Calendar )
		{
			return getDateLocation( sc, new CDateTime( (Calendar) oValue ) );
		}
		else if ( oValue instanceof NumberDataElement )
		{
			return getLocation( sc, ( (NumberDataElement) oValue ).getValue( ) );
		}
		else if ( oValue instanceof DateTimeDataElement )
		{
			return getDateLocation( sc,
					( (DateTimeDataElement) oValue ).getValueAsCDateTime( ) );
		}
		/*
		 * DefaultLoggerImpl.instance().log(ILogger.WARNING, "Unexpected data
		 * type {0}[value={1}] specified" + oValue.getClass().getName() + oValue );
		 */// i18n_CONCATENATIONS_REMOVED
		return sc.getStart( ); // RETURNS THE START EDGE OF THE SCALE
	}

	/**
	 * @param sc
	 * @param dValue
	 * @return
	 */
	public static final double getNormalizedLocation( AutoScale sc,
			double dValue )
	{
		return getLocation( sc, dValue ) - sc.getStart( );
	}

	/**
	 * @param sc
	 * @param oValue
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	public static final double getNormalizedLocation( AutoScale sc,
			Object oValue ) throws ChartException, IllegalArgumentException
	{
		return getLocation( sc, oValue ) - sc.getStart( );
	}

	/**
	 * 
	 * @param sc
	 * @param dValue
	 * 
	 * @return
	 */
	public static final double getLocation( AutoScale sc, double dValue )
			throws IllegalArgumentException
	{
		if ( ( sc.getType( ) & IConstants.TEXT ) == IConstants.TEXT
				|| sc.isCategoryScale( ) )
		{
			double[] da = sc.getTickCordinates( );
			return da[0] + ( da[1] - da[0] ) * dValue;
		}
		else if ( ( sc.getType( ) & IConstants.LINEAR ) == IConstants.LINEAR )
		{
			double dMinimum = asDouble( sc.getMinimum( ) ).doubleValue( );
			double dMaximum = asDouble( sc.getMaximum( ) ).doubleValue( );
			double[] da = sc.getEndPoints( );

			if ( dMaximum == dMinimum )
			{
				return da[0];
			}
			else
			{
				return da[0]
						- ( ( ( dValue - dMinimum ) / ( dMaximum - dMinimum ) ) * ( da[0] - da[1] ) );
			}
		}
		else if ( ( sc.getType( ) & IConstants.LOGARITHMIC ) == IConstants.LOGARITHMIC )
		{
			if ( dValue == 0 ) // CANNOT GO TO '0'
			{
				return sc.getStart( );
			}
			if ( dValue < 0 )
			{
				throw new IllegalArgumentException( MessageFormat.format( Messages.getResourceBundle( sc.getRunTimeContext( )
						.getULocale( ) )
						.getString( "exception.zero.negative.logarithmic.scale" ), //$NON-NLS-1$
						new Object[]{
							sc
						} ) );
			}
			double dMinimumLog = Math.log( asDouble( sc.getMinimum( ) ).doubleValue( ) )
					/ LOG_10;
			double dStepLog = Math.log( asDouble( sc.getStep( ) ).doubleValue( ) )
					/ LOG_10;
			double dValueLog = Math.log( dValue ) / LOG_10;
			double[] da = sc.getTickCordinates( );
			return da[0]
					- ( ( ( dValueLog - dMinimumLog ) / dStepLog ) * ( da[0] - da[1] ) );
		}
		return 0;
	}

	/**
	 * 
	 * @param sc
	 * @param cdt
	 * @return
	 */
	static final double getDateLocation( AutoScale sc, CDateTime cdtValue )
	{
		double[] da = sc.getTickCordinates( );
		CDateTime cdt = asDateTime( sc.getMinimum( ) ), cdtPrev = null;
		int iUnit = asInteger( sc.getUnit( ) );
		int iStep = asInteger( sc.getStep( ) );

		for ( int i = 0; i < da.length; i++ )
		{
			if ( cdt.after( cdtValue ) )
			{
				if ( cdtPrev == null )
				{
					return da[i];
				}
				/*
				 * SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy
				 * HH:mm:ss"); String sMin = sdf.format(cdtPrev.getTime());
				 * String sMax = sdf.format(cdt.getTime()); String sVal =
				 * sdf.format(cdtValue.getTime());
				 */

				long l1 = cdtPrev.getTimeInMillis( );
				long l2 = cdt.getTimeInMillis( );
				long l = cdtValue.getTimeInMillis( );
				double dUnitSize = da[i] - da[i - 1];

				double dOffset = ( dUnitSize / ( l2 - l1 ) ) * ( l - l1 );
				return da[i - 1] + dOffset;

			}
			cdtPrev = cdt;
			cdt = cdt.forward( iUnit, iStep );
		}
		return da[da.length - 1];
	}

	/**
	 * 
	 * @param g2d
	 * @param fm
	 * @param sText
	 * @param dAngleInDegrees
	 * @return
	 */
	protected final double computeWidth( IDisplayServer xs, Label la )
	{
		final FontDefinition fd = la.getCaption( ).getFont( );
		final double dAngleInRadians = ( ( -fd.getRotation( ) * Math.PI ) / 180.0 );
		final double dSineTheta = Math.abs( Math.sin( dAngleInRadians ) );
		final double dCosTheta = Math.abs( Math.cos( dAngleInRadians ) );
		final ITextMetrics itm = xs.getTextMetrics( la );
		double dW = itm.getFullWidth( )
				* dCosTheta
				+ itm.getFullHeight( )
				* dSineTheta;
		itm.dispose( );
		return dW;
	}

	/**
	 * 
	 * @param g2d
	 * @param fm
	 * @param sText
	 * @param iAngleInDegrees
	 * @return
	 */
	protected final double computeHeight( IDisplayServer xs, Label la )
	{
		final FontDefinition fd = la.getCaption( ).getFont( );
		final double dAngleInRadians = ( ( -fd.getRotation( ) * Math.PI ) / 180.0 );
		final double dSineTheta = Math.abs( Math.sin( dAngleInRadians ) );
		final double dCosTheta = Math.abs( Math.cos( dAngleInRadians ) );
		final ITextMetrics itm = xs.getTextMetrics( la );
		double dH = itm.getFullWidth( )
				* dSineTheta
				+ itm.getFullHeight( )
				* dCosTheta;
		itm.dispose( );
		return dH;
	}

	/**
	 * 
	 * @param iLabelLocation
	 * @param g2d
	 * @param fm
	 * @param sText
	 * @param dAngleInDegrees
	 * @param dX
	 * @param dY
	 * @return
	 */
	public static final RotatedRectangle computePolygon( IDisplayServer xs,
			int iLabelLocation, Label la, double dX, double dY )
			throws IllegalArgumentException
	{
		double dAngleInDegrees = la.getCaption( ).getFont( ).getRotation( );
		final double dAngleInRadians = ( ( -dAngleInDegrees * Math.PI ) / 180.0 );
		final double dSineTheta = Math.abs( Math.sin( dAngleInRadians ) );
		final double dCosTheta = Math.abs( Math.cos( dAngleInRadians ) );

		final ITextMetrics itm = xs.getTextMetrics( la );
		double dW = itm.getFullWidth( );
		double dH = itm.getFullHeight( );

		RotatedRectangle rr = null;
		if ( iLabelLocation == LEFT )
		{
			// ZERO : HORIZONTAL
			if ( dAngleInDegrees == 0 )
			{
				rr = new RotatedRectangle( dX - dW, dY - dH / 2, // TL
						dX,
						dY - dH / 2, // TR
						dX,
						dY + dH / 2, // BR
						dX - dW,
						dY + dH / 2 // BL
				);
			}
			// POSITIVE
			else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
			{
				rr = new RotatedRectangle( dX
						- dH
						* dSineTheta
						- dW
						* dCosTheta, dY - dH * dCosTheta + dW * dSineTheta, // TL
						dX - dH * dSineTheta,
						dY - dH * dCosTheta, // TR
						dX,
						dY, // BR
						dX - dW * dCosTheta,
						dY + dW * dSineTheta // BL
				);
			}
			// NEGATIVE
			else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
			{
				rr = new RotatedRectangle( dX - dW * dCosTheta, dY
						- dW
						* dSineTheta, // TL
						dX,
						dY, // TR
						dX - dH * dSineTheta,
						dY + dH * dCosTheta, // BR
						dX - dH * dSineTheta - dW * dCosTheta,
						dY + dH * dCosTheta - dW * dSineTheta // BL
				);
			}
			// ?90 : VERTICALLY UP OR DOWN
			else if ( dAngleInDegrees == 90 || dAngleInDegrees == -90 )
			{
				rr = new RotatedRectangle( dX - dH, dY - dW / 2, // TL
						dX,
						dY - dW / 2, // TR
						dX,
						dY + dW / 2, // BR
						dX - dH,
						dY + dW / 2 // BL
				);
			}
		}
		else if ( iLabelLocation == RIGHT )
		{
			// ZERO : HORIZONTAL
			if ( dAngleInDegrees == 0 )
			{
				rr = new RotatedRectangle( dX, dY - dH / 2, // TL
						dX + dW,
						dY - dH / 2, // TR
						dX + dW,
						dY + dH / 2, // BR
						dX,
						dY + dH / 2 // BL
				);
			}
			// POSITIVE
			else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
			{
				rr = new RotatedRectangle( dX, dY, // TL
						dX + dW * dCosTheta,
						dY - dW * dSineTheta, // TR
						dX + dW * dCosTheta + dH * dSineTheta,
						dY - dW * dSineTheta + dH * dCosTheta, // BR
						dX + dH * dSineTheta,
						dY + dH * dCosTheta // BL
				);
			}
			// NEGATIVE
			else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
			{
				rr = new RotatedRectangle( dX + dH * dSineTheta, dY
						- dH
						* dCosTheta, // TL
						dX + dH * dSineTheta + dW * dCosTheta,
						dY - dH * dCosTheta + dW * dSineTheta, // TR
						dX + dW * dCosTheta,
						dY + dW * dSineTheta, // BR
						dX,
						dY // BL
				);
			}
			// ?90 : VERTICALLY UP OR DOWN
			else if ( dAngleInDegrees == 90 || dAngleInDegrees == -90 )
			{
				rr = new RotatedRectangle( dX, dY - dW / 2, // TL
						dX + dH,
						dY - dW / 2, // TR
						dX + dH,
						dY + dW / 2, // BR
						dX,
						dY + dW / 2 // BL
				);
			}
		}
		else if ( iLabelLocation == BOTTOM )
		{
			// ZERO : HORIZONTAL
			if ( dAngleInDegrees == 0 )
			{
				rr = new RotatedRectangle( dX - dW / 2, dY, // TL
						dX + dW / 2,
						dY, // TR
						dX + dW / 2,
						dY + dH, // BR
						dX - dW / 2,
						dY + dH // BL
				);
			}
			// POSITIVE
			else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
			{
				rr = new RotatedRectangle( dX - dW * dCosTheta, dY
						+ dW
						* dSineTheta, // TL
						dX,
						dY, // TR
						dX + dH * dSineTheta,
						dY + dH * dCosTheta, // BR
						dX + dH * dSineTheta - dW * dCosTheta,
						dY + dH * dCosTheta + dW * dSineTheta // BL
				);
			}
			// NEGATIVE
			else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
			{
				rr = new RotatedRectangle( dX, dY, // TL
						dX + dW * dCosTheta,
						dY + dW * dSineTheta, // TR
						dX + dW * dCosTheta - dH * dSineTheta,
						dY + dW * dSineTheta + dH * dCosTheta, // BR
						dX - dH * dSineTheta,
						dY + dH * dCosTheta // BL
				);
			}
			// ?90 : VERTICALLY UP OR DOWN
			else if ( dAngleInDegrees == 90 || dAngleInDegrees == -90 )
			{
				rr = new RotatedRectangle( dX - dH / 2, dY, // TL
						dX + dH / 2,
						dY, // TR
						dX + dH / 2,
						dY + dW, // BR
						dX - dH / 2,
						dY + dW // BL
				);
			}
		}
		else if ( iLabelLocation == TOP )
		{
			// ZERO : HORIZONTAL
			if ( dAngleInDegrees == 0 )
			{
				rr = new RotatedRectangle( dX - dW / 2, dY - dH, // TL
						dX + dW / 2,
						dY - dH, // TR
						dX + dW / 2,
						dY, // BR
						dX - dW / 2,
						dY // BL
				);
			}
			// POSITIVE
			else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
			{
				rr = new RotatedRectangle( dX - dH * dSineTheta, dY
						- dH
						* dCosTheta, // TL
						dX - dH * dSineTheta + dW * dCosTheta,
						dY - dH * dCosTheta - dW * dSineTheta, // TR
						dX + dW * dCosTheta,
						dY - dW * dSineTheta, // BR
						dX,
						dY // BL
				);
			}
			// NEGATIVE
			else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
			{
				rr = new RotatedRectangle( dX
						- dW
						* dCosTheta
						+ dH
						* dSineTheta, dY - dW * dSineTheta - dH * dCosTheta, // TL
						dX + dH * dSineTheta,
						dY - dH * dCosTheta, // TR
						dX,
						dY, // BR
						dX - dW * dCosTheta,
						dY - dW * dSineTheta // BL
				);
			}
			// ?90 : VERTICALLY UP OR DOWN
			else if ( dAngleInDegrees == 90 || dAngleInDegrees == -90 )
			{
				rr = new RotatedRectangle( dX - dH / 2, dY - dW, // TL
						dX + dH / 2,
						dY - dW, // TR
						dX + dH / 2,
						dY, // BR
						dX - dH / 2,
						dY // BL
				);
			}
		}
		itm.dispose( );
		return rr;
	}

	/**
	 * @param xs
	 * @param bbox
	 * @param la
	 * @param fullHeight
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static final Location computeRotatedTopPoint( IDisplayServer xs,
			BoundingBox bbox, Label la, double fullHeight )
			throws IllegalArgumentException
	{
		double dAngleInDegrees = la.getCaption( ).getFont( ).getRotation( );

		if ( dAngleInDegrees < -90 || dAngleInDegrees > 90 )
		{
			throw new IllegalArgumentException( MessageFormat.format( Messages.getResourceBundle( xs.getULocale( ) )
					.getString( "exception.illegal.rotation.angle.label" ), //$NON-NLS-1$
					new Object[]{
						la
					} ) );
		}

		double dAngleInRadians = Math.toRadians( dAngleInDegrees );

		Location loc = LocationImpl.create( bbox.getLeft( ), bbox.getTop( ) );

		if ( dAngleInDegrees == 0 )
		{
			// does nothing.
		}
		else if ( dAngleInDegrees == 90 )
		{
			loc.setY( loc.getY( ) + bbox.getHeight( ) );
		}
		else if ( dAngleInDegrees == -90 )
		{
			loc.setX( loc.getX( ) + bbox.getWidth( ) );
		}
		else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
		{
			double A = bbox.getTop( )
					+ bbox.getHeight( )
					/ 2d
					- ( bbox.getLeft( ) + bbox.getWidth( ) / 2d )
					* Math.tan( dAngleInRadians );

			double ny = 2
					* ( bbox.getTop( ) + bbox.getHeight( ) / 2d )
					- ( Math.tan( dAngleInRadians ) * bbox.getLeft( ) + A )
					- fullHeight
					/ 2d
					/ Math.cos( dAngleInRadians );

			loc.setY( ny );

		}
		else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
		{
			double A = bbox.getTop( )
					+ bbox.getHeight( )
					/ 2d
					- ( bbox.getLeft( ) + bbox.getWidth( ) / 2d )
					* Math.tan( dAngleInRadians );

			double nx = 2
					* ( bbox.getLeft( ) + bbox.getWidth( ) / 2d )
					- ( ( bbox.getTop( ) - A ) / Math.tan( dAngleInRadians ) )
					+ fullHeight
					/ 2d
					/ Math.abs( Math.sin( dAngleInRadians ) );

			loc.setX( nx );
		}

		return loc;
	}

	/**
	 * 
	 * @param xs
	 * @param iLabelLocation
	 * @param la
	 * @param dX
	 * @param dY
	 * @return
	 * @throws UnexpectedInputException
	 */
	public static final BoundingBox computeBox( IDisplayServer xs,
			int iLabelLocation, Label la, double dX, double dY )
			throws IllegalArgumentException
	{
		double dAngleInDegrees = la.getCaption( ).getFont( ).getRotation( );
		if ( dAngleInDegrees < -90 || dAngleInDegrees > 90 )
		{
			throw new IllegalArgumentException( MessageFormat.format( Messages.getResourceBundle( xs.getULocale( ) )
					.getString( "exception.illegal.rotation.angle.label" ), //$NON-NLS-1$
					new Object[]{
						la
					} ) );
		}
		final double dAngleInRadians = ( ( -dAngleInDegrees * Math.PI ) / 180.0 );
		final double dSineTheta = Math.abs( Math.sin( dAngleInRadians ) );
		final double dCosTheta = Math.abs( Math.cos( dAngleInRadians ) );

		final ITextMetrics itm = xs.getTextMetrics( la );
		double dW = itm.getFullWidth( );
		double dH = itm.getFullHeight( );

		BoundingBox bb = null;
		if ( iLabelLocation == LEFT )
		{
			// ZERO : HORIZONTAL
			if ( dAngleInDegrees == 0 )
			{
				bb = new BoundingBox( LEFT,
						dX - dW,
						dY - dH / 2,
						dW,
						dH,
						dH / 2 );
			}
			// POSITIVE
			else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
			{
				bb = new BoundingBox( LEFT, dX
						- ( dH * dSineTheta + dW * dCosTheta ), dY
						- dH
						* dCosTheta, dH * dSineTheta + dW * dCosTheta, dH
						* dCosTheta
						+ dW
						* dSineTheta, dH * dCosTheta );
			}
			// NEGATIVE
			else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
			{
				bb = new BoundingBox( LEFT, dX
						- ( dH * dSineTheta + dW * dCosTheta ), dY
						- dW
						* dSineTheta, dH * dSineTheta + dW * dCosTheta, dH
						* dCosTheta
						+ dW
						* dSineTheta, dW * dSineTheta );
			}
			// ?90 : VERTICALLY UP OR DOWN
			else if ( dAngleInDegrees == 90 || dAngleInDegrees == -90 )
			{
				bb = new BoundingBox( LEFT,
						dX - dH,
						dY - dW / 2,
						dH,
						dW,
						dW / 2 );
			}
		}
		else if ( iLabelLocation == RIGHT )
		{
			// ZERO : HORIZONTAL
			if ( dAngleInDegrees == 0 )
			{
				bb = new BoundingBox( RIGHT, dX, dY - dH / 2, dW, dH, dH / 2 );
			}
			// POSITIVE
			else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
			{
				bb = new BoundingBox( RIGHT, dX, dY - dW * dSineTheta, dH
						* dSineTheta
						+ dW
						* dCosTheta, dH * dCosTheta + dW * dSineTheta, dW
						* dSineTheta );
			}
			// NEGATIVE
			else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
			{
				bb = new BoundingBox( RIGHT, dX, dY - dH * dCosTheta, dH
						* dSineTheta
						+ dW
						* dCosTheta, dH * dCosTheta + dW * dSineTheta, dH
						* dCosTheta );
			}
			// ?90 : VERTICALLY UP OR DOWN
			else if ( dAngleInDegrees == 90 || dAngleInDegrees == -90 )
			{
				bb = new BoundingBox( RIGHT, dX, dY - dW / 2, dH, dW, dW / 2 );
			}
		}
		else if ( iLabelLocation == BOTTOM )
		{
			// ZERO : HORIZONTAL
			if ( dAngleInDegrees == 0 )
			{
				bb = new BoundingBox( BOTTOM, dX - dW / 2, dY, dW, dH, dW / 2 );
			}
			// POSITIVE
			else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
			{
				bb = new BoundingBox( BOTTOM, dX - dW * dCosTheta, dY, dH
						* dSineTheta
						+ dW
						* dCosTheta, dH * dCosTheta + dW * dSineTheta, dW
						* dCosTheta );
			}
			// NEGATIVE
			else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
			{
				bb = new BoundingBox( BOTTOM, dX - dH * dSineTheta, dY, dH
						* dSineTheta
						+ dW
						* dCosTheta, dH * dCosTheta + dW * dSineTheta, dH
						* dSineTheta );
			}
			// ?90 : VERTICALLY UP OR DOWN
			else if ( dAngleInDegrees == 90 || dAngleInDegrees == -90 )
			{
				bb = new BoundingBox( BOTTOM, dX - dH / 2, dY, dH, dW, dH / 2 );
			}
		}
		else if ( iLabelLocation == TOP )
		{
			// ZERO : HORIZONTAL
			if ( dAngleInDegrees == 0 )
			{
				bb = new BoundingBox( TOP, dX - dW / 2, dY - dH, dW, dH, dW / 2 );
			}
			// POSITIVE
			else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
			{
				bb = new BoundingBox( TOP, dX - dH * dSineTheta, dY
						- ( dH * dCosTheta + dW * dSineTheta ), dH
						* dSineTheta
						+ dW
						* dCosTheta, dH * dCosTheta + dW * dSineTheta, dH
						* dSineTheta );
			}
			// NEGATIVE
			else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
			{
				bb = new BoundingBox( TOP, dX - dW * dCosTheta, dY
						- ( dH * dCosTheta + dW * dSineTheta ), dH
						* dSineTheta
						+ dW
						* dCosTheta, dH * dCosTheta + dW * dSineTheta, dW
						* dCosTheta );
			}
			// ?90 : VERTICALLY UP OR DOWN
			else if ( dAngleInDegrees == 90 || dAngleInDegrees == -90 )
			{
				bb = new BoundingBox( TOP, dX - dH / 2, dY - dW, dH, dW, dH / 2 );
			}
		}
		itm.dispose( );

		return bb;
	}

	/**
	 * Converts to internal (non public-model) data structures
	 * 
	 * @param lp
	 * @return
	 */
	public static final int getLabelPosition( Position lp )
	{
		int iLabelPosition = UNDEFINED;
		switch ( lp.getValue( ) )
		{
			case Position.LEFT :
				iLabelPosition = LEFT;
				break;
			case Position.RIGHT :
				iLabelPosition = RIGHT;
				break;
			case Position.ABOVE :
				iLabelPosition = ABOVE;
				break;
			case Position.BELOW :
				iLabelPosition = BELOW;
				break;
			case Position.OUTSIDE :
				iLabelPosition = OUTSIDE;
				break;
			case Position.INSIDE :
				iLabelPosition = INSIDE;
				break;
		}
		return iLabelPosition;
	}
}