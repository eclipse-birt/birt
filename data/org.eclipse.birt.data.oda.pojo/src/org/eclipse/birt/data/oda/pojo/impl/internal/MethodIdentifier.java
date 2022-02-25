
/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.impl.internal;

import java.util.Arrays;

import org.eclipse.birt.data.oda.pojo.querymodel.IMethodParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.MethodSource;
import org.eclipse.birt.data.oda.pojo.util.MethodParameterType;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 *
 */

public class MethodIdentifier {
	private String name; // method name
	@SuppressWarnings("unchecked")
	private Class[] params; // method parameters

	@SuppressWarnings("unchecked")
	public MethodIdentifier(String name, Class[] params) {
		assert name != null && params != null;
		this.name = name;
		this.params = params;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the params
	 */
	@SuppressWarnings("unchecked")
	public Class[] getParams() {
		return params;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + name.hashCode();
		result = prime * result + Arrays.hashCode(params);
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		MethodIdentifier other = (MethodIdentifier) obj;
		if (!name.equals(other.name)) {
			return false;
		}
		if (!Arrays.equals(params, other.params)) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static MethodIdentifier newInstance(MethodSource ms, ClassLoader pojoClassLoader) throws OdaException {
		String methodName = ms.getName();
		Class[] params = new Class[ms.getParameters().length];
		int i = 0;
		for (IMethodParameter mp : ms.getParameters()) {
			MethodParameterType mpt = MethodParameterType.getInstance(mp.getDataType(), pojoClassLoader);
			params[i++] = mpt.getJavaType();
		}
		return new MethodIdentifier(methodName, params);
	}

}
