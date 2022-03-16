/***********************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.EllipsisHelper;
import org.eclipse.birt.chart.computation.IChartComputation;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.PlotComputation;
import org.eclipse.birt.chart.computation.Point;
import org.eclipse.birt.chart.computation.Rectangle;
import org.eclipse.birt.chart.computation.RotatedRectangle;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.factory.RunTimeContext.StateKey;
import org.eclipse.birt.chart.internal.factory.DateFormatWrapperFactory;
import org.eclipse.birt.chart.internal.factory.IDateFormatWrapper;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.data.BigNumberDataElement;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.impl.BigNumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.BigNumber;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.ChartUtil.CacheDateFormat;
import org.eclipse.birt.chart.util.ChartUtil.CacheDecimalFormat;
import org.eclipse.birt.chart.util.NumberUtil;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * Encapsulates the auto scaling algorithms used by the rendering and chart
 * computation framework.
 */
public final class AutoScale extends Methods implements Cloneable {

	private AxisLabelInfo axisLabelInfo = new AxisLabelInfo();

	AxisLabelInfo getAxisLabelInfo() {
		return axisLabelInfo;
	}

	/**
	 * This class stores middle values of axis label in runtime, including the size
	 * of axis label. To ensure chart series can be shown, if chart is flip and
	 * category axis label is very long, we will reduce the length of axis label,
	 * current rule is length of axis label can't exceed half of chart width.
	 */
	static class AxisLabelInfo {
		/**
		 * Actual size of axis label.
		 */
		double dActualSize = -1;
		/**
		 * Limit size of axis label.
		 */
		double dMaxSize = -1;

		/**
		 * Selects a valid label size from actual size and specified size.
		 *
		 * @param refSize
		 * @return
		 */
		double getValidSize(double refSize) {
			if (dActualSize >= 0 && dActualSize < refSize) {
				return dActualSize;
			}
			return refSize;
		}
	}

	public static class ScaleInfo {

		public final PlotComputation plotComp;
		public final OneAxis oax;
		public final int type;
		private final RunTimeContext rtc;
		private final CacheDecimalFormat cacheNumFormat;
		private final CacheDateFormat cacheDateFormat;
		private final IChartComputation cComp;
		private final FormatSpecifier fs;
		private final boolean bAxisLabelStaggered;
		private final int iLabelShowingInterval;
		private final boolean bTickBetweenCategories;
		private final boolean bAlignZero;
		private final boolean bLabelWithinAxes;
		private final boolean bCategoryScale;
		public final int iScaleDirection;
		private final boolean bExpandMinMax;

		private int iMarginPercent = 0;
		private double dZoomFactor = 1.0;
		private double dPrecision = 0;
		private int iMinUnit = 0;
		private double dFactor = -1;
		private DataSetIterator dsiData;

		private boolean bStepFixed = false;
		public boolean bMaximumFixed = false;
		private boolean bMinimumFixed = false;
		private Object oMinimumFixed;
		private Object oMaximumFixed;

		public ScaleInfo(PlotComputation plotComp, int iType, RunTimeContext rtc, FormatSpecifier fs, OneAxis ax,
				int iScaleDirection, boolean bExpandMinMax) {
			this.plotComp = plotComp;
			this.oax = ax;
			this.type = iType;
			this.rtc = rtc;
			this.fs = fs;
			this.iScaleDirection = iScaleDirection;
			this.bExpandMinMax = bExpandMinMax;

			cacheNumFormat = new ChartUtil.CacheDecimalFormat(rtc.getULocale());
			cacheDateFormat = new ChartUtil.CacheDateFormat(rtc.getULocale());
			cComp = rtc.getState(StateKey.CHART_COMPUTATION_KEY);

			if (ax != null) {
				bAxisLabelStaggered = ax.isAxisLabelStaggered();
				iLabelShowingInterval = ax.getLableShowingInterval();
				bTickBetweenCategories = ax.isTickBwtweenCategories();
				bAlignZero = ax.getModelAxis().isAligned();
				bLabelWithinAxes = ax.getModelAxis().isLabelWithinAxes();
				bCategoryScale = (iType & TEXT) == TEXT || ax.isCategoryScale();
			} else {
				bAxisLabelStaggered = false;
				iLabelShowingInterval = 0;
				bTickBetweenCategories = true;
				bAlignZero = false;
				bLabelWithinAxes = false;
				bCategoryScale = false;
			}
		}

		public ScaleInfo iMarginPercent(int iMarginPercent) {
			this.iMarginPercent = iMarginPercent;
			return this;
		}

		public ScaleInfo dZoomFactor(double dZoomFactor) {
			this.dZoomFactor = dZoomFactor;
			return this;
		}

		public ScaleInfo dPrecision(double dPrecision) {
			this.dPrecision = dPrecision;
			return this;
		}

		public ScaleInfo iMinUnit(int iMinUnit) {
			this.iMinUnit = iMinUnit;
			return this;
		}

		public ScaleInfo dFactor(double dFactor) {
			this.dFactor = dFactor;
			return this;
		}

		public ScaleInfo dsiData(DataSetIterator dsiData) {
			this.dsiData = dsiData;
			return this;
		}

		public ScaleInfo bMaximumFixed(boolean bMaximumFixed) {
			this.bMaximumFixed = bMaximumFixed;
			return this;
		}

		public ScaleInfo bMinimumFixed(boolean bMinimumFixed) {
			this.bMinimumFixed = bMinimumFixed;
			return this;
		}

		public ScaleInfo oMinimumFixed(Object oMinimumFixed) {
			this.oMinimumFixed = oMinimumFixed;
			return this;
		}

		public ScaleInfo oMaximumFixed(Object oMaximumFixed) {
			this.oMaximumFixed = oMaximumFixed;
			return this;
		}

		public ScaleInfo bStepFixed(boolean bStepFixed) {
			this.bStepFixed = bStepFixed;
			return this;
		}
	}

	/**
	 * This class computes and provides the tick values of axis.
	 */
	private static class AxisValueProvider {
		double dAxisValue;
		double dAxisStep;
		BigDecimal bdAxisValue;
		BigDecimal bdAxisStep;
		DecimalFormat df = null;
		private AutoScale as;
		private BigDecimal bdStep;
		private ScaleInfo si;

		AxisValueProvider(double dAxisValue, double dAxisStep, AutoScale as, ScaleInfo si) {
			this.dAxisValue = dAxisValue;
			this.dAxisStep = dAxisStep;
			this.as = as;
			this.si = si;
			if (as.isBigNumber()) {
				this.bdAxisValue = BigDecimal.valueOf(dAxisValue).multiply(as.getBigNumberDivisor(),
						NumberUtil.DEFAULT_MATHCONTEXT);
				this.bdAxisStep = BigDecimal.valueOf(dAxisStep);
				this.bdStep = bdAxisStep.multiply(as.getBigNumberDivisor(), NumberUtil.DEFAULT_MATHCONTEXT);
			}
		}

		/**
		 * Return tick value object.
		 *
		 * @param nde
		 * @return
		 */
		Object getValue(NumberDataElement nde) {
			if (this.as.isBigNumber()) {
				return this.bdAxisValue;
			} else {
				nde.setValue(this.dAxisValue);
				return nde;
			}
		}

		/**
		 * This is invoked to compute the next tick value under LINEAR case.
		 */
		void addStep() {
			if (!this.as.isBigNumber()) {
				dAxisValue += dAxisStep;
			} else {
				bdAxisValue = bdAxisValue.add(bdStep);
			}
		}

		/**
		 * This is called to compute the next tick value under LOGARITHMIC case.
		 */
		void mutltiplyStep() {
			if (!this.as.isBigNumber()) {
				dAxisValue *= dAxisStep;
			} else {
				bdAxisValue = bdAxisValue.multiply(this.bdAxisStep, NumberUtil.DEFAULT_MATHCONTEXT);
			}
		}

		DecimalFormat getDecimalFormat() {
			if (this.si.fs == null) // CREATE IF FORMAT SPECIFIER IS UNDEFINED
			{
				if (!as.isBigNumber()) {
					this.df = as.computeDecimalFormat(dAxisValue, dAxisStep);
				} else {

					this.df = as.computeDecimalFormat(
							bdAxisValue.multiply(as.getBigNumberDivisor(), NumberUtil.DEFAULT_MATHCONTEXT), bdStep);
				}
			}
			return this.df;
		}

		/**
		 * Formats specified value according to specified formatter.
		 *
		 * @param oValue
		 * @param fs
		 * @param lcl
		 * @param oCachedJavaFormatter
		 * @param logger
		 * @return
		 */
		static String format(Object oValue, FormatSpecifier fs, ULocale lcl, Object oCachedJavaFormatter,
				ILogger logger) {
			String sText = NULL_STRING;
			try {
				sText = ValueFormatter.format(oValue, fs, lcl, oCachedJavaFormatter);
			} catch (ChartException dfex) {
				logger.log(dfex);
			}
			return sText;
		}

		/**
		 * Returns an actual value object according to current argument and contenxt.
		 *
		 * @param originValue
		 * @param divisor     the divisor used for big number case.
		 * @return
		 */
		static Object getValue(Object originValue, BigDecimal divisor) {
			if (originValue instanceof Number) {
				if (divisor != null) {
					return NumberUtil.asBigDecimal((Number) originValue).multiply(divisor,
							NumberUtil.DEFAULT_MATHCONTEXT);
				}
				return ((Number) originValue).doubleValue();
			} else if (originValue instanceof NumberDataElement) {
				Double d = new Double(((NumberDataElement) originValue).getValue());
				if (divisor != null) {
					return NumberUtil.asBigDecimal(d).multiply(divisor, NumberUtil.DEFAULT_MATHCONTEXT);
				}
				return d;
			} else if (originValue instanceof BigNumberDataElementImpl) {
				return ((BigNumberDataElementImpl) originValue).getValue();
			}
			return originValue;
		}
	}

	private final ScaleInfo info;
	private ScaleContext context;

	public AutoScale(ScaleInfo info) {
		this(info, new ScaleContext(info.iMarginPercent, info.type));
	}

	public AutoScale(ScaleInfo info, ScaleContext context) {
		this.info = info;
		this.context = context;
	}

	public static String KEY_SHARED_MINMAX = "SharedMinMax"; //$NON-NLS-1$

	private double dStartShift;

	private double dEndShift;

	private double dStart, dEnd;

	private AxisTickCoordinates atcTickCoordinates;

	private LabelVisibleHelper labelVisHelper = null;

	private Map<Integer, String> hmComputedLabelText = null;

	private StaggeredHelper staggeredHelper = null;

	private ScaleContext tmpSC;

	/** Indicates the max boundary of axis ticks. */
	private static final int TICKS_MAX = 1000;

	private static final NumberFormat dfDoulbeNormalized = NumberFormat.getInstance(Locale.ENGLISH);

	static {
		try {
			((DecimalFormat) dfDoulbeNormalized).applyPattern(".###############E0"); //$NON-NLS-1$
		} catch (ClassCastException e) {
		}
	}

	/**
	 * Quick static lookup for linear scaling
	 */
	// private static int[] iaLinearDeltas = { 1, 2, 5 };
	private static int[] iaLinearDeltas = { 1, 2, 5, 10 };

	/**
	 * Quick static lookup for logarithmic scaling
	 */
	// private static int[] iaLogarithmicDeltas = { 2, 4, 5, 10 };
	private static int[] iaLogarithmicDeltas = { 10 };

	/**
	 * Quick static lookup for datetime scaling
	 */
	private static int[] iaCalendarUnits = { Calendar.SECOND, Calendar.MINUTE, Calendar.HOUR_OF_DAY, Calendar.DATE,
			Calendar.MONTH, Calendar.YEAR, CDateTime.QUARTER, };

	private static int[] iaSecondDeltas = { 1, 5, 10, 15, 20, 30 };

	private static int[] iaMinuteDeltas = { 1, 5, 10, 15, 20, 30 };

	private static int[] iaHourDeltas = { 1, 2, 3, 4, 12 };

	private static int[] iaDayDeltas = { 1, 7, 14 };

	private static int[] iaMonthDeltas = { 1, 2, 3, 4, 6 };

	private static int[][] iaCalendarDeltas = { iaSecondDeltas, iaMinuteDeltas, iaHourDeltas, iaDayDeltas,
			iaMonthDeltas, null, null };

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/computation.withaxes"); //$NON-NLS-1$
	private boolean bIsBigNumber;
	private BigDecimal bigNumberDivisor;

	/**
	 * Returns the scale direction.
	 *
	 * @return direction
	 */
	public int getDirection() {
		return info.iScaleDirection;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		final AutoScale sc = new AutoScale(info);
		sc.context = context.copy();

		sc.dStart = dStart;
		sc.dEnd = dEnd;
		sc.dStartShift = dStartShift;
		sc.dEndShift = dEndShift;

		sc.atcTickCoordinates = atcTickCoordinates;
		sc.labelVisHelper = labelVisHelper;
		sc.staggeredHelper = staggeredHelper;
		sc.hmComputedLabelText = hmComputedLabelText;
		sc.tmpSC = tmpSC;
		sc.axisLabelInfo = axisLabelInfo;
		sc.setBigNubmerDivisor(getBigNumberDivisor());

		return sc;
	}

	/**
	 * Zooms IN 'once' into a scale of type numerical or datetime Typically, this is
	 * called in a loop until label overlaps occur
	 */
	public boolean zoomIn() {
		if (info.bStepFixed || ChartUtil.mathEqual(0, ((Number) context.getStep()).doubleValue(), isBigNumber())) {
			return false; // CANNOT ZOOM ANY MORE
		}

		if ((info.type & NUMERICAL) == NUMERICAL) {
			if ((info.type & LOGARITHMIC) == LOGARITHMIC) {
				final double dStep = asDouble(context.getStep()).doubleValue();
				if ((Math.log(dStep) / LOG_10) > 1) {
					setStep(new Double(dStep / 10));
				} else {
					int n = iaLogarithmicDeltas.length;
					for (int i = n - 1; i >= 0; i--) {
						if ((int) dStep == iaLogarithmicDeltas[i]) {
							if (i > 0) {
								setStep(new Double(iaLogarithmicDeltas[i - 1]));
								return true;
							}
							return false;
						}
					}
					return false;
				}
			} else if ((info.type & LINEAR) == LINEAR) {
				double dStep = asDouble(context.getStep()).doubleValue();
				final double oldStep = dStep;

				double dPower = (Math.log(dStep) / LOG_10);
				dPower = Math.floor(dPower);
				dPower = Math.pow(10.0, dPower);
				dStep /= dPower;
				dStep = Math.round(dStep);
				int n = iaLinearDeltas.length;
				for (int i = 0; i < n; i++) {
					if ((int) dStep == iaLinearDeltas[i]) {
						if (i > 0) {
							dStep = iaLinearDeltas[i - 1] * dPower;
						} else {
							dPower /= 10;
							dStep = iaLinearDeltas[n - 2] * dPower;
						}
						break;
					}
				}
				// To prevent endless loop if step is not changed
				if (ChartUtil.mathEqual(dStep, oldStep, isBigNumber())) {
					dStep /= 2;
				}
				setStep(new Double(dStep));

				if (((Number) context.getStep()).doubleValue() < info.dPrecision) {
					setStep(oldStep);
					return false; // CANNOT ZOOM ANY MORE
				}
			}

		} else if ((info.type & DATE_TIME) == DATE_TIME) {
			int[] ia = null;
			int iStep = asInteger(context.getStep());
			int iUnit = asInteger(context.getUnit());

			for (int icu = 0; icu < iaCalendarUnits.length; icu++) {
				if (iUnit == iaCalendarUnits[icu]) {
					ia = iaCalendarDeltas[icu];
					if (ia == null) // HANDLE YEARS SEPARATELY
					{
						iStep--;
						if (iStep == 0) {
							setStep(Integer.valueOf(iaMonthDeltas[iaMonthDeltas.length - 1]));
							context.setUnit(Integer.valueOf(Calendar.MONTH));
						}
					} else
					// HANDLE SECONDS, MINUTES, HOURS, DAYS, MONTHS
					{
						int i = 0;
						for (; i < ia.length; i++) {
							if (ia[i] == iStep) {
								break;
							}
						}

						if (i == 0) // WE'RE AT THE FIRST ELEMENT IN THE
						// DELTAS ARRAY
						{
							// #217377
							if (icu <= info.iMinUnit) {
								return false; // CAN'T ZOOM ANYMORE THAN
							}
							// 1-SECOND INTERVALS (AT INDEX=0)
							ia = iaCalendarDeltas[icu - 1]; // DOWNGRADE ARRAY
							// TO PREVIOUS
							// DELTAS ARRAY
							i = ia.length; // MANIPULATE OFFSET TO END+1
							context.setUnit(Integer.valueOf(iaCalendarUnits[icu - 1])); // DOWNGRADE
							// UNIT
						}
						setStep(Integer.valueOf(ia[i - 1])); // RETURN
						// PREVIOUS
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
	 * Zooms OUT 'once' into a scale of type numerical or datetime Typically, this
	 * is called in a loop until label overlaps occur
	 */
	public boolean zoomOut() {
		// Fix Bugzilla#220710 to avoid useless zoom out
		if (info.bStepFixed || this.getTickCordinates().size() < 3) {
			return false;
		}

		Object oStep = context.getStep();

		if (((Number) oStep).doubleValue() >= Double.MAX_VALUE) {
			return false; // CANNOT ZOOM ANY MORE
		}

		if ((info.type & NUMERICAL) == NUMERICAL) {
			if ((info.type & LOGARITHMIC) == LOGARITHMIC) {
				final double dStep = asDouble(oStep).doubleValue();
				if ((Math.log(dStep) / LOG_10) >= 1) {
					setStep(new Double(dStep * 10));
				} else {
					final int n = iaLogarithmicDeltas.length;
					for (int i = 0; i < n; i++) {
						if ((int) dStep == iaLogarithmicDeltas[i]) {
							setStep(new Double(iaLogarithmicDeltas[i + 1]));
							return true;
						}
					}
					return false;
				}
			} else if ((info.type & LINEAR) == LINEAR) {
				// 269641 the minimum tick number may be 3 in this case.
				if (asDouble(context.getMin()).doubleValue() * asDouble(context.getMax()).doubleValue() < 0
						&& this.getTickCordinates().size() <= 3) {
					return false;
				}
				double dStep = asDouble(oStep).doubleValue();
				double dPower = Math.log10(dStep);

				if (dPower < 0) {
					dPower = Math.floor(dPower);
					dPower = Math.pow(10, dPower);
				} else {
					dPower = dStep;
				}

				dStep /= dPower;
				dStep = Math.round(dStep);
				int n = iaLinearDeltas.length;
				int i = 0;

				for (; i < n; i++) {
					if (dStep < iaLinearDeltas[i]) {
						dStep = iaLinearDeltas[i] * dPower;
						break;
					}
				}
				if (i == n) {
					dPower *= 20;
					dStep = iaLinearDeltas[0] * dPower;
				}

				if (ChartUtil.mathEqual(((Number) oStep).doubleValue(), dStep, isBigNumber())) {
					// Can not zoom any more, result is always the same;
					return false;
				}

				dStep = ChartUtil.alignWithInt(dStep, false);
				setStep(new Double(dStep));
			}
		} else if ((info.type & DATE_TIME) == DATE_TIME) {
			int[] ia = null;
			int iStep = asInteger(oStep);
			int iUnit = asInteger(context.getUnit());

			for (int icu = 0; icu < iaCalendarUnits.length; icu++) {
				if (iUnit == iaCalendarUnits[icu]) {
					ia = iaCalendarDeltas[icu];
					if (ia == null) // HANDLE YEARS SEPARATELY
					{
						iStep++; // NO UPPER LIMIT FOR YEARS
						setStep(Integer.valueOf(iStep));
					} else
					// HANDLE SECONDS, MINUTES, HOURS, DAYS, MONTHS
					{
						int i = 0, n = ia.length;
						for (; i < n; i++) {
							if (ia[i] == iStep) {
								break;
							}
						}

						if (i == n - 1) // WE'RE AT THE LAST ELEMENT IN THE
						// DELTAS ARRAY
						{
							ia = iaCalendarDeltas[icu + 1]; // UPGRADE UNIT TO
							// NEXT DELTAS ARRAY
							context.setUnit(Integer.valueOf(iaCalendarUnits[icu + 1]));
							if (ia == null) // HANDLE YEARS
							{
								setStep(Integer.valueOf(1));
								return true;
							}
							i = -1; // MANIPULATE OFFSET TO START-1
						}
						setStep(Integer.valueOf(ia[i + 1])); // RETURN NEXT
						// STEP
						// IN
						// DELTAS ARRAY
						break;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Returns an auto computed decimal format pattern for representing axis labels
	 * on a numeric axis
	 *
	 * @return pattern
	 */
	public String getNumericPattern() {
		Object oStep = context.getStep();
		if (context.getMin() == null || oStep == null) {
			return "0.00"; //$NON-NLS-1$
		}

		Number nMinValue = (Number) context.getMin();
		Number nStep = (Number) oStep;

		if ((info.type & LOGARITHMIC) == LOGARITHMIC) {
			return ValueFormatter.getNumericPattern(nMinValue);
		}
		return ValueFormatter.getNumericPattern(nStep);
	}

	public int getType() {
		return info.type;
	}

	/**
	 *
	 * @param _oaData
	 */
	public void setData(DataSetIterator _oaData) {
		info.dsiData(_oaData);
	}

	public FormatSpecifier getFormatSpecifier() {
		return info.fs;
	}

	public Object getUnit() {
		return context.getUnit();
	}

	public DataSetIterator getData() {
		return info.dsiData;
	}

	void setTickCordinates(AxisTickCoordinates atc) {
		// if the factor is set, the tick number can be 1 if the step size is
		// large.
		if (atc != null && atc.size() == 1 && !isSetFactor()) {
			throw new RuntimeException(
					new ChartException(ChartEnginePlugin.ID, ChartException.COMPUTATION, "exception.tick.computations", //$NON-NLS-1$
							Messages.getResourceBundle(info.rtc.getULocale())));
		}
		this.atcTickCoordinates = atc;
	}

	public boolean isTickLabelVisible(int index) {
		assert labelVisHelper != null;
		return labelVisHelper.isTickLabelVisible(index);
	}

	public String getComputedLabelText(int index) {
		assert hmComputedLabelText != null;
		return hmComputedLabelText.get(index);
	}

	/**
	 * Returns a list of all visible indexes, in the moment works only for category.
	 *
	 * @return id
	 */
	public Collection<Integer> getVisibleLabelIds() {
		return hmComputedLabelText.keySet();
	}

	public boolean isTickLabelStaggered(int index) {
		if (staggeredHelper == null) {
			staggeredHelper = StaggeredHelper.createInstance(isAxisLabelStaggered(), atcTickCoordinates.size(),
					info.iLabelShowingInterval);
		}
		return staggeredHelper.isTickLabelStaggered(index);
	}

	public boolean isAxisLabelStaggered() {
		return info.bAxisLabelStaggered;
	}

	public boolean isTickBetweenCategories() {
		return info.bTickBetweenCategories;
	}

	public AxisTickCoordinates getTickCordinates() {
		return this.atcTickCoordinates;
	}

	/**
	 * Returns the normalized start point. always be Zero.
	 *
	 * @return start point
	 */
	public double getNormalizedStart() {
		return 0;
	}

	/**
	 * Returns the normalized end point. this will be the (original end - original
	 * start).
	 *
	 * @return end point
	 */
	public double getNormalizedEnd() {
		return dEnd - dStart;
	}

	/**
	 * Returns the normalized start and end point.
	 *
	 * @return start and end point
	 */
	public double[] getNormalizedEndPoints() {
		return new double[] { 0, dEnd - dStart };
	}

	public double[] getEndPoints() {
		return new double[] { dStart, dEnd };
	}

	/**
	 *
	 * @param _dStart
	 * @param _dEnd
	 */
	void setEndPoints(double _dStart, double _dEnd) {
		if (_dStart != -1) {
			dStart = _dStart;
		}

		if (_dEnd != -1) {
			dEnd = _dEnd;
		}

		if (atcTickCoordinates != null) {
			atcTickCoordinates.setEndPoints(dStart, dEnd);
		}
	}

	private void checkValible(double dValue, String sName) throws ChartException {
		if (Double.isInfinite(dValue)) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
					sName + Messages.getString("AutoScale.Exception.IsInfiite"), //$NON-NLS-1$
					Messages.getResourceBundle(info.rtc.getULocale()));
		}

		if (Double.isNaN(dValue)) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
					sName + Messages.getString("AutoScale.Exception.IsNaN"), //$NON-NLS-1$
					Messages.getResourceBundle(info.rtc.getULocale()));
		}
	}

	/**
	 * Computes tick count
	 *
	 * @return tick count
	 */
	public int getTickCount() throws ChartException {
		if (context.getStepNumber() != null) {
			if (info.bCategoryScale || (info.type & NUMERICAL) != NUMERICAL) {
				// Log the exception to notify only numeric value is supported
				logger.log(new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
						"exception.unsupported.step.number", //$NON-NLS-1$
						Messages.getResourceBundle(info.rtc.getULocale())));
			} else {
				return context.getStepNumber().intValue() + 1;
			}
		}

		Object oStep = context.getStep();
		int nTicks = 2;
		if (isCategoryScale()) {
			if (info.dsiData != null) {
				nTicks = info.dsiData.size() + 1;
				if (!info.bTickBetweenCategories) {
					nTicks++;
				}
			}
		} else if ((info.type & NUMERICAL) == NUMERICAL) {
			if ((info.type & LINEAR) == LINEAR) {
				double dMax = asDouble(context.getMax()).doubleValue();
				double dMin = asDouble(context.getMin()).doubleValue();
				double dStep = asDouble(oStep).doubleValue();

				if (!ChartUtil.mathEqual(dMax, dMin, isBigNumber())) {
					double lNTicks = Math.ceil((dMax - dMin) / dStep - 0.5) + 1;
					if ((lNTicks > TICKS_MAX) || (lNTicks < 2)) {
						if (lNTicks > TICKS_MAX) {
							nTicks = TICKS_MAX;
						} else { // dNTicks<2
							nTicks = 2;
						}
						// update the step size
						dStep = dMax / (nTicks - 1) - dMin / (nTicks - 1);
						checkValible(dStep, Messages.getString("AutoScale.ValueName.StepSize")); //$NON-NLS-1$
						dStep = ChartUtil.alignWithInt(dStep, true);
						setStep(new Double(dStep));
					} else {
						nTicks = (int) lNTicks;
					}
				} else {
					nTicks = 5;
				}
			} else if ((info.type & LOGARITHMIC) == LOGARITHMIC) {
				double dMax = asDouble(context.getMax()).doubleValue();
				double dMin = asDouble(context.getMin()).doubleValue();
				double dStep = asDouble(oStep).doubleValue();

				double dMaxLog = (Math.log(dMax) / LOG_10);
				double dMinLog = (Math.log(dMin) / LOG_10);
				double dStepLog = (Math.log(dStep) / LOG_10);

				nTicks = (int) Math.ceil((dMaxLog - dMinLog) / dStepLog) + 1;
			}
		} else if ((info.type & DATE_TIME) == DATE_TIME) {
			final CDateTime cdt1 = (CDateTime) context.getMin();
			final CDateTime cdt2 = (CDateTime) context.getMax();
			double diff = CDateTime.computeDifference(cdt2, cdt1, asInteger(context.getUnit())) / asInteger(oStep);

			nTicks = (int) Math.round(diff) + 1;
		} else {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
					"exception.unknown.axis.type.tick.computations", //$NON-NLS-1$
					Messages.getResourceBundle(info.rtc.getULocale()));
		}

		// at least 2 ticks
		if (nTicks < 2) {
			nTicks = 2;
		}

		return nTicks;
	}

	/**
	 * Returns the absolute value of the scale unit.
	 *
	 * @return unit size
	 */
	public double getUnitSize() {
		if (atcTickCoordinates == null) {
			throw new RuntimeException(
					new ChartException(ChartEnginePlugin.ID, ChartException.COMPUTATION, "exception.unit.size.failure", //$NON-NLS-1$
							Messages.getResourceBundle(info.rtc.getULocale())));
		}
		return Math.abs(atcTickCoordinates.getStep());
	}

	public Object getMinimum() {
		return context.getMin();
	}

	/**
	 * @param o
	 */
	public void setMinimum(Object o) {
		context.setMin(o);
	}

	public Object getMaximum() {
		return context.getMax();
	}

	/**
	 * @param o
	 */
	public void setMaximum(Object o) {
		context.setMax(o);
	}

	/**
	 *
	 * @return step size
	 */
	public Object getStep() {
		return context.getStep();
	}

	/**
	 * @param o
	 */
	public void setStep(Object o) {
		context.setStep(o);
	}

	/**
	 *
	 * @return step number
	 */
	public Integer getStepNumber() {
		return context.getStepNumber();
	}

	public void setStepNumber(Integer o) {
		context.setStepNumber(o);
	}

	public double getFactor() {
		return info.dFactor;
	}

	public boolean isSetFactor() {
		return info.dFactor < 0 ? false : true;
	}

	/**
	 *
	 * @return
	 */
	Object[] getMinMax() throws ChartException {
		Object oValue = null;
		try {
			if ((info.type & NUMERICAL) == NUMERICAL) {

				double dValue, dMinValue = Double.MAX_VALUE, dMaxValue = -Double.MAX_VALUE;
				info.dsiData.reset();
				while (info.dsiData.hasNext()) {
					oValue = info.dsiData.next();
					if (oValue == null) // NULL VALUE CHECK
					{
						continue;
					}
					if (NumberUtil.isBigNumber(oValue)) {
						dValue = ((BigNumber) oValue).doubleValue();
					} else {
						dValue = ((Double) oValue).doubleValue();
					}
					if (dValue < dMinValue) {
						dMinValue = dValue;
					}
					if (dValue > dMaxValue) {
						dMaxValue = dValue;
					}
				}

				return new Object[] { new Double(dMinValue), new Double(dMaxValue) };
			} else if ((info.type & DATE_TIME) == DATE_TIME) {
				Calendar cValue;
				Calendar caMin = null, caMax = null;
				info.dsiData.reset();
				while (info.dsiData.hasNext()) {
					oValue = info.dsiData.next();
					cValue = (Calendar) oValue;
					if (caMin == null) {
						caMin = cValue;
					}
					if (caMax == null) {
						caMax = cValue;
					}
					if (cValue == null) // NULL VALUE CHECK
					{
						continue;
					}
					if (cValue.before(caMin)) {
						caMin = cValue;
					} else if (cValue.after(caMax)) {
						caMax = cValue;
					}
				}
				return new Object[] { new CDateTime(caMin), new CDateTime(caMax) };
			}
		} catch (ClassCastException ex) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
					"exception.invalid.axis.data.type", //$NON-NLS-1$
					new Object[] { oValue }, Messages.getResourceBundle(info.rtc.getULocale()));
		}
		return null;
	}

	/**
	 * Computes min, max value, step size and step number of the Axis
	 *
	 * @param oMinValue min value in data points. Double or CDateTime type.
	 * @param oMaxValue max value in data points. Double or CDateTime type.
	 */
	public void updateAxisMinMax(Object oMinValue, Object oMaxValue) {
		// Use the shared context if it's shared
		if (info.rtc.getSharedScale() != null && info.rtc.getSharedScale().isShared()) {
			updateContext(info.rtc.getSharedScale().getScaleContext());
			return;
		}

		ScaleContext sct;
		if ((info.type & LOGARITHMIC) == LOGARITHMIC) {
			if ((info.type & PERCENT) == PERCENT) {
				context.setMax(Double.valueOf(100));
				context.setMin(Double.valueOf(1));
				setStep(new Double(10));
				info.bMaximumFixed(true);
				info.bMinimumFixed(true);
				info.bStepFixed(true);
				return;
			}

			sct = new ScaleContext(info.iMarginPercent, info.type, oMinValue, oMaxValue, context.getStep());
		} else if ((info.type & DATE_TIME) == DATE_TIME) {
			int iUnit = asInteger(context.getUnit());
			sct = new ScaleContext(info.iMarginPercent, info.type, iUnit, oMinValue, oMaxValue, context.getStep());
		} else {
			// Linear axis type
			sct = new ScaleContext(info.iMarginPercent, info.type, oMinValue, oMaxValue, context.getStep());
		}

		if ((info.type & DATE_TIME) == DATE_TIME) {
			// Bugzilla#217044
			sct.setFixedValue(info.bMinimumFixed, info.bMaximumFixed, info.oMinimumFixed, info.oMaximumFixed);
		} else {
			sct.setFixedValue(info.bMinimumFixed, info.bMaximumFixed, context.getMin(), context.getMax());
		}
		sct.setExpandMinmax(info.bExpandMinMax);
		sct.setFixedStep(info.bStepFixed, context.getStepNumber());
		sct.computeMinMax(info.bAlignZero);
		updateContext(sct);

		// Temporal scale for later used in shared scale
		tmpSC = sct;
		updateSharedScaleContext(info.rtc, info.type, tmpSC);
	}

	private void updateContext(ScaleContext sct) {
		context.setMax(sct.getMax());
		context.setMin(sct.getMin());
		context.setMaxWithMargin(sct.getMaxWithMargin());
		context.setMinWithMargin(sct.getMinWithMargin());
		context.setStep(sct.getStep());
		context.setUnit(sct.getUnit());
	}

	Object getMinWithMargin() {
		return context.getMinWithMargin();
	}

	Object getMaxWithMargin() {
		return context.getMaxWithMargin();
	}

	/**
	 * Checks all labels for any overlap for a given axis' scale
	 *
	 * @param la
	 * @param iLabelLocation
	 *
	 * @return fit or not
	 */
	public boolean checkFit(IDisplayServer xs, Label la, int iLabelLocation) throws ChartException {
		if (isCategoryScale()) {
			// not for text and category style
			return true;
		}

		final double dAngleInDegrees = la.getCaption().getFont().getRotation();
		double x = 0, y = 0;
		int iPointToCheck = 0;
		if (iLabelLocation == ABOVE || iLabelLocation == BELOW) {
			if (info.iScaleDirection == BACKWARD) {
				iPointToCheck = (dAngleInDegrees < 0 && dAngleInDegrees > -90) ? 1 : 2;
			} else {
				iPointToCheck = (dAngleInDegrees < 0 && dAngleInDegrees > -90) ? 3 : 0;
			}
		} else if (iLabelLocation == LEFT || iLabelLocation == RIGHT) {
			if (info.iScaleDirection == FORWARD) {
				iPointToCheck = (dAngleInDegrees < 0 && dAngleInDegrees > -90) ? 0 : 1;
			} else {
				iPointToCheck = (dAngleInDegrees < 0 && dAngleInDegrees > -90) ? 2 : 3;
			}
		}
		AxisTickCoordinates da = atcTickCoordinates;
		RotatedRectangle rrPrev = null, rrPrev2 = null, rr;
		Double fontHeight = info.cComp.computeFontHeight(xs, la);

		if ((info.type & (NUMERICAL | LINEAR)) == (NUMERICAL | LINEAR)) {
			double dAxisValue = asDouble(getMinimum()).doubleValue();
			final double dAxisStep = asDouble(getStep()).doubleValue();
			String sText;
			DecimalFormat df = null;
			AxisValueProvider avi = new AxisValueProvider(dAxisValue, dAxisStep, this, info);
			final NumberDataElement nde = NumberDataElementImpl.create(0);
			for (int i = 0; i < da.size(); i++) {
				df = avi.getDecimalFormat();
				Object value = avi.getValue(nde);
				sText = AxisValueProvider.format(value, info.fs, info.rtc.getULocale(), df, logger);

				if (iLabelLocation == ABOVE || iLabelLocation == BELOW) {
					x = da.getCoordinate(i) * info.dZoomFactor;
				} else if (iLabelLocation == LEFT || iLabelLocation == RIGHT) {
					y = da.getCoordinate(i) * info.dZoomFactor;
				}

				la.getCaption().setValue(sText);

				rr = info.cComp.computePolygon(xs, iLabelLocation, la, x, y, fontHeight);

				if (i == 0 && info.bLabelWithinAxes) {
					Rectangle rect = rr.getBounds();
					if (iLabelLocation == ABOVE || iLabelLocation == BELOW) {
						rr.shiftXVertices(rect.getWidth() / 2);
					} else {
						rr.shiftYVertices(-rect.getHeight() / 2);
					}
				}

				Point p = rr.getPoint(iPointToCheck);

				if (isAxisLabelStaggered() && isTickLabelStaggered(i)) {
					if (rrPrev2 != null && (rrPrev2.contains(p) || rrPrev2.getPoint(iPointToCheck).equals(p)
							|| ChartUtil.intersects(rr, rrPrev2))) {
						return false;
					}
					rrPrev2 = rr;
				} else {
					if (rrPrev != null && (rrPrev.contains(p) || rrPrev.getPoint(iPointToCheck).equals(p)
							|| ChartUtil.intersects(rr, rrPrev))) {
						return false;
					}
					rrPrev = rr;
				}

				avi.addStep();
			}
		} else if ((info.type & (NUMERICAL | LOGARITHMIC)) == (NUMERICAL | LOGARITHMIC)) {
			double dAxisValue = asDouble(getMinimum()).doubleValue();
			final double dAxisStep = asDouble(getStep()).doubleValue();
			String sText;
			NumberDataElement nde = NumberDataElementImpl.create(0);
			DecimalFormat df = null;
			AxisValueProvider avi = new AxisValueProvider(dAxisValue, dAxisStep, this, info);
			for (int i = 0; i < da.size() - 1; i++) {
				df = avi.getDecimalFormat();
				Object value = avi.getValue(nde);
				sText = AxisValueProvider.format(value, info.fs, info.rtc.getULocale(), df, logger);

				if (iLabelLocation == ABOVE || iLabelLocation == BELOW) {
					x = da.getCoordinate(i) * info.dZoomFactor;
				} else if (iLabelLocation == LEFT || iLabelLocation == RIGHT) {
					y = da.getCoordinate(i) * info.dZoomFactor;
				}

				la.getCaption().setValue(sText);
				rr = info.cComp.computePolygon(xs, iLabelLocation, la, x, y, fontHeight);

				Point p = rr.getPoint(iPointToCheck);

				if (isAxisLabelStaggered() && isTickLabelStaggered(i)) {
					if (rrPrev2 != null && (rrPrev2.contains(p) || rrPrev2.getPoint(iPointToCheck).equals(p)
							|| ChartUtil.intersects(rr, rrPrev2))) {
						return false;
					}
					rrPrev2 = rr;
				} else {
					if (rrPrev != null && (rrPrev.contains(p) || rrPrev.getPoint(iPointToCheck).equals(p))) {
						return false;
					}
					rrPrev = rr;
				}

				avi.mutltiplyStep();
			}
		} else if (info.type == DATE_TIME) {
			CDateTime cdt, cdtAxisValue = asDateTime(context.getMin());
			final int iUnit = asInteger(context.getUnit());
			final int iStep = asInteger(context.getStep());
			final IDateFormatWrapper sdf = DateFormatWrapperFactory.getPreferredDateFormat(iUnit,
					info.rtc.getULocale());

			String sText;
			cdt = cdtAxisValue;

			for (int i = 0; i < da.size() - 1; i++) {
				sText = ValueFormatter.format(cdt, info.fs, info.rtc.getULocale(), sdf);

				if (iLabelLocation == ABOVE || iLabelLocation == BELOW) {
					x = da.getCoordinate(i) * info.dZoomFactor;
				} else if (iLabelLocation == LEFT || iLabelLocation == RIGHT) {
					y = da.getCoordinate(i) * info.dZoomFactor;
				}

				la.getCaption().setValue(sText);
				rr = info.cComp.computePolygon(xs, iLabelLocation, la, x, y, fontHeight);

				if (i == 0 && info.bLabelWithinAxes) {
					Rectangle rect = rr.getBounds();
					if (iLabelLocation == ABOVE || iLabelLocation == BELOW) {
						rr.shiftXVertices(rect.getWidth() / 2);
					} else {
						rr.shiftYVertices(-rect.getHeight() / 2);
					}
				}

				Point p = rr.getPoint(iPointToCheck);

				if (isAxisLabelStaggered() && isTickLabelStaggered(i)) {
					if (rrPrev2 != null && (rrPrev2.contains(p) || rrPrev2.getPoint(iPointToCheck).equals(p)
							|| ChartUtil.intersects(rr, rrPrev2))) {
						return false;
					}
					rrPrev2 = rr;
				} else {
					if (rrPrev != null && (rrPrev.contains(p) || rrPrev.getPoint(iPointToCheck).equals(p))) {
						return false;
					}
					rrPrev = rr;
				}
				cdt = cdtAxisValue.forward(iUnit, iStep * (i + 1)); // ALWAYS
				// W.R.T
				// START
				// VALUE
			}
		}
		return true;
	}

	private CateLabVisTester createCateLabVisTester(IDisplayServer xs, Label la, int iLabelLocation)
			throws ChartException {
		// compute visiblility for category labels
		final double dAngleInDegrees = la.getCaption().getFont().getRotation();
		int iNewPointToCheck = 0, iPrevPointToCheck = 0;

		/*
		 * Rectangle points are layed out like this
		 *
		 * 0 1 NextLabel 3 2
		 *
		 * 0 1 0 1 PreviousLabel NextLabel 3 2 3 2
		 */
		boolean isNegativeRotation = (dAngleInDegrees < 0 && dAngleInDegrees > -90);
		switch (iLabelLocation) {
		case ABOVE:
			iNewPointToCheck = isNegativeRotation ? 3 : 0;
			iPrevPointToCheck = isNegativeRotation ? 1 : 3;
			break;
		case BELOW:
			iNewPointToCheck = isNegativeRotation ? (info.iScaleDirection == BACKWARD ? 1 : 3)
					: (info.iScaleDirection == BACKWARD ? 2 : 0);
			iPrevPointToCheck = isNegativeRotation ? (info.iScaleDirection == BACKWARD ? 2 : 0)
					: (info.iScaleDirection == BACKWARD ? 0 : 2);
			break;
		case LEFT:
			iNewPointToCheck = info.iScaleDirection == FORWARD ? 1 : 2;
			iPrevPointToCheck = info.iScaleDirection == FORWARD ? 2 : 1;
			break;
		case RIGHT:
			iNewPointToCheck = info.iScaleDirection == FORWARD ? 0 : 3;
			iPrevPointToCheck = info.iScaleDirection == FORWARD ? 3 : 0;
			break;
		}

		return new CateLabVisTester(iLabelLocation, iNewPointToCheck, iPrevPointToCheck, la, xs);
	}

	/**
	 * Calculates visibility for axis labels.
	 *
	 * @param xs
	 * @param la
	 * @param iLabelLocation
	 * @param iOrientation
	 * @throws ChartException
	 */
	protected void checkTickLabelsVisibility(IDisplayServer xs, Label la, int iLabelLocation, int iOrientation)
			throws ChartException {
		hmComputedLabelText = new HashMap<>();

		boolean vis = la.isVisible();
		if (!vis && info.rtc.getSharedScale() != null) {
			// In shared scale case, treat plot chart with invisible labels has
			// axis labels, so axis chart can have the same scale with plot
			// chart
			vis = true;
		}

		// initialize stagger state.
		boolean staggerEnabled = isAxisLabelStaggered();
		this.staggeredHelper = StaggeredHelper.createInstance(staggerEnabled, atcTickCoordinates.size(),
				info.iLabelShowingInterval);

		this.labelVisHelper = LabelVisibleHelper.createInstance(vis, isCategoryScale(), atcTickCoordinates.size(),
				info.iLabelShowingInterval);

		// all non-visible label, skip checking.
		if (!vis || !isCategoryScale()) {
			return;
		}

		// compute visibility for category labels
		DataSetIterator dsi = getData();
		dsi.reset();

		int iDateTimeUnit = IConstants.UNDEFINED;

		if (info.type == IConstants.DATE_TIME) {
			iDateTimeUnit = ChartUtil.computeDateTimeCategoryUnit(info.plotComp.getModel(), dsi);
		}

		CateLabVisTester tester = this.createCateLabVisTester(xs, la, iLabelLocation);
		EllipsisHelper eHelper = new EllipsisHelper(tester, la.getEllipsis());

		int start_id = isTickBetweenCategories() ? 0 : 1;
		RotatedRectangle rrPrev[] = new RotatedRectangle[2];

		double dStep = Math.abs(atcTickCoordinates.getStep() * info.dZoomFactor);
		int indexStep = dStep > 1 ? 1 : (int) (1d / dStep);
		int iSkip = indexStep - 1;

		double ellipsisWidth = 0;
		for (int i = start_id; i < atcTickCoordinates.size() - 1; i += indexStep) {
			Object oValue = null;

			if (iSkip > 0) {
				if (dsi.skip(iSkip) < iSkip) {
					break;
				}
			}

			if (dsi.hasNext()) {
				oValue = dsi.next();
			} else {
				break;
			}

			// only check visible labels.
			if (labelVisHelper.shouldTickLabelVisible(i)) {
				double x = 0, y = 0;
				String sText = formatCategoryValue(info.type, oValue, iDateTimeUnit);

				if (iLabelLocation == ABOVE || iLabelLocation == BELOW) {
					x = this.atcTickCoordinates.getCoordinate(i) * info.dZoomFactor;
				} else if (iLabelLocation == LEFT || iLabelLocation == RIGHT) {
					y = this.atcTickCoordinates.getCoordinate(i) * info.dZoomFactor;
				}

				la.getCaption().setValue(sText);
				RotatedRectangle rrCurr = null;

				int arrayIndex = isAxisLabelStaggered() && isTickLabelStaggered(i) ? 1 : 0;

				boolean bVis;
				if (rrPrev[arrayIndex] == null) {
					// Always show the first label.
					rrCurr = info.cComp.computePolygon(xs, iLabelLocation, la, x, y, null);
					bVis = true;
				} else {
					tester.setFPara(rrPrev[arrayIndex], x, y);
					bVis = eHelper.checkLabelEllipsis(sText, null);
					rrCurr = tester.getCurrentRR();
				}

				if (bVis) {
					labelVisHelper.addVisible(i);
					rrPrev[arrayIndex] = rrCurr;
					String str = la.getCaption().getValue();
					double rotation = la.getCaption().getFont().getRotation();
					if (iOrientation == VERTICAL && isCategoryScale() && axisLabelInfo.dMaxSize > 0
							&& Math.abs(rotation) <= 45) {
						double size = info.cComp.computeWidth(xs, la);

						// If the orientation is vertical and the vertical axis
						// is category axis(flip case), we will check if the
						// actual length of axis label exceeds half of chart
						// width and limit the length of axis label.
						if (ellipsisWidth <= 0) {
							la.getCaption().setValue(EllipsisHelper.ELLIPSIS_STRING);
							ellipsisWidth = info.cComp.computeWidth(xs, la);
						}

						if (ChartUtil.mathGT(size, axisLabelInfo.dMaxSize)) {
							int count = (int) (str.length() * (axisLabelInfo.dMaxSize - ellipsisWidth) / size);
							if (count >= 0) {
								hmComputedLabelText.put(i, str.substring(0, count) + EllipsisHelper.ELLIPSIS_STRING);
							} else {
								hmComputedLabelText.put(i, str);
							}
						} else {
							hmComputedLabelText.put(i, str);
						}
					} else {
						hmComputedLabelText.put(i, str);
					}
				}
			}
		}

	}

	private class CateLabVisTester implements EllipsisHelper.ITester {

		private RotatedRectangle rrPrev;
		private RotatedRectangle rrCurr;
		private int iLabelLocation;
		private double x;
		private double y;
		private int iNewPointToCheck;
		private int iPrevPointToCheck;
		private Label la;
		private IDisplayServer xs;
		private Double fontHeight = null;

		CateLabVisTester(int iLabelLocation, int iNewPointToCheck, int iPrevPointToCheck, Label la, IDisplayServer xs)
				throws ChartException {
			this.iLabelLocation = iLabelLocation;
			this.iNewPointToCheck = iNewPointToCheck;
			this.iPrevPointToCheck = iPrevPointToCheck;
			this.la = la;
			this.xs = xs;
			this.fontHeight = info.cComp.computeFontHeight(xs, la);
		}

		private void setFPara(RotatedRectangle rrPrev, double x, double y) {
			this.rrPrev = rrPrev;
			this.x = x;
			this.y = y;
		}

		private RotatedRectangle getCurrentRR() {
			return rrCurr;
		}

		@Override
		public boolean testLabelVisible(String sText, Object oPara) throws ChartException {
			la.getCaption().setValue(sText);

			Point previousPoint = rrPrev.getPoint(iPrevPointToCheck);
			// quick check for false (fast)
			if (quickCheckVisibility(iLabelLocation, previousPoint, x, y)) {
				// extensive check (expensive)
				rrCurr = info.cComp.computePolygon(xs, iLabelLocation, la, x, y, fontHeight);
				Point p = rrCurr.getPoint(iNewPointToCheck);

				boolean visible = !(rrPrev.contains(p) || ChartUtil.intersects(rrCurr, rrPrev));

				if (visible) {
					return true;
				}
			}
			return false;
		}

		@Override
		public double getHeight() {
			// not implemented
			return 0;
		}

		@Override
		public double getWidth() {
			// not implemented
			return 0;
		}
	}

	protected boolean quickCheckVisibility(int iLabelLocation, Point previousPoint, double x, double y) {

		// quick check first (fast)
		if (iLabelLocation == ABOVE || iLabelLocation == BELOW) {
			if ((info.iScaleDirection == BACKWARD && previousPoint.getX() < x)
					|| (info.iScaleDirection != BACKWARD && previousPoint.getX() > x)) {
				return false;
			}
		} else if (iLabelLocation == LEFT || iLabelLocation == RIGHT) {

			if ((info.iScaleDirection == FORWARD && previousPoint.getY() > y)
					|| (info.iScaleDirection != FORWARD && previousPoint.getY() < y)) {
				return false;
			}
		}
		return true;

	}

	/**
	 *
	 */
	void resetShifts() {
		dStartShift = 0;
		dEndShift = 0;
	}

	public double getStart() {
		return dStart;
	}

	public double getEnd() {
		return dEnd;
	}

	/**
	 *
	 * @return
	 */
	double getStartShift() {
		return dStartShift;
	}

	/**
	 *
	 * @return
	 */
	double getEndShift() {
		return dEndShift;
	}

	/**
	 *
	 * @param xs
	 * @param ax
	 * @param dsi
	 * @param iType
	 * @param dStart
	 * @param dEnd
	 * @param scModel
	 * @param fs
	 * @param rtc
	 * @param direction
	 * @param zoomFactor     1 is default factor
	 * @param iMarginPercent the percentage of margin area for display some charts,
	 *                       such as bubble. 0 means no margin
	 * @return AutoScale instance
	 * @throws ChartException
	 */
	static AutoScale computeScale(IDisplayServer xs, OneAxis ax, DataSetIterator dsi, int iType, double dStart,
			double dEnd, RunTimeContext rtc, int direction, double zoomFactor, int iMarginPercent,
			PlotComputation plotComp) throws ChartException {
		return computeScale(xs, ax, dsi, iType, dStart, dEnd, null, rtc, direction, zoomFactor, iMarginPercent,
				plotComp);

	}

	/**
	 *
	 * @param xs
	 * @param ax
	 * @param dsi
	 * @param iType
	 * @param dStart
	 * @param dEnd
	 * @param axisOrigin
	 * @param rtc
	 * @param direction
	 * @param zoomFactor     1 is default factor
	 * @param iMarginPercent the percentage of margin area for display some charts,
	 *                       such as bubble. 0 means no margin
	 * @return AutoScale instance
	 * @throws ChartException
	 */
	static AutoScale computeScale(IDisplayServer xs, OneAxis ax, DataSetIterator dsi, int iType, double dStart,
			double dEnd, AxisOrigin axisOrigin, RunTimeContext rtc, int direction, double zoomFactor,
			int iMarginPercent, PlotComputation plotComp) throws ChartException {
		// Get divisor if current is big number.
		BigDecimal divisor = null;
		dsi.reset();
		while (dsi.hasNext()) {
			Object v = dsi.next();
			if (NumberUtil.isBigNumber(v)) {
				divisor = ((BigNumber) v).getDivisor();
				break;
			}
		}

		final Scale scModel = ax.getModelAxis().getScale();
		final FormatSpecifier fs = ax.getFormatSpecifier();
		final Label la = ax.getLabel();
		final int iLabelLocation = ax.getLabelPosition();
		final int iOrientation = ax.getOrientation();
		DataElement oMinimum = scModel.getMin();
		DataElement oMaximum = scModel.getMax();
		final Double oStep = scModel.isSetStep() ? new Double(scModel.getStep()) : null;
		final Integer oStepNumber = scModel.isSetStepNumber() ? scModel.getStepNumber() : null;

		AutoScale sc = null;
		AutoScale scCloned = null;
		final Object oMinValue, oMaxValue;

		final boolean bIsPercent = ax.getModelAxis().isPercent();

		// if factor is set
		// add the factor logic separately
		if (scModel.isSetFactor() && (iType & LINEAR) == LINEAR && !ax.isCategoryScale()) {
			// translate from value/point to value/pixel
			double factor = scModel.getFactor() * 72 / xs.getDpiResolution();

			Object oValue;
			double dValue, dMinValue = Double.MAX_VALUE, dMaxValue = -Double.MAX_VALUE;
			dsi.reset();
			double dPrecision = Double.NaN;
			while (dsi.hasNext()) {
				oValue = dsi.next();
				if (oValue == null) // NULL VALUE CHECK
				{
					continue;
				}
				dValue = ((Number) oValue).doubleValue();
				if (dValue < dMinValue) {
					dMinValue = dValue;
				}
				if (dValue > dMaxValue) {
					dMaxValue = dValue;
				}
				dPrecision = getPrecision(dPrecision, dValue, fs, rtc.getULocale(), bIsPercent);
			}

			// if minimum value is set , then take it
			if (oMinimum instanceof NumberDataElement) {
				dMinValue = ((NumberDataElement) oMinimum).getValue();
			}

			// provide max value to compute a nice step size
			double length = Math.abs(dEnd - dStart);
			double valueLength = length * factor;
			dMaxValue = dMinValue + valueLength;

			double dStep = 1;

			double dDelta = dMaxValue - dMinValue;
			if (dDelta == 0) // Min == Max
			{
				dStep = dPrecision;
			} else {
				dStep = Math.floor(Math.log(dDelta) / LOG_10);
				dStep = Math.pow(10, dStep);
				// The automatic step should never be more precise than the
				// data itself
				if (dStep < dPrecision) {
					dStep = dPrecision;
				}
			}
			ScaleInfo info = new ScaleInfo(plotComp, iType, rtc, fs, ax, direction, scModel.isAutoExpand())
					.dZoomFactor(zoomFactor).iMarginPercent(iMarginPercent).dPrecision(dPrecision).bStepFixed(true)
					.dsiData(dsi).dFactor(factor);

			sc = new AutoScale(info);
			sc.setBigNubmerDivisor(divisor);

			sc.setMinimum(Double.valueOf(0));
			sc.setMaximum(Double.valueOf(0));
			sc.setStep(new Double(dStep));
			sc.setStepNumber(oStepNumber);

			// OVERRIDE STEP IF SPECIFIED
			// ignore step number if factor is set
			setStepToScale(sc, oStep, null, rtc);

			oMinValue = new Double(dMinValue);
			oMaxValue = new Double(dMaxValue);
			sc.setMinimum(oMinValue);
			sc.setMaximum(oMaxValue);

			sc.computeTicks(xs, la, iLabelLocation, iOrientation, dStart, dEnd, false, null);

			sc.setData(dsi);
			return sc;

		}

		// the following code didn't change in factor enhancement:210913
		if ((iType & TEXT) == TEXT || ax.isCategoryScale()) {
			ScaleInfo info = new ScaleInfo(plotComp, iType, rtc, fs, ax, direction, scModel.isAutoExpand())
					.dZoomFactor(zoomFactor).iMarginPercent(iMarginPercent);
			sc = new AutoScale(info);
			sc.setBigNubmerDivisor(divisor);

			sc.setData(dsi);
			sc.computeTicks(xs, ax.getLabel(), iLabelLocation, iOrientation, dStart, dEnd, false, null);

			// To initialize final fields
			oMinValue = null;
			oMaxValue = null;
		} else if ((iType & LINEAR) == LINEAR) {
			Object oValue;
			double dValue, dMinValue = Double.MAX_VALUE, dMaxValue = -Double.MAX_VALUE;
			dsi.reset();
			double dPrecision = Double.NaN;

			while (dsi.hasNext()) {
				oValue = dsi.next();
				if (oValue == null) // NULL VALUE CHECK
				{
					continue;
				}
				dValue = ((Number) oValue).doubleValue();
				if (dValue < dMinValue) {
					dMinValue = dValue;
				}
				if (dValue > dMaxValue) {
					dMaxValue = dValue;
				}
				dPrecision = getPrecision(dPrecision, dValue, fs, rtc.getULocale(), bIsPercent);
			}

			if (axisOrigin != null && axisOrigin.getType().equals(IntersectionType.VALUE_LITERAL)
					&& axisOrigin.getValue() instanceof NumberDataElement) {
				double origin = asDouble(axisOrigin.getValue()).doubleValue();
				if (oMinimum == null && origin < dMinValue) {
					oMinimum = axisOrigin.getValue();
				}
				if (oMaximum == null && origin > dMaxValue) {
					oMaximum = axisOrigin.getValue();
				}
			}
			final double dAbsMax = Math.abs(dMaxValue);
			final double dAbsMin = Math.abs(dMinValue);
			double dStep = Math.max(dAbsMax, dAbsMin);

			double dDelta = dMaxValue - dMinValue;
			if (dDelta == 0) // Min == Max
			{
				dStep = dPrecision;
			} else {
				dStep = Math.floor(Math.log(dDelta) / LOG_10);
				dStep = Math.pow(10, dStep);
				// The automatic step should never be more precise than the data
				// itself
				if (dStep < dPrecision) {
					dStep = dPrecision;
				}

			}
			ScaleInfo info = new ScaleInfo(plotComp, iType, rtc, fs, ax, direction, scModel.isAutoExpand())
					.dZoomFactor(zoomFactor).iMarginPercent(iMarginPercent).dPrecision(dPrecision);
			sc = new AutoScale(info);
			sc.setBigNubmerDivisor(divisor);

			sc.setMaximum(Double.valueOf(0));
			sc.setMinimum(Double.valueOf(0));
			sc.setStep(new Double(dStep));
			sc.setStepNumber(oStepNumber);
			sc.setData(dsi);

			// OVERRIDE MIN OR MAX IF SPECIFIED
			setNumberMinMaxToScale(sc, oMinimum, oMaximum, rtc, ax);

			// OVERRIDE STEP IF SPECIFIED
			setStepToScale(sc, oStep, oStepNumber, rtc);

			oMinValue = new Double(dMinValue);
			oMaxValue = new Double(dMaxValue);
			sc.updateAxisMinMax(oMinValue, oMaxValue);
		}

		else if ((iType & LOGARITHMIC) == LOGARITHMIC) {
			Object oValue;
			double dValue, dMinValue = Double.MAX_VALUE, dMaxValue = -Double.MAX_VALUE;
			if ((iType & PERCENT) == PERCENT) {
				dMinValue = 0;
				dMaxValue = 100;
			} else {
				dsi.reset();
				while (dsi.hasNext()) {
					oValue = dsi.next();
					if (oValue == null) // NULL VALUE CHECK
					{
						continue;
					}
					dValue = ((Number) oValue).doubleValue();
					if (dValue < dMinValue) {
						dMinValue = dValue;
					}
					if (dValue > dMaxValue) {
						dMaxValue = dValue;
					}
				}
				if (axisOrigin != null && axisOrigin.getType().equals(IntersectionType.VALUE_LITERAL)
						&& axisOrigin.getValue() instanceof NumberDataElement) {
					double origin = asDouble(axisOrigin.getValue()).doubleValue();
					if (oMinimum == null && origin < dMinValue) {
						oMinimum = axisOrigin.getValue();
					}
					if (oMaximum == null && origin > dMaxValue) {
						oMaximum = axisOrigin.getValue();
					}
				}
				// Avoid the number that will be multiplied is zero
				if (dMinValue == 0) {
					dMinValue = dMaxValue > 0 ? 1 : -1;
				}
			}

			ScaleInfo info = new ScaleInfo(plotComp, iType, rtc, fs, ax, direction, scModel.isAutoExpand())
					.dZoomFactor(zoomFactor).iMarginPercent(iMarginPercent);
			sc = new AutoScale(info);
			sc.setBigNubmerDivisor(divisor);

			sc.setMaximum(Double.valueOf(0));
			sc.setMinimum(Double.valueOf(0));
			sc.setStep(new Double(10));
			sc.setStepNumber(oStepNumber);
			sc.setData(dsi);

			// OVERRIDE MIN OR MAX IF SPECIFIED
			setNumberMinMaxToScale(sc, oMinimum, oMaximum, rtc, ax);

			// OVERRIDE STEP IF SPECIFIED
			setStepToScale(sc, oStep, oStepNumber, rtc);

			oMinValue = new Double(dMinValue);
			oMaxValue = new Double(dMaxValue);
			sc.updateAxisMinMax(oMinValue, oMaxValue);

			if ((iType & PERCENT) == PERCENT) {
				sc.info.bStepFixed(true);
				sc.info.bMaximumFixed(true);
				sc.info.bMinimumFixed(true);

				sc.computeTicks(xs, ax.getLabel(), iLabelLocation, iOrientation, dStart, dEnd, false, null);
				return sc;
			}
		} else if ((iType & DATE_TIME) == DATE_TIME) {
			Calendar cValue;
			Calendar caMin = null, caMax = null;
			dsi.reset();
			while (dsi.hasNext()) {
				cValue = (Calendar) dsi.next();
				if (cValue == null) // NULL VALUE CHECK
				{
					continue;
				}
				if (caMin == null) {
					caMin = cValue;
				}
				if (caMax == null) {
					caMax = cValue;
				}
				if (cValue.before(caMin)) {
					caMin = cValue;
				} else if (cValue.after(caMax)) {
					caMax = cValue;
				}
			}

			oMinValue = new CDateTime(caMin);
			oMaxValue = new CDateTime(caMax);
			if (axisOrigin != null && axisOrigin.getType().equals(IntersectionType.VALUE_LITERAL)
					&& axisOrigin.getValue() instanceof DateTimeDataElement) {
				CDateTime origin = asDateTime(axisOrigin.getValue());
				if (oMinimum == null && origin.before(oMinValue)) {
					oMinimum = axisOrigin.getValue();
				}
				if (oMaximum == null && origin.after(oMaxValue)) {
					oMaximum = axisOrigin.getValue();
				}
			}

			int iUnit;
			if (oStep != null || oStepNumber != null) {
				iUnit = ChartUtil.convertUnitTypeToCalendarConstant(scModel.getUnit());
			} else {
				iUnit = CDateTime.getPreferredUnit((CDateTime) oMinValue, (CDateTime) oMaxValue);
			}

			// Can't detect a difference, assume ms
			if (iUnit == 0) {
				iUnit = Calendar.SECOND;
			}

			CDateTime cdtMinAxis = ((CDateTime) oMinValue).backward(iUnit, 1);
			CDateTime cdtMaxAxis = ((CDateTime) oMaxValue).forward(iUnit, 1);
			cdtMinAxis.clearBelow(iUnit, true);
			cdtMaxAxis.clearBelow(iUnit, true);

			ScaleInfo info = new ScaleInfo(plotComp, DATE_TIME, rtc, fs, ax, direction, scModel.isAutoExpand())
					.dZoomFactor(zoomFactor).iMarginPercent(iMarginPercent)
					.iMinUnit(oMinValue.equals(oMaxValue) ? getUnitId(iUnit) : getMinUnitId(fs, rtc));
			sc = new AutoScale(info);
			sc.setBigNubmerDivisor(divisor);

			sc.setMaximum(cdtMaxAxis);
			sc.setMinimum(cdtMinAxis);
			sc.setStep(Integer.valueOf(1));
			sc.setStepNumber(oStepNumber);
			sc.context.setUnit(Integer.valueOf(iUnit));

			// OVERRIDE MINIMUM IF SPECIFIED
			if (oMinimum instanceof DateTimeDataElement) {
				sc.setMinimum(((DateTimeDataElement) oMinimum).getValueAsCDateTime());
				sc.info.oMinimumFixed(((DateTimeDataElement) oMinimum).getValueAsCDateTime());
				sc.info.bMinimumFixed(true);
			}

			// OVERRIDE MAXIMUM IF SPECIFIED
			if (oMaximum instanceof DateTimeDataElement) {
				sc.setMaximum(((DateTimeDataElement) oMaximum).getValueAsCDateTime());
				sc.info.oMaximumFixed(((DateTimeDataElement) oMaximum).getValueAsCDateTime());
				sc.info.bMaximumFixed(true);
			}

			// VALIDATE OVERRIDDEN MIN/MAX
			if (sc.info.bMaximumFixed && sc.info.bMinimumFixed) {
				if (((CDateTime) sc.getMinimum()).after(sc.getMaximum())) {
					throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
							"exception.min.largerthan.max", //$NON-NLS-1$
							new Object[] { sc.getMinimum(), sc.getMaximum() },
							Messages.getResourceBundle(rtc.getULocale()));
				}
			}

			// OVERRIDE STEP IF SPECIFIED
			setStepToScale(sc, oStep, oStepNumber, rtc);
			sc.updateAxisMinMax(oMinValue, oMaxValue);
		} else {
			// To initialize final fields for other axis types
			oMinValue = null;
			oMaxValue = null;
		}

		// Compute the scale of non-category axis
		if ((iType & TEXT) != TEXT && !ax.isCategoryScale()) {
			sc.computeTicks(xs, la, iLabelLocation, iOrientation, dStart, dEnd, false, null);
			dStart = sc.dStart;
			dEnd = sc.dEnd;

			boolean bFirstFit = sc.checkFit(xs, la, iLabelLocation);
			boolean bFits = bFirstFit;
			boolean bZoomSuccess = false;

			// Add limitation to avoid infinite loop
			for (int i = 0; bFits == bFirstFit && i < 50; i++) {
				bZoomSuccess = true;
				scCloned = (AutoScale) sc.clone();
				// DO NOT AUTO ZOOM IF STEP IS FIXED or shared scale is used
				if (sc.info.bStepFixed || rtc.getSharedScale() != null && rtc.getSharedScale().isShared()) {
					break;
				}
				if (bFirstFit) {
					if (!bFits) {
						break;
					}
					bZoomSuccess = sc.zoomIn();
				} else {
					if (!bFits && sc.getTickCordinates().size() == 2) {
						break;
					}
					bZoomSuccess = sc.zoomOut();
				}
				if (!bZoomSuccess) {
					break;
				}

				sc.updateAxisMinMax(oMinValue, oMaxValue);
				sc.computeTicks(xs, la, iLabelLocation, iOrientation, dStart, dEnd, false, null);
				bFits = sc.checkFit(xs, la, iLabelLocation);
				if (!bFits && sc.getTickCordinates().size() == 2) {
					sc = scCloned;
					break;
				}
			}

			// RESTORE TO LAST SCALE BEFORE ZOOM
			if (scCloned != null && bFirstFit && bZoomSuccess) {
				sc = scCloned;
			}

			updateSharedScaleContext(rtc, iType, sc.tmpSC);
		}

		if (sc != null) {
			// sc won't be null since there's no other axis type currently.
			sc.setData(dsi);
		}

		return sc;
	}

	/**
	 * Limit the significant number of digit to 15
	 */
	private static double getValidDouble(double dValue) {
		String sValue = dfDoulbeNormalized.format(dValue);
		double dNewValue = Double.parseDouble(sValue);
		return dNewValue;
	}

	/**
	 * Determine the mininal datetime unit to limit zoom in so, that no duplicated
	 * axis labels will be created with the given FormatSpecifier.
	 *
	 * @param fs
	 * @return
	 */
	private static int getMinUnitId(FormatSpecifier fs, RunTimeContext rtc) throws ChartException {
		int iUnit = 0;
		CDateTime cdt = new CDateTime(7, 6, 5, 4, 3, 2);
		String sDate = ValueFormatter.format(cdt, fs, rtc.getULocale(), null);

		for (int i = 0; i < iaCalendarUnits.length; i++) {
			cdt.set(iaCalendarUnits[i], 1);
			String sDatei = ValueFormatter.format(cdt, fs, rtc.getULocale(), null);
			if (!sDate.equals(sDatei)) {
				iUnit = i;
				break;
			}
		}

		return iUnit;
	}

	public static int getMinUnit(CDateTime cdt) {
		int iUnit = 0;

		for (int i = 0; i < iaCalendarUnits.length; i++) {
			if (cdt.get(iaCalendarUnits[i]) > 0) {
				iUnit = i;
				break;
			}
		}

		return iaCalendarUnits[iUnit];
	}

	public static int getUnitId(int iUnit) {
		int id = 0;

		for (int i = 0; i < iaCalendarUnits.length; i++) {
			if (iaCalendarUnits[i] == iUnit) {
				id = i;
				break;
			}
		}

		return id;
	}

	/**
	 * Computes value precision if more precise than existing one For instance 3.4
	 * has a precision of 0.1 and 1400 has a precision of 100. That is the position
	 * where the first significant digit appears, or in double representation, the
	 * value of the exponent
	 *
	 * @param precision
	 * @param value
	 * @return precision
	 */
	protected static double getPrecision(double precision, double pValue, FormatSpecifier fs, ULocale locale,
			boolean bIsPercent) {
		double value = Math.abs(pValue);
		value = getValidDouble(value);
		// First precision is NaN
		final boolean isFirst = Double.isNaN(precision);
		if (value == 0) {
			// Bugzilla#280620 two zero precision will return 1, and first
			// precision with 0 value will return 1 as well.
			if (isFirst || precision == 0) {
				return 1;
			}
			return precision;
		}

		if (isFirst) {
			precision = 0;
		}

		if (precision == 0) {
			if (bIsPercent) {
				precision = 1;
			} else {
				// precision not initialized yet
				// use worst precision for the double value
				precision = Math.pow(10, Math.floor(Math.log(value) / Math.log(10)));
			}
		}

		// divide number by precision. If precision good enough, it's an
		// integer
		double check = value / precision;
		int loopCounter = 0;
		while (!ChartUtil.mathEqual(Math.floor(check), check) && loopCounter < 20) {
			// avoid infinite loops. It should never take more than
			// 20 loops to get the right precision
			loopCounter++;
			// increase precision until it works
			precision /= 10;
			check = value / precision;
		}
		if (loopCounter == 20) {
			logger.log(ILogger.WARNING, "Autoscale precision not found for " + value);//$NON-NLS-1$
		}

		if (fs != null) {

			if (fs instanceof NumberFormatSpecifier) {
				NumberFormatSpecifier ns = (NumberFormatSpecifier) fs;
				if (ns.isSetFractionDigits()) {
					double multiplier = ns.isSetMultiplier() ? ns.getMultiplier() : 1;
					if (multiplier != 0) {
						double formatPrecision = Math.pow(10, -ns.getFractionDigits()) / multiplier;

						if (precision == 0) {
							precision = formatPrecision;
						} else {
							precision = Math.max(precision, formatPrecision);
						}
					}
				}
			}

		}
		return precision;
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
	public int computeTicks(IDisplayServer xs, Label la, int iLabelLocation, int iOrientation, double dStart,
			double dEnd, boolean bConsiderStartEndLabels, AllAxes aax) throws ChartException {
		return computeTicks(xs, la, iLabelLocation, iOrientation, dStart, dEnd, bConsiderStartEndLabels,
				bConsiderStartEndLabels, aax);
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
	public int computeTicks(IDisplayServer xs, Label la, int iLabelLocation, int iOrientation, double dStart,
			double dEnd, boolean bConsiderStartLabel, boolean bConsiderEndLabel, AllAxes aax) throws ChartException {
		boolean bMaxIsNotIntegralMultipleOfStep = false;
		int nTicks;
		double dLength;
		double dTickGap = 0;
		int iDirection = (info.iScaleDirection == AUTO) ? ((iOrientation == HORIZONTAL) ? FORWARD : BACKWARD)
				: info.iScaleDirection;

		if (bConsiderStartLabel || bConsiderEndLabel) {
			computeAxisStartEndShifts(xs, la, iOrientation, iLabelLocation, aax);

			// If axis labels should be within axes, do not adjust start
			// position
			if (!info.bLabelWithinAxes && bConsiderStartLabel) {
				double dNewStart = dStart + dStartShift * iDirection;
				if (dEnd > dStart && dNewStart > dEnd - 1) {
					dNewStart = dEnd - 1;
				} else if (dEnd < dStart && dNewStart < dEnd + 1) {
					dNewStart = dEnd + 1;
				}
				dStartShift = (dNewStart - dStart) / iDirection;
				dStart = dNewStart;
			}

			if (bConsiderEndLabel) {
				double dNewEnd = dEnd + dEndShift * -iDirection;
				if (dEnd > dStart && dNewEnd < dStart + 1) {
					dNewEnd = dStart + 1;
				} else if (dEnd < dStart && dNewEnd > dStart - 1) {
					dNewEnd = dStart - 1;
				}
				dEndShift = (dNewEnd - dEnd) / -iDirection;
				dEnd = dNewEnd;
			}
		}

		// Update member variables
		this.dStart = dStart;
		this.dEnd = dEnd;

		if (isSetFactor()) {
			double step = Methods.asDouble(getStep()).doubleValue();
			dTickGap = step / getFactor();
			int stepNum = (int) (Math.abs(dStart - dEnd) / dTickGap);
			AxisTickCoordinates atc = new AxisTickCoordinates(stepNum + 1, dStart,
					dStart < dEnd ? dStart + dTickGap * stepNum : dStart - dTickGap * stepNum, dTickGap * iDirection,
					true);

			setTickCordinates(atc);
			checkTickLabelsVisibility(xs, la, iLabelLocation, iOrientation);

			// If the factor is set, the max value that axis can display will
			// depend on the length of axis, so here must adjust the maximum
			// once axis's Start or End are changed.
			double length = Math.abs(this.dEnd - this.dStart);
			double valueLength = length * getFactor();
			double dMaxValue = ((Number) this.getMinimum()).doubleValue() + valueLength;
			this.setMaximum(Double.valueOf(dMaxValue));

			return stepNum + 1;
		}

		nTicks = getTickCount();
		dLength = Math.abs(dStart - dEnd);

		if (!info.bCategoryScale && (info.type & NUMERICAL) == NUMERICAL && (info.type & LINEAR) == LINEAR) {
			double dMax = asDouble(context.getMax()).doubleValue();
			double dMin = asDouble(context.getMin()).doubleValue();
			double dStep = asDouble(context.getStep()).doubleValue();

			bMaxIsNotIntegralMultipleOfStep = !ChartUtil.mathEqual(dMax / dStep, (int) (dMax / dStep), isBigNumber());

			if (info.bStepFixed && context.getStepNumber() != null) {
				// Use step number
				dTickGap = dLength / (context.getStepNumber().intValue()) * iDirection;
			} else {
				double dStepSize = asDouble(context.getStep()).doubleValue();
				dTickGap = Math.min(Math.abs(dStepSize / (dMax - dMin) * dLength), dLength) * iDirection;
			}
		} else if (!info.bCategoryScale && (info.type & DATE_TIME) == DATE_TIME) {
			CDateTime cdtMax = asDateTime(context.getMax());
			CDateTime cdtMin = asDateTime(context.getMin());
			double diff = CDateTime.computeDifference(cdtMax, cdtMin, asInteger(context.getUnit()))
					/ asDouble(context.getStep());
			if (diff == 0) {
				dTickGap = dLength;
			} else {
				dTickGap = Math.min(Math.abs(dLength / diff), dLength) * iDirection;
			}
		} else if (isTickBetweenCategories()) {
			dTickGap = dLength / (nTicks - 1) * iDirection;
		} else {
			dTickGap = dLength / (nTicks - 2) * iDirection;
		}

		// Added the maximum check for the step number in fixed step case.
		// If too many steps are used in auto scale, skip it. If it's fixed
		// step, it may be caused by improper step or unit.
		if (nTicks > TICKS_MAX && info.bStepFixed && !info.bCategoryScale) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, "exception.scale.tick.max", //$NON-NLS-1$
					Messages.getResourceBundle(info.rtc.getULocale()));
		}

		AxisTickCoordinates atc = new AxisTickCoordinates(nTicks, dStart, dEnd, dTickGap,
				!info.bCategoryScale || isTickBetweenCategories());

		setTickCordinates(null);
		setEndPoints(dStart, dEnd);
		setTickCordinates(atc);

		if (info.bStepFixed && context.getStepNumber() == null && (nTicks > 2) && bMaxIsNotIntegralMultipleOfStep) {
			// Step Size is fixed, Linear, Max is not integral multiple of step
			// size:
			// In this case the last label before the max value will be hided,
			// if there is not enough space
			if (!checkFit(xs, la, iLabelLocation)) {
				nTicks--;

				AxisTickCoordinates atc1 = new AxisTickCoordinates(nTicks, dStart, dEnd, dTickGap,
						!info.bCategoryScale || isTickBetweenCategories());

				setTickCordinates(null);
				setTickCordinates(atc1);
			}
		}

		// baTickLabelVisible = checkTickLabelsVisibility( xs, la,
		// iLabelLocation );
		checkTickLabelsVisibility(xs, la, iLabelLocation, iOrientation);

		return nTicks;
	}

	/**
	 * Returns the formatted value for given Axis type and value.
	 *
	 * @param iType
	 * @param oValue
	 * @return formatted string
	 */
	public String formatCategoryValue(int iType, Object oValue, int iDateTimeUnit) {
		if (oValue == null) {
			return IConstants.NULL_STRING;
		}

		if ((iType & IConstants.TEXT) == IConstants.TEXT) // MOST LIKELY
		{
			if (oValue instanceof Number) {
				// Bugzilla#216085 format numerical value even if in Text type
				return formatCategoryValue(IConstants.NUMERICAL, oValue, iDateTimeUnit);
			}
			if (info.fs != null) {
				try {
					return ValueFormatter.format(oValue, info.fs, info.rtc.getULocale(), null);
				} catch (ChartException dfex) {
					logger.log(dfex);
					return oValue.toString();
				}
			}
			return oValue.toString();
		} else if ((iType & IConstants.DATE_TIME) == IConstants.DATE_TIME) {
			final Calendar ca = (Calendar) oValue;
			IDateFormatWrapper sdf = null;
			if (info.fs == null) // ONLY COMPUTE INTERNALLY IF FORMAT
			// SPECIFIER
			// ISN'T DEFINED
			{
				sdf = info.cacheDateFormat.get(iDateTimeUnit);
			}

			// ADJUST THE START POSITION
			try {
				return ValueFormatter.format(ca, info.fs, info.rtc.getULocale(), sdf);
			} catch (ChartException dfex) {
				logger.log(dfex);
				return IConstants.NULL_STRING;
			}
		} else if ((iType & IConstants.NUMERICAL) == IConstants.NUMERICAL) {
			DecimalFormat df = null;
			// ONLY COMPUTE INTERNALLY IF FORMAT SPECIFIER ISN'T DEFINED
			if (info.fs == null) {
				String pattern = ValueFormatter.getNumericPattern((Number) oValue);
				df = info.cacheNumFormat.get(pattern);
			}
			try {
				return ValueFormatter.format(oValue, info.fs, info.rtc.getULocale(), df);
			} catch (ChartException dfex) {
				logger.log(dfex);
				return IConstants.NULL_STRING;
			}
		}

		return IConstants.NULL_STRING;
	}

	/**
	 * Computes the axis start/end shifts (due to start/end labels) and also takes
	 * into consideration all start/end shifts of any overlay axes in the same
	 * direction as the current scale.
	 *
	 * @param la
	 * @param iOrientation
	 * @param iLocation
	 * @param aax
	 */
	void computeAxisStartEndShifts(IDisplayServer xs, Label la, int iOrientation, int iLocation, AllAxes aax)
			throws ChartException {
		final double dMaxSS = (aax != null && iOrientation == aax.getOrientation()) ? aax.getMaxStartShift() : 0;
		final double dMaxES = (aax != null && iOrientation == aax.getOrientation()) ? aax.getMaxEndShift() : 0;

		// applied to shared scale case
		if ((!la.isVisible()) && !info.bLabelWithinAxes) {
			dStartShift = dMaxSS;
			dEndShift = dMaxES;
			return;
		}

		if (isCategoryScale()) {
			// COMPUTE THE BOUNDING BOXES FOR FIRST AND LAST LABEL TO ADJUST
			// START/END OF X-AXIS
			final double dUnitSize = getUnitSize();
			final DataSetIterator dsi = getData();
			final int iDateTimeUnit;
			BoundingBox bb = null;
			try {
				iDateTimeUnit = (getType() == IConstants.DATE_TIME) ? CDateTime.computeUnit(dsi) : IConstants.UNDEFINED;
			} catch (ClassCastException e) {
				// Happens when data in dsi is not of DateTime format
				throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
						"exception.dataset.non.datetime", //$NON-NLS-1$
						Messages.getResourceBundle(info.rtc.getULocale()));

			}

			final double rotation = la.getCaption().getFont().getRotation();
			final boolean bCenter = rotation == 0 || rotation == 90 || rotation == -90;

			if (!isTickLabelVisible(0)) {
				dStartShift = dMaxSS;
			} else {
				// ADJUST THE START POSITION
				la.getCaption().setValue(formatCategoryValue(getType(), dsi.first(), iDateTimeUnit));

				bb = info.cComp.computeBox(xs, iLocation, la, 0, 0);

				if (iOrientation == VERTICAL) // VERTICAL AXIS
				{
					if (bCenter) {
						dStartShift = Math.max(dMaxSS,
								(dUnitSize > bb.getHeight()) ? 0 : (bb.getHeight() - dUnitSize) / 2);
					} else if (info.iScaleDirection == FORWARD) {
						dStartShift = Math.max(dMaxSS, bb.getHotPoint() - dUnitSize / 2);
					} else {
						dStartShift = Math.max(dMaxSS, bb.getHeight() - bb.getHotPoint() - dUnitSize / 2);
					}
				} else if (iOrientation == HORIZONTAL) // HORIZONTAL AXIS
				{
					if (bCenter) {
						dStartShift = Math.max(dMaxSS,
								(dUnitSize > bb.getWidth()) ? 0 : (bb.getWidth() - dUnitSize) / 2);

					} else if (info.iScaleDirection == BACKWARD) {
						dStartShift = Math.max(dMaxSS, bb.getWidth() - bb.getHotPoint() - dUnitSize / 2);
					} else {
						dStartShift = Math.max(dMaxSS, bb.getHotPoint() - dUnitSize / 2);
					}
				}
			}

			if (!isTickLabelVisible(dsi.size() - 1)) {
				dEndShift = dMaxES;
				// Here computes if the last visible label exceeds out of the
				// end of axis and get a correct end shift.
				if (labelVisHelper.getLastVisibleIndex() < (dsi.size() - 1)) {
					double tmpEndShift = computeEndShift(xs, la, iOrientation, iLocation, dMaxES,
							dUnitSize * (dsi.size() - labelVisHelper.getLastVisibleIndex()), dsi, iDateTimeUnit,
							bCenter);
					if (tmpEndShift > dEndShift) {
						dEndShift = tmpEndShift;
					}
				}
			} else {
				// ADJUST THE END POSITION
				dEndShift = computeEndShift(xs, la, iOrientation, iLocation, dMaxES, dUnitSize, dsi, iDateTimeUnit,
						bCenter);
			}

		} else if ((info.type & NUMERICAL) == NUMERICAL) {
			if ((info.type & LINEAR) == LINEAR) {
				// ADJUST THE START POSITION
				DecimalFormat df = null;
				Object value = AxisValueProvider.getValue(getMinimum(),
						isBigNumber() ? this.getBigNumberDivisor() : null);
				if (info.fs == null) // ONLY COMPUTE INTERNALLY IF FORMAT
				// SPECIFIER
				// ISN'T DEFINED
				{
					if (!isBigNumber()) {
						df = new DecimalFormat(getNumericPattern());
					} else {
						df = new DecimalFormat(ValueFormatter.getNumericPattern((Number) value));
					}
				}
				String sValue = null;
				try {
					sValue = ValueFormatter.format(value, info.fs, info.rtc.getULocale(), df);
				} catch (ChartException dfex) {
					logger.log(dfex);
					sValue = IConstants.NULL_STRING;
				}
				la.getCaption().setValue(sValue);
				BoundingBox bb = info.cComp.computeBox(xs, iLocation, la, 0, 0);

				if (iOrientation == VERTICAL) // VERTICAL AXIS
				{
					dStartShift = Math.max(dMaxSS, (bb.getHeight() - bb.getHotPoint()));
				} else if (iOrientation == HORIZONTAL) {
					dStartShift = Math.max(dMaxSS, bb.getHotPoint());
				}

				// ADJUST THE END POSITION
				value = AxisValueProvider.getValue(getMaximum(), isBigNumber() ? this.getBigNumberDivisor() : null);
				if (info.fs == null)
				// ONLY COMPUTE INTERNALLY (DIFFERENT FROM
				// MINIMUM) IF FORMAT SPECIFIER ISN'T DEFINED
				{
					df = new DecimalFormat(ValueFormatter.getNumericPattern((Number) value));
				}
				try {
					sValue = ValueFormatter.format(value, info.fs, info.rtc.getULocale(), df);
				} catch (ChartException dfex) {
					logger.log(dfex);
					sValue = IConstants.NULL_STRING;
				}

				la.getCaption().setValue(sValue);
				bb = info.cComp.computeBox(xs, iLocation, la, 0, 0);

				if (iOrientation == VERTICAL) // VERTICAL AXIS
				{
					dEndShift = Math.max(dMaxES, bb.getHotPoint());
				} else if (iOrientation == HORIZONTAL) {
					dEndShift = Math.max(dMaxES, (bb.getWidth() - bb.getHotPoint()));
				}
			} else if ((info.type & LOGARITHMIC) == LOGARITHMIC) {
				// ADJUST THE START POSITION
				DecimalFormat df = null;
				Object value = AxisValueProvider.getValue(getMinimum(),
						isBigNumber() ? this.getBigNumberDivisor() : null);
				if (info.fs == null)
				// ONLY COMPUTE INTERNALLY IF FORMAT
				// SPECIFIER ISN'T DEFINED
				{
					df = new DecimalFormat(ValueFormatter.getNumericPattern((Number) value));
				}
				String sValue = null;
				try {
					sValue = ValueFormatter.format(value, info.fs, info.rtc.getULocale(), df);
				} catch (ChartException dfex) {
					logger.log(dfex);
					sValue = IConstants.NULL_STRING;
				}
				la.getCaption().setValue(sValue);
				BoundingBox bb;
				bb = info.cComp.computeBox(xs, iLocation, la, 0, 0);

				if (iOrientation == VERTICAL) // VERTICAL AXIS
				{
					dStartShift = Math.max(dMaxSS, (bb.getHeight() - bb.getHotPoint()));
				} else if (iOrientation == HORIZONTAL) {
					dStartShift = Math.max(dMaxSS, bb.getHotPoint());
				}

				// ADJUST THE END POSITION
				// final double dMaximum = asDouble( getMaximum( )
				// ).doubleValue( );
				value = AxisValueProvider.getValue(getMaximum(), isBigNumber() ? this.getBigNumberDivisor() : null);
				if (info.fs == null)
				// ONLY COMPUTE INTERNALLY (DIFFERENT FROM
				// MINIMUM) IF FORMAT SPECIFIER ISN'T DEFINED
				{
					df = new DecimalFormat(ValueFormatter.getNumericPattern((Number) value));
				}
				try {
					sValue = ValueFormatter.format(value, info.fs, info.rtc.getULocale(), df);
				} catch (ChartException dfex) {
					logger.log(dfex);
					sValue = IConstants.NULL_STRING;
				}
				la.getCaption().setValue(sValue);
				bb = info.cComp.computeBox(xs, iLocation, la, 0, 0);

				if (iOrientation == VERTICAL) // VERTICAL AXIS
				{
					dEndShift = Math.max(dMaxES, bb.getHotPoint());
				} else if (iOrientation == HORIZONTAL) {
					dEndShift = Math.max(dMaxES, (bb.getWidth() - bb.getHotPoint()));
				}
			}
		}

		else if (getType() == DATE_TIME) {
			// COMPUTE THE BOUNDING BOXES FOR FIRST AND LAST LABEL TO ADJUST
			// START/END OF X-AXIS
			CDateTime cdt = asDateTime(getMinimum());
			final int iUnit = asInteger(context.getUnit());
			IDateFormatWrapper sdf = null;
			String sText = null;

			if (info.fs == null) // ONLY COMPUTE INTERNALLY IF FORMAT
			// SPECIFIER
			// ISN'T DEFINED
			{
				sdf = DateFormatWrapperFactory.getPreferredDateFormat(iUnit, info.rtc.getULocale());
			}

			// ADJUST THE START POSITION
			try {
				sText = ValueFormatter.format(cdt, info.fs, info.rtc.getULocale(), sdf);
			} catch (ChartException dfex) {
				logger.log(dfex);
				sText = IConstants.NULL_STRING;
			}
			la.getCaption().setValue(sText);

			BoundingBox bb = info.cComp.computeBox(xs, iLocation, la, 0, 0);

			if (iOrientation == VERTICAL) // VERTICAL AXIS
			{
				dStartShift = Math.max(dMaxSS, (bb.getHeight() - bb.getHotPoint()));
			} else if (iOrientation == HORIZONTAL) {
				dStartShift = Math.max(dMaxSS, bb.getHotPoint());
			}

			// ADJUST THE END POSITION
			cdt = asDateTime(getMaximum());
			try {
				sText = ValueFormatter.format(cdt, info.fs, info.rtc.getULocale(), sdf);
			} catch (ChartException dfex) {
				logger.log(dfex);
				sText = IConstants.NULL_STRING;
			}
			la.getCaption().setValue(sText);
			bb = info.cComp.computeBox(xs, iLocation, la, 0, dEnd);

			if (iOrientation == VERTICAL) // VERTICAL AXIS
			{
				dEndShift = Math.max(dMaxES, bb.getHotPoint());
			} else if (iOrientation == HORIZONTAL) {
				dEndShift = Math.max(dMaxES, (bb.getWidth() - bb.getHotPoint()));
			}
		}
	}

	/**
	 * @param xs
	 * @param la
	 * @param iOrientation
	 * @param iLocation
	 * @param dMaxES
	 * @param dUnitsSize    this size indicates the remainder total units size from
	 *                      last visible tick label to the last tick.
	 * @param dsi
	 * @param iDateTimeUnit
	 * @param bCenter
	 * @return
	 * @throws ChartException
	 */
	private double computeEndShift(IDisplayServer xs, Label la, int iOrientation, int iLocation, final double dMaxES,
			final double dUnitsSize, final DataSetIterator dsi, final int iDateTimeUnit, final boolean bCenter)
			throws ChartException {
		double endShift = 0;
		BoundingBox bb;
		la.getCaption().setValue(formatCategoryValue(getType(), dsi.last(), iDateTimeUnit));

		bb = info.cComp.computeBox(xs, iLocation, la, 0, dEnd);

		if (iOrientation == VERTICAL) // VERTICAL AXIS
		{
			if (bCenter) {
				endShift = Math.max(dMaxES, (dUnitsSize > bb.getHeight()) ? 0 : (bb.getHeight() - dUnitsSize) / 2);
			} else if (info.iScaleDirection == FORWARD) {
				endShift = Math.max(dMaxES, bb.getHeight() - bb.getHotPoint() - dUnitsSize / 2);
			} else {
				endShift = Math.max(dMaxES, bb.getHotPoint() - dUnitsSize / 2);
			}
		} else if (iOrientation == HORIZONTAL) // HORIZONTAL AXIS
		{
			if (bCenter) {
				endShift = Math.max(dMaxES, (dUnitsSize > bb.getWidth()) ? 0 : (bb.getWidth() - dUnitsSize) / 2);
			} else if (info.iScaleDirection == BACKWARD) {
				endShift = Math.max(dMaxES, bb.getHotPoint() - dUnitsSize / 2);
			} else {
				endShift = Math.max(dMaxES, bb.getWidth() - bb.getHotPoint() - dUnitsSize / 2);
			}
		}
		return endShift;
	}

	public double computeAxisLabelThickness(IDisplayServer xs, Label la, int iOrientation) throws ChartException {
		if (!la.isVisible()) {
			return 0;
		}

		String sText;
		AxisTickCoordinates da = getTickCordinates();

		if (iOrientation == VERTICAL) {
			double dW, dMaxW = 0, dMaxW2 = 0;
			if (isCategoryScale()) {
				// final DataSetIterator dsi = getData( );
				// final int iDateTimeUnit = ( getType( ) ==
				// IConstants.DATE_TIME ) ? CDateTime.computeUnit( dsi )
				// : IConstants.UNDEFINED;
				// /

				Collection<Integer> visIds = getVisibleLabelIds();
				for (int id : visIds) {
					la.getCaption().setValue(getComputedLabelText(id));
					dW = info.cComp.computeWidth(xs, la);

					if (isAxisLabelStaggered() && isTickLabelStaggered(id)) {
						dMaxW2 = Math.max(dW, dMaxW2);
					} else if (dW > dMaxW) {
						dMaxW = dW;
					}

				}

				// /
				// dsi.reset( );
				// int i = isTickBetweenCategories( ) ? 0 : 1;
				// while ( dsi.hasNext( ) )
				// {
				// Object oValue = dsi.next( );
				// if ( isTickLabelVisible( i ) )
				// {
				// la.getCaption( )
				// .setValue( formatCategoryValue( getType( ),
				// oValue,
				// iDateTimeUnit ) );
				// dW = computeWidth( xs, la );
				//
				// if ( isAxisLabelStaggered( )
				// && isTickLabelStaggered( i ) )
				// {
				// dMaxW2 = Math.max( dW, dMaxW2 );
				// }
				// else if ( dW > dMaxW )
				// {
				// dMaxW = dW;
				// }
				// }
				// i++;
				// }
			} else if ((getType() & LINEAR) == LINEAR) {
				final NumberDataElement nde = NumberDataElementImpl.create(0);
				double dAxisValue = asDouble(getMinimum()).doubleValue();
				double dAxisStep = asDouble(getStep()).doubleValue();
				AxisValueProvider avi = new AxisValueProvider(dAxisValue, dAxisStep, this, info);
				DecimalFormat df = avi.getDecimalFormat();
				for (int i = 0; i < da.size(); i++) {
					Object value = avi.getValue(nde);
					sText = AxisValueProvider.format(value, info.fs, info.rtc.getULocale(), df, logger);
					la.getCaption().setValue(sText);
					dW = info.cComp.computeWidth(xs, la);

					if (isAxisLabelStaggered() && isTickLabelStaggered(i)) {
						dMaxW2 = Math.max(dW, dMaxW2);
					} else if (dW > dMaxW) {
						dMaxW = dW;
					}

					avi.addStep();
				}
			} else if ((getType() & LOGARITHMIC) == LOGARITHMIC) {
				final NumberDataElement nde = NumberDataElementImpl.create(0);
				double dAxisValue = asDouble(getMinimum()).doubleValue();

				double dAxisStep = asDouble(getStep()).doubleValue();

				AxisValueProvider avi = new AxisValueProvider(dAxisValue, dAxisStep, this, info);
				DecimalFormat df = null;
				for (int i = 0; i < da.size(); i++) {
					df = avi.getDecimalFormat();
					Object value = avi.getValue(nde);
					sText = AxisValueProvider.format(value, info.fs, info.rtc.getULocale(), df, logger);

					la.getCaption().setValue(sText);
					dW = info.cComp.computeWidth(xs, la);
					if (isAxisLabelStaggered() && isTickLabelStaggered(i)) {
						dMaxW2 = Math.max(dW, dMaxW2);
					} else if (dW > dMaxW) {
						dMaxW = dW;
					}

					avi.mutltiplyStep();
				}
			} else if ((getType() & DATE_TIME) == DATE_TIME) {
				CDateTime cdtAxisValue = asDateTime(getMinimum());
				int iStep = asInteger(getStep());
				int iUnit = asInteger(getUnit());
				IDateFormatWrapper sdf = null;
				if (info.fs == null) {
					sdf = DateFormatWrapperFactory.getPreferredDateFormat(iUnit, info.rtc.getULocale());
				}
				for (int i = 0; i < da.size(); i++) {
					try {
						sText = ValueFormatter.format(cdtAxisValue, info.fs, info.rtc.getULocale(), sdf);
					} catch (ChartException dfex) {
						logger.log(dfex);
						sText = IConstants.NULL_STRING;
					}
					la.getCaption().setValue(sText);
					dW = info.cComp.computeWidth(xs, la);
					if (isAxisLabelStaggered() && isTickLabelStaggered(i)) {
						dMaxW2 = Math.max(dW, dMaxW2);
					} else if (dW > dMaxW) {
						dMaxW = dW;
					}
					cdtAxisValue = cdtAxisValue.forward(iUnit, iStep);
				}
			}
			return dMaxW + dMaxW2;
		} else if (iOrientation == HORIZONTAL) {
			double dH, dMaxH = 0, dMaxH2 = 0;
			if (isCategoryScale()) {
				Collection<Integer> visIds = getVisibleLabelIds();

				for (int id : visIds) {
					la.getCaption().setValue(getComputedLabelText(id));
					dH = info.cComp.computeHeight(xs, la);
					if (isAxisLabelStaggered() && isTickLabelStaggered(id)) {
						dMaxH2 = Math.max(dH, dMaxH2);
					} else if (dH > dMaxH) {
						dMaxH = dH;
					}

				}

				// final DataSetIterator dsi = getData( );
				// final int iDateTimeUnit = ( getType( ) ==
				// IConstants.DATE_TIME ) ? CDateTime.computeUnit( dsi )
				// : IConstants.UNDEFINED;
				//
				// dsi.reset( );
				// int i = isTickBetweenCategories( ) ? 0 : 1;
				// while ( dsi.hasNext( ) )
				// {
				// Object oValue = dsi.next( );
				// if ( isTickLabelVisible( i ) )
				// {
				// la.getCaption( )
				// .setValue( formatCategoryValue( getType( ),
				// oValue,
				// iDateTimeUnit ) );
				// dH = computeHeight( xs, la );
				// if ( isAxisLabelStaggered( )
				// && isTickLabelStaggered( i ) )
				// {
				// dMaxH2 = Math.max( dH, dMaxH2 );
				// }
				// else if ( dH > dMaxH )
				// {
				// dMaxH = dH;
				// }
				// }
				// i++;
				// }
			} else if ((getType() & LINEAR) == LINEAR) {
				final NumberDataElement nde = NumberDataElementImpl.create(0);
				double dAxisValue = asDouble(getMinimum()).doubleValue();
				final double dAxisStep = asDouble(getStep()).doubleValue();
				DecimalFormat df = null;
				if (info.fs == null) {
					df = computeDecimalFormat(dAxisValue, dAxisStep);
				}
				for (int i = 0; i < da.size(); i++) {
					nde.setValue(dAxisValue);
					try {
						sText = ValueFormatter.format(nde, info.fs, info.rtc.getULocale(), df);
					} catch (ChartException dfex) {
						logger.log(dfex);
						sText = IConstants.NULL_STRING;
					}
					la.getCaption().setValue(sText);
					dH = info.cComp.computeHeight(xs, la);
					if (isAxisLabelStaggered() && isTickLabelStaggered(i)) {
						dMaxH2 = Math.max(dH, dMaxH2);
					} else if (dH > dMaxH) {
						dMaxH = dH;
					}
					dAxisValue += dAxisStep;
				}
			} else if ((getType() & LOGARITHMIC) == LOGARITHMIC) {
				final NumberDataElement nde = NumberDataElementImpl.create(0);
				double dAxisValue = asDouble(getMinimum()).doubleValue();
				final double dAxisStep = asDouble(getStep()).doubleValue();
				DecimalFormat df = null;
				for (int i = 0; i < da.size(); i++) {
					if (info.fs == null) {
						df = computeDecimalFormat(dAxisValue, dAxisStep);
					}
					nde.setValue(dAxisValue);
					try {
						sText = ValueFormatter.format(nde, info.fs, info.rtc.getULocale(), df);
					} catch (ChartException dfex) {
						logger.log(dfex);
						sText = IConstants.NULL_STRING;
					}
					la.getCaption().setValue(sText);
					dH = info.cComp.computeHeight(xs, la);
					if (isAxisLabelStaggered() && isTickLabelStaggered(i)) {
						dMaxH2 = Math.max(dH, dMaxH2);
					} else if (dH > dMaxH) {
						dMaxH = dH;
					}
					dAxisValue *= dAxisStep;
				}
			} else if ((getType() & DATE_TIME) == DATE_TIME) {
				CDateTime cdtAxisValue = asDateTime(getMinimum());
				final int iStep = asInteger(getStep());
				final int iUnit = asInteger(getUnit());
				IDateFormatWrapper sdf = null;
				if (info.fs == null) {
					sdf = DateFormatWrapperFactory.getPreferredDateFormat(iUnit, info.rtc.getULocale());
				}
				for (int i = 0; i < da.size(); i++) {
					try {
						sText = ValueFormatter.format(cdtAxisValue, info.fs, info.rtc.getULocale(), sdf);
					} catch (ChartException dfex) {
						logger.log(dfex);
						sText = IConstants.NULL_STRING;
					}
					la.getCaption().setValue(sText);
					dH = info.cComp.computeHeight(xs, la);
					if (isAxisLabelStaggered() && isTickLabelStaggered(i)) {
						dMaxH2 = Math.max(dH, dMaxH2);
					} else if (dH > dMaxH) {
						dMaxH = dH;
					}
					cdtAxisValue.forward(iUnit, iStep);
				}
			}
			return dMaxH + dMaxH2;
		}
		return 0;
	}

	public double computeStaggeredAxisLabelOffset(IDisplayServer xs, Label la, int iOrientation) throws ChartException {
		if (!la.isVisible() || !isAxisLabelStaggered()) {
			return 0;
		}

		String sText;
		AxisTickCoordinates da = getTickCordinates();

		if (iOrientation == VERTICAL) {
			double dW, dMaxW = 0;
			if (isCategoryScale()) {
				final DataSetIterator dsi = getData();
				final int iDateTimeUnit = (getType() == IConstants.DATE_TIME) ? CDateTime.computeUnit(dsi)
						: IConstants.UNDEFINED;
				dsi.reset();
				int i = 0;
				while (dsi.hasNext()) {
					la.getCaption().setValue(formatCategoryValue(getType(), dsi.next(), iDateTimeUnit));
					if (!isTickLabelStaggered(i)) {
						dW = info.cComp.computeWidth(xs, la);

						if (dW > dMaxW) {
							dMaxW = dW;
						}
					}
					i++;
				}
			} else if ((getType() & LINEAR) == LINEAR) {
				final NumberDataElement nde = NumberDataElementImpl.create(0);
				double dAxisValue = asDouble(getMinimum()).doubleValue();
				double dAxisStep = asDouble(getStep()).doubleValue();
				AxisValueProvider avi = new AxisValueProvider(dAxisValue, dAxisStep, this, info);
				DecimalFormat df = avi.getDecimalFormat();
				for (int i = 0; i < da.size(); i++) {
					Object value = avi.getValue(nde);
					sText = AxisValueProvider.format(value, info.fs, info.rtc.getULocale(), df, logger);

					la.getCaption().setValue(sText);

					if (!isTickLabelStaggered(i)) {
						dW = info.cComp.computeWidth(xs, la);

						if (dW > dMaxW) {
							dMaxW = dW;
						}
					}
					avi.addStep();
				}
			} else if ((getType() & LOGARITHMIC) == LOGARITHMIC) {
				final NumberDataElement nde = NumberDataElementImpl.create(0);
				double dAxisValue = asDouble(getMinimum()).doubleValue();
				double dAxisStep = asDouble(getStep()).doubleValue();
				AxisValueProvider avi = new AxisValueProvider(dAxisValue, dAxisStep, this, info);
				DecimalFormat df = null;
				for (int i = 0; i < da.size(); i++) {
					df = avi.getDecimalFormat();
					Object value = avi.getValue(nde);
					sText = AxisValueProvider.format(value, info.fs, info.rtc.getULocale(), df, logger);
					la.getCaption().setValue(sText);

					if (!isTickLabelStaggered(i)) {
						dW = info.cComp.computeWidth(xs, la);

						if (dW > dMaxW) {
							dMaxW = dW;
						}
					}
					avi.mutltiplyStep();
				}
			} else if ((getType() & DATE_TIME) == DATE_TIME) {
				CDateTime cdtAxisValue = asDateTime(getMinimum());
				int iStep = asInteger(getStep());
				int iUnit = asInteger(getUnit());
				IDateFormatWrapper sdf = null;
				if (info.fs == null) {
					sdf = DateFormatWrapperFactory.getPreferredDateFormat(iUnit, info.rtc.getULocale());
				}
				for (int i = 0; i < da.size(); i++) {
					try {
						sText = ValueFormatter.format(cdtAxisValue, info.fs, info.rtc.getULocale(), sdf);
					} catch (ChartException dfex) {
						logger.log(dfex);
						sText = IConstants.NULL_STRING;
					}
					la.getCaption().setValue(sText);

					if (!isTickLabelStaggered(i)) {
						dW = info.cComp.computeWidth(xs, la);

						if (dW > dMaxW) {
							dMaxW = dW;
						}
					}
					cdtAxisValue = cdtAxisValue.forward(iUnit, iStep);
				}
			}
			return dMaxW;
		} else if (iOrientation == HORIZONTAL) {
			double dH, dMaxH = 0;
			if (isCategoryScale()) {
				final DataSetIterator dsi = getData();
				final int iDateTimeUnit = (getType() == IConstants.DATE_TIME) ? CDateTime.computeUnit(dsi)
						: IConstants.UNDEFINED;

				dsi.reset();
				int i = 0;
				while (dsi.hasNext()) {
					la.getCaption().setValue(formatCategoryValue(getType(), dsi.next(), iDateTimeUnit));

					if (!isTickLabelStaggered(i)) {
						dH = info.cComp.computeHeight(xs, la);

						if (dH > dMaxH) {
							dMaxH = dH;
						}
					}
					i++;
				}
			} else if ((getType() & LINEAR) == LINEAR) {
				final NumberDataElement nde = NumberDataElementImpl.create(0);
				double dAxisValue = asDouble(getMinimum()).doubleValue();
				final double dAxisStep = asDouble(getStep()).doubleValue();
				AxisValueProvider avi = new AxisValueProvider(dAxisValue, dAxisStep, this, info);
				DecimalFormat df = avi.getDecimalFormat();
				for (int i = 0; i < da.size(); i++) {
					Object value = avi.getValue(nde);
					sText = AxisValueProvider.format(value, info.fs, info.rtc.getULocale(), df, logger);
					la.getCaption().setValue(sText);

					if (!isTickLabelStaggered(i)) {
						dH = info.cComp.computeHeight(xs, la);

						if (dH > dMaxH) {
							dMaxH = dH;
						}
					}
					avi.addStep();
				}
			} else if ((getType() & LOGARITHMIC) == LOGARITHMIC) {
				final NumberDataElement nde = NumberDataElementImpl.create(0);
				double dAxisValue = asDouble(getMinimum()).doubleValue();
				final double dAxisStep = asDouble(getStep()).doubleValue();
				AxisValueProvider avi = new AxisValueProvider(dAxisValue, dAxisStep, this, info);
				DecimalFormat df = null;
				for (int i = 0; i < da.size(); i++) {
					df = avi.getDecimalFormat();
					Object value = avi.getValue(nde);
					sText = AxisValueProvider.format(value, info.fs, info.rtc.getULocale(), df, logger);
					la.getCaption().setValue(sText);

					if (!isTickLabelStaggered(i)) {
						dH = info.cComp.computeHeight(xs, la);

						if (dH > dMaxH) {
							dMaxH = dH;
						}
					}
					dAxisValue *= dAxisStep;
				}
			} else if ((getType() & DATE_TIME) == DATE_TIME) {
				CDateTime cdtAxisValue = asDateTime(getMinimum());
				final int iStep = asInteger(getStep());
				final int iUnit = asInteger(getUnit());
				IDateFormatWrapper sdf = null;
				if (info.fs == null) {
					sdf = DateFormatWrapperFactory.getPreferredDateFormat(iUnit, info.rtc.getULocale());
				}
				for (int i = 0; i < da.size(); i++) {
					try {
						sText = ValueFormatter.format(cdtAxisValue, info.fs, info.rtc.getULocale(), sdf);
					} catch (ChartException dfex) {
						logger.log(dfex);
						sText = IConstants.NULL_STRING;
					}
					la.getCaption().setValue(sText);

					if (!isTickLabelStaggered(i)) {
						dH = info.cComp.computeHeight(xs, la);

						if (dH > dMaxH) {
							dMaxH = dH;
						}
					}
					cdtAxisValue.forward(iUnit, iStep);
				}
			}
			return dMaxH;
		}
		return 0;
	}

	public boolean isStepFixed() {
		return info.bStepFixed;
	}

	/**
	 * @param v
	 */
	public void setStepFixed(boolean v) {
		info.bStepFixed(v);
	}

	public boolean isMinimumFixed() {
		return info.bMinimumFixed;
	}

	/**
	 * @param v
	 */
	public void setMinimumFixed(boolean v) {
		info.bMinimumFixed(v);
	}

	public boolean isMaximumFixed() {
		return info.bMaximumFixed;
	}

	/**
	 * @param v
	 */
	public void setMaximumFixed(boolean v) {
		info.bMaximumFixed(v);
	}

	/**
	 * Checks if axis is category style or Text type
	 *
	 * @return category scale or not
	 */
	public boolean isCategoryScale() {
		return info.bCategoryScale;
	}

	public double[] getMinorCoordinates(int iMinorUnitsPerMajor) {
		if (atcTickCoordinates == null || iMinorUnitsPerMajor <= 0) {
			return null;
		}

		final double[] da = new double[iMinorUnitsPerMajor];
		final double dUnit = getUnitSize();
		if ((info.type & LOGARITHMIC) != LOGARITHMIC) {
			final double dEach = dUnit / iMinorUnitsPerMajor;
			for (int i = 1; i < iMinorUnitsPerMajor; i++) {
				da[i - 1] = dEach * i;
			}
		} else {
			final double dCount = iMinorUnitsPerMajor;
			final double dMax = Math.log(dCount);

			for (int i = 0; i < iMinorUnitsPerMajor; i++) {
				da[i] = (Math.log(i + 1) * dUnit) / dMax;
			}
		}
		da[iMinorUnitsPerMajor - 1] = dUnit;
		return da;
	}

	public RunTimeContext getRunTimeContext() {
		return info.rtc;
	}

	/**
	 * Updates AutoScale by checking min or max
	 *
	 * @param sc
	 * @param oMinimum
	 * @param oMaximum
	 * @param rtc
	 * @param ax
	 * @throws ChartException
	 */
	public static void setNumberMinMaxToScale(AutoScale sc, Object oMinimum, Object oMaximum, final RunTimeContext rtc,
			final OneAxis ax) throws ChartException {
		// OVERRIDE MINIMUM IF SPECIFIED
		if (oMinimum instanceof NumberDataElement) {
			sc.setMinimum(new Double(((NumberDataElement) oMinimum).getValue()));
			sc.info.bMinimumFixed(true);
		} else if (oMinimum instanceof BigNumberDataElement) {
			BigDecimal bd = NumberUtil.asBigDecimal(((BigNumberDataElement) oMinimum).getValue());
			sc.setMinimum(new BigNumber(bd, sc.bigNumberDivisor));
			sc.info.bMinimumFixed(true);
		}

		// OVERRIDE MAXIMUM IF SPECIFIED
		if (oMaximum instanceof NumberDataElement) {
			sc.setMaximum(Double.valueOf(((NumberDataElement) oMaximum).getValue()));
			sc.info.bMaximumFixed(true);
		} else if (oMaximum instanceof BigNumberDataElement) {
			BigDecimal bd = NumberUtil.asBigDecimal(((BigNumberDataElement) oMaximum).getValue());
			sc.setMaximum(new BigNumber(bd, sc.bigNumberDivisor));
			sc.info.bMaximumFixed(true);
		}

		// VALIDATE OVERRIDDEN MIN/MAX
		if (sc.info.bMaximumFixed && sc.info.bMinimumFixed) {
			boolean bInValid = false;

			Object oMin = sc.getMinimum();
			Object oMax = sc.getMaximum();

			if (oMin instanceof Double && oMax instanceof Double) {
				bInValid = ((Double) oMin).doubleValue() > ((Double) oMax).doubleValue();
			} else {
				bInValid = ((BigNumber) oMin).compareTo(oMax) > 0;
			}

			if (bInValid) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
						"exception.min.largerthan.max", //$NON-NLS-1$
						new Object[] { sc.getMinimum(), sc.getMaximum() },
						Messages.getResourceBundle(rtc.getULocale()));
			}
		}
	}

	/**
	 * Updates AutoScale by checking step size and step number
	 *
	 * @param sc
	 * @param oStep
	 * @param oStepNumber
	 * @param rtc
	 * @throws ChartException
	 */
	public static void setStepToScale(AutoScale sc, Object oStep, Integer oStepNumber, RunTimeContext rtc)
			throws ChartException {
		// OVERRIDE STEP IF SPECIFIED
		if (oStep != null) {
			sc.setStep(oStep);
			sc.info.bStepFixed(true);

			// VALIDATE OVERRIDDEN STEP
			if (((Double) sc.getStep()).doubleValue() <= 0) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, "exception.invalid.step.size", //$NON-NLS-1$
						new Object[] { oStep }, Messages.getResourceBundle(rtc.getULocale()));
			}
		}

		if (oStepNumber != null) {
			sc.setStepNumber(oStepNumber);
			sc.info.bStepFixed(true);

			// VALIDATE OVERRIDDEN STEP
			if (sc.getStepNumber().intValue() < 1) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
						"exception.invalid.step.number", //$NON-NLS-1$
						new Object[] { oStepNumber }, Messages.getResourceBundle(rtc.getULocale()));
			}
		}
	}

	private final static BigDecimal UPER_LIMIT = new BigDecimal("1E9"); //$NON-NLS-1$

	/**
	 * Creates a default decimal format based on specified number.
	 *
	 * @param number specified number.
	 * @return instance of <code>DecimalFormat</code>
	 */
	public DecimalFormat computeDefaultDecimalFormat(Number number) {
		return info.cacheNumFormat.get(ValueFormatter.getNumericPattern(number));
	}

	public DecimalFormat computeDecimalFormat(BigDecimal bdAxisValue, BigDecimal bdAxisStep) {
		if (bdAxisValue.abs().compareTo(UPER_LIMIT) >= 0 || bdAxisStep.abs().compareTo(UPER_LIMIT) >= 0) {
			return info.cacheNumFormat.get("0.0E0"); //$NON-NLS-1$
		}

		// Use a more precise pattern
		String valuePattern;
		String stepPattern;

		boolean bValuePrecise;
		boolean bStepPrecise;

		// return NumberUtil.getBigDecimalFormat( );
		valuePattern = ValueFormatter.getNumericPattern(bdAxisValue);
		stepPattern = ValueFormatter.getNumericPattern(bdAxisStep);

		bValuePrecise = ChartUtil.checkBigNumberPrecise(bdAxisValue);
		bStepPrecise = ChartUtil.checkBigNumberPrecise(bdAxisStep);

		// See Bugzilla#185883
		if (bValuePrecise) {
			if (bStepPrecise) {
				// If they are both double-precise, use the more precise one
				if (valuePattern.length() < stepPattern.length()) {
					return info.cacheNumFormat.get(stepPattern);
				}
			}
		} else if (bStepPrecise) {
			return info.cacheNumFormat.get(stepPattern);
		}
		// If they are neither double-precise, use the default value
		return info.cacheNumFormat.get(valuePattern);
	}

	/**
	 * Computes the default DecimalFormat pattern for axis according to axis value
	 * and scale steps.
	 *
	 * @param dAxisValue axis value
	 * @param dAxisStep  scale step
	 * @return default format pattern
	 */
	public DecimalFormat computeDecimalFormat(double dAxisValue, double dAxisStep) {
		// Use a more precise pattern
		String valuePattern;
		String stepPattern;

		boolean bValuePrecise = false;
		boolean bStepPrecise = false;

		if (this.isBigNumber()) {
			// return NumberUtil.getBigDecimalFormat( );
			BigDecimal bdAxisValue = this.getBigNumberDivisor().multiply(new BigDecimal(dAxisValue),
					NumberUtil.DEFAULT_MATHCONTEXT);
			BigDecimal bdAxisStep = this.getBigNumberDivisor().multiply(new BigDecimal(dAxisStep),
					NumberUtil.DEFAULT_MATHCONTEXT);

			valuePattern = ValueFormatter.getNumericPattern(bdAxisValue);
			stepPattern = ValueFormatter.getNumericPattern(bdAxisStep);

			bValuePrecise = ChartUtil.checkBigNumberPrecise(bdAxisValue);
			bStepPrecise = ChartUtil.checkBigNumberPrecise(bdAxisStep);
		} else {
			// Use a more precise pattern
			valuePattern = ValueFormatter.getNumericPattern(dAxisValue);

			// Since the axis step is computed, here normalize it first to avoid
			// error of precision and avoid to get error format pattern.
			dAxisStep = ValueFormatter.normalizeDouble(dAxisStep).doubleValue();
			stepPattern = ValueFormatter.getNumericPattern(dAxisStep);

			bValuePrecise = ChartUtil.checkDoublePrecise(dAxisValue);
			bStepPrecise = ChartUtil.checkDoublePrecise(dAxisStep);
		}

		// See Bugzilla#185883
		if (bValuePrecise) {
			if (bStepPrecise) {
				// If they are both double-precise, use the more precise one
				if (valuePattern.length() < stepPattern.length()) {
					return info.cacheNumFormat.get(stepPattern);
				}
			}
		} else if (bStepPrecise) {
			return info.cacheNumFormat.get(stepPattern);
		}
		// If they are neither double-precise, use the default value
		return info.cacheNumFormat.get(valuePattern);
	}

	private static void updateSharedScaleContext(RunTimeContext rtc, int iType, ScaleContext sct) {
		if (rtc.getSharedScale() != null && !rtc.getSharedScale().isShared()) {
			rtc.getSharedScale().setScaleContext(sct);
		}
	}

	/**
	 * Helper class for Tick Label Visiblility
	 */
	private static abstract class LabelVisibleHelper {

		protected Set<Integer> idsVis = new HashSet<>();
		protected final int iTickCount;
		protected final int iShowIterval;
		protected final CommonRule commonRule;
		private int iFirstVisibleIndex = -1;
		private int iLastVisibleIndex = -1;

		private LabelVisibleHelper(int iTickCount, int iShowIterval) {
			this.iTickCount = iTickCount;
			this.iShowIterval = iShowIterval;
			this.commonRule = iShowIterval < 2 ? CommonRule.SHOW_INTERVAL_1 : CommonRule.SHOW_INTEVAL_2UP;
		}

		public abstract boolean isTickLabelVisible(int index);

		public boolean shouldTickLabelVisible(int index) {
			return commonRule.shouldVisible(index, iTickCount, iShowIterval);
		}

		public void addVisible(int index) {
			idsVis.add(index);
			if (iFirstVisibleIndex == -1 || index < iFirstVisibleIndex) {
				iFirstVisibleIndex = index;
			}
			if (iLastVisibleIndex == -1 || index > iLastVisibleIndex) {
				iLastVisibleIndex = index;
			}
		}

		int getLastVisibleIndex() {
			return iLastVisibleIndex;
		}

		public static LabelVisibleHelper createInstance(boolean bLabelVisible, boolean bCategory, final int iTickCount,
				final int iShowIterval) {
			if (!bLabelVisible) {
				return new LabelVisibleHelper(iTickCount, iShowIterval) {

					// case label invisible
					@Override
					public boolean isTickLabelVisible(int index) {
						return false;
					}

					@Override
					public boolean shouldTickLabelVisible(int index) {
						return false;
					}
				};
			} else if (bCategory) {
				return new LabelVisibleHelper(iTickCount, iShowIterval) {

					// case Category
					@Override
					public boolean isTickLabelVisible(int index) {
						return idsVis.contains(index);
					}
				};

			} else {
				return new LabelVisibleHelper(iTickCount, iShowIterval) {

					@Override
					public boolean isTickLabelVisible(int index) {
						return commonRule.shouldVisible(index, iTickCount, iShowIterval);
					}
				};
			}

		}

		private enum CommonRule {
			SHOW_INTERVAL_1 {

				@Override
				public boolean shouldVisible(int index, int iTickCount, int iShowIterval) {
					return !isIndexOutOfBound(index, iTickCount);
				}

			},
			SHOW_INTEVAL_2UP {

				@Override
				public boolean shouldVisible(int index, int iTickCount, int iShowIterval) {
					return !isIndexOutOfBound(index, iTickCount) && (index % iShowIterval == 0);
				}

			};

			public abstract boolean shouldVisible(int index, int iTickCount, int iShowIterval);

			private static boolean isIndexOutOfBound(int index, int iTickCount) {
				return index < 0 || index > iTickCount - 1;
			}
		}

	}

	/**
	 * Helper class for Tick Label Stagger. StaggeredHelper
	 */
	private static abstract class StaggeredHelper {

		private StaggeredHelper() {
		}

		public static StaggeredHelper createInstance(final boolean staggerEnabled, final int iTickCount,
				final int iLabelShowingInterval) {
			if (!staggerEnabled) {
				return new StaggeredHelper() {

					@Override
					public boolean isTickLabelStaggered(int index) {
						// default case, stagger disabled
						return false;
					}
				};
			} else if (iLabelShowingInterval < 2) {
				return new StaggeredHelper() {

					@Override
					public boolean isTickLabelStaggered(int index) {
						// case stagger enabled, iLabelShowingInterval==1
						if (isIndexOutOfBound(index, iTickCount)) {
							return false;
						}

						return index % 2 != 0;
					}

				};
			} else {
				return new StaggeredHelper() {

					@Override
					public boolean isTickLabelStaggered(int index) {
						// case stagger enabled, iLabelShowingInterval>1
						if (isIndexOutOfBound(index, iTickCount)) {
							return false;
						}

						return (index % iLabelShowingInterval == 0) && ((index / iLabelShowingInterval) % 2 != 0);
					}

				};
			}

		}

		private static boolean isIndexOutOfBound(int index, int iTickCount) {
			return index < 0 || index > iTickCount - 1;
		}

		public abstract boolean isTickLabelStaggered(int index);
	}

	/**
	 * Sets big number divisor for axis scale.
	 *
	 * @param divisor
	 * @since 2.6
	 */
	public void setBigNubmerDivisor(BigDecimal divisor) {
		if (divisor == null) {
			this.bIsBigNumber = false;
			this.bigNumberDivisor = null;
		} else {
			this.bIsBigNumber = true;
			this.bigNumberDivisor = divisor;
		}
	}

	/**
	 * Checks if the axis scale represents big number.
	 *
	 * @return boolean
	 * @since 2.6
	 */
	public boolean isBigNumber() {
		return this.bIsBigNumber;
	}

	/**
	 * Returns big number divisor of axis scale.
	 *
	 * @return big number divisor
	 * @since 2.6
	 */
	public BigDecimal getBigNumberDivisor() {
		return this.bigNumberDivisor;
	}
}
