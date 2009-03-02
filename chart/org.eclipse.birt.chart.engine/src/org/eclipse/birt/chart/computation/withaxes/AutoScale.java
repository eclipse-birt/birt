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

package org.eclipse.birt.chart.computation.withaxes;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.EllipsisHelper;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.Point;
import org.eclipse.birt.chart.computation.Rectangle;
import org.eclipse.birt.chart.computation.RotatedRectangle;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.internal.factory.DateFormatWrapperFactory;
import org.eclipse.birt.chart.internal.factory.IDateFormatWrapper;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * Encapsulates the auto scaling algorithms used by the rendering and chart
 * computation framework.
 */
public final class AutoScale extends Methods implements Cloneable
{

	public static String KEY_SHARED_MINMAX = "SharedMinMax"; //$NON-NLS-1$

	private final int iType;

	private Object oMinimum;

	private Object oMaximum;

	private Object oMinimumFixed;

	private Object oMaximumFixed;

	/**
	 * Minimum without considering fixed value. Only valid if margin percent is
	 * set and real minimum is less than fixed minimum.
	 */
	private Object oMinimumWithMargin = null;

	/**
	 * Maximum without considering fixed value. Only valid if margin percent is
	 * set and real maximum is greater than fixed maximum.
	 */
	private Object oMaximumWithMargin = null;

	private Object oStep;

	private Integer oStepNumber;

	private double dFactor = -1;

	private Object oUnit;

	private double dStartShift;

	private double dEndShift;

	private double dZoomFactor = 1.0;

	private int iMarginPercent = 0;

	private double dStart, dEnd;

	private AxisTickCoordinates atcTickCoordinates;

	private LabelVisibleHelper labelVisHelper = null;

	private Map<Integer, String> hmComputedLabelText = null;

	private StaggeredHelper staggeredHelper = null;

	private DataSetIterator dsiData;

	private boolean bCategoryScale = false;

	private boolean bTickBetweenCategories = true;

	private boolean bLabelWithinAxes = false;

	private RunTimeContext rtc;

	private ScaleContext tmpSC;

	private boolean bExpandMinMax = true;

	private boolean bAlignZero = false;

	private ChartUtil.CacheDecimalFormat cacheNumFormat = new ChartUtil.CacheDecimalFormat( );

	private ChartUtil.Cache<Integer, IDateFormatWrapper> cacheDateFormat = new ChartUtil.Cache<Integer, IDateFormatWrapper>( ) {

		@Override
		protected IDateFormatWrapper newValue( Integer iDateTimeUnit )
		{
			return DateFormatWrapperFactory.getPreferredDateFormat( iDateTimeUnit,
					rtc.getULocale( ) );
		}

	};

	/** Indicates the max boundary of axis ticks. */
	private static final int TICKS_MAX = 1000;

	private static final NumberFormat dfDoulbeNormalized = NumberFormat.getInstance( Locale.ENGLISH );;

	static
	{
		try
		{
			( (DecimalFormat) dfDoulbeNormalized ).applyPattern( ".###############E0" ); //$NON-NLS-1$
		}
		catch ( ClassCastException e )
		{
		}
	}

	/**
	 * Quick static lookup for linear scaling
	 */
	// private static int[] iaLinearDeltas = { 1, 2, 5 };
	private static int[] iaLinearDeltas = {
			1, 2, 5, 10
	};

	/**
	 * Quick static lookup for logarithmic scaling
	 */
	// private static int[] iaLogarithmicDeltas = { 2, 4, 5, 10 };
	private static int[] iaLogarithmicDeltas = {
		10
	};

	/**
	 * Quick static lookup for datetime scaling
	 */
	private static int[] iaCalendarUnits = {
			Calendar.SECOND,
			Calendar.MINUTE,
			Calendar.HOUR_OF_DAY,
			Calendar.DATE,
			Calendar.MONTH,
			Calendar.YEAR
	};

	private static int[] iaSecondDeltas = {
			1, 5, 10, 15, 20, 30
	};

	private static int[] iaMinuteDeltas = {
			1, 5, 10, 15, 20, 30
	};

	private static int[] iaHourDeltas = {
			1, 2, 3, 4, 12
	};

	private static int[] iaDayDeltas = {
			1, 7, 14
	};

	private static int[] iaMonthDeltas = {
			1, 2, 3, 4, 6
	};

	private static int[][] iaCalendarDeltas = {
			iaSecondDeltas,
			iaMinuteDeltas,
			iaHourDeltas,
			iaDayDeltas,
			iaMonthDeltas,
			null
	};

	private boolean bIntegralZoom = true;

	private boolean bMinimumFixed = false;

	private boolean bMaximumFixed = false;

	private boolean bStepFixed = false;

	private int iScaleDirection = AUTO;

	private boolean bAxisLabelStaggered = false;

	private int iLabelShowingInterval = 0;

	private FormatSpecifier fs = null;

	private double dPrecision = 0;

	private int iMinUnit = 0;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/computation.withaxes" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param _iType
	 */
	AutoScale( int _iType )
	{
		iType = _iType;
	}

	/**
	 * The constructor.
	 * 
	 * @param _iType
	 * @param _oMinimum
	 * @param _oMaximum
	 * @param _oStep
	 */
	public AutoScale( int _iType, Object _oMinimum, Object _oMaximum )
	{
		iType = _iType;
		oMinimum = _oMinimum;
		oMaximum = _oMaximum;
	}

	final void setFixed( boolean _bMinimum, boolean _bMaximum, boolean _bStep )
	{
		bMinimumFixed = _bMinimum;
		bMaximumFixed = _bMaximum;
		bStepFixed = _bStep;
	}

	/**
	 * Sets the scale direction.
	 * 
	 * @param val
	 */
	public final void setDirection( int iValue )
	{
		iScaleDirection = iValue;
	}

	/**
	 * Returns the scale direction.
	 * 
	 * @return
	 */
	public int getDirection( )
	{
		return iScaleDirection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public final Object clone( )
	{
		final AutoScale sc = new AutoScale( iType, oMinimum, oMaximum );
		sc.oStep = oStep;
		sc.oStepNumber = this.oStepNumber;
		sc.dStart = dStart;
		sc.dEnd = dEnd;
		sc.oMaximumWithMargin = oMaximumWithMargin;
		sc.oMinimumWithMargin = oMinimumWithMargin;
		sc.iMarginPercent = iMarginPercent;
		sc.atcTickCoordinates = atcTickCoordinates;
		sc.dStartShift = dStartShift;
		sc.dEndShift = dEndShift;
		sc.dsiData = dsiData;
		sc.oUnit = oUnit;
		sc.bMaximumFixed = bMaximumFixed;
		sc.bMinimumFixed = bMinimumFixed;
		sc.bStepFixed = bStepFixed;
		sc.fs = fs;
		sc.rtc = rtc;
		sc.bIntegralZoom = bIntegralZoom;
		sc.bCategoryScale = bCategoryScale;
		sc.labelVisHelper = labelVisHelper;
		sc.iScaleDirection = iScaleDirection;
		sc.staggeredHelper = staggeredHelper;
		sc.bAxisLabelStaggered = bAxisLabelStaggered;
		sc.iLabelShowingInterval = iLabelShowingInterval;
		sc.bTickBetweenCategories = bTickBetweenCategories;
		sc.bLabelWithinAxes = bLabelWithinAxes;
		sc.iMinUnit = iMinUnit;
		sc.hmComputedLabelText = sc.hmComputedLabelText;
		sc.tmpSC = tmpSC;
		sc.bExpandMinMax = bExpandMinMax;
		sc.bAlignZero = bAlignZero;

		return sc;
	}

	/**
	 * Zooms IN 'once' into a scale of type numerical or datetime Typically,
	 * this is called in a loop until label overlaps occur
	 */
	public final boolean zoomIn( )
	{
		if ( bStepFixed )
		{
			return false; // CANNOT ZOOM FOR FIXED STEPS
		}
		if ( ChartUtil.mathEqual( 0, ( (Number) oStep ).doubleValue( ) ) )
		{
			return false; // CANNOT ZOOM ANY MORE
		}

		if ( ( iType & NUMERICAL ) == NUMERICAL )
		{
			if ( ( iType & LOGARITHMIC ) == LOGARITHMIC )
			{
				final double dStep = asDouble( oStep ).doubleValue( );
				if ( ( Math.log( dStep ) / LOG_10 ) > 1 )
				{
					setStep( new Double( dStep / 10 ) );
				}
				else
				{
					int n = iaLogarithmicDeltas.length;
					for ( int i = n - 1; i >= 0; i-- )
					{
						if ( (int) dStep == iaLogarithmicDeltas[i] )
						{
							if ( i > 0 )
							{
								setStep( new Double( iaLogarithmicDeltas[i - 1] ) );
								return true;
							}
							else
							{
								return false;
							}
						}
					}
					return false;
				}
			}
			else if ( ( iType & LINEAR ) == LINEAR )
			{
				double dStep = asDouble( oStep ).doubleValue( );
				final double oldStep = dStep;
				if ( bIntegralZoom )
				{

					double dPower = ( Math.log( dStep ) / LOG_10 );
					dPower = Math.floor( dPower );
					dPower = Math.pow( 10.0, dPower );
					dStep /= dPower;
					dStep = Math.round( dStep );
					int n = iaLinearDeltas.length;
					for ( int i = 0; i < n; i++ )
					{
						if ( (int) dStep == iaLinearDeltas[i] )
						{
							if ( i > 0 )
							{
								dStep = iaLinearDeltas[i - 1] * dPower;
							}
							else
							{
								dPower /= 10;
								dStep = iaLinearDeltas[n - 2] * dPower;
							}
							break;
						}
					}
					// To prevent endless loop if step is not changed
					if ( dStep == oldStep )
					{
						dStep /= 2;
					}
					setStep( new Double( dStep ) );
				}
				else
				{
					dStep /= 2;
					setStep( new Double( dStep ) );
				}

				if ( ( (Number) oStep ).doubleValue( ) < dPrecision )
				{
					setStep( new Double( oldStep ) );
					return false; // CANNOT ZOOM ANY MORE
				}
			}

		}
		else if ( ( iType & DATE_TIME ) == DATE_TIME )
		{
			int[] ia = null;
			int iStep = asInteger( oStep );
			int iUnit = asInteger( oUnit );

			for ( int icu = 0; icu < iaCalendarUnits.length; icu++ )
			{
				if ( iUnit == iaCalendarUnits[icu] )
				{
					ia = iaCalendarDeltas[icu];
					if ( ia == null ) // HANDLE YEARS SEPARATELY
					{
						iStep--;
						if ( iStep == 0 )
						{
							setStep( Integer.valueOf( iaMonthDeltas[iaMonthDeltas.length - 1] ) );
							oUnit = Integer.valueOf( Calendar.MONTH );
						}
					}
					else
					// HANDLE SECONDS, MINUTES, HOURS, DAYS, MONTHS
					{
						int i = 0;
						for ( ; i < ia.length; i++ )
						{
							if ( ia[i] == iStep )
							{
								break;
							}
						}

						if ( i == 0 ) // WE'RE AT THE FIRST ELEMENT IN THE
						// DELTAS ARRAY
						{
							// #217377
							if ( icu <= iMinUnit )
							{
								return false; // CAN'T ZOOM ANYMORE THAN
							}
							// 1-SECOND INTERVALS (AT INDEX=0)
							ia = iaCalendarDeltas[icu - 1]; // DOWNGRADE ARRAY
							// TO PREVIOUS
							// DELTAS ARRAY
							i = ia.length; // MANIPULATE OFFSET TO END+1
							oUnit = Integer.valueOf( iaCalendarUnits[icu - 1] ); // DOWNGRADE
							// UNIT
						}
						setStep( Integer.valueOf( ia[i - 1] ) ); // RETURN
																	// PREVIOUS
						// STEP IN DELTAS
						// ARRAY
						break;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Zooms OUT 'once' into a scale of type numerical or datetime Typically,
	 * this is called in a loop until label overlaps occur
	 */
	public final boolean zoomOut( )
	{
		// Fix Bugzilla#220710 to avoid useless zoom out
		if ( bStepFixed || this.getTickCordinates( ).size( ) < 3 )
		{
			return false;
		}

		if ( ( (Number) oStep ).doubleValue( ) >= Double.MAX_VALUE )
		{
			return false; // CANNOT ZOOM ANY MORE
		}

		if ( ( iType & NUMERICAL ) == NUMERICAL )
		{
			if ( ( iType & LOGARITHMIC ) == LOGARITHMIC )
			{
				final double dStep = asDouble( oStep ).doubleValue( );
				if ( ( Math.log( dStep ) / LOG_10 ) >= 1 )
				{
					setStep( new Double( dStep * 10 ) );
				}
				else
				{
					final int n = iaLogarithmicDeltas.length;
					for ( int i = 0; i < n; i++ )
					{
						if ( (int) dStep == iaLogarithmicDeltas[i] )
						{
							setStep( new Double( iaLogarithmicDeltas[i + 1] ) );
							return true;
						}
					}
					return false;
				}
			}
			else if ( ( iType & LINEAR ) == LINEAR )
			{
				double dStep = asDouble( oStep ).doubleValue( );

				if ( bIntegralZoom )
				{
					double dPower = Math.log10( dStep );

					if ( dPower < 0 )
					{
						dPower = Math.floor( dPower );
						dPower = Math.pow( 10, dPower );
					}
					else
					{
						dPower = dStep;
					}

					dStep /= dPower;
					dStep = Math.round( dStep );
					int n = iaLinearDeltas.length;
					int i = 0;

					for ( ; i < n; i++ )
					{
						if ( dStep < iaLinearDeltas[i] )
						{
							dStep = iaLinearDeltas[i] * dPower;
							break;
						}
					}
					if ( i == n )
					{
						dPower *= 20;
						dStep = iaLinearDeltas[0] * dPower;
					}

					if ( ( (Number) oStep ).doubleValue( ) == dStep )
					{
						// Can not zoom any more, result is always the same;
						return false;
					}
				}
				else
				{
					dStep *= 2;
				}

				dStep = ChartUtil.alignWithInt( dStep, false );
				setStep( new Double( dStep ) );
			}
		}
		else if ( ( iType & DATE_TIME ) == DATE_TIME )
		{
			int[] ia = null;
			int iStep = asInteger( oStep );
			int iUnit = asInteger( oUnit );

			for ( int icu = 0; icu < iaCalendarUnits.length; icu++ )
			{
				if ( iUnit == iaCalendarUnits[icu] )
				{
					ia = iaCalendarDeltas[icu];
					if ( ia == null ) // HANDLE YEARS SEPARATELY
					{
						iStep++; // NO UPPER LIMIT FOR YEARS
						setStep( Integer.valueOf( iStep ) );
					}
					else
					// HANDLE SECONDS, MINUTES, HOURS, DAYS, MONTHS
					{
						int i = 0, n = ia.length;
						for ( ; i < n; i++ )
						{
							if ( ia[i] == iStep )
							{
								break;
							}
						}

						if ( i == n - 1 ) // WE'RE AT THE LAST ELEMENT IN THE
						// DELTAS ARRAY
						{
							ia = iaCalendarDeltas[icu + 1]; // UPGRADE UNIT TO
							// NEXT DELTAS ARRAY
							oUnit = Integer.valueOf( iaCalendarUnits[icu + 1] );
							if ( ia == null ) // HANDLE YEARS
							{
								setStep( Integer.valueOf( 1 ) );
								return true;
							}
							i = -1; // MANIPULATE OFFSET TO START-1
						}
						setStep( Integer.valueOf( ia[i + 1] ) ); // RETURN NEXT
																	// STEP
						// IN
						// DELTAS ARRAY
						break;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Returns an auto computed decimal format pattern for representing axis
	 * labels on a numeric axis
	 * 
	 * @return
	 */
	public final String getNumericPattern( )
	{
		if ( oMinimum == null || oStep == null )
		{
			return "0.00"; //$NON-NLS-1$
		}

		double dMinValue = asDouble( oMinimum ).doubleValue( );
		double dStep = asDouble( oStep ).doubleValue( );

		if ( ( iType & LOGARITHMIC ) == LOGARITHMIC )
		{
			return ValueFormatter.getNumericPattern( dMinValue );
		}
		else
		{
			return ValueFormatter.getNumericPattern( dStep );
		}
	}

	/**
	 * 
	 * @return
	 */
	public final int getType( )
	{
		return iType;
	}

	/**
	 * 
	 * @param _oaData
	 */
	public final void setData( DataSetIterator _oaData )
	{
		dsiData = _oaData;
	}

	/**
	 * @return
	 */
	public final FormatSpecifier getFormatSpecifier( )
	{
		return this.fs;
	}

	/**
	 * @param fs
	 */
	public final void setFormatSpecifier( FormatSpecifier fs )
	{
		this.fs = fs;
	}

	/**
	 * 
	 * @return
	 */
	public final Object getUnit( )
	{
		return oUnit;
	}

	/**
	 * 
	 * @return
	 */
	public final DataSetIterator getData( )
	{
		return dsiData;
	}

	final void setTickCordinates( AxisTickCoordinates atc )
	{
		// if the factor is set, the tick number can be 1 if the step size is
		// large.
		if ( atc != null && atc.size( ) == 1 && !isSetFactor( ) )
		{
			throw new RuntimeException( new ChartException( ChartEnginePlugin.ID,
					ChartException.COMPUTATION,
					"exception.tick.computations", //$NON-NLS-1$ 
					Messages.getResourceBundle( rtc.getULocale( ) ) ) );
		}
		this.atcTickCoordinates = atc;
	}

	/**
	 * @param index
	 * @return
	 */
	public final boolean isTickLabelVisible( int index )
	{
		assert labelVisHelper != null;
		return labelVisHelper.isTickLabelVisible( index );
	}

	public final String getComputedLabelText( int index )
	{
		assert hmComputedLabelText != null;
		return hmComputedLabelText.get( index );
	}

	/**
	 * returns a list of all visible indexes, in the moment works only for
	 * categorey.
	 * 
	 * @return
	 */
	public final Collection<Integer> getVisibleLabelIds( )
	{
		return hmComputedLabelText.keySet( );
	}

	/**
	 * @param index
	 * @return
	 */
	public final boolean isTickLabelStaggered( int index )
	{
		assert staggeredHelper != null;
		return staggeredHelper.isTickLabelStaggered( index );
	}

	/**
	 * @return
	 */
	public final boolean isAxisLabelStaggered( )
	{
		return bAxisLabelStaggered;
	}

	/**
	 * 
	 * @return
	 */
	public final boolean isTickBetweenCategories( )
	{
		return bTickBetweenCategories;
	}

	/**
	 * 
	 * @return
	 */
	public final AxisTickCoordinates getTickCordinates( )
	{
		return this.atcTickCoordinates;
	}

	/**
	 * Returns the normalized start point. always be Zero.
	 * 
	 * @return
	 */
	public final double getNormalizedStart( )
	{
		return 0;
	}

	/**
	 * Returns the normalized end point. this will be the (orginal end - orginal
	 * start).
	 * 
	 * @return
	 */
	public final double getNormalizedEnd( )
	{
		return dEnd - dStart;
	}

	/**
	 * Returns the normalized start and end point.
	 * 
	 * @return
	 */
	public final double[] getNormalizedEndPoints( )
	{
		return new double[]{
				0, dEnd - dStart
		};
	}

	/**
	 * 
	 * @return
	 */
	public final double[] getEndPoints( )
	{
		return new double[]{
				dStart, dEnd
		};
	}

	/**
	 * 
	 * @param _dStart
	 * @param _dEnd
	 */
	final void setEndPoints( double _dStart, double _dEnd )
	{
		if ( _dStart != -1 )
		{
			dStart = _dStart;
		}

		if ( _dEnd != -1 )
		{
			dEnd = _dEnd;
		}

		if ( atcTickCoordinates != null )
		{
			atcTickCoordinates.setEndPoints( dStart, dEnd );
		}
	}

	private void checkValible( double dValue, String sName )
			throws ChartException
	{
		if ( Double.isInfinite( dValue ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					sName
							+ Messages.getString( "AutoScale.Exception.IsInfiite" ), //$NON-NLS-1$
					Messages.getResourceBundle( rtc.getULocale( ) ) );
		}

		if ( Double.isNaN( dValue ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					sName + Messages.getString( "AutoScale.Exception.IsNaN" ), //$NON-NLS-1$
					Messages.getResourceBundle( rtc.getULocale( ) ) );
		}
	}

	/**
	 * Computes tick count
	 * 
	 * @return tick count
	 */
	public final int getTickCount( ) throws ChartException
	{
		if ( this.oStepNumber != null )
		{
			if ( bCategoryScale || ( iType & NUMERICAL ) != NUMERICAL )
			{
				// Log the exception to notify only numeric value is supported
				logger.log( new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						"exception.unsupported.step.number", //$NON-NLS-1$
						Messages.getResourceBundle( rtc.getULocale( ) ) ) );
			}
			else
			{
				return this.oStepNumber.intValue( ) + 1;
			}
		}

		int nTicks = 2;
		if ( isCategoryScale( ) )
		{
			if ( dsiData != null )
			{
				nTicks = dsiData.size( ) + 1;
				if ( !bTickBetweenCategories )
				{
					nTicks++;
				}
			}
		}
		else if ( ( iType & NUMERICAL ) == NUMERICAL )
		{
			if ( ( iType & LINEAR ) == LINEAR )
			{
				double dMax = asDouble( oMaximum ).doubleValue( );
				double dMin = asDouble( oMinimum ).doubleValue( );
				double dStep = asDouble( oStep ).doubleValue( );

				if ( !ChartUtil.mathEqual( dMax, dMin ) )
				{
					double lNTicks = Math.ceil( ( dMax - dMin ) / dStep - 0.5 ) + 1;
					if ( ( lNTicks > TICKS_MAX ) || ( lNTicks < 2 ) )
					{
						if ( lNTicks > TICKS_MAX )
						{
							nTicks = TICKS_MAX;
						}
						else
						{ // dNTicks<2
							nTicks = 2;
						}
						// update the step size
						dStep = dMax / ( nTicks - 1 ) - dMin / ( nTicks - 1 );
						checkValible( dStep,
								Messages.getString( "AutoScale.ValueName.StepSize" ) ); //$NON-NLS-1$
						dStep = ChartUtil.alignWithInt( dStep, true );
						setStep( new Double( dStep ) );
					}
					else
					{
						nTicks = (int) lNTicks;
					}
				}
				else
					nTicks = 5;
			}
			else if ( ( iType & LOGARITHMIC ) == LOGARITHMIC )
			{
				double dMax = asDouble( oMaximum ).doubleValue( );
				double dMin = asDouble( oMinimum ).doubleValue( );
				double dStep = asDouble( oStep ).doubleValue( );

				double dMaxLog = ( Math.log( dMax ) / LOG_10 );
				double dMinLog = ( Math.log( dMin ) / LOG_10 );
				double dStepLog = ( Math.log( dStep ) / LOG_10 );

				nTicks = (int) Math.ceil( ( dMaxLog - dMinLog ) / dStepLog ) + 1;
			}
		}
		else if ( ( iType & DATE_TIME ) == DATE_TIME )
		{
			final CDateTime cdt1 = (CDateTime) oMinimum;
			final CDateTime cdt2 = (CDateTime) oMaximum;
			double diff = CDateTime.computeDifference( cdt2,
					cdt1,
					asInteger( oUnit ) )
					/ asInteger( oStep );

			nTicks = (int) Math.round( diff ) + 1;
		}
		else
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					"exception.unknown.axis.type.tick.computations", //$NON-NLS-1$
					Messages.getResourceBundle( rtc.getULocale( ) ) );
		}

		// at least 2 ticks
		if ( nTicks < 2 )
		{
			nTicks = 2;
		}

		return nTicks;
	}

	/**
	 * Returns the absolute value of the scale unit.
	 * 
	 * @return
	 */
	public final double getUnitSize( )
	{
		if ( atcTickCoordinates == null )
		{
			throw new RuntimeException( new ChartException( ChartEnginePlugin.ID,
					ChartException.COMPUTATION,
					"exception.unit.size.failure", //$NON-NLS-1$ 
					Messages.getResourceBundle( rtc.getULocale( ) ) ) );
		}
		return Math.abs( atcTickCoordinates.getStep( ) );
	}

	/**
	 * 
	 * @return
	 */
	public final Object getMinimum( )
	{
		return oMinimum;
	}

	/**
	 * @param o
	 */
	public final void setMinimum( Object o )
	{
		this.oMinimum = o;
	}

	/**
	 * 
	 * @return
	 */
	public final Object getMaximum( )
	{
		return oMaximum;
	}

	/**
	 * @param o
	 */
	public final void setMaximum( Object o )
	{
		this.oMaximum = o;
	}

	/**
	 * 
	 * @return step size
	 */
	public final Object getStep( )
	{
		return oStep;
	}

	/**
	 * @param o
	 */
	public final void setStep( Object o )
	{
		this.oStep = o;
	}

	/**
	 * 
	 * @return step number
	 */
	public final Integer getStepNumber( )
	{
		return oStepNumber;
	}

	public final void setStepNumber( Integer o )
	{
		this.oStepNumber = o;
	}

	public final double getFactor( )
	{
		return dFactor;
	}

	private void setFactor( double factor )
	{
		this.dFactor = factor;
	}

	public final boolean isSetFactor( )
	{
		return dFactor < 0 ? false : true;
	}

	/**
	 * 
	 * @return
	 */
	final Object[] getMinMax( ) throws ChartException
	{
		Object oValue = null;
		try
		{
			if ( ( iType & NUMERICAL ) == NUMERICAL )
			{

				double dValue, dMinValue = Double.MAX_VALUE, dMaxValue = -Double.MAX_VALUE;
				dsiData.reset( );
				while ( dsiData.hasNext( ) )
				{
					oValue = dsiData.next( );
					if ( oValue == null ) // NULL VALUE CHECK
					{
						continue;
					}
					dValue = ( (Double) oValue ).doubleValue( );
					if ( dValue < dMinValue )
						dMinValue = dValue;
					if ( dValue > dMaxValue )
						dMaxValue = dValue;
				}

				return new Object[]{
						new Double( dMinValue ), new Double( dMaxValue )
				};
			}
			else if ( ( iType & DATE_TIME ) == DATE_TIME )
			{
				Calendar cValue;
				Calendar caMin = null, caMax = null;
				dsiData.reset( );
				while ( dsiData.hasNext( ) )
				{
					oValue = dsiData.next( );
					cValue = (Calendar) oValue;
					if ( caMin == null )
					{
						caMin = cValue;
					}
					if ( caMax == null )
					{
						caMax = cValue;
					}
					if ( cValue == null ) // NULL VALUE CHECK
					{
						continue;
					}
					if ( cValue.before( caMin ) )
						caMin = cValue;
					else if ( cValue.after( caMax ) )
						caMax = cValue;
				}
				return new Object[]{
						new CDateTime( caMin ), new CDateTime( caMax )
				};
			}
		}
		catch ( ClassCastException ex )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					"exception.invalid.axis.data.type", //$NON-NLS-1$
					new Object[]{
						oValue
					},
					Messages.getResourceBundle( rtc.getULocale( ) ) );
		}
		return null;
	}

	/**
	 * Computes min, max value, step size and step number of the Axis
	 * 
	 * @param oMinValue
	 *            min value in data points. Double or CDateTime type.
	 * @param oMaxValue
	 *            max value in data points. Double or CDateTime type.
	 */
	public final void updateAxisMinMax( Object oMinValue, Object oMaxValue )
	{
		// Use the shared context if it's shared
		if ( rtc.getSharedScale( ) != null && rtc.getSharedScale( ).isShared( ) )
		{
			updateContext( rtc.getSharedScale( ).getScaleContext( ) );
			return;
		}

		ScaleContext sct;
		if ( ( iType & LOGARITHMIC ) == LOGARITHMIC )
		{
			if ( ( iType & PERCENT ) == PERCENT )
			{
				oMaximum = new Double( 100 );
				oMinimum = new Double( 1 );
				setStep( new Double( 10 ) );
				bMaximumFixed = true;
				bMinimumFixed = true;
				bStepFixed = true;
				return;
			}

			sct = new ScaleContext( iMarginPercent,
					iType,
					oMinValue,
					oMaxValue,
					oStep );
		}
		else if ( ( iType & DATE_TIME ) == DATE_TIME )
		{
			int iUnit = asInteger( oUnit );
			sct = new ScaleContext( iMarginPercent,
					iType,
					iUnit,
					oMinValue,
					oMaxValue,
					oStep );
		}
		else
		{
			// Linear axis type
			sct = new ScaleContext( iMarginPercent,
					iType,
					oMinValue,
					oMaxValue,
					oStep );
		}

		if ( ( iType & DATE_TIME ) == DATE_TIME )
		{
			// Bugzilla#217044
			sct.setFixedValue( bMinimumFixed,
					bMaximumFixed,
					oMinimumFixed,
					oMaximumFixed );
		}
		else
		{
			sct.setFixedValue( bMinimumFixed, bMaximumFixed, oMinimum, oMaximum );
		}
		sct.setExpandMinmax( bExpandMinMax );
		sct.setFixedStep( bStepFixed, oStepNumber );
		sct.computeMinMax( bAlignZero );
		updateContext( sct );

		// Temperory scale for later used in shared scale
		tmpSC = sct;
		updateSharedScaleContext(rtc, iType, tmpSC);
	}

	private final void updateContext( ScaleContext sct )
	{
		this.oMaximum = sct.getMax( );
		this.oMinimum = sct.getMin( );
		this.oMaximumWithMargin = sct.getMaxWithMargin( );
		this.oMinimumWithMargin = sct.getMinWithMargin( );
		setStep( sct.getStep( ) );
		this.oUnit = sct.getUnit( );
	}

	Object getMinWithMargin( )
	{
		return this.oMinimumWithMargin;
	}

	Object getMaxWithMargin( )
	{
		return this.oMaximumWithMargin;
	}

	/**
	 * Checks all labels for any overlap for a given axis' scale
	 * 
	 * @param la
	 * @param iLabelLocation
	 * 
	 * @return
	 */
	public final boolean checkFit( IDisplayServer xs, Label la,
			int iLabelLocation ) throws ChartException
	{
		if ( isCategoryScale( ) )
		{
			// not for text and category style
			return true;
		}

		final double dAngleInDegrees = la.getCaption( )
				.getFont( )
				.getRotation( );
		double x = 0, y = 0;
		int iPointToCheck = 0;
		if ( iLabelLocation == ABOVE || iLabelLocation == BELOW )
		{
			if ( iScaleDirection == BACKWARD )
			{
				iPointToCheck = ( dAngleInDegrees < 0 && dAngleInDegrees > -90 ) ? 1
						: 2;
			}
			else
			{
				iPointToCheck = ( dAngleInDegrees < 0 && dAngleInDegrees > -90 ) ? 3
						: 0;
			}
		}
		else if ( iLabelLocation == LEFT || iLabelLocation == RIGHT )
		{
			if ( iScaleDirection == FORWARD )
			{
				iPointToCheck = ( dAngleInDegrees < 0 && dAngleInDegrees > -90 ) ? 0
						: 1;
			}
			else
			{
				iPointToCheck = ( dAngleInDegrees < 0 && dAngleInDegrees > -90 ) ? 2
						: 3;
			}
		}
		AxisTickCoordinates da = atcTickCoordinates;
		RotatedRectangle rrPrev = null, rrPrev2 = null, rr;
		Double fontHeight = Methods.computeFontHeight( xs, la );

		if ( ( iType & ( NUMERICAL | LINEAR ) ) == ( NUMERICAL | LINEAR ) )
		{
			double dAxisValue = asDouble( getMinimum( ) ).doubleValue( );
			final double dAxisStep = asDouble( getStep( ) ).doubleValue( );
			String sText;
			DecimalFormat df = null;
			if ( fs == null ) // CREATE IF FORMAT SPECIFIER IS UNDEFINED
			{
				df = computeDecimalFormat( dAxisValue, dAxisStep );
			}
			final NumberDataElement nde = NumberDataElementImpl.create( 0 );

			for ( int i = 0; i < da.size( ); i++ )
			{
				// TODO special logic for last datapoint in non-equal scale unit
				// case.
				// Ignore the top label for better auto scale.

				nde.setValue( dAxisValue );
				try
				{
					sText = ValueFormatter.format( nde,
							fs,
							rtc.getULocale( ),
							df );
				}
				catch ( ChartException dfex )
				{
					logger.log( dfex );
					sText = NULL_STRING;
				}

				if ( iLabelLocation == ABOVE || iLabelLocation == BELOW )
				{
					x = da.getCoordinate( i ) * dZoomFactor;
				}
				else if ( iLabelLocation == LEFT || iLabelLocation == RIGHT )
				{
					y = da.getCoordinate( i ) * dZoomFactor;
				}

				la.getCaption( ).setValue( sText );
				try
				{
					rr = computePolygon( xs,
							iLabelLocation,
							la,
							x,
							y,
							fontHeight );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							uiex );
				}

				if ( i == 0 && bLabelWithinAxes )
				{
					Rectangle rect = rr.getBounds( );
					if ( iLabelLocation == ABOVE || iLabelLocation == BELOW )
					{
						rr.shiftXVertices( rect.getWidth( ) / 2 );
					}
					else
					{
						rr.shiftYVertices( -rect.getHeight( ) / 2 );
					}
				}

				Point p = rr.getPoint( iPointToCheck );

				if ( isAxisLabelStaggered( ) && isTickLabelStaggered( i ) )
				{
					if ( rrPrev2 != null
							&& ( rrPrev2.contains( p )
									|| rrPrev2.getPoint( iPointToCheck )
											.equals( p ) || ChartUtil.intersects( rr,
									rrPrev2 ) ) )
					{
						return false;
					}
					rrPrev2 = rr;
				}
				else
				{
					if ( rrPrev != null
							&& ( rrPrev.contains( p )
									|| rrPrev.getPoint( iPointToCheck )
											.equals( p ) || ChartUtil.intersects( rr,
									rrPrev ) ) )
					{
						return false;
					}
					rrPrev = rr;
				}
				dAxisValue += dAxisStep;
			}
		}
		else if ( ( iType & ( NUMERICAL | LOGARITHMIC ) ) == ( NUMERICAL | LOGARITHMIC ) )
		{
			double dAxisValue = asDouble( getMinimum( ) ).doubleValue( );
			final double dAxisStep = asDouble( getStep( ) ).doubleValue( );
			String sText;
			NumberDataElement nde = NumberDataElementImpl.create( 0 );
			DecimalFormat df = null;

			for ( int i = 0; i < da.size( ) - 1; i++ )
			{
				nde.setValue( dAxisValue );
				if ( fs == null )
				{
					df = computeDecimalFormat( dAxisValue, dAxisStep );
				}
				try
				{
					sText = ValueFormatter.format( nde,
							fs,
							rtc.getULocale( ),
							df );
				}
				catch ( ChartException dfex )
				{
					logger.log( dfex );
					sText = NULL_STRING;
				}

				if ( iLabelLocation == ABOVE || iLabelLocation == BELOW )
				{
					x = da.getCoordinate( i ) * dZoomFactor;
				}
				else if ( iLabelLocation == LEFT || iLabelLocation == RIGHT )
				{
					y = da.getCoordinate( i ) * dZoomFactor;
				}

				la.getCaption( ).setValue( sText );
				try
				{
					rr = computePolygon( xs,
							iLabelLocation,
							la,
							x,
							y,
							fontHeight );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							uiex );
				}

				Point p = rr.getPoint( iPointToCheck );

				if ( isAxisLabelStaggered( ) && isTickLabelStaggered( i ) )
				{
					if ( rrPrev2 != null
							&& ( rrPrev2.contains( p )
									|| rrPrev2.getPoint( iPointToCheck )
											.equals( p ) || ChartUtil.intersects( rr,
									rrPrev2 ) ) )
					{
						return false;
					}
					rrPrev2 = rr;
				}
				else
				{
					if ( rrPrev != null
							&& ( rrPrev.contains( p ) || rrPrev.getPoint( iPointToCheck )
									.equals( p ) ) )
					{
						return false;
					}
					rrPrev = rr;
				}
				dAxisValue *= dAxisStep;
			}
		}
		else if ( iType == DATE_TIME )
		{
			CDateTime cdt, cdtAxisValue = asDateTime( oMinimum );
			final int iUnit = asInteger( oUnit );
			final int iStep = asInteger( oStep );
			final IDateFormatWrapper sdf = DateFormatWrapperFactory.getPreferredDateFormat( iUnit,
					rtc.getULocale( ) );

			String sText;
			cdt = cdtAxisValue;

			for ( int i = 0; i < da.size( ) - 1; i++ )
			{
				sText = ValueFormatter.format( cdt, fs, rtc.getULocale( ), sdf );

				if ( iLabelLocation == ABOVE || iLabelLocation == BELOW )
				{
					x = da.getCoordinate( i ) * dZoomFactor;
				}
				else if ( iLabelLocation == LEFT || iLabelLocation == RIGHT )
				{
					y = da.getCoordinate( i ) * dZoomFactor;
				}

				la.getCaption( ).setValue( sText );
				try
				{
					rr = computePolygon( xs,
							iLabelLocation,
							la,
							x,
							y,
							fontHeight );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							uiex );
				}

				if ( i == 0 && bLabelWithinAxes )
				{
					Rectangle rect = rr.getBounds( );
					if ( iLabelLocation == ABOVE || iLabelLocation == BELOW )
					{
						rr.shiftXVertices( rect.getWidth( ) / 2 );
					}
					else
					{
						rr.shiftYVertices( -rect.getHeight( ) / 2 );
					}
				}

				Point p = rr.getPoint( iPointToCheck );

				if ( isAxisLabelStaggered( ) && isTickLabelStaggered( i ) )
				{
					if ( rrPrev2 != null
							&& ( rrPrev2.contains( p )
									|| rrPrev2.getPoint( iPointToCheck )
											.equals( p ) || ChartUtil.intersects( rr,
									rrPrev2 ) ) )
					{
						return false;
					}
					rrPrev2 = rr;
				}
				else
				{
					if ( rrPrev != null
							&& ( rrPrev.contains( p ) || rrPrev.getPoint( iPointToCheck )
									.equals( p ) ) )
					{
						return false;
					}
					rrPrev = rr;
				}
				cdt = cdtAxisValue.forward( iUnit, iStep * ( i + 1 ) ); // ALWAYS
				// W.R.T
				// START
				// VALUE
			}
		}
		return true;
	}

	private CateLabVisTester createCateLabVisTester( IDisplayServer xs,
			Label la, int iLabelLocation )
	{
		// compute visiblility for category labels
		final double dAngleInDegrees = la.getCaption( )
				.getFont( )
				.getRotation( );
		int iNewPointToCheck = 0, iPrevPointToCheck = 0;

		/*
		 * Rectangle points are layed out like this
		 * 
		 * 0 1 NextLabel 3 2
		 * 
		 * 0 1 0 1 PreviousLabel NextLabel 3 2 3 2
		 */
		boolean isNegativeRotation = ( dAngleInDegrees < 0 && dAngleInDegrees > -90 );
		switch ( iLabelLocation )
		{
			case ABOVE :
				iNewPointToCheck = isNegativeRotation ? 3 : 0;
				iPrevPointToCheck = isNegativeRotation ? 1 : 3;
				break;
			case BELOW :
				iNewPointToCheck = isNegativeRotation ? ( iScaleDirection == BACKWARD ? 1
						: 3 )
						: ( iScaleDirection == BACKWARD ? 2 : 0 );
				iPrevPointToCheck = isNegativeRotation ? ( iScaleDirection == BACKWARD ? 2
						: 0 )
						: ( iScaleDirection == BACKWARD ? 0 : 2 );
				break;
			case LEFT :
				iNewPointToCheck = iScaleDirection == FORWARD ? 1 : 2;
				iPrevPointToCheck = iScaleDirection == FORWARD ? 2 : 1;
				break;
			case RIGHT :
				iNewPointToCheck = iScaleDirection == FORWARD ? 0 : 3;
				iPrevPointToCheck = iScaleDirection == FORWARD ? 3 : 0;
				break;
		}

		return new CateLabVisTester( iLabelLocation,
				iNewPointToCheck,
				iPrevPointToCheck,
				la,
				xs );
	}


	/**
	 * Calculates visibility for axis labels.
	 * 
	 * @param xs
	 * @param la
	 * @param iLabelLocation
	 * @return
	 * @throws ChartException
	 */
	final protected void checkTickLabelsVisibility( IDisplayServer xs,
			Label la, int iLabelLocation ) throws ChartException
	{
		hmComputedLabelText = new HashMap<Integer, String>( );

		boolean vis = la.isSetVisible( ) && la.isVisible( );
		if ( !vis && rtc.getSharedScale( ) != null )
		{
			// In shared scale case, treat plot chart with invisible labels has
			// axis labels, so axis chart can have the same scale with plot
			// chart
			vis = true;
		}

		// initialize stagger state.
		boolean staggerEnabled = isAxisLabelStaggered( );
		this.staggeredHelper = StaggeredHelper.createInstance( staggerEnabled,
				atcTickCoordinates.size( ),
				iLabelShowingInterval );

		this.labelVisHelper = LabelVisibleHelper.createInstance( vis,
				isCategoryScale( ),
				atcTickCoordinates.size( ),
				iLabelShowingInterval );

		// all non-visible label, skip checking.
		if ( !vis || !isCategoryScale( ) )
		{
			return;
		}

		// compute visiblility for category labels
		DataSetIterator dsi = getData( );
		dsi.reset( );

		final int iDateTimeUnit = ( iType == IConstants.DATE_TIME ) ? CDateTime.computeUnit( dsi )
				: IConstants.UNDEFINED;

		dsi.reset( );

		CateLabVisTester tester = this.createCateLabVisTester( xs,
				la,
				iLabelLocation );
		EllipsisHelper eHelper = new EllipsisHelper( tester, la.getEllipsis( ) );

		int start_id = isTickBetweenCategories( ) ? 0 : 1;
		RotatedRectangle rrPrev[] = new RotatedRectangle[2];

		double dStep = Math.abs( atcTickCoordinates.getStep( ) * dZoomFactor );
		int indexStep = dStep > 1 ? 1 : (int) ( 1d / dStep );

		for ( int i = start_id; i < atcTickCoordinates.size( ) - 1; i += indexStep )
		{
			Object oValue = null;

			if ( dsi.hasNext( ) )
			{
				oValue = dsi.next( );
			}

			// only check visible labels.
			if ( labelVisHelper.shouldTickLabelVisible( i ) )
			{
				double x = 0, y = 0;
				String sText = formatCategoryValue( iType,
						oValue,
						iDateTimeUnit );

				if ( iLabelLocation == ABOVE || iLabelLocation == BELOW )
				{
					x = this.atcTickCoordinates.getCoordinate( i )
							* dZoomFactor;
				}
				else if ( iLabelLocation == LEFT || iLabelLocation == RIGHT )
				{
					y = this.atcTickCoordinates.getCoordinate( i )
							* dZoomFactor;
				}

				la.getCaption( ).setValue( sText );
				RotatedRectangle rrCurr = null;

				int arrayIndex = isAxisLabelStaggered( )
						&& isTickLabelStaggered( i ) ? 1 : 0;

				boolean bVis;
				if ( rrPrev[arrayIndex] == null )
				{
					// Always show the first label.
					rrCurr = computePolygon( xs, iLabelLocation, la, x, y );
					bVis = true;
				}
				else
				{
					tester.setFPara( rrPrev[arrayIndex], x, y );
					bVis = eHelper.checkLabelEllipsis( sText, null );
					rrCurr = tester.getCurrentRR( );
				}

				if ( bVis )
				{
					labelVisHelper.addVisible( i );
					rrPrev[arrayIndex] = rrCurr;
					hmComputedLabelText.put( i, la.getCaption( ).getValue( ) );
				}
			}
		}

	}

	private class CateLabVisTester implements
			EllipsisHelper.ITester
	{

		private RotatedRectangle rrPrev;
		private RotatedRectangle rrCurr;
		private int iLabelLocation;
		private double x;
		private double y;
		private int iNewPointToCheck;
		private int iPrevPointToCheck;
		private Label la;
		private IDisplayServer xs;
		private Double fontHeight = null;

		CateLabVisTester( int iLabelLocation, int iNewPointToCheck,
				int iPrevPointToCheck, Label la, IDisplayServer xs )
		{
			this.iLabelLocation = iLabelLocation;
			this.iNewPointToCheck = iNewPointToCheck;
			this.iPrevPointToCheck = iPrevPointToCheck;
			this.la = la;
			this.xs = xs;
			this.fontHeight = Methods.computeFontHeight( xs, la );
		}

		private void setFPara( RotatedRectangle rrPrev, double x, double y )
		{
			this.rrPrev = rrPrev;
			this.x = x;
			this.y = y;
		}

		private RotatedRectangle getCurrentRR( )
		{
			return rrCurr;
		}

		public boolean testLabelVisible( String sText, Object oPara )
				throws ChartException
		{
			la.getCaption( ).setValue( sText );

			Point previousPoint = rrPrev.getPoint( iPrevPointToCheck );
			// quick check for false (fast)
			if ( quickCheckVisibility( iLabelLocation, previousPoint, x, y ) )
			{
				// extensive check (expensive)
				rrCurr = computePolygon( xs,
						iLabelLocation,
						la,
						x,
						y,
						fontHeight );
				Point p = rrCurr.getPoint( iNewPointToCheck );

				boolean visible = !( rrPrev.contains( p ) || ChartUtil.intersects( rrCurr,
						rrPrev ) );

				if ( visible )
				{
					return true;
				}
			}
			return false;
		}

		public double getHeight( )
		{
			// not implemented
			return 0;
		}

		public double getWidth( )
		{
			// not implemented
			return 0;
		}
	}

	protected boolean quickCheckVisibility( int iLabelLocation,
			Point previousPoint, double x, double y )
	{

		// quick check first (fast)
		if ( iLabelLocation == ABOVE || iLabelLocation == BELOW )
		{
			if ( ( iScaleDirection == BACKWARD && previousPoint.getX( ) < x )
					|| ( iScaleDirection != BACKWARD && previousPoint.getX( ) > x ) )
			{
				return false;
			}
		}
		else if ( iLabelLocation == LEFT || iLabelLocation == RIGHT )
		{

			if ( ( iScaleDirection == FORWARD && previousPoint.getY( ) > y )
					|| ( iScaleDirection != FORWARD && previousPoint.getY( ) < y ) )
			{
				return false;
			}
		}
		return true;

	}

	/**
	 * 
	 */
	final void resetShifts( )
	{
		dStartShift = 0;
		dEndShift = 0;
	}

	/**
	 * 
	 * @return
	 */
	public final double getStart( )
	{
		return dStart;
	}

	/**
	 * 
	 * @return
	 */
	public final double getEnd( )
	{
		return dEnd;
	}

	/**
	 * 
	 * @return
	 */
	final double getStartShift( )
	{
		return dStartShift;
	}

	/**
	 * 
	 * @return
	 */
	final double getEndShift( )
	{
		return dEndShift;
	}

	/**
	 * 
	 * @param xs
	 * @param ax
	 * @param dsi
	 * @param iType
	 * @param dStart
	 * @param dEnd
	 * @param scModel
	 * @param fs
	 * @param rtc
	 * @param direction
	 * @param zoomFactor
	 *            1 is default factor
	 * @param iMarginPercent
	 *            the percentage of margin area for display some charts, such as
	 *            bubble. 0 means no margin
	 * @return AutoScale instance
	 * @throws ChartException
	 */
	static final AutoScale computeScale( IDisplayServer xs, OneAxis ax,
			DataSetIterator dsi, int iType, double dStart, double dEnd,
			Scale scModel, FormatSpecifier fs, RunTimeContext rtc,
			int direction, double zoomFactor, int iMarginPercent )
			throws ChartException
	{
		return computeScale( xs,
				ax,
				dsi,
				iType,
				dStart,
				dEnd,
				scModel,
				null,
				fs,
				rtc,
				direction,
				zoomFactor,
				iMarginPercent );

	}

	/**
	 * 
	 * @param xs
	 * @param ax
	 * @param dsi
	 * @param iType
	 * @param dStart
	 * @param dEnd
	 * @param scModel
	 * @param axisOrigin
	 * @param fs
	 * @param rtc
	 * @param direction
	 * @param zoomFactor
	 *            1 is default factor
	 * @param iMarginPercent
	 *            the percentage of margin area for display some charts, such as
	 *            bubble. 0 means no margin
	 * @return AutoScale instance
	 * @throws ChartException
	 */
	static final AutoScale computeScale( IDisplayServer xs, OneAxis ax,
			DataSetIterator dsi, int iType, double dStart, double dEnd,
			Scale scModel, AxisOrigin axisOrigin, FormatSpecifier fs,
			RunTimeContext rtc, int direction, double zoomFactor,
			int iMarginPercent ) throws ChartException
	{
		final Label la = ax.getLabel( );
		final int iLabelLocation = ax.getLabelPosition( );
		final int iOrientation = ax.getOrientation( );
		DataElement oMinimum = scModel.getMin( );
		DataElement oMaximum = scModel.getMax( );
		final Double oStep = scModel.isSetStep( ) ? new Double( scModel.getStep( ) )
				: null;
		final Integer oStepNumber = scModel.isSetStepNumber( ) ? Integer.valueOf( scModel.getStepNumber( ) )
				: null;

		AutoScale sc = null;
		AutoScale scCloned = null;
		final Object oMinValue, oMaxValue;

		final boolean bIsPercent = ax.getModelAxis( ).isPercent( );

		// if factor is set
		// add the factor logic separately
		if ( scModel.isSetFactor( )
				&& ( iType & LINEAR ) == LINEAR && !ax.isCategoryScale( ) )
		{
			// translate from value/point to value/pixel
			double factor = scModel.getFactor( ) * 72 / xs.getDpiResolution( );

			Object oValue;
			double dValue, dMinValue = Double.MAX_VALUE, dMaxValue = -Double.MAX_VALUE;
			dsi.reset( );
			double dPrecision = 0;
			while ( dsi.hasNext( ) )
			{
				oValue = dsi.next( );
				if ( oValue == null ) // NULL VALUE CHECK
				{
					continue;
				}
				dValue = ( (Double) oValue ).doubleValue( );
				if ( dValue < dMinValue )
					dMinValue = dValue;
				if ( dValue > dMaxValue )
					dMaxValue = dValue;
				dPrecision = getPrecision( dPrecision,
						dValue,
						fs,
						rtc.getULocale( ),
						bIsPercent );
			}
			
			// modify min and max to include zero point
			if ( dMinValue * dMaxValue > 0 )
			{
				if ( dMinValue > 0)
				{
					dMinValue = 0d;
				}
				else
				{
					dMaxValue = 0d;
				}
			}

			// provide max value to compute a nice step size
			double length = Math.abs( dEnd - dStart );
			double valueLength = length * factor;
			dMaxValue = dMinValue + valueLength;

			double dStep = 1;

			double dDelta = dMaxValue - dMinValue;
			if ( dDelta == 0 ) // Min == Max
			{
				dStep = dPrecision;
			}
			else
			{
				dStep = Math.floor( Math.log( dDelta ) / LOG_10 );
				dStep = Math.pow( 10, dStep );
				// The automatic step should never be more precise than the
				// data itself
				if ( dStep < dPrecision )
				{
					dStep = dPrecision;
				}
			}
			sc = new AutoScale( iType, new Double( 0 ), new Double( 0 ) );
			sc.setStep( new Double( dStep ) );
			sc.bStepFixed = true;
			sc.oStepNumber = oStepNumber;
			sc.setData( dsi );
			sc.setDirection( direction );
			sc.fs = fs; // FORMAT SPECIFIER
			sc.rtc = rtc; // LOCALE
			sc.bAxisLabelStaggered = ax.isAxisLabelStaggered( );
			sc.iLabelShowingInterval = ax.getLableShowingInterval( );
			sc.bTickBetweenCategories = ax.isTickBwtweenCategories( );
			sc.dZoomFactor = zoomFactor;
			sc.dPrecision = dPrecision;
			sc.iMarginPercent = iMarginPercent;
			sc.bExpandMinMax = scModel.isAutoExpand( );
			sc.bAlignZero = ax.getModelAxis( ).isAligned( );

			// OVERRIDE STEP IF SPECIFIED
			// ignore step number if factor is set
			setStepToScale( sc, oStep, null, rtc );

			oMinValue = new Double( dMinValue );
			oMaxValue = new Double( dMaxValue );
			sc.setMinimum( oMinValue );
			sc.setMaximum( oMaxValue );

			sc.setFactor( factor );

			sc.computeTicks( xs,
					la,
					iLabelLocation,
					iOrientation,
					dStart,
					dEnd,
					false,
					null );

			sc.setData( dsi );
			return sc;

		}

		// the following code didn't change in factor enhancement:210913
		if ( ( iType & TEXT ) == TEXT || ax.isCategoryScale( ) )
		{
			sc = new AutoScale( iType );
			sc.fs = fs;
			sc.rtc = rtc;
			sc.bCategoryScale = true;
			sc.bAxisLabelStaggered = ax.isAxisLabelStaggered( );
			sc.iLabelShowingInterval = ax.getLableShowingInterval( );
			sc.bTickBetweenCategories = ax.isTickBwtweenCategories( );
			sc.dZoomFactor = zoomFactor;
			sc.iMarginPercent = iMarginPercent;
			sc.bExpandMinMax = scModel.isAutoExpand( );
			sc.bAlignZero = ax.getModelAxis( ).isSetAligned( );
			sc.setData( dsi );
			sc.setDirection( direction );
			sc.computeTicks( xs,
					ax.getLabel( ),
					iLabelLocation,
					iOrientation,
					dStart,
					dEnd,
					false,
					null );

			// To initialize final fields
			oMinValue = null;
			oMaxValue = null;
		}
		else if ( ( iType & LINEAR ) == LINEAR )
		{
			Object oValue;
			double dValue, dMinValue = Double.MAX_VALUE, dMaxValue = -Double.MAX_VALUE;
			dsi.reset( );
			double dPrecision = 0;
			while ( dsi.hasNext( ) )
			{
				oValue = dsi.next( );
				if ( oValue == null ) // NULL VALUE CHECK
				{
					continue;
				}
				dValue = ( (Double) oValue ).doubleValue( );
				if ( dValue < dMinValue )
					dMinValue = dValue;
				if ( dValue > dMaxValue )
					dMaxValue = dValue;
				dPrecision = getPrecision( dPrecision,
						dValue,
						fs,
						rtc.getULocale( ),
						bIsPercent );
			}

			if ( axisOrigin != null
					&& axisOrigin.getType( )
							.equals( IntersectionType.VALUE_LITERAL )
					&& axisOrigin.getValue( ) instanceof NumberDataElement )
			{
				double origin = asDouble( axisOrigin.getValue( ) ).doubleValue( );
				if ( oMinimum == null && origin < dMinValue )
				{
					oMinimum = axisOrigin.getValue( );
				}
				if ( oMaximum == null && origin > dMaxValue )
				{
					oMaximum = axisOrigin.getValue( );
				}
			}
			final double dAbsMax = Math.abs( dMaxValue );
			final double dAbsMin = Math.abs( dMinValue );
			double dStep = Math.max( dAbsMax, dAbsMin );

			double dDelta = dMaxValue - dMinValue;
			if ( dDelta == 0 ) // Min == Max
			{
				dStep = dPrecision;
			}
			else
			{
				dStep = Math.floor( Math.log( dDelta ) / LOG_10 );
				dStep = Math.pow( 10, dStep );
				// The automatic step should never be more precise than the data
				// itself
				if ( dStep < dPrecision )
				{
					dStep = dPrecision;
				}

			}
			sc = new AutoScale( iType, new Double( 0 ), new Double( 0 ) );
			sc.setStep( new Double( dStep ) );
			sc.oStepNumber = oStepNumber;
			sc.setData( dsi );
			sc.setDirection( direction );
			sc.fs = fs; // FORMAT SPECIFIER
			sc.rtc = rtc; // LOCALE
			sc.bAxisLabelStaggered = ax.isAxisLabelStaggered( );
			sc.iLabelShowingInterval = ax.getLableShowingInterval( );
			sc.bTickBetweenCategories = ax.isTickBwtweenCategories( );
			sc.dZoomFactor = zoomFactor;
			sc.dPrecision = dPrecision;
			sc.iMarginPercent = iMarginPercent;
			sc.bExpandMinMax = scModel.isAutoExpand( );
			sc.bAlignZero = ax.getModelAxis( ).isSetAligned( );

			// OVERRIDE MIN OR MAX IF SPECIFIED
			setNumberMinMaxToScale( sc, oMinimum, oMaximum, rtc, ax );

			// OVERRIDE STEP IF SPECIFIED
			setStepToScale( sc, oStep, oStepNumber, rtc );

			oMinValue = new Double( dMinValue );
			oMaxValue = new Double( dMaxValue );
			sc.updateAxisMinMax( oMinValue, oMaxValue );
		}

		else if ( ( iType & LOGARITHMIC ) == LOGARITHMIC )
		{
			Object oValue;
			double dValue, dMinValue = Double.MAX_VALUE, dMaxValue = -Double.MAX_VALUE;
			if ( ( iType & PERCENT ) == PERCENT )
			{
				dMinValue = 0;
				dMaxValue = 100;
			}
			else
			{
				dsi.reset( );
				while ( dsi.hasNext( ) )
				{
					oValue = dsi.next( );
					if ( oValue == null ) // NULL VALUE CHECK
					{
						continue;
					}
					dValue = ( (Double) oValue ).doubleValue( );
					if ( dValue < dMinValue )
						dMinValue = dValue;
					if ( dValue > dMaxValue )
						dMaxValue = dValue;
				}
				if ( axisOrigin != null
						&& axisOrigin.getType( )
								.equals( IntersectionType.VALUE_LITERAL )
						&& axisOrigin.getValue( ) instanceof NumberDataElement )
				{
					double origin = asDouble( axisOrigin.getValue( ) ).doubleValue( );
					if ( oMinimum == null && origin < dMinValue )
					{
						oMinimum = axisOrigin.getValue( );
					}
					if ( oMaximum == null && origin > dMaxValue )
					{
						oMaximum = axisOrigin.getValue( );
					}
				}
				// Avoid the number that will be multiplied is zero
				if ( dMinValue == 0 )
				{
					dMinValue = dMaxValue > 0 ? 1 : -1;
				}
			}

			sc = new AutoScale( iType, new Double( 0 ), new Double( 0 ) );
			sc.setStep( new Double( 10 ) );
			sc.oStepNumber = oStepNumber;
			sc.fs = fs; // FORMAT SPECIFIER
			sc.rtc = rtc; // LOCALE
			sc.bAxisLabelStaggered = ax.isAxisLabelStaggered( );
			sc.iLabelShowingInterval = ax.getLableShowingInterval( );
			sc.bTickBetweenCategories = ax.isTickBwtweenCategories( );
			sc.dZoomFactor = zoomFactor;
			sc.iMarginPercent = iMarginPercent;
			sc.setData( dsi );
			sc.setDirection( direction );
			sc.bExpandMinMax = scModel.isAutoExpand( );
			sc.bAlignZero = ax.getModelAxis( ).isSetAligned( );

			// OVERRIDE MIN OR MAX IF SPECIFIED
			setNumberMinMaxToScale( sc, oMinimum, oMaximum, rtc, ax );

			// OVERRIDE STEP IF SPECIFIED
			setStepToScale( sc, oStep, oStepNumber, rtc );

			oMinValue = new Double( dMinValue );
			oMaxValue = new Double( dMaxValue );
			sc.updateAxisMinMax( oMinValue, oMaxValue );

			if ( ( iType & PERCENT ) == PERCENT )
			{
				sc.bStepFixed = true;
				sc.bMaximumFixed = true;
				sc.bMinimumFixed = true;

				sc.computeTicks( xs,
						ax.getLabel( ),
						iLabelLocation,
						iOrientation,
						dStart,
						dEnd,
						false,
						null );
				return sc;
			}
		}
		else if ( ( iType & DATE_TIME ) == DATE_TIME )
		{
			Calendar cValue;
			Calendar caMin = null, caMax = null;
			dsi.reset( );
			while ( dsi.hasNext( ) )
			{
				cValue = (Calendar) dsi.next( );
				if ( cValue == null ) // NULL VALUE CHECK
				{
					continue;
				}
				if ( caMin == null )
				{
					caMin = cValue;
				}
				if ( caMax == null )
				{
					caMax = cValue;
				}
				if ( cValue.before( caMin ) )
					caMin = cValue;
				else if ( cValue.after( caMax ) )
					caMax = cValue;
			}

			oMinValue = new CDateTime( caMin );
			oMaxValue = new CDateTime( caMax );
			if ( axisOrigin != null
					&& axisOrigin.getType( )
							.equals( IntersectionType.VALUE_LITERAL )
					&& axisOrigin.getValue( ) instanceof DateTimeDataElement )
			{
				CDateTime origin = asDateTime( axisOrigin.getValue( ) );
				if ( oMinimum == null && origin.before( oMinValue ) )
				{
					oMinimum = axisOrigin.getValue( );
				}
				if ( oMaximum == null && origin.after( oMaxValue ) )
				{
					oMaximum = axisOrigin.getValue( );
				}
			}

			int iUnit;
			if ( oStep != null || oStepNumber != null )
			{
				iUnit = ChartUtil.convertUnitTypeToCalendarConstant( scModel.getUnit( ) );
			}
			else
			{
				iUnit = CDateTime.getPreferredUnit( (CDateTime) oMinValue,
						(CDateTime) oMaxValue );
			}

			// Can't detect a difference, assume ms
			if ( iUnit == 0 )
				iUnit = Calendar.SECOND;

			CDateTime cdtMinAxis = ( (CDateTime) oMinValue ).backward( iUnit, 1 );
			CDateTime cdtMaxAxis = ( (CDateTime) oMaxValue ).forward( iUnit, 1 );
			cdtMinAxis.clearBelow( iUnit );
			cdtMaxAxis.clearBelow( iUnit );

			sc = new AutoScale( DATE_TIME, cdtMinAxis, cdtMaxAxis );
			sc.setStep( Integer.valueOf( 1 ) );
			sc.oStepNumber = oStepNumber;
			sc.oUnit = Integer.valueOf( iUnit );
			sc.iMinUnit = oMinValue.equals( oMaxValue ) ? getUnitId( iUnit )
					: getMinUnitId( fs, rtc );
			sc.setDirection( direction );
			sc.fs = fs; // FORMAT SPECIFIER
			sc.rtc = rtc; // LOCALE
			sc.bAxisLabelStaggered = ax.isAxisLabelStaggered( );
			sc.iLabelShowingInterval = ax.getLableShowingInterval( );
			sc.bTickBetweenCategories = ax.isTickBwtweenCategories( );
			sc.dZoomFactor = zoomFactor;
			sc.iMarginPercent = iMarginPercent;
			sc.bExpandMinMax = scModel.isAutoExpand( );
			sc.bAlignZero = ax.getModelAxis( ).isSetAligned( );

			// OVERRIDE MINIMUM IF SPECIFIED
			if ( oMinimum != null )
			{
				if ( oMinimum instanceof DateTimeDataElement )
				{
					sc.oMinimum = ( (DateTimeDataElement) oMinimum ).getValueAsCDateTime( );
					sc.oMinimumFixed = ( (DateTimeDataElement) oMinimum ).getValueAsCDateTime( );
				}
				else
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							"exception.invalid.minimum.scale.value", //$NON-NLS-1$ 
							new Object[]{
									oMinimum,
									ax.getModelAxis( ).getType( ).getName( )
							},
							Messages.getResourceBundle( rtc.getULocale( ) ) );
				}
				sc.bMinimumFixed = true;
			}

			// OVERRIDE MAXIMUM IF SPECIFIED
			if ( oMaximum != null )
			{
				if ( oMaximum instanceof DateTimeDataElement )
				{
					sc.oMaximum = ( (DateTimeDataElement) oMaximum ).getValueAsCDateTime( );
					sc.oMaximumFixed = ( (DateTimeDataElement) oMaximum ).getValueAsCDateTime( );
				}
				else
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							"exception.invalid.maximum.scale.value", //$NON-NLS-1$
							new Object[]{
									sc.oMaximum,
									ax.getModelAxis( ).getType( ).getName( )
							},
							Messages.getResourceBundle( rtc.getULocale( ) ) );
				}
				sc.bMaximumFixed = true;
			}

			// VALIDATE OVERRIDDEN MIN/MAX
			if ( sc.bMaximumFixed && sc.bMinimumFixed )
			{
				if ( ( (CDateTime) sc.oMinimum ).after( sc.oMaximum ) )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							"exception.min.largerthan.max", //$NON-NLS-1$
							new Object[]{
									sc.oMinimum, sc.oMaximum
							},
							Messages.getResourceBundle( rtc.getULocale( ) ) );
				}
			}

			// OVERRIDE STEP IF SPECIFIED
			setStepToScale( sc, oStep, oStepNumber, rtc );
			sc.updateAxisMinMax( oMinValue, oMaxValue );
		}
		else
		{
			// To initialize final fields for other axis types
			oMinValue = null;
			oMaxValue = null;
		}

		// Set if the axis label should be within axes.
		sc.bLabelWithinAxes = ax.getModelAxis( ).isLabelWithinAxes( );

		// Compute the scale of non-category axis
		if ( ( iType & TEXT ) != TEXT && !ax.isCategoryScale( ) )
		{
			sc.computeTicks( xs,
					la,
					iLabelLocation,
					iOrientation,
					dStart,
					dEnd,
					false,
					null );
			dStart = sc.dStart;
			dEnd = sc.dEnd;

			boolean bFirstFit = sc.checkFit( xs, la, iLabelLocation );
			boolean bFits = bFirstFit;
			boolean bZoomSuccess = false;

			// Add limitation to avoid infinite loop
			for ( int i = 0; bFits == bFirstFit && i < 50; i++ )
			{
				bZoomSuccess = true;
				scCloned = (AutoScale) sc.clone( );
				// DO NOT AUTO ZOOM IF STEP IS FIXED or shared scale is used
				if ( sc.bStepFixed
						|| rtc.getSharedScale( ) != null
						&& rtc.getSharedScale( ).isShared( ) )
				{
					break;
				}
				if ( bFirstFit )
				{
					if ( !bFits )
					{
						break;
					}
					bZoomSuccess = sc.zoomIn( );
				}
				else
				{
					if ( !bFits && sc.getTickCordinates( ).size( ) == 2 )
					{
						break;
					}
					bZoomSuccess = sc.zoomOut( );
				}
				if ( !bZoomSuccess )
					break;

				sc.updateAxisMinMax( oMinValue, oMaxValue );
				sc.computeTicks( xs,
						la,
						iLabelLocation,
						iOrientation,
						dStart,
						dEnd,
						false,
						null );
				bFits = sc.checkFit( xs, la, iLabelLocation );
				if ( !bFits && sc.getTickCordinates( ).size( ) == 2 )
				{
					sc = scCloned;
					break;
				}
			}

			// RESTORE TO LAST SCALE BEFORE ZOOM
			if ( scCloned != null && bFirstFit && bZoomSuccess )
			{
				sc = scCloned;
			}

			updateSharedScaleContext( rtc, iType, sc.tmpSC );
		}

		sc.setData( dsi );
		return sc;
	}

	/**
	 * Limit the significant number of digit to 15
	 */
	private static double getValidDouble( double dValue )
	{
		String sValue = dfDoulbeNormalized.format( dValue );
		double dNewValue = Double.valueOf( sValue ).doubleValue( );
		return dNewValue;
	}

	/**
	 * Determine the mininal datetime unit to limit zoom in so, that no
	 * duplicated axis labels will be created with the given FormatSpecifier.
	 * 
	 * @param fs
	 * @return
	 */
	private static int getMinUnitId( FormatSpecifier fs, RunTimeContext rtc )
			throws ChartException
	{
		int iUnit = 0;
		CDateTime cdt = new CDateTime( 7, 6, 5, 4, 3, 2 );
		String sDate = ValueFormatter.format( cdt, fs, rtc.getULocale( ), null );

		for ( int i = 0; i < iaCalendarUnits.length; i++ )
		{
			cdt.set( iaCalendarUnits[i], 1 );
			String sDatei = ValueFormatter.format( cdt,
					fs,
					rtc.getULocale( ),
					null );
			if ( !sDate.equals( sDatei ) )
			{
				iUnit = i;
				break;
			}
		}

		return iUnit;
	}

	public static int getMinUnit( CDateTime cdt ) throws ChartException
	{
		int iUnit = 0;

		for ( int i = 0; i < iaCalendarUnits.length; i++ )
		{
			if ( cdt.get( iaCalendarUnits[i] ) > 0 )
			{
				iUnit = i;
				break;
			}
		}

		return iaCalendarUnits[iUnit];
	}

	public static int getUnitId( int iUnit ) throws ChartException
	{
		int id = 0;

		for ( int i = 0; i < iaCalendarUnits.length; i++ )
		{
			if ( iaCalendarUnits[i] == iUnit )
			{
				id = i;
				break;
			}
		}

		return id;
	}

	/**
	 * Computes value precision if more precise than existing one For instance
	 * 3.4 has a precision of 0.1 and 1400 has a precision of 100. That is the
	 * position where the first significant digit appears, or in double
	 * representation, the value of the exponent
	 * 
	 * @param precision
	 * @param value
	 * @return
	 */
	protected static double getPrecision( double precision, double pValue,
			FormatSpecifier fs, ULocale locale, boolean bIsPercent )
	{
		double value = Math.abs( pValue );
		value = getValidDouble( value );
		if ( value == 0 )
		{
			if ( precision < 0 )
				return precision;
			else if ( precision >= 0 )
				return 1;
		}

		if ( precision == 0 )
		{
			if ( bIsPercent )
			{
				precision = 1;
			}
			else
			{
				// precision not initialized yet
				// use worst precision for the double value
				precision = Math.pow( 10, Math.floor( Math.log( value )
						/ Math.log( 10 ) ) );
			}
		}

		// divide number by precision. If precision good enough, it's an
		// integer
		double check = value / precision;
		int loopCounter = 0;
		while ( Math.floor( check ) != check && loopCounter < 20 )
		{
			// avoid infinite loops. It should never take more than
			// 20 loops to get the right precision
			loopCounter++;
			// increase precision until it works
			precision /= 10;
			check = value / precision;
		}
		if ( loopCounter == 20 )
			logger.log( ILogger.WARNING,
					"Autoscale precision not found for " + value );//$NON-NLS-1$

		if ( fs != null )
		{

			if ( fs instanceof NumberFormatSpecifier )
			{
				NumberFormatSpecifier ns = (NumberFormatSpecifier) fs;
				if ( ns.isSetFractionDigits( ) )
				{
					double multiplier = ns.isSetMultiplier( ) ? ns.getMultiplier( )
							: 1;
					if ( multiplier != 0 )
					{
						double formatPrecision = Math.pow( 10,
								-ns.getFractionDigits( ) )
								/ multiplier;

						if ( precision == 0 )
							precision = formatPrecision;
						else
							precision = Math.max( precision, formatPrecision );
					}
				}
			}

		}
		return precision;
	}

	/**
	 * 
	 * @param la
	 * @param iLabelLocation
	 * @param iOrientation
	 * @param dStart
	 * @param dEnd
	 * @param bConsiderStartEndLabels
	 * @param aax
	 */
	public final int computeTicks( IDisplayServer xs, Label la,
			int iLabelLocation, int iOrientation, double dStart, double dEnd,
			boolean bConsiderStartEndLabels, AllAxes aax )
			throws ChartException
	{
		return computeTicks( xs,
				la,
				iLabelLocation,
				iOrientation,
				dStart,
				dEnd,
				bConsiderStartEndLabels,
				bConsiderStartEndLabels,
				aax );
	}

	/**
	 * 
	 * @param la
	 * @param iLabelLocation
	 * @param iOrientation
	 * @param dStart
	 * @param dEnd
	 * @param bConsiderStartEndLabels
	 * @param aax
	 */
	public final int computeTicks( IDisplayServer xs, Label la,
			int iLabelLocation, int iOrientation, double dStart, double dEnd,
			boolean bConsiderStartLabel, boolean bConsiderEndLabel, AllAxes aax )
			throws ChartException
	{
		boolean bMaxIsNotIntegralMultipleOfStep = false;
		int nTicks = 0;
		double dLength = 0;
		double dTickGap = 0;
		int iDirection = ( iScaleDirection == AUTO ) ? ( ( iOrientation == HORIZONTAL ) ? FORWARD
				: BACKWARD )
				: iScaleDirection;

		if ( bConsiderStartLabel || bConsiderEndLabel )
		{
			computeAxisStartEndShifts( xs,
					la,
					iOrientation,
					iLabelLocation,
					aax );

			// If axis labels should be within axes, do not adjust start
			// position
			if ( !bLabelWithinAxes && bConsiderStartLabel )
			{
				double dNewStart = dStart + dStartShift * iDirection;
				if ( dEnd > dStart && dNewStart > dEnd - 1 )
				{
					dNewStart = dEnd - 1;
				}
				else if ( dEnd < dStart && dNewStart < dEnd + 1 )
				{
					dNewStart = dEnd + 1;
				}
				dStartShift = ( dNewStart - dStart ) / iDirection;
				dStart = dNewStart;
			}

			if ( bConsiderEndLabel )
			{
				double dNewEnd = dEnd + dEndShift * -iDirection;
				if ( dEnd > dStart && dNewEnd < dStart + 1 )
				{
					dNewEnd = dStart + 1;
				}
				else if ( dEnd < dStart && dNewEnd > dStart - 1 )
				{
					dNewEnd = dStart - 1;
				}
				dEndShift = ( dNewEnd - dEnd ) / -iDirection;
				dEnd = dNewEnd;
			}
		}

		// Update member variables
		this.dStart = dStart;
		this.dEnd = dEnd;

		if ( isSetFactor( ) )
		{
			double step = Methods.asDouble( getStep( ) ).doubleValue( );
			dTickGap = step / getFactor( );
			int stepNum = (int) ( Math.abs( dStart - dEnd ) / dTickGap );
			AxisTickCoordinates atc = new AxisTickCoordinates( stepNum + 1,
					dStart,
					dStart < dEnd ? dStart + dTickGap * stepNum : dStart
							- dTickGap * stepNum,
					dTickGap * iDirection,
					true );



			setTickCordinates( atc );
			checkTickLabelsVisibility( xs, la, iLabelLocation );
			return stepNum + 1;
		}

		nTicks = getTickCount( );
		dLength = Math.abs( dStart - dEnd );

		if ( !bCategoryScale
				&& ( iType & NUMERICAL ) == NUMERICAL
				&& ( iType & LINEAR ) == LINEAR )
		{
			double dMax = asDouble( oMaximum ).doubleValue( );
			double dMin = asDouble( oMinimum ).doubleValue( );
			double dStep = asDouble( oStep ).doubleValue( );

			bMaxIsNotIntegralMultipleOfStep = !ChartUtil.mathEqual( dMax
					/ dStep, (int) ( dMax / dStep ) );

			if ( bStepFixed && oStepNumber != null )
			{
				// Use step number
				dTickGap = dLength / ( oStepNumber.intValue( ) ) * iDirection;
			}
			else
			{
				double dStepSize = asDouble( oStep ).doubleValue( );
				dTickGap = Math.min( Math.abs( dStepSize
						/ ( dMax - dMin )
						* dLength ), dLength )
						* iDirection;
			}
		}
		else
		{
			if ( isTickBetweenCategories( ) )
			{
				dTickGap = dLength / ( nTicks - 1 ) * iDirection;
			}
			else
			{
				dTickGap = dLength / ( nTicks - 2 ) * iDirection;
			}
		}

		// Added the maximum check for the step number in fixed step case.
		// If too many steps are used in auto scale, skip it. If it's fixed
		// step, it may be caused by improper step or unit.
		if ( nTicks > TICKS_MAX && bStepFixed && !bCategoryScale )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					"exception.scale.tick.max", //$NON-NLS-1$
					Messages.getResourceBundle( rtc.getULocale( ) ) );
		}

		AxisTickCoordinates atc = new AxisTickCoordinates( nTicks,
				dStart,
				dEnd,
				dTickGap,
				!bCategoryScale || isTickBetweenCategories( ) );

		setTickCordinates( null );
		setEndPoints( dStart, dEnd );
		setTickCordinates( atc );

		if ( bStepFixed
				&& oStepNumber == null
				&& ( nTicks > 2 )
				&& bMaxIsNotIntegralMultipleOfStep )
		{
			// Step Size is fixed, Linear, Max is not integral multiple of step
			// size:
			// In this case the last label before the max value will be hided,
			// if there is not enough space
			if ( !checkFit( xs, la, iLabelLocation ) )
			{
				nTicks--;

				AxisTickCoordinates atc1 = new AxisTickCoordinates( nTicks,
						dStart,
						dEnd,
						dTickGap,
						!bCategoryScale || isTickBetweenCategories( ) );

				setTickCordinates( null );
				setTickCordinates( atc1 );
			}
		}

		// baTickLabelVisible = checkTickLabelsVisibility( xs, la,
		// iLabelLocation );
		checkTickLabelsVisibility( xs, la, iLabelLocation );

		return nTicks;
	}

	/**
	 * Returns the formatted value for given Axis type and value.
	 * 
	 * @param iType
	 * @param oValue
	 * @return
	 */
	public final String formatCategoryValue( int iType, Object oValue,
			int iDateTimeUnit )
	{
		if ( oValue == null )
		{
			return IConstants.NULL_STRING;
		}

		if ( ( iType & IConstants.TEXT ) == IConstants.TEXT ) // MOST LIKELY
		{
			if ( oValue instanceof Number )
			{
				// Bugzilla#216085 format numerical value even if in Text type
				return formatCategoryValue( IConstants.NUMERICAL,
						oValue,
						iDateTimeUnit );
			}
			return oValue.toString( );
		}
		else if ( ( iType & IConstants.DATE_TIME ) == IConstants.DATE_TIME )
		{
			final Calendar ca = (Calendar) oValue;
			IDateFormatWrapper sdf = null;
			if ( fs == null ) // ONLY COMPUTE INTERNALLY IF FORMAT SPECIFIER
			// ISN'T DEFINED
			{
				sdf = cacheDateFormat.get( iDateTimeUnit );
			}

			// ADJUST THE START POSITION
			try
			{
				return ValueFormatter.format( ca, fs, rtc.getULocale( ), sdf );
			}
			catch ( ChartException dfex )
			{
				logger.log( dfex );
				return IConstants.NULL_STRING;
			}
		}
		else if ( ( iType & IConstants.NUMERICAL ) == IConstants.NUMERICAL )
		{
			DecimalFormat df = null;
			// ONLY COMPUTE INTERNALLY IF FORMAT SPECIFIER ISN'T DEFINED
			if ( fs == null )
			{
				String pattern = ValueFormatter.getNumericPattern( ( (Number) oValue ).doubleValue( ) );
				df = cacheNumFormat.get( pattern );
			}
			try
			{
				return ValueFormatter.format( oValue, fs, rtc.getULocale( ), df );
			}
			catch ( ChartException dfex )
			{
				logger.log( dfex );
				return IConstants.NULL_STRING;
			}
		}

		return IConstants.NULL_STRING;
	}

	/**
	 * Computes the axis start/end shifts (due to start/end labels) and also
	 * takes into consideration all start/end shifts of any overlay axes in the
	 * same direction as the current scale.
	 * 
	 * @param la
	 * @param iOrientation
	 * @param iLocation
	 * @param aax
	 */
	final void computeAxisStartEndShifts( IDisplayServer xs, Label la,
			int iOrientation, int iLocation, AllAxes aax )
			throws ChartException
	{
		final double dMaxSS = ( aax != null && iOrientation == aax.getOrientation( ) ) ? aax.getMaxStartShift( )
				: 0;
		final double dMaxES = ( aax != null && iOrientation == aax.getOrientation( ) ) ? aax.getMaxEndShift( )
				: 0;

		// applied to shared scale case
		if ( ( !la.isVisible( ) ) && !bLabelWithinAxes )
		{
			dStartShift = dMaxSS;
			dEndShift = dMaxES;
			return;
		}

		if ( isCategoryScale( ) )
		{
			// COMPUTE THE BOUNDING BOXES FOR FIRST AND LAST LABEL TO ADJUST
			// START/END OF X-AXIS
			final double dUnitSize = getUnitSize( );
			final DataSetIterator dsi = getData( );
			final int iDateTimeUnit;
			BoundingBox bb = null;
			try
			{
				iDateTimeUnit = ( getType( ) == IConstants.DATE_TIME ) ? CDateTime.computeUnit( dsi )
						: IConstants.UNDEFINED;
			}
			catch ( ClassCastException e )
			{
				// Happens when data in dsi is not of DateTime format
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						"exception.dataset.non.datetime", //$NON-NLS-1$
						Messages.getResourceBundle( rtc.getULocale( ) ) );

			}

			final double rotation = la.getCaption( ).getFont( ).getRotation( );
			final boolean bCenter = rotation == 0
					|| rotation == 90
					|| rotation == -90;

			// TODO check first visible label shift.
			if ( !isTickLabelVisible( 0 ) )
			{
				dStartShift = dMaxSS;
			}
			else
			{
				// ADJUST THE START POSITION
				la.getCaption( ).setValue( formatCategoryValue( getType( ),
						dsi.first( ),
						iDateTimeUnit ) );
				try
				{
					bb = computeBox( xs, iLocation, la, 0, 0 );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							uiex );
				}

				if ( iOrientation == VERTICAL ) // VERTICAL AXIS
				{
					if ( bCenter )
					{
						dStartShift = Math.max( dMaxSS,
								( dUnitSize > bb.getHeight( ) ) ? 0
										: ( bb.getHeight( ) - dUnitSize ) / 2 );
					}
					else if ( iScaleDirection == FORWARD )
					{
						dStartShift = Math.max( dMaxSS, bb.getHotPoint( )
								- dUnitSize
								/ 2 );
					}
					else
					{
						dStartShift = Math.max( dMaxSS, bb.getHeight( )
								- bb.getHotPoint( )
								- dUnitSize
								/ 2 );
					}
				}
				else if ( iOrientation == HORIZONTAL ) // HORIZONTAL AXIS
				{
					if ( bCenter )
					{
						dStartShift = Math.max( dMaxSS,
								( dUnitSize > bb.getWidth( ) ) ? 0
										: ( bb.getWidth( ) - dUnitSize ) / 2 );

					}
					else if ( iScaleDirection == BACKWARD )
					{
						dStartShift = Math.max( dMaxSS, bb.getWidth( )
								- bb.getHotPoint( )
								- dUnitSize
								/ 2 );
					}
					else
					{
						dStartShift = Math.max( dMaxSS, bb.getHotPoint( )
								- dUnitSize
								/ 2 );
					}
				}
			}

			// TODO check last visible label shift.
			if ( !isTickLabelVisible( dsi.size( ) - 1 ) )
			{
				dEndShift = dMaxES;
			}
			else
			{
				// ADJUST THE END POSITION
				la.getCaption( ).setValue( formatCategoryValue( getType( ),
						dsi.last( ),
						iDateTimeUnit ) );
				try
				{
					bb = computeBox( xs, iLocation, la, 0, dEnd );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							uiex );
				}
				if ( iOrientation == VERTICAL ) // VERTICAL AXIS
				{
					if ( bCenter )
					{
						dEndShift = Math.max( dMaxES,
								( dUnitSize > bb.getHeight( ) ) ? 0
										: ( bb.getHeight( ) - dUnitSize ) / 2 );
					}
					else if ( iScaleDirection == FORWARD )
					{
						dEndShift = Math.max( dMaxES, bb.getHeight( )
								- bb.getHotPoint( )
								- dUnitSize
								/ 2 );
					}
					else
					{
						dEndShift = Math.max( dMaxES, bb.getHotPoint( )
								- dUnitSize
								/ 2 );
					}
				}
				else if ( iOrientation == HORIZONTAL ) // HORIZONTAL AXIS
				{
					if ( bCenter )
					{
						dEndShift = Math.max( dMaxES,
								( dUnitSize > bb.getWidth( ) ) ? 0
										: ( bb.getWidth( ) - dUnitSize ) / 2 );
					}
					else if ( iScaleDirection == BACKWARD )
					{
						dEndShift = Math.max( dMaxES, bb.getHotPoint( )
								- dUnitSize
								/ 2 );
					}
					else
					{
						dEndShift = Math.max( dMaxES, bb.getWidth( )
								- bb.getHotPoint( )
								- dUnitSize
								/ 2 );
					}
				}
			}

		}
		else if ( ( iType & NUMERICAL ) == NUMERICAL )
		{
			if ( ( iType & LINEAR ) == LINEAR )
			{
				// ADJUST THE START POSITION
				DecimalFormat df = null;
				if ( fs == null ) // ONLY COMPUTE INTERNALLY IF FORMAT
				// SPECIFIER
				// ISN'T DEFINED
				{
					df = new DecimalFormat( getNumericPattern( ) );
				}
				String sValue = null;
				try
				{
					sValue = ValueFormatter.format( getMinimum( ),
							fs,
							rtc.getULocale( ),
							df );
				}
				catch ( ChartException dfex )
				{
					logger.log( dfex );
					sValue = IConstants.NULL_STRING;
				}
				la.getCaption( ).setValue( sValue );
				BoundingBox bb = null;
				try
				{
					bb = computeBox( xs, iLocation, la, 0, 0 );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							uiex );
				}
				if ( iOrientation == VERTICAL ) // VERTICAL AXIS
				{
					dStartShift = Math.max( dMaxSS,
							( bb.getHeight( ) - bb.getHotPoint( ) ) );
				}
				else if ( iOrientation == HORIZONTAL )
				{
					dStartShift = Math.max( dMaxSS, bb.getHotPoint( ) );
				}

				// ADJUST THE END POSITION
				try
				{
					sValue = ValueFormatter.format( getMaximum( ),
							fs,
							rtc.getULocale( ),
							df );
				}
				catch ( ChartException dfex )
				{
					logger.log( dfex );
					sValue = IConstants.NULL_STRING;
				}
				la.getCaption( ).setValue( sValue );
				try
				{
					bb = computeBox( xs, iLocation, la, 0, 0 );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							uiex );
				}

				if ( iOrientation == VERTICAL ) // VERTICAL AXIS
				{
					dEndShift = Math.max( dMaxES, bb.getHotPoint( ) );
				}
				else if ( iOrientation == HORIZONTAL )
				{
					dEndShift = Math.max( dMaxES,
							( bb.getWidth( ) - bb.getHotPoint( ) ) );
				}
			}
			else if ( ( iType & LOGARITHMIC ) == LOGARITHMIC )
			{
				// ADJUST THE START POSITION
				final double dMinimum = asDouble( getMinimum( ) ).doubleValue( );
				DecimalFormat df = null;
				if ( fs == null )
				// ONLY COMPUTE INTERNALLY IF FORMAT
				// SPECIFIER ISN'T DEFINED
				{
					df = new DecimalFormat( ValueFormatter.getNumericPattern( dMinimum ) );
				}
				String sValue = null;
				try
				{
					sValue = ValueFormatter.format( getMinimum( ),
							fs,
							rtc.getULocale( ),
							df );
				}
				catch ( ChartException dfex )
				{
					logger.log( dfex );
					sValue = IConstants.NULL_STRING;
				}
				la.getCaption( ).setValue( sValue );
				BoundingBox bb = null;
				try
				{
					bb = computeBox( xs, iLocation, la, 0, 0 );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							uiex );
				}
				if ( iOrientation == VERTICAL ) // VERTICAL AXIS
				{
					dStartShift = Math.max( dMaxSS,
							( bb.getHeight( ) - bb.getHotPoint( ) ) );
				}
				else if ( iOrientation == HORIZONTAL )
				{
					dStartShift = Math.max( dMaxSS, bb.getHotPoint( ) );
				}

				// ADJUST THE END POSITION
				final double dMaximum = asDouble( getMaximum( ) ).doubleValue( );
				if ( fs == null )
				// ONLY COMPUTE INTERNALLY (DIFFERENT FROM
				// MINIMUM) IF FORMAT SPECIFIER ISN'T DEFINED
				{
					df = new DecimalFormat( ValueFormatter.getNumericPattern( dMaximum ) );
				}
				try
				{
					sValue = ValueFormatter.format( getMaximum( ),
							fs,
							rtc.getULocale( ),
							df );
				}
				catch ( ChartException dfex )
				{
					logger.log( dfex );
					sValue = IConstants.NULL_STRING;
				}
				la.getCaption( ).setValue( sValue );
				try
				{
					bb = computeBox( xs, iLocation, la, 0, 0 );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							uiex );
				}

				if ( iOrientation == VERTICAL ) // VERTICAL AXIS
				{
					dEndShift = Math.max( dMaxES, bb.getHotPoint( ) );
				}
				else if ( iOrientation == HORIZONTAL )
				{
					dEndShift = Math.max( dMaxES,
							( bb.getWidth( ) - bb.getHotPoint( ) ) );
				}
			}
		}

		else if ( getType( ) == DATE_TIME )
		{
			// COMPUTE THE BOUNDING BOXES FOR FIRST AND LAST LABEL TO ADJUST
			// START/END OF X-AXIS
			CDateTime cdt = asDateTime( getMinimum( ) );
			final int iUnit = asInteger( oUnit );
			IDateFormatWrapper sdf = null;
			String sText = null;

			if ( fs == null ) // ONLY COMPUTE INTERNALLY IF FORMAT SPECIFIER
			// ISN'T DEFINED
			{
				sdf = DateFormatWrapperFactory.getPreferredDateFormat( iUnit,
						rtc.getULocale( ) );
			}

			// ADJUST THE START POSITION
			try
			{
				sText = ValueFormatter.format( cdt, fs, rtc.getULocale( ), sdf );
			}
			catch ( ChartException dfex )
			{
				logger.log( dfex );
				sText = IConstants.NULL_STRING;
			}
			la.getCaption( ).setValue( sText );

			BoundingBox bb = null;
			try
			{
				bb = computeBox( xs, iLocation, la, 0, 0 );
			}
			catch ( IllegalArgumentException uiex )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						uiex );
			}
			if ( iOrientation == VERTICAL ) // VERTICAL AXIS
			{
				dStartShift = Math.max( dMaxSS,
						( bb.getHeight( ) - bb.getHotPoint( ) ) );
			}
			else if ( iOrientation == HORIZONTAL )
			{
				dStartShift = Math.max( dMaxSS, bb.getHotPoint( ) );
			}

			// ADJUST THE END POSITION
			cdt = asDateTime( getMaximum( ) );
			try
			{
				sText = ValueFormatter.format( cdt, fs, rtc.getULocale( ), sdf );
			}
			catch ( ChartException dfex )
			{
				logger.log( dfex );
				sText = IConstants.NULL_STRING;
			}
			la.getCaption( ).setValue( sText );
			try
			{
				bb = computeBox( xs, iLocation, la, 0, dEnd );
			}
			catch ( IllegalArgumentException uiex )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						uiex );
			}
			if ( iOrientation == VERTICAL ) // VERTICAL AXIS
			{
				dEndShift = Math.max( dMaxES, bb.getHotPoint( ) );
			}
			else if ( iOrientation == HORIZONTAL )
			{
				dEndShift = Math.max( dMaxES,
						( bb.getWidth( ) - bb.getHotPoint( ) ) );
			}
		}
	}

	public final double computeAxisLabelThickness( IDisplayServer xs, Label la,
			int iOrientation ) throws ChartException
	{
		if ( !la.isSetVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					"exception.unset.label.visibility", //$NON-NLS-1$
					new Object[]{
						la.getCaption( ).getValue( )
					},
					Messages.getResourceBundle( rtc.getULocale( ) ) );
		}

		if ( !la.isVisible( ) )
		{
			return 0;
		}

		String sText;
		AxisTickCoordinates da = getTickCordinates( );

		if ( iOrientation == VERTICAL )
		{
			double dW, dMaxW = 0, dMaxW2 = 0;
			if ( isCategoryScale( ) )
			{
				// final DataSetIterator dsi = getData( );
				// final int iDateTimeUnit = ( getType( ) ==
				// IConstants.DATE_TIME ) ? CDateTime.computeUnit( dsi )
				// : IConstants.UNDEFINED;
				// /

				Collection<Integer> visIds = getVisibleLabelIds( );
				for ( int id : visIds )
				{
					la.getCaption( ).setValue( getComputedLabelText( id ) );
					dW = computeWidth( xs, la );

					if ( isAxisLabelStaggered( ) && isTickLabelStaggered( id ) )
					{
						dMaxW2 = Math.max( dW, dMaxW2 );
					}
					else if ( dW > dMaxW )
					{
						dMaxW = dW;
					}

				}

				// /
				// dsi.reset( );
				// int i = isTickBetweenCategories( ) ? 0 : 1;
				// while ( dsi.hasNext( ) )
				// {
				// Object oValue = dsi.next( );
				// if ( isTickLabelVisible( i ) )
				// {
				// la.getCaption( )
				// .setValue( formatCategoryValue( getType( ),
				// oValue,
				// iDateTimeUnit ) );
				// dW = computeWidth( xs, la );
				//
				// if ( isAxisLabelStaggered( )
				// && isTickLabelStaggered( i ) )
				// {
				// dMaxW2 = Math.max( dW, dMaxW2 );
				// }
				// else if ( dW > dMaxW )
				// {
				// dMaxW = dW;
				// }
				// }
				// i++;
				// }
			}
			else if ( ( getType( ) & LINEAR ) == LINEAR )
			{
				final NumberDataElement nde = NumberDataElementImpl.create( 0 );
				double dAxisValue = asDouble( getMinimum( ) ).doubleValue( );
				double dAxisStep = asDouble( getStep( ) ).doubleValue( );
				DecimalFormat df = null;
				if ( fs == null )
				{
					df = computeDecimalFormat( dAxisValue, dAxisStep );
				}
				for ( int i = 0; i < da.size( ); i++ )
				{
					nde.setValue( dAxisValue );
					try
					{
						sText = ValueFormatter.format( nde,
								fs,
								rtc.getULocale( ),
								df );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );
					dW = computeWidth( xs, la );

					if ( isAxisLabelStaggered( ) && isTickLabelStaggered( i ) )
					{
						dMaxW2 = Math.max( dW, dMaxW2 );
					}
					else if ( dW > dMaxW )
					{
						dMaxW = dW;
					}
					dAxisValue += dAxisStep;
				}
			}
			else if ( ( getType( ) & LOGARITHMIC ) == LOGARITHMIC )
			{
				final NumberDataElement nde = NumberDataElementImpl.create( 0 );
				double dAxisValue = asDouble( getMinimum( ) ).doubleValue( );
				double dAxisStep = asDouble( getStep( ) ).doubleValue( );
				DecimalFormat df = null;
				for ( int i = 0; i < da.size( ); i++ )
				{
					if ( fs == null )
					{
						df = computeDecimalFormat( dAxisValue, dAxisStep );
					}
					nde.setValue( dAxisValue );
					try
					{
						sText = ValueFormatter.format( nde,
								fs,
								rtc.getULocale( ),
								df );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );
					dW = computeWidth( xs, la );
					if ( isAxisLabelStaggered( ) && isTickLabelStaggered( i ) )
					{
						dMaxW2 = Math.max( dW, dMaxW2 );
					}
					else if ( dW > dMaxW )
					{
						dMaxW = dW;
					}
					dAxisValue *= dAxisStep;
				}
			}
			else if ( ( getType( ) & DATE_TIME ) == DATE_TIME )
			{
				CDateTime cdtAxisValue = asDateTime( getMinimum( ) );
				int iStep = asInteger( getStep( ) );
				int iUnit = asInteger( getUnit( ) );
				IDateFormatWrapper sdf = null;
				if ( fs == null )
				{
					sdf = DateFormatWrapperFactory.getPreferredDateFormat( iUnit,
							rtc.getULocale( ) );
				}
				for ( int i = 0; i < da.size( ); i++ )
				{
					try
					{
						sText = ValueFormatter.format( cdtAxisValue,
								fs,
								rtc.getULocale( ),
								sdf );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );
					dW = computeWidth( xs, la );
					if ( isAxisLabelStaggered( ) && isTickLabelStaggered( i ) )
					{
						dMaxW2 = Math.max( dW, dMaxW2 );
					}
					else if ( dW > dMaxW )
					{
						dMaxW = dW;
					}
					cdtAxisValue = cdtAxisValue.forward( iUnit, iStep );
				}
			}
			return dMaxW + dMaxW2;
		}
		else if ( iOrientation == HORIZONTAL )
		{
			double dH, dMaxH = 0, dMaxH2 = 0;
			if ( isCategoryScale( ) )
			{
				Collection<Integer> visIds = getVisibleLabelIds( );

				for ( int id : visIds )
				{
					la.getCaption( ).setValue( getComputedLabelText( id ) );
					dH = computeHeight( xs, la );
					if ( isAxisLabelStaggered( ) && isTickLabelStaggered( id ) )
					{
						dMaxH2 = Math.max( dH, dMaxH2 );
					}
					else if ( dH > dMaxH )
					{
						dMaxH = dH;
					}
				
				 }

				// final DataSetIterator dsi = getData( );
				// final int iDateTimeUnit = ( getType( ) ==
				// IConstants.DATE_TIME ) ? CDateTime.computeUnit( dsi )
				// : IConstants.UNDEFINED;
				//
				// dsi.reset( );
				// int i = isTickBetweenCategories( ) ? 0 : 1;
				// while ( dsi.hasNext( ) )
				// {
				// Object oValue = dsi.next( );
				// if ( isTickLabelVisible( i ) )
				// {
				// la.getCaption( )
				// .setValue( formatCategoryValue( getType( ),
				// oValue,
				// iDateTimeUnit ) );
				// dH = computeHeight( xs, la );
				// if ( isAxisLabelStaggered( )
				// && isTickLabelStaggered( i ) )
				// {
				// dMaxH2 = Math.max( dH, dMaxH2 );
				// }
				// else if ( dH > dMaxH )
				// {
				// dMaxH = dH;
				// }
				// }
				// i++;
				// }
			}
			else if ( ( getType( ) & LINEAR ) == LINEAR )
			{
				final NumberDataElement nde = NumberDataElementImpl.create( 0 );
				double dAxisValue = asDouble( getMinimum( ) ).doubleValue( );
				final double dAxisStep = asDouble( getStep( ) ).doubleValue( );
				DecimalFormat df = null;
				if ( fs == null )
				{
					df = computeDecimalFormat( dAxisValue, dAxisStep );
				}
				for ( int i = 0; i < da.size( ); i++ )
				{
					nde.setValue( dAxisValue );
					try
					{
						sText = ValueFormatter.format( nde,
								fs,
								rtc.getULocale( ),
								df );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );
					dH = computeHeight( xs, la );
					if ( isAxisLabelStaggered( ) && isTickLabelStaggered( i ) )
					{
						dMaxH2 = Math.max( dH, dMaxH2 );
					}
					else if ( dH > dMaxH )
					{
						dMaxH = dH;
					}
					dAxisValue += dAxisStep;
				}
			}
			else if ( ( getType( ) & LOGARITHMIC ) == LOGARITHMIC )
			{
				final NumberDataElement nde = NumberDataElementImpl.create( 0 );
				double dAxisValue = asDouble( getMinimum( ) ).doubleValue( );
				final double dAxisStep = asDouble( getStep( ) ).doubleValue( );
				DecimalFormat df = null;
				for ( int i = 0; i < da.size( ); i++ )
				{
					if ( fs == null )
					{
						df = computeDecimalFormat( dAxisValue, dAxisStep );
					}
					nde.setValue( dAxisValue );
					try
					{
						sText = ValueFormatter.format( nde,
								fs,
								rtc.getULocale( ),
								df );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );
					dH = computeHeight( xs, la );
					if ( isAxisLabelStaggered( ) && isTickLabelStaggered( i ) )
					{
						dMaxH2 = Math.max( dH, dMaxH2 );
					}
					else if ( dH > dMaxH )
					{
						dMaxH = dH;
					}
					dAxisValue *= dAxisStep;
				}
			}
			else if ( ( getType( ) & DATE_TIME ) == DATE_TIME )
			{
				CDateTime cdtAxisValue = asDateTime( getMinimum( ) );
				final int iStep = asInteger( getStep( ) );
				final int iUnit = asInteger( getUnit( ) );
				IDateFormatWrapper sdf = null;
				if ( fs == null )
				{
					sdf = DateFormatWrapperFactory.getPreferredDateFormat( iUnit,
							rtc.getULocale( ) );
				}
				for ( int i = 0; i < da.size( ); i++ )
				{
					try
					{
						sText = ValueFormatter.format( cdtAxisValue,
								fs,
								rtc.getULocale( ),
								sdf );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );
					dH = computeHeight( xs, la );
					if ( isAxisLabelStaggered( ) && isTickLabelStaggered( i ) )
					{
						dMaxH2 = Math.max( dH, dMaxH2 );
					}
					else if ( dH > dMaxH )
					{
						dMaxH = dH;
					}
					cdtAxisValue.forward( iUnit, iStep );
				}
			}
			return dMaxH + dMaxH2;
		}
		return 0;
	}

	/**
	 * @param xs
	 * @param la
	 * @param iOrientation
	 * @return
	 * @throws ChartException
	 */
	public final double computeStaggeredAxisLabelOffset( IDisplayServer xs,
			Label la, int iOrientation ) throws ChartException
	{
		if ( !la.isSetVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					"exception.unset.label.visibility", //$NON-NLS-1$
					new Object[]{
						la.getCaption( ).getValue( )
					},
					Messages.getResourceBundle( rtc.getULocale( ) ) );
		}

		if ( !la.isVisible( ) || !isAxisLabelStaggered( ) )
		{
			return 0;
		}

		String sText;
		AxisTickCoordinates da = getTickCordinates( );

		if ( iOrientation == VERTICAL )
		{
			double dW, dMaxW = 0;
			if ( isCategoryScale( ) )
			{
				final DataSetIterator dsi = getData( );
				final int iDateTimeUnit = ( getType( ) == IConstants.DATE_TIME ) ? CDateTime.computeUnit( dsi )
						: IConstants.UNDEFINED;
				dsi.reset( );
				int i = 0;
				while ( dsi.hasNext( ) )
				{
					la.getCaption( ).setValue( formatCategoryValue( getType( ),
							dsi.next( ),
							iDateTimeUnit ) );
					if ( !isTickLabelStaggered( i ) )
					{
						dW = computeWidth( xs, la );

						if ( dW > dMaxW )
						{
							dMaxW = dW;
						}
					}
					i++;
				}
			}
			else if ( ( getType( ) & LINEAR ) == LINEAR )
			{
				final NumberDataElement nde = NumberDataElementImpl.create( 0 );
				double dAxisValue = asDouble( getMinimum( ) ).doubleValue( );
				double dAxisStep = asDouble( getStep( ) ).doubleValue( );
				DecimalFormat df = null;
				if ( fs == null )
				{
					df = computeDecimalFormat( dAxisValue, dAxisStep );
				}
				for ( int i = 0; i < da.size( ); i++ )
				{
					nde.setValue( dAxisValue );
					try
					{
						sText = ValueFormatter.format( nde,
								fs,
								rtc.getULocale( ),
								df );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );

					if ( !isTickLabelStaggered( i ) )
					{
						dW = computeWidth( xs, la );

						if ( dW > dMaxW )
						{
							dMaxW = dW;
						}
					}
					dAxisValue += dAxisStep;
				}
			}
			else if ( ( getType( ) & LOGARITHMIC ) == LOGARITHMIC )
			{
				final NumberDataElement nde = NumberDataElementImpl.create( 0 );
				double dAxisValue = asDouble( getMinimum( ) ).doubleValue( );
				double dAxisStep = asDouble( getStep( ) ).doubleValue( );
				DecimalFormat df = null;
				for ( int i = 0; i < da.size( ); i++ )
				{
					if ( fs == null )
					{
						df = computeDecimalFormat( dAxisValue, dAxisStep );
					}
					nde.setValue( dAxisValue );
					try
					{
						sText = ValueFormatter.format( nde,
								fs,
								rtc.getULocale( ),
								df );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );

					if ( !isTickLabelStaggered( i ) )
					{
						dW = computeWidth( xs, la );

						if ( dW > dMaxW )
						{
							dMaxW = dW;
						}
					}
					dAxisValue *= dAxisStep;
				}
			}
			else if ( ( getType( ) & DATE_TIME ) == DATE_TIME )
			{
				CDateTime cdtAxisValue = asDateTime( getMinimum( ) );
				int iStep = asInteger( getStep( ) );
				int iUnit = asInteger( getUnit( ) );
				IDateFormatWrapper sdf = null;
				if ( fs == null )
				{
					sdf = DateFormatWrapperFactory.getPreferredDateFormat( iUnit,
							rtc.getULocale( ) );
				}
				for ( int i = 0; i < da.size( ); i++ )
				{
					try
					{
						sText = ValueFormatter.format( cdtAxisValue,
								fs,
								rtc.getULocale( ),
								sdf );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );

					if ( !isTickLabelStaggered( i ) )
					{
						dW = computeWidth( xs, la );

						if ( dW > dMaxW )
						{
							dMaxW = dW;
						}
					}
					cdtAxisValue = cdtAxisValue.forward( iUnit, iStep );
				}
			}
			return dMaxW;
		}
		else if ( iOrientation == HORIZONTAL )
		{
			double dH, dMaxH = 0;
			if ( isCategoryScale( ) )
			{
				final DataSetIterator dsi = getData( );
				final int iDateTimeUnit = ( getType( ) == IConstants.DATE_TIME ) ? CDateTime.computeUnit( dsi )
						: IConstants.UNDEFINED;

				dsi.reset( );
				int i = 0;
				while ( dsi.hasNext( ) )
				{
					la.getCaption( ).setValue( formatCategoryValue( getType( ),
							dsi.next( ),
							iDateTimeUnit ) );

					if ( !isTickLabelStaggered( i ) )
					{
						dH = computeHeight( xs, la );

						if ( dH > dMaxH )
						{
							dMaxH = dH;
						}
					}
					i++;
				}
			}
			else if ( ( getType( ) & LINEAR ) == LINEAR )
			{
				final NumberDataElement nde = NumberDataElementImpl.create( 0 );
				double dAxisValue = asDouble( getMinimum( ) ).doubleValue( );
				final double dAxisStep = asDouble( getStep( ) ).doubleValue( );
				DecimalFormat df = null;
				if ( fs == null )
				{
					df = computeDecimalFormat( dAxisValue, dAxisStep );
				}
				for ( int i = 0; i < da.size( ); i++ )
				{
					nde.setValue( dAxisValue );
					try
					{
						sText = ValueFormatter.format( nde,
								fs,
								rtc.getULocale( ),
								df );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );

					if ( !isTickLabelStaggered( i ) )
					{
						dH = computeHeight( xs, la );

						if ( dH > dMaxH )
						{
							dMaxH = dH;
						}
					}
					dAxisValue += dAxisStep;
				}
			}
			else if ( ( getType( ) & LOGARITHMIC ) == LOGARITHMIC )
			{
				final NumberDataElement nde = NumberDataElementImpl.create( 0 );
				double dAxisValue = asDouble( getMinimum( ) ).doubleValue( );
				final double dAxisStep = asDouble( getStep( ) ).doubleValue( );
				DecimalFormat df = null;
				for ( int i = 0; i < da.size( ); i++ )
				{
					if ( fs == null )
					{
						df = computeDecimalFormat( dAxisValue, dAxisStep );
					}
					nde.setValue( dAxisValue );
					try
					{
						sText = ValueFormatter.format( nde,
								fs,
								rtc.getULocale( ),
								df );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );

					if ( !isTickLabelStaggered( i ) )
					{
						dH = computeHeight( xs, la );

						if ( dH > dMaxH )
						{
							dMaxH = dH;
						}
					}
					dAxisValue *= dAxisStep;
				}
			}
			else if ( ( getType( ) & DATE_TIME ) == DATE_TIME )
			{
				CDateTime cdtAxisValue = asDateTime( getMinimum( ) );
				final int iStep = asInteger( getStep( ) );
				final int iUnit = asInteger( getUnit( ) );
				IDateFormatWrapper sdf = null;
				if ( fs == null )
				{
					sdf = DateFormatWrapperFactory.getPreferredDateFormat( iUnit,
							rtc.getULocale( ) );
				}
				for ( int i = 0; i < da.size( ); i++ )
				{
					try
					{
						sText = ValueFormatter.format( cdtAxisValue,
								fs,
								rtc.getULocale( ),
								sdf );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );

					if ( !isTickLabelStaggered( i ) )
					{
						dH = computeHeight( xs, la );

						if ( dH > dMaxH )
						{
							dMaxH = dH;
						}
					}
					cdtAxisValue.forward( iUnit, iStep );
				}
			}
			return dMaxH;
		}
		return 0;
	}

	/**
	 * @return
	 */
	public final boolean isStepFixed( )
	{
		return bStepFixed;
	}

	/**
	 * @param v
	 */
	public final void setStepFixed( boolean v )
	{
		this.bStepFixed = v;
	}

	/**
	 * @return
	 */
	public final boolean isMinimumFixed( )
	{
		return bMinimumFixed;
	}

	/**
	 * @param v
	 */
	public final void setMinimumFixed( boolean v )
	{
		this.bMinimumFixed = v;
	}

	/**
	 * @return
	 */
	public final boolean isMaximumFixed( )
	{
		return bMaximumFixed;
	}

	/**
	 * @param v
	 */
	public final void setMaximumFixed( boolean v )
	{
		this.bMaximumFixed = v;
	}

	/**
	 * Checks if axis is category style or Text type
	 * 
	 * @return
	 */
	public final boolean isCategoryScale( )
	{
		return ( iType & TEXT ) == TEXT || bCategoryScale;
	}

	/**
	 * 
	 * @param iMinorUnitsPerMajor
	 * @return
	 */
	public final double[] getMinorCoordinates( int iMinorUnitsPerMajor )
	{
		if ( atcTickCoordinates == null || iMinorUnitsPerMajor <= 0 )
		{
			return null;
		}

		final double[] da = new double[iMinorUnitsPerMajor];
		final double dUnit = getUnitSize( );
		if ( ( iType & LOGARITHMIC ) != LOGARITHMIC )
		{
			final double dEach = dUnit / iMinorUnitsPerMajor;
			for ( int i = 1; i < iMinorUnitsPerMajor; i++ )
			{
				da[i - 1] = dEach * i;
			}
		}
		else
		{
			final double dCount = iMinorUnitsPerMajor;
			final double dMax = Math.log( dCount );

			for ( int i = 0; i < iMinorUnitsPerMajor; i++ )
			{
				da[i] = ( Math.log( i + 1 ) * dUnit ) / dMax;
			}
		}
		da[iMinorUnitsPerMajor - 1] = dUnit;
		return da;
	}

	/**
	 * @return
	 */
	public final RunTimeContext getRunTimeContext( )
	{
		return rtc;
	}

	/**
	 * @param context
	 */
	public final void setRunTimeContext( RunTimeContext context )
	{
		this.rtc = context;
	}

	/**
	 * Updates AutoScale by checking min or max
	 * 
	 * @param sc
	 * @param oMinimum
	 * @param oMaximum
	 * @param rtc
	 * @param ax
	 * @throws ChartException
	 */
	public static void setNumberMinMaxToScale( AutoScale sc, Object oMinimum,
			Object oMaximum, final RunTimeContext rtc, final OneAxis ax )
			throws ChartException
	{
		// OVERRIDE MINIMUM IF SPECIFIED
		if ( oMinimum != null )
		{
			if ( oMinimum instanceof NumberDataElement )
			{
				sc.oMinimum = new Double( ( (NumberDataElement) oMinimum ).getValue( ) );
			}
			/*
			 * else if (oMinimum instanceof DateTimeDataElement) { sc.oMinimum =
			 * ((DateTimeDataElement) oMinimum).getValueAsCDateTime(); }
			 */
			else
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						"exception.invalid.minimum.scale.value", //$NON-NLS-1$
						new Object[]{
								sc.oMinimum,
								ax.getModelAxis( ).getType( ).getName( )
						},
						Messages.getResourceBundle( rtc.getULocale( ) ) );
			}
			sc.bMinimumFixed = true;
		}

		// OVERRIDE MAXIMUM IF SPECIFIED
		if ( oMaximum != null )
		{
			if ( oMaximum instanceof NumberDataElement )
			{
				sc.oMaximum = new Double( ( (NumberDataElement) oMaximum ).getValue( ) );
			}
			/*
			 * else if (oMaximum instanceof DateTimeDataElement) { sc.oMaximum =
			 * ((DateTimeDataElement) oMaximum).getValueAsCDateTime(); }
			 */
			else
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						"exception.invalid.maximum.scale.value", //$NON-NLS-1$
						new Object[]{
								sc.oMaximum,
								ax.getModelAxis( ).getType( ).getName( )
						},
						Messages.getResourceBundle( rtc.getULocale( ) ) );
			}
			sc.bMaximumFixed = true;
		}

		// VALIDATE OVERRIDDEN MIN/MAX
		if ( sc.bMaximumFixed && sc.bMinimumFixed )
		{
			if ( ( (Double) sc.oMinimum ).doubleValue( ) > ( (Double) sc.oMaximum ).doubleValue( ) )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						"exception.min.largerthan.max", //$NON-NLS-1$ 
						new Object[]{
								sc.oMinimum, sc.oMaximum
						},
						Messages.getResourceBundle( rtc.getULocale( ) ) );
			}
		}
	}

	/**
	 * Updates AutoScale by checking step size and step number
	 * 
	 * @param sc
	 * @param oStep
	 * @param oStepNumber
	 * @param rtc
	 * @throws ChartException
	 */
	public static void setStepToScale( AutoScale sc, Object oStep,
			Integer oStepNumber, RunTimeContext rtc ) throws ChartException
	{
		// OVERRIDE STEP IF SPECIFIED
		if ( oStep != null )
		{
			sc.setStep( oStep );
			sc.bStepFixed = true;

			// VALIDATE OVERRIDDEN STEP
			if ( ( (Double) sc.oStep ).doubleValue( ) <= 0 )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						"exception.invalid.step.size", //$NON-NLS-1$
						new Object[]{
							oStep
						},
						Messages.getResourceBundle( rtc.getULocale( ) ) );
			}
		}

		if ( oStepNumber != null )
		{
			sc.oStepNumber = oStepNumber;
			sc.bStepFixed = true;

			// VALIDATE OVERRIDDEN STEP
			if ( sc.oStepNumber.intValue( ) < 1 )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						"exception.invalid.step.number", //$NON-NLS-1$
						new Object[]{
							oStepNumber
						},
						Messages.getResourceBundle( rtc.getULocale( ) ) );
			}
		}
	}

	/**
	 * Computes the default DecimalFormat pattern for axis according to axis
	 * value and scale steps.
	 * 
	 * @param dAxisValue
	 *            axis value
	 * @param dAxisStep
	 *            scale step
	 * @return default format pattern
	 */
	public final DecimalFormat computeDecimalFormat( double dAxisValue,
			double dAxisStep )
	{
		// Use a more precise pattern
		String valuePattern = ValueFormatter.getNumericPattern( dAxisValue );
		String stepPattern = ValueFormatter.getNumericPattern( dAxisStep );

		boolean bValuePrecise = ChartUtil.checkDoublePrecise( dAxisValue );
		boolean bStepPrecise = ChartUtil.checkDoublePrecise( dAxisStep );

		// See Bugzilla#185883
		if ( bValuePrecise )
		{
			if ( bStepPrecise )
			{
				// If they are both double-precise, use the more precise one
				if ( valuePattern.length( ) < stepPattern.length( ) )
				{
					return cacheNumFormat.get( stepPattern );
				}
			}
		}
		else
		{
			if ( bStepPrecise )
			{
				return cacheNumFormat.get( stepPattern );
			}
			// If they are neither double-precise, use the default value
		}
		return cacheNumFormat.get( valuePattern );
	}

	private static void updateSharedScaleContext( RunTimeContext rtc,
			int iType, ScaleContext sct )
	{
		if ( rtc.getSharedScale( ) != null && !rtc.getSharedScale( ).isShared( ) )
		{
			rtc.getSharedScale( ).setScaleContext( sct );
		}
	}

	/**
	 * Helper class for Tick Label Visiblility
	 */
	private static abstract class LabelVisibleHelper
	{
		protected Set<Integer> idsVis = new HashSet<Integer>( );
		protected final int iTickCount;
		protected final int iShowIterval;
		protected final CommonRule commonRule;

		private LabelVisibleHelper( int iTickCount, int iShowIterval )
		{
			this.iTickCount = iTickCount;
			this.iShowIterval = iShowIterval;
			this.commonRule = iShowIterval < 2 ? CommonRule.SHOW_INTERVAL_1
					: CommonRule.SHOW_INTEVAL_2UP;
		}

		public abstract boolean isTickLabelVisible( int index );

		public boolean shouldTickLabelVisible( int index )
		{
			return commonRule.shouldVisible( index, iTickCount, iShowIterval );
		}

		public void addVisible( int index )
		{
			idsVis.add( index );
		}

		public static LabelVisibleHelper createInstance( boolean bLabelVisible,
				boolean bCategory, final int iTickCount, final int iShowIterval )
		{
			if ( !bLabelVisible )
			{
				return new LabelVisibleHelper( iTickCount, iShowIterval ) {

					// case label invisible
					@Override
					public boolean isTickLabelVisible( int index )
					{
						return false;
					}

					@Override
					public boolean shouldTickLabelVisible( int index )
					{
						return false;
					}
				};
			}
			else if ( bCategory )
			{
				return new LabelVisibleHelper( iTickCount, iShowIterval ) {

					// case Category
					@Override
					public boolean isTickLabelVisible( int index )
					{
						return idsVis.contains( index );
					}
				};

			}
			else
			{
				return new LabelVisibleHelper( iTickCount, iShowIterval ) {

					@Override
					public boolean isTickLabelVisible( int index )
					{
						return commonRule.shouldVisible( index,
								iTickCount,
								iShowIterval );
					}
				};
			}

		}

		private static enum CommonRule {
			SHOW_INTERVAL_1 {

				@Override
				public boolean shouldVisible( int index, int iTickCount,
						int iShowIterval )
				{
					return !isIndexOutOfBound( index, iTickCount );
				}

			},
			SHOW_INTEVAL_2UP {

				@Override
				public boolean shouldVisible( int index, int iTickCount,
						int iShowIterval )
				{
					return !isIndexOutOfBound( index, iTickCount )
							&& ( index % iShowIterval == 0 );
				}

			};

			public abstract boolean shouldVisible( int index, int iTickCount,
					int iShowIterval );

			private static boolean isIndexOutOfBound( int index, int iTickCount )
			{
				return index < 0 || index > iTickCount - 1;
			}
		}

	}

	/**
	 * Helper class for Tick Label Stagger. StaggeredHelper
	 */
	private static abstract class StaggeredHelper
	{

		private StaggeredHelper( )
		{
		}

		public static StaggeredHelper createInstance(
				final boolean staggerEnabled, final int iTickCount,
				final int iLabelShowingInterval )
		{
			if ( !staggerEnabled )
			{
				return new StaggeredHelper( ) {

					@Override
					public boolean isTickLabelStaggered( int index )
					{
						// default case, stagger disabled
						return false;
					}
				};
			}
			else
			{
				if ( iLabelShowingInterval < 2 )
				{
					return new StaggeredHelper( ) {

						@Override
						public boolean isTickLabelStaggered( int index )
						{
							// case stagger enabled, iLabelShowingInterval==1
							if ( isIndexOutOfBound( index, iTickCount ) )
							{
								return false;
							}

							return index % 2 == 1;
						}

					};
				}
				else
				{
					return new StaggeredHelper( ) {

						@Override
						public boolean isTickLabelStaggered( int index )
						{
							// case stagger enabled, iLabelShowingInterval>1
							if ( isIndexOutOfBound( index, iTickCount ) )
							{
								return false;
							}

							return ( index % iLabelShowingInterval == 0 )
									&& ( ( index / iLabelShowingInterval ) % 2 == 1 );
						}

					};
				}
			}

		}

		private static boolean isIndexOutOfBound( int index, int iTickCount )
		{
			return index < 0 || index > iTickCount - 1;
		}

		public abstract boolean isTickLabelStaggered( int index );
	}


}