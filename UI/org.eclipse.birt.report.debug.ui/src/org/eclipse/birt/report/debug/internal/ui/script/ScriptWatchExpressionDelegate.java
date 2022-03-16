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

package org.eclipse.birt.report.debug.internal.ui.script;

import org.eclipse.birt.report.debug.internal.script.model.ScriptDebugTarget;
import org.eclipse.birt.report.debug.internal.script.model.ScriptStackFrame;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IWatchExpressionDelegate;
import org.eclipse.debug.core.model.IWatchExpressionListener;
import org.eclipse.debug.core.model.IWatchExpressionResult;

/**
 * ScriptWatchExpressionDelegate
 */
public class ScriptWatchExpressionDelegate implements IWatchExpressionDelegate {

	private IWatchExpressionListener fListener;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.core.model.IWatchExpressionDelegate#evaluateExpression(java
	 * .lang.String, org.eclipse.debug.core.model.IDebugElement,
	 * org.eclipse.debug.core.model.IWatchExpressionListener)
	 */
	@Override
	public void evaluateExpression(String expression, IDebugElement context, IWatchExpressionListener listener) {
		fListener = listener;
		// find a stack frame context if possible.
		IStackFrame frame = null;
		if (context instanceof IStackFrame) {
			frame = (IStackFrame) context;
		} else if (context instanceof IThread) {
			try {
				frame = ((IThread) context).getTopStackFrame();
			} catch (DebugException e) {
			}
		}

		final IStackFrame tempFrame = frame;
		final String tempStr = expression;
		IWatchExpressionResult watchResult = new IWatchExpressionResult() {

			@Override
			public IValue getValue() {
				if (tempFrame != null) {
					return ((ScriptDebugTarget) tempFrame.getDebugTarget()).evaluate((ScriptStackFrame) tempFrame,
							tempStr);
				}
				return null;
			}

			@Override
			public boolean hasErrors() {
				return getValue() == null;
			}

			@Override
			public String[] getErrorMessages() {
				return new String[] { "(Watch expressions not supported)" //$NON-NLS-1$
				};
			}

			@Override
			public String getExpressionText() {
				return tempStr;
			}

			@Override
			public DebugException getException() {
				return null;
			}
		};

		fListener.watchEvaluationFinished(watchResult);
	}
}
