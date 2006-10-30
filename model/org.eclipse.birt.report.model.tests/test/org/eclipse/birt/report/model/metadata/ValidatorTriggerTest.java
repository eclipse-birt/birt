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

import junit.framework.TestCase;

/**
 * Tests the validator definition and semantic validation trigger definition.
 */

public class ValidatorTriggerTest extends TestCase
{

	/**
	 * Tests the rom parsing for ValueValidator tag.
	 * <ul>
	 * <li>Attribute "name" is missing.
	 * <li>Attribute "class" is missing.
	 * <li>The class can not be instantiated.
	 * </ul>
	 */

	public void testValueValidatorParse( )
	{
		// The attribute "name" is missing.

		try
		{
			MetaDataDictionary.reset( );
			MetaDataReader.read( this.getClass( ).getResourceAsStream(
					"input/ValidatorDefnTest.def" ) ); //$NON-NLS-1$
			fail( );
		}
		catch ( MetaDataParserException e )
		{
		}

		// The attribute "class" is missing.

		try
		{
			MetaDataDictionary.reset( );
			MetaDataReader.read( this.getClass( ).getResourceAsStream(
					"input/ValidatorDefnTest1.def" ) ); //$NON-NLS-1$
			fail( );
		}
		catch ( MetaDataParserException e )
		{
		}

		// The class can not be instantiated.

		try
		{
			MetaDataDictionary.reset( );
			MetaDataReader.read( this.getClass( ).getResourceAsStream(
					"input/ValidatorDefnTest2.def" ) ); //$NON-NLS-1$
			fail( );
		}
		catch ( MetaDataParserException e )
		{
		}
	}

	/**
	 * Tests the rom parsing for AbstractSemanticValidator tag.
	 * <ul>
	 * <li>Attribute "name" is missing.
	 * <li>Attribute "class" is missing.
	 * <li>The class can not be instantiated.
	 * </ul>
	 */

	public void testSemanticValidatorParse( )
	{
		// The attribute "name" is missing.

		try
		{
			MetaDataDictionary.reset( );
			MetaDataReader.read( this.getClass( ).getResourceAsStream(
					"input/ValidatorDefnTest5.def" ) ); //$NON-NLS-1$
			fail( );
		}
		catch ( MetaDataParserException e )
		{
		}

		// The attribute "class" is missing.

		try
		{
			MetaDataDictionary.reset( );
			MetaDataReader.read( this.getClass( ).getResourceAsStream(
					"input/ValidatorDefnTest6.def" ) ); //$NON-NLS-1$
			fail( );
		}
		catch ( MetaDataParserException e )
		{
		}

		// The class can not be instantiated.

		try
		{
			MetaDataDictionary.reset( );
			MetaDataReader.read( this.getClass( ).getResourceAsStream(
					"input/ValidatorDefnTest7.def" ) ); //$NON-NLS-1$
			fail( );
		}
		catch ( MetaDataParserException e )
		{
		}
	}

	/**
	 * Tests the rom parsing for Trigger tag.
	 * <ul>
	 * <li>Attribute "validator" is missing.
	 * <li>The validator is not found.
	 * </ul>
	 */

	public void testTriggerParse( )
	{
		// The attribute "validator" is missing.

		try
		{
			MetaDataDictionary.reset( );
			MetaDataReader.read( this.getClass( ).getResourceAsStream(
					"input/ValidatorDefnTest3.def" ) ); //$NON-NLS-1$
			fail( );
		}
		catch ( MetaDataParserException e )
		{
		}

		// The validator is not found.

		try
		{
			MetaDataDictionary.reset( );
			MetaDataReader.read( this.getClass( ).getResourceAsStream(
					"input/ValidatorDefnTest4.def" ) ); //$NON-NLS-1$
			fail( );
		}
		catch ( MetaDataParserException e )
		{
		}
	}
}