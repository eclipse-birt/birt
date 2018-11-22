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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class ScriptableParametersTest extends TestCase
{

	ScriptContext context;
	Map params;
	@Before
    public void setUp()
	{
		context = new ScriptContext( );
		params = new HashMap( );
		addParameter( "string", "abc", "STRING VALUE" );
		context.setAttribute( "params", params );
	}

	private void addParameter( String name, Object value, String displayText )
	{
		params.put( name, new ParameterAttribute( value, displayText ) );
	}
	@After
    public void tearDown()
	{
		context.close( );
	}
	@Test
    public void testAssign( ) throws BirtException
	{
		// change to a exits parameters
		eval( "params['string'] = 'abcd'" );
		assertEquals( "abcd", eval( "params['string']" ) );
		eval( "params['string'].value = 'abcde'" );
		assertEquals( "abcde", eval( "params['string']" ) );
		eval( "params['string'].displayText = 'display'" );
		assertEquals( "display", eval( "params['string'].displayText" ) );

		// assign to a none exist parameter will create a new entry
		// automatcially
		eval( "params['new param'] = 'abc'" );
		assertEquals( "abc", eval( "params['new param']" ) );
		
		//test case for 163786
		eval( "params['date'] = new Date()" );
		Object date = eval( "params['date'].value");
		assertTrue(date instanceof java.util.Date);

		eval( "params['number'] = new Number(3)" );
		Object num = eval( "params['number'].value");
		assertTrue(num instanceof Double);

	}
	@Test
    public void testReterive( ) throws BirtException
	{
		// access the none exist paramter will return null directl
		try
		{
			eval( "params['none exsit'] == null" );
			fail( );
		}
		catch ( BirtException e )
		{
			assertTrue( true );
		}

		// access the paramters from value
		assertEquals( "abc", eval( "params['string'].value" ) );
		assertEquals( "bbc", eval( "params['string'].value.replace('a', 'b')" ) );
		// access the paramters from the display text
		assertEquals( "STRING VALUE", eval( "params['string'].displayText" ) );

		// access the paramters from default value
		assertEquals( "abc", eval( "params['string']" ) );
		assertEquals( "bbc", eval( "var value = params['string'];"
				+ "value.replace('a', 'b')" ) );
	}
	@Test
    public void testEval( ) throws BirtException
	{
		addParameter( "jsDate", "", "" );
		eval( "params['jsDate']=new Date();" );
		assertTrue( eval("params['jsDate'].getFullYear()") instanceof Number );
		assertTrue( eval( "params['jsDate'].value.getFullYear()" ) instanceof Number );

		addParameter( "jsString", "", "" );
		eval( "params['jsString']='testString';" );
		assertEquals( new Integer( 10 ), eval( "params['jsString'].length" ) );
		assertEquals( new Integer( 10 ),
				eval( "params['jsString'].value.length" ) );

		addParameter( "javaDate", new Date( 2008, 03, 05 ), "" );
		assertEquals( new Integer( 2008 ),
				eval( "params['javaDate'].getYear()" ) );
		assertEquals( new Integer( 2008 ),
				eval( "params['javaDate'].value.getYear()" ) );
	}

	private Object eval( String javascript ) throws BirtException
	{
		ICompiledScript compiledScript = context.compile( "javascript",
				"<inline>", 1, javascript );
		return context.evaluate( compiledScript );
	}
}
