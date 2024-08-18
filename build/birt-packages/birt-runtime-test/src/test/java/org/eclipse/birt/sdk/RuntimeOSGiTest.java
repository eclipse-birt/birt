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
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings("javadoc")
public class RuntimeOSGiTest extends BaseTestTemplate {

	@Override
	protected Class<?> getClass(String bundle, String className) throws Exception {
		System.setProperty("BIRT_HOME",
				new File("./target/birt-runtime-osgi/ReportEngine/platform/").getAbsolutePath());

		ClassLoader loader = createClassLoader("./target/birt-runtime-osgi/ReportEngine/lib");

		// Start the Platform to start the Equinox framework.
		Class<?> platformClass = loader.loadClass("org.eclipse.birt.core.framework.Platform"); //$NON-NLS-1$
		MethodHandle startup = MethodHandles.publicLookup().findStatic(platformClass, "startup",
				MethodType.methodType(void.class));
		try {
			startup.invoke();
		} catch (Throwable e) {
			throw new Exception(e);
		}

		// Get the launcher from the started Platform.
		Field launcherField = platformClass.getDeclaredField("launcher");
		launcherField.setAccessible(true);
		Object launcher = launcherField.get(null);

		// Get the org.eclipse.birt.report.engine bundle from the launcher.
		Method getBundleMethod = launcher.getClass().getDeclaredMethod("getBundle", String.class);
		getBundleMethod.setAccessible(true);
		Object birtReportEngineBundle = getBundleMethod.invoke(launcher, bundle);

		// Load the org.eclipse.birt.report.engine.api.ReportRunner class from the
		// bundle to ensure we have loaded the instance class from the actual OSGi
		// runtime..c
		Method loadClassMethod = birtReportEngineBundle.getClass().getMethod("loadClass", String.class);
		Class<?> mainClass = (Class<?>) loadClassMethod.invoke(birtReportEngineBundle, className);
		return mainClass;
	}

}
