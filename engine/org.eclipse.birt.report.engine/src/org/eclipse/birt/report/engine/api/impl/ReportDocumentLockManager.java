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

package org.eclipse.birt.report.engine.api.impl;

import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IReportDocumentLock;
import org.eclipse.birt.report.engine.api.IReportDocumentLockManager;

/**
 * The locker manager used by the system.
 *
 * The user should register the lock mangager to the report engine.
 *
 */
public class ReportDocumentLockManager implements IReportDocumentLockManager {

	static protected IReportDocumentLockManager instance = null;

	public static IReportDocumentLockManager getInstance() {
		if (instance != null) {
			return instance;
		}
		synchronized (ReportDocumentLockManager.class) {
			if (instance == null) {
				instance = new InternalLockManager();
			}
		}
		return instance;
	}

	private ReportDocumentLockManager() {
	}

	@Override
	public IReportDocumentLock lock(String document) throws BirtException {
		return null;
	}

	private static class InternalLock implements IReportDocumentLock {

		InternalLock(String document) {
		}

		@Override
		public void unlock() {
		}
	}

	private static class InternalLockManager implements IReportDocumentLockManager {

		private HashMap locks = new HashMap();

		InternalLockManager() {
		}

		@Override
		public IReportDocumentLock lock(String document) throws BirtException {
			synchronized (this) {
				IReportDocumentLock lock = (IReportDocumentLock) locks.get(document);
				if (lock == null) {
					lock = new InternalLock(document);
					// first time, we must accquire a lock
					locks.put(document, lock);
				}
				return lock;
			}
		}
	}

}
