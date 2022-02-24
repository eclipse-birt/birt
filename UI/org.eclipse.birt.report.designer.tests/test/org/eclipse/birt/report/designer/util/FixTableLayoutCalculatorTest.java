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

package org.eclipse.birt.report.designer.util;

import junit.framework.TestCase;

/**
 *  
 */

public class FixTableLayoutCalculatorTest extends TestCase {

	FixTableLayoutCalculator calculator;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		calculator = new FixTableLayoutCalculator();
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCalWidthCase1() {
		String[] width = new String[3];
		width[0] = "33";
		width[1] = "33";
		width[2] = "34";

		calculator.setTableWidth(100);
		calculator.setDefinedColWidth(width);

		doCheck();

	}

	/**
	 *  
	 */
	private void doCheck() {
		float amt = 0;
		int[] width = calculator.getIntColWidth();
		for (int i = 0; i < width.length; i++) {
			amt = amt + width[i];
			System.out.println("the result is " + width[i]);
		}

		System.out.println("the difference is " + (amt - 100));
		assertFalse(Math.abs(amt - 100) > 1);
	}

	/**
	 *  
	 */
	private void doCheck(float minSize, float tableWidth) {
		int[] width = calculator.getIntColWidth();
		if (!(minSize * width.length > tableWidth)) {
			doCheck();
		} else {
			float amt = 0;
			for (int i = 0; i < width.length; i++) {
				amt = amt + width[i];
				System.out.println("the result is " + width[i]);
			}
		}
	}

	public void testCalWidthCase2() {
		String[] width = new String[3];
		width[0] = "33";
		width[1] = "33";
		width[2] = "33";

		calculator.setTableWidth(100);
		calculator.setDefinedColWidth(width);
		doCheck();
	}

	public void testCalWidthCase3() {
		String[] width = new String[3];
		width[0] = "40%";
		width[1] = "20";
		width[2] = "20";

		calculator.setTableWidth(100);
		calculator.setDefinedColWidth(width);
		doCheck();
	}

	public void testCalWidthCase4() {
		String[] width = new String[3];
		width[0] = "40%";
		width[1] = "40%";
		width[2] = "20%";

		calculator.setTableWidth(100);
		calculator.setDefinedColWidth(width);
		doCheck();
	}

	public void testCalWidthCase5() {
		String[] width = new String[3];
		width[0] = "40%";
		width[1] = "30";
		width[2] = "20%";

		calculator.setTableWidth(100);
		calculator.setDefinedColWidth(width);
		doCheck();
	}

	public void testCalWidthCase6() {
		String[] width = new String[3];
		width[0] = "40";
		width[1] = "";
		width[2] = "90";

		calculator.setTableWidth(100);
		calculator.setDefinedColWidth(width);
		calculator.setColMinSize(6);
		doCheck();
	}

	public void testCalWidthCase7() {
		String[] width = new String[3];
		width[0] = "40%";
		width[1] = "90%";
		width[2] = "30%";

		calculator.setTableWidth(100);
		calculator.setDefinedColWidth(width);
		calculator.setColMinSize(20);

		doCheck();
	}

	public void testCalWidthCase8() {
		String[] width = new String[3];
		width[0] = "20%";
		width[1] = "20%";
		width[2] = "";

		calculator.setTableWidth(100);
		calculator.setDefinedColWidth(width);
		calculator.setColMinSize(3);

		doCheck();
	}

	public void testCalWidthCase9() {
		String[] width = new String[3];
		width[0] = "20";
		width[1] = "20";
		width[2] = "";

		calculator.setTableWidth(100);
		calculator.setDefinedColWidth(width);
		calculator.setColMinSize(5);

		doCheck();
	}

	public void testCalWidthCase10() {
		String[] width = new String[9];
		width[0] = "20";
		width[1] = "20";
		width[2] = "";
		width[3] = "20";
		width[4] = "20";
		width[5] = "";
		width[6] = "20";
		width[7] = "20";
		width[8] = "";

		calculator.setTableWidth(100);
		calculator.setDefinedColWidth(width);
		calculator.setColMinSize(10);

		doCheck();
	}

	public void testCalWidthCase11() {
		String[] width = new String[9];
		width[0] = "3";
		width[1] = "3";
		width[2] = "";
		width[3] = "20%";
		width[4] = "10%";
		width[5] = "";
		width[6] = "10%";
		width[7] = "20";
		width[8] = "";

		calculator.setTableWidth(10);
		calculator.setDefinedColWidth(width);
		calculator.setColMinSize(10);
		doCheck(10, 10);
	}

	public void testCalWidthCase12() {
		String[] width = new String[3];
		width[0] = "3.3333";
		width[1] = "3.3332";
		width[2] = "11.9098";

		calculator.setTableWidth(100);
		calculator.setDefinedColWidth(width);
		calculator.setColMinSize(10);
		doCheck(10, 10);
	}

	public void testCalWidthCase13() {
		String[] width = new String[3];
		width[0] = "3.3333";
		width[1] = "3.3332";
		width[2] = "11.9098";

		calculator.setTableWidth(19);
		calculator.setDefinedColWidth(width);
		calculator.setColMinSize(10);
		doCheck(10, 10);
	}

	public void testCalWidthCase14() {
		String[] width = new String[3];
		width[0] = "3.3333a";
		width[1] = "3.3332";
		width[2] = "11.9098";

		calculator.setTableWidth(19);
		calculator.setDefinedColWidth(width);
		calculator.setColMinSize(10);
		try {
			doCheck(10, 10);
			fail("shoule catch the exception");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public void testCalWidthCase15() {
		String[] width = new String[3];
		width[0] = "20";
		width[1] = "30";
		width[2] = "11.9098";

		calculator.setTableWidth(30);
		calculator.setDefinedColWidth(width);
		calculator.setColMinSize(15);
		try {
			doCheck(10, 10);
		} catch (NumberFormatException e) {
			fail("catch the exception");

			e.printStackTrace();
		}
	}

	public void testCalWidthCase16() {
		String[] width = new String[3];
		width[0] = "20";
		width[1] = "30";
		width[2] = "11.9098";

		calculator.setTableWidth(0);
		calculator.setDefinedColWidth(width);
		calculator.setColMinSize(15);
		try {
			doCheck(10, 10);
		} catch (NumberFormatException e) {
			fail("catch the exception");

			e.printStackTrace();
		}
	}

	public void testCalWidthCase17() {
		String[] width = new String[3];
		width[0] = "20";
		width[1] = "30";
		width[2] = "40";

		calculator.setTableWidth(0);
		calculator.setDefinedColWidth(width);
		calculator.setColMinSize(15);
		try {
			doCheck(10, 10);
		} catch (NumberFormatException e) {
			fail("catch the exception");

			e.printStackTrace();
		}
	}

}
