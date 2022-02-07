/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.tests.engine.datafeed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.birt.chart.internal.datafeed.ChartVariableHelper;

/**
 * ChartVariableHelperTest
 */
public class ChartVariableHelperTest extends TestCase {

	private String fCategoryExpr = "row[\"COUNTRY\"]"; //$NON-NLS-1$

	private String fSeriesExpr = "row[\"CREDILIMIT\"]"; //$NON-NLS-1$

	private String fSeriesName = "\"Series 1\""; //$NON-NLS-1$

	Object[] fParams = new String[4];

	private ChartVariableHelper fTestInstance;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		fParams[1] = fCategoryExpr;
		fParams[2] = fSeriesExpr;
		fParams[3] = fSeriesName;
		fTestInstance = new ChartVariableHelper();
	}

	/**
	 * The method test
	 * {@link org.eclipse.birt.chart.internal.datafeed.ChartVariableHelper#parseChartVariables(String, String, String, String )}
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void testParseChartVariables() throws SecurityException, NoSuchMethodException {
		Method m = ChartVariableHelper.class.getDeclaredMethod("parseChartVariables", //$NON-NLS-1$
				String.class, String.class, String.class, String.class);
		m.setAccessible(true);

		try {
			// Test common case.
			String v1Src = "categoryData + \":\" + valueData + \":\" + valueSeriesName"; //$NON-NLS-1$
			String v1Target = "row[\"COUNTRY\"] + \":\" + row[\"CREDILIMIT\"] + \":\" + \"Series 1\""; //$NON-NLS-1$
			fParams[0] = v1Src;
			Object result = m.invoke(fTestInstance, fParams);
			assertEquals(v1Target, result);

			// Test string case.
			String v2Src = "categoryData + \":\" + row[\"valueData\"] + \":\" + valueSeriesName"; //$NON-NLS-1$
			String v2Target = "row[\"COUNTRY\"] + \":\" + row[\"valueData\"] + \":\" + \"Series 1\""; //$NON-NLS-1$
			fParams[0] = v2Src;
			result = m.invoke(fTestInstance, fParams);
			assertEquals(v2Target, result);

			// Test C plus plus comments.
			String v3Src = "categoryData + \":\" + valueData + \":\" + valueSeriesName" //$NON-NLS-1$
					+ "\n//This is C plus plus comments."; //$NON-NLS-1$
			String v3Target = "row[\"COUNTRY\"] + \":\" + row[\"CREDILIMIT\"] + \":\" + \"Series 1\"" //$NON-NLS-1$
					+ "\n//This is C plus plus comments."; //$NON-NLS-1$
			fParams[0] = v3Src;
			result = m.invoke(fTestInstance, fParams);
			assertEquals(v3Target, result);

			// Test C plus plus comments.
			String v4Src = "//This is C plus plus comments.\n" //$NON-NLS-1$
					+ "categoryData + \":\" + valueData + \":\" + valueSeriesName"; //$NON-NLS-1$
			String v4Target = "//This is C plus plus comments.\n" //$NON-NLS-1$
					+ "row[\"COUNTRY\"] + \":\" + row[\"CREDILIMIT\"] + \":\" + \"Series 1\""; //$NON-NLS-1$
			fParams[0] = v4Src;
			result = m.invoke(fTestInstance, fParams);
			assertEquals(v4Target, result);

			// Test comments.
			String v5Src = "/*This is C plus \nplus comments.*/\n" //$NON-NLS-1$
					+ "categoryData + \":\" + valueData + \":\" + valueSeriesName"; //$NON-NLS-1$
			String v5Target = "/*This is C plus \nplus comments.*/\n" //$NON-NLS-1$
					+ "row[\"COUNTRY\"] + \":\" + row[\"CREDILIMIT\"] + \":\" + \"Series 1\""; //$NON-NLS-1$
			fParams[0] = v5Src;
			result = m.invoke(fTestInstance, fParams);
			assertEquals(v5Target, result);

			String v6Src = "/*This is C plus \nplus comments.*/\n" //$NON-NLS-1$
					+ "(categoryData)+valueData + \":\" + valueSeriesName"; //$NON-NLS-1$
			String v6Target = "/*This is C plus \nplus comments.*/\n" //$NON-NLS-1$
					+ "(row[\"COUNTRY\"])+row[\"CREDILIMIT\"] + \":\" + \"Series 1\""; //$NON-NLS-1$
			fParams[0] = v6Src;
			result = m.invoke(fTestInstance, fParams);
			assertEquals(v6Target, result);
		} catch (IllegalArgumentException e) {
			fail("Exception happened, failed to test."); //$NON-NLS-1$
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			fail("Exception happened, failed to test."); //$NON-NLS-1$
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			fail("Exception happened, failed to test."); //$NON-NLS-1$
			e.printStackTrace();
		}

		m.setAccessible(false);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestSuite(ChartVariableHelperTest.class);
	}

}
