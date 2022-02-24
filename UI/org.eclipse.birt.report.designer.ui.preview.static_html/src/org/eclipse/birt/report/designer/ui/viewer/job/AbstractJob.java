/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.viewer.job;

import org.eclipse.birt.report.designer.ui.preview.static_html.StaticHTMLPrviewPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 *
 */

public abstract class AbstractJob extends Job {

	private String designFile;

	public AbstractJob(String name, String designFile) {
		super(name);
		this.designFile = designFile;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IStatus returnValue = Status.OK_STATUS;
		setPriority(Job.SHORT);
		monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);

		if (monitor.isCanceled()) {
			Job.getJobManager().cancel(this.designFile);
			return Status.CANCEL_STATUS;
		}

		try {
			work(monitor);
		} catch (RuntimeException e) {
			returnValue = new Status(IStatus.ERROR, StaticHTMLPrviewPlugin.PLUGIN_ID, 500, e.getMessage(), e);
			Job.getJobManager().cancel(this.designFile);
		} finally {
			monitor.done();
		}

		if (monitor.isCanceled()) {
			Job.getJobManager().cancel(this.designFile);
			returnValue = Status.CANCEL_STATUS;
		}
		return returnValue;
	}

	@Override
	public boolean belongsTo(Object family) {
		if (family != null && family.equals(this.designFile)) {
			return true;
		}
		return super.belongsTo(family);
	}

	public abstract void work(IProgressMonitor monitor);
}
