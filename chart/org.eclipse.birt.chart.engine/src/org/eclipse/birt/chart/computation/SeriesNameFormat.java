/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * SeriesNameFormat is a Formatter special for SeriesIdentifier with optional
 * Y-Grouping.
 */

public class SeriesNameFormat
{

	public static final SeriesNameFormat DEFAULT_FORMAT = new SeriesNameFormat( );

	private SeriesNameFormat( )
	{
		// disable public construactor
	}

	public static SeriesNameFormat getSeriesNameFormat( SeriesDefinition sd,
			ULocale loc )
	{
		if ( sd != null
				&& sd.getQuery( ) != null
				&& sd.getQuery( ).getGrouping( ) != null )
		{
			SeriesGrouping sg = sd.getQuery( ).getGrouping( );
			if ( sg.getGroupType( ) == DataType.DATE_TIME_LITERAL )
			{
				return new SeriesNameDateFormat( sg.getGroupingUnit( ), loc );
			}
		}
		
		return DEFAULT_FORMAT;
	}

	public String format( Object obj )
	{
		String str = ""; //$NON-NLS-1$
		
		if ( obj != null )
		{
			if ( obj instanceof Number )
			{
				// TODO: use format cache to improve performance
				double d = ( (Number) obj ).doubleValue( );
				String sPattern = ValueFormatter.getNumericPattern( d );
				DecimalFormat df = new DecimalFormat( sPattern );
				str = df.format( d );
			}
			else
			{
				str = obj.toString( );
			}
		}
		
		return str;
	}

	/**
	 * sub class for DateTime grouping type SeriesNameDateFormat
	 */
	private static class SeriesNameDateFormat extends SeriesNameFormat
	{
		private final static String[] DEFAULT_PATTERN_PAGE = {
				"HH:mm:ss", // SECONDS 0 //$NON-NLS-1$
				"HH:mm", // MINUTES 1 //$NON-NLS-1$
				"HH:mm", // HOURS 2 //$NON-NLS-1$
				"yyyy-MM-dd", // DAYS 3 //$NON-NLS-1$
				"yyyy-MM-dd", // WEEKS 4 //$NON-NLS-1$
				"yyyy-MM", // MONTHS 5 //$NON-NLS-1$
				"yyyy QQQ", // QUARTERS 6 //$NON-NLS-1$
				"yyyy" // YEARS 7 //$NON-NLS-1$
		};

		private DateFormat fm = null;
		private DateFormat fmTimeOnly = null;
		private GroupingUnitType unitType;

		/**
		 * @param unitType
		 */
		public SeriesNameDateFormat( GroupingUnitType unitType, ULocale loc )
		{
			this.unitType = unitType;
			fm = new SimpleDateFormat( createPattern( unitType, loc, false ),
					loc );
			fmTimeOnly = new SimpleDateFormat( createPattern( unitType,
					loc,
					true ), loc );
		}
		
		/**
		 * Convert GroupingUnit type to CDateUnit type.
		 * 
		 * @param groupingUnitType
		 *            the GroupingUnit type.
		 * @return CDateUnit type of integer.
		 * @since 2.3, it is merged from <code>DataProcessor</code>, make the
		 *        method to be a static usage.
		 */
		private static int groupingUnit2CDateUnit(
				GroupingUnitType groupingUnitType )
		{
			if ( groupingUnitType != null )
			{
				switch ( groupingUnitType.getValue( ) )
				{
					case GroupingUnitType.SECONDS :
						return Calendar.SECOND;
					case GroupingUnitType.MINUTES :
						return Calendar.MINUTE;
					case GroupingUnitType.HOURS :
						return Calendar.HOUR_OF_DAY;
					case GroupingUnitType.DAYS :
						return Calendar.DATE;
					case GroupingUnitType.WEEKS :
						return Calendar.WEEK_OF_YEAR;
					case GroupingUnitType.MONTHS :
						return Calendar.MONTH;
					case GroupingUnitType.YEARS :
						return Calendar.YEAR;
					case GroupingUnitType.QUARTERS :
						return GroupingUnitType.QUARTERS;
				}
			}

			return Calendar.MILLISECOND;
		}
		
		private static String createPattern( GroupingUnitType unitType, ULocale loc,
				boolean isTime )
		{
			String[] patternPage = DEFAULT_PATTERN_PAGE;
			if ( unitType.getValue( ) < GroupingUnitType.DAYS )
			{
				String sPrefix = isTime ? "" //$NON-NLS-1$
						: patternPage[GroupingUnitType.DAYS] + " "; //$NON-NLS-1$
				return sPrefix + patternPage[unitType.getValue( )];
			}
			else
			{
				return patternPage[unitType.getValue( )];
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.chart.computation.SeriesNameFormat#format(java.lang
		 * .Object)
		 */
		@Override
		public String format( Object obj )
		{
			if ( obj instanceof CDateTime )
			{
				return formatCDateTime( (CDateTime) obj );
			}
			else
			{
				return super.format( obj );
			}
		}

		private String formatCDateTime( CDateTime cd )
		{
			cd.clearBelow( groupingUnit2CDateUnit( unitType ) );
			if ( cd.isTimeOnly( ) )
			{
				return fmTimeOnly.format( cd );
			}
			else
			{
				return fm.format( cd );
			}
		}
		
	}

}

