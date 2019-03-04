/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * steps to reproduce:
 * <ol>
 * <li>Create a report, new a custom style "Style", set boderTopStyle,
 * borderTopWidth, borderTopColor.
 * <li>Insert a label, add HighlightRule referring to "Style" for the label.
 * </ol>
 * <p>
 * <b>actual result:</b>
 * <p>
 * HighlightRule can not refer to custom style
 * <p>
 * <b>expected result:</b>
 * <p>
 * HighlightRule can refer to custom style
 * </p>
 * Test description:
 * <p>
 * HighlightRule should reference a Style element instead of having several
 * style properties. With a reference to a defined Style element it would be
 * easier to change the style of hightlight rule just by changing the Style
 * element (which may reside in a library and is used by several reports). The
 * current ROM requires that every report defines it own highlight rule styles.
 * </p>
 * 
 * @author Tianli Zhang
 */
public class Regression_146642 extends BaseTestCase
{

	private final static String INPUT = "Regression_146642.xml"; //$NON-NLS-1$
	private final static String LIB = "Regression_146642_lib.xml"; //$NON-NLS-1$

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );

		copyResource_INPUT( INPUT, INPUT );
		copyResource_INPUT( LIB, LIB );

	}

	public void tearDown( )
	{
		removeResource( );
	}

	/**
	 * test a label with a highlight rule which refer to a style
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_Regression_146642_Test1( ) throws DesignFileException, SemanticException
	{
		openDesign( INPUT );

		// find label
		LabelHandle label = (LabelHandle) designHandle.findElement( "label" );
		assertNotNull( label );

		// find style
		StyleHandle style = designHandle.findStyle( "Style" ); //$NON-NLS-1$
		assertNotNull( style );

		// set style to highlight
		HighlightRule rule = StructureFactory.createHighlightRule( );
		HighlightRuleHandle ruleHandle = (HighlightRuleHandle) label
				.getPropertyHandle( "highlightRules" )
				.addItem( rule );
		ruleHandle.setStyle( style );

		assertNotNull( ruleHandle.getStyle( ) );

		assertEquals( "solid", ruleHandle.getStyle( ).getBorderTopStyle( ) );
		assertEquals( "#FF0000", ruleHandle.getStyle( ).getBorderTopColor( ).getStringValue( ) );
		assertEquals( "thick", ruleHandle.getStyle( ).getBorderTopWidth( ).getValue( ) );
	}

	/**
	 * test a label with a highlight rule which refer to a style
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_Regression_146642_Test2( ) throws DesignFileException, SemanticException
	{
		openDesign( INPUT );

		// find a table
		TableHandle table = (TableHandle) designHandle.findElement( "table" );
		assertNotNull( table );

		// find a predefine style
		StyleHandle style = designHandle.findStyle( "table" );
		assertNotNull( style );

		// create a highlightrule
		HighlightRule rule = StructureFactory.createHighlightRule( );
		HighlightRuleHandle ruleHandle = (HighlightRuleHandle) table
				.getPropertyHandle( "highlightRules" )
				.addItem( rule );
		ruleHandle.setStyle( style );

		assertNotNull( ruleHandle.getStyle( ) );
		assertEquals( "small", ruleHandle.getStyle( ).getFontSize( ).getStringValue( ) );
		assertEquals( "bold", ruleHandle.getStyle( ).getFontWeight( ) );
		assertEquals( "italic", ruleHandle.getStyle( ).getFontStyle( ) );
		assertEquals( "#0000FF", ruleHandle.getColor( ).getStringValue( ) );
		assertEquals( "\"Arial\"", ruleHandle.getStyle( ).getFontFamilyHandle( ).getValue( ) );
	}

	/**
	 * 1.Create a Library, new a custom style "Style", set textUnderline,
	 * textOverline, textLineThrough, textAlign, textIndent, textTransform
	 * insert a text add HighlightRule referring to "Style" for the text. 2.New
	 * a report, extend the text from the library.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_Regression_146642_Test3( ) throws DesignFileException, SemanticException
	{
		openLibrary( LIB );
		// LibraryHandle libHandle = designHandle.getLibrary(
		// "Regression_146642_lib" );

		// find lib.text
		TextItemHandle text = (TextItemHandle) libraryHandle.findElement( "text" );
		assertNotNull( text );

		// find lib.style
		StyleHandle style = libraryHandle.findStyle( "Style3" );
		assertNotNull( style );

		HighlightRule rule = StructureFactory.createHighlightRule( );
		HighlightRuleHandle ruleHandle = (HighlightRuleHandle) text
				.getPropertyHandle( "highlightRules" )
				.addItem( rule );
		ruleHandle.setStyle( style );
		assertNotNull( ruleHandle.getStyle( ) );

		assertEquals( "line-through", ruleHandle.getStyle( ).getTextLineThrough( ) );
		assertEquals( "overline", ruleHandle.getStyle( ).getTextOverline( ) );
		assertEquals( "underline", ruleHandle.getStyle( ).getTextUnderline( ) );
		assertEquals( "center", ruleHandle.getStyle( ).getTextAlign( ) );
		assertEquals( "6pt", ruleHandle.getStyle( ).getTextIndent( ).getStringValue( ) );
		assertEquals( "uppercase", ruleHandle.getStyle( ).getTextTransform( ) );
		assertEquals( "pre", ruleHandle.getStyle( ).getWhiteSpace( ) );

	}

}
