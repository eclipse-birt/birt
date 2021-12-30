/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core.launcher;

/**
 * IReportLaunchConstants
 */
public interface IReportLaunchConstants {

	/**
	 * Report launcher ID
	 */
	String ID_REPORT_LAUNCHER = "org.eclipse.birt.report.debug.core.launcher"; //$NON-NLS-1$

	/**
	 * report file key
	 */
	String ATTR_REPORT_FILE_NAME = "report.file.name"; //$NON-NLS-1$

	/**
	 * Temp folder key
	 */
	String ATTR_TEMP_FOLDER = "temp.folder"; //$NON-NLS-1$

	/**
	 * Engine home key
	 */
	String ATTR_ENGINE_HOME = "engine.home"; //$NON-NLS-1$

	/**
	 * Engine task type key
	 */
	String ATTR_TASK_TYPE = "engine.task.type"; //$NON-NLS-1$

	/**
	 * Debug type key
	 */
	String ATTR_DEBUG_TYPE = "engine.debug.type"; //$NON-NLS-1$

	/**
	 * Engine target format key
	 */
	String ATTR_TARGET_FORMAT = "engine.target.format"; //$NON-NLS-1$

	/**
	 * Open target file key
	 */
	String ATTR_OPEN_TARGET = "open.target.file"; //$NON-NLS-1$

	/**
	 * Default eclipse home key
	 */
	String ATTR_USE_DEFULT_ENGINE_HOME = "use.default.engine.home"; //$NON-NLS-1$

	/**
	 * Resource folder
	 */
	String ATTR_RESOURCE_FOLDER = "use.resource.folder"; //$NON-NLS-1$

	/**
	 * User class path key
	 */
	String ATTR_USER_CLASS_PATH = "report.user.class.path"; //$NON-NLS-1$

	/**
	 * Class path key
	 */
	String ATTR_CLASSPATH = ID_REPORT_LAUNCHER + ".CLASSPATH"; //$NON-NLS-1$

	/**
	 * Request port key
	 */
	String ATTR_LISTEN_PORT = "report.listen.port"; //$NON-NLS-1$

	/**
	 * Parameter name key
	 */
	String ATTR_PARAMRTER = "param:"; //$NON-NLS-1$

	String ATTR_MULPARAMRTER = "mulparam:"; //$NON-NLS-1$

	String ATTR_DATA_LIMIT_SIZE = "data.limit";//$NON-NLS-1$
	/**
	 * Debug type for java classes.
	 */
	int DEBUG_TYPE_JAVA_CLASS = 1;

	/**
	 * Debug type for java scripts.
	 */
	int DEBUG_TYPE_JAVA_SCRIPT = 2;

	/**
	 * Debug type for java classes.
	 */
	int DEBUG_TYPE_ALL = DEBUG_TYPE_JAVA_CLASS | DEBUG_TYPE_JAVA_SCRIPT;

	/**
	 * Task type for run.
	 */
	int TASK_TYPE_RUN = 1;

	/**
	 * Task type for render.
	 */
	int TASK_TYPE_RENDER = 2;

	/**
	 * Task type for run then render.
	 */
	int TASK_TYPE_RUN_PLUS_RENDER = TASK_TYPE_RUN | TASK_TYPE_RENDER;

	/**
	 * Task type for run and render.
	 */
	int TASK_TYPE_RUN_AND_RENDER = 4;

	int EXIT_OK = 1;

	int EXIT_FAIL = -1;

	String DEFAULT_TARGET_FORMAT = "html"; //$NON-NLS-1$

	int DEFAULT_DEBUG_TYPE = DEBUG_TYPE_JAVA_SCRIPT;

	int DEFAULT_TASK_TYPE = TASK_TYPE_RUN_AND_RENDER;

}
