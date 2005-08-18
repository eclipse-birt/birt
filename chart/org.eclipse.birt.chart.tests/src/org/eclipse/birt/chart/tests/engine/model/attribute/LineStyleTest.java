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
import org.eclipse.birt.chart.model.attribute.LineStyle;

public class LineStyleTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( LineStyle.SOLID, 0 );
		assertEquals( LineStyle.DASHED, 1 );		
		assertEquals( LineStyle.DOTTED, 2 );	
		assertEquals( LineStyle.DASH_DOTTED, 3 );	
	}
	
	public void testGet() 
	{
		assertEquals( LineStyle.SOLID_LITERAL, LineStyle.get(LineStyle.SOLID) );
		assertEquals( LineStyle.DASHED_LITERAL, LineStyle.get(LineStyle.DASHED) );
		assertEquals( LineStyle.DOTTED_LITERAL, LineStyle.get(2) );
		assertEquals( LineStyle.DASH_DOTTED_LITERAL, LineStyle.get(3) );
		
		assertEquals( LineStyle.SOLID_LITERAL, LineStyle.get("Solid") );
		assertEquals( LineStyle.DASHED_LITERAL, LineStyle.get("Dashed") );
		assertEquals( LineStyle.DOTTED_LITERAL, LineStyle.get("Dotted") );
		assertEquals( LineStyle.DASH_DOTTED_LITERAL, LineStyle.get("Dash_Dotted") );
		
		assertNull(LineStyle.get("No Match") );
		assertNull(LineStyle.get(4) );
	}
}

