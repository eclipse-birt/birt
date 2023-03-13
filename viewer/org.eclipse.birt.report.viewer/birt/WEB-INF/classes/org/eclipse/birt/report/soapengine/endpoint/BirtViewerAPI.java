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
 * BirtViewerAPI.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.endpoint;

public interface BirtViewerAPI extends javax.xml.rpc.Service {
	java.lang.String getBirtSoapPortAddress();

	org.eclipse.birt.report.soapengine.endpoint.BirtSoapPort getBirtSoapPort() throws javax.xml.rpc.ServiceException;

	org.eclipse.birt.report.soapengine.endpoint.BirtSoapPort getBirtSoapPort(java.net.URL portAddress)
			throws javax.xml.rpc.ServiceException;
}
