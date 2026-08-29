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

package org.eclipse.birt.chart.extension.render;

import java.util.Arrays;

import org.eclipse.birt.chart.model.attribute.LineInterpolation;

/**
 * Expands the data points of one series into the vertices of a piecewise
 * constant line.
 * <p>
 * The class does not know the coordinate system. {@code base} holds the base
 * (category) axis coordinate and {@code value} the value axis coordinate, both
 * in the space of the caller, which is usually device pixels.
 * <ul>
 * <li><b>R1</b> - a missing value passes through without a corner vertex;</li>
 * <li><b>R2</b> - a missing value ends the run unless it is bridged; a bridged
 * pair writes its corner vertex directly after its left data point;</li>
 * <li><b>R3</b> - two equal consecutive values stay on one tread;</li>
 * <li><b>R4</b> - the {@link LineInterpolation} places the corner vertex;</li>
 * <li><b>R5</b> - a corner vertex equal to a neighbour vertex is dropped;</li>
 * <li><b>R6</b> - every data point is written unchanged and in order.</li>
 * </ul>
 * A {@code null} mode and {@link LineInterpolation#LINEAR_LITERAL} both expand
 * to the identity.
 *
 * @since 4.25
 */
public final class LineStepExpander {

	/**
	 * Blocks instances. The class has only static members.
	 */
	private LineStepExpander() {
	}

	/**
	 * Holds the vertices of a piecewise constant line in four uncopied arrays.
	 *
	 * @since 4.25
	 */
	public static final class Expansion {

		/** The base (category) axis coordinate of every vertex. */
		public final double[] base;

		/** The value axis coordinate of every vertex. */
		public final double[] value;

		/**
		 * For every vertex, the index of the data point whose value it carries.
		 * A corner vertex names a data point that has a value.
		 */
		public final int[] owner;

		/** For every vertex, {@code true} for a data point. */
		public final boolean[] real;

		/**
		 * Creates an expansion from its four parallel arrays.
		 */
		Expansion(double[] base, double[] value, int[] owner, boolean[] real) {
			this.base = base;
			this.value = value;
			this.owner = owner;
			this.real = real;
		}

		/**
		 * @return the number of vertices
		 */
		public int size() {
			return base.length;
		}
	}

	/**
	 * Expands the data points of one series into the vertices of a piecewise
	 * constant line. The caller must compute {@code isNull} the way the renderer
	 * does, that is with {@code BaseRenderer.isNaN(Object)}.
	 *
	 * @param base
	 * @param value
	 * @param isNull
	 * @param mode
	 * @param connectMissingValue
	 * @return the vertices
	 */
	public static Expansion expand(double[] base, double[] value, boolean[] isNull, LineInterpolation mode,
			boolean connectMissingValue) {
		int n = base.length;
		// One data point gives one vertex plus at most two corner vertices (Center).
		Vertices out = new Vertices(3 * n);

		for (int i = 0; i < n; i++) {
			// R6: the data point itself, unchanged and in order.
			out.point(base[i], value[i], i);

			// R1: no corner vertex for a missing value or a linear line.
			if (isNull[i] || mode == null || mode == LineInterpolation.LINEAR_LITERAL) {
				continue;
			}

			// R2: the next connected data point.
			int right = nextConnected(isNull, i, connectMissingValue);
			if (right < 0) {
				continue;
			}

			double bL = base[i];
			double vL = value[i];
			double bR = base[right];
			double vR = value[right];

			// R3: two equal values stay on one tread.
			if (vL == vR) {
				continue;
			}

			// R4: the geometry of the corner vertex.
			switch (mode) {
			case STEP_AFTER_LITERAL:
				out.corner(bR, vL, i, bR, vR);
				break;
			case STEP_BEFORE_LITERAL:
				out.corner(bL, vR, right, bR, vR);
				break;
			case STEP_CENTER_LITERAL:
				double bM = (bL + bR) / 2;
				out.corner(bM, vL, i, bR, vR);
				out.corner(bM, vR, right, bR, vR);
				break;
			default:
				break;
			}
		}

		return out.trim();
	}

	/**
	 * Returns the index of the next data point in the same run, or {@code -1}.
	 */
	private static int nextConnected(boolean[] isNull, int i, boolean connectMissingValue) {
		for (int right = i + 1; right < isNull.length; right++) {
			if (!isNull[right]) {
				return right;
			}
			if (!connectMissingValue) {
				return -1;
			}
		}
		return -1;
	}

	/**
	 * Collects the output vertices and drops the degenerate corner vertex (R5).
	 */
	private static final class Vertices {

		/** The base axis coordinate of every vertex written so far. */
		private final double[] base;

		/** The value axis coordinate of every vertex written so far. */
		private final double[] value;

		/** For every vertex, the index of the data point whose value it carries. */
		private final int[] owner;

		/** For every vertex, {@code true} for a data point. */
		private final boolean[] real;

		/** The number of vertices written so far. */
		private int size;

		Vertices(int capacity) {
			base = new double[capacity];
			value = new double[capacity];
			owner = new int[capacity];
			real = new boolean[capacity];
		}

		void point(double b, double v, int index) {
			append(b, v, index, true);
		}

		/**
		 * Appends one corner vertex, unless it equals the vertex before it
		 * or the data point it leads to (R5).
		 */
		void corner(double b, double v, int index, double bRight, double vRight) {
			if (size > 0 && base[size - 1] == b && value[size - 1] == v) {
				return;
			}
			if (b == bRight && v == vRight) {
				return;
			}
			append(b, v, index, false);
		}

		private void append(double b, double v, int index, boolean isReal) {
			base[size] = b;
			value[size] = v;
			owner[size] = index;
			real[size] = isReal;
			size++;
		}

		Expansion trim() {
			return new Expansion(Arrays.copyOf(base, size), Arrays.copyOf(value, size), Arrays.copyOf(owner, size),
					Arrays.copyOf(real, size));
		}
	}
}
