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
 * Handler for data set events
 */
public interface IBaseDataSetEventHandler {
	void handleBeforeOpen(IDataSetInstanceHandle dataSet) throws BirtException;

	void handleBeforeClose(IDataSetInstanceHandle dataSet) throws BirtException;

	void handleAfterOpen(IDataSetInstanceHandle dataSet) throws BirtException;

	void handleAfterClose(IDataSetInstanceHandle dataSet) throws BirtException;

	void handleOnFetch(IDataSetInstanceHandle dataSet, IDataRow row) throws BirtException;
}
