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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.IntersectionValue;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.exception.DataFormatException;
import org.eclipse.birt.chart.exception.NullValueException;
import org.eclipse.birt.chart.exception.UnexpectedInputException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.util.CDateTime;

/**
 * 
 */
public class Methods implements IConstants
{

    /**
     * 
     * @param o
     * @return
     */
    public static final CDateTime asDateTime(Object o)
    {
        if (o instanceof Calendar)
        {
            return new CDateTime((Calendar) o);
        }
        return (CDateTime) o;
    }

    /**
     * 
     * @param o
     * @return
     */
    public static final Double asDouble(Object o)
    {
        if (o instanceof Double)
        {
            return (Double) o;
        }
        return new Double(((Number) o).doubleValue());
    }

    /**
     * 
     * @param o
     * @return
     */
    public static final int asInteger(Object o)
    {
        return ((Number) o).intValue();
    }

    /**
     * 
     * @param sc
     * @param dValue
     * 
     * @return
     */
    public static final double getLocation(AutoScale sc, IntersectionValue iv)
    {
        double[] da = sc.getTickCordinates();
        if (iv.getType() == IntersectionValue.MIN)
        {
            return da[0];
        }
        else if (iv.getType() == IntersectionValue.MAX)
        {
            return da[da.length - 1];
        }

        if ((sc.getType() & DATE_TIME) == DATE_TIME)
        {
            CDateTime cdtValue = asDateTime(iv.getValue());
            CDateTime cdt = asDateTime(sc.getMinimum()), cdtPrev = null;
            CDateTime cdtEnd = asDateTime(sc.getMaximum());
            int iUnit = asInteger(sc.getUnit());
            int iStep = asInteger(sc.getStep());

            for (int i = 0; i < da.length; i++)
            {
                if (cdt.after(cdtValue))
                {
                    if (cdtPrev == null)
                    {
                        return da[i];
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    String sMin = sdf.format(cdtPrev.getTime());
                    String sMax = sdf.format(cdt.getTime());
                    String sVal = sdf.format(cdtValue.getTime());

                    long l1 = cdtPrev.getTimeInMillis();
                    long l2 = cdt.getTimeInMillis();
                    long l = cdtValue.getTimeInMillis();
                    double dUnitSize = da[i] - da[i - 1];

                    double dOffset = (dUnitSize / (double) (l2 - l1)) * (double) (l - l1);
                    return da[i - 1] + dOffset;

                }
                cdtPrev = cdt;
                cdt = cdt.forward(iUnit, iStep);
            }
            return da[da.length - 1];
        }
        else if ((sc.getType() & TEXT) == TEXT)
        {
            double dValue = iv.getValueAsDouble(sc);
            return da[0] + (da[1] - da[0]) * dValue;
        }
        else if ((sc.getType() & LOGARITHMIC) == LOGARITHMIC)
        {
            double dValue = iv.getValueAsDouble(sc);
            if (dValue == 0) // CANNOT GO TO '0'
            {
                return sc.getStart();
            }
            if (dValue < 0)
            {
                return sc.getStart();
            }
            double dMinimumLog = Math.log(asDouble(sc.getMinimum()).doubleValue()) / LOG_10;
            double dStepLog = Math.log(asDouble(sc.getStep()).doubleValue()) / LOG_10;
            double dValueLog = Math.log(dValue) / LOG_10;
            return da[0] - (((dValueLog - dMinimumLog) / dStepLog) * (da[0] - da[1]));
        }
        else
        {
            double dValue = iv.getValueAsDouble(sc);
            double dMinimum = asDouble(sc.getMinimum()).doubleValue();
            double dStep = asDouble(sc.getStep()).doubleValue();
            return da[0] - (((dValue - dMinimum) / dStep) * (da[0] - da[1]));
        }
    }

    /**
     * 
     * @param sc
     * @param oValue
     * 
     * @return
     * 
     * @throws NullValueException
     * @throws DataFormatException
     */
    public static final double getLocation(AutoScale sc, Object oValue) throws NullValueException, DataFormatException,
        UnexpectedInputException
    {
        if (oValue == null)
        {
            throw new NullValueException("Cannot locate co-ordinate for null value on scale " + sc);
        }
        if (oValue instanceof Double)
        {
            return getLocation(sc, ((Double) oValue).doubleValue());
        }
        else if (oValue instanceof Calendar)
        {
            return getDateLocation(sc, new CDateTime((Calendar) oValue));
        }
        else if (oValue instanceof NumberDataElement)
        {
            return getLocation(sc, ((NumberDataElement) oValue).getValue());
        }
        else if (oValue instanceof DateTimeDataElement)
        {
            return getDateLocation(sc, ((DateTimeDataElement) oValue).getValueAsCDateTime());
        }
        DefaultLoggerImpl.instance().log(ILogger.WARNING,
            "Unexpected data type " + oValue.getClass().getName() + "[value=" + oValue + "] specified");
        return sc.getStart(); // RETURNS THE START EDGE OF THE SCALE
    }

    /**
     * 
     * @param sc
     * @param dValue
     * 
     * @return
     */
    public static final double getLocation(AutoScale sc, double dValue) throws UnexpectedInputException
    {
        if ((sc.getType() & IConstants.LINEAR) == IConstants.LINEAR)
        {
            double dMinimum = asDouble(sc.getMinimum()).doubleValue();
            double dStep = asDouble(sc.getStep()).doubleValue();
            double[] da = sc.getTickCordinates();
            return da[0] - (((dValue - dMinimum) / dStep) * (da[0] - da[1]));
        }
        else if ((sc.getType() & IConstants.TEXT) == IConstants.TEXT)
        {
            double[] da = sc.getTickCordinates();
            return da[0] + (da[1] - da[0]) * dValue;
        }
        else if ((sc.getType() & IConstants.LOGARITHMIC) == IConstants.LOGARITHMIC)
        {
            if (dValue == 0) // CANNOT GO TO '0'
            {
                return sc.getStart();
            }
            if (dValue < 0)
            {
                throw new UnexpectedInputException("Zero or negative values may not be located on a logarithmic scale");
            }
            double dMinimumLog = Math.log(asDouble(sc.getMinimum()).doubleValue()) / LOG_10;
            double dStepLog = Math.log(asDouble(sc.getStep()).doubleValue()) / LOG_10;
            double dValueLog = Math.log(dValue) / LOG_10;
            double[] da = sc.getTickCordinates();
            return da[0] - (((dValueLog - dMinimumLog) / dStepLog) * (da[0] - da[1]));
        }
        return 0;
    }

    /**
     * 
     * @param sc
     * @param cdt
     * @return
     */
    static final double getDateLocation(AutoScale sc, CDateTime cdtValue)
    {
        double[] da = sc.getTickCordinates();
        CDateTime cdt = asDateTime(sc.getMinimum()), cdtPrev = null;
        CDateTime cdtEnd = asDateTime(sc.getMaximum());
        int iUnit = asInteger(sc.getUnit());
        int iStep = asInteger(sc.getStep());

        for (int i = 0; i < da.length; i++)
        {
            if (cdt.after(cdtValue))
            {
                if (cdtPrev == null)
                {
                    return da[i];
                }
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                String sMin = sdf.format(cdtPrev.getTime());
                String sMax = sdf.format(cdt.getTime());
                String sVal = sdf.format(cdtValue.getTime());

                long l1 = cdtPrev.getTimeInMillis();
                long l2 = cdt.getTimeInMillis();
                long l = cdtValue.getTimeInMillis();
                double dUnitSize = da[i] - da[i - 1];

                double dOffset = (dUnitSize / (double) (l2 - l1)) * (double) (l - l1);
                return da[i - 1] + dOffset;

            }
            cdtPrev = cdt;
            cdt = cdt.forward(iUnit, iStep);
        }
        return da[da.length - 1];
    }

    /**
     * 
     * @param g2d
     * @param fm
     * @param sText
     * @param dAngleInDegrees
     * @return
     */
    protected final double computeWidth(IDisplayServer xs, Label la)
    {
        final FontDefinition fd = la.getCaption().getFont();
        final double dAngleInRadians = ((-fd.getRotation() * Math.PI) / 180.0);
        final double dSineTheta = Math.abs(Math.sin(dAngleInRadians));
        final double dCosTheta = Math.abs(Math.cos(dAngleInRadians));
        final ITextMetrics itm = xs.getTextMetrics(la);
        double dW = itm.getFullWidth() * dCosTheta + itm.getFullHeight() * dSineTheta;
        itm.dispose();
        return dW;
    }

    /**
     * 
     * @param g2d
     * @param fm
     * @param sText
     * @param iAngleInDegrees
     * @return
     */
    protected final double computeHeight(IDisplayServer xs, Label la)
    {
        final FontDefinition fd = la.getCaption().getFont();
        final double dAngleInRadians = ((-fd.getRotation() * Math.PI) / 180.0);
        final double dSineTheta = Math.abs(Math.sin(dAngleInRadians));
        final double dCosTheta = Math.abs(Math.cos(dAngleInRadians));
        final ITextMetrics itm = xs.getTextMetrics(la);
        double dH = itm.getFullWidth() * dSineTheta + itm.getFullHeight() * dCosTheta;
        itm.dispose();
        return dH;
    }

    /**
     * 
     * @param iLabelLocation
     * @param g2d
     * @param fm
     * @param sText
     * @param dAngleInDegrees
     * @param dX
     * @param dY
     * @return
     */
    public static final RotatedRectangle computePolygon(IDisplayServer xs, int iLabelLocation, Label la, double dX,
        double dY)
    {
        double dAngleInDegrees = la.getCaption().getFont().getRotation();
        final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
        final double dSineTheta = Math.abs(Math.sin(dAngleInRadians));
        final double dCosTheta = Math.abs(Math.cos(dAngleInRadians));

        final ITextMetrics itm = xs.getTextMetrics(la);
        double dW = itm.getFullWidth();
        double dH = itm.getFullHeight();

        RotatedRectangle rr = null;
        if (iLabelLocation == LEFT)
        {
            // ZERO : HORIZONTAL
            if (dAngleInDegrees == 0)
            {
                rr = new RotatedRectangle(dX - dW, dY - dH / 2, // TL
                    dX, dY - dH / 2, // TR
                    dX, dY + dH / 2, // BR
                    dX - dW, dY + dH / 2 // BL
                );
            }
            // POSITIVE
            else if (dAngleInDegrees > 0 && dAngleInDegrees < 90)
            {
                rr = new RotatedRectangle(dX - dH * dSineTheta - dW * dCosTheta, dY - dH * dCosTheta + dW * dSineTheta, // TL
                    dX - dH * dSineTheta, dY - dH * dCosTheta, // TR
                    dX, dY, // BR
                    dX - dW * dCosTheta, dY + dW * dSineTheta // BL
                );
            }
            // NEGATIVE
            else if (dAngleInDegrees < 0 && dAngleInDegrees > -90)
            {
                rr = new RotatedRectangle(dX - dW * dCosTheta, dY - dW * dSineTheta, // TL
                    dX, dY, // TR
                    dX - dH * dSineTheta, dY + dH * dCosTheta, // BR
                    dX - dH * dSineTheta - dW * dCosTheta, dY + dH * dCosTheta - dW * dSineTheta // BL
                );
            }
            // ±90 : VERTICALLY UP OR DOWN
            else if (dAngleInDegrees == 90 || dAngleInDegrees == -90)
            {
                rr = new RotatedRectangle(dX - dH, dY - dW / 2, // TL
                    dX, dY - dW / 2, // TR
                    dX, dY + dW / 2, // BR
                    dX - dH, dY + dW / 2 // BL
                );
            }
        }
        else if (iLabelLocation == RIGHT)
        {
            // ZERO : HORIZONTAL
            if (dAngleInDegrees == 0)
            {
                rr = new RotatedRectangle(dX, dY - dH / 2, // TL
                    dX + dW, dY - dH / 2, // TR
                    dX + dW, dY + dH / 2, // BR
                    dX, dY + dH / 2 // BL
                );
            }
            // POSITIVE
            else if (dAngleInDegrees > 0 && dAngleInDegrees < 90)
            {
                rr = new RotatedRectangle(dX, dY, // TL
                    dX + dW * dCosTheta, dY - dW * dSineTheta, // TR
                    dX + dW * dCosTheta + dH * dSineTheta, dY - dW * dSineTheta + dH * dCosTheta, // BR
                    dX + dH * dSineTheta, dY + dH * dCosTheta // BL
                );
            }
            // NEGATIVE
            else if (dAngleInDegrees < 0 && dAngleInDegrees > -90)
            {
                rr = new RotatedRectangle(dX + dH * dSineTheta, dY - dH * dCosTheta, // TL
                    dX + dH * dSineTheta + dW * dCosTheta, dY - dH * dCosTheta + dW * dSineTheta, // TR
                    dX + dW * dCosTheta, dY + dW * dSineTheta, // BR
                    dX, dY // BL
                );
            }
            // ±90 : VERTICALLY UP OR DOWN
            else if (dAngleInDegrees == 90 || dAngleInDegrees == -90)
            {
                rr = new RotatedRectangle(dX, dY - dW / 2, // TL
                    dX + dH, dY - dW / 2, // TR
                    dX + dH, dY + dW / 2, // BR
                    dX, dY + dW / 2 // BL
                );
            }
        }
        else if (iLabelLocation == BOTTOM)
        {
            // ZERO : HORIZONTAL
            if (dAngleInDegrees == 0)
            {
                rr = new RotatedRectangle(dX - dW / 2, dY, // TL
                    dX + dW / 2, dY, // TR
                    dX + dW / 2, dY + dH, // BR
                    dX - dW / 2, dY + dH // BL
                );
            }
            // POSITIVE
            else if (dAngleInDegrees > 0 && dAngleInDegrees < 90)
            {
                rr = new RotatedRectangle(dX - dW * dCosTheta, dY + dW * dSineTheta, // TL
                    dX, dY, // TR
                    dX + dH * dSineTheta, dY + dH * dCosTheta, // BR
                    dX + dH * dSineTheta - dW * dCosTheta, dY + dH * dCosTheta + dW * dSineTheta // BL
                );
            }
            // NEGATIVE
            else if (dAngleInDegrees < 0 && dAngleInDegrees > -90)
            {
                rr = new RotatedRectangle(dX, dY, // TL
                    dX + dW * dCosTheta, dY + dW * dSineTheta, // TR
                    dX + dW * dCosTheta - dH * dSineTheta, dY + dW * dSineTheta + dH * dCosTheta, // BR
                    dX - dH * dSineTheta, dY + dH * dCosTheta // BL
                );
            }
            // ±90 : VERTICALLY UP OR DOWN
            else if (dAngleInDegrees == 90 || dAngleInDegrees == -90)
            {
                rr = new RotatedRectangle(dX - dH / 2, dY, // TL
                    dX + dH / 2, dY, // TR
                    dX + dH / 2, dY + dW, // BR
                    dX - dH / 2, dY + dW // BL
                );
            }
        }
        else if (iLabelLocation == TOP)
        {
            // ZERO : HORIZONTAL
            if (dAngleInDegrees == 0)
            {
                rr = new RotatedRectangle(dX - dW / 2, dY - dH, // TL
                    dX + dW / 2, dY - dH, // TR
                    dX + dW / 2, dY, // BR
                    dX - dW / 2, dY // BL
                );
            }
            // POSITIVE
            else if (dAngleInDegrees > 0 && dAngleInDegrees < 90)
            {
                rr = new RotatedRectangle(dX - dH * dSineTheta, dY - dH * dCosTheta, // TL
                    dX - dH * dSineTheta + dW * dCosTheta, dY - dH * dCosTheta - dW * dSineTheta, // TR
                    dX + dW * dCosTheta, dY - dW * dSineTheta, // BR
                    dX, dY // BL
                );
            }
            // NEGATIVE
            else if (dAngleInDegrees < 0 && dAngleInDegrees > -90)
            {
                rr = new RotatedRectangle(dX - dW * dCosTheta + dH * dSineTheta, dY - dW * dSineTheta - dH * dCosTheta, // TL
                    dX + dH * dSineTheta, dY - dH * dCosTheta, // TR
                    dX, dY, // BR
                    dX - dW * dCosTheta, dY - dW * dSineTheta // BL
                );
            }
            // ±90 : VERTICALLY UP OR DOWN
            else if (dAngleInDegrees == 90 || dAngleInDegrees == -90)
            {
                rr = new RotatedRectangle(dX - dH / 2, dY - dW, // TL
                    dX + dH / 2, dY - dW, // TR
                    dX + dH / 2, dY, // BR
                    dX - dH / 2, dY // BL
                );
            }
        }
        itm.dispose();
        return rr;
    }

    public static final BoundingBox computeBox(IDisplayServer xs, int iLabelLocation, Label la, double dX, double dY)
    {
        double dAngleInDegrees = la.getCaption().getFont().getRotation();
        final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
        final double dSineTheta = Math.abs(Math.sin(dAngleInRadians));
        final double dCosTheta = Math.abs(Math.cos(dAngleInRadians));

        final ITextMetrics itm = xs.getTextMetrics(la);
        double dW = itm.getFullWidth();
        double dH = itm.getFullHeight();

        BoundingBox bb = null;
        if (iLabelLocation == LEFT)
        {
            // ZERO : HORIZONTAL
            if (dAngleInDegrees == 0)
            {
                bb = new BoundingBox(LEFT, dX - dW, dY - dH / 2, dW, dH, dH / 2);
            }
            // POSITIVE
            else if (dAngleInDegrees > 0 && dAngleInDegrees < 90)
            {
                bb = new BoundingBox(LEFT, dX - (dH * dSineTheta + dW * dCosTheta), dY - dH * dCosTheta, dH
                    * dSineTheta + dW * dCosTheta, dH * dCosTheta + dW * dSineTheta, dH * dCosTheta);
            }
            // NEGATIVE
            else if (dAngleInDegrees < 0 && dAngleInDegrees > -90)
            {
                bb = new BoundingBox(LEFT, dX - (dH * dSineTheta + dW * dCosTheta), dY - dW * dSineTheta, dH
                    * dSineTheta + dW * dCosTheta, dH * dCosTheta + dW * dSineTheta, dW * dSineTheta);
            }
            // ±90 : VERTICALLY UP OR DOWN
            else if (dAngleInDegrees == 90 || dAngleInDegrees == -90)
            {
                bb = new BoundingBox(LEFT, dX - dH, dY - dW / 2, dH, dW, dW / 2);
            }
        }
        else if (iLabelLocation == RIGHT)
        {
            // ZERO : HORIZONTAL
            if (dAngleInDegrees == 0)
            {
                bb = new BoundingBox(RIGHT, dX, dY - dH / 2, dW, dH, dH / 2);
            }
            // POSITIVE
            else if (dAngleInDegrees > 0 && dAngleInDegrees < 90)
            {
                bb = new BoundingBox(RIGHT, dX, dY - dW * dSineTheta, dH * dSineTheta + dW * dCosTheta, dH * dCosTheta
                    + dW * dSineTheta, dW * dSineTheta);
            }
            // NEGATIVE
            else if (dAngleInDegrees < 0 && dAngleInDegrees > -90)
            {
                bb = new BoundingBox(RIGHT, dX, dY - dH * dCosTheta, dH * dSineTheta + dW * dCosTheta, dH * dCosTheta
                    + dW * dSineTheta, dH * dCosTheta);
            }
            // ±90 : VERTICALLY UP OR DOWN
            else if (dAngleInDegrees == 90 || dAngleInDegrees == -90)
            {
                bb = new BoundingBox(RIGHT, dX, dY - dW / 2, dH, dW, dW / 2);
            }
        }
        else if (iLabelLocation == BOTTOM)
        {
            // ZERO : HORIZONTAL
            if (dAngleInDegrees == 0)
            {
                bb = new BoundingBox(BOTTOM, dX - dW / 2, dY, dW, dH, dW / 2);
            }
            // POSITIVE
            else if (dAngleInDegrees > 0 && dAngleInDegrees < 90)
            {
                bb = new BoundingBox(BOTTOM, dX - dW * dCosTheta, dY, dH * dSineTheta + dW * dCosTheta, dH * dCosTheta
                    + dW * dSineTheta, dW * dCosTheta);
            }
            // NEGATIVE
            else if (dAngleInDegrees < 0 && dAngleInDegrees > -90)
            {
                bb = new BoundingBox(BOTTOM, dX - dH * dSineTheta, dY, dH * dSineTheta + dW * dCosTheta, dH * dCosTheta
                    + dW * dSineTheta, dH * dSineTheta);
            }
            // ±90 : VERTICALLY UP OR DOWN
            else if (dAngleInDegrees == 90 || dAngleInDegrees == -90)
            {
                bb = new BoundingBox(BOTTOM, dX - dH / 2, dY, dH, dW, dH / 2);
            }
        }
        else if (iLabelLocation == TOP)
        {
            // ZERO : HORIZONTAL
            if (dAngleInDegrees == 0)
            {
                bb = new BoundingBox(TOP, dX - dW / 2, dY - dH, dW, dH, dW / 2);
            }
            // POSITIVE
            else if (dAngleInDegrees > 0 && dAngleInDegrees < 90)
            {
                bb = new BoundingBox(TOP, dX - dH * dSineTheta, dY - (dH * dCosTheta + dW * dSineTheta), dH
                    * dSineTheta + dW * dCosTheta, dH * dCosTheta + dW * dSineTheta, dH * dSineTheta);
            }
            // NEGATIVE
            else if (dAngleInDegrees < 0 && dAngleInDegrees > -90)
            {
                bb = new BoundingBox(TOP, dX - dW * dCosTheta, dY - (dH * dCosTheta + dW * dSineTheta), dH * dSineTheta
                    + dW * dCosTheta, dH * dCosTheta + dW * dSineTheta, dW * dCosTheta);
            }
            // ±90 : VERTICALLY UP OR DOWN
            else if (dAngleInDegrees == 90 || dAngleInDegrees == -90)
            {
                bb = new BoundingBox(TOP, dX - dH / 2, dY - dW, dH, dW, dH / 2);
            }
        }
        itm.dispose();

        return bb;
    }

    /**
     * Converts to internal (non public-model) data structures
     * 
     * @param ax
     * @return
     */
    public static final int getLabelPosition(Position lp)
    {
        int iLabelPosition = UNDEFINED;
        switch (lp.getValue())
        {
            case Position.LEFT:
                iLabelPosition = LEFT;
                break;
            case Position.RIGHT:
                iLabelPosition = RIGHT;
                break;
            case Position.ABOVE:
                iLabelPosition = ABOVE;
                break;
            case Position.BELOW:
                iLabelPosition = BELOW;
                break;
            case Position.OUTSIDE:
                iLabelPosition = OUTSIDE;
                break;
            case Position.INSIDE:
                iLabelPosition = INSIDE;
                break;
        }
        return iLabelPosition;
    }

    /**
     * 
     * @param oaData
     * @param iIndex
     * 
     * @return
     * 
     * @throws DataFormatException
     */
    public final Object getValue(Object oaData, int iIndex) throws DataFormatException
    {
        if (oaData instanceof double[])
        {
            return new Double(((double[]) oaData)[iIndex]);
        }
        else if (oaData instanceof CDateTime[])
        {
            return ((CDateTime[]) oaData)[iIndex];
        }
        else if (oaData instanceof String[])
        {
            return ((String[]) oaData)[iIndex];
        }
        else if (oaData instanceof Object[])
        {
            return ((Object[]) oaData)[iIndex];
        }
        throw new DataFormatException("Unexpected internal dataset structure");
    }
}