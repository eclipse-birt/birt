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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test cases for HighlightRule.
 * 
 */

public class HighlightRuleHandleTest extends BaseTestCase
{

	private static final String inputFile = "HighlightRuleHandleTest.xml"; //$NON-NLS-1$

	/**
	 * Tested cases:
	 * 
	 * <ul>
	 * <li>The getProperty() algorithm. If the structure member has no local
	 * value, uses values of the referred style.
	 * <li>The back reference must be right for undo/redo.
	 * <li>The back reference must be right if the style member is set to a new
	 * value.
	 * <li>Circular references must throw exceptions.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testStyle( ) throws Exception
	{
		openDesign( inputFile );
		StyleHandle style2 = designHandle.findStyle( "My Style2" ); //$NON-NLS-1$
		StyleHandle style3 = designHandle.findStyle( "My Style3" ); //$NON-NLS-1$
		Iterator highlightRules = style2.highlightRulesIterator( );
		assert ( highlightRules.hasNext( ) );

		HighlightRuleHandle style2Highlight = (HighlightRuleHandle) highlightRules
				.next( );
		assertEquals( DesignChoiceConstants.TEXT_ALIGN_RIGHT, style2Highlight
				.getTextAlign( ) );
		assertEquals( ColorPropertyType.RED, style2Highlight.getColor( )
				.getStringValue( ) );

		StyleHandle style1 = designHandle.findStyle( "My Style1" ); //$NON-NLS-1$
		List refs = ( (ReferenceableElement) style1.getElement( ) )
				.getClientList( );
		assertEquals( 1, refs.size( ) );
		BackRef ref1 = (BackRef) refs.get( 0 );
		assertEquals( "My Style2", ref1.getElement( ).getName( ) ); //$NON-NLS-1$
		assertEquals( "highlightRules", ref1.getPropertyName( ) ); //$NON-NLS-1$

		// if remove the structure, the back reference should be break.

		style2Highlight.drop( );

		refs = ( (ReferenceableElement) style1.getElement( ) ).getClientList( );
		assertEquals( 0, refs.size( ) );

		designHandle.getCommandStack( ).undo( );
		refs = ( (ReferenceableElement) style1.getElement( ) ).getClientList( );
		assertEquals( 1, refs.size( ) );

		designHandle.getCommandStack( ).redo( );
		refs = ( (ReferenceableElement) style1.getElement( ) ).getClientList( );
		assertEquals( 0, refs.size( ) );

		designHandle.getCommandStack( ).undo( );

		// set to the new style.

		// the old reference is dropped

		style2Highlight.setStyle( style3 );
		refs = ( (ReferenceableElement) style1.getElement( ) ).getClientList( );
		assertEquals( 0, refs.size( ) );

		// the new reference is added.

		refs = ( (ReferenceableElement) style3.getElement( ) ).getClientList( );
		assertEquals( 1, refs.size( ) );

		assertEquals( "My Style2", ref1.getElement( ).getName( ) ); //$NON-NLS-1$
		assertEquals( "highlightRules", ref1.getPropertyName( ) ); //$NON-NLS-1$

		// exception test cases.

		try
		{
			style2Highlight.setStyle( style2 );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE,
					e.getErrorCode( ) );
		}

		Iterator style3HighlightRules = style3.highlightRulesIterator( );
		assertTrue( style3HighlightRules.hasNext( ) );

		HighlightRuleHandle style3Highlight = (HighlightRuleHandle) style3HighlightRules
				.next( );

		try
		{
			style3Highlight.setStyleName( "My Style2" ); //$NON-NLS-1$
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE,
					e.getErrorCode( ) );
		}

		// test cases on add item

		HighlightRule newRule1 = StructureFactory.createHighlightRule( );
		newRule1.setProperty( HighlightRule.STYLE_MEMBER, "My Style2" ); //$NON-NLS-1$

		try
		{
			style3.getPropertyHandle( StyleHandle.HIGHLIGHT_RULES_PROP )
					.addItem( newRule1 );
			fail( );
		}
		catch ( SemanticException e )
		{
			System.out.println( e.getLocalizedMessage( ) );
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE,
					e.getErrorCode( ) );
		}
	}
}
