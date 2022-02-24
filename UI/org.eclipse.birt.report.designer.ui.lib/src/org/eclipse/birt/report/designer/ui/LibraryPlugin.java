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

package org.eclipse.birt.report.designer.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * LibraryPlugin
 */
public class LibraryPlugin extends AbstractUIPlugin {

	public LibraryPlugin() {
		super();
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		ReportPlugin.getDefault().addIgnoreViewID("org.eclipse.birt.report.designer.ui.editors.LibraryEditor"); //$NON-NLS-1$
	}
}
