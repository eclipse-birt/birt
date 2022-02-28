/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.aggregation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.impl.BuildInAggregationFactory;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.junit.Test;

/**
 *
 * test Finance aggregation function
 */
public class FinanceTest {

	private BuildInAggregationFactory buildInAggrFactory = new BuildInAggregationFactory();

	/*
	 * @see TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testIrr() throws Exception {
		double b[] = { -70000d, 12000d, 15000d, 18000d, 21000d, 26000d };
		double a[] = { -70000, 12000, 15000 };
		double c[] = { -70000d, 12000d, 15000d, 18000d, 21000d };
		double d[] = { -70000d, 22000d, 25000d, 30000d, 31000d };

		IAggrFunction ag = buildInAggrFactory.getAggregation("irr");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_IRR_FUNC, ag.getName());

		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertTrue(!ag.getParameterDefn()[1].isOptional());

		ac.start();
		for (int i = 0; i < b.length; i++) {
			ac.onRow(new Object[] { new Double(b[i]), new Double(0.1) });
		}
		ac.finish();
		assertEquals(0.0866, ((Double) ac.getValue()).doubleValue(), 0.0001);

		ac.start();
		for (int i = 0; i < a.length; i++) {
			ac.onRow(new Object[] { new Double(a[i]), new Double(-0.6) });
		}
		ac.finish();
		assertEquals(-0.44, ((Double) ac.getValue()).doubleValue(), 0.01);

		ac.start();
		for (int i = 0; i < a.length; i++) {
			ac.onRow(new Object[] { new Double(a[i]), new Double(2.3) });
		}
		ac.finish();
		assertEquals(Double.NaN, ((Double) ac.getValue()).doubleValue(), 0.01);

		ac.start();
		for (int i = 0; i < c.length; i++) {
			ac.onRow(new Object[] { new Double(c[i]), new Double(-0.1) });
		}
		ac.finish();
		assertEquals(-0.021244, ((Double) ac.getValue()).doubleValue(), 0.000001);

		ac.start();
		for (int i = 0; i < d.length; i++) {
			ac.onRow(new Object[] { new Double(d[i]), new Double(0.05) });
		}
		ac.finish();
		assertEquals(0.19, ((Double) ac.getValue()).doubleValue(), 0.01);

		ac.start();
		ac.finish();
		assertEquals(null, ac.getValue());

		ac.start();
		try {
			ac.getValue();
			assertTrue(false);
		} catch (RuntimeException e) {
			assertTrue(true);
		}

	}

	@Test
	public void testMirr() throws Exception {
		// If the value of the guess is far from the correct answer, application
		// can't get the correct result and will report error
		double a[] = { -120000, 39000, 30000, 21000, 37000, 46000 };
		double b[] = { -120000, 39000, 30000, 21000 };
		IAggrFunction ag = buildInAggrFactory.getAggregation("mirr");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_MIRR_FUNC, ag.getName());

		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(3, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertTrue(!ag.getParameterDefn()[1].isOptional());
		assertTrue(!ag.getParameterDefn()[2].isOptional());

		ac.start();
		for (int i = 0; i < a.length; i++) {
			ac.onRow(new Object[] { new Double(a[i]), new Double(0.1), new Double(0.14) });
		}
		ac.finish();
		assertEquals(0.13475911082831482, ((Double) ac.getValue()).doubleValue(), Double.MIN_VALUE);

		ac.start();
		for (int i = 0; i < b.length; i++) {
			ac.onRow(new Object[] { new Double(b[i]), new Double(0.1), new Double(0.12) });
		}
		ac.finish();
		assertEquals(-0.048044655249980806, ((Double) ac.getValue()).doubleValue(), Double.MIN_VALUE);

		ac.start();
		ac.finish();
		assertEquals(null, ac.getValue());

		ac.start();
		try {
			ac.getValue();
			assertTrue(false);
		} catch (RuntimeException e) {
			assertTrue(true);
		}

	}

	@Test
	public void testNpv() throws Exception {

		IAggrFunction ag = buildInAggrFactory.getAggregation("npv");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_NPV_FUNC, ag.getName());

		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertFalse(!ag.getParameterDefn()[1].isOptional());

		double a[] = { -10000, 3000, 4200, 6800 };

		ac.start();
		for (int i = 0; i < a.length; i++) {
			ac.onRow(new Object[] { new Double(a[i]), new Double(0.1) });
		}
		ac.finish();
		assertEquals(1188.4434123352216, ((Double) ac.getValue()).doubleValue(), Double.MIN_VALUE);

		ac.start();
		ac.finish();
		assertEquals(null, ac.getValue());

		ac.start();
		try {
			ac.getValue();
			assertTrue(false);
		} catch (RuntimeException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRunningNpv() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("runningnpv");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_RUNNINGNPV_FUNC, ag.getName());

		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertFalse(!ag.getParameterDefn()[1].isOptional());

		double a[] = { -10000, 3000, 4200, 6800 };
		double b[] = { -9090.90909090909, -6611.570247933883, -3456.0480841472577, 1188.4434123352216 };

		ac.start();
		for (int i = 0; i < a.length; i++) {
			ac.onRow(new Object[] { new Double(a[i]), new Double(0.1), new Double(0.14) });
			assertEquals(b[i], ((Double) ac.getValue()).doubleValue(), Double.MIN_VALUE);
		}
		ac.finish();

		ac.start();
		ac.finish();
		assertEquals(null, ac.getValue());

	}

}
