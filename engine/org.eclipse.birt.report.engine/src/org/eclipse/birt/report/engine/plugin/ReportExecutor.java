/*******************************************************************************
 * Copyright (c) 2004, 2021 Actuate Corporation and others.
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

package org.eclipse.birt.report.engine.plugin;

import org.eclipse.birt.report.engine.api.ReportRunner;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class ReportExecutor implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {

		String[] args = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);

		// as we know that the last argument must be a report design
		// or report document, so remove any extra arguments which
		// added by the eclipse platform (which is started with '-').
		if (args instanceof String[]) {
			String[] platformArgs = (String[]) args;
			int appArgLength = platformArgs.length;
			for (; appArgLength > 0; appArgLength--) {
				String arg = platformArgs[appArgLength - 1];
				if (arg.charAt(0) != '-') {
					break;
				}
			}
			String[] appArgs = new String[appArgLength];
			System.arraycopy(args, 0, appArgs, 0, appArgLength);
			new ReportRunner(appArgs).execute();
		}
		return null;
	}

	@Override
	public void stop() {
	}
}
