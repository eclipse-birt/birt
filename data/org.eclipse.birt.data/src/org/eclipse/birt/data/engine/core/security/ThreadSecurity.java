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

/**
 *
 *
 */
public class ThreadSecurity {

	/**
	 *
	 * @param runnable
	 * @return Return a new thread
	 */
	public static Thread createThread(final Runnable runnable) {
		return new Thread(runnable);
	}
}
