/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests case for multiple view.
 */

public class MultiViewHandleTest extends BaseTestCase
{

	/**
	 * Tests cases about parser and API related.
	 * 
	 * @throws Exception
	 */

	public void testAPIs( ) throws Exception
	{
		createDesign( );

		TableHandle table1 = designHandle.getElementFactory( ).newTableItem(
				"table1" ); //$NON-NLS-1$

		ExtendedItemHandle box1 = designHandle.getElementFactory( )
				.newExtendedItem( "box1", "TestingBox" ); //$NON-NLS-1$//$NON-NLS-2$
		table1.addView( box1 );
		table1.setCurrentView( box1 );

		assertTrue( box1 == table1.getCurrentView( ) );

		table1.dropView( box1 );
		
		assertTrue( table1 == table1.getCurrentView( ) );
	}
}
