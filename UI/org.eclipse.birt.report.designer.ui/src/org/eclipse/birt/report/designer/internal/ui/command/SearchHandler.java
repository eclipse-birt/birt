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

package org.eclipse.birt.report.designer.internal.ui.command;

import org.eclipse.birt.report.designer.internal.ui.views.actions.SearchAction;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISources;

/**
 *
 */

public class SearchHandler extends SelectionHandler {
	/**
	 * This is set elsewhere
	 */
	public static TreeViewer treeViewer = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);
		if (treeViewer == null) {
			return Boolean.FALSE;
		}
		SearchAction searchAction = new SearchAction(treeViewer);
		searchAction.run();
		return Boolean.TRUE;
	}

	@Override
	public void setEnabled(Object evaluationContext) {
		System.out.println("\n\nsetEnabled " + evaluationContext);
		boolean correctEditor = false;
		boolean correctPlugin = false;
		boolean correctSelection = false;
		if ((evaluationContext instanceof IEvaluationContext)) {
			IEvaluationContext context = (IEvaluationContext) evaluationContext;
			Object activeEditor = context.getVariable(ISources.ACTIVE_EDITOR_NAME);
			if (activeEditor != null && "org.eclipse.birt.report.designer.ui.editors.ReportEditorProxy"
					.equals(activeEditor.getClass().getName())) {
				correctEditor = true;

			}
			System.out.println("correctEditor = " + correctEditor);
			if (activeEditor instanceof IEditorPart) {
				IEditorPart editorPart = (IEditorPart) activeEditor;
				System.out.println("editorPart = " + editorPart);
				IEditorSite editorSite = editorPart.getEditorSite();
				System.out.println("editorSite = " + editorSite);
				String pluginId = editorSite.getPluginId();
				if ("org.eclipse.birt.report.designer.ui.ide".equals(pluginId)) {
					correctPlugin = true;
				}
				System.out.println("correctPlugin = " + correctPlugin);
			}
			Object activeCurrentSelection = context.getVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME);
			if (activeCurrentSelection instanceof TreeSelection) {
				System.out.println(ISources.ACTIVE_CURRENT_SELECTION_NAME + " is a TreeSelection");
				correctSelection = SearchAction.canSearch(activeCurrentSelection);
				System.out.println("correctSelection = " + correctSelection);
			}
		}
		setBaseEnabled(correctEditor && correctPlugin && correctSelection);
	}
}
