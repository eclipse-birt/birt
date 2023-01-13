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
package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.InheritValue;
import org.eclipse.birt.report.engine.css.engine.value.RGBColorValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This interface defines constants for CSS values.
 *
 */
public interface CSSValueConstants {

	/**
	 * 0%
	 */
	Value PERCENT_0 = new FloatValue(CSSPrimitiveValue.CSS_PERCENTAGE, 0);
	/**
	 * 50%
	 */
	Value PERCENT_50 = new FloatValue(CSSPrimitiveValue.CSS_PERCENTAGE, 50);
	/**
	 * 100%
	 */
	Value PERCENT_100 = new FloatValue(CSSPrimitiveValue.CSS_PERCENTAGE, 100);

	/**
	 * 0
	 */
	Value NUMBER_0 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 0);

	/**
	 * 100
	 */
	Value NUMBER_100 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 100);

	/**
	 * 128
	 */
	Value NUMBER_128 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 128);

	/**
	 * 165
	 */
	Value NUMBER_165 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 165);

	/**
	 * 192
	 */
	Value NUMBER_192 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 192);

	/**
	 * 200
	 */
	Value NUMBER_200 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 200);

	/**
	 * 255
	 */
	Value NUMBER_255 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 255);

	/**
	 * 300
	 */
	Value NUMBER_300 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 300);

	/**
	 * 400
	 */
	Value NUMBER_400 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 400);

	/**
	 * 500
	 */
	Value NUMBER_500 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 500);

	/**
	 * 600
	 */
	Value NUMBER_600 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 600);

	/**
	 * 700
	 */
	Value NUMBER_700 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 700);

	/**
	 * 800
	 */
	Value NUMBER_800 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 800);

	/**
	 * 900
	 */
	Value NUMBER_900 = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 900);

	/**
	 * The 'inherit' CSSPrimitiveValue.
	 */
	Value INHERIT_VALUE = InheritValue.INSTANCE;

	/**
	 * The 'auto' keyword.
	 */
	Value AUTO_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_AUTO_VALUE);

	/**
	 * The 'aqua' color
	 */
	Value AQUA_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_AQUA_VALUE);
	/**
	 * The 'blink' keyword.
	 */
	Value BLINK_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BLINK_VALUE);

	/**
	 * The 'black' color
	 */
	Value BLACK_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BLACK_VALUE);
	/**
	 * The 'block' keyword.
	 */
	Value BLOCK_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BLOCK_VALUE);

	/**
	 * The 'bold' keyword.
	 */
	Value BOLD_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BOLD_VALUE);

	/**
	 * The 'bolder' keyword.
	 */
	Value BOLDER_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BOLDER_VALUE);

	/**
	 * The 'collapse' keyword.
	 */
	Value COLLAPSE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_COLLAPSE_VALUE);

	/**
	 * The 'crosshair' keyword.
	 */
	Value CROSSHAIR_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_CROSSHAIR_VALUE);

	/**
	 * The 'cursive' keyword.
	 */
	Value CURSIVE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_CURSIVE_VALUE);

	/**
	 * The 'default' keyword.
	 */
	Value DEFAULT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_DEFAULT_VALUE);

	/**
	 * The 'e-resize' keyword.
	 */
	Value E_RESIZE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_E_RESIZE_VALUE);

	/**
	 * The 'embed' keyword.
	 */
	Value EMBED_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_EMBED_VALUE);

	/**
	 * The 'fantasy' keyword.
	 */
	Value FANTASY_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_FANTASY_VALUE);

	/**
	 * The 'fixed' keyword.
	 */
	Value FIXED_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_FIXED_VALUE);
	/**
	 * The 'hidden' keyword.
	 */
	Value HIDDEN_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_HIDDEN_VALUE);

	/**
	 * The 'inline' keyword.
	 */
	Value INLINE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_INLINE_VALUE);

	/**
	 * The 'inline-table' keyword.
	 */
	Value INLINE_TABLE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_INLINE_TABLE_VALUE);

	/**
	 * The 'italic' keyword.
	 */
	Value ITALIC_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_ITALIC_VALUE);

	/**
	 * The 'large' keyword.
	 */
	Value LARGE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_LARGE_VALUE);

	/**
	 * The 'larger' keyword.
	 */
	Value LARGER_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_LARGER_VALUE);

	/**
	 * The 'lighter' keyword.
	 */
	Value LIGHTER_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_LIGHTER_VALUE);

	/**
	 * The 'line_through' keyword.
	 */
	Value LINE_THROUGH_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_LINE_THROUGH_VALUE);

	/**
	 * The 'list-item' keyword.
	 */
	Value LIST_ITEM_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_LIST_ITEM_VALUE);

	/**
	 * The 'ltr' keyword.
	 */
	Value LTR_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_LTR_VALUE);

	/**
	 * The 'medium' keyword.
	 */
	Value MEDIUM_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_MEDIUM_VALUE);

	/**
	 * The 'monospaced' keyword.
	 */
	Value MONOSPACE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_MONOSPACE_VALUE);

	/**
	 * The 'move' keyword.
	 */
	Value MOVE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_MOVE_VALUE);

	/**
	 * The 'n-resize' keyword.
	 */
	Value N_RESIZE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_N_RESIZE_VALUE);

	/**
	 * The 'ne-resize' keyword.
	 */
	Value NE_RESIZE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_NE_RESIZE_VALUE);

	/**
	 * The 'nw-resize' keyword.
	 */
	Value NW_RESIZE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_NW_RESIZE_VALUE);

	/**
	 * The 'none' keyword.
	 */
	Value NONE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_NONE_VALUE);

	/**
	 * The 'normal' keyword.
	 */
	Value NORMAL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_NORMAL_VALUE);

	/**
	 * The 'oblique' keyword.
	 */
	Value OBLIQUE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_OBLIQUE_VALUE);

	/**
	 * The 'overline' keyword.
	 */
	Value OVERLINE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_OVERLINE_VALUE);

	/**
	 * The 'pointer' keyword.
	 */
	Value POINTER_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_POINTER_VALUE);

	/**
	 * The 'rtl' keyword.
	 */
	Value RTL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_RTL_VALUE);

	/**
	 * The 'run-in' keyword.
	 */
	Value RUN_IN_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_RUN_IN_VALUE);

	/**
	 * The 's-resize' keyword.
	 */
	Value S_RESIZE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_S_RESIZE_VALUE);

	/**
	 * The 'sans-serif' keyword.
	 */
	Value SANS_SERIF_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SANS_SERIF_VALUE);

	/**
	 * The 'scroll' keyword.
	 */
	Value SCROLL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SCROLL_VALUE);

	/**
	 * The 'se-resize' keyword.
	 */
	Value SE_RESIZE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SE_RESIZE_VALUE);

	/**
	 * The 'serif' keyword.
	 */
	Value SERIF_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SERIF_VALUE);

	/**
	 * The 'small' keyword.
	 */
	Value SMALL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SMALL_VALUE);

	/**
	 * The 'small-caps' keyword.
	 */
	Value SMALL_CAPS_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SMALL_CAPS_VALUE);

	/**
	 * The 'smaller' keyword.
	 */
	Value SMALLER_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SMALLER_VALUE);

	/**
	 * The 'sw-resize' keyword.
	 */
	Value SW_RESIZE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SW_RESIZE_VALUE);

	/**
	 * The 'table' keyword.
	 */
	Value TABLE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_TABLE_VALUE);

	/**
	 * The 'table-caption' keyword.
	 */
	Value TABLE_CAPTION_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_TABLE_CAPTION_VALUE);

	/**
	 * The 'table-cell' keyword.
	 */
	Value TABLE_CELL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_TABLE_CELL_VALUE);

	/**
	 * The 'table-column' keyword.
	 */
	Value TABLE_COLUMN_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_TABLE_COLUMN_VALUE);

	/**
	 * The 'table-column-group' keyword.
	 */
	Value TABLE_COLUMN_GROUP_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT,
			CSSConstants.CSS_TABLE_COLUMN_GROUP_VALUE);

	/**
	 * The 'table-footer-group' keyword.
	 */
	Value TABLE_FOOTER_GROUP_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT,
			CSSConstants.CSS_TABLE_FOOTER_GROUP_VALUE);

	/**
	 * The 'table-header-group' keyword.
	 */
	Value TABLE_HEADER_GROUP_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT,
			CSSConstants.CSS_TABLE_HEADER_GROUP_VALUE);

	/**
	 * The 'table-row' keyword.
	 */
	Value TABLE_ROW_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_TABLE_ROW_VALUE);

	/**
	 * The 'table-row-group' keyword.
	 */
	Value TABLE_ROW_GROUP_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_TABLE_ROW_GROUP_VALUE);

	/**
	 * The 'text' keyword.
	 */
	Value TEXT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_TEXT_VALUE);

	/**
	 * The 'underline' keyword.
	 */
	Value UNDERLINE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_UNDERLINE_VALUE);

	/**
	 * The 'visible' keyword.
	 */
	Value VISIBLE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_VISIBLE_VALUE);

	/**
	 * The 'w-resize' keyword.
	 */
	Value W_RESIZE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_W_RESIZE_VALUE);

	/**
	 * The 'wait' keyword.
	 */
	Value WAIT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_WAIT_VALUE);

	/**
	 * The 'x-large' keyword.
	 */
	Value X_LARGE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_X_LARGE_VALUE);

	/**
	 * The 'x-small' keyword.
	 */
	Value X_SMALL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_X_SMALL_VALUE);

	/**
	 * The 'xx-large' keyword.
	 */
	Value XX_LARGE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_XX_LARGE_VALUE);

	/**
	 * The 'xx-small' keyword.
	 */
	Value XX_SMALL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_XX_SMALL_VALUE);

	/**
	 * The 'blue' color name.
	 */
	Value BLUE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BLUE_VALUE);

	/**
	 * The 'fuchsia' color name.
	 */
	Value FUCHSIA_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_FUCHSIA_VALUE);

	/**
	 * The 'gray' color name.
	 */
	Value GRAY_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_GRAY_VALUE);

	/**
	 * The 'green' color name.
	 */
	Value GREEN_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_GREEN_VALUE);

	/**
	 * The 'lime' color name.
	 */
	Value LIME_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_LIME_VALUE);

	/**
	 * The 'maroon' color name.
	 */
	Value MAROON_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_MAROON_VALUE);

	/**
	 * The 'navy' color name.
	 */
	Value NAVY_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_NAVY_VALUE);

	/**
	 * The 'olive' color name.
	 */
	Value OLIVE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_OLIVE_VALUE);

	/**
	 * The 'orange' color name.
	 */
	Value ORANGE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_ORANGE_VALUE);

	/**
	 * The 'purple' color name.
	 */
	Value PURPLE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_PURPLE_VALUE);

	/**
	 * The 'red' color name.
	 */
	Value RED_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_RED_VALUE);

	/**
	 * The 'silver' color name.
	 */
	Value SILVER_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SILVER_VALUE);

	/**
	 * The 'teal' color name.
	 */
	Value TEAL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_TEAL_VALUE);

	/**
	 * The 'white' color name.
	 */
	Value WHITE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_WHITE_VALUE);

	/**
	 * The 'yellow' color name.
	 */
	Value YELLOW_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_YELLOW_VALUE);

	/**
	 * The 'ACTIVEBORDER' color name.
	 */
	Value ACTIVEBORDER_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_ACTIVEBORDER_VALUE);

	/**
	 * The 'ACTIVECAPTION' color name.
	 */
	Value ACTIVECAPTION_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_ACTIVECAPTION_VALUE);

	/**
	 * The 'APPWORKSPACE' color name.
	 */
	Value APPWORKSPACE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_APPWORKSPACE_VALUE);

	/**
	 * The 'BACKGROUND' color name.
	 */
	Value BACKGROUND_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BACKGROUND_VALUE);

	/**
	 * The 'BUTTONFACE' color name.
	 */
	Value BUTTONFACE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BUTTONFACE_VALUE);

	/**
	 * The 'BUTTONHIGHLIGHT' color name.
	 */
	Value BUTTONHIGHLIGHT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BUTTONHIGHLIGHT_VALUE);

	/**
	 * The 'BUTTONSHADOW' color name.
	 */
	Value BUTTONSHADOW_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BUTTONSHADOW_VALUE);

	/**
	 * The 'BUTTONTEXT' color name.
	 */
	Value BUTTONTEXT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BUTTONTEXT_VALUE);

	/**
	 * The 'CAPTIONTEXT' color name.
	 */
	Value CAPTIONTEXT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_CAPTIONTEXT_VALUE);

	/**
	 * The 'GRAYTEXT' color name.
	 */
	Value GRAYTEXT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_GRAYTEXT_VALUE);

	/**
	 * The 'HIGHLIGHT' color name.
	 */
	Value HIGHLIGHT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_HIGHLIGHT_VALUE);

	/**
	 * The 'HIGHLIGHTTEXT' color name.
	 */
	Value HIGHLIGHTTEXT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_HIGHLIGHTTEXT_VALUE);

	/**
	 * The 'INACTIVEBORDER' color name.
	 */
	Value INACTIVEBORDER_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_INACTIVEBORDER_VALUE);

	/**
	 * The 'INACTIVECAPTION' color name.
	 */
	Value INACTIVECAPTION_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_INACTIVECAPTION_VALUE);

	/**
	 * The 'INACTIVECAPTIONTEXT' color name.
	 */
	Value INACTIVECAPTIONTEXT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT,
			CSSConstants.CSS_INACTIVECAPTIONTEXT_VALUE);

	/**
	 * The 'INFOBACKGROUND' color name.
	 */
	Value INFOBACKGROUND_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_INFOBACKGROUND_VALUE);

	/**
	 * The 'INFOTEXT' color name.
	 */
	Value INFOTEXT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_INFOTEXT_VALUE);

	/**
	 * The 'MENU' color name.
	 */
	Value MENU_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_MENU_VALUE);

	/**
	 * The 'MENUTEXT' color name.
	 */
	Value MENUTEXT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_MENUTEXT_VALUE);

	/**
	 * The 'SCROLLBAR' color name.
	 */
	Value SCROLLBAR_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SCROLLBAR_VALUE);

	/**
	 * The 'THREEDDARKSHADOW' color name.
	 */
	Value THREEDDARKSHADOW_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT,
			CSSConstants.CSS_THREEDDARKSHADOW_VALUE);

	/**
	 * The 'THREEDFACE' color name.
	 */
	Value THREEDFACE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_THREEDFACE_VALUE);

	/**
	 * The 'THREEDHIGHLIGHT' color name.
	 */
	Value THREEDHIGHLIGHT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_THREEDHIGHLIGHT_VALUE);

	/**
	 * The 'THREEDLIGHTSHADOW' color name.
	 */
	Value THREEDLIGHTSHADOW_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT,
			CSSConstants.CSS_THREEDLIGHTSHADOW_VALUE);

	/**
	 * The 'THREEDSHADOW' color name.
	 */
	Value THREEDSHADOW_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_THREEDSHADOW_VALUE);

	/**
	 * The 'WINDOW' color name.
	 */
	Value WINDOW_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_WINDOW_VALUE);

	/**
	 * The 'WINDOWFRAME' color name.
	 */
	Value WINDOWFRAME_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_WINDOWFRAME_VALUE);

	/**
	 * The 'WINDOWTEXT' color name.
	 */
	Value WINDOWTEXT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_WINDOWTEXT_VALUE);

	/**
	 * The 'black' RGB color.
	 */
	Value BLACK_RGB_VALUE = new RGBColorValue(NUMBER_0, NUMBER_0, NUMBER_0);

	/**
	 * The 'silver' RGB color.
	 */
	Value SILVER_RGB_VALUE = new RGBColorValue(NUMBER_192, NUMBER_192, NUMBER_192);

	/**
	 * The 'gray' RGB color.
	 */
	Value GRAY_RGB_VALUE = new RGBColorValue(NUMBER_128, NUMBER_128, NUMBER_128);

	/**
	 * The 'white' RGB color.
	 */
	Value WHITE_RGB_VALUE = new RGBColorValue(NUMBER_255, NUMBER_255, NUMBER_255);

	/**
	 * The 'maroon' RGB color.
	 */
	Value MAROON_RGB_VALUE = new RGBColorValue(NUMBER_128, NUMBER_0, NUMBER_0);

	/**
	 * The 'red' RGB color.
	 */
	Value RED_RGB_VALUE = new RGBColorValue(NUMBER_255, NUMBER_0, NUMBER_0);

	/**
	 * The 'purple' RGB color.
	 */
	Value PURPLE_RGB_VALUE = new RGBColorValue(NUMBER_128, NUMBER_0, NUMBER_128);

	/**
	 * The 'fuchsia' RGB color.
	 */
	Value FUCHSIA_RGB_VALUE = new RGBColorValue(NUMBER_255, NUMBER_0, NUMBER_255);

	/**
	 * The 'green' RGB color.
	 */
	Value GREEN_RGB_VALUE = new RGBColorValue(NUMBER_0, NUMBER_128, NUMBER_0);

	/**
	 * The 'lime' RGB color.
	 */
	Value LIME_RGB_VALUE = new RGBColorValue(NUMBER_0, NUMBER_255, NUMBER_0);

	/**
	 * The 'olive' RGB color.
	 */
	Value OLIVE_RGB_VALUE = new RGBColorValue(NUMBER_128, NUMBER_128, NUMBER_0);

	/**
	 * The 'orange' RGB color.
	 */
	Value ORANGE_RGB_VALUE = new RGBColorValue(NUMBER_255, NUMBER_165, NUMBER_0);

	/**
	 * The 'yellow' RGB color.
	 */
	Value YELLOW_RGB_VALUE = new RGBColorValue(NUMBER_255, NUMBER_255, NUMBER_0);

	/**
	 * The 'navy' RGB color.
	 */
	Value NAVY_RGB_VALUE = new RGBColorValue(NUMBER_0, NUMBER_0, NUMBER_128);

	/**
	 * The 'blue' RGB color.
	 */
	Value BLUE_RGB_VALUE = new RGBColorValue(NUMBER_0, NUMBER_0, NUMBER_255);

	/**
	 * The 'teal' RGB color.
	 */
	Value TEAL_RGB_VALUE = new RGBColorValue(NUMBER_0, NUMBER_128, NUMBER_128);

	/**
	 * The 'aqua' RGB color.
	 */
	Value AQUA_RGB_VALUE = new RGBColorValue(NUMBER_0, NUMBER_255, NUMBER_255);

	/**
	 * the "left" value
	 */
	Value LEFT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_LEFT_VALUE);

	/**
	 * the "center" value
	 */
	Value CENTER_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_CENTER_VALUE);

	/**
	 * the "right" value
	 */
	Value RIGHT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_RIGHT_VALUE);

	/**
	 * the "justify" value
	 */
	Value JUSTIFY_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_JUSTIFY_VALUE);

	Value CONTAIN_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_CONTAIN_VALUE);

	Value COVER_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_COVER_VALUE);

	/**
	 * the "baseline" value
	 */
	Value BASELINE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BASELINE_VALUE);
	/**
	 * the "sub" value
	 */
	Value SUB_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SUB_VALUE);
	/**
	 * the "super" value
	 */
	Value SUPER_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SUPER_VALUE);
	/**
	 * the "top " value
	 */
	Value TOP_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_TOP_VALUE);
	/**
	 * the "text-top" value
	 */
	Value TEXT_TOP_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_TEXT_TOP_VALUE);
	/**
	 * the "middle" value
	 */
	Value MIDDLE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_MIDDLE_VALUE);
	/**
	 * the "bottom" value
	 */
	Value BOTTOM_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_BOTTOM_VALUE);
	/**
	 * the "text-bottom" value
	 */
	Value TEXT_BOTTOM_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_TEXT_BOTTOM_VALUE);

	/**
	 * the "repeat" value
	 */
	Value REPEAT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_REPEAT_VALUE);
	/**
	 * the "repeat-x" value
	 */
	Value REPEAT_X_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_REPEAT_X_VALUE);
	/**
	 * the "repeat-y" value
	 */
	Value REPEAT_Y_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_REPEAT_Y_VALUE);
	/**
	 * the "no-repeat" value
	 */
	Value NO_REPEAT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_NO_REPEAT_VALUE);

	/**
	 * the "dotted" value
	 */
	Value DOTTED_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_DOTTED_VALUE);
	/**
	 * the "solid" value
	 */
	Value DASHED_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_DASHED_VALUE);
	/**
	 * the "solid" value
	 */
	Value SOLID_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SOLID_VALUE);
	/**
	 * the "double" value
	 */
	Value DOUBLE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_DOUBLE_VALUE);
	/**
	 * the "groove" value
	 */
	Value GROOVE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_GROOVE_VALUE);
	/**
	 * the "ridge" value
	 */
	Value RIDGE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_RIDGE_VALUE);
	/**
	 * the "inset" value
	 */
	Value INSET_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_INSET_VALUE);
	/**
	 * the "outset" value
	 */
	Value OUTSET_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_OUTSET_VALUE);

	/**
	 * the "thin" value
	 */
	Value THIN_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_THIN_VALUE);

	/**
	 * the "thick" value
	 */
	Value THICK_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_THICK_VALUE);

	/**
	 * the "transparent" value
	 */
	Value TRANSPARENT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_TRANSPARENT_VALUE);

	/**
	 * the "inline-block" value
	 */
	Value INLINE_BLOCK_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_INLINE_BLOCK_VALUE);

	/**
	 * the "capitalize" value
	 */
	Value CAPITALIZE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_CAPITALIZE_VALUE);
	/**
	 * the "uppercase" value
	 */
	Value UPPERCASE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_UPPERCASE_VALUE);
	/**
	 * the "lowercase" value
	 */
	Value LOWERCASE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_LOWERCASE_VALUE);

	/**
	 * the "pre" value
	 */
	Value PRE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_PRE_VALUE);
	/**
	 * the "nowrap" value
	 */
	Value NOWRAP_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_NOWRAP_VALUE);
	/**
	 * the "pre-wrap" value
	 */
	Value PRE_WRAP_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_PRE_WRAP_VALUE);
	/**
	 * the "pre-line" value
	 */
	Value PRE_LINE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_PRE_LINE_VALUE);

	/**
	 * the "avoid" value
	 */
	Value AVOID_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_AVOID_VALUE);

	/**
	 * the "always" value
	 */
	Value ALWAYS_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_ALWAYS_VALUE);

	/**
	 * the empty string value
	 */
	Value NULL_STRING_VALUE = new StringValue(CSSPrimitiveValue.CSS_STRING, null);

	/**
	 * the "auto" value for overflow
	 */
	Value OVERFLOW_AUTO_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_OVERFLOW_AUTO_VALUE);
	/**
	 * the "visible" value for overflow
	 */
	Value OVERFLOW_VISIBLE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT,
			CSSConstants.CSS_OVERFLOW_VISIBLE_VALUE);
	/**
	 * the "scroll" value for overflow
	 */
	Value OVERFLOW_SCROLL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_OVERFLOW_SCROLL_VALUE);
	/**
	 * the "hidden" value for overflow
	 */
	Value OVERFLOW_HIDDEN_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_OVERFLOW_HIDDEN_VALUE);

	/**
	 * The 'url' keyword.
	 */
	Value URL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_URL_VALUE);

}
