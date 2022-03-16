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
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.StructureContextUtil;

/**
 * Represents an "TOC" attached to an element.Obtain an instance of this class
 * by calling the <code>getTOCHandle</code> method on the handle of an element
 * that defines an action.
 *
 */

public class TOCHandle extends StructureHandle {

	/**
	 * Default TOC style's prefix name.
	 */
	public static final String defaultTOCPrefixName = "TOC-level-"; //$NON-NLS-1$

	/**
	 * Construct an handle to deal with the toc structure.
	 *
	 * @param element the element that defined the action.
	 * @param context context to the toc property.
	 */

	public TOCHandle(DesignElementHandle element, StructureContext context) {
		super(element, context);
	}

	/**
	 * Construct an handle to deal with the toc structure.
	 *
	 * @param element the element that defined the action.
	 * @param context context to the toc property
	 * @deprecated
	 */

	@Deprecated
	public TOCHandle(DesignElementHandle element, MemberRef context) {
		super(element, context);
	}

	/**
	 * Gets expression of TOC.
	 *
	 * @return expression of TOC.
	 */

	public String getExpression() {
		return getStringProperty(TOC.TOC_EXPRESSION);
	}

	/**
	 * Sets expression of TOC.
	 *
	 * @param expression expression of TOC
	 * @throws SemanticException semantic exception
	 */

	public void setExpression(String expression) throws SemanticException {
		setProperty(TOC.TOC_EXPRESSION, expression);
	}

	/**
	 * Gets style of TOC.
	 *
	 * @return style name of TOC
	 */

	public String getStyleName() {
		StyleHandle handle = getStyle();
		if (handle == null) {
			return null;
		}
		return handle.getName();
	}

	/**
	 * Sets style of TOC.
	 *
	 * @param styleName style name
	 * @throws SemanticException
	 */

	public void setStyleName(String styleName) throws SemanticException {
		setProperty(TOC.TOC_STYLE, styleName);
	}

	/**
	 * Gets TOC style.
	 *
	 * @return style handle.
	 */

	private StyleHandle getStyle() {
		Object value = ((Structure) getStructure()).getLocalProperty(getModule(), TOC.TOC_STYLE);
		if (value instanceof ElementRefValue) {
			ElementRefValue refValue = (ElementRefValue) value;
			if (refValue.isResolved()) {
				Style style = (Style) refValue.getElement();
				return (SharedStyleHandle) style.getHandle(style.getRoot());
			}
		}
		return null;
	}

	/**
	 * Returns a handle to work with the style properties of toc element. Use a
	 * style handle to work with the specific getter/setter methods for each style
	 * property. The style handle is not necessary to work with style properties
	 * generically.
	 * <p>
	 * Note a key difference between this method and the <code>getStyle( )</code>
	 * method. This method returns a handle to the <em>this</em> element. The
	 * <code>getStyle( )</code> method returns a handle to the shared style, if any,
	 * that this element references.
	 *
	 * @return a style handle to work with the style properties of this element.
	 *         Returns <code>null</code> if this element does not have style
	 *         properties.
	 */

	public PrivateStyleHandle getPrivateStyle() {
		return new PrivateStyleHandle(getModule(), getElement());
	}

	/**
	 * Gets border-top-style property.
	 *
	 * @return border-top-style property
	 */

	public String getBorderTopStyle() {
		Object value = getProperty(TOC.BORDER_TOP_STYLE_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBorderTopStyle();

		}
		return (String) value;
	}

	/**
	 * Gets border-top-width property.
	 *
	 * @return border-top-width property
	 */

	public DimensionHandle getBorderTopWidth() {
		Object value = getProperty(TOC.BORDER_TOP_WIDTH_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBorderTopWidth();

		}
		return doGetDimensionHandle(TOC.BORDER_TOP_WIDTH_MEMBER);
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
	 * Returns a color handle for a given member.
	 *
	 * @param memberName the member name
	 * @return a ColorHandle for the given member
	 */

	private ColorHandle doGetColorHandle(String memberName) {
		return new ColorHandle(getElementHandle(), StructureContextUtil.createStructureContext(this, memberName));
	}

	/**
	 * Gets border-top-color property.
	 *
	 * @return border-top-color property
	 */

	public ColorHandle getBorderTopColor() {
		Object value = getProperty(TOC.BORDER_TOP_COLOR_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBorderTopColor();

		}
		return doGetColorHandle(TOC.BORDER_TOP_COLOR_MEMBER);
	}

	/**
	 * Gets border-left-style property.
	 *
	 * @return border-left-style property
	 */

	public String getBorderLeftStyle() {
		Object value = getProperty(TOC.BORDER_LEFT_STYLE_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBorderLeftStyle();
		}
		return (String) value;
	}

	/**
	 * Gets border-left-width property.
	 *
	 * @return border-left-width property
	 */

	public DimensionHandle getBorderLeftWidth() {
		Object value = getProperty(TOC.BORDER_LEFT_WIDTH_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBorderLeftWidth();

		}
		return doGetDimensionHandle(TOC.BORDER_LEFT_WIDTH_MEMBER);
	}

	/**
	 * Gets border-left-color property.
	 *
	 * @return border-left-color property
	 */

	public ColorHandle getBorderLeftColor() {
		Object value = getProperty(TOC.BORDER_LEFT_COLOR_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBorderLeftColor();

		}
		return doGetColorHandle(TOC.BORDER_LEFT_COLOR_MEMBER);
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
		Object value = getProperty(TOC.BORDER_BOTTOM_STYLE_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBorderBottomStyle();

		}
		return (String) value;
	}

	/**
	 *
	 * Gets border-bottom-width property.
	 *
	 * @return border-bottom-width property
	 */

	public DimensionHandle getBorderBottomWidth() {
		Object value = getProperty(TOC.BORDER_BOTTOM_WIDTH_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBorderBottomWidth();

		}
		return doGetDimensionHandle(TOC.BORDER_BOTTOM_WIDTH_MEMBER);
	}

	/**
	 * Gets border-bottom-width property.
	 *
	 * @return border-bottom-width property
	 */

	public ColorHandle getBorderBottomColor() {
		Object value = getProperty(TOC.BORDER_BOTTOM_COLOR_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBorderBottomColor();

		}
		return doGetColorHandle(TOC.BORDER_BOTTOM_COLOR_MEMBER);
	}

	/**
	 * Gets border-right-style property.
	 *
	 * @return border-right-style property
	 */

	public String getBorderRightStyle() {
		Object value = getProperty(TOC.BORDER_RIGHT_STYLE_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBorderRightStyle();

		}
		return (String) value;
	}

	/**
	 * Gets border-right-width property.
	 *
	 * @return border-right-width property
	 */

	public DimensionHandle getBorderRightWidth() {
		Object value = getProperty(TOC.BORDER_RIGHT_WIDTH_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBorderRightWidth();

		}
		return doGetDimensionHandle(TOC.BORDER_RIGHT_WIDTH_MEMBER);
	}

	/**
	 * Gets border-right-color property.
	 *
	 * @return border-right-color property
	 */

	public ColorHandle getBorderRightColor() {
		Object value = getProperty(TOC.BORDER_RIGHT_COLOR_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBorderRightColor();

		}
		return doGetColorHandle(TOC.BORDER_RIGHT_COLOR_MEMBER);
	}

	/**
	 * Gets back-ground-color property.
	 *
	 * @return back-ground-color property
	 */

	public ColorHandle getBackgroundColor() {
		Object value = getProperty(TOC.BACKGROUND_COLOR_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getBackgroundColor();

		}
		return doGetColorHandle(TOC.BACKGROUND_COLOR_MEMBER);
	}

	/**
	 * Gets date time format property.
	 *
	 * @return date time format property
	 */

	public String getDateTimeFormat() {
		Object value = getProperty(TOC.DATE_TIME_FORMAT_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getDateTimeFormat();

		}
		return ((DateTimeFormatValue) value).getPattern();
	}

	/**
	 * Gets date time format category property.
	 *
	 * @return date time format category property
	 */

	public String getDateTimeFormatCategory() {
		Object value = getProperty(TOC.DATE_TIME_FORMAT_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getDateTimeFormatCategory();

		}

		assert value instanceof DateTimeFormatValue;

		return ((DateTimeFormatValue) value).getCategory();
	}

	/**
	 * Gets number format property.
	 *
	 * @return number format property
	 */

	public String getNumberFormat() {
		Object value = getProperty(TOC.NUMBER_FORMAT_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getNumberFormat();

		}
		assert value instanceof NumberFormatValue;

		return ((NumberFormatValue) value).getPattern();
	}

	/**
	 * Gets number format category property.
	 *
	 * @return number format category property
	 */

	public String getNumberFormatCategory() {
		Object value = getProperty(TOC.NUMBER_FORMAT_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getNumberFormatCategory();

		}
		assert value instanceof NumberFormatValue;

		return ((NumberFormatValue) value).getCategory();
	}

	/**
	 * Gets number align property.
	 *
	 * @return number align property
	 */

	public String getNumberAlign() {
		Object value = getProperty(TOC.NUMBER_ALIGN_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getVerticalAlign();

		}
		return (String) value;
	}

	/**
	 * Gets string format property.
	 *
	 * @return string format property
	 */

	public String getStringFormat() {
		Object value = getProperty(TOC.STRING_FORMAT_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getStringFormat();

		}

		assert value instanceof StringFormatValue;

		return ((StringFormatValue) value).getPattern();
	}

	/**
	 * Returns the category of a string format for a toc.
	 *
	 * @return the category of a string format
	 */

	public String getStringFormatCategory() {

		Object value = getProperty(TOC.STRING_FORMAT_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getStringFormatCategory();

		}
		assert value instanceof StringFormatValue;

		return ((StringFormatValue) value).getCategory();
	}

	/**
	 * Gets font family property.
	 *
	 * @return font family property
	 */

	public FontHandle getFontFamily() {
		Object value = getProperty(TOC.FONT_FAMILY_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getFontFamilyHandle();

		}
		return doGetFontHandle(TOC.FONT_FAMILY_MEMBER);
	}

	/**
	 * Returns a font family handle for a member.
	 *
	 * @param memberName the member name.
	 * @return A FontHandle for the given member.
	 */

	private FontHandle doGetFontHandle(String memberName) {
		return new FontHandle(getElementHandle(), StructureContextUtil.createStructureContext(this, memberName));
	}

	/**
	 * Gets font size property.
	 *
	 * @return font size property
	 */

	public DimensionHandle getFontSize() {
		Object value = getProperty(TOC.FONT_SIZE_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getFontSize();

		}
		return doGetDimensionHandle(TOC.FONT_SIZE_MEMBER);
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
		Object value = getProperty(TOC.FONT_STYLE_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getFontStyle();

		}
		return (String) value;
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
		Object value = getProperty(TOC.FONT_WEIGHT_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getFontWeight();

		}
		return (String) value;
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
		Object value = getProperty(TOC.FONT_VARIANT_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getFontVariant();

		}
		return (String) value;
	}

	/**
	 * Gets font color property.
	 *
	 * @return font color property
	 */

	public ColorHandle getColor() {
		Object value = getProperty(TOC.COLOR_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getColor();

		}
		return doGetColorHandle(TOC.COLOR_MEMBER);
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
		Object value = getProperty(TOC.TEXT_UNDERLINE_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getTextUnderline();

		}
		return (String) value;
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
		Object value = getProperty(TOC.TEXT_OVERLINE_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getTextOverline();

		}
		return (String) value;
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
		Object value = getProperty(TOC.TEXT_LINE_THROUGH_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getTextLineThrough();

		}
		return (String) value;
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
		Object value = getProperty(TOC.TEXT_ALIGN_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getTextAlign();

		}
		return (String) value;
	}

	/**
	 * Returns the value of direction property. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>BIDI_DIRECTION_LTR</code>
	 * <li><code>BIDI_DIRECTION_RTL</code>
	 * </ul>
	 *
	 * @return the value of direction property
	 */

	public String getTextDirection() {
		Object value = getProperty(TOC.TEXT_DIRECTION_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getTextDirection();

		}
		return (String) value;
	}

	/**
	 * Sets the Bidi direction property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>BIDI_DIRECTION_LTR</code>
	 * <li><code>BIDI_DIRECTION_RTL</code>
	 * </ul>
	 *
	 * @param value the new direction value
	 * @throws SemanticException if the value is not one of the above.
	 *
	 * @author bidi_hcg
	 */

	public void setTextDirection(String value) throws SemanticException {
		setProperty(TOC.TEXT_DIRECTION_MEMBER, value);
	}

	/**
	 * Gets text indent property.
	 *
	 * @return text indent property
	 */

	public DimensionHandle getTextIndent() {
		Object value = getProperty(TOC.TEXT_INDENT_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getTextIndent();

		}
		return doGetDimensionHandle(TOC.TEXT_INDENT_MEMBER);
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
		Object value = getProperty(TOC.TEXT_TRANSFORM_MEMBER);
		if (value == null) {
			StyleHandle style = getStyle();
			if (style == null) {
				return null;
			}

			return style.getTextTransform();

		}
		return (String) value;
	}
}
