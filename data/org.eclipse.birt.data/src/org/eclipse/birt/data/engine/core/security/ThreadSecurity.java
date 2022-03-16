/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.data.engine.core.security;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 *
 *
 */
public class ThreadSecurity {

	/**
	 *
	 * @param runnable
	 * @return
	 */
	public static Thread createThread(final Runnable runnable) {
		return AccessController.doPrivileged(new PrivilegedAction<Thread>() {

			@Override
			public Thread run() {
				return new Thread(runnable);
			}
		});
	}
}
