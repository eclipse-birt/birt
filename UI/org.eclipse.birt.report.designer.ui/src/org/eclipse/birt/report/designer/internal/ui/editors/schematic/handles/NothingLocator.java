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
import org.eclipse.draw2d.Locator;

/**
 * Provides dummy Locator. This class is used only for the case we need a
 * locator but really do nothing.
 */
public class NothingLocator implements Locator {

	/**
	 * @param ref
	 */
	public NothingLocator(IFigure ref) {
	}

	public NothingLocator() {

	}

	@Override
	public void relocate(IFigure target) {
	}
}
