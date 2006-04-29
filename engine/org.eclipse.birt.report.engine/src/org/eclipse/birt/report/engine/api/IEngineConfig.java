/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.core.framework.IPlatformConfig;

/**
 * 
 */
public interface IEngineConfig extends IPlatformConfig
{
	static final String LOG_DESTINATION = "logDest"; //$NON-NLS-1$			
	static final String LOG_LEVEL = "logLevel"; //$NON-NLS-1$
	static final String TEMP_DIR = "tmpDir"; //$NON-NLS-1$
	static final String REPORT_DOCUMENT_LOCK_MANAGER = "org.eclipse.birt.report.engine.api.IReportDocumentLockManager"; //$NON-NLS-1$
	static final String SCRIPT_OBJECTS = "org.eclipse.birt.report.engine.api.EngineConfig.scriptObjects";
	static final String EMITTER_CONFIGS = "org.eclipse.birt.report.engine.api.EngineConfig.emitterConfigs";
	static final String STATUS_HANDLER = "org.eclipse.birt.report.engine.api.EngineConfig.statusHandler";
	/**
	 * resource locator used by design engine
	 */
	static final String RESOURCE_LOCATOR = "resourceLocator";

}
