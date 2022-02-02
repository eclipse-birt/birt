/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.report.debug.internal.core.launcher.IReportLaunchConstants;
import org.eclipse.birt.report.debug.internal.core.vm.ReportVMClient;
import org.eclipse.birt.report.debug.internal.script.model.ScriptDebugTarget;
import org.eclipse.birt.report.debug.ui.DebugUI;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.IStatusHandler;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.jdi.Bootstrap;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.jdt.internal.launching.LaunchingPlugin;
import org.eclipse.jdt.internal.launching.LibraryInfo;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstall2;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.ListeningConnector;

/**
 * StandardScriptVMRunner
 */
public class StandardScriptVMRunner extends AbstractScriptVMRunner {
	protected static final String JAVA_JVM_VERSION = "JAVA_JVM_VERSION"; //$NON-NLS-1$
	protected ReportStandardAppLaunchDelegate delegate;

	public StandardScriptVMRunner(IVMInstall vmInstance, ReportStandardAppLaunchDelegate delegate) {
		super(vmInstance);

		this.delegate = delegate;
	}

	public void run(VMRunnerConfiguration config, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		int debugType = delegate.getDebugType();

		boolean debugJava = (debugType & IReportLaunchConstants.DEBUG_TYPE_JAVA_CLASS) != 0;
		boolean debugScript = (debugType & IReportLaunchConstants.DEBUG_TYPE_JAVA_SCRIPT) != 0;

		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		IProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1);
		subMonitor.beginTask("Launching VM...", 4); //$NON-NLS-1$

		subMonitor.subTask("Constructing command line... "); //$NON-NLS-1$

		String program = constructProgramString(config);

		List arguments = new ArrayList();
		arguments.add(program);

		int port = -1;

		if (debugJava) {
			// VM arguments are the first thing after the java program so that
			// users
			// can specify
			// options like '-client' & '-server' which are required to be the
			// first
			// options
			double version = getJavaVersion();
			if (version < 1.5) {
				arguments.add("-Xdebug"); //$NON-NLS-1$
				arguments.add("-Xnoagent"); //$NON-NLS-1$
			}

			port = findFreePort();

			// check if java 1.4 or greater
			if (version < 1.4) {
				arguments.add("-Djava.compiler=NONE"); //$NON-NLS-1$
			}
			if (version < 1.5) {
				arguments.add("-Xrunjdwp:transport=dt_socket,suspend=y,address=localhost:" + port); //$NON-NLS-1$
			} else {
				arguments.add("-agentlib:jdwp=transport=dt_socket,suspend=y,address=localhost:" + port); //$NON-NLS-1$
			}
		}

		String[] allVMArgs = combineVmArgs(config, fVMInstance);
		addArguments(allVMArgs, arguments);

		// addBootClassPathArguments( arguments, config );

		String[] cp = config.getClassPath();
		if (cp.length > 0) {
			arguments.add("-classpath"); //$NON-NLS-1$
			arguments.add(convertClassPath(cp));
		}

		delegate.helper.addParameterArgs(arguments);
		delegate.helper.addDataLimitArgs(arguments);
		delegate.helper.addEngineHomeArgs(arguments);
		delegate.helper.addResourceFolder(arguments);
		delegate.helper.addFileNameArgs(arguments);
		delegate.helper.addTempFolder(arguments);
		delegate.helper.addTypeArgs(arguments);
		delegate.helper.addPortArgs(arguments);
		delegate.helper.addUserClassPath(arguments, launch.getLaunchConfiguration());

		arguments.add(config.getClassToLaunch());

		String[] programArgs = config.getProgramArguments();

		addArguments(programArgs, arguments);

		String[] cmdLine = new String[arguments.size()];
		arguments.toArray(cmdLine);

		String[] envp = prependJREPath(config.getEnvironment());
		subMonitor.worked(1);

		// check for cancellation
		if (monitor.isCanceled()) {
			return;
		}

		if (debugJava) {
			subMonitor.subTask("Creating debug process..."); //$NON-NLS-1$
			ListeningConnector connector = getConnector();
			if (connector == null) {
				abort("Couldn't find an appropriate debug connector", //$NON-NLS-1$
						null, IJavaLaunchConfigurationConstants.ERR_CONNECTOR_NOT_AVAILABLE);
			}

			Map map = connector.defaultArguments();

			specifyArguments(map, port);
			Process p = null;

			try {
				try {
					// check for cancellation
					if (monitor.isCanceled()) {
						return;
					}

					connector.startListening(map);

					File workingDir = getWorkingDir(config);
					p = exec(cmdLine, workingDir, envp);
					if (p == null) {
						return;
					}

					// check for cancellation
					if (monitor.isCanceled()) {
						p.destroy();
						return;
					}

					IProcess process = newProcess(launch, p, cmdLine[0], getDefaultProcessMap());
					process.setAttribute(IProcess.ATTR_CMDLINE, renderCommandLine(cmdLine));
					subMonitor.worked(1);

					subMonitor.subTask("Establishing debug connection..."); //$NON-NLS-1$

					boolean retry = false;
					do {
						try {

							ConnectRunnable runnable = new ConnectRunnable(connector, map);
							Thread connectThread = new Thread(runnable, "Listening Connector"); //$NON-NLS-1$
							connectThread.setDaemon(true);
							connectThread.start();
							while (connectThread.isAlive()) {
								if (monitor.isCanceled()) {
									try {
										connector.stopListening(map);
									} catch (IOException ioe) {
										// expected
									}
									p.destroy();
									return;
								}

								try {
									p.exitValue();
									// process has terminated - stop waiting for
									// a
									// connection
									try {
										connector.stopListening(map);
									} catch (IOException e) {
										// expected
									}
									checkErrorMessage(process);
								} catch (IllegalThreadStateException e) {
									// expected while process is alive
								}
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
								}
							}

							Exception ex = runnable.getException();
							if (ex instanceof IllegalConnectorArgumentsException) {
								throw (IllegalConnectorArgumentsException) ex;
							}
							if (ex instanceof InterruptedIOException) {
								throw (InterruptedIOException) ex;
							}
							if (ex instanceof IOException) {
								throw (IOException) ex;
							}

							VirtualMachine vm = runnable.getVirtualMachine();
							if (vm != null) {
								createDebugTarget(config, launch, port, process, vm);

								subMonitor.worked(1);

								if (debugScript) {
									subMonitor.subTask("Starting virtual machine..."); //$NON-NLS-1$

									ReportVMClient scriptvm = new ReportVMClient();
									ScriptDebugTarget target = new ScriptDebugTarget(launch, scriptvm, null, process,
											delegate.helper.listenPort, delegate.getTempFolder());
									target.setFileName(delegate.getFileName());

									subMonitor.worked(1);
								}
								subMonitor.done();

								ReportLaunchHelper.handleProcessTermination(launch, process, delegate.getFileName(),
										delegate.getTempFolder());

							}
							return;
						} catch (InterruptedIOException e) {
							checkErrorMessage(process);

							// timeout, consult status handler if there is one
							IStatus status = new Status(IStatus.ERROR, DebugUI.ID_PLUGIN,
									IJavaLaunchConfigurationConstants.ERR_VM_CONNECT_TIMEOUT, "", e); //$NON-NLS-1$
							IStatusHandler handler = DebugPlugin.getDefault().getStatusHandler(status);

							retry = false;
							if (handler == null) {
								// if there is no handler, throw the exception
								throw new CoreException(status);
							}
							Object result = handler.handleStatus(status, this);
							if (result instanceof Boolean) {
								retry = ((Boolean) result).booleanValue();
							}
						}
					} while (retry);
				} finally {
					connector.stopListening(map);
				}
			} catch (IOException e) {
				abort("Couldn't connect to VM", //$NON-NLS-1$
						e, IJavaLaunchConfigurationConstants.ERR_CONNECTION_FAILED);
			} catch (IllegalConnectorArgumentsException e) {
				abort("Couldn't connect to VM", //$NON-NLS-1$
						e, IJavaLaunchConfigurationConstants.ERR_CONNECTION_FAILED);
			}
			if (p != null) {
				p.destroy();
			}
		} else {
			subMonitor.subTask("Creating debug process..."); //$NON-NLS-1$

			Process p = null;
			File workingDir = getWorkingDir(config);
			p = exec(cmdLine, workingDir, envp);
			if (p == null) {
				return;
			}

			// check for cancellation
			if (monitor.isCanceled()) {
				p.destroy();
				return;
			}

			IProcess process = newProcess(launch, p,
					// renderProcessLabel( cmdLine ),
					cmdLine[0], getDefaultProcessMap());
			process.setAttribute(IProcess.ATTR_CMDLINE, renderCommandLine(cmdLine));

			if (debugScript) {
				subMonitor.worked(1);
				subMonitor.subTask("Starting virtual machine..."); //$NON-NLS-1$

				ReportVMClient vm = new ReportVMClient();
				ScriptDebugTarget target = new ScriptDebugTarget(launch, vm, null, process, delegate.helper.listenPort,
						delegate.getTempFolder());
				target.setFileName(delegate.getFileName());
			}

			subMonitor.worked(1);
			subMonitor.done();

			ReportLaunchHelper.handleProcessTermination(launch, process, delegate.getFileName(),
					delegate.getTempFolder());
		}

	}

	/**
	 * Returns the version of the current VM in use
	 * 
	 * @return the VM version
	 */
	private double getJavaVersion() {
		String version = null;
		if (fVMInstance instanceof IVMInstall2) {
			version = ((IVMInstall2) fVMInstance).getJavaVersion();
		} else {
			LibraryInfo libInfo = LaunchingPlugin.getLibraryInfo(fVMInstance.getInstallLocation().getAbsolutePath());
			if (libInfo == null) {
				return 0D;
			}
			version = libInfo.getVersion();
		}
		int index = version.indexOf("."); //$NON-NLS-1$
		int nextIndex = version.indexOf(".", index + 1); //$NON-NLS-1$
		try {
			if (index > 0 && nextIndex > index) {
				return Double.parseDouble(version.substring(0, nextIndex));
			}
			return Double.parseDouble(version);
		} catch (NumberFormatException e) {
			return 0D;
		}

	}

	private ListeningConnector getConnector() {
		List connectors = Bootstrap.virtualMachineManager().listeningConnectors();
		for (int i = 0; i < connectors.size(); i++) {
			ListeningConnector c = (ListeningConnector) connectors.get(i);
			if ("com.sun.jdi.SocketListen".equals(c.name())) //$NON-NLS-1$
				return c;
		}
		return null;
	}

	private void specifyArguments(Map map, int portNumber) {
		// XXX: Revisit - allows us to put a quote (") around the classpath
		Connector.IntegerArgument port = (Connector.IntegerArgument) map.get("port"); //$NON-NLS-1$
		port.setValue(portNumber);

		Connector.IntegerArgument timeoutArg = (Connector.IntegerArgument) map.get("timeout"); //$NON-NLS-1$
		if (timeoutArg != null) {
			int timeout = JavaRuntime.getPreferences().getInt(JavaRuntime.PREF_CONNECT_TIMEOUT);
			timeoutArg.setValue(timeout);
		}
	}

	/**
	 * Checks and forwards an error from the specified process
	 * 
	 * @param process
	 * @throws CoreException
	 */
	private void checkErrorMessage(IProcess process) throws CoreException {
		IStreamsProxy streamsProxy = process.getStreamsProxy();
		if (streamsProxy != null) {
			String errorMessage = streamsProxy.getErrorStreamMonitor().getContents();
			if (errorMessage.length() == 0) {
				errorMessage = streamsProxy.getOutputStreamMonitor().getContents();
			}
			if (errorMessage.length() != 0) {
				abort(errorMessage, null, IJavaLaunchConfigurationConstants.ERR_VM_LAUNCH_ERROR);
			}
		}
	}

	/**
	 * Creates a new debug target for the given virtual machine and system process
	 * that is connected on the specified port for the given launch.
	 * 
	 * @param config  run configuration used to launch the VM
	 * @param launch  launch to add the target to
	 * @param port    port the VM is connected to
	 * @param process associated system process
	 * @param vm      JDI virtual machine
	 */
	private IDebugTarget createDebugTarget(VMRunnerConfiguration config, ILaunch launch, int port, IProcess process,
			VirtualMachine vm) {
		String debugName = "Report Running at localhost:" //$NON-NLS-1$
				+ port;

		return JDIDebugModel.newDebugTarget(launch, vm, debugName, process, true, false, config.isResumeOnStartup());
	}

	/**
	 * Used to attach to a VM in a separate thread, to allow for cancellation and
	 * detect that the associated System process died before the connect occurred.
	 */
	static class ConnectRunnable implements Runnable {

		private VirtualMachine fVirtualMachine = null;
		private ListeningConnector fConnector = null;
		private Map fConnectionMap = null;
		private Exception fException = null;

		/**
		 * Constructs a runnable to connect to a VM via the given connector with the
		 * given connection arguments.
		 * 
		 * @param connector
		 * @param map
		 */
		public ConnectRunnable(ListeningConnector connector, Map map) {
			fConnector = connector;
			fConnectionMap = map;
		}

		public void run() {
			try {
				fVirtualMachine = fConnector.accept(fConnectionMap);
			} catch (IOException e) {
				fException = e;
			} catch (IllegalConnectorArgumentsException e) {
				fException = e;
			}
		}

		/**
		 * Returns the VM that was attached to, or <code>null</code> if none.
		 * 
		 * @return the VM that was attached to, or <code>null</code> if none
		 */
		public VirtualMachine getVirtualMachine() {
			return fVirtualMachine;
		}

		/**
		 * Returns any exception that occurred while attaching, or <code>null</code>.
		 * 
		 * @return IOException or IllegalConnectorArgumentsException
		 */
		public Exception getException() {
			return fException;
		}
	}

	/**
	 * Prepends the correct java version variable state to the environment path for
	 * Mac VMs
	 * 
	 * @param env     the current array of environment variables to run with
	 * @param jdkpath the path of the current jdk
	 * @since 3.3
	 */
	protected String[] prependJREPath(String[] env) {
		if (Platform.OS_MACOSX.equals(Platform.getOS())) {
			if (fVMInstance instanceof IVMInstall2) {
				IVMInstall2 vm = (IVMInstall2) fVMInstance;
				String javaVersion = vm.getJavaVersion();
				if (javaVersion != null) {
					if (env == null) {
						Map map = DebugPlugin.getDefault().getLaunchManager().getNativeEnvironmentCasePreserved();
						if (map.containsKey(JAVA_JVM_VERSION)) {
							String[] env2 = new String[map.size()];
							Iterator iterator = map.entrySet().iterator();
							int i = 0;
							while (iterator.hasNext()) {
								Entry entry = (Entry) iterator.next();
								String key = (String) entry.getKey();
								if (JAVA_JVM_VERSION.equals(key)) {
									env2[i] = key + "=" + javaVersion; //$NON-NLS-1$
								} else {
									env2[i] = key + "=" + (String) entry.getValue(); //$NON-NLS-1$
								}
								i++;
							}
							env = env2;
						}
					} else {
						for (int i = 0; i < env.length; i++) {
							String string = env[i];
							if (string.startsWith(JAVA_JVM_VERSION)) {
								env[i] = JAVA_JVM_VERSION + "=" + javaVersion; //$NON-NLS-1$
								break;
							}
						}
					}
				}
			}
		}
		return env;
	}
}
