
package org.eclipse.birt.core.script;

import java.util.HashMap;

import junit.framework.TestCase;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class NativeJavaMapTest extends TestCase
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

		// ScriptableObject.defineClass(scope, NativeNamedList.class);

		registerBeans( );

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

	StringBuffer buffer = new StringBuffer( );

	protected void registerBeans( )
	{
		HashMap values = new HashMap( );
		values.put( "a", new Integer( 123 ) );
		values.put( "b", "STRING" );
		values.put( "c",new Long(599) );
		values.put( "d", "ABC" );

		NativeJavaMap map = new NativeJavaMap(scope, values, NativeJavaMap.class);
		scope.put( "params", scope, map );
		scope.put( "buffer", scope, buffer );
	}

	public void testIn( )
	{
		String script = "for (var a in params) { buffer.append(a); }";
		buffer.setLength( 0 );
		evaluate( script );
		assertTrue( !hasException );
		assertEquals( 4, buffer.length( ) );
		
		script = "for (var a in params) { buffer.append( params[a]); }";
		buffer.setLength( 0 );
		evaluate( script );
		assertTrue( !hasException );
		assertEquals( 19, buffer.length( ));
		
	}

	public void testLength( )
	{
		String script = "params.length";
		Object value = evaluate( script );
		assertTrue( !hasException );
		assertEquals( 4, ( (Number) value ).intValue( ) );
	}

	public void testNameAccess( )
	{
		String script = "params['a'] + params.b";
		Object value = evaluate( script );
		assertTrue( !hasException );
		assertEquals( "123STRING", value.toString( ) );
	}
}
