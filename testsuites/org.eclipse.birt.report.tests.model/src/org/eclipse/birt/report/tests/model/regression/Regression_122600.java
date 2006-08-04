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

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Top n, bottom n, top percent, bottom percent, match and like are not found in
 * "edit highlight" dialog.
 * </p>
 * Test description:
 * <p>
 * Check that those value are listed in highlight(map) operator choice Note:top
 * percent, bottom percent are removed
 */
public class Regression_122600 extends BaseTestCase
{

	public void test_122600( )
	{
		MetaDataDictionary instance = MetaDataDictionary.getInstance( );

		IChoiceSet choiceset = instance.getChoiceSet( "mapOperator" ); //$NON-NLS-1$

		IChoice[] naturedSortedChoices = choiceset.getChoices( null );

		assertEquals( "top-n", naturedSortedChoices[17].getName( ) );
		assertEquals( "bottom-n", naturedSortedChoices[1].getName( ) );
		assertEquals( "like", naturedSortedChoices[10].getName( ) );
		assertEquals( "match", naturedSortedChoices[12].getName( ) );
	}
}
