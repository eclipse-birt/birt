
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

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 *
 */

public class CancelManager extends TimerTask {
	//
	private Map<String, ICancellable> cancellableMap = new HashMap<>();

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
		synchronized (cancellableMap) {
			cancellableMap.put(cancellable.getId(), cancellable);
		}
	}

	/**
	 *
	 * @param cancellable
	 */
	public void deregister(ICancellable cancellable) {
		synchronized (cancellableMap) {
			cancellableMap.remove(cancellable.getId());
		}
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
		List<ICancellable> cancellableCollection = null;
		synchronized (cancellableMap) {
			cancellableCollection = new ArrayList<>(cancellableMap.values());
		}
		for (ICancellable cancellable : cancellableCollection) {
			if (cancellable.doCancel()) {
				cancellable.cancel();
			}
		}
	}
}
