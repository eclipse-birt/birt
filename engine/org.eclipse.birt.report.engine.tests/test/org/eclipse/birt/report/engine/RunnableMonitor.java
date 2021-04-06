/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 
 */

public class RunnableMonitor {

	ArrayList runnables = new ArrayList();
	ArrayList failedRunnables = new ArrayList();

	public synchronized void attachRunnable(RunnableMonitor.Runnable runnable) {
		runnables.add(runnable);
	}

	public synchronized void detachRunnable(RunnableMonitor.Runnable runnable) {
		runnables.remove(runnable);
		if (runnable.failedException != null) {
			failedRunnables.add(runnable);
		}
		if (runnables.size() == 0) {
			this.notifyAll();
		}
	}

	public synchronized void start() {
		failedRunnables.clear();
		Runnable[] threads = (Runnable[]) runnables.toArray(new Runnable[] {});
		for (int i = 0; i < threads.length; i++) {
			new Thread(threads[i]).start();
		}
		try {
			this.wait();
		} catch (InterruptedException ie) {
		}
	}

	public Collection getFailedRunnables() {
		return failedRunnables;
	}

	public void printStackTrace() {
		Iterator iter = failedRunnables.iterator();
		while (iter.hasNext()) {
			RunnableMonitor.Runnable runnable = (RunnableMonitor.Runnable) iter.next();
			Exception ex = runnable.getFailedException();
			ex.printStackTrace();
		}
	}

	static public abstract class Runnable implements java.lang.Runnable {

		Exception failedException;
		RunnableMonitor monitor;

		public Runnable(RunnableMonitor monitor) {
			this.monitor = monitor;
			this.monitor.attachRunnable(this);
		}

		public void run() {
			failedException = null;
			try {
				doRun();
			} catch (Exception ex) {
				failedException = ex;
			} finally {
				monitor.detachRunnable(this);
			}
		}

		public Exception getFailedException() {
			return failedException;
		}

		abstract public void doRun() throws Exception;

		public void sleep(long millis) {
			try {
				Thread.sleep(millis);
			} catch (InterruptedException iie) {

			}
		}
	}
}
