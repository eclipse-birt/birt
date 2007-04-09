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
package org.eclipse.birt.core.script.bre;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import junit.framework.TestCase;


/**
 * 
 */
public class BirtCompTest extends TestCase
{
	private Context cx;
	private Scriptable scope;
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp( ) throws Exception
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

		ScriptableObject.putProperty( scope, "BirtComp", new BirtComp() );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	public void tearDown( )
	{
		Context.exit( );
	}
	
	/**
	 * 
	 *
	 */
	public void testAnyOf()
	{
		/*String script1 = "var array = new Array(4);array[0] = 100; array[1] = \"ABC\"; array[2] = \"1999-11-10\"; array[3] = null;";*/
		String script2 = "BirtComp.anyOf(100,100,\"ABC\", \"1999-11-10\",null);";
		String script3 = "BirtComp.anyOf(null,100,\"ABC\", \"1999-11-10\",null)";
		String script4 = "BirtComp.anyOf(\"ABC\",100,\"ABC\", \"1999-11-10\",null)";
		String script5 = "BirtComp.anyOf(new Date(99,10,10),100,\"ABC\", \"1999-11-10\",null)";
		String script6 = "BirtComp.anyOf(\"1999-11-10\",100,\"ABC\", \"1999-11-10\",null)";
		String script7 = "BirtComp.anyOf(20,100,\"ABC\", \"1999-11-10\",null)";
		

		assertTrue( ( (Boolean) cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ).booleanValue( ) );

		assertTrue( ( (Boolean) cx.evaluateString( scope,
				script3,
				"inline",
				1,
				null ) ).booleanValue( ) );

		assertTrue( ( (Boolean) cx.evaluateString( scope,
				script4,
				"inline",
				1,
				null ) ).booleanValue( ) );

		assertTrue( ( (Boolean) cx.evaluateString( scope,
				script5,
				"inline",
				1,
				null ) ).booleanValue( ) );

		assertTrue( ( (Boolean) cx.evaluateString( scope,
				script6, 
				"inline",
				1,
				null ) ).booleanValue( ) );

		assertFalse( ( (Boolean) cx.evaluateString( scope,
				script7,
				"inline",
				1,
				null ) ).booleanValue( ) );
	}
	
	public void testBetween()
	{
		String script1 = "BirtComp.between(\"1923-10-11\",new Date(10,11,11),new Date(33,11,11))";

		assertTrue( ( (Boolean) cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ).booleanValue( ) );
		
		String script2 = "BirtComp.between(100,101,102)";
		assertFalse( ( (Boolean) cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ).booleanValue( ) );
		
	}
	
	/**
	 * 
	 *
	 */
	public void testNotBetween()
	{
		String script1 = "BirtComp.notBetween(\"1923-10-11\",new Date(10,11,11),new Date(33,11,11))";

		assertFalse( ( (Boolean) cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ).booleanValue( ) );
		
		String script2 = "BirtComp.notBetween(100,101,102)";
		assertTrue( ( (Boolean) cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ).booleanValue( ) );
		
	}
	
	/**
	 * 
	 *
	 */
	public void testCompare()
	{
		String[] script = new String[]{
				//Equal to
				"BirtComp.equalTo(100,100);",
				"BirtComp.equalTo(null,null)",
				"BirtComp.equalTo(\"ABC\",\"ABC\")",
				"BirtComp.equalTo(new Date(99,10,10),\"1999-11-10\")",
				"BirtComp.equalTo(\"1999-11-10\",new Date(99,10,10))",
				"BirtComp.equalTo(20,100)",
				
				//NotEqual to
				"BirtComp.notEqual(100,100);",
				"BirtComp.notEqual(null,null)",
				"BirtComp.notEqual(\"ABC\",\"ABC\")",
				"BirtComp.notEqual(new Date(99,10,10),\"1999-11-10\")",
				"BirtComp.notEqual(\"1999-11-10\",new Date(99,10,10))",
				"BirtComp.notEqual(20,100)",
				
				//greater than
				"BirtComp.greaterThan(100,10);",
				"BirtComp.greaterThan(null,null)",
				"BirtComp.greaterThan(\"aBC\",\"ABC\")",
				"BirtComp.greaterThan(new Date(99,9,10),\"1999-11-10\")",
				"BirtComp.greaterThan(\"1999-11-10\",new Date(99,9,10))",
				"BirtComp.greaterThan(20,100)",
				
				//greater than or equal to
				"BirtComp.greaterOrEqual(100,10);",
				"BirtComp.greaterOrEqual(null,null)",
				"BirtComp.greaterOrEqual(\"aBC\",\"ABC\")",
				"BirtComp.greaterOrEqual(new Date(99,9,10),\"1999-11-10\")",
				"BirtComp.greaterOrEqual(\"1999-11-10\",new Date(99,9,10))",
				"BirtComp.greaterOrEqual(20,100)",
				
				//Less than
				"BirtComp.lessThan(10,100);",
				"BirtComp.lessThan(null,null)",
				"BirtComp.lessThan(\"aBC\",\"ABC\")",
				"BirtComp.lessThan(new Date(99,9,10),\"1999-11-10\")",
				"BirtComp.lessThan(\"1999-11-10\",new Date(99,9,10))",
				"BirtComp.lessThan(20,100)",
				
				//greater than or equal to
				"BirtComp.lessOrEqual(100,10);",
				"BirtComp.lessOrEqual(null,null)",
				"BirtComp.lessOrEqual(\"aBC\",\"ABC\")",
				"BirtComp.lessOrEqual(new Date(99,9,10),\"1999-11-10\")",
				"BirtComp.lessOrEqual(\"1999-11-10\",new Date(99,9,10))",
				"BirtComp.lessOrEqual(100,100)",
		};
		
		boolean[] result = new boolean[] { true, true, true, true, true, false,
										   false,false,false,false,false,true,
										   true, false, false, false, true, false,
										   true, true, false, false, true, false,
										   true, false, true, true, false, true,
										   false, true, true, true, false, true};
		
		for( int i = 0; i < script.length; i++ )
		{
			assertTrue( ( (Boolean) cx.evaluateString( scope,
				script[i],
				"inline",
				1,
				null ) ).booleanValue( ) == result[i]);
		}
	}
	
	/**
	 * 
	 *
	 */
	public void testMatch()
	{
		String[] script = new String[]{
				//Equal to
				"BirtComp.match(\"x 99:02:03\",\".*[0-9]*:[0-9]*:[0-9]*\");",
				"BirtComp.match(\"x 99::03\",\".*[0-9]*:[0-9]*:[0-9]*\");",
				"BirtComp.match(\"x 99:02:03\",\"x [0-9]*:[0-9]*:[0-9]*\");",
				"BirtComp.match(\"x 99:02:03\",\".*99*:[0-9]*:[0-9]*\");",
				"BirtComp.match(\"x 99:02:03\",\".*[0-9]*.[0-9]*:[0-9]*\");",
				"BirtComp.match(\"x 99:02:03\",\".*[0-9]*:[0-9]*:[0-9]*3.\");",
				
		};
		
		boolean[] result = new boolean[] { true, true, true, true, true, false};
		
		for( int i = 0; i < script.length; i++ )
		{
			assertTrue( ( (Boolean) cx.evaluateString( scope,
				script[i],
				"inline",
				1,
				null ) ).booleanValue( ) == result[i]);
		}
	}
	
	/**
	 * 
	 *
	 */
	public void testLike()
	{
		String[] script = new String[]{
				//Equal to
				"BirtComp.like(\"x 99:02:03\",\"%:0_:03\");",
				"BirtComp.like(\"x 99::003\",\"%9_::__3\");",
				"BirtComp.like(\"x 99:02:03\",\"%99:02_03\");",
				"BirtComp.like(\"x 99:02:03\",\"x 99%0_\");",
				"BirtComp.like(\"x 99:02:03\",\"_ 99%03\");",
				"BirtComp.like(\"x 99:02:03\",\"%:0_:__3\");",
				
		};
		
		boolean[] result = new boolean[] { true, true, true, true, true, false};
		
		for( int i = 0; i < script.length; i++ )
		{
			assertTrue( ( (Boolean) cx.evaluateString( scope,
				script[i],
				"inline",
				1,
				null ) ).booleanValue( ) == result[i]);
			System.out.println( i );
		}
	}
	
	/**
	 * 
	 *
	 */
	public void testNotLike()
	{
		String[] script = new String[]{
				//Equal to
				"BirtComp.notLike(\"x 99:02:03\",\"%:0_:03\");",
				"BirtComp.notLike(\"x 99::003\",\"%9_::__3\");",
				"BirtComp.notLike(\"x 99:02:03\",\"%99:02_03\");",
				"BirtComp.notLike(\"x 99:02:03\",\"x 99%0_\");",
				"BirtComp.notLike(\"x 99:02:03\",\"_ 99%03\");",
				"BirtComp.notLike(\"x 99:02:03\",\"%:0_:__3\");",
				
		};
		
		boolean[] result = new boolean[] { false, false, false, false, false, true};
		
		for( int i = 0; i < script.length; i++ )
		{
			assertTrue( ( (Boolean) cx.evaluateString( scope,
				script[i],
				"inline",
				1,
				null ) ).booleanValue( ) == result[i]);
			System.out.println( i );
		}
	}
}
