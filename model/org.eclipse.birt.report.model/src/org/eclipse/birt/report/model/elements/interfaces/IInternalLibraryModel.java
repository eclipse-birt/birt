/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 *
 */

public interface IInternalLibraryModel {

	/**
	 * Identifier of the slot that holds themes.
	 */

	int THEMES_SLOT = 0;
	/**
	 * Identifier of the slot that holds a collections of cube elements.
	 */

	int CUBE_SLOT = 6;
	/**
	 * Number of slots in the library.
	 */

	int SLOT_COUNT = 7;

}
