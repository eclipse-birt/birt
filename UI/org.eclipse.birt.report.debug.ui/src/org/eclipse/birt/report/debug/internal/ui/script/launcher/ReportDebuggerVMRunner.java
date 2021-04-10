/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.ui.script.launcher;

import org.eclipse.birt.report.debug.internal.core.vm.ReportVMClient;
import org.eclipse.birt.report.debug.internal.script.model.ScriptDebugTarget;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

class ReportDebuggerVMRunner implements IVMRunner {

	private ReportOSGiLaunchDelegate config;
	private IVMRunner delegate;
	private boolean runScript;

	ReportDebuggerVMRunner(IVMRunner delegate, boolean runScript, ReportOSGiLaunchDelegate config) {
		this.delegate = delegate;
		this.runScript = runScript;
		this.config = config;
	}

	public void run(VMRunnerConfiguration configuration, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		delegate.run(configuration, launch, monitor);

		IProcess[] ps = launch.getProcesses();

		if (ps != null && ps.length > 0) {
			if (runScript) {
				if (monitor == null) {
					monitor = new NullProgressMonitor();
				}

				IProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1);
				subMonitor.beginTask("Launching VM...", 1); //$NON-NLS-1$

				ReportVMClient vm = new ReportVMClient();
				ScriptDebugTarget target = new ScriptDebugTarget(launch, vm, null, ps[0], config.helper.listenPort,
						config.helper.tempFolder);
				target.setFileName(config.helper.fileName);

				subMonitor.worked(1);
				subMonitor.done();
			}

			ReportLaunchHelper.handleProcessTermination(launch, ps[0], config.helper.fileName,
					config.helper.tempFolder);
		}
	}

}
