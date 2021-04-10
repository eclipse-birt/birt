/***********************************************************************
 * Copyright (c) 2004,2005,2006,2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.extension.render;

import java.awt.Color;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IChartComputation;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext.StateKey;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * PieRenderer
 */
public final class PieRenderer {

	static final int UNKNOWN = 0;

	protected static final IGObjectFactory goFactory = GObjectFactory.instance();

	private static final int LOWER = 1;

	private static final int UPPER = 2;

	private static final double LEADER_TICK_MIN_SIZE = 10; // POINTS

	private static final int LESS = -1;

	private static final int MORE = 1;

	private static final int EQUAL = 0;

	private static final int MAX_NUMBER_SLICE = 100;

	private final Position lpDataPoint;

	private final Position lpSeriesTitle;

	private transient double dLeaderLength;

	private final LeaderLineStyle lls;

	/**
	 * Series thickness
	 */
	private transient final double dThickness;

	private transient double dExplosion = 0;

	private transient String sExplosionExpression = null;

	private final Pie pie;

	private final PieSeries ps;

	private final List<PieSlice> pieSliceList = new ArrayList<PieSlice>();

	/**
	 * Holds list of deferred planes (flat and curved) to be sorted before rendering
	 */
	private final List<IDrawable> deferredPlanes = new ArrayList<IDrawable>();

	private final Palette pa;

	private final Label laSeriesTitle;

	private final LineAttributes liaLL;

	private final LineAttributes liaEdges;

	private transient IDisplayServer xs = null;

	private transient IDeviceRenderer idr = null;

	private transient Bounds boTitleContainer = null;

	private transient Bounds boSeriesNoTitle = null;

	private transient Insets insCA = null;

	private transient Bounds boSetDuringComputation = null;

	private final boolean bPaletteByCategory;

	private transient boolean bBoundsAdjustedForInsets = false;

	private transient boolean bMinSliceDefined = false;

	private transient double dMinSlice = 0;

	private transient double dAbsoluteMinSlice = 0;

	private transient boolean bPercentageMinSlice = false;

	private transient int orginalSliceCount = 0;

	private transient double ratio = 0;

	private transient double rotation = 0;

	private final IChartComputation cComp;

	/**
	 * The constant variable is used to adjust start angle of plane for getting
	 * correct rendering order of planes.
	 * <p>
	 * 
	 * Note: Since its value is very little, it will not affect computing the
	 * coordinates of pie slice.
	 */
	private final double MIN_DOUBLE = 0.0000000001d;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine.extension/render"); //$NON-NLS-1$

	/**
	 * 
	 * @param cwoa
	 * @param pie
	 * @param dpha
	 * @param da
	 * @param pa
	 */
	PieRenderer(ChartWithoutAxes cwoa, Pie pie, DataPointHints[] dpha, double[] da, Palette pa) throws ChartException {
		this.pa = pa;
		this.pie = pie;
		this.cComp = pie.getRunTimeContext().getState(StateKey.CHART_COMPUTATION_KEY);

		ps = (PieSeries) pie.getSeries();
		sExplosionExpression = ps.getExplosionExpression();
		dExplosion = ps.getExplosion() * pie.getDeviceScale();
		dThickness = ((cwoa.getDimension() == ChartDimension.TWO_DIMENSIONAL_LITERAL) ? 0 : cwoa.getSeriesThickness())
				* pie.getDeviceScale();
		ratio = ps.isSetRatio() ? ps.getRatio() : 1;
		rotation = ps.isSetRotation() ? ps.getRotation() : 0;
		liaLL = ps.getLeaderLineAttributes();
		if (ps.getLeaderLineAttributes().isVisible()) {
			dLeaderLength = ps.getLeaderLineLength() * pie.getDeviceScale();
		} else {
			dLeaderLength = 0;
		}

		liaEdges = goFactory.createLineAttributes(goFactory.BLACK(), LineStyle.SOLID_LITERAL, 1);
		bPaletteByCategory = (cwoa.getLegend().getItemType() == LegendItemType.CATEGORIES_LITERAL);

		lpDataPoint = ps.getLabelPosition();
		lpSeriesTitle = ps.getTitlePosition();
		laSeriesTitle = goFactory.copyOf(ps.getTitle());
		laSeriesTitle.getCaption()
				.setValue(pie.getRunTimeContext().externalizedMessage(String.valueOf(ps.getSeriesIdentifier()))); // TBD:
		laSeriesTitle.getCaption().getFont()
				.setAlignment(pie.switchTextAlignment(laSeriesTitle.getCaption().getFont().getAlignment()));

		// call script BEFORE_DRAW_SERIES_TITLE
		final AbstractScriptHandler sh = pie.getRunTimeContext().getScriptHandler();
		ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_SERIES_TITLE, ps, laSeriesTitle,
				pie.getRunTimeContext().getScriptContext());
		pie.getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_SERIES_TITLE,
				laSeriesTitle);

		// APPLY
		// FORMAT
		// SPECIFIER
		lls = ps.getLeaderLineStyle();

		bMinSliceDefined = cwoa.isSetMinSlice();
		dMinSlice = cwoa.getMinSlice();
		bPercentageMinSlice = cwoa.isMinSlicePercent();

		double dTotal = 0;
		orginalSliceCount = da.length;
		for (int i = 0; i < da.length; i++) {
			if (da[i] < 0) {
				dTotal -= da[i]; // use negative values as absolute
			} else if (!Double.isNaN(da[i])) {
				dTotal += da[i];
			}
		}

		if (bMinSliceDefined) {
			if (bPercentageMinSlice) {
				dAbsoluteMinSlice = dMinSlice * dTotal / 100d;
			} else {
				dAbsoluteMinSlice = dMinSlice;
			}

			double residualPos = 0;
			double residualNeg = 0;
			DataPointHints dphPos = null;
			DataPointHints dphNeg = null;
			for (int i = 0; i < da.length; i++) {
				// filter null values.
				if (Double.isNaN(da[i])) {
					continue;
				}

				if (Math.abs(da[i]) >= Math.abs(dAbsoluteMinSlice)) {
					pieSliceList.add(new PieSlice(da[i], dpha[i], i, false));
				} else {
					if (da[i] >= 0) {
						residualPos += da[i];
						if (dphPos == null) {
							dphPos = dpha[i].getVirtualCopy();
						} else {
							dphPos.accumulate(dpha[i].getBaseValue(), dpha[i].getOrthogonalValue(),
									dpha[i].getSeriesValue(), dpha[i].getPercentileOrthogonalValue());
						}
					} else {
						residualNeg += da[i];
						if (dphNeg == null) {
							dphNeg = dpha[i].getVirtualCopy();
						} else {
							dphNeg.accumulate(dpha[i].getBaseValue(), dpha[i].getOrthogonalValue(),
									dpha[i].getSeriesValue(), dpha[i].getPercentileOrthogonalValue());
						}

					}

				}

			}

			String extSliceLabel = pie.getRunTimeContext().externalizedMessage(cwoa.getMinSliceLabel());
			if (dphPos != null) {
				dphPos.setBaseValue(extSliceLabel);
				dphPos.setIndex(orginalSliceCount);
				// neg and pos "other" slice share the same palette color
				pieSliceList.add(new PieSlice(residualPos, dphPos, orginalSliceCount, true));
			}
			if (dphNeg != null) {
				dphNeg.setBaseValue(extSliceLabel);
				dphNeg.setIndex(orginalSliceCount);
				pieSliceList.add(new PieSlice(residualNeg, dphNeg, orginalSliceCount, true));
			}
		} else {
			// Set max slice number to avoid unnecessary computation and memory
			for (int i = 0; i < da.length && i < MAX_NUMBER_SLICE; i++) {
				// filter null values.
				if (Double.isNaN(da[i])) {
					continue;
				}
				pieSliceList.add(new PieSlice(da[i], dpha[i], i, false));
			}
		}

		double startAngle = rotation;
		double originalStartAngle = rotation;
		if (dTotal == 0) {
			dTotal = 1;
		}

		if (ps.isClockwise()) {
			Collections.reverse(pieSliceList);
		}

		PieSlice slice = null;
		double totalAngle = 0d;
		for (Iterator<PieSlice> iter = pieSliceList.iterator(); iter.hasNext();) {
			slice = iter.next();
			double length = (Math.abs(slice.getPrimitiveValue()) / dTotal) * 360d;
			double percentage = (slice.getPrimitiveValue() / dTotal) * 100d;
			slice.setStartAngle(startAngle);
			slice.setOriginalStartAngle(originalStartAngle);
			slice.setSliceLength(length);
			slice.setPercentage(percentage);
			startAngle += length + MIN_DOUBLE;
			originalStartAngle += length;
			startAngle = wrapAngle(startAngle);
			originalStartAngle = wrapAngle(originalStartAngle);
			totalAngle += length;
		}

		// complement the last slice with residual angle
		// TODO What is this for?
		if (totalAngle > 0 && 360 - totalAngle > 0.001) // handle precision
		// loss during
		// computations
		{
			slice.setSliceLength(360 - slice.getStartAngle());
		}

		initExploded();
	}

	private double wrapAngle(double angle) {
		return ChartUtil.mathGE(angle, 360d) ? angle - 360 : angle;
	}

	/**
	 * 
	 * @param idr
	 * @throws ChartException
	 */
	private final void renderDataPoints(IDeviceRenderer idr) throws ChartException {
		final AbstractScriptHandler sh = pie.getRunTimeContext().getScriptHandler();
		int iTextRenderType = TextRenderEvent.RENDER_TEXT_IN_BLOCK;
		if (lpDataPoint.getValue() == Position.OUTSIDE) {
			// RENDER SHADOWS (IF ANY) FIRST
			for (PieSlice slice : pieSliceList) {
				if (slice.getLabel().getShadowColor() != null) {
					slice.renderLabel(idr, TextRenderEvent.RENDER_SHADOW_AT_LOCATION);
				}
			}
			iTextRenderType = TextRenderEvent.RENDER_TEXT_AT_LOCATION;
		}

		// RENDER ACTUAL TEXT CAPTIONS ON DATA POINTS
		for (PieSlice slice : pieSliceList) {
			if (slice.getLabel().isVisible()) {
				slice.renderLabel(idr, iTextRenderType);
			}

			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT_LABEL, slice.getDataPointHints(),
					slice.getLabel(), pie.getRunTimeContext().getScriptContext());
			pie.getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT_LABEL,
					slice.getLabel());
		}
	}

	/**
	 * 
	 * @param bo
	 */
	/**
	 * 
	 * @param bo
	 */
	private final void computeLabelBounds(Bounds bo, boolean isOutside) throws ChartException {
		// compute additional bottom tick size due to series thickness.

		for (PieSlice slice : pieSliceList) {
			slice.setBounds(bo);

			if (isOutside) {
				slice.computeLabelBoundOutside(lls, dLeaderLength, null);
			} else {
				slice.computeLabelBoundInside();
			}
		}
	}

	/**
	 * 
	 * LabelOverlapResover
	 */
	private static class LabelOverlapResover {
		// min distance (x) from label to pie
		private static final double DMINDIST = 5;
		// min space between labels
		private static final double HSPACE = 3;
		private static final double VSPACE = 3;

		private final LeaderLineStyle lls;
		private double dLeadLineLen = 0; // length of the lead line

		private final List<PieSlice> src_sliceList;
		private final double dLeftEdge, dRightEdge, dTopEdge, dBottomEdge;
		private int idRightFirst, idLeftFirst, idRightLast, idLeftLast;

		public LabelOverlapResover(LeaderLineStyle lls, List<PieSlice> sliceList, Bounds bo, double dLeaderLength) {
			this.dLeftEdge = bo.getLeft();
			this.dRightEdge = bo.getLeft() + bo.getWidth();
			this.dTopEdge = bo.getTop();
			this.dBottomEdge = bo.getTop() + bo.getHeight();

			this.src_sliceList = sliceList;
			this.lls = lls;
			if (lls == LeaderLineStyle.FIXED_LENGTH_LITERAL) {
				this.dLeadLineLen = dLeaderLength;
			}
		}

		private void seekForIndexes() {
			int len = src_sliceList.size();

			if (len == 0) {
				return;
			} else if (len == 1) {
				if (isLeftSideSlice(src_sliceList.get(0))) {
					this.idLeftFirst = 0;
					this.idLeftLast = 0;

					this.idRightLast = -1;
				} else {
					this.idRightFirst = 0;

					this.idLeftLast = -1;
				}

				return;
			}

			boolean bCurrentIsLeft = isLeftSideSlice(src_sliceList.get(0));
			boolean bLastFound = false;
			boolean bFirstFound = false;

			for (int i = 1; i < len; i++) {
				if (bCurrentIsLeft) {
					if (!isLeftSideSlice(src_sliceList.get(i))) {
						this.idLeftLast = i - 1;
						this.idRightLast = i;
						bCurrentIsLeft = false;
						bLastFound = true;
					}
				} else {
					if (isLeftSideSlice(src_sliceList.get(i))) {
						this.idRightFirst = i - 1;
						this.idLeftFirst = i;
						bCurrentIsLeft = true;
						bFirstFound = true;
					}
				}
			}

			if (!bFirstFound) {
				this.idLeftFirst = 0;
				this.idRightFirst = len - 1;
			}

			if (!bLastFound) {
				idRightLast = 0;
				idLeftLast = len - 1;
			}

		}

		private void processLeftSideLoop(LabelGroupList lList, int id0, int id1) {
			for (int i = id0; i <= id1; i++) {
				PieSlice slice = src_sliceList.get(i);

				if (!lList.isFull) {
					SliceLabel sLabel = new SliceLabel(slice, false);
					lList.addSliceLabel(sLabel, false);
				} else {
					break;
				}
			}

		}

		private void processLeftSide(int len) {
			LabelGroupList lList = new LabelGroupList(false);

			if (idLeftLast < 0) {
				return;
			}

			if (idLeftLast >= idLeftFirst) {
				processLeftSideLoop(lList, idLeftFirst, idLeftLast);
			} else {
				processLeftSideLoop(lList, idLeftFirst, len - 1);
				processLeftSideLoop(lList, 0, idLeftLast);
			}

			//
			LabelGroup lg = lList.head.lgNext;

			for (; !lg.isTail(); lg = lg.lgNext) {
				lg.updateSlices();
			}
		}

		private void processRightSideLoop(LabelGroupList rList, int id0, int id1) {
			for (int i = id0; i >= id1; i--) {
				PieSlice slice = src_sliceList.get(i);

				if (!rList.isFull) {
					SliceLabel sLabel = new SliceLabel(slice, true);
					rList.addSliceLabel(sLabel, true);
				} else {
					break;
				}
			}

		}

		private void processRightSide(int len) {
			LabelGroupList rList = new LabelGroupList(true);

			if (idRightLast < 0) {
				return;
			}

			if (idRightLast <= idRightFirst) {
				processRightSideLoop(rList, idRightFirst, idRightLast);
			} else {
				processRightSideLoop(rList, idRightFirst, 0);
				processRightSideLoop(rList, len - 1, idRightLast);
			}

			//
			LabelGroup lg = rList.head.lgNext;

			for (; !lg.isTail(); lg = lg.lgNext) {
				lg.updateSlices();
			}
		}

		public void resolve() {
			int len = src_sliceList.size();

			if (len > 0) {
				// search the start and end index
				seekForIndexes();

				// process the slices on the left side
				processLeftSide(len);

				// process the slices on the right side
				processRightSide(len);
			}
		}

		private static boolean isLeftSideSlice(PieSlice slice) {
			double angle = slice.getOriginalMidAngle() % 360;

			return (angle >= 90 && angle < 270);
		}

		/**
		 * a vertical list of label groups, from top to bottom LabelGroupList
		 */
		private class LabelGroupList {
			private boolean isFull = false;
			private LabelGroup head;
			private LabelGroup tail;

			LabelGroupList(boolean bRight) {
				if (bRight) {
					head = new RightLabelGroup(1);
					tail = new RightLabelGroup(2);
				} else {
					head = new LeftLabelGroup(1);
					tail = new LeftLabelGroup(2);
				}

				head.lgNext = tail;
				tail.lgLast = head;
			}

			private void append_simply(LabelGroup lg) {
				tail.lgLast.lgNext = lg;
				lg.lgLast = tail.lgLast;
				lg.lgNext = tail;
				tail.lgLast = lg;
			}

			private boolean limitLgTop(LabelGroup lg) {
				double minTop = lg.computeMinTop();
				double maxTop = lg.computeMaxTop();

				if (minTop > maxTop) {
					return false;
				} else {
					lg.top = Math.max(lg.top, lg.computeMinTop());
					lg.top = Math.min(lg.top, lg.computeMaxTop());
					return true;
				}
			}

			/**
			 * add a slice label to the bottom of the list
			 * 
			 * @param sLabel
			 * @param bRight
			 * @return true if label is added.
			 */
			public boolean addSliceLabel(SliceLabel sLabel, boolean bRight) {
				// create a label group with the label
				LabelGroup lg = createLabelGroup(sLabel, bRight);

				if (tail.lgLast == head) {
					// the first
					append_simply(lg);
					if (!limitLgTop(lg)) {
						return false;
					}
					lg.xStart = lg.getXStartClosestToPie();
					return true;
				} else {
					if (!limitLgTop(lg)) {
						return false;
					}
					limitLgTop(lg);
					lg.xStart = lg.getXStartClosestToPie();

					double last_bottom = tail.lgLast.top + tail.lgLast.height + VSPACE;
					double dy = last_bottom - lg.top;

					if (dy > 0) {
						double lg_top_old = lg.top;
						lg.top = last_bottom;
						append_simply(lg);

						double dMaxTop = lg.computeMaxTop();

						if (lg.top <= dMaxTop) {
							return true;
						}

						if (tail.lgLast.pushUp(lg.top - dMaxTop)) {
							return true;
						} else {
							tail.lgLast.delete();
							lg.top = lg_top_old;
							this.isFull = true;
							return false;
						}
					} else {
						// no overlapping
						append_simply(lg);
						return true;
					}
				}

			}

		}

		private LabelGroup createLabelGroup(SliceLabel sLabel, boolean bRight) {
			LabelGroup lg = bRight ? new RightLabelGroup(sLabel) : new LeftLabelGroup(sLabel);
			return lg;
		}

		/*
		 * a row of slice label on the right side of the pie
		 */
		private class RightLabelGroup extends LabelGroup {
			public RightLabelGroup(SliceLabel sLabel) {
				super(sLabel);
			}

			public RightLabelGroup(int type) {
				super(type);
			}

			@Override
			protected double getXStartLimit() {
				return dRightEdge - width - dLeadLineLen - DMINDIST;
			}

			protected double getPrefferredXStart() {
				double x1 = getXStartLimit();
				double x0 = getXStartClosestToPie();
				double dx = x1 - x0;
				double len = 0;
				if (dx > 0) {
					len = Math.abs(dx);
					len = Math.min(len, dLeadLineLen + DMINDIST);
				}
				return x0 + len * Math.signum(dx);
			}

			@Override
			public boolean addSliceLabel(SliceLabel sLabel) {
				double width_new = (label_list.size() == 0) ? 0 : width + HSPACE;
				width_new += sLabel.width;
				double height_new = Math.max(height, sLabel.height);
				label_list.add(sLabel);
				double xStart_new = this.getXStartClosestToPie();

				if (width_new > dRightEdge - xStart_new || height_new > dBottomEdge - top) {
					label_list.remove(label_list.size() - 1);
					return false;
				}

				this.width = width_new;
				this.xStart = xStart_new;
				this.height = height_new;
				return true;
			}

			@Override
			public double getXStartClosestToPie() {
				int len = label_list.size();
				double lspace = this.height / (len + 1);
				double y = top + lspace;
				double xStart = label_list.get(0).getXStartClosestToPie(y);

				for (int i = 1; i < len; i++) {
					xStart = Math.max(xStart, label_list.get(i).getXStartClosestToPie(y));
				}

				return xStart;
			}

			@Override
			public void updateSlices() {
				this.top = Math.min(this.top, dBottomEdge - this.height);

				int len = label_list.size();
				double lspace = this.height / (len + 1);
				double dYLeadLine = this.top + lspace;

				xStart = getPrefferredXStart();

				if (lls == LeaderLineStyle.FIXED_LENGTH_LITERAL) {
					double dLeft = this.xStart;

					for (int i = 0; i < len; i++) {
						SliceLabel sLabel = label_list.get(i);

						// update the label bounding
						sLabel.slice.labelBounding.setTop(top);
						sLabel.slice.labelBounding.setLeft(dLeft);

						// update the lead line
						sLabel.slice.loEnd.setX(dLeft);
						sLabel.slice.loEnd.setY(dYLeadLine);
						sLabel.slice.loStart.setX(this.xStart - dLeadLineLen);
						sLabel.slice.loStart.setY(dYLeadLine);

						dLeft += sLabel.width + HSPACE;
						dYLeadLine += lspace;
					}
				} else {
					double dRight = dRightEdge;

					for (int i = 0; i < len; i++) {
						SliceLabel sLabel = label_list.get(i);

						// update the label bounding
						sLabel.slice.labelBounding.setTop(top);
						sLabel.slice.labelBounding.setLeft(dRight - sLabel.width);

						// update the lead line
						sLabel.slice.loEnd.setX(dRight - sLabel.width);
						sLabel.slice.loEnd.setY(dYLeadLine);
						sLabel.slice.loStart.setX(this.xStart - dLeadLineLen);
						sLabel.slice.loStart.setY(dYLeadLine);

						dRight -= sLabel.width + HSPACE;
						dYLeadLine += lspace;
					}

				}
			}

		}

		/*
		 * a row of slice label on the left side of the pie
		 */
		private class LeftLabelGroup extends LabelGroup {
			public LeftLabelGroup(SliceLabel sLabel) {
				super(sLabel);
			}

			public LeftLabelGroup(int type) {
				super(type);
			}

			@Override
			protected double getXStartLimit() {
				return dLeftEdge + width + dLeadLineLen + DMINDIST;
			}

			protected double getPrefferredXStart() {
				double x1 = getXStartLimit();
				double x0 = getXStartClosestToPie();
				double dx = x1 - x0;
				double len = 0;
				if (dx < 0) {
					len = Math.abs(dx);
					len = Math.min(len, dLeadLineLen + DMINDIST);
				}
				return x0 + len * Math.signum(dx);
			}

			@Override
			public boolean addSliceLabel(SliceLabel sLabel) {
				double height_new = Math.max(this.height, sLabel.height);
				double width_new = (label_list.size() == 0) ? 0 : width + HSPACE;
				width_new += sLabel.width;
				label_list.add(sLabel);
				double xStart_new = this.getXStartClosestToPie();

				if (width_new > xStart_new - dLeftEdge || sLabel.height > dBottomEdge - this.top) {
					label_list.remove(label_list.size() - 1);
					return false;
				}

				this.width = width_new;
				this.xStart = xStart_new;
				this.height = height_new;

				return true;
			}

			@Override
			public double getXStartClosestToPie() {
				int len = label_list.size();
				double lspace = this.height / (len + 1);
				double y = top + lspace;
				double xStart = label_list.get(0).getXStartClosestToPie(y);

				for (int i = 1; i < len; i++) {
					xStart = Math.min(xStart, label_list.get(i).getXStartClosestToPie(y));
				}

				return xStart;
			}

			@Override
			public void updateSlices() {
				this.top = Math.min(this.top, dBottomEdge - this.height);

				int len = label_list.size();
				double lspace = this.height / (len + 1);
				double dYLeadLine = this.top + lspace;

				xStart = getPrefferredXStart();

				if (lls == LeaderLineStyle.FIXED_LENGTH_LITERAL) {
					double dRight = xStart;

					for (int i = 0; i < len; i++) {
						SliceLabel sLabel = label_list.get(i);

						// update the label bounding
						sLabel.slice.labelBounding.setTop(top);
						sLabel.slice.labelBounding.setLeft(dRight - sLabel.width);

						// update the lead line
						sLabel.slice.loEnd.setX(dRight);
						sLabel.slice.loEnd.setY(dYLeadLine);
						sLabel.slice.loStart.setX(this.xStart + dLeadLineLen);
						sLabel.slice.loStart.setY(dYLeadLine);

						dRight -= sLabel.width + HSPACE;
						dYLeadLine += lspace;
					}
				} else {
					// STRETCH_TO_SIDE
					double dLeft = dLeftEdge;

					for (int i = 0; i < len; i++) {
						SliceLabel sLabel = label_list.get(i);

						// update the label bounding
						sLabel.slice.labelBounding.setTop(top);
						sLabel.slice.labelBounding.setLeft(dLeft);

						// update the lead line
						sLabel.slice.loEnd.setX(dLeft + sLabel.width);
						sLabel.slice.loEnd.setY(dYLeadLine);
						sLabel.slice.loStart.setX(this.xStart);
						sLabel.slice.loStart.setY(dYLeadLine);

						dLeft += sLabel.width + HSPACE;
						dYLeadLine += lspace;
					}
				}
			}
		}

		/*
		 * a row of slice label
		 */
		private abstract class LabelGroup {
			private int type = 0; // 0 normal, 1 head, 2 tail
			private LabelGroup lgLast = null;
			private LabelGroup lgNext = null;

			protected List<SliceLabel> label_list = new ArrayList<SliceLabel>();
			protected double xStart, width = 0, top, height = 0;
			private boolean isFull = false;

			public LabelGroup(SliceLabel sLabel) {
				label_list.add(sLabel);
				this.top = sLabel.top_init;
				this.width = sLabel.width;
				this.xStart = sLabel.xStart;
				this.height = sLabel.height;
			}

			public LabelGroup(int type) {
				this.type = type;
			}

			public boolean isTail() {
				return (type == 2);
			}

			private void recomputeHeight() {
				int len = label_list.size();
				height = label_list.get(0).height;

				for (int i = 1; i < len; i++) {
					height = Math.max(height, label_list.get(i).height);
				}
			}

			private void removeLastLabel() {
				int len = label_list.size();
				if (len < 2) {
					return;
				}

				SliceLabel sLabel = label_list.get(len - 1);
				label_list.remove(len - 1);
				width -= sLabel.width + HSPACE;
				recomputeHeight();
				xStart = getXStartClosestToPie();
			}

			public abstract double getXStartClosestToPie();

			public double computeMinTop() {
				double dMinTop = dTopEdge;
				int len = label_list.size();

				if (len > 0) {
					SliceLabel sLabel = label_list.get(label_list.size() - 1);

					if (!sLabel.bUp) {
						double dx = getXStartLimit() - sLabel.slice.xDock;
						double dy = 0;
						if (dx != 0) {
							dy = dx / sLabel.dRampRate;
						}

						double lspace = height / (len + 1);
						dMinTop = sLabel.slice.yDock + dy - height + lspace;
					}
				}

				return dMinTop;
			}

			protected abstract double getXStartLimit();

			public double computeMaxTop() {
				double dMaxTop = dBottomEdge - height;

				int len = label_list.size();

				if (len > 0) {
					SliceLabel sLabel = label_list.get(0);

					if (sLabel.bUp) {
						double dx = getXStartLimit() - sLabel.slice.xDock;
						double dy = 0;

						if (dx != 0) {
							dy = dx / sLabel.dRampRate;
						}

						double lspace = height / (len + 1);
						dMaxTop = sLabel.slice.yDock + dy - lspace;
					}
				}

				return dMaxTop;
			}

			private double getBottomLast() {
				double dBottomLast = dTopEdge;

				if (lgLast != null) {
					dBottomLast = lgLast.top + lgLast.height + VSPACE;
				}

				return dBottomLast;
			}

			public boolean pushUp(double dy) {
				if (this.isFull) {
					return false;
				}

				double top_new = top - dy;
				double dTopMin = this.computeMinTop();
				double bottom_last = getBottomLast();

				if (lgLast == null || top_new >= bottom_last) {
					// head of the list || no overlapping
					top = Math.max(top_new, dTopMin);
					return (top_new >= dTopMin);
				} else {
					// top_new < bottom_last
					if (lgLast.pushUp(bottom_last - top_new)) {
						// push succeeded
						top = Math.max(top_new, dTopMin);
						return (top_new >= dTopMin);
					} else {
						// push failed
						bottom_last = getBottomLast(); // lgLast may have changed

						if (top - lgLast.top >= dy) {
							// if possible, merge myself to the last one
							if (lgLast.merge(this)) {
								return true;
							} else {
								top = Math.max(bottom_last, dTopMin);
								return false;
							}
						} else {
							if (!lgLast.merge(this)) {
								top = Math.max(bottom_last, dTopMin);
							}
							return false;
						}
					}
				}

			}

			public boolean merge(LabelGroup lg) {
				if (lg.label_list.size() > 1) {
					// only merge lg contains one label
					return false;
				} else if (lg.label_list.size() > 0) {
					SliceLabel sLabel = lg.label_list.get(0);

					if (!addSliceLabel(sLabel)) {
						this.isFull = true;
						return false;
					} else {
						if (this.top < this.computeMinTop() || this.top > this.computeMaxTop()) {
							removeLastLabel();
							this.isFull = true;
							return false;
						}
					}
				}

				// success
				lg.delete();
				return true;
			}

			// delete myself from the list
			public void delete() {
				if (this.type == 0) {
					this.lgLast.lgNext = this.lgNext;
					this.lgNext.lgLast = this.lgLast;
				}
			}

			public abstract boolean addSliceLabel(SliceLabel sLabel);

			public abstract void updateSlices();

			@Override
			public String toString() {
				StringBuffer sBuf = new StringBuffer();
				Iterator<SliceLabel> it = label_list.iterator();

				while (it.hasNext()) {
					SliceLabel sLabel = it.next();
					sBuf.append(sLabel.slice.categoryIndex);
					if (it.hasNext()) {
						sBuf.append(", "); //$NON-NLS-1$
					}
				}

				StringBuilder sb = new StringBuilder("{ "); //$NON-NLS-1$
				sb.append(sBuf.toString());
				sb.append(" }"); //$NON-NLS-1$
				return sb.toString();
			}
		}

		/*
		 * wrapping class of slice label
		 */
		private class SliceLabel {
			private boolean bRight = true;
			private boolean bUp = true;
			private final PieSlice slice;
			private double xStart;
			private final double width;
			private final double height;
			private final double top_init;
			private double dRampRate;

			public SliceLabel(PieSlice slice, boolean bRight) {
				this.slice = slice;
				this.bRight = bRight;
				this.width = slice.labelBounding.getWidth();
				this.height = slice.labelBounding.getHeight();
				this.top_init = slice.labelBounding.getTop();

				computeRampRate();

				if (!bRight) {
					xStart = slice.xDock - DMINDIST - dLeadLineLen;
				} else {
					xStart = slice.xDock + DMINDIST + dLeadLineLen;
				}
			}

			public double getXStartClosestToPie(double y) {
				double dy = y - slice.yDock;
				double dx = dy * dRampRate;

				if ((bUp && dy <= 0) || (!bUp && dy >= 0)) {
					dx = 0;
				}

				if (!bRight) {
					return slice.xDock - DMINDIST - dLeadLineLen + dx;
				} else {
					return slice.xDock + DMINDIST + dLeadLineLen + dx;
				}
			}

			private void computeRampRate() {
				double angle = slice.getdMidAngle() % 360;
				dRampRate = Math.tan(Math.toRadians(angle));

				if (angle > 180 && angle < 360) {
					bUp = false;
				}
			}

		}

	}

	private void resolveOverlap() {
		new LabelOverlapResover(lls, pieSliceList, boSeriesNoTitle, dLeaderLength).resolve();
	}

	/**
	 * 
	 * @param bo
	 * @param boAdjusted
	 * @param ins
	 * @return
	 * @throws IllegalArgumentException
	 */
	private final Insets adjust(Bounds bo, Bounds boAdjusted, Insets ins) throws ChartException {
		computeLabelBounds(boAdjusted, true);
		ins.set(0, 0, 0, 0);
		double dDelta = 0;
		for (Iterator<PieSlice> iter = pieSliceList.iterator(); iter.hasNext();) {
			PieSlice slice = iter.next();
			BoundingBox bb = slice.getLabelBounding();
			if (bb.getLeft() < bo.getLeft()) {
				dDelta = bo.getLeft() - bb.getLeft();
				if (ins.getLeft() < dDelta) {
					ins.setLeft(dDelta);
				}
			}
			if (bb.getTop() < bo.getTop()) {
				dDelta = bo.getTop() - bb.getTop();
				if (ins.getTop() < dDelta) {
					ins.setTop(dDelta);
				}
			}
			if (bb.getLeft() + bb.getWidth() > bo.getLeft() + bo.getWidth()) {
				dDelta = bb.getLeft() + bb.getWidth() - bo.getLeft() - bo.getWidth();
				if (ins.getRight() < dDelta) {
					ins.setRight(dDelta);
				}
			}
			if (bb.getTop() + bb.getHeight() > bo.getTop() + bo.getHeight()) {
				dDelta = bb.getTop() + bb.getHeight() - bo.getTop() - bo.getHeight();
				if (ins.getBottom() < dDelta) {
					ins.setBottom(dDelta);
				}
			}
		}
		return ins;
	}

	/**
	 * 
	 * @param bo
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	final void computeInsets(Bounds bo) throws ChartException {
		boSetDuringComputation = goFactory.copyOf(bo);
		xs = pie.getXServer();

		// ALLOCATE SPACE FOR THE SERIES TITLE
		boTitleContainer = null;
		if (laSeriesTitle.isVisible()) {
			if (lpSeriesTitle == null) {
				throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.UNDEFINED_VALUE,
						"exception.unspecified.visible.series.title", //$NON-NLS-1$
						Messages.getResourceBundle(pie.getRunTimeContext().getULocale()));
			}

			final BoundingBox bb = cComp.computeBox(xs, IConstants.BELOW, laSeriesTitle, 0, 0);
			boTitleContainer = goFactory.createBounds(0, 0, 0, 0);

			switch (lpSeriesTitle.getValue()) {
			case Position.BELOW:
				bo.setHeight(bo.getHeight() - bb.getHeight());
				boTitleContainer.set(bo.getLeft(), bo.getTop() + bo.getHeight(), bo.getWidth(), bb.getHeight());
				break;
			case Position.ABOVE:
				boTitleContainer.set(bo.getLeft(), bo.getTop(), bo.getWidth(), bb.getHeight());
				bo.setTop(bo.getTop() + bb.getHeight());
				bo.setHeight(bo.getHeight() - bb.getHeight());
				break;
			case Position.LEFT:
				bo.setWidth(bo.getWidth() - bb.getWidth());
				boTitleContainer.set(bo.getLeft(), bo.getTop(), bb.getWidth(), bo.getHeight());
				bo.setLeft(bo.getLeft() + bb.getWidth());
				break;
			case Position.RIGHT:
				bo.setWidth(bo.getWidth() - bb.getWidth());
				boTitleContainer.set(bo.getLeft() + bo.getWidth(), bo.getTop(), bb.getWidth(), bo.getHeight());
				break;
			default:
				throw new IllegalArgumentException(Messages.getString("exception.illegal.pie.series.title.position", //$NON-NLS-1$
						new Object[] { lpSeriesTitle }, pie.getRunTimeContext().getULocale()));
			}
		}

		boSeriesNoTitle = goFactory.copyOf(bo);

		ChartWithoutAxes cwa = (ChartWithoutAxes) pie.getModel();
		if (cwa.isSetCoverage()) {
			double rate = cwa.getCoverage();
			double ww = 0.5 * (1d - rate) * bo.getWidth();
			double hh = 0.5 * (1d - rate) * bo.getHeight();
			insCA = goFactory.createInsets(hh, ww, hh, ww);
		} else {

			if (lpDataPoint == Position.OUTSIDE_LITERAL) {
				if (ps.getLabel().isVisible()) // FILTERED FOR PERFORMANCE
												// GAIN
				{
					// ADJUST THE BOUNDS TO ACCOMODATE THE DATA POINT LABELS +
					// LEADER LINES RENDERED OUTSIDE
					// Bounds boBeforeAdjusted = BoundsImpl.copyInstance( bo );
					Bounds boAdjusted = goFactory.copyOf(bo);
					Insets insTrim = goFactory.createInsets(0, 0, 0, 0);
					do {
						adjust(bo, boAdjusted, insTrim);
						boAdjusted.adjust(insTrim);
					} while (!insTrim.areLessThan(0.5) && boAdjusted.getWidth() > 0 && boAdjusted.getHeight() > 0);
					bo = boAdjusted;
				}
			} else if (lpDataPoint == Position.INSIDE_LITERAL) {
				if (ps.getLabel().isVisible()) // FILTERED FOR PERFORMANCE
												// GAIN
				{
					computeLabelBounds(bo, false);
				}
			} else {
				throw new IllegalArgumentException(MessageFormat.format(
						Messages.getResourceBundle(pie.getRunTimeContext().getULocale())
								.getString("exception.invalid.datapoint.position.pie"), //$NON-NLS-1$
						new Object[] { lpDataPoint })

				);
			}
			insCA = goFactory.createInsets(bo.getTop() - boSetDuringComputation.getTop(),
					bo.getLeft() - boSetDuringComputation.getLeft(),
					boSetDuringComputation.getTop() + boSetDuringComputation.getHeight()
							- (bo.getTop() + bo.getHeight()),
					boSetDuringComputation.getLeft() + boSetDuringComputation.getWidth()
							- (bo.getLeft() + bo.getWidth()));
		}

		bBoundsAdjustedForInsets = false;
	}

	/**
	 * 
	 * @return
	 */
	final Insets getFittingInsets() {
		return insCA;
	}

	/**
	 * 
	 * @param insCA
	 */
	final void setFittingInsets(Insets insCA) throws ChartException {
		this.insCA = insCA;
		if (!bBoundsAdjustedForInsets) // CHECK IF PREVIOUSLY ADJUSTED
		{
			bBoundsAdjustedForInsets = true;
			boSetDuringComputation.adjust(insCA);
		}

		if (lpDataPoint == Position.OUTSIDE_LITERAL) {
			if (ps.getLabel().isVisible()) // FILTERED FOR PERFORMANCE GAIN
			{
				computeLabelBounds(boSetDuringComputation, true);
			}
		} else if (lpDataPoint == Position.INSIDE_LITERAL) {
			if (ps.getLabel().isVisible()) // FILTERED FOR PERFORMANCE GAIN
			{
				computeLabelBounds(boSetDuringComputation, false);
			}
		}
	}

	/**
	 * 
	 * @param idr
	 * @param bo
	 * @throws ChartException
	 */
	public final void render(IDeviceRenderer idr, Bounds bo) throws ChartException {
		bo.adjust(insCA);

		xs = idr.getDisplayServer();
		this.idr = idr;

		final AbstractScriptHandler sh = pie.getRunTimeContext().getScriptHandler();

		double w = bo.getWidth() / 2d - dExplosion;
		double h = bo.getHeight() / 2d - dExplosion - dThickness / 2d;

		double xc = bo.getLeft() + bo.getWidth() / 2d;
		double yc = bo.getTop() + bo.getHeight() / 2d;

		if (ratio > 0 && w > 0) {
			if (h / w > ratio) {
				h = w * ratio;
			} else if (h / w < ratio) {
				w = h / ratio;
			}
		}

		// detect invalid rendering size.
		if (w > 0 && h > 0) {
			// RENDER THE INSIDE OF THE PIE SLICES AS DEFERRED PLANES (FLAT AND
			// CURVED)
			if (dThickness > 0) {
				for (Iterator<PieSlice> iter = pieSliceList.iterator(); iter.hasNext();) {
					PieSlice slice = iter.next();
					Fill fPaletteEntry = null;
					if (bPaletteByCategory) {
						fPaletteEntry = getPaletteColor(slice.getCategoryIndex(), slice.getDataPointHints());
					} else {
						fPaletteEntry = getPaletteColor(pie.getSeriesDefinition().getRunTimeSeries().indexOf(ps),
								slice.getDataPointHints());
					}
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_ELEMENT, slice.getDataPointHints(),
							fPaletteEntry);
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, slice.getDataPointHints(),
							fPaletteEntry, pie.getRunTimeContext().getScriptContext());
					pie.getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_ELEMENT,
							slice.getDataPointHints());
					pie.getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT,
							slice.getDataPointHints());

					slice.render(goFactory.createLocation(xc, yc), goFactory.createLocation(0, dThickness),
							SizeImpl.create(w, h), fPaletteEntry, LOWER);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_ELEMENT, slice.getDataPointHints(),
							fPaletteEntry);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, slice.getDataPointHints(),
							fPaletteEntry, pie.getRunTimeContext().getScriptContext());
					pie.getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_ELEMENT,
							slice.getDataPointHints());
					pie.getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT,
							slice.getDataPointHints());
				}

				// SORT AND ACTUALLY RENDER THE PLANES PREVIOUSLY WRITTEN AS
				// DEFERRED
				sortAndRenderPlanes();
			}

			// RENDER THE UPPER SECTORS ON THE PIE SLICES (DON'T CARE ABOUT
			// THE ORDER)
			for (Iterator<PieSlice> iter = pieSliceList.iterator(); iter.hasNext();) {
				PieSlice slice = iter.next();
				Fill fPaletteEntry = null;
				if (bPaletteByCategory) {
					fPaletteEntry = getPaletteColor(slice.getCategoryIndex(), slice.getDataPointHints());
				} else {
					fPaletteEntry = getPaletteColor(pie.getSeriesDefinition().getRunTimeSeries().indexOf(ps),
							slice.getDataPointHints());
				}
				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, slice.getDataPointHints(),
						fPaletteEntry, pie.getRunTimeContext().getScriptContext());
				pie.getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT,
						slice.getDataPointHints());
				slice.render(goFactory.createLocation(xc, yc), goFactory.createLocation(0, dThickness),
						SizeImpl.create(w, h), fPaletteEntry, UPPER);
				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, slice.getDataPointHints(),
						fPaletteEntry, pie.getRunTimeContext().getScriptContext());
				pie.getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT,
						slice.getDataPointHints());
			}

		}

		// RENDER THE SERIES TITLE NOW
//		ScriptHandler.callFunction( sh,
//				ScriptHandler.BEFORE_DRAW_SERIES_TITLE,
//				ps,
//				laSeriesTitle,
//				pie.getRunTimeContext( ).getScriptContext( ) );
//		pie.getRunTimeContext( )
//				.notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_SERIES_TITLE,
//						laSeriesTitle );
//		laDataPoint = LabelImpl.copyInstance( ps.getLabel( ) );
		if (laSeriesTitle.isVisible()) {
			final TextRenderEvent tre = ((EventObjectCache) idr)
					.getEventObject(WrappedStructureSource.createSeriesTitle(ps, laSeriesTitle), TextRenderEvent.class);
			tre.setLabel(laSeriesTitle);
			tre.setBlockBounds(boTitleContainer);
			tre.setBlockAlignment(null);
			tre.setAction(TextRenderEvent.RENDER_TEXT_IN_BLOCK);
			idr.drawText(tre);
		}
		ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_SERIES_TITLE, ps, laSeriesTitle,
				pie.getRunTimeContext().getScriptContext());
		pie.getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_SERIES_TITLE,
				laSeriesTitle);

		// LASTLY, RENDER THE DATA POINTS
		if (ps.getLabel().isVisible()) { // IF NOT VISIBLE, DON'T DO ANYTHING
			try {
				if (ps.getLabel().getCaption().getFont().getRotation() == 0) {
					if (lpDataPoint == Position.OUTSIDE_LITERAL) {
						resolveOverlap();
					}
				}
				renderDataPoints(idr);
			} catch (ChartException rex) {
				logger.log(rex);
				// Throw the exception to engine at runtime and engine will
				// display the exception to user.
				throw rex;
			}
		}
	}

	/**
	 * Add curved planes to a list for deferring to draw them.
	 * 
	 * @param planesList            the list is used to receive the planes.
	 * @param startAngle            angle of pie slice, normally it should be the
	 *                              start angle of pie slice.
	 * @param angleExtent           slice length.
	 * @param areBentOrTwistedCurve
	 * @param dX1
	 * @param dX2
	 * @param isInner
	 * @return created drawable object.
	 */
	private final IDrawable deferCurvedPlane(List<IDrawable> list, double startAngle, double angleExtent,
			AreaRenderEvent areBentOrTwistedCurve, double dX1, double dX2, boolean isInner) {
		double newAngle = convertAngleForRenderingOrder(startAngle, startAngle + angleExtent);
		IDrawable drawable = new CurvedPlane(newAngle, areBentOrTwistedCurve, isInner);
		list.add(drawable);
		return drawable;
	}

	/**
	 * Defer to draw curved outline.
	 * 
	 * @param startAngle            angle of pie slice, normally it should be the
	 *                              start angle of pie slice.
	 * @param angleExtent           length of pie slice.
	 * @param areBentOrTwistedCurve outline group.
	 * @param isInner
	 * @param lre
	 * @param planesList            the list is used to receive the planes.
	 * 
	 * @return drawable object.
	 */
	private final IDrawable deferCurvedOutline(List<IDrawable> list, double startAngle, double angleExtent,
			AreaRenderEvent areBentOrTwistedCurve, boolean isInner, LineRenderEvent lre) {
		double newAngle = convertAngleForRenderingOrder(startAngle, startAngle + angleExtent);
		return new CurvedPlane(newAngle, areBentOrTwistedCurve, lre, isInner);
	}

	/**
	 * Add flat planes to a list for deferring to draw them.
	 * 
	 * @param planesList   the list is used to receive the planes.
	 * @param angle        angle of pie slice, normally it should be the start angle
	 *                     of pie slice.
	 * @param isSliceStart indicates if the flat plane is start of slice.
	 * @param daXPoints
	 * @param daYPoints
	 * @param cd
	 */
	private final void deferFlatPlane(List<IDrawable> planesList, double angle, boolean isSliceStart,
			double[] daXPoints, double[] daYPoints, Fill cd, DataPointHints dph) {
		// Here just plus/subtract 0.01 degree to make adjacent flat plane of
		// slice get correct rendering orders.
		double newAngle = isSliceStart ? angle + 0.01 : angle - 0.01;
		newAngle = convertAngleForRenderingOrder(newAngle, newAngle);
		planesList.add(new FlatPlane(newAngle, daXPoints, daYPoints, cd, dph));
	}

	/**
	 * Convert angle of pie slice for getting correct rendering order before
	 * rendering each pie slice.
	 * 
	 * @param startAngle angle of pie slice, normally it should be the start angle
	 *                   of pie slice.
	 * @param endAngle   end angle of pie slice.
	 * @return adjusted angle.
	 */
	private double convertAngleForRenderingOrder(double startAngle, double endAngle) {
		if (!ChartUtil.mathEqual(startAngle, endAngle)) {
			double sAngle = startAngle % 360;
			double eAngle = endAngle % 360;

			// If end angle equals 0, it should be infinitely near to 360 angle,
			// we set it as 359.99.
			if (ChartUtil.mathEqual(eAngle, 0d)) {
				eAngle = 359.99d;
			}

			// If computed start angle is greater than computed end angle,
			// adjust computed start angle.
			if (ChartUtil.mathGT(sAngle, eAngle)) {
				sAngle = sAngle - 360d;
			}

			if (sAngle < 90 && eAngle >= 90) {
				return 90;
			} else if (sAngle < 270 && eAngle >= 270) {
				return 270;
			}
		}

		double newAngle = wrapAngle((startAngle + endAngle) / 2);
		if (newAngle < 180) {
			newAngle = 90 + Math.abs(newAngle - 90);
		} else {
			newAngle = 270 - Math.abs(newAngle - 270);
		}
		return newAngle;
	}

	/**
	 * Sort all planes and draw them.
	 * 
	 * @throws ChartException
	 */
	private final void sortAndRenderPlanes() throws ChartException {
		// Revised rendering algorithm of planes, no need to render different
		// planes in different steps again.
		renderPlanes(deferredPlanes);
		deferredPlanes.clear();
	}

	/**
	 * Render planes.
	 * 
	 * @param planesList the list contains plane objects.
	 * @throws ChartException
	 */
	private void renderPlanes(List<IDrawable> planesList) throws ChartException {
		IDrawable[] planes = planesList.toArray(new IDrawable[] {});
		Arrays.sort(planes, new Comparator<IDrawable>() {

			/**
			 * 
			 * @param arg0
			 * @param arg1
			 * @return 1 if arg0 great than arg1, -1 if arg0 less than arg1, 0 if arg0
			 *         equals arg1.
			 */
			public int compare(IDrawable arg0, IDrawable arg1) {
				double angleA = 0d;
				double angleB = 0d;
				if (arg0 instanceof FlatPlane) {
					angleA = ((FlatPlane) arg0).getAngle();
				} else if (arg0 instanceof CurvedPlane) {
					angleA = ((CurvedPlane) arg0).getAngle();
				}

				if (arg1 instanceof FlatPlane) {
					angleB = ((FlatPlane) arg1).getAngle();
				} else if (arg1 instanceof CurvedPlane) {
					angleB = ((CurvedPlane) arg1).getAngle();
				}

				int result = Double.compare(angleA, angleB);
				if (result == 0 && arg0 instanceof CurvedPlane && arg1 instanceof CurvedPlane) {
					// It means these two curved planes are inner curved plane
					// and outer curved plane of a pie slice, it needs to adjust
					// their order according to their position to user view.
					// If angle is between 0 - 180 angle, the outer curved plane
					// should be rendered first. If angle is between 180 -0
					// 360, the inner curved plane should be rendered first.
					if ((((CurvedPlane) arg0).isInnerPlane() && !((CurvedPlane) arg1).isInnerPlane())) {
						if (angleA >= 0 && angleA < 180) {
							// Rendering outer curved plane first
							return 1;
						} else {
							return -1;
						}
					} else if (!(((CurvedPlane) arg0).isInnerPlane() && ((CurvedPlane) arg1).isInnerPlane())) {
						if (angleA >= 0 && angleA < 180) {
							// Rendering outer curved plane first
							return -1;
						} else {
							return 1;
						}
					}
				}
				return result;
			}

		});
		for (int i = 0; i < planes.length; i++) {
			IDrawable id = planes[i];
			id.draw();
		}
	}

	private final ColorDefinition getSliceOutline(Fill f) {
		if (ps.getSliceOutline() == null) {
			if (f instanceof ColorDefinition) {
				return goFactory.darker((ColorDefinition) f);
			} else {
				return goFactory.TRANSPARENT();
			}
		}
		return goFactory.copyOf(ps.getSliceOutline());
	}

	private void initExploded() {

		if (sExplosionExpression == null) {
			return;
		}

		for (PieSlice slice : pieSliceList) {
			try {
				pie.getRunTimeContext().getScriptHandler().registerVariable(ScriptHandler.BASE_VALUE,
						slice.getDataPointHints().getBaseValue());
				pie.getRunTimeContext().getScriptHandler().registerVariable(ScriptHandler.ORTHOGONAL_VALUE,
						slice.getDataPointHints().getOrthogonalValue());
				pie.getRunTimeContext().getScriptHandler().registerVariable(ScriptHandler.SERIES_VALUE,
						slice.getDataPointHints().getSeriesValue());

				Object obj = pie.getRunTimeContext().getScriptHandler().evaluate(sExplosionExpression);

				if (obj instanceof Boolean) {
					slice.setExploded(((Boolean) obj).booleanValue());
				}

				pie.getRunTimeContext().getScriptHandler().unregisterVariable(ScriptHandler.BASE_VALUE);
				pie.getRunTimeContext().getScriptHandler().unregisterVariable(ScriptHandler.ORTHOGONAL_VALUE);
				pie.getRunTimeContext().getScriptHandler().unregisterVariable(ScriptHandler.SERIES_VALUE);

			} catch (ChartException e) {
				logger.log(e);
			}
		}
	}

	// HANDLE ELEVATION COMPUTATION FOR SOLID
	// COLORS,
	// GRADIENTS AND IMAGES

	protected Gradient getDepthGradient(Fill cd) {
		if (cd instanceof Gradient) {
			return goFactory.createGradient(goFactory.darker(((Gradient) cd).getStartColor()),
					goFactory.darker(((Gradient) cd).getEndColor()), ((Gradient) cd).getDirection(),
					((Gradient) cd).isCyclic());
		} else
			return goFactory.createGradient(
					(cd instanceof ColorDefinition) ? goFactory.darker((ColorDefinition) cd) : goFactory.GREY(),
					goFactory.BLACK(), 0, true);
	}

	/**
	 * @param cd
	 * @param startAngle
	 * @param endAngle
	 * @return
	 */
	protected Gradient getDepthGradient(Fill cd, double startAngle, double endAngle) {
		if (cd instanceof Gradient) {
			return goFactory.createGradient(goFactory.darker(((Gradient) cd).getStartColor()),
					goFactory.darker(((Gradient) cd).getEndColor()), ((Gradient) cd).getDirection(),
					((Gradient) cd).isCyclic());
		} else {
			ColorDefinition standCD = (cd instanceof ColorDefinition) ? goFactory.darker((ColorDefinition) cd)
					: goFactory.GREY();
			float[] hsbvals = Color.RGBtoHSB(standCD.getRed(), standCD.getGreen(), standCD.getBlue(), null);
			float[] startHSB = new float[3];
			startHSB[0] = hsbvals[0];
			startHSB[1] = hsbvals[1];
			startHSB[2] = hsbvals[2];

			float[] endHSB = new float[3];
			endHSB[0] = hsbvals[0];
			endHSB[1] = hsbvals[1];
			endHSB[2] = hsbvals[2];

			float brightAlpha = 1f / 180;
			if (startAngle < 180) {
				startHSB[2] = (float) (startAngle * brightAlpha);
			} else {
				startHSB[2] = (float) (1 - (startAngle - 180) * brightAlpha);
			}

			if (endAngle < 180) {
				endHSB[2] = (float) (endAngle * brightAlpha);
			} else {
				endHSB[2] = (float) (1 - (endAngle - 180) * brightAlpha);
			}

			Color startColor = new Color(Color.HSBtoRGB(startHSB[0], startHSB[1], startHSB[2]));
			Color endColor = new Color(Color.HSBtoRGB(endHSB[0], endHSB[1], endHSB[2]));

			ColorDefinition startCD = goFactory.copyOf(standCD);
			startCD.set(startColor.getRed(), startColor.getGreen(), startColor.getBlue());

			ColorDefinition endCD = goFactory.copyOf(standCD);
			endCD.set(endColor.getRed(), endColor.getGreen(), endColor.getBlue());

			if (endAngle <= 180) {
				return goFactory.createGradient(endCD, startCD, 0, true);
			} else {
				return goFactory.createGradient(startCD, endCD, 0, true);
			}
		}
	}

	/**
	 * Used for deferred rendering
	 * 
	 * @param topBound     The top round bounds of pie slice.
	 * @param bottomBound  The bottom round bounds of pie slice.
	 * @param dStartAngle  The start agnle of pie slice.
	 * @param dAngleExtent The extent angle of pie slice.
	 * @param lreStartB2T  The bottom to top line rendering event in start location
	 *                     of pie slice.
	 * @param lreEndB2T    The top to bottom line rendering event in end location of
	 *                     pie slice.
	 * @param cd
	 * @param dph          data point hints.
	 * @param loC          center point of bottom cycle.
	 * @param loCTop       center point of top cycle.
	 * @param sz           width and height of cycle.
	 * @param isInner      indicates if it is inner radius's curved surface
	 */
	private final void registerCurvedSurface(Bounds topBound, Bounds bottomBound, double dStartAngle,
			double dAngleExtent, LineRenderEvent lreStartB2T, LineRenderEvent lreEndB2T, Fill cd, DataPointHints dph,
			Location loC, Location loCTop, Size sz, boolean isInner) {
		// 1. Get all splited angles.
		double[] anglePoints = new double[4];

		double endAngle = dStartAngle + dAngleExtent;

		int i = 0;
		anglePoints[i++] = dStartAngle;
		if (endAngle > 180 && dStartAngle < 180) {
			anglePoints[i++] = 180.0d;
		}
		if (endAngle > 360 && dStartAngle < 360) {
			anglePoints[i++] = 360.0d;
			if (endAngle > 540) {
				anglePoints[i++] = 540.0d;
			}
		}
		anglePoints[i] = endAngle;

		// 2. Do Render.
		if (i == 1) // The simple case, only has one curved plane.
		{
			// CURVED PLANE 1
			final ArcRenderEvent arcRE1 = new ArcRenderEvent(WrappedStructureSource.createSeriesDataPoint(ps, dph));
			final ArcRenderEvent arcRE2 = new ArcRenderEvent(WrappedStructureSource.createSeriesDataPoint(ps, dph));
			arcRE1.setBounds(topBound);
			arcRE1.setStartAngle(dStartAngle);
			arcRE1.setAngleExtent(dAngleExtent);
			arcRE1.setStyle(ArcRenderEvent.OPEN);
			arcRE2.setBounds(bottomBound);
			arcRE2.setStartAngle(dStartAngle + dAngleExtent);
			arcRE2.setAngleExtent(-dAngleExtent);
			arcRE2.setStyle(ArcRenderEvent.OPEN);
			AreaRenderEvent areRE = new AreaRenderEvent(WrappedStructureSource.createSeriesDataPoint(ps, dph));
			areRE.add(lreStartB2T);
			areRE.add(arcRE1);
			areRE.add(lreEndB2T);
			areRE.add(arcRE2);

			areRE.setOutline(goFactory.createLineAttributes(getSliceOutline(cd), LineStyle.SOLID_LITERAL, 1));
			areRE.setBackground(getDepthGradient(cd, dStartAngle, dStartAngle + dAngleExtent));

			deferCurvedPlane(deferredPlanes, dStartAngle, dAngleExtent, areRE, lreStartB2T.getStart().getX(),
					lreEndB2T.getStart().getX(), isInner);
		} else
		// The multiple case, should be more curved plane.
		{
			IDrawable drawable = null;
			for (int j = 0; j < i; j++) {
				AreaRenderEvent areLine = new AreaRenderEvent(WrappedStructureSource.createSeriesDataPoint(ps, dph));
				areLine.setOutline(goFactory.createLineAttributes(getSliceOutline(cd), LineStyle.SOLID_LITERAL, 1));
				areLine.setBackground(null);

				double startAngle = anglePoints[j] + MIN_DOUBLE;
				double angleExtent = anglePoints[j + 1] - anglePoints[j];
				startAngle = wrapAngle(startAngle);

				Object[] edgeLines = getEdgeLines(startAngle, angleExtent, loC, loCTop, sz, dph);

				// CURVED PLANE 1
				final ArcRenderEvent arcRE1 = new ArcRenderEvent(WrappedStructureSource.createSeriesDataPoint(ps, dph));
				final ArcRenderEvent arcRE2 = new ArcRenderEvent(WrappedStructureSource.createSeriesDataPoint(ps, dph));

				arcRE1.setBounds(topBound);
				arcRE1.setStartAngle(startAngle);
				arcRE1.setAngleExtent(angleExtent);
				arcRE1.setStyle(ArcRenderEvent.OPEN);

				arcRE2.setBounds(bottomBound);
				arcRE2.setStartAngle(wrapAngle(anglePoints[j + 1]));
				arcRE2.setAngleExtent(-angleExtent);
				arcRE2.setStyle(ArcRenderEvent.OPEN);

				// Fill pie slice.
				AreaRenderEvent are = new AreaRenderEvent(WrappedStructureSource.createSeriesDataPoint(ps, dph));
				are.add((LineRenderEvent) edgeLines[0]);
				are.add(arcRE1);
				are.add((LineRenderEvent) edgeLines[1]);
				are.add(arcRE2);

				are.setOutline(null);

				are.setBackground(getDepthGradient(cd, wrapAngle(anglePoints[j]), wrapAngle(anglePoints[j + 1])));
				drawable = deferCurvedPlane(deferredPlanes, startAngle, angleExtent, are,
						((LineRenderEvent) edgeLines[0]).getStart().getX(),
						((LineRenderEvent) edgeLines[1]).getStart().getX(), isInner);

				// Arrange pie slice outline.
				if (j == 0) // It is first sector of pie slice.
				{
					areLine.add(arcRE2);
					areLine.add((LineRenderEvent) edgeLines[0]);
					areLine.add(arcRE1);
				} else if (j == (i - 1)) // It is last sector of pie slice.
				{
					areLine.add(arcRE1);
					areLine.add((LineRenderEvent) edgeLines[1]);
					areLine.add(arcRE2);

				} else {
					areLine.add(arcRE1);
					areLine.add(arcRE2);
				}

				if ((LineRenderEvent) edgeLines[2] != null) {
					((LineRenderEvent) edgeLines[2]).setLineAttributes(areLine.getLineAttributes());
				}
				// Set curved outline as next, it is rendered after current
				// curved plane.
				drawable.setNext(deferCurvedOutline(deferredPlanes, dStartAngle, dAngleExtent, areLine, isInner,
						(LineRenderEvent) edgeLines[2]));
			}
		}
	}

	/**
	 * @param startAngle
	 * @param extentAngle
	 * @param loC
	 * @param loCTop
	 * @param sz
	 * @param dph
	 * @return
	 */
	private final Object[] getEdgeLines(double startAngle, double extentAngle, Location loC, Location loCTop, Size sz,
			DataPointHints dph) {

		final LineRenderEvent lreStartB2T = getLineByAngle(loC, loCTop, sz, dph, startAngle);
		final LineRenderEvent lreEndT2B = getLineByAngle(loC, loCTop, sz, dph, startAngle + extentAngle);

		LineRenderEvent lreBetween = null;
		if ((startAngle < 180 && startAngle + extentAngle > 180)) {
			lreBetween = getLineByAngle(loC, loCTop, sz, dph, 180);
		} else if ((startAngle < 360 && startAngle + extentAngle > 360)) {
			lreBetween = getLineByAngle(loC, loCTop, sz, dph, 360);
		}

		return new Object[] { lreStartB2T, lreEndT2B, lreBetween };
	}

	private LineRenderEvent getLineByAngle(Location loC, Location loCTop, Size sz, DataPointHints dph, double dAngle) {
		double dAngleInRadius = Math.toRadians(dAngle);
		double dSineTheta = Math.sin(dAngleInRadius);
		double dCosTheta = Math.cos(dAngleInRadius);
		double xS = (sz.getWidth() * dCosTheta);
		double yS = (sz.getHeight() * dSineTheta);
		final LineRenderEvent lre = new LineRenderEvent(WrappedStructureSource.createSeriesDataPoint(ps, dph));
		lre.setStart(goFactory.createLocation(loC.getX() + xS, loC.getY() - yS));
		lre.setEnd(goFactory.createLocation(loCTop.getX() + xS, loCTop.getY() - yS));
		return lre;
	}

	/**
	 * 
	 * @param iIndex
	 * @return
	 */
	private final Fill getPaletteColor(int iIndex, DataPointHints dph) {
		Fill fiClone = FillUtil.getPaletteFill(pa.getEntries(), iIndex);
		pie.updateTranslucency(fiClone, ps);

		// Convert Fill for negative value
		if (dph != null && dph.getOrthogonalValue() instanceof Double) {
			fiClone = FillUtil.convertFill(fiClone, ((Double) dph.getOrthogonalValue()).doubleValue(), null);
		}

		return fiClone;
	}

	/**
	 * CurvedPlane
	 */
	private final class CurvedPlane implements Comparable<IDrawable>, IDrawable {

		private final AreaRenderEvent _are;

		private final Bounds _bo;

		private final double _angle;

		private boolean _isInnerPlane = false;

		private IDrawable _next;

		private final LineRenderEvent _lre;

		/**
		 * Constructor of the class.
		 * 
		 * 
		 */
		CurvedPlane(double angle, AreaRenderEvent are, boolean isInnerPlane) {
			this(angle, are, null, isInnerPlane);
		}

		CurvedPlane(double angle, AreaRenderEvent are, LineRenderEvent lre, boolean isInnerPlane) {
			_are = are;
			_bo = are.getBounds();
			_angle = angle;
			_isInnerPlane = isInnerPlane;
			_lre = lre;
		}

		public final Bounds getBounds() {
			return _bo;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public final int compareTo(IDrawable o) // Z-ORDER TEST
		{
			final CurvedPlane cp1 = this;
			if (o instanceof CurvedPlane) {
				final CurvedPlane cp2 = (CurvedPlane) o;
				final double dMinY1 = cp1.getMinY();
				final double dMinY2 = cp2.getMinY();
				double dDiff = dMinY1 - dMinY2;
				if (!ChartUtil.mathEqual(dDiff, 0)) {
					return (dDiff < 0) ? LESS : (dDiff > 0) ? MORE : EQUAL;
				} else {
					final double dMaxY1 = cp1.getMaxY();
					final double dMaxY2 = cp2.getMaxY();
					dDiff = dMaxY1 - dMaxY2;
					if (!ChartUtil.mathEqual(dDiff, 0)) {
						return (dDiff < 0) ? LESS : MORE;
					} else {
						final double dMinX1 = cp1.getMinX();
						final double dMinX2 = cp2.getMinX();
						dDiff = dMinX1 - dMinX2;
						if (!ChartUtil.mathEqual(dDiff, 0)) {
							return (dDiff < 0) ? LESS : MORE;
						} else {
							final double dMaxX1 = cp1.getMaxX();
							final double dMaxX2 = cp2.getMaxX();
							dDiff = dMaxX1 - dMaxX2;
							if (!ChartUtil.mathEqual(dDiff, 0)) {
								return (dDiff < 0) ? LESS : MORE;
							} else {
								return EQUAL;
							}
						}
					}
				}
			} else if (o instanceof FlatPlane) {
				final FlatPlane pi2 = (FlatPlane) o;
				return pi2.compareTo(cp1) * -1; // DELEGATE AND INVERT
				// RESULT
			}
			return EQUAL;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.prototype.Pie.IDrawable#draw(java.awt.Graphics2D)
		 */
		public final void draw() throws ChartException {
			idr.fillArea(_are);
			idr.drawArea(_are);
			if (_lre != null) {
				idr.drawLine(_lre);
			}
			/*
			 * GradientPaint gp = new GradientPaint( (float)dGradientStart2D, 0,
			 * Color.black, (float)(dGradientStart2D + (dGradientEnd2D -
			 * dGradientStart2D)/2), 0, _c, true); g2d.setPaint(gp); g2d.fill(_sh);
			 * g2d.setColor(_c.darker()); g2d.draw(_sh);
			 */
			if (_next != null) {
				_next.draw();
			}
		}

		private final double getMinY() {
			return _bo.getTop();
		}

		private final double getMinX() {
			return _bo.getLeft();
		}

		private final double getMaxX() {
			return _bo.getLeft() + _bo.getWidth();
		}

		private final double getMaxY() {
			return _bo.getTop() + _bo.getHeight();
		}

		public double getAngle() {
			return _angle;
		}

		public boolean isInnerPlane() {
			return _isInnerPlane;
		}

		public void setNext(IDrawable next) {
			_next = next;
		}
	}

	/**
	 * FlatPlane
	 */
	private final class FlatPlane implements Comparable<IDrawable>, IDrawable {

		private final double[] _daXPoints, _daYPoints;

		private final Fill _cd;

		private final Bounds _bo;

		private final DataPointHints _dph;

		private final double _angle;

		private IDrawable _next;

		/**
		 * Constructor of the class.
		 * 
		 * @param angle     the start angle will be used to decide the painting order.
		 * @param daXPoints
		 * @param daYPoints
		 * @param cd
		 * @param dph
		 */
		FlatPlane(double angle, double[] daXPoints, double[] daYPoints, Fill cd, DataPointHints dph) {
			_angle = angle;
			_daXPoints = daXPoints;
			_daYPoints = daYPoints;
			_dph = dph;

			// COMPUTE THE BOUNDS
			final int n = _daXPoints.length;
			double dMinX = 0, dMinY = 0, dMaxX = 0, dMaxY = 0;

			for (int i = 0; i < n; i++) {
				if (i == 0) {
					dMinX = _daXPoints[i];
					dMinY = _daYPoints[i];
					dMaxX = dMinX;
					dMaxY = dMinY;
				} else {
					if (dMinX > _daXPoints[i]) {
						dMinX = _daXPoints[i];
					}
					if (dMinY > _daYPoints[i]) {
						dMinY = _daYPoints[i];
					}
					if (dMaxX < _daXPoints[i]) {
						dMaxX = _daXPoints[i];
					}
					if (dMaxY < _daYPoints[i]) {
						dMaxY = _daYPoints[i];
					}
				}
			}
			_bo = goFactory.createBounds(dMinX, dMinY, dMaxX - dMinX, dMaxY - dMinY);

			_cd = cd;

			int nPoints = _daXPoints.length;
			int[] iaX = new int[nPoints];
			int[] iaY = new int[nPoints];
			for (int i = 0; i < nPoints; i++) {
				iaX[i] = (int) daXPoints[i];
				iaY[i] = (int) daYPoints[i];
			}
			// _p = new Polygon(iaX, iaY, nPoints);
		}

		public Bounds getBounds() {
			return _bo;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.prototype.Pie.IDrawable#draw(java.awt.Graphics2D)
		 */
		public final void draw() throws ChartException {
			PolygonRenderEvent pre = ((EventObjectCache) idr)
					.getEventObject(WrappedStructureSource.createSeriesDataPoint(ps, _dph), PolygonRenderEvent.class);
			pre.setPoints(toLocationArray());
			liaEdges.setColor(getSliceOutline(_cd));
			pre.setOutline(liaEdges);

			pre.setBackground(getDepthGradient(_cd));
			idr.fillPolygon(pre);
			idr.drawPolygon(pre);

			if (_next != null) {
				_next.draw();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public final int compareTo(IDrawable o) // Z-ORDER TEST
		{
			final FlatPlane pi1 = this;
			if (o instanceof FlatPlane) {
				final FlatPlane pi2 = (FlatPlane) o;

				final double dMinY1 = pi1.getMinY();
				final double dMinY2 = pi2.getMinY();
				double dDiff = dMinY1 - dMinY2;
				if (!ChartUtil.mathEqual(dDiff, 0)) {
					return (dDiff < 0) ? LESS : (dDiff > 0) ? MORE : EQUAL;
				} else {
					final double dMaxY1 = pi1.getMaxY();
					final double dMaxY2 = pi2.getMaxY();
					dDiff = dMaxY1 - dMaxY2;
					if (!ChartUtil.mathEqual(dDiff, 0)) {
						return (dDiff < 0) ? LESS : MORE;
					} else {
						final double dMinX1 = pi1.getMinX();
						final double dMinX2 = pi2.getMinX();
						dDiff = dMinX1 - dMinX2;
						if (!ChartUtil.mathEqual(dDiff, 0)) {
							return (dDiff < 0) ? LESS : MORE;
						} else {
							final double dMaxX1 = pi1.getMaxX();
							final double dMaxX2 = pi2.getMaxX();
							dDiff = dMaxX1 - dMaxX2;
							if (!ChartUtil.mathEqual(dDiff, 0)) {
								return (dDiff < 0) ? LESS : MORE;
							} else {
								return EQUAL;
							}
						}
					}
				}
			} else if (o instanceof CurvedPlane) {
				final CurvedPlane pi2 = (CurvedPlane) o;

				final double dMinY1 = pi1.getMinY();
				final double dMinY2 = pi2.getMinY();
				double dDiff = dMinY1 - dMinY2;
				if (!ChartUtil.mathEqual(dDiff, 0)) {
					return (dDiff < 0) ? LESS : MORE;
				} else {
					final double dMaxY1 = pi1.getMaxY();
					final double dMaxY2 = pi2.getMaxY();
					dDiff = dMaxY1 - dMaxY2;
					if (!ChartUtil.mathEqual(dDiff, 0)) {
						return (dDiff < 0) ? LESS : MORE;
					} else {
						final double dMinX1 = pi1.getMinX();
						final double dMinX2 = pi2.getMinX();
						dDiff = dMinX1 - dMinX2;
						if (!ChartUtil.mathEqual(dDiff, 0)) {
							return (dDiff < 0) ? LESS : MORE;
						} else {
							final double dMaxX1 = pi1.getMaxX();
							final double dMaxX2 = pi2.getMaxX();
							dDiff = dMaxX1 - dMaxX2;
							if (!ChartUtil.mathEqual(dDiff, 0)) {
								return (dDiff < 0) ? LESS : MORE;
							} else {
								return EQUAL;
							}
						}
					}
				}

			}
			return EQUAL;
		}

		private final double getMinY() {
			return _bo.getTop();
		}

		private final double getMinX() {
			return _bo.getLeft();
		}

		private final double getMaxX() {
			return _bo.getLeft() + _bo.getWidth();
		}

		private final double getMaxY() {
			return _bo.getTop() + _bo.getHeight();
		}

		private final Location[] toLocationArray() {
			final int n = _daXPoints.length;
			Location[] loa = new Location[n];
			for (int i = 0; i < n; i++) {
				loa[i] = goFactory.createLocation(_daXPoints[i], _daYPoints[i]);
			}
			return loa;
		}

		public double getAngle() {
			return _angle;
		}

		public boolean isInnerPlane() {
			return false;
		}

		public void setNext(IDrawable next) {
			_next = next;
		}
	}

	/**
	 * IDrawable
	 */
	private interface IDrawable {

		void draw() throws ChartException;

		Bounds getBounds();

		boolean isInnerPlane();

		void setNext(IDrawable next);
	}

	private static class OutsideLabelBoundCache {

		public int iLL = 0;
		public BoundingBox bb = null;
//
//		public void reset( )
//		{
//			iLL = 0;
//			bb = null;
//		}
	}

	public class PieSlice implements Cloneable {

		private boolean isExploded = true;
		private double originalStartAngle;
		private double startAngle;
		private double sliceLength;
		private double slicePecentage;
		private int categoryIndex;
		private DataPointHints dataPointHints;
		private double primitiveValue;

		private int quadrant = -1;

		private Location loPie;

		private Location loStart;

		private Location loEnd;

		private BoundingBox labelBounding = null;

		private Label la;

		private Bounds bounds = null;
		private double w, h, xc, yc;

		private double xDock, yDock;

		private boolean bMinSlice;

		PieSlice(double primitiveValue, DataPointHints dataPointHints, int categroyIndex, boolean bMinSlice)
				throws ChartException {
			this.primitiveValue = primitiveValue;
			this.dataPointHints = dataPointHints;
			this.categoryIndex = categroyIndex;
			this.bMinSlice = bMinSlice;
			createSliceLabel();
		}

		public boolean isMinSlice() {
			return bMinSlice;
		}

		public void createSliceLabel() throws ChartException {
			if (this.la != null) {
				return; // can only create once
			}

			this.la = goFactory.copyOf(ps.getLabel());

			la.getCaption().setValue(getDisplayValue());

			final AbstractScriptHandler sh = pie.getRunTimeContext().getScriptHandler();
			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL, getDataPointHints(), la,
					pie.getRunTimeContext().getScriptContext());
			pie.getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT_LABEL,
					la);
		}

		private PieSlice() {
			// Used to clone
		}

		public Label getLabel() {
			return la;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#clone()
		 */
		@Override
		public Object clone() {
			PieSlice slice;
			try {
				slice = (PieSlice) super.clone();
			} catch (CloneNotSupportedException e) {
				slice = new PieSlice();
			}
			copyTo(slice);
			return slice;
		}

		public void copyTo(PieSlice slice) {
			slice.primitiveValue = primitiveValue;
			slice.dataPointHints = dataPointHints;
			slice.categoryIndex = this.categoryIndex;
			slice.setLabelLocation(loPie, loStart, loEnd);
			slice.setExploded(isExploded);
			slice.setStartAngle(startAngle);
			slice.setSliceLength(sliceLength);
			slice.setPercentage(slicePecentage);
			slice.setBounds(bounds);
			slice.labelBounding = labelBounding;
			slice.quadrant = quadrant;
			slice.bMinSlice = bMinSlice;
		}

		public double getPrimitiveValue() {
			return primitiveValue;
		}

		public DataPointHints getDataPointHints() {
			return dataPointHints;
		}

		public String getDisplayValue() {
			return dataPointHints.getDisplayValue();
		}

		public double getOriginalStartAngle() {
			return originalStartAngle;
		}

		public double getStartAngle() {
			return startAngle;
		}

		public double getSliceLength() {
			return sliceLength;
		}

		public double getdMidAngle() {
			return startAngle + sliceLength / 2;
		}

		public double getOriginalMidAngle() {
			return originalStartAngle + sliceLength / 2;
		}

		public double getSlicePercentage() {
			return slicePecentage;
		}

		public int getCategoryIndex() {
			return categoryIndex;
		}

		public int getQuadrant() {
			return quadrant;
		}

		public void setLabelLocation(double dX0, double dY0, double dX1, double dY1, double dX2, double dY2) {
			setLabelLocation(goFactory.createLocation(dX0, dY0), goFactory.createLocation(dX1, dY1),
					goFactory.createLocation(dX2, dY2));
		}

		public void setLabelLocation(Location loPie, Location loStart, Location loEnd) {
			this.loPie = loPie;
			this.loStart = loStart;
			this.loEnd = loEnd;
		}

		public void setExploded(boolean isExploded) {
			this.isExploded = isExploded;
		}

		public void setOriginalStartAngle(double originalStartAngle) {
			this.originalStartAngle = originalStartAngle;
		}

		public void setStartAngle(double startAngle) {
			this.startAngle = startAngle;
		}

		/**
		 * Set degrees of a slice in 360 circle.
		 * 
		 * @param newLength
		 */
		public void setSliceLength(double newLength) {
			sliceLength = newLength;
		}

		public void setPercentage(double newPercentage) {
			slicePecentage = newPercentage;
		}

		public final BoundingBox getLabelBounding() {
			return labelBounding;
		}

		public void removeLabelBounding() {
			// Added to resolve empty category stacking (Bugzilla 197128)
			labelBounding = null;
		}

		/**
		 * @param loC        center coordinates of pie slice.
		 * @param loOffset   pie slice officeset for multiple dimension.
		 * @param sz         the width and height of pie slice.
		 * @param fi         the fill properties.
		 * @param iPieceType
		 * @throws ChartException
		 */
		private final void render(Location loC, Location loOffset, Size sz, Fill fi, int iPieceType)
				throws ChartException {
			boolean hasInnerRadius = ps.isSetInnerRadius() && ps.getInnerRadius() > 0d;
			double innerRadius = Double.NaN;
			if (hasInnerRadius) {
				innerRadius = ps.getInnerRadius();
				if (ps.isInnerRadiusPercent()) {
					innerRadius *= 0.01d;
				} else {
					double innerRadiusCap = Math.min(sz.getHeight(), sz.getWidth()) - 5.0;
					innerRadius = Math.min(innerRadiusCap, innerRadius * pie.getDeviceScale());
				}
			}

			loC.translate(loOffset.getX() / 2d, loOffset.getY() / 2d);

			if (isExploded && dExplosion != 0) {
				// double dRatio = (double) d.width / d.height;
				double dMidAngleInRadians = Math.toRadians(getStartAngle() + getSliceLength() / 2d);
				double dSineThetaMid = (Math.sin(dMidAngleInRadians));
				double dCosThetaMid = (Math.cos(dMidAngleInRadians));
				double xDelta = (dExplosion * dCosThetaMid);
				double yDelta = (dExplosion * dSineThetaMid);
				if (ratio < 1) {
					yDelta = yDelta * ratio;
				} else {
					xDelta = xDelta / ratio;
				}

				loC.translate(xDelta, -yDelta);
			}

			Location loCTop = goFactory.createLocation(loC.getX() - loOffset.getX(), loC.getY() - loOffset.getY());
			double dAngleInRadians = Math.toRadians(getStartAngle());
			double dSineThetaStart = Math.sin(dAngleInRadians);
			double dCosThetaStart = Math.cos(dAngleInRadians);
			dAngleInRadians = Math.toRadians(getStartAngle() + getSliceLength());
			double dSineThetaEnd = Math.sin(dAngleInRadians);
			double dCosThetaEnd = Math.cos(dAngleInRadians);

			double xE = (sz.getWidth() * dCosThetaEnd);
			double yE = (sz.getHeight() * dSineThetaEnd);
			double xS = (sz.getWidth() * dCosThetaStart);
			double yS = (sz.getHeight() * dSineThetaStart);

			double xInnerE = 0d;
			double yInnerE = 0d;
			double xInnerS = 0d;
			double yInnerS = 0d;
			if (hasInnerRadius) {
				if (ps.isInnerRadiusPercent()) {
					xInnerE = innerRadius * sz.getWidth() * dCosThetaEnd;
					yInnerE = innerRadius * sz.getHeight() * dSineThetaEnd;
					xInnerS = innerRadius * sz.getWidth() * dCosThetaStart;
					yInnerS = innerRadius * sz.getHeight() * dSineThetaStart;
				} else {
					double radio = sz.getHeight() / sz.getWidth();
					xInnerE = innerRadius * dCosThetaEnd;
					yInnerE = innerRadius * radio * dSineThetaEnd;
					xInnerS = innerRadius * dCosThetaStart;
					yInnerS = innerRadius * radio * dSineThetaStart;
				}
			}

			ArcRenderEvent are = null;
			if (iPieceType == LOWER) {
				are = new ArcRenderEvent(WrappedStructureSource.createSeriesDataPoint(ps, dataPointHints));
			} else {
				are = ((EventObjectCache) idr).getEventObject(
						WrappedStructureSource.createSeriesDataPoint(ps, getDataPointHints()), ArcRenderEvent.class);
			}
			are.setBackground(fi);
			liaEdges.setColor(getSliceOutline(fi));
			are.setOutline(liaEdges);
			are.setTopLeft(goFactory.createLocation(loCTop.getX() - sz.getWidth(),
					loCTop.getY() - sz.getHeight() + (iPieceType == LOWER ? dThickness : 0)));
			are.setWidth(sz.getWidth() * 2);
			are.setHeight(sz.getHeight() * 2);

			if (hasInnerRadius) {
				// Use width as standard for radius, still use width as default
				// outer radius, at render time, the
				// Y location will be adjusted according to outer radius.
				are.setOuterRadius(sz.getWidth());
				if (ps.isInnerRadiusPercent()) {
					are.setInnerRadius(sz.getWidth() * innerRadius);
				} else {
					are.setInnerRadius(innerRadius);
				}
			}

			are.setStartAngle(startAngle);
			are.setAngleExtent(sliceLength);
			are.setStyle(ArcRenderEvent.SECTOR);
			idr.fillArc(are); // Fill the top side of pie slice.

			if (iPieceType == LOWER) {
				// DRAWN INTO A BUFFER FOR DEFERRED RENDERING
				if (!hasInnerRadius) {
					double[] daXPoints = { loC.getX(), loCTop.getX(), loCTop.getX() + xE, loC.getX() + xE };
					double[] daYPoints = { loC.getY(), loCTop.getY(), loCTop.getY() - yE, loC.getY() - yE };
					deferFlatPlane(deferredPlanes, getStartAngle() + getSliceLength(), false, daXPoints, daYPoints, fi,
							dataPointHints);

					daXPoints = new double[] { loC.getX(), loC.getX() + xS, loCTop.getX() + xS, loCTop.getX() };
					daYPoints = new double[] { loC.getY(), loC.getY() - yS, loCTop.getY() - yS, loCTop.getY() };
					deferFlatPlane(deferredPlanes, getStartAngle(), true, daXPoints, daYPoints, fi, dataPointHints);

					daXPoints = new double[] { loC.getX() + xS, loCTop.getX() + xS, loCTop.getX() + xE,
							loC.getX() + xE };
					daYPoints = new double[] { loC.getY() - yS, loCTop.getY() - yS, loCTop.getY() - yE,
							loC.getY() - yE };
				} else {
					double[] daXPoints = { loC.getX() + xInnerE, loCTop.getX() + xInnerE, loCTop.getX() + xE,
							loC.getX() + xE };
					double[] daYPoints = { loC.getY() - yInnerE, loCTop.getY() - yInnerE, loCTop.getY() - yE,
							loC.getY() - yE };
					deferFlatPlane(deferredPlanes, getStartAngle() + getSliceLength(), false, daXPoints, daYPoints, fi,
							dataPointHints);

					daXPoints = new double[] { loC.getX() + xInnerS, loC.getX() + xS, loCTop.getX() + xS,
							loCTop.getX() + xInnerS };
					daYPoints = new double[] { loC.getY() - yInnerS, loC.getY() - yS, loCTop.getY() - yS,
							loCTop.getY() - yInnerS };
					deferFlatPlane(deferredPlanes, getStartAngle(), true, daXPoints, daYPoints, fi, dataPointHints);

					daXPoints = new double[] { loC.getX() + xS, loCTop.getX() + xS, loCTop.getX() + xE,
							loC.getX() + xE };
					daYPoints = new double[] { loC.getY() - yS, loCTop.getY() - yS, loCTop.getY() - yE,
							loC.getY() - yE };
				}

				if (hasInnerRadius) {
					if (ps.isInnerRadiusPercent()) {
						Size innerSize = SizeImpl.create(innerRadius * sz.getWidth(), innerRadius * sz.getHeight());
						renderCurvedSurface(loC, loCTop, innerSize, xInnerE, yInnerE, xInnerS, yInnerS, fi, true);
					} else {
						double radio = sz.getHeight() / sz.getWidth();
						Size innerSize = SizeImpl.create(innerRadius, innerRadius * radio);
						renderCurvedSurface(loC, loCTop, innerSize, xInnerE, yInnerE, xInnerS, yInnerS, fi, true);
					}
				}

				renderCurvedSurface(loC, loCTop, sz, xE, yE, xS, yS, fi, false);

			}

			else if (iPieceType == UPPER) // DRAWN IMMEDIATELY
			{
				if (ps.getSliceOutline() != null) {
					idr.drawArc(are);
				}

				if (pie.isInteractivityEnabled() && !bMinSlice) {
					final EList<Trigger> elTriggers = ps.getTriggers();
					if (!elTriggers.isEmpty()) {
						final StructureSource iSource = WrappedStructureSource.createSeriesDataPoint(ps,
								dataPointHints);
						final InteractionEvent iev = ((EventObjectCache) idr).getEventObject(iSource,
								InteractionEvent.class);
						iev.setCursor(ps.getCursor());

						Trigger tg;
						for (int t = 0; t < elTriggers.size(); t++) {
							tg = goFactory.copyOf(elTriggers.get(t));
							pie.processTrigger(tg, iSource);
							iev.addTrigger(tg);
						}
						iev.setHotSpot(are);
						idr.enableInteraction(iev);
					}
				}
			}
		}

		protected void renderCurvedSurface(Location loC, Location loCTop, Size sz, double xE, double yE, double xS,
				double yS, Fill fi, boolean isInner) {
			final LineRenderEvent lreStartB2T = new LineRenderEvent(
					WrappedStructureSource.createSeriesDataPoint(ps, dataPointHints));
			lreStartB2T.setStart(goFactory.createLocation(loC.getX() + xS, loC.getY() - yS));
			lreStartB2T.setEnd(goFactory.createLocation(loCTop.getX() + xS, loCTop.getY() - yS));
			final LineRenderEvent lreEndT2B = new LineRenderEvent(
					WrappedStructureSource.createSeriesDataPoint(ps, dataPointHints));
			lreEndT2B.setStart(goFactory.createLocation(loCTop.getX() + xE, loCTop.getY() - yE));
			lreEndT2B.setEnd(goFactory.createLocation(loC.getX() + xE, loC.getY() - yE));
			Bounds r2ddTop = goFactory.createBounds(loCTop.getX() - sz.getWidth(), loCTop.getY() - sz.getHeight(),
					sz.getWidth() * 2, sz.getHeight() * 2);
			Bounds r2ddBottom = goFactory.createBounds(loC.getX() - sz.getWidth(), loC.getY() - sz.getHeight(),
					sz.getWidth() * 2, sz.getHeight() * 2);

			registerCurvedSurface(r2ddTop, r2ddBottom, getStartAngle(), getSliceLength(), lreStartB2T, lreEndT2B, fi,
					dataPointHints, loC, loCTop, sz, isInner);
		}

		private void renderOneLine(IDeviceRenderer idr, Location lo1, Location lo2) throws ChartException {
			LineRenderEvent lre = ((EventObjectCache) idr).getEventObject(
					WrappedStructureSource.createSeriesDataPoint(ps, dataPointHints), LineRenderEvent.class);
			lre.setLineAttributes(liaLL);
			lre.setStart(lo1);
			lre.setEnd(lo2);
			idr.drawLine(lre);
		}

		private final void renderLabel(IDeviceRenderer idr, int iTextRenderType) throws ChartException {
			if (labelBounding == null) {// Do not render if no bounding
				return;
			}
			if (quadrant != -1) {
				if (iTextRenderType == TextRenderEvent.RENDER_TEXT_AT_LOCATION) {
					renderOneLine(idr, loPie, loStart);
					renderOneLine(idr, loStart, loEnd);
				}

				pie.renderLabel(WrappedStructureSource.createSeriesDataPoint(ps, dataPointHints),
						TextRenderEvent.RENDER_TEXT_IN_BLOCK, getLabel(),
						(quadrant == 1 || quadrant == 4) ? Position.RIGHT_LITERAL : Position.LEFT_LITERAL, loEnd,
						goFactory.createBounds(labelBounding.getLeft(), labelBounding.getTop(),
								labelBounding.getWidth(), labelBounding.getHeight()));
			} else {
				pie.renderLabel(StructureSource.createSeries(ps), TextRenderEvent.RENDER_TEXT_IN_BLOCK, getLabel(),
						null, null, goFactory.createBounds(labelBounding.getLeft(), labelBounding.getTop(),
								labelBounding.getWidth(), labelBounding.getHeight()));
			}
		}

		public boolean isLabelClipped(Bounds bo) {
			if (labelBounding != null) {
				if (labelBounding.getTop() < bo.getTop()) {
					return true;
				}
				if (labelBounding.getLeft() < bo.getLeft()) {
					return true;
				}
				if (labelBounding.getTop() + labelBounding.getHeight() > bo.getTop() + bo.getHeight()) {
					return true;
				}
				if (labelBounding.getLeft() + labelBounding.getWidth() > bo.getLeft() + bo.getWidth()) {
					return true;
				}
			}
			return false;
		}

		public boolean isLabelOverlap(PieSlice sliceToCompare) {
			if (sliceToCompare == null || sliceToCompare == this || sliceToCompare.labelBounding == null) {
				return false;
			}
			BoundingBox bb1 = labelBounding, bb2 = sliceToCompare.labelBounding;
			BoundingBox dHigh, dLow;
			// check which one is higher
			if (bb1.getTop() < bb2.getTop()) {
				dHigh = bb1;
				dLow = bb2;
			} else {
				dHigh = bb2;
				dLow = bb1;
			}
			double dXHigh, dXLow, dYHigh, dYLow;
			if (dHigh.getLeft() < dLow.getLeft()) {
				dXHigh = dHigh.getLeft() + dHigh.getWidth();
				dYHigh = dHigh.getTop() + dHigh.getHeight();
				dXLow = dLow.getLeft();
				dYLow = dLow.getTop();
				if (dXHigh > dXLow && dYHigh > dYLow) {
					return true;
				}
			} else {
				dXHigh = dHigh.getLeft();
				dYHigh = dHigh.getTop() + dHigh.getHeight();
				dXLow = dLow.getLeft() + dLow.getWidth();
				dYLow = dLow.getTop();
				if (dXHigh < dXLow && dYHigh > dYLow) {
					return true;
				}
			}
			return false;
		}

		public void setBounds(Bounds bo) {
			bounds = bo;
			w = bounds.getWidth() / 2 - dExplosion;
			h = bounds.getHeight() / 2 - dExplosion - dThickness / 2;
			xc = bounds.getLeft() + w + dExplosion;
			yc = bounds.getTop() + h + dExplosion + dThickness / 2;

			if (ratio > 0 && w > 0) {
				if (h / w > ratio) {
					h = w * ratio;
				} else if (h / w < ratio) {
					w = h / ratio;
				}
			}

			// detect invalid size.
			if (w <= 0 || h <= 0) {
				w = h = 1;
			}
		}

		private void computeLabelBoundOutside(LeaderLineStyle lls, double dLeaderLength, OutsideLabelBoundCache bbCache)
				throws ChartException {
			int iLL = 0;
			double dLeaderTick = Math.max(dLeaderLength / 4, LEADER_TICK_MIN_SIZE * pie.getDeviceScale());

			double dLeaderW = 0, dLeaderH = 0, dBottomLeaderW = 0, dBottomLeaderH = 0, dTopLeaderW = 0, dTopLeaderH = 0;
			Location center = goFactory.createLocation(xc, yc - dThickness / 2);
			Location depthCenter = goFactory.createLocation(xc, yc); // center
																		// in
			// the
			// middle of the depth
			double dX = 0;
			double dLeftSide = xc - dExplosion - w;
			double dRightSide = xc + dExplosion + w;

			if (w > h) {
				dTopLeaderW = dLeaderTick;
				dTopLeaderH = dLeaderTick * ratio;
			} else {
				dTopLeaderH = dLeaderTick;
				dTopLeaderW = dLeaderTick / ratio;
			}
			double dMidAngleInDegrees = getOriginalMidAngle() % 360;
			double dMidAngleInRadians = Math.toRadians(-dMidAngleInDegrees);
			double dSineThetaMid = Math.sin(dMidAngleInRadians);
			double dCosThetaMid = Math.cos(dMidAngleInRadians);

			if (dThickness > 0 && dMidAngleInDegrees > 180 && dMidAngleInDegrees < 360) {
				double dTmpLeaderTick = Math.max(dThickness * dSineThetaMid + 8 * pie.getDeviceScale(), dLeaderTick);
				if (w > h) {
					dBottomLeaderW = dTmpLeaderTick;
					dBottomLeaderH = dTmpLeaderTick * ratio;
				} else {
					dBottomLeaderH = dTmpLeaderTick;
					dBottomLeaderW = dTmpLeaderTick / ratio;
				}

				dLeaderW = dBottomLeaderW;
				dLeaderH = dBottomLeaderH;
			} else {
				dLeaderW = dTopLeaderW;
				dLeaderH = dTopLeaderH;
			}

			double xDelta1, yDelta1, xDelta2, yDelta2;
			if (isExploded) {
				xDelta1 = (w + dExplosion) * dCosThetaMid;
				yDelta1 = (h + dExplosion) * dSineThetaMid;
				xDelta2 = (w + dLeaderW + dExplosion) * dCosThetaMid;
				yDelta2 = (h + dLeaderH + dExplosion) * dSineThetaMid;
			} else {
				xDelta1 = (w) * dCosThetaMid;
				yDelta1 = (h) * dSineThetaMid;
				xDelta2 = (w + dLeaderW) * dCosThetaMid;
				yDelta2 = (h + dLeaderH) * dSineThetaMid;
			}

			if (lls == LeaderLineStyle.STRETCH_TO_SIDE_LITERAL) {
				if (dMidAngleInDegrees >= 90 && dMidAngleInDegrees < 270) {
					dX = dLeftSide - dLeaderW * 1.5;
					iLL = IConstants.LEFT;
				} else {
					dX = dRightSide + dLeaderW * 1.5;
					iLL = IConstants.RIGHT;
				}
			} else if (lls == LeaderLineStyle.FIXED_LENGTH_LITERAL) {
				if (dMidAngleInDegrees > 90 && dMidAngleInDegrees < 270) {
					dX = center.getX() + xDelta2 - dLeaderLength;
					if (dLeaderLength > 0) {
						iLL = IConstants.LEFT;
					} else {
						if (dMidAngleInDegrees < 135) {
							iLL = IConstants.TOP;
						} else if (dMidAngleInDegrees < 225) {
							iLL = IConstants.LEFT;
						} else if (dMidAngleInDegrees < 270) {
							iLL = IConstants.BOTTOM;
						} else
							assert false;
					}
				} else {
					dX = center.getX() + xDelta2 + dLeaderLength;
					if (dLeaderLength > 0) {
						iLL = IConstants.RIGHT;
					} else {
						if (dMidAngleInDegrees <= 45) {
							iLL = IConstants.RIGHT;
						} else if (dMidAngleInDegrees > 45 && dMidAngleInDegrees <= 90) {
							iLL = IConstants.TOP;
						} else if (dMidAngleInDegrees <= 315 && dMidAngleInDegrees >= 270) {
							iLL = IConstants.BOTTOM;
						} else if (dMidAngleInDegrees > 315) {
							iLL = IConstants.RIGHT;
						} else
							assert false;
					}

				}
			} else {
				// SHOULD'VE ALREADY THROWN THIS EXCEPTION PREVIOUSLY
			}

			Location relativeCenter;
			if (dMidAngleInDegrees > 0 && dMidAngleInDegrees < 180) {
				relativeCenter = center;
			} else {
				relativeCenter = depthCenter;
			}

			xDock = relativeCenter.getX() + xDelta1;
			yDock = relativeCenter.getY() + yDelta1;

			setLabelLocation(xDock, yDock, relativeCenter.getX() + xDelta2, relativeCenter.getY() + yDelta2, dX,
					relativeCenter.getY() + yDelta2);

			if (bbCache != null && bbCache.iLL == iLL && bbCache.bb != null) {
				labelBounding = bbCache.bb.clone();
			} else {
				labelBounding = cComp.computeBox(xs, iLL, getLabel(), 0, 0);

				if (bbCache != null && bbCache.iLL == 0) {
					bbCache.iLL = iLL;
					bbCache.bb = labelBounding.clone();
				}
			}

			labelBounding.setLeft(labelBounding.getLeft() + dX);
			labelBounding.setTop(labelBounding.getTop() + relativeCenter.getY() + yDelta2);

			// NEEDED FOR COMPUTING DYNAMIC REPOSITIONING LIMITS
			if (dMidAngleInDegrees >= 0 && dMidAngleInDegrees < 90) {
				quadrant = 1;
			}
			if (dMidAngleInDegrees >= 90 && dMidAngleInDegrees < 180) {
				quadrant = 2;
			}
			if (dMidAngleInDegrees >= 180 && dMidAngleInDegrees < 270) {
				quadrant = 3;
			} else {
				quadrant = 4;
			}
		}

		private void computeLabelBoundInside() throws ChartException {
			double dMidAngleInRadians = Math.toRadians(-getdMidAngle());
			double dSineThetaMid = Math.sin(dMidAngleInRadians);
			double dCosThetaMid = Math.cos(dMidAngleInRadians);
			double xDelta, yDelta;
			if (isExploded) {
				xDelta = ((w / 1.5d + dExplosion) * dCosThetaMid);
				yDelta = ((h / 1.5d + dExplosion) * dSineThetaMid);
			} else {
				xDelta = ((w / 1.5d) * dCosThetaMid);
				yDelta = ((h / 1.5d) * dSineThetaMid);
			}
			labelBounding = cComp.computeBox(xs, IConstants.LEFT/* DONT-CARE */, getLabel(), 0, 0);
			labelBounding.setLeft(xc + xDelta - labelBounding.getWidth() / 2);
			labelBounding.setTop(yc - dThickness / 2 + yDelta - labelBounding.getHeight() / 2);
		}
	}

}