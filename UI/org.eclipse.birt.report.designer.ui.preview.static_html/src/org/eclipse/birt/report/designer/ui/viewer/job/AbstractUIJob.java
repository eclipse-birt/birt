/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.ui.progress.UIJob;

/**
 * 
 */

public abstract class AbstractUIJob extends UIJob {

	private String designFile;

	public AbstractUIJob(String name, String designFile) {
		super(name);
		this.designFile = designFile;
	}

	public IStatus runInUIThread(IProgressMonitor monitor) {
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

	public boolean belongsTo(Object family) {
		if (family != null && family.equals(this.designFile))
			return true;
		return super.belongsTo(family);
	}

	public abstract void work(IProgressMonitor monitor);
}
