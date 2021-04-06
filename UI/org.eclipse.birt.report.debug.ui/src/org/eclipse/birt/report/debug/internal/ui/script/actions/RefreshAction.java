/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.ui.script.actions;

import org.eclipse.birt.report.debug.internal.ui.script.editor.DebugJsEditor;
import org.eclipse.birt.report.debug.internal.ui.script.editor.DebugJsInput;
import org.eclipse.birt.report.debug.internal.ui.script.outline.ScriptOutlinePage;
import org.eclipse.birt.report.debug.internal.ui.script.util.ScriptDebugUtil;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractViewAction;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * RefreshAction
 */
public class RefreshAction extends AbstractViewAction {
	private TreeViewer sourceViewer;

	/**
	 * Constructor
	 * 
	 * @param selectedObject
	 */
	public RefreshAction(Object selectedObject) {
		super(selectedObject);
	}

	/**
	 * Constructot
	 * 
	 * @param sourceViewer
	 * @param selectedObject
	 * @param text
	 */
	public RefreshAction(TreeViewer sourceViewer, Object selectedObject, String text) {
		super(selectedObject, text);
		this.sourceViewer = sourceViewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		DebugJsEditor editor = ScriptDebugUtil.getActiveJsEditor();
		if (editor == null) {
			return;
		}
		DebugJsInput input = (DebugJsInput) editor.getEditorInput();

		DebugJsInput newInput = new DebugJsInput(input.getFile(), input.getId());
		editor.setInput(newInput);

		sourceViewer.setInput(new Object[] { newInput.getModuleHandle() });
		sourceViewer.expandToLevel(ScriptOutlinePage.SHOW_LEVEL);
	}
}
