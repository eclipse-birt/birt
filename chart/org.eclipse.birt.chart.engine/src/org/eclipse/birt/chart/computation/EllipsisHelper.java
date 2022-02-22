/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.component.Label;

/**
 * Provides a helper class to shorten a string with ellipsis. EllipsisHelper
 */
public class EllipsisHelper {

	/**
	 *
	 * ITester
	 */
	public interface ITester {

		double getWidth() throws ChartException;

		double getHeight() throws ChartException;

		boolean testLabelVisible(String strNew, Object oPara) throws ChartException;
	}

	public static final String ELLIPSIS_STRING = "..."; //$NON-NLS-1$
	private int iMinCharToView = 0;
	private int iVisChar = 0;
	private String sText;
	private final ITester tester;

	public EllipsisHelper(ITester tester_, int iMinCharToView) {
		tester = tester_;
		this.iMinCharToView = iMinCharToView;
	}

	public ITester getTester() {
		return this.tester;
	}

	public void setIMinCharToView(int iMinCharToView) {
		this.iMinCharToView = iMinCharToView;
	}

	public static String ellipsisString(String str, int iVisChar) {
		if (iVisChar > 0 && iVisChar < str.length()) {
			return str.substring(0, iVisChar) + ELLIPSIS_STRING;
		} else {
			return str;
		}
	}

	/**
	 * Returns the visible char count before the ellipsis, 0 if no ellipsis is used.
	 * e.g. if the text is "abcd..." then 4 will be returned. if the text is
	 * "abcdefg" then 0 will be returned.
	 *
	 * @return count of visible chars.
	 */
	public int getVisibleCharCount() {
		return iVisChar;
	}

	private boolean testNthChar(int iChar, Object oPara) throws ChartException {
		String newText = sText.substring(0, iChar) + ELLIPSIS_STRING;
		boolean bResult = tester.testLabelVisible(newText, oPara);
		if (bResult) {
			iVisChar = iChar;
		}
		return bResult;
	}

	public boolean checkLabelEllipsis(String sText_, Object oPara) throws ChartException {
		sText = sText_;
		this.iVisChar = 0;
		boolean bCanViewFullText = tester.testLabelVisible(sText, oPara);

		if (bCanViewFullText) {
			// full text can be displayed, do not need ellipsis
			return true;
		}

		if (iMinCharToView <= 0) {
			// do not use ellipsis
			return bCanViewFullText;
		}

		int len = sText.length() - 1;

		if ((len < iMinCharToView) || !testNthChar(iMinCharToView, oPara)) {
			return false;
		}

		if (len < 8) {
			for (int iChar = len; iChar >= iMinCharToView; iChar--) {
				if (testNthChar(iChar, oPara)) {
					return true;
				}
			}

			return false;
		} else {
			int iStart = iMinCharToView;
			int iEnd = len;
			int iChar = iEnd;

			for (int iLimit = 19; iLimit > 0 && iEnd > iStart + 1; iLimit--) {
				iChar = (iStart + iEnd) / 2;

				if (testNthChar(iChar, oPara)) {
					iStart = iChar;
				} else {
					iEnd = iChar;
				}
			}

			if (iChar != iStart) {
				return testNthChar(iStart, oPara);
			} else {
				iVisChar = iChar;
				return true;
			}
		}
	}

	public static ITester createSimpleTester(IChartComputation cComp, IDisplayServer xs, Label la, Double fontHeight)
			throws ChartException {
		return new SimpleTester(cComp, xs, la, fontHeight);
	}

	public static EllipsisHelper simpleInstance(IChartComputation cComp, IDisplayServer xs, Label la, Double fontHeight)
			throws ChartException {
		return new EllipsisHelper(createSimpleTester(cComp, xs, la, fontHeight), 1);
	}

	/**
	 * A simple implementation of EllipsisHelper.ITester SimpleTester
	 */
	private static class SimpleTester implements EllipsisHelper.ITester {

		private final IChartComputation cComp;
		private final IDisplayServer xs;
		private final Label la;
		private final Double fontHeight;
		private BoundingBox bb = null;

		public SimpleTester(IChartComputation cComp, IDisplayServer xs, Label la, Double fontHeight)
				throws ChartException {
			this.cComp = cComp;
			this.xs = xs;
			this.la = la;
			if (fontHeight != null) {
				this.fontHeight = fontHeight;
			} else {
				this.fontHeight = cComp.computeFontHeight(xs, la);
			}
		}

		private void computeSize(double dWrapping) throws ChartException {
			bb = cComp.computeLabelSize(xs, la, dWrapping, fontHeight);
		}

		private boolean testSize(LabelLimiter lblLimit) {
			return bb.getWidth() <= lblLimit.getMaxWidth() && bb.getHeight() <= lblLimit.getMaxHeight();
		}

		@Override
		public boolean testLabelVisible(String strNew, Object para) throws ChartException {
			LabelLimiter lbLimit = (LabelLimiter) para;
			la.getCaption().setValue(strNew);
			if (lbLimit.getMaxHeight() < fontHeight) {
				return false;
			}
			computeSize(0);
			if (testSize(lbLimit)) {
				return true;
			}
			if (lbLimit.getWrapping() > 0) {
				computeSize(lbLimit.getWrapping());
				return testSize(lbLimit);
			} else {
				return false;
			}
		}

		@Override
		public double getHeight() throws ChartException {
			if (bb == null) {
				computeSize(0);
			}
			return bb.getHeight();
		}

		@Override
		public double getWidth() throws ChartException {
			if (bb == null) {
				computeSize(0);
			}
			return bb.getWidth();
		}

	}

}
