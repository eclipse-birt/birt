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

import java.util.ArrayList;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.exception.GenerationException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * 
 */
public final class LegendBuilder
{

    private final double dHorizontalSpacing = 4;

    private final double dVerticalSpacing = 4;

    private Size sz;

    public LegendBuilder()
    {
    }

    /**
     * Computes the size of the legend
     * 
     * @param lg
     * @param sea
     * 
     * @throws GenerationException
     */
    public final Size compute(IDisplayServer xs, Chart cm, SeriesDefinition[] seda) throws GenerationException
    {
        // THREE CASES:
        // 1. ALL SERIES IN ONE ARRAYLIST
        // 2. ONE SERIES PER ARRAYLIST
        // 3. ALL OTHERS

        Legend lg = cm.getLegend();
        if (!lg.isSetOrientation())
        {
            throw new GenerationException(
                "The legend's orientation was not explicitly set to either horizontal or vertical");
        }
        if (!lg.isSetDirection())
        {
            throw new GenerationException(
                "The legend's direction was not explicitly set to either top-bottom or left-right");
        }

        // INITIALIZATION OF VARS USED IN FOLLOWING LOOPS
        Orientation o = lg.getOrientation();
        Direction d = lg.getDirection();

        Label la = LabelImpl.create();
        la.setCaption((Text) EcoreUtil.copy(lg.getText()));

        ClientArea ca = lg.getClientArea();
        LineAttributes lia = ca.getOutline();
        double dSeparatorThickness = lia.getThickness();
        double dWidth = 0, dHeight = 0;
        la.getCaption().setValue("X");
        final ITextMetrics itm = xs.getTextMetrics(la);
        double dItemHeight = itm.getFullHeight();
        String sRC;
        Series se;
        ArrayList al;
        Insets insCA = ca.getInsets().scaledInstance(xs.getDpiResolution() / 72d);
        final boolean bPaletteByCategory = (cm.getLegend().getItemType().getValue() == LegendItemType.CATEGORIES);

        Series seBase;

        // COMPUTATIONS HERE MUST BE IN SYNC WITH THE ACTUAL RENDERER
        if (o.getValue() == Orientation.VERTICAL)
        {
            double dW, dMaxW = 0;
            if (bPaletteByCategory)
            {
                SeriesDefinition sdBase = null;
                if (cm instanceof ChartWithAxes)
                {
                    final Axis axPrimaryBase = ((ChartWithAxes) cm).getBaseAxes()[0]; // ONLY SUPPORT 1 BASE AXIS FOR
                    // NOW
                    if (axPrimaryBase.getSeriesDefinitions().isEmpty())
                    {
                        return SizeImpl.create(0, 0);
                        //throw new GenerationException("The primary base axis
                        // does not contain any series definitions");
                    }
                    sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions().get(0); // OK TO ASSUME
                    // THAT 1 BASE
                    // SERIES
                    // DEFINITION
                    // EXISTS
                }
                else if (cm instanceof ChartWithoutAxes)
                {
                    if (((ChartWithoutAxes) cm).getSeriesDefinitions().isEmpty())
                    {
                        return SizeImpl.create(0, 0);
                        //throw new GenerationException("The primary base axis
                        // does not contain any series definitions");
                    }
                    sdBase = (SeriesDefinition) ((ChartWithoutAxes) cm).getSeriesDefinitions().get(0); // OK TO ASSUME
                    // THAT 1 BASE
                    // SERIES
                    // DEFINITION
                    // EXISTS
                }
                seBase = (Series) sdBase.getRunTimeSeries().get(0); // OK TO
                // ASSUME
                // THAT 1
                // BASE
                // RUNTIME
                // SERIES
                // EXISTS

                DataSetIterator dsiBase = null;
                try
                {
                    dsiBase = new DataSetIterator(seBase.getDataSet());
                }
                catch (Exception ex )
                {
                    throw new GenerationException(ex);
                }

                while (dsiBase.hasNext())
                {
                    la.getCaption().setValue(String.valueOf(dsiBase.next()));
                    itm.reuse(la);
                    dWidth = Math.max(itm.getFullWidth(), dWidth);
                    dHeight += insCA.getTop() + itm.getFullHeight() + insCA.getBottom();
                }
                dWidth += insCA.getLeft() + (3 * dItemHeight) / 2 + dHorizontalSpacing + insCA.getRight();
            }
            else if (d.getValue() == Direction.TOP_BOTTOM) // (VERTICAL =>
            // TB)
            {
                dSeparatorThickness += dVerticalSpacing;
                for (int j = 0; j < seda.length; j++)
                {
                    al = seda[j].getRunTimeSeries();
                    for (int i = 0; i < al.size(); i++)
                    {
                        se = (Series) al.get(i);
                        la.getCaption().setValue(String.valueOf(se.getSeriesIdentifier()));// TBD: APPLY FORMAT
                        // SPECIFIER
                        itm.reuse(la);
                        dW = itm.getFullWidth();
                        if (dW > dMaxW)
                        {
                            dMaxW = dW;
                        }
                        dHeight += insCA.getTop() + dItemHeight + insCA.getBottom();
                    }

                    // SETUP HORIZONTAL SEPARATOR SPACING
                    if (j < seda.length - 1)
                    {
                        dHeight += dSeparatorThickness;
                    }
                }

                // LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING + MAX
                // ITEM WIDTH + RIGHT INSETS
                dWidth = insCA.getLeft() + (3 * dItemHeight) / 2 + dHorizontalSpacing + dMaxW + insCA.getRight();
            }
            else if (d.getValue() == Direction.LEFT_RIGHT) // (VERTICAL =>
            // LR)
            {
                double dMaxH = 0;
                dSeparatorThickness += dHorizontalSpacing;
                for (int j = 0; j < seda.length; j++)
                {
                    al = seda[j].getRunTimeSeries();
                    for (int i = 0; i < al.size(); i++)
                    {
                        se = (Series) al.get(i);
                        la.getCaption().setValue(String.valueOf(se.getSeriesIdentifier()));// TBD: APPLY FORMAT
                        // SPECIFIER
                        itm.reuse(la);
                        dMaxW = Math.max(dMaxW, itm.getFullWidth());
                        dHeight += insCA.getTop() + dItemHeight + insCA.getBottom();
                    }

                    // SETUP VERTICAL SEPARATOR SPACING
                    if (j < seda.length - 1)
                    {
                        dWidth += dSeparatorThickness;
                    }
                    // LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING +
                    // MAX ITEM WIDTH + RIGHT INSETS
                    dWidth += insCA.getLeft() + (3 * dItemHeight / 2) + dHorizontalSpacing + dMaxW + insCA.getRight();

                    if (dHeight > dMaxH)
                        dMaxH = dHeight;
                    dHeight = 0;
                    dMaxW = 0;
                }
                dHeight = dMaxH;
            }
            else
            {
                throw new GenerationException("Invalid argument specified for legend rendering direction = " + d);
            }
        }
        else if (o.getValue() == Orientation.HORIZONTAL)
        {
            if (bPaletteByCategory)
            {
                SeriesDefinition sdBase = null;
                if (cm instanceof ChartWithAxes)
                {
                    final Axis axPrimaryBase = ((ChartWithAxes) cm).getBaseAxes()[0]; // ONLY SUPPORT 1 BASE AXIS FOR
                    // NOW
                    if (axPrimaryBase.getSeriesDefinitions().isEmpty())
                    {
                        throw new GenerationException("The primary base axis does not contain any series definitions");
                    }
                    sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions().get(0); // OK TO ASSUME
                    // THAT 1 BASE
                    // SERIES
                    // DEFINITION
                    // EXISTS
                }
                else if (cm instanceof ChartWithoutAxes)
                {
                    if (((ChartWithoutAxes) cm).getSeriesDefinitions().isEmpty())
                    {
                        throw new GenerationException("The primary base axis does not contain any series definitions");
                    }
                    sdBase = (SeriesDefinition) ((ChartWithoutAxes) cm).getSeriesDefinitions().get(0); // OK TO ASSUME
                    // THAT 1 BASE
                    // SERIES
                    // DEFINITION
                    // EXISTS
                }
                seBase = (Series) sdBase.getRunTimeSeries().get(0); // OK TO
                // ASSUME
                // THAT 1
                // BASE
                // RUNTIME
                // SERIES
                // EXISTS

                DataSetIterator dsiBase = null;
                try
                {
                    dsiBase = new DataSetIterator(seBase.getDataSet());
                }
                catch (Exception ex )
                {
                    throw new GenerationException(ex);
                }

                double dMaxHeight = 0;
                while (dsiBase.hasNext())
                {
                    la.getCaption().setValue(String.valueOf(dsiBase.next()));
                    itm.reuse(la);
                    dMaxHeight = Math.max(itm.getFullHeight(), dMaxHeight);
                    dWidth += itm.getFullWidth();
                }
                dHeight = insCA.getTop() + dMaxHeight + insCA.getBottom();
                dWidth += dsiBase.size()
                    * (insCA.getLeft() + (3 * dItemHeight) / 2 + dHorizontalSpacing + insCA.getRight());
            }
            else if (d.getValue() == Direction.TOP_BOTTOM) // (HORIZONTAL =>
            // TB)
            {
                double dMaxW = 0;
                dSeparatorThickness += dVerticalSpacing;
                for (int j = 0; j < seda.length; j++)
                {
                    dWidth = 0;
                    al = seda[j].getRunTimeSeries();
                    for (int i = 0; i < al.size(); i++)
                    {
                        se = (Series) al.get(i);
                        la.getCaption().setValue(String.valueOf(se.getSeriesIdentifier()));// TBD: APPLY FORMAT
                        // SPECIFIER
                        itm.reuse(la);

                        // LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING
                        // + MAX ITEM WIDTH + RIGHT INSETS
                        dWidth += insCA.getLeft() + (3 * dItemHeight) / 2 + dHorizontalSpacing + itm.getFullWidth()
                            + insCA.getRight();
                    }

                    // SETUP HORIZONTAL SEPARATOR SPACING
                    if (j < seda.length - 1)
                    {
                        dHeight += dSeparatorThickness;
                    }

                    // SET WIDTH TO MAXIMUM ROW WIDTH
                    dMaxW = Math.max(dWidth, dMaxW);
                    dHeight += insCA.getTop() + dItemHeight + insCA.getRight();
                }
                dWidth = dMaxW;
            }
            else if (d.getValue() == Direction.LEFT_RIGHT) // (HORIZONTAL =>
            // LR)
            {
                dSeparatorThickness += dHorizontalSpacing;
                for (int j = 0; j < seda.length; j++)
                {
                    al = seda[j].getRunTimeSeries();
                    for (int i = 0; i < al.size(); i++)
                    {
                        se = (Series) al.get(i);
                        la.getCaption().setValue(String.valueOf(se.getSeriesIdentifier()));// TBD: APPLY FORMAT
                        // SPECIFIER
                        itm.reuse(la);

                        // LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING
                        // + MAX ITEM WIDTH + RIGHT INSETS
                        dWidth += insCA.getLeft() + (3 * dItemHeight) / 2 + dHorizontalSpacing + itm.getFullWidth()
                            + insCA.getRight();
                    }

                    // SETUP VERTICAL SEPARATOR SPACING
                    if (j < seda.length - 1)
                    {
                        dWidth += dSeparatorThickness;
                    }
                }
                dHeight = insCA.getTop() + dItemHeight + insCA.getRight();
            }
            else
            {
                throw new GenerationException("Invalid argument specified for legend rendering direction = " + d);
            }
        }
        else
        {
            throw new GenerationException("Invalid argument specified for legend rendering orientation = " + o);
        }

        itm.dispose(); // DISPOSE RESOURCE AFTER USE
        sz = SizeImpl.create(dWidth, dHeight);
        return sz;
    }

    public final Size getSize()
    {
        return sz;
    }
}