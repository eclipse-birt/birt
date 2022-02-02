/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.pptx.util;

import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.ooxml.util.OOXmlUtil;

public class PPTXUtil {
	public static int convertToPointer(int milliPointerValue) {
		return milliPointerValue / 1000;
	}

	public static int convertToEnums(double milliPointerValue) {
		return (int) OOXmlUtil.convertPointerToEmus(milliPointerValue / 1000);
	}

	/**
	 * The default will be solid, double is not implemented as cell does not support
	 * it
	 * 
	 * @param style
	 * @return
	 */
	public static String parseStyle(int style) {
		// set by default solid
		switch (style) {

		case BoxStyle.BORDER_STYLE_DASHED:
			return "dash";

		case BoxStyle.BORDER_STYLE_DOTTED:
			return "dot";

		// double is not specified in the cell dash.

		}

		return "solid";
	}

	public static int pixelToEmu(int pixels, int dpi) {
		if (dpi <= 0) {// default resolution:
			dpi = 96;
		}
		return pixels * (int) ((float) 914400 / dpi);

	}

	public static float parsePercentageOffset(int contianermeasure, int contianersubmeasure) {
		float diffpercentage = (float) (contianermeasure - contianersubmeasure) / contianermeasure;
		return diffpercentage * 100000;
	}

	/**
	 * convert String dimension to pptx enum
	 */
	public static int convertCssToEnum(String cssDimension) {
		if (cssDimension == null || "0".equals(cssDimension)) {
			return 0;
		}
		return convertToEnums(DimensionType.parserUnit(cssDimension).convertTo(DimensionType.UNITS_PT) * 1000);
	}
}
