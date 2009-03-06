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

import java.util.List;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;

/**
 * Tests meta trim string operation.
 * 
 */
public class MetaDataStringTrimTest extends AbstractMetaTest
{

	/**
	 * Tests parser the input trim string value and convert it into number. Then
	 * trim the input string according to the option value.
	 * 
	 * @throws Exception
	 */
	public void testTrimString( ) throws Exception
	{
		loadMetaData( MetaDataStringTrimTest.class
				.getResourceAsStream( "input/TrimStringRomTest.def" ) ); //$NON-NLS-1$

		IElementDefn elemDefn = MetaDataDictionary.getInstance( ).getElement(
				ReportDesignConstants.REPORT_DESIGN_ELEMENT );
		List<IElementPropertyDefn> propertyList = elemDefn.getProperties( );

		// no trim
		PropertyDefn propDefn = (PropertyDefn) propertyList.get( 0 );
		assertEquals( "noTrim", propDefn.getName( ) ); //$NON-NLS-1$
		assertEquals( " test ", propDefn.validateValue( design, " test " ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "  ", propDefn.validateValue( design, "  " ) ); //$NON-NLS-1$ //$NON-NLS-2$

		// trim space
		propDefn = (PropertyDefn) propertyList.get( 1 );
		assertEquals( "trimSpace", propDefn.getName( ) ); //$NON-NLS-1$
		assertEquals( "test", propDefn.validateValue( design, " test " ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "", propDefn.validateValue( design, "  " ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "", propDefn.validateValue( design, "" ) ); //$NON-NLS-1$ //$NON-NLS-2$

		// trimEmptyToNull
		propDefn = (PropertyDefn) propertyList.get( 2 );
		assertEquals( "trimEmptyToNull", propDefn.getName( ) ); //$NON-NLS-1$
		assertEquals( " test ", propDefn.validateValue( design, " test " ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "   ", propDefn.validateValue( design, "   " ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull( propDefn.validateValue( design, "" ) ); //$NON-NLS-1$ 

		// trimSpace and trimEmptyToNull
		propDefn = (PropertyDefn) propertyList.get( 3 );
		assertEquals( "trimEmptyAndNull", propDefn.getName( ) ); //$NON-NLS-1$
		assertEquals( "test", propDefn.validateValue( design, " test " ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull( propDefn.validateValue( design, "   " ) ); //$NON-NLS-1$ 
		assertNull( propDefn.validateValue( design, "" ) ); //$NON-NLS-1$ 

		// trim option has no value
		propDefn = (PropertyDefn) propertyList.get( 4 );
		assertEquals( "noTrimOptionValue", propDefn.getName( ) ); //$NON-NLS-1$
		assertEquals( "test", propDefn.validateValue( design, " test " ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull( propDefn.validateValue( design, "   " ) ); //$NON-NLS-1$ 
		assertNull( propDefn.validateValue( design, "" ) ); //$NON-NLS-1$ 

	}
}
