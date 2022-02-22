/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.lang.reflect.Method;

import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.scripts.ClassInfo;

public class ScriptClassInfo extends ClassInfo {

	public ScriptClassInfo(Class clazz) {
		super(clazz);
	}

	@Override
	protected IMethodInfo createMethodInfo(Method classMethod) {
		ScriptMethodInfo info = new ScriptMethodInfo(classMethod);
		if (!info.isDeprecated()) {
			return info;
		} else {
			return null;
		}
	}
}
