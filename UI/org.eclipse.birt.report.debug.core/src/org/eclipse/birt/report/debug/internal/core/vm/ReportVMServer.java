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

package org.eclipse.birt.report.debug.internal.core.vm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.report.debug.core.i18n.Messages;
import org.eclipse.birt.report.debug.internal.core.vm.rm.RMStackFrame;
import org.eclipse.birt.report.debug.internal.core.vm.rm.RMValue;
import org.eclipse.birt.report.debug.internal.core.vm.rm.RMVariable;
import org.mozilla.javascript.Context;

/**
 * ReportVMServer
 */
public class ReportVMServer implements VMConstants, VMListener {

	private static final Logger logger = Logger.getLogger(ReportVMServer.class.getName());

	private ServerSocket serverSocket;

	private ObjectInputStream clientRequestReader;
	private ObjectOutputStream clientRequestWriter;
	private ObjectOutputStream clientEventWriter;

	private Thread requestDispatchThread;

	private ReportVM vm;

	private boolean isShutdown;

	private List vmListeners;

	private Map id2val, val2id;

	private long counter;

	public ReportVMServer() {
		vm = new ReportVM();

		vm.addVMListener(this);

		vmListeners = new ArrayList();
		id2val = new HashMap();
		val2id = new HashMap();
	}

	public void addVMListener(VMListener listener) {
		if (!vmListeners.contains(listener)) {
			vmListeners.add(listener);
		}
	}

	public void removeVMListener(VMListener listener) {
		vmListeners.remove(listener);
	}

	public void start(int listenPort, Context cx) throws VMException {
		try {
//			serverSocket = new ServerSocket( listenPort,
//					50,
//					InetAddress.getLocalHost( ) );
			serverSocket = new ServerSocket(listenPort, 50, null);

			Socket clientRequestSocket = serverSocket.accept();
			clientRequestReader = new ObjectInputStream(clientRequestSocket.getInputStream());
			clientRequestWriter = new ObjectOutputStream(clientRequestSocket.getOutputStream());

			Socket clientEventSocket = serverSocket.accept();
			clientEventWriter = new ObjectOutputStream(clientEventSocket.getOutputStream());

			logger.info(Messages.getString("ReportVMServer.ClientAccpted")); //$NON-NLS-1$

			isShutdown = false;

			vm.attach(cx, true);

			startRequestDispatch();
		} catch (IOException e) {
			throw new VMException(e);
		}
	}

	public void shutdown(Context cx) {
		vm.detach(cx);

		terminate();
	}

	public void dispose(Context cx) {
		vm.dispose(cx);

		terminate();
	}

	private void terminate() {
		if (isShutdown) {
			return;
		}

		isShutdown = true;

		try {
			clientRequestReader.close();
			clientRequestWriter.close();
			clientEventWriter.close();
		} catch (IOException e) {
			logger.warning(Messages.getString("ReportVMServer.ClientDisconnected")); //$NON-NLS-1$
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (requestDispatchThread.isAlive()) {
			requestDispatchThread.interrupt();
		}

		counter = 0;
		id2val.clear();
		val2id.clear();

		requestDispatchThread = null;
		clientRequestReader = null;
		clientRequestWriter = null;
		clientEventWriter = null;
		serverSocket = null;

		logger.info(Messages.getString("ReportVMServer.ServerShutDown")); //$NON-NLS-1$
	}

	private void startRequestDispatch() {
		requestDispatchThread = new Thread(new Runnable() {

			public void run() {
				logger.info(Messages.getString("ReportVMServer.EnterRequestDispatching")); //$NON-NLS-1$

				while (!isShutdown) {
					try {
						int op = clientRequestReader.readInt();

						logger.info(Messages.getString("ReportVMServer.ReceivedRequest") + op); //$NON-NLS-1$

						Object arg = null;

						if ((op & OP_ARGUMENT_MASK) != 0) {
							arg = clientRequestReader.readObject();
						}

						Object rt = handleRequest(op, arg);

						if ((op & OP_RETURN_VALUE_MASK) != 0) {
							logger.info(Messages.getString("ReportVMServer.SendBackResponse")); //$NON-NLS-1$

							clientRequestWriter.writeObject(rt);
							clientRequestWriter.flush();
						}

						Thread.sleep(50);
					} catch (IOException ie) {
						logger.warning(Messages.getString("ReportVMServer.ClientDisconnected")); //$NON-NLS-1$
						break;
					} catch (InterruptedException ie) {
						logger.warning(Messages.getString("ReportVMServer.ServerShuttingDown")); //$NON-NLS-1$
						break;
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
				}
			}
		}, "Server Request Dispatcher"); //$NON-NLS-1$

		requestDispatchThread.start();
	}

	private Object handleRequest(int op, Object arg) {
		Object rt = null;

		switch (op) {
		case OP_RESUME:
			vm.resume();
			break;
		case OP_SUSPEND:
			vm.suspend();
			break;
		case OP_STEP_OVER:
			vm.step();
			break;
		case OP_STEP_INTO:
			vm.stepInto();
			break;
		case OP_STEP_OUT:
			vm.stepOut();
			break;
		case OP_TERMINATE:
			vm.terminate();
			break;
		case OP_QUERY_SUSPENDED:
			rt = Boolean.valueOf(vm.isSuspended());
			break;
		case OP_QUERY_TERMINATED:
			rt = Boolean.valueOf(vm.isTerminated());
			break;
		case OP_ADD_BREAKPOINT:
			op_breakpoint(ADD, (VMBreakPoint) arg);
			break;
		case OP_MOD_BREAKPOINT:
			op_breakpoint(CHANGE, (VMBreakPoint) arg);
			break;
		case OP_REMOVE_BREAKPOINT:
			op_breakpoint(REMOVE, (VMBreakPoint) arg);
			break;
		case OP_CLEAR_BREAKPOINTS:
			vm.clearBreakPoints();
			break;
		case OP_GET_VARIABLES:
			rt = wrapVariables(vm.getVariables());
			break;
		case OP_GET_STACKFRAMES:
			rt = wrapStackFrames(vm.getStackFrames());
			break;
		case OP_GET_STACKFRAME:
			rt = wrapStackFrame(vm.getStackFrame(((Integer) arg).intValue()));
			break;
		case OP_EVALUATE:
			rt = wrapValue(vm.evaluate((String) arg), false);
			break;
		case OP_GET_MEMBERS:
			rt = wrapMembers(((Long) arg).longValue());
			break;

		}

		return rt;
	}

	private void op_breakpoint(int op, VMBreakPoint bp) {
		if (bp != null) {
			switch (op) {
			case ADD:
				vm.addBreakPoint(bp);
				break;
			case CHANGE:
				vm.modifyBreakPoint(bp);
				break;
			case REMOVE:
				vm.removeBreakPoint(bp);
				break;
			}
		}
	}

	private VMStackFrame[] wrapStackFrames(VMStackFrame[] frames) {
		if (frames != null && frames.length > 0) {
			VMStackFrame[] rfs = new RMStackFrame[frames.length];

			for (int i = 0; i < frames.length; i++) {
				rfs[i] = wrapStackFrame(frames[i]);
			}

			return rfs;
		}

		return NO_FRAMES;
	}

	private VMStackFrame wrapStackFrame(VMStackFrame frame) {
		if (frame != null) {
			VMVariable[] vars = frame.getVariables();

			VMVariable[] rvars = NO_VARS;

			if (vars != null && vars.length > 0) {
				rvars = wrapVariables(vars);
			}

			return new RMStackFrame(frame.getName(), rvars, frame.getLineNumber());
		}

		return null;
	}

	private VMVariable[] wrapVariables(VMVariable[] vars) {
		if (vars != null && vars.length > 0) {
			VMVariable[] rvars = new RMVariable[vars.length];

			for (int i = 0; i < vars.length; i++) {
				rvars[i] = wrapVariable(vars[i]);
			}

			return rvars;
		}

		return NO_VARS;
	}

	private VMVariable wrapVariable(VMVariable var) {
		if (var != null) {
			return new RMVariable(wrapValue(var.getValue(), false), var.getName(), var.getTypeName());
		}

		return null;
	}

	private synchronized VMValue wrapValue(VMValue val, boolean wrapMember) {
		if (val != null) {
			Object oid = val2id.get(val);

			if (oid != null) {
				return new RMValue(((Long) oid).longValue(), val.getValueString(), val.getTypeName(),
						wrapMember ? wrapMembers(val) : null);
			} else {
				Long vid = Long.valueOf(++counter);
				id2val.put(vid, val);
				val2id.put(val, vid);

				return new RMValue(vid.longValue(), val.getValueString(), val.getTypeName(),
						wrapMember ? wrapMembers(val) : null);
			}
		}

		return null;
	}

	private VMVariable[] wrapMembers(VMValue val) {
		if (val != null) {
			VMVariable[] nms = NO_CHILD;

			// wrap first layer member only
			VMVariable[] members = val.getMembers();

			if (members != null && members.length > 0) {
				nms = new RMVariable[members.length];

				for (int i = 0; i < members.length; i++) {
					nms[i] = new RMVariable(wrapValue(members[i].getValue(), false), members[i].getName(),
							members[i].getTypeName());
				}
			}

			return nms;
		}

		return null;
	}

	private synchronized VMVariable[] wrapMembers(long vid) {
		VMValue val = (VMValue) id2val.get(Long.valueOf(vid));

		if (val != null) {
			return wrapMembers(val);
		}

		return NO_CHILD;
	}

	public void handleEvent(int eventCode, VMContextData context) {
		if (isShutdown) {
			return;
		}

		// notify client first
		synchronized (serverSocket) {
			try {
				logger.info(Messages.getString("ReportVMServer.SendVMEvent") //$NON-NLS-1$
						+ eventCode + "|" //$NON-NLS-1$
						+ EVENT_NAMES[eventCode]);

				clientEventWriter.writeInt(eventCode);
				clientEventWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// notify server listener
		for (int i = 0; i < vmListeners.size(); i++) {
			((VMListener) vmListeners.get(i)).handleEvent(eventCode, context);
		}

	}
}
