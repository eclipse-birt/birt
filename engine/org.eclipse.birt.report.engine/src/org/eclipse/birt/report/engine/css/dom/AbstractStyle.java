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

package org.eclipse.birt.report.engine.css.dom;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.BIRTCSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSValue;

abstract public class AbstractStyle implements IStyle
{

	protected CSSEngine engine;

	public AbstractStyle( )
	{
		engine = BIRTCSSEngine.getInstance( );
	}

	public AbstractStyle( CSSEngine engine )
	{
		this.engine = engine;
	}

	public void setProperties( IStyle style )
	{
		for ( int i = 0; i < NUMBER_OF_STYLE; i++ )
		{
			CSSValue v = style.getProperty( i );
			if ( v != null )
			{
				setProperty( i, v );
			}
		}
	}

	public String getCssText( )
	{
		StringBuffer sb = new StringBuffer( );

		for ( int i = 0; i < NUMBER_OF_STYLE; i++ )
		{
			CSSValue value = getProperty( i );
			if ( value != null )
			{
				sb.append( engine.getPropertyName( i ) );
				sb.append( ": " );
				sb.append( value.getCssText( ) );
				sb.append( "; " );
			}
		}
		if ( sb.length( ) > 2 )
		{
			sb.setLength( sb.length( ) - 2 );
		}
		return sb.toString( );
	}

	public void setCssText( String cssText ) throws DOMException
	{

		throw new DOMException( DOMException.NOT_SUPPORTED_ERR, "setCssText" );
	}

	protected int getPropertyIndex( String propertyName )
	{
		return engine.getPropertyIndex( propertyName );
	}

	public String getPropertyValue( String propertyName )
	{
		int index = getPropertyIndex( propertyName );
		if ( index != -1 )
		{
			return getCssText( index );
		}
		return null;
	}

	public CSSValue getPropertyCSSValue( String propertyName )
	{
		int index = getPropertyIndex( propertyName );
		if ( index != -1 )
		{
			return getProperty( index );
		}
		return null;
	}

	public String removeProperty( String propertyName ) throws DOMException
	{
		int index = getPropertyIndex( propertyName );
		if ( index != -1 )
		{
			setProperty( index, null );
		}
		return null;
	}

	public String getPropertyPriority( String propertyName )
	{
		throw new DOMException( DOMException.NOT_SUPPORTED_ERR, "setCssText" );
	}

	public void setProperty( String propertyName, String value, String priority )
			throws DOMException
	{
		int index = getPropertyIndex( propertyName );
		if ( index != -1 )
		{
			setCssText( index, value );
		}
	}

	public int getLength( )
	{
		throw new DOMException( DOMException.NOT_SUPPORTED_ERR, "getLength" );
	}

	public String item( int index )
	{
		throw new DOMException( DOMException.NOT_SUPPORTED_ERR, "item" );
	}

	public CSSRule getParentRule( )
	{
		throw new DOMException( DOMException.NOT_SUPPORTED_ERR, "getParentRule" );
	}

	public String getCssText( int index )
	{
		CSSValue value = getProperty( index );
		if ( value != null )
		{
			return value.getCssText( );
		}
		return null;
	}

	public void setCssText( int index, String cssText )
	{
		if ( cssText != null )
		{
			CSSValue value = engine.parsePropertyValue( index, cssText );
			setProperty( index, value );
		}
		else
		{
			setProperty( index, null );
		}
	}

	public String getFontFamily( )
	{
		return getCssText( STYLE_FONT_FAMILY );
	}

	public String getFontStyle( )
	{
		return getCssText( STYLE_FONT_STYLE );
	}

	public String getFontVariant( )
	{
		return getCssText( STYLE_FONT_VARIANT );
	}

	public String getFontWeight( )
	{
		return getCssText( STYLE_FONT_WEIGHT );
	}

	public String getFontSize( )
	{
		return getCssText( STYLE_FONT_SIZE );
	}

	public String getColor( )
	{
		return getCssText( STYLE_COLOR );
	}

	public String getBackgroundColor( )
	{
		return getCssText( STYLE_BACKGROUND_COLOR );
	}

	public String getBackgroundImage( )
	{
		return getCssText( STYLE_BACKGROUND_IMAGE );
	}

	public String getBackgroundRepeat( )
	{
		return getCssText( STYLE_BACKGROUND_REPEAT );
	}

	public String getBackgroundAttachment( )
	{
		return getCssText( STYLE_BACKGROUND_ATTACHMENT );
	}

	public String getBackgroundPositionX( )
	{
		return getCssText( STYLE_BACKGROUND_POSITION_X );
	}

	public String getBackgroundPositionY( )
	{
		return getCssText( STYLE_BACKGROUND_POSITION_Y );
	}

	public String getWordSpacing( )
	{
		return getCssText( STYLE_WORD_SPACING );
	}

	public String getLetterSpacing( )
	{
		return getCssText( STYLE_LETTER_SPACING );
	}

	public String getTextUnderline( )
	{
		return getCssText( STYLE_TEXT_UNDERLINE );
	}

	public String getTextOverline( )
	{
		return getCssText( STYLE_TEXT_OVERLINE );
	}

	public String getTextLineThrough( )
	{
		return getCssText( STYLE_TEXT_LINETHROUGH );
	}

	public String getVerticalAlign( )
	{
		return getCssText( STYLE_VERTICAL_ALIGN );
	}

	public String getTextTransform( )
	{
		return getCssText( STYLE_TEXT_TRANSFORM );
	}

	public String getTextAlign( )
	{
		return getCssText( STYLE_TEXT_ALIGN );
	}

	public String getTextIndent( )
	{
		return getCssText( STYLE_TEXT_INDENT );
	}

	public String getLineHeight( )
	{
		return getCssText( STYLE_LINE_HEIGHT );
	}

	public String getWhiteSpace( )
	{
		return getCssText( STYLE_WHITE_SPACE );
	}

	public String getMarginTop( )
	{
		return getCssText( STYLE_MARGIN_TOP );
	}

	public String getMarginBottom( )
	{
		return getCssText( STYLE_MARGIN_BOTTOM );
	}

	public String getMarginLeft( )
	{
		return getCssText( STYLE_MARGIN_LEFT );
	}

	public String getMarginRight( )
	{
		return getCssText( STYLE_MARGIN_RIGHT );
	}

	public String getPaddingTop( )
	{
		return getCssText( STYLE_PADDING_TOP );
	}

	public String getPaddingBottom( )
	{
		return getCssText( STYLE_PADDING_BOTTOM );
	}

	public String getPaddingLeft( )
	{
		return getCssText( STYLE_PADDING_LEFT );
	}

	public String getPaddingRight( )
	{
		return getCssText( STYLE_PADDING_RIGHT );
	}

	public String getBorderTopWidth( )
	{
		return getCssText( STYLE_BORDER_TOP_WIDTH );
	}

	public String getBorderBottomWidth( )
	{
		return getCssText( STYLE_BORDER_BOTTOM_WIDTH );
	}

	public String getBorderLeftWidth( )
	{
		return getCssText( STYLE_BORDER_LEFT_WIDTH );
	}

	public String getBorderRightWidth( )
	{
		return getCssText( STYLE_BORDER_RIGHT_WIDTH );
	}

	public String getBorderTopColor( )
	{
		return getCssText( STYLE_BORDER_TOP_COLOR );
	}

	public String getBorderBottomColor( )
	{
		return getCssText( STYLE_BORDER_BOTTOM_COLOR );
	}

	public String getBorderLeftColor( )
	{
		return getCssText( STYLE_BORDER_LEFT_COLOR );
	}

	public String getBorderRightColor( )
	{
		return getCssText( STYLE_BORDER_RIGHT_COLOR );
	}

	public String getBorderTopStyle( )
	{
		return getCssText( STYLE_BORDER_TOP_STYLE );
	}

	public String getBorderBottomStyle( )
	{
		return getCssText( STYLE_BORDER_BOTTOM_STYLE );
	}

	public String getBorderLeftStyle( )
	{
		return getCssText( STYLE_BORDER_LEFT_STYLE );
	}

	public String getBorderRightStyle( )
	{
		return getCssText( STYLE_BORDER_RIGHT_STYLE );
	}

	public String getDisplay( )
	{
		return getCssText( STYLE_DISPLAY );
	}

	public String getOrphans( )
	{
		return getCssText( STYLE_ORPHANS );
	}

	public String getWidows( )
	{
		return getCssText( STYLE_WIDOWS );
	}

	public String getPageBreakAfter( )
	{
		return getCssText( STYLE_PAGE_BREAK_AFTER );
	}

	public String getPageBreakBefore( )
	{
		return getCssText( STYLE_PAGE_BREAK_BEFORE );
	}

	public String getPageBreakInside( )
	{
		return getCssText( STYLE_PAGE_BREAK_INSIDE );
	}

	public String getMasterPage( )
	{
		return getCssText( STYLE_MASTER_PAGE );
	}

	public String getShowIfBlank( )
	{
		return getCssText( STYLE_SHOW_IF_BLANK );
	}

	public String getCanShrink( )
	{
		return getCssText( STYLE_CAN_SHRINK );
	}

	public String getVisibleFormat( )
	{
		return getCssText( STYLE_VISIBLE_FORMAT );
	}

	/**
	 * @param backgroundAttachment
	 *            The backgroundAttachment to set.
	 */
	public void setBackgroundAttachment( String backgroundAttachment )
	{
		setCssText( STYLE_BACKGROUND_ATTACHMENT, backgroundAttachment );
	}

	/**
	 * @param backgroundColor
	 *            The backgroundColor to set.
	 */
	public void setBackgroundColor( String backgroundColor )
	{
		setCssText( STYLE_BACKGROUND_COLOR, backgroundColor );
	}

	/**
	 * @param backgroundImage
	 *            The backgroundImage to set.
	 */
	public void setBackgroundImage( String backgroundImage )
	{
		setCssText( STYLE_BACKGROUND_IMAGE, backgroundImage );
	}

	/**
	 * @param backgroundPositionX
	 *            The backgroundPositionX to set.
	 */
	public void setBackgroundPositionX( String backgroundPositionX )
	{
		setCssText( STYLE_BACKGROUND_POSITION_X, backgroundPositionX );
	}

	/**
	 * @param backgroundPositionY
	 *            The backgroundPositionY to set.
	 */
	public void setBackgroundPositionY( String backgroundPositionY )
	{
		setCssText( STYLE_BACKGROUND_POSITION_Y, backgroundPositionY );
	}

	/**
	 * @param backgroundRepeat
	 *            The backgroundRepeat to set.
	 */
	public void setBackgroundRepeat( String backgroundRepeat )
	{
		setCssText( STYLE_BACKGROUND_REPEAT, backgroundRepeat );
	}

	/**
	 * @param borderBottomColor
	 *            The borderBottomColor to set.
	 */
	public void setBorderBottomColor( String borderBottomColor )
	{
		setCssText( STYLE_BORDER_BOTTOM_COLOR, borderBottomColor );
	}

	/**
	 * @param borderBottomStyle
	 *            The borderBottomStyle to set.
	 */
	public void setBorderBottomStyle( String borderBottomStyle )
	{
		setCssText( STYLE_BORDER_BOTTOM_STYLE, borderBottomStyle );
	}

	/**
	 * @param borderBottomWidth
	 *            The borderBottomWidth to set.
	 */
	public void setBorderBottomWidth( String borderBottomWidth )
	{
		setCssText( STYLE_BORDER_BOTTOM_WIDTH, borderBottomWidth );
	}

	/**
	 * @param borderLeftColor
	 *            The borderLeftColor to set.
	 */
	public void setBorderLeftColor( String borderLeftColor )
	{
		setCssText( STYLE_BORDER_LEFT_COLOR, borderLeftColor );
	}

	/**
	 * @param borderLeftStyle
	 *            The borderLeftStyle to set.
	 */
	public void setBorderLeftStyle( String borderLeftStyle )
	{
		setCssText( STYLE_BORDER_LEFT_STYLE, borderLeftStyle );
	}

	/**
	 * @param borderLeftWidth
	 *            The borderLeftWidth to set.
	 */
	public void setBorderLeftWidth( String borderLeftWidth )
	{
		setCssText( STYLE_BORDER_LEFT_WIDTH, borderLeftWidth );
	}

	/**
	 * @param borderRightColor
	 *            The borderRightColor to set.
	 */
	public void setBorderRightColor( String borderRightColor )
	{
		setCssText( STYLE_BORDER_RIGHT_COLOR, borderRightColor );
	}

	/**
	 * @param borderRightStyle
	 *            The borderRightStyle to set.
	 */
	public void setBorderRightStyle( String borderRightStyle )
	{
		setCssText( STYLE_BORDER_RIGHT_STYLE, borderRightStyle );
	}

	/**
	 * @param borderRightWidth
	 *            The borderRightWidth to set.
	 */
	public void setBorderRightWidth( String borderRightWidth )
	{
		setCssText( STYLE_BORDER_RIGHT_WIDTH, borderRightWidth );
	}

	/**
	 * @param borderTopColor
	 *            The borderTopColor to set.
	 */
	public void setBorderTopColor( String borderTopColor )
	{
		setCssText( STYLE_BORDER_TOP_COLOR, borderTopColor );
	}

	/**
	 * @param borderTopStyle
	 *            The borderTopStyle to set.
	 */
	public void setBorderTopStyle( String borderTopStyle )
	{
		setCssText( STYLE_BORDER_TOP_STYLE, borderTopStyle );
	}

	/**
	 * @param borderTopWidth
	 *            The borderTopWidth to set.
	 */
	public void setBorderTopWidth( String borderTopWidth )
	{
		setCssText( STYLE_BORDER_TOP_WIDTH, borderTopWidth );
	}

	/**
	 * @param canShrink
	 *            The canShrink to set.
	 */
	public void setCanShrink( String canShrink )
	{
		setCssText( STYLE_CAN_SHRINK, canShrink );
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public void setColor( String color )
	{
		setCssText( STYLE_COLOR, color );
	}

	/**
	 * @param display
	 *            The display to set.
	 */
	public void setDisplay( String display )
	{
		setCssText( STYLE_DISPLAY, display );
	}

	/**
	 * @param fontFamily
	 *            The fontFamily to set.
	 */
	public void setFontFamily( String fontFamily )
	{
		setCssText( STYLE_FONT_FAMILY, fontFamily );
	}

	/**
	 * @param fontSize
	 *            The fontSize to set.
	 */
	public void setFontSize( String fontSize )
	{
		setCssText( STYLE_FONT_SIZE, fontSize );
	}

	/**
	 * @param fontStyle
	 *            The fontStyle to set.
	 */
	public void setFontStyle( String fontStyle )
	{
		setCssText( STYLE_FONT_STYLE, fontStyle );
	}

	/**
	 * @param fontVariant
	 *            The fontVariant to set.
	 */
	public void setFontVariant( String fontVariant )
	{
		setCssText( STYLE_FONT_VARIANT, fontVariant );
	}

	/**
	 * @param fontWeight
	 *            The fontWeight to set.
	 */
	public void setFontWeight( String fontWeight )
	{
		setCssText( STYLE_FONT_WEIGHT, fontWeight );
	}

	/**
	 * @param letterSpacing
	 *            The letterSpacing to set.
	 */
	public void setLetterSpacing( String letterSpacing )
	{
		setCssText( STYLE_LETTER_SPACING, letterSpacing );
	}

	/**
	 * @param lineHeight
	 *            The lineHeight to set.
	 */
	public void setLineHeight( String lineHeight )
	{
		setCssText( STYLE_LINE_HEIGHT, lineHeight );
	}

	/**
	 * @param marginBottom
	 *            The marginBottom to set.
	 */
	public void setMarginBottom( String marginBottom )
	{
		setCssText( STYLE_MARGIN_BOTTOM, marginBottom );
	}

	/**
	 * @param marginLeft
	 *            The marginLeft to set.
	 */
	public void setMarginLeft( String marginLeft )
	{
		setCssText( STYLE_MARGIN_LEFT, marginLeft );
	}

	/**
	 * @param marginRight
	 *            The marginRight to set.
	 */
	public void setMarginRight( String marginRight )
	{
		setCssText( STYLE_MARGIN_RIGHT, marginRight );
	}

	/**
	 * @param marginTop
	 *            The marginTop to set.
	 */
	public void setMarginTop( String marginTop )
	{
		setCssText( STYLE_MARGIN_TOP, marginTop );
	}

	/**
	 * @param masterPage
	 *            The masterPage to set.
	 */
	public void setMasterPage( String masterPage )
	{
		setCssText( STYLE_MASTER_PAGE, masterPage );
	}

	/**
	 * @param orphans
	 *            The orphans to set.
	 */
	public void setOrphans( String orphans )
	{
		setCssText( STYLE_ORPHANS, orphans );
	}

	/**
	 * @param paddingBottom
	 *            The paddingBottom to set.
	 */
	public void setPaddingBottom( String paddingBottom )
	{
		setCssText( STYLE_PADDING_BOTTOM, paddingBottom );
	}

	/**
	 * @param paddingLeft
	 *            The paddingLeft to set.
	 */
	public void setPaddingLeft( String paddingLeft )
	{
		setCssText( STYLE_PADDING_LEFT, paddingLeft );
	}

	/**
	 * @param paddingRight
	 *            The paddingRight to set.
	 */
	public void setPaddingRight( String paddingRight )
	{
		setCssText( STYLE_PADDING_RIGHT, paddingRight );
	}

	/**
	 * @param paddingTop
	 *            The paddingTop to set.
	 */
	public void setPaddingTop( String paddingTop )
	{
		setCssText( STYLE_PADDING_TOP, paddingTop );
	}

	/**
	 * @param pageBreakAfter
	 *            The pageBreakAfter to set.
	 */
	public void setPageBreakAfter( String pageBreakAfter )
	{
		setCssText( STYLE_PAGE_BREAK_AFTER, pageBreakAfter );
	}

	/**
	 * @param pageBreakBefore
	 *            The pageBreakBefore to set.
	 */
	public void setPageBreakBefore( String pageBreakBefore )
	{
		setCssText( STYLE_PAGE_BREAK_BEFORE, pageBreakBefore );
	}

	/**
	 * @param pageBreakInside
	 *            The pageBreakInside to set.
	 */
	public void setPageBreakInside( String pageBreakInside )
	{
		setCssText( STYLE_PAGE_BREAK_INSIDE, pageBreakInside );
	}

	/**
	 * @param showIfBlank
	 *            The showIfBlank to set.
	 */
	public void setShowIfBlank( String showIfBlank )
	{
		setCssText( STYLE_SHOW_IF_BLANK, showIfBlank );
	}

	/**
	 * @param textAlign
	 *            The textAlign to set.
	 */
	public void setTextAlign( String textAlign )
	{
		setCssText( STYLE_TEXT_ALIGN, textAlign );
	}

	/**
	 * @param textIndent
	 *            The textIndent to set.
	 */
	public void setTextIndent( String textIndent )
	{
		setCssText( STYLE_TEXT_INDENT, textIndent );
	}

	/**
	 * @param textLineThrough
	 *            The textLineThrough to set.
	 */
	public void setTextLineThrough( String textLineThrough )
	{
		setCssText( STYLE_TEXT_LINETHROUGH, textLineThrough );
	}

	/**
	 * @param textOverline
	 *            The textOverline to set.
	 */
	public void setTextOverline( String textOverline )
	{
		setCssText( STYLE_TEXT_OVERLINE, textOverline );
	}

	/**
	 * @param textTransform
	 *            The textTransform to set.
	 */
	public void setTextTransform( String textTransform )
	{
		setCssText( STYLE_TEXT_TRANSFORM, textTransform );
	}

	/**
	 * @param textUnderline
	 *            The textUnderline to set.
	 */
	public void setTextUnderline( String textUnderline )
	{
		setCssText( STYLE_TEXT_UNDERLINE, textUnderline );
	}

	/**
	 * @param verticalAlign
	 *            The verticalAlign to set.
	 */
	public void setVerticalAlign( String verticalAlign )
	{
		setCssText( STYLE_VERTICAL_ALIGN, verticalAlign );
	}

	/**
	 * @param whiteSpace
	 *            The whiteSpace to set.
	 */
	public void setWhiteSpace( String whiteSpace )
	{
		setCssText( STYLE_WHITE_SPACE, whiteSpace );
	}

	/**
	 * @param widows
	 *            The widows to set.
	 */
	public void setWidows( String widows )
	{
		setCssText( STYLE_WIDOWS, widows );
	}

	public void setStringFormat( String format ) throws DOMException
	{
		setCssText( STYLE_STRING_FORMAT, format );
	}

	public void setNumberFormat( String format ) throws DOMException
	{
		setCssText( STYLE_NUMBER_FORMAT, format );
	}

	public void setDateFormat( String format ) throws DOMException
	{
		setCssText( STYLE_DATE_FORMAT, format );
	}

	public void setNumberAlign( String align ) throws DOMException
	{
		setCssText( STYLE_NUMBER_ALIGN, align );
	}

	public void setVisibleFormat( String formats ) throws DOMException
	{
		setCssText( STYLE_VISIBLE_FORMAT, formats );
	}

	public String getStringFormat( )
	{
		return getCssText( STYLE_STRING_FORMAT );
	}

	public String getNumberFormat( )
	{
		return getCssText( STYLE_NUMBER_FORMAT );
	}

	public String getDateFormat( )
	{
		return getCssText( STYLE_DATE_FORMAT );
	}

	public String getNumberAlign( )
	{
		return getCssText( STYLE_NUMBER_ALIGN );
	}

	// unsupported CSS2 properties
	public String getAzimuth( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setAzimuth( String azimuth ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBackground( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBackground( String background ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBackgroundPosition( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBackgroundPosition( String backgroundPosition )
			throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBorder( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBorder( String border ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBorderCollapse( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBorderCollapse( String borderCollapse ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBorderColor( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBorderColor( String borderColor ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBorderSpacing( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBorderSpacing( String borderSpacing ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBorderStyle( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBorderStyle( String borderStyle ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBorderTop( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBorderTop( String borderTop ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBorderRight( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBorderRight( String borderRight ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBorderBottom( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBorderBottom( String borderBottom ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBorderLeft( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBorderLeft( String borderLeft ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBorderWidth( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBorderWidth( String borderWidth ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getBottom( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBottom( String bottom ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getCaptionSide( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setCaptionSide( String captionSide ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getClear( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setClear( String clear ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getClip( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setClip( String clip ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getContent( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setContent( String content ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getCounterIncrement( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setCounterIncrement( String counterIncrement )
			throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getCounterReset( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setCounterReset( String counterReset ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getCue( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setCue( String cue ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getCueAfter( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setCueAfter( String cueAfter ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getCueBefore( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setCueBefore( String cueBefore ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getCursor( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setCursor( String cursor ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getDirection( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setDirection( String direction ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getElevation( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setElevation( String elevation ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getEmptyCells( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setEmptyCells( String emptyCells ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getCssFloat( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setCssFloat( String cssFloat ) throws DOMException
	{
		// TODO Auto-generated method stub

	}

	public String getFont( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setFont( String font ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getFontSizeAdjust( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setFontSizeAdjust( String fontSizeAdjust ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getFontStretch( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setFontStretch( String fontStretch ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getHeight( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setHeight( String height ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getLeft( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setLeft( String left ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getListStyle( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setListStyle( String listStyle ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getListStyleImage( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setListStyleImage( String listStyleImage ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getListStylePosition( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setListStylePosition( String listStylePosition )
			throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getListStyleType( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setListStyleType( String listStyleType ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getMargin( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setMargin( String margin ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getMarkerOffset( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setMarkerOffset( String markerOffset ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getMarks( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setMarks( String marks ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getMaxHeight( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setMaxHeight( String maxHeight ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getMaxWidth( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setMaxWidth( String maxWidth ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getMinHeight( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setMinHeight( String minHeight ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getMinWidth( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setMinWidth( String minWidth ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getOutline( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setOutline( String outline ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getOutlineColor( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setOutlineColor( String outlineColor ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getOutlineStyle( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setOutlineStyle( String outlineStyle ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getOutlineWidth( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setOutlineWidth( String outlineWidth ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getOverflow( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setOverflow( String overflow ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getPadding( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setPadding( String padding ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getPage( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setPage( String page ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getPause( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setPause( String pause ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getPauseAfter( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setPauseAfter( String pauseAfter ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getPauseBefore( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setPauseBefore( String pauseBefore ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getPitch( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setPitch( String pitch ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getPitchRange( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setPitchRange( String pitchRange ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getPlayDuring( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setPlayDuring( String playDuring ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getPosition( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setPosition( String position ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getQuotes( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setQuotes( String quotes ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getRichness( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setRichness( String richness ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getRight( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setRight( String right ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getSize( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setSize( String size ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getSpeak( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setSpeak( String speak ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getSpeakHeader( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setSpeakHeader( String speakHeader ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getSpeakNumeral( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setSpeakNumeral( String speakNumeral ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getSpeakPunctuation( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setSpeakPunctuation( String speakPunctuation )
			throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getSpeechRate( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setSpeechRate( String speechRate ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getStress( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setStress( String stress ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getTableLayout( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setTableLayout( String tableLayout ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getTextDecoration( )
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public void setTextDecoration( String textDecoration ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-decoration" );
	}

	public String getTextShadow( )
	{
		throw createUnsupportedPropertyException( "text-shadow" );
	}

	public void setTextShadow( String textShadow ) throws DOMException
	{
		throw createUnsupportedPropertyException( "text-shadow" );
	}

	public String getTop( )
	{
		throw createUnsupportedPropertyException( "top" );
	}

	public void setTop( String top ) throws DOMException
	{
		throw createUnsupportedPropertyException( "top" );
	}

	public String getUnicodeBidi( )
	{
		throw createUnsupportedPropertyException( "unicode-bidi" );
	}

	public void setUnicodeBidi( String unicodeBidi ) throws DOMException
	{
		throw createUnsupportedPropertyException( "unicode-bidi" );
	}

	public String getVisibility( )
	{
		throw createUnsupportedPropertyException( "visibility" );
	}

	public void setVisibility( String visibility ) throws DOMException
	{
		throw createUnsupportedPropertyException( "visibility" );
	}

	public String getVoiceFamily( )
	{
		throw createUnsupportedPropertyException( "voice-family" );
	}

	public void setVoiceFamily( String voiceFamily ) throws DOMException
	{
		throw createUnsupportedPropertyException( "voice-family" );
	}

	public String getVolume( )
	{
		throw createUnsupportedPropertyException( "volumn" );
	}

	public void setVolume( String volume ) throws DOMException
	{
		throw createUnsupportedPropertyException( "volumn" );
	}

	public String getWidth( )
	{
		throw createUnsupportedPropertyException( "width" );
	}

	public void setWidth( String width ) throws DOMException
	{
		throw createUnsupportedPropertyException( "width" );
	}

	public void setWordSpacing( String wordSpacing ) throws DOMException
	{
	}

	public String getZIndex( )
	{
		throw createUnsupportedPropertyException( "zindex" );
	}

	public void setZIndex( String zIndex ) throws DOMException
	{
		throw createUnsupportedPropertyException( "zindex" );
	}

	private DOMException createUnsupportedPropertyException( String property )
	{
		return new DOMException( DOMException.NOT_SUPPORTED_ERR, property );
	}
}
