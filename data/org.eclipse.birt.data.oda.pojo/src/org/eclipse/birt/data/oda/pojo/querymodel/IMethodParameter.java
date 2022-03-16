
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

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 */

public interface IMethodParameter {
	String getDataType();

	void setDataType(String type);

	Element createElement(Document doc);

	/**
	 *
	 * @param paramValues all query parameter values set from user
	 * @throws OdaException
	 */
	void prepareValue(Map<String, Object> paramValues, ClassLoader pojoClassLoader) throws OdaException;

	Object getTargetValue();

	void setStringValue(String value);

	String getStringValue();
}
