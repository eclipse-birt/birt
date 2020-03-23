
package org.eclipse.birt.report.model.core;

/**
 * Represents whether the current thread is running a java script.
 * 
 * Restricting access to encrypted password from java script event handlers needs the
 * context from where the encrypted password is accessed. This class provides the required
 * context for differentiating encrypted password access from java script event handlers.
 */

public class JavaScriptExecutionStatus
{

	private static final ThreadLocal<Boolean> CURRENT = new ThreadLocal<Boolean>( ) {

		protected Boolean initialValue( )
		{
			return false;
		}
	};

	public static void setExeucting( boolean executionOnGoing )
	{
		CURRENT.set( executionOnGoing );
	}

	public static boolean isExecuting( )
	{
		return CURRENT.get( );
	}

	public static void remove( )
	{
		CURRENT.remove( );
	}

}
