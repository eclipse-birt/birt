/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.soapengine.processor;

import java.rmi.RemoteException;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;

public interface IDocumentProcessor {

	/**
	 * Handle page navigation.
	 *
	 * @param dsSession
	 * @param op
	 * @param response
	 */
	void handleGetPage(IContext context, Operation op, GetUpdatedObjectsResponse response) throws RemoteException;

	/**
	 * Handle change parameter.
	 *
	 * @param dsSession
	 * @param op
	 * @param response
	 */
	void handleChangeParameter(IContext context, Operation op, GetUpdatedObjectsResponse response)
			throws RemoteException;

	/**
	 * Handle cache parameter.
	 *
	 * @param dsSession
	 * @param op
	 * @param response
	 */
	void handleCacheParameter(IContext context, Operation op, GetUpdatedObjectsResponse response)
			throws RemoteException;

	/**
	 * Handle getting cascade parameter selection list.
	 *
	 * @param context
	 * @param op
	 * @param response
	 * @throws RemoteException
	 */
	void handleGetCascadingParameter(IContext context, Operation op, GetUpdatedObjectsResponse response)
			throws RemoteException;

	/**
	 * Handle retrieve toc nodes.
	 *
	 * @param context
	 * @param op
	 * @param response
	 * @throws RemoteException
	 */
	void handleGetToc(IContext context, Operation op, GetUpdatedObjectsResponse response) throws RemoteException;

	/**
	 * Do export data from report.
	 *
	 * @param context
	 * @param op
	 * @param response
	 * @throws RemoteException
	 */
	void handleQueryExport(IContext context, Operation op, GetUpdatedObjectsResponse response) throws RemoteException;

	/**
	 * Handle cancel task.
	 *
	 * @param dsSession
	 * @param op
	 * @param response
	 */
	void handleCancelTask(IContext context, Operation op, GetUpdatedObjectsResponse response) throws RemoteException;

	/**
	 * Handle get page all.
	 *
	 * @param dsSession
	 * @param op
	 * @param response
	 */
	void handleGetPageAll(IContext context, Operation op, GetUpdatedObjectsResponse response) throws RemoteException;
}
