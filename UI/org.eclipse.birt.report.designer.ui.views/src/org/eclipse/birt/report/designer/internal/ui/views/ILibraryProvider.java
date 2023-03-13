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

package org.eclipse.birt.report.designer.internal.ui.views;

import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.swt.graphics.Image;

/**
 * Libraries Provider interface
 *
 * @deprecated
 */

@Deprecated
public interface ILibraryProvider {

	/**
	 * Returns associated libraries for the report.
	 *
	 * @return
	 */
	LibraryHandle[] getLibraries();

	/**
	 * Returns the display icon for the given library If it returns null, a default
	 * icon will be used.
	 *
	 * @param handle the library handle
	 * @return the icon for the given library.
	 */
	Image getDisplayIcon(LibraryHandle handle);
}
