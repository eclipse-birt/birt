/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.scripts.ClassInfo;
import org.eclipse.birt.report.model.api.scripts.IScriptableObjectClassInfo;

public class ChartScriptableClassInfo implements IScriptableObjectClassInfo {

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
			return null;
		} catch (RuntimeException e) {
			return null;
		}
	}

}
