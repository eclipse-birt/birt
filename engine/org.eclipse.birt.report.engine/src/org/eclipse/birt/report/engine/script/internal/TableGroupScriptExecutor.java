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

import org.eclipse.birt.report.engine.api.script.element.ITableGroup;
import org.eclipse.birt.report.engine.api.script.eventhandler.ITableGroupEventHandler;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.script.internal.element.TableGroup;
import org.eclipse.birt.report.model.api.TableGroupHandle;

public class TableGroupScriptExecutor extends ScriptExecutor
{

	public static void handleOnPrepare( TableGroupHandle groupHandle,
			ExecutionContext context )
	{
		try
		{
			ITableGroup group = new TableGroup( groupHandle );
			if ( handleJS( group, groupHandle.getOnPrepare( ), context )
					.didRun( ) )
				return;
			ITableGroupEventHandler eh = ( ITableGroupEventHandler ) getInstance( groupHandle );
			if ( eh != null )
				eh.onPrepare( group, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

}
