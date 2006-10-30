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

package org.eclipse.birt.report.model.extension;

import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;
import org.eclipse.birt.report.model.metadata.EncryptionHelperExtensionLoader;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataParserException;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the extension pointer of
 * org.eclipse.birt.report.model.encryptionHelper.
 */

public class EncryptionHelperExtensionTest extends BaseTestCase
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		new EncryptionHelperExtensionLoader( ).load( );
	}

	/**
	 * Tests the encryption helper from extension.
	 */

	public void testHelper( )
	{
		IEncryptionHelper helper = MetaDataDictionary.getInstance( )
				.getEncryptionHelper( );
		assertNotNull( helper );

		assertEquals(
				"org.eclipse.birt.report.model.tests.encryptionHelper.EncryptionHelperImpl", //$NON-NLS-1$
				helper.getClass( ).getName( ) );

		String testString = "something"; //$NON-NLS-1$
		assertEquals( " something ", helper.encrypt( testString ) ); //$NON-NLS-1$
		assertEquals( testString, helper.decrypt( helper.encrypt( testString ) ) );
	}

	/**
	 * Tests the encryption helper exists already.
	 */

	public void testExistingHelper( )
	{
		try
		{
			new EncryptionHelperExtensionLoader( ).load( );
			fail( );
		}
		catch ( MetaDataParserException e )
		{
		}
	}
}
