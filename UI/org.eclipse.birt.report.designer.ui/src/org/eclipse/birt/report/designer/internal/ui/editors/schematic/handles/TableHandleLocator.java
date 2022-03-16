/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.handles.MoveHandleLocator;

/**
 * Table handle locator
 */
public class TableHandleLocator extends MoveHandleLocator {

	/**
	 * Constructor
	 *
	 * @param ref
	 */
	public TableHandleLocator(IFigure ref) {
		super(ref);
	}

}
