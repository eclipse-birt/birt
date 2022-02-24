/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.core.runtime.Platform;

/**
 * Static accessor for engine instance.
 * <p>
 */
public class EngineAccessor {
	/**
	 * Static engine instance.
	 */
	public static ReportEngine engine = null;

	/**
	 * Get engine instance.
	 *
	 * @return engine instance
	 */
	synchronized public static ReportEngine getInstance() {
		if (engine == null) {
			System.setProperty("RUN_UNDER_ECLIPSE", "false"); //$NON-NLS-1$ //$NON-NLS-2$
			EngineConfig config = new EngineConfig();
			String t = Platform.getLocation().toFile().getAbsolutePath();
			config.setEngineHome(t); // $NON-NLS-1$
			engine = new ReportEngine(config);
		}

		return engine;
	}
}
