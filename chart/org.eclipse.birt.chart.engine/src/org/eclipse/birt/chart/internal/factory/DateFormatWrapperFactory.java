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

package org.eclipse.birt.chart.internal.factory;

import java.text.FieldPosition;
import java.util.Date;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * An internal factory help to generate IDateFormatWrapper.
 */
public class DateFormatWrapperFactory
{

	/**
	 * Prevent from instanciation
	 */
	private DateFormatWrapperFactory( )
	{
	}

	/**
	 * Returns a preferred format specifier for tick labels that represent axis
	 * values that will be computed based on the difference between cdt1 and
	 * cdt2
	 * 
	 * @param iUnit
	 *            The unit for which a preferred pattern is being requested
	 * 
	 * @return A preferred datetime format for the given unit
	 */
	public static final IDateFormatWrapper getPreferredDateFormat( int iUnit )
	{
		return getPreferredDateFormat( iUnit, ULocale.getDefault( ) );
	}

	/**
	 * Returns a preferred format specifier for tick labels that represent axis
	 * values that will be computed based on the difference between cdt1 and
	 * cdt2
	 * 
	 * @param iUnit
	 *            The unit for which a preferred pattern is being requested
	 * @param locale
	 *            The locale for format style
	 * 
	 * @return A preferred datetime format for the given unit
	 */
	public static final IDateFormatWrapper getPreferredDateFormat( int iUnit,
			ULocale locale )
	{
		IDateFormatWrapper df = null;
		switch ( iUnit )
		{
			case Calendar.YEAR :
				df = new CommonDateFormatWrapper( new SimpleDateFormat( "yyyy", //$NON-NLS-1$
						locale ) );
				break;
			case Calendar.MONTH :
				df = new MonthDateFormat( locale );
				break;
			case Calendar.DATE :
				df = new CommonDateFormatWrapper( DateFormat.getDateInstance( DateFormat.LONG,
						locale ) );
				break;
			case Calendar.HOUR_OF_DAY :
				df = new HourDateFormat( locale );
				break;
			case Calendar.MINUTE :
			case Calendar.SECOND :
				df = new CommonDateFormatWrapper( new SimpleDateFormat( "HH:mm:ss", //$NON-NLS-1$
						locale ) );
				break;

		}
		return df;
	}

	static class CommonDateFormatWrapper implements IDateFormatWrapper
	{

		private DateFormat formater;

		public CommonDateFormatWrapper( DateFormat formater )
		{
			this.formater = formater;
		}

		public String format( Date date )
		{
			return formater.format( date );
		}

	}

	static class HourDateFormat implements IDateFormatWrapper
	{

		private ULocale locale;

		public HourDateFormat( ULocale locale )
		{
			super( );
			this.locale = locale;
		}

		public String format( Date date )
		{
			return DateFormat.getDateInstance( DateFormat.LONG, locale )
					.format( date )
					+ "\n" //$NON-NLS-1$
					+ new SimpleDateFormat( "HH:mm", locale ).format( date ); //$NON-NLS-1$
		}

	}

	static class MonthDateFormat implements IDateFormatWrapper
	{

		private ULocale locale;

		public MonthDateFormat( ULocale locale )
		{
			super( );
			this.locale = locale;
		}

		public String format( Date date )
		{
			StringBuffer str = new StringBuffer( );
			FieldPosition pos = new FieldPosition( DateFormat.DATE_FIELD );
			DateFormat df = DateFormat.getDateInstance( DateFormat.LONG, locale );
			df.format( date, str, pos );
			int endIndex = pos.getEndIndex( )
					+ ( str.charAt( pos.getEndIndex( ) ) == ',' ? 2 : 1 );
			if ( endIndex >= str.length( ) )
			{
				return str.substring( 0, pos.getBeginIndex( ) );
			}
			return str.substring( 0, pos.getBeginIndex( ) )
					+ str.substring( endIndex );
		}
	}
}
