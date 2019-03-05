
package org.eclipse.birt.data.engine.expression;

import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 * test case for expression parser
 * @author Administrator
 *
 */
public class ExpressionCompilerUtilTest {

	String[] oldExpressions = new String[]{
			null,
			"   " + Messages.getString( "ExpressionUtilTest.old.0" ),
			Messages.getString( "ExpressionUtilTest.old.1" ),
			Messages.getString( "ExpressionUtilTest.old.2" ),
			Messages.getString( "ExpressionUtilTest.old.3" ),
			Messages.getString( "ExpressionUtilTest.old.4" ),
			Messages.getString( "ExpressionUtilTest.old.5" ),
			Messages.getString( "ExpressionUtilTest.old.6" ),
			Messages.getString( "ExpressionUtilTest.old.7" ),
			Messages.getString( "ExpressionUtilTest.old.8" ),
			Messages.getString( "ExpressionUtilTest.old.9" ),
			Messages.getString( "ExpressionUtilTest.old.10" ),
			Messages.getString( "ExpressionUtilTest.old.11" )
	};
	@Test
    public void testExpression1( )
	{
		String expression = oldExpressions[1];
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.size( ) == 1 );
		}
		catch ( BirtException e )
		{
			fail( "An exception occurs" );
		}
	}
	@Test
    public void testExpression2( )
	{
		String expression = oldExpressions[2];
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.size( ) == 1 );
		}
		catch ( BirtException e )
		{
			fail( "An exception occurs" );
		}
	}
	@Test
    public void testExpression3( )
	{
		String expression = oldExpressions[3];
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.size( ) == 2 );
		}
		catch ( BirtException e )
		{
			fail( "An exception occurs" );
		}
	}
	@Test
    public void testExpression4( )
	{
		String expression = oldExpressions[4];
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.isEmpty( ) );
		}
		catch ( BirtException e )
		{
		}
	}
	@Test
    public void testExpression5( )
	{
		String expression = oldExpressions[5];
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.isEmpty( ) );
		}
		catch ( BirtException e )
		{
		}
	}
	@Test
    public void testExpression6( )
	{
		String expression = oldExpressions[6];
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.size( ) == 1 );
		}
		catch ( BirtException e )
		{
			fail( "An exception occurs" );
		}
	}
	@Test
    public void testExpression7( )
	{
		String expression = oldExpressions[7];
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.isEmpty( ) );
		}
		catch ( BirtException e )
		{
		}
	}
	@Test
    public void testExpression8( )
	{
		String expression = oldExpressions[8];
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.size( ) == 1 );
		}
		catch ( BirtException e )
		{
			fail( "An exception occurs" );
		}
	}
	@Test
    public void testExpression9( )
	{
		String expression = oldExpressions[9];
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.size( ) == 1 );
		}
		catch ( BirtException e )
		{
			fail( "An exception occurs" );
		}
	}
	@Test
    public void testExpression10( )
	{
		String expression = oldExpressions[10];
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.size( ) == 2 );
		}
		catch ( BirtException e )
		{
			fail( "An exception occurs" );
		}
	}
	@Test
    public void testExpression11( )
	{
		String expression = oldExpressions[11];
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.size( ) == 1 );
		}
		catch ( BirtException e )
		{
			fail( "An exception occurs" );
		}
	}
	@Test
    public void testExpression12( )
	{
		String expression = oldExpressions[12];
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.size( ) == 2 );
		}
		catch ( BirtException e )
		{
			fail( "An exception occurs" );
		}
	}
	@Test
    public void testAggregationExpression13( )
	{
		String expression = "row[\"customer\"].replace(\"aa\",\"bb\")";
		String expression2 = "( row[\"customer\"]+ row.customer ).replace(\"aa\",\"bb\")";
		String expression3 = "( row[\"customer\"]+ row.customer ).replace(row.aaa.replace(\"aa\",\"bb\"), row.bbb );";
		try
		{
			List list = extractColumnExpression( new ScriptExpression( expression ) );
			assertTrue( list.size( ) == 1 );
			list = extractColumnExpression( new ScriptExpression( expression2 ) );
			assertTrue( list.size( ) == 1 );
			list = extractColumnExpression( new ScriptExpression( expression3 ) );
			assertTrue( list.size( ) == 3 );
		}
		catch ( BirtException e )
		{
			fail( "An exception occurs" );
		}
	}
	@Test
    public void testConditionalExpression( )
	{
		IConditionalExpression ce1 = new ConditionalExpression( "row[\"abc\"]",
				1,
				"row[\"abc1\"]" );
		IConditionalExpression ce2 = new ConditionalExpression( "row[\"abc\"]+row[\"abc\"]",
				1,
				"row[\"abc\"]" );
		IConditionalExpression ce3 = new ConditionalExpression( "row[\"abc\"] + row[\"abc3\"]",
				1,
				"row[\"abc2\"]" );

		try
		{
			List list = extractColumnExpression( ce1 );
			assertEquals( list.size( ), 2 );
			list = extractColumnExpression( ce2 );
			assertEquals( list.size( ), 1 );
			list = extractColumnExpression( ce3 );
			assertEquals( list.size( ), 3 );
		}
		catch ( DataException e )
		{
			fail( "An exception occurs" );
		}
	}
	
	/**
	 * test whether there is columnReference in aggregation expression
	 * 
	 */
	@Test
    public void testHasRowExprInAggregation( )
	{
		String expression1 = "row[\"customer\"].replace( Total.sum( row.aa ),\"bb\")";
		String expression2 = "Total.sum( row.aaa + row.bbb )+ ccc";
		String expression3 = "Total.count( abc )+ ccc";
		String expression4 = "row.aaa + Total.sum( Total.sum(row.aaa)+1)";
		String expression5 = "row.aaa + Total.sum( Total.sum(dataSetRow.aaa)+1)";
		String expression6 = " dataSetRow.aaa + Total.sum( row.abc + dataSetRow.abc )";
		String expression7 = " Total.sum( abc + dataSetRow.abc, row.aaa>0, 1 )";
		String expression8 = " row.aaa + row.bbb + \"Total\"";
		
		ConditionalExpression ce1 = new ConditionalExpression(expression3, 0, expression5, expression7);
		ConditionalExpression ce2 = new ConditionalExpression(expression1, 0, expression5, expression7);
		ConditionalExpression ce3 = new ConditionalExpression(expression3, 0, expression5, null);
		
		assertTrue( ExpressionCompilerUtil.hasAggregationInExpr( new ScriptExpression( expression1 ) ) );
		assertTrue( ExpressionCompilerUtil.hasAggregationInExpr( new ScriptExpression( expression2 ) ) );
		assertTrue( ExpressionCompilerUtil.hasAggregationInExpr( new ScriptExpression( expression3 ) ) );
		assertTrue( ExpressionCompilerUtil.hasAggregationInExpr( new ScriptExpression( expression4 ) ) );
		assertTrue( ExpressionCompilerUtil.hasAggregationInExpr( new ScriptExpression( expression5 ) ) );
		assertTrue( ExpressionCompilerUtil.hasAggregationInExpr( new ScriptExpression( expression6 ) ) );
		assertTrue( ExpressionCompilerUtil.hasAggregationInExpr( new ScriptExpression( expression7 ) ) );
		assertFalse( ExpressionCompilerUtil.hasAggregationInExpr( new ScriptExpression( expression8 ) ) );
		assertTrue( ExpressionCompilerUtil.hasAggregationInExpr( ce1 ) );
		assertTrue( ExpressionCompilerUtil.hasAggregationInExpr( ce2 ) );
		assertTrue( ExpressionCompilerUtil.hasAggregationInExpr( ce3 ) );
		

	}
	
	private static List extractColumnExpression(IBaseExpression expression) throws DataException
	{
		return ExpressionCompilerUtil.extractColumnExpression( expression, ExpressionUtil.ROW_INDICATOR );
	}
}
