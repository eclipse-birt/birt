
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import org.eclipse.birt.data.engine.api.timefunction.TimeMember;

import com.ibm.icu.util.Calendar;


/**
 * This abstract class used for MDX function. MTD,YTD,trailing,etc,,, will
 * extends this class, you can add some base method here.
 * 
 * @author peng.shi
 * 
 */
abstract public class AbstractMDX
{

	protected static final String YEAR = "year";
	protected static final String QUARTER = "quarter";
	protected static final String MONTH = "month";
	protected static final String WEEK = "week";
	protected static final String DAY = "day";
	
	protected boolean isCurrent = false;

	/**
	 * translate the TimeMember.values to Calendar return the base
	 * level("year","month","day"...)
	 * 
	 * @param cal
	 * @param levelTypes
	 * @param values
	 * @return
	 */
	protected String translateToCal( Calendar cal, String[] levelTypes,
			int[] values )
	{
		String type = "";
		for ( int i = 0; i < values.length; i++ )
		{
			if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_YEAR ) )
			{
				//cal.get(Calendar.YEAR );
				cal.set( Calendar.YEAR, values[i] );
				type = YEAR;
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_QUARTER ) )
			{
				// no quarter in cal,so set the corresponding month
				cal.set( Calendar.MONTH, values[i] * 3 - 1 );
				type = QUARTER;
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_MONTH ) )
			{
				cal.set( Calendar.MONTH, values[i] - 1 );
				type = MONTH;
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH ) )
			{
				cal.get( Calendar.WEEK_OF_MONTH );
				cal.set( Calendar.WEEK_OF_MONTH, values[i] );
				type = WEEK;
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR ) )
			{
				cal.get( Calendar.WEEK_OF_YEAR );
				cal.set( Calendar.WEEK_OF_YEAR, values[i] );
				type = WEEK;
			}
			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK ) )
			{
				// here seems a com.ibm.icu.util.Calendar bug, if do not call cal.get()
				// sometimes cal.set() will change date to a wrong result.
				cal.get( Calendar.DAY_OF_WEEK );
				cal.set( Calendar.DAY_OF_WEEK, values[i] );
				type = DAY;
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH ) )
			{
				cal.set( Calendar.DAY_OF_MONTH, values[i] );
				type = DAY;
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR ) )
			{
				cal.set( Calendar.DAY_OF_YEAR, values[i] );
				type = DAY;
			}
		}

		return type;
	}

	/**
	 * get the TimeMember.values from Calendar
	 * 
	 * @param cal
	 * @param levelTypes
	 * @return
	 */
	protected int[] getValueFromCal( Calendar cal, String[] levelTypes )
	{
		int[] tmp = new int[levelTypes.length];

		for ( int i = 0; i < levelTypes.length; i++ )
		{
			if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_YEAR ) )
			{
				tmp[i] = cal.get( Calendar.YEAR );
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_QUARTER ) )
			{
				tmp[i] = cal.get( Calendar.MONTH ) / 3 + 1;
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_MONTH ) )
			{
				tmp[i] = cal.get( Calendar.MONTH ) + 1;
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH ) )
			{
				tmp[i] = cal.get( Calendar.WEEK_OF_MONTH );
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR ) )
			{
				tmp[i] = cal.get( Calendar.WEEK_OF_YEAR );
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK ) )
			{
				tmp[i] = cal.get( Calendar.DAY_OF_WEEK );
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH ) )
			{
				tmp[i] = cal.get( Calendar.DAY_OF_MONTH );
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR ) )
			{
				tmp[i] = cal.get( Calendar.DAY_OF_YEAR );
			}
		}

		return tmp;
	}
	
	public void setIsCurrent( boolean isCurrent )
	{
		this.isCurrent = isCurrent;
	}
}
