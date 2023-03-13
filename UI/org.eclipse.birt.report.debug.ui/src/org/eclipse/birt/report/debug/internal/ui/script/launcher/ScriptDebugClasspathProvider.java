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

package org.eclipse.birt.report.debug.internal.ui.script.launcher;

import org.eclipse.birt.report.debug.internal.core.launcher.IReportLaunchConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.StandardClasspathProvider;

/**
 * ScriptDebugClasspathProvider
 */
public class ScriptDebugClasspathProvider extends StandardClasspathProvider {

	public IRuntimeClasspathEntry[] computeExtraBootClasspath(ILaunchConfiguration configuration) throws CoreException {
		return recoverRuntimePath(configuration, IReportLaunchConstants.ATTR_CLASSPATH);
	}

	public IRuntimeClasspathEntry[] computeUserClasspath(ILaunchConfiguration configuration) throws CoreException {
		return recoverRuntimePath(configuration, IJavaLaunchConfigurationConstants.ATTR_CLASSPATH);
	}

}
