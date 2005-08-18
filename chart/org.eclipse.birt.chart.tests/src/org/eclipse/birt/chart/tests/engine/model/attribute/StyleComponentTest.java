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
import org.eclipse.birt.chart.model.attribute.StyledComponent;

public class StyleComponentTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( StyledComponent.CHART_TITLE, 0 );
		assertEquals( StyledComponent.CHART_BACKGROUND , 1 );
		assertEquals( StyledComponent.PLOT_BACKGROUND, 2 );		
		assertEquals( StyledComponent.LEGEND_BACKGROUND, 3 );
		assertEquals( StyledComponent.LEGEND_LABEL, 4 );
		assertEquals( StyledComponent.DATA_LABEL, 5 );
		assertEquals( StyledComponent.AXIS_TITLE, 6 );
		assertEquals( StyledComponent.AXIS_LABEL, 7 );
		assertEquals( StyledComponent.AXIS_LINE, 8 );
		assertEquals( StyledComponent.SERIES_TITLE, 9 );
		assertEquals( StyledComponent.SERIES_LABEL, 10 );
	}
	
	public void testGet() 
	{
		assertEquals( StyledComponent.CHART_TITLE_LITERAL, StyledComponent.get(StyledComponent.CHART_TITLE) );
		assertEquals( StyledComponent.CHART_BACKGROUND_LITERAL, StyledComponent.get(StyledComponent.CHART_BACKGROUND) );
		assertEquals( StyledComponent.PLOT_BACKGROUND_LITERAL, StyledComponent.get(StyledComponent.PLOT_BACKGROUND) );
		assertEquals( StyledComponent.LEGEND_BACKGROUND_LITERAL, StyledComponent.get(StyledComponent.LEGEND_BACKGROUND) );
		assertEquals( StyledComponent.LEGEND_LABEL_LITERAL, StyledComponent.get(4) );
		assertEquals( StyledComponent.DATA_LABEL_LITERAL, StyledComponent.get(5) );
		assertEquals( StyledComponent.AXIS_TITLE_LITERAL, StyledComponent.get(6) );
		assertEquals( StyledComponent.AXIS_LABEL_LITERAL, StyledComponent.get(7) );
		assertEquals( StyledComponent.AXIS_LINE_LITERAL, StyledComponent.get(8) );
		assertEquals( StyledComponent.SERIES_TITLE_LITERAL, StyledComponent.get(9) );
		assertEquals( StyledComponent.SERIES_LABEL_LITERAL, StyledComponent.get(10) );
		assertNull(StyledComponent.get("11") );
		
		assertEquals( StyledComponent.CHART_TITLE_LITERAL, StyledComponent.get("Chart_Title") );
		assertEquals( StyledComponent.CHART_BACKGROUND_LITERAL, StyledComponent.get("Chart_Background") );
		assertEquals( StyledComponent.PLOT_BACKGROUND_LITERAL, StyledComponent.get("Plot_Background") );
		assertEquals( StyledComponent.LEGEND_BACKGROUND_LITERAL, StyledComponent.get("Legend_Background") );
		assertEquals( StyledComponent.LEGEND_LABEL_LITERAL, StyledComponent.get("Legend_Label") );
		assertEquals( StyledComponent.DATA_LABEL_LITERAL, StyledComponent.get("Data_Label") );
		assertEquals( StyledComponent.AXIS_TITLE_LITERAL, StyledComponent.get("Axis_Title") );
		assertEquals( StyledComponent.AXIS_LABEL_LITERAL, StyledComponent.get("Axis_Label") );
		assertEquals( StyledComponent.AXIS_LINE_LITERAL, StyledComponent.get("Axis_Line") );
		assertEquals( StyledComponent.SERIES_TITLE_LITERAL, StyledComponent.get("Series_Title") );
		assertEquals( StyledComponent.SERIES_LABEL_LITERAL, StyledComponent.get("Series_Label") );
		assertNull(StyledComponent.get("No Match") );
	}
}
