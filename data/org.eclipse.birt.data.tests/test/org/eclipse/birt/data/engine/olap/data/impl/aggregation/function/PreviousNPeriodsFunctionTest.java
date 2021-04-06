package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.io.IOException;
import java.util.Date;

import org.eclipse.birt.data.engine.api.timefunction.IParallelPeriod;
import org.eclipse.birt.data.engine.api.timefunction.ReferenceDate;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;

import org.junit.Test;
import static org.junit.Assert.*;

public class PreviousNPeriodsFunctionTest {
	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testFunctions1() throws IOException {
		int[] values = new int[] { 2002, 3, 8, 7 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);

		TimeMember resultMember = TimeFunctionFactory.createParallelPeriodFunction(TimeMember.TIME_LEVEL_TYPE_YEAR, -1)
				.getResult(member);
		int[] result = new int[] { 2001, 3, 8, 7 };
		assertEquals(resultMember.getMemberValue()[0], result[0]);
		assertEquals(resultMember.getMemberValue()[1], result[1]);
		assertEquals(resultMember.getMemberValue()[2], result[2]);
		assertEquals(resultMember.getMemberValue()[3], result[3]);
	}

	@Test
	public void testFunctions2() throws IOException {
		int[] values = new int[] { 2002, 3, 8, 7 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);

		TimeMember resultMember = TimeFunctionFactory
				.createParallelPeriodFunction(TimeMember.TIME_LEVEL_TYPE_QUARTER, -5).getResult(member);
		int[] result = new int[] { 2001, 2, 5, 7 };
		assertEquals(resultMember.getMemberValue()[0], result[0]);
		assertEquals(resultMember.getMemberValue()[1], result[1]);
		assertEquals(resultMember.getMemberValue()[2], result[2]);
		assertEquals(resultMember.getMemberValue()[3], result[3]);
	}

	@Test
	public void testFunctions3() throws IOException {
		int[] values = new int[] { 2002, 3, 8, 7 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);

		TimeMember resultMember = TimeFunctionFactory.createParallelPeriodFunction(TimeMember.TIME_LEVEL_TYPE_MONTH, -4)
				.getResult(member);
		int[] result = new int[] { 2002, 2, 4, 7 };
		assertEquals(resultMember.getMemberValue()[0], result[0]);
		assertEquals(resultMember.getMemberValue()[1], result[1]);
		assertEquals(resultMember.getMemberValue()[2], result[2]);
		assertEquals(resultMember.getMemberValue()[3], result[3]);
	}

	@Test
	public void testFunctions4() throws IOException {
		int[] values = new int[] { 2002, 3, 8, 7 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);

		TimeMember resultMember = TimeFunctionFactory
				.createParallelPeriodFunction(TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH, -4).getResult(member);
		int[] result = new int[] { 2002, 3, 8, 3 };
		assertEquals(resultMember.getMemberValue()[0], result[0]);
		assertEquals(resultMember.getMemberValue()[1], result[1]);
		assertEquals(resultMember.getMemberValue()[2], result[2]);
		assertEquals(resultMember.getMemberValue()[3], result[3]);
	}

	@Test
	public void testFunctions5() throws IOException {
		int[] values = new int[] { 2002, 3, 8, 7 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);

		TimeMember resultMember = TimeFunctionFactory
				.createParallelPeriodFunction(TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH, -10).getResult(member);
		int[] result = new int[] { 2002, 3, 7, 28 };
		assertEquals(resultMember.getMemberValue()[0], result[0]);
		assertEquals(resultMember.getMemberValue()[1], result[1]);
		assertEquals(resultMember.getMemberValue()[2], result[2]);
		assertEquals(resultMember.getMemberValue()[3], result[3]);
	}

	@Test
	public void testFunctions6() throws IOException {
		int[] values = new int[] { 2002, 8, 7 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);

		TimeMember resultMember = TimeFunctionFactory
				.createParallelPeriodFunction(TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH, -10).getResult(member);
		int[] result = new int[] { 2002, 7, 28 };
		assertEquals(resultMember.getMemberValue()[0], result[0]);
		assertEquals(resultMember.getMemberValue()[1], result[1]);
		assertEquals(resultMember.getMemberValue()[2], result[2]);
	}

	@Test
	public void testFunctions7() throws IOException {
		int[] values = new int[] { 2002, 8, 4 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);
		IParallelPeriod parallelPeriod = TimeFunctionFactory
				.createParallelPeriodFunction(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH, -5);

		ReferenceDate referenceDate = new ReferenceDate(new Date(2002, 7, 20));
		((AbstractMDX) parallelPeriod).setReferenceDate(referenceDate);

		TimeMember resultMember = parallelPeriod.getResult(member);
		int[] result = new int[] { 2002, 7, 3 };
		assertEquals(resultMember.getMemberValue()[0], result[0]);
		assertEquals(resultMember.getMemberValue()[1], result[1]);
		assertEquals(resultMember.getMemberValue()[2], result[2]);
	}

	@Test
	public void testFunctions8() throws IOException {
		int[] values = new int[] { 2002, 125 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR };
		TimeMember member = new TimeMember(values, levels);

		TimeMember resultMember = TimeFunctionFactory
				.createParallelPeriodFunction(TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR, -25).getResult(member);
		int[] result = new int[] { 2002, 100 };
		assertEquals(resultMember.getMemberValue()[0], result[0]);
		assertEquals(resultMember.getMemberValue()[1], result[1]);
	}

	@Test
	public void testFunctions9() throws IOException {
		int[] values = new int[] { 2002, 23 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR };
		TimeMember member = new TimeMember(values, levels);

		IParallelPeriod parallelPeriod = TimeFunctionFactory
				.createParallelPeriodFunction(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR, -10);

		ReferenceDate referenceDate = new ReferenceDate(new Date(2002, 5, 5));
		((AbstractMDX) parallelPeriod).setReferenceDate(referenceDate);

		TimeMember resultMember = parallelPeriod.getResult(member);
		int[] result = new int[] { 2002, 13 };
		assertEquals(resultMember.getMemberValue()[0], result[0]);
		assertEquals(resultMember.getMemberValue()[1], result[1]);
	}
}
