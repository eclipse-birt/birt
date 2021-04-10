/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.script;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Handler for data set events
 */
public interface IBaseDataSetEventHandler {
	public void handleBeforeOpen(IDataSetInstanceHandle dataSet) throws BirtException;

	public void handleBeforeClose(IDataSetInstanceHandle dataSet) throws BirtException;

	public void handleAfterOpen(IDataSetInstanceHandle dataSet) throws BirtException;

	public void handleAfterClose(IDataSetInstanceHandle dataSet) throws BirtException;

	public void handleOnFetch(IDataSetInstanceHandle dataSet, IDataRow row) throws BirtException;
}
