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

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * No exception for two group element with the same names
 * </p>
 * Test description:
 * <p>
 * Add two groups with the same name to the table, NameException should be thrown out
 * </p>
 */

public class Regression_73182 extends BaseTestCase
{

	public final static String INPUT = "Reg_73182.xml"; //$NON-NLS-1$

	public Regression_73182( String name )
	{
		super( name );
	}

	public void test( ) throws Exception
	{
		openDesign( INPUT );

		TableHandle table = (TableHandle) designHandle.findElement( "table" );
		ElementFactory factory = designHandle.getElementFactory( );
		TableGroupHandle group1 = factory.newTableGroup( );
		group1.setName( "group1" );
		TableGroupHandle group2 = factory.newTableGroup( );
		table.getGroups( ).add( group1 );
		table.getGroups( ).add( group2 );
		try
		{
			group2.setName( "group1" );
			table.getGroups( ).add( group2 );
			fail( );
		}
		catch ( NameException e )
		{
			assertNotNull( e );
		}
	}
}
