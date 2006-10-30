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

package org.eclipse.birt.report.model.i18n;

import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test for ResourceHandle. The message files are named like
 * "Messages.properties", "Messages_xx.properties".
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testGetMessage()}</td>
 * <td>Given the key, get a localized message from the corresponding resource
 * bundle.</td>
 * <td>The name returned should be the same as defined in the corresponding
 * message files.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetMessage()}</td>
 * <td>Get a message that has placeholders.</td>
 * <td>The placeholders in the message should be replaced by the input
 * parameters.</td>
 * </tr>
 * </table>
 * 
 */
public class ResourceHandleTest extends BaseTestCase
{

	ModelResourceHandle resourceHandle = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );

		resourceHandle = new ModelResourceHandle( ULocale.ENGLISH );
	}

	/**
	 * Test getMessage(). Get a localized name from the corresponding resource
	 * bundle.
	 */
	public void testGetMessage( )
	{
		String msg = resourceHandle.getMessage( "Choices.colors.maroon" ); //$NON-NLS-1$
		assertEquals( "Maroon", msg ); //$NON-NLS-1$

		msg = resourceHandle.getMessage( "Element.ReportDesign" ); //$NON-NLS-1$
		assertEquals( "Report Design", msg ); //$NON-NLS-1$
	}

	/**
	 * Test getMessage() with parameters.
	 */
	public void testGetMessageWithParameters( )
	{
		String msg = resourceHandle.getMessage(
				"Error.Msg001", new String[]{"Element", "NameSpace"} ); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		assertEquals( "Element not found in NameSpace.", msg ); //$NON-NLS-1$
	}
}