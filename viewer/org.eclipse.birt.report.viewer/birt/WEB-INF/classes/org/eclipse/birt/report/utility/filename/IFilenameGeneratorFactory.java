/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
