/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.adapter.internal.script;

import junit.framework.TestCase;

import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.timeFunction.DateTimeUtility;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

public class DateTimeUtillityTest  extends TestCase
{
	public void testDate1( ) throws AdapterException
	{
		int dayOfmonth = DateTimeUtility.getPortion("2003-11-4 8:0:0.0+0000", DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH, null);
		assertTrue( dayOfmonth == 4 );
		int dayOfweek = DateTimeUtility.getPortion("2003-11-4 8:0:0.0+0000", DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK, null);
		assertTrue( dayOfweek == 3 );
		int dayOfyear = DateTimeUtility.getPortion("2003-11-4 8:0:0.0+0000", DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR, null);
		assertTrue( dayOfyear == 308 );
		int month = DateTimeUtility.getPortion("2003-11-4 8:0:0.0+0000", DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH, null);
		assertTrue( month == 11 );
		int year = DateTimeUtility.getPortion("2003-11-4 8:0:0.0+0000", DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR, null);
		assertTrue( year == 2003 );
	}
}
