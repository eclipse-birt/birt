/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.tests.engine.computation;

import junit.framework.TestCase;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.withaxes.ScaleContext;
import org.eclipse.birt.chart.util.CDateTime;

/**
 * 
 */

public class ScaleContextTest extends TestCase {

	public void testLinearScaleWithoutFixed() {
		// Without fixed value
		ScaleContext scale = new ScaleContext(0, IConstants.LINEAR, Integer.valueOf(0), Integer.valueOf(5),
				Integer.valueOf(1));
		scale.computeMinMax();
		assertEquals(new Double(0), scale.getMin());
		assertEquals(new Double(6), scale.getMax());
		assertEquals(null, scale.getMinWithMargin());
		assertEquals(null, scale.getMaxWithMargin());

		scale = new ScaleContext(0, IConstants.LINEAR, Integer.valueOf(0), Integer.valueOf(5), new Double(1.2));
		scale.computeMinMax();
		assertEquals(new Double(0), scale.getMin());
		assertEquals(new Double(6), scale.getMax());

		scale = new ScaleContext(0, IConstants.LINEAR, Integer.valueOf(0), Integer.valueOf(6), new Double(1.2));
		scale.computeMinMax();
		assertEquals(new Double(0), scale.getMin());
		assertEquals(new Double(7.2), scale.getMax());

		scale = new ScaleContext(0, IConstants.LINEAR, Integer.valueOf(1), Integer.valueOf(6), new Double(1.5));
		scale.computeMinMax();
		assertEquals(new Double(0), scale.getMin());
		assertEquals(new Double(7.5), scale.getMax());

		scale = new ScaleContext(0, IConstants.LINEAR, Integer.valueOf(-1), Integer.valueOf(6), new Double(1.5));
		scale.computeMinMax();
		assertEquals(new Double(-1.5), scale.getMin());
		assertEquals(new Double(7.5), scale.getMax());
	}

	public void testLinearScaleWithFixed() {
		// With fixed value
		ScaleContext scale = new ScaleContext(0, IConstants.LINEAR, Integer.valueOf(0), Integer.valueOf(5),
				Integer.valueOf(1));
		scale.setFixedValue(true, false, new Double(1), null);
		scale.computeMinMax();
		assertEquals(new Double(1), scale.getMin());
		assertEquals(new Double(6), scale.getMax());
		assertEquals(null, scale.getMinWithMargin());
		assertEquals(null, scale.getMaxWithMargin());

		scale = new ScaleContext(0, IConstants.LINEAR, Integer.valueOf(0), Integer.valueOf(5), new Double(1.2));
		scale.setFixedValue(true, true, new Double(1), new Double(5));
		scale.computeMinMax();
		assertEquals(new Double(1), scale.getMin());
		assertEquals(new Double(5), scale.getMax());
		assertEquals(null, scale.getMinWithMargin());
		assertEquals(null, scale.getMaxWithMargin());

	}

	public void testLinearScaleWithMargin() {
		// With margin area
		ScaleContext scale = new ScaleContext(20, IConstants.LINEAR, Integer.valueOf(1), Integer.valueOf(5),
				Integer.valueOf(1));
		scale.computeMinMax();
		assertEquals(new Double(0), scale.getMin());
		assertEquals(new Double(6), scale.getMax());

		scale = new ScaleContext(20, IConstants.LINEAR, Integer.valueOf(1), Integer.valueOf(6), new Double(1));
		scale.computeMinMax();
		assertEquals(new Double(0), scale.getMin());
		assertEquals(new Double(7), scale.getMax());

		scale = new ScaleContext(20, IConstants.LINEAR, new Double(0), new Double(6), new Double(2));
		scale.computeMinMax();
		assertEquals(new Double(-2), scale.getMin());
		assertEquals(new Double(8), scale.getMax());

		scale = new ScaleContext(20, IConstants.LINEAR, new Double(-1), new Double(7), new Double(2));
		scale.computeMinMax();
		assertEquals(new Double(-4), scale.getMin());
		assertEquals(new Double(10), scale.getMax());

		scale = new ScaleContext(20, IConstants.LINEAR, new Double(3), new Double(4), new Double(1));
		scale.computeMinMax();
		assertEquals(new Double(2), scale.getMin());
		assertEquals(new Double(5), scale.getMax());

		scale = new ScaleContext(20, IConstants.LINEAR, new Double(-4), new Double(-3), new Double(1));
		scale.computeMinMax();
		assertEquals(new Double(-5), scale.getMin());
		assertEquals(new Double(-2), scale.getMax());

		scale = new ScaleContext(20, IConstants.LINEAR, new Double(25.21), new Double(27.9), new Double(1));
		scale.computeMinMax();
		assertEquals(new Double(24), scale.getMin());
		assertEquals(new Double(29), scale.getMax());
	}

	public void testLinearScaleWithMarginAndFixed() {
		ScaleContext scale = new ScaleContext(20, IConstants.LINEAR, new Double(1), new Double(5), new Double(1));
		scale.setFixedValue(true, true, new Double(0), new Double(7));
		scale.computeMinMax();
		assertEquals(new Double(0), scale.getMin());
		assertEquals(new Double(7), scale.getMax());
		assertEquals(null, scale.getMinWithMargin());
		// Real value is 6, but less than 7
		assertEquals(null, scale.getMaxWithMargin());

		scale = new ScaleContext(20, IConstants.LINEAR, new Double(1), new Double(5), new Double(1));
		scale.setFixedValue(true, true, new Double(1), new Double(5));
		scale.computeMinMax();
		assertEquals(new Double(1), scale.getMin());
		assertEquals(new Double(5), scale.getMax());
		assertEquals(0, Math.round(Methods.asDouble(scale.getMinWithMargin()).doubleValue()));
		assertEquals(6, Math.round(Methods.asDouble(scale.getMaxWithMargin()).doubleValue()));
	}

	public void testDateTimeScaleWithoutFixed() {
		// Without fixed value
		ScaleContext scale = new ScaleContext(0, IConstants.DATE_TIME, CDateTime.YEAR,
				CDateTime.parse("01-01-2007 00:00:00"), CDateTime.parse("03-31-2007 00:00:00"), Integer.valueOf(1));
		scale.computeMinMax();
		assertEquals(CDateTime.parse("01-01-2006 00:00:00"), scale.getMin());
		assertEquals(CDateTime.parse("01-01-2008 00:00:00"), scale.getMax());
		// Margin not supported for datetime
		assertEquals(null, scale.getMinWithMargin());
		assertEquals(null, scale.getMaxWithMargin());

		scale = new ScaleContext(0, IConstants.DATE_TIME, CDateTime.MONTH, CDateTime.parse("01-01-2007 00:00:00"),
				CDateTime.parse("03-31-2007 00:00:00"), Integer.valueOf(1));
		scale.computeMinMax();
		assertEquals(CDateTime.parse("12-01-2006 00:00:00"), scale.getMin());
		assertEquals(CDateTime.parse("04-01-2007 00:00:00"), scale.getMax());

		scale = new ScaleContext(0, IConstants.DATE_TIME, CDateTime.DAY_OF_MONTH,
				CDateTime.parse("01-01-2007 00:00:00"), CDateTime.parse("03-31-2007 00:00:00"), Integer.valueOf(1));
		scale.computeMinMax();
		assertEquals(CDateTime.parse("12-31-2006 00:00:00"), scale.getMin());
		assertEquals(CDateTime.parse("04-01-2007 00:00:00"), scale.getMax());

		scale = new ScaleContext(0, IConstants.DATE_TIME, CDateTime.HOUR, CDateTime.parse("03-10-2007 10:12:12"),
				CDateTime.parse("03-31-2007 09:13:22"), Integer.valueOf(1));
		scale.computeMinMax();
		assertEquals(CDateTime.parse("03-10-2007 09:00:00"), scale.getMin());
		assertEquals(CDateTime.parse("03-31-2007 10:00:00"), scale.getMax());

		scale = new ScaleContext(0, IConstants.DATE_TIME, CDateTime.MINUTE, CDateTime.parse("03-10-2007 10:12:12"),
				CDateTime.parse("03-31-2007 09:13:22"), Integer.valueOf(1));
		scale.computeMinMax();
		assertEquals(CDateTime.parse("03-10-2007 10:11:00"), scale.getMin());
		assertEquals(CDateTime.parse("03-31-2007 09:14:00"), scale.getMax());

		scale = new ScaleContext(0, IConstants.DATE_TIME, CDateTime.SECOND, CDateTime.parse("03-10-2007 10:12:12"),
				CDateTime.parse("03-31-2007 09:13:22"), Integer.valueOf(1));
		scale.computeMinMax();
		assertEquals(CDateTime.parse("03-10-2007 10:12:11"), scale.getMin());
		assertEquals(CDateTime.parse("03-31-2007 09:13:23"), scale.getMax());
	}

	public void testDateTimeScaleWithFixed() {
		// With fixed value
		ScaleContext scale = new ScaleContext(0, IConstants.DATE_TIME, CDateTime.YEAR,
				CDateTime.parse("01-01-2007 00:00:00"), CDateTime.parse("03-31-2007 00:00:00"), Integer.valueOf(1));
		scale.setFixedValue(true, false, CDateTime.parse("01-01-2007 00:00:00"), null);
		scale.computeMinMax();
		assertEquals(CDateTime.parse("01-01-2007 00:00:00"), scale.getMin());
		assertEquals(CDateTime.parse("01-01-2008 00:00:00"), scale.getMax());
		// Margin not supported for datetime
		assertEquals(null, scale.getMinWithMargin());
		assertEquals(null, scale.getMaxWithMargin());

		scale = new ScaleContext(0, IConstants.DATE_TIME, CDateTime.MONTH, CDateTime.parse("01-01-2007 00:00:00"),
				CDateTime.parse("03-31-2007 00:00:00"), Integer.valueOf(1));
		scale.setFixedValue(true, true, CDateTime.parse("01-01-2007 00:00:00"), CDateTime.parse("03-01-2007 00:00:00"));
		scale.computeMinMax();
		assertEquals(CDateTime.parse("01-01-2007 00:00:00"), scale.getMin());
		assertEquals(CDateTime.parse("03-01-2007 00:00:00"), scale.getMax());

		scale = new ScaleContext(0, IConstants.DATE_TIME, CDateTime.DAY_OF_MONTH,
				CDateTime.parse("01-01-2007 00:00:00"), CDateTime.parse("03-31-2007 00:00:00"), Integer.valueOf(1));
		scale.setFixedValue(true, true, CDateTime.parse("01-01-2007 00:00:00"), CDateTime.parse("03-01-2007 00:00:00"));
		scale.computeMinMax();
		assertEquals(CDateTime.parse("01-01-2007 00:00:00"), scale.getMin());
		assertEquals(CDateTime.parse("03-01-2007 00:00:00"), scale.getMax());

		scale = new ScaleContext(0, IConstants.DATE_TIME, CDateTime.HOUR, CDateTime.parse("03-10-2007 10:12:12"),
				CDateTime.parse("03-31-2007 09:13:22"), Integer.valueOf(1));
		scale.setFixedValue(true, true, CDateTime.parse("03-10-2007 11:00:00"), CDateTime.parse("04-01-2007 00:00:00"));
		scale.computeMinMax();
		assertEquals(CDateTime.parse("03-10-2007 11:00:00"), scale.getMin());
		assertEquals(CDateTime.parse("04-01-2007 00:00:00"), scale.getMax());

		scale = new ScaleContext(0, IConstants.DATE_TIME, CDateTime.MINUTE, CDateTime.parse("03-10-2007 10:12:12"),
				CDateTime.parse("03-31-2007 09:13:22"), Integer.valueOf(1));
		scale.setFixedValue(true, true, CDateTime.parse("03-01-2007 01:12:00"), CDateTime.parse("03-31-2007 10:01:00"));
		scale.computeMinMax();
		assertEquals(CDateTime.parse("03-01-2007 01:12:00"), scale.getMin());
		assertEquals(CDateTime.parse("03-31-2007 10:01:00"), scale.getMax());

		scale = new ScaleContext(0, IConstants.DATE_TIME, CDateTime.SECOND, CDateTime.parse("03-10-2007 10:12:12"),
				CDateTime.parse("03-31-2007 09:13:22"), Integer.valueOf(1));
		scale.setFixedValue(true, true, CDateTime.parse("03-01-2007 01:12:12"), CDateTime.parse("03-31-2007 10:01:31"));
		scale.computeMinMax();
		assertEquals(CDateTime.parse("03-01-2007 01:12:12"), scale.getMin());
		assertEquals(CDateTime.parse("03-31-2007 10:01:31"), scale.getMax());
	}

	public void testLogScaleWithoutFixed() {
		// Without fixed value
		ScaleContext scale = new ScaleContext(0, IConstants.LOGARITHMIC, Integer.valueOf(0), Integer.valueOf(5),
				Integer.valueOf(10));
		scale.computeMinMax();
		assertEquals(new Double(0), scale.getMin());
		assertEquals(new Double(10), scale.getMax());

		scale = new ScaleContext(0, IConstants.LOGARITHMIC, Integer.valueOf(1), Integer.valueOf(5),
				Integer.valueOf(10));
		scale.computeMinMax();
		assertEquals(new Double(1), scale.getMin());
		assertEquals(new Double(10), scale.getMax());

		scale = new ScaleContext(0, IConstants.LOGARITHMIC, Integer.valueOf(2), Integer.valueOf(12),
				Integer.valueOf(10));
		scale.computeMinMax();
		assertEquals(new Double(1), scale.getMin());
		assertEquals(new Double(100), scale.getMax());
	}

	public void testLogScaleWithFixed() {
		// Without fixed value
		ScaleContext scale = new ScaleContext(0, IConstants.LOGARITHMIC, Integer.valueOf(0), Integer.valueOf(5),
				Integer.valueOf(10));
		scale.computeMinMax();
		scale.setFixedValue(true, true, new Double(1), new Double(6));
		assertEquals(new Double(1), scale.getMin());
		assertEquals(new Double(6), scale.getMax());

		scale = new ScaleContext(0, IConstants.LOGARITHMIC, Integer.valueOf(1), Integer.valueOf(5),
				Integer.valueOf(10));
		scale.computeMinMax();
		scale.setFixedValue(true, true, new Double(1), new Double(6));
		assertEquals(new Double(1), scale.getMin());
		assertEquals(new Double(6), scale.getMax());

		scale = new ScaleContext(0, IConstants.LOGARITHMIC, Integer.valueOf(2), Integer.valueOf(12),
				Integer.valueOf(10));
		scale.computeMinMax();
		scale.setFixedValue(true, true, new Double(1), new Double(100));
		assertEquals(new Double(1), scale.getMin());
		assertEquals(new Double(100), scale.getMax());
	}
}
