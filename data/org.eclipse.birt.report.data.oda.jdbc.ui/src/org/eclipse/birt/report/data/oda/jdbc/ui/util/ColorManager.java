/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Manages color resource.
 */

public final class ColorManager {

	/**
	 * This map stores color name - Color pairs, used to quickly lookup a Color of a
	 * predefined color.
	 *
	 * @param c color value
	 */
	public static Color getColor(int c) {
		RGB rgb = DEUtil.getRGBValue(c);
		return getColor(rgb);
	}

	/**
	 * This map stores color name - Color pairs, used to quickly lookup a Color of a
	 * predefined color.
	 *
	 * @param red   red value of RGB
	 * @param green green value of RGB
	 * @param blue  blue value of RGB
	 */
	public static Color getColor(int red, int green, int blue) {
		return getColor(new RGB(red, green, blue));
	}

	/**
	 * This map stores color name - Color pairs, used to quickly lookup a Color of a
	 * predefined color.
	 *
	 * @param rgb RGB value of color
	 */
	public static Color getColor(RGB rgb) {
		if (rgb == null) {
			return null;
		}

		String key = rgb.toString();
		Color color = JFaceResources.getColorRegistry().get(key);
		if (color == null) {
			JFaceResources.getColorRegistry().put(key, rgb);
			color = JFaceResources.getColorRegistry().get(key);
		}
		return color;
	}
}
