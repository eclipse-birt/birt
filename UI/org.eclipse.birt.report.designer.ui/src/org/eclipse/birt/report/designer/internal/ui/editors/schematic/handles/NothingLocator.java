/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public void relocate(IFigure target) {
	}
}