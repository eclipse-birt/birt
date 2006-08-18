/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.group;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IQuery.GroupSpec;

/**
 * A factory of group calculator.
 */
public class GroupCalculatorFactory
{
	/**
	 * 
	 * @param interval
	 * @param dataType
	 * @param intervalStart
	 * @param intervalRange
	 * @return
	 * @throws DataException
	 */
	public static ICalculator getGroupCalculator( int interval, int dataType,
			Object intervalStart, double intervalRange ) throws DataException
	{
		if ( !isValidInterval( interval, dataType ) )
		{
			return null;
		}
		try
		{
			switch ( interval )
			{
				case IGroupDefinition.YEAR_INTERVAL :
					return new YearGroupCalculator( intervalStart,
							intervalRange );

				case IGroupDefinition.MONTH_INTERVAL :
					return new MonthGroupCalculator( intervalStart,
							intervalRange );

				case IGroupDefinition.QUARTER_INTERVAL :
					return new QuarterGroupCalculator( intervalStart,
							intervalRange );

				case IGroupDefinition.WEEK_INTERVAL :
					return new WeekGroupCalculator( intervalStart,
							intervalRange );

				case IGroupDefinition.DAY_INTERVAL :
					return new DayGroupCalculator( intervalStart, intervalRange );

				case IGroupDefinition.HOUR_INTERVAL :
					return new HourGroupCalculator( intervalStart,
							intervalRange );

				case IGroupDefinition.MINUTE_INTERVAL :
					return new MinuteGroupCalculator( intervalStart,
							intervalRange );

				case IGroupDefinition.SECOND_INTERVAL :
					return new SecondGroupCalculator( intervalStart,
							intervalRange );

				case IGroupDefinition.NUMERIC_INTERVAL :
					return new NumericGroupCalculator( intervalStart,
							intervalRange );

				case IGroupDefinition.STRING_PREFIX_INTERVAL :
					return new StringGroupCalculator( intervalStart,
							intervalRange );
				default :
					throw new DataException( ResourceConstants.BAD_GROUP_INTERVAL_INVALID );
			}
		}
		catch ( BirtException be )
		{
			throw DataException.wrap( be );
		}
	}

	/**
	 * 
	 * @param interval
	 * @param dataType
	 * @return
	 * @throws DataException
	 */
	private static boolean isValidInterval( int interval, int dataType )
			throws DataException
	{
		if ( dataType == DataType.ANY_TYPE || dataType == DataType.UNKNOWN_TYPE )
		{
			return true;
		}

		switch ( interval )
		{
			case GroupSpec.NO_INTERVAL :
				return true;
			case GroupSpec.NUMERIC_INTERVAL :
				if ( isNumber( dataType ) )
					return true;
				else
					throw new DataException( ResourceConstants.BAD_GROUP_INTERVAL_TYPE,
							new Object[]{
									"numeric", DataType.getName( dataType )
							} );
			case GroupSpec.STRING_PREFIX_INTERVAL :
				if ( isString( dataType ) )
					return true;
				else
					throw new DataException( ResourceConstants.BAD_GROUP_INTERVAL_TYPE,
							new Object[]{
									"string prefix",
									DataType.getName( dataType )
							} );
			default :
				if ( isDate( dataType ) )
					return true;
				else
					throw new DataException( ResourceConstants.BAD_GROUP_INTERVAL_TYPE,
							new Object[]{
									"date", DataType.getName( dataType )
							} );
		}
	}

	/**
	 * 
	 * @param dataType
	 * @return
	 */
	private static boolean isNumber( int dataType )
	{
		return ( dataType == DataType.DECIMAL_TYPE
				|| dataType == DataType.DOUBLE_TYPE || dataType == DataType.INTEGER_TYPE );
	}

	/**
	 * 
	 * @param dataType
	 * @return
	 */
	private static boolean isDate( int dataType )
	{
		return ( dataType == DataType.DATE_TYPE );
	}

	/**
	 * 
	 * @param dataType
	 * @return
	 */
	private static boolean isString( int dataType )
	{
		return ( dataType == DataType.STRING_TYPE );
	}
}
