/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import org.eclipse.swt.graphics.RGB;

/**
 * This class integrated some methods that will be used in GUI. It provides the
 * information that GUI will use and is called widely. *
 */
public class DEUtil {
	/**
	 * Converts an Integer value to an RGB object value, the Integer format is
	 * 0xRRGGBB.
	 * 
	 * @param rgbValue Integer value.
	 * @return RGB value.
	 */
	public static RGB getRGBValue(int rgbValue) {
		if (rgbValue == -1) {
			return null;
		}

		return new RGB((rgbValue >> 16) & 0xff, (rgbValue >> 8) & 0xff, rgbValue & 0xff);
	}

	/**
	 * Converts an RGB object value to an Integer value, the Integer format is
	 * 0xRRGGBB.
	 * 
	 * @param rgb RGB value.
	 * @return Integer value.
	 */
	public static int getRGBInt(RGB rgb) {
		if (rgb == null) {
			return -1;
		}

		return ((rgb.red & 0xff) << 16) | ((rgb.green & 0xff) << 8) | (rgb.blue & 0xff);
	}

}