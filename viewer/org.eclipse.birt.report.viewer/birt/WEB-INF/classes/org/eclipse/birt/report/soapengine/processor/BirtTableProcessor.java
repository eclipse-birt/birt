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
import org.eclipse.birt.report.service.actionhandler.IActionHandler;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;

public class BirtTableProcessor extends AbstractBaseTableProcessor {
	/**
	 * Operator list definition.
	 */
	static final protected String[] opList = new String[] {};

	/**
	 * Operator mapping.
	 */
	static protected Hashtable operatorMap = new Hashtable();

	/**
	 * Constructor.
	 */
	public BirtTableProcessor() {
		super();
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
	 * Access the operator list.
	 * 
	 * @return String[]
	 */
	protected String[] getOperatorList() {
		return opList;
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
