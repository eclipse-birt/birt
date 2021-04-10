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

import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;

/**
 * Represents the handle of library element. The library contains the resuable
 * element components.
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Content Item</th>
 * <th width="40%">Description</th>
 * 
 * <tr>
 * <td>Code Modules</td>
 * <td>Global scripts that apply to the report as a whole.</td>
 * </tr>
 * 
 * <tr>
 * <td>Parameters</td>
 * <td>A list of Parameter elements that describe the data that the user can
 * enter when running the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Data Sources</td>
 * <td>The connections used by the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Data Sets</td>
 * <td>Data sets defined in the design.</td>
 * </tr>
 * 
 * <tr>
 * <td>Color Palette</td>
 * <td>A set of custom color names as part of the design.</td>
 * </tr>
 * 
 * <tr>
 * <td>Styles</td>
 * <td>User-defined styles used to format elements in the report. Each style
 * must have a unique name within the set of styles for this report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Page Setup</td>
 * <td>The layout of the master pages within the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Components</td>
 * <td>Reusable report items defined in this design. Report items can extend
 * these items. Defines a "private library" for this design.</td>
 * </tr>
 * 
 * <tr>
 * <td>Translations</td>
 * <td>The list of externalized messages specifically for this report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Images</td>
 * <td>A list of images embedded in this report.</td>
 * </tr>
 * 
 * </table>
 * 
 * <p>
 * There are two cases that this library handle represents:
 * <ul>
 * <li>the library file that user opens directly <br>
 * In this case, user can take operations as same as design file.
 * <li>the library file that is included in one design file <br>
 * User can not take operations, because the included library file is read-only.
 * Any operation on the library file of this type will throw runtime exception.
 * </ul>
 */

public class LibraryHandle extends LibraryHandleImpl implements ILibraryModel {

	/**
	 * Constructs one library handle with the given library element.
	 * 
	 * @param library library element
	 */

	public LibraryHandle(Library library) {
		super(library);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#isDirectionRTL()
	 */
}