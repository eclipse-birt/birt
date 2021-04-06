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

package org.eclipse.birt.chart.reportitem;

import java.lang.reflect.Method;

import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.scripts.ClassInfo;

public class ScriptClassInfo extends ClassInfo {

	public ScriptClassInfo(Class clazz) {
		super(clazz);
	}

	protected IMethodInfo createMethodInfo(Method classMethod) {
		ScriptMethodInfo info = new ScriptMethodInfo(classMethod);
		if (!info.isDeprecated())
			return info;
		else
			return null;
	}
}
