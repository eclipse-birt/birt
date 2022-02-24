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

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.core.exception.BirtException;

public interface IReportDocumentLockManager {

	/**
	 * try to lock a file, onece the user get the lock of the file, it should
	 * synchronize the locker to read/write the report document.
	 * 
	 * @param document
	 * @return the locker.
	 * @throws BirtException
	 */
	IReportDocumentLock lock(String document) throws BirtException;
}
