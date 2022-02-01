/*******************************************************************************
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
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.core.framework.IPlatformConfig;

/**
 * 
 */
public interface IEngineConfig extends IPlatformConfig {
	static final String LOG_DESTINATION = "logDest"; //$NON-NLS-1$
	static final String LOG_LEVEL = "logLevel"; //$NON-NLS-1$
	static final String LOG_FILE = "logFile"; //$NON-NLS-1$
	static final String LOG_ROLLING_SIZE = "logRollingSize";//$NON-NLS-1$
	static final String LOG_MAX_BACKUP_INDEX = "logMaxBackupIndex";//$NON-NLS-1$
	/**
	 * define the logger used by the report engine.
	 * 
	 * the value is a java.util.Logger object. The user can change the logger level
	 * in the setted logger. It will overides the setting of LOG_DESTINATION and
	 * LOG_LEVEL
	 */
	static final String ENGINE_LOGGER = "org.eclipse.birt.report.engine.logger"; //$NON-NLS-1$
	static final String REPORT_DOCUMENT_LOCK_MANAGER = "org.eclipse.birt.report.engine.api.IReportDocumentLockManager"; //$NON-NLS-1$
	static final String SCRIPT_OBJECTS = "org.eclipse.birt.report.engine.api.EngineConfig.scriptObjects";//$NON-NLS-1$
	/**
	 * contains a hashmap which saved the default render options.
	 * 
	 * each key is either a DEFAULT_EMITTER_CONFIG or format or emitter id. each
	 * value is a IRenderOption
	 */
	static final String EMITTER_CONFIGS = "org.eclipse.birt.report.engine.api.EngineConfig.emitterConfigs"; //$NON-NLS-1$
	/**
	 * the default render option which used by all emitters
	 */
	static final String DEFAULT_RENDER_OPTION = "org.eclipse.birt.report.engine.api.EngineConfig.defaultRenderOption";//$NON-NLS-1$
	static final String STATUS_HANDLER = "org.eclipse.birt.report.engine.api.EngineConfig.statusHandler";//$NON-NLS-1$
	/**
	 * resource locator used by design engine
	 */
	static final String RESOURCE_LOCATOR = "resourceLocator";

	/**
	 * resource path used by design engine
	 */
	static final String RESOURCE_PATH = "resourcePath";

	/**
	 * the user defined font configuration file path.
	 */
	static final String FONT_CONFIG = "fontConfig";

	/**
	 * The max rows per query
	 */
	static final String MAX_ROWS_PER_QUERY = "maxRowsPerQuery";
}
