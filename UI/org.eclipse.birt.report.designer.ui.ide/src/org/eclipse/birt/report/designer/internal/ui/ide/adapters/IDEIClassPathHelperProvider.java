/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public OptionsConfigurationBlock createBlock(IStatusChangeListener listener, IProject project) {
		return new IDEClassPathBlock(listener, project);
	}

}
