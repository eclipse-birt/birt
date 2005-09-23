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

import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.parser.DesignReader;
import org.eclipse.birt.report.model.parser.LibraryReader;

/**
 * Provides some tool methods about the modules.
 */

public class ModuleUtil
{

	/**
	 * Justifies whether a given input stream is a valid report design.
	 * 
	 * @param sessionHandle
	 *            the current session of the report design
	 * @param fileName
	 *            the file name of the report design
	 * @param is
	 *            the input stream of the report design
	 * @return true if the input stream is a valid report design, otherwise
	 *         false
	 */

	public static boolean isValidDesign( SessionHandle sessionHandle,
			String fileName, InputStream is )
	{
		ReportDesign design = null;
		try
		{
			design = DesignReader.getInstance( ).read(
					sessionHandle.getSession( ), fileName, is );
			return design != null;
		}
		catch ( DesignFileException e )
		{
			return false;
		}
	}

	/**
	 * Justifies whether a library resource with the given file name is a valid
	 * library.
	 * 
	 * @param sessionHandle
	 *            the current session of the library
	 * @param fileName
	 *            the file name of the library
	 * @param is
	 *            the input stream of the library
	 * @return true if the library resource is a valid library, otherwise false
	 */

	public static boolean isValidLibrary( SessionHandle sessionHandle,
			String fileName, InputStream is )
	{
		Library lib = null;
		try
		{
			lib = LibraryReader.getInstance( ).read(
					sessionHandle.getSession( ), fileName, is );
			return lib != null;
		}
		catch ( DesignFileException e )
		{
			return false;
		}
	}
}
