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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.elements.structures.HighlightRule;

/**
 * Represents a highlight rule in the highlight property of a style. A highlight
 * rule gives a set of conditional style properties along with a condition for
 * when to apply the properties. A highlight can be defined in either a shared
 * style or a private style.
 * 
 * @see ColorHandle
 * @see DimensionHandle
 * @see FontHandle
 * @see org.eclipse.birt.report.model.elements.structures.HighlightRule
 * @see org.eclipse.birt.report.model.elements.DesignChoiceConstants
 */

public class HighlightRuleHandle extends StyleRuleHandle
{

	/**
	 * Constructs a highlight rule handle with the given
	 * <code>SimpleValueHandle</code> and the index of the highlight rule in
	 * the highlight.
	 * 
	 * @param valueHandle
	 *            handle to a list property or member
	 * @param index
	 *            index of the structure within the list
	 *  
	 */

	public HighlightRuleHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Returns a handle to work with the color property.
	 * 
	 * @return a ColorHandle to deal with the color.
	 */

	public ColorHandle getColor( )
	{
		return doGetColorHandle( HighlightRule.COLOR_MEMBER );
	}

	/**
	 * Returns a handle to work with the background color.
	 * 
	 * @return a ColorHandle to deal with the background color.
	 */

	public ColorHandle getBackgroundColor( )
	{
		return doGetColorHandle( HighlightRule.BACKGROUND_COLOR_MEMBER );
	}

	/**
	 * Returns a handle to work with the border top color.
	 * 
	 * @return a ColorHandle to deal with the border top color.
	 */

	public ColorHandle getBorderTopColor( )
	{
		return doGetColorHandle( HighlightRule.BORDER_TOP_COLOR_MEMBER );
	}

	/**
	 * Returns a handle to work with the border left color.
	 * 
	 * @return a ColorHandle to deal with the border left color.
	 */

	public ColorHandle getBorderLeftColor( )
	{
		return doGetColorHandle( HighlightRule.BORDER_LEFT_COLOR_MEMBER );
	}

	/**
	 * Returns a handle to work with the border right color.
	 * 
	 * @return a ColorHandle to deal with the border right color.
	 */

	public ColorHandle getBorderRightColor( )
	{
		return doGetColorHandle( HighlightRule.BORDER_RIGHT_COLOR_MEMBER );
	}

	/**
	 * Returns a handle to work with the border bottom color.
	 * 
	 * @return a ColorHandle to deal with the border bottom color.
	 */

	public ColorHandle getBorderBottomColor( )
	{
		return doGetColorHandle( HighlightRule.BORDER_BOTTOM_COLOR_MEMBER );
	}

	/**
	 * Returns a color handle for a given member.
	 * 
	 * @param memberName
	 *            the member name
	 * 
	 * @return a ColorHandle for the given member
	 */

	private ColorHandle doGetColorHandle( String memberName )
	{
		MemberRef memberRef = new MemberRef( structRef, memberName );
		return new ColorHandle( getElementHandle( ), memberRef );
	}

	/**
	 * Returns the style of the border bottom line. The return value is one of
	 * the CSS (pre-defined) values see <code>DesignChoiceConstants</code>.
	 * They are:
	 * 
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

	public String getBorderBottomStyle( )
	{
		return getStringProperty( HighlightRule.BORDER_BOTTOM_STYLE_MEMBER );
	}

	/**
	 * Sets the style of the border bottom line. The input value is one of the
	 * CSS (pre-defined) values see <code>DesignChoiceConstants</code>.
	 * 
	 * @param value
	 *            the new border bottom line style
	 * @throws SemanticException
	 *             if the value is not one of above.
	 * @see #getBorderBottomStyle( )
	 */

	public void setBorderBottomStyle( String value ) throws SemanticException
	{
		setProperty( HighlightRule.BORDER_BOTTOM_STYLE_MEMBER, value );
	}

	/**
	 * Returns the style of the border left line.
	 * 
	 * @return the border left line style
	 * @see #getBorderBottomStyle( )
	 */

	public String getBorderLeftStyle( )
	{
		return getStringProperty( HighlightRule.BORDER_LEFT_STYLE_MEMBER );

	}

	/**
	 * Sets the style of the border left line.
	 * 
	 * @param value
	 *            the new border left line style
	 * @throws SemanticException
	 *             if the value is not one of above.
	 * @see #setBorderBottomStyle( String )
	 */

	public void setBorderLeftStyle( String value ) throws SemanticException
	{
		setProperty( HighlightRule.BORDER_LEFT_STYLE_MEMBER, value );
	}

	/**
	 * Returns the style of the border right line.
	 * 
	 * @return the border right line style
	 * @see #getBorderBottomStyle( )
	 */

	public String getBorderRightStyle( )
	{
		return getStringProperty( HighlightRule.BORDER_RIGHT_STYLE_MEMBER );
	}

	/**
	 * Sets the style of the border right line.
	 * 
	 * @param value
	 *            the new border right line style
	 * @throws SemanticException
	 *             if the value is not one of above.
	 * @see #setBorderBottomStyle( String )
	 */

	public void setBorderRightStyle( String value ) throws SemanticException
	{
		setProperty( HighlightRule.BORDER_RIGHT_STYLE_MEMBER, value );
	}

	/**
	 * Returns the style of the top line of the border.
	 * 
	 * @return the border top line style
	 * @see #getBorderBottomStyle( )
	 */

	public String getBorderTopStyle( )
	{
		return getStringProperty( HighlightRule.BORDER_TOP_STYLE_MEMBER );

	}

	/**
	 * Sets the style of the top line of the border.
	 * 
	 * @param value
	 *            the new border top line style
	 * @throws SemanticException
	 *             if the value is not one of above.
	 * @see #setBorderBottomStyle( String )
	 */

	public void setBorderTopStyle( String value ) throws SemanticException
	{
		setProperty( HighlightRule.BORDER_TOP_STYLE_MEMBER, value );
	}

	/**
	 * Returns the operator of the highlight rule. The returned value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>MAP_OPERATOR_EQ</code>
	 * <li><code>MAP_OPERATOR_NE</code>
	 * <li><code>MAP_OPERATOR_LT</code>
	 * <li><code>MAP_OPERATOR_LE</code>
	 * <li><code>MAP_OPERATOR_GE</code>
	 * <li><code>MAP_OPERATOR_GT</code>
	 * <li><code>MAP_OPERATOR_BETWEEN</code>
	 * <li><code>MAP_OPERATOR_NOT_BETWEEN</code>
	 * <li><code>MAP_OPERATOR_NULL</code>
	 * <li><code>MAP_OPERATOR_NOT_NULL</code>
	 * <li><code>MAP_OPERATOR_TRUE</code>
	 * <li><code>MAP_OPERATOR_FALSE</code>
	 * <li><code>MAP_OPERATOR_LIKE</code>
	 * <li><code>MAP_OPERATOR_ANY</code>
	 * </ul>
	 * 
	 * @return the operator
	 */

	public String getOperator( )
	{
		return getStringProperty( HighlightRule.OPERATOR_MEMBER );

	}

	/**
	 * Sets the operator of the highlight. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * @param value
	 *            the new operator
	 * @throws SemanticException
	 *             if the value is not one of above.
	 * @see #getOperator( )
	 */

	public void setOperator( String value ) throws SemanticException
	{
		setProperty( HighlightRule.OPERATOR_MEMBER, value );
	}

	/**
	 * Returns the value1 of the highlight. Value1 is the value for simple
	 * conditions with the operators: <, <=, =, <>, >=, >, between, not between,
	 * like.
	 * 
	 * @return the value1 as a string
	 * @see #setValue1(String)
	 */

	public String getValue1( )
	{
		return getStringProperty( HighlightRule.VALUE1_MEMBER );
	}

	/**
	 * Sets the value1 of the highlight. Value1 is the value for simple
	 * conditions with the operators: <, <=, =, <>, >=, >, between, not between,
	 * like.
	 * 
	 * @param value
	 *            the new value1.
	 */

	public void setValue1( String value )
	{
		setPropertySilently( HighlightRule.VALUE1_MEMBER, value );
	}

    
	/**
	 * Sets the value2 of the highlight rule. Value2 is the value for simple
	 * conditions with the operators: between, not between.
	 * 
	 * @param value
	 *            the new value2.
	 */

	public void setValue2( String value )
	{
		setPropertySilently( HighlightRule.VALUE2_MEMBER, value );
	}

    
	/**
	 * Returns the value2 of the highlight rule. Value2 is the value for simple
	 * conditions with the operators: between, not between.
	 * 
	 * @return the value2 as a string
	 */

	public String getValue2( )
	{
		return getStringProperty( HighlightRule.VALUE2_MEMBER );
	}

    
	/**
	 * Returns the value of the underline property. The returned value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>TEXT_UNDERLINE_NONE</code>
	 * <li><code>TEXT_UNDERLINE_UNDERLINE</code>
	 * </ul>
	 * 
	 * @return the value of the underline property
	 */

	public String getTextUnderline( )
	{
		return getStringProperty( HighlightRule.TEXT_UNDERLINE_MEMBER );
	}

    
	/**
	 * Sets the text underline property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>TEXT_UNDERLINE_NONE</code>
	 * <li><code>TEXT_UNDERLINE_UNDERLINE</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new text underline
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setTextUnderline( String value ) throws SemanticException
	{
		setProperty( HighlightRule.TEXT_UNDERLINE_MEMBER, value );
	}

    
	/**
	 * Returns the value of the overline property. The returned value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>TEXT_OVERLINE_NONE</code>
	 * <li><code>TEXT_OVERLINE_OVERLINE</code>
	 * </ul>
	 * 
	 * @return the value of the overline property
	 */

	public String getTextOverline( )
	{
		return getStringProperty( HighlightRule.TEXT_OVERLINE_MEMBER );
	}

    
	/**
	 * Sets the text overline property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>TEXT_OVERLINE_NONE</code>
	 * <li><code>TEXT_OVERLINE_OVERLINE</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new text overline
	 * @throws SemanticException
	 *             if the value is not one of the above
	 */

	public void setTextOverline( String value ) throws SemanticException
	{
		setProperty( HighlightRule.TEXT_OVERLINE_MEMBER, value );
	}

    
    /**
	 * Returns the value of the line through property. The returned value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>TEXT_LINE_THROUGH_NONE</code>
	 * <li><code>TEXT_LINE_THROUGH_LINE_THROUGH</code>
	 * </ul>
	 * 
	 * @return the text line through
	 */

	public String getTextLineThrough( )
	{
		return getStringProperty( HighlightRule.TEXT_LINE_THROUGH_MEMBER );
	}

    
	/**
	 * Sets the text line through property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>TEXT_LINE_THROUGH_NONE</code>
	 * <li><code>TEXT_LINE_THROUGH_LINE_THROUGH</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new text line through
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setTextLineThrough( String value ) throws SemanticException
	{
		setProperty( HighlightRule.TEXT_LINE_THROUGH_MEMBER, value );
	}

    
	/**
	 * Returns the value of text align property. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>TEXT_ALIGN_LEFT</code>
	 * <li><code>TEXT_ALIGN_CENTER</code>
	 * <li><code>TEXT_ALIGN_RIGHT</code>
	 * <li><code>TEXT_ALIGN_JUSTIFY</code>
	 * </ul>
	 * 
	 * 
	 * @return the value of text align property
	 */

	public String getTextAlign( )
	{
		return getStringProperty( HighlightRule.TEXT_ALIGN_MEMBER );
	}

    
	/**
	 * Sets the text align property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>TEXT_ALIGN_LEFT</code>
	 * <li><code>TEXT_ALIGN_CENTER</code>
	 * <li><code>TEXT_ALIGN_RIGHT</code>
	 * <li><code>TEXT_ALIGN_JUSTIFY</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new text align value
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setTextAlign( String value ) throws SemanticException
	{
		setProperty( HighlightRule.TEXT_ALIGN_MEMBER, value );
	}

    
	/**
	 * Returns the value of the text transform property. The return value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>TRANSFORM_CAPITALIZE</code>
	 * <li><code>TRANSFORM_UPPERCASE</code>
	 * <li><code>TRANSFORM_LOWERCASE</code>
	 * <li><code>TRANSFORM_NONE</code>
	 * </ul>
	 * 
	 * 
	 * @return the value of the transform property
	 */

	public String getTextTransform( )
	{
		return getStringProperty( HighlightRule.TEXT_TRANSFORM_MEMBER );
	}

    
	/**
	 * Sets the text transform property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>TRANSFORM_CAPITALIZE</code>
	 * <li><code>TRANSFORM_UPPERCASE</code>
	 * <li><code>TRANSFORM_LOWERCASE</code>
	 * <li><code>TRANSFORM_NONE</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new text transform
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setTextTransform( String value ) throws SemanticException
	{
		setProperty( HighlightRule.TEXT_TRANSFORM_MEMBER, value );
	}

    
	/**
	 * Gets a handle to deal with the value of the text-indent property.
	 * 
	 * @return a DimensionHandle to deal with the text-indent.
	 */

	public DimensionHandle getTextIndent( )
	{
		return doGetDimensionHandle( HighlightRule.TEXT_INDENT_MEMBER );
	}

    
	/**
	 * Returns the value of the date-time-format property.
	 * 
	 * @return the date-time-format as a string
	 */

	public String getDateTimeFormat( )
	{
		return getStringProperty( HighlightRule.DATE_TIME_FORMAT_MEMBER );
	}

    
	/**
	 * Sets the value of the date-time-format property.
	 * 
	 * @param value
	 *            the new text transform
	 */

	public void setDateTimeFormat( String value )
	{
		setPropertySilently( HighlightRule.DATE_TIME_FORMAT_MEMBER, value );
	}

    
	/**
	 * Returns the value of the number-format member.
	 * 
	 * @return the value of the number-format member
	 */

	public String getNumberFormat( )
	{
		return getStringProperty( HighlightRule.NUMBER_FORMAT_MEMBER );
	}

    
	/**
	 * Sets the value of the date-time-format member.
	 * 
	 * @param value
	 *            the new number-format value
	 */

	public void setNumberFormat( String value )
	{
		setPropertySilently( HighlightRule.NUMBER_FORMAT_MEMBER, value );
	}

	/**
	 * Returns the value of the string format
	 * 
	 * @return the value of the string format member
	 */

	public String getStringFormat( )
	{
		return getStringProperty( HighlightRule.STRING_FORMAT_MEMBER );
	}

	/**
	 * Sets the value of the string-format member.
	 * 
	 * @param value
	 *            the new string-format
	 */

	public void setStringFormat( String value )
	{
		setPropertySilently( HighlightRule.STRING_FORMAT_MEMBER, value );
	}

	/**
	 * Returns the value of the number-align member.
	 * 
	 * @return the number-align value
	 */

	public String getNumberAlign( )
	{
		return getStringProperty( HighlightRule.NUMBER_ALIGN_MEMBER );
	}

	/**
	 * Sets the value of the number-align member
	 * 
	 * @param value
	 *            the new number-align value.
	 */

	public void setNumberAlign( String value )
	{
		setPropertySilently( HighlightRule.NUMBER_ALIGN_MEMBER, value );
	}

	/**
	 * Returns a handle to work with the width of the top side of the border.
	 * 
	 * @return a DimensionHandle to deal with the width of the top side of the
	 *         border.
	 */

	public DimensionHandle getBorderTopWidth( )
	{
		return doGetDimensionHandle( HighlightRule.BORDER_TOP_WIDTH_MEMBER );
	}

	/**
	 * Returns a handle to work with the width of the left side of the border.
	 * 
	 * @return a DimensionHandle to deal with the width of the left side of the
	 *         border.
	 */

	public DimensionHandle getBorderLeftWidth( )
	{
		return doGetDimensionHandle( HighlightRule.BORDER_LEFT_WIDTH_MEMBER );
	}

	/**
	 * Returns a handle to work with the width of the right side of the border.
	 * 
	 * @return DimensionHandle to deal with the width of the right side of the
	 *         border.
	 */

	public DimensionHandle getBorderRightWidth( )
	{
		return doGetDimensionHandle( HighlightRule.BORDER_RIGHT_WIDTH_MEMBER );
	}

	/**
	 * Returns a handle to work with the width of the bottom side of the border.
	 * 
	 * @return a DimensionHandle to deal with the width of the bottom side of
	 *         the border.
	 */

	public DimensionHandle getBorderBottomWidth( )
	{
		return doGetDimensionHandle( HighlightRule.BORDER_BOTTOM_WIDTH_MEMBER );
	}

	/**
	 * Returns a handle to work with the font size.
	 * 
	 * @return a aDimensionHandle to deal with the font size.
	 */

	public DimensionHandle getFontSize( )
	{
		return doGetDimensionHandle( HighlightRule.FONT_SIZE_MEMBER );
	}

	/**
	 * Returns a dimension handle for a member.
	 * 
	 * @param memberName
	 *            the member name.
	 * 
	 * @return A DimensionHandle for the given member.
	 */

	private DimensionHandle doGetDimensionHandle( String memberName )
	{
		MemberRef memberRef = new MemberRef( structRef, memberName );
		return new DimensionHandle( getElementHandle( ), memberRef );
	}

	/**
	 * Returns the font family handle of the highlight rule.
	 * 
	 * @return the font family handle of the highlight rule.
	 */

	public FontHandle getFontFamilyHandle( )
	{
		MemberRef memberRef = new MemberRef( structRef,
				HighlightRule.FONT_FAMILY_MEMBER );
		return new FontHandle( getElementHandle( ), memberRef );

	}

	/**
	 * Returns the font weight of the highlight rule. The return value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * 
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

	public String getFontWeight( )
	{
		return getStringProperty( HighlightRule.FONT_WEIGHT_MEMBER );
	}

	/**
	 * Sets the font weight in a string for the style. The input value is
	 * defined in <code>DesignChoiceConstants</code>.
	 * 
	 * @param value
	 *            the new font weight
	 * @throws SemanticException
	 *             if the input value is not one of the above
	 * @see #getFontWeight( )
	 */

	public void setFontWeight( String value ) throws SemanticException
	{
		setProperty( HighlightRule.FONT_WEIGHT_MEMBER, value );
	}

	/**
	 * Returns the font variant in a string. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>FONT_VARIANT_NORMAL</code>
	 * <li><code>FONT_VARIANT_SMALL_CAPS</code>
	 * </ul>
	 * 
	 * @return the font variant in a string.
	 */

	public String getFontVariant( )
	{
		return getStringProperty( HighlightRule.FONT_VARIANT_MEMBER );
	}

	/**
	 * Sets the font variant in a string . The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>FONT_VARIANT_NORMAL</code>
	 * <li><code>FONT_VARIANT_SMALL_CAPS</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new font variant.
	 * @throws SemanticException
	 *             if the input value is not one of the above
	 */

	public void setFontVariant( String value ) throws SemanticException
	{
		setProperty( HighlightRule.FONT_VARIANT_MEMBER, value );
	}

	/**
	 * Returns the font style handle for the style. The return value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>FONT_STYLE_NORMAL</code>
	 * <li><code>FONT_STYLE_ITALIC</code>
	 * <li><code>FONT_STYLE_OBLIQUE</code>
	 * </ul>
	 * 
	 * @return the font style in string.
	 */

	public String getFontStyle( )
	{
		return getStringProperty( HighlightRule.FONT_STYLE_MEMBER );
	}

	/**
	 * Sets the font style in a string for the style. The input value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>FONT_STYLE_NORMAL</code>
	 * <li><code>FONT_STYLE_ITALIC</code>
	 * <li><code>FONT_STYLE_OBLIQUE</code>
	 * </ul>
	 * 
	 * @param value
	 *            the new font style.
	 * @throws SemanticException
	 *             if the input value is not one of the above
	 */

	public void setFontStyle( String value ) throws SemanticException
	{
		setProperty( HighlightRule.FONT_STYLE_MEMBER, value );
	}
}