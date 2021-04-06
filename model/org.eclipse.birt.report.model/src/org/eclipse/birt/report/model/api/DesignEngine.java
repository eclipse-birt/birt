/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.api.metadata.IMetaLogger;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;
import org.eclipse.birt.report.model.metadata.MetaLogManager;

import com.ibm.icu.util.ULocale;

/**
 * Represents the BIRT design engine as a whole. Used to create new designs or
 * open existing designs.
 * <p>
 * The design engine uses <em>meta-data</em> defined in an external file. This
 * file is defined by BIRT and should both be available and valid. However, if
 * an application wants to catch and handle errors associated with this file, it
 * can create and register an instance of <code>IMetaLogger</code> before
 * creating or opening the first report design. The logger is most useful for
 * test suites.
 * <p>
 * This is a wrapper class for the IDesignEngine. The new user should use the
 * IDesignEngineFactory to create the IDesignEngine instead of use this class
 * directly.
 * 
 * @see IMetaLogger
 * @see MetaLogManager
 */

public final class DesignEngine implements IDesignEngine {

	/**
	 * The logger for errors.
	 */

	protected final static Logger errorLogger = Logger.getLogger(DesignEngine.class.getName());

	/**
	 * 
	 */

	protected static IDesignEngineFactory cachedFactory;

	/**
	 * The implementation of the design engine.
	 */

	protected IDesignEngine engine;

	/**
	 * Constructs a DesignEngine with the given platform config.
	 * 
	 * @param config the platform config.
	 */

	public DesignEngine(DesignConfig config) {
		try {
			Platform.startup(config);
		} catch (BirtException e) {
			errorLogger.log(Level.SEVERE, "Error occurs while start the platform", e); //$NON-NLS-1$
		}

		if (cachedFactory == null) {
			Object factory = Platform.createFactoryObject(IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY);
			if (factory instanceof IDesignEngineFactory) {
				cachedFactory = (IDesignEngineFactory) factory;
			}
		}

		if (cachedFactory != null)
			engine = cachedFactory.createDesignEngine(config);

		if (engine == null) {
			errorLogger.log(Level.INFO, "Can not start the design engine."); //$NON-NLS-1$
		}
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

	public SessionHandle newSessionHandle(ULocale locale) {
		return engine.newSessionHandle(locale);
	}

	/**
	 * Creates a new design session handle. The application uses the handle to open,
	 * create and manage designs. The session also represents the user and maintains
	 * the user's locale information.
	 * <p>
	 * This method is not suggested to use. The user should use new
	 * DesignEngine(config).newSessionHandle() to create the session.
	 * 
	 * @param locale the user's locale. If <code>null</code>, uses the system
	 *               locale.
	 * @return the design session handle
	 * @see SessionHandle
	 * 
	 * @deprecated
	 */

	public static SessionHandle newSession(ULocale locale) {
		return new DesignEngine(new DesignConfig()).newSessionHandle(locale);
	}

	/**
	 * Gets the meta-data of the design engine.
	 * 
	 * @return the meta-data of the design engine.
	 */

	public IMetaDataDictionary getMetaData() {
		return engine.getMetaData();
	}

	/**
	 * Gets the meta-data dictionary of the design engine.
	 * <p>
	 * This method is not suggested to use. The user should use new
	 * DesignEngine(config).getMetaData() to get the metadata dictionary.
	 * 
	 * @return the meta-data dictionary of the design engine
	 * 
	 * @deprecated
	 */

	public static IMetaDataDictionary getMetaDataDictionary() {
		return new DesignEngine(new DesignConfig()).getMetaData();
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

	public void registerMetaLogger(IMetaLogger newLogger) {
		engine.registerMetaLogger(newLogger);
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

	public boolean removeMetaLogger(IMetaLogger logger) {
		return engine.removeMetaLogger(logger);
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

	public IReportDesign openDesign(String fileName, InputStream ins, IModuleOption options)
			throws DesignFileException {
		return engine.openDesign(fileName, ins, options);

	}
}