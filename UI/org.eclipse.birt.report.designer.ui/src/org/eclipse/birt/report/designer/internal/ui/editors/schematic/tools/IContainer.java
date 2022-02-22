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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.draw2d.geometry.Point;

/**
 * Helper Interface for parameter transfer.
 */

public interface IContainer {

	/**
	 * If the point is contained within specified object.
	 *
	 * @param pt
	 * @return
	 */
	boolean contains(Point pt);

	boolean isSelect();
}
