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

package org.eclipse.birt.chart.computation.withaxes;

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.RotatedRectangle;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.exception.DataFormatException;
import org.eclipse.birt.chart.exception.GenerationException;
import org.eclipse.birt.chart.exception.UnexpectedInputException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.util.CDateTime;

/**
 * Encapsulates the auto scaling algorithms used by the rendering and chart
 * computation framework.
 */
public final class AutoScale extends Methods implements Cloneable
{

    /**
     *  
     */
    private final int iType;

    /**
     *  
     */
    private Object oMinimum;

    /**
     *  
     */
    private Object oMaximum;

    /**
     *  
     */
    private Object oStep;

    /**
     *  
     */
    private Object oUnit;

    /**
     *  
     */
    private double dStartShift;

    /**
     *  
     */
    private double dEndShift;

    /**
     *  
     */
    private transient double dStart, dEnd;

    /**
     *  
     */
    private transient double[] daTickCoordinates;

    /**
     *  
     */
    private transient DataSetIterator dsiData;

    /**
     *  
     */
    private transient boolean bCategoryScale = false;

    /**
     *  
     */
    private RunTimeContext rtc;;

    /**
     * A default numeric pattern for integer number representation of axis labels
     */
    private static final String sNumericPattern = "0"; //$NON-NLS-1$

    /**
     * Quick static lookup for linear scaling
     */
    private static int[] iaLinearDeltas =
    {
        1, 2, 5
    };

    /**
     * Quick static lookup for logarithmic scaling
     */
    //private static int[] iaLogarithmicDeltas = { 2, 4, 5, 10 };
    private static int[] iaLogarithmicDeltas =
    {
        10
    };

    /**
     * Quick static lookup for datetime scaling
     */
    private static int[] iaCalendarUnits =
    {
        Calendar.SECOND, Calendar.MINUTE, Calendar.HOUR_OF_DAY, Calendar.DATE, Calendar.MONTH, Calendar.YEAR
    };

    /**
     *  
     */
    private static int[] iaSecondDeltas =
    {
        1, 5, 10, 15, 20, 30
    };

    /**
     *  
     */
    private static int[] iaMinuteDeltas =
    {
        1, 5, 10, 15, 20, 30
    };

    /**
     *  
     */
    private static int[] iaHourDeltas =
    {
        1, 2, 3, 4, 12
    };

    /**
     *  
     */
    private static int[] iaDayDeltas =
    {
        1, 7, 14
    };

    /**
     *  
     */
    private static int[] iaMonthDeltas =
    {
        1, 2, 3, 4, 6
    };

    /**
     *  
     */
    private static int[][] iaCalendarDeltas =
    {
        iaSecondDeltas, iaMinuteDeltas, iaHourDeltas, iaDayDeltas, iaMonthDeltas, null
    };

    /**
     *  
     */
    private boolean bIntegralZoom = true;

    /**
     *  
     */
    private boolean bMinimumFixed = false;

    /**
     *  
     */
    private boolean bMaximumFixed = false;

    /**
     *  
     */
    private boolean bStepFixed = false;

    /**
     *  
     */
    FormatSpecifier fs = null;

    /**
     * 
     * @param _iType
     */
    AutoScale(int _iType)
    {
        iType = _iType;
    }

    /**
     * 
     * @param _iType
     * @param _oMinimum
     * @param _oMaximum
     * @param _oStep
     */
    AutoScale(int _iType, Object _oMinimum, Object _oMaximum, Object _oStep)
    {
        oMinimum = _oMinimum;
        oMaximum = _oMaximum;
        oStep = _oStep;
        iType = _iType;
    }

    /**
     * 
     * @param _iType
     * @param _oMinimum
     * @param _oMaximum
     * @param _oUnit
     * @param _oStep
     */
    AutoScale(int _iType, Object _oMinimum, Object _oMaximum, Object _oUnit, Object _oStep)
    {
        oMinimum = _oMinimum;
        oMaximum = _oMaximum;
        oUnit = _oUnit;
        oStep = _oStep;
        iType = _iType;
    }

    final void setFixed(boolean _bMinimum, boolean _bMaximum, boolean _bStep)
    {
        bMinimumFixed = _bMinimum;
        bMaximumFixed = _bMaximum;
        bStepFixed = _bStep;
    }

    /**
     *  
     */
    public final Object clone()
    {
        final AutoScale sc = new AutoScale(iType, oMinimum, oMaximum, oStep);
        sc.dStart = dStart;
        sc.dEnd = dEnd;
        sc.daTickCoordinates = daTickCoordinates;
        sc.dStartShift = dStartShift;
        sc.dEndShift = dEndShift;
        sc.dsiData = dsiData;
        sc.oStep = oStep;
        sc.oUnit = oUnit;
        sc.bMaximumFixed = bMaximumFixed;
        sc.bMinimumFixed = bMinimumFixed;
        sc.bStepFixed = bStepFixed;
        sc.fs = fs;
        sc.rtc = rtc;
        sc.bCategoryScale = bCategoryScale;
        return sc;
    }

    /**
     * Zooms IN 'once' into a scale of type numerical or datetime Typically, this is called in a loop until label
     * overlaps occur
     */
    final boolean zoomIn()
    {
        if (bStepFixed)
            return false; // CANNOT ZOOM FOR FIXED STEPS

        if ((iType & NUMERICAL) == NUMERICAL)
        {
            if ((iType & LOGARITHMIC) == LOGARITHMIC)
            {
                final double dStep = asDouble(oStep).doubleValue();
                if ((Math.log(dStep) / LOG_10) > 1)
                {
                    oStep = new Double(dStep / 10);
                }
                else
                {
                    int n = iaLogarithmicDeltas.length;
                    for (int i = n - 1; i >= 0; i--)
                    {
                        if ((int) dStep == iaLogarithmicDeltas[i])
                        {
                            if (i > 0)
                            {
                                oStep = new Double(iaLogarithmicDeltas[i - 1]);
                                return true;
                            }
                            else
                            {
                                return false;
                            }
                        }
                    }
                    return false;
                }
            }
            else if ((iType & LINEAR) == LINEAR)
            {
                double dStep = asDouble(oStep).doubleValue();
                if (bIntegralZoom)
                {
                    double dPower = (Math.log(dStep) / LOG_10);
                    dPower = Math.floor(dPower);
                    dPower = Math.pow(10.0, dPower);
                    dStep /= dPower;
                    dStep = Math.round(dStep);
                    int n = iaLinearDeltas.length;
                    for (int i = 0; i < n; i++)
                    {
                        if ((int) dStep == iaLinearDeltas[i])
                        {
                            if (i > 0)
                            {
                                dStep = iaLinearDeltas[i - 1] * dPower;
                            }
                            else
                            {
                                dPower /= 10;
                                dStep = iaLinearDeltas[n - 1] * dPower;
                            }
                            break;
                        }
                    }
                    oStep = new Double(dStep);
                }
                else
                {
                    dStep /= 2;
                    oStep = new Double(dStep);
                }
            }
        }
        else if ((iType & DATE_TIME) == DATE_TIME)
        {
            int[] ia = null;
            int iStep = asInteger(oStep);
            int iUnit = asInteger(oUnit);

            for (int icu = 0; icu < iaCalendarUnits.length; icu++)
            {
                if (iUnit == iaCalendarUnits[icu])
                {
                    ia = iaCalendarDeltas[icu];
                    if (ia == null) // HANDLE YEARS SEPARATELY
                    {
                        iStep--;
                        if (iStep == 0)
                        {
                            oStep = new Integer(iaMonthDeltas[iaMonthDeltas.length - 1]);
                            oUnit = new Integer(Calendar.MONTH);
                        }
                    }
                    else
                    // HANDLE SECONDS, MINUTES, HOURS, DAYS, MONTHS
                    {
                        int i = 0;
                        for (; i < ia.length; i++)
                        {
                            if (ia[i] == iStep)
                            {
                                break;
                            }
                        }

                        if (i == 0) // WE'RE AT THE FIRST ELEMENT IN THE
                        // DELTAS ARRAY
                        {
                            if (icu == 0)
                                return false; // CAN'T ZOOM ANYMORE THAN
                            // 1-SECOND INTERVALS (AT INDEX=0)
                            ia = iaCalendarDeltas[icu - 1]; // DOWNGRADE ARRAY
                            // TO PREVIOUS
                            // DELTAS ARRAY
                            i = ia.length; // MANIPULATE OFFSET TO END+1
                            oUnit = new Integer(iaCalendarUnits[icu - 1]); // DOWNGRADE
                            // UNIT
                        }
                        oStep = new Integer(ia[i - 1]); // RETURN PREVIOUS
                        // STEP IN DELTAS
                        // ARRAY
                        break;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Zooms OUT 'once' into a scale of type numerical or datetime Typically, this is called in a loop until label
     * overlaps occur
     */
    final boolean zoomOut()
    {
        if (bStepFixed)
            return false;

        if ((iType & NUMERICAL) == NUMERICAL)
        {
            if ((iType & LOGARITHMIC) == LOGARITHMIC)
            {
                final double dStep = asDouble(oStep).doubleValue();
                if ((Math.log(dStep) / LOG_10) >= 1)
                {
                    oStep = new Double(dStep * 10);
                }
                else
                {
                    final int n = iaLogarithmicDeltas.length;
                    for (int i = 0; i < n; i++)
                    {
                        if ((int) dStep == iaLogarithmicDeltas[i])
                        {
                            oStep = new Double(iaLogarithmicDeltas[i + 1]);
                            return true;
                        }
                    }
                    return false;
                }
            }
            else if ((iType & LINEAR) == LINEAR)
            {
                double dStep = asDouble(oStep).doubleValue();
                if (bIntegralZoom)
                {
                    double dPower = (Math.log(dStep) / LOG_10);
                    if (dPower < 0)
                    {
                        dPower = Math.floor(dPower);
                    }
                    dPower = Math.pow(10, dPower);
                    dStep /= dPower;
                    dStep = Math.round(dStep);
                    int n = iaLinearDeltas.length;
                    int i = 0;
                    for (; i < n; i++)
                    {
                        if ((double) dStep == iaLinearDeltas[i])
                        {
                            if (i < n - 1)
                            {
                                dStep = iaLinearDeltas[i + 1] * dPower;
                                if (dStep > 1)
                                    dStep = Math.round(dStep);
                            }
                            else
                            {
                                dPower *= 10;
                                dStep = iaLinearDeltas[0] * dPower;
                                if (dStep > 1)
                                    dStep = Math.round(dStep);
                            }
                            break;
                        }
                    }
                    if (i == n)
                    {
                        throw new RuntimeException(
                            new ChartException(
                                "exception.step.zoom.out", //$NON-NLS-1$
                                new Object[] { new Double(dStep) },
                                ResourceBundle.getBundle(
                                    Messages.ENGINE, 
                                    rtc.getLocale()
                                )
                            )
                        ); // i18n_CONCATENATIONS_REMOVED 
                    }
                    oStep = new Double(dStep);
                }
                else
                {
                    dStep *= 2;
                    oStep = new Double(dStep);
                }
            }
        }
        else if ((iType & DATE_TIME) == DATE_TIME)
        {
            int[] ia = null;
            int iStep = asInteger(oStep);
            int iUnit = asInteger(oUnit);

            for (int icu = 0; icu < iaCalendarUnits.length; icu++)
            {
                if (iUnit == iaCalendarUnits[icu])
                {
                    ia = iaCalendarDeltas[icu];
                    if (ia == null) // HANDLE YEARS SEPARATELY
                    {
                        iStep++; // NO UPPER LIMIT FOR YEARS
                    }
                    else
                    // HANDLE SECONDS, MINUTES, HOURS, DAYS, MONTHS
                    {
                        int i = 0, n = ia.length;
                        for (; i < n; i++)
                        {
                            if (ia[i] == iStep)
                            {
                                break;
                            }
                        }

                        if (i == n - 1) // WE'RE AT THE LAST ELEMENT IN THE
                        // DELTAS ARRAY
                        {
                            ia = iaCalendarDeltas[icu + 1]; // UPGRADE UNIT TO
                            // NEXT DELTAS ARRAY
                            oUnit = new Integer(iaCalendarUnits[icu + 1]);
                            if (ia == null) // HANDLE YEARS
                            {
                                oStep = new Integer(1);
                                return false; // DO NO PROCEED FOR YEARS
                            }
                            i = -1; // MANIPULATE OFFSET TO START-1
                        }
                        oStep = new Integer(ia[i + 1]); // RETURN NEXT STEP IN
                        // DELTAS ARRAY
                        break;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns an auto computed decimal format pattern for representing axis labels on a numeric axis This is used for
     * representing logarithmic values
     * 
     * @return
     */
    public final String getNumericPattern(double dValue)
    {

        if (dValue - (int) dValue == 0) // IF MANTISSA IS INSIGNIFICANT, SHOW
        // LABELS AS INTEGERS
        {
            return sNumericPattern;
        }

        final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        final String sMinimum = String.valueOf(dValue);
        final int iDecimalPosition = sMinimum.indexOf(dfs.getDecimalSeparator()); // THIS RELIES ON THE FACT THAT IN
        // ANY LOCALE, DECIMAL IS A DOT
        if (iDecimalPosition >= 0)
        {
            int n = sMinimum.length();
            for (int i = n - 1; i > 0; i--)
            {
                if (sMinimum.charAt(i) == '0')
                {
                    n--;
                }
                else
                {
                    break;
                }
            }
            final int iMantissaCount = n - 1 - iDecimalPosition;
            final StringBuffer sb = new StringBuffer(sNumericPattern);
            sb.append('.');
            for (int i = 0; i < iMantissaCount; i++)
            {
                sb.append('0');
            }
            return sb.toString();
        }
        return sNumericPattern;
    }

    /**
     * Returns an auto computed decimal format pattern for representing axis labels on a numeric axis
     * 
     * @return
     */
    public final String getNumericPattern()
    {
        if (oMinimum == null || oStep == null)
        {
            return "0.00"; //$NON-NLS-1$
        }
        final double dMinValue = asDouble(oMinimum).doubleValue();
        final double dStep = asDouble(oStep).doubleValue();

        if ((iType & LOGARITHMIC) == LOGARITHMIC)
        {
            final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            final String sMinimum = oMinimum.toString();
            final int iDecimalPosition = sMinimum.indexOf(dfs.getDecimalSeparator()); 
            if (iDecimalPosition >= 0)
            {
                int n = sMinimum.length();
                for (int i = n - 1; i > 0; i--)
                {
                    if (sMinimum.charAt(i) == '0')
                    {
                        n--;
                    }
                    else
                    {
                        break;
                    }
                }
                final int iMantissaCount = n - 1 - iDecimalPosition;
                final StringBuffer sb = new StringBuffer(sNumericPattern);
                sb.append('.');
                for (int i = 0; i < iMantissaCount; i++)
                {
                    sb.append('0');
                }
                return sb.toString();
            }
            else
            {
                return sNumericPattern;
            }

        }

        if (dMinValue - (int) dMinValue == 0 // IF MANTISSA IS INSIGNIFICANT, SHOW LABELS AS INTEGERS
            && dStep - (int) dStep == 0)
        {
            return sNumericPattern;
        }

        final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        final String sStep = oStep.toString();
        final int iDecimalPosition = sStep.indexOf(dfs.getDecimalSeparator()); 
        if (iDecimalPosition >= 0)
        {
            int n = sStep.length();
            for (int i = n - 1; i > 0; i--)
            {
                if (sStep.charAt(i) == '0')
                {
                    n--;
                }
                else
                {
                    break;
                }
            }
            final int iMantissaCount = n - 1 - iDecimalPosition;
            final StringBuffer sb = new StringBuffer(sNumericPattern);
            sb.append('.');
            for (int i = 0; i < iMantissaCount; i++)
            {
                sb.append('0');
            }
            return sb.toString();
        }
        else
        {
            return sNumericPattern;
        }
    }

    /**
     * 
     * @return
     */
    public final int getType()
    {
        return iType;
    }

    /**
     * 
     * @param _oaData
     */
    final void setData(DataSetIterator _oaData)
    {
        dsiData = _oaData;
    }

    /**
     * 
     * @return
     */
    public final Object getUnit()
    {
        return oUnit;
    }

    /**
     * 
     * @return
     */
    public final DataSetIterator getData()
    {
        return dsiData;
    }

    /**
     * 
     * @param da
     */
    final void setTickCordinates(double[] da)
    {
        if (da != null && da.length == 1)
        {
            throw new RuntimeException(
                new ChartException(
                    "exception.tick.computations", //$NON-NLS-1$ 
                    ResourceBundle.getBundle(
                        Messages.ENGINE, 
                        rtc.getLocale()
                    )
                )
            );
        }
        daTickCoordinates = da;
    }

    /**
     * 
     * @return
     */
    public final double[] getTickCordinates()
    {
        return daTickCoordinates;
    }

    /**
     * 
     * @return
     */
    public final double[] getEndPoints()
    {
        return new double[]
        {
            dStart, dEnd
        };
    }

    /**
     * 
     * @param _dStart
     * @param _dEnd
     */
    final void setEndPoints(double _dStart, double _dEnd)
    {
        if (_dStart != -1)
        {
            dStart = _dStart;
        }

        if (_dEnd != -1)
        {
            dEnd = _dEnd;
        }

        if (daTickCoordinates != null)
        {
            int n = daTickCoordinates.length - 1;
            double dDelta = (dEnd - dStart) / n;
            double d = dStart;

            for (int i = 0; i < n; i++)
            {
                daTickCoordinates[i] = d;
                d += dDelta;
            }
            daTickCoordinates[n] = dEnd;
        }
    }

    /**
     * 
     * @return
     */
    final int getTickCount()
    {
        int nTicks = 2;
        if ((iType & TEXT) == TEXT || bCategoryScale)
        {
            if (dsiData != null)
            {
                nTicks = dsiData.size() + 1;
            }
        }
        else if ((iType & NUMERICAL) == NUMERICAL)
        {
            if ((iType & LINEAR) == LINEAR)
            {
                double dMax = asDouble(oMaximum).doubleValue();
                double dMin = asDouble(oMinimum).doubleValue();
                double dStep = asDouble(oStep).doubleValue();
                nTicks = (int) Math.round((dMax - dMin) / dStep) + 1;
                if (nTicks < 2)
                {
                    nTicks = 2;
                }
            }
            else if ((iType & LOGARITHMIC) == LOGARITHMIC)
            {
                double dMax = asDouble(oMaximum).doubleValue();
                double dMin = asDouble(oMinimum).doubleValue();
                double dStep = asDouble(oStep).doubleValue();

                double dMaxLog = (Math.log(dMax) / LOG_10);
                double dMinLog = (Math.log(dMin) / LOG_10);
                double dStepLog = (Math.log(dStep) / LOG_10);

                nTicks = (int) Math.round((dMaxLog - dMinLog) / dStepLog) + 1;
                if (nTicks < 2)
                {
                    nTicks = 2;
                }
            }
        }
        else if ((iType & DATE_TIME) == DATE_TIME)
        {
            final CDateTime cdt1 = (CDateTime) oMinimum;
            final CDateTime cdt2 = (CDateTime) oMaximum;
            nTicks = (int) (Math.ceil(CDateTime.computeDifference(cdt2, cdt1, asInteger(oUnit))) / asInteger(oStep)) + 1;
            if (nTicks < 2)
            {
                nTicks = 2;
            }
        }
        return nTicks;
    }

    /**
     * 
     * @return
     */
    public final double getUnitSize()
    {
        if (daTickCoordinates == null)
        {
            throw new RuntimeException(
                new ChartException(
                    "exception.unit.size.failure", //$NON-NLS-1$ 
                    ResourceBundle.getBundle(
                        Messages.ENGINE, 
                        rtc.getLocale()
                    )
                )
            ); 
        }
        return Math.abs(daTickCoordinates[1] - daTickCoordinates[0]);
    }

    /**
     * 
     * @return
     */
    public final Object getMinimum()
    {
        return oMinimum;
    }

    /**
     * 
     * @return
     */
    public final Object getMaximum()
    {
        return oMaximum;
    }

    /**
     * 
     * @return
     */
    public final Object getStep()
    {
        return oStep;
    }

    /**
     * 
     * @param dValueMin
     * @param dValueMax
     */
    final void adjustAxisMinMax(CDateTime cdtMinValue, CDateTime cdtMaxValue)
    {
        int iUnit = asInteger(oUnit);
        int iStep = asInteger(oStep);

        if (!bMinimumFixed)
        {
            oMinimum = cdtMinValue.backward(iUnit, iStep);
        }

        if (!bMaximumFixed)
        {
            oMaximum = cdtMaxValue.forward(iUnit, iStep);
        }
        ((CDateTime) oMinimum).clearBelow(iUnit);
        ((CDateTime) oMaximum).clearBelow(iUnit);
        /*
         * SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); String sMin = sdf.format(((CDateTime)
         * oMinimum).getTime()); String sMax = sdf.format(((CDateTime) oMaximum).getTime());
         */
    }

    /**
     * 
     * @return
     */
    final Object[] getMinMax()
    {
        if ((iType & NUMERICAL) == NUMERICAL)
        {
            Object oValue;
            double dValue, dMinValue = Double.MAX_VALUE, dMaxValue = Double.MIN_VALUE;
            dsiData.reset();
            while (dsiData.hasNext())
            {
                oValue = dsiData.next();
                if (oValue == null) // NULL VALUE CHECK
                {
                    continue;
                }
                dValue = ((Double) oValue).doubleValue();
                if (dValue < dMinValue)
                    dMinValue = dValue;
                if (dValue > dMaxValue)
                    dMaxValue = dValue;
            }

            return new Object[]
            {
                new Double(dMinValue), new Double(dMaxValue)
            };
        }
        else if ((iType & DATE_TIME) == DATE_TIME)
        {
            Calendar cValue;
            Calendar caMin = null, caMax = null;
            dsiData.reset();
            while (dsiData.hasNext())
            {
                cValue = (Calendar) dsiData.next();
                if (caMin == null)
                {
                    caMin = cValue;
                }
                if (caMax == null)
                {
                    caMax = cValue;
                }
                if (cValue == null) // NULL VALUE CHECK
                {
                    continue;
                }
                if (cValue.before(caMin))
                    caMin = cValue;
                else if (cValue.after(caMax))
                    caMax = cValue;
            }
            return new Object[]
            {
                new CDateTime(caMin), new CDateTime(caMax)
            };
        }
        return null;
    }

    /**
     * 
     * @param oMinValue
     * @param oMaxValue
     */
    final void updateAxisMinMax(Object oMinValue, Object oMaxValue)
    {
        if ((iType & LOGARITHMIC) == LOGARITHMIC)
        {
            if ((iType & PERCENT) == PERCENT)
            {
                oMaximum = new Double(100);
                oMinimum = new Double(1);
                oStep = new Double(10);
                bMaximumFixed = true;
                bMinimumFixed = true;
                bStepFixed = true;
                return;
            }

            final double dMinValue = asDouble(oMinValue).doubleValue();
            final double dMaxValue = asDouble(oMaxValue).doubleValue();
            final double dAbsMax = Math.abs(dMaxValue);
            final double dAbsMin = Math.abs(dMinValue);
            final double dStep = asDouble(oStep).doubleValue();
            final double dStepLog = Math.log(dStep);

            int iPow = (int) Math.floor(Math.log(dAbsMax) / dStepLog) + 1;
            double dMaxAxis = Math.pow(dStep, iPow);
            iPow = (int) Math.floor(Math.log(dAbsMin) / dStepLog) - 1;
            double dMinAxis = Math.pow(dStep, iPow + 1);
            if (!bMaximumFixed)
            {
                oMaximum = new Double(dMaxAxis);
            }
            if (!bMinimumFixed)
            {
                oMinimum = new Double(dMinAxis);
            }
        }
        else if ((iType & DATE_TIME) == DATE_TIME)
        {
            int iUnit = asInteger(oUnit);
            int iStep = asInteger(oStep);
            CDateTime cdtMinValue = asDateTime(oMinValue);
            CDateTime cdtMaxValue = asDateTime(oMaxValue);

            if (!bMinimumFixed)
            {
                oMinimum = cdtMinValue.backward(iUnit, iStep);
            }
            ((CDateTime) oMinimum).clearBelow(iUnit);
            if (!bMaximumFixed)
            {
                oMaximum = cdtMaxValue.forward(iUnit, iStep);
            }
            ((CDateTime) oMaximum).clearBelow(iUnit);
        }
        else
        {
            final double dMinValue = asDouble(oMinValue).doubleValue();
            final double dMaxValue = asDouble(oMaxValue).doubleValue();
            final double dAbsMax = Math.abs(dMaxValue);
            final double dAbsMin = Math.abs(dMinValue);
            final double dStep = asDouble(oStep).doubleValue();

            double dMinAxis = (dStep > 1) ? Math.floor(dAbsMin / dStep) : Math.round(dAbsMin / dStep);
            dMinAxis *= dStep;
            if (dMinAxis == dAbsMin)
            {
                dMinAxis += dStep;
                if (dMinValue < 0)
                {
                    dMinAxis = -dMinAxis;
                }
                else if (dMinValue == 0)
                {
                    dMinAxis = 0;
                }
            }
            else
            {
                if (dMinValue < 0)
                {
                    dMinAxis = -(dMinAxis + dStep);
                }
                else if (dMinAxis == dMinValue && dMinAxis != 0)
                {
                    dMinAxis -= dStep;
                }
            }

            double dMaxAxis = (dStep > 1) ? Math.floor(dAbsMax / dStep) : Math.round(dAbsMax / dStep);
            dMaxAxis *= dStep;
            if (dMaxAxis == dAbsMax)
            {
                dMaxAxis += dStep;
                if (dMaxValue == 0)
                {
                    dMaxAxis = 0;
                }
            }
            else if (dMinAxis != dMaxValue)
            {
                if (dMaxValue < 0)
                {
                    dMaxAxis = -(dMaxAxis - dStep);
                }
                else if (dMaxValue > 0)
                {
                    if (dMaxAxis < dMaxValue)
                    {
                        dMaxAxis += dStep;
                    }
                }
            }

            if (dMinValue < 0 && dMaxValue < 0)
                dMaxAxis = 0;
            if (dMinValue > 0 && dMaxValue > 0)
                dMinAxis = 0;
            if (!bMaximumFixed)
            {
                oMaximum = new Double(dMaxAxis);
            }
            if (!bMinimumFixed)
            {
                oMinimum = new Double(dMinAxis);
            }
        }
    }

    /**
     * Checks all labels for any overlap for a given axis' scale
     * 
     * @param la
     * @param iLabelLocation
     * 
     * @return
     */
    final boolean checkFit(IDisplayServer xs, Label la, int iLabelLocation) throws GenerationException
    {
        if (iType == TEXT || bCategoryScale)
        {
            return true;
        }

        final double dAngleInDegrees = la.getCaption().getFont().getRotation();
        double x = 0, y = 0;
        int iPointToCheck = 0;
        if (iLabelLocation == ABOVE || iLabelLocation == BELOW)
        {
            iPointToCheck = (dAngleInDegrees < 0 && dAngleInDegrees > -90) ? 3 : 0;
        }
        else if (iLabelLocation == LEFT || iLabelLocation == RIGHT)
        {
            iPointToCheck = (dAngleInDegrees < 0 && dAngleInDegrees > -90) ? 2 : 3;
        }
        double[] da = daTickCoordinates;
        RotatedRectangle rrPrev = null, rr;

        if ((iType & (NUMERICAL | LINEAR)) == (NUMERICAL | LINEAR))
        {
            double dAxisValue = asDouble(getMinimum()).doubleValue();
            final double dAxisStep = asDouble(getStep()).doubleValue();
            String sText;
            DecimalFormat df = null;
            if (fs == null) // CREATE IF FORMAT SPECIFIER IS UNDEFINED
            {
                df = new DecimalFormat(getNumericPattern());
            }
            final NumberDataElement nde = NumberDataElementImpl.create(0);

            for (int i = 0; i < da.length; i++)
            {
                nde.setValue(dAxisValue);
                try
                {
                    sText = ValueFormatter.format(nde, fs, rtc.getLocale(), df);
                }
                catch (DataFormatException dfex )
                {
                    DefaultLoggerImpl.instance().log(dfex);
                    sText = NULL_STRING;
                }

                if (iLabelLocation == ABOVE || iLabelLocation == BELOW)
                {
                    x = da[i];
                }
                else if (iLabelLocation == LEFT || iLabelLocation == RIGHT)
                {
                    y = da[i];
                }

                la.getCaption().setValue(sText);
                try
                {
                    rr = computePolygon(xs, iLabelLocation, la, x, y);
                }
                catch (UnexpectedInputException uiex )
                {
                    throw new GenerationException(uiex);
                }

                Point p = rr.getPoint(iPointToCheck);
                if (rrPrev != null && (rrPrev.contains(p) || rrPrev.getPoint(iPointToCheck).equals(p)))
                {
                    return false;
                }
                rrPrev = rr;
                dAxisValue += dAxisStep;
            }
        }
        else if ((iType & (NUMERICAL | LOGARITHMIC)) == (NUMERICAL | LOGARITHMIC))
        {
            double dAxisValue = asDouble(getMinimum()).doubleValue();
            final double dAxisStep = asDouble(getStep()).doubleValue();
            String sText;
            NumberDataElement nde = NumberDataElementImpl.create(0);
            DecimalFormat df = null;

            for (int i = 0; i < da.length; i++)
            {
                nde.setValue(dAxisValue);
                if (fs == null)
                {
                    df = new DecimalFormat(getNumericPattern(dAxisValue));
                }
                try
                {
                    sText = ValueFormatter.format(nde, fs, rtc.getLocale(), df);
                }
                catch (DataFormatException dfex )
                {
                    DefaultLoggerImpl.instance().log(dfex);
                    sText = NULL_STRING;
                }

                if (iLabelLocation == ABOVE || iLabelLocation == BELOW)
                {
                    x = da[i];
                }
                else if (iLabelLocation == LEFT || iLabelLocation == RIGHT)
                {
                    y = da[i];
                }

                la.getCaption().setValue(sText);
                try
                {
                    rr = computePolygon(xs, iLabelLocation, la, x, y);
                }
                catch (UnexpectedInputException uiex )
                {
                    throw new GenerationException(uiex);
                }

                Point p = rr.getPoint(iPointToCheck);
                if (rrPrev != null && (rrPrev.contains(p) || rrPrev.getPoint(iPointToCheck).equals(p)))
                {
                    return false;
                }
                rrPrev = rr;
                dAxisValue *= dAxisStep;
            }
        }
        else if (iType == DATE_TIME)
        {
            CDateTime cdt, cdtAxisValue = asDateTime(oMinimum);
            final int iUnit = asInteger(oUnit);
            final int iStep = asInteger(oStep);
            final SimpleDateFormat sdf = new SimpleDateFormat(CDateTime.getPreferredFormat(iUnit));

            String sText;
            cdt = cdtAxisValue;

            for (int i = 0; i < da.length; i++)
            {
                sText = sdf.format(cdt.getTime());

                if (iLabelLocation == ABOVE || iLabelLocation == BELOW)
                    x = da[i];
                else if (iLabelLocation == LEFT || iLabelLocation == RIGHT)
                    y = da[i];

                la.getCaption().setValue(sText);
                try
                {
                    rr = computePolygon(xs, iLabelLocation, la, x, y);
                }
                catch (UnexpectedInputException uiex )
                {
                    throw new GenerationException(uiex);
                }

                Point p = rr.getPoint(iPointToCheck);
                if (rrPrev != null && (rrPrev.contains(p) || rrPrev.getPoint(iPointToCheck).equals(p)))
                {
                    return false;
                }
                rrPrev = rr;
                cdt = cdtAxisValue.forward(iUnit, iStep * (i + 1)); // ALWAYS
                // W.R.T
                // START
                // VALUE
            }
        }
        return true;
    }

    /**
     *  
     */
    final void resetShifts()
    {
        dStartShift = 0;
        dEndShift = 0;
    }

    /**
     * 
     * @return
     */
    public final double getStart()
    {
        return dStart;
    }

    /**
     * 
     * @return
     */
    final double getEnd()
    {
        return dEnd;
    }

    /**
     * 
     * @return
     */
    final double getStartShift()
    {
        return dStartShift;
    }

    /**
     * 
     * @return
     */
    final double getEndShift()
    {
        return dEndShift;
    }

    /**
     * 
     * @param ax
     * @param dsi
     * @param iType
     * @param dStart
     * @param dEnd
     * 
     * @return
     */
    static final AutoScale computeScale(IDisplayServer xs, OneAxis ax, DataSetIterator dsi, int iType, 
        double dStart, double dEnd, DataElement oMinimum, DataElement oMaximum,
        Double oStep, FormatSpecifier fs, RunTimeContext rtc)
        throws GenerationException
    {
        final Label la = ax.getLabel();
        final int iLabelLocation = ax.getLabelPosition();
        final int iOrientation = ax.getOrientation();

        AutoScale sc = null;
        AutoScale scCloned = null;

        if ((iType & TEXT) == TEXT || ax.isCategoryScale())
        {
            sc = new AutoScale(iType);
            sc.fs = fs;
            sc.rtc = rtc;
            sc.bCategoryScale = true;
            sc.setData(dsi);
            sc.computeTicks(xs, ax.getLabel(), iLabelLocation, iOrientation, dStart, dEnd, false, null);
        }
        else if ((iType & LINEAR) == LINEAR)
        {
            Object oValue;
            double dValue, dMinValue = Double.MAX_VALUE, dMaxValue = Double.MIN_VALUE;
            dsi.reset();
            while (dsi.hasNext())
            {
                oValue = dsi.next();
                if (oValue == null) // NULL VALUE CHECK
                {
                    continue;
                }
                dValue = ((Double) oValue).doubleValue();
                if (dValue < dMinValue)
                    dMinValue = dValue;
                if (dValue > dMaxValue)
                    dMaxValue = dValue;
            }

            double dDelta = dMaxValue - dMinValue;
            if (dDelta == 0) // PREVENT INFINITE LOOP DUE TO LOG(dDelta) IN
            // AUTO SCALING ALGORITHM
            {
                dDelta = 1;
            }
            final double dAbsMax = Math.abs(dMaxValue);
            final double dAbsMin = Math.abs(dMinValue);
            double dStep = Math.max(dAbsMax, dAbsMin);
            dStep = Math.floor(Math.log(dDelta) / LOG_10);
            dStep = Math.pow(10, dStep);

            sc = new AutoScale(iType, new Double(0), new Double(0), new Double(dStep));
            sc.setData(dsi);
            sc.fs = fs; // FORMAT SPECIFIER
            sc.rtc = rtc; // LOCALE

            // OVERRIDE MINIMUM IF SPECIFIED
            if (oMinimum != null)
            {
                if (oMinimum instanceof NumberDataElement)
                {
                    sc.oMinimum = new Double(((NumberDataElement) oMinimum).getValue());
                }
                /*else if (oMinimum instanceof DateTimeDataElement)
                {
                    sc.oMinimum = ((DateTimeDataElement) oMinimum).getValueAsCDateTime();
                }*/
                else
                {
                    throw new GenerationException(
                        "exception.invalid.minimum.scale.value", //$NON-NLS-1$
                        new Object[] { oMinimum, ax.getModelAxis().getType().getName() },
                        ResourceBundle.getBundle(
                            Messages.ENGINE, 
                            rtc.getLocale()
                        )
                    ); // i18n_CONCATENATIONS_REMOVED
                }
                sc.bMinimumFixed = true;
            }

            // OVERRIDE MAXIMUM IF SPECIFIED
            if (oMaximum != null)
            {
                if (oMaximum instanceof NumberDataElement)
                {
                    sc.oMaximum = new Double(((NumberDataElement) oMaximum).getValue());
                }
                /*else if (oMaximum instanceof DateTimeDataElement)
                {
                    sc.oMaximum = ((DateTimeDataElement) oMaximum).getValueAsCDateTime();
                }*/
                else
                {
                    throw new GenerationException(
                        "exception.invalid.maximum.scale.value", //$NON-NLS-1$
                        new Object[] { oMaximum, ax.getModelAxis().getType().getName() },
                        ResourceBundle.getBundle(
                            Messages.ENGINE, 
                            rtc.getLocale()
                        ) 
                    ); // i18n_CONCATENATIONS_REMOVED 
                }
                sc.bMaximumFixed = true;
            }

            // OVERRIDE STEP IF SPECIFIED
            if (oStep != null)
            {
                sc.oStep = oStep;
                sc.bStepFixed = true;
                
                // VALIDATE OVERRIDDEN STEP
                if (((Double) sc.oStep).doubleValue() <= 0)
                {
                    throw new GenerationException(
                        "exception.invalid.step.value", //$NON-NLS-1$
                        new Object[] { oStep },
                        ResourceBundle.getBundle(
                            Messages.ENGINE, 
                            rtc.getLocale()
                        )
                    ); // i18n_CONCATENATIONS_REMOVED 
                }
            }

            // VALIDATE OVERRIDDEN MIN/MAX
            if (sc.bMaximumFixed && sc.bMinimumFixed)
            {
                if (((Double) sc.oMinimum).doubleValue() > ((Double) sc.oMaximum).doubleValue())
                {
                    throw new GenerationException(
                        "exception.min.largerthan.max", //$NON-NLS-1$ 
                        new Object[] { oMinimum, oMaximum },
                        ResourceBundle.getBundle(
                            Messages.ENGINE, 
                            rtc.getLocale()
                        )
                    ); // i18n_CONCATENATIONS_REMOVED 
                }
            }
            
            final Object oMinValue = new Double(dMinValue);
            final Object oMaxValue = new Double(dMaxValue);
            sc.updateAxisMinMax(oMinValue, oMaxValue);

            sc.computeTicks(xs, ax.getLabel(), iLabelLocation, iOrientation, dStart, dEnd, false, null);
            dStart = sc.dStart;
            dEnd = sc.dEnd;

            boolean bFirstFit = sc.checkFit(xs, la, iLabelLocation);
            boolean bFits = bFirstFit;
            boolean bZoomSuccess = false;

            // THE AUTO ZOOM LOOP
            while (bFits == bFirstFit)
            {
                bZoomSuccess = true;
                scCloned = (AutoScale) sc.clone();
                if (sc.bStepFixed) // DO NOT AUTO ZOOM IF STEP IS FIXED
                {
                    break;
                }
                if (bFirstFit)
                {
                    if (!bFits)
                    {
                        break;
                    }
                    bZoomSuccess = sc.zoomIn();
                }
                else
                {
                    if (!bFits && sc.getTickCount() == 2)
                    {
                        break;
                    }
                    bZoomSuccess = sc.zoomOut();
                }
                if (!bZoomSuccess)
                    break;

                sc.updateAxisMinMax(oMinValue, oMaxValue);
                sc.computeTicks(xs, ax.getLabel(), iLabelLocation, iOrientation, dStart, dEnd, false, null);
                bFits = sc.checkFit(xs, la, iLabelLocation);
                if (!bFits && sc.getTickCount() == 2)
                {
                    sc = scCloned;
                    break;
                }
            }

            // RESTORE TO LAST SCALE BEFORE ZOOM
            if (scCloned != null && bFirstFit && bZoomSuccess)
            {
                sc = scCloned;
            }
        }

        else if ((iType & LOGARITHMIC) == LOGARITHMIC)
        {
            Object oValue;
            double dValue, dMinValue = Double.MAX_VALUE, dMaxValue = Double.MIN_VALUE;
            if ((iType & PERCENT) == PERCENT)
            {
                dMinValue = 0;
                dMaxValue = 100;
            }
            else
            {
                dsi.reset();
                while (dsi.hasNext())
                {
                    oValue = dsi.next();
                    if (oValue == null) // NULL VALUE CHECK
                    {
                        continue;
                    }
                    dValue = ((Double) oValue).doubleValue();
                    if (dValue < dMinValue)
                        dMinValue = dValue;
                    if (dValue > dMaxValue)
                        dMaxValue = dValue;
                }
            }
            final Object oMinValue = new Double(dMinValue);
            final Object oMaxValue = new Double(dMaxValue);

            sc = new AutoScale(iType, new Double(0), new Double(0), new Double(10));
            sc.fs = fs; // FORMAT SPECIFIER
            sc.rtc = rtc; // LOCALE
            sc.setData(dsi);
            sc.updateAxisMinMax(oMinValue, oMaxValue);
            if ((iType & PERCENT) == PERCENT)
            {
                sc.bStepFixed = true;
                sc.bMaximumFixed = true;
                sc.bMinimumFixed = true;
            }
            sc.computeTicks(xs, ax.getLabel(), iLabelLocation, iOrientation, dStart, dEnd, false, null);
            if ((iType & PERCENT) == PERCENT)
            {
                return sc;
            }

            dStart = sc.dStart;
            dEnd = sc.dEnd;

            boolean bFirstFit = sc.checkFit(xs, la, iLabelLocation);
            boolean bFits = bFirstFit;
            boolean bZoomSuccess = false;
            //sc.oDebug = String.valueOf(bFirstFit);

            while (bFits == bFirstFit)
            {
                bZoomSuccess = true;
                scCloned = (AutoScale) sc.clone();
                if (sc.bStepFixed) // DO NOT AUTO ZOOM IF STEP IS FIXED
                {
                    break;
                }
                if (bFirstFit)
                {
                    if (!bFits)
                    {
                        break;
                    }
                    bZoomSuccess = sc.zoomIn();
                }
                else
                {
                    if (!bFits && sc.getTickCount() == 2)
                    {
                        break;
                    }
                    bZoomSuccess = sc.zoomOut();
                }
                if (!bZoomSuccess)
                    break;

                sc.updateAxisMinMax(oMinValue, oMaxValue);
                sc.computeTicks(xs, ax.getLabel(), iLabelLocation, iOrientation, dStart, dEnd, false, null);

                bFits = sc.checkFit(xs, la, iLabelLocation);
                if (!bFits && sc.getTickCount() == 2)
                {
                    sc = scCloned;
                    break;
                }
            }

            // RESTORE TO LAST SCALE BEFORE ZOOM
            if (scCloned != null && bFirstFit && bZoomSuccess)
            {
                sc = scCloned;
            }
        }
        else if ((iType & DATE_TIME) == DATE_TIME)
        {
            Calendar cValue;
            Calendar caMin = null, caMax = null;
            dsi.reset();
            while (dsi.hasNext())
            {
                cValue = (Calendar) dsi.next();
                if (cValue == null) // NULL VALUE CHECK
                {
                    continue;
                }
                if (caMin == null)
                {
                    caMin = cValue;
                }
                if (caMax == null)
                {
                    caMax = cValue;
                }
                if (cValue.before(caMin))
                    caMin = cValue;
                else if (cValue.after(caMax))
                    caMax = cValue;
            }

            CDateTime cdtMinValue = new CDateTime(caMin);
            CDateTime cdtMaxValue = new CDateTime(caMax);
            int iUnit = CDateTime.getDifference(cdtMinValue, cdtMaxValue);

            CDateTime cdtMinAxis = cdtMinValue.backward(iUnit, 1);
            CDateTime cdtMaxAxis = cdtMaxValue.forward(iUnit, 1);
            cdtMinAxis.clearBelow(iUnit);
            cdtMaxAxis.clearBelow(iUnit);

            sc = new AutoScale(DATE_TIME, cdtMinAxis, cdtMaxAxis, new Integer(iUnit), new Integer(1));
            sc.computeTicks(xs, la, iLabelLocation, iOrientation, dStart, dEnd, false, null);
            
            sc.fs = fs; // FORMAT SPECIFIER
            sc.rtc = rtc; // LOCALE
            dStart = sc.dStart;
            dEnd = sc.dEnd;

            // OVERRIDE MINIMUM IF SPECIFIED
            if (oMinimum != null)
            {
                if (oMinimum instanceof DateTimeDataElement)
                {
                    sc.oMinimum = ((DateTimeDataElement) oMinimum).getValueAsCDateTime();
                }
                else
                {
                    throw new GenerationException(
                        "exception.invalid.minimum.scale.value", //$NON-NLS-1$ 
                        new Object[] { oMinimum + ax.getModelAxis().getType().getName() },
                        ResourceBundle.getBundle(
                            Messages.ENGINE, 
                            rtc.getLocale()
                        )
                    ); // i18n_CONCATENATIONS_REMOVED
                }
                sc.bMinimumFixed = true;
            }

            // OVERRIDE MAXIMUM IF SPECIFIED
            if (oMaximum != null)
            {
                if (oMaximum instanceof DateTimeDataElement)
                {
                    sc.oMaximum = ((DateTimeDataElement) oMaximum).getValueAsCDateTime();
                }
                else
                {
                    throw new GenerationException(
                        "exception.invalid.maximum.scale.value", //$NON-NLS-1$
                        new Object[] { oMaximum + ax.getModelAxis().getType().getName() },
                        ResourceBundle.getBundle(
                            Messages.ENGINE, 
                            rtc.getLocale()
                        )
                    ); // i18n_CONCATENATIONS_REMOVED 
                }
                sc.bMaximumFixed = true;
            }

            // OVERRIDE STEP IF SPECIFIED
            /*if (oStep != null)
            {
                sc.oStep = oStep;
                sc.bStepFixed = true;
                
                // VALIDATE OVERRIDDEN STEP
                if (((Double) sc.oStep).doubleValue() <= 0)
                {
                    throw new GenerationException("Invalid 'step({0}) <= 0' specified for axis scale" + oStep ); // i18n_CONCATENATIONS_REMOVED
                }
            }*/

            // VALIDATE OVERRIDDEN MIN/MAX
            if (sc.bMaximumFixed && sc.bMinimumFixed)
            {
                if (((CDateTime) sc.oMinimum).after(((CDateTime) sc.oMaximum)))
                {
                    throw new GenerationException(
                        "exception.min.largerthan.max", //$NON-NLS-1$
                        new Object[] { oMinimum, oMaximum },
                        ResourceBundle.getBundle(
                            Messages.ENGINE, 
                            rtc.getLocale()
                        )
                    ); // i18n_CONCATENATIONS_REMOVED 
                }
            }
            
            
            boolean bFirstFit = sc.checkFit(xs, la, iLabelLocation);
            boolean bFits = bFirstFit, bZoomSuccess = false;

            while (bFits == bFirstFit)
            {
                bZoomSuccess = true;
                scCloned = (AutoScale) sc.clone();
                if (sc.bStepFixed) // DO NOT AUTO ZOOM IF STEP IS FIXED
                {
                    break;
                }
                if (bFirstFit)
                {
                    if (!bFits)
                    {
                        break;
                    }
                    bZoomSuccess = sc.zoomIn();
                }
                else
                {
                    if (!bFits && sc.getTickCount() == 2)
                    {
                        break;
                    }
                    bZoomSuccess = sc.zoomOut();
                }
                if (!bZoomSuccess)
                    break;

                sc.adjustAxisMinMax(cdtMinValue, cdtMaxValue);

                sc.computeTicks(xs, la, iLabelLocation, iOrientation, dStart, dEnd, false, null);
                bFits = sc.checkFit(xs, la, iLabelLocation);
                if (!bFits && sc.getTickCount() == 2)
                {
                    sc = scCloned;
                    break;
                }
            }

            // RESTORE TO LAST SCALE BEFORE ZOOM
            if (scCloned != null && bFirstFit && bZoomSuccess)
            {
                sc = scCloned;
            }
        }

        sc.setData(dsi);
        return sc;
    }

    /**
     * 
     * @param la
     * @param iLabelLocation
     * @param iOrientation
     * @param dStart
     * @param dEnd
     * @param bConsiderStartEndLabels
     * @param aax
     */
    final void computeTicks(IDisplayServer xs, Label la, int iLabelLocation, int iOrientation, double dStart,
        double dEnd, boolean bConsiderStartEndLabels, AllAxes aax) throws GenerationException
    {
        int nTicks = 0;
        double dLength = 0;
        int iDirection = (iOrientation == HORIZONTAL) ? FORWARD : BACKWARD;
        DataSetIterator dsi = getData();

        if (bConsiderStartEndLabels)
        {
            computeAxisStartEndShifts(xs, la, iOrientation, iLabelLocation, aax);
            dStart += dStartShift * iDirection;
            dEnd += dEndShift * -iDirection;
        }

        if ((iType & TEXT) == TEXT || bCategoryScale)
        {
            nTicks = dsi.size() + 1;
            dLength = Math.abs(dStart - dEnd);
        }
        else if ((iType & NUMERICAL) == NUMERICAL)
        {
            nTicks = getTickCount();
            dLength = Math.abs(dStart - dEnd);
        }
        else if ((iType & DATE_TIME) == DATE_TIME)
        {
            final CDateTime cdt1 = (CDateTime) oMinimum;
            final CDateTime cdt2 = (CDateTime) oMaximum;
            final double dNumberOfSteps = Math.ceil(CDateTime.computeDifference(cdt2, cdt1, asInteger(oUnit)));
            nTicks = (int) (dNumberOfSteps / asInteger(oStep)) + 1;
            dLength = Math.abs(dStart - dEnd);
        }
        else
        {
            throw new GenerationException(
                "exception.unknown.axis.type.tick.computations", //$NON-NLS-1$
                ResourceBundle.getBundle(
                    Messages.ENGINE, 
                    rtc.getLocale()
                )
            );
        }

        final double dTickGap = dLength / (nTicks - 1) * iDirection;
        double d = dStart + dTickGap;
        final double[] da = new double[nTicks];

        for (int i = 1; i < nTicks - 1; i++, d += dTickGap)
        {
            da[i] = d;
        }
        da[0] = dStart;
        da[nTicks - 1] = dEnd;
        setTickCordinates(null);
        setEndPoints(dStart, dEnd);
        setTickCordinates(da);
    }

    /**
     * 
     * @param iType
     * @param oValue
     * @return
     */
    public final String formatCategoryValue(int iType, Object oValue, int iDateTimeUnit)
    {
        if (oValue == null)
        {
            return IConstants.NULL_STRING;
        }

        if (iType == IConstants.TEXT) // MOST LIKELY
        {
            return oValue.toString();
        }
        else if (iType == IConstants.DATE_TIME)
        {
            final Calendar ca = (Calendar) oValue;
            SimpleDateFormat sdf = null;
            if (fs == null) // ONLY COMPUTE INTERNALLY IF FORMAT SPECIFIER ISN'T DEFINED
            {
                sdf = new SimpleDateFormat(CDateTime.getPreferredFormat(iDateTimeUnit));
            }

            // ADJUST THE START POSITION
            try
            {
                return ValueFormatter.format(ca, fs, rtc.getLocale(), sdf);
            }
            catch (DataFormatException dfex )
            {
                DefaultLoggerImpl.instance().log(dfex);
                return IConstants.NULL_STRING;
            }
        }
        else if ((iType & IConstants.NUMERICAL) == IConstants.NUMERICAL)
        {
            DecimalFormat df = null;
            if (fs == null) // ONLY COMPUTE INTERNALLY IF FORMAT SPECIFIER ISN'T DEFINED
            {
                df = new DecimalFormat(getNumericPattern());
            }
            try
            {
                return ValueFormatter.format(oValue, fs, rtc.getLocale(), df);
            }
            catch (DataFormatException dfex )
            {
                DefaultLoggerImpl.instance().log(dfex);
                return IConstants.NULL_STRING;
            }
        }

        return IConstants.NULL_STRING;
    }

    /**
     * Computes the axis start/end shifts (due to start/end labels) and also takes into consideration all start/end
     * shifts of any overlay axes in the same direction as the current scale.
     * 
     * @param la
     * @param iOrientation
     * @param iLocation
     * @param aax
     */
    final void computeAxisStartEndShifts(IDisplayServer xs, Label la, int iOrientation, int iLocation, AllAxes aax)
        throws GenerationException
    {
        final double dMaxSS = (aax != null && iOrientation == aax.getOrientation()) ? aax.getMaxStartShift() : 0;
        final double dMaxES = (aax != null && iOrientation == aax.getOrientation()) ? aax.getMaxEndShift() : 0;

        if (!la.isVisible())
        {
            dStartShift = dMaxSS;
            dEndShift = dMaxES;
            return;
        }

        if (getType() == TEXT || bCategoryScale)
        {
            // COMPUTE THE BOUNDING BOXES FOR FIRST AND LAST LABEL TO ADJUST
            // START/END OF X-AXIS
            final double dUnitSize = getUnitSize();
            final DataSetIterator dsi = getData();
            final int iDateTimeUnit = (getType() == IConstants.DATE_TIME) ? CDateTime.computeUnit(dsi)
                : IConstants.UNDEFINED;

            // ADJUST THE START POSITION
            la.getCaption().setValue(formatCategoryValue(getType(), dsi.first(), iDateTimeUnit));
            BoundingBox bb = null;
            try
            {
                bb = computeBox(xs, iLocation, la, 0, 0);
            }
            catch (UnexpectedInputException uiex )
            {
                throw new GenerationException(uiex);
            }
            if (iOrientation == VERTICAL) // VERTICAL AXIS
            {
                dStartShift = Math.max(dMaxSS, (dUnitSize > bb.getHeight()) ? 0 : (bb.getHeight() - dUnitSize) / 2);
            }
            else if (iOrientation == HORIZONTAL) // HORIZONTAL AXIS
            {
                dStartShift = Math.max(dMaxSS, (dUnitSize > bb.getWidth()) ? 0 : (bb.getWidth() - dUnitSize) / 2);
            }

            // ADJUST THE END POSITION
            la.getCaption().setValue(formatCategoryValue(getType(), dsi.last(), iDateTimeUnit));
            try
            {
                bb = computeBox(xs, iLocation, la, 0, dEnd);
            }
            catch (UnexpectedInputException uiex )
            {
                throw new GenerationException(uiex);
            }
            if (iOrientation == VERTICAL) // VERTICAL AXIS
            {
                dEndShift = Math.max(dMaxES, (dUnitSize > bb.getHeight()) ? 0 : (bb.getHeight() - dUnitSize) / 2);
            }
            else if (iOrientation == HORIZONTAL) // HORIZONTAL AXIS
            {
                dEndShift = Math.max(dMaxES, (dUnitSize > bb.getWidth()) ? 0 : (bb.getWidth() - dUnitSize) / 2);
            }
        }
        else if ((iType & NUMERICAL) == NUMERICAL)
        {
            if ((iType & LINEAR) == LINEAR)
            {
                // ADJUST THE START POSITION
                DecimalFormat df = null;
                if (fs == null) // ONLY COMPUTE INTERNALLY IF FORMAT SPECIFIER ISN'T DEFINED
                {
                    df = new DecimalFormat(getNumericPattern());
                }
                String sValue = null;
                try
                {
                    sValue = ValueFormatter.format(getMinimum(), fs, rtc.getLocale(), df);
                }
                catch (DataFormatException dfex )
                {
                    DefaultLoggerImpl.instance().log(dfex);
                    sValue = IConstants.NULL_STRING;
                }
                la.getCaption().setValue(sValue);
                BoundingBox bb = null;
                try
                {
                    bb = computeBox(xs, iLocation, la, 0, 0);
                }
                catch (UnexpectedInputException uiex )
                {
                    throw new GenerationException(uiex);
                }
                if (iOrientation == VERTICAL) // VERTICAL AXIS
                {
                    dStartShift = Math.max(dMaxSS, (bb.getHeight() - bb.getHotPoint()));
                }
                else if (iOrientation == HORIZONTAL)
                {
                    dStartShift = Math.max(dMaxSS, bb.getHotPoint());
                }

                // ADJUST THE END POSITION
                try
                {
                    sValue = ValueFormatter.format(getMaximum(), fs, rtc.getLocale(), df);
                }
                catch (DataFormatException dfex )
                {
                    DefaultLoggerImpl.instance().log(dfex);
                    sValue = IConstants.NULL_STRING;
                }
                la.getCaption().setValue(sValue);
                try
                {
                    bb = computeBox(xs, iLocation, la, 0, 0);
                }
                catch (UnexpectedInputException uiex )
                {
                    throw new GenerationException(uiex);
                }

                if (iOrientation == VERTICAL) // VERTICAL AXIS
                {
                    dEndShift = Math.max(dMaxES, bb.getHotPoint());
                }
                else if (iOrientation == HORIZONTAL)
                {
                    dEndShift = Math.max(dMaxES, (bb.getWidth() - bb.getHotPoint()));
                }
            }
            else if ((iType & LOGARITHMIC) == LOGARITHMIC)
            {
                // ADJUST THE START POSITION
                final double dMinimum = asDouble(getMinimum()).doubleValue();
                DecimalFormat df = null;
                if (fs == null) // ONLY COMPUTE INTERNALLY IF FORMAT SPECIFIER ISN'T DEFINED
                {
                    df = new DecimalFormat(getNumericPattern(dMinimum));
                }
                String sValue = null;
                try
                {
                    sValue = ValueFormatter.format(getMinimum(), fs, rtc.getLocale(), df);
                }
                catch (DataFormatException dfex )
                {
                    DefaultLoggerImpl.instance().log(dfex);
                    sValue = IConstants.NULL_STRING;
                }
                la.getCaption().setValue(sValue);
                BoundingBox bb = null;
                try
                {
                    bb = computeBox(xs, iLocation, la, 0, 0);
                }
                catch (UnexpectedInputException uiex )
                {
                    throw new GenerationException(uiex);
                }
                if (iOrientation == VERTICAL) // VERTICAL AXIS
                {
                    dStartShift = Math.max(dMaxSS, (bb.getHeight() - bb.getHotPoint()));
                }
                else if (iOrientation == HORIZONTAL)
                {
                    dStartShift = Math.max(dMaxSS, bb.getHotPoint());
                }

                // ADJUST THE END POSITION
                final double dMaximum = asDouble(getMaximum()).doubleValue();
                if (fs == null) // ONLY COMPUTE INTERNALLY (DIFFERENT FROM MINIMUM) IF FORMAT SPECIFIER ISN'T DEFINED
                {
                    df = new DecimalFormat(getNumericPattern(dMaximum));
                }
                try
                {
                    sValue = ValueFormatter.format(getMaximum(), fs, rtc.getLocale(), df);
                }
                catch (DataFormatException dfex )
                {
                    DefaultLoggerImpl.instance().log(dfex);
                    sValue = IConstants.NULL_STRING;
                }
                la.getCaption().setValue(sValue);
                try
                {
                    bb = computeBox(xs, iLocation, la, 0, 0);
                }
                catch (UnexpectedInputException uiex )
                {
                    throw new GenerationException(uiex);
                }

                if (iOrientation == VERTICAL) // VERTICAL AXIS
                {
                    dEndShift = Math.max(dMaxES, bb.getHotPoint());
                }
                else if (iOrientation == HORIZONTAL)
                {
                    dEndShift = Math.max(dMaxES, (bb.getWidth() - bb.getHotPoint()));
                }
            }
        }

        else if (getType() == DATE_TIME)
        {
            // COMPUTE THE BOUNDING BOXES FOR FIRST AND LAST LABEL TO ADJUST START/END OF X-AXIS
            CDateTime cdt = asDateTime(getMinimum());
            final int iUnit = asInteger(oUnit);
            SimpleDateFormat sdf = null;
            String sText = null;

            if (fs != null) // ONLY COMPUTE INTERNALLY IF FORMAT SPECIFIER ISN'T DEFINED
            {
                sdf = new SimpleDateFormat(CDateTime.getPreferredFormat(iUnit));
            }

            // ADJUST THE START POSITION
            try
            {
                sText = ValueFormatter.format(cdt, fs, rtc.getLocale(), sdf);
            }
            catch (DataFormatException dfex )
            {
                DefaultLoggerImpl.instance().log(dfex);
                sText = IConstants.NULL_STRING;
            }
            la.getCaption().setValue(sText);

            BoundingBox bb = null;
            try
            {
                bb = computeBox(xs, iLocation, la, 0, 0);
            }
            catch (UnexpectedInputException uiex )
            {
                throw new GenerationException(uiex);
            }
            if (iOrientation == VERTICAL) // VERTICAL AXIS
            {
                dStartShift = Math.max(dMaxSS, (bb.getHeight() - bb.getHotPoint()));
            }
            else if (iOrientation == HORIZONTAL)
            {
                dStartShift = Math.max(dMaxSS, bb.getHotPoint());
            }

            // ADJUST THE END POSITION
            cdt = asDateTime(getMaximum());
            try
            {
                sText = ValueFormatter.format(cdt, fs, rtc.getLocale(), sdf);
            }
            catch (DataFormatException dfex )
            {
                DefaultLoggerImpl.instance().log(dfex);
                sText = IConstants.NULL_STRING;
            }
            la.getCaption().setValue(sText);
            try
            {
                bb = computeBox(xs, iLocation, la, 0, dEnd);
            }
            catch (UnexpectedInputException uiex )
            {
                throw new GenerationException(uiex);
            }
            if (iOrientation == VERTICAL) // VERTICAL AXIS
            {
                dEndShift = Math.max(dMaxES, bb.getHotPoint());
            }
            else if (iOrientation == HORIZONTAL)
            {
                dEndShift = Math.max(dMaxES, (bb.getWidth() - bb.getHotPoint()));
            }
        }
    }

    final double computeAxisLabelThickness(IDisplayServer xs, Label la, int iOrientation) throws GenerationException
    {
        if (!la.isSetVisible())
        {
            throw new GenerationException(
                "exception.unset.label.visibility", //$NON-NLS-1$
                new Object[] { la },
                ResourceBundle.getBundle(
                    Messages.ENGINE, 
                    rtc.getLocale()
                )
            ); // i18n_CONCATENATIONS_REMOVED 
        }

        if (!la.isVisible())
        {
            return 0;
        }

        String sText;
        double[] da = getTickCordinates();

        if (iOrientation == VERTICAL)
        {
            double dW, dMaxW = 0;
            if ((getType() & TEXT) == TEXT || bCategoryScale)
            {
                final DataSetIterator dsi = getData();
                final int iDateTimeUnit = (getType() == IConstants.DATE_TIME) ? CDateTime.computeUnit(dsi)
                    : IConstants.UNDEFINED;
                dsi.reset();
                while (dsi.hasNext())
                {
                    la.getCaption().setValue(formatCategoryValue(getType(), dsi.next(), iDateTimeUnit));
                    dW = computeWidth(xs, la);
                    if (dW > dMaxW)
                        dMaxW = dW;
                }
            }
            else if ((getType() & LINEAR) == LINEAR)
            {
                final NumberDataElement nde = NumberDataElementImpl.create(0);
                double dAxisValue = asDouble(getMinimum()).doubleValue();
                double dAxisStep = asDouble(getStep()).doubleValue();
                DecimalFormat df = null;
                if (fs == null)
                {
                    df = new DecimalFormat(getNumericPattern());
                }
                for (int i = 0; i < da.length; i++)
                {
                    nde.setValue(dAxisValue);
                    try
                    {
                        sText = ValueFormatter.format(nde, fs, rtc.getLocale(), df);
                    }
                    catch (DataFormatException dfex )
                    {
                        DefaultLoggerImpl.instance().log(dfex);
                        sText = IConstants.NULL_STRING;
                    }
                    la.getCaption().setValue(sText);
                    dW = computeWidth(xs, la);
                    if (dW > dMaxW)
                        dMaxW = dW;
                    dAxisValue += dAxisStep;
                }
            }
            else if ((getType() & LOGARITHMIC) == LOGARITHMIC)
            {
                final NumberDataElement nde = NumberDataElementImpl.create(0);
                double dAxisValue = asDouble(getMinimum()).doubleValue();
                double dAxisStep = asDouble(getStep()).doubleValue();
                DecimalFormat df = null;
                for (int i = 0; i < da.length; i++)
                {
                    if (fs == null)
                    {
                        df = new DecimalFormat(getNumericPattern(dAxisValue));
                    }
                    nde.setValue(dAxisValue);
                    try
                    {
                        sText = ValueFormatter.format(nde, fs, rtc.getLocale(), df);
                    }
                    catch (DataFormatException dfex )
                    {
                        DefaultLoggerImpl.instance().log(dfex);
                        sText = IConstants.NULL_STRING;
                    }
                    la.getCaption().setValue(sText);
                    dW = computeWidth(xs, la);
                    if (dW > dMaxW)
                        dMaxW = dW;
                    dAxisValue *= dAxisStep;
                }
            }
            else if ((getType() & DATE_TIME) == DATE_TIME)
            {
                CDateTime cdtAxisValue = asDateTime(getMinimum());
                int iStep = asInteger(getStep());
                int iUnit = asInteger(getUnit());
                SimpleDateFormat sdf = null;
                if (fs == null)
                {
                    sdf = new SimpleDateFormat(CDateTime.getPreferredFormat(iUnit));
                }
                for (int i = 0; i < da.length; i++)
                {
                    try
                    {
                        sText = ValueFormatter.format(cdtAxisValue, fs, rtc.getLocale(), sdf);
                    }
                    catch (DataFormatException dfex )
                    {
                        DefaultLoggerImpl.instance().log(dfex);
                        sText = IConstants.NULL_STRING;
                    }
                    la.getCaption().setValue(sText);
                    dW = computeWidth(xs, la);
                    if (dW > dMaxW)
                        dMaxW = dW;
                    cdtAxisValue = cdtAxisValue.forward(iUnit, iStep);
                }
            }
            return dMaxW;
        }
        else if (iOrientation == HORIZONTAL)
        {
            double dH, dMaxH = 0;
            if ((getType() & TEXT) == TEXT || bCategoryScale)
            {
                final DataSetIterator dsi = getData();
                final int iDateTimeUnit = (getType() == IConstants.DATE_TIME) ? CDateTime.computeUnit(dsi)
                    : IConstants.UNDEFINED;

                dsi.reset();
                while (dsi.hasNext())
                {
                    la.getCaption().setValue(formatCategoryValue(getType(), dsi.next(), iDateTimeUnit));
                    dH = computeHeight(xs, la);
                    if (dH > dMaxH)
                    {
                        dMaxH = dH;
                    }
                }
            }
            else if ((getType() & LINEAR) == LINEAR)
            {
                final NumberDataElement nde = NumberDataElementImpl.create(0);
                double dAxisValue = asDouble(getMinimum()).doubleValue();
                final double dAxisStep = asDouble(getStep()).doubleValue();
                DecimalFormat df = null;
                if (fs == null)
                {
                    df = new DecimalFormat(getNumericPattern());
                }
                for (int i = 0; i < da.length; i++)
                {
                    nde.setValue(dAxisValue);
                    try
                    {
                        sText = ValueFormatter.format(nde, fs, rtc.getLocale(), df);
                    }
                    catch (DataFormatException dfex )
                    {
                        DefaultLoggerImpl.instance().log(dfex);
                        sText = IConstants.NULL_STRING;
                    }
                    la.getCaption().setValue(sText);
                    dH = computeHeight(xs, la);
                    if (dH > dMaxH)
                        dMaxH = dH;
                    dAxisValue += dAxisStep;
                }
            }
            else if ((getType() & LOGARITHMIC) == LOGARITHMIC)
            {
                final NumberDataElement nde = NumberDataElementImpl.create(0);
                double dAxisValue = asDouble(getMinimum()).doubleValue();
                final double dAxisStep = asDouble(getStep()).doubleValue();
                DecimalFormat df = null;
                for (int i = 0; i < da.length; i++)
                {
                    if (fs == null)
                    {
                        df = new DecimalFormat(getNumericPattern(dAxisValue));
                    }
                    nde.setValue(dAxisValue);
                    try
                    {
                        sText = ValueFormatter.format(nde, fs, rtc.getLocale(), df);
                    }
                    catch (DataFormatException dfex )
                    {
                        DefaultLoggerImpl.instance().log(dfex);
                        sText = IConstants.NULL_STRING;
                    }
                    la.getCaption().setValue(sText);
                    dH = computeHeight(xs, la);
                    if (dH > dMaxH)
                        dMaxH = dH;
                    dAxisValue *= dAxisStep;
                }
            }
            else if ((getType() & DATE_TIME) == DATE_TIME)
            {
                CDateTime cdtAxisValue = asDateTime(getMinimum());
                final int iStep = asInteger(getStep());
                final int iUnit = asInteger(getUnit());
                SimpleDateFormat sdf = null;
                if (fs == null)
                {
                    sdf = new SimpleDateFormat(CDateTime.getPreferredFormat(iUnit));
                }
                for (int i = 0; i < da.length; i++)
                {
                    try
                    {
                        sText = ValueFormatter.format(cdtAxisValue, fs, rtc.getLocale(), sdf);
                    }
                    catch (DataFormatException dfex )
                    {
                        DefaultLoggerImpl.instance().log(dfex);
                        sText = IConstants.NULL_STRING;
                    }
                    la.getCaption().setValue(sText);
                    dH = computeHeight(xs, la);
                    if (dH > dMaxH)
                        dMaxH = dH;
                    cdtAxisValue.forward(iUnit, iStep);
                }
            }
            return dMaxH;
        }
        return 0;
    }

    final boolean isStepFixed()
    {
        return bStepFixed;
    }

    final boolean isMinimumFixed()
    {
        return bMinimumFixed;
    }

    final boolean isMaximumFixed()
    {
        return bMaximumFixed;
    }

    public final boolean isCategoryScale()
    {
        return bCategoryScale;
    }

    /**
     * 
     * @param iMinorUnitsPerMajor
     * @return
     */
    public final double[] getMinorCoordinates(int iMinorUnitsPerMajor)
    {
        if (daTickCoordinates == null || iMinorUnitsPerMajor <= 0)
        {
            return null;
        }

        final double[] da = new double[iMinorUnitsPerMajor];
        final double dUnit = getUnitSize();
        if ((iType & LOGARITHMIC) != LOGARITHMIC)
        {
            final double dEach = dUnit / iMinorUnitsPerMajor;
            for (int i = 1; i < iMinorUnitsPerMajor; i++)
            {
                da[i - 1] = dEach * i;
            }
        }
        else
        {
            final double dCount = iMinorUnitsPerMajor;
            final double dMax = Math.log(dCount);

            for (int i = 0; i < iMinorUnitsPerMajor; i++)
            {
                da[i] = (Math.log(i + 1) * dUnit) / dMax;
            }
        }
        da[iMinorUnitsPerMajor - 1] = dUnit;
        return da;
    }
    
    public RunTimeContext getRunTimeContext()
    {
        return rtc;
    }
}
