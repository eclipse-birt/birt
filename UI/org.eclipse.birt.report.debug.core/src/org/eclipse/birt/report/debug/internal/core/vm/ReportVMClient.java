/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  Others: See git history
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core.vm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.report.debug.core.i18n.Messages;
import org.eclipse.birt.report.debug.internal.core.vm.rm.RMClient;
import org.eclipse.birt.report.debug.internal.core.vm.rm.RMValue;
import org.eclipse.birt.report.debug.internal.core.vm.rm.RMVariable;

/**
 * ReportVMClient
 */
public class ReportVMClient extends RMClient implements VMConstants {

	private static final Logger logger = Logger.getLogger(ReportVMClient.class.getName());

	private Socket requestSocket;
	private ObjectOutputStream requestWriter;
	private ObjectInputStream requestReader;

	private Socket eventSocket;
	private ObjectInputStream eventReader;

	private Thread eventDispatchThread;

	private boolean isTerminated;
	private boolean isConnected;

	private List vmListeners;
	private List deferredBreakPoints;

	public ReportVMClient() {
		vmListeners = new ArrayList();
		deferredBreakPoints = new LinkedList();
	}

	public void connect(int listenPort) throws VMException {
		try {
			connect(null, listenPort);
		} catch (VMException e) {
			throw new VMException(e);
		}
	}

	public void connect(InetAddress host, int listenPort) throws VMException {
		try {
			// requestSocket = new Socket( host, listenPort );
			requestSocket = new Socket("localhost", listenPort);//$NON-NLS-1$
			requestWriter = new ObjectOutputStream(requestSocket.getOutputStream());
			requestReader = new ObjectInputStream(requestSocket.getInputStream());

			// eventSocket = new Socket( host, listenPort );
			eventSocket = new Socket("localhost", listenPort);//$NON-NLS-1$
			eventReader = new ObjectInputStream(eventSocket.getInputStream());

			isTerminated = false;
			isConnected = true;

			logger.info(Messages.getString("ReportVMClient.ServerConnected")); //$NON-NLS-1$

			addDeferredBreakPoints();

			startEventDispatch();
		} catch (Exception e) {
			throw new VMException(e);
		}
	}

	public void disconnect() {
		if (!isConnected) {
			return;
		}

		isConnected = false;

		try {
			requestReader.close();
			requestWriter.close();
			eventReader.close();
		} catch (IOException e) {
			logger.warning(Messages.getString("ReportVMClient.ServerShutDown")); //$NON-NLS-1$
		}

		try {
			requestSocket.close();
			eventSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (eventDispatchThread.isAlive()) {
			eventDispatchThread.interrupt();
		}

		eventDispatchThread = null;
		requestReader = null;
		requestWriter = null;
		eventReader = null;
		requestSocket = null;
		eventSocket = null;

		logger.info(Messages.getString("ReportVMClient.ClientDisconnected")); //$NON-NLS-1$
	}

	private void startEventDispatch() {
		eventDispatchThread = new Thread(new Runnable() {

			public void run() {
				logger.info(Messages.getString("ReportVMClient.EnterEventDispatching")); //$NON-NLS-1$

				while (!isTerminated) {
					try {
						int event = eventReader.readInt();

						logger.info(Messages.getString("ReportVMClient.ReceivedVMEvent") //$NON-NLS-1$
								+ event + "|" //$NON-NLS-1$
								+ EVENT_NAMES[event]);

						if (event == VM_TERMINATED) {
							isTerminated = true;
						}

						for (int i = 0; i < vmListeners.size(); i++) {
							((VMListener) vmListeners.get(i)).handleEvent(event, null);
						}

						Thread.sleep(50);
					} catch (InterruptedException ie) {
						isTerminated = true;
						break;
					} catch (IOException ie) {
						logger.warning(Messages.getString("ReportVMClient.ServerShuttingDown")); //$NON-NLS-1$
						isTerminated = true;
						break;
					} catch (Exception e) {
						isTerminated = true;
						e.printStackTrace();
						break;
					}
				}
			}
		}, "Client Event Dispatcher");//$NON-NLS-1$

		eventDispatchThread.start();
	}

	public VMStackFrame[] getStackFrames() throws VMException {
		Object rt = sendRequest(OP_GET_STACKFRAMES);

		if (rt instanceof VMStackFrame[]) {
			VMStackFrame[] fms = (VMStackFrame[]) rt;

			for (int i = 0; i < fms.length; i++) {
				hookVM(fms[i].getVariables());
			}

			return (VMStackFrame[]) rt;
		}

		return NO_FRAMES;
	}

	public VMStackFrame getStackFrame(int index) throws VMException {
		Object rt = sendRequest(OP_GET_STACKFRAME, Integer.valueOf(index));

		if (rt instanceof VMStackFrame) {
			hookVM(((VMStackFrame) rt).getVariables());

			return (VMStackFrame) rt;
		}

		return null;
	}

	public VMValue evaluate(String expression) throws VMException {
		Object rt = sendRequest(OP_EVALUATE, expression);

		if (rt instanceof VMValue) {
			((RMValue) rt).attach(this);

			return (VMValue) rt;
		}

		return null;
	}

	public VMVariable[] getVariables() throws VMException {
		Object rt = sendRequest(OP_GET_VARIABLES);

		if (rt instanceof VMVariable[]) {
			hookVM((VMVariable[]) rt);

			return (VMVariable[]) rt;
		}

		return NO_VARS;
	}

	protected VMVariable[] getMembers(long rid) throws VMException {
		Object rt = sendRequest(OP_GET_MEMBERS, Long.valueOf(rid));

		if (rt instanceof VMVariable[]) {
			hookVM((VMVariable[]) rt);

			return (VMVariable[]) rt;
		}

		return NO_VARS;
	}

	private void hookVM(VMVariable[] vars) {
		if (vars instanceof RMVariable[]) {
			RMVariable[] rvars = (RMVariable[]) vars;

			for (int i = 0; i < rvars.length; i++) {
				RMValue val = (RMValue) vars[i].getValue();

				if (val != null) {
					val.attach(this);

					hookVM(val.getLocalMembers());
				}
			}
		}
	}

	public void suspend() throws VMException {
		sendRequest(OP_SUSPEND);
	}

	public void resume() throws VMException {
		sendRequest(OP_RESUME);
	}

	public void step() throws VMException {
		sendRequest(OP_STEP_OVER);
	}

	public void stepInto() throws VMException {
		sendRequest(OP_STEP_INTO);
	}

	public void stepOut() throws VMException {
		sendRequest(OP_STEP_OUT);
	}

	public void terminate() throws VMException {
		sendRequest(OP_TERMINATE);
	}

	public boolean isSuspended() throws VMException {
		if (isTerminated || !isConnected) {
			return false;
		}

		Object rt = sendRequest(OP_QUERY_SUSPENDED);

		if (rt instanceof Boolean) {
			return ((Boolean) rt).booleanValue();
		}

		return false;
	}

	public boolean isTerminated() throws VMException {
		if (isTerminated || !isConnected) {
			return true;
		}

		Object rt = sendRequest(OP_QUERY_TERMINATED);

		if (rt instanceof Boolean) {
			boolean terminated = ((Boolean) rt).booleanValue();

			if (terminated) {
				isTerminated = true;
			}

			return terminated;
		}

		return true;
	}

	public void addVMListener(VMListener listener) {
		if (!vmListeners.contains(listener)) {
			vmListeners.add(listener);
		}
	}

	public void removeVMListener(VMListener listener) {
		vmListeners.remove(listener);
	}

	public void addBreakPoint(VMBreakPoint bp) throws VMException {
		if (!isTerminated && isConnected) {
			sendRequest(OP_ADD_BREAKPOINT, bp);
		} else {
			deferredBreakPoints.add(bp);
		}
	}

	public void removeBreakPoint(VMBreakPoint bp) throws VMException {
		if (!isTerminated && isConnected) {
			sendRequest(OP_REMOVE_BREAKPOINT, bp);
		} else {
			deferredBreakPoints.remove(bp);
		}
	}

	public void modifyBreakPoint(VMBreakPoint bp) throws VMException {
		if (!isTerminated && isConnected) {
			sendRequest(OP_MOD_BREAKPOINT, bp);
		} else {
			int idx = deferredBreakPoints.indexOf(bp);
			if (idx != -1) {
				deferredBreakPoints.set(idx, bp);
			}
		}
	}

	public void clearBreakPoints() throws VMException {
		if (!isTerminated && isConnected) {
			sendRequest(OP_CLEAR_BREAKPOINTS);
		} else {
			deferredBreakPoints.clear();
		}
	}

	private Object sendRequest(int op) throws VMException {
		return sendRequest(op, null);
	}

	private Object sendRequest(int op, Object arg) throws VMException {
		if (isTerminated || !isConnected) {
			logger.warning(Messages.getString("ReportVMClient.RequestIgnored.ServerDisconnected") //$NON-NLS-1$
					+ op);
			return null;
		}

		synchronized (requestSocket) {
			try {
				requestWriter.writeInt(op);

				if ((op & OP_ARGUMENT_MASK) != 0) {
					requestWriter.writeObject(arg);
				}
				requestWriter.flush();

				if ((op & OP_RETURN_VALUE_MASK) != 0) {
					// wait for reply
					return requestReader.readObject();
				}
			} catch (IOException se) {
				logger.warning(Messages.getString("ReportVMClient.RequestIgnored.ServerShuttingDown") //$NON-NLS-1$
						+ op);
			} catch (Exception e) {
				throw new VMException(e);
			}
		}

		return null;
	}

	private void addDeferredBreakPoints() throws VMException {
		for (int i = 0; i < deferredBreakPoints.size(); i++) {
			addBreakPoint((VMBreakPoint) deferredBreakPoints.get(i));
		}

		deferredBreakPoints.clear();
	}

}
