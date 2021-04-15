/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.data;

import java.util.Set;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.exception.CoreException;
import org.junit.Test;

import junit.framework.TestCase;

/**
 *
 */
public class ExpressionUtilTest extends TestCase {

	@Test
	public void testToNewExpression() {
		ExpressionUtil.resetSuffix();

		String[] oldExpressions = new String[] { null, "   " + Messages.getString("ExpressionUtilTest.old.0"),
				Messages.getString("ExpressionUtilTest.old.1"), Messages.getString("ExpressionUtilTest.old.2"),
				Messages.getString("ExpressionUtilTest.old.3"), Messages.getString("ExpressionUtilTest.old.4"),
				Messages.getString("ExpressionUtilTest.old.5"), Messages.getString("ExpressionUtilTest.old.6"),
				Messages.getString("ExpressionUtilTest.old.7"), Messages.getString("ExpressionUtilTest.old.8"),
				Messages.getString("ExpressionUtilTest.old.9"), Messages.getString("ExpressionUtilTest.old.10"),
				Messages.getString("ExpressionUtilTest.old.11"), Messages.getString("ExpressionUtilTest.old.12")

		};

		String[] newExpressions = new String[] { null, "   " + Messages.getString("ExpressionUtilTest.new.0"),
				Messages.getString("ExpressionUtilTest.new.1"), Messages.getString("ExpressionUtilTest.new.2"),
				Messages.getString("ExpressionUtilTest.new.3"), Messages.getString("ExpressionUtilTest.new.4"),
				Messages.getString("ExpressionUtilTest.new.5"), Messages.getString("ExpressionUtilTest.new.6"),
				Messages.getString("ExpressionUtilTest.new.7"), Messages.getString("ExpressionUtilTest.new.8"),
				Messages.getString("ExpressionUtilTest.new.9"), Messages.getString("ExpressionUtilTest.new.10"),
				Messages.getString("ExpressionUtilTest.new.11"), Messages.getString("ExpressionUtilTest.new.12"), };

		for (int i = 0; i < oldExpressions.length; i++) {
			IColumnBinding icb = ExpressionUtil.getColumnBinding(oldExpressions[i]);
			assertEquals(icb.getBoundExpression(), newExpressions[i]);
			assertEquals(icb.getResultSetColumnName(), "COLUMN_" + (i + 1));
		}
	}

	@Test
	public void testreplaceRowsExpression() {
		String[] oldExpressions = new String[] { null, "   " + Messages.getString("ExpressionUtilTest.old.13"),
				Messages.getString("ExpressionUtilTest.old.14"), Messages.getString("ExpressionUtilTest.old.15"),
				Messages.getString("ExpressionUtilTest.old.16"), Messages.getString("ExpressionUtilTest.old.17"),
				Messages.getString("ExpressionUtilTest.old.18"), Messages.getString("ExpressionUtilTest.old.19"),
				Messages.getString("ExpressionUtilTest.old.20"), Messages.getString("ExpressionUtilTest.old.21"),
				Messages.getString("ExpressionUtilTest.old.22"), Messages.getString("ExpressionUtilTest.old.23"),
				Messages.getString("ExpressionUtilTest.old.24"), Messages.getString("ExpressionUtilTest.old.25")

		};

		String[] newExpressions = new String[] { null, "   " + Messages.getString("ExpressionUtilTest.new.13"),
				Messages.getString("ExpressionUtilTest.new.14"), Messages.getString("ExpressionUtilTest.new.15"),
				Messages.getString("ExpressionUtilTest.new.16"), Messages.getString("ExpressionUtilTest.new.17"),
				Messages.getString("ExpressionUtilTest.new.18"), Messages.getString("ExpressionUtilTest.new.19"),
				Messages.getString("ExpressionUtilTest.new.20"), Messages.getString("ExpressionUtilTest.new.21"),
				Messages.getString("ExpressionUtilTest.new.22"), Messages.getString("ExpressionUtilTest.new.23"),
				Messages.getString("ExpressionUtilTest.new.24"), Messages.getString("ExpressionUtilTest.new.25"), };

		for (int i = 0; i < oldExpressions.length; i++) {
			assertEquals(ExpressionUtil.updateParentQueryReferenceExpression(oldExpressions[i], false),
					newExpressions[i]);
		}

		String paramBinding = "rows[0].abc";
		String result = "row.abc";
		assertEquals(ExpressionUtil.updateParentQueryReferenceExpression(paramBinding, true), result);
	}

	@Test
	public void testCreateExpression() {
		assertEquals("row[\"abc\"]", ExpressionUtil.createRowExpression("abc"));
		assertEquals("row[\"\"]", ExpressionUtil.createRowExpression(null));
		assertEquals("dataSetRow[\"abc\"]", ExpressionUtil.createDataSetRowExpression("abc"));
		assertEquals("dataSetRow[\"\"]", ExpressionUtil.createDataSetRowExpression(null));
	}

	@Test
	public void testIsScalarParamReference() {
		assertTrue(ExpressionUtil.isScalarParamReference("params[\"aaa\"]"));
		assertFalse(ExpressionUtil.isScalarParamReference("params[\"\"]"));

		assertFalse(ExpressionUtil.isScalarParamReference("reparams[\"aaa\"]"));
		assertFalse(ExpressionUtil.isScalarParamReference("params[aaa]"));
	}

	@Test
	public void testGetColumnBindingName() throws BirtException {
		assertTrue(ExpressionUtil.getColumnBindingName("100") == null);
		assertTrue(ExpressionUtil.getColumnBindingName("row[\"col1\"]").equals("col1"));
		assertTrue(ExpressionUtil.getColumnBindingName("row[\"col1\"+1]").equals("col11"));
		assertTrue(ExpressionUtil.getColumnBindingName("row[\"col1\"]+ \"abc\"") == null);
		assertTrue(ExpressionUtil.getColumnBindingName("row[0]") == null);
		assertTrue(ExpressionUtil.getColumnBindingName("row.col1").equals("col1"));
		assertTrue(ExpressionUtil.getColumnBindingName("100+row[\"col1\"]") == null);
		assertTrue(ExpressionUtil.getColumnBindingName("Total.sum( row[\"col1\"])") == null);
		assertTrue(ExpressionUtil.getColumnBindingName("row[\"col1\"]+ row[\"col2\"]") == null);

	}

	@Test
	public void testCreateJSRowExpression() throws BirtException {
		assertEquals(ExpressionUtil.createJSRowExpression("abc"), "row[\"abc\"]");
		assertEquals(ExpressionUtil.createJSRowExpression(null), "row[\"\"]");
	}

	@Test
	public void testCreateJSDataSetRowExpression() throws BirtException {
		assertEquals(ExpressionUtil.createJSDataSetRowExpression("abc"), "dataSetRow[\"abc\"]");
		assertEquals(ExpressionUtil.createJSDataSetRowExpression(null), "dataSetRow[\"\"]");
	}

	@Test
	public void testCreateJSParameterExpression() throws BirtException {
		assertEquals(ExpressionUtil.createJSParameterExpression("abc"), "params[\"abc\"]");
		assertEquals(ExpressionUtil.createJSParameterExpression(null), "params[\"\"]");
	}

	@Test
	public void testCreateJSMeasureExpression() throws BirtException {
		assertEquals(ExpressionUtil.createJSMeasureExpression("abc"), "measure[\"abc\"]");
		assertEquals(ExpressionUtil.createJSMeasureExpression(null), "measure[\"\"]");
	}

	@Test
	public void testCreateJSDimensionExpression() throws BirtException {
		assertEquals(ExpressionUtil.createJSDimensionExpression("abc", "def"), "dimension[\"abc\"][\"def\"]");
		assertEquals(ExpressionUtil.createJSDimensionExpression(null, null), "dimension[\"\"][\"\"]");
		assertEquals(ExpressionUtil.createJSDimensionExpression("abc", "def", "ghi"),
				"dimension[\"abc\"][\"def\"][\"ghi\"]");
	}

	@Test
	public void testReplaceParameterExpression() {
		assertEquals(ExpressionUtil.replaceParameterName("params[\"param\"]", "param", "PARAM"), "params[\"PARAM\"]");
		assertEquals(ExpressionUtil.replaceParameterName("params[\"param1\"]+ params[\"param2\"]", "param2", "PARAM2"),
				"params[\"param1\"]+ params[\"PARAM2\"]");
		assertEquals(ExpressionUtil.replaceParameterName("123+ params[\"param2\"]", "param2", "PARAM2"),
				"123+ params[\"PARAM2\"]");
		assertEquals(ExpressionUtil.replaceParameterName("params.param1+ params[\"param2\"]", "param2", "PARAM2"),
				"params.param1+ params[\"PARAM2\"]");
		assertEquals(
				ExpressionUtil.replaceParameterName("params.param1.value+ params.param2.value", "param2", "PARAM2"),
				"params.param1.value+ params.PARAM2.value");
	}

	@Test
	public void testReferedMeasureExpression() throws CoreException {
		assertEquals(ExpressionUtil.getReferencedMeasure("measure[\"m1\"]"), "m1");
		assertEquals(ExpressionUtil.getReferencedMeasure("measure[\"m1\"]+measure[\"m2\"]"), "m1");
		Set str = ExpressionUtil.getAllReferencedMeasures("measure[\"m1\"]+ measure[\"m2\"]*measure[\"m3\"]");
		assertEquals(str.contains("m1"), true);
		assertEquals(str.contains("m2"), true);
		assertEquals(str.contains("m3"), true);
		Set set = ExpressionUtil.getAllReferencedMeasures("measure[\"m1\"]/measure[\"m2\"]*measure[\"m2\"]");
		assertEquals(set.contains("m1"), true);
		assertEquals(set.contains("m2"), true);
		assertEquals(set.contains("m3"), false);
	}
}
