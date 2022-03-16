/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
package org.eclipse.birt.report.data.oda.jdbc.ui.model;

public abstract class ChildrenAllowedNode implements IDBNode {
	private IDBNode[] children;

	public boolean isChildrenPrepared() {
		return children != null;
	}

	public void prepareChildren(FilterConfig fc, long timeout) {
		class TempThread extends Thread {
			private FilterConfig fc;

			TempThread(FilterConfig fc) {
				this.fc = fc;
			}

			private IDBNode[] result = null;

			@Override
			public void run() {
				result = refetchChildren(fc);
			}

			public IDBNode[] getResult() {
				return result;
			}
		}
		TempThread tt = new TempThread(fc);
		tt.start();
		try {
			tt.join(timeout);
		} catch (InterruptedException e) {

		}
		IDBNode[] children = tt.getResult();
		if (children == null) {
			children = new IDBNode[0];
		}
		setChildren(children);
	}

	protected abstract IDBNode[] refetchChildren(FilterConfig fc);

	public IDBNode[] getChildren() {
		return children;
	}

	protected void setChildren(IDBNode[] children) {
		this.children = children;
	}
}
