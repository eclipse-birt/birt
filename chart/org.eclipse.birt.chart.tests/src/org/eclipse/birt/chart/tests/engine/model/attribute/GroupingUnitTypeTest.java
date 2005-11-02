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
package org.eclipse.birt.chart.tests.engine.model.attribute;

import junit.framework.TestCase;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;

public class GroupingUnitTypeTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( GroupingUnitType.SECONDS, 0 );
		assertEquals( GroupingUnitType.MINUTES, 1 );
		assertEquals( GroupingUnitType.HOURS, 2 );	
		assertEquals( GroupingUnitType.DAYS, 3 );
		assertEquals( GroupingUnitType.WEEKS, 4 );
		assertEquals( GroupingUnitType.MONTHS, 5 );
		assertEquals( GroupingUnitType.YEARS, 6 );
	}
	
	public void testGet() 
	{		
		assertEquals( GroupingUnitType.SECONDS_LITERAL, GroupingUnitType.get(GroupingUnitType.SECONDS) );
		assertEquals( GroupingUnitType.MINUTES_LITERAL, GroupingUnitType.get(GroupingUnitType.MINUTES) );
		assertEquals( GroupingUnitType.HOURS_LITERAL, GroupingUnitType.get(GroupingUnitType.HOURS) );
		assertEquals( GroupingUnitType.DAYS_LITERAL, GroupingUnitType.get(GroupingUnitType.DAYS) );
		assertEquals( GroupingUnitType.WEEKS_LITERAL, GroupingUnitType.get(4) );
		assertEquals( GroupingUnitType.MONTHS_LITERAL, GroupingUnitType.get(5) );
		assertEquals( GroupingUnitType.YEARS_LITERAL, GroupingUnitType.get(6) );
		assertNull(GroupingUnitType.get("7") ); //$NON-NLS-1$
		
		assertEquals( GroupingUnitType.SECONDS_LITERAL, GroupingUnitType.get("Seconds") ); //$NON-NLS-1$
		assertEquals( GroupingUnitType.MINUTES_LITERAL, GroupingUnitType.get("Minutes") ); //$NON-NLS-1$
		assertEquals( GroupingUnitType.HOURS_LITERAL, GroupingUnitType.get("Hours") ); //$NON-NLS-1$
		assertEquals( GroupingUnitType.DAYS_LITERAL, GroupingUnitType.get("Days") ); //$NON-NLS-1$
		assertEquals( GroupingUnitType.WEEKS_LITERAL, GroupingUnitType.get("Weeks") ); //$NON-NLS-1$
		assertEquals( GroupingUnitType.MONTHS_LITERAL, GroupingUnitType.get("Months") ); //$NON-NLS-1$
		assertEquals( GroupingUnitType.YEARS_LITERAL, GroupingUnitType.get("Years") ); //$NON-NLS-1$
		assertNull(GroupingUnitType.get("No Match") ); //$NON-NLS-1$
	}
}