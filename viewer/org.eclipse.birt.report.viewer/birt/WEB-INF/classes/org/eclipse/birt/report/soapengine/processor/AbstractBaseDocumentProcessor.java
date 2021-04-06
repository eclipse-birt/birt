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

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.service.actionhandler.IActionHandler;

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
	protected void __executeAction(IActionHandler action, IContext context, Operation op,
			GetUpdatedObjectsResponse response) throws RemoteException {
		if (action != null && action.canExecute()) {
			action.execute();
		}
	}
}
