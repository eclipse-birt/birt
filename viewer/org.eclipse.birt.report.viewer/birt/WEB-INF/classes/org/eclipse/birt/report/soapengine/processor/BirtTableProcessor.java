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
import java.util.Hashtable;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.actionhandler.IActionHandler;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;

public class BirtTableProcessor extends AbstractBaseTableProcessor {
	/**
	 * Operator list definition.
	 */
	static final protected String[] opList = {};

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
	@Override
	protected Hashtable getOpMap() {
		return operatorMap;
	}

	/**
	 * Access the operator list.
	 *
	 * @return String[]
	 */
	@Override
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
	@Override
	protected void __executeAction(IActionHandler action, IContext context, Operation op,
			GetUpdatedObjectsResponse response) throws RemoteException {
		if (action != null && action.canExecute()) {
			action.execute();
		}
	}
}
