/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.script;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Handler for Data Source events.
 */
public interface IBaseDataSourceEventHandler {
	public void handleBeforeOpen(IDataSourceInstanceHandle dataSource) throws BirtException;

	public void handleBeforeClose(IDataSourceInstanceHandle dataSource) throws BirtException;

	public void handleAfterOpen(IDataSourceInstanceHandle dataSource) throws BirtException;

	public void handleAfterClose(IDataSourceInstanceHandle dataSource) throws BirtException;
}
