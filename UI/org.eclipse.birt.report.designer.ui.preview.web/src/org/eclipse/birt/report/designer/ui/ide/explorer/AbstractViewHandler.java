/*******************************************************************************
 * Copyright (c) 2021 Solme AB and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Claes Rosell  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.ui.ide.explorer;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISources;

/**
 *
 * Abstract handler used for all handlers in the explorer
 *
 */
abstract class AbstractViewHandler extends AbstractHandler {

	private IFile selectedFile;

	protected IFile getSelectedFile() {
		return this.selectedFile;
	}

	protected void updateSelectedFile(IStructuredSelection selection) {
		this.selectedFile = null;
		if (selection.size() == 1 && selection.getFirstElement() instanceof IFile) {
			this.selectedFile = (IFile) selection.getFirstElement();
		}
	}

	@Override
	public void setEnabled(Object evaluationContext) {

		if ((evaluationContext instanceof IEvaluationContext)) {
			IEvaluationContext context = (IEvaluationContext) evaluationContext;
			Object object = context.getVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME);
			if (object instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection) object;
				updateSelectedFile(selection);
			}
		}

		setBaseEnabled(this.selectedFile != null);
	}
}
