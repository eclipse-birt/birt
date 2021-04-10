/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
