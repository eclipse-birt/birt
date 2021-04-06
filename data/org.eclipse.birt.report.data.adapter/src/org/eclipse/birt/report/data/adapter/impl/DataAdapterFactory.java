/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IDataAdapterFactory;

/**
 * 
 */
public class DataAdapterFactory implements IDataAdapterFactory {

	/*
	 * @see
	 * org.eclipse.birt.report.data.adaptor.api.IDataAdaptorFactory#createSession(
	 * org.eclipse.birt.report.data.adaptor.api.DataSessionContext)
	 */
	public DataRequestSession createSession(DataSessionContext context) throws BirtException {
		return new DataRequestSessionImpl(context);
	}

}
