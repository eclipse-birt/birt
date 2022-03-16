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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.StructureContextUtil;

/**
 * Represents a highlight rule in the highlight property of a style. A highlight
 * rule gives a set of conditional style properties along with a condition for
 * when to apply the properties. A highlight can be defined in either a shared
 * style or a private style.
 *
 * @see ColorHandle
 * @see DimensionHandle
 * @see FontHandle
 * @see org.eclipse.birt.report.model.api.elements.structures.HighlightRule
 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
 */

public class HighlightRuleHandle extends StyleRuleHandle {

	/**
	 * Constructs a highlight rule handle with the given
	 * <code>SimpleValueHandle</code> and the index of the highlight rule in the
	 * highlight.
	 *
	 * @param valueHandle handle to a list property or member
	 * @param index       index of the structure within the list
	 */

	public HighlightRuleHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns a handle to work with the color property.
	 *
	 * @return a ColorHandle to deal with the color.
	 */

	public ColorHandle getColor() {
		return doGetColorHandle(HighlightRule.COLOR_MEMBER);
	}

	/**
	 * Returns a handle to work with the background color.
	 *
	 * @return a ColorHandle to deal with the background color.
	 */

	public ColorHandle getBackgroundColor() {
		return doGetColorHandle(HighlightRule.BACKGROUND_COLOR_MEMBER);
	}

	/**
	 * Returns a handle to work with the border top color.
	 *
	 * @return a ColorHandle to deal with the border top color.
	 */

	public ColorHandle getBorderTopColor() {
		return doGetColorHandle(HighlightRule.BORDER_TOP_COLOR_MEMBER);
	}

	/**
	 * Returns a handle to work with the border left color.
	 *
	 * @return a ColorHandle to deal with the border left color.
	 */

	public ColorHandle getBorderLeftColor() {
		return doGetColorHandle(HighlightRule.BORDER_LEFT_COLOR_MEMBER);
	}

	/**
	 * Returns a handle to work with the border right color.
	 *
	 * @return a ColorHandle to deal with the border right color.
	 */

	public ColorHandle getBorderRightColor() {
		return doGetColorHandle(HighlightRule.BORDER_RIGHT_COLOR_MEMBER);
	}

	/**
	 * Returns a handle to work with the border bottom color.
	 *
	 * @return a ColorHandle to deal with the border bottom color.
	 */

	public ColorHandle getBorderBottomColor() {
		return doGetColorHandle(HighlightRule.BORDER_BOTTOM_COLOR_MEMBER);
	}

	/**
	 * Returns a color handle for a given member.
	 *
	 * @param memberName the member name
	 * @return a ColorHandle for the given member
	 */

	private ColorHandle doGetColorHandle(String memberName) {
		return new ColorHandle(getElementHandle(), StructureContextUtil.createStructureContext(this, memberName));
	}

	/**
	 * Returns the style of the border bottom line. The return value is one of the
	 * CSS (pre-defined) values see <code>DesignChoiceConstants</code>. They are:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 *
	 * @return the border bottom style
	 */

	public String getBorderBottomStyle() {
		return getStringProperty(HighlightRule.BORDER_BOTTOM_STYLE_MEMBER);
	}

	/**
	 * Sets the style of the border bottom line. The input value is one of the CSS
	 * (pre-defined) values see <code>DesignChoiceConstants</code>.
	 *
	 * @param value the new border bottom line style
	 * @throws SemanticException if the value is not one of above.
	 * @see #getBorderBottomStyle()
	 */

	public void setBorderBottomStyle(String value) throws SemanticException {
		setProperty(HighlightRule.BORDER_BOTTOM_STYLE_MEMBER, value);
	}

	/**
	 * Returns the style of the border left line.
	 *
	 * @return the border left line style
	 * @see #getBorderBottomStyle()
	 */

	public String getBorderLeftStyle() {
		return getStringProperty(HighlightRule.BORDER_LEFT_STYLE_MEMBER);

	}

	/**
	 * Sets the style of the border left line.
	 *
	 * @param value the new border left line style
	 * @throws SemanticException if the value is not one of above.
	 * @see #setBorderBottomStyle(String )
	 */

	public void setBorderLeftStyle(String value) throws SemanticException {
		setProperty(HighlightRule.BORDER_LEFT_STYLE_MEMBER, value);
	}

	/**
	 * Returns the style of the border right line.
	 *
	 * @return the border right line style
	 * @see #getBorderBottomStyle()
	 */

	public String getBorderRightStyle() {
		return getStringProperty(HighlightRule.BORDER_RIGHT_STYLE_MEMBER);
	}

	/**
	 * Sets the style of the border right line.
	 *
	 * @param value the new border right line style
	 * @throws SemanticException if the value is not one of above.
	 * @see #setBorderBottomStyle(String )
	 */

	public void setBorderRightStyle(String value) throws SemanticException {
		setProperty(HighlightRule.BORDER_RIGHT_STYLE_MEMBER, value);
	}

	/**
	 * Returns the style of the top line of the border.
	 *
	 * @return the border top line style
	 * @see #getBorderBottomStyle()
	 */

	public String getBorderTopStyle() {
		return getStringProperty(HighlightRule.BORDER_TOP_STYLE_MEMBER);

	}

	/**
	 * Sets the style of the top line of the border.
	 *
	 * @param value the new border top line style
	 * @throws SemanticException if the value is not one of above.
	 * @see #setBorderBottomStyle(String )
	 */

	public void setBorderTopStyle(String value) throws SemanticException {
		setProperty(HighlightRule.BORDER_TOP_STYLE_MEMBER, value);
	}

	/**
	 * gets the expression for this highlight rule.
	 *
	 * @return the expression value
	 */
	public String getTestExpression() {
		return getStringProperty(HighlightRule.TEST_EXPR_MEMBER);
	}

	/**
	 * sets the test expression for this hilghtlight rule.
	 *
	 * @param expression the expression
	 */

	public void setTestExpression(String expression) {
		setPropertySilently(HighlightRule.TEST_EXPR_MEMBER, expression);
	}

	/**
	 * Returns the value of the underline property. The returned value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>TEXT_UNDERLINE_NONE</code>
	 * <li><code>TEXT_UNDERLINE_UNDERLINE</code>
	 * </ul>
	 *
	 * @return the value of the underline property
	 */

	public String getTextUnderline() {
		return getStringProperty(HighlightRule.TEXT_UNDERLINE_MEMBER);
	}

	/**
	 * Sets the text underline property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>TEXT_UNDERLINE_NONE</code>
	 * <li><code>TEXT_UNDERLINE_UNDERLINE</code>
	 * </ul>
	 *
	 * @param value the new text underline
	 * @throws SemanticException if the value is not one of the above.
	 */

	public void setTextUnderline(String value) throws SemanticException {
		setProperty(HighlightRule.TEXT_UNDERLINE_MEMBER, value);
	}

	/**
	 * Returns the value of the overline property. The returned value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>TEXT_OVERLINE_NONE</code>
	 * <li><code>TEXT_OVERLINE_OVERLINE</code>
	 * </ul>
	 *
	 * @return the value of the overline property
	 */

	public String getTextOverline() {
		return getStringProperty(HighlightRule.TEXT_OVERLINE_MEMBER);
	}

	/**
	 * Sets the text overline property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>TEXT_OVERLINE_NONE</code>
	 * <li><code>TEXT_OVERLINE_OVERLINE</code>
	 * </ul>
	 *
	 * @param value the new text overline
	 * @throws SemanticException if the value is not one of the above
	 */

	public void setTextOverline(String value) throws SemanticException {
		setProperty(HighlightRule.TEXT_OVERLINE_MEMBER, value);
	}

	/**
	 * Returns the value of the line through property. The returned value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>TEXT_LINE_THROUGH_NONE</code>
	 * <li><code>TEXT_LINE_THROUGH_LINE_THROUGH</code>
	 * </ul>
	 *
	 * @return the text line through
	 */

	public String getTextLineThrough() {
		return getStringProperty(HighlightRule.TEXT_LINE_THROUGH_MEMBER);
	}

	/**
	 * Sets the text line through property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>TEXT_LINE_THROUGH_NONE</code>
	 * <li><code>TEXT_LINE_THROUGH_LINE_THROUGH</code>
	 * </ul>
	 *
	 * @param value the new text line through
	 * @throws SemanticException if the value is not one of the above.
	 */

	public void setTextLineThrough(String value) throws SemanticException {
		setProperty(HighlightRule.TEXT_LINE_THROUGH_MEMBER, value);
	}

	/**
	 * Returns the value of text align property. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>TEXT_ALIGN_LEFT</code>
	 * <li><code>TEXT_ALIGN_CENTER</code>
	 * <li><code>TEXT_ALIGN_RIGHT</code>
	 * <li><code>TEXT_ALIGN_JUSTIFY</code>
	 * </ul>
	 *
	 * @return the value of text align property
	 */

	public String getTextAlign() {
		return getStringProperty(HighlightRule.TEXT_ALIGN_MEMBER);
	}

	/**
	 * Sets the text align property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>TEXT_ALIGN_LEFT</code>
	 * <li><code>TEXT_ALIGN_CENTER</code>
	 * <li><code>TEXT_ALIGN_RIGHT</code>
	 * <li><code>TEXT_ALIGN_JUSTIFY</code>
	 * </ul>
	 *
	 * @param value the new text align value
	 * @throws SemanticException if the value is not one of the above.
	 */

	public void setTextAlign(String value) throws SemanticException {
		setProperty(HighlightRule.TEXT_ALIGN_MEMBER, value);
	}

	/**
	 * Returns the value of Bidi direction property. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>BIDI_ORIENTATION_LTR</code>
	 * <li><code>BIDI_ORIENTATION_RTL</code>
	 * </ul>
	 *
	 * @return the value of Bidi direction property
	 *
	 * @author bidi_hcg
	 */

	public String getTextDirection() {
		return getStringProperty(HighlightRule.TEXT_DIRECTION_MEMBER);
	}

	/**
	 * Sets the Bidi direction property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>BIDI_ORIENTATION_LTR</code>
	 * <li><code>BIDI_ORIENTATION_RTL</code>
	 * </ul>
	 *
	 * @param value the new direction value
	 * @throws SemanticException if the value is not one of the above.
	 *
	 * @author bidi_hcg
	 */

	public void setTextDirection(String value) throws SemanticException {
		setProperty(HighlightRule.TEXT_DIRECTION_MEMBER, value);
	}

	/**
	 * Returns the value of the text transform property. The return value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>TRANSFORM_CAPITALIZE</code>
	 * <li><code>TRANSFORM_UPPERCASE</code>
	 * <li><code>TRANSFORM_LOWERCASE</code>
	 * <li><code>TRANSFORM_NONE</code>
	 * </ul>
	 *
	 * @return the value of the transform property
	 */

	public String getTextTransform() {
		return getStringProperty(HighlightRule.TEXT_TRANSFORM_MEMBER);
	}

	/**
	 * Sets the text transform property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>TRANSFORM_CAPITALIZE</code>
	 * <li><code>TRANSFORM_UPPERCASE</code>
	 * <li><code>TRANSFORM_LOWERCASE</code>
	 * <li><code>TRANSFORM_NONE</code>
	 * </ul>
	 *
	 * @param value the new text transform
	 * @throws SemanticException if the value is not one of the above.
	 */

	public void setTextTransform(String value) throws SemanticException {
		setProperty(HighlightRule.TEXT_TRANSFORM_MEMBER, value);
	}

	/**
	 * Gets a handle to deal with the value of the text-indent property.
	 *
	 * @return a DimensionHandle to deal with the text-indent.
	 */

	public DimensionHandle getTextIndent() {
		return doGetDimensionHandle(HighlightRule.TEXT_INDENT_MEMBER);
	}

	/**
	 * Returns the value of the number-align member.
	 *
	 * @return the number-align value
	 */

	public String getNumberAlign() {
		return getStringProperty(HighlightRule.NUMBER_ALIGN_MEMBER);
	}

	/**
	 * Sets the value of the number-align member
	 *
	 * @param value the new number-align value.
	 */

	public void setNumberAlign(String value) {
		setPropertySilently(HighlightRule.NUMBER_ALIGN_MEMBER, value);
	}

	/**
	 * Returns a handle to work with the width of the top side of the border.
	 *
	 * @return a DimensionHandle to deal with the width of the top side of the
	 *         border.
	 */

	public DimensionHandle getBorderTopWidth() {
		return doGetDimensionHandle(HighlightRule.BORDER_TOP_WIDTH_MEMBER);
	}

	/**
	 * Returns a handle to work with the width of the left side of the border.
	 *
	 * @return a DimensionHandle to deal with the width of the left side of the
	 *         border.
	 */

	public DimensionHandle getBorderLeftWidth() {
		return doGetDimensionHandle(HighlightRule.BORDER_LEFT_WIDTH_MEMBER);
	}

	/**
	 * Returns a handle to work with the width of the right side of the border.
	 *
	 * @return DimensionHandle to deal with the width of the right side of the
	 *         border.
	 */

	public DimensionHandle getBorderRightWidth() {
		return doGetDimensionHandle(HighlightRule.BORDER_RIGHT_WIDTH_MEMBER);
	}

	/**
	 * Returns a handle to work with the width of the bottom side of the border.
	 *
	 * @return a DimensionHandle to deal with the width of the bottom side of the
	 *         border.
	 */

	public DimensionHandle getBorderBottomWidth() {
		return doGetDimensionHandle(HighlightRule.BORDER_BOTTOM_WIDTH_MEMBER);
	}

	/**
	 * Returns a handle to work with the font size.
	 *
	 * @return a aDimensionHandle to deal with the font size.
	 */

	public DimensionHandle getFontSize() {
		return doGetDimensionHandle(HighlightRule.FONT_SIZE_MEMBER);
	}

	/**
	 * Returns a dimension handle for a member.
	 *
	 * @param memberName the member name.
	 * @return A DimensionHandle for the given member.
	 */

	private DimensionHandle doGetDimensionHandle(String memberName) {
		return new DimensionHandle(getElementHandle(), StructureContextUtil.createStructureContext(this, memberName));
	}

	/**
	 * Returns the font family handle of the highlight rule.
	 *
	 * @return the font family handle of the highlight rule.
	 */

	public FontHandle getFontFamilyHandle() {
		return new FontHandle(getElementHandle(),
				StructureContextUtil.createStructureContext(this, HighlightRule.FONT_FAMILY_MEMBER));

	}

	/**
	 * Returns the font weight of the highlight rule. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>FONT_WEIGHT_NORMAL</code>
	 * <li><code>FONT_WEIGHT_BOLD</code>
	 * <li><code>FONT_WEIGHT_BOLDER</code>
	 * <li><code>FONT_WEIGHT_LIGHTER</code>
	 * <li><code>FONT_WEIGHT_100</code>
	 * <li><code>FONT_WEIGHT_200</code>
	 * <li><code>FONT_WEIGHT_300</code>
	 * <li><code>FONT_WEIGHT_400</code>
	 * <li><code>FONT_WEIGHT_500</code>
	 * <li><code>FONT_WEIGHT_600</code>
	 * <li><code>FONT_WEIGHT_700</code>
	 * <li><code>FONT_WEIGHT_800</code>
	 * <li><code>FONT_WEIGHT_900</code>
	 * </ul>
	 *
	 * @return the font weight in string.
	 */

	public String getFontWeight() {
		return getStringProperty(HighlightRule.FONT_WEIGHT_MEMBER);
	}

	/**
	 * Sets the font weight in a string for the style. The input value is defined in
	 * <code>DesignChoiceConstants</code>.
	 *
	 * @param value the new font weight
	 * @throws SemanticException if the input value is not one of the above
	 * @see #getFontWeight()
	 */

	public void setFontWeight(String value) throws SemanticException {
		setProperty(HighlightRule.FONT_WEIGHT_MEMBER, value);
	}

	/**
	 * Returns the font variant in a string. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>FONT_VARIANT_NORMAL</code>
	 * <li><code>FONT_VARIANT_SMALL_CAPS</code>
	 * </ul>
	 *
	 * @return the font variant in a string.
	 */

	public String getFontVariant() {
		return getStringProperty(HighlightRule.FONT_VARIANT_MEMBER);
	}

	/**
	 * Sets the font variant in a string . The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>FONT_VARIANT_NORMAL</code>
	 * <li><code>FONT_VARIANT_SMALL_CAPS</code>
	 * </ul>
	 *
	 * @param value the new font variant.
	 * @throws SemanticException if the input value is not one of the above
	 */

	public void setFontVariant(String value) throws SemanticException {
		setProperty(HighlightRule.FONT_VARIANT_MEMBER, value);
	}

	/**
	 * Returns the font style handle for the style. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>FONT_STYLE_NORMAL</code>
	 * <li><code>FONT_STYLE_ITALIC</code>
	 * <li><code>FONT_STYLE_OBLIQUE</code>
	 * </ul>
	 *
	 * @return the font style in string.
	 */

	public String getFontStyle() {
		return getStringProperty(HighlightRule.FONT_STYLE_MEMBER);
	}

	/**
	 * Sets the font style in a string for the style. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>FONT_STYLE_NORMAL</code>
	 * <li><code>FONT_STYLE_ITALIC</code>
	 * <li><code>FONT_STYLE_OBLIQUE</code>
	 * </ul>
	 *
	 * @param value the new font style.
	 * @throws SemanticException if the input value is not one of the above
	 */

	public void setFontStyle(String value) throws SemanticException {
		setProperty(HighlightRule.FONT_STYLE_MEMBER, value);
	}

	/**
	 * Returns the pattern of a string format for a highlight rule.
	 *
	 * @return the pattern of a string format
	 */

	public String getStringFormat() {
		Object value = getProperty(HighlightRule.STRING_FORMAT_MEMBER);
		if (value == null) {
			return null;
		}

		assert value instanceof StringFormatValue;

		return ((StringFormatValue) value).getPattern();
	}

	/**
	 * Returns the category of a string format for a highlight rule.
	 *
	 * @return the category of a string forma
	 */

	public String getStringFormatCategory() {
		Object value = getProperty(HighlightRule.STRING_FORMAT_MEMBER);
		if (value == null) {
			return null;
		}

		assert value instanceof StringFormatValue;

		return ((StringFormatValue) value).getCategory();
	}

	/**
	 * Sets the pattern of a string format for a highlight rule.
	 *
	 * @param pattern the pattern of a string forma
	 */

	public void setStringFormat(String pattern) {
		try {
			setFormatValue(HighlightRule.STRING_FORMAT_MEMBER, FormatValue.PATTERN_MEMBER, pattern);
		} catch (SemanticException e) {
			assert false;
		}
	}

	/**
	 * Sets the category of a string format for a highlight rule. The
	 * <code>pattern</code> can be one of:
	 *
	 * <ul>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER</code>
	 * <li>
	 * <code>DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER</code>
	 * </ul>
	 *
	 * @param category the category of a string format
	 * @throws SemanticException if <code>category</code> is not one of the above
	 *                           values.
	 */

	public void setStringFormatCategory(String category) throws SemanticException {
		setFormatValue(HighlightRule.STRING_FORMAT_MEMBER, FormatValue.CATEGORY_MEMBER, category);
	}

	/**
	 * Returns the pattern of a number format for a highlight rule.
	 *
	 * @return the pattern of a number format
	 */

	public String getNumberFormat() {
		Object value = getProperty(HighlightRule.NUMBER_FORMAT_MEMBER);
		if (value == null) {
			return null;
		}

		assert value instanceof NumberFormatValue;

		return ((NumberFormatValue) value).getPattern();
	}

	/**
	 * Returns the category of a number format for a highlight rule.
	 *
	 * @return the category of a number format
	 */

	public String getNumberFormatCategory() {
		Object value = getProperty(HighlightRule.NUMBER_FORMAT_MEMBER);
		if (value == null) {
			return null;
		}

		assert value instanceof NumberFormatValue;

		return ((NumberFormatValue) value).getCategory();
	}

	/**
	 * Sets the pattern of a number format for a highlight rule.
	 *
	 * @param pattern the pattern of a number format
	 */

	public void setNumberFormat(String pattern) {
		try {
			setFormatValue(HighlightRule.NUMBER_FORMAT_MEMBER, FormatValue.PATTERN_MEMBER, pattern);
		} catch (SemanticException e) {
			assert false;
		}

	}

	/**
	 * Sets the category of a number format for a highlight rule. The
	 * <code>pattern</code> can be one of:
	 *
	 * <ul>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_STANDARD</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM</code>
	 * </ul>
	 *
	 * @param category the category of a number format
	 * @throws SemanticException if <code>category</code> is not one of the above
	 *                           values.
	 */

	public void setNumberFormatCategory(String category) throws SemanticException {
		setFormatValue(HighlightRule.NUMBER_FORMAT_MEMBER, FormatValue.CATEGORY_MEMBER, category);
	}

	/**
	 * Returns the pattern of the date-time-format for a highlight rule.
	 *
	 * @return the pattern of the date-time-format
	 */

	public String getDateTimeFormat() {
		Object value = getProperty(HighlightRule.DATE_TIME_FORMAT_MEMBER);
		if (value == null) {
			return null;
		}

		assert value instanceof DateTimeFormatValue;

		return ((DateTimeFormatValue) value).getPattern();
	}

	/**
	 * Returns the category of the date-time-format for a highlight rule.
	 *
	 * @return the category of the date-time-format
	 */

	public String getDateTimeFormatCategory() {
		Object value = getProperty(HighlightRule.DATE_TIME_FORMAT_MEMBER);
		if (value == null) {
			return null;
		}

		assert value instanceof DateTimeFormatValue;

		return ((DateTimeFormatValue) value).getCategory();
	}

	/**
	 * Sets the pattern of a date time format for a highlight rule.
	 *
	 * @param pattern the pattern of a date time format
	 */

	public void setDateTimeFormat(String pattern) {
		try {
			setFormatValue(HighlightRule.DATE_TIME_FORMAT_MEMBER, FormatValue.PATTERN_MEMBER, pattern);
		} catch (SemanticException e) {
			assert false;
		}

	}

	/**
	 * Sets the category of a number format for a highlight rule. The
	 * <code>pattern</code> can be one of:
	 *
	 * <ul>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM</code>
	 * </ul>
	 *
	 * @param pattern the category of a date-time format
	 * @throws SemanticException if <code>pattern</code> is not one of the above
	 *                           values.
	 */

	public void setDateTimeFormatCategory(String pattern) throws SemanticException {
		setFormatValue(HighlightRule.DATE_TIME_FORMAT_MEMBER, FormatValue.CATEGORY_MEMBER, pattern);
	}

	/**
	 * Sets the category/pattern value for a string/number/date-time format.
	 *
	 * @param propName   the property name
	 * @param memberName the category or pattern member name
	 * @param valueToSet the value to set
	 * @throws SemanticException if the category is not one of BIRT defined.
	 */

	private void setFormatValue(String propName, String memberName, String valueToSet) throws SemanticException {
		Object value = getProperty(propName);
		FormatValue formatValueToSet = null;

		if (value == null) {
			if (HighlightRule.DATE_TIME_FORMAT_MEMBER.equalsIgnoreCase(propName)) {
				formatValueToSet = new DateTimeFormatValue();
			} else if (HighlightRule.NUMBER_FORMAT_MEMBER.equalsIgnoreCase(propName)) {
				formatValueToSet = new NumberFormatValue();
			} else if (HighlightRule.STRING_FORMAT_MEMBER.equalsIgnoreCase(propName)) {
				formatValueToSet = new StringFormatValue();
			} else {
				assert false;
			}

			if (FormatValue.CATEGORY_MEMBER.equalsIgnoreCase(memberName)) {
				formatValueToSet.setCategory(valueToSet);
			} else if (FormatValue.PATTERN_MEMBER.equalsIgnoreCase(memberName)) {
				formatValueToSet.setPattern(valueToSet);
			} else {
				assert false;
			}

			setProperty(propName, formatValueToSet);
		} else {
			MemberHandle propHandle = getMember(propName);
			formatValueToSet = (FormatValue) propHandle.getValue();

			FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);

			if (FormatValue.CATEGORY_MEMBER.equalsIgnoreCase(memberName)) {
				formatHandle.setCategory(valueToSet);
			} else if (FormatValue.PATTERN_MEMBER.equalsIgnoreCase(memberName)) {
				formatHandle.setPattern(valueToSet);
			} else {
				assert false;
			}
		}
	}

	/**
	 * Sets the style property. If it is a valid style and highlight rule has no
	 * local values, values on the style are returned.
	 *
	 * @param style the style
	 * @throws SemanticException
	 */

	public void setStyle(StyleHandle style) throws SemanticException {
		if (style == null) {
			setProperty(HighlightRule.STYLE_MEMBER, null);
			return;
		}

		setProperty(HighlightRule.STYLE_MEMBER, style.getElement());
	}

	/**
	 * Sets the style property. If it is a valid style and highlight rule has no
	 * local values, values on the style are returned.
	 *
	 * @param styleName the style name
	 * @throws SemanticException
	 */

	public void setStyleName(String styleName) throws SemanticException {
		setProperty(HighlightRule.STYLE_MEMBER, styleName);
	}

	/**
	 * Returns the style that the highlight rule links with.
	 *
	 * @return the style
	 */

	public StyleHandle getStyle() {

		Object value = ((Structure) getStructure()).getLocalProperty(getModule(), HighlightRule.STYLE_MEMBER);

		if (value instanceof ElementRefValue) {
			ElementRefValue refValue = (ElementRefValue) value;
			if (refValue.isResolved()) {
				Style style = (Style) refValue.getElement();
				return (StyleHandle) style.getHandle(style.getRoot());
			}
		}
		return null;
	}

	/**
	 * Returns a handle to work with the height of the line.
	 *
	 * @return a DimensionHandle to deal with the height o f the line.
	 */

	public DimensionHandle getLineHeight() {
		return doGetDimensionHandle(HighlightRule.LINE_HEIGHT_MEMBER);
	}
}
