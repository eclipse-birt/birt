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

import java.util.Map;

import org.eclipse.birt.data.oda.pojo.impl.internal.ClassMethodFieldBuffer;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * An identity for the source of a column mapping
 */
public interface IMappingSource {
	/**
	 * @return the source name
	 */
	String getName();

	/**
	 * 
	 * @param from
	 * @return the mapped value from the <code>from</code> object
	 * @throws OdaException
	 */
	Object fetchValue(Object from, ClassLoader pojoClassLoader, ClassMethodFieldBuffer cmfbInstance)
			throws OdaException;

	Element createElement(Document doc);

	void prepareParameterValues(Map<String, Object> paramValues, ClassLoader pojoClassLoader) throws OdaException;
}
