package org.eclipse.birt.chart.internal.datafeed;

import java.util.Date;

import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;


/**
 * The class defines some static methods for grouping.
 * @since 2.3
 */
final class GroupingUtil
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
		else if ( obj instanceof Calendar )
		{
			date = new CDateTime( (Calendar) obj );
		}
		else if ( obj instanceof Date )
		{
			date = new CDateTime( (Date) obj );
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
	static int groupingUnit2CDateUnit( GroupingUnitType groupingUnitType )
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
	
}
