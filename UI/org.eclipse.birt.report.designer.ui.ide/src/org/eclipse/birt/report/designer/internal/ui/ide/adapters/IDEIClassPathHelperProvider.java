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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.designer.internal.ui.preferences.IClassPathHelperProvider;
import org.eclipse.birt.report.designer.ui.preferences.IStatusChangeListener;
import org.eclipse.birt.report.designer.ui.preferences.OptionsConfigurationBlock;
import org.eclipse.core.resources.IProject;

/**
 *
 */

public class IDEIClassPathHelperProvider implements IClassPathHelperProvider {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.preferences.
	 * IClassPathHelperProvider#createBlock(org.eclipse.birt.report.designer.ui.
	 * preferences.IStatusChangeListener, org.eclipse.core.resources.IProject)
	 */
	@Override
	public OptionsConfigurationBlock createBlock(IStatusChangeListener listener, IProject project) {
		return new IDEClassPathBlock(listener, project);
	}

}
