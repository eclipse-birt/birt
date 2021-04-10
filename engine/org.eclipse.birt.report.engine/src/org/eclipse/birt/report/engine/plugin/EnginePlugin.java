/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.plugin;

import org.eclipse.birt.core.plugin.BIRTPlugin;
import org.eclipse.birt.report.engine.api.script.element.ScriptAPIFactory;
import org.eclipse.birt.report.engine.script.internal.element.ScriptAPIBaseFactory;
import org.osgi.framework.BundleContext;

public class EnginePlugin extends BIRTPlugin {

	public void start(BundleContext context) throws Exception {
		super.start(context);
		ScriptAPIFactory.initeTheFactory(new ScriptAPIBaseFactory());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */

	public void stop(BundleContext context) throws Exception {
		ScriptAPIFactory.releaseInstance();
		super.stop(context);
	}
}
