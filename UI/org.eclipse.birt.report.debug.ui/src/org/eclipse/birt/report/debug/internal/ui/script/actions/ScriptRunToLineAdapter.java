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

package org.eclipse.birt.report.debug.internal.ui.script.actions;

import org.eclipse.birt.report.debug.internal.script.model.RunToLinebreakPoint;
import org.eclipse.birt.report.debug.internal.script.model.ScriptDebugElement;
import org.eclipse.birt.report.debug.internal.script.model.ScriptDebugTarget;
import org.eclipse.birt.report.debug.internal.script.model.ScriptLineBreakpoint;
import org.eclipse.birt.report.debug.internal.ui.script.editor.DebugJsInput;
import org.eclipse.birt.report.debug.internal.ui.script.util.ScriptDebugUtil;
import org.eclipse.birt.report.debug.ui.DebugUI;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ISuspendResume;
import org.eclipse.debug.ui.actions.IRunToLineTarget;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Supprot <CTRL+R>
 */

public class ScriptRunToLineAdapter implements IRunToLineTarget {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.ui.actions.IRunToLineTarget#canRunToLine(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection,
	 * org.eclipse.debug.core.model.ISuspendResume)
	 */
	@Override
	public boolean canRunToLine(IWorkbenchPart part, ISelection selection, ISuspendResume target) {
		if (target instanceof ScriptDebugElement) {
			IDebugElement element = (IDebugElement) target;
			ScriptDebugTarget adapter = (ScriptDebugTarget) element.getDebugTarget().getAdapter(IDebugTarget.class);
			return adapter != null;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.ui.actions.IRunToLineTarget#runToLine(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection,
	 * org.eclipse.debug.core.model.ISuspendResume)
	 */
	@Override
	public void runToLine(IWorkbenchPart part, ISelection selection, ISuspendResume target) throws CoreException {
		ITextEditor textEditor = getTextEditor(part);

		if (textEditor == null) {
		} else {
			IEditorInput input = textEditor.getEditorInput();

			if (input == null || !(input instanceof DebugJsInput)) {
				return;
			}

			DebugJsInput scriptInput = (DebugJsInput) input;
			IResource resource = (IResource) input.getAdapter(IResource.class);
			if (resource == null) {
				resource = ScriptDebugUtil.getDefaultResource();
			}

			final IDocument document = textEditor.getDocumentProvider().getDocument(input);
			if (document == null) {
			} else {
				final int[] validLine = new int[1];
				// final String[] typeName = new String[1];
				final int[] lineNumber = new int[1];
				final ITextSelection textSelection = (ITextSelection) selection;
				Runnable r = new Runnable() {

					@Override
					public void run() {
						lineNumber[0] = textSelection.getStartLine() + 1;
					}
				};
				BusyIndicator.showWhile(DebugUI.getStandardDisplay(), r);
				// TODO add the validLine to adjust if the line is validLine
				validLine[0] = lineNumber[0];
				if (validLine[0] == lineNumber[0]) {
					ScriptLineBreakpoint point = new RunToLinebreakPoint(resource,
							scriptInput.getFile().getAbsolutePath(), scriptInput.getId(), lineNumber[0]);
					point.setType(ScriptLineBreakpoint.RUNTOLINE);
					if (target instanceof IAdaptable) {
						ScriptDebugTarget debugTarget = (ScriptDebugTarget) ((IAdaptable) target)
								.getAdapter(IDebugTarget.class);
						if (debugTarget != null) {
							debugTarget.breakpointAdded(point);
							debugTarget.resume();
						}
					}
				}
			}

		}
	}

	/**
	 * @param part
	 * @return
	 */
	protected ITextEditor getTextEditor(IWorkbenchPart part) {
		if (part instanceof ITextEditor) {
			return (ITextEditor) part;
		}
		return (ITextEditor) part.getAdapter(ITextEditor.class);
	}

}
