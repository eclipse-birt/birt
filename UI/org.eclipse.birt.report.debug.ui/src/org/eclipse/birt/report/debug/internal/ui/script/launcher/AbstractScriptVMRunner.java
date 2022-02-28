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

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import org.eclipse.birt.report.debug.internal.ui.script.util.ScriptDebugUtil;
import org.eclipse.birt.report.debug.ui.DebugUI;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.jdt.launching.AbstractVMRunner;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

/**
 * Run the processor.
 */
public abstract class AbstractScriptVMRunner extends AbstractVMRunner {

	IVMInstall fVMInstance;

	/**
	 * Constractor
	 *
	 * @param instance
	 */
	public AbstractScriptVMRunner(IVMInstall instance) {
		super();
		fVMInstance = instance;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jdt.launching.AbstractVMRunner#getPluginIdentifier()
	 */
	@Override
	protected String getPluginIdentifier() {
		return DebugUI.getUniqueIdentifier();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jdt.launching.IVMRunner#run(org.eclipse.jdt.launching.
	 * VMRunnerConfiguration, org.eclipse.debug.core.ILaunch,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void run(VMRunnerConfiguration configuration, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		// donothing now see delegate
	}

	/**
	 * @param config
	 * @return
	 * @throws CoreException
	 */
	protected String constructProgramString(VMRunnerConfiguration config) throws CoreException {
		File exe = ScriptDebugUtil.findJavaExecutable(fVMInstance.getInstallLocation());
		if (exe == null) {
			throw new Error("not java exe file");//$NON-NLS-1$
		}
		return exe.getAbsolutePath();
	}

	/**
	 * Add the arguments
	 *
	 * @param args
	 * @param v
	 */
	protected void addArguments(String[] args, List v) {
		if (args == null) {
			return;
		}
		for (int i = 0; i < args.length; i++) {
			v.add(args[i]);
		}
	}

	/**
	 * Add the separator
	 *
	 * @param cp
	 * @return
	 */
	protected String convertClassPath(String[] cp) {
		int pathCount = 0;
		StringBuilder buf = new StringBuilder();
		if (cp.length == 0) {
			return ""; //$NON-NLS-1$
		}
		for (int i = 0; i < cp.length; i++) {
			if (pathCount > 0) {
				buf.append(File.pathSeparator);
			}
			buf.append(cp[i]);
			pathCount++;
		}
		return buf.toString();
	}

	/**
	 * Gets the work dir.
	 *
	 * @param config
	 * @return
	 * @throws CoreException
	 */
	protected File getWorkingDir(VMRunnerConfiguration config) throws CoreException {
		String path = config.getWorkingDirectory();
		if (path == null) {
			return null;
		}
		File dir = new File(path);
		if (!dir.isDirectory()) {
			throw new Error("Workking directory is null");//$NON-NLS-1$
		}
		return dir;
	}

	/**
	 * Change the list to the command line
	 *
	 * @param commandLine
	 * @return
	 */
	protected static String renderCommandLine(String[] commandLine) {
		if (commandLine.length < 1) {
			return ""; //$NON-NLS-1$
		}
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < commandLine.length; i++) {
			buf.append(' ');
			char[] characters = commandLine[i].toCharArray();
			StringBuilder command = new StringBuilder();
			boolean containsSpace = false;
			for (int j = 0; j < characters.length; j++) {
				char character = characters[j];
				if (character == '\"') {
					command.append('\\');
				} else if (character == ' ') {
					containsSpace = true;
				}
				command.append(character);
			}
			if (containsSpace) {
				buf.append('\"');
				buf.append(command.toString());
				buf.append('\"');
			} else {
				buf.append(command.toString());
			}
		}
		return buf.toString();
	}

	protected static int findFreePort() {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			return socket.getLocalPort();
		} catch (IOException e) {
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		return -1;
	}

}
