/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.ide.explorer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISources;

/**
 * Support the add the reprot project nature to the any project.
 */

public class SetupReportProjectHandler extends AbstractHandler {

	private List<IProject> validProjects = new ArrayList<>();

	@Override
	public void setEnabled(Object evaluationContext) {

		this.validProjects.clear();

		if ((evaluationContext instanceof IEvaluationContext)) {
			IEvaluationContext context = (IEvaluationContext) evaluationContext;
			Object object = context.getVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME);
			if (object instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection) object;

				for (Object selectedObject : selection.toList()) {
					if (selectedObject instanceof IProject) {
						IProject project = (IProject) selectedObject;
						try {
							if (!project.hasNature(ReportPlugin.NATURE_ID)) {
								this.validProjects.add(project);
							}
						} catch (CoreException e) {
							/* This project has some problem, but that needs to be handled elsewhere */
						}
					}
				}
			}
		}
		this.setBaseEnabled(!this.validProjects.isEmpty());
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		for (IProject project : this.validProjects) {
			try {
				IProjectDescription description = project.getDescription();

				List<String> newNatures = new ArrayList<>(Arrays.asList(description.getNatureIds()));
				newNatures.add(ReportPlugin.NATURE_ID);
				description.setNatureIds(newNatures.toArray(new String[newNatures.size()]));
				project.setDescription(description, new NullProgressMonitor());

			} catch (CoreException e) {
				ExceptionUtil.handle(e);
				throw new ExecutionException("Error executing command", e);
			}
		}
		return null;
	}

}
