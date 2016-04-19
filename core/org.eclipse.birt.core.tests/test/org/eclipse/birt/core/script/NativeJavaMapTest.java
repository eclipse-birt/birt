
package org.eclipse.birt.core.script;

import java.util.HashMap;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.*;

public class NativeJavaMapTest
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

		// ScriptableObject.defineClass(scope, NativeNamedList.class);

		registerBeans( );

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
	protected Object evaluate( String script )
	{
		try
		{
			hasException = false;
			return cx.evaluateString( scope, script, "inline", 1, null );
		}
		catch ( Throwable ex )
		{
			ex.printStackTrace( );
			hasException = true;
		}
		return null;
	}

	org.eclipse.birt.core.script.NativeJavaList list;
	Integer count = new Integer(0);
	
	protected void registerBeans( )
	{
		HashMap values = new HashMap( );
		values.put( "a", new Integer( 123 ) );
		values.put( "b", "STRING" );
		values.put( "c",new Long(599) );
		values.put( "d", "ABC" );

		NativeJavaMap map = new NativeJavaMap(scope, values, NativeJavaMap.class);
		scope.put( "params", scope, map );
	}
	@Test
    public void testIn( )
	{
		String script = "value = \"\"; for (var a in params) { value = value.concat(a) };";
		Object res = evaluate( script );
		assertTrue( !hasException );
		assertEquals( 4, res.toString().length() );
		
		script = "value = \"\"; for (var a in params) { value = value.concat( params[a] ); }";
		res = evaluate( script );
		assertTrue( !hasException );
		assertEquals( 15, res.toString().length());
	}
	@Test
    public void testLength( )
	{
		String script = "params.length";
		Object value = evaluate( script );
		assertTrue( !hasException );
		assertEquals( 4, ( (Number) value ).intValue( ) );
	}
	@Test
    public void testNameAccess( )
	{
		String script = "params['a'] + params.b";
		Object value = evaluate( script );
		assertTrue( !hasException );
		assertEquals( "123STRING", value.toString( ) );
	}
}
