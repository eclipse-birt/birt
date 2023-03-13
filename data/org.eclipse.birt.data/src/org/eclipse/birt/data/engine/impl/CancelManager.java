
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

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 *
 */

public class CancelManager extends TimerTask {
	//
	private List<ICancellable> cancellableList;

	/**
	 * Constructor
	 */
	public CancelManager() {
		cancellableList = new ArrayList<>();
	}

	/**
	 *
	 * @param cancellable
	 */
	public void register(ICancellable cancellable) {
		synchronized (cancellableList) {
			cancellableList.add(cancellable);
		}
	}

	/**
	 *
	 * @param cancellable
	 */
	public void deregister(ICancellable cancellable) {
		synchronized (cancellableList) {
			cancellableList.remove(cancellable);
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
		synchronized (cancellableList) {
			List<ICancellable> cancellableLists = new ArrayList<>(cancellableList);
			for (ICancellable cancellable : cancellableLists) {
				if (cancellable.doCancel()) {
					cancellable.cancel();
				}
			}
		}
	}
}
