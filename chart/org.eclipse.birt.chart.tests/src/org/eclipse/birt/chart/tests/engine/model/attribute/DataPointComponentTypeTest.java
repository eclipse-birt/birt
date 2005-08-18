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
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;

public class DataPointComponentTypeTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( DataPointComponentType.BASE_VALUE, 0 );
		assertEquals( DataPointComponentType.ORTHOGONAL_VALUE, 1 );	
		assertEquals( DataPointComponentType.SERIES_VALUE, 2 );	
	}
	
	public void testGet() 
	{
		assertEquals( DataPointComponentType.BASE_VALUE_LITERAL, DataPointComponentType.get(DataPointComponentType.BASE_VALUE) );
		assertEquals( DataPointComponentType.ORTHOGONAL_VALUE_LITERAL, DataPointComponentType.get(1) );
		assertEquals( DataPointComponentType.SERIES_VALUE_LITERAL, DataPointComponentType.get(DataPointComponentType.SERIES_VALUE) );
		
		assertEquals( DataPointComponentType.BASE_VALUE_LITERAL, DataPointComponentType.get("Base_Value") );
		assertEquals( DataPointComponentType.ORTHOGONAL_VALUE_LITERAL, DataPointComponentType.get("Orthogonal_Value") );
		assertEquals( DataPointComponentType.SERIES_VALUE_LITERAL, DataPointComponentType.get("Series_Value") );
		
		assertNull(DataPointComponentType.get("No Match") );
		assertNull(DataPointComponentType.get(3) );
	}
}

