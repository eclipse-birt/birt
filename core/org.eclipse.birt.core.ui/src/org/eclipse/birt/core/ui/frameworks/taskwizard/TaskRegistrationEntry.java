/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.core.ui.frameworks.taskwizard;

import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;

/**
 *
 */

public class TaskRegistrationEntry {

	private final String taskID;
	private final ITask classDefinition;
	private final int priority;

	public TaskRegistrationEntry(String taskID, ITask classDefinition, int priority) {
		this.taskID = taskID;
		this.classDefinition = classDefinition;
		this.priority = priority;
	}

	public String getTaskID() {
		return taskID;
	}

	public ITask getClassDefinition() {
		return classDefinition;
	}

	public int getPriority() {
		return priority;
	}
}
