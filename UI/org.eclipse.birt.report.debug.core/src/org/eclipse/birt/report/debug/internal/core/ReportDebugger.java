/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
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

	@Override
	public Object start(IApplicationContext context) throws Exception {
		String[] appArgs = {};

		if (context.getArguments() != null) {
			Object args = context.getArguments().get(IApplicationContext.APPLICATION_ARGS);

			if (args instanceof String[]) {
				appArgs = (String[]) args;
			}
		}

		ReportLauncher.main(appArgs);

		return null;
	}

	@Override
	public void stop() {
	}

}
