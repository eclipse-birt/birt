/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.RGB;

import com.ibm.icu.util.StringTokenizer;

public class ColorHelper {

	public final static String BACKGROUND = "background";//$NON-NLS-1$
	public final static String BOLD = "bold";//$NON-NLS-1$
	public final static String FOREGROUND = "foreground";//$NON-NLS-1$
	public final static String NAME = "name";//$NON-NLS-1$
	private final static String STYLE_SEPARATOR = "|"; //$NON-NLS-1$
	private final static String NULL = "null"; //$NON-NLS-1$

	/**
	 * Return an RGB String given the int r, g, b values
	 */
	public static String getColorString(int r, int g, int b) {
		return "#" + getHexString(r, 2) + getHexString(g, 2) + getHexString(b, 2);//$NON-NLS-1$
	}

	private static String getHexString(int value, int minWidth) {
		String hexString = Integer.toHexString(value);
		for (int i = hexString.length(); i < minWidth; i++) {
			hexString = "0" + hexString;//$NON-NLS-1$
		}
		return hexString;
	}

	/**
	 * Generates a preference string to be placed in preferences from the given
	 * String array.
	 *
	 * @param stylePrefs assumes not null and should be in the form of String[0] =
	 *                   Foreground RGB String, String[1] = Background RGB String,
	 *                   String[2] = Bold true/false
	 *
	 * @return String in the form of Foreground RGB String | Background RGB String |
	 *         Bold true/false
	 */
	public static String packStylePreferences(String[] stylePrefs) {
		StringBuilder styleString = new StringBuilder();

		for (int i = 0; i < stylePrefs.length; ++i) {
			String s = stylePrefs[i];
			if (i < 2) {
				if (s != null) {
					styleString.append(s);
				} else {
					styleString.append(NULL);
				}
			} else {
				styleString.append(Boolean.valueOf(s));
			}

			// add in the separator (except on last iteration)
			if (i < stylePrefs.length - 1) {
				styleString.append(" " + STYLE_SEPARATOR + " "); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		return styleString.toString();
	}

	/**
	 * @return org.eclipse.swt.graphics.RGB
	 * @param anRGBString java.lang.String
	 */
	public static RGB toRGB(String anRGBString) {
		RGB result = null;
		if (anRGBString.length() > 6 && anRGBString.charAt(0) == '#') {
			int r = 0;
			int g = 0;
			int b = 0;
			try {
				r = Integer.parseInt(anRGBString.substring(1, 3), 16);
				g = Integer.parseInt(anRGBString.substring(3, 5), 16);
				b = Integer.parseInt(anRGBString.substring(5, 7), 16);
				result = new RGB(r, g, b);
			} catch (NumberFormatException e) {
				ExceptionHandler.handle(e);
			}
		}
		return result;
	}

	/**
	 * @return java.lang.String
	 * @param anRGB org.eclipse.swt.graphics.RGB
	 */
	public static String toRGBString(RGB anRGB) {
		if (anRGB == null) {
			return "#000000";//$NON-NLS-1$
		}
		String red = Integer.toHexString(anRGB.red);
		while (red.length() < 2) {
			red = "0" + red;//$NON-NLS-1$
		}
		String green = Integer.toHexString(anRGB.green);
		while (green.length() < 2) {
			green = "0" + green;//$NON-NLS-1$
		}
		String blue = Integer.toHexString(anRGB.blue);
		while (blue.length() < 2) {
			blue = "0" + blue;//$NON-NLS-1$
		}
		return "#" + red + green + blue;//$NON-NLS-1$
	}

	/**
	 * Extracts the foreground (RGB String), background (RGB String), bold (boolean
	 * String) from the given preference string.
	 *
	 * @param preference should be in the form of Foreground RGB String | Background
	 *                   RGB String | Bold true/false | Italic true/false |
	 *                   Strikethrough true/false | Underline true/false
	 * @return String[] where String[0] = Foreground RGB String, String[1] =
	 *         Background RGB String, String[2] = Bold true/false, 3 = Italic
	 *         true/false, 4 = Strikethrough true/false, 5 = Underline true/false;
	 *         indexes 2-4 may be null if we ran into problems extracting
	 */
	public static String[] unpackStylePreferences(String preference) {
		String[] stylePrefs = new String[6];
		if (preference != null) {
			StringTokenizer st = new StringTokenizer(preference, STYLE_SEPARATOR);
			if (st.hasMoreTokens()) {
				String foreground = st.nextToken().trim();
				stylePrefs[0] = foreground;
			} else {
				stylePrefs[0] = NULL;
			}
			if (st.hasMoreTokens()) {
				String background = st.nextToken().trim();
				stylePrefs[1] = background;
			} else {
				stylePrefs[1] = NULL;
			}

			if (st.hasMoreTokens()) {
				String bold = st.nextToken().trim();
				stylePrefs[2] = Boolean.valueOf(bold).toString();
			} else {
				stylePrefs[2] = Boolean.FALSE.toString();
			}
			if (st.hasMoreTokens()) {
				String italic = st.nextToken().trim();
				stylePrefs[3] = Boolean.valueOf(italic).toString();
			} else {
				stylePrefs[3] = Boolean.FALSE.toString();
			}
			if (st.hasMoreTokens()) {
				String strikethrough = st.nextToken().trim();
				stylePrefs[4] = Boolean.valueOf(strikethrough).toString();
			} else {
				stylePrefs[4] = Boolean.FALSE.toString();
			}
			if (st.hasMoreTokens()) {
				String underline = st.nextToken().trim();
				stylePrefs[5] = Boolean.valueOf(underline).toString();
			} else {
				stylePrefs[5] = Boolean.FALSE.toString();
			}
		}

		return stylePrefs;
	}

	/**
	 * Attempts to lookup the RGB value for <code>key</code> from the color
	 * registry. If one is not found, the <code>defaultRGB</code> is used.
	 *
	 * @param registry   The ColorRegistry to search for the RGB value
	 * @param key        The key that the RGB value is stored under in the registry
	 * @param defaultRGB The default RGB value to return in the absence of one from
	 *                   the color registry
	 *
	 * @return The RGB value from the color registry for a given key, if it exists.
	 *         Otherwise, return the default RGB value.
	 */
	public static RGB findRGB(ColorRegistry registry, String key, RGB defaultRGB) {
		if (registry.hasValueFor(key)) {
			return registry.getRGB(key);
		}
		return defaultRGB;
	}

	/**
	 * Attempts to find the RGB string for <code>key</code> from the color registry.
	 * If one is not found, an RGB string is generated from the parameters
	 * <code>r,g,b</code>.
	 *
	 * @param registry The ColorRegistry to search for the RGB value
	 * @param key      The key that the RGB value is stored under in the registry
	 * @param r        The default red value
	 * @param g        The default green value
	 * @param b        The default blue value
	 *
	 * @return The String RGB value from the color registry for a given key, if it
	 *         exists. Otherwise, return the string RGB value created from the
	 *         default r,g,b parameters.
	 *
	 */
	public static String findRGBString(ColorRegistry registry, String key, int r, int g, int b) {
		if (registry.hasValueFor(key)) {
			return toRGBString(registry.getRGB(key));
		}
		return getColorString(r, g, b);
	}

}
