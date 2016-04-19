/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * Created on Nov 12, 2004 NativeFinanceTest.
 * 
 * Unit test for class JsFinance. To each method in class, write JavaScript to
 * call every method of Class Finance, then test whether these methods can be
 * successfully called and return a correct result
 * 
 * 
 *         <p>
 * @docRoot <p>
 *          This unit test including the following test case
 *          <p>
 *          <ul>
 *          <li>testDdb()</li>
 *          <li>testSln()</li>
 *          <li>testSyd()</li>
 *          <li>testFv()</li>
 *          <li>testPmt()</li>
 *          <li>testIpmt()</li>
 *          <li>testPpmt()</li>
 *          <li>testNper()</li>
 *          <li>testPv()</li>
 *          <li>testRate()</li>
 *          <li>testPercent()</li>
 *          <li>testNpv()</li>
 *          <li>testIrr()</li>
 *          <li>testMirr()</li>
 */
public class NativeFinanceTest
{

	/**
	 * Create a Context instance
	 */
	Context cx;
	/**
	 * Create a Scriptable instance
	 */
	Scriptable scope;

	/**
	 * Record whether there exists an error
	 */
	boolean hasException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
    public void setUp() throws Exception
	{
		/*
		 * Creates and enters a Context. The Context stores information about
		 * the execution environment of a script.
		 */

		cx = Context.enter( );
		/*
		 * Initialize the standard objects (Object, Function, etc.) This must be
		 * done before scripts can be executed. Returns a scope object that we
		 * use in later calls.
		 */
		scope = cx.initStandardObjects( );

		new CoreJavaScriptInitializer().initialize( cx, scope );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
    public void tearDown()
	{
		Context.exit( );
	}

	/**
	 * Evaluate a JavaScript source string.
	 * 
	 * @param script
	 * @return the result
	 */
	protected double eval( String script )
	{
		try
		{
			hasException = false;
			Object value = cx.evaluateString( scope, script, "inline", 1, null );
			return ( (Double) value ).doubleValue( );
		}
		catch ( Throwable ex )
		{
			hasException = true;
			return Double.NaN;
		}
	}
	@Test
    public void testDdb( )
	{
		//ddb(1400, 200, 10, 1) == 280
		double value = eval( "Finance.ddb(1400, '200', '10', '1')" );
		assertEquals( 280.0, value, Double.MIN_VALUE );
		value = eval( "Finance.ddb( 1400, 200, 10, 1 )" );
		assertEquals( 280.0, value, Double.MIN_VALUE );
		/*
		 * Failure Test. All parameters of this
		 * function,cost,salvage,life,period should be positive. If we provide a
		 * negative parameter, application will report IllegalArgumentException
		 */
		eval( "Finance.ddb( 1400, 200, -10, 1 )" );
		assertTrue(hasException);
	}
	@Test
    public void testSln( )
	{
		//sln(1400, 200, 10) == 120
		double value = eval( "Finance.sln( 1400, 200, 10 )" );
		assertEquals( 120, value, Double.MIN_VALUE );

		value = eval( "Finance.sln( 1400, 200, -10 )" );
		assertEquals(-120, value, Double.MIN_VALUE);
		
	}
	@Test
    public void testSyd( )
	{
		double value = eval( "Finance.syd( 1400, 200, 2, 1 )" );
		assertEquals( 800, value, Double.MIN_VALUE );
		
		value = eval( "Finance.syd( -1400, 200, 3, 1 )" );
		assertEquals( -800, value, Double.MIN_VALUE );

	}
	@Test
    public void testFv( )
	{

		String script1 = "Finance.fv( 0.057 / 365, 18 * 365, 0, -10000, 1 )";
		double value1 = eval( script1 );
		assertEquals( 27896.60, value1, 0.01 );
		String script2 = "Finance.fv( 0.057 / 12, 18 * 12, -55, -10000, 1 )";
		double value2 = eval( script2 );
		assertEquals( 48575.82, value2, 0.01 );
		assertFalse( hasException );
		/*
		 * All parameters of this function should be positive. If we provide a
		 * negative parameter, application will report IllegalArgumentException.
		 * And if we input the value of parameter due is neither 1 or 0,
		 * application will also report error.
		 */
		String script3 = "Finance.fv( 0.057 / 12, 18 * 12, -55, -10000, 3 )";
		eval( script3 );
	}
	@Test
    public void testPmt( )
	{

		String script1 = " Finance.pmt( 0.115 / 12, 36, -20000, 0, 1 )";
		double value1 = eval( script1 );
		assertEquals( 653.26, value1, 0.01 );
		assertFalse( hasException );
		/*
		 * If we input the value of parameter due is neither 1 or 0, application
		 * will also report error.
		 */
		String script3 = "Finance.pmt( 0.115 / 12, 36, -20000, 0, -1 )";
		eval( script3 );
	}
	@Test
    public void testIpmt( )
	{

		String script1 = "Finance.ipmt( 0.115 / 12, 5, 36, -20000, 0, 1 )";
		double value1 = eval( script1 );
		assertEquals( 171.82, value1, 0.01 );
		assertFalse( hasException );

		/*
		 * If any parameter meet the following condition:rate < 0 || nPer < 0 ||
		 * per > nPer || ( due != 1 && due != 0 ), application will report error
		 */
		String script2 = "Finance.ipmt( 0.115 / 12, 37, 36, -20000, 0, 1 )";
		eval( script2 );
		String script3 = "Finance.ipmt( 0.115 / 12, 3, 36, -20000, 0, 8 )";
		eval( script3 );
	}
	@Test
    public void testPpmt( )
	{

		String script1 = " Finance.ppmt( 0.115 / 12, 5, 36, -20000, 0, 1 )";
		double value1 = eval( script1 );
		assertEquals( 481.43, value1, 0.01 );
		assertFalse( hasException );

		/*
		 * If any parameter meet the following condition:rate < 0 || nPer < 0 ||
		 * per > nPer || ( due != 1 && due != 0 ), application will report error
		 */
		String script2 = "Finance.ppmt( 0.115 / 12, 66, 36, -20000, 0, 1 )";
		eval( script2 );
		String script3 = "Finance.ppmt( 0.115 / 12, 5, 36, -20000, 0, 7 )";
		eval( script3 );
	}
	@Test
    public void testNPer( )
	{
		double value = eval( "Finance.nper( 0.01, -2000, 20000, 0, 1 )" );
		assertEquals( 10.478, value, 0.001 );

		value = eval( "Finance.nper( 0.01, -2000, 20000, 0, 0 )" );
		assertEquals( 10.588, value, 0.001 );
	}
	@Test
    public void testPv( )
	{
		String script1 = "Finance.pv( 0.105 / 12, 3 * 12, -325, 11000, 1 )";
		double value1 = eval( script1 );
		assertEquals( 2048.06, value1, 0.01 );
		/*
		 * If any parameter meet the following condition:(( nPer < 0 || ( due !=
		 * 1 && due != 0 ) ), application will report error
		 */
		String script3 = "Finance.nPer( 0.115 / 12, -4, 20000, 0, 1 )";
		eval( script3 );
	}
	@Test
    public void testRate( )
	{
		String script1 = "Finance.rate( 3 * 12, -653.26, 20000, 0, 1, 0.1 ) * 12";
		double value1 = eval( script1 );
		assertEquals( 0.115, value1, 0.01 );
		assertFalse( hasException );
		/*
		 * If any parameter meet the following condition:( nPer <= 0 || ( due !=
		 * 1 && due != 0 ) ), application will report error
		 */
		String script2 = "Finance.rate( -3, -653.26, 20000, 0, 1, 0.1 )";
		eval( script2 );

	}
	@Test
    public void testPercent( )
	{
		String script1 = "Finance.percent( 20, 50 )";
		double value1 = eval( script1 );
		assertEquals( 250, value1, Double.MIN_VALUE );
		String script2 = "Finance.percent( 50, 0 )";
		double value2 = eval( script2 );
		assertEquals( 0, value2, Double.MIN_VALUE );
		String script3 = "Finance.percent( '20', 50 )";
		double value3 = eval( script3 );
		assertEquals( 250, value3, Double.MIN_VALUE );
		
		//String script7 = " var a[2]=new String(2);a[0]=new
		// String('5');Finance.percent( a, 50 )";
		//double value7 = eval( script7 );
		//assertEquals( 1000, value7, Double.MIN_VALUE );
	}
	@Test
    public void testNpv( )
	{
		String script1 = "var array=new Array(4);array[0]=-10000;array[1]=3000;array[2]=4200;array[3]=6800;"
				+ "Finance.npv( 0.1, array )";
		double value1 = eval( script1 );
		assertEquals( 1188.44, value1, 0.01 );
	}
	@Test
    public void testIrr( )
	{
		//use double array to test this case
		double a[] = new double[]{-70000, 12000, 15000};
		Object jsNumber=Context.javaToJS(a,scope);
		ScriptableObject.putProperty(scope,"array",jsNumber);
		String script4="Finance.irr( array, -0.6 )";
		double value4 = eval( script4 );
		assertEquals( -0.44, value4, 0.01 );
		
		String script1 = "var array=new Array(6);array[0]=-70000;array[1]=12000;"
				+ "array[2]=15000;array[3]=18000;array[4]=21000;array[5]=26000;"
				+ "Finance.irr( array, 0.01)";
		double value1 = eval( script1 );
		assertEquals( 0.0866, value1, 0.0001 );
		String script2 = "var array=new Array(5);array[0]=-70000;array[1]=12000;array[2]=15000;array[3]=18000;array[4]=21000;Finance.irr(array, -0.1 )";
		double value2 = eval( script2 );
		assertEquals( -0.021244848273899997, value2, Double.MIN_VALUE );
		String script3 = "var array=new Array(3);array[0]=-70000;array[1]=12000;array[2]=15000;Finance.irr(array, -0.6 )";
		double value3 = eval( script3 );
		assertEquals( -0.44, value3, 0.01 );
		assertFalse( hasException );

	}
	@Test
    public void testMirr( )
	{
		//If the value of the guess is far from the correct answer, application
		// can't get the correct result and will report error
		String script1 = "var array=new Array(6);array[0]=-120000;array[1]=39000;"
				+ "array[2]=30000;array[3]=21000;array[4]=37000;array[5]=46000;"
				+ "Finance.mirr( array, 0.1, 0.12 )";
		double value1 = eval( script1 );
		assertEquals( 0.126, value1, 0.001);
	}

}
