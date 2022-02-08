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

package org.eclipse.birt.report.model.tests.box;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.scripts.ClassInfo;
import org.eclipse.birt.report.model.api.scripts.IScriptableObjectClassInfo;

public class ScriptableFactory implements IScriptableObjectClassInfo {

	public IClassInfo getScriptableClass(String className) {
		if ("org.eclipse.birt.report.model.tests.box.Box".equalsIgnoreCase(className))
			return new ClassInfo(Box.class);

		return null;
	}
}
