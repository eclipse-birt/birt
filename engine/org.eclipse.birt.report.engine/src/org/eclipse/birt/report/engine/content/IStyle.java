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

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.engine.ir.HighlightDesign;
import org.eclipse.birt.report.engine.ir.MapDesign;

/**
 * Provides the interfaces for the ROM style
 * 
 * 
 * @version $Revision$ $Date$
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
	public String getStyleProperty( String styleName );

	public HighlightDesign getHighlight( );

	public MapDesign getMap( );

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

	public String getStringFormat( );

	public String getNumberFormat( );

	public String getNumberAlign( );

	public String getDateTimeFormat( );

	public String getMasterPage( );

	public String getShowIfBlank( );

	public String getCanShrink( );

	public NumberFormatter getNumberFormatObject( );

	public DateFormatter getDateFormatObject( );

	public StringFormatter getStringFormatObject( );

	public String getName( );

	public boolean isSameStyle( Object o );

	public boolean isEmpty( );

}