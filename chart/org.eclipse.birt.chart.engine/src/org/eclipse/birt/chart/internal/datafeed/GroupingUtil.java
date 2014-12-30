package org.eclipse.birt.chart.internal.datafeed;

import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;


/**
 * The class defines some static methods for grouping.
 * @since 2.3
 */
public final class GroupingUtil
{

	/**
	 * Format object into specified data format. 
	 * 
	 * @param obj object will be formated as date time.
	 * @param groupingUnitType the grouping unit type. 
	 * @return instance of <code>CDateTime</code>.
     * @since 2.3
	 */
	static CDateTime formatGroupedDateTime( Object obj, GroupingUnitType groupingUnitType )
	{
		int cunit = groupingUnit2CDateUnit( groupingUnitType );
		// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
		CDateTime date = null;
		if ( obj instanceof CDateTime )
		{
			date = (CDateTime) obj;
		}
		else
		{
			// set as the smallest Date.
			date = new CDateTime( 0 );
		}

		date.clearBelow( cunit );
		return date;
	}

	/**
	 * Convert GroupingUnit type to CDateUnit type.
	 * 
	 * @param groupingUnitType the GroupingUnit type.
	 * @return CDateUnit type of integer.
     * @since 2.3, it is merged from <code>DataProcessor</code>, make the method to be a static usage.
	 */
	public static int groupingUnit2CDateUnit( GroupingUnitType groupingUnitType )
	{
		if ( groupingUnitType != null )
		{
			switch ( groupingUnitType.getValue( ) )
			{
				case GroupingUnitType.SECONDS : 		// 0
					return Calendar.SECOND; 				// 13
				case GroupingUnitType.MINUTES : 		// 1
					return Calendar.MINUTE; 				// 12
				case GroupingUnitType.HOURS : 			// 2
					return Calendar.HOUR_OF_DAY; 			// 11
				case GroupingUnitType.DAYS : 			// 3
					return Calendar.DATE; 					// 5
				case GroupingUnitType.WEEKS :	 		// 4
					return Calendar.WEEK_OF_YEAR; 			// 3
				case GroupingUnitType.MONTHS : 			// 5
					return Calendar.MONTH; 					// 2
				case GroupingUnitType.QUARTERS : 		// 6
					return GroupingUnitType.QUARTERS; 		// 6
				case GroupingUnitType.YEARS : 			// 7
					return Calendar.YEAR; 					// 1
				case GroupingUnitType.WEEK_OF_MONTH: 	// 10
					return Calendar.WEEK_OF_YEAR; 			// 3
				case GroupingUnitType.DAY_OF_WEEK: 		// 11
				case GroupingUnitType.DAY_OF_MONTH: 	// 12
					return Calendar.DATE; 					// 5
			}
		}

		return Calendar.MILLISECOND;
	}

	/**
	 * Check if specified two strings are in same group with grouping setting. 
	 * 
	 * @param baseValue
	 * @param baseReference
	 * @param groupingUnit
	 * @param groupingInterval
	 * @return
	 * @since BIRT 2.3
	 */
	public static boolean isMatchedGroupingString( String baseValue,
			String baseReference, GroupingUnitType groupingUnit,
			int groupingInterval )
	{
		if ( baseValue == null && baseReference == null )
		{
			return true;
		}

		if ( baseValue == null || baseReference == null )
		{
			return false;
		}

		if ( groupingUnit == GroupingUnitType.STRING_PREFIX_LITERAL )
		{
			if ( groupingInterval <= 0 )
			{
				// The "0" means all data should be in one group.
				return true;
			}

			if ( baseValue.length( ) < groupingInterval ||
					baseReference.length( ) < groupingInterval )
			{
				return baseValue.equals( baseReference );
			}

			return baseValue.substring( 0, groupingInterval )
					.equals( baseReference.substring( 0, groupingInterval ) );

		}
		else if ( groupingUnit == GroupingUnitType.STRING_LITERAL )
		{
			return baseValue.equals( baseReference );
		}

		return baseValue.equals( baseReference );
	}
	
	/**
	 * Returns grouped string of specified string on grouping setting.
	 * 
	 * @param stringValue
	 * @param groupingUnit
	 * @param groupingInterval
	 * @return
	 * @since BIRT 2.3
	 */
	public static Object getGroupedString( String stringValue,
			GroupingUnitType groupingUnit, int groupingInterval )
	{
		if ( stringValue == null )
		{
			return stringValue;
		}
		
		if ( groupingUnit == GroupingUnitType.STRING_PREFIX_LITERAL )
		{
			if ( groupingInterval <= 0 )
			{
				// Always return empty string to make all data in one group.
				return ""; //$NON-NLS-1$
			}
			
			if ( stringValue.length( ) < groupingInterval )
			{
				return stringValue;
			}
			return stringValue.substring( 0, groupingInterval );
		}
		
		return stringValue;
	}
}
