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

import org.eclipse.birt.report.engine.api.script.element.IListGroup;
import org.eclipse.birt.report.engine.api.script.eventhandler.IListGroupEventHandler;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.script.internal.element.ListGroup;
import org.eclipse.birt.report.model.api.ListGroupHandle;

public class ListGroupScriptExecutor extends ScriptExecutor
{

	public static void handleOnPrepare( ListGroupHandle groupHandle,
			ExecutionContext context )
	{
		try
		{
			IListGroup group = new ListGroup( groupHandle );
			if ( handleJS( group, groupHandle.getOnPrepare( ), context )
					.didRun( ) )
				return;
			IListGroupEventHandler eh = ( IListGroupEventHandler ) getInstance( groupHandle );
			if ( eh != null )
				eh.onPrepare( group, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

}
