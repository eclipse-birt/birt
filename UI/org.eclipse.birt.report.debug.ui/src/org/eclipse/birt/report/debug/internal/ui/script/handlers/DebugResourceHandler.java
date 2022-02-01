/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.ui.script.handlers;

import org.eclipse.birt.report.debug.internal.ui.script.launcher.ScriptLaunchShortcut;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISources;

/**
 * Add the debug report action in the navigate view
 */

public class DebugResourceHandler extends AbstractHandler {

	protected IFile selectedFile;

	@Override
	public void setEnabled(Object evaluationContext) {

		this.selectedFile = null;

		if ((evaluationContext instanceof IEvaluationContext)) {
			IEvaluationContext context = (IEvaluationContext) evaluationContext;
			Object object = context.getVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME);
			if (object instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection) object;
				if (selection.size() == 1 && selection.getFirstElement() instanceof IFile) {
					this.selectedFile = (IFile) selection.getFirstElement();
				}
			}
		}

		this.setBaseEnabled(this.selectedFile != null);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		if (this.selectedFile != null) {
			String fileName = this.selectedFile.getLocation().toOSString();
			ILaunchConfiguration config = ScriptLaunchShortcut.findLaunchConfiguration(fileName,
					ScriptLaunchShortcut.getConfigurationType());
			if (config != null) {
				DebugUITools.launch(config, "debug");//$NON-NLS-1$
			}
		}

		return null;
	}

}
