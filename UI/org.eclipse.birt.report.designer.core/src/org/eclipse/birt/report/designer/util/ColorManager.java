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

package org.eclipse.birt.report.designer.util;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.PlatformUI;

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
	 * Gets the color.
	 *
	 * @param id
	 * @param defaultRGB
	 * @return
	 */
	public static Color getColor(String id, RGB defaultRGB) {
		ColorRegistry registry = null;
		if (PlatformUI.isWorkbenchRunning()) {
			registry = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getColorRegistry();
		}
		RGB rgb = findRGB(registry, id, defaultRGB);
		return getColor(rgb);
	}

	private static RGB findRGB(ColorRegistry registry, String key, RGB defaultRGB) {
		if (registry == null) {
			return defaultRGB;
		}

		RGB rgb = registry.getRGB(key);
		if (rgb != null) {
			return rgb;
		}
		return defaultRGB;
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

	/**
	 * Creates a new <code>Color</code> that is a darker version of the specified
	 * color.
	 *
	 * @param c the specified color value.
	 */
	public static Color darker(Color c) {
		if (c == null) {
			return null;
		}

		java.awt.Color color = new java.awt.Color(c.getRed(), c.getGreen(), c.getBlue());

		color = color.darker();
		return getColor(color.getRed(), color.getGreen(), color.getBlue());
	}

	/**
	 * Creates a new <code>Color</code> that is a brighter version of the specified
	 * color.
	 *
	 * @param c the specified color value.
	 */
	public static Color brighter(Color c) {
		if (c == null) {
			return null;
		}

		java.awt.Color color = new java.awt.Color(c.getRed(), c.getGreen(), c.getBlue());

		color = color.brighter();
		return getColor(color.getRed(), color.getGreen(), color.getBlue());
	}

	/**
	 * Creates a new <code>Color</code> that is a brighter version of this
	 * <code>Color</code>.
	 *
	 * @param origColor   initial color.
	 * @param brightColor the target bright color.
	 * @return a new <code>Color</code> object that is a brighter version of this
	 *         <code>Color</code>.
	 */
	public static Color brighter(Color origColor, Color brightColor) {
		return getColor((origColor.getRed() + brightColor.getRed()) / 2,
				(origColor.getGreen() + brightColor.getGreen()) / 2, (origColor.getBlue() + brightColor.getBlue()) / 2);
	}

	/**
	 * Creates a new <code>Color</code> that is a darker version of this
	 * <code>Color</code>.
	 * <p>
	 * This method applies an arbitrary scale factor to each of the three RGB
	 * components of this <code>Color</code> to create a darker version of this
	 * <code>Color</code>. Although <code>brighter</code> and <code>darker</code>
	 * are inverse operations, the results of a series of invocations of these two
	 * methods might be inconsistent because of rounding errors.
	 *
	 * @param origColor initial color.
	 * @param darkColor the target dark color.
	 * @return a new <code>Color</code> object that is a darker version of this
	 *         <code>Color</code>.
	 */
	public static Color darker(Color origColor, Color darkColor) {
		double redFactor = darkColor.getRed() / 255.0;
		double greenFactor = darkColor.getGreen() / 255.0;
		double blueFactor = darkColor.getBlue() / 255.0;

		return getColor(Math.max((int) (origColor.getRed() * redFactor), 0),
				Math.max((int) (origColor.getGreen() * greenFactor), 0),
				Math.max((int) (origColor.getBlue() * blueFactor), 0));
	}
}
