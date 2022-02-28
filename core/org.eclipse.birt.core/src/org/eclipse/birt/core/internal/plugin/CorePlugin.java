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

package org.eclipse.birt.core.internal.plugin;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.eclipse.EclipsePlatform;
import org.eclipse.birt.core.internal.function.impl.FunctionProviderImpl;
import org.eclipse.birt.core.plugin.BIRTPlugin;
import org.eclipse.birt.core.script.ScriptEngineFactoryManager;
import org.eclipse.birt.core.script.functionservice.impl.FunctionProvider;
import org.osgi.framework.BundleContext;

public class CorePlugin extends BIRTPlugin {

	/**
	 * This method is called upon plug-in activation.
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		ClassLoader contextClassLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<Object>() {

			@Override
			public Object run() {
				return Thread.currentThread().getContextClassLoader();
			}
		});

		Platform.setPlatform(new EclipsePlatform(context, contextClassLoader));
		FunctionProvider.setFunctionProvider(new FunctionProviderImpl());
		ScriptEngineFactoryManager.setInstance(new ScriptEngineFactoryManagerImpl());
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		Platform.setPlatform(null);
		FunctionProvider.setFunctionProvider(null);
		ScriptEngineFactoryManager.setInstance(null);
	}
}
