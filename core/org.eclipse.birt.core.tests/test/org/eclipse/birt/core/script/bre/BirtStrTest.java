
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

public class BirtStrTest extends TestCase
{
	String str = " I am a test    string";
	
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

		ScriptableObject.putProperty( scope, "BirtStr", new BirtStr() );

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
	
	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_left(String, int)'
	 */
	public void testLeftStringInt( )
	{
		String script1 = "BirtStr.left(\"" + str + "\",5)";
		
		assertEquals( ( (String) cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ) , " I am" );	
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_right(String, int)'
	 */
	public void testRightStringInt( )
	{
		try
		{
			String script1 = "BirtStr.right(\"" + str + "\",0)";
			String script2 = "BirtStr.right(\"" + str + "\",5)";
			String script3 = "BirtStr.right(\"" + str + "\",50)";
			String script4 = "BirtStr.right(" + null + ",1)";
			String script5 = "BirtStr.right(\"" + str + "\",-2)";
			assertEquals( ( (String) cx.evaluateString( scope,
					script1,
					"inline",
					1,
					null ) ) , "" );
			assertEquals( ( (String) cx.evaluateString( scope,
					script2,
					"inline",
					1,
					null ) ) , "tring" );
			assertEquals( ( (String) cx.evaluateString( scope,
					script3,
					"inline",
					1,
					null ) ) , str );
			assertEquals( ( (String) cx.evaluateString( scope,
					script4,
					"inline",
					1,
					null ) ) , null );
			cx.evaluateString( scope,
					script5,
					"inline",
					1,
					null );
		
			fail( "it should be invalid" );
		}
		catch ( Exception e )
		{
			assertTrue( e instanceof IllegalArgumentException );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_toUpper(String)'
	 */
	public void testToUpper( )
	{
		String script1 = "BirtStr.toUpper(\"" + str + "\")";
		String script2 = "BirtStr.toUpper(" + null + ")";
		assertEquals( ( (String) cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ) , " I AM A TEST    STRING" );
		
		assertEquals( ( (String) cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ) , null);
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_toLower(String)'
	 */
	public void testToLower( )
	{
		String script1 = "BirtStr.toLower(\"" + str + "\")";
		String script2 = "BirtStr.toLower(" + null + ")";
		assertEquals( ( (String) cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ) , " i am a test    string" );
		
		assertEquals( ( (String) cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ) , null);
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_trim(String)'
	 */
	public void testTrim( )
	{
		String script1 = "BirtStr.trim(\"" + str + "\")";
		String script2 = "BirtStr.trim(" + null + ")";
		assertEquals( ( (String) cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ) , "I am a test string" );
		
		assertEquals( ( (String) cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ) , null);
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_trimLeft(String)'
	 */
	public void testTrimLeft( )
	{
		String script1 = "BirtStr.trimLeft(\"" + str + "\")";
		String script2 = "BirtStr.trimLeft(" + null + ")";
		assertEquals( ( (String) cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ) , "I am a test    string" );
		
		assertEquals( ( (String) cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ) , null);
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_trimRight(String)'
	 */
	public void testTrimRight( )
	{
		String script1 = "BirtStr.trimRight(\"" + str + "\")";
		String script2 = "BirtStr.trimRight(" + null + ")";
		assertEquals( ( (String) cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ) , " I am a test    string" );
		
		assertEquals( ( (String) cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ) , null);
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_indexOf(String, String, int)'
	 */
	public void testIndexOfStringStringInt( )
	{
		String script1 = "BirtStr.indexOf(\"a\",\"" + str + "\",1)";
		String script2 = "BirtStr.indexOf(\"a\",\"" + str + "\",4)";
		assertEquals( ( cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ) , new Integer(3) );
		
		assertEquals( ( cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ) , new Integer(6));
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_search(String,
	 * String, int)'
	 */
	public void testSearchStringStringInt( )
	{
		assertEquals( ( (Integer) cx.evaluateString( scope,
				"BirtStr.search(\"a test\",\" I am a test    string\",1)",
				"inline",
				1,
				null ) ) , new Integer(6) );
		
		assertEquals( ( (Integer) cx.evaluateString( scope,
				"BirtStr.search(\"a*t\",\" I am a test    string\",5)",
				"inline",
				1,
				null ) ) , new Integer(6) );
		assertEquals( ( (Integer) cx.evaluateString( scope,
				"BirtStr.search(\"a?t\",\" I am a test    string\",1)",
				"inline",
				1,
				null ) ) , new Integer(6) );
		assertEquals( ( (Integer) cx.evaluateString( scope,
				"BirtStr.search(\"a*t\",\" I am a test    string\",10)",
				"inline",
				1,
				null ) ) , new Integer(-1) );
		assertEquals( ( (Integer) cx.evaluateString( scope,
				"BirtStr.search(\" I*a*t\",\" I am a test    string\",1)",
				"inline",
				1,
				null ) ) , new Integer(0) );
	}

	public void testSearchStringString( )
	{
		assertEquals( ( cx.evaluateString( scope,
				"BirtStr.search(\"a?t\",\" I am a test    string\")",
				"inline",
				1,
				null ) ) , new Integer(6) );
		
		assertEquals( ( cx.evaluateString( scope,
				"BirtStr.search(\"a*t\",\" I am a test    string\")",
				"inline",
				1,
				null ) ) , new Integer(3) );
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_charLength(String)'
	 */
	public void testCharLength( )
	{

	}
}
