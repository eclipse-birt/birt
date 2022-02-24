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

package org.eclipse.birt.report.model.elements.interfaces;

import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.AbstractTheme;

/**
 * The interface for elements that supports to set theme on it.
 */

public interface ISupportThemeElement {

	String getThemeName();

	AbstractTheme getTheme();

	AbstractTheme getTheme(Module module);
}
