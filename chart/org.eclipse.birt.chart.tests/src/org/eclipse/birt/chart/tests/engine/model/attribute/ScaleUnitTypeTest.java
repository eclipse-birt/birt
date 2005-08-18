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
import org.eclipse.birt.chart.model.attribute.ScaleUnitType;

public class ScaleUnitTypeTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( ScaleUnitType.SECONDS, 0 );
		assertEquals( ScaleUnitType.MINUTES, 1 );
		assertEquals( ScaleUnitType.HOURS, 2 );	
		assertEquals( ScaleUnitType.DAYS, 3 );
		assertEquals( ScaleUnitType.WEEKS, 4 );
		assertEquals( ScaleUnitType.MONTHS, 5 );
		assertEquals( ScaleUnitType.YEARS, 6 );
	}
	
	public void testGet() 
	{		
		assertEquals( ScaleUnitType.SECONDS_LITERAL, ScaleUnitType.get(ScaleUnitType.SECONDS) );
		assertEquals( ScaleUnitType.MINUTES_LITERAL, ScaleUnitType.get(ScaleUnitType.MINUTES) );
		assertEquals( ScaleUnitType.HOURS_LITERAL, ScaleUnitType.get(ScaleUnitType.HOURS) );
		assertEquals( ScaleUnitType.DAYS_LITERAL, ScaleUnitType.get(ScaleUnitType.DAYS) );
		assertEquals( ScaleUnitType.WEEKS_LITERAL, ScaleUnitType.get(4) );
		assertEquals( ScaleUnitType.MONTHS_LITERAL, ScaleUnitType.get(5) );
		assertEquals( ScaleUnitType.YEARS_LITERAL, ScaleUnitType.get(6) );
		assertNull( ScaleUnitType.get("7") );
		
		assertEquals( ScaleUnitType.SECONDS_LITERAL, ScaleUnitType.get("Seconds") );
		assertEquals( ScaleUnitType.MINUTES_LITERAL, ScaleUnitType.get("Minutes") );
		assertEquals( ScaleUnitType.HOURS_LITERAL, ScaleUnitType.get("Hours") );
		assertEquals( ScaleUnitType.DAYS_LITERAL, ScaleUnitType.get("Days") );
		assertEquals( ScaleUnitType.WEEKS_LITERAL, ScaleUnitType.get("Weeks") );
		assertEquals( ScaleUnitType.MONTHS_LITERAL, ScaleUnitType.get("Months") );
		assertEquals( ScaleUnitType.YEARS_LITERAL, ScaleUnitType.get("Years") );
		assertNull( ScaleUnitType.get("No Match") );
	}
}