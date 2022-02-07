/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.preferences;

import org.eclipse.birt.report.designer.ui.preferences.IStatusChangeListener;
import org.eclipse.birt.report.designer.ui.preferences.OptionsConfigurationBlock;
import org.eclipse.core.resources.IProject;

/**
 * Provider to support the class path block
 */

public interface IClassPathHelperProvider {
	/**
	 * Create the block.
	 * 
	 * @param listener
	 * @param project
	 * @return
	 */
	OptionsConfigurationBlock createBlock(IStatusChangeListener listener, IProject project);
}
