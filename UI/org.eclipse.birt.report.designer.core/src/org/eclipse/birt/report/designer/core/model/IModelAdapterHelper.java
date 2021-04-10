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

package org.eclipse.birt.report.designer.core.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;

/**
 * Interface to provide model adapter helper.
 * 
 */
public interface IModelAdapterHelper {

	/**
	 * Marks the flag
	 * 
	 * @param bool
	 */
	void markDirty(boolean bool);

	/**
	 * Gets the flag
	 * 
	 * @return
	 */
	boolean isDirty();

	/**
	 * Gets the preferred size
	 * 
	 * @return
	 */
	Dimension getPreferredSize();

	/**
	 * Gets the insets
	 * 
	 * @return
	 */
	Insets getInsets();
}