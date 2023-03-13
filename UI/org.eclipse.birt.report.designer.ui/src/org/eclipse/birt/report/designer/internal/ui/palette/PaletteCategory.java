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

package org.eclipse.birt.report.designer.internal.ui.palette;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * The palette entry used to describe the category on the palette view
 */

public class PaletteCategory extends PaletteDrawer {

	/** The category name */
	private String name;

	/**
	 *
	 *
	 * @param name
	 * @param displayLabellabel
	 * @param icon
	 */
	public PaletteCategory(String name, String displayLabel, ImageDescriptor icon) {
		super(displayLabel, icon);
		Assert.isNotNull(name);
		this.name = name;
	}

	/**
	 * Gets the category name
	 *
	 * @return Returns the category name
	 */
	public String getCategoryName() {
		return name;
	}

}
