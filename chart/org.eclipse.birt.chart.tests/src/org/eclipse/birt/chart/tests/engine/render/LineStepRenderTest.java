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

import static org.eclipse.birt.chart.model.attribute.ChartDimension.THREE_DIMENSIONAL_LITERAL;
import static org.eclipse.birt.chart.model.attribute.ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL;
import static org.eclipse.birt.chart.model.attribute.LineInterpolation.LINEAR_LITERAL;
import static org.eclipse.birt.chart.model.attribute.LineInterpolation.STEP_AFTER_LITERAL;
import static org.eclipse.birt.chart.model.attribute.LineInterpolation.STEP_BEFORE_LITERAL;
import static org.eclipse.birt.chart.model.attribute.LineInterpolation.STEP_CENTER_LITERAL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.model.attribute.LineInterpolation;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.tests.engine.render.SegmentRecordingRenderer.Seg;
import org.eclipse.birt.chart.tests.engine.render.StepChartFixture.Options;
import org.eclipse.birt.core.framework.PlatformConfig;

import junit.framework.TestCase;

/**
 * Checks the geometry of a step interpolated line. Each test renders the same
 * chart twice, once linear and once stepped; both come from the same options,
 * so data point coordinates compare with exact double equality.
 */
public class LineStepRenderTest extends TestCase {

	/** Three data points on the categories A, B and C. */
	private static final Double[] VALUES = { 5.0, 9.0, 3.0 };

	/** The second value series of the stacked and percent charts. */
	private static final Double[] SECOND = { 3.0, 4.0, 2.0 };

	@Override
	protected void setUp() {
		ChartEngine.instance(new PlatformConfig()); // idempotent
	}

	/** Options every geometry test starts from: the line only, no markers. */
	private static Options bare() {
		return new Options().markersVisible(false);
	}

	// --- 1. the modes ------------------------------

	public void testAfterMode() throws Exception {
		assertStaircase(bare(), STEP_AFTER_LITERAL, VALUES, 4);
	}

	public void testBeforeMode() throws Exception {
		assertStaircase(bare(), STEP_BEFORE_LITERAL, VALUES, 4);
	}

	public void testCenterMode() throws Exception {
		assertStaircase(bare(), STEP_CENTER_LITERAL, VALUES, 6);
	}

	// --- 2. transposed ------------------------------

	public void testTransposedChartStepsAlongDeviceY() throws Exception {
		// The category axis runs down the device: the corner takes x from the
		// left data point and y from the right one.
		Oracle o = assertStaircase(bare().transposed(true), STEP_AFTER_LITERAL, VALUES, 4);
		double[] p0 = o.stock.get(0), p1 = o.stock.get(1), corner = o.step.get(1);
		assertEquals(p0[0], corner[0], 0.0);
		assertEquals(p1[1], corner[1], 0.0);
	}

	// --- 3./4. missing ------------------------------

	public void testNullGapBreaksTheRun() throws Exception {
		Options o = bare().connectMissingValue(false);
		Double[] v = { 5.0, null, 3.0 };
		SegmentRecordingRenderer stock = SegmentRecordingRenderer.render(StepChartFixture.chart(null, v, o));
		SegmentRecordingRenderer step = SegmentRecordingRenderer
				.render(StepChartFixture.chart(STEP_AFTER_LITERAL, v, o));
		assertEquals(0, stock.segments.size());
		assertEquals(0, step.segments.size());
		assertTrue(stock.seriesPointOvals > 0);
		assertEquals(stock.seriesPointOvals, step.seriesPointOvals);
	}

	public void testNullBridgedStepsOverTheMissingPoint() throws Exception {
		Oracle o = assertStaircase(bare().connectMissingValue(true), STEP_AFTER_LITERAL,
				new Double[] { 5.0, null, 3.0 }, 2);
		assertEquals(2, o.stock.size());
	}

	// --- 5. flat ------------------------------

	public void testEqualValuesFormOneTread() throws Exception {
		assertStaircase(bare(), STEP_AFTER_LITERAL, new Double[] { 5.0, 5.0, 3.0 }, 3);
	}

	// --- 6./7. many series ------------------------------

	public void testStackedLinesEachStep() throws Exception {
		assertStaircasePerSeries(bare().stacked(true).secondValues(SECOND), STEP_AFTER_LITERAL);
	}

	public void testPercentStackedLinesStep() throws Exception {
		// The stacked group a percent axis reads exists only when stacked.
		assertStaircasePerSeries(bare().percent(true).stacked(true).secondValues(SECOND), STEP_AFTER_LITERAL);
	}

	// --- 8./9. depth ------------------------------

	public void testTwoDimensionalWithDepthSteps() throws Exception {
		assertStaircase(bare().dimension(TWO_DIMENSIONAL_WITH_DEPTH_LITERAL), STEP_AFTER_LITERAL, VALUES, 4);
	}

	/** Documented limitation: 3D keeps the linear renderer. */
	public void testThreeDimensionalFallsBackToLinear() throws Exception {
		Options o = bare().dimension(THREE_DIMENSIONAL_LITERAL);
		SegmentRecordingRenderer stock = SegmentRecordingRenderer.render(StepChartFixture.chart(null, VALUES, o));
		SegmentRecordingRenderer step = SegmentRecordingRenderer
				.render(StepChartFixture.chart(STEP_AFTER_LITERAL, VALUES, o));
		assertFalse("the 3D chart drew no segment", step.segments.isEmpty()); //$NON-NLS-1$
		assertEquals(stock.segments.toString(), step.segments.toString());
		assertEquals(polygons(stock), polygons(step));
	}

	// --- 10. curve ------------------------------

	public void testStepWinsOverCurve() throws Exception {
		// The layout ignores the curve flag, so the plain chart is a reference.
		List<double[]> stock = vertices(
				SegmentRecordingRenderer.render(StepChartFixture.chart(null, VALUES, bare())).segments);
		List<Seg> steps = SegmentRecordingRenderer
				.render(StepChartFixture.chart(STEP_AFTER_LITERAL, VALUES, bare().curve(true))).segments;
		assertEquals(4, steps.size());
		assertAxisParallel(steps);
		assertVertices(staircase(stock, STEP_AFTER_LITERAL, false), vertices(steps));
	}

	// --- 11. shadow ------------------------------

	public void testShadowFollowsTheSteps() throws Exception {
		Options o = bare().shadowColor(ColorDefinitionImpl.GREY());
		List<Seg> steps = SegmentRecordingRenderer
				.render(StepChartFixture.chart(STEP_AFTER_LITERAL, VALUES, o)).segments;
		assertEquals(8, steps.size()); // shadow first, then the line
		for (int k = 0; k < 4; k++) {
			Seg shadow = steps.get(k), main = steps.get(k + 4);
			assertEquals(main.x1, shadow.x1, 0.0);
			assertEquals(main.y1 + 3, shadow.y1, 0.0);
			assertEquals(main.x2, shadow.x2, 0.0);
			assertEquals(main.y2 + 3, shadow.y2, 0.0);
		}
	}

	// --- 12. markers and labels ------------------------------

	public void testMarkersAndLabelsAddNoSegments() throws Exception {
		Options o = new Options().markersVisible(true).labelsVisible(true);
		SegmentRecordingRenderer r = SegmentRecordingRenderer
				.render(StepChartFixture.chart(STEP_AFTER_LITERAL, VALUES, o));
		assertEquals(4, r.segments.size());
		assertAxisParallel(r.segments);
	}

	// --- 13. degenerate ------------------------------

	public void testSinglePointDrawsNoSegment() throws Exception {
		// Only the seeker that breaks a run at a gap reports an isolated point,
		// and it runs when connectMissingValue is false.
		Options o = bare().connectMissingValue(false);
		SegmentRecordingRenderer stock = SegmentRecordingRenderer
				.render(StepChartFixture.chart(null, new Double[] { 5.0 }, o));
		SegmentRecordingRenderer step = SegmentRecordingRenderer
				.render(StepChartFixture.chart(STEP_AFTER_LITERAL, new Double[] { 5.0 }, o));
		assertEquals(0, step.segments.size());
		assertEquals(1, step.seriesPointOvals);
		assertEquals(stock.seriesPointOvals, step.seriesPointOvals);
	}

	// --- 14. the default path ------------------------------

	/** An explicit Linear must render exactly like an unset interpolation. */
	public void testExplicitLinearEqualsUnset() throws Exception {
		Options o = new Options().markersVisible(true).shadowColor(ColorDefinitionImpl.GREY());
		List<Seg> stock = SegmentRecordingRenderer.render(StepChartFixture.chart(null, VALUES, o)).segments;
		List<Seg> lin = SegmentRecordingRenderer.render(StepChartFixture.chart(LINEAR_LITERAL, VALUES, o)).segments;
		assertEquals(stock.toString(), lin.toString());
	}

	// --- 15. area ------------------------------

	public void testAreaFillUsesTheSteps() throws Exception {
		Options o = bare().area(true);
		SegmentRecordingRenderer stock = SegmentRecordingRenderer.render(StepChartFixture.chart(null, VALUES, o));
		SegmentRecordingRenderer step = SegmentRecordingRenderer
				.render(StepChartFixture.chart(STEP_AFTER_LITERAL, VALUES, o));
		assertEquals(4, step.segments.size()); // front line
		assertAxisParallel(step.segments);
		assertEquals(1, stock.polygons.size());
		assertEquals(1, step.polygons.size());
		assertEquals(5, stock.polygons.get(0).length); // base, p0, p1, p2, base
		assertEquals(7, step.polygons.get(0).length); // base, p0, c0, p1, c1, p2, base
		List<double[]> oracle = Arrays.asList(stock.polygons.get(0)).subList(1, 4);
		assertVertices(staircase(oracle, STEP_AFTER_LITERAL, false),
				Arrays.asList(step.polygons.get(0)).subList(1, 6));
	}

	public void testAreaNullGapWithoutConnectDrawsLikeTheOracle() throws Exception {
		// R2: unbridged, a missing value ends the run and gets no corner vertex.
		Options o = bare().area(true).connectMissingValue(false);
		Double[] v = { 5.0, null, 3.0 };
		SegmentRecordingRenderer stock = SegmentRecordingRenderer.render(StepChartFixture.chart(null, v, o));
		SegmentRecordingRenderer step = SegmentRecordingRenderer
				.render(StepChartFixture.chart(STEP_AFTER_LITERAL, v, o));
		assertEquals(1, stock.polygons.size());
		assertEquals(1, step.polygons.size());
		assertEquals(stock.segments.toString(), step.segments.toString());
		assertEquals(polygons(stock), polygons(step));
	}

	public void testAreaNullBridgedStepsOverTheMissingPoint() throws Exception {
		Options o = bare().area(true).connectMissingValue(true);
		Double[] v = { 5.0, null, 3.0 };
		SegmentRecordingRenderer stock = SegmentRecordingRenderer.render(StepChartFixture.chart(null, v, o));
		SegmentRecordingRenderer step = SegmentRecordingRenderer
				.render(StepChartFixture.chart(STEP_AFTER_LITERAL, v, o));

		// A bridged run skips the missing value, so the oracle draws one front line.
		assertEquals(1, stock.segments.size());
		assertEquals(2, step.segments.size());
		assertAxisParallel(step.segments);
		assertVertices(staircase(vertices(stock.segments), STEP_AFTER_LITERAL, false), vertices(step.segments));

		// The corner vertex of the bridged pair adds one point to the fill polygon.
		assertEquals(1, stock.polygons.size());
		assertEquals(1, step.polygons.size());
		assertEquals(4, stock.polygons.get(0).length); // base, p0, p2, base
		assertEquals(5, step.polygons.get(0).length); // base, p0, c0, p2, base
	}

	public void testTransposedAreaStepsAlongDeviceY() throws Exception {
		// The category axis runs down the device; the fill closes on device x.
		assertAreaStaircase(bare().area(true).transposed(true), STEP_AFTER_LITERAL, VALUES, 4, 7);
	}

	public void testAreaBeforeAndCenterModes() throws Exception {
		assertAreaStaircase(bare().area(true), STEP_BEFORE_LITERAL, VALUES, 4, 7);
		assertAreaStaircase(bare().area(true), STEP_CENTER_LITERAL, VALUES, 6, 9);
	}

	/** Documented limitation: stacked areas keep the linear renderer. */
	public void testStackedAreaFallsBackToLinear() throws Exception {
		Options o = bare().area(true).stacked(true).secondValues(SECOND);
		SegmentRecordingRenderer stock = SegmentRecordingRenderer.render(StepChartFixture.chart(null, VALUES, o));
		SegmentRecordingRenderer step = SegmentRecordingRenderer
				.render(StepChartFixture.chart(STEP_AFTER_LITERAL, VALUES, o));
		assertEquals(stock.segments.toString(), step.segments.toString());
		assertEquals(polygons(stock), polygons(step));
	}

	// --- helpers ------------------------------

	/** The vertex lists of one comparison: reference chart and step chart. */
	private static final class Oracle {

		final List<double[]> stock;
		final List<double[]> step;

		Oracle(List<double[]> stock, List<double[]> step) {
			this.stock = stock;
			this.step = step;
		}
	}

	/**
	 * Asserts that the step chart draws the reference vertices plus the corner
	 * vertices of the mode.
	 */
	private Oracle assertStaircase(Options o, LineInterpolation mode, Double[] values, int expectedSegments)
			throws Exception {
		List<double[]> stock = vertices(
				SegmentRecordingRenderer.render(StepChartFixture.chart(null, values, o)).segments);
		List<Seg> steps = SegmentRecordingRenderer.render(StepChartFixture.chart(mode, values, o)).segments;
		assertEquals("segment count", expectedSegments, steps.size()); //$NON-NLS-1$
		assertAxisParallel(steps);
		List<double[]> stepVertices = vertices(steps);
		assertVertices(staircase(stock, mode, o.isTransposed()), stepVertices);
		return new Oracle(stock, stepVertices);
	}

	/**
	 * Asserts the comparison of {@link #assertStaircase} for an area chart, and
	 * the point count of the fill polygon: one point per vertex plus the two on
	 * the zero line that close it.
	 */
	private void assertAreaStaircase(Options o, LineInterpolation mode, Double[] values, int expectedSegments,
			int expectedPolygonPoints) throws Exception {
		SegmentRecordingRenderer stock = SegmentRecordingRenderer.render(StepChartFixture.chart(null, values, o));
		SegmentRecordingRenderer step = SegmentRecordingRenderer.render(StepChartFixture.chart(mode, values, o));
		assertEquals("segment count", expectedSegments, step.segments.size()); //$NON-NLS-1$
		assertAxisParallel(step.segments);
		assertVertices(staircase(vertices(stock.segments), mode, o.isTransposed()), vertices(step.segments));
		assertEquals("polygon count", 1, step.polygons.size()); //$NON-NLS-1$
		assertEquals("polygon point count", expectedPolygonPoints, step.polygons.get(0).length); //$NON-NLS-1$
	}

	/** Asserts the comparison of {@link #assertStaircase} per value series. */
	private void assertStaircasePerSeries(Options o, LineInterpolation mode) throws Exception {
		List<List<Seg>> stock = SegmentRecordingRenderer.render(StepChartFixture.chart(null, VALUES, o))
				.segmentGroups();
		List<List<Seg>> steps = SegmentRecordingRenderer.render(StepChartFixture.chart(mode, VALUES, o))
				.segmentGroups();
		assertEquals("the reference chart draws two value series", 2, stock.size()); //$NON-NLS-1$
		assertEquals(stock.size(), steps.size());
		for (int s = 0; s < stock.size(); s++) {
			assertAxisParallel(steps.get(s));
			assertVertices(staircase(vertices(stock.get(s)), mode, o.isTransposed()), vertices(steps.get(s)));
		}
	}

	/**
	 * Turns a run of segments into its vertex list, asserting that a segment
	 * starts where the one before it ended.
	 */
	private static List<double[]> vertices(List<Seg> segments) {
		assertTrue("the value series drew no segment", !segments.isEmpty()); //$NON-NLS-1$

		List<double[]> points = new ArrayList<>();
		points.add(segments.get(0).start());
		for (int k = 0; k < segments.size(); k++) {
			Seg segment = segments.get(k);
			double[] previous = points.get(points.size() - 1);
			assertEquals("segment " + k + " does not start where segment " + (k - 1) + " ended (x): " + segments, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					previous[0], segment.x1, 0.0);
			assertEquals("segment " + k + " does not start where segment " + (k - 1) + " ended (y): " + segments, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					previous[1], segment.y1, 0.0);
			points.add(segment.end());
		}
		return points;
	}

	/** Asserts that every segment is a tread or a step, never a diagonal. */
	private static void assertAxisParallel(List<Seg> segments) {
		for (Seg segment : segments) {
			assertTrue("segment is not axis parallel: " + segment, //$NON-NLS-1$
					(segment.x1 == segment.x2) ^ (segment.y1 == segment.y2));
		}
	}

	/**
	 * Computes the vertices that the step line must contain over a run of data
	 * point vertices.
	 */
	private static List<double[]> staircase(List<double[]> points, LineInterpolation mode, boolean transposed) {
		// A transposed chart swaps the roles of the two device coordinates.
		int base = transposed ? 1 : 0;
		int value = transposed ? 0 : 1;

		List<double[]> out = new ArrayList<>();
		for (int i = 0; i < points.size(); i++) {
			double[] left = points.get(i);
			out.add(left);
			if (i + 1 >= points.size()) {
				break;
			}

			double[] right = points.get(i + 1);
			if (left[value] == right[value]) {
				// Two equal values make one tread and need no corner vertex.
				continue;
			}

			switch (mode) {
			case STEP_AFTER_LITERAL:
				out.add(corner(right[base], left[value], base, value));
				break;
			case STEP_BEFORE_LITERAL:
				out.add(corner(left[base], right[value], base, value));
				break;
			case STEP_CENTER_LITERAL:
				double middle = (left[base] + right[base]) / 2;
				out.add(corner(middle, left[value], base, value));
				out.add(corner(middle, right[value], base, value));
				break;
			default:
				break;
			}
		}
		return out;
	}

	/** Builds one corner vertex as a device point. */
	private static double[] corner(double baseCoordinate, double valueCoordinate, int base, int value) {
		double[] point = new double[2];
		point[base] = baseCoordinate;
		point[value] = valueCoordinate;
		return point;
	}

	/** Compares two vertex lists as text, so a failure shows both lists. */
	private static void assertVertices(List<double[]> expected, List<double[]> actual) {
		assertEquals("vertex list", format(expected), format(actual)); //$NON-NLS-1$
	}

	/** Renders every recorded fill polygon as text, in fill order. */
	private static String polygons(SegmentRecordingRenderer r) {
		StringBuilder text = new StringBuilder();
		for (double[][] polygon : r.polygons) {
			text.append(format(Arrays.asList(polygon))).append("--\n"); //$NON-NLS-1$
		}
		return text.toString();
	}

	private static String format(List<double[]> points) {
		StringBuilder text = new StringBuilder();
		for (double[] point : points) {
			text.append('(').append(point[0]).append(", ").append(point[1]).append(")\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return text.toString();
	}
}
