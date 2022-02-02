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

package org.eclipse.birt.chart.device.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.CursorType;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.emf.common.util.EList;

/**
 * The class defines fields and methods for Cascading Style Sheet.
 * 
 * @since 2.5
 */

public class CSSHelper {
	public static final String CURSOR_STYLE_PREFIX = "cursor:"; //$NON-NLS-1$

	public static final Map<CursorType, String> CSS_CURSOR_MAP = new HashMap<org.eclipse.birt.chart.model.attribute.CursorType, String>();
	static {
		CSS_CURSOR_MAP.put(CursorType.AUTO, "auto"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.CROSSHAIR, "crosshair"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.DEFAULT, "default"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.POINTER, "pointer"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.MOVE, "move"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.TEXT, "text"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.WAIT, "wait"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.ERESIZE, "e-resize"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.NE_RESIZE, "ne-resize"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.NW_RESIZE, "nw-resize"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.NRESIZE, "n-resize"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.SE_RESIZE, "se-resize"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.SW_RESIZE, "sw-resize"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.SRESIZE, "s-resize"); //$NON-NLS-1$
		CSS_CURSOR_MAP.put(CursorType.WRESIZE, "w-resize"); //$NON-NLS-1$
	}

	public static String getCSSCursorValue(Cursor cursor) {
		if (cursor == null || cursor.getType() == null) {
			return null;
		}

		StringBuffer value = new StringBuffer(CSSHelper.CURSOR_STYLE_PREFIX);
		value.append(" ");//$NON-NLS-1$
		if (cursor.getType() != CursorType.CUSTOM) {
			value.append(CSSHelper.CSS_CURSOR_MAP.get(cursor.getType())).append(";"); //$NON-NLS-1$
		} else {
			// Custom cursors.
			EList<Image> cursorImages = cursor.getImage();
			int i = 0;
			for (Image uri : cursorImages) {
				if (uri.getURL() == null || uri.getURL().trim().length() == 0) {
					continue;
				}

				String sUri = uri.getURL();
				if (sUri.startsWith("\"") && sUri.endsWith("\"")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					sUri = sUri.substring(1, sUri.length() - 1);
				}
				if (sUri.trim().length() == 0) {
					continue;
				}

				if (i != 0) {
					value.append(","); //$NON-NLS-1$
				}

				value.append("url(").append(sUri).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
				i++;
			}
			if (cursorImages.size() > 0) {
				value.append(",auto;"); //$NON-NLS-1$
			} else {
				value.append("auto;"); //$NON-NLS-1$
			}
		}

		return value.toString();
	}

	/**
	 * Converts CSS properties to hyphen format.
	 * 
	 * @param cssProperties
	 * @return string format
	 * @since 2.5.1
	 */
	public static String getStylingHyphenFormat(String cssProperties) {
		if (cssProperties == null) {
			return null;
		}

		StringBuilder returnStr = new StringBuilder();
		String[] properties = cssProperties.split(";"); //$NON-NLS-1$
		if (properties == null || properties.length == 0) {
			return cssProperties;
		}

		for (int j = 0; j < properties.length; j++) {
			if (j != 0) {
				returnStr.append(";"); //$NON-NLS-1$
			}
			String[] pair = properties[j].split(":");//$NON-NLS-1$
			if (pair == null || pair.length <= 1) {
				returnStr.append(properties[j]).append(";");//$NON-NLS-1$
				continue;
			}

			List<String> words = new ArrayList<String>(3);
			int begin = 0;
			int i = 0;
			for (; i < pair[0].length(); i++) {
				if (Character.isUpperCase(pair[0].charAt(i)) && i != 0) {
					words.add(pair[0].substring(begin, i).toLowerCase());
					begin = i;
				}
			}
			if (begin != i) {
				words.add(pair[0].substring(begin, i).toLowerCase());
			}
			StringBuilder sb = new StringBuilder();
			i = 0;
			for (i = 0; i < words.size(); i++) {
				if (i != 0) {
					sb.append("-");//$NON-NLS-1$
				}
				sb.append(words.get(i));
			}

			returnStr.append(sb);
			for (i = 1; i < pair.length; i++) {
				returnStr.append(":").append(pair[i]);//$NON-NLS-1$
			}

		}

		return returnStr.toString();
	}

	/**
	 * Converts CSS properties to non-hyphen format.
	 * 
	 * @param cssProperties
	 * @return string format
	 * @since 2.5.1
	 */
	public static String getStylingNonHyphenFormat(String cssProperties) {
		if (cssProperties == null) {
			return null;
		}

		StringBuilder returnStr = new StringBuilder();
		String[] properties = cssProperties.split(";"); //$NON-NLS-1$
		if (properties == null || properties.length == 0) {
			return cssProperties;
		}

		for (int j = 0; j < properties.length; j++) {
			if (j != 0) {
				returnStr.append(";"); //$NON-NLS-1$
			}
			String[] pair = properties[j].split(":");//$NON-NLS-1$
			if (pair == null || pair.length <= 1) {
				returnStr.append(properties[j]).append(";");//$NON-NLS-1$
				continue;
			}

			StringBuilder wordsStr = new StringBuilder();
			String[] words = pair[0].split("-");//$NON-NLS-1$
			for (int i = 0; i < words.length; i++) {
				if (i == 0) {
					wordsStr.append(words[i]);
				} else {
					wordsStr.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1));
				}
			}
			returnStr.append(wordsStr);
			for (int i = 1; i < pair.length; i++) {
				returnStr.append(":").append(pair[i]);//$NON-NLS-1$
			}
		}

		return returnStr.toString();
	}
}
