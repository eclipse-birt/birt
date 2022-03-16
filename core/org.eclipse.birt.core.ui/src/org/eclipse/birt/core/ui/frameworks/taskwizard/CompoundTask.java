/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.LinkedHashMap;

import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;

public class CompoundTask extends SimpleTask {

	private transient LinkedHashMap<String, ISubtaskSheet> subtasks = new LinkedHashMap<>();
	protected transient ISubtaskSheet sCurrentTaskSheet = null;
	private transient String sCurrentSubtask = ""; //$NON-NLS-1$

	public CompoundTask(String title) {
		super(title);
	}

	public void addSubtask(String sSubtaskPath, ISubtaskSheet subtask) {
		subtasks.put(sSubtaskPath, subtask);
	}

	public void removeSubtask(String sSubtaskPath) {
		// If the current subtask is being removed...first switch to the first
		// available subtask and THEN remove the subtask
		if (subtasks.containsKey(sSubtaskPath) && sCurrentSubtask.equals(sSubtaskPath)) {
			switchTo(subtasks.keySet().toArray()[0].toString());
		}
		subtasks.remove(sSubtaskPath);
	}

	public void switchTo(String sSubtaskPath) {
		if (getCurrentSubtask() != null) {
			getCurrentSubtask().onHide();
		}
		if (containSubtask(sSubtaskPath)) {
			sCurrentTaskSheet = getSubtask(sSubtaskPath);
			this.sCurrentSubtask = sSubtaskPath;
		}
		getCurrentSubtask().onShow(context, container);
	}

	protected boolean containSubtask(String sSubtaskPath) {
		return subtasks.containsKey(sSubtaskPath);
	}

	protected ISubtaskSheet getSubtask(String sSubtaskPath) {
		if (!subtasks.containsKey(sSubtaskPath)) {
			return null;
		}
		return subtasks.get(sSubtaskPath);
	}

	protected ISubtaskSheet getCurrentSubtask() {
		return sCurrentTaskSheet;
	}

	@Override
	public void dispose() {
		super.dispose();
		// Hide current subtask
		if (sCurrentTaskSheet != null) {
			sCurrentTaskSheet.onHide();
		}
		sCurrentTaskSheet = null;

		// Dispose all subtasks
		for (ISubtaskSheet subtask : subtasks.values()) {
			subtask.dispose();
		}
		subtasks.clear();
	}
}
