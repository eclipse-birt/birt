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

package org.eclipse.birt.report.engine.content;

/**
 * Provides the interfaces for the ROM style
 * 
 * 
 * @version $Revision: 1.4 $ $Date: 2005/05/08 06:59:45 $
 */
public interface IStyle
{

	/**
	 * get property.
	 * 
	 * @param name
	 *            property name
	 * @return property value, null if the property is not set.
	 */
	public String getProperty( String styleName );
	
	public void setProperty(String styleName, String value);

	public String getFontFamily( );

	public String getFontStyle( );

	public String getFontVariant( );

	public String getFontWeight( );

	public String getFontSize( );

	public String getColor( );

	public String getBackgroundColor( );

	public String getBackgroundImage( );

	public String getBackgroundRepeat( );

	public String getBackgroundAttachment( );

	public String getBackgroundPositionX( );

	public String getBackgroundPositionY( );

	public String getWordSpacing( );

	public String getLetterSpacing( );

	public String getTextUnderline( );

	public String getTextOverline( );

	public String getTextLineThrough( );

	public String getVerticalAlign( );

	public String getTextTransform( );

	public String getTextAlign( );

	public String getTextIndent( );

	public String getLineHeight( );

	public String getWhiteSpace( );

	public String getMarginTop( );

	public String getMarginBottom( );

	public String getMarginLeft( );

	public String getMarginRight( );

	public String getPaddingTop( );

	public String getPaddingBottom( );

	public String getPaddingLeft( );

	public String getPaddingRight( );

	public String getBorderTopWidth( );

	public String getBorderBottomWidth( );

	public String getBorderLeftWidth( );

	public String getBorderRightWidth( );

	public String getBorderTopColor( );

	public String getBorderBottomColor( );

	public String getBorderLeftColor( );

	public String getBorderRightColor( );

	public String getBorderTopStyle( );

	public String getBorderBottomStyle( );

	public String getBorderLeftStyle( );

	public String getBorderRightStyle( );

	public String getDisplay( );

	public String getOrphans( );

	public String getWidows( );

	public String getPageBreakAfter( );

	public String getPageBreakBefore( );

	public String getPageBreakInside( );

	public String getMasterPage( );

	public String getShowIfBlank( );

	public String getCanShrink( );
	
	public String getName( );
	
	public String getNumberAlign();
	
	public String getNumberFormat();
	
	public String getStringFormat();
	
	public String getDateTimeFormat();
	
	public void setFontFamily(String v);

	public void setFontStyle(String v);

	public void setFontVariant(String v);

	public void setFontWeight(String v);

	public void setFontSize(String v);

	public void setColor(String v);

	public void setBackgroundColor(String v);

	public void setBackgroundImage(String v);

	public void setBackgroundRepeat(String v);

	public void setBackgroundAttachment(String v);

	public void setBackgroundPositionX(String v);

	public void setBackgroundPositionY(String v);

	public void setWordSpacing(String v);

	public void setLetterSpacing(String v);

	public void setTextUnderline(String v);

	public void setTextOverline(String v);

	public void setTextLineThrough(String v);

	public void setVerticalAlign(String v);

	public void setTextTransform(String v);

	public void setTextAlign(String v);

	public void setTextIndent(String v);

	public void setLineHeight(String v);

	public void setWhiteSpace(String v);

	public void setMarginTop(String v);

	public void setMarginBottom(String v);

	public void setMarginLeft(String v);

	public void setMarginRight(String v);

	public void setPaddingTop(String v);

	public void setPaddingBottom(String v);

	public void setPaddingLeft(String v);

	public void setPaddingRight(String v);

	public void setBorderTopWidth(String v);

	public void setBorderBottomWidth(String v);

	public void setBorderLeftWidth(String v);

	public void setBorderRightWidth(String v);

	public void setBorderTopColor(String v);

	public void setBorderBottomColor(String v);

	public void setBorderLeftColor(String v);

	public void setBorderRightColor(String v);

	public void setBorderTopStyle(String v);

	public void setBorderBottomStyle(String v);

	public void setBorderLeftStyle(String v);

	public void setBorderRightStyle(String v);

	public void setDisplay(String v);

	public void setOrphans(String v);

	public void setWidows(String v);

	public void setPageBreakAfter(String v);

	public void setPageBreakBefore(String v);

	public void setPageBreakInside(String v);

	public void setMasterPage(String v);

	public void setShowIfBlank(String v);

	public void setCanShrink(String v);
	
	public void setName(String v);
	
	public void setNumberAlign(String v);
	
	public void setNumberFormat(String v);
	
	public void setStringFormat(String v);
	
	public void setDateTimeFormat(String v);


	public boolean isSameStyle( Object o );

	public boolean isEmpty( );

}