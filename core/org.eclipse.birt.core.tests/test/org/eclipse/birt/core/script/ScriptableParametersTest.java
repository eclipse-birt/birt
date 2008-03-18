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

import junit.framework.TestCase;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptableParameters;
import org.mozilla.javascript.JavaScriptException;

public class ScriptableParametersTest extends TestCase
{

	ScriptContext context;
	Map params;
	
	public void setUp( )
	{
		context = new ScriptContext( );
		params = new HashMap( );
		addParameter( "string", "abc", "STRING VALUE" );
		context.registerBean( "params", new ScriptableParameters( params,
				context.getScope( ) ) );
	}

	private void addParameter( String name, Object value, String displayText )
	{
		params.put( name, new ParameterAttribute( value, displayText ) );
	}

	public void tearDown( )
	{
		context.exit( );
	}

	public void testAssign( )
	{
		// change to a exits parameters
		context.eval( "params['string'] = 'abcd'" );
		assertEquals( "abcd", context.eval( "params['string']" ) );
		context.eval( "params['string'].value = 'abcde'" );
		assertEquals( "abcde", context.eval( "params['string']" ) );
		context.eval( "params['string'].displayText = 'display'" );
		assertEquals( "display", context.eval( "params['string'].displayText" ) );

		// assign to a none exist parameter will create a new entry
		// automatcially
		context.eval( "params['new param'] = 'abc'" );
		assertEquals( "abc", context.eval( "params['new param']" ) );
		
		//test case for 163786
		context.eval( "params['date'] = new Date()" );
		Object date = context.eval( "params['date'].value");
		assertTrue(date instanceof java.util.Date);

		context.eval( "params['number'] = new Number(3)" );
		Object num = context.eval( "params['number'].value");
		assertTrue(num instanceof Double);

	}

	public void testReterive( )
	{
		// access the none exist paramter will return null directl
		try
		{
			context.eval( "params['none exsit'] == null" );
			fail( );
		}
		catch ( JavaScriptException e )
		{
			assertTrue( true );
		}

		// access the paramters from value
		assertEquals( "abc", context.eval( "params['string'].value" ) );
		assertEquals( "bbc", context
				.eval( "params['string'].value.replace('a', 'b')" ) );
		// access the paramters from the display text
		assertEquals( "STRING VALUE", context
				.eval( "params['string'].displayText" ) );

		// access the paramters from default value
		assertEquals( "abc", context.eval( "params['string']" ) );
		assertEquals( "bbc", context.eval( "var value = params['string'];"
				+ "value.replace('a', 'b')" ) );
	}

	public void testEval( )
	{
		addParameter( "jsDate", "", "" );
		context.eval( "params['jsDate']=new Date();" );
		assertTrue( context.eval("params['jsDate'].getFullYear()") instanceof Number );
		assertTrue( context.eval( "params['jsDate'].value.getFullYear()" ) instanceof Number );

		addParameter( "jsString", "", "" );
		context.eval( "params['jsString']='testString';" );
		assertEquals( new Integer( 10 ), context
				.eval( "params['jsString'].length" ) );
		assertEquals( new Integer( 10 ), context
				.eval( "params['jsString'].value.length" ) );

		addParameter( "javaDate", new Date(2008, 03, 05), "" );
		assertEquals( new Integer( 2008 ), context
				.eval( "params['javaDate'].getYear()" ) );
		assertEquals( new Integer( 2008 ), context
				.eval( "params['javaDate'].value.getYear()" ) );
	}
}
