/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core.vm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.report.debug.internal.core.vm.js.JsContextData;
import org.eclipse.birt.report.debug.internal.core.vm.js.JsDebugger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.debug.Debugger;

/**
 * ReportVM
 */
public class ReportVM implements VMConstants {

	private List vmListeners;

	private List bpListeners;

	private List deferredBreakPoints;

	private Object monitor;

	private boolean suspended;

	private boolean isStartSuspended;

	private boolean isAttached;

	private volatile String evalRequest;

	private volatile Object evalResult;

	private volatile VMContextData currentContextData;

	private volatile int currentVMState = VM_IDLE;

	private JsDebugger jsDebugger;

	private ContextFactory.Listener factoryListener;

	public ReportVM() {
		vmListeners = new ArrayList();
		bpListeners = new ArrayList();
		deferredBreakPoints = new LinkedList();
		monitor = new Object();
	}

	public void attach(Context cx) {
		attach(cx, false);
	}

	public void attach(Context cx, boolean startSuspended) {
		isStartSuspended = startSuspended;

		jsDebugger = new JsDebugger(this);

		isAttached = true;

		factoryListener = new ContextFactory.Listener() {

			@Override
			public void contextCreated(Context cx) {
				JsContextData cxData = new JsContextData();

				cx.setDebugger(jsDebugger, cxData);
				cx.setGeneratingDebug(true);
				cx.setOptimizationLevel(-1);
			}

			@Override
			public void contextReleased(Context cx) {
				cx.setDebugger(null, null);
			}

		};

		// cx.getFactory( ).addListener( factoryListener );

		if (cx.getDebugger() == null) {
			JsContextData cxData = new JsContextData();

			cx.setDebugger(jsDebugger, cxData);
			cx.setGeneratingDebug(true);
			cx.setOptimizationLevel(-1);

			addDeferredBreakPoints();
		}

		if (startSuspended) {
			currentVMState = VM_SUSPEND;

			Object cxData = cx.getDebuggerContextData();

			if (cxData instanceof JsContextData) {
				((JsContextData) cxData).setBreakOnStart(true);
			}
		} else {
			currentVMState = VM_STARTED;
			dispatchEvent(VM_STARTED, null);
		}
	}

	public void detach(Context cx) {
		if (!isTerminated()) {
			terminate();
		}

		Debugger dbg = cx.getDebugger();

		if (dbg instanceof JsDebugger) {
			cx.setDebugger(null, null);
		}

		isAttached = false;

		if (jsDebugger != null) {
			jsDebugger.dispose();
			jsDebugger = null;
		}

		if (factoryListener != null) {
			cx.getFactory().removeListener(factoryListener);
			factoryListener = null;
		}
	}

	public void dispose(Context cx) {
		detach(cx);

		vmListeners.clear();
		bpListeners.clear();
		deferredBreakPoints.clear();
	}

	public VMStackFrame[] getStackFrames() {
		if (isSuspended() && currentContextData != null) {
			VMStackFrame[] frames = new VMStackFrame[currentContextData.frameCount()];

			for (int i = 0; i < frames.length; i++) {
				frames[i] = currentContextData.getFrame(i);
			}

			return frames;
		}

		return NO_FRAMES;
	}

	public VMStackFrame getStackFrame(int index) {
		if (index < 0 || !isSuspended()) {
			return null;
		}
		if (currentContextData != null && index < currentContextData.frameCount()) {
			return currentContextData.getFrame(index);
		}

		return null;
	}

	public VMValue evaluate(String expression) {
		if (isSuspended() && expression != null && currentContextData != null
				&& currentContextData.getCurrentFrame() != null) {
			synchronized (monitor) {
				evalRequest = expression;
				monitor.notify();

				do {
					try {
						monitor.wait();
					} catch (InterruptedException exc) {
						Thread.currentThread().interrupt();
						return null;
					}

					if (isTerminated()) {
						break;
					}

				} while (evalRequest != null);

				return (VMValue) evalResult;
			}
		}
		return null;
	}

	public VMVariable[] getVariables() {
		if (isSuspended() && currentContextData != null) {
			VMStackFrame frame = currentContextData.getCurrentFrame();

			if (frame != null) {
				return frame.getVariables();
			}
		}
		return NO_VARS;
	}

	int currentState() {
		return currentVMState;
	}

	void interrupt(VMContextData contextData, int interruptState) {
		synchronized (monitor) {
			try {
				suspended = true;
				currentVMState = interruptState;
				evalRequest = null;

				if (isStartSuspended) {
					// This is the first started suspended event
					isStartSuspended = false;
					dispatchEvent(VM_STARTED, contextData);
				} else {
					dispatchEvent(currentVMState, contextData);
				}

				while (true) {
					try {
						monitor.wait();
					} catch (InterruptedException exc) {
						Thread.currentThread().interrupt();
						return;
					}

					if (evalRequest != null) {
						evalResult = null;

						// TODO ensure current contextData

						try {
							evalResult = contextData.getCurrentFrame().evaluate(evalRequest);
						} finally {
							evalRequest = null;
							monitor.notify();
						}

						continue;
					}

					break;
				}
			} finally {
				suspended = false;
			}

			if (isTerminated()) {
				return;
			}
		}

		dispatchEvent(VM_RESUMED, contextData);

		// TODO extract public interface to access step info

		switch (currentVMState) {
		case VM_STEP_OVER:
			((JsContextData) contextData).breakNextLine(contextData.frameCount());
			break;
		case VM_STEP_INTO:
			((JsContextData) contextData).breakNextLine(-1);
			break;
		case VM_STEP_OUT:
			if (contextData.frameCount() > 1) {
				((JsContextData) contextData).breakNextLine(contextData.frameCount() - 1);
			}
			break;
		}

		// synchronized ( monitor )
		// {
		// monitor.notifyAll( );
		// }
	}

	public void suspend() {
		synchronized (monitor) {
			currentVMState = VM_SUSPEND;
		}
	}

	public void resume() {
		synchronized (monitor) {
			currentVMState = VM_RESUME;
			monitor.notifyAll();
		}
	}

	public void resume(VMBreakPoint bp, boolean forceBreak) {

	}

	public void step() {
		synchronized (monitor) {
			currentVMState = VM_STEP_OVER;
			monitor.notifyAll();
		}
	}

	public void stepInto() {
		synchronized (monitor) {
			currentVMState = VM_STEP_INTO;
			monitor.notifyAll();
		}
	}

	public void stepOut() {
		synchronized (monitor) {
			currentVMState = VM_STEP_OUT;
			monitor.notifyAll();
		}
	}

	public void terminate() {
		synchronized (monitor) {
			currentVMState = VM_TERMINATED;
			monitor.notifyAll();

			dispatchEvent(VM_TERMINATED, null);
		}
	}

	public boolean isSuspended() {
		return suspended && (currentVMState != VM_TERMINATED);
	}

	public boolean isTerminated() {
		return currentVMState == VM_TERMINATED;
	}

	public void addVMListener(VMListener listener) {
		if (!vmListeners.contains(listener)) {
			vmListeners.add(listener);
		}
	}

	public void removeVMListener(VMListener listener) {
		vmListeners.remove(listener);
	}

	public void addBreakPointListener(VMBreakPointListener listener) {
		if (!bpListeners.contains(listener)) {
			bpListeners.add(listener);
		}
	}

	public void removeBreakPointListener(VMBreakPointListener listener) {
		bpListeners.remove(listener);
	}

	public void addBreakPoint(VMBreakPoint bp) {
		if (isAttached) {
			notifyBreakPointChange(bp, ADD);
		} else if (!deferredBreakPoints.contains(bp)) {
			deferredBreakPoints.add(bp);
		}
	}

	public void removeBreakPoint(VMBreakPoint bp) {
		if (isAttached) {
			notifyBreakPointChange(bp, REMOVE);
		} else if (deferredBreakPoints.contains(bp)) {
			deferredBreakPoints.remove(bp);
		}
	}

	public void modifyBreakPoint(VMBreakPoint bp) {
		if (isAttached) {
			notifyBreakPointChange(bp, CHANGE);
		} else if (deferredBreakPoints.contains(bp)) {
			// TODO
		}
	}

	public void clearBreakPoints() {
		if (isAttached) {
			notifyBreakPointChange(null, CLEAR);
		} else {
			deferredBreakPoints.clear();
		}
	}

	private void addDeferredBreakPoints() {
		for (int i = 0; i < deferredBreakPoints.size(); i++) {
			notifyBreakPointChange((VMBreakPoint) deferredBreakPoints.get(i), ADD);
		}

		deferredBreakPoints.clear();
	}

	private void notifyBreakPointChange(VMBreakPoint bp, int command) {
		switch (command) {
		case ADD:
			for (int i = 0; i < bpListeners.size(); i++) {
				VMBreakPointListener bpListener = (VMBreakPointListener) bpListeners.get(i);
				bpListener.breakPointAdded(bp);
			}
			break;
		case REMOVE:
			for (int i = 0; i < bpListeners.size(); i++) {
				VMBreakPointListener bpListener = (VMBreakPointListener) bpListeners.get(i);
				bpListener.breakPointRemoved(bp);
			}
			break;
		case CHANGE:
			for (int i = 0; i < bpListeners.size(); i++) {
				VMBreakPointListener bpListener = (VMBreakPointListener) bpListeners.get(i);
				bpListener.breakPointChanged(bp);
			}
			break;
		case CLEAR:
			for (int i = 0; i < bpListeners.size(); i++) {
				VMBreakPointListener bpListener = (VMBreakPointListener) bpListeners.get(i);
				bpListener.breakPointCleared();
			}
			break;
		}
	}

	private void dispatchEvent(int event, VMContextData contextData) {
		currentContextData = contextData;

		for (int i = 0; i < vmListeners.size(); i++) {
			((VMListener) vmListeners.get(i)).handleEvent(event, contextData);
		}
	}

}
