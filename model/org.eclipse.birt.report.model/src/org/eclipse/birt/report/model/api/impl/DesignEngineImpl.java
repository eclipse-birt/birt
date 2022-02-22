/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.impl;

import java.io.InputStream;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IModuleOption;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.api.metadata.IMetaLogger;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaLogManager;

import com.ibm.icu.util.ULocale;

/**
 * Implementation of DesignEngine.
 *
 */

public class DesignEngineImpl implements IDesignEngine {

	/**
	 * The flag to determine whether the meta data and extensions have been loaded.
	 */

	private static boolean initialized = false;

	/**
	 * The configuration for the design engine.
	 */

	private DesignConfig designConfig;

	/**
	 * Constructs a DesignEngine with the given platform config.
	 *
	 * @param config the platform config.
	 */

	public DesignEngineImpl(DesignConfig config) {
		designConfig = config;
		ensureInitialized();
	}

	/**
	 * Creates a new design session handle. The application uses the handle to open,
	 * create and manage designs. The session also represents the user and maintains
	 * the user's locale information.
	 *
	 * @param locale the user's locale. If <code>null</code>, uses the system
	 *               locale.
	 * @return the design session handle
	 * @see SessionHandle
	 */

	@Override
	public SessionHandle newSessionHandle(ULocale locale) {
		// meta-data ready.
		ensureInitialized();
		SessionHandle session = new SessionHandle(locale);
		if (designConfig != null) {
			IResourceLocator locator = designConfig.getResourceLocator();
			if (locator != null) {
				session.setResourceLocator(locator);
			}

		}

		return session;
	}

	static synchronized void ensureInitialized() {
		MetaDataDictionary.initialize();
		if (!initialized) {
			SimpleElementFactory.setInstance(new org.eclipse.birt.report.model.api.impl.SimpleElementFactory());
			initialized = true;
		}
	}

	/**
	 * Gets the meta-data of the design engine.
	 *
	 * @return the meta-data of the design engine.
	 */

	@Override
	public IMetaDataDictionary getMetaData() {
		ensureInitialized();
		return MetaDataDictionary.getInstance();
	}

	/**
	 * Registers a <code>IMetaLogger</code> to record initialization errors. The
	 * logger will be notified of the errors during meta-data initialization. The
	 * meta-data system will be initialized once (and only once). Loggers should be
	 * registered before the first time a session is created so that it can be
	 * notified of the logging actions.
	 *
	 * @param newLogger the <code>MetaLogger</code> to be registered.
	 *
	 * @see #removeMetaLogger(IMetaLogger)
	 */

	@Override
	public void registerMetaLogger(IMetaLogger newLogger) {
		MetaLogManager.registerLogger(newLogger);
	}

	/**
	 * Removes a <code>IMetaLogger</code>. This method will remove the logger from
	 * the list and close the logger if it has already been registered. The logger
	 * will no longer be notified of the errors during metadata initialization.
	 * Returns <code>true</code> if this logger manager contained the specified
	 * logger.
	 *
	 * @param logger the <code>MetaLogger</code> to be removed.
	 * @return <code>true</code> if this logger manager contained the specified
	 *         logger.
	 *
	 * @see #registerMetaLogger(IMetaLogger)
	 */

	@Override
	public boolean removeMetaLogger(IMetaLogger logger) {
		return MetaLogManager.removeLogger(logger);
	}

	/**
	 * Opens the report design.
	 *
	 * @param fileName the report file name
	 * @param ins      the input stream. Can be <code>null</code>.
	 * @param options  options to control the way to open the design
	 * @return the report design instance
	 * @throws DesignFileException if the report file cannot be found or the file is
	 *                             invalid.
	 */

	@Override
	public IReportDesign openDesign(String fileName, InputStream ins, IModuleOption options)
			throws DesignFileException {
		SessionHandle tmpSession = newSessionHandle(null);

		return new org.eclipse.birt.report.model.simpleapi.ReportDesign(
				tmpSession.openDesign(fileName, ins, (ModuleOption) options));
	}

}
