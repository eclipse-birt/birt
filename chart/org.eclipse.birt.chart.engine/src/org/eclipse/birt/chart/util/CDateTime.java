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

package org.eclipse.birt.chart.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class CDateTime extends GregorianCalendar
{

    /**
     *  
     */
    public CDateTime()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     *  
     */
    public CDateTime(Date d)
    {
        super();
        setTime(d);
    }

    /**
     *  
     */
    public CDateTime(Calendar c)
    {
        super();
        setTime(c.getTime());
    }

    /**
     *  
     */
    public CDateTime(long lTimeInMillis)
    {
        super();
        setTimeInMillis(lTimeInMillis);
    }

    /**
     * @param year
     * @param month
     * @param date
     */
    public CDateTime(int year, int month, int date)
    {
        super(year, month - 1, date);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     */
    public CDateTime(int year, int month, int date, int hour, int minute)
    {
        super(year, month, date, hour, minute);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param second
     */
    public CDateTime(int year, int month, int date, int hour, int minute, int second)
    {
        super(year, month, date, hour, minute, second);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param aLocale
     */
    public CDateTime(Locale aLocale)
    {
        super(aLocale);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param zone
     */
    public CDateTime(TimeZone zone)
    {
        super(zone);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @param zone
     * @param aLocale
     */
    public CDateTime(TimeZone zone, Locale aLocale)
    {
        super(zone, aLocale);
        // TODO Auto-generated constructor stub
    }

    /**
     * A convenient method used in building the ticks for a datetime scale Computes a new datetime object relative to
     * the existing one moving back by 'step' units.
     * 
     * @param iUnit
     * @param iStep
     * @return
     */
    public CDateTime backward(int iUnit, int iStep)
    {
        CDateTime cd = (CDateTime) clone();
        cd.add(iUnit, -iStep);
        return cd;
    }

    /**
     * A convenient method used in building the ticks for a datetime scale Computes a new datetime object relative to
     * the existing one moving forward by 'step' units.
     * 
     * @param iUnit
     * @param iStep
     * @return
     */
    public CDateTime forward(int iUnit, int iStep)
    {
        CDateTime cd = (CDateTime) clone();
        cd.add(iUnit, iStep);
        return cd;
    }

    public final int getYear()
    {
        return get(Calendar.YEAR);
    }

    public final int getMonth()
    {
        return get(Calendar.MONTH);
    }

    public final int getDay()
    {
        return get(Calendar.DATE);
    }

    public final int getHour()
    {
        return get(Calendar.HOUR_OF_DAY);
    }

    public final int getMinute()
    {
        return get(Calendar.MINUTE);
    }

    public final int getSecond()
    {
        return get(Calendar.SECOND);
    }

    /**
     * Returns the most significant datetime unit in which there's a difference or 0 if there is no difference.
     * 
     * @return
     */
    public static final int getDifference(CDateTime cdt1, CDateTime cdt2)
    {
        if (cdt1.getYear() != cdt2.getYear())
        {
            return Calendar.YEAR;
        }
        else if (cdt1.getMonth() != cdt2.getMonth())
        {
            return Calendar.MONTH;
        }
        else if (cdt1.getDay() != cdt2.getDay())
        {
            return Calendar.DATE;
        }
        else if (cdt1.getHour() != cdt2.getHour())
        {
            return Calendar.HOUR_OF_DAY;
        }
        else if (cdt1.getMinute() != cdt2.getMinute())
        {
            return Calendar.MINUTE;
        }
        else if (cdt1.getSecond() != cdt2.getSecond())
        {
            return Calendar.SECOND;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Returns a preferred format specifier for tick labels that represent axis values that will be computed based on
     * the difference between cdt1 and cdt2
     * 
     * @return
     */
    public static final String getPreferredFormat(int iUnit)
    {
        if (iUnit == Calendar.YEAR)
        {
            return "yyyy";
        }
        else if (iUnit == Calendar.MONTH)
        {
            return "MMM yyyy";
        }
        else if (iUnit == Calendar.DATE)
        {
            return "MM-dd-yyyy";
        }
        else if (iUnit == Calendar.HOUR_OF_DAY)
        {
            return "MM-dd-yy\nHH:mm";
        }
        else if (iUnit == Calendar.MINUTE)
        {
            return "HH:mm:ss";
        }
        else if (iUnit == Calendar.SECOND)
        {
            return "HH:mm:ss";
        }
        return null;
    }

    private static final int MILLIS_IN_SECOND = 1000;

    private static final int MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60;

    private static final int MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;

    private static final int MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;

    /**
     * Computes the difference between two given dates as a fraction for the requested field
     * 
     * @param cdt1
     * @param cdt2
     * @param iUnit
     * 
     * @return
     */
    public static final double computeDifference(CDateTime cdt1, CDateTime cdt2, int iUnit)
    {
        final long l1 = cdt1.getTimeInMillis();
        final long l2 = cdt2.getTimeInMillis();
        if (iUnit == Calendar.MILLISECOND)
        {
            return (l1 - l2);
        }
        else if (iUnit == Calendar.SECOND)
        {
            return (l1 - l2) / MILLIS_IN_SECOND;
        }
        else if (iUnit == Calendar.MINUTE)
        {
            return (l1 - l2) / MILLIS_IN_MINUTE;
        }
        else if (iUnit == Calendar.HOUR_OF_DAY)
        {
            return (l1 - l2) / MILLIS_IN_HOUR;
        }
        else if (iUnit == Calendar.DATE)
        {
            return (l1 - l2) / MILLIS_IN_DAY;
        }
        else if (iUnit == Calendar.MONTH)
        {
            final double dDays = computeDifference(cdt1, cdt2, Calendar.DATE);
            return dDays / 30.4375;
        }
        else if (iUnit == Calendar.YEAR)
        {
            final double dDays = computeDifference(cdt1, cdt2, Calendar.DATE);
            return dDays / 365.25;
        }
        return 0;
    }

    private static final int[] iaUnitTypes =
    {
        Calendar.MILLISECOND, Calendar.SECOND, Calendar.MINUTE, Calendar.HOUR_OF_DAY, Calendar.DATE, Calendar.MONTH,
        Calendar.YEAR
    };

    public static final int computeUnit(DataSetIterator dsi)
    {
        Calendar cCurr, cPrev;

        for (int k = 0; k < iaUnitTypes.length; k++)
        {
            cPrev = (Calendar) dsi.last();
            dsi.reset();
            while (dsi.hasNext())
            {
                cCurr = (Calendar) dsi.next();
                if (cCurr.get(iaUnitTypes[k]) != cPrev.get(iaUnitTypes[k]))
                {
                    return iaUnitTypes[k]; // THE UNIT FOR WHICH A DIFFERENCE WAS NOTED
                }
                cPrev = cCurr;
            }
        }
        return IConstants.UNDEFINED;
    }

    public static final int computeUnit(CDateTime[] cdta)
    {
        int j;
        for (int k = 0; k < iaUnitTypes.length; k++)
        {
            for (int i = 0; i < cdta.length; i++)
            {
                j = i + 1;
                if (j > cdta.length - 1)
                    j = 0;
                if (cdta[i].get(iaUnitTypes[k]) != cdta[j].get(iaUnitTypes[k]))
                {
                    return iaUnitTypes[k]; // THE UNIT FOR WHICH A DIFFERENCE WAS NOTED
                }
            }
        }
        return IConstants.UNDEFINED;
    }

    public static final int getMaximumDaysIn(int iMonth, int iYear)
    {
        CDateTime cdt = new CDateTime();
        cdt.set(Calendar.YEAR, iYear);
        cdt.set(Calendar.MONTH, iMonth);
        return cdt.getActualMaximum(Calendar.DATE);
    }

    public static final int getMaximumDaysIn(int iYear)
    {
        CDateTime cdt = new CDateTime();
        cdt.set(Calendar.YEAR, iYear);
        return cdt.getActualMaximum(Calendar.DAY_OF_YEAR);
    }

    public static final double inMillis(int iUnit)
    {
        if (iUnit == Calendar.SECOND)
        {
            return MILLIS_IN_SECOND;
        }
        else if (iUnit == Calendar.MINUTE)
        {
            return MILLIS_IN_MINUTE;
        }
        else if (iUnit == Calendar.HOUR)
        {
            return MILLIS_IN_HOUR;
        }
        else if (iUnit == Calendar.DATE)
        {
            return MILLIS_IN_DAY;
        }
        else if (iUnit == Calendar.MONTH)
        {
            return MILLIS_IN_DAY * 30.4375;
        }
        else if (iUnit == Calendar.YEAR)
        {
            return MILLIS_IN_DAY * 365.25;
        }
        return 0;
    }

    public final void clearBelow(int iUnit)
    {
        if (iUnit == YEAR)
        {
            set(Calendar.MILLISECOND, 0);
            set(Calendar.SECOND, 0);
            set(Calendar.MINUTE, 0);
            set(Calendar.HOUR, 0);
            set(Calendar.DATE, 1);
            set(Calendar.MONTH, 0);
        }
        else if (iUnit == MONTH)
        {
            set(Calendar.MILLISECOND, 0);
            set(Calendar.SECOND, 0);
            set(Calendar.MINUTE, 0);
            set(Calendar.HOUR, 0);
            set(Calendar.DATE, 1);
        }
        else if (iUnit == DATE)
        {
            set(Calendar.MILLISECOND, 0);
            set(Calendar.SECOND, 0);
            set(Calendar.MINUTE, 0);
            set(Calendar.HOUR, 0);
        }
        else if (iUnit == HOUR)
        {
            set(Calendar.MILLISECOND, 0);
            set(Calendar.SECOND, 0);
            set(Calendar.MINUTE, 0);
        }
        else if (iUnit == MINUTE)
        {
            set(Calendar.MILLISECOND, 0);
            set(Calendar.SECOND, 0);
        }
    }

    private static final SimpleDateFormat _sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public final String toString()
    {
        return _sdf.format(getTime());
    }

    public static CDateTime parse(String s)
    {
        try
        {
            return new CDateTime(_sdf.parse(s));
        }
        catch (Exception ex )
        {
            return null;
        }
    }
}