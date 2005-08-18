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
import org.eclipse.birt.chart.model.attribute.DateFormatDetail;

public class DateFormatDetailTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( DateFormatDetail.DATE, 0 );
		assertEquals( DateFormatDetail.DATE_TIME, 1 );		
	}
	
	public void testGet() 
	{
		assertEquals( DateFormatDetail.DATE_LITERAL, DateFormatDetail.get(DateFormatDetail.DATE) );
		assertEquals( DateFormatDetail.DATE_TIME_LITERAL, DateFormatDetail.get(1) );
		
		assertEquals( DateFormatDetail.DATE_LITERAL, DateFormatDetail.get("Date") );
		assertEquals( DateFormatDetail.DATE_TIME_LITERAL, DateFormatDetail.get("Date_Time") );
		
		assertNull(DateFormatDetail.get("No Match") );
		assertNull(DateFormatDetail.get(2));
	}
}