/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
	@Override
	public DataRequestSession createSession(DataSessionContext context) throws BirtException {
		return new DataRequestSessionImpl(context);
	}

}
