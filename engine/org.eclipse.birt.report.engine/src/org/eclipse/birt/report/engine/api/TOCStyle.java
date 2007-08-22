/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;

public class TOCStyle implements IScriptStyle, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String BACKGROUND_ATTACHMENT = "background-attachment";
	public static final String BACKGROUND_COLOR = "background-color";
	public static final String BACKGROUND_IMAGE = "background-image";
	public static final String BACKGROUND_POSITION_X = "background-position-x";
	public static final String BACKGROUND_POSITION_Y = "background-position-y";
	public static final String BACKGROUND_REPEAT = "background-repeat";
	public static final String BORDER_BOTTOM_COLOR = "border-bottom-color";
	public static final String BORDER_TOP_COLOR = "border-top-color";
	public static final String BORDER_LEFT_COLOR = "border-left-color";
	public static final String BORDER_RIGHT_COLOR = "border-right-color";
	public static final String BORDER_BOTTOM_WIDTH = "border-bottom-width";
	public static final String BORDER_TOP_WIDTH = "border-top-width";
	public static final String BORDER_LEFT_WIDTH = "border-left-width";
	public static final String BORDER_RIGHT_WIDTH = "border-right-width";
	public static final String BORDER_BOTTOM_STYLE = "border-bottom-style";
	public static final String BORDER_TOP_STYLE = "border-top-style";
	public static final String BORDER_LEFT_STYLE = "border-left-style";
	public static final String BORDER_RIGHT_STYLE = "border-right-style";
	public static final String CAN_SHRINK = "can-shrink";
	public static final String COLOR = "color";
	public static final String DATE_FORMAT = "date-format";
	public static final String DISPLAY = "display";
	public static final String FONT_FAMILY = "font-family";
	public static final String FONT_SIZE = "font-size";
	public static final String FONT_STYLE = "font-style";
	public static final String FONT_VARIANT = "font-variant";
	public static final String FONT_WEIGHT = "font-weight";
	public static final String LETTER_SPACING = "letter-spacing";
	public static final String LINE_HEIGHT = "line-height";
	public static final String MARGIN_BOTTOM = "margin-bottom";
	public static final String MARGIN_LEFT = "margin-left";
	public static final String MARGIN_RIGHT = "margin-right";
	public static final String MARGIN_TOP = "margin-top";
	public static final String MASTER_PAGE = "master-page";
	public static final String NUMBER_FORMAT = "number-format";
	public static final String PADDING_BOTTOM = "padding-bottom";
	public static final String PADDING_LEFT = "padding-left";
	public static final String PADDING_RIGHT = "padding-right";
	public static final String PADDING_TOP = "padding-top";
	public static final String PAGE_BREAK_AFTER = "page-break-after";
	public static final String PAGE_BREAK_BEFORE = "page-break-before";
	public static final String PAGE_BREAK_INSIDE = "page-break-inside";
	public static final String SHOW_IF_BLANK = "show-if-blank";
	public static final String STRING_FORMAT = "string-format";
	public static final String TEXT_ALIGN = "text-align";
	public static final String TEXT_INDENT = "text-indent";
	public static final String TEXT_LINE_THROUGH = "text-line-through";
	public static final String TEXT_OVERLINE = "text-overline";
	public static final String TEXT_TRANSFORM = "text-transform";
	public static final String TEXT_UNDERLINE = "text-underline";
	public static final String VERTICAL_ALIGN = "vertical-align";
	public static final String VISIBLE_FORMAT = "visible-format";
	public static final String WHITE_SPACE = "white-space";
	public static final String WORD_SPACING = "word-spacing";

	private HashMap properties = new HashMap( );

	public String getBackgroundAttachement( )
	{
		return (String) properties.get( BACKGROUND_ATTACHMENT );
	}

	public String getBackgroundAttachment( )
	{
		return (String) properties.get( BACKGROUND_ATTACHMENT );
	}

	public String getBackgroundColor( )
	{
		return (String) properties.get( BACKGROUND_COLOR );
	}

	public String getBackgroundImage( )
	{
		return (String) properties.get( BACKGROUND_IMAGE );
	}

	public String getBackgroundPositionX( )
	{
		return (String) properties.get( BACKGROUND_POSITION_X );
	}

	public String getBackgroundPositionY( )
	{
		return (String) properties.get( BACKGROUND_POSITION_Y );
	}

	public String getBackgroundRepeat( )
	{
		return (String) properties.get( BACKGROUND_REPEAT );
	}

	public String getBorderBottomColor( )
	{
		return (String) properties.get( BORDER_BOTTOM_COLOR );
	}

	public String getBorderBottomStyle( )
	{
		return (String) properties.get( BORDER_BOTTOM_STYLE );
	}

	public String getBorderBottomWidth( )
	{
		return (String) properties.get( BORDER_BOTTOM_WIDTH );
	}

	public String getBorderLeftColor( )
	{
		return (String) properties.get( BORDER_LEFT_COLOR );
	}

	public String getBorderLeftStyle( )
	{
		return (String) properties.get( BORDER_LEFT_STYLE );
	}

	public String getBorderLeftWidth( )
	{
		return (String) properties.get( BORDER_LEFT_WIDTH );
	}

	public String getBorderRightColor( )
	{
		return (String) properties.get( BORDER_RIGHT_COLOR );
	}

	public String getBorderRightStyle( )
	{
		return (String) properties.get( BORDER_RIGHT_STYLE );
	}

	public String getBorderRightWidth( )
	{
		return (String) properties.get( BORDER_RIGHT_WIDTH );
	}

	public String getBorderTopColor( )
	{
		return (String) properties.get( BORDER_TOP_COLOR );
	}

	public String getBorderTopStyle( )
	{
		return (String) properties.get( BORDER_TOP_STYLE );
	}

	public String getBorderTopWidth( )
	{
		return (String) properties.get( BORDER_TOP_WIDTH );
	}

	public String getCanShrink( )
	{
		return (String) properties.get( CAN_SHRINK );
	}

	public String getColor( )
	{
		return (String) properties.get( COLOR );
	}

	public String getDateFormat( )
	{
		return (String) properties.get( DATE_FORMAT );
	}

	public String getDisplay( )
	{
		return (String) properties.get( DISPLAY );
	}

	public String getFontFamily( )
	{
		return (String) properties.get( FONT_FAMILY );
	}

	public String getFontSize( )
	{
		return (String) properties.get( FONT_SIZE );
	}

	public String getFontStyle( )
	{
		return (String) properties.get( FONT_STYLE );
	}

	public String getFontVariant( )
	{
		return (String) properties.get( FONT_VARIANT );
	}

	public String getFontWeight( )
	{
		return (String) properties.get( FONT_WEIGHT );
	}

	public String getLetterSpacing( )
	{
		return (String) properties.get( LETTER_SPACING );
	}

	public String getLineHeight( )
	{
		return (String) properties.get( LINE_HEIGHT );
	}

	public String getMarginBottom( )
	{
		return (String) properties.get( MARGIN_BOTTOM );
	}

	public String getMarginLeft( )
	{
		return (String) properties.get( MARGIN_LEFT );
	}

	public String getMarginRight( )
	{
		return (String) properties.get( MARGIN_RIGHT );
	}

	public String getMarginTop( )
	{
		return (String) properties.get( MARGIN_TOP );
	}

	public String getMasterPage( )
	{
		return (String) properties.get( MASTER_PAGE );
	}

	public String getNumberFormat( )
	{
		return (String) properties.get( NUMBER_FORMAT );
	}

	public String getPaddingBottom( )
	{
		return (String) properties.get( PADDING_BOTTOM );
	}

	public String getPaddingLeft( )
	{
		return (String) properties.get( PADDING_LEFT );
	}

	public String getPaddingRight( )
	{
		return (String) properties.get( PADDING_RIGHT );
	}

	public String getPaddingTop( )
	{
		return (String) properties.get( PADDING_TOP );
	}

	public String getPageBreakAfter( )
	{
		return (String) properties.get( PAGE_BREAK_AFTER );
	}

	public String getPageBreakBefore( )
	{
		return (String) properties.get( PAGE_BREAK_BEFORE );
	}

	public String getPageBreakInside( )
	{
		return (String) properties.get( PAGE_BREAK_INSIDE );
	}

	public String getShowIfBlank( )
	{
		return (String) properties.get( SHOW_IF_BLANK );
	}

	public String getStringFormat( )
	{
		return (String) properties.get( STRING_FORMAT );
	}

	public String getTextAlign( )
	{
		return (String) properties.get( TEXT_ALIGN );
	}

	public String getTextIndent( )
	{
		return (String) properties.get( TEXT_INDENT );
	}

	public String getTextLineThrough( )
	{
		return (String) properties.get( TEXT_LINE_THROUGH );
	}

	public String getTextOverline( )
	{
		return (String) properties.get( TEXT_OVERLINE );
	}

	public String getTextTransform( )
	{
		return (String) properties.get( TEXT_TRANSFORM );
	}

	public String getTextUnderline( )
	{
		return (String) properties.get( TEXT_UNDERLINE );
	}

	public String getVerticalAlign( )
	{
		return (String) properties.get( VERTICAL_ALIGN );
	}

	public String getVisibleFormat( )
	{
		return (String) properties.get( VISIBLE_FORMAT );
	}

	public String getWhiteSpace( )
	{
		return (String) properties.get( WHITE_SPACE );
	}

	public String getWordSpacing( )
	{
		return (String) properties.get( WORD_SPACING );
	}

	public void setBackgroundAttachement( String attachement )
	{
		setProperty( BACKGROUND_ATTACHMENT, attachement );
	}

	public void setBackgroundAttachment( String attachment )
	{
		setProperty( BACKGROUND_ATTACHMENT, attachment );
	}

	public void setBackgroundColor( String color )
	{
		setProperty( BACKGROUND_COLOR, color );
	}

	public void setBackgroundImage( String imageURI )
	{
		setProperty( BACKGROUND_IMAGE, imageURI );
	}

	public void setBackgroundPositionX( String x ) throws ScriptException
	{
		setProperty( BACKGROUND_POSITION_X, x );
	}

	public void setBackgroundPositionY( String y ) throws ScriptException
	{
		setProperty( BACKGROUND_POSITION_Y, y );
	}

	public void setBackgroundRepeat( String repeat )
	{
		setProperty( BACKGROUND_REPEAT, repeat );
	}

	public void setBorderBottomColor( String color )
	{
		setProperty( BORDER_BOTTOM_COLOR, color );
	}

	public void setBorderBottomStyle( String borderstyle )
	{
		setProperty( BORDER_BOTTOM_STYLE, borderstyle );
	}

	public void setBorderBottomWidth( String width )
	{
		setProperty( BORDER_BOTTOM_WIDTH, width );
	}

	public void setBorderLeftColor( String color )
	{
		setProperty( BORDER_LEFT_COLOR, color );
	}

	public void setBorderLeftStyle( String borderstyle )
	{
		setProperty( BORDER_LEFT_STYLE, borderstyle );
	}

	public void setBorderLeftWidth( String width )
	{
		setProperty( BORDER_LEFT_WIDTH, width );
	}

	public void setBorderRightColor( String color )
	{
		setProperty( BORDER_RIGHT_COLOR, color );
	}

	public void setBorderRightStyle( String borderstyle )
	{
		setProperty( BORDER_RIGHT_STYLE, borderstyle );
	}

	public void setBorderRightWidth( String width )
	{
		setProperty( BORDER_RIGHT_WIDTH, width );
	}

	public void setBorderTopColor( String color )
	{
		setProperty( BORDER_TOP_COLOR, color );
	}

	public void setBorderTopStyle( String borderstyle )
	{
		setProperty( BORDER_TOP_STYLE, borderstyle );
	}

	public void setBorderTopWidth( String width )
	{
		setProperty( BORDER_TOP_WIDTH, width );
	}

	public void setCanShrink( String canShrink )
	{
		setProperty( CAN_SHRINK, canShrink );
	}

	public void setColor( String color )
	{
		setProperty( COLOR, color );
	}

	public void setDateFormat( String dateTimeFormat )
	{
		setProperty( DATE_FORMAT, dateTimeFormat );
	}

	public void setDisplay( String display )
	{
		setProperty( DISPLAY, display );
	}

	public void setFontFamily( String fontFamily )
	{
		setProperty( FONT_FAMILY, fontFamily );
	}

	public void setFontSize( String fontSize )
	{
		setProperty( FONT_SIZE, fontSize );
	}

	public void setFontStyle( String fontStyle )
	{
		setProperty( FONT_STYLE, fontStyle );
	}

	public void setFontVariant( String fontVariant )
	{
		setProperty( FONT_VARIANT, fontVariant );
	}

	public void setFontWeight( String fontWeight )
	{
		setProperty( FONT_WEIGHT, fontWeight );
	}

	public void setLetterSpacing( String spacing )
	{
		setProperty( LETTER_SPACING, spacing );
	}

	public void setLineHeight( String lineHeight )
	{
		setProperty( LINE_HEIGHT, lineHeight );
	}

	public void setMarginBottom( String margin )
	{
		setProperty( MARGIN_BOTTOM, margin );
	}

	public void setMarginLeft( String margin )
	{
		setProperty( MARGIN_LEFT, margin );
	}

	public void setMarginRight( String margin )
	{
		setProperty( MARGIN_RIGHT, margin );
	}

	public void setMarginTop( String margin )
	{
		setProperty( MARGIN_TOP, margin );
	}

	public void setMasterPage( String masterPage )
	{
		setProperty( MASTER_PAGE, masterPage );
	}

	public void setNumberFormat( String numberFormat )
	{
		setProperty( NUMBER_FORMAT, numberFormat );
	}

	public void setPaddingBottom( String padding )
	{
		setProperty( PADDING_BOTTOM, padding );
	}

	public void setPaddingLeft( String padding )
	{
		setProperty( PADDING_LEFT, padding );
	}

	public void setPaddingRight( String padding )
	{
		setProperty( PADDING_RIGHT, padding );
	}

	public void setPaddingTop( String padding )
	{
		setProperty( PADDING_TOP, padding );
	}

	public void setPageBreakAfter( String pageBreak )
	{
		setProperty( PAGE_BREAK_AFTER, pageBreak );
	}

	public void setPageBreakBefore( String pageBreak )
	{
		setProperty( PAGE_BREAK_BEFORE, pageBreak );
	}

	public void setPageBreakInside( String pageBreak )
	{
		setProperty( PAGE_BREAK_INSIDE, pageBreak );
	}

	public void setShowIfBlank( String showIfBlank )
	{
		setProperty( SHOW_IF_BLANK, showIfBlank );
	}

	public void setStringFormat( String stringFormat )
	{
		setProperty( STRING_FORMAT, stringFormat );
	}

	public void setTextAlign( String align )
	{
		setProperty( TEXT_ALIGN, align );
	}

	public void setTextIndent( String indent )
	{
		setProperty( TEXT_INDENT, indent );
	}

	public void setTextLineThrough( String through ) throws ScriptException
	{
		setProperty( TEXT_LINE_THROUGH, through );
	}

	public void setTextOverline( String overline ) throws ScriptException
	{
		setProperty( TEXT_OVERLINE, overline );
	}

	public void setTextTransform( String transform )
	{
		setProperty( TEXT_TRANSFORM, transform );
	}

	public void setTextUnderline( String underline ) throws ScriptException
	{
		setProperty( TEXT_UNDERLINE, underline );
	}

	public void setVerticalAlign( String valign )
	{
		setProperty( VERTICAL_ALIGN, valign );
	}

	public void setVisibleFormat( String format )
	{
		setProperty( VISIBLE_FORMAT, format );
	}

	public void setWhiteSpace( String whitespace )
	{
		setProperty( WHITE_SPACE, whitespace );
	}

	public void setWordSpacing( String wordspacing )
	{
		setProperty( WORD_SPACING, wordspacing );
	}

	public void setProperty( String name, String value )
	{
		if ( value != null )
		{
			properties.put( name, value );
		}
		else
		{
			properties.remove( name );
		}
	}

	public Map getProperties( )
	{
		return properties;
	}
}
