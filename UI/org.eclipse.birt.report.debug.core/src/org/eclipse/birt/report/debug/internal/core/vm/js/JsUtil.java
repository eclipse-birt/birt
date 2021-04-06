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

package org.eclipse.birt.report.debug.internal.core.vm.js;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.debug.DebugFrame;
import org.mozilla.javascript.debug.DebuggableScript;
import org.mozilla.javascript.debug.Debugger;

/**
 * JsUtil
 */
public class JsUtil {

	private JsUtil() {
	}

	public static boolean checkBreakable(String source, int lineNumber) {
		return BreakableSourceChecker.check(source, lineNumber);
	}
}

class BreakableSourceChecker implements Debugger {

	static boolean check(String source, int lineNumber) {
		Context cx = Context.enter();

		Debugger oldDebugger = cx.getDebugger();
		Object oldContext = cx.getDebuggerContextData();
		boolean oldGenerate = cx.isGeneratingDebug();
		int oldLevel = cx.getOptimizationLevel();

		try {
			BreakableSourceChecker checker = new BreakableSourceChecker();
			checker.lineNumber = lineNumber + 2;

			cx.setDebugger(checker, null);
			cx.setGeneratingDebug(true);
			cx.setOptimizationLevel(-1);

			cx.compileString(addHeader(source), "<check>", 1, null); //$NON-NLS-1$

			return checker.breakable;
		} catch (Exception e) {
			return false;
		} finally {
			cx.setDebugger(oldDebugger, oldContext);
			cx.setGeneratingDebug(oldGenerate);
			cx.setOptimizationLevel(oldLevel);

			Context.exit();
		}
	}

	private static String addHeader(String source) {
		return "function addHeader(){\r\n" + source + "\r\n}"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	int lineNumber;
	boolean breakable;

	public DebugFrame getFrame(Context arg0, DebuggableScript arg1) {
		return null;
	}

	public void handleCompilationDone(Context arg0, DebuggableScript arg1, String arg2) {
		if (!arg1.isTopLevel()) {
			return;
		}

		breakable = false;

		checkBreakable(arg1);
	}

	private void checkBreakable(DebuggableScript script) {
		int[] nums = script.getLineNumbers();

		if (nums != null && nums.length > 0) {
			for (int i = 0; i < nums.length; i++) {
				if (nums[i] == lineNumber) {
					breakable = true;
					return;
				}
			}
		}

		for (int i = 0; i < script.getFunctionCount(); i++) {
			checkBreakable(script.getFunction(i));

			if (breakable) {
				return;
			}
		}
	}

}
