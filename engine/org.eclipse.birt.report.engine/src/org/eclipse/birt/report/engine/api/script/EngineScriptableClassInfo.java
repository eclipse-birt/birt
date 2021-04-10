/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.api.script;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.scripts.ClassInfo;
import org.eclipse.birt.report.model.api.scripts.IScriptableObjectClassInfo;
import org.eclipse.birt.report.model.api.scripts.ScriptableClassInfo;

public class EngineScriptableClassInfo extends ScriptableClassInfo implements IScriptableObjectClassInfo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.scripts.IScriptableObjectClassInfo#
	 * getScriptableClass(java.lang.String)
	 */
	public IClassInfo getScriptableClass(String className) {
		try {
			Class clazz = Class.forName(className);
			ClassInfo info = new ClassInfo(clazz);
			return info;
		} catch (ClassNotFoundException e) {
			return getClass(className);
		} catch (RuntimeException e) {
			return null;
		}
	}

}
