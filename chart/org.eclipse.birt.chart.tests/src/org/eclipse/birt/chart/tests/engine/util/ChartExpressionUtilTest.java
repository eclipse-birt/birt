/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.tests.engine.util;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.util.ChartExpressionUtil;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;

public class ChartExpressionUtilTest extends TestCase {

	protected final ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();

	/**
	 * Construct and initialize any objects that will be used in multiple tests.
	 * Currently Empty.
	 */
	protected void setUp() throws Exception {

	}

	/**
	 * Collect and empty any objects that are used in multiple tests. Currently
	 * Empty.
	 */
	protected void tearDown() throws Exception {

	}

	public void testIsCubeBinding() {
		// Test pure binding
		assertEquals(true, exprCodec.isCubeBinding("data[\"ab c\"]", false)); //$NON-NLS-1$
		assertEquals(true, exprCodec.isCubeBinding("data[\"data\"]", false)); //$NON-NLS-1$
		assertEquals(false, exprCodec.isCubeBinding("data[\"ab c\"]+100", //$NON-NLS-1$
				false));
		assertEquals(false, exprCodec.isCubeBinding("data[\"year\"]+\"Q\"+data[\"quarter\"]", //$NON-NLS-1$
				false));

		// Test complex expression
		assertEquals(true, exprCodec.isCubeBinding("data[\"ab c\"]+100", true)); //$NON-NLS-1$
		assertEquals(true, exprCodec.isCubeBinding("100+data[\"ab c\"]", true)); //$NON-NLS-1$
		assertEquals(true, exprCodec.isCubeBinding("data[\"year\"]+\"Q\"+data[\"quarter\"]", //$NON-NLS-1$
				true));
		assertEquals(true, exprCodec.isCubeBinding(
				"if(data[\"LastJan\"]!=null)\r\n(data[\"ThisMonth\"]-data[\"LastJan\"])/data[\"LastJan\"]\r\nelse \r\nnull", //$NON-NLS-1$
				true));
	}

	public void testGetCubeBindingName() {
		assertEquals("ab c", exprCodec.getCubeBindingName("data[\"ab c\"]", //$NON-NLS-1$ //$NON-NLS-2$
				false));
		assertEquals("data", exprCodec.getCubeBindingName("data[\"data\"]", //$NON-NLS-1$ //$NON-NLS-2$
				false));
		assertEquals(null, exprCodec.getCubeBindingName("data[\"data\"] + 100", false)); //$NON-NLS-1$
		assertEquals(null, exprCodec.getCubeBindingName("data[\"year\"]+\"Q\"+data[\"quarter\"]", false)); //$NON-NLS-1$

		assertEquals("ab c", exprCodec.getCubeBindingName("data[\"ab c\"]", //$NON-NLS-1$ //$NON-NLS-2$
				true));
		assertEquals("ab c", //$NON-NLS-1$
				exprCodec.getCubeBindingName("data[\"ab c\"] + 100", true)); //$NON-NLS-1$
		assertEquals("ab c", //$NON-NLS-1$
				exprCodec.getCubeBindingName("100 * data[\"ab c\"] ", true)); //$NON-NLS-1$
		assertEquals("123", //$NON-NLS-1$
				exprCodec.getCubeBindingName("data[\"123\"] + data[\"ab c\"] ", //$NON-NLS-1$
						true));

		// Test script expression
		assertEquals("123", //$NON-NLS-1$
				exprCodec.getCubeBindingName("data[\"12\"+\"3\"] ", //$NON-NLS-1$
						true));
	}

	public void testGetCubeBindingNameList() {
		List<String> names = exprCodec.getCubeBindingNameList("data[\"123\"] + data[\"ab c\"]"); //$NON-NLS-1$
		assertEquals(2, names.size());
		assertEquals("123", names.get(0)); //$NON-NLS-1$
		assertEquals("ab c", names.get(1)); //$NON-NLS-1$

		names = exprCodec.getCubeBindingNameList("123"); //$NON-NLS-1$
		assertEquals(0, names.size());

		names = exprCodec.getCubeBindingNameList("data[\"123\"]"); //$NON-NLS-1$
		assertEquals(1, names.size());
		assertEquals("123", names.get(0)); //$NON-NLS-1$

		names = exprCodec.getCubeBindingNameList("data[\"123\"] + 100"); //$NON-NLS-1$
		assertEquals(1, names.size());
		assertEquals("123", names.get(0)); //$NON-NLS-1$

		names = exprCodec.getCubeBindingNameList("data[\"123\"] + data[\"ab c\"] + data[\"a\"]"); //$NON-NLS-1$
		assertEquals(3, names.size());
		assertEquals("123", names.get(0)); //$NON-NLS-1$
		assertEquals("ab c", names.get(1)); //$NON-NLS-1$
		assertEquals("a", names.get(2)); //$NON-NLS-1$
	}

	public void testIsDimensionExpresion() {
		assertEquals(true, exprCodec.isDimensionExpresion("dimension[\"abc\"][\"12 3\"]")); //$NON-NLS-1$
		assertEquals(true, exprCodec.isDimensionExpresion("dimension[\"a\"+\"bc\"][\"12 3\"]")); //$NON-NLS-1$
		assertEquals(false, exprCodec.isDimensionExpresion("dimension[\"abc\"][\"12 3\"]+2")); //$NON-NLS-1$
		assertEquals(false, exprCodec.isDimensionExpresion("2+dimension[\"abc\"][\"12 3\"]")); //$NON-NLS-1$
		assertEquals(false, exprCodec.isDimensionExpresion("dimension[\"abc\"][12 3]")); //$NON-NLS-1$
		assertEquals(false, exprCodec.isDimensionExpresion("dimension[\"abc\"]")); //$NON-NLS-1$
	}

	public void testGetLevelNameFromDimensionExpression() {
		String[] levels = exprCodec.getLevelNames("dimension[\"abc\"][\"12 3\"]");//$NON-NLS-1$
		assertEquals("abc", levels[0]); //$NON-NLS-1$
		assertEquals("12 3", levels[1]); //$NON-NLS-1$

		// dimension["a"+"bc"]["a"+2*3+"b"]
		levels = exprCodec.getLevelNames("dimension[\"a\"+\"bc\"][\"a\"+2*3+\"b\"]"); //$NON-NLS-1$
		assertEquals("abc", levels[0]); //$NON-NLS-1$
		assertEquals("a6b", levels[1]); //$NON-NLS-1$

		levels = exprCodec.getLevelNames("1+dimension[\"abc\"][\"12 3\"]");//$NON-NLS-1$
		assertNull(levels);
	}

	public void testIsMeasureExpresion() {
		assertEquals(true, exprCodec.isMeasureExpresion("measure[\"12 3\"]")); //$NON-NLS-1$
		assertEquals(true, exprCodec.isMeasureExpresion("measure[\"a\"+\"bc\"]")); //$NON-NLS-1$
		assertEquals(false, exprCodec.isMeasureExpresion("measure[\"12 3\"]+1")); //$NON-NLS-1$
		assertEquals(false, exprCodec.isMeasureExpresion("1*measure[\"12 3\"]")); //$NON-NLS-1$
		assertEquals(false, exprCodec.isMeasureExpresion("measure[12 3]")); //$NON-NLS-1$
		assertEquals(false, exprCodec.isMeasureExpresion("dimension[\"abc\"]")); //$NON-NLS-1$
	}

	public void testGetMeasureName() {
		assertEquals("12 3", exprCodec.getMeasureName("measure[\"12 3\"]")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("abc", exprCodec.getMeasureName("measure[\"a\"+\"bc\"]")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(exprCodec.getMeasureName("measure[\"abc\"+5]")); //$NON-NLS-1$
	}

	public void testCheckStringInExpression() {
		assertEquals(true, ChartExpressionUtil.checkStringInExpression("data[\"year\"]+\"Q\"+data[\"quarter\"]")); //$NON-NLS-1$
		assertEquals(true, ChartExpressionUtil.checkStringInExpression("\"Q\"+data[\"quarter\"]")); //$NON-NLS-1$
		assertEquals(true, ChartExpressionUtil.checkStringInExpression("data[\"quarter\"]+\"Q\"")); //$NON-NLS-1$
		assertEquals(false, ChartExpressionUtil.checkStringInExpression("data[\"year\"]+data[\"quarter\"]")); //$NON-NLS-1$
		assertEquals(false, ChartExpressionUtil.checkStringInExpression("4+data[\"quarter\"]")); //$NON-NLS-1$
	}

	public void testGetFullBindingName() {
		// Cube expression
		assertEquals("abc", //$NON-NLS-1$
				exprCodec.getFullBindingName("data[\"abc\"]")); //$NON-NLS-1$
		assertEquals("data[abc] + 100", //$NON-NLS-1$
				exprCodec.getFullBindingName("data[\"abc\"] + 100")); //$NON-NLS-1$

		// Row expression
		assertEquals("abc", //$NON-NLS-1$
				exprCodec.getFullBindingName("row[\"abc\"]")); //$NON-NLS-1$
		assertEquals("row[abc] + 100", //$NON-NLS-1$
				exprCodec.getFullBindingName("row[\"abc\"] + 100")); //$NON-NLS-1$

		// Constant
		assertEquals("abc", //$NON-NLS-1$
				exprCodec.getFullBindingName("\"abc\"")); //$NON-NLS-1$
		assertEquals("100", //$NON-NLS-1$
				exprCodec.getFullBindingName("100")); //$NON-NLS-1$
	}

}
