/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core;

import org.eclipse.birt.report.debug.internal.core.launcher.ReportLauncher;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * ReportDebugger
 */
public class ReportDebugger implements IApplication {

	public Object start(IApplicationContext context) throws Exception {
		String[] appArgs = new String[0];

		if (context.getArguments() != null) {
			Object args = context.getArguments().get(IApplicationContext.APPLICATION_ARGS);

			if (args instanceof String[]) {
				appArgs = (String[]) args;
			}
		}

		ReportLauncher.main(appArgs);

		return null;
	}

	public void stop() {
	}

}
