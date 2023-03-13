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

import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.w3c.dom.css.CSSValue;

/**
 * Definition of area constants
 *
 * @since 3.3
 *
 */
public abstract class AreaConstants {

	/**
	 * repeat background image "none"
	 */
	public final static int NO_REPEAT = 0;

	/**
	 * repeat background image "horizontal"
	 */
	public final static int REPEAT_X = 1;

	/**
	 * repeat background image "vertical"
	 */
	public final static int REPEAT_Y = 2;

	/**
	 * repeat background image "horizontal" & "vertical"
	 */
	public final static int REPEAT = 3;

	/**
	 * source type of background images "URL"
	 */
	public final static String BGI_SRC_TYPE_URL = "url";

	/**
	 * source type of background images "EMBED"
	 */
	public final static String BGI_SRC_TYPE_EMBED = "embed";

	/**
	 * mapping list of repeat values
	 */
	public static HashMap<CSSValue, Integer> repeatMap = new HashMap<>();

	/**
	 * mapping list of bgi source type values
	 */
	public static HashMap<CSSValue, String> bgiSourceTypeMap = new HashMap<>();

	static {
		repeatMap.put(CSSValueConstants.NO_REPEAT_VALUE, NO_REPEAT);
		repeatMap.put(CSSValueConstants.REPEAT_X_VALUE, REPEAT_X);
		repeatMap.put(CSSValueConstants.REPEAT_Y_VALUE, REPEAT_Y);
		repeatMap.put(CSSValueConstants.REPEAT_VALUE, REPEAT);
	}

	static {
		bgiSourceTypeMap.put(CSSValueConstants.URL_VALUE, BGI_SRC_TYPE_URL);
		bgiSourceTypeMap.put(CSSValueConstants.EMBED_VALUE, BGI_SRC_TYPE_EMBED);
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

	/**
	 * Mapping from CCS value to area constants
	 */
	public static HashMap<CSSValue, Integer> valueStyleMap = new HashMap<>();
	static {
		valueStyleMap.put(CSSValueConstants.DOTTED_VALUE, AreaConstants.BORDER_STYLE_DOTTED);
		valueStyleMap.put(CSSValueConstants.SOLID_VALUE, AreaConstants.BORDER_STYLE_SOLID);
		valueStyleMap.put(CSSValueConstants.DASHED_VALUE, AreaConstants.BORDER_STYLE_DASHED);
		valueStyleMap.put(CSSValueConstants.DOUBLE_VALUE, AreaConstants.BORDER_STYLE_DOUBLE);
		valueStyleMap.put(CSSValueConstants.GROOVE_VALUE, AreaConstants.BORDER_STYLE_GROOVE);
		valueStyleMap.put(CSSValueConstants.RIDGE_VALUE, AreaConstants.BORDER_STYLE_RIDGE);
		valueStyleMap.put(CSSValueConstants.INSET_VALUE, AreaConstants.BORDER_STYLE_INSET);
		valueStyleMap.put(CSSValueConstants.OUTSET_VALUE, AreaConstants.BORDER_STYLE_OUTSET);
		valueStyleMap.put(CSSValueConstants.NONE_VALUE, AreaConstants.BORDER_STYLE_NONE);
		valueStyleMap.put(CSSValueConstants.HIDDEN_VALUE, AreaConstants.BORDER_STYLE_HIDDEN);

	}

	/**
	 * Mapping from CCS style to area constants
	 */
	public static HashMap<String, Integer> stringStyleMap = new HashMap<>();
	static {
		stringStyleMap.put(CSSConstants.CSS_DOTTED_VALUE, AreaConstants.BORDER_STYLE_DOTTED);
		stringStyleMap.put(CSSConstants.CSS_SOLID_VALUE, AreaConstants.BORDER_STYLE_SOLID);
		stringStyleMap.put(CSSConstants.CSS_DASHED_VALUE, AreaConstants.BORDER_STYLE_DASHED);
		stringStyleMap.put(CSSConstants.CSS_DOUBLE_VALUE, AreaConstants.BORDER_STYLE_DOUBLE);
		stringStyleMap.put(CSSConstants.CSS_GROOVE_VALUE, AreaConstants.BORDER_STYLE_GROOVE);
		stringStyleMap.put(CSSConstants.CSS_RIDGE_VALUE, AreaConstants.BORDER_STYLE_RIDGE);
		stringStyleMap.put(CSSConstants.CSS_INSET_VALUE, AreaConstants.BORDER_STYLE_INSET);
		stringStyleMap.put(CSSConstants.CSS_OUTSET_VALUE, AreaConstants.BORDER_STYLE_OUTSET);
		stringStyleMap.put(CSSConstants.CSS_NONE_VALUE, AreaConstants.BORDER_STYLE_NONE);
		stringStyleMap.put(CSSConstants.CSS_HIDDEN_VALUE, AreaConstants.BORDER_STYLE_HIDDEN);
	}

	/**
	 * The value of direction left-to-right "LTR"
	 */
	public final static int DIRECTION_LTR = 40;

	/**
	 * The value of direction right-to-left "RTL"
	 */
	public final static int DIRECTION_RTL = 41;

}
