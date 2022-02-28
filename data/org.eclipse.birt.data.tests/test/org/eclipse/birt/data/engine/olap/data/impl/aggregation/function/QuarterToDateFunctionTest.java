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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.data.engine.api.timefunction.IPeriodsFunction;
import org.eclipse.birt.data.engine.api.timefunction.ReferenceDate;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;
import org.junit.Test;

import testutil.BaseTestCase;

public class QuarterToDateFunctionTest extends BaseTestCase {
	/*
	 * @see TestCase#tearDown()
	 */
	private void printResult(List<TimeMember> resultMember) throws IOException {
		for (int i = 0; i < resultMember.size(); i++) {
			int[] resultValues = resultMember.get(i).getMemberValue();
			for (int j = 0; j < resultValues.length; j++) {
				testPrint(String.valueOf(resultValues[j]));
				testPrint(" ");
			}
			testPrint("\n");
		}
	}

	@Test
	public void testFunctions() throws IOException {
		int[] values = { 2002, 2 };
		String[] levels = { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_QUARTER, false).getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions1() throws IOException {
		int[] values = { 2002, 8 };
		String[] levels = { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_MONTH };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_QUARTER, false).getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions2() throws IOException {
		int[] values = { 2002, 3, 9 };
		String[] levels = { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_QUARTER, false).getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions3() throws IOException {
		int[] values = { 2002, 3, 8, 18 };
		String[] levels = { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_QUARTER, false).getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions4() throws IOException {
		int[] values = { 2002, 3, 8, 3 };
		String[] levels = { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);

		ReferenceDate referenceDate = new ReferenceDate(new Date(2002, 7, 20));
		IPeriodsFunction periodsFunction = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_QUARTER, false);
		((AbstractMDX) periodsFunction).setReferenceDate(referenceDate);

		List<TimeMember> resultMember = periodsFunction.getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions5() throws IOException {
		int[] values = { 2002, 3, 8, 3 };
		String[] levels = { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);

		ReferenceDate referenceDate = new ReferenceDate(new Date(2002, 7, 20));
		IPeriodsFunction periodsFunction = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_QUARTER, true);
		((AbstractMDX) periodsFunction).setReferenceDate(referenceDate);

		List<TimeMember> resultMember = periodsFunction.getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions6() throws IOException {
		int[] values = { 2002, 3, 8, 18 };
		String[] levels = { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_QUARTER, true).getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions7() throws IOException {
		int[] values = { 2002, 8 };
		String[] levels = { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_MONTH };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_QUARTER, true).getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}
}
