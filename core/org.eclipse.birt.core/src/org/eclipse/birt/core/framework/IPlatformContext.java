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

package org.eclipse.birt.core.framework;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Defines an interface to access engine context 
 */
public interface IPlatformContext 
{	
	/**
	 * Return all of the folders under homeFolder\subFolder as a string list.
	 * @param homeFolder - the home folder
	 * @param subFolder - a subFolder name under home folder
	 * @return string list of all of the folders in homeFolder\subFolder. 
	 */
	public List getFileList( String homeFolder, String subFolder, boolean includingFiles, boolean relativeFileList );
	
	/**
	 * @param pluginFolder - the folder that contains the file.
	 * @return the input stream of the file which is under the folder
	 */
	public InputStream getInputStream( String folder, String fileName );
	
	/**
	 * @param folder - the folder that contains the file
	 * @param fileName - the name of the file
	 * @return the URL of the file. The URL will be used in URLClassLoader in the future.  
	 */
	public URL getURL( String folder, String fileName );
	
}
