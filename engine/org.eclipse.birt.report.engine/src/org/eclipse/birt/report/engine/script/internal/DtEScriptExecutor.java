/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.script.internal;

import java.util.logging.Level;

import org.eclipse.birt.data.engine.script.JSMethodRunner;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.mozilla.javascript.Scriptable;

public abstract class DtEScriptExecutor extends ScriptExecutor
{

	protected static final String DATA_SET = "DataSet";

	protected static final String DATA_SOURCE = "DataSource";

	protected static final String BEFORE_OPEN = "beforeOpen";

	protected static final String BEFORE_CLOSE = "beforeClose";

	protected static final String AFTER_OPEN = "afterOpen";

	protected static final String AFTER_CLOSE = "afterClose";

	protected ExecutionContext context;

	protected IReportContext reportContext;

	private JSMethodRunner runner;

	public DtEScriptExecutor( ExecutionContext context )
	{
		this.context = context;
		if ( context != null )
			this.reportContext = context.getReportContext( );
		else
			this.reportContext = null;
	}

	protected JSMethodRunner getRunner( Scriptable scope, String type,
			String name )
	{
		String scopeName = type + "[" + name + "]";
		runner = new JSMethodRunner( scope, scopeName );
		return runner;
	}

	protected JSScriptStatus handleJS( Scriptable scope, String type,
			String name, String method, String script )
	{
		if ( script == null || type == null || name == null || method == null )
			return JSScriptStatus.NO_RUN;
		if ( !( DATA_SET.equals( type ) || DATA_SOURCE.equals( type ) ) )
			return JSScriptStatus.NO_RUN;
		Object result = null;
		try
		{
			JSMethodRunner jsr = getRunner( scope, type, name );
			result = jsr.runScript( method, script );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
			if ( context != null )
				context.addException( new EngineException(
						MessageConstants.SCRIPT_EVALUATION_ERROR, script, e ) );
		}
		return new JSScriptStatus( true, result );
	}

	protected abstract JSScriptStatus handleJS( Scriptable scope, String name,
			String method, String script );
}
