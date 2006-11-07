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

package org.eclipse.birt.report.model.css;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetParserException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.CssPropertyConstants;

import com.ibm.icu.util.ULocale;

/**
 * Tests the function of the style sheet loader.
 */

public class StyleSheetLoaderTest extends BaseTestCase
{

	private String fileName = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		SessionHandle session = DesignEngine.newSession( (ULocale) null );
		designHandle = session.createDesign( );
	}

	private CssStyleSheetHandle loadStyleSheet( String fileName )
			throws Exception
	{
		fileName = INPUT_FOLDER + fileName;
		InputStream is = getResourceAStream( fileName );
		return designHandle.openCssStyleSheet( is );
	}

	/**
	 * Tests a normal input css file, and all the input is loaded into the
	 * report.
	 * 
	 * @throws Exception
	 */

	public void testParserForAllProperties( ) throws Exception
	{
		fileName = "base.css"; //$NON-NLS-1$

		Iterator styles = loadStyleSheet( fileName ).getStyleIterator( );

		StyleHandle style1 = (StyleHandle) styles.next( );
		StyleHandle style2 = (StyleHandle) styles.next( );
		StyleHandle style3 = (StyleHandle) styles.next( );
		assertFalse( styles.hasNext( ) );

		assertEquals( "fullstyle", style1.getName( ) ); //$NON-NLS-1$
		assertEquals( "H1", style2.getName( ) ); //$NON-NLS-1$
		assertEquals( "table", style3.getName( ) ); //$NON-NLS-1$

		// font

		isSame( styles, IStyleModel.FONT_FAMILY_PROP,
				"\"Bitstream Vera Sans\", Tahoma, Verdana, \"Myriad Web\", Syntax, sans-serif" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.FONT_SIZE_PROP, "2em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.FONT_STYLE_PROP,
				DesignChoiceConstants.FONT_STYLE_ITALIC );
		isSame( styles, IStyleModel.FONT_VARIANT_PROP,
				DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS );
		isSame( styles, IStyleModel.FONT_WEIGHT_PROP,
				DesignChoiceConstants.FONT_WEIGHT_BOLD );

		// text

		isSame( styles, IStyleModel.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_JUSTIFY );
		isSame( styles, IStyleModel.TEXT_INDENT_PROP, "2em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.TEXT_UNDERLINE_PROP,
				DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE );
		isSame( styles, IStyleModel.TEXT_OVERLINE_PROP,
				DesignChoiceConstants.TEXT_OVERLINE_OVERLINE );
		isSame( styles, IStyleModel.TEXT_LINE_THROUGH_PROP,
				DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH );
		isSame( styles, IStyleModel.LETTER_SPACING_PROP, "1pt" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.WORD_SPACING_PROP, "2pt" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.TEXT_TRANSFORM_PROP,
				DesignChoiceConstants.TRANSFORM_UPPERCASE );
		isSame( styles, IStyleModel.WHITE_SPACE_PROP,
				DesignChoiceConstants.WHITE_SPACE_PRE );

		// margin

		isSame( styles, IStyleModel.MARGIN_BOTTOM_PROP, "1em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.MARGIN_LEFT_PROP, "1em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.MARGIN_RIGHT_PROP, "1em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.MARGIN_TOP_PROP, "1em" ); //$NON-NLS-1$

		// padding

		isSame( styles, IStyleModel.PADDING_BOTTOM_PROP, "1em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.PADDING_LEFT_PROP, "1em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.PADDING_RIGHT_PROP, "1em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.PADDING_TOP_PROP, "1em" ); //$NON-NLS-1$

		// background

		// color is rgb( 0, 79, 147)
		isSame( styles, IStyleModel.COLOR_PROP, "#004F93" ); //$NON-NLS-1$ 
		isSame( styles, IStyleModel.BACKGROUND_COLOR_PROP,
				IColorConstants.BLACK );
		isSame( styles, IStyleModel.BACKGROUND_IMAGE_PROP, "images/header" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.BACKGROUND_REPEAT_PROP,
				DesignChoiceConstants.BACKGROUND_REPEAT_NO_REPEAT );
		isSame( styles, IStyleModel.BACKGROUND_ATTACHMENT_PROP,
				DesignChoiceConstants.BACKGROUND_ATTACHMENT_SCROLL );
		isSame( styles, IStyleModel.BACKGROUND_POSITION_X_PROP, "50%" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.BACKGROUND_POSITION_Y_PROP,
				DesignChoiceConstants.BACKGROUND_POSITION_TOP );

		// page meida

		isSame( styles, IStyleModel.ORPHANS_PROP, "1" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.WIDOWS_PROP, "3" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.DISPLAY_PROP,
				DesignChoiceConstants.DISPLAY_INLINE );
		isSame( styles, IStyleModel.PAGE_BREAK_BEFORE_PROP,
				DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS );
		isSame( styles, IStyleModel.PAGE_BREAK_AFTER_PROP,
				DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS );
		isSame( styles, IStyleModel.PAGE_BREAK_INSIDE_PROP,
				DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID );

		// visual

		isSame( styles, IStyleModel.VERTICAL_ALIGN_PROP,
				DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE );
		isSame( styles, IStyleModel.LINE_HEIGHT_PROP, "120%" ); //$NON-NLS-1$

		// border

		isSame( styles, IStyleModel.BORDER_BOTTOM_COLOR_PROP, "#445566" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.BORDER_BOTTOM_STYLE_PROP,
				DesignChoiceConstants.LINE_STYLE_SOLID );
		isSame( styles, IStyleModel.BORDER_BOTTOM_WIDTH_PROP, "10px" ); //$NON-NLS-1$		

	}

	/**
	 * @throws Exception
	 */

	public void testStyleIterator( ) throws Exception
	{
		fileName = "base.css"; //$NON-NLS-1$
		CssStyleSheetHandle styleSheetHandle = loadStyleSheet( fileName );
		assertNotNull( styleSheetHandle );

		Iterator iter = styleSheetHandle.getStyleIterator( );
		int i = 0;
		while ( iter.hasNext( ) )
		{
			SharedStyleHandle styleHandle = (SharedStyleHandle) iter.next( );
			switch ( i++ )
			{
				case 0 :
					assertEquals( "fullstyle", styleHandle.getName( ) ); //$NON-NLS-1$
					break;
				case 1 :
					assertEquals( "H1", styleHandle.getName( ) ); //$NON-NLS-1$
					break;
				case 2 :
					assertEquals( "table", styleHandle.getName( ) ); //$NON-NLS-1$
					break;

			}
		}
	}

	/**
	 * Tests a css file with wrong at rule key word. The parser will ignore it
	 * and parse on.
	 * 
	 * @throws Exception
	 */

	public void testWrongAtKeyWord( ) throws Exception
	{
		fileName = "wrong_1.css"; //$NON-NLS-1$
		Iterator styles = loadStyleSheet( fileName ).getStyleIterator( );
		assertNotNull( styles.next( ) );
		assertFalse( styles.hasNext( ) );
	}

	/**
	 * Tests a css file with wrong selector. The parser will ignore the entire
	 * selector and its declaration and parse on.
	 * 
	 * @throws Exception
	 */

	public void testWrongSelector( ) throws Exception
	{
		fileName = "wrong_2.css"; //$NON-NLS-1$
		Iterator styles = loadStyleSheet( fileName ).getStyleIterator( );
		assertNotNull( styles.next( ) );
		assertFalse( styles.hasNext( ) );
	}

	/**
	 * @throws Exception
	 */
	public void testPropertyCombination( ) throws Exception
	{
		fileName = "property_combination.css"; //$NON-NLS-1$
		Iterator styles = loadStyleSheet( fileName ).getStyleIterator( );

		isSame( styles, IStyleModel.FONT_FAMILY_PROP,
				"\"Bitstream Vera Sans\", Tahoma, Verdana, \"Myriad Web\", Syntax, sans-serif" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.FONT_SIZE_PROP, "2em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.FONT_STYLE_PROP,
				DesignChoiceConstants.FONT_STYLE_ITALIC );
		isSame( styles, IStyleModel.FONT_VARIANT_PROP,
				DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS );
		isSame( styles, IStyleModel.FONT_WEIGHT_PROP,
				DesignChoiceConstants.FONT_WEIGHT_BOLD );
	}

	/**
	 * @throws Exception
	 */
	public void testShortHand( ) throws Exception
	{
		fileName = "property_shorthand.css"; //$NON-NLS-1$
		Iterator styles = loadStyleSheet( fileName ).getStyleIterator( );
		StyleHandle style = (StyleHandle) styles.next( );
		assertEquals( "table", style.getName( ) ); //$NON-NLS-1$

		assertFalse( styles.hasNext( ) );

		// font
		isSame( styles, IStyleModel.FONT_FAMILY_PROP,
				"\"Bitstream Vera Sans\",Tahoma,Verdana,\"Myriad Web\",Syntax,sans-serif" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.FONT_SIZE_PROP, "2em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.FONT_STYLE_PROP,
				DesignChoiceConstants.FONT_STYLE_ITALIC );
		isSame( styles, IStyleModel.FONT_VARIANT_PROP,
				DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS );
		isSame( styles, IStyleModel.FONT_WEIGHT_PROP,
				DesignChoiceConstants.FONT_WEIGHT_BOLD );

		// margin

		isSame( styles, IStyleModel.MARGIN_BOTTOM_PROP, "1em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.MARGIN_LEFT_PROP, "1em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.MARGIN_RIGHT_PROP, "1em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.MARGIN_TOP_PROP, "1em" ); //$NON-NLS-1$

		// padding

		isSame( styles, IStyleModel.PADDING_BOTTOM_PROP, "1em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.PADDING_LEFT_PROP, "1em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.PADDING_RIGHT_PROP, "1em" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.PADDING_TOP_PROP, "1em" ); //$NON-NLS-1$

		// background

		isSame( styles, IStyleModel.BACKGROUND_COLOR_PROP,
				IColorConstants.BLACK );
		isSame( styles, IStyleModel.BACKGROUND_IMAGE_PROP, "images/header" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.BACKGROUND_REPEAT_PROP,
				DesignChoiceConstants.BACKGROUND_REPEAT_NO_REPEAT );
		isSame( styles, IStyleModel.BACKGROUND_ATTACHMENT_PROP,
				DesignChoiceConstants.BACKGROUND_ATTACHMENT_SCROLL );
		isSame( styles, IStyleModel.BACKGROUND_POSITION_X_PROP, "50%" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.BACKGROUND_POSITION_Y_PROP,
				DesignChoiceConstants.BACKGROUND_POSITION_TOP );

		// visual

		isSame( styles, IStyleModel.LINE_HEIGHT_PROP, "120%" ); //$NON-NLS-1$

		// border

		isSame( styles, IStyleModel.BORDER_BOTTOM_COLOR_PROP, "#445566" ); //$NON-NLS-1$
		isSame( styles, IStyleModel.BORDER_BOTTOM_STYLE_PROP,
				DesignChoiceConstants.LINE_STYLE_SOLID );
		isSame( styles, IStyleModel.BORDER_BOTTOM_WIDTH_PROP, "10px" ); //$NON-NLS-1$		
	}

	/**
	 * Tests warnings related.
	 * 
	 * @throws Exception
	 */

	public void testWarnings( ) throws Exception
	{
		fileName = "wrong.css"; //$NON-NLS-1$
		CssStyleSheetHandle styleSheet = loadStyleSheet( fileName );
		Iterator styles = styleSheet.getStyleIterator( );

		StyleHandle style = (StyleHandle) styles.next( );
		assertEquals( "fullstyle", style.getName( ) ); //$NON-NLS-1$

		List errors = styleSheet.getWarnings( style.getName( ) );
		assertEquals( 2, errors.size( ) );
		StyleSheetParserException e = null;
		e = (StyleSheetParserException) errors.get( 0 );
		assertEquals(
				StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE,
				e.getErrorCode( ) );
		assertEquals( CssPropertyConstants.ATTR_FONT, e.getCSSPropertyName( ) );
		assertEquals(
				"2em small-caps \"Bitstream Vera Sans\", Tahoma, Verdana, \"Myriad Web\", Syntax, sans-serif", e.getCSSValue( ) ); //$NON-NLS-1$
		e = (StyleSheetParserException) errors.get( 1 );
		assertEquals(
				StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE,
				e.getErrorCode( ) );
		assertEquals( CssPropertyConstants.ATTR_BACKGROUND_IMAGE, e
				.getCSSPropertyName( ) );
		assertEquals( "uattr(images) / attr(header))", e.getCSSValue( ) ); //$NON-NLS-1$

		style = (StyleHandle) styles.next( );
		assertEquals( "H1", style.getName( ) ); //$NON-NLS-1$
		errors = styleSheet.getWarnings( style.getName( ) );
		assertEquals( 2, errors.size( ) );
		e = (StyleSheetParserException) errors.get( 0 );
		assertEquals(
				StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE,
				e.getErrorCode( ) );
		assertEquals( CssPropertyConstants.ATTR_FONT, e.getCSSPropertyName( ) );
		assertEquals(
				"2em small-caps \"Bitstream Vera Sans\", Tahoma, Verdana, \"Myriad Web\", Syntax, sans-serif", e.getCSSValue( ) ); //$NON-NLS-1$
		e = (StyleSheetParserException) errors.get( 1 );
		assertEquals(
				StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE,
				e.getErrorCode( ) );
		assertEquals( CssPropertyConstants.ATTR_BACKGROUND_IMAGE, e
				.getCSSPropertyName( ) );
		assertEquals( "uattr(images) / attr(header))", e.getCSSValue( ) ); //$NON-NLS-1$

		style = (StyleHandle) styles.next( );
		assertEquals( "table", style.getName( ) ); //$NON-NLS-1$
		errors = styleSheet.getWarnings( style.getName( ) );
		assertEquals( 3, errors.size( ) );
		e = (StyleSheetParserException) errors.get( 2 );
		assertEquals(
				StyleSheetParserException.DESIGN_EXCEPTION_PROPERTY_NOT_SUPPORTED,
				e.getErrorCode( ) );
		assertEquals( "wrongproperty", e.getCSSPropertyName( ) ); //$NON-NLS-1$
		assertEquals( "value", e.getCSSValue( ) ); //$NON-NLS-1$

		List unsupportedStyles = styleSheet.getUnsupportedStyles( );
		assertEquals( "table:link", (String) unsupportedStyles.get( 0 ) ); //$NON-NLS-1$

	}

	/**
	 * Tests warnings related.
	 * 
	 * @throws Exception
	 */

	public void testParserErrors( ) throws Exception
	{
		fileName = "wrong_3.css"; //$NON-NLS-1$
		CssStyleSheetHandle styleSheet = loadStyleSheet( fileName );

		assertEquals( 1, styleSheet.getParserErrors( ).size( ) );
		assertEquals(
				"[11:1] encountered \"_\". Was expecting one of: \"{\" \",\" \"[\" \".\" \":\" <HASH> \"+\" \">\" ", styleSheet.getParserErrors( ).get( 0 ) ); //$NON-NLS-1$
		assertEquals( 0, styleSheet.getParserFatalErrors( ).size( ) );
		assertEquals( 0, styleSheet.getParserWarnings( ).size( ) );

	}

	/**
	 * Tests a group styles has a same property with a given property name. Each
	 * one in the list is instance of <code>StyleHandle</code>.
	 * 
	 * @param styles
	 * @param propName
	 * @param value
	 */

	private void isSame( Iterator styles, String propName, Object value )
	{
		for ( ; styles.hasNext( ); )
		{
			StyleHandle style = (StyleHandle) styles.next( );
			assertEquals( value, style.getStringProperty( propName ) );
		}
	}
}