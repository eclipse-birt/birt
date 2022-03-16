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

package org.eclipse.birt.report.context;

import java.io.Serializable;

import org.eclipse.birt.report.engine.api.IEngineTask;

/**
 * Implement Serializable interface to support serialize from session object.
 *
 */
public class BaseTaskBean implements Serializable {

	private static final long serialVersionUID = -7555178979209848162L;
	private String taskid;

	// don't serialize engine task
	private transient IEngineTask task;

	/**
	 * Constructor with taskid and engine task
	 *
	 * @param taskid
	 * @param task
	 */
	public BaseTaskBean(String taskid, IEngineTask task) {
		this.taskid = taskid;
		this.task = task;
	}

	/**
	 * @return the task
	 */
	public IEngineTask getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(IEngineTask task) {
		this.task = task;
	}

	/**
	 * @return the taskid
	 */
	public String getTaskid() {
		return taskid;
	}

	/**
	 * @param taskid the taskid to set
	 */
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
}
