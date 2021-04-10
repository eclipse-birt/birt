/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.javascript.plugin;

import org.eclipse.birt.core.plugin.BIRTPlugin;
import org.eclipse.birt.report.engine.javascript.JavascriptEngineFactory;
import org.osgi.framework.BundleContext;

public class JavaScriptPlugin extends BIRTPlugin {

	public void start(BundleContext context) throws Exception {
		super.start(context);
		JavascriptEngineFactory.initMyFactory();
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		JavascriptEngineFactory.destroyMyFactory();
	}

}