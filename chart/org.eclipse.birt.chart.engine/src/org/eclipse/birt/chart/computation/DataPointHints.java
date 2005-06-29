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

import java.util.ResourceBundle;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.emf.common.util.EList;

/**
 *  
 */
public final class DataPointHints
{

	private final RunTimeContext rtc;

	/**
	 *  
	 */
	private final Object oBaseValue;

	/**
	 *  
	 */
	private final Object oOrthogonalValue;

	/**
	 *  
	 */
	private final Object oSeriesValue;

	/**
	 *  
	 */
	private final Location lo;

	/**
	 *  
	 */
	private final double dSize;

	/**
	 *  
	 */
	private final DataPoint dp;

	/**
	 *  
	 */
	private final FormatSpecifier fsBase, fsOrthogonal, fsSeries;

	/**
	 * 
	 * @param _oBaseValue
	 * @param _oOrthogonalValue
	 * @param _sSeriesValue
	 * @param _dp
	 * @param _lo
	 * @param _dSize
	 * @param _lcl
	 * @throws UndefinedValueException
	 */
	public DataPointHints( Object _oBaseValue, Object _oOrthogonalValue,
			Object _oSeriesValue, DataPoint _dp, // FOR COMBINED VALUE RETRIEVAL
			FormatSpecifier _fsBase, FormatSpecifier _fsOrthogonal,
			FormatSpecifier _fsSeries, Location _lo, double _dSize,
			RunTimeContext _rtc ) throws ChartException
	{
		if ( _dp == null )
		{
			throw new ChartException( ChartException.UNDEFINED_VALUE,
					"exception.undefined.data.point", //$NON-NLS-1$
					ResourceBundle.getBundle( Messages.ENGINE, _rtc.getLocale( ) ) );
		}
		dp = _dp;
		oBaseValue = _oBaseValue;
		oOrthogonalValue = _oOrthogonalValue;
		oSeriesValue = _oSeriesValue;

		fsBase = _fsBase;
		fsOrthogonal = _fsOrthogonal;
		fsSeries = _fsSeries;

		lo = _lo;
		rtc = _rtc;
		dSize = _dSize;
	}

	/**
	 * 
	 * @return
	 */
	public final Object getBaseValue( )
	{
		return oBaseValue;
	}

	/**
	 * 
	 * @return
	 */
	public final Object getOrthogonalValue( )
	{
		return oOrthogonalValue;
	}

	/**
	 * 
	 * @return
	 */
	public final Object getSeriesValue( )
	{
		return oSeriesValue;
	}

	/**
	 * 
	 * @return
	 */
	public final Location getLocation( )
	{
		return lo;
	}

	/**
	 * 
	 * @return
	 */
	public final double getSize( )
	{
		return dSize;
	}

	/**
	 * 
	 * @return
	 */
	public final String getOrthogonalDisplayValue( )
	{
		return getOrthogonalDisplayValue( fsOrthogonal );
	}

	/**
	 * 
	 * @return
	 */
	public final String getBaseDisplayValue( )
	{
		return getBaseDisplayValue( fsBase );
	}

	/**
	 * 
	 * @return
	 */
	public final String getSeriesDisplayValue( )
	{
		return getSeriesDisplayValue( fsSeries );
	}

	/**
	 * 
	 * @return
	 */
	private final String getBaseDisplayValue( FormatSpecifier fs )
	{
		if ( oBaseValue == null )
		{
			return IConstants.NULL_STRING;
		}
		try
		{
			return ValueFormatter.format( oBaseValue,
					fs,
					rtc.getLocale( ),
					null );
		}
		catch ( Exception ex )
		{
			DefaultLoggerImpl.instance( )
					.log( ILogger.ERROR,
							Messages.getString( "exception.parse.value.format.specifier", //$NON-NLS-1$
									new Object[]{
											oBaseValue, fs
									},
									rtc.getLocale( ) ) );
		}
		return IConstants.NULL_STRING;
	}

	/**
	 * 
	 * @return
	 */
	private final String getOrthogonalDisplayValue( FormatSpecifier fs )
	{
		if ( oOrthogonalValue == null )
		{
			return IConstants.NULL_STRING;
		}
		try
		{
			return ValueFormatter.format( oOrthogonalValue,
					fs,
					rtc.getLocale( ),
					null );
		}
		catch ( Exception ex )
		{
			DefaultLoggerImpl.instance( )
					.log( ILogger.ERROR,
							Messages.getString( "exception.parse.value.format.specifier", //$NON-NLS-1$
									new Object[]{
											oOrthogonalValue, fs
									},
									rtc.getLocale( ) ) );
		}
		return IConstants.NULL_STRING;
	}

	/**
	 * 
	 * @return
	 */
	private final String getSeriesDisplayValue( FormatSpecifier fs )
	{
		if ( oSeriesValue == null )
		{
			return IConstants.NULL_STRING;
		}
		try
		{
			return ValueFormatter.format( oSeriesValue,
					fs,
					rtc.getLocale( ),
					null );
		}
		catch ( Exception ex )
		{
			DefaultLoggerImpl.instance( )
					.log( ILogger.ERROR,
							Messages.getString( "exception.parse.value.format.specifier", //$NON-NLS-1$
									new Object[]{
											oSeriesValue, fs
									},
									rtc.getLocale( ) ) );
		}
		return IConstants.NULL_STRING;
	}

	/**
	 * 
	 * @return
	 */
	public final String getDisplayValue( )
	{
		final EList el = dp.getComponents( );
		final StringBuffer sb = new StringBuffer( );

		if ( dp.getPrefix( ) != null )
		{
			sb.append( dp.getPrefix( ) );
		}
		DataPointComponent dpc;
		DataPointComponentType dpct;

		for ( int i = 0; i < el.size( ); i++ )
		{
			dpc = (DataPointComponent) el.get( i );
			dpct = dpc.getType( );
			if ( dpct == DataPointComponentType.BASE_VALUE_LITERAL )
			{
				sb.append( getBaseDisplayValue( ) );
			}
			else if ( dpct == DataPointComponentType.ORTHOGONAL_VALUE_LITERAL )
			{
				sb.append( getOrthogonalDisplayValue( ) );
			}
			else if ( dpct == DataPointComponentType.SERIES_VALUE_LITERAL )
			{
				sb.append( getSeriesDisplayValue( ) );
			}

			if ( i < el.size( ) - 1 )
			{
				sb.append( dp.getSeparator( ) );
			}
		}
		if ( dp.getSuffix( ) != null )
		{
			sb.append( dp.getSuffix( ) );
		}
		return sb.toString( );
	}

	/**
	 *  
	 */
	public final String toString( )
	{
		return Messages.getString( "info.datapoint.to.string", //$NON-NLS-1$
				new Object[]{
					super.toString( ) + getDisplayValue( )
				}, rtc.getLocale( ) ); 
	}
}