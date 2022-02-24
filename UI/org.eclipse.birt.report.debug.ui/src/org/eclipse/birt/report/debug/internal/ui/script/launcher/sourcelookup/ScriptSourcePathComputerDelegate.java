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

package org.eclipse.birt.report.debug.internal.ui.script.launcher.sourcelookup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.debug.internal.core.launcher.IReportLaunchConstants;
import org.eclipse.birt.report.debug.internal.ui.script.util.ScriptDebugUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IRuntimeClasspathProvider;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.StandardSourcePathProvider;

/**
 * ScriptSourcePathComputerDelegate
 */
public class ScriptSourcePathComputerDelegate implements ISourcePathComputerDelegate {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate#
	 * computeSourceContainers(org.eclipse.debug.core.ILaunchConfiguration,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ISourceContainer[] computeSourceContainers(ILaunchConfiguration configuration, IProgressMonitor monitor)
			throws CoreException {
		List containers = new ArrayList();

		String path = getPath(configuration);

		if (path != null) {
			containers.add(new ScriptDirectorySourceContainer(new File(path), false));
		}

		// always use standard source path provider to avoid PDE setting overwritten
		IRuntimeClasspathProvider scp = new StandardSourcePathProvider();

		IRuntimeClasspathEntry[] entries = scp.computeUnresolvedClasspath(configuration);
		IRuntimeClasspathEntry[] resolved = scp.resolveClasspath(entries, configuration);
		ISourceContainer[] cts = JavaRuntime.getSourceContainers(resolved);

		if (cts != null) {
			for (int i = 0; i < cts.length; i++) {
				containers.add(cts[i]);
			}
		}

		return (ISourceContainer[]) containers.toArray(new ISourceContainer[containers.size()]);
	}

	private String getFileName(ILaunchConfiguration configuration) {
		String retValue = ""; //$NON-NLS-1$
		try {
			retValue = configuration.getAttribute(IReportLaunchConstants.ATTR_REPORT_FILE_NAME, ""); //$NON-NLS-1$
		} catch (CoreException e) {
			return retValue;
		}
		try {
			retValue = ScriptDebugUtil.getSubstitutedString(retValue);
		} catch (CoreException e) {
			return retValue;
		}
		return retValue;
	}

	private String getPath(ILaunchConfiguration configuration) {
		String str = getFileName(configuration);
		int index = str.lastIndexOf(File.separator);
		return str.substring(0, index + 1);
	}
}
