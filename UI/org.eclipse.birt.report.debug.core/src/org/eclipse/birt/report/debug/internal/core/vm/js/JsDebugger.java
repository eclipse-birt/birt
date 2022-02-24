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

package org.eclipse.birt.report.debug.internal.core.vm.js;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.report.debug.internal.core.vm.ReportVM;
import org.eclipse.birt.report.debug.internal.core.vm.VMBreakPoint;
import org.eclipse.birt.report.debug.internal.core.vm.VMBreakPointListener;
import org.eclipse.birt.report.debug.internal.core.vm.VMDebugger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.debug.DebugFrame;
import org.mozilla.javascript.debug.DebuggableScript;
import org.mozilla.javascript.debug.Debugger;

/**
 * JsDebugger
 */
public class JsDebugger extends VMDebugger implements Debugger, VMBreakPointListener {

	// private static final Logger logger = Logger.getLogger(
	// JsDebugger.class.getName( ) );

	private Map scripts;
	private LinkedList cachedBreakPoints;
	private LinkedList transientBreakPoints;
	private boolean disposed;

	public JsDebugger(ReportVM vm) {
		super(vm);

		this.disposed = false;
		this.vm = vm;
		this.vm.addBreakPointListener(this);
		this.cachedBreakPoints = new LinkedList();
		this.transientBreakPoints = new LinkedList();

		this.scripts = new HashMap();
	}

	public DebugFrame getFrame(Context arg0, DebuggableScript arg1) {
		if (disposed) {
			return null;
		}

		JsFunctionSource src = (JsFunctionSource) scripts.get(arg1);

		if (src == null) {
			// this is not debuggable
			return null;
		}

		System.out.println(">>>> Frame Source Name: " + src.getSourceName()); //$NON-NLS-1$
		System.out.println(">>>> Frame Function Name: " + src.getFunctionName()); //$NON-NLS-1$

		return new JsDebugFrame(arg0, this, arg1, src);
	}

	public void handleCompilationDone(Context arg0, DebuggableScript arg1, String arg2) {
		if (disposed || !arg1.isTopLevel()) {
			return;
		}

		// check debuggable scripts
		if (arg1.getSourceName() == null || arg1.getSourceName().equals(ScriptExpression.defaultID)) {
			// skip default scripts, which is only used internally.

			// System.out.println( ">>>> Skipped Source: " //$NON-NLS-1$
			// + arg1.getSourceName( )
			// + "\r\n" //$NON-NLS-1$
			// + arg2
			// + "\r\n>>>> Skipped." ); //$NON-NLS-1$

			return;
		}

		registerTopLevelScripts(arg1, arg2);

		System.out.println(">>>> Compiled Source: " //$NON-NLS-1$
				+ arg1.getSourceName() + "\r\n" //$NON-NLS-1$
				+ arg2 + "\r\n>>>> end compilation."); //$NON-NLS-1$
	}

	private void registerTopLevelScripts(DebuggableScript script, String source) {
		List functions = new ArrayList();
		collectFunctions(script, functions);

		registerFunctions(source, functions);
	}

	private void registerFunctions(String source, List functions) {
		for (int i = 0; i < functions.size(); i++) {
			DebuggableScript function = (DebuggableScript) functions.get(i);

			int firstLineNumber = getMinNumber(function.getLineNumbers());

			if (firstLineNumber == -1) {
				continue;
			}

			String funcName = function.getFunctionName();

			if (funcName == null) {
				funcName = ""; //$NON-NLS-1$
			}

			scripts.put(function, new JsFunctionSource(function.getSourceName(), funcName, source, firstLineNumber));
		}
	}

	private int getMinNumber(int[] numbers) {
		if (numbers == null || numbers.length == 0) {
			return -1;
		}

		int min = numbers[0];

		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < min) {
				min = numbers[i];
			}
		}

		return min;
	}

	private void collectFunctions(DebuggableScript function, List holder) {
		holder.add(function);

		for (int i = 0; i < function.getFunctionCount(); i++) {
			collectFunctions(function.getFunction(i), holder);
		}
	}

	public void dispose() {
		disposed = true;
		vm.removeBreakPointListener(this);
		cachedBreakPoints.clear();
		transientBreakPoints.clear();
		scripts.clear();
	}

	int currentState() {
		return vmState();
	}

	boolean breakHitTest(JsContextData contextData) {
		boolean hit = checkBpHits(contextData, cachedBreakPoints);

		if (!hit) {
			hit = checkBpHits(contextData, transientBreakPoints);
		}

		return hit;
	}

	private boolean checkBpHits(JsContextData contextData, List bpList) {
		for (int i = 0; i < bpList.size(); i++) {
			JsLineBreakPoint jsbp = (JsLineBreakPoint) bpList.get(i);

			if (stringEqual(jsbp.name, contextData.currentName) && jsbp.lineNo == contextData.currentLineNo) {
				return true;
			}
		}
		return false;
	}

	private static boolean stringEqual(String s1, String s2) {
		return (s1 == null && s2 == null) || (s1 != null && s1.equals(s2));
		// return true;
	}

	void handleBreakHit(JsContextData contextData, int interruptState) {
		// always clear transient bp when interrupted
		transientBreakPoints.clear();

		vmInterrupt(contextData, interruptState);
	}

	private boolean interested(VMBreakPoint bp) {
		return bp instanceof JsLineBreakPoint;
	}

	public void breakPointAdded(VMBreakPoint bp) {
		if (interested(bp)) {
			if (bp instanceof JsTransientLineBreakPoint && !transientBreakPoints.contains(bp)) {
				transientBreakPoints.add(bp);
			} else if (!cachedBreakPoints.contains(bp)) {
				cachedBreakPoints.add(bp);
			}
		}
	}

	public void breakPointChanged(VMBreakPoint bp) {
		if (interested(bp)) {
			// TODO
		}
	}

	public void breakPointRemoved(VMBreakPoint bp) {
		if (interested(bp)) {
			if (bp instanceof JsTransientLineBreakPoint) {
				transientBreakPoints.remove(bp);
			} else {
				cachedBreakPoints.remove(bp);
			}
		}
	}

	public void breakPointCleared() {
		transientBreakPoints.clear();
		cachedBreakPoints.clear();
	}

}
