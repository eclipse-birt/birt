/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.javascript;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.IScriptEngine;
import org.eclipse.birt.core.script.IScriptEngineFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

public class JavascriptEngineFactory implements IScriptEngineFactory
{
	public static String SCRIPT_JAVASCRIPT = "javascript";

	private static Logger logger = Logger
			.getLogger( JavascriptEngineFactory.class.getName( ) );

	/**
	 * root script scope. contains objects shared by the whole engine.
	 */
	protected ScriptableObject rootScope;

	private Context context;

	public JavascriptEngineFactory( )
	{
		context = Context.enter( );
		try
		{
			try
			{
				context.setSecurityController( ScriptUtil
						.createSecurityController( ) );
			}
			catch ( Throwable throwable )
			{
			}
			rootScope = context.initStandardObjects( );
			context
					.evaluateString(
							rootScope,
							"function registerGlobal( name, value) { _jsContext.registerGlobalBean(name, value); }",
							"<inline>", 0, null );
			context
					.evaluateString(
							rootScope,
							"function unregisterGlobal(name) { _jsContext.unregisterGlobalBean(name); }",
							"<inline>", 0, null );
		}
		catch ( Exception ex )
		{
			rootScope = null;
			logger.log( Level.INFO,
					"Error occurs while initialze script scope", ex );
		}
		finally
		{
			Context.exit( );
		}
	}

	public IScriptEngine createScriptEngine( ) throws BirtException
	{
		return new JavascriptEngine( this, rootScope );
	}

	public String getScriptLanguage( )
	{
		return SCRIPT_JAVASCRIPT;
	}
}
