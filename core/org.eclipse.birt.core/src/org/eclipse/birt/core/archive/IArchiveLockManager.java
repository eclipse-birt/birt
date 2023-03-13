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
package org.eclipse.birt.core.archive;

import java.io.IOException;

/**
 * lock used by the report engine to synchronize the document archives. the call
 * sequence of such a locker should be:
 *
 * <pre>
 * Object lock = manager.lock("fileName");
 * synchronized(lock)
 * {
 * 	 ... process ...
 * }
 * manager.unlock(lock).
 * </pre>
 */
public interface IArchiveLockManager {
	/**
	 * lock the object named by "name"
	 *
	 * @param name object name, the file name for file object.
	 * @return a locker used to lock the object.
	 */
	Object lock(String name) throws IOException;

	/**
	 * unlock the object locked by "lock".
	 *
	 * @param lock the lock object return from lock().
	 */
	void unlock(Object lock);
}
