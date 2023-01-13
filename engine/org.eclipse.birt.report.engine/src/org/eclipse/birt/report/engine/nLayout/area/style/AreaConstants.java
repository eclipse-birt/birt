/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
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
package org.eclipse.birt.report.engine.nLayout.area.style;

import java.util.HashMap;

import org.eclipse.birt.report.engine.content.IStyle;
import org.w3c.dom.css.CSSValue;

public abstract class AreaConstants {
	public final static int NO_REPEAT = 0;
	public final static int REPEAT_X = 1;
	public final static int REPEAT_Y = 2;
	public final static int REPEAT = 3;
	public final static String URL = "url";
	public final static String EMBED = "embed";

	public static HashMap<CSSValue, Integer> repeatMap = new HashMap<>();
	public static HashMap<CSSValue, String> bgiSourceTypeMap = new HashMap<>();

	static {
		repeatMap.put(IStyle.NO_REPEAT_VALUE, NO_REPEAT);
		repeatMap.put(IStyle.REPEAT_X_VALUE, REPEAT_X);
		repeatMap.put(IStyle.REPEAT_Y_VALUE, REPEAT_Y);
		repeatMap.put(IStyle.REPEAT_VALUE, REPEAT);
	}

	static {
		bgiSourceTypeMap.put(IStyle.URL_VALUE, URL);
		bgiSourceTypeMap.put(IStyle.EMBED_VALUE, EMBED);
	}

	/**
	 * the "dotted" value
	 */
	public final static int BORDER_STYLE_DOTTED = 20;
	/**
	 * the "solid" value
	 */
	public static final int BORDER_STYLE_SOLID = 21;
	/**
	 * the "dashed" value
	 */
	public static final int BORDER_STYLE_DASHED = 22;
	/**
	 * the "double" value
	 */
	public static final int BORDER_STYLE_DOUBLE = 23;
	/**
	 * the "groove" value
	 */
	public static final int BORDER_STYLE_GROOVE = 24;
	/**
	 * the "ridge" value
	 */
	public static final int BORDER_STYLE_RIDGE = 25;
	/**
	 * the "inset" value
	 */
	public static final int BORDER_STYLE_INSET = 26;
	/**
	 * the "outset" value
	 */
	public static final int BORDER_STYLE_OUTSET = 27;

	/**
	 * the "none" value
	 */
	public final static int BORDER_STYLE_NONE = 28;

	/**
	 * the "hidden" value
	 */
	public final static int BORDER_STYLE_HIDDEN = 29;

	public static HashMap<CSSValue, Integer> valueStyleMap = new HashMap<>();
	static {
		valueStyleMap.put(IStyle.DOTTED_VALUE, BORDER_STYLE_DOTTED);
		valueStyleMap.put(IStyle.SOLID_VALUE, BORDER_STYLE_SOLID);
		valueStyleMap.put(IStyle.DASHED_VALUE, BORDER_STYLE_DASHED);
		valueStyleMap.put(IStyle.DOUBLE_VALUE, BORDER_STYLE_DOUBLE);
		valueStyleMap.put(IStyle.GROOVE_VALUE, BORDER_STYLE_GROOVE);
		valueStyleMap.put(IStyle.RIDGE_VALUE, BORDER_STYLE_RIDGE);
		valueStyleMap.put(IStyle.INSET_VALUE, BORDER_STYLE_INSET);
		valueStyleMap.put(IStyle.OUTSET_VALUE, BORDER_STYLE_OUTSET);
		valueStyleMap.put(IStyle.NONE_VALUE, BORDER_STYLE_NONE);
		valueStyleMap.put(IStyle.HIDDEN_VALUE, BORDER_STYLE_HIDDEN);

	}

	public static HashMap<String, Integer> stringStyleMap = new HashMap<>();
	static {
		stringStyleMap.put(IStyle.CSS_DOTTED_VALUE, BORDER_STYLE_DOTTED);
		stringStyleMap.put(IStyle.CSS_SOLID_VALUE, BORDER_STYLE_SOLID);
		stringStyleMap.put(IStyle.CSS_DASHED_VALUE, BORDER_STYLE_DASHED);
		stringStyleMap.put(IStyle.CSS_DOUBLE_VALUE, BORDER_STYLE_DOUBLE);
		stringStyleMap.put(IStyle.CSS_GROOVE_VALUE, BORDER_STYLE_GROOVE);
		stringStyleMap.put(IStyle.CSS_RIDGE_VALUE, BORDER_STYLE_RIDGE);
		stringStyleMap.put(IStyle.CSS_INSET_VALUE, BORDER_STYLE_INSET);
		stringStyleMap.put(IStyle.CSS_OUTSET_VALUE, BORDER_STYLE_OUTSET);
		stringStyleMap.put(IStyle.CSS_NONE_VALUE, BORDER_STYLE_NONE);
		stringStyleMap.put(IStyle.CSS_HIDDEN_VALUE, BORDER_STYLE_HIDDEN);

	}

	public final static int DIRECTION_LTR = 40;
	public final static int DIRECTION_RTL = 41;

}
