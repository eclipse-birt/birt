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
/**
 * BirtSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.endpoint;

import java.rmi.RemoteException;

import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjects;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.processor.BaseProcessorFactory;
import org.eclipse.birt.report.soapengine.processor.IComponentProcessor;
import org.eclipse.birt.report.soapengine.processor.IProcessorFactory;

public class BirtSoapBindingImpl {

	public GetUpdatedObjectsResponse getUpdatedObjects(GetUpdatedObjects request) throws RemoteException {
		IProcessorFactory processorFactory = BaseProcessorFactory.getInstance();

		GetUpdatedObjectsResponse response = new GetUpdatedObjectsResponse();
		Operation[] ops = request.getOperation();

		IContext context = BirtContext.getInstance();
		if (context.getBean().getException() != null) {
			throw new BirtSoapException("BirtSoapBindingImpl.getUpdatedObjects",
					context.getBean().getException().getLocalizedMessage());
		}

		for (int i = 0; i < ops.length; i++) {
			Operation op = ops[i];
			IComponentProcessor processor = processorFactory.createProcessor(context.getBean().getCategory(),
					op.getTarget().getType());

			if (processor == null) {
				throw new BirtSoapException("BirtSoapBindingImpl.getUpdatedObjects",
						BirtResources.getMessage(ResourceConstants.SOAP_BINDING_EXCEPTION_NO_HANDLER_FOR_TARGET,
								new Object[] { op.getTarget() }));
			}

			processor.process(context, op, response);
		}

		return response;
	}
}
