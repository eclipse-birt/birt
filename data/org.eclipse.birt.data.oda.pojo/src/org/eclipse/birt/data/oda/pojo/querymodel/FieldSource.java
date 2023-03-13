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

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.impl.internal.ClassMethodFieldBuffer;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The source is a filed of the class A counterpart of
 * <code>ElEMENT_FIELD</code> element in POJO query text.
 */
public class FieldSource implements IMappingSource {
	private String name; // the filed name

	/**
	 * @param name: the field name
	 * @throws NullPointerException if <code>name</code> is null
	 */
	public FieldSource(String name) {
		if (name == null) {
			throw new NullPointerException("name is null"); //$NON-NLS-1$
		}
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object fetchValue(Object from, ClassLoader pojoClassLoader, ClassMethodFieldBuffer cmfbInstance)
			throws OdaException {
		if (from == null || cmfbInstance == null) {
			return null;
		}
		Field f = cmfbInstance.getField(from.getClass(), getName());
		try {
			return f.get(from);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new OdaException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.oda.pojo.querymodel.IMappingSource#createElement(org.
	 * w3c.dom.Document)
	 */
	@Override
	public Element createElement(Document doc) {
		Element ele = doc.createElement(Constants.ELEMENT_FIELD);
		ele.setAttribute(Constants.ATTR_FIELD_NAME, getName());
		return ele;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name);
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
		FieldSource other = (FieldSource) obj;
		if (!Objects.equals(name, other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public void prepareParameterValues(Map<String, Object> paramValues, ClassLoader pojoClassLoader)
			throws OdaException {
		// no parameter at all for FildSource
	}

}
