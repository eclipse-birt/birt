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

import java.text.DateFormat;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
 */
public class ScriptContextTest extends TestCase
{

	ScriptContext context;

	public void setUp( )
	{
		context = new ScriptContext( );
	}

	public void tearDown( )
	{
		context.exit( );
	}

	/**
	 * test if the enterScope & exitScope is correct.
	 */
	public void testScope( )
	{
		//register A in root
		context.registerBean( "A", new Integer( 10 ) );
		context.enterScope( );
		//register B in root
		context.registerBean( "B", new Integer( 20 ) );
		Object result = context.eval( "A + B" );
		assertEquals( ( (Number) result ).doubleValue( ), 30.0,
				Double.MIN_VALUE );
		context.exitScope( );
		//B is valid now
		boolean hasException = false;
		try
		{
			result = context.eval( "A + B" );
		}
		catch ( Exception ex )
		{
			hasException = true;
		}
		assertTrue( !hasException );
		//A is still valid
		result = context.eval( "A" );
		assertEquals( ( (Number) result ).doubleValue( ), 10.0,
				Double.MIN_VALUE );
	}
	
	/**
	 * Test if we can use NativeJavaObject as scope.
	 */
	public void testJavaScope()
	{
		StringBuffer buffer = new StringBuffer();
		Scriptable javaScope = (Scriptable)Context.javaToJS(buffer, context.getScope());
		//define a function in the root
		context.eval("function getText() { return 'TEXT'};");
		
		//enter java-based scope
		context.enterScope(javaScope);
		context.eval("append(getText());");
		context.eval("append('TEXT2');");
		context.exitScope();
		
		assertEquals("TEXTTEXT2", buffer.toString());
		Object result = context.eval("getText()");
		assertEquals("TEXT", result);
	}
	
	/**
	 * compile a script and running it in different scope 
	 * to see if it returns differnt values.
	 * 
	 * Expected:
	 * 
	 * the same code running in different scope reutrns different values.
	 */
	public void testCompiledScript()
	{
		context.enterScope();
		context.eval("function getText() { return 'A'}");
		assertEquals("A", context.eval("getText()"));
		context.exitScope();
		context.enterScope();
		context.eval("function getText() { return 'B'}");
		assertEquals("B", context.eval("getText()"));
		context.exitScope();
		boolean hasException = false;
		try
		{
			context.eval("getText()");
		}
		catch(Exception ex)
		{
			hasException = true;
		}
		assertTrue(hasException);
			
	}
	
	/**
	 * Test if the defineClass/definePackage is supported by script.
	 */
	public void testGlobal()
	{
		context.eval("importPackage(java.util)");
		context.eval("importClass(java.text.DateFormat)");
		Object list = context.eval("new ArrayList()");
		Object fmt = context.eval("DateFormat.getInstance()");
		assertTrue(list instanceof ArrayList);
		assertTrue(fmt instanceof DateFormat);
	}

	
	/**
	 * context shares the object in the root scope 
	 */
	public void testRootScope( )
	{
		Context context = Context.enter( );
		ScriptableObject root = context.initStandardObjects( );
		root.put( "share", root, "ABCDEFG" );
		Context.exit( );
		ScriptContext cx = new ScriptContext( root );
		Object result = cx.eval( "share + 'c'" );
		assertEquals( "ABCDEFGc", result.toString( ) );
		cx.exit( );
	}
	
	/**
	 * In javascript, the "this" always point to the 
	 * current scope.
	 */
	public void testThisObject()
	{
		context.registerBean("A", "ABCDE");
		
		Context cx = context.getContext();
		
		Scriptable scope = context.getScope();
		Scriptable obj = cx.newObject(scope);
		obj.put("a", obj, "VALUE");
		//enter a scope
		context.enterScope(obj);
		//this is the current scope
		Object result = context.eval("this");
		assertEquals(obj, result);
		//it can access the member in the scope
		result = context.eval("a");
		assertEquals("VALUE", result);

		//it can use this to access the member of scope
		result = context.eval("this.a");
		assertEquals("VALUE", result);
		
		//it can access the member of parent
		result = context.eval("A");
		assertEquals("ABCDE", result);
		
		//it can not use this to access the member of parent.
		result = context.eval("this.A");
		assertEquals(null, result);
		
		context.exit();
	}
}