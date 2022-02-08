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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Represents a highlight rule that says how a data item should appear based on
 * the value within it. All highlight rules are driven by a value expression
 * defined on the style. Each rule has an expression that matches a set of
 * values, and a set of font and border instructions for how to format the data
 * item when the rule "fires."
 * 
 */

public class HighlightRule extends StyleRule {

	public static final String BORDER_TOP_STYLE_MEMBER = Style.BORDER_TOP_STYLE_PROP;
	public static final String BORDER_TOP_WIDTH_MEMBER = Style.BORDER_TOP_WIDTH_PROP;
	public static final String BORDER_TOP_COLOR_MEMBER = Style.BORDER_TOP_COLOR_PROP;
	public static final String BORDER_LEFT_STYLE_MEMBER = Style.BORDER_LEFT_STYLE_PROP;
	public static final String BORDER_LEFT_WIDTH_MEMBER = Style.BORDER_LEFT_WIDTH_PROP;
	public static final String BORDER_LEFT_COLOR_MEMBER = Style.BORDER_LEFT_COLOR_PROP;
	public static final String BORDER_BOTTOM_STYLE_MEMBER = Style.BORDER_BOTTOM_STYLE_PROP;
	public static final String BORDER_BOTTOM_WIDTH_MEMBER = Style.BORDER_BOTTOM_WIDTH_PROP;
	public static final String BORDER_BOTTOM_COLOR_MEMBER = Style.BORDER_BOTTOM_COLOR_PROP;
	public static final String BORDER_RIGHT_STYLE_MEMBER = Style.BORDER_RIGHT_STYLE_PROP;
	public static final String BORDER_RIGHT_WIDTH_MEMBER = Style.BORDER_RIGHT_WIDTH_PROP;
	public static final String BORDER_RIGHT_COLOR_MEMBER = Style.BORDER_RIGHT_COLOR_PROP;
	public static final String BACKGROUND_COLOR_MEMBER = Style.BACKGROUND_COLOR_PROP;
	public static final String BACKGROUND_IMAGE_MEMBER = Style.BACKGROUND_IMAGE_PROP;
	public static final String DATE_TIME_FORMAT_MEMBER = Style.DATE_TIME_FORMAT_PROP;
	public static final String NUMBER_FORMAT_MEMBER = Style.NUMBER_FORMAT_PROP;
	public static final String NUMBER_ALIGN_MEMBER = Style.NUMBER_ALIGN_PROP;
	public static final String STRING_FORMAT_MEMBER = Style.STRING_FORMAT_PROP;
	public static final String FONT_FAMILY_MEMBER = Style.FONT_FAMILY_PROP;
	public static final String FONT_SIZE_MEMBER = Style.FONT_SIZE_PROP;
	public static final String FONT_STYLE_MEMBER = Style.FONT_STYLE_PROP;
	public static final String FONT_WEIGHT_MEMBER = Style.FONT_WEIGHT_PROP;
	public static final String FONT_VARIANT_MEMBER = Style.FONT_VARIANT_PROP;
	public static final String COLOR_MEMBER = Style.COLOR_PROP;
	public static final String TEXT_UNDERLINE_MEMBER = Style.TEXT_UNDERLINE_PROP;
	public static final String TEXT_OVERLINE_MEMBER = Style.TEXT_OVERLINE_PROP;
	public static final String TEXT_LINE_THROUGH_MEMBER = Style.TEXT_LINE_THROUGH_PROP;
	public static final String TEXT_ALIGN_MEMBER = Style.TEXT_ALIGN_PROP;
	public static final String TEXT_TRANSFORM_MEMBER = Style.TEXT_TRANSFORM_PROP;
	public static final String TEXT_INDENT_MEMBER = Style.TEXT_INDENT_PROP;
	public static final String TEXT_DIRECTION_MEMBER = Style.TEXT_DIRECTION_PROP; // bidi_hcg
	public static final String PADDING_TOP_MEMBER = Style.PADDING_TOP_PROP;
	public static final String PADDING_LEFT_MEMBER = Style.PADDING_LEFT_PROP;
	public static final String PADDING_BOTTOM_MEMBER = Style.PADDING_BOTTOM_PROP;
	public static final String PADDING_RIGHT_MEMBER = Style.PADDING_RIGHT_PROP;
	public static final String BACKGROUND_REPEAT_MEMBER = Style.BACKGROUND_REPEAT_PROP;
	public static final String LINE_HEIGHT_MEMBER = Style.LINE_HEIGHT_PROP;
	/**
	 * Name of this structure within the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "HighlightRule"; //$NON-NLS-1$

	/**
	 * The style member.
	 */

	public static final String STYLE_MEMBER = "style"; //$NON-NLS-1$

	/**
	 * The reference to a style.
	 */

	private ElementRefValue style = null;

	/**
	 * Default Constructor.
	 */

	public HighlightRule() {
		super();
	}

	/**
	 * Constructs the highlight rule with an operator and its arguments.
	 * 
	 * @param op        the supported operator. One of the internal choice values
	 *                  identified in the meta-data dictionary
	 * @param v1        the comparison value expressions for operators that take one
	 *                  or two arguments (equals, like, between)
	 * @param v2        the second comparison value for operators that take two
	 *                  arguments (between)
	 * @param testExpre the expression to check
	 */

	public HighlightRule(String op, String v1, String v2, String testExpre) {
		super(op, v1, v2, testExpre);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return STRUCTURE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getDefn()
	 */

	public IStructureDefn getDefn() {
		return MetaDataDictionary.getInstance().getStructure(STRUCTURE_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new HighlightRuleHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.elements.structures.StyleRule#
	 * getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (STYLE_MEMBER.equals(propName))
			return style;

		return super.getIntrinsicProperty(propName);
	}

	/**
	 * Gets the style which defined on this element itself. This method will try to
	 * resolve the style.
	 * 
	 * @param module the module
	 * @return style element. Null if the style is not defined on this element
	 *         itself.
	 * 
	 */
	private StyleElement getStyle(Module module) {
		getLocalProperty(module, (PropertyDefn) getMemberDefn(STYLE_MEMBER));
		if (style == null)
			return null;

		return (StyleElement) style.getElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.elements.structures.StyleRule#
	 * setIntrinsicProperty(java.lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (STYLE_MEMBER.equals(propName)) {
			if (value instanceof String)
				style = new ElementRefValue(StringUtil.extractNamespace((String) value),
						StringUtil.extractName((String) value));
			else if (value instanceof StyleElement)
				style = new ElementRefValue(null, (Style) value);
			else
				style = (ElementRefValue) value;
		} else
			super.setIntrinsicProperty(propName, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IPropertySet#getProperty(org.eclipse
	 * .birt.report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	public Object getProperty(Module module, PropertyDefn propDefn) {
		Object value = getLocalProperty(module, propDefn);
		if (value != null)
			return value;

		StyleElement styleElement = getStyle(module);

		if (styleElement != null) {
			ElementPropertyDefn newPropDefn = styleElement.getPropertyDefn(propDefn.getName());
			if (newPropDefn != null && newPropDefn.isStyleProperty())
				value = styleElement.getLocalProperty(module, newPropDefn);
		}

		if (value != null)
			return value;

		return propDefn.getDefault();
	}

	/**
	 * Sets the style property. If it is a valid style and highlight rule has no
	 * local values, values on the style are returned.
	 * 
	 * @param styleElement the style
	 */

	public void setStyle(StyleHandle styleElement) {
		DesignElement element = styleElement == null ? null : styleElement.getElement();

		setProperty(HighlightRule.STYLE_MEMBER, element);
	}

	/**
	 * Returns the style that the highlight rule links with.
	 * 
	 * @return the style
	 */

	public StyleHandle getStyle() {
		if (style == null || !style.isResolved())
			return null;

		Style styleElement = (Style) style.getElement();
		Module root = styleElement.getRoot();
		return (StyleHandle) styleElement.getHandle(root);
	}
}
