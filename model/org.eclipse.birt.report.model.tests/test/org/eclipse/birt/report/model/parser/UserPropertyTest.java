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

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.core.MultiElementSlot;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementRefPropertyType;
import org.eclipse.birt.report.model.metadata.ExtendsPropertyType;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyType;
import org.eclipse.birt.report.model.metadata.StructRefPropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The Test Case of user-defined properties parse.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testHandlerError()}</td>
 * <td>test the parse errors in design file, such as missing name, duplicate
 * property defn, wrong type. When the type is a choice and no choices are
 * provided. The value of the user-defined choice is missing.</td>
 * <td>The error count is 6</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testHandler()}</td>
 * <td>parse the design file and check the related content of user-defined
 * property.</td>
 * <td>Content of the property is consistent with the design file</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testWrite()}</td>
 * <td>parse, write and parse, write again. The result of two writer files is
 * the same.</td>
 * <td>The two writer file is the same.</td>
 * </tr>
 * 
 * </table>
 * 
 */

public class UserPropertyTest extends BaseTestCase
{

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		openDesign( "UserPropertyTest.xml" ); //$NON-NLS-1$ 	

	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
	}

	/**
	 * Test the exceptions parsing the user-defined properties.
	 * 
	 */

	public void testHandlerError( )
	{
		try
		{
			openDesign( "UserPropertyTest_1.xml" ); //$NON-NLS-1$ 	
		}
		catch ( DesignFileException e )
		{
			assertEquals( 4, e.getErrorList( ).size( ) );

			int i = 0;
			assertEquals(
					UserPropertyException.DESIGN_EXCEPTION_DUPLICATE_NAME,
					( (ErrorDetail) e.getErrorList( ).get( i++ ) )
							.getErrorCode( ) );
			assertEquals(
					UserPropertyException.DESIGN_EXCEPTION_CHOICE_VALUE_REQUIRED,
					( (ErrorDetail) e.getErrorList( ).get( i++ ) )
							.getErrorCode( ) );
			assertEquals(
					DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY,
					( (ErrorDetail) e.getErrorList( ).get( i++ ) )
							.getErrorCode( ) );
			assertEquals(
					DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY,
					( (ErrorDetail) e.getErrorList( ).get( i++ ) )
							.getErrorCode( ) );
		}
	}

	/**
	 * Test the write for user-defined properties.
	 * 
	 * @throws Exception
	 */

	public void testWrite( ) throws Exception
	{
		openDesign( "UserPropertyTest.xml" ); //$NON-NLS-1$ 	

		saveAs( "UserPropertyTest_out.xml" ); //$NON-NLS-1$
		assertTrue( compareTextFile( "UserPropertyTest_golden.xml", //$NON-NLS-1$
				"UserPropertyTest_out.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests getProperty/setProperty methods in UserPropertyDefn.
	 * 
	 * @throws Exception
	 */

	public void testProperties( ) throws Exception
	{
		UserPropertyDefn prop = new UserPropertyDefn( );

		IStructureDefn structDefn = prop.getStructDefn( );
		StructPropertyDefn memberDefn = (StructPropertyDefn) structDefn
				.getMember( UserPropertyDefn.NAME_MEMBER );

		prop.setProperty( memberDefn, "new name" ); //$NON-NLS-1$
		assertEquals( "new name", prop.getProperty( null, memberDefn ) ); //$NON-NLS-1$

		memberDefn = (StructPropertyDefn) structDefn
				.getMember( UserPropertyDefn.TYPE_MEMBER );
		prop.setProperty( memberDefn, PropertyType.BOOLEAN_TYPE_NAME );
		assertEquals( PropertyType.BOOLEAN_TYPE_NAME, prop.getProperty( null,
				memberDefn ) );

		memberDefn = (StructPropertyDefn) structDefn
				.getMember( UserPropertyDefn.DISPLAY_NAME_MEMBER );
		prop.setProperty( memberDefn, "new display name" ); //$NON-NLS-1$
		assertEquals( "new display name", prop.getProperty( null, memberDefn ) ); //$NON-NLS-1$

		memberDefn = (StructPropertyDefn) structDefn
				.getMember( UserPropertyDefn.DISPLAY_NAME_ID_MEMBER );
		prop.setProperty( memberDefn, "new display name id" ); //$NON-NLS-1$
		assertEquals( "new display name id", prop //$NON-NLS-1$
				.getProperty( null, memberDefn ) );

	}

	/**
	 * Test the properties for user-defined properties.
	 * 
	 * @throws Exception
	 */

	public void testHandler( ) throws Exception
	{
		openDesign( "UserPropertyTest.xml" ); //$NON-NLS-1$ 	

		MultiElementSlot lists = (MultiElementSlot) design
				.getSlot( ReportDesign.COMPONENT_SLOT );

		assertEquals( 2, lists.getCount( ) );
		ListItem list = (ListItem) design.findElement( "My List" ); //$NON-NLS-1$
		assertNotNull( list );
		assertEquals( 7, list.getUserProperties( ).size( ) );
		
		List userProperties = (List)list.getUserProperties();
		assertEquals( "myProp", ((UserPropertyDefn) userProperties.get( 0 )).getName() );  //$NON-NLS-1$
		assertEquals( "myProp1", ((UserPropertyDefn) userProperties.get( 1 )).getName() ); //$NON-NLS-1$
		assertEquals( "myProp5", ((UserPropertyDefn) userProperties.get( 2 )).getName() ); //$NON-NLS-1$
		assertEquals( "myProp6", ((UserPropertyDefn) userProperties.get( 3 )).getName() ); //$NON-NLS-1$
		assertEquals( "myProp3", ((UserPropertyDefn) userProperties.get( 4 )).getName() ); //$NON-NLS-1$
		assertEquals( "myProp2", ((UserPropertyDefn) userProperties.get( 5 )).getName() ); //$NON-NLS-1$
		assertEquals( "parentProp", ((UserPropertyDefn) userProperties.get( 6 )).getName() ); //$NON-NLS-1$
		
		UserPropertyDefn propDefn = list.getUserPropertyDefn( "myProp1" ); //$NON-NLS-1$
		assertNotNull( propDefn );
		assertEquals( "string", propDefn.getType( ).getName( ) ); //$NON-NLS-1$
		assertEquals( "abcde", propDefn.getDisplayNameID( ) ); //$NON-NLS-1$
		propDefn = list.getUserPropertyDefn( "myProp2" ); //$NON-NLS-1$
		assertNotNull( propDefn );
		assertEquals( "choice", propDefn.getType( ).getName( ) ); //$NON-NLS-1$
		assertEquals( "abc", propDefn.getDisplayNameID( ) ); //$NON-NLS-1$
		IChoiceSet choiceSet = propDefn.getChoices( );
		assertNotNull( choiceSet );
		IChoice[] choices = choiceSet.getChoices( );
		assertEquals( 3, choices.length );

		UserPropertyDefn prop = new UserPropertyDefn( );
		prop.setName( "test" ); //$NON-NLS-1$
		PropertyType typeDefn = MetaDataDictionary.getInstance( )
				.getPropertyType( PropertyType.ELEMENT_REF_NAME );
		prop.setType( typeDefn );
		try
		{
			list.getHandle( design ).addUserPropertyDefn( prop );

		}
		catch ( UserPropertyException e )
		{
			assertEquals( UserPropertyException.DESIGN_EXCEPTION_INVALID_TYPE,
					e.getErrorCode( ) );
		}
	}

	/**
	 * Tests GetAllowedType method in UserPropertyDefn.
	 * 
	 * @throws Exception
	 */

	public void testGetAllowedType( ) throws Exception
	{
		assertTrue( !UserPropertyDefn.getAllowedTypes( ).contains(
				new StructRefPropertyType( ) ) );
		assertTrue( !UserPropertyDefn.getAllowedTypes( ).contains(
				new ElementRefPropertyType( ) ) );
		assertTrue( !UserPropertyDefn.getAllowedTypes( ).contains(
				new StructPropertyType( ) ) );
		assertTrue( !UserPropertyDefn.getAllowedTypes( ).contains(
				new ExtendsPropertyType( ) ) );
	}
}