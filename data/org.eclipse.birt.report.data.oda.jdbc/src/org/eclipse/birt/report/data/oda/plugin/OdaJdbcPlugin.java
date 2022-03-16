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
package org.eclipse.birt.report.data.oda.plugin;

import org.eclipse.birt.report.data.oda.jdbc.JDBCDriverManager;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * Obtain support from Plugin
 */
public class OdaJdbcPlugin extends Plugin {
	@Override
	public void stop(BundleContext context) {
		JDBCDriverManager.getInstance().close();
	}
}
