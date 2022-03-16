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
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.impl.BuildInAggregationFactory;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.junit.Test;

/**
 *
 * test total aggregation function
 */
public class TotalTest {
	private double[] weight = { 1.2, 1.4, 2, 0.5, 1.4, 2.4, 1.5, 2.5, 3.7, 1.4, 1.03, 0.5, 0.2, 0.6, 1.3, 1.5, 12.4 };
	private double[] doubleArray1 = { 1, 3, 5, 4, 6, 8, 3, 4, 5, 7, 9, 10, 4, 6, 7 };
	private boolean[] doubleArray1TopBottom = { false, false, false, false, false, true, false, false, false, true,
			true, true, false, false, true };
	private double[] doubleArray2 = { 4, -43, 4, 23, -15, -6, 4, -6, 3, 63, 33, -6, -23, 34 };
	private Double[] doubleArray3 = { Double.valueOf("100"), Double.valueOf("20"), null, Double.valueOf("300"), null,
			Double.valueOf("40"), Double.valueOf("10"), Double.valueOf("10") };
	private Double[] doubleArrayNull = { null, null };
	private boolean[] doubleArray3TopBottom = { true, false, false, true, false, false, false, false };
	private int[] doubleArray3RankDec = { 2, 4, 7, 1, 7, 3, 5, 5 };
	private int[] doubleArray3RankAsc = { 7, 5, 1, 8, 1, 6, 3, 3 };
	private int[] doubleArray3PercentRank = { 857, 571, 0, 1000, 0, 714, 285, 285 };
	private Object[] doubleArray3PercentSum = { new Integer(208), new Integer(41), null, new Integer(625), null,
			new Integer(83), new Integer(20), new Integer(20) };
	private String[] str1 = { "4", "-43", "4", "23", "-15", "-6", "4", "-6", "3", "63", "33", "-6", "-23", "34" };
	private double[] doubleArray4 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	private double[] doubleArray5 = { 1, 2, 2, 3, 1, 3, 4, 1, 2 };
	private double[] doubleArray6 = { 1, 2, 2, 3, 1, 3, 4, 2, 1 };
	private Object[] anyObjectArray = { "aa", "bb", null, new Integer(0), null, new Double(1), new Float(0), null };
	private Object[] anyObjectArray2 = { "aa", "bb", null, new Integer(0), null, new Double(1), new Float(0), null,
			false, new Date(1000000L) };

	private Date[] dates = { new Date(1000000L), new Date(2000000L), new Date(3000000L), new Date(4000000L) };

	private Date[] dates2 = { new Date(1000000L), new Date(2000000L), new Date(2000000L), new Date(3000000L),
			new Date(4000000L) };

	private String[] str2 = { "test", "string", "array", "for", "aggregation" };

	private String[] str4 = { "test", "string", "array", "for", "aggregation", "test", "string", "array", "for",
			"aggregation" };

	private BigDecimal[] bigDecimalArray = { new BigDecimal("1"), new BigDecimal("3"), new BigDecimal("5"),
			new BigDecimal("4"), new BigDecimal("6"), new BigDecimal("8"), new BigDecimal("3"), new BigDecimal("4"),
			new BigDecimal("5"), new BigDecimal("7"), new BigDecimal("9"), new BigDecimal("10"), new BigDecimal("4"),
			new BigDecimal("6"), new BigDecimal("7") };

	private int[] bigDecimalRankAsc = { 1, 2, 7, 4, 9, 13, 2, 4, 7, 11, 14, 15, 4, 9, 11 };

	private int[] bigDecimalPercentRank = { 0, 71, 428, 214, 571, 857, 71, 214, 428, 714, 928, 1000, 214, 571, 714

	};

	private int[] bigDecimalPercentSum = { 12, 36, 60, 48, 73, 97, 36, 48, 60, 85, 109, 121, 48, 73, 85 };

	private BuildInAggregationFactory buildInAggrFactory = new BuildInAggregationFactory();

	/*
	 * @see TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testTotalConcatenate() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("concatenate");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_CONCATENATE_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(4, ag.getParameterDefn().length);
		String separator = "-";

		String result = "";

		ac.start();
		for (int i = 0; i < str2.length; i++) {
			ac.onRow(new Object[] { str2[i], separator, null, null });
			result += str2[i];
			result += separator;
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - separator.length());
		}
		ac.finish();
		assertEquals(result, ac.getValue());

		ac.start();
		for (int i = 0; i < str4.length; i++) {
			ac.onRow(new Object[] { str4[i], separator, null, null });
		}
		ac.finish();
		assertEquals(result, ac.getValue());

		ac.start();
		result = "";
		boolean exceedsMaxLength = false;
		for (int i = 0; i < str4.length; i++) {
			ac.onRow(new Object[] { str4[i], separator, 50, true });
			if (exceedsMaxLength || result.getBytes().length > 50 - str4[i].length()) {
				exceedsMaxLength = true;
				continue;
			}
			result += str4[i];
			result += separator;
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - separator.length());
		}
		ac.finish();
		assertEquals(result, ac.getValue());

		ac.start();
		result = "";
		exceedsMaxLength = false;
		for (int i = 0; i < str2.length; i++) {
			ac.onRow(new Object[] { str2[i], separator, 20, null });
			if (exceedsMaxLength || result.getBytes().length > 20 - str2[i].length()) {
				exceedsMaxLength = true;
				continue;
			}
			result += str2[i];
			result += separator;
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - separator.length());
		}
		ac.finish();
		assertEquals(result, ac.getValue());

		ac.start();
		result = "";
		separator = ";  ";
		exceedsMaxLength = false;
		for (int i = 0; i < str2.length; i++) {
			ac.onRow(new Object[] { str2[i], separator, 20, null });
			if (exceedsMaxLength || result.getBytes().length > 20 - str2[i].length()) {
				exceedsMaxLength = true;
				continue;
			}
			result += str2[i];
			result += separator;
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - separator.length());
		}
		ac.finish();
		assertEquals(result, ac.getValue());

		ac.start();
		result = "";
		separator = "\n";
		exceedsMaxLength = false;
		for (int i = 0; i < str2.length; i++) {
			ac.onRow(new Object[] { str2[i], separator, 20, null });
			if (exceedsMaxLength || result.getBytes().length > 20 - str2[i].length()) {
				exceedsMaxLength = true;
				continue;
			}
			result += str2[i];
			result += separator;
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - separator.length());
		}
		ac.finish();
		assertEquals(result, ac.getValue());

		ac.start();
		result = "";
		separator = "\t";
		exceedsMaxLength = false;
		for (int i = 0; i < str2.length; i++) {
			ac.onRow(new Object[] { str2[i], separator, 20, null });
			if (exceedsMaxLength || result.getBytes().length > 20 - str2[i].length()) {
				exceedsMaxLength = true;
				continue;
			}
			result += str2[i];
			result += separator;
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - separator.length());
		}
		ac.finish();
		assertEquals(result, ac.getValue());

		ac.start();
		result = "";
		separator = "   ";
		exceedsMaxLength = false;
		for (int i = 0; i < str2.length; i++) {
			ac.onRow(new Object[] { str2[i], separator, 20, null });
			if (exceedsMaxLength || result.getBytes().length > 20 - str2[i].length()) {
				exceedsMaxLength = true;
				continue;
			}
			result += str2[i];
			result += separator;
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - separator.length());
		}
		ac.finish();
		assertEquals(result, ac.getValue());

		ac.start();
		result = "";
		separator = ",,, ";
		exceedsMaxLength = false;
		for (int i = 0; i < str2.length; i++) {
			ac.onRow(new Object[] { str2[i], separator, 20, null });
			if (exceedsMaxLength || result.getBytes().length > 20 - str2[i].length()) {
				exceedsMaxLength = true;
				continue;
			}
			result += str2[i];
			result += separator;
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - separator.length());
		}
		ac.finish();
		assertEquals(result, ac.getValue());

		ac.start();
		result = "";
		separator = "#";
		exceedsMaxLength = false;
		LinkedHashSet<String> objects = new LinkedHashSet<>();
		for (int i = 0; i < anyObjectArray2.length; i++) {
			if (anyObjectArray2[i] != null) {
				objects.add(anyObjectArray2[i].toString());
			}
		}
		Iterator<String> iterator = objects.iterator();
		while (iterator.hasNext()) {
			String value = iterator.next();
			ac.onRow(new Object[] { value, separator, 30, false });
			if (value != null) {
				if (exceedsMaxLength || result.getBytes().length > 30 - value.length()) {
					exceedsMaxLength = true;
					continue;
				}
				result += value;
				result += separator;
			}
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - separator.length());
		}
		ac.finish();
		assertEquals(result, ac.getValue());

	}

	@Test
	public void testTotalCount() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("count");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_COUNT_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);

		ac.start();
		for (int i = 0; i < anyObjectArray.length; i++) {
			ac.onRow(new Object[] { anyObjectArray[i] });
		}
		ac.finish();
		assertEquals(new Integer(5), ac.getValue());

		ac.start();
		for (int i = 0; i < anyObjectArray.length; i++) {
			ac.onRow(null);
		}
		ac.finish();
		assertEquals(new Integer(8), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Object[] {});
		}
		ac.finish();
		assertEquals(new Integer(15), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Object[] {});
		}
		ac.finish();
		assertEquals(new Integer(14), ac.getValue());

		ac.start();

		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] {});
		}
		ac.finish();
		assertEquals(new Integer(14), ac.getValue());

		ac.start();
		ac.finish();
		assertEquals(new Integer(0), ac.getValue());

		ac.start();
		try {
			ac.getValue();
			assertTrue(false);
		} catch (RuntimeException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testTotalSum() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("sum");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_SUM_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		// null-value handling
		{
			BigDecimal[] arr = { null, null, new BigDecimal("1.0"), null, new BigDecimal("2.5") };
			ac.start();
			for (int i = 0; i < arr.length; i++) {
				ac.onRow(new Object[] { arr[i] });
			}
			ac.finish();
			assertEquals(3.5D, ac.getValue());
		}

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]) });
		}
		ac.finish();
		assertEquals(new Double(82.0), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray2[i]) });
		}
		ac.finish();
		assertEquals(new Double(69.0), ac.getValue());

		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i] });
		}
		ac.finish();
		assertEquals(69D, ac.getValue());

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

		// test Total.sum() on String objects with data which can't be convert
		// to Number
		ac.start();
		try {
			for (int i = 0; i < str2.length; i++) {
				ac.onRow(new Object[] { str2[i] });
			}
			fail();
		} catch (DataException e) {
			assertTrue(true);
		}
		ac.finish();

		// test Total.sum() for BigDecimal data
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		assertEquals(82D, ac.getValue());

		ac.start();
		ac.onRow(new Object[] { new BigDecimal("1.2") });
		ac.finish();
		assertEquals(1.2D, ac.getValue());

		// test Total.sum() for heterogeneous Number array data
		{
			Number numberArray[] = { BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.TEN, Double.valueOf("0.1"),
					new BigDecimal("0.1"), new BigDecimal(0.1D), new BigDecimal("0.1"), new Double(0.1D), };
			ac.start();
			for (int i = 0; i < numberArray.length; i++) {
				ac.onRow(new Object[] { numberArray[i] });
			}
			ac.finish();
			Number x = (Number) ac.getValue();
			assertTrue(new BigDecimal("11.49").compareTo(new BigDecimal(x.toString())) == -1);
			assertTrue(new BigDecimal("11.51").compareTo(new BigDecimal(x.toString())) == 1);
		}

		// test Total.sum() for data with NaN
		{
			ac.start();
			ac.onRow(new Object[] { 0D / 0D });
			ac.onRow(new Object[] { Double.valueOf("0.1"), Double.valueOf("0.2") });
			ac.onRow(new Object[] { new BigDecimal("0.3") });
			ac.finish();
			Number x = (Number) ac.getValue();
			assertTrue(x instanceof Double && ((Double) x).isNaN());

			ac.start();
			ac.onRow(new Object[] { Double.valueOf("0.1") });
			ac.onRow(new Object[] { new BigDecimal("0.2"), new BigDecimal("0.3") });
			ac.onRow(new Object[] { Double.NaN });
			ac.onRow(new Object[] { new BigDecimal("0.4") });
			ac.finish();
			x = (Number) ac.getValue();
			assertTrue(x instanceof Double && ((Double) x).isNaN());
		}

		// test Total.sum() for data with Infinity
		{
			ac.start();
			ac.onRow(new Object[] { Double.POSITIVE_INFINITY });
			ac.onRow(new Object[] { Double.valueOf("0.1"), Double.valueOf("0.2") });
			ac.onRow(new Object[] { new BigDecimal("0.3") });
			ac.finish();
			Number x = (Number) ac.getValue();
			assertTrue(x instanceof Double);
			assertTrue(((Double) x).isNaN());

			ac.start();
			ac.onRow(new Object[] { Double.valueOf("0.1"), Double.valueOf("0.2") });
			ac.onRow(new Object[] { new BigDecimal("0.3") });
			ac.onRow(new Object[] { -1D / 0D });
			ac.finish();
			x = (Number) ac.getValue();
			assertTrue(x instanceof Double);
			assertTrue(((Double) x).isNaN());

			ac.start();
			ac.onRow(new Object[] { Double.valueOf("0.1") });
			ac.onRow(new Object[] { Double.NEGATIVE_INFINITY });
			ac.onRow(new Object[] { new BigDecimal("0.3") });
			ac.finish();
			x = (Number) ac.getValue();
			assertTrue(x instanceof Double);
			assertTrue(((Double) x).isNaN());
		}
	}

	@Test
	public void testTotalRunningSum() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("RUNNINGSUM");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_RUNNINGSUM_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		{
			double sum = 0D;
			ac.start();
			for (int i = 0; i < doubleArray1.length; i++) {
				ac.onRow(new Double[] { new Double(doubleArray1[i]) });
				sum += doubleArray1[i];
				assertEquals(new Double(sum), ac.getValue());
			}
			ac.finish();
		}

		{
			double sum = 0D;
			ac.start();
			for (int i = 0; i < doubleArray2.length; i++) {
				ac.onRow(new Double[] { new Double(doubleArray2[i]) });
				sum += doubleArray2[i];
				assertEquals(new Double(sum), ac.getValue());
			}
			ac.finish();
		}

		{
			Double sum = 0.0D;
			ac.start();
			for (int i = 0; i < str1.length; i++) {
				ac.onRow(new Object[] { str1[i] });
				sum += Double.valueOf(str1[i]);
				assertTrue(ac.getValue() instanceof Double);
				assertEquals(sum, ac.getValue());
			}
			ac.finish();
		}

		{
			ac.start();
			ac.finish();
			assertEquals(null, ac.getValue());
		}

		// test Total.RUNNINGSUM() for BigDecimal data
		Double bigSum = 0.0D;
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
			bigSum += bigDecimalArray[i].doubleValue();
			Object ret = ac.getValue();
			assertTrue(ret instanceof Double);
			assertEquals(bigSum, ret);
		}
		ac.finish();
	}

	@Test
	public void testTotalAve() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("ave");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_AVE_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		ac.start();
		for (int i = 0; i < dates.length; i++) {
			ac.onRow(new Object[] { dates[i] });
		}
		ac.finish();
		assertEquals(2500000D, ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]) });
		}
		ac.finish();
		assertEquals(new Double(5.466666666666667), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray2[i]) });
		}
		ac.finish();
		assertEquals(new Double(4.928571428571429), ac.getValue());

		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i] });
		}
		ac.finish();
		assertEquals(new Double(4.928571428571429), ac.getValue());

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

		// test Total.AVE() for BigDecimal data
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof Double);
		assertEquals(5.466666666666666666666666666666667D, ret);
	}

	@Test
	public void testTotalFirst() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("first");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_FIRST_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]) });
		}
		ac.finish();
		assertEquals(new Double(1.0), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray2[i]) });
		}
		ac.finish();
		assertEquals(new Double(4), ac.getValue());

		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i] });
		}
		ac.finish();
		assertEquals("4", ac.getValue());

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

		// test Total.FIRST() for BigDecimal data
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof BigDecimal);
		assertTrue(new BigDecimal(1).compareTo((BigDecimal) ret) == 0);
	}

	@Test
	public void testTotalLast() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("last");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_LAST_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]) });
		}
		ac.finish();
		assertEquals(new Double(7.0), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray2[i]) });
		}
		ac.finish();
		assertEquals(new Double(34.0), ac.getValue());

		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i] });
		}
		ac.finish();
		assertEquals("34", ac.getValue());

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
		// test Total.LAST() for BigDecimal data
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof BigDecimal);
		assertTrue(new BigDecimal(7).compareTo((BigDecimal) ret) == 0);
	}

	@Test
	public void testTotalMax() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("max");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_MAX_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]) });
		}
		ac.finish();
		assertEquals(new Double(10.0), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray2[i]) });
		}
		ac.finish();
		assertEquals(new Double(63.0), ac.getValue());

		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i] });
		}
		ac.finish();
		assertEquals("63", ac.getValue());

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
		// test Total.MAX() for BigDecimal data
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof BigDecimal);
		assertTrue(new BigDecimal(10).compareTo((BigDecimal) ret) == 0);
	}

	@Test
	public void testTotalMin() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("min");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_MIN_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]) });
		}
		ac.finish();
		assertEquals(new Double(1.0), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray2[i]) });
		}
		ac.finish();
		assertEquals(new Double(-43), ac.getValue());

		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i] });
		}
		ac.finish();
		assertEquals("-15", ac.getValue());

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

		// test Total.MIN() for BigDecimal data
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof BigDecimal);
		assertTrue(new BigDecimal(1).compareTo((BigDecimal) ret) == 0);
	}

	@Test
	public void testTotalRange() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("range");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_RANGE_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]) });
		}
		ac.finish();
		assertEquals(new Double(9.0), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray2[i]) });
		}
		ac.finish();
		assertEquals(new Double(106), ac.getValue());

		// String data type returns 0
		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i] });
		}
		ac.finish();
		assertEquals(0d, ac.getValue());

		// Null data returns null
		ac.start();
		for (int i = 0; i < doubleArrayNull.length; i++) {
			ac.onRow(new Object[] { doubleArrayNull[i] });
		}
		ac.finish();
		assertEquals(null, ac.getValue());

		// No data returns null
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

		// test Total.RANGE() for BigDecimal data
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof Double);
		assertEquals(9D, ret);
	}

	@Test
	public void testTotalMedian() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("median");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_MEDIAN_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]) });
		}
		ac.finish();
		assertEquals(new Double(5.0), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray2[i]) });
		}
		ac.finish();
		assertEquals(new Double(3.5), ac.getValue());

		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i] });
		}
		ac.finish();
		assertEquals(new Double(3.5D), ac.getValue());

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

		// median test for Date
		// 1:
		ac.start();
		for (int i = 0; i < dates.length; i++) {
			ac.onRow(new Object[] { dates[i] });
		}
		ac.finish();
		assertEquals(2500000D, ac.getValue());
		// 2:
		ac.start();
		for (int i = 0; i < dates2.length; i++) {
			ac.onRow(new Object[] { dates2[i] });
		}
		ac.finish();
		assertEquals(2000000D, ac.getValue());

		// test Total.MEDIAN() for BigDecimal data
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof Double);
		assertEquals(5D, ret);

	}

	@Test
	public void testTotalMode() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("mode");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_MODE_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]) });
		}
		ac.finish();
		assertEquals(new Double(4.0), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray2[i]) });
		}
		ac.finish();
		assertEquals(new Double(4.0), ac.getValue());

		// double4: unique numbers
		ac.start();
		for (int i = 0; i < doubleArray4.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray4[i]) });
		}
		ac.finish();
		assertEquals(null, ac.getValue());
		// double 5: mutiple mode, return the first appeared mode
		ac.start();
		for (int i = 0; i < doubleArray5.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray5[i]) });
		}
		ac.finish();
		assertEquals(new Double(1.0), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray6.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray6[i]) });
		}
		ac.finish();
		assertEquals(new Double(1.0), ac.getValue());

		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i] });
		}
		ac.finish();
		assertEquals("4", ac.getValue());

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

		ac.start();
		ac.onRow(new Double[] { new Double(4) });
		ac.onRow(new Double[] { new Double(4) });
		ac.onRow(new Double[] { new Double(3) });
		ac.finish();
		assertEquals(ac.getValue(), new Double(4));
		// mode test for Date
		// 1: no mode date
		ac.start();
		for (int i = 0; i < dates.length; i++) {
			ac.onRow(new Object[] { dates[i] });
		}
		ac.finish();
		assertEquals(ac.getValue(), null);
		// 2: mode date is new Date(2000000L)
		ac.start();
		for (int i = 0; i < dates2.length; i++) {
			ac.onRow(new Object[] { dates2[i] });
		}
		ac.finish();
		assertEquals(ac.getValue(), new Date(2000000L));

		// test Total.MODE() for BigDecimal data
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof BigDecimal);
		assertTrue(new BigDecimal(4).compareTo((BigDecimal) ret) == 0);
	}

	@Test
	public void testTotalStdDev() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("stddev");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_STDDEV_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]) });
		}
		ac.finish();
		assertEquals(2.445598573141631D, ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray2[i]) });
		}
		ac.finish();
		assertEquals(new Double(26.560422510872147), ac.getValue());

		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i] });
		}
		ac.finish();
		assertEquals(26.560422510872147D, ac.getValue());

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

		// test Total.StdDEV() for BigDecimal data
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof Double);
		assertEquals(2.445598573141631D, ret);

	}

	@Test
	public void testTotalVariance() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("variance");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_VARIANCE_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]) });
		}
		ac.finish();
		assertEquals(5.980952380952381D, ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray2[i]) });
		}
		ac.finish();
		assertEquals(new Double(705.4560439560439), ac.getValue());

		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i] });
		}
		ac.finish();
		assertEquals(705.4560439560439D, ac.getValue());

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

		// test Total.Variance() for BigDecimal data
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof Double);
		assertEquals(5.980952380952380952380952380952381D, ret);
	}

	@Test
	public void testTotalWeightedAva() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("weightedAve");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertTrue(!ag.getParameterDefn()[1].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]), new Double(weight[i]) });
		}
		ac.finish();

		assertEquals(new Double(5.343042071197409), ac.getValue());

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray2[i]), new Double(weight[i]) });
		}
		ac.finish();

		assertEquals(new Double(3.236104279390063), ac.getValue());

		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i], new Double(weight[i]) });
		}
		ac.finish();

		assertEquals(3.236104279390063D, ac.getValue());

		ac.start();
		ac.onRow(new Object[] { new Double(1), new Double(1) });
		ac.onRow(new Object[] { new Double(2), new Double(2) });
		ac.finish();
		assertEquals(new Double(1.6666666666666667), ac.getValue());

		ac.start();
		ac.onRow(new Object[] { null, new Double(3) });
		ac.onRow(new Object[] { new Double(2), new Double(2) });
		ac.onRow(new Object[] { new Double(2), null });
		ac.finish();
		assertEquals(new Double(2), ac.getValue());

		ac.start();
		ac.onRow(new Object[] { new Double(1), new Double(3) });
		ac.onRow(new Object[] { new Double(1), new Double(-3) });
		ac.finish();
		assertEquals(Double.NaN, ac.getValue());

		ac.start();
		ac.onRow(new Object[] { new Double(1), new Double(2) });
		ac.onRow(new Object[] { new Double(2), new Double(-4) });
		ac.finish();
		assertEquals(new Double(3.0), ac.getValue());

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

		// test Total.WeightedAve() for BigDecimal data
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Double(weight[i]) });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof Double);
		assertEquals(5.343042071197409D, ret);

	}

	@Test
	public void testTotalMovingAve() throws Exception {
		double[] values1 = { 1.0, 2.0, 3.0, 3.25, 3.8, 4.5, 4.285714285714286, 4.25, 4.75, 5.25, 5.75, 6.5, 6.25, 6,
				6.5 };
		String[] values2 = { "4", "-19.5", "-11.66666666666666666666666666666667", "-3", "-5.4", "-5.5", "-5.5",
				"0.6666666666666666666666666666666667", "0.5", "7.166666666666666666666666666666667",
				"15.16666666666666666666666666666667", "15.16666666666666666666666666666667",
				"10.66666666666666666666666666666667", "17.33333333333333333333333333333333" };
		IAggrFunction ag = buildInAggrFactory.getAggregation("movingAve");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_MOVINGAVE_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertTrue(!ag.getParameterDefn()[1].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Object[] { new Double(doubleArray1[i]), new Integer(8) });
			assertEquals(new Double(values1[i]), ac.getValue());
		}
		ac.finish();

		ac.start();
		for (int i = 0; i < doubleArray2.length; i++) {
			ac.onRow(new Object[] { new Double(doubleArray2[i]), new Integer(6) });
			assertEquals(Double.valueOf(values2[i]), ac.getValue());
		}
		ac.finish();

		ac.start();
		for (int i = 0; i < str1.length; i++) {
			ac.onRow(new Object[] { str1[i], new Integer(6) });
			assertEquals(Double.valueOf(values2[i]), ac.getValue());
		}
		ac.finish();

		ac.start();
		ac.finish();
		assertEquals(null, ac.getValue());

		// test Total.MovingAve() for BigDecimal data
		BigDecimal[] expectResults = { new BigDecimal("1.0"), new BigDecimal("2.0"), new BigDecimal("3.0"),
				new BigDecimal("3.25"), new BigDecimal("3.8"), new BigDecimal("4.5"),
				new BigDecimal("4.285714285714285714285714285714286"), new BigDecimal("4.25"), new BigDecimal("4.75"),
				new BigDecimal("5.25"), new BigDecimal("5.75"), new BigDecimal("6.5"), new BigDecimal("6.25"),
				new BigDecimal("6.0"), new BigDecimal("6.5") };

		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], 8 });
			Object ret = ac.getValue();
			assertTrue(ret instanceof Double);
			assertEquals(expectResults[i].doubleValue(), ret);
		}
		ac.finish();

	}

	@Test
	public void testTotalAveDate() throws Exception {

		IAggrFunction ag = buildInAggrFactory.getAggregation("ave");
		Accumulator ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < dates.length; i++) {
			ac.onRow(new Object[] { dates[i] });
		}
		ac.finish();
		assertEquals(2500000D, ac.getValue());

	}

	@Test
	public void testTotalMaxDate() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("max");
		Accumulator ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < dates.length; i++) {
			ac.onRow(new Object[] { dates[i] });
		}
		ac.finish();
		assertEquals(new Date(4000000L), ac.getValue());

	}

	@Test
	public void testTotalMinDate() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("min");
		Accumulator ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < dates.length; i++) {
			ac.onRow(new Object[] { dates[i] });
		}
		ac.finish();
		assertEquals(new Date(1000000L), ac.getValue());

	}

	@Test
	public void testTotalMaxString() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("max");
		Accumulator ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < str2.length; i++) {
			ac.onRow(new Object[] { str2[i] });
		}
		ac.finish();
		assertEquals("test", ac.getValue());
	}

	@Test
	public void testTotalMinString() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("min");
		Accumulator ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < str2.length; i++) {
			ac.onRow(new Object[] { str2[i] });
		}
		ac.finish();
		assertEquals("aggregation", ac.getValue());
	}

	@Test
	public void testTotalTop() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("isTopN");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_TOP_N_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertTrue(!ag.getParameterDefn()[1].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]), new Double(5) });
		}
		ac.finish();

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]), new Double(5) });
			assertEquals(new Boolean(doubleArray1TopBottom[i]), ac.getValue());
		}
		ac.finish();

		ag = buildInAggrFactory.getAggregation("isTopN");
		ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_TOP_N_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertTrue(!ag.getParameterDefn()[1].isOptional());

		ac.start();
		ac.onRow(new Double[] { new Double(6), new Double(5) });
		ac.finish();
		ac.start();
		ac.onRow(new Double[] { new Double(6), new Double(5) });
		ac.finish();

		assertEquals(new Boolean(true), ac.getValue());

		// test Total.ISTOPN() for BigDecimal data
		ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Double(5) });
		}
		ac.finish();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Double(5) });
			assertEquals(new Boolean(doubleArray1TopBottom[i]), ac.getValue());
		}
		ac.finish();

		ag = buildInAggrFactory.getAggregation("isTopNPercent");
		ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_TOP_PERCENT_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertTrue(!ag.getParameterDefn()[1].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]), new Double(33) });
		}
		ac.finish();
		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]), new Double(33) });
			assertEquals(new Boolean(doubleArray1TopBottom[i]), ac.getValue());
		}
		ac.finish();

		// test Total.ISTOPNPercent() for BigDecimal data
		ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Double(33) });
		}
		ac.finish();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Double(33) });
			assertEquals(new Boolean(doubleArray1TopBottom[i]), ac.getValue());
		}
		ac.finish();

		ag = buildInAggrFactory.getAggregation("isTopNPercent");
		ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_TOP_PERCENT_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertTrue(!ag.getParameterDefn()[1].isOptional());

		ac.start();
		ac.onRow(new Double[] { new Double(6), new Double(100) });
		ac.finish();
		ac.start();
		ac.onRow(new Double[] { new Double(6), new Double(100) });
		ac.finish();

		assertEquals(new Boolean(true), ac.getValue());
	}

	/**
	 * test top n aggregation with null values. Note: the null values will not be
	 * considered as the candidates for the result.
	 */
	@Test
	public void testTotalTop2() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("isTopN");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_TOP_N_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertTrue(!ag.getParameterDefn()[1].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Double[] { doubleArray3[i], new Double(2) });
		}
		ac.finish();

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Double[] { doubleArray3[i], new Double(2) });
			assertEquals(new Boolean(doubleArray3TopBottom[i]), ac.getValue());
		}
		ac.finish();
	}

	@Test
	public void testTotalBottom() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("isBottomN");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_BOTTOM_N_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertTrue(!ag.getParameterDefn()[1].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]), new Double(10) });
		}
		ac.finish();
		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]), new Double(10) });
			assertEquals(new Boolean(!doubleArray1TopBottom[i]), ac.getValue());
		}
		ac.finish();

		// test Total.ISBottomN() for BigDecimal data
		ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Double(10) });
		}
		ac.finish();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Double(10) });
			assertEquals(new Boolean(!doubleArray1TopBottom[i]), ac.getValue());
		}
		ac.finish();

		ag = buildInAggrFactory.getAggregation("isBottomNPercent");
		ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_BOTTOM_PERCENT_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertTrue(!ag.getParameterDefn()[1].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]), new Double(66) });
		}
		ac.finish();

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]), new Double(66) });
			assertEquals(new Boolean(!doubleArray1TopBottom[i]), ac.getValue());
		}
		ac.finish();

		// test Total.ISBottomNPercent() for BigDecimal data
		ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Double(66) });
		}
		ac.finish();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Double(66) });
			assertEquals(new Boolean(!doubleArray1TopBottom[i]), ac.getValue());
		}
		ac.finish();
	}

	@Test
	public void testTotalRank() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("rank");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_RANK_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertFalse(!ag.getParameterDefn()[1].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Boolean(false) });
		}
		ac.finish();

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Integer(0) });
			assertEquals(new Integer(doubleArray3RankDec[i]), ac.getValue());
		}
		ac.finish();

		ag = buildInAggrFactory.getAggregation("rank");
		ac = ag.newAccumulator();

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Boolean(true) });
		}
		ac.finish();
		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Integer(1) });
			assertEquals(new Integer(doubleArray3RankAsc[i]), ac.getValue());
		}
		ac.finish();

		// test Total.rank() for BigDecimal data
		ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Boolean(true) });
		}
		ac.finish();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Double(0) });
			assertEquals(new Integer(bigDecimalRankAsc[i]), ac.getValue());
		}
		ac.finish();
	}

	@Test
	public void testTotalPercentRank() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("percentrank");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_PERCENT_RANK_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i] });
		}
		ac.finish();
		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i] });
			assertEquals(doubleArray3PercentRank[i],
					new Double((((Double) ac.getValue()).doubleValue() * 1000)).intValue());
		}
		ac.finish();

		// test Total.PERCENTRANK() for BigDecimal data
		ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
			assertEquals(bigDecimalPercentRank[i],
					new Double((((Double) ac.getValue()).doubleValue() * 1000)).intValue());
		}
		ac.finish();
	}

	@Test
	public void testTotalPercentSum() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("percentsum");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_PERCENTSUM_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i] });
		}
		ac.finish();

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i] });
			assertEquals(doubleArray3PercentSum[i] == null ? new Integer(0) : doubleArray3PercentSum[i],
					new Integer((int) (Double.parseDouble(ac.getValue().toString()) * 1000)));
		}
		ac.finish();
		// DataException should be throwed if the parameter is non-numeric
		ac.start();
		for (int i = 0; i < str2.length; i++) {
			try {
				ac.onRow(new Object[] { str2[i] });
				fail();
			} catch (DataException e) {
			}
		}
		ac.finish();

		// test Total.PERCENTSUM() for BigDecimal data
		ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
		}
		ac.finish();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i] });
			Object ret = ac.getValue();
			assertTrue(ret instanceof Double);
			assertEquals(bigDecimalPercentSum[i], new Double((((Number) ret).doubleValue() * 1000)).intValue());
		}
		ac.finish();
	}

	@Test
	public void testTotalPercentile() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("percentile");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_PERCENTILE_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertFalse(ag.getParameterDefn()[1].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Double(0.1) });
		}
		ac.finish();
		Object value = ac.getValue();
		assertEquals(value, new Double(10.0));

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Double(0) });
		}
		ac.finish();
		value = ac.getValue();
		assertEquals(value, new Double(10));

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Double(1) });
		}
		ac.finish();
		value = ac.getValue();
		assertEquals(value, new Double(300));

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Double(0.7) });
		}
		ac.finish();
		value = ac.getValue();
		assertEquals(value, new Double(70));

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Double(0.35) });
		}
		ac.finish();
		value = ac.getValue();
		assertEquals(value, new Double(17.5));

		try {
			ac.start();
			for (int i = 0; i < doubleArray3.length; i++) {
				ac.onRow(new Object[] { doubleArray3[i], new Double(-1) });
			}
			ac.finish();
			fail("should not arrive here");
		} catch (DataException e) {
		}

		// test the parameter boundary, which should be not less than 0 and greater than
		// 4
		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			try {
				ac.onRow(new Object[] { doubleArray3[i], new Double(-0.9) });
				fail();
			} catch (DataException e) {
			}

			try {
				ac.onRow(new Object[] { doubleArray3[i], new Double(4.1) });
				fail();
			} catch (DataException e) {
			}
		}
		ac.finish();

		// test Total.Percentile() for BigDecimal data
		ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Double(0.1) });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof Double);
		assertEquals(3.0D, ret);
	}

	@Test
	public void testTotalQuartile() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("quartile");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_QUARTILE_FUNC, ag.getName());
		assertEquals(IAggrFunction.SUMMARY_AGGR, ag.getType());
		assertEquals(2, ag.getParameterDefn().length);
		assertTrue(!ag.getParameterDefn()[0].isOptional());
		assertFalse(ag.getParameterDefn()[1].isOptional());

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Double(0) });
		}
		ac.finish();
		Object value = ac.getValue();
		assertEquals(value, new Double(10));

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Double(1) });
		}
		ac.finish();
		value = ac.getValue();
		assertEquals(value, new Double(12.5));

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Double(2) });
		}
		ac.finish();
		value = ac.getValue();
		assertEquals(value, new Double(30));

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Double(3) });
		}
		ac.finish();
		value = ac.getValue();
		assertEquals(value, new Double(85));

		ac.start();
		for (int i = 0; i < doubleArray3.length; i++) {
			ac.onRow(new Object[] { doubleArray3[i], new Double(4) });
		}
		ac.finish();
		value = ac.getValue();
		assertEquals(value, new Double(300));

		try {
			ac.start();
			for (int i = 0; i < doubleArray3.length; i++) {
				ac.onRow(new Object[] { doubleArray3[i], new Double(5) });
			}
			ac.finish();
			fail("should not arrive here");
		} catch (DataException e) {
		}
		// test Total.Quartile() for BigDecimal data
		ac = ag.newAccumulator();
		ac.start();
		for (int i = 0; i < bigDecimalArray.length; i++) {
			ac.onRow(new Object[] { bigDecimalArray[i], new Double(1) });
		}
		ac.finish();
		Object ret = ac.getValue();
		assertTrue(ret instanceof Double);
		assertEquals(4.0D, ret);
	}

	@Test
	public void testTotalRunningCount() throws Exception {
		IAggrFunction ag = buildInAggrFactory.getAggregation("runningcount");
		Accumulator ac = ag.newAccumulator();
		assertEquals(IBuildInAggregation.TOTAL_RUNNINGCOUNT_FUNC, ag.getName());
		assertEquals(IAggrFunction.RUNNING_AGGR, ag.getType());
		assertEquals(1, ag.getParameterDefn().length);

		ac.start();
		for (int i = 0; i < doubleArray1.length; i++) {
			ac.onRow(new Double[] { new Double(doubleArray1[i]), new Double(5) });
			assertEquals(new Integer(i + 1), ac.getValue());
		}
		ac.finish();

		ac.start();
		for (int i = 0; i < anyObjectArray.length; i++) {
			ac.onRow(new Object[] { anyObjectArray[i] });
		}
		ac.finish();
		assertEquals(new Integer(5), ac.getValue());

		ac.start();
		for (int i = 0; i < anyObjectArray.length; i++) {
			ac.onRow(null);
		}
		ac.finish();
		assertEquals(new Integer(8), ac.getValue());
	}

}
