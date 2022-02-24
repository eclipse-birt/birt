/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.computation;

import java.util.EnumSet;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * Helper class to limit the size of a label by wrapping its text or shorten its
 * text with ellipsis.
 */

public class LabelLimiter {

	/**
	 * Options used by method limitLabelSize. Option
	 */
	public static enum Option {
		/**
		 * limitLabelSize will return the old maxWidth instead of the calculated one
		 * (smaller).
		 */
		FIX_WIDTH,
		/**
		 * limitLabelSize will return the old maxHeight instead of the calculated one
		 * (smaller).
		 */
		FIX_HEIGHT
	};

	private static final IGObjectFactory goFactory = GObjectFactory.instance();
	private double maxWidth;
	private double maxHeight;
	private double wrapping;
	private boolean bSuccessed = true;

	/**
	 * Constructor
	 * 
	 * @param maxWidth
	 * @param maxHeight
	 * @param wrapping
	 */
	public LabelLimiter(double maxWidth, double maxHeight, double wrapping) {
		this(maxWidth, maxHeight, wrapping, true);
	}

	private LabelLimiter(double maxWidth, double maxHeight, double wrapping, boolean bSuccessed) {
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.wrapping = wrapping;
		this.bSuccessed = bSuccessed;
	}

	public double computeWrapping(IDisplayServer xs, Label la) {
		return computeWrapping(xs, la, this);
	}

	/**
	 * Returns a bounding box using maxWidth and maxHeight
	 * 
	 * @param bb will be updated and returned if not null, otherwise a new bounding
	 *           box will be created.
	 * @return
	 */
	public BoundingBox getBounding(BoundingBox bb) {
		if (bb == null) {
			bb = new BoundingBox(0, 0, 0, maxWidth, maxHeight, 0);
		}
		return bb;
	}

	/**
	 * Compute the wrapping with maxWidth, maxHeight. If the wrapping is set to 0,
	 * namely auto, this method should be called before calling limitLabelSize.
	 * 
	 * @param xs
	 * @param la
	 * @param lbLimit
	 * @return
	 */
	public static final double computeWrapping(IDisplayServer xs, Label la, LabelLimiter lbLimit) {
		double dWrapping = 0;
		boolean bIsSWT = xs.getClass().getName().equals("org.eclipse.birt.chart.device.swt.SwtDisplayServer"); //$NON-NLS-1$
		final double dSafe = bIsSWT ? 10 : 0;

		if (lbLimit != null) {
			double dScale = xs.getDpiResolution() / 72d;
			double fRotation = la.getCaption().getFont().getRotation();
			Insets insets = goFactory.scaleInsets(la.getInsets(), dScale);
			double dInsetsWidth = insets.getLeft() + insets.getRight();

			if (ChartUtil.mathEqual(fRotation, 0)) {
				dWrapping = Math.floor(lbLimit.maxWidth - dInsetsWidth) - dSafe;
			} else if (ChartUtil.mathEqual(fRotation, 90)) {
				dWrapping = Math.floor(lbLimit.maxHeight - dInsetsWidth) - dSafe;
			} else {
				fRotation %= 180;
				if (fRotation < 0) {
					fRotation += 180;
				}
				double rad = Math.toRadians(fRotation % 90);
				double tg = Math.tan(rad);
				double m = 1 - tg * tg;
				double r = 2 * tg / (1 + tg * tg);
				// double wd1 = lbLimit.maxWidth - dInsetsWidth;
				// double ht1 = lbLimit.maxHeight - dInsetsHeight;
				double wd1 = lbLimit.maxWidth;
				double ht1 = lbLimit.maxHeight;
				double b, d;
				double wd2;

				if (wd1 < ht1) {
					if (((tg < 1) && (r < wd1 / ht1)) || ((tg > 1) && (r < wd1 / ht1))) {
						b = (wd1 - ht1 * tg) / m;
						d = (ht1 - wd1 * tg) / m;
					} else {
						b = wd1 / 2;
						d = b / tg;
					}

				} else {
					if (((tg < 1) && (r < ht1 / wd1)) || ((tg > 1) && (r < ht1 / wd1))) {
						b = (wd1 - ht1 * tg) / m;
						d = (ht1 - wd1 * tg) / m;
					} else {
						d = ht1 / 2;
						b = d / tg;
					}
				}

				double cos = Math.cos(rad);

				if (fRotation < 90) {
					wd2 = b / cos;
					// ht2 = d / cos;
				} else {
					wd2 = d / cos;
					// ht2 = b / cos;
				}

				dWrapping = Math.floor(wd2) - dInsetsWidth - dSafe;
			}
			lbLimit.wrapping = dWrapping;
		}

		return dWrapping;
	}

	/**
	 * modify the text of la to fit the limit size.
	 * 
	 * @param xs
	 * @param la
	 * @return
	 * @throws ChartException
	 */
	public LabelLimiter limitLabelSize(IChartComputation cComp, IDisplayServer xs, Label la) throws ChartException {
		return limitLabelSize(cComp, xs, la, this, EnumSet.noneOf(Option.class));
	}

	/**
	 * modify the text of la to fit the limit size.
	 * 
	 * @param xs
	 * @param la
	 * @param options
	 * @return
	 * @throws ChartException
	 */
	public LabelLimiter limitLabelSize(IChartComputation cComp, IDisplayServer xs, Label la, EnumSet<Option> options)
			throws ChartException {
		return limitLabelSize(cComp, xs, la, this, options);
	}

	/**
	 * To compute the text of the label with a limited size, the label text will be
	 * wrapped and shortened with ellipsis if required, the size of the label bound
	 * will be returned.
	 * 
	 * @param xs
	 * @param la
	 * @param maxSize
	 * @param lbLimit
	 * @param options
	 * @return
	 * @throws ChartException
	 */
	public static final LabelLimiter limitLabelSize(IChartComputation cComp, IDisplayServer xs, Label la,
			LabelLimiter lbLimit, EnumSet<Option> options) throws ChartException {
		double maxWidth, maxHeight, wrapping;
		boolean bSuccessed = true;

		if (lbLimit != null) {
			EllipsisHelper eHelper = EllipsisHelper.simpleInstance(cComp, xs, la, null);
			if (eHelper.checkLabelEllipsis(la.getCaption().getValue(), lbLimit)) {
				maxWidth = eHelper.getTester().getWidth();
				maxHeight = eHelper.getTester().getHeight();
			} else {
				la.getCaption().setValue(""); //$NON-NLS-1$
				maxWidth = 0;
				maxHeight = 0;
				bSuccessed = false;
			}
			wrapping = lbLimit.getWrapping();
		} else {
			BoundingBox bb = cComp.computeLabelSize(xs, la, 0, null);
			maxWidth = bb.getWidth();
			maxHeight = bb.getHeight();
			wrapping = 0;
		}

		if (options.contains(Option.FIX_WIDTH)) {
			maxWidth = lbLimit.maxWidth;
		}

		if (options.contains(Option.FIX_HEIGHT)) {
			maxHeight = lbLimit.maxHeight;
		}

		return new LabelLimiter(maxWidth, maxHeight, wrapping, bSuccessed);
	}

	/**
	 * @return Returns the maxWidth.
	 */
	public final double getMaxWidth() {
		return maxWidth;
	}

	/**
	 * @param maxWidth The maxWidth to set.
	 */
	public final void setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
	}

	/**
	 * @return Returns the maxHeight.
	 */
	public final double getMaxHeight() {
		return maxHeight;
	}

	/**
	 * @param maxHeight The maxHeight to set.
	 */
	public final void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
	}

	/**
	 * @return Returns the wrapping.
	 */
	public final double getWrapping() {
		return wrapping;
	}

	/**
	 * @param wrapping The wrapping to set.
	 */
	public final void setWrapping(double wrapping) {
		this.wrapping = wrapping;
	}

	/**
	 * @return Returns the bSuccessed.
	 */
	public final boolean isSuccessed() {
		return bSuccessed;
	}

}
