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
import org.eclipse.birt.report.service.actionhandler.IActionHandler;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;

abstract public class AbstractBaseDocumentProcessor extends AbstractBaseComponentProcessor
		implements IDocumentProcessor {

	/**
	 * Default constructor.
	 */
	public AbstractBaseDocumentProcessor() {
		super();
	}

	/**
	 * Local execution.
	 *
	 * @param action
	 * @param context
	 * @param op
	 * @param response
	 */
	@Override
	protected void __executeAction(IActionHandler action, IContext context, Operation op,
			GetUpdatedObjectsResponse response) throws RemoteException {
		if (action != null && action.canExecute()) {
			action.execute();
		}
	}
}
