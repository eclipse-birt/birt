/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.util;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

public class FormatDateTimePattern
{

	public static final String DATETIEM_FORMAT_TYPE_YEAR = "datetiem_format_type_year"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_SHORT_YEAR = "datetiem_format_type_short_year"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_LONG_MONTH_YEAR = "datetiem_format_type_long_month_year"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_SHOT_MONTH_YEAR = "datetiem_format_type_shot_month_year"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_MONTH = "datetiem_format_type_month"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_LONG_DAY_OF_WEEK = "datetiem_format_type_long_day_of_week"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_DAY_OF_MONTH = "datetiem_format_type_day_of_month"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_MEDIUM_DAY_OF_YEAR = "datetiem_format_type_medium_day_of_year"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_MINUTES = "datetiem_format_type_minutes"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_SECONTDS = "datetiem_format_type_secontds"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_GENERAL_TIME = "datetiem_format_type_general_time"; //$NON-NLS-1$

	public static final HashMap<String, String> customFormatMap = new LinkedHashMap<String, String>( );

	static
	{
		customFormatMap.put( DATETIEM_FORMAT_TYPE_GENERAL_TIME,
				Messages.getString( "FormatDateTimePattern.pattern." + DATETIEM_FORMAT_TYPE_GENERAL_TIME ) ); //$NON-NLS-1$
		customFormatMap.put( DATETIEM_FORMAT_TYPE_YEAR,
				Messages.getString( "FormatDateTimePattern.pattern." + DATETIEM_FORMAT_TYPE_YEAR ) ); //$NON-NLS-1$
		customFormatMap.put( DATETIEM_FORMAT_TYPE_SHORT_YEAR,
				Messages.getString( "FormatDateTimePattern.pattern." + DATETIEM_FORMAT_TYPE_SHORT_YEAR ) ); //$NON-NLS-1$
		customFormatMap.put( DATETIEM_FORMAT_TYPE_LONG_MONTH_YEAR,
				Messages.getString( "FormatDateTimePattern.pattern." + DATETIEM_FORMAT_TYPE_LONG_MONTH_YEAR ) ); //$NON-NLS-1$
		customFormatMap.put( DATETIEM_FORMAT_TYPE_SHOT_MONTH_YEAR,
				Messages.getString( "FormatDateTimePattern.pattern." + DATETIEM_FORMAT_TYPE_SHOT_MONTH_YEAR ) ); //$NON-NLS-1$
		customFormatMap.put( DATETIEM_FORMAT_TYPE_MONTH,
				Messages.getString( "FormatDateTimePattern.pattern." + DATETIEM_FORMAT_TYPE_MONTH ) ); //$NON-NLS-1$
		customFormatMap.put( DATETIEM_FORMAT_TYPE_LONG_DAY_OF_WEEK,
				Messages.getString( "FormatDateTimePattern.pattern." + DATETIEM_FORMAT_TYPE_LONG_DAY_OF_WEEK ) ); //$NON-NLS-1$
		customFormatMap.put( DATETIEM_FORMAT_TYPE_DAY_OF_MONTH,
				Messages.getString( "FormatDateTimePattern.pattern." + DATETIEM_FORMAT_TYPE_DAY_OF_MONTH ) ); //$NON-NLS-1$
		customFormatMap.put( DATETIEM_FORMAT_TYPE_MEDIUM_DAY_OF_YEAR,
				Messages.getString( "FormatDateTimePattern.pattern." + DATETIEM_FORMAT_TYPE_MEDIUM_DAY_OF_YEAR ) ); //$NON-NLS-1$
		customFormatMap.put( DATETIEM_FORMAT_TYPE_MINUTES,
				Messages.getString( "FormatDateTimePattern.pattern." + DATETIEM_FORMAT_TYPE_MINUTES ) ); //$NON-NLS-1$
		customFormatMap.put( DATETIEM_FORMAT_TYPE_SECONTDS,
				Messages.getString( "FormatDateTimePattern.pattern." + DATETIEM_FORMAT_TYPE_SECONTDS ) ); //$NON-NLS-1$
	}

	public static String[] getCustormPatternCategorys( )
	{
		return customFormatMap.keySet( ).toArray( new String[0] );
	}

	public static String getDisplayName4CustomCategory( String custormCategory )
	{
		return Messages.getString( "FormatDateTimePattern." + custormCategory ); //$NON-NLS-1$
	}

	public static String getCustormFormatPattern( String custormCategory )
	{
		return customFormatMap.get( custormCategory ) == null ? "" //$NON-NLS-1$
				: customFormatMap.get( custormCategory ).toString( );
	}

	/**
	 * Retrieves format pattern from arrays given format type categorys.
	 * 
	 * @param category
	 *            Given format type category.
	 * @return The corresponding format pattern string.
	 */

	public static String getPatternForCategory( String category )
	{
		String pattern;
		if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE.equals( category )
				|| DesignChoiceConstants.DATE_FORMAT_TYPE_GENERAL_DATE.equals( category ) )
		{
			pattern = "General Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE.equals( category )
				|| DesignChoiceConstants.DATE_FORMAT_TYPE_LONG_DATE.equals( category ) )
		{
			pattern = "Long Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE.equals( category )
				|| DesignChoiceConstants.DATE_FORMAT_TYPE_MUDIUM_DATE.equals( category ) )
		{
			pattern = "Medium Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE.equals( category )
				|| DesignChoiceConstants.DATE_FORMAT_TYPE_SHORT_DATE.equals( category ) )
		{
			pattern = "Short Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME.equals( category )
				|| DesignChoiceConstants.TIME_FORMAT_TYPE_LONG_TIME.equals( category ) )
		{
			pattern = "Long Time"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME.equals( category )
				|| DesignChoiceConstants.TIME_FORMAT_TYPE_MEDIUM_TIME.equals( category ) )
		{
			pattern = "Medium Time"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME.equals( category )
				|| DesignChoiceConstants.TIME_FORMAT_TYPE_SHORT_TIME.equals( category ) )
		{
			pattern = "Short Time"; //$NON-NLS-1$
		}
		else
		{
			// default, unformatted.
			pattern = ""; //$NON-NLS-1$
		}
		return pattern;
	}
}