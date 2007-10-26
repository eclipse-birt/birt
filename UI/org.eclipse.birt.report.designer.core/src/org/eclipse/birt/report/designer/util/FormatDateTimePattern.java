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

	public static final String DATETIEM_FORMAT_TYPE_YEAR = "datetiem_format_type_year";
	public static final String DATETIEM_FORMAT_TYPE_SHORT_YEAR = "datetiem_format_type_short_year";
	public static final String DATETIEM_FORMAT_TYPE_LONG_MONTH_YEAR = "datetiem_format_type_long_month_year";
	public static final String DATETIEM_FORMAT_TYPE_SHOT_MONTH_YEAR = "datetiem_format_type_shot_month_year";
	public static final String DATETIEM_FORMAT_TYPE_MONTH = "datetiem_format_type_month";
	public static final String DATETIEM_FORMAT_TYPE_LONG_DAY_OF_WEEK = "datetiem_format_type_long_day_of_week";
	public static final String DATETIEM_FORMAT_TYPE_DAY_OF_MONTH = "datetiem_format_type_day_of_month";
	public static final String DATETIEM_FORMAT_TYPE_MEDIUM_DAY_OF_YEAR = "datetiem_format_type_medium_day_of_year";
	public static final String DATETIEM_FORMAT_TYPE_MINUTES = "datetiem_format_type_minutes";
	public static final String DATETIEM_FORMAT_TYPE_SECONTDS = "datetiem_format_type_secontds";
	public static final String DATETIEM_FORMAT_TYPE_GENERAL_TIME = "datetiem_format_type_general_time";
	public static HashMap customFormatMap = new LinkedHashMap( );
	static
	{
		customFormatMap.put( DATETIEM_FORMAT_TYPE_GENERAL_TIME,
				"ahh:mm:ss.SSS" );
		customFormatMap.put( DATETIEM_FORMAT_TYPE_YEAR, "yyyy" );
		customFormatMap.put( DATETIEM_FORMAT_TYPE_SHORT_YEAR, "yy" );
		customFormatMap.put( DATETIEM_FORMAT_TYPE_LONG_MONTH_YEAR, "MMMM yyyy" );
		customFormatMap.put( DATETIEM_FORMAT_TYPE_SHOT_MONTH_YEAR, "MMM yy" );
		customFormatMap.put( DATETIEM_FORMAT_TYPE_MONTH, "MMMM" );
		customFormatMap.put( DATETIEM_FORMAT_TYPE_LONG_DAY_OF_WEEK, "EEEE" );
		customFormatMap.put( DATETIEM_FORMAT_TYPE_DAY_OF_MONTH, "dd" );
		customFormatMap.put( DATETIEM_FORMAT_TYPE_MEDIUM_DAY_OF_YEAR,
				"MMMM dd, yy" );
		customFormatMap.put( DATETIEM_FORMAT_TYPE_MINUTES, "mm" );
		customFormatMap.put( DATETIEM_FORMAT_TYPE_SECONTDS, "ss" );
	}

	public static String[] getCustormPatternCategorys( )
	{
		return (String[]) customFormatMap.keySet( ).toArray( new String[0] );
	}

	public static String getDisplayName4CustomCategory( String custormCategory )
	{
		return Messages.getString( "FormatDateTimePattern." + custormCategory );
	}

	public static String getCustormFormatPattern( String custormCategory )
	{
		return customFormatMap.get( custormCategory ) == null ? ""
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