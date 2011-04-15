/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.datafeed.IDataPointEntry;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.internal.factory.IDateFormatWrapper;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.StringFormatSpecifier;
import org.eclipse.birt.chart.model.data.BigNumberDataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.BigNumber;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.NumberUtil;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * This class handles the formatting work of any data value.
 */
public final class ValueFormatter
{

	private static final String sNegativeZero = "-0."; //$NON-NLS-1$

	/**
	 * A default numeric pattern for integer number representation of category
	 * data or axis label
	 */
	private static final String sNumericPattern = "0"; //$NON-NLS-1$

	/**
	 * Returns the formatted string representation of given object.
	 * 
	 * @param oValue
	 * @param formatSpecifier
	 * @param lcl
	 * @return formatted string
	 */
	public static final String format( Object oValue, FormatSpecifier formatSpecifier,
			ULocale lcl, Object oCachedJavaFormatter ) throws ChartException
	{
		FormatSpecifier fs = formatSpecifier;
		String sValue;
		if ( oValue == null ) // NULL VALUES CANNOT BE FORMATTED
		{
			return null;
		}
		else if ( ( oValue instanceof String )
				&& !( fs instanceof StringFormatSpecifier ) )
		{
			return (String) oValue;
		}

		// Add data type check for format specifier
		fs = resetFormatSpecifier( oValue, fs );

		// IF A FORMAT SPECIFIER WAS NOT ASSOCIATED WITH THE VALUE
		if ( fs == null )
		{
			// CHECK IF AN INTERNAL JAVA FORMAT SPECIFIER WAS COMPUTED
			if ( oCachedJavaFormatter != null )
			{
				if ( NumberUtil.isBigNumber( oValue ) )
				{
					return ( (DecimalFormat) oCachedJavaFormatter ).format( ( (BigNumber) oValue ).getValue( ) );
				}
				else if ( NumberUtil.isBigDecimal( oValue ) )
				{
					return ( (DecimalFormat) oCachedJavaFormatter ).format( oValue );
				}
				else if ( oValue instanceof Double
						|| oValue instanceof NumberDataElement )
				{
					if ( oCachedJavaFormatter instanceof DecimalFormat )
					{
						final double dValue = oValue instanceof Double ? ( (Double) oValue ).doubleValue( )
								: ( (NumberDataElement) oValue ).getValue( );
						sValue = ( (DecimalFormat) oCachedJavaFormatter ).format( dValue );
						return correctNumber( sValue );
					}
				}
				else if ( oValue instanceof BigNumberDataElement )
				{
					if ( oCachedJavaFormatter instanceof DecimalFormat )
					{
						sValue = ( (DecimalFormat) oCachedJavaFormatter ).format( NumberUtil.asBigDecimal( ( (BigNumberDataElement) oValue ).getValue( ) ) );
						return correctNumber( sValue );
					}
				}
				else if ( oValue instanceof Calendar
						|| oValue instanceof DateTimeDataElement )
				{
					Calendar calendar = oValue instanceof Calendar ? (Calendar) oValue
							: ( (DateTimeDataElement) oValue ).getValueAsCalendar( );
					if ( oCachedJavaFormatter instanceof IDateFormatWrapper )
					{
						return ( (IDateFormatWrapper) oCachedJavaFormatter ).format( calendar.getTime( ) );
					}
					else if ( oCachedJavaFormatter instanceof DateFormat )
					{
						return ( (DateFormat) oCachedJavaFormatter ).format( calendar );
					}
					else if ( oCachedJavaFormatter instanceof DateFormatSpecifier )
					{
						return ( (DateFormatSpecifier) oCachedJavaFormatter ).format( calendar,
								lcl );
					}
				}
				else if ( oValue instanceof IDataPointEntry )
				{
					if ( oCachedJavaFormatter instanceof FormatSpecifier )
					{
						return ( (IDataPointEntry) oValue ).getFormattedString( (FormatSpecifier) oCachedJavaFormatter,
								lcl );
					}
				}
			}
			else
			{
				if ( NumberUtil.isBigNumber( oValue ) )
				{
					return NumberUtil.getDefaultBigDecimalFormat( lcl )
							.format( ( (BigNumber) oValue ).getValue( ) );
				}
				else if ( NumberUtil.isBigDecimal( oValue ) )
				{
					return NumberUtil.getDefaultBigDecimalFormat( lcl )
							.format( oValue );
				}
				else if ( oValue instanceof Number )
				{
					return NumberFormat.getInstance( lcl )
							.format( ( (Number) oValue ).doubleValue( ) );
				}
				else if ( oValue instanceof NumberDataElement )
				{
					return NumberFormat.getInstance( lcl )
							.format( ( (NumberDataElement) oValue ).getValue( ) );
				}
				else if ( oValue instanceof BigNumberDataElement )
				{
					return NumberFormat.getInstance( lcl )
							.format( ( (BigNumberDataElement) oValue ).getValue( ) );
				}
				else if ( oValue instanceof CDateTime )
				{
					int dateStyle = ( (CDateTime) oValue ).isTimeOnly( ) ? DateFormat.NONE
							: DateFormat.DEFAULT;
					return DateFormat.getDateTimeInstance( dateStyle,
							DateFormat.DEFAULT,
							lcl ).format( oValue );
				}
				else if ( oValue instanceof Calendar )
				{
					return DateFormat.getDateTimeInstance( DateFormat.SHORT,
							DateFormat.SHORT,
							lcl ).format( oValue );
				}
				else if ( oValue instanceof DateTimeDataElement )
				{
					return DateFormat.getDateTimeInstance( DateFormat.SHORT,
							DateFormat.SHORT,
							lcl )
							.format( ( (DateTimeDataElement) oValue ).getValueAsCalendar( ) );
				}
				else if ( oValue instanceof IDataPointEntry )
				{
					return ( (IDataPointEntry) oValue ).getFormattedString( null,
							lcl );
				}
			}
		}
		else if ( oValue instanceof IDataPointEntry )
		{
			return ( (IDataPointEntry) oValue ).getFormattedString( fs, lcl );
		}
		else if ( NumberFormatSpecifier.class.isInstance( fs ) )
		{
			final NumberFormatSpecifier nfs = (NumberFormatSpecifier) fs;
			if ( NumberUtil.isBigNumber( oValue ) )
			{
				return correctNumber( nfs.format( ( (BigNumber) oValue ).getValue( ),
						lcl ) );
			}
			else if ( NumberUtil.isBigDecimal( oValue ) )
			{
				return correctNumber( nfs.format( (Number) oValue, lcl ) );
			}
			else
			{
				final double dValue = asPrimitiveDouble( oValue, lcl );
				return correctNumber( nfs.format( dValue, lcl ) );
			}
		}
		else if ( JavaNumberFormatSpecifier.class.isInstance( fs ) )
		{
			final JavaNumberFormatSpecifier nfs = (JavaNumberFormatSpecifier) fs;
			if ( NumberUtil.isBigNumber( oValue ) )
			{
				return correctNumber( nfs.format( ( (BigNumber) oValue ).getValue( ),
						lcl ) );
			}
			else if ( NumberUtil.isBigDecimal( oValue ) )
			{
				return correctNumber( nfs.format( (Number) oValue, lcl ) );
			}
			else
			{
				final double dValue = asPrimitiveDouble( oValue, lcl );
				return correctNumber( nfs.format( dValue, lcl ) );
			}
		}
		else if ( FractionNumberFormatSpecifier.class.isInstance( fs ) )
		{
			final FractionNumberFormatSpecifier fnfs = (FractionNumberFormatSpecifier) fs;
			final double dValue = asPrimitiveDouble( oValue, lcl );
			return correctNumber( fnfs.format( dValue, lcl ) );
		}
		else if ( DateFormatSpecifier.class.isInstance( fs ) )
		{
			final DateFormatSpecifier dfs = (DateFormatSpecifier) fs;
			return dfs.format( asCalendar( oValue, lcl ), lcl );
		}
		else if ( JavaDateFormatSpecifier.class.isInstance( fs ) )
		{
			final JavaDateFormatSpecifier jdfs = (JavaDateFormatSpecifier) fs;
			return jdfs.format( asCalendar( oValue, lcl ), lcl );
		}
		else if ( StringFormatSpecifier.class.isInstance( fs ) )
		{
			final StringFormatSpecifier jdfs = (StringFormatSpecifier) fs;
			return jdfs.format( oValue.toString( ), lcl );
		}
		else
		{
			if ( NumberUtil.isBigNumber( oValue ) )
			{
				return NumberUtil.getDefaultBigDecimalFormat( lcl )
						.format( ( (BigNumber) oValue ).getValue( ) );
			}
			else if ( NumberUtil.isBigDecimal( oValue ) )
			{
				return NumberUtil.getDefaultBigDecimalFormat( lcl )
						.format( oValue );
			}
			else if ( oValue instanceof Number )
			{
				return NumberFormat.getInstance( lcl )
						.format( ( (Number) oValue ).doubleValue( ) );
			}
			else if ( oValue instanceof NumberDataElement )
			{
				return NumberFormat.getInstance( lcl )
						.format( ( (NumberDataElement) oValue ).getValue( ) );
			}
			else if ( oValue instanceof BigNumberDataElement )
			{
				return NumberFormat.getInstance( lcl )
						.format( ( (BigNumberDataElement) oValue ).getValue( ) );
			}
			else if ( oValue instanceof Calendar )
			{
				return DateFormat.getDateTimeInstance( DateFormat.SHORT,
						DateFormat.SHORT,
						lcl ).format( oValue );
			}
			else if ( oValue instanceof DateTimeDataElement )
			{
				return DateFormat.getDateTimeInstance( DateFormat.SHORT,
						DateFormat.SHORT,
						lcl )
						.format( ( (DateTimeDataElement) oValue ).getValueAsCalendar( ) );
			}
		}
		return oValue.toString( );
	}

	/**
	 * Under some cases, the specified format specifier is not suitable for
	 * current value, the format specifier must be reset to fit current value.
	 * 
	 * @param oValue
	 * @param fs
	 * @return
	 */
	private static FormatSpecifier resetFormatSpecifier( Object oValue,
			FormatSpecifier fs )
	{
		if ( oValue instanceof IDataPointEntry )
		{
			return fs;
		}
		if ( fs instanceof DateFormatSpecifier
				|| fs instanceof JavaDateFormatSpecifier )
		{
			if ( !( oValue instanceof Calendar || oValue instanceof DateTimeDataElement ) )
			{
				return null;
			}
		}
		else if ( fs instanceof NumberFormatSpecifier
				|| fs instanceof JavaNumberFormatSpecifier
				|| fs instanceof FractionNumberFormatSpecifier )
		{
			if ( !( oValue instanceof Number
					|| oValue instanceof NumberDataElement
					|| oValue instanceof BigNumberDataElement
					|| NumberUtil.isBigNumber( oValue ) || NumberUtil.isBigDecimal( oValue ) ) )
			{
				return null;
			}
		}
		else if ( fs instanceof StringFormatSpecifier )
		{
			if ( !( oValue instanceof String ) )
			{
				return null;
			}
		}
		return fs;
	}

	private static final double asPrimitiveDouble( Object o, ULocale lcl )
			throws ChartException
	{
		if ( o instanceof Number )
		{
			return ( (Number) o ).doubleValue( );
		}
		else if ( o instanceof NumberDataElement )
		{
			return ( (NumberDataElement) o ).getValue( );
		}
		throw new ChartException( ChartEnginePlugin.ID,
				ChartException.DATA_FORMAT,
				"exception.convert.double", //$NON-NLS-1$
				new Object[]{
					o
				},
				Messages.getResourceBundle( lcl ) );
	}

	private static final Calendar asCalendar( Object o, ULocale lcl )
			throws ChartException
	{
		if ( o instanceof Calendar )
		{
			return (Calendar) o;
		}
		else if ( o instanceof DateTimeDataElement )
		{
			return ( (DateTimeDataElement) o ).getValueAsCalendar( );
		}
		throw new ChartException( ChartEnginePlugin.ID,
				ChartException.DATA_FORMAT,
				"exception.convert.calendar", //$NON-NLS-1$
				new Object[]{
					o
				},
				Messages.getResourceBundle( lcl ) );
	}

	/**
	 * Takes care of problems while presenting -0.00
	 * 
	 * @param sValue
	 * @return corrected number
	 */
	private static final String correctNumber( String sValue )
	{
		int n = ( sValue.length( ) - sNegativeZero.length( ) );
		final StringBuffer sb = new StringBuffer( sNegativeZero );
		for ( int i = 0; i < n; i++ )
		{
			sb.append( '0' );
		}

		if ( sValue.equals( sb.toString( ) ) )
		{
			return sb.substring( 1 ); // JUST THE ZERO IN THE EXPECTED PATTERN
			// WITHOUT THE STRAY NEGATIVE SYMBOL
		}
		return sValue;
	}

	/**
	 * Returns an auto computed number pattern.
	 * 
	 * @param num
	 *            number value
	 * @return number pattern
	 * @since 2.5.3
	 */
	public static String getNumericPattern( Number num )
	{
		Number numValue = num;
		if ( NumberUtil.isBigDecimal( num ) )
		{
			// Do nothing here.
		}
		else if ( NumberUtil.isBigNumber( num ) )
		{
			numValue = ( (BigNumber) num ).getValue( );
		}
		else
		{
			double value = numValue.doubleValue( );
			if ( ChartUtil.mathEqual( value, (long) value ) )
			{
				// IF MANTISSA IS INSIGNIFICANT, SHOW LABELS AS INTEGERS
				return sNumericPattern;
			}
		}

		final DecimalFormatSymbols dfs = new DecimalFormatSymbols( );
		String sValue = String.valueOf( numValue );
		int iEPosition = sValue.indexOf( dfs.getExponentSeparator( ) );

		if ( iEPosition > 0 )
		{
			double dValue = Double.valueOf( sValue.substring( 0, iEPosition ) )
					.doubleValue( );

			if ( ChartUtil.mathEqual( dValue, Math.round( dValue ) ) )
			{
				// IF MANTISSA IS INSIGNIFICANT, SHOW LABELS AS INTEGERS
				return "0E0"; //$NON-NLS-1$
			}
			else
			{
				sValue = String.valueOf( dValue );
			}
		}

		final int iDecimalPosition = sValue.indexOf( dfs.getDecimalSeparator( ) );
		// THIS RELIES ON THE FACT THAT IN ANY LOCALE, DECIMAL IS A DOT
		if ( iDecimalPosition >= 0 )
		{
			int n = sValue.length( );
			for ( int i = n - 1; i > 0; i-- )
			{
				if ( sValue.charAt( i ) == '0' )
				{
					n--;
				}
				else
				{
					break;
				}
			}
			final int iMantissaCount = n - 1 - iDecimalPosition;
			final StringBuffer sb = new StringBuffer( sNumericPattern );
			if ( iMantissaCount > 0 )
			{
				sb.append( '.' );
				for ( int i = 0; i < iMantissaCount; i++ )
				{
					sb.append( '0' );
				}
			}
			if ( iEPosition > 0 )
			{
				sb.append( "E0" ); //$NON-NLS-1$
			}
			return sb.toString( );
		}
		return sNumericPattern;
	}

	/**
	 * Returns an auto computed decimal format pattern for category data or axis
	 * label. If it's an integer, no decimal point and no separator. This is
	 * also used for representing logarithmic values.
	 * 
	 * @return numeric pattern
	 */
	public static String getNumericPattern( double dValue )
	{
		return getNumericPattern( Double.valueOf( dValue ) );
	}
}