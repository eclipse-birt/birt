/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IStyle;

public class Style implements IStyle
{

	private StyleHandle style;

	public Style( StyleHandle style )
	{
		this.style = style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBackgroundAttachment()
	 */

	public String getBackgroundAttachment( )
	{
		return style.getBackgroundAttachment( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBackgroundAttachment(java.lang.String)
	 */

	public void setBackgroundAttachment( String value )
			throws SemanticException
	{
		style.setBackgroundAttachment( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBackgroundImage()
	 */

	public String getBackgroundImage( )
	{
		return style.getBackgroundImage( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBackgroundImage(java.lang.String)
	 */

	public void setBackgroundImage( String value ) throws SemanticException
	{
		style.setBackgroundImage( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBackgroundRepeat()
	 */

	public String getBackgroundRepeat( )
	{
		return style.getBackgroundRepeat( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBackgroundRepeat(java.lang.String)
	 */

	public void setBackgroundRepeat( String value ) throws SemanticException
	{
		style.setBackgroundRepeat( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderBottomStyle()
	 */

	public String getBorderBottomStyle( )
	{
		return style.getBorderBottomStyle( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderBottomStyle(java.lang.String)
	 */

	public void setBorderBottomStyle( String value ) throws SemanticException
	{
		style.setBorderBottomStyle( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderLeftStyle()
	 */

	public String getBorderLeftStyle( )
	{
		return style.getBorderLeftStyle( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderLeftStyle(java.lang.String)
	 */

	public void setBorderLeftStyle( String value ) throws SemanticException
	{
		style.setBorderLeftStyle( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderRightStyle()
	 */

	public String getBorderRightStyle( )
	{
		return style.getBorderRightStyle( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderRightStyle(java.lang.String)
	 */

	public void setBorderRightStyle( String value ) throws SemanticException
	{
		style.setBorderRightStyle( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderTopStyle()
	 */

	public String getBorderTopStyle( )
	{
		return style.getBorderTopStyle( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderTopStyle(java.lang.String)
	 */

	public void setBorderTopStyle( String value ) throws SemanticException
	{
		style.setBorderTopStyle( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#canShrink()
	 */

	public boolean canShrink( )
	{
		return style.canShrink( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setCanShrink(boolean)
	 */

	public void setCanShrink( boolean value ) throws SemanticException
	{
		style.setCanShrink( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getStringFormat()
	 */

	public String getStringFormat( )
	{
		return style.getStringFormat( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getStringFormatCategory()
	 */

	public String getStringFormatCategory( )
	{
		return style.getStringFormatCategory( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setStringFormat(java.lang.String)
	 */

	public void setStringFormat( String pattern ) throws SemanticException
	{
		style.setStringFormat( pattern );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setStringFormatCategory(java.lang.String)
	 */

	public void setStringFormatCategory( String pattern )
			throws SemanticException
	{
		style.setStringFormatCategory( pattern );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getNumberFormat()
	 */

	public String getNumberFormat( )
	{
		return style.getNumberFormat( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getNumberFormatCategory()
	 */

	public String getNumberFormatCategory( )
	{
		return style.getNumberFormatCategory( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setNumberFormat(java.lang.String)
	 */

	public void setNumberFormat( String pattern ) throws SemanticException
	{
		style.setNumberFormat( pattern );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setNumberFormatCategory(java.lang.String)
	 */

	public void setNumberFormatCategory( String category )
			throws SemanticException
	{
		style.setNumberFormatCategory( category );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getDateTimeFormat()
	 */

	public String getDateTimeFormat( )
	{
		return style.getDateTimeFormat( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getDateTimeFormatCategory()
	 */

	public String getDateTimeFormatCategory( )
	{
		return style.getDateTimeFormatCategory( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setDateTimeFormat(java.lang.String)
	 */

	public void setDateTimeFormat( String pattern ) throws SemanticException
	{
		style.setDateTimeFormat( pattern );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setDateTimeFormatCategory(java.lang.String)
	 */

	public void setDateTimeFormatCategory( String pattern )
			throws SemanticException
	{
		style.setDateTimeFormatCategory( pattern );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getDisplay()
	 */

	public String getDisplay( )
	{
		return style.getDisplay( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setDisplay(java.lang.String)
	 */

	public void setDisplay( String value ) throws SemanticException
	{
		style.setDisplay( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getMasterPage()
	 */

	public String getMasterPage( )
	{
		return style.getMasterPage( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setMasterPage(java.lang.String)
	 */

	public void setMasterPage( String value ) throws SemanticException
	{
		style.setMasterPage( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getOrphans()
	 */

	public String getOrphans( )
	{
		return style.getOrphans( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setOrphans(java.lang.String)
	 */

	public void setOrphans( String value ) throws SemanticException
	{
		style.setOrphans( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getPageBreakAfter()
	 */

	public String getPageBreakAfter( )
	{
		return style.getPageBreakAfter( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setPageBreakAfter(java.lang.String)
	 */

	public void setPageBreakAfter( String value ) throws SemanticException
	{
		style.setPageBreakAfter( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getPageBreakBefore()
	 */

	public String getPageBreakBefore( )
	{
		return style.getPageBreakBefore( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setPageBreakBefore(java.lang.String)
	 */

	public void setPageBreakBefore( String value ) throws SemanticException
	{
		style.setPageBreakBefore( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getPageBreakInside()
	 */

	public String getPageBreakInside( )
	{
		return style.getPageBreakInside( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setPageBreakInside(java.lang.String)
	 */

	public void setPageBreakInside( String value ) throws SemanticException
	{
		style.setPageBreakInside( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#showIfBlank()
	 */

	public boolean getShowIfBlank( )
	{
		return style.showIfBlank( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setShowIfBlank(boolean)
	 */

	public void setShowIfBlank( boolean value ) throws SemanticException
	{
		style.setShowIfBlank( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextUnderline()
	 */

	public String getTextUnderline( )
	{
		return style.getTextUnderline( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextUnderline(java.lang.String)
	 */

	public void setTextUnderline( String value ) throws SemanticException
	{
		style.setTextUnderline( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextOverline()
	 */

	public String getTextOverline( )
	{
		return style.getTextOverline( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextOverline(java.lang.String)
	 */

	public void setTextOverline( String value ) throws SemanticException
	{
		style.setTextOverline( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextLineThrough()
	 */

	public String getTextLineThrough( )
	{
		return style.getTextLineThrough( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextLineThrough(java.lang.String)
	 */

	public void setTextLineThrough( String value ) throws SemanticException
	{
		style.setTextLineThrough( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextAlign()
	 */

	public String getTextAlign( )
	{
		return style.getTextAlign( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextAlign(java.lang.String)
	 */

	public void setTextAlign( String value ) throws SemanticException
	{
		style.setTextAlign( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextTransform()
	 */

	public String getTextTransform( )
	{
		return style.getTextTransform( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextTransform(java.lang.String)
	 */

	public void setTextTransform( String value ) throws SemanticException
	{
		style.setTextTransform( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getVerticalAlign()
	 */

	public String getVerticalAlign( )
	{
		return style.getVerticalAlign( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setVerticalAlign(java.lang.String)
	 */

	public void setVerticalAlign( String value ) throws SemanticException
	{
		style.setVerticalAlign( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getWhiteSpace()
	 */

	public String getWhiteSpace( )
	{
		return style.getWhiteSpace( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setWhiteSpace(java.lang.String)
	 */

	public void setWhiteSpace( String value ) throws SemanticException
	{
		style.setWhiteSpace( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getWidows()
	 */

	public String getWidows( )
	{
		return style.getWidows( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setWidows(java.lang.String)
	 */

	public void setWidows( String value ) throws SemanticException
	{
		style.setWidows( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getColor()
	 */

	public String getColor( )
	{
		return style.getColor( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setColor(java.lang.String)
	 */
	public void setColor( String color ) throws SemanticException
	{
		style.getColor( ).setValue( color );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBackgroundColor()
	 */

	public String getBackgroundColor( )
	{
		return style.getBackgroundColor( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBackgroundColor(java.lang.String)
	 */
	public void setBackgroundColor( String color ) throws SemanticException
	{
		style.getBackgroundColor( ).setValue( color );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderTopColor()
	 */

	public String getBorderTopColor( )
	{
		return style.getBorderTopColor( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderTopColor(java.lang.String)
	 */
	public void setBorderTopColor( String color ) throws SemanticException
	{
		style.getBorderTopColor( ).setValue( color );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderLeftColor()
	 */
	public String getBorderLeftColor( )
	{
		return style.getBorderLeftColor( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderLeftColor(java.lang.String)
	 */
	public void setBorderLeftColor( String color ) throws SemanticException
	{

		style.getBorderLeftColor( ).setValue( color );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderRightColor()
	 */

	public String getBorderRightColor( )
	{
		return style.getBorderRightColor( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderRightColor(java.lang.String)
	 */
	public void setBorderRightColor( String color ) throws SemanticException
	{
		style.getBorderRightColor( ).setValue( color );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderBottomColor()
	 */

	public String getBorderBottomColor( )
	{
		return style.getBorderBottomColor( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderBottomColor(java.lang.String)
	 */
	public void setBorderBottomColor( String color ) throws SemanticException
	{
		style.getBorderBottomColor( ).setValue( color );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBackGroundPositionX()
	 */

	public String getBackGroundPositionX( )
	{
		return style.getBackGroundPositionX( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBackGroundPositionX(java.lang.String)
	 */
	public void setBackGroundPositionX( String x ) throws SemanticException
	{
		style.getBackGroundPositionX( ).setValue( x );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBackGroundPositionY()
	 */

	public String getBackGroundPositionY( )
	{
		return style.getBackGroundPositionY( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBackGroundPositionY(java.lang.String)
	 */
	public void setBackGroundPositionY( String y ) throws SemanticException
	{
		style.getBackGroundPositionY( ).setValue( y );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getLetterSpacing()
	 */

	public String getLetterSpacing( )
	{
		return style.getLetterSpacing( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setLetterSpacing(java.lang.String)
	 */
	public void setLetterSpacing( String spacing ) throws SemanticException
	{

		style.getLetterSpacing( ).setValue( spacing );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getLineHeight()
	 */

	public String getLineHeight( )
	{
		return style.getLineHeight( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setLineHeight(java.lang.String)
	 */
	public void setLineHeight( String height ) throws SemanticException
	{
		style.getLineHeight( ).setValue( height );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextIndent()
	 */

	public String getTextIndent( )
	{
		return style.getTextIndent( ).getStringValue( );
	}

	public void setTextIndent( String indent ) throws SemanticException
	{
		style.getTextIndent( ).setValue( indent );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getWordSpacing()
	 */

	public String getWordSpacing( )
	{
		return style.getWordSpacing( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setWordSpacing(java.lang.String)
	 */
	public void setWordSpacing( String spacing ) throws SemanticException
	{
		style.getWordSpacing( ).setValue( spacing );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderTopWidth()
	 */

	public String getBorderTopWidth( )
	{
		return style.getBorderTopWidth( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderTopWidth(java.lang.String)
	 */
	public void setBorderTopWidth( String width ) throws SemanticException
	{
		style.getBorderTopWidth( ).setValue( width );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderLeftWidth()
	 */

	public String getBorderLeftWidth( )
	{
		return style.getBorderLeftWidth( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderLeftWidth(java.lang.String)
	 */
	public void setBorderLeftWidth( String width ) throws SemanticException
	{
		style.getBorderLeftWidth( ).setValue( width );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderRightWidth()
	 */

	public String getBorderRightWidth( )
	{
		return style.getBorderRightWidth( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderRightWidth(java.lang.String)
	 */
	public void setBorderRightWidth( String width ) throws SemanticException
	{
		style.getBorderRightWidth( ).setValue( width );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderBottomWidth()
	 */

	public String getBorderBottomWidth( )
	{
		return style.getBorderBottomWidth( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderBottomWidth(java.lang.String)
	 */
	public void setBorderBottomWidth( String width ) throws SemanticException
	{
		style.getBorderBottomWidth( ).setValue( width );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getMarginTop()
	 */

	public String getMarginTop( )
	{
		return style.getMarginTop( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginTop(java.lang.String)
	 */
	public void setMarginTop( String margin ) throws SemanticException
	{
		style.getMarginTop( ).setValue( margin );
	}

	public String getMarginRight( )
	{
		return style.getMarginRight( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginRight(java.lang.String)
	 */
	public void setMarginRight( String margin ) throws SemanticException
	{
		style.getMarginRight( ).setValue( margin );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getMarginLeft()
	 */

	public String getMarginLeft( )
	{
		return style.getMarginLeft( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginLeft(margin)
	 */
	public void setMarginLeft( String margin ) throws SemanticException
	{

		style.getMarginLeft( ).setValue( margin );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getMarginBottom()
	 */

	public String getMarginBottom( )
	{
		return style.getMarginBottom( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginBottom(java.lang.String)
	 */
	public void setMarginBottom( String margin ) throws SemanticException
	{
		style.getMarginBottom( ).setValue( margin );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getPaddingTop()
	 */

	public String getPaddingTop( )
	{
		return style.getPaddingTop( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingTop(java.lang.String)
	 */
	public void setPaddingTop( String padding ) throws SemanticException
	{
		style.getPaddingTop( ).setValue( padding );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getPaddingRight()
	 */

	public String getPaddingRight( )
	{
		return style.getPaddingRight( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingRight(java.lang.String)
	 */
	public void setPaddingRight( String padding ) throws SemanticException
	{
		style.getPaddingRight( ).setValue( padding );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getPaddingLeft()
	 */

	public String getPaddingLeft( )
	{
		return style.getPaddingLeft( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingLeft(java.lang.String)
	 */
	public void setPaddingLeft( String padding ) throws SemanticException
	{
		style.getPaddingLeft( ).setValue( padding );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getPaddingBottom()
	 */

	public String getPaddingBottom( )
	{
		return style.getPaddingBottom( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingBottom(java.lang.String)
	 */
	public void setPaddingBottom( String padding ) throws SemanticException
	{
		style.getPaddingBottom( ).setValue( padding );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontSize()
	 */

	public String getFontSize( )
	{
		return style.getFontSize( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontSize(java.lang.String)
	 */
	public void setFontSize( String fontSize ) throws SemanticException
	{
		style.getFontSize( ).setValue( fontSize );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontFamily()
	 */

	public String getFontFamily( )
	{
		return style.getFontFamilyHandle( ).getStringValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontFamily(java.lang.String)
	 */
	public void setFontFamily( String fontFamily ) throws SemanticException
	{
		style.getFontFamilyHandle( ).setValue( fontFamily );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontWeight()
	 */

	public String getFontWeight( )
	{
		return style.getFontWeight( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontWeight(java.lang.String)
	 */

	public void setFontWeight( String fontWeight ) throws SemanticException
	{
		style.setFontWeight( fontWeight );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontVariant()
	 */

	public String getFontVariant( )
	{
		return style.getFontVariant( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontVariant(java.lang.String)
	 */

	public void setFontVariant( String fontVariant ) throws SemanticException
	{
		style.setFontVariant( fontVariant );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontStyle()
	 */

	public String getFontStyle( )
	{
		return style.getFontStyle( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontStyle(java.lang.String)
	 */

	public void setFontStyle( String fontStyle ) throws SemanticException
	{
		style.setFontStyle( fontStyle );
	}
}
