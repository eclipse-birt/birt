/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.javascript.plugin;

import org.eclipse.birt.core.plugin.BIRTPlugin;
import org.eclipse.birt.report.engine.javascript.JavascriptEngineFactory;
import org.osgi.framework.BundleContext;

public class JavaScriptPlugin extends BIRTPlugin {

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		JavascriptEngineFactory.initMyFactory();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		JavascriptEngineFactory.destroyMyFactory();
	}

}
