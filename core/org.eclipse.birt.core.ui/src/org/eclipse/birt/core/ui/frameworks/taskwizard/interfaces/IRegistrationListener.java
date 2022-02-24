/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces;

public interface IRegistrationListener {
	/**
	 * Notification method...called when a new task is successfully registered with
	 * the TasksManager.
	 * 
	 * @param sTaskID ID of the newly registered task.
	 */
	public void taskRegistered(String sTaskID);

	/**
	 * Notification method...called when a task is successfully deregistered from
	 * the TasksManager.
	 * 
	 * @param sTaskID ID of the deregistered task.
	 */
	public void taskDeregistered(String sTaskID);
}
