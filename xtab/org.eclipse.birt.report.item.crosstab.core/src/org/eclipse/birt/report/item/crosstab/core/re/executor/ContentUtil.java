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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.model.api.FactoryPropertyHandle;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TOCHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Style;

/**
 * ContentUtil
 */
class ContentUtil
{

	/**
	 * Prevent from instantiation
	 */
	private ContentUtil( )
	{
	}

	static void processStyle( IExecutorContext context, IContent content,
			AbstractCrosstabItemHandle handle )
	{
		ReportElementHandle modelHandle = getReportElementHandle( handle );

		if ( modelHandle == null || modelHandle.getPrivateStyle( ) == null )
		{
			return;
		}

		IStyle style = context.getReportContent( ).createStyle( );
		setupPrivateStyle( modelHandle, style );
		content.setInlineStyle( style );
	}

	static void processVisibility( IExecutorContext context, IContent content,
			AbstractCrosstabItemHandle handle, IResultSet evaluator )
	{
		ReportItemHandle modelHandle = getReportItemHandle( handle );

		if ( modelHandle == null || evaluator == null )
		{
			return;
		}

		Iterator visItr = modelHandle.visibilityRulesIterator( );

		if ( visItr != null && visItr.hasNext( ) )
		{
			StringBuffer buffer = new StringBuffer( );

			while ( visItr.hasNext( ) )
			{
				HideRuleHandle rule = (HideRuleHandle) visItr.next( );

				String expr = validExpression( rule.getExpression( ) );

				Object result = null;
				if ( expr != null )
				{
					result = evaluator.evaluate( expr );
				}

				if ( result == null || !( result instanceof Boolean ) )
				{
					continue;
				}

				boolean isHidden = ( (Boolean) result ).booleanValue( );
				if ( !isHidden )
				{
					continue;
				}

				// we should use rule as the string as
				buffer.append( rule.getFormat( ) ).append( ", " ); //$NON-NLS-1$
			}

			int len = buffer.length( );
			if ( len > 2 )
			{
				buffer.delete( len - 2, len );
			}
			content.getStyle( ).setVisibleFormat( buffer.toString( ) );
		}

	}

	static void processBookmark( IExecutorContext context, IContent content,
			AbstractCrosstabItemHandle handle, IResultSet evaluator )
	{
		ReportItemHandle modelHandle = getReportItemHandle( handle );

		if ( modelHandle == null || evaluator == null )
		{
			return;
		}

		String bookmark = modelHandle.getBookmark( );
		if ( bookmark != null )
		{
			Object tmp = evaluator.evaluate( validExpression( bookmark ) );
			if ( tmp != null && !tmp.equals( "" ) ) //$NON-NLS-1$
			{
				content.setBookmark( tmp.toString( ) );
			}
		}

		TOCHandle toc = modelHandle.getTOC( );
		if ( toc != null )
		{
			Object tmp = evaluator.evaluate( validExpression( toc.getExpression( ) ) );
			if ( tmp != null )
			{
				content.setTOC( tmp );
			}
		}

	}

	static void processAction( IExecutorContext context, IContent content,
			AbstractCrosstabItemHandle handle )
	{
		// TODO no action for crosstab itself?
		return;
	}

	static String validExpression( String expr )
	{
		if ( expr != null && !expr.trim( ).equals( "" ) ) //$NON-NLS-1$
		{
			return expr;
		}
		return null;
	}

	static ReportItemHandle getReportItemHandle(
			AbstractCrosstabItemHandle handle )
	{
		if ( handle != null
				&& handle.getModelHandle( ) instanceof ReportItemHandle )
		{
			return (ReportItemHandle) handle.getModelHandle( );
		}
		return null;
	}

	static ReportElementHandle getReportElementHandle(
			AbstractCrosstabItemHandle handle )
	{
		if ( handle != null
				&& handle.getModelHandle( ) instanceof ReportElementHandle )
		{
			return (ReportElementHandle) handle.getModelHandle( );
		}
		return null;
	}

	static void setupPrivateStyle( ReportElementHandle handle, IStyle style )
	{
		// Background
		style.setBackgroundColor( getElementProperty( handle,
				Style.BACKGROUND_COLOR_PROP,
				true ) );
		style.setBackgroundImage( getElementProperty( handle,
				Style.BACKGROUND_IMAGE_PROP ) );
		style.setBackgroundPositionX( getElementProperty( handle,
				Style.BACKGROUND_POSITION_X_PROP ) );
		style.setBackgroundPositionY( getElementProperty( handle,
				Style.BACKGROUND_POSITION_Y_PROP ) );
		style.setBackgroundRepeat( getElementProperty( handle,
				Style.BACKGROUND_REPEAT_PROP ) );

		// Text related
		style.setTextAlign( getElementProperty( handle, Style.TEXT_ALIGN_PROP ) );
		style.setTextIndent( getElementProperty( handle, Style.TEXT_INDENT_PROP ) );

		style.setTextUnderline( getElementProperty( handle,
				Style.TEXT_UNDERLINE_PROP ) );

		style.setTextLineThrough( getElementProperty( handle,
				Style.TEXT_LINE_THROUGH_PROP ) );
		style.setTextOverline( getElementProperty( handle,
				Style.TEXT_OVERLINE_PROP ) );

		style.setLetterSpacing( getElementProperty( handle,
				Style.LETTER_SPACING_PROP ) );
		style.setLineHeight( getElementProperty( handle, Style.LINE_HEIGHT_PROP ) );
		style.setOrphans( getElementProperty( handle, Style.ORPHANS_PROP ) );
		style.setTextTransform( getElementProperty( handle,
				Style.TEXT_TRANSFORM_PROP ) );
		style.setVerticalAlign( getElementProperty( handle,
				Style.VERTICAL_ALIGN_PROP ) );
		style.setWhiteSpace( getElementProperty( handle, Style.WHITE_SPACE_PROP ) );
		style.setWidows( getElementProperty( handle, Style.WIDOWS_PROP ) );
		style.setWordSpacing( getElementProperty( handle,
				Style.WORD_SPACING_PROP ) );

		// Section properties
		style.setDisplay( getElementProperty( handle, Style.DISPLAY_PROP ) );
		style.setMasterPage( getElementProperty( handle, Style.MASTER_PAGE_PROP ) );
		String pageBreakAfter = getElementProperty( handle,
				StyleHandle.PAGE_BREAK_AFTER_PROP );
		style.setPageBreakAfter( decodePageBreak( pageBreakAfter ) );
		String pageBreakBefore = getElementProperty( handle,
				StyleHandle.PAGE_BREAK_BEFORE_PROP );
		style.setPageBreakBefore( decodePageBreak( pageBreakBefore ) );

		style.setPageBreakInside( getElementProperty( handle,
				Style.PAGE_BREAK_INSIDE_PROP ) );

		// Font related
		style.setFontFamily( getElementProperty( handle, Style.FONT_FAMILY_PROP ) );
		style.setColor( getElementProperty( handle, Style.COLOR_PROP, true ) );
		style.setFontSize( getElementProperty( handle, Style.FONT_SIZE_PROP ) );
		style.setFontStyle( getElementProperty( handle, Style.FONT_STYLE_PROP ) );
		style.setFontWeight( getElementProperty( handle, Style.FONT_WEIGHT_PROP ) );
		style.setFontVariant( getElementProperty( handle,
				Style.FONT_VARIANT_PROP ) );

		// Border
		style.setBorderBottomColor( getElementProperty( handle,
				Style.BORDER_BOTTOM_COLOR_PROP,
				true ) );
		style.setBorderBottomStyle( getElementProperty( handle,
				Style.BORDER_BOTTOM_STYLE_PROP ) );
		style.setBorderBottomWidth( getElementProperty( handle,
				Style.BORDER_BOTTOM_WIDTH_PROP ) );
		style.setBorderLeftColor( getElementProperty( handle,
				Style.BORDER_LEFT_COLOR_PROP,
				true ) );
		style.setBorderLeftStyle( getElementProperty( handle,
				Style.BORDER_LEFT_STYLE_PROP ) );
		style.setBorderLeftWidth( getElementProperty( handle,
				Style.BORDER_LEFT_WIDTH_PROP ) );
		style.setBorderRightColor( getElementProperty( handle,
				Style.BORDER_RIGHT_COLOR_PROP,
				true ) );
		style.setBorderRightStyle( getElementProperty( handle,
				Style.BORDER_RIGHT_STYLE_PROP ) );
		style.setBorderRightWidth( getElementProperty( handle,
				Style.BORDER_RIGHT_WIDTH_PROP ) );
		style.setBorderTopColor( getElementProperty( handle,
				Style.BORDER_TOP_COLOR_PROP,
				true ) );
		style.setBorderTopStyle( getElementProperty( handle,
				Style.BORDER_TOP_STYLE_PROP ) );
		style.setBorderTopWidth( getElementProperty( handle,
				Style.BORDER_TOP_WIDTH_PROP ) );

		// Margin
		style.setMarginTop( getElementProperty( handle, Style.MARGIN_TOP_PROP ) );
		style.setMarginLeft( getElementProperty( handle, Style.MARGIN_LEFT_PROP ) );
		style.setMarginBottom( getElementProperty( handle,
				Style.MARGIN_BOTTOM_PROP ) );
		style.setMarginRight( getElementProperty( handle,
				Style.MARGIN_RIGHT_PROP ) );

		// Padding
		style.setPaddingTop( getElementProperty( handle, Style.PADDING_TOP_PROP ) );
		style.setPaddingLeft( getElementProperty( handle,
				Style.PADDING_LEFT_PROP ) );
		style.setPaddingBottom( getElementProperty( handle,
				Style.PADDING_BOTTOM_PROP ) );
		style.setPaddingRight( getElementProperty( handle,
				Style.PADDING_RIGHT_PROP ) );

		// Data Formatting
		style.setNumberAlign( getElementProperty( handle,
				Style.NUMBER_ALIGN_PROP ) );
		style.setDateFormat( getElementProperty( handle,
				Style.DATE_TIME_FORMAT_PROP ) );
		style.setNumberFormat( getElementProperty( handle,
				Style.NUMBER_FORMAT_PROP ) );
		style.setStringFormat( getElementProperty( handle,
				Style.STRING_FORMAT_PROP ) );

		// Others
		style.setCanShrink( getElementProperty( handle, Style.CAN_SHRINK_PROP ) );
		style.setShowIfBlank( getElementProperty( handle,
				Style.SHOW_IF_BLANK_PROP ) );
	}

	static String decodePageBreak( String pageBreak )
	{
		if ( pageBreak == null )
		{
			return null;
		}
		if ( DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS.equals( pageBreak ) )
		{
			return IStyle.CSS_ALWAYS_VALUE;
		}
		if ( DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST.equals( pageBreak ) )
		{
			return IStyle.CSS_ALWAYS_VALUE;
		}
		if ( DesignChoiceConstants.PAGE_BREAK_AFTER_AUTO.equals( pageBreak ) )
		{
			return IStyle.CSS_AUTO_VALUE;
		}
		if ( DesignChoiceConstants.PAGE_BREAK_AFTER_AVOID.equals( pageBreak ) )
		{
			return IStyle.CSS_AVOID_VALUE;
		}
		if ( DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS.equals( pageBreak ) )
		{
			return IStyle.CSS_ALWAYS_VALUE;
		}
		if ( DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST.equals( pageBreak ) )
		{
			return IStyle.CSS_ALWAYS_VALUE;
		}
		if ( DesignChoiceConstants.PAGE_BREAK_BEFORE_AUTO.equals( pageBreak ) )
		{
			return IStyle.CSS_AUTO_VALUE;
		}
		if ( DesignChoiceConstants.PAGE_BREAK_BEFORE_AVOID.equals( pageBreak ) )
		{
			return IStyle.CSS_AVOID_VALUE;
		}
		return IStyle.CSS_AUTO_VALUE;
	}

	static String getElementProperty( ReportElementHandle handle, String name )
	{
		return getElementProperty( handle, name, false );
	}

	static String getElementProperty( ReportElementHandle handle, String name,
			boolean isColorProperty )
	{
		FactoryPropertyHandle prop = handle.getFactoryPropertyHandle( name );
		if ( prop != null && prop.isSet( ) )
		{
			if ( isColorProperty )
			{
				return prop.getColorValue( );
			}

			return prop.getStringValue( );
		}
		return null;
	}

}
