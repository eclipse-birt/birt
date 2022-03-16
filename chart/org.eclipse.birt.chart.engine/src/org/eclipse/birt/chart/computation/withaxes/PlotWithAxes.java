/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation.withaxes;

import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.LabelLimiter;
import org.eclipse.birt.chart.computation.LegendItemRenderingHints;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.PlotComputation;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.AxesRenderer;
import org.eclipse.birt.chart.render.IAxesDecorator;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;

import com.ibm.icu.util.Calendar;

/**
 * PlotWithAxes
 */
public abstract class PlotWithAxes extends PlotComputation implements IConstants {

	protected static final double AXIS_TITLE_PERCENT = 0.3;

	protected int iDimension = TWO_D;

	protected double dSeriesThickness = 10;

	protected double dXAxisPlotSpacing = 0;

	protected double dYAxisPlotSpacing = 0;

	/**
	 * All axes defined in the model are maintained in a fast data structure
	 * containing additional rendering attributes
	 */
	protected AllAxes aax = null;

	/**
	 * Bounds of the whole plot client area (include axes), insets have been
	 * calculated.
	 */
	protected Bounds boPlot = goFactory.createBounds(0, 0, 100, 100);

	/**
	 * Instance of the Helper class for align zero point of multiple axes
	 */
	protected AlignZeroHelper azHelper = null;

	public PlotWithAxes(IDisplayServer ids, RunTimeContext rtc, ChartWithAxes cwa) {
		super(ids, rtc, cwa);
	}

	/**
	 * Look up table for label limit.
	 *
	 * @param la
	 * @param lbLimit
	 */
	public void putLabelLimiter(Label la, LabelLimiter lbLimit) {
		Map<Label, LabelLimiter> mapLimiter = rtc.getState(RunTimeContext.StateKey.LABEL_LIMITER_LOOKUP_KEY);
		mapLimiter.put(la, lbLimit);
	}

	/**
	 * Look up table for label limit.
	 *
	 * @param la
	 * @return LabelLimiter
	 */
	public LabelLimiter getLabellLimiter(Label la) {
		Map<Label, LabelLimiter> mapLimiter = rtc.getState(RunTimeContext.StateKey.LABEL_LIMITER_LOOKUP_KEY);
		return mapLimiter.get(la);
	}

	/**
	 * Converts to internal (non public-model) data structures
	 *
	 * @param cd
	 * @return
	 */
	protected static final int getDimension(ChartDimension cd) {
		switch (cd.getValue()) {
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
	public static final int getAxisType(Axis ax) {
		int iAxisType = UNDEFINED;
		final AxisType at = ax.getType();
		switch (at.getValue()) {
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
		if (ax.isPercent()) {
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
	protected static final int getTickStyle(Axis ax, int iMajorOrMinor) {
		int iTickStyle = TICK_NONE;

		org.eclipse.birt.chart.model.component.Grid gr = (iMajorOrMinor == MAJOR) ? ax.getMajorGrid()
				: ax.getMinorGrid();
		final LineAttributes lia = gr.getTickAttributes();
		if (!lia.isVisible()) {
			return iTickStyle;
		}

		final TickStyle ts = gr.getTickStyle();
		switch (ts.getValue()) {
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
	protected static final IntersectionValue getIntersection(Axis ax) {
		IntersectionValue iv = null;
		AxisOrigin ao = ax.getOrigin();
		if (ao.getType() == IntersectionType.MAX_LITERAL) {
			iv = IntersectionValue.MAX_VALUE;
		} else if (ao.getType() == IntersectionType.MIN_LITERAL) {
			iv = IntersectionValue.MIN_VALUE;
		} else {
			iv = new IntersectionValue(IConstants.VALUE, ao.getValue());
		}
		return iv;
	}

	public final int getDimension() {
		return iDimension;
	}

	public final double getSeriesThickness() {
		return dSeriesThickness;
	}

	public final double getHorizontalSpacingInPixels() {
		return dXAxisPlotSpacing;
	}

	public final double getVerticalSpacingInPixels() {
		return dYAxisPlotSpacing;
	}

	/**
	 * @throws IllegalArgumentException
	 * @throws ChartException
	 */
	abstract void buildAxes() throws IllegalArgumentException, ChartException;

	public final AllAxes getAxes() {
		return aax;
	}

	public final IDisplayServer getDisplayServer() {
		return ids;
	}

	/**
	 *
	 * @param se
	 * @return
	 */
	protected final OneAxis findOrthogonalAxis(Series se) {
		Axis[] axaBase = ((ChartWithAxesImpl) getModel()).getBaseAxes();
		Axis axPrimaryBase = axaBase[0];
		final Axis[] axaOrthogonal = ((ChartWithAxesImpl) getModel()).getOrthogonalAxes(axPrimaryBase, true);
		Series[] sea;

		for (int i = 0; i < axaOrthogonal.length; i++) {
			sea = axaOrthogonal[i].getRuntimeSeries();
			for (int j = 0; j < sea.length; j++) {
				if (sea[j] == se) {
					if (i == 0) // FIRST ONE IS ALWAYS THE PRIMARY
					// ORTHOGONAL
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
	 * This method validates several crucial properties for an axis associated with
	 * a Chart
	 *
	 * @param ax The axis to validate
	 * @throws ChartException
	 */
	protected void validateAxis(Axis ax) throws ChartException {
		final int iOrientation = ax.getOrientation().getValue();

		if (iOrientation == Orientation.VERTICAL) {
			int iPosition = -1;
			if (ax.getLabel().isVisible()) // LABEL POSITION (IF VISIBLE)
			{
				iPosition = ax.getLabelPosition().getValue();
				if (iPosition != Position.LEFT && iPosition != Position.RIGHT) {
					throw new ChartException(ChartEnginePlugin.ID, ChartException.VALIDATION,
							"exception.illegal.vaxis.label.position", //$NON-NLS-1$
							new Object[] { ax.getLabelPosition().getName(), ax },
							Messages.getResourceBundle(rtc.getULocale()));
				}
			}
			if (ax.getTitle().isVisible()) // LABEL POSITION (IF VISIBLE)
			{
				iPosition = ax.getTitlePosition().getValue();
				if (iPosition != Position.LEFT && iPosition != Position.RIGHT) {
					throw new ChartException(ChartEnginePlugin.ID, ChartException.VALIDATION,
							"exception.illegal.vaxis.title.position", //$NON-NLS-1$
							new Object[] { ax.getLabelPosition().getName(), ax },
							Messages.getResourceBundle(rtc.getULocale()));
				}
			}

			int iTickStyle = ax.getMajorGrid().getTickStyle().getValue();
			if (iTickStyle != TickStyle.ACROSS && iTickStyle != TickStyle.LEFT && iTickStyle != TickStyle.RIGHT) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.VALIDATION,
						"exception.illegal.vaxis.major.tick.style", //$NON-NLS-1$
						new Object[] { ax.getMajorGrid().getTickStyle().getName(), ax },
						Messages.getResourceBundle(rtc.getULocale()));
			}
			iTickStyle = ax.getMinorGrid().getTickStyle().getValue();
			if (iTickStyle != TickStyle.ACROSS && iTickStyle != TickStyle.LEFT && iTickStyle != TickStyle.RIGHT) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.VALIDATION,
						"exception.illegal.vaxis.minor.tick.style", //$NON-NLS-1$
						new Object[] { ax.getMinorGrid().getTickStyle().getName(), ax },
						Messages.getResourceBundle(rtc.getULocale()));
			}
		} else if (iOrientation == Orientation.HORIZONTAL) {
			int iPosition = -1;
			if (ax.getLabel().isVisible()) // LABEL POSITION (IF VISIBLE)
			{
				iPosition = ax.getLabelPosition().getValue();
				if (iPosition != Position.ABOVE && iPosition != Position.BELOW) {
					throw new ChartException(ChartEnginePlugin.ID, ChartException.VALIDATION,
							"exception.illegal.haxis.label.position", //$NON-NLS-1$
							new Object[] { ax.getLabelPosition().getName(), ax },
							Messages.getResourceBundle(rtc.getULocale()));
				}
			}
			if (ax.getTitle().isVisible()) // LABEL POSITION (IF VISIBLE)
			{
				iPosition = ax.getTitlePosition().getValue();
				if (iPosition != Position.ABOVE && iPosition != Position.BELOW) {
					throw new ChartException(ChartEnginePlugin.ID, ChartException.VALIDATION,
							"exception.illegal.haxis.title.position", //$NON-NLS-1$
							new Object[] { ax.getTitlePosition().getName(), ax },
							Messages.getResourceBundle(rtc.getULocale()));
				}
			}

			int iTickStyle = ax.getMajorGrid().getTickStyle().getValue();
			if (iTickStyle != TickStyle.ACROSS && iTickStyle != TickStyle.ABOVE && iTickStyle != TickStyle.BELOW) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.VALIDATION,
						"exception.illegal.haxis.major.tick.style", //$NON-NLS-1$
						new Object[] { ax.getMajorGrid().getTickStyle().getName(), ax },
						Messages.getResourceBundle(rtc.getULocale()));
			}
			iTickStyle = ax.getMinorGrid().getTickStyle().getValue();
			if (iTickStyle != TickStyle.ACROSS && iTickStyle != TickStyle.ABOVE && iTickStyle != TickStyle.BELOW) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.VALIDATION,
						"exception.illegal.haxis.minor.tick.style", //$NON-NLS-1$
						new Object[] { ax.getMinorGrid().getTickStyle().getName(), ax },
						Messages.getResourceBundle(rtc.getULocale()));
			}
		}
	}

	/**
	 * This method converts a generic text dataset to a typed dataset as expected by
	 * the renderer
	 *
	 * @param ax           The model's axis for which a series will be queried
	 * @param iType        The renderer datatype associated with the axis
	 * @param iSeriesIndex The series index for which the typed dataset is being
	 *                     built
	 *
	 * @return
	 */
	protected final DataSetIterator getTypedDataSet(Axis ax, int iType, int iSeriesIndex)
			throws ChartException, IllegalArgumentException {
		final Series[] sea = ax.getRuntimeSeries();
		if (sea.length == 0) // TBD: PULL FROM SAMPLE DATA
		{
			if ((iType & NUMERICAL) == NUMERICAL) {
				return new DataSetIterator(new Double[] { new Double(1), new Double(2) });
			} else if ((iType & DATE_TIME) == DATE_TIME) {
				return new DataSetIterator(new Calendar[] { new CDateTime(), new CDateTime() });
			} else if ((iType & TEXT) == TEXT) {
				return new DataSetIterator(new String[] { "Category1", "Category2", "Category3" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				});
			}
		}
		return getTypedDataSet(sea[iSeriesIndex], iType);

	}

	/**
	 * @param se
	 * @param iType
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	protected final DataSetIterator getTypedDataSet(Series se, int iType)
			throws ChartException, IllegalArgumentException {
		DataSetIterator dsi = checkDataType(se, iType, rtc);

		// Reverse the series categories if needed.
		dsi.reverse(getModel().isReverseCategory());
		return dsi;
	}

	/**
	 * @param se
	 * @param iType
	 * @return
	 * @throws ChartException
	 */
	public static DataSetIterator checkDataType(Series se, int iType, RunTimeContext rtc) throws ChartException {
		DataSetIterator dsi = new DataSetIterator(se.getDataSet());

		if ((dsi.getDataType() & TEXT) == TEXT) {
			if ((iType & LINEAR) == LINEAR || (iType & LOGARITHMIC) == LOGARITHMIC
					|| (iType & DATE_TIME) == DATE_TIME) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
						"exception.text.data.numerical.datetime.axis", //$NON-NLS-1$
						Messages.getResourceBundle(rtc.getULocale()));
			}
		}

		if ((dsi.getDataType() & NUMERICAL) == NUMERICAL) {
			if ((iType & DATE_TIME) == DATE_TIME) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
						"exception.numerical.data.datetime.axis", //$NON-NLS-1$
						Messages.getResourceBundle(rtc.getULocale()));
			}
		}

		if ((dsi.getDataType() & DATE_TIME) == DATE_TIME) {
			if ((iType & LINEAR) == LINEAR || (iType & LOGARITHMIC) == LOGARITHMIC) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
						"exception.datetime.data.numerical.axis", //$NON-NLS-1$
						Messages.getResourceBundle(rtc.getULocale()));
			}
		}
		return dsi;
	}

	/**
	 * Converts to internal (non public-model) data structures and transposes value
	 * if needed
	 *
	 * @param iBaseOrOrthogonal
	 * @return
	 */
	protected final int getOrientation(int iBaseOrOrthogonal) {
		if (!getModel().isTransposed()) {
			return (iBaseOrOrthogonal == IConstants.BASE || iBaseOrOrthogonal == IConstants.ANCILLARY_BASE)
					? IConstants.HORIZONTAL
					: IConstants.VERTICAL;
		} else {
			return (iBaseOrOrthogonal == IConstants.BASE || iBaseOrOrthogonal == IConstants.ANCILLARY_BASE)
					? IConstants.VERTICAL
					: IConstants.HORIZONTAL;
		}
	}

	/**
	 * Returns a transpose of the original angle
	 *
	 * @param dOriginalAngle
	 * @return angle
	 * @throws IllegalArgumentException
	 */
	public final double getTransposedAngle(double dOriginalAngle) throws IllegalArgumentException {
		if (dOriginalAngle >= 0 && dOriginalAngle <= 90) {
			return -(90 - dOriginalAngle);
		} else if (dOriginalAngle < 0 && dOriginalAngle >= -90) {
			return (dOriginalAngle + 90);
		}
		throw new IllegalArgumentException(MessageFormat.format(
				Messages.getResourceBundle(rtc.getULocale()).getString("exception.angle.range.transpose"), //$NON-NLS-1$
				new Object[] { new Double(dOriginalAngle) }));
	}

	/**
	 * Returns a transposed or the original label position as requested depending on
	 * the plot's orientation
	 *
	 * @param iBaseOrOrthogonal
	 * @param iOriginalPosition
	 * @return position state
	 * @throws IllegalArgumentException
	 */
	public final int transposeLabelPosition(int iBaseOrOrthogonal, int iOriginalPosition)
			throws IllegalArgumentException {
		if (!getModel().isTransposed()) {
			return iOriginalPosition;
		}
		if (iBaseOrOrthogonal == IConstants.BASE) {
			switch (iOriginalPosition) {
			case IConstants.ABOVE:
				return IConstants.RIGHT;
			case IConstants.BELOW:
				return IConstants.LEFT;
			case IConstants.OUTSIDE:
			case IConstants.INSIDE:
				return iOriginalPosition;
			}
		} else if (iBaseOrOrthogonal == IConstants.ORTHOGONAL) {
			switch (iOriginalPosition) {
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
		throw new IllegalArgumentException(MessageFormat.format(
				Messages.getResourceBundle(rtc.getULocale()).getString("exception.combination.axis.label.position"), //$NON-NLS-1$
				new Object[] { Integer.valueOf(iBaseOrOrthogonal), Integer.valueOf(iOriginalPosition) }));
	}

	/**
	 * Returns a transposed or the original tick style as requested depending on the
	 * plot's orientation
	 *
	 * @param iBaseOrOrthogonal
	 * @param iOriginalStyle
	 * @return
	 * @throws IllegalArgumentException
	 */
	protected final int transposeTickStyle(int iBaseOrOrthogonal, int iOriginalStyle) throws IllegalArgumentException {
		if (!getModel().isTransposed() || iOriginalStyle == IConstants.TICK_ACROSS
				|| iOriginalStyle == IConstants.TICK_NONE) {
			return iOriginalStyle;
		}

		if (iBaseOrOrthogonal == IConstants.BASE) {
			switch (iOriginalStyle) {
			case IConstants.TICK_BELOW:
				return IConstants.TICK_LEFT;
			case IConstants.TICK_ABOVE:
				return IConstants.TICK_RIGHT;
			}
		} else if (iBaseOrOrthogonal == IConstants.ORTHOGONAL) {
			switch (iOriginalStyle) {
			case IConstants.TICK_LEFT:
				return IConstants.TICK_BELOW;
			case IConstants.TICK_RIGHT:
				return IConstants.TICK_ABOVE;
			}
		}
		throw new IllegalArgumentException(MessageFormat.format(
				Messages.getResourceBundle(rtc.getULocale()).getString("exception.combination.axis.tick.style"), //$NON-NLS-1$
				new Object[] { Integer.valueOf(iBaseOrOrthogonal), Integer.valueOf(iOriginalStyle) }));
	}

	/**
	 * Goals: 1. Adjust the two ends of the vertical axis to fit start/end labels 2.
	 * Compute the horizontal co-ordinate for the axis
	 *
	 * @param dBlockX
	 * @param dBlockWidth
	 * @param aax
	 * @param orthogonalAxis
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	protected final double adjustHorizontal(double dBlockX, double dBlockWidth, AllAxes aax, OneAxis orthogonalAxis)
			throws ChartException, IllegalArgumentException {
		final OneAxis axPH = aax.areAxesSwapped() ? orthogonalAxis : aax.getPrimaryBase();
		final OneAxis axPV = aax.areAxesSwapped() ? aax.getPrimaryBase() : orthogonalAxis;

		final AutoScale scX = axPH.getScale();
		final AutoScale scY = axPV.getScale();

		final int iXLabelLocation = axPH.getLabelPosition();
		final int iYLabelLocation = axPV.getLabelPosition();

		final int iYTitleLocation = axPV.getTitlePosition();

		final Label laXAxisLabels = axPH.getLabel();

		final Label laYAxisLabels = axPV.getLabel();
		final Label laYAxisTitle = axPV.getTitle();

		final int iYTickStyle = axPV.getCombinedTickStyle();
		final IntersectionValue iv = axPV.getIntersectionValue();

		// Compute the thickness of the axis including axis label bounds and axis-plot
		// spacing.
		final boolean bTicksLeft = (iYTickStyle & TICK_LEFT) == TICK_LEFT;
		final boolean bTicksRight = (iYTickStyle & TICK_RIGHT) == TICK_RIGHT;
		// If axis labels should be within axes, do not set default spacing, so
		// value axis labels can be aligned with category axis.
		final double dAppliedYAxisPlotSpacing = (iv.iType == IConstants.MAX || iv.iType == IConstants.MIN)
				&& !aax.getPrimaryOrthogonal().getModelAxis().isLabelWithinAxes() ? dYAxisPlotSpacing : 0;

		// Update Y-axis endpoints due to axis label shifts
		double dStart = scY.getStart(), dEnd = scY.getEnd();
		scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
		if (!scY.isStepFixed()) {
			final Object[] oaMinMax = scY.getMinMax();
			while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation)) {
				if (!scY.zoomOut()) {
					break;
				}
				scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
				int tickCount = scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true,
						aax);
				if (scY.getUnit() != null && asInteger(scY.getUnit()) == Calendar.YEAR && tickCount <= 3
						|| isSharedScale()) {
					break;
				}
			}
		}

		double dYAxisLabelsThickness = scY.computeAxisLabelThickness(ids, axPV.getLabel(), VERTICAL);

		// Compute axes decoration thickness, the value sequence is either
		// [left,right] or
		// [top, bottom]
		double[] dDecorationThickness = { 0, 0 };
		Series[] sea = getModel().getSeries(IConstants.ORTHOGONAL);
		Map<Series, LegendItemRenderingHints> seriesRenderingHints = rtc.getSeriesRenderers();
		for (int i = 0; i < sea.length; i++) {
			LegendItemRenderingHints lirh = seriesRenderingHints.get(sea[i]);

			if (lirh != null && lirh.getRenderer() instanceof AxesRenderer) {
				IAxesDecorator iad = ((AxesRenderer) lirh.getRenderer()).getAxesDecorator(axPV);

				if (iad != null) {
					double[] thickness = iad.computeDecorationThickness(ids, axPV);

					if (thickness[0] > dDecorationThickness[0]) {
						dDecorationThickness[0] = thickness[0];
					}
					if (thickness[1] > dDecorationThickness[1]) {
						dDecorationThickness[1] = thickness[1];
					}
				}
			}
		}

		double dYAxisTitleThickness = 0;
		if (laYAxisTitle.isVisible()) {
			final String sPreviousValue = laYAxisTitle.getCaption().getValue();
			laYAxisTitle.getCaption().setValue(rtc.externalizedMessage(sPreviousValue));

			// compute and save the limit of vertical axis title;
			double maxWidth = AXIS_TITLE_PERCENT * boPlot.getWidth();
			double maxHeight = boPlot.getHeight();
			if (ChartUtil.isStudyLayout(getModel())) {
				// The max height should be the range of orthogonal axis scale if it is study
				// layout.
				maxHeight = Math.abs(axPV.getScale().getEndPoints()[1] - axPV.getScale().getEndPoints()[0]);
			}
			LabelLimiter lblLimit = new LabelLimiter(maxWidth, maxHeight, 0);
			lblLimit.computeWrapping(ids, laYAxisTitle);
			lblLimit = lblLimit.limitLabelSize(cComp, ids, laYAxisTitle, EnumSet.of(LabelLimiter.Option.FIX_HEIGHT));
			putLabelLimiter(axPV.getModelAxis().getTitle(), lblLimit);
			dYAxisTitleThickness = lblLimit.getMaxWidth();
			laYAxisTitle.getCaption().setValue(sPreviousValue);
		}
		double dX = getLocation(scX, iv), dX1 = dX, dX2 = dX;
		double dWTotal = Math.abs(scX.getStart() - scX.getEnd());

		// If chart is flip, we will limit the length of category axis label
		// can't exceed half of chart width to ensure chart series can be shown,
		// here check this case and revise axis label thickness for following
		// computation.
		if (axPV.isShowLabels() && axPV.getScale().isCategoryScale()) {
			double leftTickSize = bTicksLeft ? getTickSize() : 0;
			double rightTickSize = bTicksRight ? getTickSize() : 0;
			double v = (dWTotal - dYAxisTitleThickness - leftTickSize - rightTickSize) / 2;
			if (dYAxisLabelsThickness > v) {
				axPV.getScale().getAxisLabelInfo().dActualSize = dYAxisLabelsThickness;
				dYAxisLabelsThickness = v;
			}
		}

		// handle fixed label thickness #177744
		if (axPV.getModelAxis().isSetLabelSpan()) {
			double dFixedLabelThickness = axPV.getModelAxis().getLabelSpan() * dPointToPixel;

			// if the fixed label thickness is to greate, it will not take
			// affect.
			if (dFixedLabelThickness < dWTotal - 2 * getTickSize()) {
				if (dYAxisLabelsThickness + dYAxisTitleThickness > dFixedLabelThickness) {
					axPV.setShowLabels(false);
				}
				if (dYAxisTitleThickness > dFixedLabelThickness) {
					laYAxisTitle.setVisible(false);
					dYAxisTitleThickness = 0;
				}
				dYAxisLabelsThickness = dFixedLabelThickness;
			}
		}

		// Compute values for x1, x, x2
		// x = HORIZONTAL LOCATION OF Y-AXIS ALONG PLOT
		// x1 = LEFT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TITLE, TICKS &
		// SPACING)
		// x2 = RIGHT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TITLE, TICKS &
		// SPACING)
		if (iv.iType == IConstants.MIN) {
			dX = adjustHorizontalWithOrigin(dBlockX, aax, axPV, scX, iXLabelLocation, iYLabelLocation, iYTitleLocation,
					laXAxisLabels, bTicksLeft, bTicksRight, dAppliedYAxisPlotSpacing, dYAxisLabelsThickness,
					dDecorationThickness, dYAxisTitleThickness, dX, dWTotal);

		} else if (iv.iType == IConstants.MAX) {
			dX = adjustHorizontalWithMaxOrigin(dBlockX, dBlockWidth, aax, axPV, scX, iXLabelLocation, iYLabelLocation,
					iYTitleLocation, laXAxisLabels, bTicksLeft, bTicksRight, dAppliedYAxisPlotSpacing,
					dYAxisLabelsThickness, dDecorationThickness, dYAxisTitleThickness, dX, dWTotal);
		} else {
			dX = adjustHorizontalWithValueOrigin(dBlockX, dBlockWidth, aax, axPV, scX, iXLabelLocation, iYLabelLocation,
					iYTitleLocation, laXAxisLabels, iv, bTicksLeft, bTicksRight, dYAxisLabelsThickness,
					dDecorationThickness, dYAxisTitleThickness, dX, dX1, dX2, dWTotal);
		}
		return dX;
	}

	/**
	 * @param dBlockX
	 * @param dBlockWidth
	 * @param aax
	 * @param axPV
	 * @param scX
	 * @param iXLabelLocation
	 * @param iYLabelLocation
	 * @param iYTitleLocation
	 * @param laXAxisLabels
	 * @param iv
	 * @param bTicksLeft
	 * @param bTicksRight
	 * @param dYAxisLabelsThickness
	 * @param dDecorationThickness
	 * @param dYAxisTitleThickness
	 * @param dX
	 * @param dX1
	 * @param dX2
	 * @param dWTotal
	 * @return
	 * @throws ChartException
	 */
	private double adjustHorizontalWithValueOrigin(double dBlockX, double dBlockWidth, AllAxes aax, final OneAxis axPV,
			final AutoScale scX, final int iXLabelLocation, final int iYLabelLocation, final int iYTitleLocation,
			final Label laXAxisLabels, final IntersectionValue iv, final boolean bTicksLeft, final boolean bTicksRight,
			double dYAxisLabelsThickness, double[] dDecorationThickness, double dYAxisTitleThickness, double dX,
			double dX1, double dX2, double dWTotal) throws ChartException {
		double dStart;
		double dEnd;
		double dDeltaX1 = 0, dDeltaX2 = 0;
		if (iYTitleLocation == RIGHT) {
			dX2 += dYAxisTitleThickness;
		} else if (iYTitleLocation == LEFT) {
			dX1 -= dYAxisTitleThickness;
		}

		double dW1 = 0, dW2 = 0;
		if (iYLabelLocation == LEFT) {
			dW1 = (bTicksLeft ? getTickSize() : 0) + Math.max(dYAxisLabelsThickness, dDecorationThickness[0]);
			dW2 = (bTicksRight ? getTickSize() : 0);

			if (dW1 + dW2 <= dWTotal) {
				dX1 -= dW1;
				dX2 += dW2;
				// dWTotal -= ( dW1 + dW2 );
			} else {
				// axPV.setShowTicks( false );
				axPV.setShowLabels(false);
			}

			dDeltaX1 = dX - dX1;
			dDeltaX2 = dX2 - dX;

			// Check if left edge of Y-axis band goes behind the plot left edge
			if (dX1 < dBlockX) {
				final Object[] oaMinMax = scX.getMinMax();
				boolean bForceBreak = false;

				// A loop that iteratively attempts to adjust the left edge
				// of the Y-axis labels with the left edge of the plot
				// and/or ensure that the start point of the X-axis scale is
				// Suitably positioned
				do {
					// Cancel out the endpoint label shift computations from
					// the X-axis
					scX.setEndPoints(scX.getStart() - scX.getStartShift(), scX.getEnd() + scX.getEndShift()); // RESTORE
					scX.resetShifts();

					// Apply the axis reduction formula W.R.T. X-axis
					// startpoint
					double[] da = scX.getEndPoints();
					double dT_RI = dBlockX - dX1; // Threshold requested intersection

					if (scX.getDirection() == BACKWARD) {
						double dAMin_AMax = da[0] - da[1];
						double dAMax_RI = Math.abs(da[0] - dX);
						double dDelta = (dT_RI / dAMax_RI) * dAMin_AMax;
						dEnd = da[1] + dDelta;
						dStart = da[0];

						if (dEnd < dBlockX) {
							dEnd = dBlockX;
							bForceBreak = true;
						}
					} else {
						double dAMin_AMax = da[1] - da[0];
						double dAMax_RI = Math.abs(da[1] - dX);
						double dDelta = (dT_RI / dAMax_RI) * dAMin_AMax;
						dStart = da[0] + dDelta;
						dEnd = da[1];

						if (dStart < dBlockX) {
							dStart = dBlockX;
							bForceBreak = true;
						}
					}

					// Loop that auto-resizes Y-axis and re-computes Y-axis
					// labels if overlaps occur
					scX.setEndPoints(dStart, dEnd);
					scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
					while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
						if (!scX.zoomOut()) {
							bForceBreak = true;
							break;
						}
						scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
						int tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd,
								true, aax);
						if (scX.getUnit() != null && asInteger(scX.getUnit()) == Calendar.YEAR && tickCount <= 3
								|| isSharedScale()) {
							bForceBreak = true;
							break;
						}
					}
					dX = getLocation(scX, iv);
					dX1 = dX - dDeltaX1;// Re-calculate X-axis band left edge
				} while (Math.abs(dX1 - dBlockX) > 1 && !bForceBreak);
			} else {
				// Loop that auto-resizes Y-axis and re-computes Y-axis labels if overlaps occur
				dStart = scX.getStart();
				dEnd = scX.getEnd();
				scX.setEndPoints(dStart, dEnd);
				scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
				if (!scX.isStepFixed()) {
					final Object[] oaMinMax = scX.getMinMax();
					while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
						if (!scX.zoomOut()) {
							break;
						}
						scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
						int tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd,
								true, aax);
						if (scX.getUnit() != null && asInteger(scX.getUnit()) == Calendar.YEAR && tickCount <= 3
								|| isSharedScale()) {
							break;
						}
					}
				}
				dX = getLocation(scX, iv);
			}
			dX1 = dX - dDeltaX1;
			dX2 = dX + dDeltaX2;
		} else if (iYLabelLocation == RIGHT) {
			dW1 = (bTicksLeft ? getTickSize() : 0);
			dW2 = (bTicksRight ? getTickSize() : 0) + Math.max(dYAxisLabelsThickness, dDecorationThickness[1]);

			if (dW1 + dW2 <= dWTotal) {
				dX1 -= dW1;
				dX2 += dW2;
				// dWTotal -= ( dW1 + dW2 );
			}

			dDeltaX1 = dX - dX1;
			dDeltaX2 = dX2 - dX;

			// Check if right edge of Y-axis band goes behind the plot right
			// edge
			if (dX2 > dBlockX + dBlockWidth) {
				final Object[] oaMinMax = scX.getMinMax();
				boolean bForceBreak = false;

				// A loop that iteratively attempts to adjust the right edge
				// of the Y-axis labels with the right edge of the plot
				// and/or
				// Ensure that the start point of the X-axis scale is
				// Suitably positioned

				do {
					// Cancel out the endpoint label shift computations from
					// the X-axis.
					scX.setEndPoints(scX.getStart() - scX.getStartShift(), scX.getEnd() + scX.getEndShift()); // RESTORE
					scX.resetShifts();

					// Apply the axis reduction formula W.R.T. X-axis
					// endpoint
					double[] da = scX.getEndPoints();
					double dT_RI = dX2 - (dBlockX + dBlockWidth); // Threshold requested intersection

					if (scX.getDirection() == BACKWARD) {
						double dAMin_AMax = da[0] - da[1];
						double dAMin_RI = Math.abs(dX - da[1]);
						double dDelta = Math.abs(dT_RI / dAMin_RI) * dAMin_AMax;
						dStart = da[0] - dDelta;
						dEnd = da[1];

						if (dStart > dBlockX + dBlockWidth) {
							dStart = dBlockX + dBlockWidth;
							bForceBreak = true;
						}
					} else {
						double dAMin_AMax = da[1] - da[0];
						double dAMin_RI = Math.abs(dX - da[0]);
						double dDelta = (dT_RI / dAMin_RI) * dAMin_AMax;
						dEnd = da[1] - dDelta;
						dStart = da[0];

						if (dEnd > dBlockX + dBlockWidth) {
							dEnd = dBlockX + dBlockWidth;
							bForceBreak = true;
						}
					}

					// Loop that auto-resizes Y-axis and re-computes Y-axis
					// labels if overlaps occur
					scX.setEndPoints(dStart, dEnd);
					scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
					if (!scX.isStepFixed()) {
						while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
							if (!scX.zoomOut()) {
								bForceBreak = true;
								break;
							}
							scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
							int tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart,
									dEnd, true, aax);
							if (scX.getUnit() != null && asInteger(scX.getUnit()) == Calendar.YEAR && tickCount <= 3
									|| isSharedScale()) {
								bForceBreak = true;
								break;
							}
						}
					}
					dX = getLocation(scX, iv);
					dX2 = dX + dDeltaX2; // Re-calculate X-axis band right edge
				} while (Math.abs(dX2 - (dBlockX + dBlockWidth)) > 1 && !bForceBreak);
			} else {
				// Loop that auto-resizes Y-axis and re-computes Y-axis
				// labels if overlaps occur
				dStart = scX.getStart();
				dEnd = scX.getEnd();
				scX.setEndPoints(dStart, dEnd);
				scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, aax);
				if (!scX.isStepFixed()) {
					final Object[] oaMinMax = scX.getMinMax();
					while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
						if (!scX.zoomOut()) {
							break;
						}
						scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
						int tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd,
								true, aax);
						if (scX.getUnit() != null && asInteger(scX.getUnit()) == Calendar.YEAR && tickCount <= 3
								|| isSharedScale()) {
							break;
						}
					}
				}
				dX = getLocation(scX, iv);
			}
			dX2 = dX + dDeltaX2;
			dX1 = dX - dDeltaX1;
		}

		updateAxisTitleLocationNLabelSize(axPV, iYTitleLocation, dYAxisTitleThickness, dWTotal, dX, dX1, dX2);
		return dX;
	}

	private void updateAxisTitleLocationNLabelSize(final OneAxis axPV, final int iYTitleLocation,
			double dYAxisTitleThickness, double dWTotal, double dX, double dX1, double dX2) {
		if (axPV.isShowLabels() && axPV.getScale().isCategoryScale()) {
			AutoScale.AxisLabelInfo ali = axPV.getScale().getAxisLabelInfo();
			if (axPV.getLabelPosition() == IConstants.LEFT) {
				double tickSize = ((axPV.getCombinedTickStyle() & TICK_LEFT) == TICK_LEFT) ? getTickSize() : 0;
				double labelThickness = dX - dX1 - tickSize - ((iYTitleLocation == LEFT) ? dYAxisTitleThickness : 0);
				labelThickness = ali.getValidSize(labelThickness);
				ali.dMaxSize = labelThickness;
				axPV.setTitleCoordinate(
						(iYTitleLocation == LEFT) ? dX - 1 - tickSize - labelThickness - dYAxisTitleThickness
								: dX2 + 1 - dYAxisTitleThickness);
			} else if (axPV.getLabelPosition() == IConstants.RIGHT) {
				double tickSize = ((axPV.getCombinedTickStyle() & TICK_RIGHT) == TICK_RIGHT) ? getTickSize() : 0;
				double labelThickness = dX2 - dX - tickSize - ((iYTitleLocation == RIGHT) ? dYAxisTitleThickness : 0);
				labelThickness = ali.getValidSize(labelThickness);
				ali.dMaxSize = labelThickness;
				axPV.setTitleCoordinate((iYTitleLocation == LEFT) ? dX1 - 1 : dX + 1 + tickSize + labelThickness);
			}
		} else {
			axPV.setTitleCoordinate((iYTitleLocation == LEFT) ? dX1 - 1 : dX2 + 1 - dYAxisTitleThickness);
		}
	}

	/**
	 * @param dBlockX
	 * @param dBlockWidth
	 * @param aax
	 * @param axPV
	 * @param scX
	 * @param iXLabelLocation
	 * @param iYLabelLocation
	 * @param iYTitleLocation
	 * @param laXAxisLabels
	 * @param bTicksLeft
	 * @param bTicksRight
	 * @param dAppliedYAxisPlotSpacing
	 * @param dYAxisLabelsThickness
	 * @param dDecorationThickness
	 * @param dYAxisTitleThickness
	 * @param dX
	 * @param dWTotal
	 * @return
	 * @throws ChartException
	 */
	private double adjustHorizontalWithMaxOrigin(double dBlockX, double dBlockWidth, AllAxes aax, final OneAxis axPV,
			final AutoScale scX, final int iXLabelLocation, final int iYLabelLocation, final int iYTitleLocation,
			final Label laXAxisLabels, final boolean bTicksLeft, final boolean bTicksRight,
			final double dAppliedYAxisPlotSpacing, double dYAxisLabelsThickness, double[] dDecorationThickness,
			double dYAxisTitleThickness, double dX, double dWTotal) throws ChartException {
		// 1. Compute x, xLeft, xRight.
		double dStart;
		double dEnd;
		double dX1;
		double dX2;
		if (scX.getDirection() == BACKWARD) {
			// switch if scale is backward.
			dX = getLocation(scX, IntersectionValue.MIN_VALUE);
		}

		dX += dAppliedYAxisPlotSpacing;
		dWTotal -= dAppliedYAxisPlotSpacing;
		dX1 = dX;
		dX2 = dX;

		if (bTicksRight && getTickSize() <= dWTotal) {
			dX2 += getTickSize();
			dWTotal -= getTickSize();
		} else {
			// drop the ticks of vertical axis
			// axPV.setShowTicks( false );
		}

		double dW1 = 0, dW2 = 0;
		if (iYLabelLocation == RIGHT) {
			dW1 = Math.max((bTicksLeft ? getTickSize() : 0) + dDecorationThickness[0], dAppliedYAxisPlotSpacing);
			dW2 = Math.max(dYAxisLabelsThickness, dDecorationThickness[1]);
		} else if (iYLabelLocation == LEFT) {
			dW1 = Math.max((bTicksLeft ? getTickSize() : 0) + Math.max(dYAxisLabelsThickness, dDecorationThickness[0]),
					dAppliedYAxisPlotSpacing);
			dW2 = dDecorationThickness[1];
		}

		if (dW1 + dW2 <= dWTotal) {
			dX1 -= dW1;
			dX2 += dW2;
			// dWTotal -= ( dW1 + dW2 );
		} else {
			// drop the vertical axis labels
			axPV.setShowLabels(false);
		}

		if (iYTitleLocation == RIGHT) {
			dX2 += dYAxisTitleThickness;
		} else if (iYTitleLocation == LEFT) {
			dX1 -= dYAxisTitleThickness;
		}

		// Ensure that we don't do ahead of the right plot block edge.
		if (dX2 > dBlockX + dBlockWidth) {
			final double dDelta = dX2 - (dBlockX + dBlockWidth);
			dX2 = dBlockX + dBlockWidth;
			dX -= dDelta;
			dX1 -= dDelta;
		}
		final double dDeltaX1 = dX - dX1;
		final double dDeltaX2 = dX2 - dX;

		// Compute the Y-axis band thickness and adjust x1 if Y-axis labels
		// are on the left.
		if (iYLabelLocation == LEFT) {
			// Y-axis band is ((x-AxisPlotSpacing) -> x2)
			dX1 = (dX - dAppliedYAxisPlotSpacing);
		}

		// 2. Compute and set endpoints of orthogonal axis.
		double dYAxisThickness = dX2 - dX1; // REUSE VARIABLE

		// Check if X-axis thickness requires a plot height resize at the upper end.
		scX.computeAxisStartEndShifts(ids, laXAxisLabels, HORIZONTAL, iXLabelLocation, aax);

		boolean startEndChanged = false;

		if (scX.getDirection() == BACKWARD) {
			if (dYAxisThickness > scX.getStartShift()) {
				// Reduce scX's endpoint to fit the Y-axis on the right
				dStart = dX1;
				startEndChanged = true;
			} else {
				dStart = scX.getStart();
			}
			dEnd = scX.getEnd();
		} else {
			if (dYAxisThickness > scX.getEndShift()) {
				// Reduce scX's endpoint to fit the Y-axis on the right
				dEnd = dX1;
				startEndChanged = true;
			} else {
				dEnd = scX.getEnd();
			}
			dStart = scX.getStart();
		}

		scX.resetShifts();

		// Loop that auto-resizes Y-axis and re-computes Y-axis labels if overlaps occur
		scX.setEndPoints(dStart, dEnd);

		if (scX.getDirection() == BACKWARD) {
			scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, !startEndChanged, true,
					aax);
		} else {
			scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, !startEndChanged,
					aax);
		}

		if (!scX.isStepFixed()) {
			final Object[] oaMinMax = scX.getMinMax();
			while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
				if (!scX.zoomOut()) {
					break;
				}
				scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);

				int tickCount;

				if (scX.getDirection() == BACKWARD) {
					tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd,
							!startEndChanged, true, aax);
				} else {
					tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true,
							!startEndChanged, aax);
				}

				if (scX.getUnit() != null && asInteger(scX.getUnit()) == Calendar.YEAR && tickCount <= 3
						|| isSharedScale()) {
					break;
				}
			}
		}

		// Move the Y-axis to the left edge of the plot if slack space exists or scale
		// is recomputed.
		if (scX.getDirection() == BACKWARD) {
			if (dYAxisThickness < scX.getStartShift()) {
				dX = scX.getStart() - (dX1 - dX);
			}
		} else if (dYAxisThickness < scX.getEndShift()) {
			dX = scX.getEnd() - (dX1 - dX);
		}

		// 3. Get final x, xLeft, xRight. Set title coordinate.
		dX += insCA.getRight();
		dX2 = dX + dDeltaX2;
		dX1 = dX - dDeltaX1;

		updateAxisTitleLocationNLabelSize(axPV, iYTitleLocation, dYAxisTitleThickness, dWTotal, dX, dX1, dX2);
		return dX;
	}

	/**
	 * @param dBlockX
	 * @param aax
	 * @param axPV
	 * @param scX
	 * @param iXLabelLocation
	 * @param iYLabelLocation
	 * @param iYTitleLocation
	 * @param laXAxisLabels
	 * @param bTicksLeft
	 * @param bTicksRight
	 * @param dAppliedYAxisPlotSpacing
	 * @param dYAxisLabelsThickness
	 * @param dDecorationThickness
	 * @param dYAxisTitleThickness
	 * @param dX
	 * @param dWTotal
	 * @return
	 * @throws ChartException
	 */
	private double adjustHorizontalWithOrigin(double dBlockX, AllAxes aax, final OneAxis axPV, final AutoScale scX,
			final int iXLabelLocation, final int iYLabelLocation, final int iYTitleLocation, final Label laXAxisLabels,
			final boolean bTicksLeft, final boolean bTicksRight, final double dAppliedYAxisPlotSpacing,
			double dYAxisLabelsThickness, double[] dDecorationThickness, double dYAxisTitleThickness, double dX,
			double dWTotal) throws ChartException {
		// 1. Compute x, xLeft, xRight.
		double dStart;
		double dEnd;
		double dX1;
		double dX2;
		if (scX.getDirection() == BACKWARD) {
			// switch if scale is backward.
			dX = getLocation(scX, IntersectionValue.MAX_VALUE);
		}

		dX -= dAppliedYAxisPlotSpacing;
		dX1 = dX;
		dX2 = dX;

		double dTickSize = getTickSize();

		if (bTicksLeft && dTickSize <= dWTotal) {
			dX1 -= getTickSize();
			dWTotal -= dTickSize;
		} else {
			// drop the ticks of vertical axis
			// axPV.setShowTicks( false );
			dTickSize = 0;
		}

		double dW1 = 0, dW2 = 0;

		if (iYLabelLocation == LEFT) {
			dW1 = Math.max(dYAxisLabelsThickness, dDecorationThickness[0]);
			dW2 = Math.max( // IF LABELS ARE LEFT, THEN RIGHT SPACING IS
					// MAX(RT_TICK_SIZE, HORZ_SPACING)
					bTicksRight ? getTickSize() : 0, dAppliedYAxisPlotSpacing);
		} else if (iYLabelLocation == RIGHT) {
			dW1 = dDecorationThickness[0];
			// IF LABELS ARE RIGHT, THEN RIGHT SPACING IS
			// MAX(RT_TICK_SIZE+AXIS_LBL_THCKNESS, HORZ_SPACING)
			dW2 = Math.max((bTicksRight ? getTickSize() : 0) + dYAxisLabelsThickness, dAppliedYAxisPlotSpacing);
		}

		if (dW1 + dW2 <= dWTotal) {
			dX1 -= dW1;
			dX2 += dW2;
			// dWTotal -= ( dW1 + dW2 );
		} else {
			// drop the labels of vertical axis
			axPV.setShowLabels(false);

			// if ( dFixedPos > 0 )
			// {
			// dX1 -= dW1;
			// dX2 += dW2;
			// dWTotal -= ( dW1 + dW2 );
			// }
		}

		if (iYTitleLocation == LEFT) {
			dX1 -= dYAxisTitleThickness;
		} else if (iYTitleLocation == RIGHT) {
			dX2 += dYAxisTitleThickness;
		}

		// Ensure that we don't go behind the left plot block edge.
		if (dX1 < dBlockX) {
			final double dDelta = (dBlockX - dX1);
			dX1 = dBlockX;
			dX += dDelta;
			dX2 += dDelta;
		}
		final double dDeltaX1 = dX - dX1;
		final double dDeltaX2 = dX2 - dX;

		// Compute the Y-axis band thickness and adjust x2 for labels below
		if (iYLabelLocation == RIGHT) {
			// Y-axis band is (x1 -> (x+AxisPlotSpacing))
			dX2 = (dX + dAppliedYAxisPlotSpacing);
		}

		// 2. Compute and set endpoints of orthogonal axis.
		double dYAxisThickness = dX2 - dX1; // Reuse variable

		// Check if X-axis thickness requires a plot width resize at the upper end
		scX.computeAxisStartEndShifts(ids, laXAxisLabels, HORIZONTAL, iXLabelLocation, aax);

		boolean startEndChanged = false;

		if (scX.getDirection() == BACKWARD) {
			if (dYAxisThickness > scX.getEndShift()) {
				// Reduce scX's startpoint to fit the Y-axis on the left
				dEnd = dX2;
				startEndChanged = true;
			} else {
				dEnd = scX.getEnd();
			}
			dStart = scX.getStart();
		} else {
			if (dYAxisThickness > scX.getStartShift()) {
				// Reduce scX's startpoint to fit the Y-axis on the left.
				dStart = dX2;
				startEndChanged = true;
			} else {
				dStart = scX.getStart();
			}
			dEnd = scX.getEnd();
		}

		scX.resetShifts();

		// Reset the X axis end points by the Y axis location.
		scX.setEndPoints(dStart, dEnd);

		// Loop that auto-resizes Y-axis and re-computes Y-axis labels if overlaps
		// occur.
		if (scX.getDirection() == BACKWARD) {
			scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true, !startEndChanged,
					aax);
		} else {
			scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, !startEndChanged, true,
					aax);
		}

		if (!scX.isStepFixed()) {
			final Object[] oaMinMax = scX.getMinMax();

			while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
				if (!scX.zoomOut()) {
					break;
				}
				scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);

				int tickCount;
				if (scX.getDirection() == BACKWARD) {
					tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd, true,
							!startEndChanged, aax);
				} else {
					tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, HORIZONTAL, dStart, dEnd,
							!startEndChanged, true, aax);
				}

				if (scX.getUnit() != null && asInteger(scX.getUnit()) == Calendar.YEAR && tickCount <= 3
						|| isSharedScale()) {
					break;
				}
			}
		}

		// Move the Y-axis to the left edge of the plot if slack space
		// exists or scale is recomputed
		if (scX.getDirection() == BACKWARD) {
			if (dYAxisThickness < scX.getEndShift()) {
				dX = scX.getEnd() - (dX2 - dX);
			}
		} else if (dYAxisThickness < scX.getStartShift()) {
			dX = scX.getStart() - (dX2 - dX);
		}

		// 3. Get final x, xLeft, xRight. Set title coordinate.
		dX -= insCA.getLeft();
		dX2 = dX + dDeltaX2;
		dX1 = dX - dDeltaX1;

		updateAxisTitleLocationNLabelSize(axPV, iYTitleLocation, dYAxisTitleThickness, dWTotal, dX, dX1, dX2);
		return dX;
	}

	/**
	 * @param dBlockY
	 * @param dBlockHeight
	 * @param aax
	 * @param orthogonalAxis
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	protected final double adjustVerticalDueToHorizontal(double dBlockY, double dBlockHeight, AllAxes aax,
			OneAxis orthogonalAxis) throws ChartException, IllegalArgumentException {
		final OneAxis axPH = aax.areAxesSwapped() ? orthogonalAxis : aax.getPrimaryBase();
		final OneAxis axPV = aax.areAxesSwapped() ? aax.getPrimaryBase() : orthogonalAxis;
		final AutoScale scX = axPH.getScale();
		final AutoScale scY = axPV.getScale();
		final int iXLabelLocation = axPH.getLabelPosition();
		final int iYLabelLocation = axPV.getLabelPosition();
		final int iXTitleLocation = axPH.getTitlePosition();

		final Label laXAxisTitle = axPH.getTitle();
		final Label laYAxisLabels = axPV.getLabel();
		final int iXTickStyle = axPH.getCombinedTickStyle();
		final IntersectionValue iv = axPH.getIntersectionValue();

		// COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS AND
		// AXIS-PLOT SPACING
		double dXAxisLabelsThickness = scX.computeAxisLabelThickness(ids, axPH.getLabel(), HORIZONTAL);

		// Compute axes decoration thickness, the value sequence is either
		// [left,right] or
		// [top, bottom]
		double[] dDecorationThickness = { 0, 0 };
		Series[] sea = getModel().getSeries(IConstants.ORTHOGONAL);
		Map<Series, LegendItemRenderingHints> seriesRenderingHints = rtc.getSeriesRenderers();
		for (int i = 0; i < sea.length; i++) {
			LegendItemRenderingHints lirh = seriesRenderingHints.get(sea[i]);

			if (lirh != null && lirh.getRenderer() instanceof AxesRenderer) {
				IAxesDecorator iad = ((AxesRenderer) lirh.getRenderer()).getAxesDecorator(axPH);

				if (iad != null) {
					double[] thickness = iad.computeDecorationThickness(ids, axPH);

					if (thickness[0] > dDecorationThickness[0]) {
						dDecorationThickness[0] = thickness[0];
					}
					if (thickness[1] > dDecorationThickness[1]) {
						dDecorationThickness[1] = thickness[1];
					}
				}
			}
		}

		double dXAxisTitleThickness = 0;
		if (laXAxisTitle.isVisible()) {
			final String sPreviousValue = laXAxisTitle.getCaption().getValue();
			laXAxisTitle.getCaption().setValue(rtc.externalizedMessage(sPreviousValue)); // EXTERNALIZE

			double maxWidth = Math.abs(scX.getEnd() - scX.getStart());
			double maxHeight = AXIS_TITLE_PERCENT * boPlot.getHeight();
			LabelLimiter lblLimit = new LabelLimiter(maxWidth, maxHeight, 0);
			lblLimit.computeWrapping(ids, laXAxisTitle);
			lblLimit = lblLimit.limitLabelSize(cComp, ids, laXAxisTitle, EnumSet.of(LabelLimiter.Option.FIX_WIDTH));
			putLabelLimiter(axPH.getModelAxis().getTitle(), lblLimit);
			dXAxisTitleThickness = lblLimit.getMaxHeight();
			laXAxisTitle.getCaption().setValue(sPreviousValue);
		}

		double dHTotal = Math.abs(scY.getStart() - scY.getEnd());

		// handle fixed label thickness #177744
		if (axPH.getModelAxis().isSetLabelSpan()) {
			double dFixedLabelThickness = axPH.getModelAxis().getLabelSpan() * dPointToPixel;

			// if the fixed label thickness is to greate, it will not take
			// affect.
			if (dFixedLabelThickness < dHTotal - 2 * getTickSize()) {
				if (dXAxisTitleThickness + dXAxisLabelsThickness > dFixedLabelThickness) {
					axPH.setShowLabels(false);
				}
				if (dXAxisTitleThickness > dFixedLabelThickness) {
					laXAxisTitle.setVisible(false);
					dXAxisLabelsThickness = 0;
				}
				dXAxisLabelsThickness = dFixedLabelThickness;
			}
		}

		double dY = getLocation(scY, iv), dY1 = dY, dY2 = dY;
		final boolean bTicksAbove = (iXTickStyle & TICK_ABOVE) == TICK_ABOVE;
		final boolean bTicksBelow = (iXTickStyle & TICK_BELOW) == TICK_BELOW;
		final double dAppliedXAxisPlotSpacing = (iv.iType == IConstants.MAX || iv.iType == IConstants.MIN)
				? dXAxisPlotSpacing
				: 0;
		final boolean bForwardScale = scY.getDirection() == FORWARD;

		// COMPUTE VALUES FOR y1, y, y2
		// y = VERTICAL LOCATION OF X-AXIS ALONG PLOT
		// y1 = UPPER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS, SPACING)
		// y2 = LOWER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS, SPACING)
		if ((bForwardScale && iv.iType == IConstants.MIN) || (!bForwardScale && iv.iType == IConstants.MAX)) {
			dY = adjustVerticalWithMinOrigin(dBlockY, aax, axPH, scY, iXLabelLocation, iYLabelLocation, iXTitleLocation,
					laYAxisLabels, dXAxisLabelsThickness, dDecorationThickness, dXAxisTitleThickness, dHTotal, dY,
					bTicksAbove, bTicksBelow, dAppliedXAxisPlotSpacing, bForwardScale);
		} else if ((bForwardScale && iv.iType == IConstants.MAX) || (!bForwardScale && iv.iType == IConstants.MIN)) {
			dY = adjustVerticalWithMaxOrigin(dBlockY, dBlockHeight, aax, axPH, scY, iXLabelLocation, iYLabelLocation,
					iXTitleLocation, laYAxisLabels, dXAxisLabelsThickness, dDecorationThickness, dXAxisTitleThickness,
					dHTotal, dY, bTicksAbove, bTicksBelow, dAppliedXAxisPlotSpacing, bForwardScale);
		} else {
			dY = adjustVerticalWithValueOrigin(dBlockY, dBlockHeight, aax, axPH, scY, iXLabelLocation, iYLabelLocation,
					iXTitleLocation, laYAxisLabels, iv, dXAxisLabelsThickness, dDecorationThickness,
					dXAxisTitleThickness, dHTotal, dY, dY1, dY2, bTicksAbove, bTicksBelow, bForwardScale);
		}

		return dY;
	}

	/**
	 * @param dBlockY
	 * @param dBlockHeight
	 * @param aax
	 * @param axPH
	 * @param scY
	 * @param iXLabelLocation
	 * @param iYLabelLocation
	 * @param iXTitleLocation
	 * @param laYAxisLabels
	 * @param iv
	 * @param dXAxisLabelsThickness
	 * @param dDecorationThickness
	 * @param dXAxisTitleThickness
	 * @param dHTotal
	 * @param dY
	 * @param dY1
	 * @param dY2
	 * @param bTicksAbove
	 * @param bTicksBelow
	 * @param bForwardScale
	 * @return
	 * @throws ChartException
	 */
	private double adjustVerticalWithValueOrigin(double dBlockY, double dBlockHeight, AllAxes aax, final OneAxis axPH,
			final AutoScale scY, final int iXLabelLocation, final int iYLabelLocation, final int iXTitleLocation,
			final Label laYAxisLabels, final IntersectionValue iv, double dXAxisLabelsThickness,
			double[] dDecorationThickness, double dXAxisTitleThickness, double dHTotal, double dY, double dY1,
			double dY2, final boolean bTicksAbove, final boolean bTicksBelow, final boolean bForwardScale)
			throws ChartException {
		double dDeltaY1 = 0, dDeltaY2 = 0;
		if (iXLabelLocation == ABOVE) {
			double dH1 = (bTicksAbove ? getTickSize() : 0) + Math.max(dXAxisLabelsThickness, dDecorationThickness[0]);
			double dH2 = (bTicksBelow ? getTickSize() : 0);
			if (dH1 + dH2 <= dHTotal) {
				dY1 -= dH1;
				dY2 += dH2;
				// dHTotal -= ( dH1 + dH2 );
			} else {
				// axPH.setShowTicks( false );
				axPH.setShowLabels(false);
			}

			if (iXTitleLocation == ABOVE) {
				dY1 -= dXAxisTitleThickness;
			} else if (iXTitleLocation == BELOW) {
				dY2 += dXAxisTitleThickness;
			}
			dDeltaY1 = dY - dY1;
			dDeltaY2 = dY2 - dY;

			// CHECK IF UPPER EDGE OF X-AXIS BAND GOES ABOVE PLOT UPPER EDGE
			if (dY1 < dBlockY) {
				final Object[] oaMinMax = scY.getMinMax();
				boolean bForceBreak = false;

				// A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE TOP EDGE
				// OF THE X-AXIS LABELS WITH THE TOP EDGE OF THE PLOT AND/OR
				// ENSURE THAT THE END POINT OF THE Y-AXIS SCALE IS SUITABLY
				// POSITIONED

				do {
					// CANCEL OUT THE END LABEL SHIFT COMPUTATIONS FROM THE
					// Y-AXIS
					scY.setEndPoints(scY.getStart() + scY.getStartShift(), scY.getEnd() - scY.getEndShift()); // RESTORE
					scY.resetShifts();

					// APPLY THE AXIS REDUCTION FORMULA W.R.T. Y-AXIS
					// ENDPOINT
					double[] da = scY.getEndPoints();
					double dT_RI = dBlockY - dY1; // THRESHOLD -
					// REQUESTEDINTERSECTION

					double dStart, dEnd;

					if (bForwardScale) {
						double dAMin_AMax = da[1] - da[0];
						double dAMin_RI = da[1] - dY;
						dEnd = da[1];
						dStart = (dT_RI / dAMin_RI) * dAMin_AMax + da[0];
						if (dStart < dBlockY) {
							dStart = dBlockY;
							bForceBreak = true; // ADJUST THE TOP EDGE OF
							// THE
							// Y-AXIS SCALE TO THE TOP EDGE
							// OF THE PLOT BLOCK
						}
					} else {
						double dAMin_AMax = da[0] - da[1];
						double dAMin_RI = da[0] - dY;
						dStart = da[0];
						dEnd = (dT_RI / dAMin_RI) * dAMin_AMax + da[1];
						if (dEnd < dBlockY) {
							dEnd = dBlockY;
							bForceBreak = true; // ADJUST THE TOP EDGE OF
							// THE
							// Y-AXIS SCALE TO THE TOP EDGE
							// OF THE PLOT BLOCK
						}
					}

					// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
					// LABELS IF OVERLAPS OCCUR
					scY.setEndPoints(dStart, dEnd);
					scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
					if (!scY.isStepFixed()) {
						while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation)) {
							if (!scY.zoomOut()) {
								bForceBreak = true;
								break;
							}
							scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
							int tickCount = scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart,
									dEnd, true, aax);
							if (scY.getUnit() != null && asInteger(scY.getUnit()) == Calendar.YEAR && tickCount <= 3
									|| isSharedScale()) {
								bForceBreak = true;
								break;
							}
						}
					}

					dY = getLocation(scY, iv);
					dY1 = dY - dDeltaY1; // RE-CALCULATE X-AXIS BAND
					// UPPER
					// EDGE
					dY2 = dY + dDeltaY2; // REDUNDANT: RE-CALCULATE
					// X-AXIS
					// BAND LOWER EDGE
				} while (Math.abs(dY1 - dBlockY) > 1 && !bForceBreak);
			} else if (dY2 > dBlockY + dBlockHeight) {
				final Object[] oaMinMax = scY.getMinMax();
				boolean bForceBreak = false;

				// A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE TOP EDGE
				// OF THE X-AXIS LABELS WITH THE TOP EDGE OF THE PLOT AND/OR
				// ENSURE THAT THE END POINT OF THE Y-AXIS SCALE IS SUITABLY
				// POSITIONED

				do {
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

					double dStart, dEnd;

					if (bForwardScale) {
						double dAMin_AMax = da[1] - da[0];
						double dX2_AMax = dY - da[0];
						dEnd = da[1] - (dX2_X1 / dX2_AMax) * dAMin_AMax;
						dStart = da[0];

						if (dEnd > dBlockY + dBlockHeight) {
							dEnd = dBlockY + dBlockHeight;
							bForceBreak = true;
						}
					} else {
						double dAMin_AMax = da[0] - da[1];
						double dX2_AMax = dY - da[1];
						dStart = da[0] - (dX2_X1 / dX2_AMax) * dAMin_AMax;
						dEnd = da[1];

						if (dStart > dBlockY + dBlockHeight) {
							dStart = dBlockY + dBlockHeight;
							bForceBreak = true; // ADJUST THE TOP EDGE OF
							// THE
							// Y-AXIS SCALE TO THE TOP EDGE
							// OF THE PLOT BLOCK
						}
					}

					if (ChartUtil.mathEqual(Math.abs(dEnd - dStart), 0)) {
						// too small space to adjust, break here.
						bForceBreak = true;
					}

					// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
					// LABELS IF OVERLAPS OCCUR
					scY.setEndPoints(dStart, dEnd);
					scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
					if (!scY.isStepFixed()) {
						while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation)) {
							if (!scY.zoomOut()) {
								bForceBreak = true;
								break;
							}
							scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
							int tickCount = scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart,
									dEnd, true, aax);
							if (scY.getUnit() != null && asInteger(scY.getUnit()) == Calendar.YEAR && tickCount <= 3
									|| isSharedScale()) {
								bForceBreak = true;
								break;
							}
						}
					}

					dY = getLocation(scY, iv);
					dY2 = dY + dDeltaY2; // RE-CALCULATE X-AXIS BAND
					// LOWER
					// EDGE
					dY1 = dY - dDeltaY1; // RE-CALCULATE X-AXIS BAND
					// LOWER
					// EDGE
				} while (Math.abs(dY2 - (dBlockY + dBlockHeight)) > 1 && !bForceBreak);
			}
		} else if (iXLabelLocation == BELOW) {
			double dH1 = (bTicksAbove ? getTickSize() : 0);
			double dH2 = (bTicksBelow ? getTickSize() : 0) + Math.max(dXAxisLabelsThickness, dDecorationThickness[1]);
			if (dH1 + dH2 <= dHTotal) {
				dY1 -= dH1;
				dY2 += dH2;
				// dHTotal -= ( dH1 + dH2 );
			}

			if (iXTitleLocation == ABOVE) {
				dY1 -= dXAxisTitleThickness;
			} else if (iXTitleLocation == BELOW) {
				dY2 += dXAxisTitleThickness;
			}
			dDeltaY1 = dY - dY1;
			dDeltaY2 = dY2 - dY;

			// CHECK IF LOWER EDGE OF X-AXIS BAND GOES BELOW PLOT LOWER EDGE
			if (dY2 > dBlockY + dBlockHeight) {
				final Object[] oaMinMax = scY.getMinMax();
				boolean bForceBreak = false;

				// A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE TOP EDGE
				// OF THE X-AXIS LABELS WITH THE TOP EDGE OF THE PLOT AND/OR
				// ENSURE THAT THE END POINT OF THE Y-AXIS SCALE IS SUITABLY
				// POSITIONED

				do {
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

					double dStart, dEnd;

					if (bForwardScale) {
						double dAMin_AMax = da[1] - da[0];
						double dX2_AMax = dY - da[0];
						dEnd = da[1] - (dX2_X1 / dX2_AMax) * dAMin_AMax;
						dStart = da[0];

						if (dEnd > dBlockY + dBlockHeight) {
							dEnd = dBlockY + dBlockHeight;
							bForceBreak = true;
						}
					} else {
						double dAMin_AMax = da[0] - da[1];
						double dX2_AMax = dY - da[1];
						dStart = da[0] - (dX2_X1 / dX2_AMax) * dAMin_AMax;
						dEnd = da[1];

						if (dStart > dBlockY + dBlockHeight) {
							dStart = dBlockY + dBlockHeight;
							bForceBreak = true; // ADJUST THE TOP EDGE OF
							// THE
							// Y-AXIS SCALE TO THE TOP EDGE
							// OF THE PLOT BLOCK
						}
					}

					if (ChartUtil.mathEqual(Math.abs(dEnd - dStart), 0)) {
						// too small space to adjust, break here.
						bForceBreak = true;
					}

					// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
					// LABELS IF OVERLAPS OCCUR
					scY.setEndPoints(dStart, dEnd);
					scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
					if (!scY.isStepFixed()) {
						while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation)) {
							if (!scY.zoomOut()) {
								bForceBreak = true;
								break;
							}
							scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
							int tickCount = scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart,
									dEnd, true, aax);
							if (scY.getUnit() != null && asInteger(scY.getUnit()) == Calendar.YEAR && tickCount <= 3
									|| isSharedScale()) {
								bForceBreak = true;
								break;
							}
						}
					}

					dY = getLocation(scY, iv);
					dY2 = dY + dDeltaY2; // RE-CALCULATE X-AXIS BAND
					// LOWER
					// EDGE
					dY1 = dY - dDeltaY1; // RE-CALCULATE X-AXIS BAND
					// LOWER
					// EDGE
				} while (Math.abs(dY2 - (dBlockY + dBlockHeight)) > 1 && !bForceBreak);
			}
		}

		axPH.setTitleCoordinate((iXTitleLocation == ABOVE) ? dY1 - 1 : dY2 + 1 - dXAxisTitleThickness);
		return dY;
	}

	/**
	 * @param dBlockY
	 * @param dBlockHeight
	 * @param aax
	 * @param axPH
	 * @param scY
	 * @param iXLabelLocation
	 * @param iYLabelLocation
	 * @param iXTitleLocation
	 * @param laYAxisLabels
	 * @param dXAxisLabelsThickness
	 * @param dDecorationThickness
	 * @param dXAxisTitleThickness
	 * @param dHTotal
	 * @param dY
	 * @param bTicksAbove
	 * @param bTicksBelow
	 * @param dAppliedXAxisPlotSpacing
	 * @param bForwardScale
	 * @return
	 * @throws ChartException
	 */
	private double adjustVerticalWithMaxOrigin(double dBlockY, double dBlockHeight, AllAxes aax, final OneAxis axPH,
			final AutoScale scY, final int iXLabelLocation, final int iYLabelLocation, final int iXTitleLocation,
			final Label laYAxisLabels, double dXAxisLabelsThickness, double[] dDecorationThickness,
			double dXAxisTitleThickness, double dHTotal, double dY, final boolean bTicksAbove,
			final boolean bTicksBelow, final double dAppliedXAxisPlotSpacing, final boolean bForwardScale)
			throws ChartException {
		// Compute y, yAbove, yBelow.
		double dY1;
		double dY2;
		// NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MAX'

		dY += dAppliedXAxisPlotSpacing;
		dHTotal -= dAppliedXAxisPlotSpacing;

		dY1 = dY;
		dY2 = dY;

		double dTickSize = getTickSize();
		if (bTicksBelow && dTickSize < dHTotal) {
			dY2 += dTickSize;
			dHTotal -= dTickSize;
		} else {
			dTickSize = 0;
			// axPH.setShowTicks( false );
		}

		if (iXLabelLocation == ABOVE) {
			double dXLabelHeight = Math.max((bTicksAbove ? dTickSize : 0) + dXAxisLabelsThickness,
					dAppliedXAxisPlotSpacing);

			if (dXLabelHeight + dDecorationThickness[1] < dHTotal) {
				dY1 -= dXLabelHeight;
				dY2 += dDecorationThickness[1];
				// dHTotal -= ( dXLabelHeight + dDecorationThickness[1] );
			} else {
				dXLabelHeight = 0;
				axPH.setShowLabels(false);
			}
		} else if (iXLabelLocation == BELOW) {
			double dXLabelHeight = Math.max(dXAxisLabelsThickness, dDecorationThickness[1]);
			double dHt1 = Math.max(bTicksAbove ? dTickSize : 0, dAppliedXAxisPlotSpacing);
			if (dXLabelHeight + dHt1 <= dHTotal) {
				dY2 += dXLabelHeight;
				dY1 -= dHt1;
				// dHTotal -= ( dXLabelHeight + dHt1 );
			} else {
				dXLabelHeight = 0;
				axPH.setShowLabels(false);
			}
		}
		if (iXTitleLocation == ABOVE) {
			dY1 -= dXAxisTitleThickness;
		} else if (iXTitleLocation == BELOW) {
			dY2 += dXAxisTitleThickness;
		}

		// ENSURE THAT WE DON'T GO BELOW THE LOWER PLOT BLOCK EDGE
		if (dY2 > dBlockY + dBlockHeight) {
			final double dDelta = (dY2 - (dBlockY + dBlockHeight));
			dY2 = dBlockY + dBlockHeight;
			dY -= dDelta;
			dY1 -= dDelta;
		}

		double dDeltaY1 = dY - dY1;
		double dDeltaY2 = dY2 - dY;

		// 2. Compute and set endpoints of orthogonal axis.
		// COMPUTE THE X-AXIS BAND THICKNESS AND ADJUST Y2 FOR LABELS BELOW
		double dXAxisThickness = 0; // REUSE VARIABLE
		if (iXLabelLocation == ABOVE) {
			// X-AXIS BAND IS ((y+AxisPlotSpacing) -> y2)
			dY1 = (dY - dAppliedXAxisPlotSpacing);
			dXAxisThickness = dY2 - dY1;
		} else if (iXLabelLocation == BELOW) {
			// X-AXIS BAND IS (y1 -> y2)
			dXAxisThickness = dY2 - dY1;
		}

		// CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
		// LOWER END
		if ((bForwardScale && dXAxisThickness > scY.getEndShift())
				|| (!bForwardScale && dXAxisThickness > scY.getStartShift())) {
			// REDUCE scY's STARTPOINT TO FIT THE X-AXIS AT THE TOP
			double dStart = dY1 + scY.getStartShift(), dEnd = scY.getEnd();

			if (bForwardScale) {
				dStart = scY.getStart();
				dEnd = dY1 + scY.getEndShift();
			}
			scY.resetShifts();

			if (dStart < dEnd + 1) {
				dStart = dEnd + 1;
			}

			// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
			// IF OVERLAPS OCCUR
			scY.setEndPoints(dStart, dEnd);
			scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
			if (!scY.isStepFixed()) {
				final Object[] oaMinMax = scY.getMinMax();
				while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation)) {
					if (!scY.zoomOut()) {
						break;
					}
					double dOldStep = ((Number) scY.getStep()).doubleValue();
					scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
					int tickCount = scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true,
							aax);
					double dNewStep = ((Number) scY.getStep()).doubleValue();
					if (dNewStep < dOldStep) {
						break;
					}

					if (scY.getUnit() != null && asInteger(scY.getUnit()) == Calendar.YEAR && tickCount <= 3
							|| isSharedScale()) {
						break;
					}
				}
			}
		}

		// 3. Get final y, yAbove, yBelow. set title coordinate.
		// MOVE THE BAND DOWNWARDS BY INSETS.BOTTOM
		dY += insCA.getBottom();
		dY1 = dY - dDeltaY1;
		dY2 = dY + dDeltaY2;

		// SET THE AXIS TITLE's BOX TOP CO-ORDINATE
		axPH.setTitleCoordinate((iXTitleLocation == ABOVE) ? dY1 - 1 : dY2 + 1 - dXAxisTitleThickness);
		return dY;
	}

	/**
	 * @param dBlockY
	 * @param aax
	 * @param axPH
	 * @param scY
	 * @param iXLabelLocation
	 * @param iYLabelLocation
	 * @param iXTitleLocation
	 * @param laYAxisLabels
	 * @param dXAxisLabelsThickness
	 * @param dDecorationThickness
	 * @param dXAxisTitleThickness
	 * @param dHTotal
	 * @param dY
	 * @param bTicksAbove
	 * @param bTicksBelow
	 * @param dAppliedXAxisPlotSpacing
	 * @param bForwardScale
	 * @return
	 * @throws ChartException
	 */
	private double adjustVerticalWithMinOrigin(double dBlockY, AllAxes aax, final OneAxis axPH, final AutoScale scY,
			final int iXLabelLocation, final int iYLabelLocation, final int iXTitleLocation, final Label laYAxisLabels,
			double dXAxisLabelsThickness, double[] dDecorationThickness, double dXAxisTitleThickness, double dHTotal,
			double dY, final boolean bTicksAbove, final boolean bTicksBelow, final double dAppliedXAxisPlotSpacing,
			final boolean bForwardScale) throws ChartException {

		// 1. Compute y, yAbove, yBelow
		double dY1;
		double dY2;
		// NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'

		dY -= dAppliedXAxisPlotSpacing;
		dHTotal -= dAppliedXAxisPlotSpacing;
		dY1 = dY;
		dY2 = dY;

		double dTickSize = getTickSize();

		if (bTicksAbove && dTickSize <= dHTotal) {
			dY1 -= dTickSize;
			dHTotal -= dTickSize;
		} else {
			// axPH.setShowTicks( false );
			dTickSize = 0;
		}

		double dH1 = 0;
		double dH2 = 0;

		if (iXLabelLocation == ABOVE) {
			dH1 = Math.max(dXAxisLabelsThickness, dDecorationThickness[0]);
			dH2 = Math.max(bTicksBelow ? getTickSize() : 0, dAppliedXAxisPlotSpacing);
		} else if (iXLabelLocation == BELOW) {
			dH1 = dDecorationThickness[0];
			dH2 += Math.max((bTicksBelow ? getTickSize() : 0) + dXAxisLabelsThickness, dAppliedXAxisPlotSpacing);
		}

		if (dH1 + dH2 <= dHTotal) {
			dY1 -= dH1;
			dY2 += dH2;
			// dHTotal -= ( dH1 + dH2 );
		} else {
			axPH.setShowLabels(false);
		}

		if (iXTitleLocation == ABOVE) {
			dY1 -= dXAxisTitleThickness;
		} else if (iXTitleLocation == BELOW) {
			dY2 += dXAxisTitleThickness;
		}

		// ENSURE THAT WE DON'T GO ABOVE THE UPPER PLOT BLOCK EDGE
		if (dY1 < dBlockY) {
			final double dDelta = (dBlockY - dY1);
			dY1 = dBlockY;
			dY += dDelta;
			dY2 += dDelta;
		}
		double dDeltaY1 = dY - dY1;
		double dDeltaY2 = dY2 - dY;

		// 2. Compute and set endpoints of orthogonal axis.
		// COMPUTE THE X-AXIS BAND THICKNESS AND ADJUST Y2 FOR LABELS BELOW
		double dXAxisThickness = 0; // REUSE VARIABLE
		if (iXLabelLocation == ABOVE) {
			// X-AXIS BAND IS (y1 -> y2)
			dXAxisThickness = dY2 - dY1;
		} else if (iXLabelLocation == BELOW) {
			// X-AXIS BAND IS (y1 -> (y+AxisPlotSpacing))
			dY2 = (dY + dAppliedXAxisPlotSpacing);
			dXAxisThickness = dY2 - dY1;
		}

		// CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
		// UPPER END
		if ((bForwardScale && dXAxisThickness > scY.getStartShift())
				|| (!bForwardScale && dXAxisThickness > scY.getEndShift())) {
			// REDUCE scY's ENDPOINT TO FIT THE X-AXIS AT THE TOP
			double dStart = scY.getStart(), dEnd = dY2 - scY.getEndShift();

			if (bForwardScale) {
				dStart = dY2 - scY.getStartShift();
				dEnd = scY.getEnd();
			}

			scY.resetShifts();

			// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
			// IF OVERLAPS OCCUR
			scY.setEndPoints(dStart, dEnd);
			scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true, aax);
			if (!scY.isStepFixed()) {
				final Object[] oaMinMax = scY.getMinMax();
				while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation)) {
					if (!scY.zoomOut()) {
						break;
					}
					scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
					int tickCount = scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, VERTICAL, dStart, dEnd, true,
							aax);
					if (scY.getUnit() != null && asInteger(scY.getUnit()) == Calendar.YEAR && tickCount <= 3
							|| isSharedScale()) {
						break;
					}
				}
			}
		}

		// 3. Get final y, yAbove, yBelow, set title cooidinate.
		dY -= insCA.getTop();
		dY1 = dY - dDeltaY1;
		dY2 = dY + dDeltaY2;
		axPH.setTitleCoordinate((iXTitleLocation == ABOVE) ? dY1 - 1 : dY2 + 1 - dXAxisTitleThickness);
		return dY;
	}

	// Returns the tick size according to the dpi
	public double getTickSize() {
		return IConstants.TICK_SIZE / 72d * ids.getDpiResolution();
	}

	boolean isSharedScale() {
		return rtc.getSharedScale() != null && rtc.getSharedScale().isShared();
	}

	protected abstract Object getMinMax(Axis ax, int iType) throws ChartException, IllegalArgumentException;

	protected void initAlignZeroHelper() {
		azHelper = null;
		azHelper = AlignZeroHelper.getInstance(this);
	}

	@Override
	public ChartWithAxes getModel() {
		return (ChartWithAxes) cm;
	}

	static final Double asDouble(Object o) {
		return Methods.asDouble(o);
	}

	static final CDateTime asDateTime(Object o) {
		return Methods.asDateTime(o);
	}

	static final int asInteger(Object o) {
		return ((Number) o).intValue();
	}

	static final int getLabelPosition(Position lp) {
		return Methods.getLabelPosition(lp);
	}

	static final double getLocation(AutoScale sc, double dValue) throws IllegalArgumentException {
		return Methods.getLocation(sc, dValue);
	}

	static final double getLocation(AutoScale sc, Object oValue) throws ChartException, IllegalArgumentException {
		return Methods.getLocation(sc, oValue);
	}

	protected static class AlignZeroHelper {

		private final Map<Axis, double[]> minMaxLookup;

		private AlignZeroHelper(Map<Axis, double[]> minMaxLookup) {
			this.minMaxLookup = minMaxLookup;
		}

		public static AlignZeroHelper getInstance(PlotWithAxes pwa) {
			Map<Axis, double[]> minMaxLookup = new HashMap<>(2);
			Axis[] axList = pwa.getModel().getOrthogonalAxes(pwa.getModel().getPrimaryBaseAxes()[0], true);

			boolean bAnyPositive = false;
			boolean bAnyNegative = false;

			for (Axis ax : axList) {
				if (ax.getType().getValue() == AxisType.LINEAR && ax.isAligned()) {
					try {
						double[] minmax = (double[]) pwa.getMinMax(ax, IConstants.NUMERICAL | IConstants.LINEAR);
						if (minmax[0] > 0) {
							minmax[0] = 0;
						}
						if (minmax[1] < 0) {
							minmax[1] = 0;
						}
						minMaxLookup.put(ax, minmax);

						bAnyPositive |= minmax[1] > 0d;
						bAnyNegative |= minmax[0] < 0d;

					} catch (Exception e) {

					}

				}

			}

			if (bAnyPositive && bAnyNegative && minMaxLookup.size() > 1) {
				for (double[] minmax : minMaxLookup.values()) {
					applyRateToMinmax(minmax);
				}

				return new AlignZeroHelper(minMaxLookup);
			}

			return null;
		}

		private static void applyRateToMinmax(double[] minmax) {
			if (minmax[0] >= 0) {
				// positive only
				minmax[0] = -minmax[1];
			} else if (minmax[1] <= 0) {
				minmax[1] = -minmax[0];
			} else if (minmax[1] < -minmax[0]) {
				minmax[1] = -minmax[0];
			} else if (minmax[1] > -minmax[0]) {
				minmax[0] = -minmax[1];
			}
		}

		public double[] getCachedMinMax(Axis ax) {
			return minMaxLookup.get(ax);
		}

	}
}
