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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.PlotContent;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.DataFormatException;
import org.eclipse.birt.chart.exception.DataSetException;
import org.eclipse.birt.chart.exception.GenerationException;
import org.eclipse.birt.chart.exception.NotFoundException;
import org.eclipse.birt.chart.exception.NullValueException;
import org.eclipse.birt.chart.exception.OutOfSyncException;
import org.eclipse.birt.chart.exception.PluginException;
import org.eclipse.birt.chart.exception.UndefinedValueException;
import org.eclipse.birt.chart.exception.UnexpectedInputException;
import org.eclipse.birt.chart.exception.ValidationException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This class is capable of computing the content of a chart (with axes) based on preferred sizes, text rotation, fit
 * ability, scaling, etc and prepares it for rendering.
 */
public final class PlotWith2DAxes extends PlotContent
{

    /**
     * A final internal reference to the model used in rendering computations
     */
    private final ChartWithAxes cwa;

    /**
     * A computed plot area based on the block dimensions and the axis attributes and label values
     */
    private Bounds boPlotBackground = BoundsImpl.create(0, 0, 100, 100);

    /**
     * All axes defined in the model are maintained in a fast data structure containing additional rendering attributes
     */
    private AllAxes aax = null;

    /**
     * An internal XServer implementation capable of obtaining text metrics, etc.
     */
    private IDisplayServer ids;

    /**
     * Insets maintained as pixels equivalent of the points value specified in the model used here for fast computations
     */
    private Insets insCA = null;

    /**
     * Ratio for converting a point to a pixel
     */
    private transient double dPointToPixel = 0;

    /**
     * This complex reference is used in rendering stacked series otherwise unused.
     */
    private StackedSeriesLookup ssl = null;

    /**
     * The locale (specified at generation time) associated with the chart being computed
     */
    private final Locale lcl;
    
    /**
     * 
     */
    private Series seBaseRuntime = null;

    /**
     * The default constructor
     * 
     * @param _ids
     *            The display server using which the chart is computed
     * @param _cwa
     *            An instance of the model (ChartWithAxes)
     */
    public PlotWith2DAxes(IDisplayServer _ids, ChartWithAxes _cwa, Locale _lcl)
    {
        cwa = _cwa;
        ids = _ids;
        lcl = _lcl;
        ssl = new StackedSeriesLookup();
        dPointToPixel = ids.getDpiResolution() / 72d;
        try
        {
            buildAxes(); // CREATED ONCE
        }
        catch (Exception ex )
        {
            DefaultLoggerImpl.instance().log(ex);
        }
    }

    /**
     * Converts to internal (non public-model) data structures
     * 
     * @param cd
     * @return
     */
    public static final int getDimension(ChartDimension cd)
    {
        switch (cd.getValue())
        {
            case ChartDimension.TWO_DIMENSIONAL:
                return IConstants.TWO_D;
            case ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH:
                return IConstants.TWO_5_D;
            case ChartDimension.THREE_DIMENSIONAL:
                return IConstants.THREE_D;
        }
        return IConstants.UNDEFINED;
    }

    /**
     * Converts to internal (non public-model) data structures
     * 
     * @param ax
     * @return
     */
    private static final int getAxisType(Axis ax)
    {
        int iAxisType = UNDEFINED;
        final AxisType at = ax.getType();
        switch (at.getValue())
        {
            case AxisType.LINEAR:
                iAxisType = NUMERICAL | LINEAR;
                break;
            case AxisType.LOGARITHMIC:
                iAxisType = NUMERICAL | LOGARITHMIC;
                break;
            case AxisType.TEXT:
                iAxisType = TEXT;
                break;
            case AxisType.DATE_TIME:
                iAxisType = DATE_TIME;
                break;
        }
        if (ax.isPercent())
        {
            iAxisType |= PERCENT;
        }
        return iAxisType;
    }

    /**
     * Converts to internal (non public-model) data structures
     * 
     * @param ax
     * @return
     */
    private static final int getTickStyle(Axis ax, int iMajorOrMinor)
    {
        int iTickStyle = TICK_NONE;

        org.eclipse.birt.chart.model.component.Grid gr = (iMajorOrMinor == MAJOR) ? ax.getMajorGrid() : ax
            .getMinorGrid();
        if (!gr.isSetTickStyle())
        {
            return iTickStyle;
        }
        final LineAttributes lia = gr.getTickAttributes();
        if (!lia.isSetStyle() || !lia.isSetThickness() || !lia.isSetVisible() || !lia.isVisible())
        {
            return iTickStyle;
        }

        final TickStyle ts = gr.getTickStyle();
        switch (ts.getValue())
        {
            case TickStyle.LEFT:
                iTickStyle = TICK_LEFT;
                break;
            case TickStyle.RIGHT:
                iTickStyle = TICK_RIGHT;
                break;
            case TickStyle.ABOVE:
                iTickStyle = TICK_ABOVE;
                break;
            case TickStyle.BELOW:
                iTickStyle = TICK_BELOW;
                break;
            case TickStyle.ACROSS:
                iTickStyle = TICK_ACROSS;
                break;
        }
        return iTickStyle;
    }

    /**
     * Converts to internal (non public-model) data structures
     * 
     * @param ax
     * @return
     */
    private static final IntersectionValue getIntersection(Axis ax)
    {
        IntersectionValue iv = null;
        AxisOrigin ao = ax.getOrigin();
        if (ao.getType() == IntersectionType.MAX_LITERAL)
        {
            iv = new IntersectionValue(IntersectionValue.MAX, 0);
        }
        else if (ao.getType() == IntersectionType.MIN_LITERAL)
        {
            iv = new IntersectionValue(IntersectionValue.MIN, 0);
        }
        else
        {
            iv = new IntersectionValue(IntersectionValue.VALUE, ao.getValue());
        }
        return iv;
    }

    /**
     * Converts to internal (non public-model) data structures and transposes value if needed
     * 
     * @param iBaseOrOrthogonal
     * @return
     */
    private final int getOrientation(int iBaseOrOrthogonal)
    {
        if (!cwa.isTransposed())
        {
            return (iBaseOrOrthogonal == IConstants.BASE) ? IConstants.HORIZONTAL : IConstants.VERTICAL;
        }
        else
        {
            return (iBaseOrOrthogonal == IConstants.BASE) ? IConstants.VERTICAL : IConstants.HORIZONTAL;
        }
    }

    /**
     * Transposes the anchor for a given source orientation
     * 
     * @param or
     * @param an
     * 
     * @return
     */
    public static final Anchor transposedAnchor(Orientation or, Anchor an) throws UnexpectedInputException
    {
        if (an == null)
        {
            return null; // CENTERED ANCHOR
        }

        final int iOrientation = or.getValue();
        if (iOrientation == Orientation.HORIZONTAL)
        {
            switch (an.getValue())
            {
                case Anchor.NORTH:
                    return Anchor.WEST_LITERAL;
                case Anchor.SOUTH:
                    return Anchor.EAST_LITERAL;
                case Anchor.EAST:
                    return Anchor.NORTH_LITERAL;
                case Anchor.WEST:
                    return Anchor.SOUTH_LITERAL;
                case Anchor.NORTH_WEST:
                    return Anchor.SOUTH_WEST_LITERAL;
                case Anchor.NORTH_EAST:
                    return Anchor.NORTH_WEST_LITERAL;
                case Anchor.SOUTH_WEST:
                    return Anchor.SOUTH_EAST_LITERAL;
                case Anchor.SOUTH_EAST:
                    return Anchor.NORTH_EAST_LITERAL;
            }
        }
        else if (iOrientation == Orientation.VERTICAL)
        {
            switch (an.getValue())
            {
                case Anchor.NORTH:
                    return Anchor.EAST_LITERAL;
                case Anchor.SOUTH:
                    return Anchor.WEST_LITERAL;
                case Anchor.EAST:
                    return Anchor.SOUTH_LITERAL;
                case Anchor.WEST:
                    return Anchor.NORTH_LITERAL;
                case Anchor.NORTH_WEST:
                    return Anchor.NORTH_EAST_LITERAL;
                case Anchor.NORTH_EAST:
                    return Anchor.SOUTH_EAST_LITERAL;
                case Anchor.SOUTH_WEST:
                    return Anchor.NORTH_WEST_LITERAL;
                case Anchor.SOUTH_EAST:
                    return Anchor.SOUTH_WEST_LITERAL;
            }
        }
        throw new UnexpectedInputException("Cannot transpose anchor " + an + " for axis orientation " + or);
    }

    /**
     * Returns a transpose of the original angle
     * 
     * @param dOriginalAngle
     * @return 
     * @throws UnexpectedInputException
     */
    public static final double getTransposedAngle(double dOriginalAngle) throws UnexpectedInputException
    {
        if (dOriginalAngle >= 0 && dOriginalAngle <= 90)
        {
            return -(90 - dOriginalAngle);
        }
        else if (dOriginalAngle < 0 && dOriginalAngle >= -90)
        {
            return (dOriginalAngle + 90);
        }
        throw new UnexpectedInputException("Cannot transpose [angle=" + dOriginalAngle
            + "] beyond range (90 >= 0 >= -90)");
    }

    /**
     * Returns a transposed or the original angle as requested depending on the plot's orientation
     * 
     * @param iBaseOrOrthogonal
     * @param dOriginalAngle
     * @return @throws
     *         UnexpectedInputException
     */
    public final double transposeAngle(double dOriginalAngle) throws UnexpectedInputException
    {
        if (!cwa.isTransposed())
        {
            return dOriginalAngle;
        }
        if (dOriginalAngle >= 0 && dOriginalAngle <= 90)
        {
            return -(90 - dOriginalAngle);
        }
        else if (dOriginalAngle < 0 && dOriginalAngle >= -90)
        {
            return (dOriginalAngle + 90);
        }
        throw new UnexpectedInputException("Cannot transpose [angle=" + dOriginalAngle
            + "] beyond range (90 >= 0 >= -90)");
    }

    /**
     * Returns a transposed or the original label position as requested depending on the plot's orientation
     * 
     * @param iBaseOrOrthogonal
     * @param iOriginalPosition
     * @return @throws
     *         UnexpectedInputException
     */
    public final int transposeLabelPosition(int iBaseOrOrthogonal, int iOriginalPosition)
        throws UnexpectedInputException
    {
        if (!cwa.isTransposed())
        {
            return iOriginalPosition;
        }
        if (iBaseOrOrthogonal == IConstants.BASE)
        {
            switch (iOriginalPosition)
            {
                case IConstants.ABOVE:
                    return IConstants.RIGHT;
                case IConstants.BELOW:
                    return IConstants.LEFT;
                case IConstants.OUTSIDE:
                case IConstants.INSIDE:
                    return iOriginalPosition;
            }
        }
        else if (iBaseOrOrthogonal == IConstants.ORTHOGONAL)
        {
            switch (iOriginalPosition)
            {
                case IConstants.ABOVE:
                    return IConstants.RIGHT;
                case IConstants.BELOW:
                    return IConstants.LEFT;
                case IConstants.LEFT:
                    return IConstants.BELOW;
                case IConstants.RIGHT:
                    return IConstants.ABOVE;
                case IConstants.OUTSIDE:
                case IConstants.INSIDE:
                    return iOriginalPosition;
            }
        }
        throw new UnexpectedInputException("Didn't expect a combination of axis=" + iBaseOrOrthogonal
            + " and labelposition=" + iOriginalPosition);
    }

    /**
     * Returns a transposed or the original tick style as requested depending on the plot's orientation
     * 
     * @param iBaseOrOrthogonal
     * @param iOriginalStyle
     * @return @throws
     *         UnexpectedInputException
     */
    private final int transposeTickStyle(int iBaseOrOrthogonal, int iOriginalStyle) throws UnexpectedInputException
    {
        if (!cwa.isTransposed() || iOriginalStyle == IConstants.TICK_ACROSS || iOriginalStyle == IConstants.TICK_NONE)
        {
            return iOriginalStyle;
        }

        if (iBaseOrOrthogonal == IConstants.BASE)
        {
            switch (iOriginalStyle)
            {
                case IConstants.TICK_BELOW:
                    return IConstants.TICK_LEFT;
                case IConstants.TICK_ABOVE:
                    return IConstants.TICK_RIGHT;
            }
        }
        else if (iBaseOrOrthogonal == IConstants.ORTHOGONAL)
        {
            switch (iOriginalStyle)
            {
                case IConstants.TICK_LEFT:
                    return IConstants.TICK_BELOW;
                case IConstants.TICK_RIGHT:
                    return IConstants.TICK_ABOVE;
            }
        }
        throw new UnexpectedInputException("Didn't expect a combination of axis=" + iBaseOrOrthogonal
            + " and tickstyle=" + iOriginalStyle);
    }

    /**
     * This method validates several crucial properties for an axis associated with a Chart
     *  
     * @param	ax	The axis to validate
     * @throws ValidationException
     */
    private final void validateAxis(Axis ax) throws ValidationException
    {
        if (!ax.isSetType()) // AXIS TYPE UNDEFINED
        {
            throw new ValidationException(new UndefinedValueException("The data type is undefined for axis " + ax));
        }
        
        if (!ax.getLabel().isSetVisible())
        {
            throw new ValidationException(new UndefinedValueException("The label visibility is undefined for axis " + ax));
        }
        
        if (!ax.getTitle().isSetVisible())
        {
            throw new ValidationException(new UndefinedValueException("The title visibility is undefined for axis " + ax));
        }
        
        if (!ax.isSetLabelPosition() && ax.getLabel().isVisible())
        {
            throw new ValidationException(new UndefinedValueException("The label position is undefined for axis " + ax));
        }
        
        if (!ax.isSetTitlePosition() && ax.getTitle().isVisible())
        {
            throw new ValidationException(new UndefinedValueException("The title position is undefined for axis " + ax));
        }
        
        LineAttributes liaTicks = ax.getMajorGrid().getTickAttributes();
        if (!ax.getMajorGrid().isSetTickStyle() && liaTicks.isVisible())
        {
            throw new ValidationException(new UndefinedValueException("The major grid tick style is undefined for axis " + ax));
        }
        
        liaTicks = ax.getMinorGrid().getTickAttributes();
        if (!ax.getMinorGrid().isSetTickStyle() && liaTicks.isVisible())
        {
            throw new ValidationException(new UndefinedValueException("The minor grid tick style is undefined for axis " + ax));
        }
        
        final int iOrientation = ax.getOrientation().getValue();
        if (iOrientation == Orientation.VERTICAL)
        {
            int iPosition = -1;
            if (ax.getLabel().isVisible()) // LABEL POSITION (IF VISIBLE)
            {
                iPosition = ax.getLabelPosition().getValue();
	            if (iPosition != Position.LEFT && iPosition != Position.RIGHT)
	            {
	                throw new ValidationException("Illegal label position value " + ax.getLabelPosition() + " specified for vertical axis " + ax);
	            }
            }
            if (ax.getTitle().isVisible()) // LABEL POSITION (IF VISIBLE)
            {
	            iPosition = ax.getTitlePosition().getValue();
	            if (iPosition != Position.LEFT && iPosition != Position.RIGHT)
	            {
	                throw new ValidationException("Illegal title position value " + ax.getLabelPosition() + " specified for vertical axis " + ax);
	            }   
            }
            
            int iTickStyle = ax.getMajorGrid().getTickStyle().getValue();
            if (iTickStyle != TickStyle.ACROSS && iTickStyle != TickStyle.LEFT && iTickStyle != TickStyle.RIGHT)
            {
                throw new ValidationException("Illegal major tick style specified as " + ax.getMajorGrid().getTickStyle() + " for vertical axis " + ax);
            }
            iTickStyle = ax.getMinorGrid().getTickStyle().getValue();
            if (iTickStyle != TickStyle.ACROSS && iTickStyle != TickStyle.LEFT && iTickStyle != TickStyle.RIGHT)
            {
                throw new ValidationException("Illegal minor tick style specified as " + ax.getMinorGrid().getTickStyle() + " for vertical axis " + ax);
            }
        }
        else if (iOrientation == Orientation.HORIZONTAL)
        {
            int iPosition = -1;
            if (ax.getLabel().isVisible()) // LABEL POSITION (IF VISIBLE)
            {
                iPosition = ax.getLabelPosition().getValue();
	            if (iPosition != Position.ABOVE && iPosition != Position.BELOW)
	            {
	                throw new ValidationException("Illegal label position value " + ax.getLabelPosition() + " specified for horizontal axis " + ax);
	            }
            }
            if (ax.getTitle().isVisible()) // LABEL POSITION (IF VISIBLE)
            {
	            iPosition = ax.getTitlePosition().getValue();
	            if (iPosition != Position.ABOVE && iPosition != Position.BELOW)
	            {
	                throw new ValidationException("Illegal title position value " + ax.getLabelPosition() + " specified for horizontal axis " + ax);
	            }   
            }
            
            int iTickStyle = ax.getMajorGrid().getTickStyle().getValue();
            if (iTickStyle != TickStyle.ACROSS && iTickStyle != TickStyle.ABOVE && iTickStyle != TickStyle.BELOW)
            {
                throw new ValidationException("Illegal major tick style specified as " + ax.getMajorGrid().getTickStyle() + " for horizontal axis " + ax);
            }
            iTickStyle = ax.getMinorGrid().getTickStyle().getValue();
            if (iTickStyle != TickStyle.ACROSS && iTickStyle != TickStyle.ABOVE && iTickStyle != TickStyle.BELOW)
            {
                throw new ValidationException("Illegal minor tick style specified as " + ax.getMinorGrid().getTickStyle() + " for horizontal axis " + ax);
            }
        }
    }
    
    /**
     * Internally maps the EMF model to internal (non-public) rendering fast data structures
     */
    final void buildAxes() throws UnexpectedInputException, UndefinedValueException, ValidationException
    {
        final Axis[] axa = cwa.getPrimaryBaseAxes();
        final Axis axPrimaryBase = axa[0]; // NOTE: FOR REL 1 AXIS RENDERS, WE SUPPORT A SINGLE PRIMARY BASE AXIS ONLY
        validateAxis(axPrimaryBase);
        final Axis axPrimaryOrthogonal = cwa.getPrimaryOrthogonalAxis(axPrimaryBase);
        validateAxis(axPrimaryOrthogonal);
        final Axis[] axaOverlayOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, false);
        aax = new AllAxes(cwa.getPlot().getClientArea().getInsets().scaledInstance(dPointToPixel)); // CONVERSION
        insCA = aax.getInsets();

        SeriesDefinition sdBase = null;
        // ONLY SUPPORT 1 BASE AXIS
        if (!axPrimaryBase.getSeriesDefinitions().isEmpty())
        {
            // OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
            sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions().get(0);
            final ArrayList alRuntimeBaseSeries = sdBase.getRunTimeSeries();
            if (alRuntimeBaseSeries != null)
            {
	            // OK TO ASSUME THAT 1 BASE RUNTIME SERIES EXISTS
	            seBaseRuntime = (Series) sdBase.getRunTimeSeries().get(0);
            }
        }
        
        aax.swapAxes(cwa.isTransposed());
        Label l;
        IntersectionValue iv = null;
        AxisOrigin ao;

        // SETUP THE PRIMARY BASE-AXIS PROPERTIES AND ITS SCALE
        final OneAxis oaxPrimaryBase = new OneAxis(axPrimaryBase);
        l = axPrimaryBase.getLabel();
        Text t = l.getCaption();
        FontDefinition fd = t.getFont();
        oaxPrimaryBase.set(getOrientation(IConstants.BASE), transposeLabelPosition(IConstants.BASE,
            getLabelPosition(axPrimaryBase.getLabelPosition())), transposeLabelPosition(IConstants.BASE,
            getLabelPosition(axPrimaryBase.getTitlePosition())), axPrimaryBase.isSetCategoryAxis()
            && axPrimaryBase.isCategoryAxis());
        oaxPrimaryBase.setGridProperties(axPrimaryBase.getMajorGrid().getLineAttributes(), axPrimaryBase.getMinorGrid()
            .getLineAttributes(), transposeTickStyle(IConstants.BASE, getTickStyle(axPrimaryBase, MAJOR)),
            transposeTickStyle(IConstants.BASE, getTickStyle(axPrimaryBase, MINOR)), axPrimaryBase.getScale()
                .getMinorGridsPerUnit());

        if (cwa.isTransposed())
        {
            // TRANSPOSE ROTATION OF LABELS AS APPROPRIATE
            final Label laAxisLabels = (Label) EcoreUtil.copy(axPrimaryBase.getLabel());
            final Label laAxisTitle = (Label) EcoreUtil.copy(axPrimaryBase.getTitle());
            laAxisLabels.getCaption().getFont().setRotation(
                transposeAngle(laAxisLabels.getCaption().getFont().getRotation()));
            laAxisTitle.getCaption().getFont().setRotation(
                transposeAngle(laAxisTitle.getCaption().getFont().getRotation()));
            oaxPrimaryBase.set(laAxisLabels, laAxisTitle); // ASSOCIATE FONT,
            // ETC
        }
        else
        {
            oaxPrimaryBase.set(axPrimaryBase.getLabel(), axPrimaryBase.getTitle()); // ASSOCIATE FONT
        }

        oaxPrimaryBase.set(getIntersection(axPrimaryBase));
        oaxPrimaryBase.set(axPrimaryBase.getLineAttributes());
        aax.definePrimary(oaxPrimaryBase); // ADD TO AXIS SET

        // SETUP THE PRIMARY ORTHOGONAL-AXIS PROPERTIES AND ITS SCALE
        final OneAxis oaxPrimaryOrthogonal = new OneAxis(axPrimaryOrthogonal);
        l = axPrimaryOrthogonal.getLabel();
        t = l.getCaption();
        fd = t.getFont();
        oaxPrimaryOrthogonal.set(getOrientation(IConstants.ORTHOGONAL), transposeLabelPosition(IConstants.ORTHOGONAL,
            getLabelPosition(axPrimaryOrthogonal.getLabelPosition())), transposeLabelPosition(IConstants.ORTHOGONAL,
            getLabelPosition(axPrimaryOrthogonal.getTitlePosition())), axPrimaryOrthogonal.isSetCategoryAxis()
            && axPrimaryOrthogonal.isCategoryAxis());
        oaxPrimaryOrthogonal.setGridProperties(axPrimaryOrthogonal.getMajorGrid().getLineAttributes(),
            axPrimaryOrthogonal.getMinorGrid().getLineAttributes(), transposeTickStyle(IConstants.ORTHOGONAL,
                getTickStyle(axPrimaryOrthogonal, MAJOR)), transposeTickStyle(IConstants.ORTHOGONAL, getTickStyle(
                axPrimaryOrthogonal, MINOR)), axPrimaryOrthogonal.getScale().getMinorGridsPerUnit());

        if (cwa.isTransposed())
        {
            // TRANSPOSE ROTATION OF LABELS AS APPROPRIATE
            final Label laAxisLabels = (Label) EcoreUtil.copy(axPrimaryOrthogonal.getLabel());
            final Label laAxisTitle = (Label) EcoreUtil.copy(axPrimaryOrthogonal.getTitle());
            laAxisLabels.getCaption().getFont().setRotation(
                transposeAngle(laAxisLabels.getCaption().getFont().getRotation()));
            laAxisTitle.getCaption().getFont().setRotation(
                transposeAngle(laAxisTitle.getCaption().getFont().getRotation()));
            oaxPrimaryOrthogonal.set(laAxisLabels, laAxisTitle); // ASSOCIATE
            // FONT, ETC
        }
        else
        {
            oaxPrimaryOrthogonal.set(axPrimaryOrthogonal.getLabel(), axPrimaryOrthogonal.getTitle()); // ASSOCIATE FONT,
            // ETC
        }
        oaxPrimaryOrthogonal.set(getIntersection(axPrimaryOrthogonal));
        oaxPrimaryOrthogonal.set(axPrimaryOrthogonal.getLineAttributes());
        aax.definePrimary(oaxPrimaryOrthogonal); // ADD TO AXIS SET

        // SETUP THE OVERLAY AXES
        aax.initOverlays(axaOverlayOrthogonal.length, getOrientation(IConstants.ORTHOGONAL));
        OneAxis oaxOverlayOrthogonal;
        for (int i = 0; i < axaOverlayOrthogonal.length; i++)
        {
            validateAxis(axaOverlayOrthogonal[i]);
            l = axaOverlayOrthogonal[i].getLabel();
            t = l.getCaption();
            fd = t.getFont();

            oaxOverlayOrthogonal = new OneAxis(axaOverlayOrthogonal[i]);
            oaxOverlayOrthogonal.set(getOrientation(IConstants.ORTHOGONAL), transposeLabelPosition(
                IConstants.ORTHOGONAL, getLabelPosition(axaOverlayOrthogonal[i].getLabelPosition())),
                transposeLabelPosition(IConstants.ORTHOGONAL, getLabelPosition(axaOverlayOrthogonal[i]
                    .getTitlePosition())), axaOverlayOrthogonal[i].isSetCategoryAxis()
                    && axaOverlayOrthogonal[i].isCategoryAxis());
            oaxOverlayOrthogonal.setGridProperties(axaOverlayOrthogonal[i].getMajorGrid().getLineAttributes(),
                axaOverlayOrthogonal[i].getMinorGrid().getLineAttributes(), transposeTickStyle(IConstants.ORTHOGONAL,
                    getTickStyle(axaOverlayOrthogonal[i], MAJOR)), transposeTickStyle(IConstants.ORTHOGONAL,
                    getTickStyle(axaOverlayOrthogonal[i], MINOR)), axaOverlayOrthogonal[i].getScale()
                    .getMinorGridsPerUnit());

            if (cwa.isTransposed())
            {
                // TRANSPOSE ROTATION OF LABELS AS APPROPRIATE
                final Label laAxisLabels = (Label) EcoreUtil.copy(axaOverlayOrthogonal[i].getLabel());
                final Label laAxisTitle = (Label) EcoreUtil.copy(axaOverlayOrthogonal[i].getTitle());
                laAxisLabels.getCaption().getFont().setRotation(
                    transposeAngle(laAxisLabels.getCaption().getFont().getRotation()));
                laAxisTitle.getCaption().getFont().setRotation(
                    transposeAngle(laAxisTitle.getCaption().getFont().getRotation()));
                oaxOverlayOrthogonal.set(laAxisLabels, laAxisTitle); // ASSOCIATE
                // FONT,
                // ETC
            }
            else
            {
                oaxOverlayOrthogonal.set(axaOverlayOrthogonal[i].getLabel(), axaOverlayOrthogonal[i].getTitle());
            }
            oaxOverlayOrthogonal.set(axaOverlayOrthogonal[i].getLineAttributes());
            oaxOverlayOrthogonal.set(getIntersection(axaOverlayOrthogonal[i]));
            aax.defineOverlay(i, oaxOverlayOrthogonal);
        }

        // BUILD STACKED STRUCTURE (FOR STACKED SERIES) ASSOCIATED WITH EACH
        // ORTHOGONAL AXIS
        ssl = StackedSeriesLookup.create(cwa);
    }

    /**
     * This method converts a generic text dataset to a typed dataset as expected by the renderer
     * 
     * @param ax
     *            The model's axis for which a series will be queried
     * @param iType
     *            The renderer datatype associated with the axis
     * @param iSeriesIndex
     *            The series index for which the typed dataset is being built
     * 
     * @return @throws
     *         DataFormatException
     */
    private final DataSetIterator getTypedDataSet(Axis ax, int iType, int iSeriesIndex) throws DataFormatException,
        UnexpectedInputException
    {
        final Series[] sea = ax.getRuntimeSeries();
        if (sea.length == 0) // TBD: PULL FROM SAMPLE DATA
        {
            if ((iType & NUMERICAL) == NUMERICAL)
            {
                return new DataSetIterator(new Double[]
                {
                    new Double(1), new Double(2)
                });
            }
            else if ((iType & DATE_TIME) == DATE_TIME)
            {
                return new DataSetIterator(new Calendar[]
                {
                    new CDateTime(), new CDateTime()
                });
            }
            else if ((iType & TEXT) == TEXT)
            {
                return new DataSetIterator(new String[]
                {
                    "Category1", "Category2", "Category3"
                });
            }
        }
        return getTypedDataSet(sea[iSeriesIndex], iType);

    }

    /**
     * 
     * @param se
     * @param iType
     * 
     * @return 
     * @throws DataFormatException
     */
    private final DataSetIterator getTypedDataSet(Series se, int iType) throws DataFormatException,
        UnexpectedInputException
    {
        return new DataSetIterator(se.getDataSet());
    }

    /**
     * This method pulls out the 'min' and 'max' value for all datasets associated with a single axis using the custom
     * data source processor implementation
     * 
     * @param ax
     *            The orthogonal axis for which the min/max values are being computed
     * @param iType
     *            The renderer's axis data type
     * 
     * @return
     */
    private final Object getMinMax(Axis ax, int iType) throws PluginException, DataSetException,
        UnexpectedInputException
    {
        final Series[] sea = ax.getRuntimeSeries();
        final int iSeriesCount = sea.length;
        Series se;
        DataSet ds;

        Object oV1, oV2, oMin = null, oMax = null;
        EList elDataElements;
        DataElement de;

        PluginSettings ps = PluginSettings.instance();
        IDataSetProcessor iDSP = null;
        Class cDSP = null;
        boolean bAnyStacked = false; // ANY STACKED SERIES ASSOCIATED WITH AXIS
        // 'ax'

        for (int i = 0; i < iSeriesCount; i++)
        {
            if (sea[i].isStacked())
            {
                if (sea[i].canBeStacked())
                {
                    bAnyStacked = true;
                    continue;
                }
                else
                {
                    throw new UnexpectedInputException("A series " + sea[i]
                        + " that may not be stacked is set to stacked");
                }
            }

            iDSP = ps.getDataSetProcessor(sea[i].getClass());
            ds = sea[i].getDataSet();

            oV1 = iDSP.getMinimum(ds, iType);
            oV2 = iDSP.getMaximum(ds, iType);

            if ((iType & NUMERICAL) == NUMERICAL)
            {
                if (oV1 != null) // SETUP THE MINIMUM VALUE FOR ALL DATASETS
                {
                    if (oMin == null)
                    {
                        oMin = oV1;
                    }
                    else
                    {
                        final double dV1 = asDouble(oV1).doubleValue();
                        if (Math.min(asDouble(oMin).doubleValue(), dV1) == dV1)
                        {
                            oMin = oV1;
                        }
                    }
                }

                if (oV2 != null) // SETUP THE MAXIMUM VALUE FOR ALL DATASETS
                {
                    if (oMax == null)
                    {
                        oMax = oV2;
                    }
                    else
                    {
                        final double dV2 = asDouble(oV2).doubleValue();
                        if (Math.max(asDouble(oMax).doubleValue(), dV2) == dV2)
                        {
                            oMax = oV2;
                        }
                    }
                }
            }
            else if ((iType & DATE_TIME) == DATE_TIME)
            {
                if (oV1 != null) // SETUP THE MINIMUM VALUE FOR ALL DATASETS
                {
                    if (oMin == null)
                    {
                        oMin = oV1;
                    }
                    else
                    {
                        final CDateTime cdtV1 = asDateTime(oV1);
                        final CDateTime cdtMin = asDateTime(oMin);
                        if (cdtV1.before(cdtMin))
                        {
                            oMin = cdtV1;
                        }
                    }
                }

                if (oV2 != null) // SETUP THE MAXIMUM VALUE FOR ALL DATASETS
                {
                    if (oMax == null)
                    {
                        oMax = oV2;
                    }
                    else
                    {
                        final CDateTime cdtV2 = asDateTime(oV2);
                        final CDateTime cdtMax = asDateTime(oMax);
                        if (cdtV2.after(cdtMax))
                        {
                            oMax = cdtV2;
                        }
                    }
                }
            }
        }

        // ONLY NUMERIC VALUES ARE SUPPORTED IN STACKED ELEMENT COMPUTATIONS
        if (bAnyStacked || ax.isPercent())
        {
            if (ax.getType().getValue() == AxisType.DATE_TIME)
            {
                throw new UnexpectedInputException("Cannot define stacked series on a date-time axis " + ax);
            }
            Object oValue;
            ArrayList alI;
            int iSeriesPerGroup;
            NumberDataElement nde;
            double dGroupMin, dGroupMax, dValue, dAbsTotal, dPercentMax = 0, dPercentMin = 0;
            double dAxisMin = Double.MAX_VALUE, dAxisMax = Double.MIN_VALUE;
            ArrayList alSeriesGroupsPerAxis = ssl.getStackGroups(ax);
            ArrayList alSeriesPerGroup;
            StackGroup sg;
            DataSetIterator[] dsi = new DataSetIterator[ssl.getSeriesCount(ax)];

            if (alSeriesGroupsPerAxis == null)
            {
                throw new DataSetException("Stacked series incorrectly setup for axis " + ax);
            }
            DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Processing stacked info for axis " + ax);
            int iSeriesIndex, iDataSetCount = ssl.getUnitCount();

            for (int k = 0; k < iDataSetCount; k++) // PER UNIT
            {
                dAbsTotal = 0;
                iSeriesIndex = 0;
                for (int i = 0; i < alSeriesGroupsPerAxis.size(); i++)
                {
                    sg = (StackGroup) alSeriesGroupsPerAxis.get(i);
                    alSeriesPerGroup = sg.getSeries();
                    iSeriesPerGroup = alSeriesPerGroup.size();

                    if (iSeriesPerGroup > 0)
                    {
                        se = (Series) alSeriesPerGroup.get(0);
                        ds = se.getDataSet();
                        if (dsi[iSeriesIndex] == null)
                        {
                            dsi[iSeriesIndex] = new DataSetIterator(ds);
                            if ((dsi[iSeriesIndex].getDataType() & IConstants.NUMERICAL) != IConstants.NUMERICAL)
                            {
                                throw new DataSetException(
                                    "Unable to compute a percent/stacked axis containing non-numerical data");
                            }
                        }
                        iDataSetCount = dsi[iSeriesIndex].size(); // ALL SERIES
                        // MUST HAVE
                        // THE SAME
                        // DATASET
                        // ELEMENT
                        // COUNT

                        dGroupMin = 0;
                        dGroupMax = 0;
                        if (ax.isPercent())
                        {
                            dAbsTotal = 0;
                        }
                        for (int j = 0; j < iSeriesPerGroup; j++) // FOR ALL
                        // SERIES
                        {
                            se = (Series) alSeriesPerGroup.get(j); // EACH
                            // SERIES
                            if (j > 0) // ALREADY DONE FOR '0'
                            {
                                if (dsi[iSeriesIndex] == null)
                                {
                                    ds = se.getDataSet(); // DATA SET
                                    dsi[iSeriesIndex] = new DataSetIterator(ds);
                                    if ((dsi[iSeriesIndex].getDataType() & IConstants.NUMERICAL) != IConstants.NUMERICAL)
                                    {
                                        throw new DataSetException(
                                            "Unable to compute a percent/stacked axis containing non-numerical data");
                                    }
                                }
                            }
                            oValue = dsi[iSeriesIndex].next(); // EACH ROW OF
                            // DATA
                            if (oValue != null) // NULL CHECK
                            {
                                dValue = ((Double) oValue).doubleValue(); // EXTRACT
                                // WRAPPED
                                // VALUE
                                dAbsTotal += Math.abs(dValue);
                                if (dValue > 0)
                                {
                                    dGroupMax += dValue; // UPDATE MAX
                                }
                                else if (dValue < 0)
                                {
                                    dGroupMin += dValue; // UPDATE MIN
                                }
                            }
                            iSeriesIndex++;
                        }
                        final AxisSubUnit au = ssl.getSubUnit(sg, k);
                        au.setPositiveTotal(dGroupMax);
                        au.setNegativeTotal(dGroupMin);

                        // FOR EACH UNIT, UPDATE THE MIN/MAX BASED ON ALL
                        // STACKED SERIES
                        dAxisMax = Math.max(dGroupMax, dAxisMax);
                        dAxisMin = Math.min(dGroupMin, dAxisMin);
                        if (ax.isPercent())
                        {
                            dPercentMax = Math.max((dGroupMax / dAbsTotal) * 100d, dPercentMax);
                            dPercentMin = Math.min((dGroupMin / dAbsTotal) * 100d, dPercentMin);
                        }
                    }

                }
                //DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Min
                // for unit " + k + " is " + dAxisMin);
                //DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Max
                // for unit " + k + " is " + dAxisMax);
            }
            if (ax.isPercent()) // HANDLE PERCENT
            {
                if (dPercentMax >= 100)
                    dPercentMax = 99.9;
                if (dPercentMin <= -100)
                    dPercentMin = -99.9;
                if (dPercentMax == 0 && dPercentMin == 0)
                {
                    dPercentMax = 99;
                }
                dAxisMin = dPercentMin;
                dAxisMax = dPercentMax;
            }
            if ((iType & LOGARITHMIC) == LOGARITHMIC)
            {
                dAxisMin = 1;
            }
            oMin = new Double(dAxisMin);
            oMax = new Double(dAxisMax);
        }

        // IF NO DATASET WAS FOUND BECAUSE NO SERIES WERE ATTACHED TO AXES
        if (oMin == null && oMax == null)
        {
            if (iType == DATE_TIME)
            {
                oMin = new CDateTime(1, 1, 2005);
                oMax = new CDateTime(1, 1, 2006);
                return new Calendar[]
                {
                    asDateTime(oMin), asDateTime(oMax)
                };
            }
            else if ((iType & NUMERICAL) == NUMERICAL)
            {
                if ((iType & PERCENT) == PERCENT)
                {
                    oMin = new Double(0);
                    oMax = new Double(99.99);
                }
                else
                {
                    oMin = new Double(-1);
                    oMax = new Double(1);
                }
                return new double[]
                {
                    asDouble(oMin).doubleValue(), asDouble(oMax).doubleValue()
                };
            }
        }

        if (iType == DATE_TIME)
        {
            return new Calendar[]
            {
                asDateTime(oMin), asDateTime(oMax)
            };
        }
        else if ((iType & NUMERICAL) == NUMERICAL)
        {
            return new double[]
            {
                asDouble(oMin).doubleValue(), asDouble(oMax).doubleValue()
            };
        }
        return null;
    }

    /**
     * This method computes the entire chart within the given bounds. If the dataset has changed but none of the axis
     * attributes have changed, simply re-compute without 'rebuilding axes'.
     * 
     * @param bo
     * 
     * @throws PluginException
     * @throws DataFormatException
     * @throws DataSetException
     * @throws GenerationException
     */
    public final void compute(Bounds bo) throws PluginException, DataFormatException, DataSetException,
        GenerationException, NullValueException, UnexpectedInputException
    {
        bo = bo.scaledInstance(dPointToPixel); // CONVERSION
        dSeriesThickness = (ids.getDpiResolution()/72d) * cwa.getSeriesThickness();

        // MAINTAIN IN LOCAL VARIABLES FOR PERFORMANCE/CONVENIENCE
        double dX = bo.getLeft() + insCA.getLeft();
        double dY = bo.getTop() + insCA.getTop();
        double dW = bo.getWidth() - insCA.getLeft() - insCA.getRight();
        double dH = bo.getHeight() - insCA.getTop() - insCA.getBottom();

        iDimension = getDimension(cwa.getDimension());
        dXAxisPlotSpacing = cwa.getPlot().getHorizontalSpacing() * dPointToPixel; // CONVERSION
        dYAxisPlotSpacing = cwa.getPlot().getVerticalSpacing() * dPointToPixel; // CONVERSION

        if (iDimension == TWO_5_D)
        {
            dY += dSeriesThickness;
            dH -= dSeriesThickness;
            dW -= dSeriesThickness;
            bo.setHeight(dH);
            bo.setTop(dY);
            bo.setWidth(dW);
        }

        // PLACE OVERLAYS FIRST TO REDUCE VIRTUAL PLOT BOUNDS
        if (aax.getOverlayCount() > 0)
        {
            if (aax.areAxesSwapped()) // ORTHOGONAL OVERLAYS = HORIZONTAL
            {
                updateOverlayScales(aax, dX, dX + dW, dY, dH);
                dY = aax.getStart();
                dH = aax.getLength();
            }
            else
            // ORTHOGONAL OVERLAYS = VERTICAL
            {
                updateOverlayScales(aax, dY - dH, dY, dX, dW);
                dX = aax.getStart();
                dW = aax.getLength();
            }
        }

        double dStart, dEnd;
        final Axis[] axa = cwa.getPrimaryBaseAxes();
        final Axis axPrimaryBase = axa[0];
        final Axis axPrimaryOrthogonal = cwa.getPrimaryOrthogonalAxis(axPrimaryBase);
        Scale sc = axPrimaryBase.getScale();

        // COMPUTE PRIMARY-BASE-AXIS PROPERTIES AND ITS SCALE
        AutoScale scPrimaryBase = null;
        OneAxis oaxPrimaryBase = aax.getPrimaryBase();
        int iAxisType = getAxisType(axPrimaryBase);

        Object oaData = null;
        if (iAxisType == TEXT || oaxPrimaryBase.isCategoryScale())
        {
            oaData = getTypedDataSet(axPrimaryBase, iAxisType, 0);
        }
        else if ((iAxisType & NUMERICAL) == NUMERICAL)
        {
            oaData = getMinMax(axPrimaryBase, iAxisType);
        }
        else if ((iAxisType & DATE_TIME) == DATE_TIME)
        {
            oaData = getMinMax(axPrimaryBase, iAxisType);
        }
        DataSetIterator dsi = (oaData instanceof DataSetIterator) ? (DataSetIterator) oaData : new DataSetIterator(
            oaData, iAxisType);
        oaData = null;

        dStart = (aax.areAxesSwapped()) ? dY + dH : dX;
        dEnd = (aax.areAxesSwapped()) ? dY : dStart + dW;
        scPrimaryBase = AutoScale.computeScale(ids, oaxPrimaryBase, dsi, iAxisType, dStart, dEnd, sc.getMin(), sc
            .getMax(), sc.isSetStep() ? new Double(sc.getStep()) : null, axPrimaryBase.getFormatSpecifier(), lcl);
        oaxPrimaryBase.set(scPrimaryBase); // UPDATE SCALE ON PRIMARY-BASE
        // AXIS

        // COMPUTE PRIMARY-ORTHOGONAL-AXIS PROPERTIES AND ITS SCALE
        AutoScale scPrimaryOrthogonal = null;
        OneAxis oaxPrimaryOrthogonal = aax.getPrimaryOrthogonal();
        iAxisType = getAxisType(axPrimaryOrthogonal);
        oaData = null;
        if ((iAxisType & NUMERICAL) == NUMERICAL || (iAxisType & DATE_TIME) == DATE_TIME)
        {
            dsi = new DataSetIterator(getMinMax(axPrimaryOrthogonal, iAxisType), iAxisType);
        }
        else
        {
            throw new DataFormatException("The orthogonal axis may only have a numerical or datetime format");
        }

        dStart = (aax.areAxesSwapped()) ? dX : dY + dH;
        dEnd = (aax.areAxesSwapped()) ? dX + dW : dY;
        sc = axPrimaryOrthogonal.getScale();
        scPrimaryOrthogonal = AutoScale.computeScale(ids, oaxPrimaryOrthogonal, dsi, iAxisType, dStart, dEnd, sc
            .getMin(), sc.getMax(), sc.isSetStep() ? new Double(sc.getStep()) : null, axPrimaryOrthogonal
            .getFormatSpecifier(), lcl);
        oaxPrimaryOrthogonal.set(scPrimaryOrthogonal); // UPDATE SCALE ON
        // PRIMARY-ORTHOGONAL
        // AXIS

        // ITERATIVELY ADJUST THE PRIMARY ORTHOGONAL AXIS POSITION DUE TO THE
        // SCALE, START/END LABELS
        double dYAxisLocation = adjustHorizontal(dX, dW, aax);

        // ITERATIVELY ADJUST THE PRIMARY BASE AXIS POSITION DUE TO THE SCALE,
        // START/END LABELS
        double dXAxisLocation = adjustVerticalDueToHorizontal(dY, dH, aax);

        // SETUP THE FULL DATASET FOR THE PRIMARY ORTHOGONAL AXIS
        iAxisType = getAxisType(axPrimaryOrthogonal);
        oaData = getTypedDataSet(axPrimaryOrthogonal, iAxisType, 0);
        scPrimaryOrthogonal.setData(dsi);

        // SETUP THE FULL DATASET FOR THE PRIMARY ORTHOGONAL AXIS
        iAxisType = getAxisType(axPrimaryBase);
        if (iAxisType != IConstants.TEXT)
        {
            scPrimaryBase.setData(getTypedDataSet(axPrimaryBase, iAxisType, 0));
        }

        scPrimaryBase.resetShifts();
        scPrimaryOrthogonal.resetShifts();

        // UPDATE THE SIZES OF THE OVERLAY AXES
        updateOverlayAxes(aax);
        growBaseAxis(aax, bo);

        //dXAxisLocation = getLocation(scY,
        // oaxPrimaryOrthogonal.getIntersectionValue()); // UPDATE FOR OVERLAYS
        final OneAxis axPH = aax.areAxesSwapped() ? aax.getPrimaryOrthogonal() : aax.getPrimaryBase();
        final OneAxis axPV = aax.areAxesSwapped() ? aax.getPrimaryBase() : aax.getPrimaryOrthogonal();
        axPH.setAxisCoordinate(dXAxisLocation);
        axPV.setAxisCoordinate(dYAxisLocation);

        double[] daX = axPH.getScale().getEndPoints();
        double[] daY = axPV.getScale().getEndPoints();

        boPlotBackground.setLeft(daX[0] - insCA.getLeft());
        boPlotBackground.setWidth(daX[1] - daX[0] + insCA.getLeft() + insCA.getRight());
        boPlotBackground.setTop(daY[1] - insCA.getTop());
        boPlotBackground.setHeight(daY[0] - daY[1] + insCA.getTop() + insCA.getBottom());
        if (iDimension == TWO_5_D)
        {
            boPlotBackground.delta(dSeriesThickness, -dSeriesThickness, 0, 0);
        }
    }

    /**
     * This method attempts to stretch the base axis so it fits snugly (w.r.t. horizontal/vertical spacing) with the
     * overlay axes (if any)
     * 
     * @param aax
     */
    private final void growBaseAxis(AllAxes aax, Bounds bo) throws GenerationException
    {
        //if (true) return;
        OneAxis oaxBase = aax.getPrimaryBase();
        OneAxis oaxOrthogonal = aax.getPrimaryOrthogonal();
        AutoScale scBase = oaxBase.getScale();
        Axis axPrimaryBase = cwa.getPrimaryBaseAxes()[0];

        if (!aax.areAxesSwapped()) // STANDARD ORIENTATION
        {
            // IF PRIMARY ORTHOGONAL AXIS IS NOT ON THE RIGHT
            if (aax.getOverlayCount() > 0 && oaxOrthogonal.getIntersectionValue().getType() != IConstants.MAX)
            {
                // IF ANY OVERLAY ORTHOGONAL AXES ARE ON THE RIGHT
                if (aax.anyOverlayPositionedAt(IConstants.MAX))
                {
                    scBase.computeAxisStartEndShifts(ids, oaxBase.getLabel(), HORIZONTAL, oaxBase.getLabelPosition(),
                        aax);
                    final double dRightThreshold = bo.getLeft() + bo.getWidth();
                    double dEnd = scBase.getEnd();
                    final double dEndShift = scBase.getEndShift();
                    if (dEnd + dEndShift < dRightThreshold)
                    {
                        dEnd += dEndShift;
                        scBase.computeTicks(ids, oaxBase.getLabel(), oaxBase.getLabelPosition(), HORIZONTAL, scBase
                            .getStart(), dEnd, false, null);
                    }
                }
            }

            // IF PRIMARY ORTHOGONAL AXIS IS NOT ON THE LEFT
            else if (aax.getOverlayCount() > 0 && oaxOrthogonal.getIntersectionValue().getType() != IConstants.MIN)
            {
                // IF ANY OVERLAY ORTHOGONAL AXES ARE ON THE LEFT
                if (aax.anyOverlayPositionedAt(IConstants.MIN))
                {
                    scBase.computeAxisStartEndShifts(ids, oaxBase.getLabel(), HORIZONTAL, oaxBase.getLabelPosition(),
                        aax);
                    final double dLeftThreshold = bo.getLeft();
                    double dStart = scBase.getStart();
                    final double dEndShift = scBase.getEndShift();
                    final double dStartShift = scBase.getStartShift();
                    if (dStart - dStartShift > dLeftThreshold)
                    {
                        dStart -= dStartShift;
                        final double dEnd = scBase.getEnd() + dEndShift;
                        scBase.computeTicks(ids, oaxBase.getLabel(), oaxBase.getLabelPosition(), HORIZONTAL, dStart,
                            dEnd, false, null);
                    }
                }
            }
        }
        else
        {
            // IF PRIMARY ORTHOGONAL AXIS IS NOT AT THE TOP
            if (aax.getOverlayCount() > 0 && oaxOrthogonal.getIntersectionValue().getType() != IConstants.MAX)
            {
                // IF ANY OVERLAY ORTHOGONAL AXES ARE AT THE TOP
                if (aax.anyOverlayPositionedAt(IConstants.MAX))
                {
                    scBase.computeAxisStartEndShifts(ids, oaxBase.getLabel(), VERTICAL, oaxBase.getLabelPosition(),
                        aax);
                    final double dTopThreshold = bo.getTop();
                    double dEnd = scBase.getEnd();
                    final double dEndShift = Math.floor(scBase.getEndShift());
                    final double dStartShift = Math.floor(scBase.getStartShift());
                    if (dEnd - dEndShift > dTopThreshold)
                    {
                        dEnd = dEnd - dEndShift;
                        final double dStart = scBase.getStart();
                        scBase.computeTicks(ids, oaxBase.getLabel(), oaxBase.getLabelPosition(), VERTICAL, dStart,
                            dEnd, false, null);
                    }
                }
            }

            // IF PRIMARY ORTHOGONAL AXIS IS NOT AT THE BOTTOM
            else if (aax.getOverlayCount() > 0 && oaxOrthogonal.getIntersectionValue().getType() != IConstants.MIN)
            {
                // IF ANY OVERLAY ORTHOGONAL AXES IS AT THE BOTTOM
                if (aax.anyOverlayPositionedAt(IConstants.MIN))
                {
                    scBase.computeAxisStartEndShifts(ids, oaxBase.getLabel(), VERTICAL, oaxBase.getLabelPosition(),
                        aax);
                    final double dBottomThreshold = bo.getTop() + bo.getHeight();
                    double dStart = scBase.getStart();
                    final double dEndShift = scBase.getEndShift();
                    final double dStartShift = scBase.getStartShift();
                    if (dStart + dStartShift < dBottomThreshold)
                    {
                        dStart += dStartShift;
                        final double dEnd = scBase.getEnd() - dEndShift;
                        scBase.computeTicks(ids, oaxBase.getLabel(), oaxBase.getLabelPosition(), VERTICAL, dStart,
                            dEnd, false, null);
                    }
                }
            }
        }
    }

    /**
     * 
     * @param aax
     * @param dAxisStart
     * @param dAxisEnd
     * @param dBlockStart
     * @param dBlockLength
     * 
     * @throws PluginException
     * @throws DataSetException
     * @throws GenerationException
     */
    private final void updateOverlayScales(AllAxes aax, double dAxisStart, double dAxisEnd, double dBlockStart,
        double dBlockLength) throws PluginException, DataSetException, GenerationException, UnexpectedInputException
    {
        final Axis[] axa = ((ChartWithAxesImpl) cwa).getPrimaryBaseAxes();
        final Axis axPrimaryBase = axa[0];
        final Axis[] axaOrthogonal = ((ChartWithAxesImpl) cwa).getOrthogonalAxes(axPrimaryBase, false);

        IntersectionValue iv;
        AutoScale sc = null;
        OneAxis oaxOverlay = null;
        int iTickStyle, iAxisType, j, iTitleLocation;
        int iOverlayCount = aax.getOverlayCount();
        int iOrientation = aax.getOrientation();
        FontDefinition fd;
        double dStart, dEnd, dAxisLabelsThickness;
        Label laAxisTitle;
        Scale scModel;

        for (int i = 0; i < iOverlayCount; i++)
        {
            j = iOverlayCount - i - 1; // GO BACKWARDS TO ENSURE CORRECT
            // RENDERING ORDER
            oaxOverlay = aax.getOverlay(j); // UPDATE A PREVIOUSLY DEFINED
            // OVERLAY AXIS
            fd = oaxOverlay.getLabel().getCaption().getFont(); // NEEDED TO
            // AUTO
            // COMPUTE
            // SCALE
            iTickStyle = oaxOverlay.getCombinedTickStyle();
            iTitleLocation = oaxOverlay.getTitlePosition();
            laAxisTitle = oaxOverlay.getTitle();
            iAxisType = getAxisType(axaOrthogonal[j]);

            scModel = axaOrthogonal[j].getScale();
            sc = AutoScale.computeScale(ids, oaxOverlay, new DataSetIterator(getMinMax(axaOrthogonal[j], iAxisType),
                iAxisType), iAxisType, dAxisStart, dAxisEnd, scModel.getMin(), scModel.getMax(),
                scModel.isSetStep() ? new Double(scModel.getStep()) : null, axaOrthogonal[j].getFormatSpecifier(), lcl);

            oaxOverlay.set(sc);
            iv = oaxOverlay.getIntersectionValue();

            // UPDATE AXIS ENDPOINTS DUE TO ITS AXIS LABEL SHIFTS
            dStart = sc.getStart();
            dEnd = sc.getEnd();
            sc.computeTicks(ids, oaxOverlay.getLabel(), oaxOverlay.getLabelPosition(), iOrientation, dStart, dEnd,
                true, null);
            if (!sc.isStepFixed())
            {
                final Object[] oaMinMax = sc.getMinMax();
                while (!sc.checkFit(ids, oaxOverlay.getLabel(), oaxOverlay.getLabelPosition()))
                {
                    sc.zoomOut();
                    sc.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                    sc.computeTicks(ids, oaxOverlay.getLabel(), oaxOverlay.getLabelPosition(), iOrientation, dStart,
                        dEnd, true, null);
                }
            }
            dAxisLabelsThickness = sc.computeAxisLabelThickness(ids, oaxOverlay.getLabel(), iOrientation) /*
                                                                                                            * REQUIRED
                                                                                                            * TO FIT
                                                                                                            * CLEANLY
                                                                                                            */;
            double dAxisTitleThickness = 0;
            sc.resetShifts();

            if (iOrientation == VERTICAL)
            {
                // COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS
                // AND AXIS-PLOT SPACING
                double dX = 0, dX1 = 0, dX2 = 0; // Y-AXIS BAND VERTICAL
                // CO-ORDINATES
                final boolean bTicksLeft = (iTickStyle & TICK_LEFT) == TICK_LEFT; // 'boolean'
                // FOR
                // CONVENIENCE
                // &
                // READABILITY
                final boolean bTicksRight = (iTickStyle & TICK_RIGHT) == TICK_RIGHT; // 'boolean'
                // FOR
                // CONVENIENCE
                // &
                // READABILITY
                final double dAppliedYAxisPlotSpacing = dYAxisPlotSpacing;
                double dDeltaX1 = 0, dDeltaX2 = 0;
                if (laAxisTitle.isVisible())
                {
                    dAxisTitleThickness = computeBox(ids, iTitleLocation, laAxisTitle, 0, 0).getWidth() /*
                                                                                                          * REQUIRED TO
                                                                                                          * FIT CLEANLY
                                                                                                          */;
                }

                // COMPUTE VALUES FOR x1, x, x2
                // x = HORIZONTAL LOCATION OF Y-AXIS ALONG PLOT
                // x1 = LEFT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TICKS,
                // SPACING)
                // x2 = RIGHT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TICKS,
                // SPACING)
                if (iv.getType() == IntersectionValue.MIN) // LEFT OF PLOT
                {
                    // NOTE: ENSURE CODE SYMMETRY WITH 'iaLabelPositions[i] ==
                    // RIGHT'
                    dX = dBlockStart;
                    dX -= dAppliedYAxisPlotSpacing;
                    dX1 = dX;
                    dX2 = dX;
                    if (bTicksLeft)
                    {
                        dX1 -= TICK_SIZE;
                    }
                    if (oaxOverlay.getLabelPosition() == LEFT)
                    {
                        dX1 -= dAxisLabelsThickness;
                        dX2 += Math.max(bTicksRight ? TICK_SIZE : 0, dAppliedYAxisPlotSpacing);
                    }
                    else if (oaxOverlay.getLabelPosition() == RIGHT)
                    {
                        dX2 += Math.max((bTicksRight ? TICK_SIZE : 0) + dAxisLabelsThickness, dAppliedYAxisPlotSpacing);
                    }

                    if (iTitleLocation == LEFT)
                    {
                        dX1 -= dAxisTitleThickness;
                    }
                    else if (iTitleLocation == RIGHT)
                    {
                        dX2 += dAxisTitleThickness;
                    }

                    // ENSURE THAT WE DON'T GO BEHIND THE LEFT PLOT BLOCK EDGE
                    if (dX1 < dBlockStart)
                    {
                        final double dDelta = (dBlockStart - dX1);
                        dX1 = dBlockStart;
                        dX += dDelta;
                        dX2 += dDelta;
                    }
                    dDeltaX1 = dX - dX1;
                    dDeltaX2 = dX2 - dX;

                    dBlockStart += (dX2 - dX1); // SHIFT LEFT EDGE >>
                }
                else if (iv.getType() == IntersectionValue.MAX) // RIGHT
                {
                    // NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'
                    dX = dBlockStart + dBlockLength;
                    dX += dAppliedYAxisPlotSpacing;
                    dX1 = dX;
                    dX2 = dX;
                    if (bTicksRight)
                    {
                        dX2 += TICK_SIZE;
                    }

                    if (oaxOverlay.getLabelPosition() == RIGHT)
                    {
                        dX2 += dAxisLabelsThickness;
                        dX1 -= Math.max(bTicksLeft ? TICK_SIZE : 0, dAppliedYAxisPlotSpacing);
                    }
                    else if (oaxOverlay.getLabelPosition() == LEFT)
                    {
                        dX1 -= Math.max((bTicksLeft ? TICK_SIZE : 0) + dAxisLabelsThickness, dAppliedYAxisPlotSpacing);
                    }

                    dDeltaX1 = dX - dX1;
                    dDeltaX2 = dX2 - dX;
                    if (iTitleLocation == LEFT)
                    {
                        dX1 -= dAxisTitleThickness;
                    }
                    else if (iTitleLocation == RIGHT)
                    {
                        dX2 += dAxisTitleThickness;
                    }

                    // ENSURE THAT WE DON'T GO AHEAD OF THE RIGHT PLOT BLOCK
                    // EDGE
                    if (dX2 > dBlockStart + dBlockLength)
                    {
                        final double dDelta = dX2 - (dBlockStart + dBlockLength);
                        dX2 = dBlockStart + dBlockLength;
                        dX -= dDelta;
                        dX1 -= dDelta;
                    }

                    dAxisLabelsThickness = dX2 - dX1; // REUSE VARIABLE
                }
                dBlockLength -= dAxisLabelsThickness; // SHIFT RIGHT EDGE <<

                double dDelta = 0;
                if (iv.getType() == IntersectionValue.MIN)
                {
                    dDelta = -insCA.getLeft();
                }
                else if (iv.getType() == IntersectionValue.MAX)
                {
                    dDelta = insCA.getRight();
                }

                oaxOverlay.setAxisCoordinate(dX + dDelta);
                oaxOverlay.setTitleCoordinate((iTitleLocation == LEFT) ? dX1 + dDelta - 1 : dX2 + 1
                    - dAxisTitleThickness + dDelta // dX1<=>dX<=>dX2
                // INCORPORATES
                    // TITLE
                    );
            }
            else if (iOrientation == HORIZONTAL)
            {
                // COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS
                // AND AXIS-PLOT SPACING
                double dY = 0, dY1 = dY, dY2 = dY, dDeltaY1 = 0, dDeltaY2 = 0; // X-AXIS
                // BAND
                // VERTICAL
                // CO-ORDINATES
                final boolean bTicksAbove = (iTickStyle & TICK_ABOVE) == TICK_ABOVE; // 'boolean'
                // FOR
                // CONVENIENCE
                // &
                // READABILITY
                final boolean bTicksBelow = (iTickStyle & TICK_BELOW) == TICK_BELOW; // 'boolean'
                // FOR
                // CONVENIENCE
                // &
                // READABILITY
                final double dAppliedXAxisPlotSpacing = dXAxisPlotSpacing;
                if (laAxisTitle.isVisible())
                {
                    dAxisTitleThickness = computeBox(ids, iTitleLocation, laAxisTitle, 0, 0).getHeight() /*
                                                                                                           * REQUIRED TO
                                                                                                           * FIT CLEANLY
                                                                                                           */;
                }

                // COMPUTE VALUES FOR y1, y, y2
                // y = VERTICAL LOCATION OF X-AXIS ALONG PLOT
                // y1 = UPPER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS,
                // SPACING)
                // y2 = LOWER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS,
                // SPACING)
                if (iv.getType() == IntersectionValue.MAX) // ABOVE THE PLOT
                {
                    dY = dBlockStart;
                    dY -= dAppliedXAxisPlotSpacing;
                    dY1 = dY;
                    dY2 = dY;
                    if (bTicksAbove)
                    {
                        dY1 -= TICK_SIZE;
                    }
                    if (oaxOverlay.getLabelPosition() == ABOVE)
                    {
                        dY1 -= dAxisLabelsThickness;
                        dY2 += Math.max(bTicksBelow ? TICK_SIZE : 0, dAppliedXAxisPlotSpacing);
                    }
                    else if (oaxOverlay.getLabelPosition() == BELOW)
                    {
                        dY2 += Math.max((bTicksBelow ? TICK_SIZE : 0) + dAxisLabelsThickness, dAppliedXAxisPlotSpacing);
                    }

                    if (iTitleLocation == ABOVE)
                    {
                        dY1 -= dAxisTitleThickness;
                    }
                    else if (iTitleLocation == BELOW)
                    {
                        dY2 += dAxisTitleThickness;
                    }

                    // ENSURE THAT WE DON'T GO BEHIND THE LEFT PLOT BLOCK EDGE
                    if (dY1 < dBlockStart)
                    {
                        final double dDelta = (dBlockStart - dY1);
                        dY1 = dBlockStart;
                        dY += dDelta;
                        dY2 += dDelta;
                    }
                    dDeltaY1 = dY - dY1;
                    dDeltaY2 = dY2 - dY;

                    dBlockStart += (dY2 - dY1); // SHIFT TOP EDGE >>
                }
                else if (iv.getType() == IntersectionValue.MIN) // BELOW THE
                // PLOT
                {
                    // NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'
                    dY = dBlockStart + dBlockLength;
                    dY += dAppliedXAxisPlotSpacing;
                    dY1 = dY;
                    dY2 = dY;
                    if (bTicksBelow)
                    {
                        dY2 += TICK_SIZE;
                    }

                    if (oaxOverlay.getLabelPosition() == BELOW)
                    {
                        dY2 += dAxisLabelsThickness;
                        dY1 -= Math.max(bTicksAbove ? TICK_SIZE : 0, dAppliedXAxisPlotSpacing);
                    }
                    else if (oaxOverlay.getLabelPosition() == ABOVE)
                    {
                        dY1 -= Math.max((bTicksAbove ? TICK_SIZE : 0) + dAxisLabelsThickness, dAppliedXAxisPlotSpacing);
                    }

                    dDeltaY1 = dY - dY1;
                    dDeltaY2 = dY2 - dY;
                    if (iTitleLocation == ABOVE)
                    {
                        dY1 -= dAxisTitleThickness;
                    }
                    else if (iTitleLocation == BELOW)
                    {
                        dY2 += dAxisTitleThickness;
                    }

                    // ENSURE THAT WE DON'T GO AHEAD OF THE RIGHT PLOT BLOCK
                    // EDGE
                    if (dY2 > dBlockStart + dBlockLength)
                    {
                        final double dDelta = dY2 - (dBlockStart + dBlockLength);
                        dY2 = dBlockStart + dBlockLength;
                        dY -= dDelta;
                        dY1 -= dDelta;
                    }
                }
                double dDelta = 0;
                if (iv.getType() == IntersectionValue.MAX)
                {
                    dDelta = -insCA.getTop();
                }
                else if (iv.getType() == IntersectionValue.MIN)
                {
                    dDelta = insCA.getBottom();
                }

                oaxOverlay.setAxisCoordinate(dY + dDelta);
                oaxOverlay.setTitleCoordinate((iTitleLocation == ABOVE) ? dY1 + dDelta - 1 : dY2 + 1
                    - dAxisTitleThickness + dDelta // dY1<=>dX<=>dY2
                // INCORPORATES
                    // TITLE
                    );

                dBlockLength -= (dY2 - dY1); // SHIFT BOTTOM EDGE <<
            }
        }

        aax.setBlockCordinates(dBlockStart, dBlockLength);
    }

    /**
     * 
     * @param aax
     * @throws DataFormatException
     */
    private final void updateOverlayAxes(AllAxes aax) throws DataFormatException, GenerationException,
        UnexpectedInputException
    {
        int iDirection = (aax.getOrientation() == HORIZONTAL) ? 1 : -1;
        final Axis[] axa = cwa.getPrimaryBaseAxes();
        final Axis axPrimaryBase = axa[0]; // NOTE: FOR REL 1 AXIS RENDERS, WE
        // SUPPORT A SINGLE PRIMARY BASE AXIS
        // ONLY
        final Axis[] axaOverlayOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, false);

        OneAxis axOverlay, axPrimary = aax.getPrimaryOrthogonal();
        AutoScale scOA, sc = axPrimary.getScale();
        double dMaxSS = Math.max(sc.getStartShift(), aax.getMaxStartShift());
        double dMaxES = Math.max(sc.getEndShift(), aax.getMaxEndShift());
        double dStart, dEnd;
        Object[] oaMinMax;
        FontDefinition fd = null;
        Object oaData;
        int iAxisType = (aax.getOverlayCount() > 0) ? aax.getOverlay(0).getScale().getType() : 0;

        Label la;
        for (int i = 0; i < aax.getOverlayCount(); i++)
        {
            axOverlay = aax.getOverlay(i);
            la = axOverlay.getLabel();
            fd = la.getCaption().getFont();
            scOA = axOverlay.getScale();
            scOA.setEndPoints(scOA.getStart() - scOA.getStartShift() * iDirection, scOA.getEnd() + scOA.getEndShift()
                * iDirection);
            /*
             * dStart = scOA.getStart() + dMaxSS * iDirection; dEnd = scOA.getEnd() - dMaxES * iDirection;
             */

            dStart = sc.getStart();
            dEnd = sc.getEnd();

            scOA.setEndPoints(dStart, dEnd);
            scOA.computeTicks(ids, la, axOverlay.getLabelPosition(), aax.getOrientation(), dStart, dEnd, false, null);
            if (!scOA.isStepFixed())
            {
                oaMinMax = scOA.getMinMax();
                while (!scOA.checkFit(ids, la, axOverlay.getLabelPosition()))
                {
                    scOA.zoomOut();
                    scOA.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                    scOA.computeTicks(ids, la, axOverlay.getLabelPosition(), aax.getOrientation(), dStart, dEnd,
                        false, null);
                }
            }

            // SETUP THE FULL DATASET FOR THE PRIMARY ORTHOGONAL AXIS
            scOA.setData(getTypedDataSet(axaOverlayOrthogonal[i], iAxisType, 0));
        }

        /*
         * if (aax.areAxesSwapped()) { final AutoScale scPrimaryHorizontal = aax.getPrimaryOrthogonal().getScale();
         * scPrimaryHorizontal.setEndPoints( scPrimaryHorizontal.getStart() + scPrimaryHorizontal.getEndShift(),
         * scPrimaryHorizontal.getEnd() - scPrimaryHorizontal.getStartShift() ); } else { final AutoScale
         * scPrimaryHorizontal = aax.getPrimaryBase().getScale(); scPrimaryHorizontal.setEndPoints(
         * scPrimaryHorizontal.getStart() + scPrimaryHorizontal.getEndShift(), scPrimaryHorizontal.getEnd() -
         * scPrimaryHorizontal.getStartShift() ); }
         */
    }

    /**
     * Goals: 1. Adjust the two ends of the vertical axis to fit start/end labels 2. Compute the horizontal co-ordinate
     * for the axis
     * 
     * @param dBlockX
     * @param dBlockWidth
     * @param aax
     * 
     * @return
     */
    private final double adjustHorizontal(double dBlockX, double dBlockWidth, AllAxes aax) throws GenerationException,
        DataFormatException, NullValueException
    {
        final OneAxis axPH = aax.areAxesSwapped() ? aax.getPrimaryOrthogonal() : aax.getPrimaryBase();
        final OneAxis axPV = aax.areAxesSwapped() ? aax.getPrimaryBase() : aax.getPrimaryOrthogonal();
        final AutoScale scX = axPH.getScale();
        final AutoScale scY = axPV.getScale();
        final int iXLabelLocation = axPH.getLabelPosition();
        final int iYLabelLocation = axPV.getLabelPosition();

        final int iYTitleLocation = axPV.getTitlePosition();
        final Label laXAxisLabels = axPH.getLabel();
        final Label laYAxisLabels = axPV.getLabel();
        final Label laXAxisTitle = axPH.getTitle();
        final Label laYAxisTitle = axPV.getTitle();
        final FontDefinition fdPH = axPH.getLabel().getCaption().getFont();
        final FontDefinition fdPV = axPV.getLabel().getCaption().getFont();
        final double dXRotation = fdPH.getRotation();
        final double dYRotation = fdPV.getRotation();
        final int iXTickStyle = axPH.getCombinedTickStyle();
        final int iYTickStyle = axPV.getCombinedTickStyle();
        final IntersectionValue iv = axPV.getIntersectionValue();

        // COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS AND
        // AXIS-PLOT SPACING
        final boolean bTicksLeft = (iYTickStyle & TICK_LEFT) == TICK_LEFT; // 'boolean'
        // FOR
        // CONVENIENCE
        // &
        // READABILITY
        final boolean bTicksRight = (iYTickStyle & TICK_RIGHT) == TICK_RIGHT; // 'boolean'
        // FOR
        // CONVENIENCE
        // &
        // READABILITY
        final double dAppliedYAxisPlotSpacing = (iv.iType == IntersectionValue.MAX || iv.iType == IntersectionValue.MIN) ? dYAxisPlotSpacing
            : 0;

        // UPDATE Y-AXIS ENDPOINTS DUE TO AXIS LABEL SHIFTS
        double dStart = scY.getStart(), dEnd = scY.getEnd();
        scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
        if (!scY.isStepFixed())
        {
            final Object[] oaMinMax = scY.getMinMax();
            while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation))
            {
                scY.zoomOut();
                scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
            }
        }

        double dYAxisLabelsThickness = scY.computeAxisLabelThickness(ids, axPV.getLabel(), VERTICAL);
        double dYAxisTitleThickness = 0;
        if (laYAxisTitle.isVisible())
        {
            try {
                dYAxisTitleThickness = computeBox(ids, iYTitleLocation, laYAxisTitle, 0, 0).getWidth();
            } catch (UnexpectedInputException uiex)
            {
                throw new GenerationException(uiex);
            }
        }
        double dX = getLocation(scX, iv), dX1 = dX, dX2 = dX; // Y-AXIS BAND HORIZONTAL CO-ORDINATES

        // COMPUTE VALUES FOR x1, x, x2
        // x = HORIZONTAL LOCATION OF Y-AXIS ALONG PLOT
        // x1 = LEFT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TITLE, TICKS &
        // SPACING)
        // x2 = RIGHT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TITLE, TICKS &
        // SPACING)
        if (iv.iType == IntersectionValue.MIN) // LEFT
        {
            // NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MAX'
            dX -= dAppliedYAxisPlotSpacing;
            dX1 = dX;
            dX2 = dX;

            if (bTicksLeft)
            {
                dX1 -= TICK_SIZE;
            }
            if (iYLabelLocation == LEFT)
            {
                dX1 -= dYAxisLabelsThickness;
                dX2 += Math.max( // IF LABELS ARE LEFT, THEN RIGHT SPACING IS
                    // MAX(RT_TICK_SIZE, HORZ_SPACING)
                    bTicksRight ? TICK_SIZE : 0, dAppliedYAxisPlotSpacing);
            }
            else if (iYLabelLocation == RIGHT)
            {
                dX2 += Math.max( // IF LABELS ARE RIGHT, THEN RIGHT SPACING IS
                    // MAX(RT_TICK_SIZE+AXIS_LBL_THCKNESS,
                    // HORZ_SPACING)
                    (bTicksRight ? TICK_SIZE : 0) + dYAxisLabelsThickness, dAppliedYAxisPlotSpacing);
            }

            if (iYTitleLocation == LEFT)
            {
                dX1 -= dYAxisTitleThickness;
            }
            else if (iYTitleLocation == RIGHT)
            {
                dX2 += dYAxisTitleThickness;
            }

            // ENSURE THAT WE DON'T GO BEHIND THE LEFT PLOT BLOCK EDGE
            if (dX1 < dBlockX)
            {
                final double dDelta = (dBlockX - dX1);
                dX1 = dBlockX;
                dX += dDelta;
                dX2 += dDelta;
            }
            final double dDeltaX1 = dX - dX1;
            final double dDeltaX2 = dX2 - dX;

            // COMPUTE THE Y-AXIS BAND THICKNESS AND ADJUST X2 FOR LABELS BELOW
            if (iYLabelLocation == RIGHT)
            {
                // Y-AXIS BAND IS (x1 -> (x+AxisPlotSpacing))
                dX2 = (dX + dAppliedYAxisPlotSpacing);
            }
            dYAxisLabelsThickness = dX2 - dX1; // REUSE VARIABLE

            // CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
            // UPPER END
            scX.computeAxisStartEndShifts(ids, laXAxisLabels, HORIZONTAL, iXLabelLocation, aax);
            if (dYAxisLabelsThickness > scX.getStartShift())
            {
                // REDUCE scX's STARTPOINT TO FIT THE Y-AXIS ON THE LEFT
                dStart = dX2 - scX.getStartShift();
            }
            else
            {
                dStart = scX.getStart();
            }
            dEnd = scX.getEnd();
            scX.resetShifts();

            // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS IF
            // OVERLAPS OCCUR
            scX.setEndPoints(dStart, dEnd);
            scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
            if (!scX.isStepFixed())
            {
                final Object[] oaMinMax = scX.getMinMax();
                while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation))
                {
                    scX.zoomOut();
                    scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                    scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
                }
            }

            // MOVE THE Y-AXIS TO THE LEFT EDGE OF THE PLOT IF SLACK SPACE
            // EXISTS
            if (dYAxisLabelsThickness < scX.getStartShift())
            {
                dX = scX.getStart() - (dX2 - dX);
            }
            dX -= insCA.getLeft();
            dX2 = dX + dDeltaX2;
            dX1 = dX - dDeltaX1;

            axPV.setTitleCoordinate((iYTitleLocation == LEFT) ? dX1 - 1 : dX2 + 1 - dYAxisTitleThickness);

        }
        else if (iv.iType == IntersectionValue.MAX) // RIGHT
        {
            // NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'

            dX += dAppliedYAxisPlotSpacing;
            dX1 = dX;
            dX2 = dX;
            if (bTicksRight)
            {
                dX2 += TICK_SIZE;
            }

            if (iYLabelLocation == RIGHT)
            {
                dX2 += dYAxisLabelsThickness;
                dX1 -= Math.max(bTicksLeft ? TICK_SIZE : 0, dAppliedYAxisPlotSpacing);
            }
            else if (iYLabelLocation == LEFT)
            {
                dX1 -= Math.max((bTicksLeft ? TICK_SIZE : 0) + dYAxisLabelsThickness, dAppliedYAxisPlotSpacing);
            }
            if (iYTitleLocation == RIGHT)
            {
                dX2 += dYAxisTitleThickness;
            }
            else if (iYTitleLocation == LEFT)
            {
                dX1 -= dYAxisTitleThickness;
            }

            // ENSURE THAT WE DON'T GO AHEAD OF THE RIGHT PLOT BLOCK EDGE
            if (dX2 > dBlockX + dBlockWidth)
            {
                final double dDelta = dX2 - (dBlockX + dBlockWidth);
                dX2 = dBlockX + dBlockWidth;
                dX -= dDelta;
                dX1 -= dDelta;
            }
            final double dDeltaX1 = dX - dX1;
            final double dDeltaX2 = dX2 - dX;

            // COMPUTE THE Y-AXIS BAND THICKNESS AND ADJUST X1 IF Y-AXIS LABELS
            // ARE ON THE LEFT
            if (iYLabelLocation == LEFT)
            {
                // Y-AXIS BAND IS ((x-AxisPlotSpacing) -> x2)
                dX1 = (dX - dAppliedYAxisPlotSpacing);
            }
            dYAxisLabelsThickness = dX2 - dX1; // REUSE VARIABLE

            // CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
            // UPPER END
            scX.computeAxisStartEndShifts(ids, laXAxisLabels, HORIZONTAL, iXLabelLocation, aax);
            if (dYAxisLabelsThickness > scX.getEndShift())
            {
                // REDUCE scX's ENDPOINT TO FIT THE Y-AXIS ON THE RIGHT
                dEnd = dX1 + scX.getEndShift();
            }
            else
            {
                dEnd = scX.getEnd();
            }
            dStart = scX.getStart();

            scX.resetShifts();

            // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS IF
            // OVERLAPS OCCUR
            scX.setEndPoints(dStart, dEnd);
            scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
            if (!scX.isStepFixed())
            {
                final Object[] oaMinMax = scX.getMinMax();
                while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation))
                {
                    scX.zoomOut();
                    scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                    scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
                }
            }

            // MOVE THE Y-AXIS TO THE LEFT EDGE OF THE PLOT IF SLACK SPACE
            // EXISTS
            if (dYAxisLabelsThickness < scX.getEndShift())
            {
                dX = scX.getEnd() - (dX1 - dX);
            }
            dX += insCA.getRight();
            dX2 = dX + dDeltaX2;
            dX1 = dX - dDeltaX1;

            axPV.setTitleCoordinate((iYTitleLocation == LEFT) ? dX1 - 1 : dX2 + 1 - dYAxisTitleThickness);

        }
        else
        {
            double dDeltaX1 = 0, dDeltaX2 = 0;
            if (iYTitleLocation == RIGHT)
            {
                dX2 += dYAxisTitleThickness;
            }
            else if (iYTitleLocation == LEFT)
            {
                dX1 -= dYAxisTitleThickness;
            }

            if (iYLabelLocation == LEFT)
            {
                dX1 -= (bTicksLeft ? TICK_SIZE : 0) + dYAxisLabelsThickness;
                dX2 += (bTicksRight ? TICK_SIZE : 0);
                dDeltaX1 = dX - dX1;
                dDeltaX2 = dX2 - dX;

                // CHECK IF LEFT EDGE OF Y-AXIS BAND GOES BEHIND THE PLOT LEFT
                // EDGE
                if (dX1 < dBlockX)
                {
                    final Object[] oaMinMax = scX.getMinMax();
                    boolean bForceBreak = false;

                    // A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE LEFT EDGE
                    // OF THE Y-AXIS LABELS WITH THE LEFT EDGE OF THE PLOT
                    // AND/OR
                    // ENSURE THAT THE START POINT OF THE X-AXIS SCALE IS
                    // SUITABLY POSITIONED

                    //computeTicks(g2d, fm, iXLabelLocation, iXRotation,
                    // HORIZONTAL, scX.dStart, scX.dEnd, scX, true);
                    do
                    {
                        // CANCEL OUT THE ENDPOINT LABEL SHIFT COMPUTATIONS FROM
                        // THE X-AXIS
                        scX.setEndPoints(scX.getStart() - scX.getStartShift(), scX.getEnd() + scX.getEndShift()); // RESTORE
                        scX.resetShifts();

                        // APPLY THE AXIS REDUCTION FORMULA W.R.T. X-AXIS
                        // STARTPOINT
                        double[] da = scX.getEndPoints();
                        double dT_RI = dBlockX - dX1; // THRESHOLD -
                        // REQUESTEDINTERSECTION
                        double dAMin_AMax = da[1] - da[0];
                        double dAMax_RI = da[1] - dX;
                        double dDelta = (dT_RI / dAMax_RI) * dAMin_AMax;
                        dStart = da[0] + dDelta;
                        dEnd = da[1];

                        if (dStart < dBlockX)
                        {
                            dStart = dBlockX;
                            bForceBreak = true; // ADJUST THE TOP EDGE OF THE
                            // Y-AXIS SCALE TO THE TOP EDGE
                            // OF THE PLOT BLOCK
                        }

                        // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
                        // LABELS IF OVERLAPS OCCUR
                        scX.setEndPoints(dStart, dEnd);
                        scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
                        while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation))
                        {
                            scX.zoomOut();
                            scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                            scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
                        }

                        dX = getLocation(scX, iv);
                        dX1 = dX - dDeltaX1; // RE-CALCULATE X-AXIS BAND LEFT
                        // EDGE
                    }
                    while (Math.abs(dX1 - dBlockX) > 1 && !bForceBreak);
                }
                else
                {
                    // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
                    // LABELS IF OVERLAPS OCCUR
                    dStart = scX.getStart();
                    dEnd = scX.getEnd();
                    scX.setEndPoints(dStart, dEnd);
                    scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
                    if (!scX.isStepFixed())
                    {
                        final Object[] oaMinMax = scX.getMinMax();
                        while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation))
                        {
                            scX.zoomOut();
                            scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                            scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
                        }
                    }
                    dX = getLocation(scX, iv);
                }
                dX1 = dX - dDeltaX1;
                dX2 = dX + dDeltaX2;
            }
            else if (iYLabelLocation == RIGHT)
            {
                dX2 += (bTicksRight ? TICK_SIZE : 0) + dYAxisLabelsThickness;
                dX1 -= (bTicksLeft ? TICK_SIZE : 0);
                dDeltaX1 = dX - dX1;
                dDeltaX2 = dX2 - dX;

                // CHECK IF RIGHT EDGE OF Y-AXIS BAND GOES BEHIND THE PLOT RIGHT
                // EDGE
                if (dX2 > dBlockX + dBlockWidth)
                {
                    final Object[] oaMinMax = scX.getMinMax();
                    boolean bForceBreak = false;

                    // A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE RIGHT EDGE
                    // OF THE Y-AXIS LABELS WITH THE RIGHT EDGE OF THE PLOT
                    // AND/OR
                    // ENSURE THAT THE START POINT OF THE X-AXIS SCALE IS
                    // SUITABLY POSITIONED

                    do
                    {
                        // CANCEL OUT THE ENDPOINT LABEL SHIFT COMPUTATIONS FROM
                        // THE X-AXIS
                        scX.setEndPoints(scX.getStart() - scX.getStartShift(), scX.getEnd() + scX.getEndShift()); // RESTORE
                        scX.resetShifts();

                        // APPLY THE AXIS REDUCTION FORMULA W.R.T. X-AXIS
                        // ENDPOINT
                        double[] da = scX.getEndPoints();
                        double dT_RI = dX2 - (dBlockX + dBlockWidth); // THRESHOLD
                        // -
                        // REQUESTEDINTERSECTION
                        double dAMin_AMax = da[1] - da[0];
                        double dAMin_RI = dX - da[0];
                        double dDelta = (dT_RI / dAMin_RI) * dAMin_AMax;
                        dEnd = da[1] - dDelta;
                        dStart = da[0];

                        if (dEnd > dBlockX + dBlockWidth)
                        {
                            dEnd = dBlockX + dBlockWidth;
                            bForceBreak = true; // ADJUST THE TOP EDGE OF THE
                            // Y-AXIS SCALE TO THE TOP EDGE
                            // OF THE PLOT BLOCK
                        }

                        // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
                        // LABELS IF OVERLAPS OCCUR
                        scX.setEndPoints(dStart, dEnd);
                        scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
                        if (!scX.isStepFixed())
                        {
                            while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation))
                            {
                                scX.zoomOut();
                                scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                                scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true,
                                    aax);
                            }
                        }
                        dX = getLocation(scX, iv);
                        dX2 = dX + dDeltaX2; // RE-CALCULATE X-AXIS BAND RIGHT
                        // EDGE
                    }
                    while (Math.abs(dX2 - (dBlockX + dBlockWidth)) > 1 && !bForceBreak);
                }
                else
                {
                    // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
                    // LABELS IF OVERLAPS OCCUR
                    dStart = scX.getStart();
                    dEnd = scX.getEnd();
                    scX.setEndPoints(dStart, dEnd);
                    scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
                    if (!scX.isStepFixed())
                    {
                        final Object[] oaMinMax = scX.getMinMax();
                        while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation))
                        {
                            scX.zoomOut();
                            scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                            scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
                        }
                    }
                    dX = getLocation(scX, iv);
                }
                dX2 = dX + dDeltaX2;
                dX1 = dX - dDeltaX1;
            }
            axPV.setTitleCoordinate((iYTitleLocation == LEFT) ? dX1 - 1 : dX2 + 1 - dYAxisTitleThickness);
        }
        return dX;
    }

    /**
     * 
     * @param dBlockY
     * @param dBlockHeight
     * @param aax
     * 
     * @return
     */
    private final double adjustVerticalDueToHorizontal(double dBlockY, double dBlockHeight, AllAxes aax)
        throws GenerationException, DataFormatException, NullValueException
    {
        final OneAxis axPH = aax.areAxesSwapped() ? aax.getPrimaryOrthogonal() : aax.getPrimaryBase();
        final OneAxis axPV = aax.areAxesSwapped() ? aax.getPrimaryBase() : aax.getPrimaryOrthogonal();
        final AutoScale scX = axPH.getScale();
        final AutoScale scY = axPV.getScale();
        final int iXLabelLocation = axPH.getLabelPosition();
        final int iYLabelLocation = axPV.getLabelPosition();
        final int iXTitleLocation = axPH.getTitlePosition();

        final Label laXAxisTitle = axPH.getTitle();
        final Label laYAxisLabels = axPV.getLabel();
        final FontDefinition fdPH = axPH.getLabel().getCaption().getFont();
        final FontDefinition fdPV = axPV.getLabel().getCaption().getFont();
        final double dXRotation = fdPH.getRotation();
        final double dYRotation = fdPV.getRotation();
        final int iXTickStyle = axPH.getCombinedTickStyle();
        final int iYTickStyle = axPV.getCombinedTickStyle();
        final IntersectionValue iv = axPH.getIntersectionValue();

        // COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS AND
        // AXIS-PLOT SPACING
        double dXAxisLabelsThickness = scX.computeAxisLabelThickness(ids, axPH.getLabel(), HORIZONTAL);
        double dXAxisTitleThickness = 0;
        if (laXAxisTitle.isVisible())
        {
            try {
                dXAxisTitleThickness = computeBox(ids, iXTitleLocation, laXAxisTitle, 0, 0).getHeight();
            } catch (UnexpectedInputException uiex)
            {
                throw new GenerationException(uiex);
            }
        }

        double dY = getLocation(scY, iv), dY1 = dY, dY2 = dY; // X-AXIS BAND
        // VERTICAL
        // CO-ORDINATES
        final boolean bTicksAbove = (iXTickStyle & TICK_ABOVE) == TICK_ABOVE; // 'boolean'
        // FOR
        // CONVENIENCE
        // &
        // READABILITY
        final boolean bTicksBelow = (iXTickStyle & TICK_BELOW) == TICK_BELOW; // 'boolean'
        // FOR
        // CONVENIENCE
        // &
        // READABILITY
        final double dAppliedXAxisPlotSpacing = (iv.iType == IntersectionValue.MAX || iv.iType == IntersectionValue.MIN) ? dXAxisPlotSpacing
            : 0;

        // COMPUTE VALUES FOR y1, y, y2
        // y = VERTICAL LOCATION OF X-AXIS ALONG PLOT
        // y1 = UPPER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS, SPACING)
        // y2 = LOWER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS, SPACING)
        if (iv.iType == IntersectionValue.MAX)
        {
            // NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'

            dY -= dAppliedXAxisPlotSpacing;
            dY1 = dY;
            dY2 = dY;
            if (bTicksAbove)
            {
                dY1 -= TICK_SIZE;
            }
            if (iXLabelLocation == ABOVE)
            {
                dY1 -= dXAxisLabelsThickness;
                dY2 += Math.max(bTicksBelow ? TICK_SIZE : 0, dAppliedXAxisPlotSpacing);
            }
            else if (iXLabelLocation == BELOW)
            {
                dY2 += Math.max((bTicksBelow ? TICK_SIZE : 0) + dXAxisLabelsThickness, dAppliedXAxisPlotSpacing);
            }

            if (iXTitleLocation == ABOVE)
            {
                dY1 -= dXAxisTitleThickness;
            }
            else if (iXTitleLocation == BELOW)
            {
                dY2 += dXAxisTitleThickness;
            }

            // ENSURE THAT WE DON'T GO ABOVE THE UPPER PLOT BLOCK EDGE
            if (dY1 < dBlockY)
            {
                final double dDelta = (dBlockY - dY1);
                dY1 = dBlockY;
                dY += dDelta;
                dY2 += dDelta;
            }
            double dDeltaY1 = dY - dY1;
            double dDeltaY2 = dY2 - dY;

            // COMPUTE THE X-AXIS BAND THICKNESS AND ADJUST Y2 FOR LABELS BELOW
            dXAxisLabelsThickness = 0; // REUSE VARIABLE
            if (iXLabelLocation == ABOVE)
            {
                // X-AXIS BAND IS (y1 -> y2)
                dXAxisLabelsThickness = dY2 - dY1;
            }
            else if (iXLabelLocation == BELOW)
            {
                // X-AXIS BAND IS (y1 -> (y+AxisPlotSpacing))
                dY2 = (dY + dAppliedXAxisPlotSpacing);
                dXAxisLabelsThickness = dY2 - dY1;
            }

            // CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
            // UPPER END
            if (dXAxisLabelsThickness > scY.getEndShift())
            {
                // REDUCE scY's ENDPOINT TO FIT THE X-AXIS AT THE TOP
                scY.setEndPoints(scY.getStart() + scY.getStartShift(), scY.getEnd() - scY.getEndShift());
                double dStart = scY.getStart(), dEnd = dY2 - scY.getEndShift();
                scY.resetShifts();

                // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
                // IF OVERLAPS OCCUR
                scY.setEndPoints(dStart, dEnd);
                scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
                if (!scY.isStepFixed())
                {
                    final Object[] oaMinMax = scY.getMinMax();
                    while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation))
                    {
                        scY.zoomOut();
                        scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                        scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
                    }
                }
            }

            dY -= insCA.getTop();
            dY1 = dY - dDeltaY1;
            dY2 = dY + dDeltaY2;
            axPH.setTitleCoordinate((iXTitleLocation == ABOVE) ? dY1 - 1 : dY2 + 1 - dXAxisTitleThickness);
        }
        else if (iv.iType == IntersectionValue.MIN)
        {
            // NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MAX'

            dY += dAppliedXAxisPlotSpacing;
            dY1 = dY;
            dY2 = dY;
            if (bTicksBelow)
            {
                dY2 += TICK_SIZE;
            }
            if (iXLabelLocation == ABOVE)
            {
                dY1 -= Math.max((bTicksAbove ? TICK_SIZE : 0) + dXAxisLabelsThickness, dAppliedXAxisPlotSpacing);
            }
            else if (iXLabelLocation == BELOW)
            {
                dY2 += dXAxisLabelsThickness;
                dY1 -= Math.max(bTicksAbove ? TICK_SIZE : 0, dAppliedXAxisPlotSpacing);
            }
            if (iXTitleLocation == ABOVE)
            {
                dY1 -= dXAxisTitleThickness;
            }
            else if (iXTitleLocation == BELOW)
            {
                dY2 += dXAxisTitleThickness;
            }

            // ENSURE THAT WE DON'T GO BELOW THE LOWER PLOT BLOCK EDGE
            if (dY2 > dBlockY + dBlockHeight)
            {
                final double dDelta = (dY2 - (dBlockY + dBlockHeight));
                dY2 = dBlockY + dBlockHeight;
                dY -= dDelta;
                dY1 -= dDelta;
            }
            double dDeltaY1 = dY - dY1;
            double dDeltaY2 = dY2 - dY;

            // COMPUTE THE X-AXIS BAND THICKNESS AND ADJUST Y2 FOR LABELS BELOW
            dXAxisLabelsThickness = 0; // REUSE VARIABLE
            if (iXLabelLocation == ABOVE)
            {
                // X-AXIS BAND IS ((y+AxisPlotSpacing) -> y2)
                dY1 = (dY - dAppliedXAxisPlotSpacing);
                dXAxisLabelsThickness = dY2 - dY1;
            }
            else if (iXLabelLocation == BELOW)
            {
                // X-AXIS BAND IS (y1 -> y2)
                dXAxisLabelsThickness = dY2 - dY1;
            }

            // CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
            // LOWER END
            if (dXAxisLabelsThickness > scY.getStartShift())
            {
                // REDUCE scY's STARTPOINT TO FIT THE X-AXIS AT THE TOP
                scY.setEndPoints(scY.getStart() + scY.getStartShift(), scY.getEnd() - scY.getEndShift()); // RESTORE
                double dStart = dY1 + scY.getStartShift(), dEnd = scY.getEnd();
                scY.resetShifts();

                // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
                // IF OVERLAPS OCCUR
                scY.setEndPoints(dStart, dEnd);
                scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
                if (!scY.isStepFixed())
                {
                    final Object[] oaMinMax = scY.getMinMax();
                    while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation))
                    {
                        scY.zoomOut();
                        scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                        scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
                    }
                }
            }

            // MOVE THE BAND DOWNWARDS BY INSETS.BOTTOM
            dY += insCA.getBottom();
            dY1 = dY - dDeltaY1;
            dY2 = dY + dDeltaY2;

            // SET THE AXIS TITLE's BOX TOP CO-ORDINATE
            axPH.setTitleCoordinate((iXTitleLocation == ABOVE) ? dY1 - 1 : dY2 + 1 - dXAxisTitleThickness);
        }
        else
        {
            double dDeltaY1 = 0, dDeltaY2 = 0;
            if (iXLabelLocation == ABOVE)
            {
                dY1 -= (bTicksAbove ? TICK_SIZE : 0) + dXAxisLabelsThickness;
                dY2 += (bTicksBelow ? TICK_SIZE : 0);

                if (iXTitleLocation == ABOVE)
                {
                    dY1 -= dXAxisTitleThickness;
                }
                else if (iXTitleLocation == BELOW)
                {
                    dY2 += dXAxisTitleThickness;
                }
                dDeltaY1 = dY - dY1;
                dDeltaY2 = dY2 - dY;

                // CHECK IF UPPER EDGE OF X-AXIS BAND GOES ABOVE PLOT UPPER EDGE
                if (dY1 < dBlockY)
                {
                    final Object[] oaMinMax = scY.getMinMax();
                    boolean bForceBreak = false;

                    // A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE TOP EDGE
                    // OF THE X-AXIS LABELS WITH THE TOP EDGE OF THE PLOT AND/OR
                    // ENSURE THAT THE END POINT OF THE Y-AXIS SCALE IS SUITABLY
                    // POSITIONED

                    do
                    {
                        // CANCEL OUT THE END LABEL SHIFT COMPUTATIONS FROM THE
                        // Y-AXIS
                        scY.setEndPoints(scY.getStart() + scY.getStartShift(), scY.getEnd() - scY.getEndShift()); // RESTORE
                        double dES = scY.getEndShift();
                        scY.resetShifts();

                        // APPLY THE AXIS REDUCTION FORMULA W.R.T. Y-AXIS
                        // ENDPOINT
                        double[] da = scY.getEndPoints();
                        double dT_RI = dBlockY - dY1; // THRESHOLD -
                        // REQUESTEDINTERSECTION
                        double dAMin_AMax = da[0] - da[1];
                        double dAMin_RI = da[0] - dY;
                        double dStart = da[0];
                        double dEnd = (dT_RI / dAMin_RI) * dAMin_AMax + da[1];
                        if (dEnd < dBlockY)
                        {
                            dEnd = dBlockY;
                            bForceBreak = true; // ADJUST THE TOP EDGE OF THE
                            // Y-AXIS SCALE TO THE TOP EDGE
                            // OF THE PLOT BLOCK
                        }

                        // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
                        // LABELS IF OVERLAPS OCCUR
                        scY.setEndPoints(dStart, dEnd);
                        scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
                        if (!scY.isStepFixed())
                        {
                            while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation))
                            {
                                scY.zoomOut();
                                scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                                scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true,
                                    aax);
                            }
                        }

                        dY = getLocation(scY, iv);
                        dY1 = dY - dDeltaY1; // RE-CALCULATE X-AXIS BAND UPPER
                        // EDGE
                        dY2 = dY + dDeltaY2; // REDUNDANT: RE-CALCULATE X-AXIS
                        // BAND LOWER EDGE
                    }
                    while (Math.abs(dY1 - dBlockY) > 1 && !bForceBreak);
                }
            }
            else if (iXLabelLocation == BELOW)
            {
                dY1 -= (bTicksAbove ? TICK_SIZE : 0);
                dY2 += (bTicksBelow ? TICK_SIZE : 0) + dXAxisLabelsThickness;

                if (iXTitleLocation == ABOVE)
                {
                    dY1 -= dXAxisTitleThickness;
                }
                else if (iXTitleLocation == BELOW)
                {
                    dY2 += dXAxisTitleThickness;
                }
                dDeltaY1 = dY - dY1;
                dDeltaY2 = dY2 - dY;

                // CHECK IF LOWER EDGE OF X-AXIS BAND GOES BELOW PLOT LOWER EDGE
                if (dY2 > dBlockY + dBlockHeight)
                {
                    final Object[] oaMinMax = scY.getMinMax();
                    boolean bForceBreak = false;

                    // A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE TOP EDGE
                    // OF THE X-AXIS LABELS WITH THE TOP EDGE OF THE PLOT AND/OR
                    // ENSURE THAT THE END POINT OF THE Y-AXIS SCALE IS SUITABLY
                    // POSITIONED

                    do
                    {
                        // CANCEL OUT THE END LABEL SHIFT COMPUTATIONS FROM THE
                        // Y-AXIS
                        scY.setEndPoints(scY.getStart() + scY.getStartShift(), scY.getEnd() - scY.getEndShift()); // RESTORE
                        scY.resetShifts();

                        // APPLY THE AXIS REDUCTION FORMULA W.R.T. Y-AXIS
                        // ENDPOINT
                        double[] da = scY.getEndPoints();
                        double dX2_X1 = dY2 - (dBlockY + dBlockHeight); // THRESHOLD
                        // -
                        // REQUESTEDINTERSECTION
                        double dAMin_AMax = da[0] - da[1];
                        double dX2_AMax = dY - da[1];
                        double dStart = da[0] - (dX2_X1 / dX2_AMax) * dAMin_AMax;
                        double dEnd = da[1];

                        if (dStart > dBlockY + dBlockHeight)
                        {
                            dStart = dBlockY + dBlockHeight;
                            bForceBreak = true; // ADJUST THE TOP EDGE OF THE
                            // Y-AXIS SCALE TO THE TOP EDGE
                            // OF THE PLOT BLOCK
                        }

                        // LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
                        // LABELS IF OVERLAPS OCCUR
                        scY.setEndPoints(dStart, dEnd);
                        scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
                        if (!scY.isStepFixed())
                        {
                            while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation))
                            {
                                scY.zoomOut();
                                scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
                                scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true,
                                    aax);
                            }
                        }

                        dY = getLocation(scY, iv);
                        dY2 = dY + dDeltaY2; // RE-CALCULATE X-AXIS BAND LOWER
                        // EDGE
                        dY1 = dY - dDeltaY1; // RE-CALCULATE X-AXIS BAND LOWER
                        // EDGE
                    }
                    while (Math.abs(dY2 - (dBlockY + dBlockHeight)) > 1 && !bForceBreak);
                }
            }

            axPH.setTitleCoordinate((iXTitleLocation == ABOVE) ? dY1 - 1 : dY2 + 1 - dXAxisTitleThickness);
        }

        return dY;
    }

    /**
     * 
     * @return
     */
    public final Bounds getPlotBounds()
    {
        return boPlotBackground;
    }

    /**
     * 
     * @return
     */
    public final AllAxes getAxes()
    {
        return aax;
    }

    /**
     * 
     * @param se
     * @return
     */
    private final OneAxis findOrthogonalAxis(Series se)
    {
        Axis[] axaBase = ((ChartWithAxesImpl) cwa).getBaseAxes();
        Axis axPrimaryBase = axaBase[0];
        final Axis[] axaOrthogonal = ((ChartWithAxesImpl) cwa).getOrthogonalAxes(axPrimaryBase, true);
        Series[] sea;
        Object oSeriesFound;

        for (int i = 0; i < axaOrthogonal.length; i++)
        {
            sea = axaOrthogonal[i].getRuntimeSeries();
            for (int j = 0; j < sea.length; j++)
            {
                if (sea[j] == se)
                {
                    if (i == 0) // FIRST ONE IS ALWAYS THE PRIMARY ORTHOGONAL
                    {
                        return aax.getPrimaryOrthogonal();
                    }
                    return aax.getOverlay(i - 1);
                }
            }
        }
        return null;
    }

    /**
     * 
     * @return @throws
     *         DataFormatException
     */
    public final SeriesRenderingHints getSeriesRenderingHints(Series seOrthogonal) throws NullValueException,
        DataFormatException, NotFoundException, OutOfSyncException, UndefinedValueException, UnexpectedInputException
    {
        if (seOrthogonal == null || seOrthogonal.getClass() == SeriesImpl.class) // EMPTY PLOT RENDERING TECHNIQUE
        {
            return null;
        }
        OneAxis oaxOrthogonal = findOrthogonalAxis(seOrthogonal);
        if (oaxOrthogonal == null)
        {
            throw new NotFoundException("Axis definition for series " + seOrthogonal + " could not be found");
        }
        OneAxis oaxBase = aax.getPrimaryBase();
        
        AutoScale scBase = oaxBase.getScale();
        AutoScale scOrthogonal = oaxOrthogonal.getScale();
        int iTickCount = scBase.getTickCount();
        int iUnitCount = iTickCount;
        double dUnitSize = scBase.getUnitSize();
        if (scBase.getType() == IConstants.DATE_TIME)
        {
            // TBD: HANDLE DATETIME VALUE VS TEXT AXIS
        }

        double[] daTickCoordinates = scBase.getTickCordinates();
        Object oDataBase = null;
        DataSetIterator dsiDataBase = scBase.getData();
        Object oDataOrthogonal;
        DataSetIterator dsiDataOrthogonal = getTypedDataSet(seOrthogonal, oaxOrthogonal.getScale().getType());
        double dOrthogonalZero = 0;
        if ((scOrthogonal.getType() & LOGARITHMIC) == 0)
        {
            dOrthogonalZero = getLocation(scOrthogonal, 0);
        }
        else
        {
            dOrthogonalZero = scOrthogonal.getStart();
        }
        double dBaseZero = ((scBase.getType() & IConstants.NUMERICAL) == IConstants.NUMERICAL && !oaxBase
            .isCategoryScale()) ? getLocation(scBase, 0) : scBase.getStart();

        if (scBase.getType() == TEXT || oaxBase.isCategoryScale())
        {
            iUnitCount--;
        }

        final int iDirection = (oaxBase.getOrientation() == HORIZONTAL) ? 1 : -1;

        double dX = 0, dY = 0, dLength = 0;
        Location lo;

        final int iBaseCount = dsiDataBase.size();
        final int iOrthogonalCount = dsiDataOrthogonal.size();
        if (iBaseCount != iOrthogonalCount)
        {
            throw new OutOfSyncException("Base axis contains " + iBaseCount + " items; orthogonal axis contains "
                + iOrthogonalCount + " items.");
        }

        final DataPointHints[] dpa = new DataPointHints[iBaseCount];
        final boolean bScatter = (oaxBase.getScale().getType() != IConstants.TEXT && !oaxBase.isCategoryScale());
        
        // OPTIMIZED PRE-FETCH FORMAT SPECIFIERS FOR ALL DATA POINTS
        final DataPoint dp = seOrthogonal.getDataPoint();
        final EList el = dp.getComponents();
        DataPointComponent dpc;
        DataPointComponentType dpct;
        FormatSpecifier fsBase = null, fsOrthogonal = null, fsSeries = null;
        for (int i = 0; i < el.size(); i++)
        {
            dpc = (DataPointComponent) el.get(i);
            dpct = dpc.getType();
            if (dpct == DataPointComponentType.BASE_VALUE_LITERAL)
            {
                fsBase = dpc.getFormatSpecifier();
            }
            else if (dpct == DataPointComponentType.ORTHOGONAL_VALUE_LITERAL)
            {
                fsOrthogonal = dpc.getFormatSpecifier();
            }
            else if (dpct == DataPointComponentType.SERIES_VALUE_LITERAL)
            {
                fsSeries = dpc.getFormatSpecifier();
            }
        }
        
        dsiDataBase.reset();
        dsiDataOrthogonal.reset();
        for (int i = 0; i < iBaseCount; i++)
        {
            oDataBase = dsiDataBase.next();
            oDataOrthogonal = dsiDataOrthogonal.next();

            if (!bScatter)
            {
                if (aax.areAxesSwapped())
                {
                    dY = daTickCoordinates[0] - dUnitSize * i;
                    try
                    {
                        dX = getLocation(scOrthogonal, oDataOrthogonal);
                    }
                    catch (NullValueException nvex )
                    {
                        dX = dOrthogonalZero;
                    }
                    catch (DataFormatException dfex )
                    {
                        dX = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
                    }
                }
                else
                {
                    dX = daTickCoordinates[0] + dUnitSize * i;
                    try
                    {
                        dY = getLocation(scOrthogonal, oDataOrthogonal);
                    }
                    catch (NullValueException nvex )
                    {
                        dY = dOrthogonalZero;
                    }
                    catch (DataFormatException dfex )
                    {
                        dY = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
                    }
                }
            }
            else
            // SCATTER CHARTS (BASE AXIS != CATEGORY AXIS)
            {
                try
                {
                    dX = getLocation(scBase, oDataBase);
                }
                catch (NullValueException nvex )
                {
                    dX = dBaseZero;
                }
                catch (DataFormatException dfex )
                {
                    dX = dBaseZero; // FOR CUSTOM DATA ELEMENTS
                }

                try
                {
                    dY = getLocation(scOrthogonal, oDataOrthogonal);
                }
                catch (NullValueException nvex )
                {
                    dY = dOrthogonalZero; // MAP TO ZERO
                }
                catch (DataFormatException dfex )
                {
                    dY = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
                }

                if (aax.areAxesSwapped())
                {
                    final double dTemp = dX;
                    dX = dY;
                    dY = dTemp;
                }
            }
            lo = LocationImpl.create(dX, dY);
            dLength = (i < iTickCount - 1) ? daTickCoordinates[i + 1] - daTickCoordinates[i] : 0;

            dpa[i] = new DataPointHints(
                oDataBase, oDataOrthogonal, 
                seOrthogonal.getSeriesIdentifier(), 
                seOrthogonal.getDataPoint(),
                fsBase, fsOrthogonal, fsSeries,
                lo, dLength, lcl
            );
        }

        return new SeriesRenderingHints(this, oaxBase.getAxisCoordinate(), scOrthogonal.getStart(), dOrthogonalZero,
            dSeriesThickness, daTickCoordinates, dpa, scBase, scOrthogonal, ssl);
    }

    /**
     * 
     * @return
     */
    public final IDisplayServer getDisplayServer()
    {
        return ids;
    }

    /**
     * 
     * @return
     */
    public final StackedSeriesLookup getStackedSeriesLookup()
    {
        return ssl;
    }
}