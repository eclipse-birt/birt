/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.api.timeFunction;

import java.util.List;

import org.eclipse.birt.report.data.adapter.i18n.Message;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;

import com.ibm.icu.util.ULocale;

public interface IArgumentInfo 
{
	/**
	 * Arguments for time function
	 */
	public static final String PERIOD_1 = "Period1";
	public static final String PERIOD_2 = "Period2";
	public static final String N_PERIOD1 = "N for Period1";
	public static final String N_PERIOD2 = "N for Period2";
	
	/**
	 * Get argument name for time function
	 * @return
	 */
	public String getName( );
	
	/**
	 * Get argument display name for time function
	 * @return
	 */
	public String getDisplayName( );
	
	/**
	 * Is this argument required for this time function
	 * @return
	 */
	public boolean isOptional( );
	
	/**
	 * Available value choices for this time function
	 * @return
	 */
	public List<Period_Type> getPeriodChoices( );
	
	/**
	 * Get description for this argument
	 * @return
	 */
	public String getDescription( );
	
	public class Period_Type 
	{
		public enum Period_Type_ENUM { YEAR, QUARTER, MONTH, WEEK, DAY };

		private Period_Type_ENUM type;
		private ULocale locale;
		
		public Period_Type( Period_Type_ENUM type, ULocale locale )
		{
			this.type = type;
			this.locale = locale;
		}
		
		/**
		 * 
		 * @return
		 */
		public String name( )
		{
			return this.type.name();
		}
		
		/**
		 * 
		 * @return
		 */
		public String displayName( )
		{
			if( this.type.equals( Period_Type_ENUM.YEAR ) )
			{
				return Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIODCHOICE_YEAR_DISPLAYNAME, locale );
			}
			if( this.type.equals( Period_Type_ENUM.QUARTER ) )
			{
				return Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIODCHOICE_QUARTER_DISPLAYNAME , locale );	
			}
			if( this.type.equals( Period_Type_ENUM.MONTH ) )
			{
				return Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIODCHOICE_MONTH_DISPLAYNAME, locale );			
			}
			if( this.type.equals( Period_Type_ENUM.WEEK ) )
			{
				return Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIODCHOICE_WEEK_DISPLAYNAME, locale );			
			}
			if( this.type.equals( Period_Type_ENUM.DAY ) )
			{
				return Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIODCHOICE_DAY_DISPLAYNAME , locale );
			}		
			return this.type.name();
		}
	}
}
