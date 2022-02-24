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

package org.eclipse.birt.report.model.api;

import java.io.InputStream;

import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.api.metadata.IMetaLogger;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;

import com.ibm.icu.util.ULocale;

/**
 * Represents the BIRT design engine as a whole. Used to create new sessions.
 * 
 * @see IMetaLogger
 */

public interface IDesignEngine {

	/**
	 * Gets the meta-data of the design engine.
	 * 
	 * @return the meta-data of the design engine
	 */

	public IMetaDataDictionary getMetaData();

	/**
	 * Creates a new design session handle. The application uses the handle to open,
	 * create and manage designs. The session also represents the user and maintains
	 * the user's locale information.
	 * 
	 * @param locale the user's locale. If <code>null</code>, uses the system
	 *               locale.
	 * @param config the platform config
	 * @return the design session handle
	 * @see SessionHandle
	 */

	public SessionHandle newSessionHandle(ULocale locale);

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

	public void registerMetaLogger(IMetaLogger newLogger);

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

	public boolean removeMetaLogger(IMetaLogger logger);

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

	public IReportDesign openDesign(String fileName, InputStream ins, IModuleOption options) throws DesignFileException;
}
