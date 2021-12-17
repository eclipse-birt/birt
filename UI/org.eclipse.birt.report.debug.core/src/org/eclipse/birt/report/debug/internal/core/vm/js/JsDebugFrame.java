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

package org.eclipse.birt.report.debug.internal.core.vm.js;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.debug.internal.core.vm.VMConstants;
import org.eclipse.birt.report.debug.internal.core.vm.VMStackFrame;
import org.eclipse.birt.report.debug.internal.core.vm.VMValue;
import org.eclipse.birt.report.debug.internal.core.vm.VMVariable;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.debug.DebugFrame;
import org.mozilla.javascript.debug.DebuggableScript;
import org.mozilla.javascript.debug.Debugger;

/**
 * JsDebugFrame
 */
public class JsDebugFrame implements DebugFrame, VMStackFrame, VMConstants {

	// private static final Logger logger = Logger.getLogger(
	// JsDebugFrame.class.getName( ) );

	private JsDebugger debugger;
	private DebuggableScript script;
	private JsFunctionSource src;
	private Scriptable scope;
	private Scriptable thisObj;
	private Context cx;
	private int lineNo;

	public JsDebugFrame(Context cx, JsDebugger debugger, DebuggableScript script, JsFunctionSource src) {
		this.cx = cx;
		this.debugger = debugger;
		this.script = script;
		this.src = src;
	}

	public DebuggableScript getScript() {
		return script;
	}

	public String getName() {
		return script.getSourceName();
	}

	public int getLineNumber() {
		return lineNo;
	}

	public JsFunctionSource getSource() {
		return src;
	}

	public Scriptable getScope() {
		return scope;
	}

	public Scriptable getThisObject() {
		return thisObj;
	}

	private JsContextData contextData(Context cx) {
		return (JsContextData) cx.getDebuggerContextData();
	}

	public void onDebuggerStatement(Context arg0) {
	}

	public void onEnter(Context arg0, Scriptable arg1, Scriptable arg2, Object[] arg3) {
		int currentState = debugger.currentState();

		if (currentState == VM_TERMINATED) {
			return;
		}

		this.cx = arg0;
		this.scope = arg1;
		this.thisObj = arg2;
		this.lineNo = src.getStartLineNumber();

		System.out.println(">>>> Enter script. " + lineNo); //$NON-NLS-1$

		JsContextData cxData = contextData(arg0);

		cxData.pushFrame(this);

		if (cxData.breakOnStart) {
			cxData.breakOnStart = false;
			debugger.handleBreakHit(cxData, VM_SUSPENDED_CLIENT);
		}
	}

	public void onExceptionThrown(Context arg0, Throwable arg1) {
		System.out.println(">>>> Debugger exception occured:"); //$NON-NLS-1$
		arg1.printStackTrace();
	}

	public void onExit(Context arg0, boolean arg1, Object arg2) {
		int currentState = debugger.currentState();

		if (currentState == VM_TERMINATED) {
			return;
		}

		cx = arg0;
		;

		JsContextData contextData = contextData(arg0);

		contextData.popFrame();

		System.out.println(">>>> Exit script."); //$NON-NLS-1$
	}

	public void onLineChange(Context arg0, int arg1) {
		int currentState = debugger.currentState();

		if (currentState == VM_TERMINATED) {
			return;
		}

		cx = arg0;
		lineNo = arg1;

		System.out.println(">>>> Line changed to: " + lineNo); //$NON-NLS-1$

		handleBreak(arg0, currentState);
	}

	private void handleBreak(Context cx, int currentState) {
		JsContextData contextData = contextData(cx);

		contextData.currentLineNo = lineNo;
		contextData.currentName = script.getSourceName();

		boolean stepped = false;

		// handle stepping first
		if (contextData.breakNextLine) {
			if (contextData.targetFrmaeDepth >= 0) {
				stepped = contextData.frameCount() <= contextData.targetFrmaeDepth;
			} else {
				stepped = true;
			}

			if (stepped) {
				cleanSteppingStatus(contextData);

				if (currentState == VM_STEP_INTO) {
					debugger.handleBreakHit(contextData, VM_SUSPENDED_STEP_INTO);
				} else if (currentState == VM_STEP_OVER) {
					debugger.handleBreakHit(contextData, VM_SUSPENDED_STEP_OVER);
				} else if (currentState == VM_STEP_OUT) {
					debugger.handleBreakHit(contextData, VM_SUSPENDED_STEP_OUT);
				}
			}
		}

		if (!stepped) {
			if (currentState == VM_SUSPEND) {
				cleanSteppingStatus(contextData);
				debugger.handleBreakHit(contextData, VM_SUSPENDED_CLIENT);
			} else if (debugger.breakHitTest(contextData)) {
				cleanSteppingStatus(contextData);
				debugger.handleBreakHit(contextData, VM_SUSPENDED_BREAKPOINT);
			}
		}

	}

	private void cleanSteppingStatus(JsContextData contextData) {
		contextData.breakNextLine = false;
		contextData.targetFrmaeDepth = -1;
	}

	public synchronized VMValue evaluate(String expression) {
		int currentState = debugger.currentState();

		if (currentState == VM_TERMINATED) {
			return null;
		}

		JsValue result = null;
		Debugger oldDebugger = cx.getDebugger();
		Object oldContextData = cx.getDebuggerContextData();
		int oldLevel = cx.getOptimizationLevel();

		cx.setDebugger(null, null);
		cx.setOptimizationLevel(-1);
		cx.setGeneratingDebug(false);

		try {
			Callable script = (Callable) cx.compileString(expression, EVALUATOR_LITERAL, 0, null);
			Object val = script.call(cx, scope, thisObj, ScriptRuntime.emptyArgs);

			if (val == Undefined.instance) {
				result = new JsValue(UNDEFINED_LITERAL, UNDEFINED_TYPE);
			} else {
				result = new JsValue(val);
			}
		} catch (Exception ex) {
			result = new JsValue(ex.getMessage(), EXCEPTION_TYPE);
		} finally {
			cx.setGeneratingDebug(true);
			cx.setOptimizationLevel(oldLevel);
			cx.setDebugger(oldDebugger, oldContextData);
		}

		return result;
	}

	public VMVariable[] getVariables() {
		// TODO ensure current context
		return (VMVariable[]) Context.call(new ContextAction() {

			public Object run(Context arg0) {
				try {
					return getVariablesImpl(arg0);
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));

					return new VMVariable[] { new JsVariable(sw.toString(), ERROR_LITERAL, EXCEPTION_TYPE) };
				}
			}

		});
	}

	private VMVariable[] getVariablesImpl(Context cx) {
		List vars = new ArrayList();

		if (thisObj != null) {
			vars.add(new JsVariable(thisObj, "this")); //$NON-NLS-1$
		}

		if (scope != null && script != null) {
			for (int i = 0; i < script.getParamAndVarCount(); i++) {
				String name = script.getParamOrVarName(i);

				Object val = ScriptableObject.getProperty(scope, name);

				if (JsValue.isValidJsValue(val)) {
					vars.add(new JsVariable(val, name));
				}
			}
		}

		if (vars.size() == 0) {
			return NO_VARS;
		}

		Collections.sort(vars);

		return (VMVariable[]) vars.toArray(new VMVariable[vars.size()]);
	}
}
