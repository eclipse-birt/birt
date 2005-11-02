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
import org.eclipse.birt.chart.model.attribute.DataType;

public class DataTypeTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( DataType.NUMERIC, 0 );
		assertEquals( DataType.DATE_TIME, 1 );	
		assertEquals( DataType.TEXT, 2 );	
	}
	
	public void testGet() 
	{
		assertEquals( DataType.NUMERIC_LITERAL, DataType.get(DataType.NUMERIC) );
		assertEquals( DataType.DATE_TIME_LITERAL, DataType.get(1) );
		assertEquals( DataType.TEXT_LITERAL, DataType.get(DataType.TEXT) );
		
		assertEquals( DataType.NUMERIC_LITERAL, DataType.get("Numeric") ); //$NON-NLS-1$
		assertEquals( DataType.DATE_TIME_LITERAL, DataType.get("DateTime") ); //$NON-NLS-1$
		assertEquals( DataType.TEXT_LITERAL, DataType.get("Text") ); //$NON-NLS-1$
		
		assertNull(DataType.get("No Match") ); //$NON-NLS-1$
		assertNull(DataType.get(3) );
	}
}
