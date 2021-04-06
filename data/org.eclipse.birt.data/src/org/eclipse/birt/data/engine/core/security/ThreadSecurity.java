/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

			public Thread run() {
				return new Thread(runnable);
			}
		});
	}
}
