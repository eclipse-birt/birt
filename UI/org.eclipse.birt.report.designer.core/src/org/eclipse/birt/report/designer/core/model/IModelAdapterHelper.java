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
