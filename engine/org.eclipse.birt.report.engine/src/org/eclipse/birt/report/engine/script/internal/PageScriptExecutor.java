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

package org.eclipse.birt.report.engine.script.internal;

import java.util.Collection;

import org.eclipse.birt.report.engine.api.script.instance.IPageInstance;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.PageContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.script.internal.instance.PageInstance;

public class PageScriptExecutor extends ScriptExecutor
{

	public static void handleOnPageScript( ExecutionContext context,
			PageContent pageContent, Collection<IContent> contents,
			Expression pageScript )
	{
		try
		{
			IPageInstance pageInstance = new PageInstance( context,
					pageContent, contents );
			if ( handleJS( pageInstance, pageScript, context ).didRun( ) )
				return;
		}
		catch ( Exception e )
		{
			addException( context, e );
		}
	}
}
