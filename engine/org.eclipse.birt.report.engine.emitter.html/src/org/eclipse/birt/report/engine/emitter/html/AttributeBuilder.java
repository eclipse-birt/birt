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

package org.eclipse.birt.report.engine.emitter.html;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.metadata.Choice;

/**
 * <code>AttributeBuilder</code> is a concrete class that HTML Emitters use to
 * build the Style strings.
 * 
 * @version $Revision: 1.15 $ $Date: 2005/07/07 06:25:38 $
 */
public class AttributeBuilder
{

	/**
	 * Build the relative position of a component. This method is obsolete.
	 */
	public static String buildPos( DimensionType x, DimensionType y,
			DimensionType width, DimensionType height )
	{
		StringBuffer content = new StringBuffer( );

		if ( x != null || y != null )
		{
			content.append( "position: relative;" ); //$NON-NLS-1$
			buildSize( content, HTMLTags.ATTR_LEFT, x );
			buildSize( content, HTMLTags.ATTR_TOP, y );
		}

		buildSize( content, HTMLTags.ATTR_WIDTH, width );
		buildSize( content, HTMLTags.ATTR_HEIGHT, height );

		return content.toString( );
	}

	/**
	 * Output the style content to a given <code>StringBuffer</code> object.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param style
	 *            The style object.
	 * @param emitter
	 *            The <code>HTMLReportEmitter</code> object which provides
	 *            resource manager and hyperlink builder objects for
	 *            background-image property.
	 */
	public static void buildStyle( StringBuffer content, IStyle style,
			HTMLReportEmitter emitter )
	{
		if ( style == null )
		{
			return;
		}
		buildFont( content, style );
		buildText( content, style );
		buildBox( content, style );
		buildBackground( content, style, emitter );
		buildPagedMedia( content, style );
		buildVisual( content, style );

		/*
		 * style.getNumberAlign(); style.getID(); style.getName();
		 */
	}

	/**
	 * Builds the Visual style string.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param style
	 *            The style object.
	 */
	private static void buildVisual( StringBuffer content, IStyle style )
	{
		// move display property from css file to html file
		// buildProperty( content, "display", style.getDisplay( ) );
		buildProperty( content, HTMLTags.ATTR_VERTICAL_ALIGN, style
				.getVerticalAlign( ) ); //$NON-NLS-1$
		buildProperty( content, HTMLTags.ATTR_LINE_HEIGHT, style
				.getLineHeight( ) ); //$NON-NLS-1$
	}

	/**
	 * Build the PagedMedia style string.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param style
	 *            The style object.
	 */
	private static void buildPagedMedia( StringBuffer content,
			IStyle style )
	{
		buildProperty( content, HTMLTags.ATTR_ORPHANS, style.getOrphans( ) );
		buildProperty( content, HTMLTags.ATTR_WIDOWS, style.getWidows( ) );
		buildProperty( content, HTMLTags.ATTR_PAGE_BREAK_BEFORE, style
				.getPageBreakBefore( ) );
		buildProperty( content, HTMLTags.ATTR_PAGE_BREAK_AFTER, style
				.getPageBreakAfter( ) );
		buildProperty( content, HTMLTags.ATTR_PAGE_BREAK_INSIDE, style
				.getPageBreakInside( ) );
	}

	/**
	 * Build the background style string.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param style
	 *            The style object.
	 * @param emitter
	 *            The <code>HTMLReportEmitter</code> object which provides
	 *            resource manager and hyperlink builder objects.
	 */
	private static void buildBackground( StringBuffer content,
			IStyle style, HTMLReportEmitter emitter )
	{
		buildProperty( content, HTMLTags.ATTR_COLOR, style.getColor( ) );
		buildProperty( content, HTMLTags.ATTR_BACKGROUND_COLOR, style.getBackgroundColor( ) );

		String image = style.getBackgroundImage( );
		if ( image == null && !"none".equalsIgnoreCase( image ) ) //$NON-NLS-1$
		{
			return;
		}

		image = HTMLBaseEmitter.handleStyleImage(image, emitter);
		if( image != null && image.length( ) > 0 )
		{
			buildURLProperty( content, HTMLTags.ATTR_BACKGROUND_IMAGE, image );
			buildProperty( content, HTMLTags.ATTR_BACKGROUND_REPEAT, style.getBackgroundRepeat( ) );
			buildProperty( content, HTMLTags.ATTR_BACKGROUND_ATTACHEMNT, style.getBackgroundAttachment( ) );
			
			String x = style.getBackgroundPositionX( );
			String y = style.getBackgroundPositionY( );
			if( x != null || y != null )
			{
				addPropName( content, HTMLTags.ATTR_BACKGROUND_POSITION );
				addPropValue( content, x );
				addPropValue( content, y );
				content.append( ';' );
			}
		}
	}

	/**
	 * Build the Box style string.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param style
	 *            The style object.
	 */
	private static void buildBox( StringBuffer content, IStyle style )
	{
		buildProperty( content, HTMLTags.ATTR_MARGIN_TOP, style.getMarginTop( ) );
		buildProperty( content, HTMLTags.ATTR_MARGIN_RIGHT, style
				.getMarginRight( ) );
		buildProperty( content, HTMLTags.ATTR_MARGIN_BOTTOM, style
				.getMarginBottom( ) );
		buildProperty( content, HTMLTags.ATTR_MARGIN_LEFT, style
				.getMarginLeft( ) );

		buildProperty( content, HTMLTags.ATTR_PADDING_TOP, style
				.getPaddingTop( ) );
		buildProperty( content, HTMLTags.ATTR_PADDING_RIGHT, style
				.getPaddingRight( ) );
		buildProperty( content, HTMLTags.ATTR_PADDING_BOTTOM, style
				.getPaddingBottom( ) );
		buildProperty( content, HTMLTags.ATTR_PADDING_LEFT, style
				.getPaddingLeft( ) );

		buildBorder( content, HTMLTags.ATTR_BORDER_TOP, style
				.getBorderTopWidth( ), style.getBorderTopStyle( ), style
				.getBorderTopColor( ) );

		buildBorder( content, HTMLTags.ATTR_BORDER_RIGHT, style
				.getBorderRightWidth( ), style.getBorderRightStyle( ), style
				.getBorderRightColor( ) );

		buildBorder( content, HTMLTags.ATTR_BORDER_BOTTOM, style
				.getBorderBottomWidth( ), style.getBorderBottomStyle( ), style
				.getBorderBottomColor( ) );

		buildBorder( content, HTMLTags.ATTR_BORDER_LEFT, style
				.getBorderLeftWidth( ), style.getBorderLeftStyle( ), style
				.getBorderLeftColor( ) );
	}

	/**
	 * Build the Text style string.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param style
	 *            The style object.
	 */
	private static void buildText( StringBuffer content, IStyle style )
	{
		buildProperty( content, HTMLTags.ATTR_TEXT_INDENT, style
				.getTextIndent( ) );
		buildProperty( content, HTMLTags.ATTR_TEXT_ALIGN, style.getTextAlign( ) );

		buildTextDecoration( content, style.getTextLineThrough( ), style
				.getTextOverline( ), style.getTextUnderline( ) );

		buildProperty( content, HTMLTags.ATTR_LETTER_SPACING, style
				.getLetterSpacing( ) );
		buildProperty( content, HTMLTags.ATTR_WORD_SPACING, style
				.getWordSpacing( ) );
		buildProperty( content, HTMLTags.ATTR_TEXT_TRANSFORM, style
				.getTextTransform( ) );
		buildProperty( content, HTMLTags.ATTR_WHITE_SPACE, style
				.getWhiteSpace( ) );
	}

	/**
	 * Build Font style string.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param style
	 *            The style object.
	 */
	private static void buildFont( StringBuffer content, IStyle style )
	{
		buildProperty( content, HTMLTags.ATTR_FONT_FAMILY, style
				.getFontFamily( ) );

		buildProperty( content, HTMLTags.ATTR_FONT_STYLE, style.getFontStyle( ) );

		buildProperty( content, HTMLTags.ATTR_FONT_VARIANT, style
				.getFontVariant( ) );

		buildProperty( content, HTMLTags.ATTR_FONT_WEIGTH, style
				.getFontWeight( ) );

		buildProperty( content, HTMLTags.ATTR_FONT_SIZE, style.getFontSize( ) );
	}
	
	public static void checkHyperlinkTextDecoration( IStyle style, StringBuffer content )
	{
		if( style != null )
		{
			buildTextDecoration( content, style.getTextLineThrough( ),
					style.getTextUnderline( ), style.getTextOverline( ) );
		}
	}
	
	/**
	 * Build the Text-Decoration style string.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param linethrough
	 *            The line-through value.
	 * @param underline
	 *            The underline value.
	 * @param overline
	 *            The overline value.
	 */
	private static void buildTextDecoration( StringBuffer content,
			String linethrough, String underline, String overline )
	{
		int flag = 0;

		if ( linethrough != null && !"none".equalsIgnoreCase( linethrough ) ) //$NON-NLS-1$
		{
			flag = 1; // linethrough
		}
		if ( underline != null && !"none".equalsIgnoreCase( underline ) ) //$NON-NLS-1$
		{
			flag |= 2; // underline
		}
		if ( overline != null && !"none".equalsIgnoreCase( overline ) ) //$NON-NLS-1$
		{
			flag |= 4; // overline
		}

		if ( flag > 0 )
		{
			content.append( " text-decoration:" ); //$NON-NLS-1$
			if ( ( flag & 1 ) > 0 ) // linethrough
			{
				addPropValue( content, linethrough );
			}
			if ( ( flag & 2 ) > 0 ) // underline
			{
				addPropValue( content, underline );
			}
			if ( ( flag & 4 ) > 0 ) //overline
			{
				addPropValue( content, overline );
			}
			content.append( ';' );
		}
	}

	/**
	 * Build the border string.
	 * <li>ignore all the border styles is style is null
	 * <li>CSS default border-color is the font-color, while BIRT is black
	 * <li>border-color is not inheritable.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param name
	 *            The proerty name.
	 * @param width
	 *            The border-width value.
	 * @param style
	 *            The border-style value.
	 * @param color
	 *            The border-color value
	 */
	private static void buildBorder( StringBuffer content, String name,
			String width, String style, String color )
	{
		if ( style == null || style.length( ) <= 0 )
		{
			return;
		}
		addPropName( content, name );
		addPropValue( content, width );
		addPropValue( content, style );
		addPropValue( content, color == null ? "black" : color ); //$NON-NLS-1$
		content.append( ';' );
	}

	/**
	 * Build size style string say, "width: 10.0mm;".
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param name
	 *            The property name
	 * @param value
	 *            The values of the property
	 */
	public static void buildSize( StringBuffer content, String name,
			DimensionType value )
	{
		if ( value != null )
		{
			addPropName( content, name );
			addPropValue( content, value.toString( ) );
			content.append( ';' );
		}
	}

	/**
	 * Build the style property.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param name
	 *            The name of the property
	 * @param value
	 *            The values of the property
	 */
	private static void buildProperty( StringBuffer content, String name,
			String value )
	{
		if ( value != null )
		{
			addPropName( content, name );
			addPropValue( content, value );
			content.append( ';' );
		}
	}

	/**
	 * Build the style property.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param name
	 *            The name of the property
	 * @param value
	 *            The values of the property
	 */
	private static void buildProperty( StringBuffer content, String name,
			DimensionValue value )
	{
		if ( value != null )
		{
			buildProperty( content, name, value.toString( ) );
		}
	}

	/**
	 * Build the style property, this method is obsolete.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param name
	 *            The name of the property
	 * @param value
	 *            The values of the property
	 */
	private static void buildProperty( StringBuffer content, String name,
			Choice value )
	{
		if ( value != null )
		{
			buildProperty( content, name, value.getName( ) );
		}
	}

	private static void buildURLProperty( StringBuffer content, String name, String url )
	{
		if( url != null )
		{
			addPropName( content, name );
			addURLValue( content, url );
			content.append( ';' );
		}
	}
	
	/**
	 * Add property name to the Style string.
	 * 
	 * @param content
	 *            The StringBuffer to which the result should be output.
	 * @param name
	 *            The property name.
	 */
	private static void addPropName( StringBuffer content, String name )
	{
		content.append( ' ' );
		content.append( name );
		content.append( ':' );
	}

	/**
	 * Add property value to the Style content.
	 * 
	 * @param content -
	 *            specifies the StringBuffer to which the result should be
	 *            output
	 * @param value -
	 *            specifies the values of the property
	 */
	private static void addPropValue( StringBuffer content, String value )
	{
		if ( value != null )
		{
			content.append( ' ' );
			content.append( value );
		}
	}

	/**
	 * Add URL property name to the Style content.
	 * 
	 * @param content -
	 *            specifies the StringBuffer to which the result should be
	 *            output
	 * @param url -
	 *            specifies the values of the property
	 */
	private static void addURLValue( StringBuffer content, String url )
	{
		if ( url == null )
		{
			return;
		}

		// escape the URL string
		StringBuffer escapedUrl = null;
		for ( int i = 0, max = url.length( ), delta = 0; i < max; i++ )
		{
			char c = url.charAt( i );
			String replacement = null;
			if ( c == '\\' )
			{
				replacement = "%5c"; //$NON-NLS-1$
			}
			else if ( c == '#' )
			{
				replacement = "%23"; //$NON-NLS-1$
			}
			else if ( c == '%' )
			{
				replacement = "%25"; //$NON-NLS-1$
			}
			else if ( c == '\'' )
			{
				replacement = "%27"; //$NON-NLS-1$
			}
			else if ( c >= 0x80 )
			{
				replacement = '%' + Integer.toHexString( c );
			}

			if ( replacement != null )
			{
				if ( escapedUrl == null )
				{
					escapedUrl = new StringBuffer( url );
				}
				escapedUrl.replace( i + delta, i + delta + 1, replacement );
				delta += ( replacement.length( ) - 1 );
			}
		}

		if ( escapedUrl != null )
		{
			url = escapedUrl.toString( );
		}

		if ( url.length( ) > 0 )
		{
			content.append( " url('" ); //$NON-NLS-1$
			content.append( url );
			content.append( "')" ); //$NON-NLS-1$
		}
	}
}

