/***********************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.computation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.eclipse.birt.chart.datafeed.IDataPointEntry;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.factory.RunTimeContext.StateKey;
import org.eclipse.birt.chart.internal.factory.DateFormatWrapperFactory;
import org.eclipse.birt.chart.internal.factory.IDateFormatWrapper;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.birt.chart.util.ChartUtil;

import com.ibm.icu.util.Calendar;

/**
 * A helper class for Legend computation.
 */
public final class LegendBuilder implements IConstants {
	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * inner class for legend data
	 */
	private static class LegendData {

		private final IDisplayServer xs;
		private final Chart cm;
		private final Legend lg;
		private final SeriesDefinition[] seda;
		private final RunTimeContext rtc;
		private final boolean bPaletteByCategory;
		private final double dItemHeight;
		private final Label la;
		private final double dSeparatorThickness;
		private final double dScale;
		private final Insets insCa;
		private final double maxWrappingSize;
		private final double dHorizontalSpacing;
		private final double dVerticalSpacing;
		private final double dSafeSpacing;
		private final double dHorizonalReservedSpace;
		private final double dShadowness;
		private final IChartComputation cComp;

		public LegendData(IDisplayServer xs, Chart cm, SeriesDefinition[] seda, RunTimeContext rtc) {
			this.xs = xs;
			this.cm = cm;
			this.lg = cm.getLegend();
			this.seda = seda;
			this.rtc = rtc;
			this.cComp = rtc.getState(StateKey.CHART_COMPUTATION_KEY);
			this.bPaletteByCategory = (lg.getItemType().getValue() == LegendItemType.CATEGORIES);

			this.la = goFactory.createLabel();
			la.setEllipsis(lg.getEllipsis());
			la.setCaption(goFactory.copyOf(lg.getText()));

			la.getCaption().setValue("X"); //$NON-NLS-1$
			ITextMetrics itm = cComp.getTextMetrics(xs, la, 0);
			this.dItemHeight = itm.getFullHeight();
			cComp.recycleTextMetrics(itm);

			ClientArea ca = lg.getClientArea();
			dSeparatorThickness = lg.getSeparator() != null ? lg.getSeparator().getThickness()
					: ca.getOutline().getThickness();

			dScale = xs.getDpiResolution() / 72d;
			insCa = goFactory.scaleInsets(ca.getInsets(), dScale);
			maxWrappingSize = lg.getWrappingSize() * dScale;

			dHorizontalSpacing = 3 * dScale;
			dVerticalSpacing = 3 * dScale;
			dSafeSpacing = 3 * dScale;
			this.dShadowness = 3 * dScale;

			dHorizonalReservedSpace = insCa.getLeft() + insCa.getRight() + 1.5 * dItemHeight + dHorizontalSpacing;
		}

		private boolean bMinSliceApplied;
		private Collection<Integer> filteredMinSliceEntry = Collections.emptySet();
		private double dAvailableWidth;
		private double dAvailableHeight;
		private List<LegendItemHints> legendItems = new ArrayList<>();
		private String sMinSliceLabel;
		private Label laTitle;

	}

	private static class InvertibleIterator<T> implements Iterator<T> {

		private boolean isInverse_ = false;
		private ListIterator<T> lit_ = null;
		private int index_ = -1;

		/**
		 * The constructor.
		 */
		public InvertibleIterator(List<T> tList, boolean isInverse, int index) {
			lit_ = tList.listIterator(index);
			isInverse_ = isInverse;
			if (isInverse) {
				index_ = lit_.previousIndex();
			} else {
				index_ = lit_.nextIndex();
			}
		}

		public InvertibleIterator(List<T> tList, boolean isInverse) {
			this(tList, isInverse, isInverse ? tList.size() : 0);
		}

		/**
		 * Methods to implement Iterator.
		 */
		@Override
		public boolean hasNext() {
			return isInverse_ ? lit_.hasPrevious() : lit_.hasNext();
		}

		@Override
		public final T next() throws NoSuchElementException {
			if (isInverse_) {
				index_ = lit_.previousIndex();
				return lit_.previous();
			} else {
				index_ = lit_.nextIndex();
				return lit_.next();
			}
		}

		@Override
		public void remove() {
		}

		/**
		 * Special Methods.
		 */
		public int getIndex() {
			return index_;
		}

	}

	public static class LabelItem implements EllipsisHelper.ITester {
		private final LegendData lgData;
		private final double dWrapping;
		private final Label la;
		private final Double fontHeight;
		private String text; // Text without considering about ellipsis
		private double dEllipsisWidth;
		private BoundingBox bb = null;
		private EllipsisHelper eHelper = null;
		private int iValidLen = 0;

		public LabelItem(LegendData lgData, Label la, double dWrapping) {
			this.lgData = lgData;
			this.la = la;
			this.dWrapping = dWrapping;
			la.getCaption().setValue(EllipsisHelper.ELLIPSIS_STRING);
			ITextMetrics itm = lgData.cComp.getTextMetrics(lgData.xs, la, 0);
			dEllipsisWidth = itm.getFullWidth();
			fontHeight = itm.getHeight();
			lgData.cComp.recycleTextMetrics(itm);
			eHelper = new EllipsisHelper(this, la.getEllipsis());
		}

		public LabelItem(LegendData lgData, Label la) {
			this(lgData, la, 0);
		}

		public void setText(String sText) throws ChartException {
			text = sText;
			updateLabel(sText);
		}

		public String getFullText() {
			return this.text;
		}

		public int getValidTextLen() {
			return this.iValidLen;
		}

		@Override
		public boolean testLabelVisible(String strNew, Object oPara) throws ChartException {
			double dWidthLimit = ((Double) oPara).doubleValue();
			updateLabel(strNew);
			return (getWidth() <= dWidthLimit);
		}

		/**
		 * Checks if current label text should use ellipsis to shorten the length.
		 *
		 * @param dWidthLimit the expected width to be reduced from the text
		 * @throws ChartException
		 */
		public boolean checkEllipsis(double dWidthLimit) throws ChartException {
			if (dWidthLimit < dEllipsisWidth) {
				iValidLen = 0;
				return false;
			}

			double dWidth = getWidth();

			if (dWidth <= dWidthLimit) {
				iValidLen = 0;
				return true;
			}

			boolean rst = eHelper.checkLabelEllipsis(text, new Double(dWidthLimit));
			if (!rst) {
				/*
				 * Fail to short it with ellipsis, so restore the fulltext.
				 */
				updateLabel(text);
			}
			iValidLen = eHelper.getVisibleCharCount();
			return rst;
		}

		@Override
		public double getWidth() throws ChartException {
			return bb.getWidth();
		}

		@Override
		public double getHeight() throws ChartException {
			return bb.getHeight();
		}

		/**
		 * get the display text of the label
		 *
		 * @return caption string
		 */
		public String getCaption() {
			return la.getCaption().getValue();
		}

		private void updateLabel(String strText) throws ChartException {
			la.getCaption().setValue(strText);
			bb = lgData.cComp.computeLabelSize(lgData.xs, la, dWrapping, fontHeight);
		}

	}

	private Size sz;

	/**
	 * initialize the dAvailableWidth/Height in legendData
	 *
	 * @param lgData
	 * @param cm
	 */
	private void initAvailableSize(LegendData lgData) throws ChartException {
		final Block bl = lgData.cm.getBlock();
		final Position lgPosition = lgData.lg.getPosition();
		final Bounds boFull = goFactory.scaleBounds(bl.getBounds(), lgData.dScale);
		final Insets ins = goFactory.scaleInsets(bl.getInsets(), lgData.dScale);
		final Insets lgIns = goFactory.scaleInsets(lgData.lg.getInsets(), lgData.dScale);

		int titleWPos = 0;
		int titleHPos = 0;

		final TitleBlock titleBlock = lgData.cm.getTitle();
		final Bounds titleBounds = goFactory.scaleBounds(titleBlock.getBounds(), lgData.dScale);

		if (titleBlock.isVisible()) {
			switch (titleBlock.getAnchor().getValue()) {
			case Anchor.EAST:
			case Anchor.WEST:
				titleWPos = 1;
				break;
			case Anchor.NORTH:
			case Anchor.NORTH_EAST:
			case Anchor.NORTH_WEST:
			case Anchor.SOUTH:
			case Anchor.SOUTH_EAST:
			case Anchor.SOUTH_WEST:
				titleHPos = 1;
				break;
			}
		}

		lgData.dAvailableWidth = boFull.getWidth() - ins.getLeft() - ins.getRight() - lgIns.getLeft() - lgIns.getRight()
				- titleBounds.getWidth() * titleWPos;

		lgData.dAvailableHeight = boFull.getHeight() - ins.getTop() - ins.getBottom() - lgIns.getTop()
				- lgIns.getBottom() - titleBounds.getHeight() * titleHPos;

		double dMaxPercent = lgData.lg.getMaxPercent();
		if (dMaxPercent < 0 || dMaxPercent > 1) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
					Messages.getString("exception.legend.orientation.InvalidMaxPercent"), //$NON-NLS-1$
					Messages.getResourceBundle(lgData.xs.getULocale()));

		}

		double dMaxLegendWidth = boFull.getWidth() * dMaxPercent;
		double dMaxLegendHeight = boFull.getHeight() * dMaxPercent;

		switch (lgPosition.getValue()) {
		case Position.LEFT:
		case Position.RIGHT:
		case Position.OUTSIDE:
			if (lgData.dAvailableWidth > dMaxLegendWidth) {
				lgData.dAvailableWidth = dMaxLegendWidth;
			}
			break;
		case Position.ABOVE:
		case Position.BELOW:
			if (lgData.dAvailableHeight > dMaxLegendHeight) {
				lgData.dAvailableHeight = dMaxLegendHeight;
			}
			break;
		}

	}

	/*
	 * Return the size of legend title. Note: lgData will be modified!
	 */
	private static Size getTitleSize(LegendData lgData) throws ChartException {
		Size size = null;
		LabelLimiter lbLimit = null;
		Label laTitle = lgData.lg.getTitle();

		if (laTitle != null && laTitle.isVisible()) {
			laTitle = goFactory.copyOf(laTitle);
			String sTitle = laTitle.getCaption().getValue();
			laTitle.getCaption().setValue(lgData.rtc.externalizedMessage(sTitle));
			int iTitlePos = lgData.lg.getTitlePosition().getValue();
			double shadow = lgData.dShadowness;
			double space = 2 * shadow;
			double percent = lgData.lg.getTitlePercent();
			double dMaxTWidth, dMaxTHeight;

			if (iTitlePos == Position.ABOVE || iTitlePos == Position.BELOW) {
				dMaxTWidth = lgData.dAvailableWidth - shadow;
				dMaxTHeight = lgData.dAvailableHeight * percent - space;
			} else {
				dMaxTWidth = lgData.dAvailableWidth * percent - space;
				dMaxTHeight = lgData.dAvailableHeight - shadow;
			}

			lbLimit = new LabelLimiter(dMaxTWidth, dMaxTHeight, 0);
			lbLimit.computeWrapping(lgData.xs, laTitle);
			lbLimit = lbLimit.limitLabelSize(lgData.cComp, lgData.xs, laTitle);
			size = SizeImpl.create(lbLimit.getMaxWidth() + space, lbLimit.getMaxHeight() + space);

			if (iTitlePos == Position.ABOVE || iTitlePos == Position.BELOW) {
				lgData.dAvailableHeight -= size.getHeight();
			} else {
				lgData.dAvailableWidth -= size.getWidth();
			}
		}

		lgData.laTitle = laTitle;
		return size;
	}

	/**
	 * Computes the size of the legend. Note the computation relies on the title
	 * size, so the title block must be layouted first before this.
	 *
	 * @param lg
	 * @param sea
	 *
	 * @throws ChartException
	 */
	public Size compute(IDisplayServer xs, Chart cm, SeriesDefinition[] seda, RunTimeContext rtc)
			throws ChartException {
		// THREE CASES:
		// 1. ALL SERIES IN ONE ARRAYLIST
		// 2. ONE SERIES PER ARRAYLIST
		// 3. ALL OTHERS

		LegendData lgData = new LegendData(xs, cm, seda, rtc);

		// Get maximum block width/height available
		initAvailableSize(lgData);

		// Calculate if minSlice applicable.
		boolean bMinSliceDefined = false;

		if (cm instanceof ChartWithoutAxes) {
			bMinSliceDefined = ((ChartWithoutAxes) cm).isSetMinSlice();
			lgData.sMinSliceLabel = ((ChartWithoutAxes) cm).getMinSliceLabel();
			if (lgData.sMinSliceLabel == null || lgData.sMinSliceLabel.length() == 0) {
				lgData.sMinSliceLabel = IConstants.UNDEFINED_STRING;
			} else {
				lgData.sMinSliceLabel = rtc.externalizedMessage(lgData.sMinSliceLabel);
			}
		}

		// calculate if need an extra legend item when minSlice defined.
		if (bMinSliceDefined && lgData.bPaletteByCategory && cm instanceof ChartWithoutAxes) {
			calculateExtraLegend(cm, rtc, lgData);
		}

		// consider legend title size.

		Size titleSize = getTitleSize(lgData);

		double[] size = null;

		Boolean bDataEmpty = null;
		if (rtc != null) {
			bDataEmpty = rtc.getState(RunTimeContext.StateKey.DATA_EMPTY_KEY);
		}
		if (bDataEmpty == null) {
			bDataEmpty = false;
		}

		if (!bDataEmpty) {
			// COMPUTATIONS HERE MUST BE IN SYNC WITH THE ACTUAL RENDERER
			ContentProvider cProvider = ContentProvider.newInstance(lgData);
			ContentPlacer cPlacer = ContentPlacer.newInstance(lgData);
			LegendItemHints lih;

			while ((lih = cProvider.nextContent()) != null) {
				if (!cPlacer.placeContent(lih)) {
					break;
				}
			}

			cPlacer.finishPlacing();
			size = cPlacer.getSize();
		}

		if (size == null) {
			// return SizeImpl.create( 0, 0 );
			size = new double[] { 0, 0 };
		}

		double dWidth = size[0], dHeight = size[1];

		if (titleSize != null) {
			int iTitlePos = lgData.lg.getTitlePosition().getValue();

			if (iTitlePos == Position.ABOVE || iTitlePos == Position.BELOW) {
				dWidth = Math.max(dWidth, titleSize.getWidth());
				dHeight = dHeight + titleSize.getHeight();
			} else {
				dWidth = dWidth + titleSize.getWidth();
				dHeight = Math.max(dHeight, titleSize.getHeight());
			}
		}

		if (rtc != null) {
			List<LegendItemHints> legendItems = lgData.legendItems;
			LegendItemHints[] liha = legendItems.toArray(new LegendItemHints[legendItems.size()]);

			// update context hints here.
			LegendLayoutHints lilh = new LegendLayoutHints(SizeImpl.create(dWidth, dHeight), titleSize, lgData.laTitle,
					lgData.bMinSliceApplied, lgData.sMinSliceLabel, liha);

			rtc.setLegendLayoutHints(lilh);
		}

		sz = SizeImpl.create(dWidth, dHeight);

		return sz;
	}

	// calculate if need an extra legend item when minSlice defined.
	private void calculateExtraLegend(Chart cm, RunTimeContext rtc, LegendData legendData) throws ChartException {
		Map<Series, LegendItemRenderingHints> renders = rtc.getSeriesRenderers();

		if (renders != null && !((ChartWithoutAxes) cm).getSeriesDefinitions().isEmpty()) {
			List<SeriesDefinition> sedList = ChartUtil.getAllOrthogonalSeriesDefinitions(cm);
			boolean started = false;

			for (SeriesDefinition sed : sedList) {
				List<Series> sdRuntimeSA = sed.getRunTimeSeries();

				for (Series seRuntime : sdRuntimeSA) {
					try {
						DataSetIterator dsiOrtho = new DataSetIterator(seRuntime.getDataSet());
						LegendItemRenderingHints lirh = renders.get(seRuntime);

						if (lirh == null) {
							return;
						}

						BaseRenderer br = lirh.getRenderer();
						Collection<Integer> fsa = br.getFilteredMinSliceEntry(dsiOrtho);

						if (fsa.size() > 0) {
							legendData.bMinSliceApplied = true;
						}

						if (!started) {
							started = true;
							legendData.filteredMinSliceEntry = fsa;
						} else {
							legendData.filteredMinSliceEntry.retainAll(fsa);

							if (legendData.filteredMinSliceEntry.size() == 0) {
								return;
							}
						}
					} catch (Exception ex) {
						throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, ex);
					}
				}
			}
		}

	}

	/**
	 * Returns a non empty value, if it is null or empty string, replace with
	 * specified value.
	 *
	 * @param value        specified value.
	 * @param defaultValue default return value.
	 * @return a non empty value.
	 */
	private static Object getNonEmptyValue(Object value, Object defaultValue) {
		if (value == null || value.toString().length() == 0) {
			return defaultValue;
		}

		return value;
	}

	private static abstract class ContentProvider {

		private ChartUtil.CacheDecimalFormat dfCache;
		protected final LegendData lgData;
		protected final boolean bNeedInvert;
		protected FormatSpecifier fs = null;

		protected ContentProvider(LegendData lgData) {
			this.lgData = lgData;
			this.bNeedInvert = needInvert(lgData.bPaletteByCategory, lgData.cm, lgData.seda);
			this.dfCache = new ChartUtil.CacheDecimalFormat(lgData.rtc.getULocale());
		}

		public static ContentProvider newInstance(LegendData lgData) throws ChartException {
			LegendItemType itemType = lgData.lg.getItemType();
			if (itemType.getValue() == LegendItemType.CATEGORIES) {
				return new CategoryContentProvider(lgData);
			} else if (itemType.getValue() == LegendItemType.SERIES) {
				return new ValueContentProvider(lgData);
			} else {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
						"exception.illegal.rendering.legend.itemtype", //$NON-NLS-1$
						new Object[] { itemType }, Messages.getResourceBundle(lgData.rtc.getULocale()));
			}

		}

		public abstract LegendItemHints nextContent() throws ChartException;

		protected String format(Object oText) throws ChartException {
			// Format numerical and datetime category data with default pattern
			// if no format
			// specified
			Object df = null;
			if (fs == null && oText instanceof Number) {
				String sPattern = ValueFormatter.getNumericPattern((Number) oText);
				df = dfCache.get(sPattern);
			}

			// apply user defined format if exists
			try {
				return ValueFormatter.format(oText, fs, lgData.rtc.getULocale(), df);
			} catch (ChartException e) {
				// ignore, use original text.
				return oText.toString();
			}

		}

		/**
		 * Check if the legend items need display in a inverted order (Stack Bar)
		 */
		private static boolean needInvert(final boolean bPaletteByCategory, final Chart cm,
				final SeriesDefinition[] seda) {
			boolean bNeedInvert = false; // return value

			if (!(cm instanceof ChartWithAxes)) {
				return false;
			}

			boolean bIsStacked = isStacked(seda);
			boolean bIsFliped = ((ChartWithAxes) cm).isTransposed();

			if (bPaletteByCategory) { // by Category
				bNeedInvert = bIsFliped;
			} else { // by Value
				bNeedInvert = (bIsStacked && !bIsFliped) || (!bIsStacked && bIsFliped);
			}

			return bNeedInvert;
		}

	}

	private static class CategoryContentProvider extends ContentProvider {

		private IDateFormatWrapper defaultDateFormat = null;
		private SeriesDefinition sdBase = null;
		private DataSetIterator dsiBase = null;
		private Series seBase = null;
		private int pos = -1;
		private boolean bMinSliceCreated = false;

		protected CategoryContentProvider(LegendData lgData) throws ChartException {
			super(lgData);
			sdBase = ChartUtil.getBaseSeriesDefinitions(lgData.cm).get(0);

			// OK TO ASSUME THAT 1 BASE RUNTIME SERIES EXISTS
			seBase = sdBase.getRunTimeSeries().get(0);

			dsiBase = createDataSetIterator(seBase, lgData.cm);

			fs = lgData.cm.getLegend().getFormatSpecifier();
			if (fs == null) {
				// Get pre-defined format specifier, this format specifier might
				// inherit and copy from container.
				if (lgData.cm instanceof ChartWithAxes) {
					ChartWithAxes cwa = (ChartWithAxes) lgData.cm;
					Axis xAxis = cwa.getAxes().get(0);
					fs = xAxis.getFormatSpecifier();
				} else {
					ChartWithoutAxes cwa = (ChartWithoutAxes) lgData.cm;
					fs = cwa.getSeriesDefinitions().get(0).getFormatSpecifier();
				}
			}

			// Get default formatter.
			int iDateTimeUnit = ChartUtil.computeDateTimeCategoryUnit(lgData.cm, dsiBase);

			if (iDateTimeUnit != IConstants.UNDEFINED) {
				defaultDateFormat = DateFormatWrapperFactory.getPreferredDateFormat(iDateTimeUnit,
						lgData.rtc.getULocale());
			}

			boolean bDataReverse = bNeedInvert;
			if (lgData.cm instanceof ChartWithAxes) {
				ChartWithAxes cwa = (ChartWithAxes) lgData.cm;
				bDataReverse = ChartUtil.XOR(bNeedInvert, cwa.isReverseCategory());
			}
			dsiBase.reverse(bDataReverse);

		}

		@Override
		public LegendItemHints nextContent() throws ChartException {
			if (dsiBase.hasNext()) {
				Object obj = dsiBase.next();

				obj = getNonEmptyValue(obj, IConstants.ONE_SPACE);

				// Skip invalid data
				while (!isValidValue(obj) && dsiBase.hasNext()) {
					obj = dsiBase.next();
				}

				pos++;

				// filter the not-used legend.
				if (lgData.bMinSliceApplied && lgData.filteredMinSliceEntry.contains(pos)) {
					return nextContent();
				} else {
					int index = bNeedInvert ? dsiBase.size() - 1 - pos : pos;
					return LegendItemHints.newCategoryEntry(format(obj), sdBase, seBase, index);
				}

			} else if (lgData.bMinSliceApplied && !bMinSliceCreated) {
				pos++;
				int index = bNeedInvert ? dsiBase.size() - 1 - pos : pos;
				bMinSliceCreated = true;
				return LegendItemHints.newMinSliceEntry(lgData.sMinSliceLabel, sdBase, seBase, index);
			} else {
				return null;
			}
		}

		@Override
		protected String format(Object oText) throws ChartException {
			if (defaultDateFormat != null && fs == null && oText instanceof Calendar) {
				return ValueFormatter.format(oText, fs, lgData.rtc.getULocale(), defaultDateFormat);
			} else {
				return super.format(oText);
			}
		}
	}

	private static class ValueContentProvider extends ContentProvider {

		private enum Status {
			WAIT_SD, WAIT_SERIES;
		}

		private final boolean bSeparator;
		private List<SeriesDefinition> alSed = null;
		private Iterator<SeriesDefinition> itSed = null;
		private List<Series> alSeries = null;
		private InvertibleIterator<Series> itSeries = null;
		private SeriesDefinition sed = null;
		private SeriesNameFormat snFormat = SeriesNameFormat.DEFAULT_FORMAT;
		private Status status;

		protected ValueContentProvider(LegendData lgData) {
			super(lgData);
			Legend lg = lgData.cm.getLegend();
			this.bSeparator = lg.getSeparator() == null || lg.getSeparator().isVisible();
			this.alSed = Arrays.asList(lgData.seda);
			this.itSed = new InvertibleIterator<>(alSed, bNeedInvert);
			this.status = Status.WAIT_SD;
			fs = lgData.cm.getLegend().getFormatSpecifier();
		}

		@Override
		public LegendItemHints nextContent() throws ChartException {
			switch (status) {
			case WAIT_SD:
				return visitSed();
			case WAIT_SERIES:
				return visitSeries();
			}
			return null;
		}

		private LegendItemHints visitSed() throws ChartException {
			if (itSed.hasNext()) {
				sed = itSed.next();
				if (fs == null) {
					snFormat = SeriesNameFormat.getSeriesNameFormat(sed, lgData.rtc.getULocale());
				}
				alSeries = sed.getRunTimeSeries();
				itSeries = new InvertibleIterator<>(alSeries, bNeedInvert);
				status = Status.WAIT_SERIES;

				if (needToShowGroupName(sed)) {
					return LegendItemHints.newGroupNameEntry(getGroupName(sed));
				} else {
					return visitSeries();
				}
			}

			return null;
		}

		private LegendItemHints visitSeries() throws ChartException {
			if (itSeries.hasNext()) {
				Series se = itSeries.next();
				String sItem = formatItemText(se.getSeriesIdentifier());
				String sValue = getValueText(se);
				return LegendItemHints.newEntry(sItem, sValue, sed, se, itSeries.getIndex());
			} else {
				this.status = Status.WAIT_SD;
				if (bSeparator && alSeries.size() > 0 && itSed.hasNext()) {
					return LegendItemHints.createSeperator();
				} else {
					return visitSed();
				}
			}
		}

		/**
		 * return the extra value text, if it exists and is visible
		 *
		 * @param cm
		 * @param se
		 * @return Value Text
		 * @throws ChartException
		 */
		private String getValueText(Series se) throws ChartException {
			String strValueText = null;

			DataSetIterator dsiBase = createDataSetIterator(se, lgData.cm);

			// Use first value for each series.
			if (dsiBase.hasNext()) {
				Object obj = dsiBase.next();

				// Skip invalid data
				while (!isValidValue(obj) && dsiBase.hasNext()) {
					obj = dsiBase.next();
				}

				try {
					strValueText = ValueFormatter.format(obj, null, lgData.rtc.getULocale(), null);
				} catch (ChartException ex) {
					strValueText = String.valueOf(obj);
				}

			}

			return strValueText;
		}

		private String formatItemText(Object oText) throws ChartException {
			String str;
			if (snFormat != SeriesNameFormat.DEFAULT_FORMAT) {
				str = snFormat.format(oText);
			} else {
				str = format(oText);
			}
			return lgData.rtc.externalizedMessage(str);
		}

		private boolean needToShowGroupName(SeriesDefinition sed) {
			if (alSed == null || alSed.size() <= 1 || sed.getQuery() == null || sed.getQuery().getDefinition() == null
					|| sed.getQuery().getDefinition().trim().length() == 0) {
				return false;
			}

			List<?> alRun = sed.getRunTimeSeries();
			if (alRun.size() < 1) {
				return false;
			}

			Series seDesign = sed.getDesignTimeSeries();
			Series seRun = (Series) alRun.get(0);

			if (seDesign == null || alRun.size() > 1) {
				return true;
			}

			return !seDesign.getSeriesIdentifier().equals(seRun.getSeriesIdentifier());

		}

		private String getGroupName(SeriesDefinition sed) throws ChartException {
			String sGN = ""; //$NON-NLS-1$
			Series seDesign = sed.getDesignTimeSeries();
			if (seDesign != null) {
				sGN = formatItemText(seDesign.getSeriesIdentifier());
			}
			return sGN;
		}

	}

	private static abstract class ContentPlacer {

		public static ContentPlacer newInstance(LegendData legendData) throws ChartException {
			Orientation orientation = legendData.lg.getOrientation();
			if (orientation.getValue() == Orientation.VERTICAL) {
				return new VerticalPlacer(legendData);
			} else if (orientation.getValue() == Orientation.HORIZONTAL) {
				return new HorizontalPlacer(legendData);
			} else {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
						"exception.illegal.rendering.orientation", //$NON-NLS-1$
						new Object[] { orientation }, Messages.getResourceBundle(legendData.rtc.getULocale()));
			}

		}

		protected LabelItem laiItem = null;
		protected LabelItem laiValue = null;
		protected final LegendData lgData;
		protected final boolean bIsShowValue;
		protected final boolean bIsLeftRight;
		protected double dSepThick = 0;
		protected final boolean bCategory;

		protected double dX = 0;
		protected double dY = 0;
		protected double dMaxW = 0;
		protected double dMaxH = 0;
		protected List<LegendItemHints> columnList = new ArrayList<>();
		protected List<LegendItemHints> gnList = new ArrayList<>();

		private SeriesDefinition sed = null;

		protected ContentPlacer(LegendData lgData) {
			this.lgData = lgData;
			this.bCategory = (lgData.lg.getItemType().getValue() == LegendItemType.CATEGORIES);
			this.bIsShowValue = !bCategory && lgData.lg.isShowValue();
			this.bIsLeftRight = (lgData.lg.getDirection().getValue() == Direction.LEFT_RIGHT);
			this.laiItem = new LabelItem(lgData, lgData.la, lgData.maxWrappingSize);
			if (!bCategory) {
				this.dSepThick = lgData.dSeparatorThickness
						+ (bIsLeftRight ? lgData.dHorizontalSpacing : lgData.dVerticalSpacing);
			}

		}

		public abstract boolean placeContent(LegendItemHints lih) throws ChartException;

		public abstract void finishPlacing();

		public abstract double[] getSize();

		protected Point computeContentSize(LegendItemHints lih) throws ChartException {
			Point size = null;

			if (lih.getType() == LegendItemHints.Type.LG_GROUPNAME) {
				laiItem.setText(lih.getItemText());
				double[] dsize = getItemSizeGN(laiItem, lgData, dX);
				size = new Point(dsize[0], dsize[1]);
			} else if (lih.getType() == LegendItemHints.Type.LG_ENTRY
					|| lih.getType() == LegendItemHints.Type.LG_MINSLICE) {
				laiItem.setText(lih.getItemText());
				if (bIsShowValue) {
					checkValueLabel(lih);
					laiValue.setText(lih.getValueText());
				}
				double[] dsize = getItemSize(laiItem, laiValue, bIsShowValue, lgData, dX);
				size = new Point(dsize[0], dsize[1]);
			}
			return size;

		}

		private void checkValueLabel(LegendItemHints lih) {
			if (sed != lih.getSeriesDefinition()) {
				Series series = lih.getSeriesDefinition().getSeries().get(0);
				Label laValue = goFactory.copyOf(series.getLabel());
				laValue.setEllipsis(1);
				this.laiValue = new LabelItem(lgData, laValue, lgData.maxWrappingSize);
			}
		}

		/**
		 * Check if the available size of legend can contain at least one legend item
		 *
		 * @param itemSize
		 * @param legendData
		 * @return
		 */
		protected static boolean hasPlaceForOneItem(Point itemSize, LegendData legendData) {
			return itemSize.getX() <= legendData.dAvailableWidth && itemSize.getY() <= legendData.dAvailableHeight;
		}

	}

	private static class HorizontalPlacer extends ContentPlacer {

		public HorizontalPlacer(LegendData legendData) {
			super(legendData);
		}

		@Override
		public void finishPlacing() {
			flushColumnList();
		}

		@Override
		public double[] getSize() {
			double dHeight = bIsLeftRight ? dMaxH + dY : dY;
			double dWidth = Math.max(dMaxW, dX);

			return new double[] { dWidth, dHeight };
		}

		@Override
		public boolean placeContent(LegendItemHints lih) throws ChartException {
			if (lih.getType() == LegendItemHints.Type.LG_SEPERATOR) {
				if (bIsLeftRight) {
					dX += dSepThick;
					lih.left(dX - dSepThick * 0.5);
					lih.top(dY);
					lih.itemHeight(dMaxH - lgData.dVerticalSpacing);
				} else {
					flushColumnList();
					dY += dSepThick;
					lih.top(dY - dSepThick * 0.5);
					lih.width(dMaxW);
				}
				columnList.add(lih);

				return true;
			} else {
				Point size = computeContentSize(lih);
				return placeContentWithSize(lih, size);
			}

		}

		private boolean placeContentWithSize(LegendItemHints lih, Point size) throws ChartException {
			if (!hasPlaceForOneItem(size, lgData)) {
				return false;
			}

			if (dY + size.getY() > lgData.dAvailableHeight + lgData.dSafeSpacing) {
				columnList.clear();
				return false;
			} else if (dX + size.getX() > lgData.dAvailableWidth + lgData.dSafeSpacing) {
				flushColumnList();
				size = computeContentSize(lih);
				return placeContentWithSize(lih, size);
			} else {
				dMaxH = Math.max(size.getY(), dMaxH);
				dX += size.getX();

				lih.validItemLen(laiItem.getValidTextLen());
				lih.left(dX - size.getX());
				lih.top(dY);

				if (lih.getType() == LegendItemHints.Type.LG_GROUPNAME) {
					gnList.add(lih);
					lih.width(size.getX());
					lih.itemHeight(size.getY() - lgData.dVerticalSpacing);
				} else {
					lih.width(size.getX() - lgData.dHorizonalReservedSpace);
					lih.itemHeight(laiItem.getHeight());

					if (bIsShowValue) {
						lih.valueHeight(laiValue.getHeight());
						lih.validValueLen(laiValue.getValidTextLen());
					}
				}
				columnList.add(lih);

				return true;
			}

		}

		private void flushColumnList() {
			if (columnList.size() > 0) {
				lgData.legendItems.addAll(columnList);
				processColumnList();
				columnList.clear();

				dMaxW = Math.max(dMaxW, dX);
				dY += dMaxH;
				dMaxH = 0;
				dX = 0;
			}
		}

		private void processColumnList() {
			for (LegendItemHints lih : gnList) {
				lih.itemHeight(dMaxH);
			}
			gnList.clear();
		}

	}

	private static class VerticalPlacer extends ContentPlacer {

		public VerticalPlacer(LegendData legendData) {
			super(legendData);
		}

		@Override
		public boolean placeContent(LegendItemHints lih) throws ChartException {
			if (lih.getType() == LegendItemHints.Type.LG_SEPERATOR) {
				if (bIsLeftRight) {
					flushColumnList();
					dX += dSepThick;
					lih.left(dX - dSepThick * 0.5);
					lih.itemHeight(dMaxH);
				} else {
					dY += dSepThick;
					lih.left(dX);
					lih.top(dY - dSepThick * 0.5);
					lih.width(dMaxW - lgData.dHorizontalSpacing);
				}
				columnList.add(lih);

				return true;
			} else {
				Point size = computeContentSize(lih);
				return placeContentWithSize(lih, size);
			}

		}

		private void flushColumnList() {
			if (columnList.size() > 0) {
				lgData.legendItems.addAll(columnList);
				columnList.clear();

				dMaxH = Math.max(dMaxH, dY);
				dX += dMaxW;
				dY = 0;
				dMaxW = 0;
			}
		}

		@Override
		public void finishPlacing() {
			flushColumnList();
		}

		private boolean placeContentWithSize(LegendItemHints lih, Point size) throws ChartException {
			if (!hasPlaceForOneItem(size, lgData)) {
				return false;
			}

			if (dX + size.getX() > lgData.dAvailableWidth + lgData.dSafeSpacing) {
				columnList.clear();
				return false;
			} else if (dY + size.getY() > lgData.dAvailableHeight + lgData.dSafeSpacing) {
				flushColumnList();
				// The label will be rendered in a new column, it needs to recompute
				// the available width of label again, if the width of label
				// is greater than remainder width of legend, the label will
				// be reduced with ellipsis.
				Point newSize = computeContentSize(lih);
				return placeContentWithSize(lih, newSize);
			} else {
				dMaxW = Math.max(size.getX(), dMaxW);
				dY += size.getY();

				lih.validItemLen(laiItem.getValidTextLen());
				lih.left(dX);
				lih.itemHeight(laiItem.getHeight());

				if (lih.getType() == LegendItemHints.Type.LG_GROUPNAME) {
					gnList.add(lih);
					lih.top(dY - size.getY() + lgData.insCa.getTop());
					lih.width(size.getX());
				} else {
					lih.top(dY - size.getY());
					lih.width(size.getX() - lgData.dHorizonalReservedSpace);
					if (bIsShowValue) {
						lih.valueHeight(laiValue.getHeight()).validValueLen(laiValue.getValidTextLen());
					}

				}
				columnList.add(lih);

				return true;
			}

		}

		@Override
		public double[] getSize() {
			double dWidth = dX;
			double dHeight = Math.max(dMaxH, dY);

			return new double[] { dWidth, dHeight };
		}

	}

	/**
	 * Returns the size computed previously.
	 *
	 * @return size
	 */
	public Size getSize() {
		return sz;
	}

	private static double[] getItemSize(LabelItem laiLegend, LabelItem laiValue, boolean bIsShowValue,
			LegendData legendData, double dX) throws ChartException {
		double dWidth = 0, dHeight = 0;

		laiLegend.checkEllipsis(getWidthLimit(dX, legendData));

		dWidth = laiLegend.getWidth() + legendData.dHorizonalReservedSpace;
		dHeight = legendData.insCa.getTop() + laiLegend.getHeight() + legendData.insCa.getBottom();

		if (bIsShowValue) {
			laiValue.checkEllipsis(legendData.dAvailableWidth - dX);

			dWidth = Math.max(dWidth, laiValue.getWidth());
			dHeight += laiValue.getHeight() + 2 * legendData.dScale;
		}

		return new double[] { dWidth, dHeight };
	}

	private static double[] getItemSizeGN(LabelItem laiLegend, LegendData legendData, double dX) throws ChartException {
		double dWidth = 0, dHeight = 0;

		laiLegend.checkEllipsis(legendData.dAvailableWidth - dX);

		dWidth = laiLegend.getWidth();
		dHeight = laiLegend.getHeight();

		return new double[] { dWidth, dHeight };
	}

	private static double getWidthLimit(double dX, LegendData legendData) {
		return legendData.dAvailableWidth - legendData.dHorizonalReservedSpace - dX;
	}

	private static boolean isValidValue(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof Double) {
			return !((Double) obj).isNaN() && !((Double) obj).isInfinite();
		} else if (obj instanceof String) {
			return ((String) obj).length() != 0;
		} else if (obj instanceof IDataPointEntry) {
			return ((IDataPointEntry) obj).isValid();
		}
		return true;
	}

	/**
	 * Check if the legend items need display in a inverted order (Stack Bar)
	 */
	private static boolean isStacked(final SeriesDefinition[] seda) {
		boolean bIsStack = true;

		for (int i = 0; i < seda.length; i++) {
			if (bIsStack) {
				// check if the chart is stacked
				for (Iterator<Series> iter = seda[i].getSeries().iterator(); iter.hasNext();) {
					Series series = iter.next();
					if (!series.isStacked()) {
						bIsStack = false;
						break;
					}
				}
			}
		}

		return bIsStack;
	}

	private static DataSetIterator createDataSetIterator(Series se, Chart cm) throws ChartException {
		DataSetIterator dsi = null;
		try {
			dsi = new DataSetIterator(se.getDataSet());
			// Reverse Legend items if needed
			if (cm instanceof ChartWithAxes) {
				dsi.reverse(((ChartWithAxes) cm).isReverseCategory());
			}
		} catch (Exception ex) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, ex);
		}
		return dsi;
	}

}
