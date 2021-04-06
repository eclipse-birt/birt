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

package org.eclipse.birt.report.soapengine.processor;

import java.rmi.RemoteException;
import java.util.Hashtable;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.service.actionhandler.BirtCacheParameterActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtCancelTaskActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtChangeParameterActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtGetCascadeParameterActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtGetPageActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtGetPageAllActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtGetTOCActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtQueryExportActionHandler;

public class BirtDocumentProcessor extends AbstractBaseDocumentProcessor {
	/**
	 * Operator list definition.
	 */
	protected static String[] opList = new String[] { IBirtOperators.Operator_GetPage_Literal,
			IBirtOperators.Operator_GetToc_Literal, IBirtOperators.Operator_GetCascadeParameter_Literal,
			IBirtOperators.Operator_ChangeParameter_Literal, IBirtOperators.Operator_QueryExport_Literal,
			IBirtOperators.Operator_CacheParameter_Literal, IBirtOperators.Operator_CancelTask_Literal,
			IBirtOperators.Operator_GetPageAll_Literal };

	/**
	 * Operator mapping.
	 */
	protected static Hashtable operatorMap = new Hashtable();

	/**
	 * Default constructor.
	 */
	public BirtDocumentProcessor() {
		super();
	}

	/**
	 * Access the operator list.
	 * 
	 * @return String[]
	 */
	protected String[] getOperatorList() {
		return opList;
	}

	/**
	 * Access the operator map.
	 * 
	 * @return Hashtable
	 */
	protected Hashtable getOpMap() {
		return operatorMap;
	}

	/**
	 * Handle page navigation.
	 * 
	 * @param dsSession
	 * @param op
	 * @param response
	 */
	public void handleGetPage(IContext context, Operation op, GetUpdatedObjectsResponse response)
			throws RemoteException {
		executeAction(new BirtGetPageActionHandler(context, op, response), context, op, response);
	}

	/**
	 * Handle change parameter.
	 * 
	 * @param dsSession
	 * @param op
	 * @param response
	 */
	public void handleChangeParameter(IContext context, Operation op, GetUpdatedObjectsResponse response)
			throws RemoteException {
		executeAction(new BirtChangeParameterActionHandler(context, op, response), context, op, response);
	}

	/**
	 * Handle cache parameter.
	 * 
	 * @param dsSession
	 * @param op
	 * @param response
	 */
	public void handleCacheParameter(IContext context, Operation op, GetUpdatedObjectsResponse response)
			throws RemoteException {
		executeAction(new BirtCacheParameterActionHandler(context, op, response), context, op, response);
	}

	/**
	 * Handle getting cascade parameter selection list.
	 * 
	 * @param context
	 * @param op
	 * @param response
	 * @throws RemoteException
	 */
	public void handleGetCascadingParameter(IContext context, Operation op, GetUpdatedObjectsResponse response)
			throws RemoteException {
		executeAction(new BirtGetCascadeParameterActionHandler(context, op, response), context, op, response);
	}

	/**
	 * Handle retrieve toc nodes.
	 * 
	 * @param context
	 * @param op
	 * @param response
	 * @throws RemoteException
	 */
	public void handleGetToc(IContext context, Operation op, GetUpdatedObjectsResponse response)
			throws RemoteException {
		executeAction(new BirtGetTOCActionHandler(context, op, response), context, op, response);
	}

	/**
	 * Handle export data from report.
	 * 
	 * @param context
	 * @param op
	 * @param response
	 * @throws RemoteException
	 */
	public void handleQueryExport(IContext context, Operation op, GetUpdatedObjectsResponse response)
			throws RemoteException {
		executeAction(new BirtQueryExportActionHandler(context, op, response), context, op, response);
	}

	/**
	 * Handle cancel current task.
	 * 
	 * @param context
	 * @param op
	 * @param response
	 * @throws RemoteException
	 */
	public void handleCancelTask(IContext context, Operation op, GetUpdatedObjectsResponse response)
			throws RemoteException {
		executeAction(new BirtCancelTaskActionHandler(context, op, response), context, op, response);
	}

	/**
	 * Handle get page all.
	 * 
	 * @param context
	 * @param op
	 * @param response
	 * @throws RemoteException
	 */
	public void handleGetPageAll(IContext context, Operation op, GetUpdatedObjectsResponse response)
			throws RemoteException {
		executeAction(new BirtGetPageAllActionHandler(context, op, response), context, op, response);
	}
}
