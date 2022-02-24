/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Refresh action
 */
public class RefreshAction extends AbstractViewerAction {

	private static final String TEXT = Messages.getString("RefreshAction.text"); //$NON-NLS-1$

	/**
	 * Create a new refresh action with given selection and default text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * 
	 */
	public RefreshAction(TreeViewer sourceViewer) {
		this(sourceViewer, TEXT);
	}

	/**
	 * Create a new refresh action with given selection and text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * @param text           the text of the action
	 */
	public RefreshAction(TreeViewer sourceViewer, String text) {
		super(sourceViewer, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	public boolean isEnabled() {
		if (!(getSelection() instanceof TreeViewer)) {
			return false;
		}

		Object obj = getSelectedObjects().getFirstElement();
		if (obj instanceof DataSetHandle) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Refresh action >> Refresh " + getSourceViewer()); //$NON-NLS-1$
		}
		if (isEnabled()) {
			DataSetHandle handle = (DataSetHandle) getSelectedObjects().getFirstElement();
			try {
				DataSetUIUtil.updateColumnCacheAfterCleanRs(handle);
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
			}
			getSourceViewer().refresh(handle);
		}
	}
}
