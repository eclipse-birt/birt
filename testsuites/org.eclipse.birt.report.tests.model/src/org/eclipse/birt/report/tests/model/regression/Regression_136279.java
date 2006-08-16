/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Description: "Structure" and "table" should be hidden in column data binding
 * data type choice list.
 * 
 * Steps to reproduce: In table data binding, I saw structure and table two
 * datatypes. They are not supported now, so they should be removed.
 * <p>
 * Test description:
 * <p>
 * Make sure that "columnDataType" choice set do not support "structure" and
 * "table" types.
 * <p>
 */
public class Regression_136279 extends BaseTestCase
{

	/**
	 * 
	 */
	public void test_136279( )
	{
		IMetaDataDictionary dict = MetaDataDictionary.getInstance( );
		IChoiceSet datatypes = dict.getChoiceSet( "columnDataType" ); //$NON-NLS-1$
		IChoice[] choices = datatypes.getChoices( );
		assertEquals( "any", choices[0].getName( ) );//$NON-NLS-1$
		assertEquals( "integer", choices[1].getName( ) );//$NON-NLS-1$
		assertEquals( "string", choices[2].getName( ) );//$NON-NLS-1$
		assertEquals( "date-time", choices[3].getName( ) );//$NON-NLS-1$
		assertEquals( "decimal", choices[4].getName( ) );//$NON-NLS-1$
		assertEquals( "float", choices[5].getName( ) );//$NON-NLS-1$
		assertEquals( 6, choices.length );
	}
}
