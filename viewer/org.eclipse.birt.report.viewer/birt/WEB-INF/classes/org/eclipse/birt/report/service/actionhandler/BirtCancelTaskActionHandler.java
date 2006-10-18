/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.actionhandler;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;

/**
 * This action handler is to handle cancel current task.
 * <p>
 * Task should be engine task including RunTask,RenderTask,RunAndRenderTask and
 * so on.
 * <p>
 * When viewer is processing an engine task, put it in current session using
 * unique id from request.So this action handler can find out current task
 * according to an unique id.
 */
public class BirtCancelTaskActionHandler extends AbstractBaseActionHandler
{

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtCancelTaskActionHandler( IContext context, Operation operation,
			GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	/**
	 * execute the action
	 */
	protected void __execute( ) throws Exception
	{
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean( );
		assert attrBean != null;

		// get task id
		Oprand[] op = this.operation.getOprand( );
		String taskid = null;
		if ( op != null )
		{
			for ( int i = 0; i < op.length; i++ )
			{
				String paramName = op[i].getName( );
				String paramValue = op[i].getValue( );

				if ( IBirtConstants.OPRAND_TASKID.equalsIgnoreCase( paramName ) )
				{
					taskid = paramValue;
					break;
				}
			}
		}

		if ( taskid == null )
			return;

		// get task map
		HttpSession session = context.getRequest( ).getSession( );
		if ( session == null )
			return;

		Map map = (Map) session.getAttribute( IBirtConstants.TASK_MAP );
		if ( map != null && map.containsKey( taskid ) )
		{
			// cancel task
			IEngineTask task = (IEngineTask) map.get( taskid );
			if ( task != null )
			{
				task.cancel( );
			}

			// remove task from task map
			synchronized ( map )
			{
				map.remove( taskid );
			}
		}

		handleUpdate( );
	}

	/**
	 * After done action,update response
	 * 
	 */
	protected void handleUpdate( )
	{
		// do nothing
	}

	/**
	 * Implement getReportService()
	 */
	protected IViewerReportService getReportService( )
	{
		return null;
	}
}