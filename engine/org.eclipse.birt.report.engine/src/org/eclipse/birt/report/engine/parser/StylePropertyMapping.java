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

package org.eclipse.birt.report.engine.parser;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Style is used to store the style properties.
 * 
 */
public class StylePropertyMapping
{
	
	protected static final HashMap nameMapping = new HashMap( );
	
	static
	{
		nameMapping.put( IStyleModel.FONT_FAMILY_PROP, new Integer( StyleConstants.STYLE_FONT_FAMILY ) );
		nameMapping.put( IStyleModel.FONT_STYLE_PROP, new Integer( StyleConstants.STYLE_FONT_STYLE ) );
		nameMapping.put( IStyleModel.FONT_VARIANT_PROP, new Integer( StyleConstants.STYLE_FONT_VARIANT ) );
		nameMapping.put( IStyleModel.FONT_WEIGHT_PROP, new Integer( StyleConstants.STYLE_FONT_WEIGHT ) );
		nameMapping.put( IStyleModel.FONT_SIZE_PROP, new Integer( StyleConstants.STYLE_FONT_SIZE ) );
		nameMapping.put( IStyleModel.COLOR_PROP, new Integer( StyleConstants.STYLE_COLOR ) );
		nameMapping.put( IStyleModel.BACKGROUND_COLOR_PROP, new Integer( StyleConstants.STYLE_BACKGROUND_COLOR ) );
		nameMapping.put( IStyleModel.BACKGROUND_IMAGE_PROP, new Integer( StyleConstants.STYLE_BACKGROUND_IMAGE ) );
		nameMapping.put( IStyleModel.BACKGROUND_REPEAT_PROP, new Integer( StyleConstants.STYLE_BACKGROUND_REPEAT ) );
		nameMapping.put( IStyleModel.BACKGROUND_ATTACHMENT_PROP, new Integer( StyleConstants.STYLE_BACKGROUND_ATTACHMENT ) );
		nameMapping.put( IStyleModel.BACKGROUND_POSITION_X_PROP, new Integer( StyleConstants.STYLE_BACKGROUND_POSITION_X ) );
		nameMapping.put( IStyleModel.BACKGROUND_POSITION_Y_PROP, new Integer( StyleConstants.STYLE_BACKGROUND_POSITION_Y ) );
		nameMapping.put( IStyleModel.WORD_SPACING_PROP, new Integer( StyleConstants.STYLE_WORD_SPACING ) );
		nameMapping.put( IStyleModel.LETTER_SPACING_PROP, new Integer( StyleConstants.STYLE_LETTER_SPACING ) );
		nameMapping.put( IStyleModel.TEXT_UNDERLINE_PROP, new Integer( StyleConstants.STYLE_TEXT_UNDERLINE ) );
		nameMapping.put( IStyleModel.TEXT_OVERLINE_PROP, new Integer( StyleConstants.STYLE_TEXT_OVERLINE ) );
		nameMapping.put( IStyleModel.TEXT_LINE_THROUGH_PROP, new Integer( StyleConstants.STYLE_TEXT_LINETHROUGH ) );
		nameMapping.put( IStyleModel.VERTICAL_ALIGN_PROP, new Integer( StyleConstants.STYLE_VERTICAL_ALIGN ) );
		nameMapping.put( IStyleModel.TEXT_TRANSFORM_PROP, new Integer( StyleConstants.STYLE_TEXT_TRANSFORM ) );
		nameMapping.put( IStyleModel.TEXT_ALIGN_PROP, new Integer( StyleConstants.STYLE_TEXT_ALIGN ) );
		nameMapping.put( IStyleModel.TEXT_INDENT_PROP, new Integer( StyleConstants.STYLE_TEXT_INDENT ) );
		nameMapping.put( IStyleModel.LINE_HEIGHT_PROP, new Integer( StyleConstants.STYLE_LINE_HEIGHT ) );
		nameMapping.put( IStyleModel.WHITE_SPACE_PROP, new Integer( StyleConstants.STYLE_WHITE_SPACE ) );
		nameMapping.put( IStyleModel.MARGIN_TOP_PROP, new Integer( StyleConstants.STYLE_MARGIN_TOP ) );
		nameMapping.put( IStyleModel.MARGIN_BOTTOM_PROP, new Integer( StyleConstants.STYLE_MARGIN_BOTTOM ) );
		nameMapping.put( IStyleModel.MARGIN_LEFT_PROP, new Integer( StyleConstants.STYLE_MARGIN_LEFT ) );
		nameMapping.put( IStyleModel.MARGIN_RIGHT_PROP, new Integer( StyleConstants.STYLE_MARGIN_RIGHT ) );
		nameMapping.put( IStyleModel.PADDING_TOP_PROP, new Integer( StyleConstants.STYLE_PADDING_TOP ) );
		nameMapping.put( IStyleModel.PADDING_BOTTOM_PROP, new Integer( StyleConstants.STYLE_PADDING_BOTTOM ) );
		nameMapping.put( IStyleModel.PADDING_LEFT_PROP, new Integer( StyleConstants.STYLE_PADDING_LEFT ) );
		nameMapping.put( IStyleModel.PADDING_RIGHT_PROP, new Integer( StyleConstants.STYLE_PADDING_RIGHT ) );
		nameMapping.put( IStyleModel.BORDER_TOP_WIDTH_PROP, new Integer( StyleConstants.STYLE_BORDER_TOP_WIDTH ) );
		nameMapping.put( IStyleModel.BORDER_BOTTOM_WIDTH_PROP, new Integer( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) );
		nameMapping.put( IStyleModel.BORDER_LEFT_WIDTH_PROP, new Integer( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) );
		nameMapping.put( IStyleModel.BORDER_RIGHT_WIDTH_PROP, new Integer( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) );
		nameMapping.put( IStyleModel.BORDER_TOP_COLOR_PROP, new Integer( StyleConstants.STYLE_BORDER_TOP_COLOR ) );
		nameMapping.put( IStyleModel.BORDER_BOTTOM_COLOR_PROP, new Integer( StyleConstants.STYLE_BORDER_BOTTOM_COLOR ) );
		nameMapping.put( IStyleModel.BORDER_LEFT_COLOR_PROP, new Integer( StyleConstants.STYLE_BORDER_LEFT_COLOR ) );
		nameMapping.put( IStyleModel.BORDER_RIGHT_COLOR_PROP, new Integer( StyleConstants.STYLE_BORDER_RIGHT_COLOR ) );
		nameMapping.put( IStyleModel.BORDER_TOP_STYLE_PROP, new Integer( StyleConstants.STYLE_BORDER_TOP_STYLE ) );
		nameMapping.put( IStyleModel.BORDER_BOTTOM_STYLE_PROP, new Integer( StyleConstants.STYLE_BORDER_BOTTOM_STYLE ) );
		nameMapping.put( IStyleModel.BORDER_LEFT_STYLE_PROP, new Integer( StyleConstants.STYLE_BORDER_LEFT_STYLE ) );
		nameMapping.put( IStyleModel.BORDER_RIGHT_STYLE_PROP, new Integer( StyleConstants.STYLE_BORDER_RIGHT_STYLE ) );
		nameMapping.put( IStyleModel.DISPLAY_PROP, new Integer( StyleConstants.STYLE_DISPLAY ) );
		nameMapping.put( IStyleModel.ORPHANS_PROP, new Integer( StyleConstants.STYLE_ORPHANS ) );
		nameMapping.put( IStyleModel.WIDOWS_PROP, new Integer( StyleConstants.STYLE_WIDOWS ) );
		nameMapping.put( IStyleModel.PAGE_BREAK_AFTER_PROP, new Integer( StyleConstants.STYLE_PAGE_BREAK_AFTER ) );
		nameMapping.put( IStyleModel.PAGE_BREAK_BEFORE_PROP, new Integer( StyleConstants.STYLE_PAGE_BREAK_BEFORE ) );
		nameMapping.put( IStyleModel.PAGE_BREAK_INSIDE_PROP, new Integer( StyleConstants.STYLE_PAGE_BREAK_INSIDE ) );
		nameMapping.put( IStyleModel.MASTER_PAGE_PROP, new Integer( StyleConstants.STYLE_MASTER_PAGE ) );
		nameMapping.put( IStyleModel.SHOW_IF_BLANK_PROP, new Integer( StyleConstants.STYLE_SHOW_IF_BLANK ) );
		nameMapping.put( IStyleModel.CAN_SHRINK_PROP, new Integer( StyleConstants.STYLE_CAN_SHRINK ) );
		nameMapping.put( IStyleModel.NUMBER_FORMAT_PROP, new Integer( StyleConstants.STYLE_NUMBER_FORMAT ) );
		nameMapping.put( IStyleModel.STRING_FORMAT_PROP, new Integer( StyleConstants.STYLE_STRING_FORMAT ) );
		nameMapping.put( IStyleModel.DATE_TIME_FORMAT_PROP, new Integer( StyleConstants.STYLE_DATE_FORMAT ) );
		nameMapping.put( IStyleModel.NUMBER_ALIGN_PROP, new Integer( StyleConstants.STYLE_NUMBER_ALIGN ) );

		// bidi_hcg: Add Bidi text direction.
		nameMapping.put( IStyleModel.TEXT_DIRECTION_PROP, new Integer(
				StyleConstants.STYLE_DIRECTION ) );
	}

	protected static IElementDefn styleDefn;

	/**
	 * get the style element definition.
	 * 
	 * @return style definition.
	 *  
	 */
	protected static IElementDefn getStyleDefn( )
	{
		if ( styleDefn != null )
		{
			return styleDefn;
		}
		styleDefn = MetaDataDictionary.getInstance( ).getElement(
				ReportDesignConstants.STYLE_ELEMENT );
		assert styleDefn != null;
		return styleDefn;
	}

	/**
	 * if the property is inheritable.
	 * 
	 * @param name
	 *            property name
	 * @return true: can inherit, false: can't inherit.
	 */
	public static boolean canInherit( String name )
	{
		IElementPropertyDefn defn = getStyleDefn( ).getProperty( name );
		if ( defn != null )
		{
			return defn.canInherit( );
		}
		return false;
	}

	public static Object getDefaultValue( String name )
	{
		IElementPropertyDefn defn = getStyleDefn( ).getProperty( name );
		if ( defn != null )
		{
			return defn.getDefault( );
		}
		return null;
	}

	/**
	 * @param name
	 * @param handle
	 * @return
	 * @author bidi_hcg
	 */
	public static Object getDefaultValue( String name, ReportDesignHandle handle )
	{
		Object value = getDefaultValue( name );

		if ( value == null && handle != null )
		{
			if ( IStyleModel.TEXT_ALIGN_PROP.equals( name ) )
			{
				return handle.isDirectionRTL( ) ? DesignChoiceConstants.TEXT_ALIGN_RIGHT
						: null;
			}
			if ( IStyleModel.TEXT_DIRECTION_PROP.equals( name ) )
				return handle.isDirectionRTL( )
						? DesignChoiceConstants.BIDI_DIRECTION_RTL
						: DesignChoiceConstants.BIDI_DIRECTION_LTR;
		}
		return value;
	}

	public static Object getDefaultValue( int index )
	{
		return getDefaultValue( nameMapping.get( new Integer( index ) ).toString( ) );
	}

	public static int getPropertyID( String name )
	{
		int id = -1;
		Object obj = nameMapping.get( name );
		if( obj instanceof Integer )
		{
			id = ( ( Integer ) obj ).intValue( );
		}
		return id;
	}

	public static Set getPropertyMapping( )
	{
		return nameMapping.entrySet( );
	}


}