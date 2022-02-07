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
package org.eclipse.birt.data.oda.pojo.querymodel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.impl.internal.ClassMethodFieldBuffer;
import org.eclipse.birt.data.oda.pojo.impl.internal.MethodIdentifier;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The source is a method of the class. A counterpart of
 * <code>ElEMENT_METHOD</code> element in POJO query text.
 */

public class MethodSource implements IMappingSource {
	private String name; // the method name

	private IMethodParameter[] parameters;

	private MethodIdentifier mi;

	private Object[] parameterValues;

	/**
	 * @param name: the method name
	 */
	public MethodSource(String name, IMethodParameter[] parameters) {
		assert name != null;
		this.name = name;
		this.parameters = parameters == null ? new IMethodParameter[0] : parameters;
	}

	public String getName() {
		return name;
	}

	public IMethodParameter[] getParameters() {
		return parameters;
	}

	public void prepareParameterValues(Map<String, Object> paramValues, ClassLoader pojoClassLoader)
			throws OdaException {
		parameterValues = new Object[parameters.length];
		int i = 0;
		for (IMethodParameter mp : parameters) {
			mp.prepareValue(paramValues, pojoClassLoader);
			parameterValues[i++] = mp.getTargetValue();
		}
	}

	public Object fetchValue(Object from, ClassLoader pojoClassLoader, ClassMethodFieldBuffer cmfbInstance)
			throws OdaException {
		if (from == null || cmfbInstance == null) {
			return null;
		}
		if (mi == null) {
			mi = MethodIdentifier.newInstance(this, pojoClassLoader);
		}
		Method m = cmfbInstance.getMethod(from.getClass(), mi);
		try {
			return m.getReturnType().equals(Void.TYPE) ? null : m.invoke(from, parameterValues);
		} catch (IllegalArgumentException e) {
			throw new OdaException(Messages.getString("IllegalArgument.errorMessage") + mi.getName());
		} catch (IllegalAccessException e) {
			throw new OdaException(e);
		} catch (InvocationTargetException e) {
			throw new OdaException(Messages.getString("IllegalArgument.errorMessage") + mi.getName() + "\n"
					+ e.getTargetException().getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.oda.pojo.querymodel.IMappingSource#createElement(org.
	 * w3c.dom.Document)
	 */
	public Element createElement(Document doc) {
		Element ele = doc.createElement(Constants.ELEMENT_METHOD);
		ele.setAttribute(Constants.ATTR_METHOD_NAME, getName());
		for (IMethodParameter p : getParameters()) {
			ele.appendChild(p.createElement(doc));
		}
		return ele;
	}

	public void updateMethodParameter(IMethodParameter old, IMethodParameter newParam) {
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].equals(old)) {
				parameters[i] = newParam;
			}
		}
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
		result = prime * result + Arrays.hashCode(parameters);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodSource other = (MethodSource) obj;
		if (!name.equals(other.name))
			return false;
		if (!Arrays.equals(parameters, other.parameters))
			return false;
		return true;
	}

}
