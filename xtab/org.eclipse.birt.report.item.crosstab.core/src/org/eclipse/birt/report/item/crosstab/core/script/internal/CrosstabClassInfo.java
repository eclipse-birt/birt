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

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import java.lang.reflect.Method;

import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.scripts.ClassInfo;

/**
 * CrosstabClassInfo
 */
public class CrosstabClassInfo extends ClassInfo {

	public CrosstabClassInfo(Class<?> clazz) {
		super(clazz);
	}

	protected IMethodInfo createMethodInfo(Method classMethod) {
		CrosstabMethodInfo info = new CrosstabMethodInfo(classMethod);
		if (!info.isDeprecated()) {
			return info;
		}
		return null;
	}
}
