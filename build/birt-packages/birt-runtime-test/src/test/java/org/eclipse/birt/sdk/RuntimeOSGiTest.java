/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/
package org.eclipse.birt.sdk;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings("javadoc")
// TODO This works locally for me on Windows but fails on the Linux build machines.
// One must first do a full Maven build for these artifacts to be available in the target folder.
public abstract class RuntimeOSGiTest extends BaseTestTemplate {
	public int run(String[] args) throws Exception {
		System.setProperty("BIRT_HOME",
				new File("./target/birt-runtime-osgi/ReportEngine/platform/").getAbsolutePath());

		ClassLoader loader = createClassLoader("./target/birt-runtime-osgi/ReportEngine/lib");

		// Start the Platform to start the Equinox framework.
		Class<?> platformClass = loader.loadClass("org.eclipse.birt.core.framework.Platform"); //$NON-NLS-1$
		platformClass.getMethod("startup").invoke(null);

		// Get the launcher from the started Platform.
		Field launcherField = platformClass.getDeclaredField("launcher");
		launcherField.setAccessible(true);
		Object launcher = launcherField.get(null);

		// Get the org.eclipse.birt.report.engine bundle from the launcher.
		Method getBundleMethod = launcher.getClass().getDeclaredMethod("getBundle", String.class);
		getBundleMethod.setAccessible(true);
		Object birtReportEngineBundle = getBundleMethod.invoke(launcher, "org.eclipse.birt.report.engine");

		// Load the org.eclipse.birt.report.engine.api.ReportRunner class from the
		// bundle to ensure we have loaded the instance class from the actual OSGi
		// runtime..c
		Method loadClassMethod = birtReportEngineBundle.getClass().getMethod("loadClass", String.class);
		Class<?> mainClass = (Class<?>) loadClassMethod.invoke(birtReportEngineBundle,
				"org.eclipse.birt.report.engine.api.ReportRunner");
		return run(mainClass, args);
	}
}
