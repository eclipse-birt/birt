/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.preview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class defines a task for live preview, user should use this class to
 * wrap their live preview transaction and add this task into current
 * ChartLivePreview thread. The <code>ChartLivePreviewThread</code> is
 * responsible to run all added task.
 * 
 * @since 2.5.2
 */

public class LivePreviewTask implements Runnable {
	private String name;

	private String description;

	private Map<String, Object> parameters = new HashMap<String, Object>();

	private List<LivePreviewTask> tasks = new ArrayList<LivePreviewTask>();

	/**
	 * Constructor.
	 */
	public LivePreviewTask() {
		// Default constructor.
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param description
	 */
	public LivePreviewTask(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * Returns name of this task.
	 * 
	 * @return name of task
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns description of this task.
	 * 
	 * @return description of task.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Add a sub task.
	 * 
	 * @param task
	 */
	public void addTask(LivePreviewTask task) {
		tasks.add(task);
	}

	/**
	 * Remove a sub task.
	 * 
	 * @param index
	 */
	public void removeTask(int index) {
		tasks.remove(index);
	}

	/**
	 * Remove a sub task.
	 * 
	 * @param task
	 */
	public void removeTask(LivePreviewTask task) {
		tasks.remove(task);
	}

	/**
	 * Returns all sub tasks.
	 * 
	 * @return all sub tasks.
	 */
	public LivePreviewTask[] getTasks() {
		return tasks.toArray(new LivePreviewTask[] {});
	}

	/**
	 * Clear all sub tasks.
	 */
	public void clear() {
		tasks.clear();
	}

	/**
	 * Saves parameter.
	 * 
	 * @param key
	 * @param parameter
	 */
	public void setParameter(String key, Object parameter) {
		parameters.put(key, parameter);
	}

	/**
	 * Returns stored parameter.
	 * 
	 * @param key
	 * @return stored parameter.
	 */
	public Object getParameter(String key) {
		return parameters.get(key);
	}

	/**
	 * Sets parameters map.
	 * 
	 * @param parameters
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Returns parameters map.
	 * 
	 * @return parameters map.
	 */
	public Map<String, Object> getParameters() {
		if (this.parameters.isEmpty()) {
			return null;
		}
		return this.parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		for (LivePreviewTask lpt : tasks) {
			lpt.run();
		}
	}
}
