/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.soapengine.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.actionhandler.IActionHandler;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.BirtUtility;

public abstract class AbstractBaseComponentProcessor implements IComponentProcessor {

	/**
	 * Abstract methods.
	 */
	abstract protected Hashtable getOpMap();

	abstract protected String[] getOperatorList();

	abstract protected void __executeAction(IActionHandler action, IContext context, Operation op,
			GetUpdatedObjectsResponse response) throws RemoteException;

	/**
	 * Default constructor.
	 */
	public AbstractBaseComponentProcessor() {
		Hashtable map = getOpMap();
		if (map.size() <= 0) {
			synchronized (map) {
				initOpMap(map, getOperatorList());
			}
		}
	}

	/**
	 * Init operation map. Operation method format: handleXXX( IContext, Operation,
	 * GetUpdatedObjectsResponse );
	 * 
	 * @param operatorMap
	 * @param operators
	 */
	protected void initOpMap(Hashtable operatorMap, String[] operators) {
		for (int i = 0; i < operators.length; i++) {
			String methodName = "handle" + operators[i]; //$NON-NLS-1$
			Class[] args = new Class[3];
			args[0] = IContext.class;
			args[1] = Operation.class;
			args[2] = GetUpdatedObjectsResponse.class;

			try {
				Method method = this.getClass().getMethod(methodName, args);
				operatorMap.put(operators[i].toUpperCase(), method);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assert false;
			}
		}
	}

	/**
	 * Processor entry point. Generic processing.
	 * 
	 * @param context
	 * @param op
	 * @param response
	 * @throws RemoteException
	 */
	public void process(IContext context, Operation op, GetUpdatedObjectsResponse response) throws RemoteException {
		assert context != null;
		String operator = op.getOperator().toUpperCase();
		if (operator == null) {
			// TODO: need s common method for this.
			AxisFault fault = new AxisFault();
			fault.setFaultCode(new QName(this.getClass().getName()));
			fault.setFaultString(
					BirtResources.getMessage(ResourceConstants.COMPONENT_PROCESSOR_EXCEPTION_MISSING_OPERATOR));
			throw fault;
		}

		Method method = (Method) getOpMap().get(operator);
		if (method != null) {
			try {
				method.invoke(this, new Object[] { context, op, response });
			} catch (InvocationTargetException e) {
				Exception target = (Exception) e.getTargetException();
				throw BirtUtility.makeAxisFault(target);
			} catch (Exception e) {
				// TODO: clear this out.
				AxisFault fault = new AxisFault();
				fault.setFaultCode(new QName("Clear out this.")); //$NON-NLS-1$
				fault.setFaultString(e.getLocalizedMessage());
				throw fault;
			}
		}
	}

	/**
	 * Execution template method.
	 * 
	 * @param action
	 * @param context
	 * @param op
	 * @param response
	 * @throws RemoteException
	 */
	protected void executeAction(IActionHandler action, IContext context, Operation op,
			GetUpdatedObjectsResponse response) throws RemoteException {
		__executeAction(action, context, op, response);
	}

	protected long getDesignId(String instanceId) {
		return InstanceID.parse(instanceId).getComponentID();
	}

}
