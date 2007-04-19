/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.Date;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.report.designer.ui.dialogs.FormatBuilder;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.FormatDateTimePattern;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.DateFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.TimeFormatValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

public class FormatDateTimeAdapter
{

	private static final String[] DATETIME_FORMAT_TYPES = {
			DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE,
			DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE,
			DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE,
			DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE,
			DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME,
			DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME,
			DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME
	};

	private static final String[] DATE_FORMAT_TYPES = {
			DesignChoiceConstants.DATE_FORMAT_TYPE_GENERAL_DATE,
			DesignChoiceConstants.DATE_FORMAT_TYPE_LONG_DATE,
			DesignChoiceConstants.DATE_FORMAT_TYPE_MUDIUM_DATE,
			DesignChoiceConstants.DATE_FORMAT_TYPE_SHORT_DATE
	};

	private static final String[] TIME_FORMAT_TYPES = {
			DesignChoiceConstants.TIME_FORMAT_TYPE_LONG_TIME,
			DesignChoiceConstants.TIME_FORMAT_TYPE_MEDIUM_TIME,
			DesignChoiceConstants.TIME_FORMAT_TYPE_SHORT_TIME
	};

	private static String UNFORMATTED_DISPLAYNAME, CUSTOM, UNFORMATTED_NAME;

	private int type;
	private Date defaultDate = new Date( );

	public FormatDateTimeAdapter( int type )
	{
		this.type = type;
		init( );
	}

	private void init( )
	{
		switch ( type )
		{
			case FormatBuilder.DATETIME :
				UNFORMATTED_DISPLAYNAME = DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED;
				CUSTOM = DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM;
				UNFORMATTED_NAME = DateFormatter.DATETIME_UNFORMATTED;

				break;
			case FormatBuilder.DATE :
				UNFORMATTED_DISPLAYNAME = DesignChoiceConstants.DATE_FORMAT_TYPE_UNFORMATTED;
				CUSTOM = DesignChoiceConstants.DATE_FORMAT_TYPE_CUSTOM;
				UNFORMATTED_NAME = DateFormatter.DATE_UNFORMATTED;
				break;
			case FormatBuilder.TIME :
				UNFORMATTED_DISPLAYNAME = "Unformatted";
				CUSTOM = DesignChoiceConstants.TIME_FORMAT_TYPE_CUSTOM;
				UNFORMATTED_NAME = DateFormatter.TIME_UNFORMATTED;
				break;
		}
	}

	public String[] getSimpleDateTimeFormatTypes( )
	{
		if ( type == FormatBuilder.DATETIME )
		{
			return DATETIME_FORMAT_TYPES;
		}
		else if ( type == FormatBuilder.DATE )
		{
			return DATE_FORMAT_TYPES;
		}
		else if ( type == FormatBuilder.TIME )
		{
			return TIME_FORMAT_TYPES;
		}
		else
		{
			return new String[0];
		}
	}

	/**
	 * Returns the choiceArray of this choice element from model.
	 */
	public String[][] getFormatTypeChoiceSet( )
	{
		String[][] choiceArray;
		String structName, property;

		if ( type == FormatBuilder.DATETIME )
		{
			structName = DateTimeFormatValue.FORMAT_VALUE_STRUCT;
			property = DateTimeFormatValue.CATEGORY_MEMBER;
		}
		else if ( type == FormatBuilder.DATE )
		{
			structName = DateFormatValue.FORMAT_VALUE_STRUCT;
			property = DateFormatValue.CATEGORY_MEMBER;
		}
		else
		{
			structName = TimeFormatValue.FORMAT_VALUE_STRUCT;
			property = TimeFormatValue.CATEGORY_MEMBER;
		}
		IChoiceSet set = ChoiceSetFactory.getStructChoiceSet( structName,
				property );
		IChoice[] choices = set.getChoices( );

		if ( choices.length > 0 )
		{
			choiceArray = new String[choices.length][2];
			for ( int i = 0, j = 0; i < choices.length; i++ )
			{
				{
					choiceArray[j][0] = choices[i].getDisplayName( );
					choiceArray[j][1] = choices[i].getName( );
					j++;
				}
			}
		}
		else
		{
			choiceArray = new String[0][0];
		}
		return choiceArray;
	}

	public String[] getFormatTypes( )
	{
		String[] formatTypes;
		String[][] choiceArray = getFormatTypeChoiceSet( );
		if ( choiceArray != null && choiceArray.length > 0 )
		{
			formatTypes = new String[choiceArray.length];
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				String fmtStr = ""; //$NON-NLS-1$
				String category = choiceArray[i][1];
				if ( category.equals( UNFORMATTED_DISPLAYNAME ) || category.equals( CUSTOM ) )
				{
					fmtStr = choiceArray[i][0];
				}
				else
				{
					// uses UI specified display names.
					String pattern = FormatDateTimePattern.getPatternForCategory( category );
					fmtStr = new DateFormatter( pattern ).format( defaultDate );
				}
				formatTypes[i] = fmtStr;
			}
		}
		else
		{
			formatTypes = new String[0];
		}
		return formatTypes;
	}
	
	public String getUnformattedCategoryDisplayName()
	{
		return UNFORMATTED_DISPLAYNAME;
	}
	
	public String getCustomCategoryName()
	{
		return CUSTOM;
	}
	public String getUnformattedCategoryName()
	{
		return UNFORMATTED_NAME;
	}
}
