
package org.eclipse.birt.core.data;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * test case for expression parser
 *
 */
public class ExpressionParserUtilityTest extends TestCase {

	String[] oldExpressions = new String[] { null, "   " + Messages.getString("ExpressionUtilTest.old.0"),
			Messages.getString("ExpressionUtilTest.old.1"), Messages.getString("ExpressionUtilTest.old.2"),
			Messages.getString("ExpressionUtilTest.old.3"), Messages.getString("ExpressionUtilTest.old.4"),
			Messages.getString("ExpressionUtilTest.old.5"), Messages.getString("ExpressionUtilTest.old.6"),
			Messages.getString("ExpressionUtilTest.old.7"), Messages.getString("ExpressionUtilTest.old.8"),
			Messages.getString("ExpressionUtilTest.old.9"), Messages.getString("ExpressionUtilTest.old.10"),
			Messages.getString("ExpressionUtilTest.old.11") };

	@Test
	public void testExpression1() {
		String expression = oldExpressions[1];
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression);
			assertTrue(list.size() == 1);
		} catch (BirtException e) {
			fail("An exception occurs");
		}
	}

	@Test
	public void testExpression2() {
		String expression = oldExpressions[2];
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression);
			assertTrue(list.size() == 1);
		} catch (BirtException e) {
			fail("An exception occurs");
		}
	}

	@Test
	public void testExpression3() {
		String expression = oldExpressions[3];
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression);
			assertTrue(list.size() == 2);
		} catch (BirtException e) {
			fail("An exception occurs");
		}
	}

	@Test
	public void testExpression4() {
		String expression = oldExpressions[4];
		try {
			ExpressionParserUtility.compileColumnExpression(expression);
			fail("Should throw a BirtException.");
		} catch (BirtException e) {
		}
	}

	@Test
	public void testExpression5() {
		String expression = oldExpressions[5];
		try {
			ExpressionParserUtility.compileColumnExpression(expression);
			fail("Should throw a BirtException.");
		} catch (BirtException e) {
		}
	}

	@Test
	public void testExpression6() {
		String expression = oldExpressions[6];
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression);
			assertTrue(list.size() == 1);
		} catch (BirtException e) {
			fail("An exception occurs");
		}
	}

	@Test
	public void testExpression7() {
		String expression = oldExpressions[7];
		try {
			ExpressionParserUtility.compileColumnExpression(expression);
			fail("Should throw a BirtException.");
		} catch (BirtException e) {
		}
	}

	@Test
	public void testExpression8() {
		String expression = oldExpressions[8];
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression);
			assertTrue(list.size() == 1);
		} catch (BirtException e) {
			fail("An exception occurs");
		}
	}

	@Test
	public void testExpression9() {
		String expression = oldExpressions[9];
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression);
			assertTrue(list.size() == 1);
		} catch (BirtException e) {
			fail("An exception occurs");
		}
	}

	@Test
	public void testExpression10() {
		String expression = oldExpressions[10];
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression);
			assertTrue(list.size() == 2);
		} catch (BirtException e) {
			fail("An exception occurs");
		}
	}

	@Test
	public void testExpression11() {
		String expression = oldExpressions[11];
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression);
			assertTrue(list.size() == 1);
		} catch (BirtException e) {
			fail("An exception occurs");
		}
	}

	@Test
	public void testExpression12() {
		String expression = oldExpressions[12];
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression);
			assertTrue(list.size() == 3);
		} catch (BirtException e) {
			fail("An exception occurs");
		}
	}

	@Test
	public void testAggregationExpression13() {
		String expression = "row[\"customer\"].replace(\"aa\",\"bb\")";
		String expression2 = "( row[\"customer\"]+ row.customer ).replace(\"aa\",\"bb\")";
		String expression3 = "( row[\"customer\"]+ row.customer ).replace(row.aaa.replace(\"aa\",\"bb\"), row.bbb );";
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression);
			assertTrue(list.size() == 1);
			list = ExpressionParserUtility.compileColumnExpression(expression2);
			assertTrue(list.size() == 2);
			list = ExpressionParserUtility.compileColumnExpression(expression3);
			assertTrue(list.size() == 4);
		} catch (BirtException e) {
			fail("An exception occurs");
		}
	}

	@Test
	public void testAggregationExpression14() {
		String expression = "row._outer[\"aaa\"]";
		String expression2 = "row._outer._outer._outer[\"aaa\"]";
		String expression3 = "row._outer._outer._outer._outer[\"aaa\"]+ row._outer[\"bbb\"] + 123";
		String expression4 = "row._outer._outer._outer._outer.__rownum + row._outer[\"bbb\"]/row.aaa + 123";
		String expression5 = "Total.sum(row._outer._outer.aaa) + Total.sum(row._outer._outer.aaa + 1 ) +Total.ave( row._rownum._outer.bbb)";
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression);
			assertTrue(list.size() == 1);
			assertTrue(((IColumnBinding) list.get(0)).getResultSetColumnName().equals("aaa"));
			assertTrue(((IColumnBinding) list.get(0)).getOuterLevel() == 1);

			list = ExpressionParserUtility.compileColumnExpression(expression2);
			assertTrue(list.size() == 1);
			assertTrue(((IColumnBinding) list.get(0)).getResultSetColumnName().equals("aaa"));
			assertTrue(((IColumnBinding) list.get(0)).getOuterLevel() == 3);

			list = ExpressionParserUtility.compileColumnExpression(expression3);
			assertTrue(list.size() == 2);
			assertTrue(((IColumnBinding) list.get(0)).getResultSetColumnName().equals("aaa"));
			assertTrue(((IColumnBinding) list.get(0)).getOuterLevel() == 4);
			assertTrue(((IColumnBinding) list.get(1)).getResultSetColumnName().equals("bbb"));
			assertTrue(((IColumnBinding) list.get(1)).getOuterLevel() == 1);

			list = ExpressionParserUtility.compileColumnExpression(expression4);
			assertTrue(list.size() == 3);
			assertTrue(((IColumnBinding) list.get(0)).getResultSetColumnName().equals("__rownum"));
			assertTrue(((IColumnBinding) list.get(0)).getOuterLevel() == 4);
			assertTrue(((IColumnBinding) list.get(1)).getResultSetColumnName().equals("bbb"));
			assertTrue(((IColumnBinding) list.get(1)).getOuterLevel() == 1);
			assertTrue(((IColumnBinding) list.get(2)).getResultSetColumnName().equals("aaa"));
			assertTrue(((IColumnBinding) list.get(2)).getOuterLevel() == 0);

			list = ExpressionParserUtility.compileColumnExpression(expression5);
			assertTrue(list.size() == 3);

		} catch (BirtException e) {
			fail("An exception occurs");
		}
	}

	@Test
	public void testHasAggregation() {
		String expression0 = "Totla.aaa(ccc)+ ";
		String expression1 = "row[\"customer\"].replace(\"aa\",\"bb\")";
		String expression2 = "Total.sum(row.aaa)";
		String expression3 = "row.aaa+Total.runningSum( row.bbb+ row.ccc ) + row.eee";
		String expression4 = "row.aaa+Total.runningSum( Total.sum(row.aaa)+ row.ccc ) + row.eee";
		try {
			ExpressionParserUtility.hasAggregation(expression0);
			fail("Should not get here");
		} catch (BirtException e) {
		}
		try {
			assertTrue(!ExpressionParserUtility.hasAggregation(expression1));
			assertTrue(ExpressionParserUtility.hasAggregation(expression2));
			assertTrue(ExpressionParserUtility.hasAggregation(expression3));
			assertTrue(ExpressionParserUtility.hasAggregation(expression4));

		} catch (BirtException e) {
			fail(" An exception occurs");
		}
	}

	@Test
	public void testHasCarriageReturn() {
		String expression0 = "if(row.aaa>0)  \r\n row.bbb = Total.sum(row.aaa) \n else row.bbb=Total.sum(row.aaa)";
		String expression1 = " function a( ){ if(row.aaa>0) row.ccc = Total.sum(row.bbb);}";
		String expression2 = "if (row[\"STATE\"])\n{\n   row[\"CITY\"] + \", \" + row[\"STATE\"] + \" - \" + row[\"POSTALCODE\"];\n}\nelse\n{\n   row[\"CITY\"] + \", \" + row[\"POSTALCODE\"];\n}";
		String expression3 = "row[\"aaa\"]= \nrow.bbb";
		String expression4 = "if (Total.sum( row[\"CUSTOMERNUMBER\"])>1)  Total.sum( row[\"CUSTOMERNUMBER_1\"]); else \"bbb\";";
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression0);
			assertTrue(list.size() == 5);
			list = ExpressionParserUtility.compileColumnExpression(expression1);
			assertTrue(list.size() == 3);
			list = ExpressionParserUtility.compileColumnExpression(expression2);
			assertTrue(list.size() == 6);
			list = ExpressionParserUtility.compileColumnExpression(expression3);
			assertTrue(list.size() == 2);
			list = ExpressionParserUtility.compileColumnExpression(expression4);
			assertTrue(list.size() == 2);
		} catch (BirtException e) {
			fail("should not get there ");
		}
	}

	@Test
	public void testRows0Expr() {
		String expression0 = "rows[0][\"aaa\"] = 0";
		String expression1 = "if(row.aaa>0)  \r\n row.bbb = Total.sum(rows[0][\"aaa\"]) \n else row.bbb=Total.sum(row.aaa)";
		String expression2 = " function a( ){ if(row.aaa>0) rows[0][\"ccc\"] = Total.sum(row.bbb);}";
		String expression3 = "if (row[\"STATE\"])\n{\n   rows[0][\"CITY\"] + \", \" + row[\"STATE\"] + \" - \" + row[\"POSTALCODE\"];\n}\nelse\n{\n   row[\"CITY\"] + \", \" + row[\"POSTALCODE\"];\n}";
		String expression4 = "row[\"aaa\"]= \nrows[0][\"bbb\"]";
		String expression5 = "if (Total.sum( rows[0][\"CUSTOMERNUMBER\"])>1)  Total.sum( row[\"CUSTOMERNUMBER_1\"]); else \"bbb\";";
		String expression6 = "rows[1][\"bbb\"]=1; rows[0][\"aaa\"] = 5;";
		String expression7 = "rows[0][\"aaa\"][\"bbb\"]+5";
		try {
			List list = ExpressionParserUtility.compileColumnExpression(expression0);
			assertTrue(list.size() == 1);
			assertTrue("aaa".equals(((IColumnBinding) list.get(0)).getResultSetColumnName()));
			assertTrue("dataSetRow[\"aaa\"]".equals(((IColumnBinding) list.get(0)).getBoundExpression()));
			assertTrue(1 == ((IColumnBinding) list.get(0)).getOuterLevel());

			list = ExpressionParserUtility.compileColumnExpression(expression1);
			assertTrue(list.size() == 5);
			list = ExpressionParserUtility.compileColumnExpression(expression2);
			assertTrue(list.size() == 3);
			list = ExpressionParserUtility.compileColumnExpression(expression3);
			assertTrue(list.size() == 6);
			list = ExpressionParserUtility.compileColumnExpression(expression4);
			assertTrue(list.size() == 2);
			list = ExpressionParserUtility.compileColumnExpression(expression5);
			assertTrue(list.size() == 2);
			list = ExpressionParserUtility.compileColumnExpression(expression6);
			assertTrue(list.size() == 1);
			list = ExpressionParserUtility.compileColumnExpression(expression7);
			assertTrue(list.size() == 1);
		} catch (BirtException e) {
			fail("should not get there ");
		}
	}

}
