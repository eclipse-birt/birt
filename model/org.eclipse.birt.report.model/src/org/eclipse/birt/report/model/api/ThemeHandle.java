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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;

/**
 * Represents a theme in the library. Each theme contains some number of styles.
 *
 *
 * @see org.eclipse.birt.report.model.elements.Theme
 */

public class ThemeHandle extends AbstractThemeHandle implements IThemeModel {

	/**
	 * Constructs the handle for a theme with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public ThemeHandle(Module module, DesignElement element) {
		super(module, element);
	}

}
