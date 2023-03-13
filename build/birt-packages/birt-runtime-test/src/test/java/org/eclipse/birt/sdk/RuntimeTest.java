/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.sdk;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RuntimeTest extends BaseTestTemplate {

	@Override
	public int run(String[] args) throws Exception {
		if (mainClass == null) {
			System.clearProperty("BIRT_HOME");
			loader = createClassLoader("./target/birt-runtime/ReportEngine/lib"); //$NON-NLS-1$
			mainClass = loader.loadClass("org.eclipse.birt.report.engine.api.ReportRunner"); //$NON-NLS-1$
		}
		Constructor constructor = mainClass.getConstructor(String[].class);
		Object runner = constructor.newInstance(new Object[] { args });
		Method execute = mainClass.getMethod("execute", null);
		Object result = execute.invoke(runner, null);
		return ((Integer) result).intValue();
	}

}
