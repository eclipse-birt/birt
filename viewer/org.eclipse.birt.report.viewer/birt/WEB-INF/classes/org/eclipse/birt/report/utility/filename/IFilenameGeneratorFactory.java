/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/
package org.eclipse.birt.report.utility.filename;

import javax.servlet.ServletContext;

/**
 * Factory interface for the implementors of IFilenameGenerator.
 */
public interface IFilenameGeneratorFactory {
	/**
	 * Returns an instance of IFilenameGenerator.
	 * 
	 * @param servletContext servlet context
	 * @return instance of IFilenameGenerator
	 */
	public IFilenameGenerator createFilenameGenerator(ServletContext servletContext);
}
