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

import java.net.URL;

/**
 * Implements this interface to provide a custom file search algorithm. This
 * class defines the file search algorithm used to locate files referenced in
 * the design. For example, when parsing a design file and a library is
 * encountered, a file locator will be used to locate the library. This
 * interface is implemented differently for each environment. For example, the
 * GUI might have its own file search requirement, while the deployment
 * environment in application server has another.
 * <p>
 * The default file locator is <code>{@link DefaultResourceLocator}</code>.
 * <p>
 * The customized file search must be installed before opening designs. Set the
 * file locator using {@link
 * org.eclipse.birt.report.model.api.SessionHandle#setResourceLocator(IResourceLocator)}
 * method.
 */

public interface IResourceLocator
{
	/**
	 * The host name of the fragments where inner resources are located.
	 */

	public final static String SYMBOLIC_NAME = "org.eclipse.birt.resources"; //$NON-NLS-1$

	/**
	 * The type of the images to search
	 */

	public final static int IMAGE = 1;

	/**
	 * The type of the libraries to search
	 */

	public final static int LIBRARY = 2;

	/**
	 * The type of the cascading style sheet to search.
	 */

	public final static int CASCADING_STYLE_SHEET = 3;

	/**
	 * Searches the file by the given file name. The actual search algorithm
	 * will be different in different environment. The file type is just helpful
	 * when different file searching steps for different files are required.
	 * Because new file type will be added if design file includes new file, the
	 * default searching steps are encouraged for unknown file type to improve
	 * robustness.
	 * 
	 * @param moduleHandle
	 *            The module to tell the search context. This could be null if
	 *            the search algorithm does not need the design. It can be the
	 *            instance of one of <code>ReportDesignHandle</code> and
	 *            <code>LibraryHandle</code>.
	 * @param filename
	 *            The file name to be searched. This could be an absolute path
	 *            or a relative path.
	 * @param type
	 *            The type of the file to search. The value must be one of
	 *            <code>IMAGE</code>,<code>LIBRARY</code> and
	 *            <code>CASCADING_STYLE_SHEET</code>.
	 * @return The <code>URL</code> object. <code>null</code> if the file
	 *         can not be found.
	 */

	public URL findResource( ModuleHandle moduleHandle, String filename,
			int type );
}