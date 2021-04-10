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

package org.eclipse.birt.chart.internal.layout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.computation.LabelLimiter;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;

/**
 * A default layout policy implementation
 */
public final class LayoutManager {

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * The constructor.
	 * 
	 * @param _blRoot
	 */
	public LayoutManager(Block _blRoot) {
	}

	/*
	 * initialize the LabelLimiter for Chart Title
	 */
	private void initTitleSizeLimit(IDisplayServer xs, Chart cm, RunTimeContext rtc, Bounds bo) {
		final double dPercent = 0.5;
		bo = goFactory.scaleBounds(bo, xs.getDpiResolution() / 72d);
		bo.adjust(cm.getTitle().getInsets());
		int iTitleAnchor = cm.getTitle().getAnchor().getValue();
		LabelLimiter lbLimiter = null;
		if (iTitleAnchor == Anchor.EAST || iTitleAnchor == Anchor.WEST) {
			lbLimiter = new LabelLimiter(bo.getWidth() * dPercent, bo.getHeight(), 0);
		} else {
			lbLimiter = new LabelLimiter(bo.getWidth(), bo.getHeight() * dPercent, 0);
		}
		Map<Label, LabelLimiter> mapLimiter = rtc.getState(RunTimeContext.StateKey.LABEL_LIMITER_LOOKUP_KEY);
		mapLimiter.put(cm.getTitle().getLabel(), lbLimiter);
	}

	/**
	 * ChartLayout
	 */
	class ChartLayout {
		private Bounds bo;
		private Size szTitle;
		private Bounds boPlot;
		private Bounds boLegend;
		private Bounds boTitle;
		private Anchor plotAnchor;
		double plotWidthHint = -1, plotHeightHint = -1;
		private Size szLegend;
		private Anchor titleAnchor;

		ChartLayout(IDisplayServer xs, Chart cm, Bounds boFull, RunTimeContext rtc) throws ChartException {
			final boolean isRightToLeft = rtc.isRightToLeft();

			Block bl = cm.getBlock();
			bl.setBounds(boFull);
			Insets ins = bl.getInsets();

			bo = goFactory.adjusteBounds(boFull, ins);
			Legend lg = cm.getLegend();
			Plot pl = cm.getPlot();

			initTitleSizeLimit(xs, cm, rtc, bo);
			TitleBlock tb = cm.getTitle();
			szTitle = (!tb.isVisible()) ? SizeImpl.create(0, 0) : tb.getPreferredSize(xs, cm, rtc);

			boPlot = pl.getBounds();
			boLegend = lg.getBounds();

			// always layout title block first, for legend computing need its
			// infomation.
			boTitle = tb.getBounds();
			titleAnchor = tb.getAnchor();
			boTitle.setLeft(bo.getLeft());
			boTitle.setTop(bo.getTop());
			boTitle.setWidth(szTitle.getWidth());
			boTitle.setHeight(szTitle.getHeight());

			szLegend = (!lg.isVisible()) ? SizeImpl.create(0, 0) : lg.getPreferredSize(xs, cm, rtc);

			if (pl.isSetWidthHint()) {
				plotWidthHint = pl.getWidthHint();
			}
			if (pl.isSetHeightHint()) {
				plotHeightHint = pl.getHeightHint();
			}

			adjustPlotAnchor(isRightToLeft, pl);
		}

		private void adjustPlotAnchor(final boolean isRightToLeft, Plot pl) {
			plotAnchor = pl.getAnchor();

			// swap west/east
			if (isRightToLeft) {
				switch (plotAnchor.getValue()) {
				case Anchor.EAST:
					plotAnchor = Anchor.WEST_LITERAL;
					break;
				case Anchor.NORTH_EAST:
					plotAnchor = Anchor.NORTH_WEST_LITERAL;
					break;
				case Anchor.SOUTH_EAST:
					plotAnchor = Anchor.SOUTH_WEST_LITERAL;
					break;
				case Anchor.WEST:
					plotAnchor = Anchor.EAST_LITERAL;
					break;
				case Anchor.NORTH_WEST:
					plotAnchor = Anchor.NORTH_EAST_LITERAL;
					break;
				case Anchor.SOUTH_WEST:
					plotAnchor = Anchor.SOUTH_EAST_LITERAL;
					break;
				}
			}
		}

		void compute(Position pos) {
			switch (pos.getValue()) {
			case Position.INSIDE:
				doLayoutInside();
				break;

			case Position.RIGHT:
			case Position.OUTSIDE:
				doLayoutOutside();
				break;

			case Position.LEFT:
				doLayoutLeft();
				break;

			case Position.ABOVE:
				doLayoutAbove();
				break;

			case Position.BELOW:
				doLayoutBelow();
				break;
			}
		}

		private void doLayoutOutside() {
			boPlot.setWidth(plotWidthHint < 0 ? (bo.getWidth() - szLegend.getWidth()) : plotWidthHint);
			boPlot.setHeight(plotHeightHint < 0 ? (bo.getHeight() - szTitle.getHeight()) : plotHeightHint);

			boLegend.setTop(bo.getTop());
			boLegend.setWidth(szLegend.getWidth());
			boLegend.setHeight(bo.getHeight() - szTitle.getHeight());

			double plotLeft = bo.getLeft();
			double plotTop = bo.getTop();

			switch (titleAnchor.getValue()) {
			case Anchor.EAST:
				boPlot.setWidth(
						plotWidthHint < 0 ? (bo.getWidth() - boLegend.getWidth() - szTitle.getWidth()) : plotWidthHint);
				boPlot.setHeight(plotHeightHint < 0 ? bo.getHeight() : plotHeightHint);
				if (szTitle.getWidth() + boPlot.getWidth() + boLegend.getWidth() > bo.getWidth()) {
					boLegend.setWidth(bo.getWidth() - boPlot.getWidth() - szTitle.getWidth());
				}
				boLegend.setHeight(bo.getHeight());
				boTitle.setHeight(bo.getHeight());
				boTitle.setLeft(bo.getLeft() + boPlot.getWidth() + boLegend.getWidth());
				break;
			case Anchor.WEST:
				plotLeft = bo.getLeft() + szTitle.getWidth();
				boPlot.setWidth(
						plotWidthHint < 0 ? (bo.getWidth() - boLegend.getWidth() - szTitle.getWidth()) : plotWidthHint);
				boPlot.setHeight(plotHeightHint < 0 ? bo.getHeight() : plotHeightHint);
				boTitle.setHeight(bo.getHeight());
				if (szTitle.getWidth() + boPlot.getWidth() + boLegend.getWidth() > bo.getWidth()) {
					boLegend.setWidth(bo.getWidth() - boPlot.getWidth() - szTitle.getWidth());
				}
				boLegend.setHeight(bo.getHeight());
				break;
			case Anchor.SOUTH:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setWidth(bo.getWidth());
				break;

			case Anchor.SOUTH_EAST:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());

				break;

			case Anchor.SOUTH_WEST:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setLeft(bo.getLeft());
				break;

			case Anchor.NORTH:
				plotTop = bo.getTop() + szTitle.getHeight();
				boLegend.setTop(bo.getTop() + szTitle.getHeight());
				boTitle.setWidth(bo.getWidth());
				break;

			case Anchor.NORTH_EAST:
				plotTop = bo.getTop() + szTitle.getHeight();
				boLegend.setTop(bo.getTop() + szTitle.getHeight());
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());
				break;

			case Anchor.NORTH_WEST:
				plotTop = bo.getTop() + szTitle.getHeight();
				boLegend.setTop(bo.getTop() + szTitle.getHeight());
				boTitle.setLeft(bo.getLeft());
				break;
			}

			// adjust plot left.
			switch (titleAnchor.getValue()) {
			case Anchor.WEST:
			case Anchor.EAST:
				switch (plotAnchor.getValue()) {
				case Anchor.EAST:
				case Anchor.NORTH_EAST:
				case Anchor.SOUTH_EAST:
					plotLeft = plotLeft + bo.getWidth() - szTitle.getWidth() - boLegend.getWidth() - boPlot.getWidth();
					break;
				case Anchor.NORTH:
				case Anchor.SOUTH:
					plotLeft = plotLeft
							+ (bo.getWidth() - szTitle.getWidth() - boLegend.getWidth() - boPlot.getWidth()) / 2;
					break;
				}
				break;
			default:
				switch (plotAnchor.getValue()) {
				case Anchor.EAST:
				case Anchor.NORTH_EAST:
				case Anchor.SOUTH_EAST:
					plotLeft = plotLeft + bo.getWidth() - boLegend.getWidth() - boPlot.getWidth();
					break;
				case Anchor.NORTH:
				case Anchor.SOUTH:
					plotLeft = plotLeft + (bo.getWidth() - boLegend.getWidth() - boPlot.getWidth()) / 2;
					break;
				}
				break;
			}

			// adjust plot top.
			switch (titleAnchor.getValue()) {
			case Anchor.WEST:
			case Anchor.EAST:
				switch (plotAnchor.getValue()) {
				case Anchor.SOUTH:
				case Anchor.SOUTH_WEST:
				case Anchor.SOUTH_EAST:
					plotTop = plotTop + bo.getHeight() - boPlot.getHeight();
					break;
				case Anchor.WEST:
				case Anchor.EAST:
					plotTop = plotTop + (bo.getHeight() - boPlot.getHeight()) / 2;
					break;
				}
				break;
			default:
				switch (plotAnchor.getValue()) {
				case Anchor.SOUTH:
				case Anchor.SOUTH_WEST:
				case Anchor.SOUTH_EAST:
					plotTop = plotTop + bo.getHeight() - szTitle.getHeight() - boPlot.getHeight();
					break;
				case Anchor.WEST:
				case Anchor.EAST:
					plotTop = plotTop + (bo.getHeight() - szTitle.getHeight() - boPlot.getHeight()) / 2;
					break;
				}
				break;
			}

			boPlot.setLeft(plotLeft);
			boPlot.setTop(plotTop);
			boLegend.setLeft(plotLeft + boPlot.getWidth());
		}

		private void doLayoutInside() {
			boPlot.setWidth(plotWidthHint < 0 ? bo.getWidth() : plotWidthHint);
			boPlot.setHeight(plotHeightHint < 0 ? (bo.getHeight() - szTitle.getHeight()) : plotHeightHint);

			double plotLeft = bo.getLeft();
			double plotTop = bo.getTop();

			switch (titleAnchor.getValue()) {
			case Anchor.EAST:
				boPlot.setWidth(plotWidthHint < 0 ? (bo.getWidth() - szTitle.getWidth()) : plotWidthHint);
				boPlot.setHeight(plotHeightHint < 0 ? bo.getHeight() : plotHeightHint);
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());
				boTitle.setHeight(bo.getHeight());
				break;

			case Anchor.WEST:
				plotLeft = bo.getLeft() + szTitle.getWidth();
				boPlot.setWidth(plotWidthHint < 0 ? (bo.getWidth() - szTitle.getWidth()) : plotWidthHint);
				boPlot.setHeight(plotHeightHint < 0 ? bo.getHeight() : plotHeightHint);
				boTitle.setHeight(bo.getHeight());
				break;

			case Anchor.SOUTH:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setWidth(bo.getWidth());
				break;

			case Anchor.SOUTH_EAST:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());
				break;

			case Anchor.SOUTH_WEST:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setLeft(bo.getLeft());
				break;

			case Anchor.NORTH:
				plotTop = bo.getTop() + szTitle.getHeight();
				boTitle.setWidth(bo.getWidth());
				break;

			case Anchor.NORTH_EAST:
				plotTop = bo.getTop() + szTitle.getHeight();
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());
				break;

			case Anchor.NORTH_WEST:
				plotTop = bo.getTop() + szTitle.getHeight();
				boTitle.setLeft(bo.getLeft());
				break;
			}

			// adjust plot left.
			switch (titleAnchor.getValue()) {
			case Anchor.WEST:
			case Anchor.EAST:
				switch (plotAnchor.getValue()) {
				case Anchor.EAST:
				case Anchor.NORTH_EAST:
				case Anchor.SOUTH_EAST:
					plotLeft = plotLeft + bo.getWidth() - szTitle.getWidth() - boPlot.getWidth();
					break;
				case Anchor.NORTH:
				case Anchor.SOUTH:
					plotLeft = plotLeft + (bo.getWidth() - szTitle.getWidth() - boPlot.getWidth()) / 2;
					break;
				}
				break;
			default:
				switch (plotAnchor.getValue()) {
				case Anchor.EAST:
				case Anchor.NORTH_EAST:
				case Anchor.SOUTH_EAST:
					plotLeft = plotLeft + bo.getWidth() - boPlot.getWidth();
					break;
				case Anchor.NORTH:
				case Anchor.SOUTH:
					plotLeft = plotLeft + (bo.getWidth() - boPlot.getWidth()) / 2;
					break;
				}
				break;
			}

			// adjust plot top.
			switch (titleAnchor.getValue()) {
			case Anchor.WEST:
			case Anchor.EAST:
				switch (plotAnchor.getValue()) {
				case Anchor.SOUTH:
				case Anchor.SOUTH_WEST:
				case Anchor.SOUTH_EAST:
					plotTop = plotTop + bo.getHeight() - boPlot.getHeight();
					break;
				case Anchor.WEST:
				case Anchor.EAST:
					plotTop = plotTop + (bo.getHeight() - boPlot.getHeight()) / 2;
					break;
				}
				break;
			default:
				switch (plotAnchor.getValue()) {
				case Anchor.SOUTH:
				case Anchor.SOUTH_WEST:
				case Anchor.SOUTH_EAST:
					plotTop = plotTop + bo.getHeight() - szTitle.getHeight() - boPlot.getHeight();
					break;
				case Anchor.WEST:
				case Anchor.EAST:
					plotTop = plotTop + (bo.getHeight() - szTitle.getHeight() - boPlot.getHeight()) / 2;
					break;
				}
				break;
			}

			boPlot.setLeft(plotLeft);
			boPlot.setTop(plotTop);

			boLegend.set(0, 0, szLegend.getWidth(), szLegend.getHeight());
		}

		private void doLayoutBelow() {
			boPlot.setWidth(plotWidthHint < 0 ? bo.getWidth() : plotWidthHint);
			boPlot.setHeight(plotHeightHint < 0 ? (bo.getHeight() - boTitle.getHeight() - szLegend.getHeight())
					: plotHeightHint);

			boLegend.setLeft(bo.getLeft());
			boLegend.setWidth(bo.getWidth());
			if (szTitle.getHeight() + boPlot.getHeight() + szLegend.getHeight() > bo.getHeight()) {
				boLegend.setHeight(bo.getHeight() - szTitle.getHeight() - boPlot.getHeight());
			} else {
				boLegend.setHeight(szLegend.getHeight());
			}

			double plotLeft = bo.getLeft();
			double plotTop = bo.getTop();

			switch (titleAnchor.getValue()) {
			case Anchor.EAST:
				boPlot.setWidth(plotWidthHint < 0 ? (bo.getWidth() - szTitle.getWidth()) : plotWidthHint);
				boPlot.setHeight(plotHeightHint < 0 ? (bo.getHeight() - boLegend.getHeight()) : plotHeightHint);
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());
				boTitle.setHeight(bo.getHeight());
				boLegend.setWidth(bo.getWidth() - szTitle.getWidth());
				break;

			case Anchor.WEST:
				boPlot.setWidth(plotWidthHint < 0 ? (bo.getWidth() - szTitle.getWidth()) : plotWidthHint);
				boPlot.setHeight(plotHeightHint < 0 ? (bo.getHeight() - boLegend.getHeight()) : plotHeightHint);
				boTitle.setHeight(bo.getHeight());
				boLegend.setWidth(bo.getWidth() - szTitle.getWidth());
				boLegend.setLeft(bo.getLeft() + szTitle.getWidth());
				plotLeft = bo.getLeft() + szTitle.getWidth();
				break;

			case Anchor.SOUTH:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setWidth(bo.getWidth());
				break;

			case Anchor.SOUTH_EAST:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());
				break;

			case Anchor.SOUTH_WEST:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setLeft(bo.getLeft());
				break;

			case Anchor.NORTH:
				boTitle.setWidth(bo.getWidth());
				plotTop = bo.getTop() + szTitle.getHeight();
				break;

			case Anchor.NORTH_EAST:
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());
				plotTop = bo.getTop() + szTitle.getHeight();
				break;

			case Anchor.NORTH_WEST:
				boTitle.setLeft(bo.getLeft());
				plotTop = bo.getTop() + szTitle.getHeight();
				break;
			}

			// adjust plot left.
			switch (titleAnchor.getValue()) {
			case Anchor.WEST:
			case Anchor.EAST:
				switch (plotAnchor.getValue()) {
				case Anchor.EAST:
				case Anchor.NORTH_EAST:
				case Anchor.SOUTH_EAST:
					plotLeft = plotLeft + bo.getWidth() - szTitle.getWidth() - boPlot.getWidth();
					break;
				case Anchor.NORTH:
				case Anchor.SOUTH:
					plotLeft = plotLeft + (bo.getWidth() - szTitle.getWidth() - boPlot.getWidth()) / 2;
					break;
				}
				break;
			default:
				switch (plotAnchor.getValue()) {
				case Anchor.EAST:
				case Anchor.NORTH_EAST:
				case Anchor.SOUTH_EAST:
					plotLeft = plotLeft + bo.getWidth() - boPlot.getWidth();
					break;
				case Anchor.NORTH:
				case Anchor.SOUTH:
					plotLeft = plotLeft + (bo.getWidth() - boPlot.getWidth()) / 2;
					break;
				}
				break;
			}

			// adjust plot top.
			switch (titleAnchor.getValue()) {
			case Anchor.WEST:
			case Anchor.EAST:
				switch (plotAnchor.getValue()) {
				case Anchor.SOUTH:
				case Anchor.SOUTH_WEST:
				case Anchor.SOUTH_EAST:
					plotTop = plotTop + bo.getHeight() - boLegend.getHeight() - boPlot.getHeight();
					break;
				case Anchor.WEST:
				case Anchor.EAST:
					plotTop = plotTop + (bo.getHeight() - boLegend.getHeight() - boPlot.getHeight()) / 2;
					break;
				}
				break;
			default:
				switch (plotAnchor.getValue()) {
				case Anchor.SOUTH:
				case Anchor.SOUTH_WEST:
				case Anchor.SOUTH_EAST:
					plotTop = plotTop + bo.getHeight() - szTitle.getHeight() - boLegend.getHeight()
							- boPlot.getHeight();
					break;
				case Anchor.WEST:
				case Anchor.EAST:
					plotTop = plotTop
							+ (bo.getHeight() - szTitle.getHeight() - boLegend.getHeight() - boPlot.getHeight()) / 2;
					break;
				}
				break;
			}

			boPlot.setLeft(plotLeft);
			boPlot.setTop(plotTop);

			boLegend.setTop(plotTop + boPlot.getHeight());
		}

		private void doLayoutAbove() {
			boPlot.setWidth(plotWidthHint < 0 ? bo.getWidth() : plotWidthHint);
			boPlot.setHeight(plotHeightHint < 0 ? (bo.getHeight() - szTitle.getHeight() - szLegend.getHeight())
					: plotHeightHint);

			boLegend.setTop(bo.getTop());
			boLegend.setLeft(bo.getLeft());
			boLegend.setWidth(bo.getWidth());

			if (szTitle.getHeight() + boPlot.getHeight() + szLegend.getHeight() > bo.getHeight()) {
				boLegend.setHeight(bo.getHeight() - szTitle.getHeight() - boPlot.getHeight());
			} else {
				boLegend.setHeight(szLegend.getHeight());
			}

			double plotLeft = bo.getLeft();
			double plotTop = bo.getTop() + boLegend.getHeight();

			switch (titleAnchor.getValue()) {
			case Anchor.EAST:
				boPlot.setWidth(plotWidthHint < 0 ? (bo.getWidth() - szTitle.getWidth()) : plotWidthHint);
				boPlot.setHeight(plotHeightHint < 0 ? (bo.getHeight() - boLegend.getHeight()) : plotHeightHint);
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());
				boTitle.setHeight(bo.getHeight());
				boLegend.setWidth(bo.getWidth() - szTitle.getWidth());
				break;

			case Anchor.WEST:
				boPlot.setWidth(plotWidthHint < 0 ? (bo.getWidth() - szTitle.getWidth()) : plotWidthHint);
				boPlot.setHeight(plotHeightHint < 0 ? (bo.getHeight() - boLegend.getHeight()) : plotHeightHint);
				boTitle.setHeight(bo.getHeight());
				boLegend.setWidth(bo.getWidth() - szTitle.getWidth());
				boLegend.setLeft(bo.getLeft() + szTitle.getWidth());
				plotLeft = bo.getLeft() + szTitle.getWidth();
				break;

			case Anchor.SOUTH:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setWidth(bo.getWidth());
				break;

			case Anchor.SOUTH_EAST:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());
				break;

			case Anchor.SOUTH_WEST:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setLeft(bo.getLeft());
				break;

			case Anchor.NORTH:
				plotTop = bo.getTop() + boLegend.getHeight() + szTitle.getHeight();
				boLegend.setTop(bo.getTop() + szTitle.getHeight());
				boTitle.setWidth(bo.getWidth());
				break;

			case Anchor.NORTH_EAST:
				plotTop = bo.getTop() + boLegend.getHeight() + szTitle.getHeight();
				boLegend.setTop(bo.getTop() + szTitle.getHeight());
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());
				break;

			case Anchor.NORTH_WEST:
				plotTop = bo.getTop() + boLegend.getHeight() + szTitle.getHeight();
				boLegend.setTop(bo.getTop() + szTitle.getHeight());
				boTitle.setLeft(bo.getLeft());
				break;
			}

			// adjust plot left.
			switch (titleAnchor.getValue()) {
			case Anchor.WEST:
			case Anchor.EAST:
				switch (plotAnchor.getValue()) {
				case Anchor.EAST:
				case Anchor.NORTH_EAST:
				case Anchor.SOUTH_EAST:
					plotLeft = plotLeft + bo.getWidth() - szTitle.getWidth() - boPlot.getWidth();
					break;
				case Anchor.NORTH:
				case Anchor.SOUTH:
					plotLeft = plotLeft + (bo.getWidth() - szTitle.getWidth() - boPlot.getWidth()) / 2;
					break;
				}
				break;
			default:
				switch (plotAnchor.getValue()) {
				case Anchor.EAST:
				case Anchor.NORTH_EAST:
				case Anchor.SOUTH_EAST:
					plotLeft = plotLeft + bo.getWidth() - boPlot.getWidth();
					break;
				case Anchor.NORTH:
				case Anchor.SOUTH:
					plotLeft = plotLeft + (bo.getWidth() - boPlot.getWidth()) / 2;
					break;
				}
				break;
			}

			// adjust plot top.
			switch (titleAnchor.getValue()) {
			case Anchor.WEST:
			case Anchor.EAST:
				switch (plotAnchor.getValue()) {
				case Anchor.SOUTH:
				case Anchor.SOUTH_WEST:
				case Anchor.SOUTH_EAST:
					plotTop = plotTop + bo.getHeight() - boLegend.getHeight() - boPlot.getHeight();
					break;
				case Anchor.WEST:
				case Anchor.EAST:
					plotTop = plotTop + (bo.getHeight() - boLegend.getHeight() - boPlot.getHeight()) / 2;
					break;
				}
				break;
			default:
				switch (plotAnchor.getValue()) {
				case Anchor.SOUTH:
				case Anchor.SOUTH_WEST:
				case Anchor.SOUTH_EAST:
					plotTop = plotTop + bo.getHeight() - szTitle.getHeight() - boLegend.getHeight()
							- boPlot.getHeight();
					break;
				case Anchor.WEST:
				case Anchor.EAST:
					plotTop = plotTop
							+ (bo.getHeight() - szTitle.getHeight() - boLegend.getHeight() - boPlot.getHeight()) / 2;
					break;
				}
				break;
			}

			boPlot.setLeft(plotLeft);
			boPlot.setTop(plotTop);
		}

		private void doLayoutLeft() {
			boLegend.setTop(bo.getTop());
			boLegend.setLeft(bo.getLeft());
			boLegend.setWidth(szLegend.getWidth());
			boLegend.setHeight(bo.getHeight() - szTitle.getHeight());

			boPlot.setWidth(plotWidthHint < 0 ? (bo.getWidth() - boLegend.getWidth()) : plotWidthHint);
			boPlot.setHeight(plotHeightHint < 0 ? (bo.getHeight() - szTitle.getHeight()) : plotHeightHint);
			double plotLeft = bo.getLeft() + szLegend.getWidth();
			double plotTop = bo.getTop();

			switch (titleAnchor.getValue()) {
			case Anchor.EAST:
				boPlot.setWidth(
						plotWidthHint < 0 ? (bo.getWidth() - boLegend.getWidth() - szTitle.getWidth()) : plotWidthHint);
				boPlot.setHeight(plotHeightHint < 0 ? bo.getHeight() : plotHeightHint);
				if (boLegend.getWidth() + boPlot.getWidth() + szTitle.getWidth() > bo.getWidth()) {
					boLegend.setWidth(bo.getWidth() - boPlot.getWidth() - szTitle.getWidth());
				}
				boLegend.setHeight(bo.getHeight());
				boTitle.setLeft(bo.getLeft() + boPlot.getWidth() + boLegend.getWidth());
				boTitle.setHeight(bo.getHeight());
				plotLeft = bo.getLeft() + boLegend.getWidth();
				break;

			case Anchor.WEST:
				boPlot.setWidth(
						plotWidthHint < 0 ? (bo.getWidth() - boLegend.getWidth() - szTitle.getWidth()) : plotWidthHint);
				boPlot.setHeight(plotHeightHint < 0 ? bo.getHeight() : plotHeightHint);
				boTitle.setHeight(bo.getHeight());
				boLegend.setLeft(bo.getLeft() + szTitle.getWidth());
				if (boLegend.getWidth() + boPlot.getWidth() + szTitle.getWidth() > bo.getWidth()) {
					boLegend.setWidth(bo.getWidth() - boPlot.getWidth() - szTitle.getWidth());
				}
				boLegend.setHeight(bo.getHeight());
				plotLeft = bo.getLeft() + szTitle.getWidth() + boLegend.getWidth();
				break;

			case Anchor.SOUTH:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setWidth(bo.getWidth());
				break;

			case Anchor.SOUTH_EAST:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());
				break;

			case Anchor.SOUTH_WEST:
				boTitle.setTop(bo.getTop() + bo.getHeight() - szTitle.getHeight());
				boTitle.setLeft(bo.getLeft());
				break;

			case Anchor.NORTH:
				plotTop = bo.getTop() + szTitle.getHeight();
				boLegend.setTop(bo.getTop() + szTitle.getHeight());
				boTitle.setWidth(bo.getWidth());
				break;

			case Anchor.NORTH_EAST:
				plotTop = bo.getTop() + szTitle.getHeight();
				boLegend.setTop(bo.getTop() + szTitle.getHeight());
				boTitle.setLeft(bo.getLeft() + bo.getWidth() - szTitle.getWidth());
				break;

			case Anchor.NORTH_WEST:
				plotTop = bo.getTop() + szTitle.getHeight();
				boLegend.setTop(bo.getTop() + szTitle.getHeight());
				boTitle.setLeft(bo.getLeft());
				break;
			}

			// adjust plot left.
			switch (titleAnchor.getValue()) {
			case Anchor.WEST:
			case Anchor.EAST:
				switch (plotAnchor.getValue()) {
				case Anchor.EAST:
				case Anchor.NORTH_EAST:
				case Anchor.SOUTH_EAST:
					plotLeft = plotLeft + bo.getWidth() - szTitle.getWidth() - boLegend.getWidth() - boPlot.getWidth();
					break;
				case Anchor.NORTH:
				case Anchor.SOUTH:
					plotLeft = plotLeft
							+ (bo.getWidth() - szTitle.getWidth() - boLegend.getWidth() - boPlot.getWidth()) / 2;
					break;
				}
				break;
			default:
				switch (plotAnchor.getValue()) {
				case Anchor.EAST:
				case Anchor.NORTH_EAST:
				case Anchor.SOUTH_EAST:
					plotLeft = plotLeft + bo.getWidth() - boLegend.getWidth() - boPlot.getWidth();
					break;
				case Anchor.NORTH:
				case Anchor.SOUTH:
					plotLeft = plotLeft + (bo.getWidth() - boLegend.getWidth() - boPlot.getWidth()) / 2;
					break;
				}
				break;
			}

			// adjust plot top.
			switch (titleAnchor.getValue()) {
			case Anchor.WEST:
			case Anchor.EAST:
				switch (plotAnchor.getValue()) {
				case Anchor.SOUTH:
				case Anchor.SOUTH_WEST:
				case Anchor.SOUTH_EAST:
					plotTop = plotTop + bo.getHeight() - boPlot.getHeight();
					break;
				case Anchor.WEST:
				case Anchor.EAST:
					plotTop = plotTop + (bo.getHeight() - boPlot.getHeight()) / 2;
					break;
				}
				break;
			default:
				switch (plotAnchor.getValue()) {
				case Anchor.SOUTH:
				case Anchor.SOUTH_WEST:
				case Anchor.SOUTH_EAST:
					plotTop = plotTop + bo.getHeight() - szTitle.getHeight() - boPlot.getHeight();
					break;
				case Anchor.WEST:
				case Anchor.EAST:
					plotTop = plotTop + (bo.getHeight() - szTitle.getHeight() - boPlot.getHeight()) / 2;
					break;
				}
				break;
			}

			boPlot.setLeft(plotLeft);
			boPlot.setTop(plotTop);
		}
	}

	private void doLayout_tmp(IDisplayServer xs, Chart cm, Bounds boFull, RunTimeContext rtc) throws ChartException {
		// init Label Limiter Lookup Table.
		rtc.putState(RunTimeContext.StateKey.LABEL_LIMITER_LOOKUP_KEY, new HashMap<Label, LabelLimiter>());

		Legend lg = cm.getLegend();

		// Compute title,plot and legend position.
		Position lgPos = lg.getPosition();
		new ChartLayout(xs, cm, boFull, rtc).compute(lgPos);

		Block bl = cm.getBlock();
		bl.setBounds(boFull);
		Plot pl = cm.getPlot();
		TitleBlock tb = cm.getTitle();
		// layout custom blocks.
		for (Iterator<Block> itr = bl.getChildren().iterator(); itr.hasNext();) {
			Block cbl = itr.next();

			if (cbl != lg && cbl != pl && cbl != tb) {
				layoutBlock(xs, cm, bl.getBounds(), bl.getInsets(), cbl, rtc);
			}
		}

		// layout custom legend blocks.
		for (Iterator<Block> itr = lg.getChildren().iterator(); itr.hasNext();) {
			Block cbl = itr.next();

			layoutBlock(xs, cm, lg.getBounds(), lg.getInsets(), cbl, rtc);
		}

		// layout custom title blocks.
		for (Iterator<Block> itr = tb.getChildren().iterator(); itr.hasNext();) {
			Block cbl = itr.next();

			layoutBlock(xs, cm, tb.getBounds(), tb.getInsets(), cbl, rtc);
		}

		// layout custom plot blocks.
		for (Iterator<Block> itr = pl.getChildren().iterator(); itr.hasNext();) {
			Block cbl = itr.next();

			layoutBlock(xs, cm, pl.getBounds(), pl.getInsets(), cbl, rtc);
		}

	}

	private void layoutBlock(IDisplayServer xs, Chart cm, Bounds bo, Insets ins, Block block, RunTimeContext rtc)
			throws ChartException {
		if (!block.isSetAnchor()) {
			return;
		}

		Bounds cbo = block.getBounds();

		if (cbo == null) {
			cbo = goFactory.createBounds(0, 0, 0, 0);
		} else if (cbo.getLeft() != 0 || cbo.getTop() != 0 || cbo.getWidth() != 0 || cbo.getHeight() != 0) {
			return;
		}

		bo = goFactory.adjusteBounds(bo, ins);

		Anchor anchor = block.getAnchor();

		// swap west/east
		if (rtc != null && rtc.isRightToLeft()) {
			switch (anchor.getValue()) {
			case Anchor.EAST:
				anchor = Anchor.WEST_LITERAL;
				break;
			case Anchor.NORTH_EAST:
				anchor = Anchor.NORTH_WEST_LITERAL;
				break;
			case Anchor.SOUTH_EAST:
				anchor = Anchor.SOUTH_WEST_LITERAL;
				break;
			case Anchor.WEST:
				anchor = Anchor.EAST_LITERAL;
				break;
			case Anchor.NORTH_WEST:
				anchor = Anchor.NORTH_EAST_LITERAL;
				break;
			case Anchor.SOUTH_WEST:
				anchor = Anchor.SOUTH_EAST_LITERAL;
				break;
			}
		}

		Size sz = block.getPreferredSize(xs, cm, rtc);

		cbo.setWidth(sz.getWidth());
		cbo.setHeight(sz.getHeight());

		switch (anchor.getValue()) {
		case Anchor.EAST:
			cbo.setLeft(bo.getLeft() + bo.getWidth() - sz.getWidth());
			cbo.setTop(bo.getTop() + (bo.getHeight() - sz.getHeight()) / 2);
			break;
		case Anchor.NORTH:
			cbo.setLeft(bo.getLeft() + (bo.getWidth() - sz.getWidth()) / 2);
			cbo.setTop(bo.getTop());
			break;
		case Anchor.NORTH_EAST:
			cbo.setLeft(bo.getLeft() + bo.getWidth() - sz.getWidth());
			cbo.setTop(bo.getTop());
			break;
		case Anchor.NORTH_WEST:
			cbo.setLeft(bo.getLeft());
			cbo.setTop(bo.getTop());
			break;
		case Anchor.SOUTH:
			cbo.setLeft(bo.getLeft() + (bo.getWidth() - sz.getWidth()) / 2);
			cbo.setTop(bo.getTop() + bo.getHeight() - sz.getHeight());
			break;
		case Anchor.SOUTH_EAST:
			cbo.setLeft(bo.getLeft() + bo.getWidth() - sz.getWidth());
			cbo.setTop(bo.getTop() + bo.getHeight() - sz.getHeight());
			break;
		case Anchor.SOUTH_WEST:
			cbo.setLeft(bo.getLeft());
			cbo.setTop(bo.getTop() + bo.getHeight() - sz.getHeight());
			break;
		case Anchor.WEST:
			cbo.setLeft(bo.getLeft());
			cbo.setTop(bo.getTop() + (bo.getHeight() - sz.getHeight()) / 2);
			break;
		}

		block.setBounds(cbo);
	}

	/**
	 * This method recursively walks down the chart layout and establishes bounds
	 * for each contained block based on the following rule:
	 * 
	 * All immediate children under 'blRoot' are added as ElasticLayout with
	 * appropriate constraints All other children (at deeper levels) are added as
	 * NullLayout with fixed 'relative' bounds
	 * 
	 * @param bo
	 * @throws ChartException
	 */
	public void doLayout(IDisplayServer xs, Chart cm, Bounds bo, RunTimeContext rtc) throws ChartException {
		doLayout_tmp(xs, cm, bo, rtc);
	}
}