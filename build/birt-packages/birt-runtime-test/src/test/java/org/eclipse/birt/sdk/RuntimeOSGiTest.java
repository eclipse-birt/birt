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
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 *
 */

public class RuntimeOSGiTest extends BaseTestTemplate {
	@Override
	public int run(String[] args) throws Exception {
		if (mainClass == null) {
			System.setProperty("BIRT_HOME", new File("./target/birt-runtime-osgi/ReportEngine").getAbsolutePath());
			loader = createClassLoader("./target/birt-runtime-osgi/ReportEngine/lib"); //$NON-NLS-1$
			mainClass = loader.loadClass("org.eclipse.birt.report.engine.api.ReportRunner"); //$NON-NLS-1$
		}
		Constructor constructor = mainClass.getConstructor(String[].class);
		Object runner = constructor.newInstance(new Object[] { args });
		Method execute = mainClass.getMethod("execute", null);
		Object result = execute.invoke(runner, null);
		return ((Integer) result).intValue();
	}
}
