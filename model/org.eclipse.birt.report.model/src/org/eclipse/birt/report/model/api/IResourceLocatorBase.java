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

package org.eclipse.birt.report.model.api;

import java.net.URL;
import java.util.Map;

/**
 *
 */

interface IResourceLocatorBase {

	/**
	 * The host name of the fragments where inner resources are located.
	 */

	String FRAGMENT_RESOURCE_HOST = "org.eclipse.birt.resources"; //$NON-NLS-1$

	/**
	 * The type of the images to search
	 */

	int IMAGE = 1;

	/**
	 * The type of the libraries to search
	 */

	int LIBRARY = 2;

	/**
	 * The type of the cascading style sheet to search.
	 */

	int CASCADING_STYLE_SHEET = 3;

	/**
	 * The type for the jar file. Includes .jar type.
	 */

	int JAR_FILE = 4;

	/**
	 * The type for the message file.
	 */

	int MESSAGE_FILE = 5;

	/**
	 * The other types.
	 */

	int OTHERS = 0;

	/**
	 * Key for the location to search in appContext.
	 */
	String BIRT_RESOURCELOCATOR_SEARCH_LOCATION = "birtResourceLocatorSearchLocation"; //$NON-NLS-1$

	/**
	 * The location mask which searches in the file system with path.
	 */
	int RESOURCE_FILEPATH = 0x01;
	/**
	 * The location mask which searches in the resource bundle.
	 */
	int RESOURCE_BUNDLE = 0x02;
	/**
	 * The location mask which searches in the resource folder.
	 */
	int RESOURCE_FOLDER = 0x04;
	/**
	 * The location mask which searches the file relative to design.
	 */
	int RESOURCE_DESIGN = 0x08;

	/**
	 * Searches the file by the given file name. The actual search algorithm will be
	 * different in different environment. The file type is just helpful when
	 * different file searching steps for different files are required. Because new
	 * file type will be added if design file includes new file, the default
	 * searching steps are encouraged for unknown file type to improve robustness.
	 *
	 * @param moduleHandle The module to tell the search context. This could be null
	 *                     if the search algorithm does not need the design. It can
	 *                     be the instance of one of <code>ReportDesignHandle</code>
	 *                     and <code>LibraryHandle</code>.
	 * @param fileName     The file name to be searched. This could be an absolute
	 *                     path or a relative path.
	 * @param type         The type of the file to search. The value must be one of
	 *                     <code>IMAGE</code>,<code>LIBRARY</code> ,
	 *                     <code>CASCADING_STYLE_SHEET</code> and
	 *                     <code>MESSAGEFILE</code>.
	 * @return The <code>URL</code> object. <code>null</code> if the file can not be
	 *         found.
	 */

	URL findResource(ModuleHandle moduleHandle, String fileName, int type);

	/**
	 * Searches the file by the given file name and the given user's information.
	 * The actual search algorithm will be different in different environment. The
	 * file type is just helpful when different file searching steps for different
	 * files are required. Because new file type will be added if design file
	 * includes new file, the default searching steps are encouraged for unknown
	 * file type to improve robustness.
	 *
	 * @param moduleHandle The module to tell the search context. This could be null
	 *                     if the search algorithm does not need the design. It can
	 *                     be the instance of one of <code>ReportDesignHandle</code>
	 *                     and <code>LibraryHandle</code>.
	 * @param fileName     The file name to be searched. This could be an absolute
	 *                     path or a relative path.
	 * @param type         The type of the file to search. The value must be one of
	 *                     <code>IMAGE</code>,<code>LIBRARY</code> ,
	 *                     <code>CASCADING_STYLE_SHEET</code> and
	 *                     <code>MESSAGEFILE</code>.
	 * @param appContext   The map containing the user's information
	 * @return The <code>URL</code> object. <code>null</code> if the file can not be
	 *         found.
	 */

	URL findResource(ModuleHandle moduleHandle, String fileName, int type, Map appContext);

}
