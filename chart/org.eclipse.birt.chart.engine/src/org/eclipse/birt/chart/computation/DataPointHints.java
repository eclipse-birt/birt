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

import java.util.Calendar;
import java.util.Locale;

import org.eclipse.birt.chart.exception.UndefinedValueException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.emf.common.util.EList;

/**
 *  
 */
public final class DataPointHints
{

    private final Locale lcl;

    /**
     *  
     */
    private final Object oBaseValue;

    /**
     *  
     */
    private final Object oOrthogonalValue;

    /**
     *  
     */
    private final Object oSeriesValue;

    /**
     *  
     */
    private final Location lo;

    /**
     *  
     */
    private final double dSize;

    /**
     *  
     */
    private final DataPoint dp;

    /**
     * 
     */
    private final FormatSpecifier fsBase, fsOrthogonal, fsSeries;
    
    /**
     * 
     * @param _oBaseValue
     * @param _oOrthogonalValue
     * @param _sSeriesValue
     * @param _dp
     * @param _lo
     * @param _dSize
     * @param _lcl
     * @throws UndefinedValueException
     */
    public DataPointHints(Object _oBaseValue, Object _oOrthogonalValue, Object _oSeriesValue, 
        DataPoint _dp, // FOR COMBINED VALUE RETRIEVAL
        FormatSpecifier _fsBase, FormatSpecifier _fsOrthogonal, FormatSpecifier _fsSeries, 
        Location _lo, double _dSize, Locale _lcl) throws UndefinedValueException
    {
        if (_dp == null)
        {
            throw new UndefinedValueException("The DataPoint value associated with the series definition is undefined");
        }
        dp = _dp;
        oBaseValue = _oBaseValue;
        oOrthogonalValue = _oOrthogonalValue;
        oSeriesValue = _oSeriesValue;
        
        fsBase = _fsBase;
        fsOrthogonal = _fsOrthogonal;
        fsSeries = _fsSeries;
        
        lo = _lo;
        lcl = (_lcl == null) ? Locale.getDefault() : _lcl;
        dSize = _dSize;
    }

    /**
     * 
     * @return
     */
    public final Object getBaseValue()
    {
        return oBaseValue;
    }

    /**
     * 
     * @return
     */
    public final Object getOrthogonalValue()
    {
        return oOrthogonalValue;
    }

    /**
     * 
     * @return
     */
    public final Object getSeriesValue()
    {
        return oSeriesValue;
    }

    /**
     * 
     * @return
     */
    public final Location getLocation()
    {
        return lo;
    }

    /**
     * 
     * @return
     */
    public final double getSize()
    {
        return dSize;
    }

    /**
     * 
     * @return
     */
    public final String getOrthogonalDisplayValue()
    {
        return getOrthogonalDisplayValue(fsOrthogonal);
    }

    /**
     * 
     * @return
     */
    public final String getBaseDisplayValue()
    {
        return getBaseDisplayValue(fsBase);
    }

    /**
     * 
     * @return
     */
    public final String getSeriesDisplayValue()
    {
        return getSeriesDisplayValue(fsSeries);
    }

    /**
     * 
     * @return
     */
    private final String getBaseDisplayValue(FormatSpecifier fs)
    {
        if (oBaseValue == null)
        {
            return IConstants.NULL_STRING;
        }
        try
        {
            return ValueFormatter.format(oBaseValue, fs, lcl, null);
        }
        catch (Exception ex )
        {
            DefaultLoggerImpl.instance().log(ILogger.ERROR,
                "Failed to parse value " + oBaseValue + " with format specifier " + fs);
        }
        return IConstants.NULL_STRING;
    }

    /**
     * 
     * @return
     */
    private final String getOrthogonalDisplayValue(FormatSpecifier fs)
    {
        if (oOrthogonalValue == null)
        {
            return IConstants.NULL_STRING;
        }
        try
        {
            return ValueFormatter.format(oOrthogonalValue, fs, lcl, null);
        }
        catch (Exception ex )
        {
            DefaultLoggerImpl.instance().log(ILogger.ERROR,
                "Failed to parse value " + oOrthogonalValue + " with format specifier " + fs);
        }
        return IConstants.NULL_STRING;
    }

    /**
     * 
     * @return
     */
    private final String getSeriesDisplayValue(FormatSpecifier fs)
    {
        if (oSeriesValue == null)
        {
            return IConstants.NULL_STRING;
        }
        try
        {
            return ValueFormatter.format(oSeriesValue, fs, lcl, null);
        }
        catch (Exception ex )
        {
            DefaultLoggerImpl.instance().log(ILogger.ERROR,
                "Failed to parse value " + oSeriesValue + " with format specifier " + fs);
        }
        return IConstants.NULL_STRING;
    }
    
    /**
     * 
     * @return
     */
    public final String getDisplayValue()
    {
        final EList el = dp.getComponents();
        final StringBuffer sb = new StringBuffer();

        if (dp.getPrefix() != null)
        {
            sb.append(dp.getPrefix());
        }
        DataPointComponent dpc;
        DataPointComponentType dpct;

        for (int i = 0; i < el.size(); i++)
        {
            dpc = (DataPointComponent) el.get(i);
            dpct = dpc.getType();
            if (dpct == DataPointComponentType.BASE_VALUE_LITERAL)
            {
                sb.append(getBaseDisplayValue());
            }
            else if (dpct == DataPointComponentType.ORTHOGONAL_VALUE_LITERAL)
            {
                sb.append(getOrthogonalDisplayValue());
            }
            else if (dpct == DataPointComponentType.SERIES_VALUE_LITERAL)
            {
                sb.append(getOrthogonalDisplayValue());
            }

            if (i < el.size() - 1)
            {
                sb.append(dp.getSeparator());
            }
        }
        if (dp.getSuffix() != null)
        {
            sb.append(dp.getSuffix());
        }
        return sb.toString();
    }

    /**
     * 
     * @param o
     * @return
     */
    private final double asDouble(Object o)
    {
        if (o instanceof Double)
        {
            return ((Double) o).doubleValue();
        }
        else if (o instanceof NumberDataElement)
        {
            return ((NumberDataElement) o).getValue();
        }
        return 0;
    }

    /**
     * 
     * @param o
     * @return
     */
    private final Calendar asCalendar(Object o)
    {
        if (o instanceof Calendar)
        {
            return (Calendar) o;
        }
        else if (o instanceof DateTimeDataElement)
        {
            return ((DateTimeDataElement) o).getValueAsCalendar();
        }
        return null;
    }

    /**
     *  
     */
    public final String toString()
    {
        return super.toString() + ":" + getDisplayValue();
    }
}