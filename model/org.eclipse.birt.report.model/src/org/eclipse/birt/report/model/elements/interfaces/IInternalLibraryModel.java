/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public static final int THEMES_SLOT = 0;
	/**
	 * Identifier of the slot that holds a collections of cube elements.
	 */

	public static final int CUBE_SLOT = 6;
	/**
	 * Number of slots in the library.
	 */

	public static final int SLOT_COUNT = 7;

}