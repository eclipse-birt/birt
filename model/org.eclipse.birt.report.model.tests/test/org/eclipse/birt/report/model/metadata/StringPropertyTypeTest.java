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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;

/**
 * Test case for StringPropertyType.
 * 
 */
public class StringPropertyTypeTest extends PropertyTypeTestCase
{

	StringPropertyType type = new StringPropertyType( );

	PropertyDefn propDefn = new PropertyDefnFake( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode()
	 */
	public void testGetTypeCode( )
	{
		assertEquals( PropertyType.STRING_TYPE, type.getTypeCode( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	public void testGetName( )
	{
		assertEquals( PropertyType.STRING_TYPE_NAME, type.getName( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateValue()
	 */
	public void testValidateValue( ) throws PropertyValueException
	{
        assertEquals( null, type.validateValue( design, propDefn, null ) );
        assertEquals( null, type.validateValue( design, propDefn, "" ) ); //$NON-NLS-1$
		assertEquals( "    ", type.validateValue( design, propDefn, "    " ) ); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals( "abc", type.validateValue( design, propDefn, "abc" ) ); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(
				"123", type.validateValue( design, propDefn, new Integer( 123 ) ) ); //$NON-NLS-1$
		assertEquals(
				"123.0", type.validateValue( design, propDefn, new Float( 123.0f ) ) ); //$NON-NLS-1$
		assertEquals(
				"123.0", type.validateValue( design, propDefn, new Double( 123.0d ) ) ); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateInputString()
	 */
	public void testValidateInputString( ) throws PropertyValueException
	{
		// covered.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateXml()
	 */
	public void testValidateXml( ) throws PropertyValueException
	{
        assertEquals( null, type.validateValue( design, propDefn, null ) );
        assertEquals( null, type.validateValue( design, propDefn, "" ) ); //$NON-NLS-1$
		assertEquals( "    ", type.validateValue( design, propDefn, "    " ) );  //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(
				"any-input", type.validateXml( design, propDefn, "any-input" ) ); //$NON-NLS-1$//$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble()
	 */
	public void testToDouble( )
	{
		assertEquals( 0.0d, type.toDouble( design, "any-input" ), 1 ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger()
	 */
	public void testToInteger( )
	{
		assertEquals( 123, type.toInteger( design, "123" ) ); //$NON-NLS-1$
		assertEquals( 0, type.toInteger( design, null ) ); 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	public void testToXml( )
	{
		assertEquals( "any-input", type.toXml( design, propDefn, "any-input" ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString()
	 */
	public void testToString( )
	{
		assertEquals(
				"any-input", type.toString( design, propDefn, "any-input" ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDisplayString()
	 */
	public void testToDisplayString( )
	{
		assertEquals(
				"any-input", type.toDisplayString( design, propDefn, "any-input" ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber()
	 */
	public void testToNumber( )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToBoolean()
	 */
	public void testToBoolean( )
	{
	}

}
