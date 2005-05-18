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

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This class provides an internal implementation of the axis class used by the renderer. Note that positions,
 * orientations, rotation angles, etc could be transposed and may not reflect the same values maintained by the model.
 */
public final class OneAxis
{
    private AutoScale sc;

    private double dAxisRenderingCoordinate;

    private double dTitleRenderingCoordinate;

    private int iOrientation;

    private int iLabelPosition, iTitlePosition;

    private LineAttributes lia = null;

    private Label la = null; // FOR AXIS LABELS

    private Label laTitle = null; // FOR AXIS TITLE

    private IntersectionValue iv = null;

    private Grid gr = null;

    private boolean bCategoryScale = false;

    private final Axis axModel;

    public int getCombinedTickStyle()
    {
        return gr.getTickStyle(IConstants.MAJOR) | gr.getTickStyle(IConstants.MINOR);
    }

    /**
     * A default zero-arg constructor
     */
    OneAxis(Axis axModel)
    {
        this.axModel = axModel;
        gr = new Grid();
    }

    /**
     * 
     * @param _iOrientation
     * @param _iLabelLocation
     * @param _iLabelRotation
     * @param _iTickStyle
     * @param _iAxisLocation
     */
    void set(int _iOrientation, int _iLabelPosition, int _iTitlePosition, boolean _bCategoryScale)
    {
        iOrientation = _iOrientation;
        iLabelPosition = _iLabelPosition;
        iTitlePosition = _iTitlePosition;
        bCategoryScale = _bCategoryScale;
    }

    void setAxisCoordinate(double _dAxisRenderingCoordinate)
    {
        dAxisRenderingCoordinate = _dAxisRenderingCoordinate;
    }

    void setTitleCoordinate(double _dTitleRenderingCoordinate)
    {
        dTitleRenderingCoordinate = _dTitleRenderingCoordinate;
    }

    public final double getAxisCoordinate()
    {
        return dAxisRenderingCoordinate;
    }

    public final double getTitleCoordinate()
    {
        return dTitleRenderingCoordinate;
    }

    public final int getLabelPosition()
    {
        return iLabelPosition;
    }

    public final int getTitlePosition()
    {
        return iTitlePosition;
    }

    public final Axis getModelAxis()
    {
        return axModel;
    }

    final void setGridProperties(LineAttributes laMajorGrid, LineAttributes laMinorGrid, LineAttributes laMajorTicks, LineAttributes laMinorTicks, int iMajorTickStyle,
        int iMinorTickStyle, int iMinorUnitsPerMajorUnit)
    {
        gr.laMajorGrid = laMajorGrid;
        gr.laMinorGrid = laMinorGrid;
        gr.laMajorTicks = laMajorTicks;
        gr.laMinorTicks = laMinorTicks;
        gr.iMajorTickStyle = iMajorTickStyle;
        gr.iMinorTickStyle = iMinorTickStyle;
        gr.iMinorUnitsPerMajorUnit = iMinorUnitsPerMajorUnit;
    }

    public final Grid getGrid()
    {
        return gr;
    }

    public final int getOrientation()
    {
        return iOrientation;
    }

    public final boolean isCategoryScale()
    {
        return bCategoryScale;
    }

    /**
     * 
     * @param _sc
     */
    void set(AutoScale _sc)
    {
        sc = _sc;
    }

    public AutoScale getScale()
    {
        return sc;
    }

    void set(IntersectionValue _iv)
    {
        iv = _iv;
    }

    void set(Label _laAxisLabels, Label _laAxisTitle)
    {
        la = (Label) EcoreUtil.copy(_laAxisLabels);
        laTitle = (Label) EcoreUtil.copy(_laAxisTitle);
    }

    void set(LineAttributes _la)
    {
        lia = _la;
    }

    public final LineAttributes getLineAttributes()
    {
        return lia;
    }

    public final IntersectionValue getIntersectionValue()
    {
        return iv;
    }

    public final Label getLabel()
    {
        return la;
    }

    public final Label getTitle()
    {
        return laTitle;
    }

    public final FormatSpecifier getFormatSpecifier()
    {
        return axModel.getFormatSpecifier();
    }

    public final RunTimeContext getRunTimeContext()
    {
        return sc.getRunTimeContext();
    }
}
