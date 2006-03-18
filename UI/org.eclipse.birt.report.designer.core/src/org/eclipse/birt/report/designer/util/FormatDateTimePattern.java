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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

public class FormatDateTimePattern
{
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
		if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE.equals( category ) )
		{
			pattern = "General Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE.equals( category ) )
		{
			pattern = "Long Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE.equals( category ) )
		{
			pattern = "Medium Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE.equals( category ) )
		{
			pattern = "Short Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME.equals( category ) )
		{
			pattern = "Long Time"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME.equals( category ) )
		{
			pattern = "Medium Time"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME.equals( category ) )
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