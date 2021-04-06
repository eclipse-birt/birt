
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		cancellableList = new ArrayList<ICancellable>();
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
	public void run() {
		doCancel();
	}

	public void doCancel() {
		synchronized (cancellableList) {
			List<ICancellable> cancellableLists = new ArrayList<ICancellable>(cancellableList);
			for (ICancellable cancellable : cancellableLists) {
				if (cancellable.doCancel())
					cancellable.cancel();
			}
		}
	}
}
