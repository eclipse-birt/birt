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
package org.eclipse.birt.report.engine.api.impl;

import java.util.Comparator;

import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;

import com.ibm.icu.util.ULocale;

public class SelectionChoiceComparator implements Comparator
{

    protected boolean sortDisplayValue;
    protected String format = null;
    protected ULocale locale = null;
    
    public SelectionChoiceComparator( boolean sortDisplayValue, String format, ULocale locale)
    {
        this.sortDisplayValue = sortDisplayValue;
        this.format = format;
        this.locale = locale;
        if( null == this.locale)
        {
        	this.locale = ULocale.getDefault( );
        }
    }
    
    public int compare(Object o1, Object o2)
    {
        if ((o1 instanceof IParameterSelectionChoice)
                && (o2 instanceof IParameterSelectionChoice))
        {
            Object value1;
            Object value2;
            if(sortDisplayValue)
            {
                value1 = ((IParameterSelectionChoice) o1).getLabel();
                value2 = ((IParameterSelectionChoice) o2).getLabel();
                if( null == value1 )
                {
                	value1 = getDisplayValue( ((IParameterSelectionChoice) o1).getValue() );
                }
                if( null == value2 )
                {
                	value2 = getDisplayValue( ((IParameterSelectionChoice) o2).getValue() );
                }
            }
            else
            {
                value1 = ((IParameterSelectionChoice) o1).getValue();
                value2 = ((IParameterSelectionChoice) o2).getValue();
            }
            
            if (value1 == value2)
            {
            	return 0;
            }
            if (value1 == null)
            {
            	return -1;
            }
            if (value2 == null)
            {
            	return 1;
            }
            if((value1 instanceof Boolean)&&(value2 instanceof Boolean))
            {
                if(((Boolean)value1).booleanValue() ^ ((Boolean)value1).booleanValue())
                {
                    return 0;
                }
                return ((Boolean)value1).booleanValue() ? 1: -1;
            }
            
            if (value1 instanceof Comparable)
            {
            	return ((Comparable) value1).compareTo(value2);
            }
            if (value2 instanceof Comparable)
            {
            	return -((Comparable) value2).compareTo(value1);
            }
        }
        return -1;
    }


	/**
	 * convert value to display value
	 * @param value
	 */
    private String getDisplayValue( Object value )
    {
    	if( null == value )
    	{
    		return null;
    	}
    	
    	ReportParameterConverter converter = new ReportParameterConverter(
				format, locale );
    	return converter.format( value );
    }
}
