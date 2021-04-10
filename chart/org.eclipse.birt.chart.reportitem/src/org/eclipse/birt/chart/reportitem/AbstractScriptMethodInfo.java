/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.birt.report.model.api.scripts.MethodInfo;

/**
 * The abstract class defines common methods to provide script method info.
 * 
 * @since 2.5
 */

abstract public class AbstractScriptMethodInfo extends MethodInfo {

	/**
	 * Constructor.
	 * 
	 * @param method
	 */
	public AbstractScriptMethodInfo(Method method) {
		super(method);
	}

	/**
	 * Returns map of javadoc of all script methods, the key if method name, value
	 * is javadoc.
	 * 
	 * @return
	 */
	abstract protected Map<String, String> getMethodsJavaDoc();

	/**
	 * Check if method is deprecated.
	 * 
	 * @return
	 */
	public boolean isDeprecated() {
		String javaDoc = getJavaDoc();
		if (javaDoc == null)
			return true;
		return getJavaDoc().indexOf("@deprecated") != -1; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMethodInfo#getJavaDoc()
	 */
	public String getJavaDoc() {
		return getMethodsJavaDoc().get(getMethod().getName());
	}
}
