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

package org.eclipse.birt.report.debug.internal.script.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.report.debug.internal.core.vm.ReportVMClient;
import org.eclipse.birt.report.debug.internal.core.vm.VMConstants;
import org.eclipse.birt.report.debug.internal.core.vm.VMContextData;
import org.eclipse.birt.report.debug.internal.core.vm.VMException;
import org.eclipse.birt.report.debug.internal.core.vm.VMListener;
import org.eclipse.birt.report.debug.internal.core.vm.VMStackFrame;
import org.eclipse.birt.report.debug.internal.core.vm.VMValue;
import org.eclipse.birt.report.debug.internal.core.vm.VMVariable;
import org.eclipse.birt.report.debug.internal.core.vm.js.JsLineBreakPoint;
import org.eclipse.birt.report.debug.internal.core.vm.js.JsTransientLineBreakPoint;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManagerListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;

import com.ibm.icu.util.ULocale;

/**
 * Debug target class
 */
public class ScriptDebugTarget extends ScriptDebugElement
		implements IDebugTarget, IBreakpointManagerListener, VMListener {

	private ModuleHandle handle;
	private static final Logger logger = Logger.getLogger(ScriptDebugTarget.class.getName());
	/**
	 * Debug process, run the ReportLauncher class.
	 */
	private IProcess process;

	/**
	 * The thread, only one thread.
	 */
	private ScriptDebugThread thread;

	/**
	 * Thread group contain the thread.
	 */
	private IThread[] threads;

	/**
	 * Script display name.
	 */
	private String name;

	/**
	 * Client Report VM.
	 */
	ReportVMClient reportVM;

	/**
	 * Launch
	 */
	private ILaunch launch;

	/**
	 * Break point list
	 */
	private List breakPoints = new ArrayList();

	/**
	 * File name
	 */
	private String fileName = ""; //$NON-NLS-1$

	/**
	 * Send and receive port number.
	 */
	private int listenPort;

	/**
	 * If the target is terminating.
	 */
	private boolean fTerminating;

	/**
	 * If the target is terminated
	 */
	private boolean fTerminated;

	/**
	 * Contructor
	 *
	 * @param launch
	 * @param vm
	 * @param name
	 * @param process
	 * @param listenPort
	 * @param eventPort
	 * @param tempFolder
	 */
	public ScriptDebugTarget(ILaunch launch, ReportVMClient vm, String name, IProcess process, int listenPort,
			String tempFolder) {
		super(null);
		this.launch = launch;
		this.reportVM = vm;
		this.name = name;
		this.process = process;

		this.listenPort = listenPort;

		launch.addDebugTarget(this);
		vm.addVMListener(this);

		setTerminating(false);
		setTerminated(false);
		thread = new ScriptDebugThread(this);
		// There are only one thread
		threads = new IThread[] { thread };

		DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(this);

		DebugPlugin.getDefault().getBreakpointManager().addBreakpointManagerListener(this);

		// connect the server util the ReportLauncher run already
		while (!isTerminated()) {
			try {
				vm.connect(listenPort);
				break;
			} catch (VMException e) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// do nothing
				}
				continue;
			}
		}
	}

	/**
	 * Gets the module handle
	 *
	 * @return
	 */
	public ModuleHandle getModuleHandle() {
		if (handle == null) {
			try {
				handle = getSessionHandle().openModule(getFileName(),
						// No need to close the stream here, the report
						// design parser will automaically close it.
						new FileInputStream(getFileName()));
			} catch (DesignFileException | FileNotFoundException e) {
			}
		}
		return handle;
	}

	private SessionHandle getSessionHandle() {
		return new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.getDefault());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.debug.internal.script.model.ScriptDebugElement#
	 * getDebugTarget()
	 */
	@Override
	public IDebugTarget getDebugTarget() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IDebugTarget#getName()
	 */
	@Override
	public String getName() throws DebugException {
		if (name == null) {
			name = getDefaultName();
			try {
				name = getLaunch().getLaunchConfiguration().getAttribute(IScriptConstants.ATTR_REPORT_PROGRAM, name);
			} catch (CoreException e) {
			}
		}
		return renderState() + name;
	}

	private String renderState() {
		if (isTerminated()) {
			return "<terminated>"; //$NON-NLS-1$
		}
		if (isDisconnected()) {
			return "<disconnected>"; //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}

	private String getDefaultName() {
		return "Report Script Running at localhost:" //$NON-NLS-1$
				+ listenPort;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IDebugTarget#getProcess()
	 */
	@Override
	public IProcess getProcess() {
		return process;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IDebugTarget#getThreads()
	 */
	@Override
	public IThread[] getThreads() throws DebugException {
		return threads;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IDebugTarget#hasThreads()
	 */
	@Override
	public boolean hasThreads() throws DebugException {
		return !(isTerminated() || isDisconnected());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.core.model.IDebugTarget#supportsBreakpoint(org.eclipse.
	 * debug.core.model.IBreakpoint)
	 */
	@Override
	public boolean supportsBreakpoint(IBreakpoint breakpoint) {
		if (!(breakpoint instanceof ScriptLineBreakpoint)) {
			return false;
		}
		String str = ((ScriptLineBreakpoint) breakpoint).getFileName();
		if (str == null || str.length() == 0) {
			return false;
		}
		return str.equals(getFileName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.ITerminate#canTerminate()
	 */
	@Override
	public boolean canTerminate() {
		return !(isTerminated() || isTerminating());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.ITerminate#isTerminated()
	 */
	@Override
	public boolean isTerminated() {
		return fTerminated;
	}

	/**
	 * Set the taget flag terminated.
	 */
	private void setTerminated(boolean terminated) {
		fTerminated = terminated;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.ITerminate#terminate()
	 */
	@Override
	public void terminate() throws DebugException {
		setTerminating(true);
		try {
			// Process the proces is terminated by client directly.
			if ((!isTerminated()) && reportVM.isTerminated()) {
				terminated();
				return;
			}
		} catch (VMException e1) {
			logger.warning(e1.getMessage());
		}

		try {
			reportVM.terminate();

		} catch (VMException e) {
			logger.warning(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.ISuspendResume#canResume()
	 */
	@Override
	public boolean canResume() {
		return !isTerminated() && isSuspended();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.ISuspendResume#canSuspend()
	 */
	@Override
	public boolean canSuspend() {
		return !isTerminated() && !isSuspended();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.ISuspendResume#isSuspended()
	 */
	@Override
	public boolean isSuspended() {
		try {
			return reportVM.isSuspended();
		} catch (VMException e) {
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.ISuspendResume#resume()
	 */
	@Override
	public void resume() throws DebugException {
		try {
			reportVM.resume();
		} catch (VMException e) {
			logger.warning(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.ISuspendResume#suspend()
	 */
	@Override
	public void suspend() throws DebugException {
		try {
			reportVM.suspend();
		} catch (VMException e) {
			logger.warning(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.core.IBreakpointListener#breakpointAdded(org.eclipse.debug.
	 * core.model.IBreakpoint)
	 */
	@Override
	public void breakpointAdded(IBreakpoint breakpoint) {
		if (!supportsBreakpoint(breakpoint)) {
			return;
		}
		try {
			if (!breakpoint.isEnabled()) {
				return;
			}
		} catch (CoreException e1) {
			return;
		}

		ScriptLineBreakpoint scriptPoint = (ScriptLineBreakpoint) breakpoint;
		try {
			if (scriptPoint.shouldSkipBreakpoint()) {
				return;
			}
		} catch (CoreException e1) {
			// do nothing
		}
		JsLineBreakPoint point = createJsLineBreakPoint(scriptPoint);
		try {
			if (ScriptLineBreakpoint.RUNTOLINE.equals(((ScriptLineBreakpoint) breakpoint).getType())) {
				reportVM.addBreakPoint(point);
			} else if ((!breakPoints.contains(point))) {
				breakPoints.add(point);

				reportVM.addBreakPoint(point);
			}

		} catch (VMException e) {
			logger.warning(e.getMessage());
		}

	}

	private JsLineBreakPoint createJsLineBreakPoint(ScriptLineBreakpoint breakpoint) {
		if (ScriptLineBreakpoint.RUNTOLINE.equals(breakpoint.getType())) {
			return new JsTransientLineBreakPoint(breakpoint.getSubName(), breakpoint.getScriptLineNumber());
		}
		return new JsLineBreakPoint(breakpoint.getSubName(), breakpoint.getScriptLineNumber());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.core.IBreakpointListener#breakpointChanged(org.eclipse.
	 * debug.core.model.IBreakpoint, org.eclipse.core.resources.IMarkerDelta)
	 */
	@Override
	public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
		if (!supportsBreakpoint(breakpoint)) {
			return;
		}
		try {
			if (breakpoint.isEnabled()) {
				breakpointAdded(breakpoint);
			} else {
				breakpointRemoved(breakpoint, null);
			}
		} catch (CoreException e) {

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.core.IBreakpointListener#breakpointRemoved(org.eclipse.
	 * debug.core.model.IBreakpoint, org.eclipse.core.resources.IMarkerDelta)
	 */
	@Override
	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
		if (!supportsBreakpoint(breakpoint)) {
			return;
		}

		JsLineBreakPoint point = new JsLineBreakPoint(((ScriptLineBreakpoint) breakpoint).getSubName(),
				((ScriptLineBreakpoint) breakpoint).getScriptLineNumber());
		if (breakPoints.contains(point)) {
			breakPoints.remove(point);
			try {
				reportVM.removeBreakPoint(point);
			} catch (VMException e) {
				logger.warning(e.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IDisconnect#canDisconnect()
	 */
	@Override
	public boolean canDisconnect() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IDisconnect#disconnect()
	 */
	@Override
	public void disconnect() throws DebugException {
		// do nothing now
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IDisconnect#isDisconnected()
	 */
	@Override
	public boolean isDisconnected() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IMemoryBlockRetrieval#getMemoryBlock(long,
	 * long)
	 */
	@Override
	public IMemoryBlock getMemoryBlock(long startAddress, long length) throws DebugException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.core.model.IMemoryBlockRetrieval#supportsStorageRetrieval()
	 */
	@Override
	public boolean supportsStorageRetrieval() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.debug.internal.script.model.ScriptDebugElement#
	 * getLaunch()
	 */
	@Override
	public ILaunch getLaunch() {
		return launch;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.debug.internal.core.vm.VMListener#handleEvent(int,
	 * org.eclipse.birt.report.debug.internal.core.vm.VMContextData)
	 */
	@Override
	public void handleEvent(int eventCode, VMContextData context) {
		if (eventCode == VMConstants.VM_SUSPENDED_STEP_OVER) {
			suspended(DebugEvent.STEP_END);
			thread.setStepping(false);
		} else if (eventCode == VMConstants.VM_SUSPENDED_STEP_INTO) {
			suspended(DebugEvent.STEP_END);
			thread.setStepping(false);
		} else if (eventCode == VMConstants.VM_SUSPENDED_STEP_OUT) {
			suspended(DebugEvent.STEP_END);
			thread.setStepping(false);
		} else if (eventCode == VMConstants.VM_STARTED) {
			started();
		} else if (eventCode == VMConstants.VM_SUSPENDED_BREAKPOINT) {
			suspended(DebugEvent.BREAKPOINT);
		} else if (eventCode == VMConstants.VM_SUSPENDED_CLIENT) {
			suspended(DebugEvent.CLIENT_REQUEST);
		} else if (eventCode == VMConstants.VM_TERMINATED) {
			terminated();
		} else if (eventCode == VMConstants.VM_RESUMED) {
			resumed(DebugEvent.RESUME);
		}
	}

	/**
	 * Notification the target has resumed for the given reason
	 *
	 * @param detail reason for the resume
	 */
	private void resumed(int detail) {
		thread.fireResumeEvent(detail);
	}

	private void started() {
		installDeferredBreakpoints();
		try {
			// reportVM.addBreakPoint( new JsLineBreakPoint( "test", 1 ) );
			reportVM.resume();
		} catch (VMException e) {
			logger.warning(e.getMessage());
		}
		fireCreationEvent();
	}

	private void installDeferredBreakpoints() {
		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager()
				.getBreakpoints(IScriptConstants.SCRIPT_DEBUG_MODEL);
		for (int i = 0; i < breakpoints.length; i++) {
			breakpointAdded(breakpoints[i]);
		}
	}

	private void suspended(int detail) {
		thread.fireSuspendEvent(detail);
	}

	private void terminated() {
		DebugPlugin.getDefault().getBreakpointManager().removeBreakpointListener(this);
		DebugPlugin.getDefault().getBreakpointManager().removeBreakpointManagerListener(this);
		setTerminating(false);
		if (!fTerminated) {
			setTerminated(true);
			reportVM.disconnect();
			fireTerminateEvent();
		}
		breakPoints.clear();
	}

	/**
	 * Gets the stack frames.
	 *
	 * @return
	 * @throws DebugException
	 */
	protected IStackFrame[] getStackFrames() throws DebugException {
		VMStackFrame[] frames;
		try {
			frames = reportVM.getStackFrames();

			int len = frames.length;

			IStackFrame[] retValue = new IStackFrame[len];

			for (int i = len - 1; i >= 0; i--) {
				VMStackFrame frame = frames[i];
				// may be need to init the variable
				ScriptStackFrame debugStack = new ScriptStackFrame(thread, frame.getName(), i);
				debugStack.setLineNumber(frame.getLineNumber());
				retValue[len - i - 1] = debugStack;
			}
			return retValue;
		} catch (VMException e) {
			logger.warning(e.getMessage());
		}
		return null;
	}

	/**
	 * Step into
	 *
	 * @throws DebugException
	 */
	public void stepInto() throws DebugException {
		try {
			thread.setStepping(true);
			reportVM.stepInto();
		} catch (VMException e) {
			logger.warning(e.getMessage());
		}

	}

	/**
	 * Step over
	 *
	 * @throws DebugException
	 */
	public void stepOver() throws DebugException {
		try {
			thread.setStepping(true);
			reportVM.step();
		} catch (VMException e) {
			logger.warning(e.getMessage());
		}

	}

	/**
	 * Step return
	 *
	 * @throws DebugException
	 */
	public void stepReturn() throws DebugException {
		try {
			thread.setStepping(true);
			reportVM.stepOut();
		} catch (VMException e) {
			logger.warning(e.getMessage());
		}

	}

	/**
	 * Gets the variables from the stack frame.
	 *
	 * @param frame
	 * @return
	 */
	public IVariable[] getVariables(ScriptStackFrame frame) {
		VMVariable[] variables;
		try {
			VMStackFrame fm = reportVM.getStackFrame(frame.getIdentifier());

			if (fm == null) {
				return null;
			}

			variables = fm.getVariables();

			IVariable[] retValue = new IVariable[variables.length];

			for (int i = 0; i < variables.length; i++) {
				VMVariable variable = variables[i];
				ScriptVariable debugVariable = new ScriptVariable(frame, variable.getName(), variable.getTypeName());

				VMValue value = variable.getValue();

				ScriptValue debugValue = new ScriptValue(frame, value);

				debugVariable.setOriVale(debugValue);

				retValue[i] = debugVariable;

			}

			return retValue;
		} catch (VMException e) {
			logger.warning(e.getMessage());
		}
		return null;
	}

	/**
	 * Gets teh value from the String throw the VM.
	 *
	 * @param frame
	 * @param expression
	 * @return
	 */
	public ScriptValue evaluate(ScriptStackFrame frame, String expression) {
		VMValue value;
		try {
			value = reportVM.evaluate(expression);
		} catch (VMException e) {
			return null;
		}
		if ((value == null) || VMConstants.UNDEFINED_TYPE.equals(value.getTypeName())
				|| VMConstants.EXCEPTION_TYPE.equals(value.getTypeName())) {
			return null;
		}
		ScriptValue debugValue = new ScriptValue(frame, value);

		return debugValue;
	}

	/**
	 * Gets the file name.
	 *
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * IF the target is terminating.
	 *
	 * @return
	 */
	protected boolean isTerminating() {
		return fTerminating;
	}

	/**
	 * @param terminating
	 */
	protected void setTerminating(boolean terminating) {
		fTerminating = terminating;
	}

	/**
	 * @return
	 */
	public boolean isAvailable() {
		return !(isTerminated() || isTerminating());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.debug.internal.script.model.ScriptDebugElement#
	 * getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		try {
			return getName();
		} catch (DebugException e) {
			return getDefaultName();
		}
	}

	@Override
	public void breakpointManagerEnablementChanged(boolean enabled) {
		if (!isAvailable()) {
			return;
		}

		Iterator breakpoints = new ArrayList(breakPoints).iterator();

		while (breakpoints.hasNext()) {
			JsLineBreakPoint breakpoint = (JsLineBreakPoint) breakpoints.next();
			try {
				if (enabled) {
					reportVM.removeBreakPoint(breakpoint);
				} else {
					reportVM.removeBreakPoint(breakpoint);
				}
			} catch (VMException e) {
				logger.warning(e.getMessage());
			}

		}
	}
}
