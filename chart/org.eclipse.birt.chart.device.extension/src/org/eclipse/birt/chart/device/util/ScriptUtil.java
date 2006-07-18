/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.util;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;

import com.ibm.icu.util.Calendar;

public class ScriptUtil
{

	/**
	 * Add the value of categoryData, valueData, and valueSeriesName into
	 * script.
	 * 
	 * @param str
	 * @param dph
	 * @return the output script
	 */
	public static String script( String str, DataPointHints dph )
	{
		str += addDataValueToScript( dph.getBaseValue( ) );
		str += ","; //$NON-NLS-1$
		str += addDataValueToScript( dph.getOrthogonalValue( ) );
		str += ","; //$NON-NLS-1$
		str += addDataValueToScript( dph.getSeriesValue( ) );
		return str;
	}

	/**
	 * Return the correct string according the the data type.
	 * 
	 * @param oValue
	 * @return the formatted string
	 */
	private static String addDataValueToScript( Object oValue )
	{
		if ( oValue instanceof String )
		{
			return "'" + (String) oValue + "'";//$NON-NLS-1$ //$NON-NLS-2$
		}
		else if ( oValue instanceof Double )
		{
			return ( (Double) oValue ).toString( );
		}
		else if ( oValue instanceof NumberDataElement )
		{
			return ( (NumberDataElement) oValue ).toString( );
		}
		else if ( oValue instanceof Calendar )
		{
			return "'" + ( (Calendar) oValue ).getTime( ).toString( ) + "'";//$NON-NLS-1$ //$NON-NLS-2$
		}
		else if ( oValue instanceof DateTimeDataElement )
		{
			return "'" + ( (DateTimeDataElement) oValue ).getValueAsCalendar( ).toString( ) + "'";//$NON-NLS-1$ //$NON-NLS-2$
		}
		else
		{
			return "'" + oValue.toString( ) + "'";//$NON-NLS-1$ //$NON-NLS-2$
		}
	}

}
