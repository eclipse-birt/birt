/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.chart.tests.engine.model.attribute;

import org.eclipse.birt.chart.model.attribute.StyledComponent;

import junit.framework.TestCase;

public class StyleComponentTest extends TestCase {

	public void testConstant() {
		assertEquals(StyledComponent.CHART_TITLE, StyledComponent.CHART_TITLE_LITERAL.getValue());
		assertEquals(StyledComponent.CHART_BACKGROUND, StyledComponent.CHART_BACKGROUND_LITERAL.getValue());
		assertEquals(StyledComponent.PLOT_BACKGROUND, StyledComponent.PLOT_BACKGROUND_LITERAL.getValue());
		assertEquals(StyledComponent.LEGEND_BACKGROUND, StyledComponent.LEGEND_BACKGROUND_LITERAL.getValue());
		assertEquals(StyledComponent.LEGEND_LABEL, StyledComponent.LEGEND_LABEL_LITERAL.getValue());
		assertEquals(StyledComponent.AXIS_TITLE, StyledComponent.AXIS_TITLE_LITERAL.getValue());
		assertEquals(StyledComponent.AXIS_LABEL, StyledComponent.AXIS_LABEL_LITERAL.getValue());
		assertEquals(StyledComponent.AXIS_LINE, StyledComponent.AXIS_LINE_LITERAL.getValue());
		assertEquals(StyledComponent.SERIES_TITLE, StyledComponent.SERIES_TITLE_LITERAL.getValue());
		assertEquals(StyledComponent.SERIES_LABEL, StyledComponent.SERIES_LABEL_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(StyledComponent.CHART_TITLE_LITERAL, StyledComponent.get(StyledComponent.CHART_TITLE));
		assertEquals(StyledComponent.CHART_BACKGROUND_LITERAL, StyledComponent.get(StyledComponent.CHART_BACKGROUND));
		assertEquals(StyledComponent.PLOT_BACKGROUND_LITERAL, StyledComponent.get(StyledComponent.PLOT_BACKGROUND));
		assertEquals(StyledComponent.LEGEND_BACKGROUND_LITERAL, StyledComponent.get(StyledComponent.LEGEND_BACKGROUND));
		assertEquals(StyledComponent.LEGEND_LABEL_LITERAL, StyledComponent.get(5));

		assertEquals(StyledComponent.CHART_TITLE_LITERAL, StyledComponent.get("Chart_Title")); //$NON-NLS-1$
		assertEquals(StyledComponent.CHART_BACKGROUND_LITERAL, StyledComponent.get("Chart_Background")); //$NON-NLS-1$
		assertEquals(StyledComponent.PLOT_BACKGROUND_LITERAL, StyledComponent.get("Plot_Background")); //$NON-NLS-1$
		assertEquals(StyledComponent.LEGEND_BACKGROUND_LITERAL, StyledComponent.get("Legend_Background")); //$NON-NLS-1$
		assertEquals(StyledComponent.LEGEND_LABEL_LITERAL, StyledComponent.get("Legend_Label")); //$NON-NLS-1$
		assertEquals(StyledComponent.AXIS_TITLE_LITERAL, StyledComponent.get("Axis_Title")); //$NON-NLS-1$
		assertEquals(StyledComponent.AXIS_LABEL_LITERAL, StyledComponent.get("Axis_Label")); //$NON-NLS-1$
		assertEquals(StyledComponent.AXIS_LINE_LITERAL, StyledComponent.get("Axis_Line")); //$NON-NLS-1$
		assertEquals(StyledComponent.SERIES_TITLE_LITERAL, StyledComponent.get("Series_Title")); //$NON-NLS-1$
		assertEquals(StyledComponent.SERIES_LABEL_LITERAL, StyledComponent.get("Series_Label")); //$NON-NLS-1$
		assertNull(StyledComponent.get("No Match")); //$NON-NLS-1$
	}
}
