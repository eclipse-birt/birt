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

package org.eclipse.birt.report.engine.css.engine;

import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.css.engine.value.birt.BackgroundPositionXManager;
import org.eclipse.birt.report.engine.css.engine.value.birt.BackgroundPositionYManager;
import org.eclipse.birt.report.engine.css.engine.value.birt.BooleanManager;
import org.eclipse.birt.report.engine.css.engine.value.birt.DataFormatManager;
import org.eclipse.birt.report.engine.css.engine.value.birt.VisibleFormatManager;
import org.eclipse.birt.report.engine.css.engine.value.css.BackgroundAttachmentManager;
import org.eclipse.birt.report.engine.css.engine.value.css.BackgroundRepeatManager;
import org.eclipse.birt.report.engine.css.engine.value.css.BackgroundSizeManager;
import org.eclipse.birt.report.engine.css.engine.value.css.BorderColorManager;
import org.eclipse.birt.report.engine.css.engine.value.css.BorderStyleManager;
import org.eclipse.birt.report.engine.css.engine.value.css.BorderWidthManager;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.ColorManager;
import org.eclipse.birt.report.engine.css.engine.value.css.DirectionManager;
import org.eclipse.birt.report.engine.css.engine.value.css.DisplayManager;
import org.eclipse.birt.report.engine.css.engine.value.css.FontFamilyManager;
import org.eclipse.birt.report.engine.css.engine.value.css.FontSizeManager;
import org.eclipse.birt.report.engine.css.engine.value.css.FontStyleManager;
import org.eclipse.birt.report.engine.css.engine.value.css.FontVariantManager;
import org.eclipse.birt.report.engine.css.engine.value.css.FontWeightManager;
import org.eclipse.birt.report.engine.css.engine.value.css.IntegerManager;
import org.eclipse.birt.report.engine.css.engine.value.css.LengthManager;
import org.eclipse.birt.report.engine.css.engine.value.css.LineHeightManager;
import org.eclipse.birt.report.engine.css.engine.value.css.MarginManager;
import org.eclipse.birt.report.engine.css.engine.value.css.OverflowManager;
import org.eclipse.birt.report.engine.css.engine.value.css.PageBreakBeforeAfterManager;
import org.eclipse.birt.report.engine.css.engine.value.css.PageBreakInsideManager;
import org.eclipse.birt.report.engine.css.engine.value.css.SpacingManager;
import org.eclipse.birt.report.engine.css.engine.value.css.StringManager;
import org.eclipse.birt.report.engine.css.engine.value.css.TextAlignManager;
import org.eclipse.birt.report.engine.css.engine.value.css.TextLineThroughManager;
import org.eclipse.birt.report.engine.css.engine.value.css.TextOverlineManager;
import org.eclipse.birt.report.engine.css.engine.value.css.TextTransformManager;
import org.eclipse.birt.report.engine.css.engine.value.css.TextUnderlineManager;
import org.eclipse.birt.report.engine.css.engine.value.css.URIManager;
import org.eclipse.birt.report.engine.css.engine.value.css.VerticalAlignManager;
import org.eclipse.birt.report.engine.css.engine.value.css.WhiteSpaceManager;

/**
 * provide engine a utilitis to parser the styles.
 * 
 * 
 */
public class BIRTPropertyManagerFactory implements PropertyManagerFactory {

	ValueManager[] vms;

	public BIRTPropertyManagerFactory() {
		vms = new ValueManager[PerfectHash.TOTAL_KEYWORDS];

		// bidi_hcg: Leave this property blank to respect bidirectionality.
		vms[StyleConstants.STYLE_TEXT_ALIGN] = new TextAlignManager(CSSConstants.CSS_TEXT_ALIGN_PROPERTY,
				CSSValueConstants.NULL_STRING_VALUE);
		// .LEFT_VALUE );
		vms[StyleConstants.STYLE_TEXT_INDENT] = new LengthManager(CSSConstants.CSS_TEXT_INDENT_PROPERTY, true,
				CSSValueConstants.NUMBER_0);
		vms[StyleConstants.STYLE_NUMBER_ALIGN] = new TextAlignManager(BIRTConstants.BIRT_NUMBER_ALIGN_PROPERTY,
				CSSValueConstants.NONE_VALUE);
		/*
		 * vms[StyleConstants.STYLE_NUMBER_FORMAT] = new StringManager(
		 * BIRTConstants.BIRT_NUMBER_FORMAT_PROPERTY, true,
		 * CSSValueConstants.NULL_STRING_VALUE );
		 * vms[StyleConstants.STYLE_STRING_FORMAT] = new StringManager(
		 * BIRTConstants.BIRT_STRING_FORMAT_PROPERTY, true,
		 * CSSValueConstants.NULL_STRING_VALUE ); vms[StyleConstants.STYLE_DATE_FORMAT]
		 * = new StringManager( BIRTConstants.BIRT_DATE_TIME_FORMAT_PROPERTY, true,
		 * CSSValueConstants.NULL_STRING_VALUE );
		 * vms[StyleConstants.STYLE_SQL_DATE_FORMAT] = new StringManager(
		 * BIRTConstants.BIRT_DATE_FORMAT_PROPERTY, true,
		 * CSSValueConstants.NULL_STRING_VALUE );
		 * vms[StyleConstants.STYLE_SQL_TIME_FORMAT] = new StringManager(
		 * BIRTConstants.BIRT_TIME_FORMAT_PROPERTY, true,
		 * CSSValueConstants.NULL_STRING_VALUE );
		 */

		vms[StyleConstants.STYLE_VERTICAL_ALIGN] = new VerticalAlignManager();

		vms[StyleConstants.STYLE_LINE_HEIGHT] = new LineHeightManager();
		vms[StyleConstants.STYLE_BACKGROUND_REPEAT] = new BackgroundRepeatManager();
		vms[StyleConstants.STYLE_BACKGROUND_ATTACHMENT] = new BackgroundAttachmentManager();
		vms[StyleConstants.STYLE_CAN_SHRINK] = new BooleanManager(BIRTConstants.BIRT_CAN_SHRINK_PROPERTY, true, false);
		vms[StyleConstants.STYLE_TEXT_OVERLINE] = new TextOverlineManager();
		vms[StyleConstants.STYLE_TEXT_UNDERLINE] = new TextUnderlineManager();
		vms[StyleConstants.STYLE_TEXT_LINETHROUGH] = new TextLineThroughManager();
		vms[StyleConstants.STYLE_BACKGROUND_IMAGE] = new URIManager(CSSConstants.CSS_BACKGROUND_IMAGE_PROPERTY, false,
				CSSValueConstants.NONE_VALUE);
		vms[StyleConstants.STYLE_BORDER_TOP_STYLE] = new BorderStyleManager(CSSConstants.CSS_BORDER_TOP_STYLE_PROPERTY);
		vms[StyleConstants.STYLE_BORDER_LEFT_STYLE] = new BorderStyleManager(
				CSSConstants.CSS_BORDER_LEFT_STYLE_PROPERTY);
		vms[StyleConstants.STYLE_BORDER_RIGHT_STYLE] = new BorderStyleManager(
				CSSConstants.CSS_BORDER_RIGHT_STYLE_PROPERTY);
		vms[StyleConstants.STYLE_BORDER_BOTTOM_STYLE] = new BorderStyleManager(
				CSSConstants.CSS_BORDER_BOTTOM_STYLE_PROPERTY);
		vms[StyleConstants.STYLE_COLOR] = new ColorManager(CSSConstants.CSS_COLOR_PROPERTY, true,
				CSSValueConstants.BLACK_RGB_VALUE);

		vms[StyleConstants.STYLE_BORDER_TOP_WIDTH] = new BorderWidthManager(CSSConstants.CSS_BORDER_TOP_WIDTH_PROPERTY);
		vms[StyleConstants.STYLE_BORDER_LEFT_WIDTH] = new BorderWidthManager(
				CSSConstants.CSS_BORDER_LEFT_WIDTH_PROPERTY);
		vms[StyleConstants.STYLE_BORDER_RIGHT_WIDTH] = new BorderWidthManager(
				CSSConstants.CSS_BORDER_RIGHT_WIDTH_PROPERTY);
		vms[StyleConstants.STYLE_BORDER_BOTTOM_WIDTH] = new BorderWidthManager(
				CSSConstants.CSS_BORDER_BOTTOM_WIDTH_PROPERTY);
		vms[StyleConstants.STYLE_BACKGROUND_COLOR] = new ColorManager(CSSConstants.CSS_BACKGROUND_COLOR_PROPERTY, false,
				CSSValueConstants.TRANSPARENT_VALUE);
		vms[StyleConstants.STYLE_BORDER_TOP_COLOR] = new BorderColorManager(CSSConstants.CSS_BORDER_TOP_COLOR_PROPERTY);
		vms[StyleConstants.STYLE_BORDER_LEFT_COLOR] = new BorderColorManager(
				CSSConstants.CSS_BORDER_LEFT_COLOR_PROPERTY);
		vms[StyleConstants.STYLE_BORDER_RIGHT_COLOR] = new BorderColorManager(
				CSSConstants.CSS_BORDER_RIGHT_COLOR_PROPERTY);
		vms[StyleConstants.STYLE_BORDER_BOTTOM_COLOR] = new BorderColorManager(
				CSSConstants.CSS_BORDER_BOTTOM_COLOR_PROPERTY);
		vms[StyleConstants.STYLE_LETTER_SPACING] = new SpacingManager(CSSConstants.CSS_LETTER_SPACING_PROPERTY);
		vms[StyleConstants.STYLE_FONT_WEIGHT] = new FontWeightManager();
		vms[StyleConstants.STYLE_FONT_VARIANT] = new FontVariantManager();
		vms[StyleConstants.STYLE_MARGIN_LEFT] = new MarginManager(CSSConstants.CSS_MARGIN_LEFT_PROPERTY);
		vms[StyleConstants.STYLE_MARGIN_RIGHT] = new MarginManager(CSSConstants.CSS_MARGIN_RIGHT_PROPERTY);
		vms[StyleConstants.STYLE_DISPLAY] = new DisplayManager();
		vms[StyleConstants.STYLE_TEXT_TRANSFORM] = new TextTransformManager();

		vms[StyleConstants.STYLE_BACKGROUND_POSITION_Y] = new BackgroundPositionYManager();
		vms[StyleConstants.STYLE_PADDING_LEFT] = new LengthManager(CSSConstants.CSS_PADDING_LEFT_PROPERTY, false,
				CSSValueConstants.NUMBER_0);
		vms[StyleConstants.STYLE_PADDING_RIGHT] = new LengthManager(CSSConstants.CSS_PADDING_RIGHT_PROPERTY, false,
				CSSValueConstants.NUMBER_0);
		vms[StyleConstants.STYLE_FONT_SIZE] = new FontSizeManager();
		vms[StyleConstants.STYLE_FONT_STYLE] = new FontStyleManager();
		vms[StyleConstants.STYLE_WHITE_SPACE] = new WhiteSpaceManager();
		vms[StyleConstants.STYLE_ORPHANS] = new IntegerManager(CSSConstants.CSS_ORPHANS_PROPERTY, true, 2);
		// TODO: check the masterpage default value
		vms[StyleConstants.STYLE_MASTER_PAGE] = new StringManager(BIRTConstants.BIRT_MASTER_PAGE_PROPERTY, false, null);
		vms[StyleConstants.STYLE_WORD_SPACING] = new SpacingManager(CSSConstants.CSS_WORD_SPACING_PROPERTY);
		vms[StyleConstants.STYLE_BACKGROUND_POSITION_X] = new BackgroundPositionXManager();
		vms[StyleConstants.STYLE_PAGE_BREAK_BEFORE] = new PageBreakBeforeAfterManager(
				CSSConstants.CSS_PAGE_BREAK_BEFORE_PROPERTY);
		vms[StyleConstants.STYLE_PAGE_BREAK_INSIDE] = new PageBreakInsideManager();
		vms[StyleConstants.STYLE_SHOW_IF_BLANK] = new BooleanManager(BIRTConstants.BIRT_SHOW_IF_BLANK_PROPERTY, true,
				false);
		vms[StyleConstants.STYLE_FONT_FAMILY] = new FontFamilyManager();
		vms[StyleConstants.STYLE_PAGE_BREAK_AFTER] = new PageBreakBeforeAfterManager(
				CSSConstants.CSS_PAGE_BREAK_AFTER_PROPERTY);
		vms[StyleConstants.STYLE_MARGIN_BOTTOM] = new MarginManager(CSSConstants.CSS_MARGIN_BOTTOM_PROPERTY);
		vms[StyleConstants.STYLE_MARGIN_TOP] = new MarginManager(CSSConstants.CSS_MARGIN_TOP_PROPERTY);
		vms[StyleConstants.STYLE_WIDOWS] = new IntegerManager(CSSConstants.CSS_WIDOWS_PROPERTY, true, 2);
		vms[StyleConstants.STYLE_PADDING_BOTTOM] = new LengthManager(CSSConstants.CSS_PADDING_BOTTOM_PROPERTY, false,
				CSSValueConstants.NUMBER_0);
		vms[StyleConstants.STYLE_PADDING_TOP] = new LengthManager(CSSConstants.CSS_PADDING_TOP_PROPERTY, false,
				CSSValueConstants.NUMBER_0);
		vms[StyleConstants.STYLE_VISIBLE_FORMAT] = new VisibleFormatManager();

		// bidi_hcg: Bidi direction
		vms[StyleConstants.STYLE_DIRECTION] = new DirectionManager();

		vms[StyleConstants.STYLE_BACKGROUND_HEIGHT] = new BackgroundSizeManager(
				CSSConstants.CSS_BACKGROUND_HEIGHT_PROPERTY);
		vms[StyleConstants.STYLE_BACKGROUND_WIDTH] = new BackgroundSizeManager(
				CSSConstants.CSS_BACKGROUND_WIDTH_PROPERTY);

		vms[StyleConstants.STYLE_DATA_FORMAT] = new DataFormatManager();
		vms[StyleConstants.STYLE_OVERFLOW] = new OverflowManager();
		vms[StyleConstants.STYLE_HEIGHT] = new LengthManager(CSSConstants.CSS_HEIGHT_PROPERTY, false,
				CSSValueConstants.NUMBER_0);
		vms[StyleConstants.STYLE_WIDTH] = new LengthManager(CSSConstants.CSS_WIDTH_PROPERTY, false,
				CSSValueConstants.NUMBER_0);
	}

	public int getNumberOfProperties() {
		return PerfectHash.TOTAL_KEYWORDS;
	}

	public int getPropertyIndex(String name) {
		return PerfectHash.in_word_set(name);
	}

	public ValueManager getValueManager(int idx) {
		return vms[idx];
	}

	public String getPropertyName(int idx) {
		return vms[idx].getPropertyName();
	}
}
