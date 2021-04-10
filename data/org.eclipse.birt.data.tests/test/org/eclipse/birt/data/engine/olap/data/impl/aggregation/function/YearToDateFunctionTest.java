
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.data.engine.api.timefunction.IPeriodsFunction;
import org.eclipse.birt.data.engine.api.timefunction.ReferenceDate;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;

import testutil.BaseTestCase;

import org.junit.Test;

public class YearToDateFunctionTest extends BaseTestCase {
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
		int[] values = new int[] { 2002 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_YEAR, false).getResult(member);

		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions1() throws IOException {
		int[] values = new int[] { 2002, 8 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_MONTH };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_YEAR, false).getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions2() throws IOException {
		int[] values = new int[] { 2002, 3, 9 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_YEAR, false).getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions3() throws IOException {
		int[] values = new int[] { 2004, 3, 8, 18 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_YEAR, false).getResult(member);

		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions4() throws IOException {
		int[] values = new int[] { 2004, 3, 8, 4 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH };

		ReferenceDate referenceDate = new ReferenceDate(new Date(2004, 7, 26));
		IPeriodsFunction periodsFunction = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_YEAR, false);
		((AbstractMDX) periodsFunction).setReferenceDate(referenceDate);

		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = periodsFunction.getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions5() throws IOException {
		int[] values = new int[] { 2004, 3, 8, 4 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH };

		ReferenceDate referenceDate = new ReferenceDate(new Date(2004, 7, 26));
		IPeriodsFunction periodsFunction = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_YEAR, true);
		((AbstractMDX) periodsFunction).setReferenceDate(referenceDate);

		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = periodsFunction.getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions6() throws IOException {
		int[] values = new int[] { 2004, 3, 8, 18 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH, TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_YEAR, true).getResult(member);

		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions7() throws IOException {
		int[] values = new int[] { 2002, 8 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_MONTH };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_YEAR, true).getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}

	@Test
	public void testFunctions8() throws IOException {
		int[] values = new int[] { 2002, 2 };
		String[] levels = new String[] { TimeMember.TIME_LEVEL_TYPE_YEAR, TimeMember.TIME_LEVEL_TYPE_QUARTER };
		TimeMember member = new TimeMember(values, levels);

		List<TimeMember> resultMember = TimeFunctionFactory
				.createPeriodsToDateFunction(TimeMember.TIME_LEVEL_TYPE_YEAR, true).getResult(member);
		printResult(resultMember);
		this.checkOutputFile();
	}
}
