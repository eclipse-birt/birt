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
import org.eclipse.birt.chart.model.attribute.ChartType;

public class ChartTypeTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( ChartType.PIE, 0 );
		assertEquals( ChartType.BAR, 1 );
		assertEquals( ChartType.LINE, 2 );	
		assertEquals( ChartType.COMBO, 3 );
		assertEquals( ChartType.SCATTER, 4 );
		assertEquals( ChartType.STOCK, 5 );
	}
	
	public void testGet() 
	{
		assertEquals( ChartType.PIE_LITERAL, ChartType.get(ChartType.PIE) );
		assertEquals( ChartType.BAR_LITERAL, ChartType.get(ChartType.BAR) );
		assertEquals( ChartType.LINE_LITERAL, ChartType.get(ChartType.LINE) );
		assertEquals( ChartType.COMBO_LITERAL, ChartType.get(ChartType.COMBO) );
		assertEquals( ChartType.SCATTER_LITERAL, ChartType.get(4) );
		assertEquals( ChartType.STOCK_LITERAL, ChartType.get(5) );
		assertNull(ChartType.get("6") ); //$NON-NLS-1$
		
		assertEquals( ChartType.PIE_LITERAL, ChartType.get("Pie") ); //$NON-NLS-1$
		assertEquals( ChartType.BAR_LITERAL, ChartType.get("Bar") ); //$NON-NLS-1$
		assertEquals( ChartType.LINE_LITERAL, ChartType.get("Line") ); //$NON-NLS-1$
		assertEquals( ChartType.COMBO_LITERAL, ChartType.get("Combo") ); //$NON-NLS-1$
		assertEquals( ChartType.SCATTER_LITERAL, ChartType.get("Scatter") ); //$NON-NLS-1$
		assertEquals( ChartType.STOCK_LITERAL, ChartType.get("Stock") ); //$NON-NLS-1$
		assertNull(ChartType.get("No Match") ); //$NON-NLS-1$
	}
}

