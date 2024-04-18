
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl;

import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */

public class CancelManager extends TimerTask {
	//
	private final Set<ICancellable> cancellables = ConcurrentHashMap.newKeySet();

	/**
	 * Constructor
	 */
	public CancelManager() {
	}

	/**
	 *
	 * @param cancellable
	 */
	public void register(ICancellable cancellable) {
		cancellables.add(cancellable);
	}

	/**
	 *
	 * @param cancellable
	 */
	public void deregister(ICancellable cancellable) {
		cancellables.remove(cancellable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		doCancel();
	}

	public void doCancel() {
		for (ICancellable cancellable : cancellables.toArray(ICancellable[]::new)) {
			if (cancellable.doCancel()) {
				cancellable.cancel();
			}
		}
	}
}
