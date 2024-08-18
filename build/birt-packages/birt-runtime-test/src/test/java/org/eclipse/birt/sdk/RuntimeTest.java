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

@SuppressWarnings("javadoc")
public class RuntimeTest extends BaseTestTemplate {

	@Override
	protected Class<?> getClass(String bundle, String className) throws Exception {
		System.clearProperty("BIRT_HOME");
		ClassLoader loader = createClassLoader("./target/birt-runtime/ReportEngine/lib"); //$NON-NLS-1$
		Class<?> mainClass = loader.loadClass(className); // $NON-NLS-1$
		return mainClass;
	}
}
