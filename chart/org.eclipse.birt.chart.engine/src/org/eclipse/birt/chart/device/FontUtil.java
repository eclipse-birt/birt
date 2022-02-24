/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.device;

import java.util.HashMap;
import java.util.Map;

/**
 * Font utility class.
 * 
 * @since 2.3
 */
public final class FontUtil {
	// Defines default font families.
	public static final String LOGIC_FONT_FAMILY_SERIF = "serif"; //$NON-NLS-1$

	public static final String LOGIC_FONT_FAMILY_SANS_SERIF = "sans-serif"; //$NON-NLS-1$

	public static final String LOGIC_FONT_FAMILY_CURSIVE = "cursive"; //$NON-NLS-1$

	public static final String LOGIC_FONT_FAMILY_FANTASY = "fantasy"; //$NON-NLS-1$

	public static final String LOGIC_FONT_FAMILY_MONOSPACE = "monospace"; //$NON-NLS-1$

	public static final String FONT_FAMILY_COURIER_NEW = "Courier New"; //$NON-NLS-1$

	public static final String FONT_FAMILY_IMPACT = "Impact"; //$NON-NLS-1$

	public static final String FONT_FAMILY_COMIC_SANS_MS = "Comic Sans MS"; //$NON-NLS-1$

	public static final String FONT_FAMILY_ARIAL = "Arial"; //$NON-NLS-1$

	public static final String FONT_FAMILY_TIMES_NEW_ROMAN = "Times New Roman"; //$NON-NLS-1$

	/**
	 * Map between CSS style font family to system font family
	 */
	private static Map<String, String> sFamilyMap = new HashMap();

	/**
	 * Static table stores the font families. It provides the font name and the
	 * family of the fonts.
	 */
	static {
		sFamilyMap.put(LOGIC_FONT_FAMILY_SERIF, FONT_FAMILY_TIMES_NEW_ROMAN);
		sFamilyMap.put(LOGIC_FONT_FAMILY_SANS_SERIF, FONT_FAMILY_ARIAL);
		sFamilyMap.put(LOGIC_FONT_FAMILY_CURSIVE, FONT_FAMILY_COMIC_SANS_MS);
		sFamilyMap.put(LOGIC_FONT_FAMILY_FANTASY, FONT_FAMILY_IMPACT);
		sFamilyMap.put(LOGIC_FONT_FAMILY_MONOSPACE, FONT_FAMILY_COURIER_NEW);
	};

	/**
	 * Returns the final font family name.
	 * 
	 * @param fontFamily specified font family name.
	 * @return actually font family name.
	 */
	public static String getFontFamily(String fontFamily) {
		String destFontName = (String) sFamilyMap.get(fontFamily);

		if (destFontName == null) {
			destFontName = fontFamily;
		}

		return destFontName;
	}
}
