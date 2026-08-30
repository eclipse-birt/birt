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

import java.util.Arrays;
import java.util.Random;

import org.eclipse.birt.chart.extension.render.LineStepExpander;
import org.eclipse.birt.chart.extension.render.LineStepExpander.Expansion;
import org.eclipse.birt.chart.model.attribute.LineInterpolation;

import junit.framework.TestCase;

/**
 * Verifies the expansion of data points into step line vertices. The expansion
 * is pure arithmetic, so these tests need no chart engine.
 */
public class LineStepExpanderTest extends TestCase {

	private static final double[] BASE = { 0, 10, 20 };

	private static final double[] VALUE = { 5, 9, 3 };

	private static boolean[] noNulls(int n) {
		return new boolean[n];
	}

	/**
	 * Compares one expansion with the four expected arrays.
	 */
	private static void assertExpansion(Expansion actual, double[] base, double[] value, int[] owner, boolean[] real) {
		assertEquals("base", Arrays.toString(base), Arrays.toString(actual.base)); //$NON-NLS-1$
		assertEquals("value", Arrays.toString(value), Arrays.toString(actual.value)); //$NON-NLS-1$
		assertEquals("owner", Arrays.toString(owner), Arrays.toString(actual.owner)); //$NON-NLS-1$
		assertEquals("real", Arrays.toString(real), Arrays.toString(actual.real)); //$NON-NLS-1$
	}

	public void testAfterModeJumpsAtTheNextPoint() {
		Expansion out = LineStepExpander.expand(BASE, VALUE, noNulls(3), LineInterpolation.STEP_AFTER_LITERAL, false);
		assertExpansion(out, new double[] { 0, 10, 10, 20, 20 }, new double[] { 5, 5, 9, 9, 3 },
				new int[] { 0, 0, 1, 1, 2 }, new boolean[] { true, false, true, false, true });
	}

	public void testBeforeModeJumpsAtTheCurrentPoint() {
		Expansion out = LineStepExpander.expand(BASE, VALUE, noNulls(3), LineInterpolation.STEP_BEFORE_LITERAL, false);
		assertExpansion(out, new double[] { 0, 0, 10, 10, 20 }, new double[] { 5, 9, 9, 3, 3 },
				new int[] { 0, 1, 1, 2, 2 }, new boolean[] { true, false, true, false, true });
	}

	public void testCenterModeJumpsHalfwayBetweenPoints() {
		Expansion out = LineStepExpander.expand(BASE, VALUE, noNulls(3), LineInterpolation.STEP_CENTER_LITERAL, false);
		assertExpansion(out, new double[] { 0, 5, 5, 10, 15, 15, 20 }, new double[] { 5, 5, 9, 9, 9, 3, 3 },
				new int[] { 0, 0, 1, 1, 1, 2, 2 }, new boolean[] { true, false, false, true, false, false, true });
	}

	public void testLinearIsIdentity() {
		Expansion out = LineStepExpander.expand(BASE, VALUE, noNulls(3), LineInterpolation.LINEAR_LITERAL, true);
		assertExpansion(out, BASE, VALUE, new int[] { 0, 1, 2 }, new boolean[] { true, true, true });
	}

	public void testEqualValuesMakeAFlatStepWithoutACorner() {
		Expansion out = LineStepExpander.expand(new double[] { 0, 10 }, new double[] { 5, 5 }, noNulls(2),
				LineInterpolation.STEP_AFTER_LITERAL, false);
		assertExpansion(out, new double[] { 0, 10 }, new double[] { 5, 5 }, new int[] { 0, 1 },
				new boolean[] { true, true });
	}

	public void testADuplicateBaseDropsTheDegenerateCorner() {
		for (LineInterpolation mode : new LineInterpolation[] { LineInterpolation.STEP_AFTER_LITERAL,
				LineInterpolation.STEP_BEFORE_LITERAL, LineInterpolation.STEP_CENTER_LITERAL }) {
			Expansion out = LineStepExpander.expand(new double[] { 0, 0 }, new double[] { 5, 9 }, noNulls(2), mode,
					false);
			assertExpansion(out, new double[] { 0, 0 }, new double[] { 5, 9 }, new int[] { 0, 1 },
					new boolean[] { true, true });
		}
	}

	public void testTwoPointsGetASingleCornerInAfterMode() {
		Expansion out = LineStepExpander.expand(new double[] { 0, 10 }, new double[] { 5, 9 }, noNulls(2),
				LineInterpolation.STEP_AFTER_LITERAL, false);
		assertExpansion(out, new double[] { 0, 10, 10 }, new double[] { 5, 5, 9 }, new int[] { 0, 0, 1 },
				new boolean[] { true, false, true });
	}

	public void testASinglePointAndEmptyInputAreCopiedThrough() {
		assertExpansion(LineStepExpander.expand(new double[] { 0 }, new double[] { 5 }, noNulls(1),
				LineInterpolation.STEP_CENTER_LITERAL, false), new double[] { 0 }, new double[] { 5 }, new int[] { 0 },
				new boolean[] { true });
		assertExpansion(LineStepExpander.expand(new double[0], new double[0], new boolean[0],
				LineInterpolation.STEP_AFTER_LITERAL, true), new double[0], new double[0], new int[0], new boolean[0]);
	}

	public void testANullBreaksTheRunWhenMissingValuesAreNotConnected() {
		Expansion out = LineStepExpander.expand(BASE, new double[] { 5, Double.NaN, 3 },
				new boolean[] { false, true, false }, LineInterpolation.STEP_AFTER_LITERAL, false);
		assertExpansion(out, new double[] { 0, 10, 20 }, new double[] { 5, Double.NaN, 3 }, new int[] { 0, 1, 2 },
				new boolean[] { true, true, true });
	}

	public void testANullGetsItsCornerBeforeTheNullWhenMissingValuesAreConnected() {
		Expansion out = LineStepExpander.expand(BASE, new double[] { 5, Double.NaN, 3 },
				new boolean[] { false, true, false }, LineInterpolation.STEP_AFTER_LITERAL, true);
		assertExpansion(out, new double[] { 0, 20, 10, 20 }, new double[] { 5, 5, Double.NaN, 3 },
				new int[] { 0, 0, 1, 2 }, new boolean[] { true, false, true, true });
	}

	public void testAnIsolatedPointBetweenNullsIsLeftAlone() {
		Expansion out = LineStepExpander.expand(BASE, new double[] { Double.NaN, 9, Double.NaN },
				new boolean[] { true, false, true }, LineInterpolation.STEP_AFTER_LITERAL, false);
		assertExpansion(out, new double[] { 0, 10, 20 }, new double[] { Double.NaN, 9, Double.NaN },
				new int[] { 0, 1, 2 }, new boolean[] { true, true, true });
	}

	public void testLeadingAndTrailingNullsKeepTheirPlaceAroundTheOnlyCorner() {
		Expansion out = LineStepExpander.expand(new double[] { 0, 10, 20, 30 },
				new double[] { Double.NaN, 5, 9, Double.NaN }, new boolean[] { true, false, false, true },
				LineInterpolation.STEP_AFTER_LITERAL, true);
		assertExpansion(out, new double[] { 0, 10, 20, 20, 30 }, new double[] { Double.NaN, 5, 5, 9, Double.NaN },
				new int[] { 0, 1, 1, 2, 3 }, new boolean[] { true, true, false, true, true });
	}

	public void testAllNullPointsAreCopiedThroughUnchanged() {
		Expansion out = LineStepExpander.expand(BASE, new double[] { Double.NaN, Double.NaN, Double.NaN },
				new boolean[] { true, true, true }, LineInterpolation.STEP_CENTER_LITERAL, true);
		assertExpansion(out, new double[] { 0, 10, 20 }, new double[] { Double.NaN, Double.NaN, Double.NaN },
				new int[] { 0, 1, 2 }, new boolean[] { true, true, true });
	}

	public void testANullModeIsIdentity() {
		Expansion out = LineStepExpander.expand(BASE, VALUE, noNulls(3), null, false);
		assertExpansion(out, BASE, VALUE, new int[] { 0, 1, 2 }, new boolean[] { true, true, true });
	}

	public void testRandomInputsKeepTheStepInvariants() {
		Random random = new Random(20260827L);
		int corners = 0;
		// 300 iterations: this suite runs on every CI build
		for (int iteration = 0; iteration < 300; iteration++) {
			int n = random.nextInt(13);
			double[] base = new double[n];
			double[] value = new double[n];
			boolean[] isNull = new boolean[n];
			double b = random.nextInt(5);
			for (int i = 0; i < n; i++) {
				base[i] = b;
				b += 1 + random.nextInt(5);
				isNull[i] = random.nextInt(4) == 0;
				value[i] = isNull[i] ? Double.NaN : random.nextInt(4);
			}
			for (LineInterpolation mode : LineInterpolation.VALUES) {
				for (boolean connect : new boolean[] { false, true }) {
					Expansion out = LineStepExpander.expand(base, value, isNull, mode, connect);
					String context = "iteration " + iteration + " mode " + mode + " connect " + connect; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertCornersBelongToRealPoints(out, isNull, connect, context);
					if (mode == LineInterpolation.LINEAR_LITERAL) {
						// A linear line is not piecewise constant, so the expansion is identity.
						assertEquals("linear is not the identity; " + context, n, out.size()); //$NON-NLS-1$
					} else {
						assertConnectedSegmentsAreAxisParallel(out, isNull, connect, context);
					}
					assertRealVerticesAreTheInput(out, base, value, context);
					corners += out.size() - n;
				}
			}
		}
		assertTrue("too few corners generated: " + corners, corners > 1500); //$NON-NLS-1$
	}

	/**
	 * Asserts that every corner vertex carries the value of a real data point
	 * that is not a missing value, and, unbridged, never stands next to one.
	 */
	private static void assertCornersBelongToRealPoints(Expansion out, boolean[] isNull, boolean connectMissingValue,
			String context) {
		for (int k = 0; k < out.base.length; k++) {
			assertTrue("vertex " + k + " has an out-of-range owner; " + context, //$NON-NLS-1$ //$NON-NLS-2$
					out.owner[k] >= 0 && out.owner[k] < isNull.length);
			if (out.real[k]) {
				continue;
			}
			assertFalse("corner vertex " + k + " belongs to a missing value; " + context, //$NON-NLS-1$ //$NON-NLS-2$
					isNull[out.owner[k]]);
			if (connectMissingValue) {
				continue;
			}
			assertFalse("corner vertex " + k + " follows a missing value; " + context, //$NON-NLS-1$ //$NON-NLS-2$
					k > 0 && isNullVertex(out, k - 1, isNull));
			assertFalse("corner vertex " + k + " precedes a missing value; " + context, //$NON-NLS-1$ //$NON-NLS-2$
					k + 1 < out.base.length && isNullVertex(out, k + 1, isNull));
		}
	}

	/**
	 * Asserts that every pair of joined vertices is axis parallel.
	 */
	private static void assertConnectedSegmentsAreAxisParallel(Expansion out, boolean[] isNull,
			boolean connectMissingValue, String context) {
		int previous = -1;
		boolean runBroken = false;
		for (int k = 0; k < out.base.length; k++) {
			if (isNullVertex(out, k, isNull)) {
				// A missing value breaks the run unless connectMissingValue bridges it.
				runBroken |= !connectMissingValue;
				continue;
			}
			if (previous >= 0 && !runBroken) {
				boolean sameBase = out.base[previous] == out.base[k];
				boolean sameValue = out.value[previous] == out.value[k];
				assertTrue("segment " + previous + " -> " + k + " is not axis parallel; " + context, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						sameBase ^ sameValue);
			}
			previous = k;
			runBroken = false;
		}
	}

	/**
	 * Asserts that the real vertices are the input points in the input order.
	 */
	private static void assertRealVerticesAreTheInput(Expansion out, double[] base, double[] value, String context) {
		double[] realBase = new double[base.length];
		double[] realValue = new double[base.length];
		int[] realOwner = new int[base.length];
		int[] expectedOwner = new int[base.length];
		int count = 0;
		for (int k = 0; k < out.base.length; k++) {
			if (!out.real[k]) {
				continue;
			}
			assertTrue("more real vertices than input points; " + context, count < base.length); //$NON-NLS-1$
			realBase[count] = out.base[k];
			realValue[count] = out.value[k];
			realOwner[count] = out.owner[k];
			expectedOwner[count] = count;
			count++;
		}
		assertEquals("wrong number of real vertices; " + context, base.length, count); //$NON-NLS-1$
		assertEquals("real base sequence; " + context, Arrays.toString(base), Arrays.toString(realBase)); //$NON-NLS-1$
		assertEquals("real value sequence; " + context, Arrays.toString(value), Arrays.toString(realValue)); //$NON-NLS-1$
		assertEquals("real owner sequence; " + context, Arrays.toString(expectedOwner), Arrays.toString(realOwner)); //$NON-NLS-1$
	}

	/**
	 * @return true if the vertex is a real point holding a missing value
	 */
	private static boolean isNullVertex(Expansion out, int k, boolean[] isNull) {
		return out.real[k] && isNull[out.owner[k]];
	}
}
