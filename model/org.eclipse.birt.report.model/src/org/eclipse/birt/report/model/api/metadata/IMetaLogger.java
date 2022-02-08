/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.model.api.metadata;

import org.eclipse.birt.report.model.metadata.MetaLogManager;

/**
 * Interface to handle meta-data errors during initialization. Logs errors due
 * to missing a rom.def file, rom.def parser errors, meta-data build errors,
 * etc.
 * 
 * The class that is interested in the errors may implement this interface.
 * Then, create an instance of that class and register it with
 * {@link MetaLogManager }, using the static <code>registerLogger</code> method.
 * When an error occurs during parsing of the meta-data file, the parser calls
 * the <code>log</code> method of the custom logger.
 * 
 * @see MetaLogManager
 */

public interface IMetaLogger {

	/**
	 * Log an error message.
	 * 
	 * @param message the message object to be logged.
	 */

	public void log(String message);

	/**
	 * Log a message object including the stack trace of the Throwable t passed as
	 * parameter.
	 * 
	 * @param message the message object to be logged.
	 * @param t       the exception to log, including its stack trace.
	 */

	public void log(String message, Throwable t);

	/**
	 * Release the logger, implement this method to do clean up of the logger, close
	 * the writer, release a lock, etc. This method is called by
	 * {@link MetaLogManager#shutDown()}.
	 */

	public void close();

}
