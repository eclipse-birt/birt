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

/**
 *  
 */
public final class AxisSubUnit
{

    /**
     *  
     */
    private double dValueMax = 0;

    /**
     *  
     */
    private double dValueMin = 0;

    /**
     *  
     */
    private double dPositiveTotal = 0;

    /**
     *  
     */
    private double dNegativeTotal = 0;

    /**
     *  
     */
    AxisSubUnit()
    {
    }

    /**
     *  
     */
    public final void reset()
    {
        dValueMax = 0;
        dValueMin = 0;
    }

    /**
     * @return Returns the valueMax.
     */
    public final double getValueMax()
    {
        return dValueMax;
    }

    /**
     * @param dValueMax
     *            The valueMax to set.
     */
    public final void setValueMax(double dValueMax)
    {
        this.dValueMax = dValueMax;
    }

    /**
     * @return Returns the valueMin.
     */
    public final double getValueMin()
    {
        return dValueMin;
    }

    /**
     * @param dValueMin
     *            The valueMin to set.
     */
    public final void setValueMin(double dValueMin)
    {
        this.dValueMin = dValueMin;
    }

    /**
     * 
     * @param dPositiveTotal
     */
    public final void setPositiveTotal(double dPositiveTotal)
    {
        this.dPositiveTotal = dPositiveTotal;
    }

    /**
     * 
     * @return
     */
    public final double getPositiveTotal()
    {
        return dPositiveTotal;
    }

    /**
     * 
     * @param dPositiveTotal
     */
    public final void setNegativeTotal(double dNegativeTotal)
    {
        this.dNegativeTotal = dNegativeTotal;
    }

    /**
     * 
     * @return
     */
    public final double getNegativeTotal()
    {
        return dNegativeTotal;
    }

    /**
     * 
     * @param dValue
     * @return
     */
    public final double valuePercentage(double dValue)
    {
        return (dValue * 100d) / (dPositiveTotal - dNegativeTotal);
    }
}
