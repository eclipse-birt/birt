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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;

/**
 * this custom task implement for-each logic
 *
 */
public class ForEach extends Task implements TaskContainer {

	public final String TASK_STATUS_PASS = "pass"; //$NON-NLS-1$
	public final String TASK_STATUS_FAIL = "fail"; //$NON-NLS-1$
	public final String TASK_STATUS_FAIL_ON_DEPENDENCY = "failOnDependency"; //$NON-NLS-1$

	/**
	 * Holding the nested tasks
	 */

	protected Vector nestedTasks = new Vector();

	/**
	 * a project list reference.
	 */

	protected String refID;

	/**
	 * Failure property name;
	 */

	protected String failurePropertyName = "task.issuccess"; //$NON-NLS-1$

	/**
	 * A list of dynamic properties. dynamic properties define all parameters used
	 * in loop process
	 */

	protected ArrayList dynamicProperties = new ArrayList();

	/**
	 * fail on error
	 */

	protected boolean failOnError = true;

	/**
	 * File system path for the log files. Loggers(ERROR/INFO) with the forEach
	 * block will go into that path.
	 * <p>
	 * By default,we do not provide a specific logger for the projects.
	 */

	protected String loggerPath = null; // $NON-NLS-1$

	/**
	 * Set a project list reference, this task will be executed over the list of
	 * projects.
	 *
	 * @param id a project list reference.
	 */

	public void setIteratorId(String id) {
		this.refID = id;
	}

	public void setFailureProperty(String propertyName) {
		this.failurePropertyName = propertyName;
	}

	/**
	 * @param loggerPath The loggerPath to set.
	 */
	public void setLoggerPath(String loggerPath) {
		this.loggerPath = loggerPath;
	}

	/**
	 * Add a dynamic property. Dynamic property has its own semantics, its runtime
	 * value will be different to different targets.
	 *
	 * @param property a new dynamic property.
	 */

	public void addDynamicProperty(DynamicProperty property) {
		dynamicProperties.add(property);
	}

	/**
	 * Whether the task will fail if any containing task failed.
	 *
	 * @param f
	 */
	public void setFailOnError(boolean f) {
		this.failOnError = f;
	}

	/**
	 * Add a nested task to Sequential.
	 * <p>
	 *
	 * @param nestedTask Nested task to execute Sequential
	 *                   <p>
	 */
	@Override
	public void addTask(Task nestedTask) {
		nestedTasks.addElement(nestedTask);
	}

	/**
	 * Execute all nestedTasks.
	 *
	 * @throws BuildException if one of the nested tasks fails.
	 */

	@Override
	public void execute() throws BuildException {
		Object o = getProject().getReference(refID);
		ProjectList list;
		if (o instanceof ProjectList) {
			list = (ProjectList) o;
		} else {
			log("ForEach only support one ProjectList reference!", //$NON-NLS-1$
					Project.MSG_ERR);
			throw new BuildException("ForEach only support one ProjectList reference!"); //$NON-NLS-1$
		}

		for (int i = 0; i < list.getCount(); i++) {
			ProjectInfo projectInfo = list.getProject(i);

			// register dynamic property

			for (int j = 0; j < dynamicProperties.size(); j++) {
				DynamicProperty property = (DynamicProperty) dynamicProperties.get(j);
				getDynamicValue(projectInfo, property);
			}

			// ================= Add Build Listeners
			// ============================
			DefaultLogger logger = null;
			if (loggerPath != null) {
				File loggerPathDir = new File(loggerPath);
				if (!loggerPathDir.exists()) {
					loggerPathDir.mkdir();
				}

				if (!loggerPathDir.isDirectory()) {
					throw new BuildException("loggerPath attribute should reference to a directory."); //$NON-NLS-1$
				}

				// Add INFO level logger.
				logger = new DefaultLogger();
				PrintStream str = null;
				try {
					String fileName = projectInfo.getValue("projectName") + "_log.txt"; //$NON-NLS-1$//$NON-NLS-2$
					str = new PrintStream(new FileOutputStream(new File(loggerPathDir, fileName), true), true); // $NON-NLS-1$
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				logger.setMessageOutputLevel(Project.MSG_INFO);
				logger.setErrorPrintStream(str);
				logger.setOutputPrintStream(str);
				getProject().addBuildListener(logger);
			}
			// ================= End of adding Build Listeners
			// ============================

			for (Enumeration e = nestedTasks.elements(); e.hasMoreElements();) {
				Task nestedTask = (Task) e.nextElement();

				try {
					nestedTask.perform();
					getProject().setProperty(failurePropertyName, TASK_STATUS_PASS); // $NON-NLS-1$
				} catch (BuildException ex) {

					log("Execution error on task: [" + nestedTask.getTaskName() + "] message: " + ex.toString(), //$NON-NLS-1$ //$NON-NLS-2$
							Project.MSG_WARN);

					// Check for Ant execution, check to see whether the
					// individual project
					// failed on itself or failed on dependencies.

					if ("Ant".equalsIgnoreCase(nestedTask.getTaskName())) //$NON-NLS-1$
					{
						if (isThisProjectError(ex)) {
							getProject().setProperty(failurePropertyName, TASK_STATUS_FAIL); // $NON-NLS-1$
							log("Project failed on itself: ", Project.MSG_ERR); //$NON-NLS-1$
						} else {
							getProject().setProperty(failurePropertyName, TASK_STATUS_FAIL_ON_DEPENDENCY);
							log("Project failed on dependencies:", Project.MSG_WARN); //$NON-NLS-1$
						}
					}

					if (failOnError) {
						throw ex;
					}

					log("Continue execution.", Project.MSG_INFO); //$NON-NLS-1$
				}
			}

			// Remove Build Listener.
			if (logger != null) {
				getProject().removeBuildListener(logger);
			}
		}
	}

	private String getDynamicValue(ProjectInfo info, DynamicProperty property) {

		String result = ""; //$NON-NLS-1$

		// e.g: property.value = "utest.report.base.dir, '/',
		// current.projectName"
		// Split using ',' within tokens.

		String value = property.getValue();
		String[] tokens = value.split(","); //$NON-NLS-1$

		for (int i = 0; i < tokens.length; i++) {
			result = result + getRuntimeValue(info, tokens[i].trim());
		}

		getProject().setProperty(property.getName(), result);

		return result;
	}

	/**
	 * Get the runtime value of an expression. <br>
	 * <li><b>"current" </b> points to the current project object.
	 * <li>Characters in '' will keep there original string value.
	 * <li>Other tokens will be treated as property key of current project. <br>
	 * <br>
	 * e.g: "utest.report.base.dir, '/', current.projectName" will be split into
	 * three tokens: Runtime value for "utest.report.base.dir" will be retrieved
	 * from current project, '/' will keep its original value, while
	 * "current.projectName" will be the current project name.
	 *
	 * @param info
	 * @param key
	 * @return
	 */

	private String getRuntimeValue(ProjectInfo info, String key) {

		final String CURRENT_KEY = "current"; //$NON-NLS-1$

		if (key.startsWith(CURRENT_KEY)) {
			// e.g:
			// projectInfo.getAttribute( "projectName" )
			// projectInfo.getAttribute( "baseDir" )

			return info.getValue(key.substring(CURRENT_KEY.length() + 1));
		} else if (key.startsWith("'") && key.endsWith("'")) //$NON-NLS-1$//$NON-NLS-2$
		{
			return key.substring(1, key.length() - 1);
		} else { // $NON-NLS-1$//$NON-NLS-2$
			return getProject().getProperty(key);
		}
	}

	/**
	 *
	 * @param ex
	 * @return
	 */

	private boolean isThisProjectError(BuildException ex) {
		// The cause must be a BuildException.
		BuildException e = (BuildException) ex.getCause();

		if (null == e) {
			return true;
		}

		Location baseScriptLocation = e.getLocation();
		String baseScriptPath;

		if (baseScriptLocation == null) {
			return true;
		}

		String baseScriptFileName = baseScriptLocation.getFileName();
		if (baseScriptFileName == null) {
			return true;
		}
		baseScriptPath = baseScriptFileName.substring(0, baseScriptFileName.lastIndexOf(File.separator));

		assert baseScriptPath != null;

		Throwable current = e;
		Throwable old = current;
		while (current != null) {
			old = current;
			current = current.getCause();
		}
		if (old instanceof BuildException) {
			Location extScriptLocation = ((BuildException) old).getLocation();

			if (extScriptLocation != null) {
				String extScriptFileName = extScriptLocation.getFileName();

				// if the path of the bottom most level build file in exception is different
				// with the second level, means failed on dependency
				if (extScriptFileName != null && !extScriptFileName.startsWith(baseScriptPath)) {
					log("Task failed on its dependency : " + extScriptLocation.getFileName() + " Original script path " //$NON-NLS-1$ //$NON-NLS-2$
							+ baseScriptPath, Project.MSG_ERR);
					return false;
				}
			}
		}
		return true;
	}

}
