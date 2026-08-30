/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.chart.tests.engine.render;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.image.PngRendererImpl;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;

import com.ibm.icu.util.ULocale;

/**
 * A PNG device that records the geometry of a value series into memory.
 * <p>
 * The {@link StructureSource} filter selects value-series line segments, filled
 * polygons and isolated-point ovals. A marker carries the same
 * {@link StructureType#SERIES_DATA_POINT} source, so marker geometry is
 * recorded as well: the default box marker adds one entry to {@link #polygons}
 * per data point, a circle marker would count towards
 * {@link #seriesPointOvals}, and a cross marker would add to {@link #segments}.
 * A geometry test therefore hides the markers.
 * {@code DeferredCache.flushLines} replays cached line events in insertion
 * order, so {@link #segments} is in seeker order.
 */
public class SegmentRecordingRenderer extends PngRendererImpl {

	/** One recorded line segment, in device coordinates. */
	public static final class Seg {

		public final double x1, y1, x2, y2;

		Seg(double x1, double y1, double x2, double y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		/** @return the start point as <code>{x, y}</code>. */
		public double[] start() {
			return new double[] { x1, y1 };
		}

		/** @return the end point as <code>{x, y}</code>. */
		public double[] end() {
			return new double[] { x2, y2 };
		}

		@Override
		public String toString() {
			return "(" + x1 + "," + y1 + ")->(" + x2 + "," + y2 + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
	}

	/** Every line segment of a value series, in the order the device drew it. */
	public final List<Seg> segments = new ArrayList<>();

	/** The series that each entry of {@link #segments} belongs to, by index. */
	private final List<Object> segmentSeries = new ArrayList<>();

	/**
	 * Points of every filled polygon whose source is a value series: Area fills,
	 * and box markers while they are visible.
	 */
	public final List<double[][]> polygons = new ArrayList<>();

	/** The number of isolated data points the device strokes as an oval. */
	public int seriesPointOvals;

	/** @return the series of a render event, or null outside a value series. */
	private static Object seriesOf(Object source) {
		if (source instanceof WrappedStructureSource) {
			WrappedStructureSource ws = (WrappedStructureSource) source;
			if (ws.getType() == StructureType.SERIES_DATA_POINT && ws.getParent() != null
					&& ws.getParent().getType() == StructureType.SERIES) {
				return ws.getParent().getSource(); // Area front lines are tagged per data point
			}
			return null;
		}
		if (source instanceof StructureSource && ((StructureSource) source).getType() == StructureType.SERIES) {
			return ((StructureSource) source).getSource();
		}
		return null;
	}

	@Override
	public void drawLine(LineRenderEvent lre) throws ChartException {
		Object series = seriesOf(lre.getSource());
		if (series != null) {
			segments.add(
					new Seg(lre.getStart().getX(), lre.getStart().getY(), lre.getEnd().getX(), lre.getEnd().getY()));
			segmentSeries.add(series);
		}
		super.drawLine(lre);
	}

	@Override
	public void fillPolygon(PolygonRenderEvent pre) throws ChartException {
		if (seriesOf(pre.getSource()) != null) {
			Location[] pts = pre.getPoints();
			double[][] copy = new double[pts.length][];
			for (int i = 0; i < pts.length; i++) {
				copy[i] = new double[] { pts[i].getX(), pts[i].getY() };
			}
			polygons.add(copy);
		}
		super.fillPolygon(pre);
	}

	@Override
	public void drawOval(OvalRenderEvent ore) throws ChartException {
		if (ore.getSource() instanceof StructureSource
				&& ((StructureSource) ore.getSource()).getType() == StructureType.SERIES_DATA_POINT
				&& ore.getOutline() != null && ore.getOutline().isVisible()) {
			seriesPointOvals++;
		}
		super.drawOval(ore);
	}

	/**
	 * Splits {@link #segments} into one list per value series that drew a
	 * segment, in the order the device drew the first segment of each.
	 */
	public List<List<Seg>> segmentGroups() {
		List<Object> order = new ArrayList<>();
		List<List<Seg>> groups = new ArrayList<>();
		for (int i = 0; i < segments.size(); i++) {
			int g = order.indexOf(segmentSeries.get(i));
			if (g < 0) {
				order.add(segmentSeries.get(i));
				groups.add(new ArrayList<>());
				g = groups.size() - 1;
			}
			groups.get(g).add(segments.get(i));
		}
		return groups;
	}

	/**
	 * Renders the chart at 600x400 points into memory.
	 */
	public static SegmentRecordingRenderer render(Chart chart) throws ChartException {
		SegmentRecordingRenderer idr = new SegmentRecordingRenderer();
		idr.setProperty(IDeviceRenderer.FILE_IDENTIFIER, new ByteArrayOutputStream());
		RunTimeContext rtc = new RunTimeContext();
		rtc.setULocale(ULocale.ENGLISH);
		Generator gr = Generator.instance();
		GeneratedChartState gcs = gr.build(idr.getDisplayServer(), chart, BoundsImpl.create(0, 0, 600, 400), null, rtc,
				null);
		gr.render(idr, gcs);
		return idr;
	}
}
