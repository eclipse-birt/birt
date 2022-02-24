/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.adapter;

import org.eclipse.birt.report.engine.api.IProgressMonitor;

/**
 * A proxy for IProgressMonitor.
 *
 *
 */
public class ProgressMonitorProxy implements IProgressMonitor {
	private IProgressMonitor proxy;

	public ProgressMonitorProxy(IProgressMonitor monitor) {
		proxy = monitor;
		initialize();
	}

	private void initialize() {

	}

	@Override
	public void onProgress(int type, int page) {
		if (proxy != null) {
			proxy.onProgress(type, page);
		}
	}
}
