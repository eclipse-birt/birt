/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.build;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;

/**
 * 
 * this custom task implements retrail logic, it's a task container. Tasks in
 * this container will retry <code>retrial</code> times, the interval also can
 * be set following attributes can be set: <br>
 * <li><code>retrial</code> retry times, default is 1.
 * <li><code>interval</code>the interval between two execution, the units is ms,
 * default is 0.
 * <li><code>failOnError</code>Stop the build process if failed, default is
 * true;
 */
public class ReliableTask extends Task implements TaskContainer {

	/** Optional Vector holding the nested tasks */
	private Vector nestedTasks = new Vector();

	/**
	 * retry times
	 */
	protected int retrial = 1;

	/**
	 * fail property name
	 */
	protected String failProperty = "isfail";

	/**
	 * fail on error
	 */
	protected boolean failOnError = false;

	/**
	 * the interval
	 */
	protected int interval = 0;

	/**
	 * 
	 * @param retrial
	 */
	public void setRetrial(int retrial) {
		this.retrial = retrial;
	}

	/**
	 * 
	 * @param property
	 */
	public void setFailProperty(String property) {
		this.failProperty = property;
	}

	/**
	 * 
	 * @param interval
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * 
	 * @param f
	 */
	public void setFailOnError(boolean f) {
		this.failOnError = f;
	}

	/**
	 * Add a nested task to container.
	 * <p>
	 * 
	 * @param nestedTask Nested task to execute
	 *                   <p>
	 */
	public void addTask(Task nestedTask) {
		nestedTasks.addElement(nestedTask);
	}

	/**
	 * Execute all nestedTasks and retry.
	 * 
	 * @throws BuildException if one of the nested tasks fails.
	 */
	public void execute() throws BuildException {
		boolean pass = false;
		int count = 0;
		while (count < retrial && !pass) {

			try {
				for (Enumeration e = nestedTasks.elements(); e.hasMoreElements();) {
					Task nestedTask = (Task) e.nextElement();
					nestedTask.perform();
				}
				pass = true;
			} catch (BuildException e) {
				count++;
				pass = false;
				log("excute reliableTask failed one time!", Project.MSG_INFO);
				try {
					Thread.sleep(interval);
				} catch (Exception ee) {
					log(ee.getMessage(), Project.MSG_WARN);
				}
			}
		}

		if (!pass) {
			if (failOnError) {
				// throw exception to interupt the build process
				throw new BuildException("reliable task failed after retry " + retrial + " times!");
			} else {
				getProject().setProperty(this.failProperty, "true");
				// log error information and continue
				log("reliable task failed after retry " + retrial + " times!", Project.MSG_WARN);
			}
		}
	}
}
