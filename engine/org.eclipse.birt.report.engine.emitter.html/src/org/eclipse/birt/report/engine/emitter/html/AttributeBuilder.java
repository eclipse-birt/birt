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

import org.eclipse.birt.report.engine.api.IHyperlinkProcessor;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.StyleDesign;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.eclipse.birt.report.model.metadata.Choice;
import org.eclipse.birt.report.model.metadata.DimensionValue;

/**
 * <code>AttributeBuilder</code> is a concrete class that HTML Emitters use to
 * build the Style strings.
 * 
 * @version $Revision: #4 $ $Date: 2005/02/05 $
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
			buildSize( content, "left", x ); //$NON-NLS-1$
			buildSize( content, "top", y ); //$NON-NLS-1$
		}

		buildSize( content, "width", width ); //$NON-NLS-1$
		buildSize( content, "height", height ); //$NON-NLS-1$

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
	public static void buildStyle( StringBuffer content, StyleDesign style,
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
	private static void buildVisual( StringBuffer content, StyleDesign style )
	{
		// move display property from css file to html file
		// buildProperty( content, "display", style.getDisplay( ) );
		buildProperty( content, "vertical-align", style.getVerticalAlign( ) ); //$NON-NLS-1$
		buildProperty( content, "line-height", style.getLineHeight( ) ); //$NON-NLS-1$
	}

	/**
	 * Build the PagedMedia style string.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param style
	 *            The style object.
	 */
	private static void buildPagedMedia( StringBuffer content, StyleDesign style )
	{
		buildProperty( content, "orphans", style.getOrphans( ) ); //$NON-NLS-1$
		buildProperty( content, "widows", style.getWidows( ) ); //$NON-NLS-1$
		buildProperty( content, "page-break-before", style.getPageBreakBefore( ) ); //$NON-NLS-1$
		buildProperty( content, "page-break-after", style.getPageBreakAfter( ) ); //$NON-NLS-1$
		buildProperty( content, "page-break-inside", style.getPageBreakInside( ) ); //$NON-NLS-1$
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
			StyleDesign style, HTMLReportEmitter emitter )
	{
		buildProperty( content, "color", style.getColor( ) ); //$NON-NLS-1$

		String color = style.getBackgroundColor( );
		String image = style.getBackgroundImage( );
		String repeat = style.getBackgroundRepeat( );
		String attach = style.getBackgroundAttachment( );
		String x = style.getBackgroundPositionX( );
		String y = style.getBackgroundPositionY( );

		if ( color == null && image == null && !"none".equalsIgnoreCase( image )
				&& repeat == null && attach == null && x == null && y == null )
		{
			return;
		}

		content.append( " background:" ); //$NON-NLS-1$
		addPropValue( content, color );

		if ( !"none".equalsIgnoreCase( image )
				&& FileUtil.isRelativePath( image )
				&& emitter.getReport( ) != null )
		{
			image = FileUtil.getAbsolutePath( emitter.getReport( )
					.getBasePath( ), image );
		}
		if ( image != null && image.length( ) > 0
				&& !"none".equalsIgnoreCase( image ) )
		{
			if ( FileUtil.isLocalResource( image ) )
			{
				if ( emitter.needSaveImgFile( ) )
				{
					image = emitter.getResourceManager( ).storeResource( image,
							HTMLReportEmitter.IMAGE_FOLDER ); //$NON-NLS-1$
					if ( image == null )
					{
						image = "none";
					}
				}
				else
				{
					image = "file:" + image;
				}
				image = emitter.getHyperlinkBuilder( ).createHyperlink(
						IHyperlinkProcessor.HYPERLINK_IMAGE, image );
			}
			addURLValue( content, image );
		}

		addPropValue( content, repeat );

		addPropValue( content, attach );

		addPropValue( content, x );

		addPropValue( content, y );

		content.append( ';' );
	}

	/**
	 * Build the Box style string.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param style
	 *            The style object.
	 */
	private static void buildBox( StringBuffer content, StyleDesign style )
	{
		buildPaddingMargine( content, style.getMarginTop( ), style
				.getMarginRight( ), style.getMarginBottom( ), style
				.getMarginLeft( ), false );

		buildPaddingMargine( content, style.getPaddingTop( ), style
				.getPaddingRight( ), style.getPaddingBottom( ), style
				.getPaddingLeft( ), true );

		buildBorder( content, "border-top", //$NON-NLS-1$
				style.getBorderTopWidth( ), style.getBorderTopStyle( ), style
						.getBorderTopColor( ) );

		buildBorder( content, "border-right", //$NON-NLS-1$
				style.getBorderRightWidth( ), style.getBorderRightStyle( ),
				style.getBorderRightColor( ) );

		buildBorder( content, "border-bottom", //$NON-NLS-1$
				style.getBorderBottomWidth( ), style.getBorderBottomStyle( ),
				style.getBorderBottomColor( ) );

		buildBorder( content, "border-left", //$NON-NLS-1$
				style.getBorderLeftWidth( ), style.getBorderLeftStyle( ), style
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
	private static void buildText( StringBuffer content, StyleDesign style )
	{
		buildProperty( content, "text-indent", style.getTextIndent( ) ); //$NON-NLS-1$
		buildProperty( content, "text-align", style.getTextAlign( ) ); //$NON-NLS-1$

		buildTextDecoration( content, style.getTextLineThrough( ), style
				.getTextOverline( ), style.getTextUnderline( ) );

		buildProperty( content, "letter-spacing", style.getLetterSpacing( ) ); //$NON-NLS-1$
		buildProperty( content, "word-spacing", style.getWordSpacing( ) ); //$NON-NLS-1$
		buildProperty( content, "text-transform", style.getTextTransform( ) ); //$NON-NLS-1$
		buildProperty( content, "white-space", style.getWhiteSpace( ) ); //$NON-NLS-1$
	}

	/**
	 * Build Font style string.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param style
	 *            The style object.
	 */
	private static void buildFont( StringBuffer content, StyleDesign style )
	{
		buildProperty( content, "font-family", style.getFontFamily( ) ); //$NON-NLS-1$

		buildProperty( content, "font-style", style.getFontStyle( ) ); //$NON-NLS-1$

		buildProperty( content, "font-variant", style.getFontVariant( ) ); //$NON-NLS-1$

		buildProperty( content, "font-weight", style.getFontWeight( ) ); //$NON-NLS-1$

		buildProperty( content, "font-size", style.getFontSize( ) ); //$NON-NLS-1$
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

		if ( linethrough != null && !"none".equalsIgnoreCase( linethrough ) )
		{
			flag = 1; // linethrough
		}
		if ( underline != null && !"none".equalsIgnoreCase( underline ) )
		{
			flag |= 2; // underline
		}
		if ( overline != null && !"none".equalsIgnoreCase( overline ) )
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
		if ( width == null && style == null && color == null )
		{
			return;
		}

		addPropName( content, name );
		addPropValue( content, width );
		addPropValue( content, style );
		addPropValue( content, color );

		content.append( ';' );
	}

	/**
	 * Build Padding/Margin string.
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param top -
	 *            top value
	 * @param right -
	 *            right value
	 * @param bottom -
	 *            bottom value
	 * @param left -
	 *            left value
	 * @param isPadding -
	 *            A boolean value; <code>true</code> indicating to build
	 *            Padding property, otherwise for Margin property
	 */
	private static void buildPaddingMargine( StringBuffer content, String top,
			String right, String bottom, String left, boolean isPadding )
	{
		if ( isPadding )
		{
			buildProperty( content, "padding-top", top ); //$NON-NLS-1$
			buildProperty( content, "padding-right", right ); //$NON-NLS-1$
			buildProperty( content, "padding-bottom", bottom ); //$NON-NLS-1$
			buildProperty( content, "padding-left", left ); //$NON-NLS-1$
		}
		else
		{
			buildProperty( content, "margin-top", top ); //$NON-NLS-1$
			buildProperty( content, "margin-right", right ); //$NON-NLS-1$
			buildProperty( content, "margin-bottom", bottom ); //$NON-NLS-1$
			buildProperty( content, "margin-left", left ); //$NON-NLS-1$
		}
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
		if ( url != null && url.length( ) > 0 )
		{
			// TODO: escape URL string
			content.append( " url('" ); //$NON-NLS-1$
			content.append( url );
			content.append( "')" ); //$NON-NLS-1$
		}
	}
}

