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

package org.eclipse.birt.chart.computation;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

import org.eclipse.birt.chart.exception.DataFormatException;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;

/**
 *  
 */
public final class ValueFormatter
{

    /**
     *  
     */
    private static final String sNegativeZero = "-0.";

    /**
     * 
     * @param oValue
     * @param fs
     * @param lcl
     * @return
     */
    public static final String format(Object oValue, FormatSpecifier fs, Locale lcl, Object oCachedJavaFormatter)
        throws DataFormatException
    {
        String sValue;
        if (oValue == null) // NULL VALUES CANNOT BE FORMATTED
        {
            return null;
        }

        if (fs == null) // IF A FORMAT SPECIFIER WAS NOT ASSOCIATED WITH THE
        // VALUE
        {
            if (oCachedJavaFormatter != null) // CHECK IF AN INTERNAL JAVA
            // FORMAT SPECIFIER WAS COMPUTED
            {
                if (oValue instanceof Double)
                {
                    if (oCachedJavaFormatter instanceof DecimalFormat)
                    {
                        final double dValue = ((Double) oValue).doubleValue();
                        sValue = ((DecimalFormat) oCachedJavaFormatter).format(((Double) oValue).doubleValue());
                        return correctNumber(sValue, dValue);
                    }
                }
                else if (oValue instanceof NumberDataElement)
                {
                    if (oCachedJavaFormatter instanceof DecimalFormat)
                    {
                        final double dValue = ((NumberDataElement) oValue).getValue();
                        sValue = ((DecimalFormat) oCachedJavaFormatter).format(dValue);
                        return correctNumber(sValue, dValue);
                    }
                }
                else if (oValue instanceof Calendar)
                {
                    if (oCachedJavaFormatter instanceof DateFormat)
                    {
                        return ((DateFormat) oCachedJavaFormatter).format(((Calendar) oValue).getTime());
                    }
                }
                else if (oValue instanceof DateTimeDataElement)
                {
                    if (oCachedJavaFormatter instanceof DecimalFormat)
                    {
                        return ((DateFormat) oCachedJavaFormatter).format(((DateTimeDataElement) oValue)
                            .getValueAsCalendar());
                    }
                }
            }
        }
        else if (NumberFormatSpecifier.class.isInstance(fs))
        {
            final NumberFormatSpecifier nfs = (NumberFormatSpecifier) fs;
            final double dValue = asPrimitiveDouble(oValue);
            return correctNumber(nfs.format(dValue), dValue);
        }
        else if (JavaNumberFormatSpecifier.class.isInstance(fs))
        {
            final JavaNumberFormatSpecifier nfs = (JavaNumberFormatSpecifier) fs;
            final double dValue = asPrimitiveDouble(oValue);
            return correctNumber(nfs.format(dValue), dValue);
        }
        else if (DateFormatSpecifier.class.isInstance(fs))
        {
            final DateFormatSpecifier dfs = (DateFormatSpecifier) fs;
            return dfs.format(asCalendar(oValue), lcl);
        }
        else if (JavaDateFormatSpecifier.class.isInstance(fs))
        {
            final JavaDateFormatSpecifier jdfs = (JavaDateFormatSpecifier) fs;
            return jdfs.format(asCalendar(oValue));
        }
        return oValue.toString();
    }

    /**
     * 
     * @param o
     * @return
     * @throws DataFormatException
     */
    private static final double asPrimitiveDouble(Object o) throws DataFormatException
    {
        if (o instanceof Double)
        {
            return ((Double) o).doubleValue();
        }
        else if (o instanceof NumberDataElement)
        {
            return ((NumberDataElement) o).getValue();
        }
        throw new DataFormatException("Unable to convert value " + o + " to a double value");
    }

    /**
     * 
     * @param o
     * @return
     * @throws DataFormatException
     */
    private static final Calendar asCalendar(Object o) throws DataFormatException
    {
        if (o instanceof Calendar)
        {
            return (Calendar) o;
        }
        else if (o instanceof DateTimeDataElement)
        {
            return ((DateTimeDataElement) o).getValueAsCalendar();
        }
        throw new DataFormatException("Unable to convert value " + o + " to a calendar value");
    }

    /**
     * Takes care of problems while presenting -0.00
     * 
     * @param df
     * @param dValue
     * @return
     */
    public static final String correctNumber(String sValue, double dValue)
    {
        int n = (sValue.length() - sNegativeZero.length());
        final StringBuffer sb = new StringBuffer(sNegativeZero);
        for (int i = 0; i < n; i++)
        {
            sb.append('0');
        }

        if (sValue.equals(sb.toString()))
        {
            return sb.substring(1); // JUST THE ZERO IN THE EXPECTED PATTERN
            // WITHOUT THE STRAY NEGATIVE SYMBOL
        }
        return sValue;
    }
}