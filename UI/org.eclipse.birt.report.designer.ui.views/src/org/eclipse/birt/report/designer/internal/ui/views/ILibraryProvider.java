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

package org.eclipse.birt.report.designer.internal.ui.views;

import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.swt.graphics.Image;

/**
 * Libraries Provider interface
 * 
 * @deprecated
 */

public interface ILibraryProvider {

	/**
	 * Returns associated libraries for the report.
	 * 
	 * @return
	 */
	public LibraryHandle[] getLibraries();

	/**
	 * Returns the display icon for the given library If it returns null, a default
	 * icon will be used.
	 * 
	 * @param handle the library handle
	 * @return the icon for the given library.
	 */
	public Image getDisplayIcon(LibraryHandle handle);
}
