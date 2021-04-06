/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.LinkedHashMap;

import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;

public class CompoundTask extends SimpleTask {

	private transient LinkedHashMap<String, ISubtaskSheet> subtasks = new LinkedHashMap<String, ISubtaskSheet>();
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