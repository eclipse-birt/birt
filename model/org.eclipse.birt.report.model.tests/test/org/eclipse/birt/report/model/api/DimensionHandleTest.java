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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test DimensionHandle.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testGetDisplayValue()}</td>
 * <td>value is from an choice</td>
 * <td>display value is from message file of the locale</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>value is a dimension value, locale is English, China</td>
 * <td>display value is locale-specific</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>value is a dimension value of a highlight member, locales are English,
 * China</td>
 * <td>display value is locale-specific and matches with the design file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetStringValue()}</td>
 * <td>value is from an choice</td>
 * <td>return should be the choice name</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>value is a real dimension</td>
 * <td>return should be the dimension in string</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>value is a member of a highlight.</td>
 * <td>returned value matches with the value in the design file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSetValue()}</td>
 * <td>property type is dimension with choices</td>
 * <td>set the value to an choice</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>property type is dimension with choices</td>
 * <td>set the value to "12pt"</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>application unit is "cm", set the value to 12( double )</td>
 * <td>the dimension should be set using the application unit</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets a dimension value to a highlight member</td>
 * <td>The value is set and the value matches with the output file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testIsKeyword()}</td>
 * <td>value is "larger" from an choice.</td>
 * <td>is a keyword</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>value is a real dimension value -- "12pt".</td>
 * <td>not a keyword</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetMeasure()}</td>
 * <td>dimension is from an choice</td>
 * <td>return should be 0.0</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>a real dimension</td>
 * <td>return is the numeric part of it.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>value is a dimension value of a highlight member</td>
 * <td>The measure matches with the set value.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetUnits()}</td>
 * <td>dimension is from an choice</td>
 * <td>return should be <code>DimensionValue.DEFAULT_UNIT </code></td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>a real dimension</td>
 * <td>return should be the corresponding unit of it.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>value is a dimension value of a highlight member</td>
 * <td>The unit matches with the set value.</td>
 * </tr>
 * 
 */

public class DimensionHandleTest extends BaseTestCase
{

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		openDesign( "DimensionHandleTest.xml" ); //$NON-NLS-1$
		assertNotNull( designHandle );
	}

	/**
	 * test getDisplayValue().
	 * <p>
	 * 1. value is from an choice, display value is from message file of the
	 * locale.
	 * <p>
	 * 2. value is a dimension value, locale is English, China display value is
	 * locale-specific.
	 */

	public void testGetDisplayValue( )
	{
		StyleHandle styleHandle = designHandle.findStyle( "My-Style" ); //$NON-NLS-1$
		DimensionHandle fontSizeHandle = styleHandle.getFontSize( );
		assertEquals( DesignChoiceConstants.UNITS_PT, fontSizeHandle
				.getDefaultUnit( ) );
		DimensionHandle backGroundPosXHandle = styleHandle
				.getBackGroundPositionX( );

		ThreadResources.setLocale( TEST_LOCALE );

		assertEquals( "\u5927\u4e00\u53f7", fontSizeHandle.getDisplayValue( ) ); //$NON-NLS-1$
		assertEquals( "12,000.123cm", backGroundPosXHandle.getDisplayValue( ) ); //$NON-NLS-1$

		ThreadResources.setLocale( ULocale.ENGLISH );
		assertEquals( "Larger", fontSizeHandle.getDisplayValue( ) ); //$NON-NLS-1$
		assertEquals( "12,000.123cm", backGroundPosXHandle.getDisplayValue( ) ); //$NON-NLS-1$

		Iterator highlightHandles = styleHandle.highlightRulesIterator( );
		assertNotNull( highlightHandles );
		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles
				.next( );
		assertNotNull( highlightHandle );

		// the dimension in the first highlight rule.

		DimensionHandle dimensionHandle = highlightHandle.getFontSize( );
		ThreadResources.setLocale( TEST_LOCALE );
		assertEquals( "\u4e2d\u53f7", dimensionHandle.getDisplayValue( ) ); //$NON-NLS-1$
		ThreadResources.setLocale( ULocale.ENGLISH );
		assertEquals( "Medium", dimensionHandle.getDisplayValue( ) ); //$NON-NLS-1$

		// the dimension in the second highlight rule.

		highlightHandle = (HighlightRuleHandle) highlightHandles.next( );
		assertNotNull( highlightHandle );
		dimensionHandle = highlightHandle.getFontSize( );

		ThreadResources.setLocale( ULocale.CHINA );

		assertEquals( "10mm", dimensionHandle.getDisplayValue( ) ); //$NON-NLS-1$

		ThreadResources.setLocale( ULocale.ENGLISH );

		assertEquals( "10mm", dimensionHandle.getDisplayValue( ) ); //$NON-NLS-1$

	}

	/**
	 * test getStringValue().
	 * <p>
	 * 1. value is from an choice, return should be the choice name.
	 * <p>
	 * 2. value is a real dimension, return should be the dimension in string.
	 * 
	 */

	public void testGetStringValue( )
	{
		StyleHandle styleHandle = designHandle.findStyle( "My-Style" ); //$NON-NLS-1$
		DimensionHandle fontSizeHandle = styleHandle.getFontSize( );

		assertEquals( "larger", fontSizeHandle.getStringValue( ) ); //$NON-NLS-1$

		DimensionHandle backGroundPosXHandle = styleHandle
				.getBackGroundPositionX( );
		assertEquals( "12000.123cm", backGroundPosXHandle.getStringValue( ) ); //$NON-NLS-1$

		Iterator highlightHandles = styleHandle.highlightRulesIterator( );
		assertNotNull( highlightHandles );
		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles
				.next( );
		assertNotNull( highlightHandle );

		// not defined in the design file, without the default value.

		DimensionHandle dimensionHandle = highlightHandle.getBorderRightWidth( );
		assertNotNull( dimensionHandle );
		assertNull( dimensionHandle.getStringValue( ) );

		dimensionHandle = highlightHandle.getBorderTopWidth( );
		assertNotNull( dimensionHandle );
		assertEquals( DesignChoiceConstants.LINE_WIDTH_THIN, dimensionHandle
				.getStringValue( ) );

		dimensionHandle = highlightHandle.getFontSize( );
		assertNotNull( dimensionHandle );
		assertEquals( DesignChoiceConstants.FONT_SIZE_MEDIUM, dimensionHandle
				.getStringValue( ) );

		highlightHandle = (HighlightRuleHandle) highlightHandles.next( );
		dimensionHandle = highlightHandle.getFontSize( );
		assertEquals( "10mm", dimensionHandle.getStringValue( ) ); //$NON-NLS-1$

		styleHandle = designHandle.findStyle( "My-Style1" ); //$NON-NLS-1$

		// not defined in the design file and with the default value.

		dimensionHandle = styleHandle.getFontSize( );
		assertEquals( "10pt", dimensionHandle.getStringValue( ) ); //$NON-NLS-1$
	}

	/**
	 * Test setValue( String ) and setValue( double ).
	 * <p>
	 * 1. property type is dimension with choices, set the value to an choice.
	 * <p>
	 * 2. property type is dimension with choices, set the value to "12pt"
	 * <p>
	 * 3. application unit is "cm", set the value to 12( double ), the dimension
	 * should be set using the application unit.
	 * 
	 * @throws Exception
	 *             if the value is invalid or the output file cannot be saved on
	 *             the storage.
	 */

	public void testSetValue( ) throws Exception
	{
		StyleHandle styleHandle = designHandle.findStyle( "My-Style" ); //$NON-NLS-1$
		DimensionHandle fontSizeHandle = styleHandle.getFontSize( );

		assertEquals( "larger", fontSizeHandle.getStringValue( ) ); //$NON-NLS-1$

		fontSizeHandle.setValue( "smaller" ); //$NON-NLS-1$
		assertEquals( "smaller", fontSizeHandle.getStringValue( ) ); //$NON-NLS-1$

		fontSizeHandle.setValue( "12pt" ); //$NON-NLS-1$
		assertEquals( "12pt", fontSizeHandle.getStringValue( ) ); //$NON-NLS-1$

		design.getSession( ).setUnits( DesignChoiceConstants.UNITS_CM );

		try
		{
			fontSizeHandle.setValue( "abc" ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e
							.getErrorCode( ) );
		}

		try
		{
			fontSizeHandle.setValue( "12none-exsit-units" ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e
							.getErrorCode( ) );
		}

		Iterator highlightHandles = styleHandle.highlightRulesIterator( );
		assertNotNull( highlightHandles );
		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles
				.next( );
		assertNotNull( highlightHandle );

		// not defined in the design file, without the default value.

		DimensionHandle dimensionHandle = highlightHandle.getBorderRightWidth( );
		assertNotNull( dimensionHandle );
		ULocale locale = designHandle.getModule().getLocale();
		dimensionHandle.setStringValue(new NumberFormatter(locale).format(12.0) + "mm"); //$NON-NLS-1$

		dimensionHandle = highlightHandle.getFontSize( );
		dimensionHandle.setValue( "medium" ); //$NON-NLS-1$

		styleHandle = designHandle.findStyle( "My-Style1" ); //$NON-NLS-1$

		// not defined in the design file and with the default value.

		dimensionHandle = styleHandle.getFontSize( );
		dimensionHandle.setValue( "14pc" ); //$NON-NLS-1$

		highlightHandles = styleHandle.highlightRulesIterator( );
		assertNotNull( highlightHandles );
		highlightHandle = (HighlightRuleHandle) highlightHandles.next( );
		assertNotNull( highlightHandle );

		dimensionHandle = highlightHandle.getFontSize( );
		dimensionHandle.setValue( "22pt" ); //$NON-NLS-1$

		save( );
		assertTrue( compareFile( "DimensionHandleTest_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Test isKeyword().
	 * <p>
	 * 1. value is "larger" from an choice.
	 * <p>
	 * 2. value is a real dimension value -- "12pt".
	 * 
	 * @throws SemanticException
	 */

	public void testIsKeyword( ) throws SemanticException
	{
		StyleHandle styleHandle = designHandle.findStyle( "My-Style" ); //$NON-NLS-1$

		DimensionHandle fontSizeHandle = styleHandle.getFontSize( );
		assertEquals( "larger", fontSizeHandle.getStringValue( ) ); //$NON-NLS-1$
		assertEquals( true, fontSizeHandle.isKeyword( ) );

		fontSizeHandle.setValue( "12pt" ); //$NON-NLS-1$
		assertEquals( false, fontSizeHandle.isKeyword( ) );
	}

	/**
	 * test getMeasure().
	 * <p>
	 * 1. dimension is from an choice, return should be 0.0
	 * <p>
	 * 2. a real dimension, return is the numeric part of it.
	 * 
	 * @throws SemanticException
	 */

	public void testGetMeasure( ) throws SemanticException
	{
		StyleHandle styleHandle = designHandle.findStyle( "My-Style" ); //$NON-NLS-1$
		DimensionHandle fontSizeHandle = styleHandle.getFontSize( );
		assertEquals( DesignChoiceConstants.FONT_SIZE_LARGER, fontSizeHandle
				.getStringValue( ) );

		assertEquals( 0.0d, fontSizeHandle.getMeasure( ), 0.1 );
		fontSizeHandle.setValue( "12pt" ); //$NON-NLS-1$

		assertEquals( 12d, fontSizeHandle.getMeasure( ), 0.1 );

		Iterator highlightHandles = styleHandle.highlightRulesIterator( );
		assertNotNull( highlightHandles );
		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles
				.next( );
		assertNotNull( highlightHandle );

		DimensionHandle dimensionHandle = highlightHandle.getFontSize( );
		assertEquals( DesignChoiceConstants.FONT_SIZE_MEDIUM, dimensionHandle
				.getStringValue( ) );

		fontSizeHandle.setValue( "15pc" ); //$NON-NLS-1$

		assertEquals( 15d, fontSizeHandle.getMeasure( ), 0.1 );
	}

	/**
	 * Test getUnits().
	 * <p>
	 * 1. dimension is from an choice, return should be
	 * <code>DimensionValue.DEFAULT_UNIT
	 * </code>
	 * <p>
	 * 2. a real dimension, return should be the corresponding unit of it.
	 * 
	 * @throws SemanticException
	 */

	public void testGetUnits( ) throws SemanticException
	{
		StyleHandle styleHandle = designHandle.findStyle( "My-Style" ); //$NON-NLS-1$
		DimensionHandle fontSizeHandle = styleHandle.getFontSize( );
		assertEquals( "larger", fontSizeHandle.getStringValue( ) ); //$NON-NLS-1$

		assertEquals( DimensionValue.DEFAULT_UNIT, fontSizeHandle.getUnits( ) );

		fontSizeHandle.setValue( "12pt" ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.UNITS_PT, fontSizeHandle.getUnits( ) );

		Iterator highlightHandles = styleHandle.highlightRulesIterator( );
		assertNotNull( highlightHandles );
		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles
				.next( );
		assertNotNull( highlightHandle );

		DimensionHandle dimensionHandle = highlightHandle.getFontSize( );
		assertEquals( DesignChoiceConstants.FONT_SIZE_MEDIUM, dimensionHandle
				.getStringValue( ) );

		dimensionHandle.setValue( "15pc" ); //$NON-NLS-1$

		assertEquals( DesignChoiceConstants.UNITS_PC, dimensionHandle
				.getUnits( ) );

		IChoice[] choices = dimensionHandle.getAllowedUnits( );
		assertEquals( 9, choices.length );

	}

	/**
	 * Tests the computed dimension value.
	 */

	public void testComputedValue( )
	{
		FreeFormHandle freeFormHandle1 = (FreeFormHandle) designHandle
				.findElement( "form1" ); //$NON-NLS-1$
		TextItemHandle textHandle1 = (TextItemHandle) designHandle
				.findElement( "text1" ); //$NON-NLS-1$

		// form1 has fontSize and textIndent, while text1 has fontSize only.
		// The font size is from form1

		assertEquals(
				"12px", freeFormHandle1.getStringProperty( Style.FONT_SIZE_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"3em", freeFormHandle1.getStringProperty( Style.TEXT_INDENT_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"15px", textHandle1.getStringProperty( Style.FONT_SIZE_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"3em", textHandle1.getStringProperty( Style.TEXT_INDENT_PROP ) ); //$NON-NLS-1$

		assertEquals( "12px", freeFormHandle1.getDimensionProperty( //$NON-NLS-1$
				Style.FONT_SIZE_PROP ).getAbsoluteValue( ).toString( ) );
		assertEquals( "36px", freeFormHandle1.getDimensionProperty( //$NON-NLS-1$
				Style.TEXT_INDENT_PROP ).getAbsoluteValue( ).toString( ) );
		assertEquals( "36px", textHandle1.getDimensionProperty( //$NON-NLS-1$
				Style.TEXT_INDENT_PROP ).getAbsoluteValue( ).toString( ) );

		// textIndent is computed with the font size from text2

		FreeFormHandle freeFormHandle2 = (FreeFormHandle) designHandle
				.findElement( "form2" ); //$NON-NLS-1$
		TextItemHandle textHandle2 = (TextItemHandle) designHandle
				.findElement( "text2" ); //$NON-NLS-1$

		assertEquals( "12px", freeFormHandle2.getDimensionProperty( //$NON-NLS-1$
				Style.FONT_SIZE_PROP ).getAbsoluteValue( ).toString( ) );
		assertEquals( "15px", textHandle2.getDimensionProperty( //$NON-NLS-1$
				Style.FONT_SIZE_PROP ).getAbsoluteValue( ).toString( ) );
		assertEquals( "45px", textHandle2.getDimensionProperty( //$NON-NLS-1$
				Style.TEXT_INDENT_PROP ).getAbsoluteValue( ).toString( ) );

		// textIndent is computed with the font size from MyStyle

		FreeFormHandle freeFormHandle3 = (FreeFormHandle) designHandle
				.findElement( "form3" ); //$NON-NLS-1$
		TextItemHandle textHandle3 = (TextItemHandle) designHandle
				.findElement( "text3" ); //$NON-NLS-1$

		assertEquals( "14pt", freeFormHandle3.getDimensionProperty( //$NON-NLS-1$
				Style.FONT_SIZE_PROP ).getAbsoluteValue( ).toString( ) );
		assertEquals( "70pt", textHandle3.getDimensionProperty( //$NON-NLS-1$
				Style.TEXT_INDENT_PROP ).getAbsoluteValue( ).toString( ) );

		// textIndent is computed with the font size from default

		FreeFormHandle freeFormHandle4 = (FreeFormHandle) designHandle
				.findElement( "form4" ); //$NON-NLS-1$
		TextItemHandle textHandle4 = (TextItemHandle) designHandle
				.findElement( "text4" ); //$NON-NLS-1$

		assertEquals( "12pt", freeFormHandle4.getDimensionProperty( //$NON-NLS-1$
				Style.FONT_SIZE_PROP ).getAbsoluteValue( ).toString( ) );
		assertEquals( "36pt", textHandle4.getDimensionProperty( //$NON-NLS-1$
				Style.TEXT_INDENT_PROP ).getAbsoluteValue( ).toString( ) );

		// textIndent is computed with the font size from form5

		FreeFormHandle freeFormHandle5 = (FreeFormHandle) designHandle
				.findElement( "form5" ); //$NON-NLS-1$
		TextItemHandle textHandle5 = (TextItemHandle) designHandle
				.findElement( "text5" ); //$NON-NLS-1$

		assertEquals( "20px", freeFormHandle5.getDimensionProperty( //$NON-NLS-1$
				Style.FONT_SIZE_PROP ).getAbsoluteValue( ).toString( ) );
		assertEquals( "60px", textHandle5.getDimensionProperty( //$NON-NLS-1$
				Style.TEXT_INDENT_PROP ).getAbsoluteValue( ).toString( ) );

		// textIndent is computed with the font size from form6

		FreeFormHandle freeFormHandle6 = (FreeFormHandle) designHandle
				.findElement( "form6" ); //$NON-NLS-1$
		FreeFormHandle innerFreeFormHandle6 = (FreeFormHandle) designHandle
				.findElement( "innerForm6" ); //$NON-NLS-1$
		TextItemHandle textHandle6 = (TextItemHandle) designHandle
				.findElement( "text6" ); //$NON-NLS-1$

		assertEquals( "19px", freeFormHandle6.getDimensionProperty( //$NON-NLS-1$
				Style.FONT_SIZE_PROP ).getAbsoluteValue( ).toString( ) );
		assertEquals( "57px", textHandle6.getDimensionProperty( //$NON-NLS-1$
				Style.TEXT_INDENT_PROP ).getAbsoluteValue( ).toString( ) );

		// BACKGROUND_POSITION_X_PROP can not be inherit.

		assertEquals( "19px", freeFormHandle6.getDimensionProperty( //$NON-NLS-1$
				Style.FONT_SIZE_PROP ).getAbsoluteValue( ).toString( ) );
		assertEquals( "38px", innerFreeFormHandle6.getDimensionProperty( //$NON-NLS-1$
				Style.BACKGROUND_POSITION_X_PROP ).getAbsoluteValue( )
				.toString( ) );
		assertEquals( "0px", textHandle6.getDimensionProperty( //$NON-NLS-1$
				Style.BACKGROUND_POSITION_X_PROP ).getAbsoluteValue( )
				.toString( ) );

		// textIndent is computed with the font size from form7

		TextItemHandle textHandle7 = (TextItemHandle) designHandle
				.findElement( "text7" ); //$NON-NLS-1$
		assertEquals( "18px", textHandle7.getDimensionProperty( //$NON-NLS-1$
				Style.TEXT_INDENT_PROP ).getAbsoluteValue( ).toString( ) );

		TextItemHandle textHandle9 = (TextItemHandle) designHandle
				.findElement( "text9" ); //$NON-NLS-1$
		FreeFormHandle innerFreeFormHandle9 = (FreeFormHandle) designHandle
				.findElement( "innerForm9" ); //$NON-NLS-1$
		assertEquals( "20px", innerFreeFormHandle9.getDimensionProperty( //$NON-NLS-1$
				Style.FONT_SIZE_PROP ).getAbsoluteValue( ).toString( ) );
		assertEquals( "30px", textHandle9.getDimensionProperty( //$NON-NLS-1$
				Style.TEXT_INDENT_PROP ).getAbsoluteValue( ).toString( ) );
	}

	/**
	 * Tests the computation the relative dimension value with the relative
	 * value.
	 * 
	 * @throws SemanticException
	 */

	public void testFontSizeAbsoluteValue( ) throws SemanticException
	{
		FreeFormHandle freeFormHandle8 = (FreeFormHandle) designHandle
				.findElement( "form8" ); //$NON-NLS-1$
		TextItemHandle textHandle8 = (TextItemHandle) designHandle
				.findElement( "text8" ); //$NON-NLS-1$

		DimensionHandle fontSizeOnForm = freeFormHandle8
				.getDimensionProperty( Style.FONT_SIZE_PROP );

		DimensionHandle fontSizeOnText = textHandle8
				.getDimensionProperty( Style.FONT_SIZE_PROP );

		fontSizeOnText.setStringValue( DesignChoiceConstants.FONT_SIZE_SMALL );
		assertEquals( DesignChoiceConstants.FONT_SIZE_SMALL, fontSizeOnText
				.getValue( ).toString( ) );
		assertEquals( "10pt", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

		// The absolute font size in container
		fontSizeOnForm.setStringValue( "12px" ); //$NON-NLS-1$
		assertEquals( "12px", fontSizeOnForm.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "12px", fontSizeOnForm.getValue( ).toString( ) ); //$NON-NLS-1$

		fontSizeOnText.setStringValue( "2em" ); //$NON-NLS-1$
		assertEquals( "2em", fontSizeOnText.getValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "24px", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

		fontSizeOnText.setStringValue( "3ex" ); //$NON-NLS-1$
		assertEquals( "3ex", fontSizeOnText.getValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "18px", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

		fontSizeOnText.setStringValue( "50%" ); //$NON-NLS-1$
		assertEquals( "50%", fontSizeOnText.getValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "6px", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

		// The relative font size in container

		fontSizeOnForm.setStringValue( "2em" ); //$NON-NLS-1$
		assertEquals( "24pt", fontSizeOnForm.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "2em", fontSizeOnForm.getValue( ).toString( ) ); //$NON-NLS-1$

		fontSizeOnText.setStringValue( "2em" ); //$NON-NLS-1$
		assertEquals( "2em", fontSizeOnText.getValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "48pt", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

		fontSizeOnText.setStringValue( "3ex" ); //$NON-NLS-1$
		assertEquals( "3ex", fontSizeOnText.getValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "36pt", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

		fontSizeOnText.setStringValue( "50%" ); //$NON-NLS-1$
		assertEquals( "50%", fontSizeOnText.getValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "12pt", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

		// The no font size in container

		fontSizeOnForm.setStringValue( null );
		assertEquals( "12pt", fontSizeOnForm.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.FONT_SIZE_MEDIUM, fontSizeOnForm
				.getValue( ).toString( ) );

		fontSizeOnText.setStringValue( "2em" ); //$NON-NLS-1$
		assertEquals( "2em", fontSizeOnText.getValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "24pt", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

		fontSizeOnText.setStringValue( "3ex" ); //$NON-NLS-1$
		assertEquals( "3ex", fontSizeOnText.getValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "18pt", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

		fontSizeOnText.setStringValue( "50%" ); //$NON-NLS-1$
		assertEquals( "50%", fontSizeOnText.getValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "6pt", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

		// The font size constant in container

		fontSizeOnForm.setStringValue( DesignChoiceConstants.FONT_SIZE_LARGE );
		assertEquals( "14pt", fontSizeOnForm.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.FONT_SIZE_LARGE, fontSizeOnForm
				.getValue( ).toString( ) );

		fontSizeOnText.setStringValue( "2em" ); //$NON-NLS-1$
		assertEquals( "2em", fontSizeOnText.getValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "28pt", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

		fontSizeOnText.setStringValue( "3ex" ); //$NON-NLS-1$
		assertEquals( "3ex", fontSizeOnText.getValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "21pt", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

		fontSizeOnText.setStringValue( "50%" ); //$NON-NLS-1$
		assertEquals( "50%", fontSizeOnText.getValue( ).toString( ) ); //$NON-NLS-1$
		assertEquals( "7pt", fontSizeOnText.getAbsoluteValue( ).toString( ) ); //$NON-NLS-1$

	}

	/**
	 * Tests all the cases without a unit.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testUnits( ) throws Exception
	{
		List errors = designHandle.getErrorList( );
		assertEquals( 1, errors.size( ) );

		// During parser, if a dimension has no unit, we will add
		// the default unit to it.

		FreeFormHandle freeFormHandle1 = (FreeFormHandle) designHandle
				.findElement( "form1" ); //$NON-NLS-1$
		DimensionHandle dimensionHandle = freeFormHandle1
				.getDimensionProperty( IReportItemModel.X_PROP );
		assertEquals(
				"12" + dimensionHandle.getDefaultUnit( ), freeFormHandle1.getX( ).getStringValue( ) ); //$NON-NLS-1$
	}

	/**
	 * DesignSession.initDefaultTOCStyle() set the locale as English in default.
	 * This was wrong. Adds this test case to use DesignSession.locale instead.
	 * 
	 * @throws Exception
	 */

	public void testLocale( ) throws Exception
	{
		createDesign( new ULocale( "fi", "FI" ) ); //$NON-NLS-1$//$NON-NLS-2$

		ImageHandle image = designHandle.getElementFactory( ).newImage(
				"image1" ); //$NON-NLS-1$
		designHandle.getBody( ).add( image );

		image.setWidth( "3,3cm" ); //$NON-NLS-1$
		assertEquals( "3.3cm", image.getWidth( ).getStringValue( ) ); //$NON-NLS-1$
	}
}